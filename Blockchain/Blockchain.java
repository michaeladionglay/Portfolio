//package Blockchain;
/*
_____________________________________________________________________________________________
Name: Michaela Dionglay
Date: 05/26/2020
Java version: 1.8.0_221-b11

Command-line compilation instructions twice: javac -cp "gson-2.8.2.jar" *.java 

Instructions to run this program: 

=Must indicate what process you're running in a console=

This code was tested using Windows 10

1. Process 0: 
>java -cp ".;gson-2.8.2.jar" Blockchain 0

2. Process 1: 
>java -cp ".;gson-2.8.2.jar" Blockchain 1

3. Process 2: 
>java -cp ".;gson-2.8.2.jar" Blockchain 2

This code was tested using Ubuntu

1. Process 0: 
>java -cp ".:gson-2.8.2.jar" Blockchain 0

2. Process 1: 
>java -cp ".:gson-2.8.2.jar" Blockchain 1

3. Process 2: 
>java -cp ".:gson-2.8.2.jar" Blockchain 2

Summary:
Each process will read their own designated file (BlockInput0.txt, BlockInput1.txt, BlockInput2.txt)
Make each record into a BlockRecord and send these unverified blocks to all the processes, including 
itself. Each block will compete in who can find the puzzle first. The process who finds the puzzle 
first gets to add it to a copy of the global ledger, send the updated copy to the blockchain Server,
and update the global ledger with the updated copy. 
Must wait for menu to appear in order to put console commands

List of files needed to run program:
	a. Blockchain.java
	b. BlockInput0.txt
	c. BlockInput1.txt
	d. BlockInput2.txt
	e. gson-2.8.2.jar
	f. BlockchainLedger.json (generated once code runs)
	
Resources:

From utility "Blockchain utilities sample program Version J"
https://mkyong.com/java/how-to-parse-json-with-gson/
http://www.java2s.com/Code/Java/Security/SignatureSignAndVerify.htm
https://www.mkyong.com/java/java-digital-signatures-example/ (not so clear)
https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
https://www.programcreek.com/java-api-examples/index.php?api=java.security.SecureRandom
https://www.mkyong.com/java/java-sha-hashing-example/
https://stackoverflow.com/questions/19818550/java-retrieve-the-actual-value-of-the-public-key-from-the-keypair-object
https://www.java67.com/2014/10/how-to-pad-numbers-with-leading-zeroes-in-Java-example.html

From utility "Blockchain input utilty program"
http://www.fredosaurus.com/notes-java/data/strings/96string_examples/example_stringToArray.html
Good explanation of linked lists:
https://beginnersbook.com/2013/12/linkedlist-in-java-with-example/
Priority queue:
https://www.javacodegeeks.com/2013/07/java-priority-queue-priorityqueue-example.html

From utility "Sample Work Program"
https://www.quickprogrammingtips.com/java/how-to-generate-sha256-hash-in-java.html  @author JJ
https://dzone.com/articles/generate-random-alpha-numeric  by Kunal Bhatia  ·  Aug. 09, 12 · Java Zone

From utility " Process Coordination"
_____________________________________________________________________________________________
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyRep.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;




public class Blockchain {
	static boolean confirmPID0 = false;
	static boolean confirmPID1 = false;
	
	static int PID = 0;//default PID to 0
	static boolean BlockchainActivated = false;
	static BlockingQueue<BlockRecord> queue = new PriorityBlockingQueue<BlockRecord>(4, BlockRecord.BlockTSComparator);//specified initial capacity of 4; with comparator that prioritizes the earliest timestamp
	static LinkedList<BlockRecord> bc_ledger = new LinkedList<BlockRecord>();
	private static PrivateKey SK;
	private static PublicKey PK;
	public static boolean MenuStatus = true;
	
	public static void main(String[] args) throws IOException {
		//if a number argument is provided, that will be the number assigned for the processes
		//ex. java Blockchain 1....then this process will be PROCESS 1. if not number argument is provided, then it is default 0.
		if(args.length == 1) {
			PID = Integer.parseInt(args[0]);
		}
		
		System.out.println("*********************************************************************\n");
		System.out.println("Michaela Dionglay's Blockchain (control-c to quit)\n");
	    System.out.println("Using PROCESSID " + PID + "\n");
	    System.out.println("*********************************************************************\n");
		
		//set's up the different port numbers based off of the PID the user provided
		//sets up a PublicKeyServer, UnverifiedBlockServer, and BlockchainServer
		new Ports().setPorts();
		
		//starts the run() method for the publickeyServer. which will open the socket, ready to receive
		PublicKeyInstance pk = new PublicKeyInstance(Ports.KeyServerPort);
		new Thread(pk).start();
		
		//starts the run() method for the UnverifiedBlockServer. which will open the socket, ready to receive
		UnverifiedBlockInstance ubi = new UnverifiedBlockInstance(queue);
		new Thread(ubi).start();
		
		//starts the run() method for the BlockchainServer. which will open the socket, ready to receive
		BlockchainInstance bci = new BlockchainInstance(Ports.BlockchainServerPort);
		new Thread(bci).start();
		
		//starts the run() method for the ActivatorServer. which will open the socket, ready to receive
		ActivatorInstance as = new ActivatorInstance(Ports.ActivateProcessesServerPort);
		new Thread(as).start();
	    
		try{Thread.sleep(1000);}catch(Exception e){}
		
		//ONLY process 2 boolean changes to true, process 0 and 1's blockchainActivated is still false
		if(	PID == 2 ) {
			
			BlockchainActivated = true;
				
		}
		
		//Process 0 and 1 will wait here until Process 2 starts
		//BlockchainActivited will become true for process 0 and 1 once Process 2 sends the Public Keys
		
		while(BlockchainActivated == false) {
			try{Thread.sleep(1000);}catch(Exception e){}
			System.out.println("Waiting for Process 2 to start...");
		}
	
		try{Thread.sleep(2000);}catch(Exception e){}
		new Blockchain().MultiSend();//method where each process will send public keys to each process's PublickeyServer
		//read files, create blocks, and send these unverified blocks to each process's UnverifiedBlockServer(which will put in a queue)
	    try{Thread.sleep(2000);}catch(Exception e){} 
	    
	    //takes that queue and verifies each block by doing WORK
	    new Thread(new UnverifiedBlockConsumer(queue, SK)).start(); // Start consuming the queued-up unverified blocks
	
	    //added sleep here in order for the blockchain to be made before the menu pops up
	    try{Thread.sleep(30000);}catch(Exception e){}
	    
	    //displays menu to choose C, R, V, and L
	    while(MenuStatus) {
	    	DisplayMenu();
	    }
	}
	
	//sends to ActivatorServer to every processes to start them when they were waiting for Process 2 to start
	public static void ActivateProcessesNOW(int numberOfProcesses) {
		Socket sock = null;
		try {
		for(int i=0; i< (numberOfProcesses); i++){
			PrintStream sendToBlockchainServer = null;
			sock = new Socket("localhost", Ports.ActivateProcessesServerPortBase + (i * 1000));
			sendToBlockchainServer = new PrintStream(sock.getOutputStream());
			int num = 1;
			sendToBlockchainServer.print(num);//just sends an int, nothing fancy
			sendToBlockchainServer.flush();
			sock.close();
      		}
		}
		catch (Exception x) {
			x.printStackTrace ();
			}
	}
	
	public static void DisplayMenu() {
		System.out.println("*********************************************************************\n");
	    System.out.println("Options: ");
	    System.out.println("Enter C to COUNT how many blocks each processes verified");
	    System.out.println("Enter R followed by a text file name to READ a file of records to create new data. Ex. \"R file.txt\" ");
	    System.out.println("Enter V to VERIFY the entire blockchain and report errors");
	    System.out.println("Enter L to LOOK at information for each block in the blockchain");
	    System.out.println();
	    System.out.println("*********************************************************************\n");
	    

	    Scanner in = new Scanner(System.in);//allows user to write on console
	    String answer = in.nextLine();//reads what the user enters in the console
	    
	    ActionToUserResponse(answer);//method to handle the user response 
	    
	}
	
	public static void ActionToUserResponse(String ans) {
	    int numProcesses = 3;
	    
	    //checks if string answer is equal to uppercase OR lowercase C
	    if(ans.equalsIgnoreCase("C") || ans.equalsIgnoreCase("c")) {
	    	System.out.println("Displaying credit tally for each process: ");
	    	DisplayTallyCredit();//displays how many blocks were verified by each process
	    }
	    else if(ans.contains("R") || ans.contains("r")) {//checks if string answer is equal to uppercase OR lowercase R
	    	String file = ans.substring(2);//were will enter R MyBlockData.txt; this substring will get the string starting from char [2] and on; M to the end
	    	
	    	System.out.println("Reading file " + file);
	    	//current process reads file and stores the created BlockRecord in an arraylist
	    	ArrayList<BlockRecord> UnverifiedBlockRecordList = Tools.readFile(file, PID, SK);
	    	//try{Thread.sleep(2000);}catch(Exception e){} 
	    	
	    	System.out.println("Sending data from " + file + " to be verified");
	    	//sends the arraylist to the UnverifiedblockServer
	    	SendUnverifiedBlocksToProcesses(UnverifiedBlockRecordList, numProcesses);
	    }
	    else if(ans.equalsIgnoreCase("V") || ans.equalsIgnoreCase("v")) {//checks if string answer is equal to uppercase OR lowercase V
	    	//verfies that each block is valid and there was no counterfeit
	    	VerifyBlockchain();
	    	System.out.println("Verified blockchain!");
	    }
	    else if(ans.equalsIgnoreCase("L") || ans.equalsIgnoreCase("l")) {//checks if string answer is equal to uppercase OR lowercase L
	    	System.out.println("Displaying blocks in ledger: ");
	    	DisplayRecordsInLedger();
	    }
	}
	
	//checks that each block was from the the circle of processes and that each blocks hashed data+prev+seed fits the requirements
	public static void VerifyBlockchain() {
		
	    for(BlockRecord br : Blockchain.bc_ledger) {
	    	if(br.getBlockNum() == 0) {//skips the dummy block
				continue;
			}
	    	if(!checkRequirement(br)) {
	    		System.out.println("Block " + br.getBlockNum() + " does NOT meet work threshold.");
	    	}
	    	if(!checkIfFromProcesses(br)) {
	    		System.out.println("Block " + br.getBlockNum() + " signature not verified. Secret key potentially exposed.");
	    	}
	    }
	}
	
	//check hashed data+prev+seed fits requirement of being less than 200000
	public static boolean checkRequirement(BlockRecord br) {
		
		int requirement = 20000;
		int puzz = Integer.parseInt(br.getSHA256_DataString().substring(0,4),16);
    	if(puzz < requirement) {
    		return true;
    	}
    	else {
    		return false;
    	}
	}
	
	//check if each block is from the network of processes - this case, only process 0, 1, and 2
	public static boolean checkIfFromProcesses(BlockRecord br) {
		boolean verified = false;
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		try {
			//loops through the hashmap of public keys to see if the block has valid signature
			for(int i = 0; i < PublicKeyWorker.PublicKeyHashMapList.size(); i++) {
				PublicKey publicKey = PublicKeyWorker.PublicKeyHashMapList.get(i);//obtains the Publickey from process i
	  	  		byte[] signature = Base64.getDecoder().decode(br.getSignedRecordString());//decode the signed data to get the digital signature
	  	  		verified = Blockchain.verifySig(br.getSHA256_DataString().getBytes(), publicKey, signature);//checks if digital signature contains the paired private key
	  	  		results.add(verified);//stores the results in an arraylist (true or false)
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//if any of the public keys match the private key in the signature, that means this current block was verified
		if(results.contains(true)) {
			return true;
		}
		else{//if all were false, that means none of the public keys match that private key in the signature, meaning signature is invalid.
			return false;
		}
	}
	
	//prints out the BlockRecords inside the main global ledger that all the processes are updated on
	public static void DisplayRecordsInLedger() {
		LinkedList<BlockRecord> reverseList = new LinkedList<BlockRecord>();
		
		for(BlockRecord br : Blockchain.bc_ledger) {
			reverseList.push(br);
		}
		
	    for(BlockRecord rbr : reverseList) {
	    	System.out.println(rbr.getBlockNum() + ". " +
	    						rbr.getTimeStamp() + " " +
	    						rbr.getFname() + " " + 
	    						rbr.getLname() + " " +
	    						rbr.getDOB() + " " +
	    						rbr.getSSNum() + " " +
	    						rbr.getDiag() + " " +
	    						rbr.getTreat() + " " +
	    						rbr.getRx());
	    }
	}
	
	//display on the console tallies the verified blocks for each process
	public static void DisplayTallyCredit() {
		int tallyProcess_0_PuzzleSolving = 0;
		int tallyProcess_1_PuzzleSolving = 0;
		int tallyProcess_2_PuzzleSolving = 0;
		
		for(BlockRecord br : bc_ledger) {
			
			if(br.getVerificationProcessID() == null) {//ignores the dummy block
				continue;
			}
			else if(br.getVerificationProcessID().equalsIgnoreCase("0")) {
				tallyProcess_0_PuzzleSolving++;//adds 1 to tally
			}
			else if(br.getVerificationProcessID().equalsIgnoreCase("1")) {
				tallyProcess_1_PuzzleSolving++;
			}
			else if(br.getVerificationProcessID().equalsIgnoreCase("2")) {
				tallyProcess_2_PuzzleSolving++;
			}
		}
		
		System.out.println("Process 0: " + tallyProcess_0_PuzzleSolving);
		System.out.println("Process 1: " + tallyProcess_1_PuzzleSolving);
		System.out.println("Process 2: " + tallyProcess_2_PuzzleSolving);
	}
	
	public void MultiSend (){ 
	    int numProcesses = 3;
	    	try{
	    		
	    		//changes the other processes BlockchainActivated to true
	    		ActivateProcessesNOW(3);
	    		
	    		//generates keypair(public key and private key) and stores in static variable in blockchain class
	    		KeyPair keyPair = generateKeyPair(999 + PID);//providing seed, varying depending on PID to add uniqueness
	    		PK = keyPair.getPublic();
	    		SK = keyPair.getPrivate();
				
	    		System.out.println("Sending Public Keys to processes...");
	    		SendPublicKeysToProcesses(PK, numProcesses);
	    		
	    		Thread.sleep(1000);
	    		
	    		//only PROCESS 2 creates dummy block, adds it to a ledger, and sends it the BlockchainServer for all processes, including self
	    		if(PID == 2) {
	    			System.out.println("Process " + PID + " creating DUMMY Record...");
	    			LinkedList<BlockRecord> Starter_ledger = CreateDummyRecordToPutInLedger();
	    			
	    			BlockchainRecord dummyBCR = new BlockchainRecord(Starter_ledger, 10);//store info in a blockchainRecord class
	    			System.out.println("Sending DUMMY Record to processes...");
	    			SendDummyBlockchainToStartBlockchain(dummyBCR, numProcesses);
				
	    		}
	    		Thread.sleep(1000);
	    		
	    		//each process will read a file in directory based off its PID, ex. if PID = 0, then that process will read BlockInput0.txt
	    		ArrayList<BlockRecord> UnverifiedBlockRecordList = ReadFilesAndCreateListOfUnverifiedBlocks(PID, SK);
				
				Thread.sleep(1000);
				
				//Each process is going to send a marshaled BlockRecord to each Process's UnverifiedBlockInstance(including itself) 
				SendUnverifiedBlocksToProcesses(UnverifiedBlockRecordList, numProcesses);
	    	}
	    	catch (Exception x) {x.printStackTrace ();}
	    }
	
	//sends public keys to all process's PublicKeyServer, including self
	public static void SendPublicKeysToProcesses(PublicKey key, int numberOfProcesses) {
		Socket sock = null;
		try{
			for(int i=0; i< numberOfProcesses; i++){
				sock = new Socket("localhost", Ports.KeyServerPortBase + (i * 1000));
				ObjectOutputStream sendToPublicKeyServer = new ObjectOutputStream(sock.getOutputStream());
			    
				byte[] array = key.getEncoded();//convert key to byte array
				String PKbase64 = Base64.getEncoder().encodeToString(array);//convert PK byte array into a string of base64
				PKRecord PKR = new PKRecord(PID, PKbase64);//current PID and PK in base64 string in a PKRecord
				String Json_PublicKeyRecord = Tools.SerializeKeyRecord(PKR);//marshal PKRecord into a JSON string
				sendToPublicKeyServer.writeObject(Json_PublicKeyRecord);//output json string to process i
			
				sendToPublicKeyServer.flush();
				sock.close();
			}
		}
		catch (Exception x) {
				x.printStackTrace ();
				}
		}
	
	//send out ledger with dummy block to all process's BlockchainServer to start the blockchain
	public static void SendDummyBlockchainToStartBlockchain(BlockchainRecord ledger, int numberOfProcesses) {
		Socket sock = null;
		try {
		for(int i=0; i< (numberOfProcesses); i++){
			ObjectOutputStream sendToBlockchainServer = null;
			sock = new Socket("localhost", Ports.BlockchainServerPortBase + (i * 1000));
			sendToBlockchainServer = new ObjectOutputStream(sock.getOutputStream());
			String Json_Dum = Tools.SerializeBlockchainRecord(ledger);	//convert ledger to JSON string																			
			sendToBlockchainServer.writeObject(Json_Dum);//send out json string to process i
			sendToBlockchainServer.flush();
			sock.close();
      		}
		}
		catch (Exception x) {
			x.printStackTrace ();
			}
	}
	
	//creates dummy record, stores it in temp ledger, returns temp ledger
	public static LinkedList<BlockRecord> CreateDummyRecordToPutInLedger() {
		try {
			Thread.sleep(1000); 
			BlockRecord dum = new BlockRecord();
			dum.setBlockNum(0);
			UUID uuid = UUID.randomUUID();
			dum.setBlockID(uuid.toString());
			dum.setUUID(uuid);//creates unique id 
			dum.setProcessID(PID);
			dum.setFname("Michaela");
			dum.setLname("DummyBlock");
	
			String potentialSeed = UnverifiedBlockConsumer.randomAlphaNumeric(7);//method to choose 7 random alphanumeric, append to a string, return that string
			dum.setRandomSeed(potentialSeed);
			String DataJson = Tools.SerializeRecord(dum);//convert BlockRecord into a Json string
			String seed_data = DataJson + potentialSeed;//add json string and seed
			byte[] hashed_seed_data = Tools.hashData(seed_data);//hash the combined seed data into a byte array
			StringBuffer Hex_hashed_seed_data = Tools.byteToHex(hashed_seed_data);//convert to hex format
			dum.setSignedRecordString(Hex_hashed_seed_data.toString());//set with hex format

			LinkedList<BlockRecord> temp_bc_ledger = new LinkedList<BlockRecord>();
			temp_bc_ledger.add(dum);//add dummy blockRecord to temp ledger
			return temp_bc_ledger;
		}
		catch (Exception x) {
			x.printStackTrace ();
			return null;
			}
	}
	
	//Process reads a file based off of their PID, CREATES BLOCKRECORD object, stores in a arraylist and returns that arraylist
	public static ArrayList<BlockRecord> ReadFilesAndCreateListOfUnverifiedBlocks(int pid, PrivateKey pk) {
		try {
			String filename = "BlockInput" + Blockchain.PID + ".txt";//depending if 0 ,1, 2
			System.out.println("Current Process"+ PID + " is reading: " + filename);
			ArrayList<BlockRecord> UnverifiedBlockRecordList = Tools.readFile(filename, PID, SK);//method to read text file
			return UnverifiedBlockRecordList;
		}
		catch (Exception x) {
			x.printStackTrace ();
			return null;
			}
	}
	
	//sends each unverified BlockRecord at a time to the UnverifiedBlockServer
	public static void SendUnverifiedBlocksToProcesses(ArrayList<BlockRecord> lst, int numberOfProcesses) {
		Socket sock = null;
		try {
			for(int i=0; i < numberOfProcesses; i++){
    			for (BlockRecord record : lst) {
    				PrintStream toUnverifiedBlockServer = null;
	    			sock = new Socket("localhost", Ports.UnverifiedBlockServerPortBase + (i * 1000));
	    			toUnverifiedBlockServer = new PrintStream(sock.getOutputStream());
    				String Json_Record = Tools.SerializeRecord(record);//converts BlockRecord to a Json string 
    				
    				toUnverifiedBlockServer.println(Json_Record);//sends Json string
    				toUnverifiedBlockServer.flush();//actual time it is sent to stream, like a toilet flush
    				sock.close();
    			}
    		}
		}
			catch (Exception x) {
				x.printStackTrace ();

				}
		}

	//method to check if the blockID of a BlockRecord exists within the global Ledger
	public static boolean containsRecord(BlockRecord rec) {
		for(BlockRecord block : Blockchain.bc_ledger)
		{
			if (block.getBlockID().equals(rec.getBlockID()))
				return true;
		}
		return false;
	}
	
	//generates key based off of seed
	public static KeyPair generateKeyPair(long seed) throws Exception {
	    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");//creates this object with encryption algorithm "RSA"
	    SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");//"SHA1PRNG" is the hashing algorithm, "SUN" is the provider
	    rng.setSeed(seed);//uses the 8 bytes from the given seed but never replaces if method is called again
	    keyGenerator.initialize(1024, rng);//initializes KeyPairGenerator with size of key and given random seed from object secureRandom
	    
	    return (keyGenerator.generateKeyPair());
	  }
	
	//uses private key to sign data; returns a digital signature
	public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
	    Signature signer = Signature.getInstance("SHA1withRSA");//signature object with given encryption algorithm - "SHA1withRSA"
	    signer.initSign(key);//initializes signature with private key for signing
	    signer.update(data);//updates data as officially signed/verified
	    return (signer.sign());
	  }
	
	//verifies signature with potential matching public key
	public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
	    Signature signer = Signature.getInstance("SHA1withRSA");//signature object with given encryption algorithm - "SHA1withRSA"
	    signer.initVerify(key);//initializes verification of the signature
	    signer.update(data);//updates data as officially signed/verified
	    
	    return (signer.verify(sig));
	  }
}








//initialize ports based of of their PID; in order for all the ports for each server in each processes to be unique
class Ports{
	  public static int KeyServerPortBase = 6050;
	  public static int UnverifiedBlockServerPortBase = 6051;
	  public static int BlockchainServerPortBase = 6052;
	  public static int ActivateProcessesServerPortBase = 6053;

	  public static int KeyServerPort;
	  public static int UnverifiedBlockServerPort;
	  public static int BlockchainServerPort;
	  public static int ActivateProcessesServerPort;

	  public void setPorts(){
	    KeyServerPort = KeyServerPortBase + (Blockchain.PID * 1000);
	    UnverifiedBlockServerPort = UnverifiedBlockServerPortBase + (Blockchain.PID * 1000);
	    BlockchainServerPort = BlockchainServerPortBase + (Blockchain.PID * 1000);
	    ActivateProcessesServerPort = ActivateProcessesServerPortBase + (Blockchain.PID * 1000);
	  }
}








//Allows object to store Public key and PID
class PKRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ProcessID;
	public String PublicKey;
	public PublicKey PubKey;
	
	PKRecord(int PID, String PK){
		ProcessID = PID;
		PublicKey = PK;
	}
}





//server that takes care of the socket
class ActivatorInstance implements Runnable {
	  int port;
	  ActivatorInstance(int p){
		  port = p;
	  }
	  
	  public void run(){
	    int q_len = 6; 
	    Socket sock;

	    try{
	      ServerSocket servsock = new ServerSocket(port, q_len);//creates an instance of the ServerSocket class and setting port and q_len
	      while (true) {
	    	  sock = servsock.accept(); //once a connection is made, a socket is created
	    	  new ActivatorWorker(sock).start(); //executes this thread instance but at the same time, JVM executes UnverifiedBlockWorker.run()
	      }
	    }catch (IOException ioe) {System.out.println(ioe);}
	  }
}







class ActivatorWorker extends Thread { //class within a class
    Socket sock; 
    ActivatorWorker(Socket s) {sock = s;} 
    public void run(){
    	try{
    		BufferedReader in = null;
    		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    		int status = in.read();//receives int but does nothing to it really
    		
    		if(Blockchain.BlockchainActivated == false) { //changes BlockchainActivated variable to be true so that Process 0 and 1 can start
    			Blockchain.BlockchainActivated = true;
    		}
    		sock.close();
    	}
    	catch (Exception x){x.printStackTrace();}
    }
}







//UnverifiedBlockServer that listens for a connection and runs UnverifiedBlockWorker
class UnverifiedBlockInstance implements Runnable {
	  BlockingQueue<BlockRecord> queue;
	  public static int count = 0;
	  UnverifiedBlockInstance(BlockingQueue<BlockRecord> q){
	    this.queue = q; 
	  }
	  
	  public void run(){
	    int q_len = 6; 
	    Socket sock;

	    try{
	      System.out.println("Starting the Unverified Block Server input thread, listening at Port " +
				       Integer.toString(Ports.UnverifiedBlockServerPort));
	      ServerSocket servsock = new ServerSocket(Ports.UnverifiedBlockServerPort, q_len);//creates an instance of the ServerSocket class and setting port and q_len
	      while (true) {
	    	  sock = servsock.accept(); //once a connection is made, a socket is created
	    	  new UnverifiedBlockWorker(sock).start(); //executes this thread instance but at the same time, JVM executes UnverifiedBlockWorker.run()
	      }
	    }catch (IOException ioe) {System.out.println(ioe);}
	  }
	  
	  class UnverifiedBlockWorker extends Thread { //class within a class
		    Socket sock; 
		    UnverifiedBlockWorker (Socket s) {sock = s;} 
		    public void run(){
		      try{
		    	  BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		    	  @SuppressWarnings("unchecked")
		    	  String incomingData;
		    	  String Json_Records = "";
		    	  while((incomingData = in.readLine()) != null){//receives json string of BlockRecord that each process multisends using Blockchain.Multisend()
		    		  Json_Records += incomingData + "\n";//add \n if you just wanna see a pretty printed one
		    	  }
		    	  //System.out.println("Process" + Blockchain.PID + " received: ");
	    		  //System.out.println(Json_Records);
		    	  
		    	  //
		    	  BlockRecord block = Tools.DeserializeRecord(Json_Records);//converts Json string back to a BlockRecord
		    	  //System.out.println("The following will put in the queue: ");
		    	  //block.printString();
		    	  
		    	  queue.put(block);//stores block inside a priority queue - 3 processes, each process creates 4 BlockRecords. at the end, this queue should have a total of 12 unique blocks that have gone through the queue
		    	  sock.close(); 
		      } catch (Exception x){x.printStackTrace();}
		    }
		  }
	}











class UnverifiedBlockConsumer implements Runnable{
	  BlockingQueue<BlockRecord> tempQ = new PriorityBlockingQueue<BlockRecord>(4, BlockRecord.BlockTSComparator);//specified initial capacity of 4; with comparator that prioritizes the earliest timestamp
	  int PID;
	  BlockRecord retrievedRecord;
	  PrintStream toServer;
	  String previousHash;
	  static Socket sock;
	  String newblockchain;
	  private PrivateKey SecretKey;
	  boolean BlockAlreadyExists = false;
	  final static String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	  static boolean LedgerWasChanged = false;
	  
	  //do the work to figure out the puzzle
	  UnverifiedBlockConsumer(BlockingQueue<BlockRecord> q, PrivateKey SK){
		  tempQ = q;
		  SecretKey = SK;
		  }

	  @SuppressWarnings("unchecked")
	public void run() {

	    System.out.println("Starting the Unverified Block Priority Queue Consumer thread.\n");
	    try{
	        while(true){ 
	        	retrievedRecord = tempQ.take();//takes data and if empty, will block the while loop for now; UnverifiedBlockWorker filled up queue

	        	PublicKey publicKey = PublicKeyWorker.PublicKeyHashMapList.get(retrievedRecord.getProcessID());//retrieves the stored public key from public key worker according to which PID read the file
	        	byte[] signature = Base64.getDecoder().decode(retrievedRecord.getSignedBlockID());//decodes the signed blockID to show signature as a byte array
	        	boolean verified = Blockchain.verifySig(retrievedRecord.getSHA256_BlockIDString().getBytes(), publicKey, signature);//checks if signature contains the matching private key for the public key

	        	if (verified == false)
				{
	        		throw new Exception("Wrong signature");//will throw an exception if it is the wrong signature
				}

	        	
	        	LinkedList<BlockRecord> currentBlockchain = new LinkedList<BlockRecord>();//temporary ledger that will hold a copy of the global ledger at that time
	        	for(BlockRecord rec: Blockchain.bc_ledger) {
	        		currentBlockchain.add(rec);
	        	}
	        	
	        	if (Blockchain.containsRecord(retrievedRecord)){//if this block already exists within the global ledger
	        		//System.out.println("BlockID: " + retrievedRecord.getBlockID() + " has already been verified.");
	        		continue;//gets out of while loop and goes to the next retrievedRecord
	        	}

    		    //Work done on the unverified block
	        	boolean SeedFound = false;
	        	while(SeedFound == false) {
	        		if (Blockchain.containsRecord(retrievedRecord)){//checks again if block already exists because by the end of this while loop, the BlockRecord could already exist
	        			//System.out.println("BlockID: " + retrievedRecord.getBlockID() + " has already been verified.");
	        			break;//gets out of while loop and goes to the next retrievedRecord
	        		}
	        		
	        		String potentialSeed = randomAlphaNumeric(7);//method to choose 7 random alphanumerics, append to a string, return that string (7 chars long)
	        		retrievedRecord.setRandomSeed(potentialSeed);//set those 7 chars as a seed
	        		String prevHash = currentBlockchain.getLast().getSignedRecordString();//obtain the hashed hex format of the potential block this block will be placed after
	        		String DataJson = Tools.SerializeRecord(retrievedRecord);//Convert the current BlockRecord into a Json string 
	        		String seed_prevhash_data = prevHash + DataJson + potentialSeed;//add all the strings together
	        		byte[] SHA_256_Data = Tools.hashData(seed_prevhash_data);//hash the combined string into a SHA-256
	        		String hex_SHA_256_Data = Tools.byteToHex(SHA_256_Data).toString();//convert the SHA-256 string data into hex format
	        		int puzzle = Integer.parseInt(hex_SHA_256_Data.substring(0,4),16);//get the first 16 bits of this hex format
	        		try{Thread.sleep(1000);}catch(Exception e){}
	        		
	        		if(puzzle < 20000) {//found the puzzle if the first 16 bits of the hex format is less than given number. The smaller the given number, to harder the work
	        			
	        			SeedFound = true;//to make it hard, I could've decreased the number to 1000, increasing the time since each process will have to start over again with a random seed and see if the end results fits the requirement (puzzle < 1000)
	    				
	    				byte[] digitalSignature_hex_SHA_256_Data = Blockchain.signData(hex_SHA_256_Data.getBytes(), SecretKey);//creates digital signature with given private key and hex format of the hashed combined data
	    				String stringSigned_Data = Base64.getEncoder().encodeToString(digitalSignature_hex_SHA_256_Data);//encodes with base64 string
	    				retrievedRecord.setSignedRecordString(stringSigned_Data);//stores the signed data
	    				retrievedRecord.setSHA256_DataString(hex_SHA_256_Data);//stores the combined hashed data(SHA-256 string) in hex format

	        			checkIfMainLedgerWasChanged(currentBlockchain);//checks if the global ledger was changed
	        			
	        			if(LedgerWasChanged == true) {
	        				SeedFound = false;//if global ledger was changed AND the current BlockRecord doesnt exist in the global ledger
	        				//at the end of this loop, it will go in the beginning of the while loop with the same current BlockRecord to be checked again if it exists in global ledger
	        				//important: since global ledger was changed, it might affect the prevhash. better to just start all over again with the work
	        				currentBlockchain = new LinkedList<BlockRecord>();
	        				for(BlockRecord block : Blockchain.bc_ledger) {//updates current ledger to the global ledger since global ledger was changed 
	        					currentBlockchain.add(block);
	        				}
	        				LedgerWasChanged = false;//resets since current ledger is updated

	        			}
	        			else {//if global ledger was NOT changed compared to current ledger
	        				if (Blockchain.containsRecord(retrievedRecord)){//checks again if block already exists because by the end of this while loop, the BlockRecord could already exist
	    	        			System.out.println("BlockID: " + retrievedRecord.getBlockID() + " has already been verified.");
	    	        			break;//gets out of while loop and goes to the next retrievedRecord
	    	        		}
	        				//System.out.println("Process " + Blockchain.PID + " verified a BlockID: " + retrievedRecord.getBlockID());
	        				int newBlockNum = currentBlockchain.size();//sets what block number this current block will be since it's been verified
	        				retrievedRecord.setBlockNum(newBlockNum);
	        	        	retrievedRecord.setVerificationProcessID(Integer.toString(Blockchain.PID));
	        	        	currentBlockchain.add(retrievedRecord);
	    				
	        				int numProcesses = 3;
	        				//send out updated ledger to all
	        				SendUpdatedLedgertoProcesses(currentBlockchain, numProcesses);
	        				
	        			}
    			  }
    		  }
	        }
	        
	    }
       
	    catch (Exception e) {
	    		e.printStackTrace();
	    		Tools.printError("Consumer");
	    		}
	  		}
	  
	  public static String randomAlphaNumeric(int count) {
		    StringBuilder builder = new StringBuilder();
		    while (count-- != 0) {//while the given number count is decremented
		      int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());//pick random int from length num of constant ALPHA_NUMERIC_STRING
		      builder.append(ALPHA_NUMERIC_STRING.charAt(character));//choose the character from the ALPHA_NUMERIC_STRING string based off of the random int chosen, append that character to stringbuilder var
		    }
		    return builder.toString();
		  }
	  
	  public static void checkIfMainLedgerWasChanged(LinkedList<BlockRecord> current_bc) {
		  if(current_bc.size() != Blockchain.bc_ledger.size()) {//checks if the size of the global ledger differs from the current copy of the ledger 
				LedgerWasChanged = true;
			}
			else {//if the size of both ledgers are the same, it will compare each blockrecord from the current blockchain to the global ledger
				LedgerWasChanged = false;
				for(int i=0; i < current_bc.size(); i++)
				{
					String X = current_bc.get(i).getBlockID();
					String Y = Blockchain.bc_ledger.get(i).getBlockID();
					if (!X.equals(Y))//if the blockIDs do not match, that means the ledger was changed, breaks the for loop and goes back to the run method in Unverified Block Consumer
					{				//if the all the blockIDs match, LedgerWasChanged will remain false as it initially was
						LedgerWasChanged = true;
						break;
					}
				}
			}
	  
	  }
	  
	  //method that sends an updated ledger to all the processes
	  public static void SendUpdatedLedgertoProcesses(LinkedList<BlockRecord> updated_ledg, int numberOfProcesses) {
		  try {
		  for(int i=0; i< numberOfProcesses; i++){
				ObjectOutputStream sendToBlockchainServer = null;
				sock = new Socket("localhost", Ports.BlockchainServerPortBase + (i * 1000));
				sendToBlockchainServer = new ObjectOutputStream(sock.getOutputStream());
				
				//int processSender = Integer.parseInt(updated_ledg.getLast().getVerificationProcessID());//gets the int process sending this updated blockchain (last one that updated it, adding the last block)
				int processSender = Blockchain.PID;
				
				BlockchainRecord bcr = new BlockchainRecord(updated_ledg, processSender);//create a blockchain record class that holds the process that last updated the ledger and the currently updated ledger
				
				String Json_updatedBlockchainRecord = Tools.SerializeBlockchainRecord(bcr);//convert the blockchainrecord into a Json string
				sendToBlockchainServer.writeObject(Json_updatedBlockchainRecord);//send json string to process i
				sendToBlockchainServer.flush();
				sock.close();
		    	}
		  }
		  catch (Exception x) {
				x.printStackTrace ();
				}
		}
	  
}










//PublicKeyServer that listens for a connection and runs PublicKeyWorker
class PublicKeyInstance implements Runnable {
	int port;
	
	PublicKeyInstance(int p){
		port = p;
	}
	
	public void run() {
		System.out.println("Starting the Public Key Server thread using " + Integer.toString(Ports.KeyServerPort));
		int queueNum = 6;
		Socket sock;
		
		try 
		{
			ServerSocket servSock = new ServerSocket(port, queueNum);
			
			while(true) {
				sock = servSock.accept();
				new PublicKeyWorker(sock).start();
			}
		}
		catch (IOException x) {
			Tools.printError("PublicKeyInstance");
		}
	}
}








//Collects the public keys that were sent from each process, stored in the hashmap with the process number (sender) as the key, and the then the publickey is the value
class PublicKeyWorker extends Thread {
	Socket sock;
	static HashMap<Integer, PublicKey> PublicKeyHashMapList = new HashMap<Integer, PublicKey>();
	int processSender;
	
	PublicKeyWorker(Socket s){
		this.sock = s;
	}
	
	public void run() {
		try{
	    	  @SuppressWarnings("unchecked")
	    	  ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
			  
	    	  String Json_pkr = (String)in.readObject();//reads the json string 

	    	  PKRecord PKR = Tools.DeserializeKeyRecord(Json_pkr);//converts json string into a PKRecord(Process that sent the key, public key)
	    	  processSender = PKR.ProcessID;
	    	  String PK_base64 = PKR.PublicKey;//stores base64 string of the public key
	    	  
	    	  //System.out.println("Received a key from Process: " + processSender + " with PublicKey: ");
	    	  byte[] publicBytes = Base64.getDecoder().decode(PK_base64);//decodes base64 key
	    	  X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
	    	  KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	    	  PublicKey pubKey = keyFactory.generatePublic(keySpec);//public key is fully obtained
	    	  //System.out.println(pubKey);

	    	  PublicKeyHashMapList.put(processSender, pubKey);//added public key and the PID of the one that send the key into the hashmap
    		  
		      sock.close(); 
		    } catch (Exception x){
		    	if(x instanceof NoSuchAlgorithmException) {
		    		x.printStackTrace();
		    	}
		    	
		    	if(x instanceof InvalidKeySpecException) {
		    		x.printStackTrace();
		    	}
		    	
		    	if(x instanceof IOException) {
		    		x.printStackTrace();
		    	}
		    	}
	}
}











//Blockchain Server that listens for a connection and runs BlockchainWorker
class BlockchainInstance implements Runnable {
	int port;
	
	BlockchainInstance(int p){
		port = p;
	}
	
	public void run() {
		int queueNum = 6;
		Socket sock;
		
		try 
		{
			ServerSocket servSock = new ServerSocket(port, queueNum);
			
			while(true) {
				sock = servSock.accept();
				new BlockchainWorker(sock).start();
			}
		}
		catch (IOException x) {
			Tools.printError("Waiting for connection to the BLOCKCHAIN in BlockchainServer");
		}
	}
}











//Takes input from clients with their updated blockchain
class BlockchainWorker extends Thread {
	Socket sock;
	
	BlockchainWorker(Socket s){
		sock = s;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		
		
		try
		{		

	    ObjectInputStream in = new ObjectInputStream(sock.getInputStream());

  	  	String Json_Records = (String) in.readObject();//receives Json string input from Process's UnverifiedConsumer
  	  	
  	  	BlockchainRecord updatedBlockchain = Tools.DeserializeBlockchainRecord(Json_Records);//Converts json string into a BlockchainRecord (contains the PID of the sender and the updated ledger)
  	  	
  	  	if(updatedBlockchain.getUpdatedLedger().size() > 1) {//if it is not the ledger with just the dummy block in it
  	  	
  	  		PublicKey publicKey = PublicKeyWorker.PublicKeyHashMapList.get(updatedBlockchain.getProcessSender());//retrieves the stored public key from public key worker according to which PID that send the updated ledger
  	  		BlockRecord newVerifiedBlock = updatedBlockchain.getUpdatedLedger().getLast();//receives the last block that was updated
  	  		byte[] signature = Base64.getDecoder().decode(newVerifiedBlock.getSignedRecordString());//gets the signature of that block
  	  		boolean verified = Blockchain.verifySig(newVerifiedBlock.getSHA256_DataString().getBytes(), publicKey, signature);//checks if that block is verified. 
  	  		
  	  		if (verified == false)
  	  		{
  	  			throw new Exception("Wrong signature");
  	  		}
  	  	}
  	  	else {//if it is just the dummy block
  	  		System.out.println("Blockchain created!");
  	  	}
  	  	
  	  	if (Blockchain.containsRecord(updatedBlockchain.getUpdatedLedger().getLast())){//checks again if block already exists because by the end of this while loop, the BlockRecord could already exist
			//System.out.println("BlockID: " + updatedBlockchain.getUpdatedLedger().getLast().getBlockID() + " has already been verified.");
		}
  	  	else {
  	  		Blockchain.bc_ledger = updatedBlockchain.getUpdatedLedger();//updates the global ledger with the updated blockchain that was received
  	  		
  	  		System.out.println("Process " + updatedBlockchain.getUpdatedLedger().getLast().getVerificationProcessID() + " verified a BlockID: " + updatedBlockchain.getUpdatedLedger().getLast().getBlockID());
  	  		
  	  		if(Blockchain.PID == 0) {//if the PID is 0, it output the ledger as a json string onto the file "BlockchainLedger.json"; it will replace the "BlockchainLedger.json" if it already exists in directory
  	  			String json_bc = Tools.SerializeList(Blockchain.bc_ledger);
  	  			try (
  	  					FileWriter file = new FileWriter("BlockchainLedger.json")) {
  	  	 
  	  				file.write(json_bc);
  	  				file.flush();
 
  	  			} catch (IOException e) {
  	  				e.printStackTrace();
  	  			}
  	  		}
  	  	}
//  	    System.out.println("size of ledger: " + Blockchain.bc_ledger.size());
  	    
//  	    if(Blockchain.bc_ledger.size() == 13) {
//	    	for(BlockRecord br : Blockchain.bc_ledger)
//	    	System.out.println(br.getLname() + ", " + br.getFname() + ", Verified by Process " + br.getVerificationProcessID());
//	    	}

  	  	sock.close(); 
		}
		catch (Exception e){
			if(e instanceof InterruptedException) {
				e.printStackTrace();
			}
			else {
				e.printStackTrace();
			}
		}
	}
	
}









//stores PID of the sender and the updated ledger
class BlockchainRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	LinkedList<BlockRecord> updated_ledger;
	int ProcessThatSent_ledger;
	
	BlockchainRecord(LinkedList<BlockRecord> l, int p){
		updated_ledger = l;
		ProcessThatSent_ledger = p;
	}
	
		public int getProcessSender() {return ProcessThatSent_ledger;}

		public LinkedList<BlockRecord> getUpdatedLedger() {return updated_ledger;}

	
}






//Stores the data that was read from the file
class BlockRecord implements Serializable{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  int ProcessID;
	  int BlockNum;
	  String BlockID;
	  String TimeStamp;
	  String VerificationProcessID; 
	  UUID uuid; 
	  String Fname;
	  String Lname;
	  String SSNum;
	  String DOB;
	  String RandomSeed; 
	  String Diag;
	  String Treat;
	  String Rx;
	  String SHA256_DataString;
	  String SHA256_BlockIDString;
	  String SignedBlockID;
	  String SignedRecordString;
	  String HashedSHA256String;

	  public int getProcessID() {return ProcessID;}
	  public void setProcessID(int _PID){ProcessID = _PID;}
	  
	  public int getBlockNum() {return BlockNum;}
	  public void setBlockNum(int BN){BlockNum = BN;}

	  public String getBlockID() {return BlockID;}
	  public void setBlockID(String BID){BlockID = BID;}
	  
	  public String getSignedBlockID() {return SignedBlockID;}
	  public void setSignedBlockID(String SBID){SignedBlockID = SBID;}

	  public String getSHA256_DataString() {return SHA256_DataString;}
	  public void setSHA256_DataString(String SBID){SHA256_DataString = SBID;}
	  
	  public String getSHA256_BlockIDString() {return SHA256_BlockIDString;}
	  public void setSHA256_BlockIDString(String SBID){SHA256_BlockIDString = SBID;}
	  
	  public String getVerificationProcessID() {return VerificationProcessID;}
	  public void setVerificationProcessID(String VID){VerificationProcessID = VID;}
	  
	  public String getSignedRecordString() {return SignedRecordString;}
	  public void setSignedRecordString(String SRS){SignedRecordString = SRS;}
	  
	  public UUID getUUID() {return uuid;} 
	  public void setUUID (UUID ud){uuid = ud;}

	  public String getTimeStamp() {return TimeStamp;}
	  public void setTimeStamp (String TS){TimeStamp = TS;}
	  
	  public String getLname() {return Lname;}
	  public void setLname (String LN){Lname = LN;}
	  
	  public String getFname() {return Fname;}
	  public void setFname (String FN){Fname = FN;}
	  
	  public String getSSNum() {return SSNum;}
	  public void setSSNum (String SS){SSNum = SS;}
	  
	  public String getDOB() {return DOB;}
	  public void setDOB (String RS){DOB = RS;}

	  public String getDiag() {return Diag;}
	  public void setDiag (String D){Diag = D;}

	  public String getTreat() {return Treat;}
	  public void setTreat (String Tr){Treat = Tr;}

	  public String getRx() {return Rx;}
	  public void setRx (String rx){Rx = rx;}

	  public String getRandomSeed() {return RandomSeed;}
	  public void setRandomSeed (String RS){RandomSeed = RS;}
	  
	  public static Comparator<BlockRecord> BlockTSComparator = new Comparator<BlockRecord>()//helps compare timestamps - used for priority queue
	    {
	     @Override
	     public int compare(BlockRecord b1, BlockRecord b2)
	     {
	      String s1 = b1.getTimeStamp();
	      String s2 = b2.getTimeStamp();
	      if (s1 == s2) {return 0;}
	      if (s1 == null) {return -1;}
	      if (s2 == null) {return 1;}
	      return s1.compareTo(s2);
	     }
	    };
	 
	  public void printString() {
		  System.out.println("Firstname: " + Fname);
		  System.out.println("Lastname: " + Lname);
	  }
}









//Class that consists of methods that will help in reading files, serializing(convert object to json string), 
//deserializing(convert json string to object), SHA-256 hashing, converting byte array to hexformat, printing errors
class Tools{
	
	//reading files from directory
	public static ArrayList<BlockRecord> readFile(String file, int ProcessID, PrivateKey SK) {
		ArrayList<BlockRecord> data = new ArrayList<BlockRecord>();
		String input;
		String[] inputArray;
		
		try (BufferedReader buffread = new BufferedReader(new FileReader(file))){
			while((input = buffread.readLine()) != null) {
				BlockRecord rec = new BlockRecord();
				
				//set UUID
				UUID _uuid = UUID.randomUUID();
				rec.setUUID(_uuid);
				rec.setBlockID(_uuid.toString());		
				
				try{Thread.sleep(1001);}catch(InterruptedException e){}
				Date date = new Date();
				String T1 = String.format("%1$s %2$tF.%2$tT", "", date);//formats the timestamp with current date and time
				String TimeStampString = T1 + "." + ProcessID;//adds processID for uniqueness
				rec.setTimeStamp(TimeStampString);
				
				//set ProcessID
				rec.setProcessID(ProcessID);
				
				inputArray = input.split(" +");
				
				//Set first name, last name, date, SSN, diagnosis, treatment, medication
				rec.setFname(inputArray[0]);
				rec.setLname(inputArray[1]);
				rec.setDOB(inputArray[2]);
				rec.setSSNum(inputArray[3]);
				rec.setDiag(inputArray[4]);
				rec.setTreat(inputArray[5]);
				rec.setRx(inputArray[6]);
				
				//uses private key to sign blockID and sets the signed BlockID and hexformat in the blockRecord
				byte[] SHA_256_BlockID = Tools.hashData(rec.getBlockID());
				String hex_SHA_256_BlockID = Tools.byteToHex(SHA_256_BlockID).toString(); 
				byte[] digitalSignature_hex_SHA_256_BlockID = Blockchain.signData(hex_SHA_256_BlockID.getBytes(), SK);
				String stringSigned_BlockID = Base64.getEncoder().encodeToString(digitalSignature_hex_SHA_256_BlockID);
				rec.setSignedBlockID(stringSigned_BlockID);
				rec.setSHA256_BlockIDString(hex_SHA_256_BlockID);
				data.add(rec);
				}
			}
		catch(Exception x){
			x.printStackTrace();
		}
		
		return data;
	}
	
	//converts Json string into PKRecord 
	public static PKRecord DeserializeKeyRecord(String K)
    {
		PKRecord br;
		try {
			Gson gson = new Gson();
			JsonReader reader = new JsonReader(new StringReader(K));
			reader.setLenient(true);
			PKRecord pkr = new PKRecord(5, "fake");
			br = gson.fromJson(reader, pkr.getClass());
		}
		catch(Exception e) {
			if(e instanceof NoSuchAlgorithmException) {
				Tools.printError("NoSuchAlgorithm inside PKRecord");
			}
			
			if(e instanceof InvalidKeySpecException) {
				Tools.printError("InvalidKeySpec inside PKRecord");
			}
			return null;
		}
		return br;
			
    }
	
	//converts Json string into BlockRecord
	public static BlockRecord DeserializeRecord(String recordString)
    {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(recordString));
		reader.setLenient(true);
		BlockRecord br = gson.fromJson(reader, BlockRecord.class);
		return br;
    }
	
	//converts Json string into LinkedList<BlockRecord>
	@SuppressWarnings("unchecked")
	public static LinkedList<BlockRecord> DeserializeList(String recordString){
		JsonReader reader = new JsonReader(new StringReader(recordString));
		LinkedList<BlockRecord> LBR = new LinkedList<BlockRecord>();
		reader.setLenient(true);
		LinkedList<BlockRecord> br = new Gson().fromJson(recordString, new TypeToken<LinkedList<BlockRecord>>() {}.getType());
		return br;
    }
	
	//converts Json string into BlockchainRecord
	public static BlockchainRecord DeserializeBlockchainRecord(String recordString)
    {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(recordString));
		reader.setLenient(true);
		BlockchainRecord br = gson.fromJson(reader, BlockchainRecord.class);
		return br;
    }
	
	//converts BlockchainRecord into a Jsonstring
	public static String SerializeBlockchainRecord(BlockchainRecord bcr) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(bcr);
        return json;
	}
	
	//converts PKRecord into a Jsonstring
	public static String SerializeKeyRecord(PKRecord K) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(K);
        return json;
	}
	
	//converts BlockRecord into a Jsonstring
	public static String SerializeRecord(BlockRecord block) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(block);
        return json;
	}
	
	//converts LinkedList<BlockRecord> into a Jsonstring
	public static String SerializeList(LinkedList<BlockRecord> blockList) {
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String json = gson.toJson(blockList);
        return json;
	}
	
	//Prints error
	static void printError(String m) {
		System.out.println("Error occured: " + m);
	 }
	
	//converts string into a SHA-256 hash
	static byte[] hashData(String d) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(d.getBytes());
			return md.digest();
		}
		catch(Exception e) {
			Tools.printError("hashData");
			return null;
		}
	}
	
	//converts byte array into a hex format
	static StringBuffer byteToHex(byte[] b) {
		StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	      sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
	    }
	    return sb;
	}
}


	
