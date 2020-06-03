/*_____________________________________________________________________________________________
Name: Michaela Dionglay
Date: 04/18/2020
Java version: 1.8.0_221-b11
Command-line compilation instructions: javac JokeClient.java
Instructions to run this program: 

=Depends on what you want. Must indicate what server to connect to in arguments=

1. no argument? default server localhost used, port 4545; WILL NOT CONNECT TO SECONDARY SERVER
>java JokeClient

2. 1 argument? admin will connect to the server ipaddress at port 4545; WILL NOT CONNECT TO SECONDARY SERVER
>java JokeClient <ipaddress>

3. 2 arguments? admin can switch between primary and secondary servers at port 4546
>java JokeClient localhost <ipaddress> 		ex. 140.192.1.9
or 
>java JokeClient localhost localhost

IP address: 10.0.0.186
List of files needed to run program:
	a. JokeServer.java
	b. JokeClient.java
	c. JokeClientAdmin.java
_____________________________________________________________________________________________
*/

//package JokeServer;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;



public class JokeClient {
	static int port = 4545;
	static int port2 = 4546;
	static int currentPort;
	static String serverName;
	static String serverName2 = null;
	static String currentServer;
	static String userName;
	static Socket socket;
	static boolean SecondaryExists = false;
	static boolean SecondaryAdminActivated = false;
	static Manager JokeProverbManager;
	static Manager JokeProverbManager2;//keep track of conversations by keeping different objects of Manager
	public static void main (String args[]) {
		if (args.length < 1) {//default localhost
			serverName = "localhost";
			currentServer = serverName;//default is localhost
			currentPort = port;//default is 4545
			System.out.println("Michaela Dionglay's JokeClient.\n");
			System.out.format("Using server: " + serverName + " and listening to port: %s" + "\n", port);
		}
		if (args.length == 1) {//adminclient will connect to this ipaddress
			serverName = args[0];
			currentServer = serverName;
			currentPort = port;
			System.out.println("Michaela Dionglay's JokeClient.\n");
			System.out.format("Using server: " + serverName + " and listening to port: %s" + "\n", port);
		}
		if (args.length > 1) {
			serverName = args[0];
			serverName2 = args[1];
			currentServer = serverName;
			currentPort = port;
			System.out.println("Michaela Dionglay's JokeClient.\n");
			System.out.format("Using server one: " + serverName + " and server two: " + serverName2 + " and listening to port: %s" + "\n", port);
			SecondaryExists = true;
		}

		System.out.println("What is your name? ");//inside client
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//get user input from client side
		try
		{
			String choice;
			userName = in.readLine();//reads the users input
			if(userName.length() == 0) {//chooses a default name
				userName = "Joker";
			}
			
			System.out.flush();
			JokeProverbManager = new Manager();
			JokeProverbManager2 = new Manager();
			JokeProverbManager.generateJokes();
			JokeProverbManager.generateProverbs();//generates proverbs and jokes for new client
			JokeProverbManager2.generateJokes();
			JokeProverbManager2.generateProverbs();
			System.out.println("Continue pressing enter or type 'quit' to exit");
			do {
					System.out.println("(1) Enter 's' if you want to switch to the other server");//give user option to switch servers
					System.out.println("(2) Press enter to continue");
					System.out.println("(3) Enter 'quit' to exit");
					choice = in.readLine();//reads the users input
					if(choice.compareTo("s") == 0) {//if user chooses serverName 1, secondary is deactivated
						if(serverName2 == null) {
							System.out.println("No secondary server being used...");
							System.out.println();
							continue;
						}
						SecondaryAdminActivated = !SecondaryAdminActivated;//SWITCHES BETWEEN SERVERS!!!!!!
						if(SecondaryAdminActivated) //changes current connected server to secondary
						{//if current is primary, change to secondary server
							currentServer = serverName2;
							currentPort = port2;
						}
						else if(!SecondaryAdminActivated)//changes current connected server to primary
						{//if current is secondary, change to primary
							currentServer = serverName;
							currentPort = port;
						}
						System.out.println("Now communicating with: " + currentServer + " at port: " + currentPort);
						continue;
					}
					
					if (choice.indexOf("quit") < 0) {//if user just presses enter (or any key as long as its not 's' or 'quit')
						if(SecondaryAdminActivated) {//will only send request to secondary server
							ClientRequestToServer(choice, JokeProverbManager2);
						}
						else {//will only send request to primary server
							ClientRequestToServer(choice, JokeProverbManager);
						}
					}
			} 
			while (choice.indexOf("quit") < 0);
			System.out.println ("Cancelled by user request.\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void ClientRequestToServer(String request, Manager M) {//method that takes care of inputs and outputs for streams
		String serverReply;
		Manager newManagerObject = null;
		ReplyAndUpdateObject ObjectReceived = null;
		
		while(serverIsOpen() == false) {//connects sockets
			System.out.println("Server is currently preoccupied \n");
		}
			try 
			{
				//signal server to send joke and provide available jokes this client has not seen
				ObjectOutputStream ToServer = new ObjectOutputStream(socket.getOutputStream());
				ClientRequest req = new ClientRequest(request, userName, M);
				ToServer.writeObject(req);//OUTPUTS ClientRequest OBJECT, username, and Manager object to server
				ToServer.flush();

				ObjectInputStream receiveObjectFromServer = new ObjectInputStream(socket.getInputStream());//receives updated ManagerObject to client

//				try {
				ObjectReceived = (ReplyAndUpdateObject)receiveObjectFromServer.readObject();
				System.out.println("Received from server: "+ currentServer + " in port " + currentPort);
				System.out.println(ObjectReceived.getreply());
				System.out.println();
//				}
//				catch(EOFException l) {}
				if(SecondaryAdminActivated) {//when the object comes back, if currently in secondary server, it will change secondary Manager
					JokeProverbManager2 = ObjectReceived.getManager();
				}
				else{//when the object comes back, if currently in primary server, it will change primary Manager
					JokeProverbManager = ObjectReceived.getManager();
				}
				socket.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			catch(ClassNotFoundException x) {
				x.printStackTrace();
			}
		}
	
	static boolean serverIsOpen(){
		try 
		{
			socket = new Socket(currentServer, currentPort);
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}
}


class ClientRequest implements Serializable{//object with name and client request 
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//a class admin and client can use and server will be able to identify
	//requires an object to be serializable when sending through a socket
	public String userName;
	public String requestFromClient;
	public Manager Jokes_Proverbs;

	
	ClientRequest( String CR, String UN, Manager JP){//request from client
		this.userName = UN;
		this.requestFromClient = CR;
		this.Jokes_Proverbs = JP;
	}
	
	public String toString() {
			return this.requestFromClient + ";" + this.userName;
	}
}



class JokesProverbObject implements Serializable{//object with string joke/proverb and type(JA, JB, etc.)
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type;
	String response;
	JokesProverbObject(String t, String r){
		this.type = t;
		this.response = r;
	}
	
	public String getType(){
		return this.type;
	}
	
	public String getResponse(){
		return this.response;
	}
	
}


class Manager implements Serializable{//Manages Jokes and Proverbs as a list of JokesProverbObjects

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<JokesProverbObject> AvailableProverbs = new ArrayList<JokesProverbObject>();
	 ArrayList<JokesProverbObject> UsedProverbs = new ArrayList<JokesProverbObject>();
	 ArrayList<JokesProverbObject> AvailableJokes = new ArrayList<JokesProverbObject>();
	 ArrayList<JokesProverbObject> UsedJokes = new ArrayList<JokesProverbObject>();
	Manager(){}
	
	public void generateProverbs() {//add in arraylist AvailableProverbs
		AvailableProverbs.add(new JokesProverbObject("PA", "A bad workman always blames his tools\n"));//readline expects a \n at the end to know its the end
		AvailableProverbs.add(new JokesProverbObject("PB", "A journey of thousand miles begins with a single step.\n"));
		AvailableProverbs.add(new JokesProverbObject("PC", "Birds of a feather flock together\n"));
		AvailableProverbs.add(new JokesProverbObject("PD", "Too many cooks spoil the broth\n"));
	}
	
	public void generateJokes() {//add in arraylist AvailableJokes
		AvailableJokes.add(new JokesProverbObject("JA", "What do you call a pig that does Karate? PORKCHOP!\n"));
		AvailableJokes.add(new JokesProverbObject("JB", "Why did the bike fall over? BECAUSE IT WAS TOO TIRED!\n"));
		AvailableJokes.add(new JokesProverbObject("JC", "What did the Policeman say to his belly button? YOU'RE UNDER A VEST!\n"));
		AvailableJokes.add(new JokesProverbObject("JD", "What did the nut say when it was chasing the other nut?I'LL CASHEW!\n"));
	}
	
	public void usedJoke(JokesProverbObject used) {//takes in the used joke, removes it from the availablejokes
												//stores the used jokes in UsedJokes
		AvailableJokes.remove(used);
		UsedJokes.add(used);
	}
	
	public void usedProverb(JokesProverbObject used) {//takes in the used proverb, removes it from the availableproverbs
													//stores the used proverbs in UsedProverbs
		AvailableProverbs.remove(used);
		UsedProverbs.add(used);
	}
	
	public void resetJOKE() { //clears and the generates new jokes
		UsedJokes.clear();
		generateJokes();
	}
	
	public void resetPROVERB() {//clears and the generates new proverbs
		UsedProverbs.clear();
		generateProverbs();
	}

}

