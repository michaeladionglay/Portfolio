/*
_____________________________________________________________________________________________
Name: Michaela Dionglay
Date: 04/18/2020
Java version: 1.8.0_221-b11
Command-line compilation instructions: javac JokeServer.java
Instructions to run this program: 

=Must indicate what server to connect to in arguments=

1. 0 arguments? default is primary server connected to port 4545
>java JokeServer 

2. 1 argument? default server local host connected to port 4546
>java JokeServer secondary

java JokeServer
IP address: 10.0.0.186
List of files needed to run program:
	a. JokeServer.java
	b. JokeClient.java
	c. JokeClientAdmin.java
_____________________________________________________________________________________________
 */

//package JokeServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;



public class JokeServer {
	
	static int _state;
	static boolean SecondaryAdminActivated = false;
	
	public static void main(String a[]) throws IOException {
		int queueNum = 6;
		int port = 4545;
		int adminPort = 5050;
		Socket socket;
		
		if(a.length < 1) {
			System.out.format("Michaela Dionglay's JokeServer[localhost default] starting up, listening at port %s.\n", port);
		}
		else if((a.length == 1) && (a[0].compareTo("secondary") == 0)) {
			port = 4546;
			adminPort = 5051;
			System.out.format("Michaela Dionglay's JokeServer[secondary] starting up, listening at port %s.\n", port);
			SecondaryAdminActivated = true;
		}
		
		//default localhost for adminclient
		try {
			AdminInstance AL = new AdminInstance(adminPort); //port 5050
		    new Thread(AL).start(); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		ServerSocket serverSocket = new ServerSocket(port, queueNum);
		
		//default localhost for client
		try 
		{
			while(true){
				socket = serverSocket.accept();//port 4545
				new ClientWorker(socket).start();
				}
		}
		catch (IOException e){
			e.printStackTrace();
			if (serverSocket != null && !serverSocket.isClosed()) {
		        try 
		        {
		            serverSocket.close();
		        } 
		        catch (IOException o)
		        {
		            o.printStackTrace(System.err);
		        }
		    }
		}
	}
}

class ClientWorker extends Thread{

	static int state = 0; //0 = joke, 1 = proverb
	Socket sock; 
	static String userName;
	
	ClientWorker(Socket s) 
	{
		sock = s;
	} 
	
	//action in JokeClient
	public void run(){

			try 
			{
					ObjectInputStream reqObjectFromClient = new ObjectInputStream(sock.getInputStream());	
					ClientRequest clientReq = (ClientRequest)reqObjectFromClient.readObject();//receive object from Client
					System.out.println(clientReq.userName + " made a request!");
					
					if(state == 1) 
							{//PROVERBS
								Manager currentManager;
								currentManager = clientReq.Jokes_Proverbs;
								if(currentManager.AvailableProverbs.isEmpty()) {//checks if empty and resets if it is
									currentManager.resetPROVERB();
									shuffleList(currentManager.AvailableProverbs);//shuffles list after end of cycle
								}
								shuffleList(currentManager.AvailableProverbs);
								JokesProverbObject proverb = selectFromList(currentManager.AvailableProverbs);//picks a proverb in list
								PrintStream outputToClient = new PrintStream(sock.getOutputStream());//create stream to output a print stream
								//printRemoteResponse(clientReq.userName, proverb, outputToClient);//prints proverb in client window 
								
								currentManager.usedProverb(proverb);//changes current Manager object with an update
								ReplyAndUpdateObject objectToReturn = new ReplyAndUpdateObject(Reply(clientReq.userName, proverb), currentManager);//creates instance to store reply and updated manager
								
								System.out.println("Server sent " + clientReq.userName + " proverb!");
								if(currentManager.AvailableProverbs.size() == 0) {//will occur after printing the last unseen joke/proverb to the client
									System.out.println("PROVERB CYCLE ENDED for "+ clientReq.userName);
								}
								//currentManager.usedProverb(proverb);//changes Manager object with an update
								
								ObjectOutputStream sendObjectToClient = new ObjectOutputStream(sock.getOutputStream());//create stream to output an object to client
								outputObjectRemoteResponse(objectToReturn, sendObjectToClient);//send back updated Manager object
								System.out.println("Server sent " + clientReq.userName + " update");
								sendObjectToClient.flush();
							}
					else
							{//JOKES; look at comments for proverbs right above
								Manager currentManager;
								currentManager = clientReq.Jokes_Proverbs;
								if(currentManager.AvailableJokes.isEmpty()) {
									currentManager.resetJOKE();	
									shuffleList(currentManager.AvailableJokes);
								}
								shuffleList(currentManager.AvailableJokes);
								JokesProverbObject joke = selectFromList(currentManager.AvailableJokes);
								PrintStream outputToClient = new PrintStream(sock.getOutputStream());
								//printRemoteResponse(clientReq.userName, joke, outputToClient);
								
								currentManager.usedJoke(joke);
								ReplyAndUpdateObject objectToReturn = new ReplyAndUpdateObject(Reply(clientReq.userName, joke), currentManager);
								
										
								System.out.println("Server sent " + clientReq.userName + " joke!");
								if(currentManager.AvailableJokes.size() == 0) {
									System.out.println("JOKE CYCLE ENDED for "+ clientReq.userName);
								}
								//currentManager.usedJoke(joke);
								
								ObjectOutputStream sendObjectToClient = new ObjectOutputStream(sock.getOutputStream());
								//outputObjectRemoteResponse(currentManager, sendObjectToClient);
								outputObjectRemoteResponse(objectToReturn, sendObjectToClient);
								System.out.println("Server sent " + clientReq.userName + " update");
								sendObjectToClient.flush();
							}
					
					this.sock.close();

			} 
			catch (IOException x) 
			{
				System.out.println("Server read error\n");
				x.printStackTrace ();
				
			}
			catch (ClassNotFoundException c) {
				c.printStackTrace();
			}
		} 
	

	static void outputObjectRemoteResponse (ReplyAndUpdateObject RU, ObjectOutputStream outx) {//method to send Updated Manager object
		try {
			outx.writeObject(RU);
		} 
		catch(Exception e)
		{
			e.printStackTrace();

		}
	}
	
	public static String Reply(String clientName, JokesProverbObject jokesProverbObject){//returns the joke with username, 
																			//type of joke/proverb, and the joke/proverb in a format
		if(JokeServer.SecondaryAdminActivated) {
			return("<S2> " + jokesProverbObject.getType() + " " + clientName + " : " + jokesProverbObject.getResponse().trim());
		}
			return(jokesProverbObject.getType() + " " + clientName + " : " + jokesProverbObject.getResponse().trim());
	}


	
	public JokesProverbObject selectFromList(ArrayList<JokesProverbObject> availList) {
		return availList.get(0);//select first element
	}
	
	public void shuffleList(ArrayList<JokesProverbObject> lis) {//shuffles the arraylist
		Collections.shuffle(lis);
	} 
}

class ReplyAndUpdateObject implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//contains string reply and updated manager
	String reply;
	Manager manager;
	ReplyAndUpdateObject(String r, Manager m){
		reply = r;
		manager = m;
	}
	
	public String getreply() {
		return reply;
	}
	
	public Manager getManager() {
		return manager;
	}
}



class AdminInstance implements Runnable{
	public static boolean adminControlSwitch = true;
	ServerSocket adminServSocket;
	int port;
	AdminInstance(int p){
		port = p;
	}
	public void run() {
		int queueNum = 6;
		Socket socket;
		
		try
		{
			adminServSocket = new ServerSocket(port, queueNum);
			while(adminControlSwitch) {
				socket = adminServSocket.accept();
				new AdminWorker(socket).start();
			}
		}
		catch (IOException errorInAdminThread) {
			errorInAdminThread.printStackTrace();
			if (adminServSocket != null && !adminServSocket.isClosed()) {
		        try {
		        	adminServSocket.close();
		        } catch (IOException e)
		        {
		            e.printStackTrace(System.err);
		        }
		    }
		}
	}
}



class AdminWorker extends Thread{
	
	public Socket socket;
	
	AdminWorker(Socket s){
		socket = s;
	}
	
	public void run() {
		try {
			//takes in object that was send FROM admin
			ObjectInputStream reqObjectFromAdmin = new ObjectInputStream(socket.getInputStream());
			try
			{
				AdminRequest AdminReq = (AdminRequest)reqObjectFromAdmin.readObject();//reads Admin request object from admin
				System.out.println("Server received this command from " + AdminReq.userName + " : change current mode to " + AdminReq.requestFromAdmin);//prints in server
				//sends reply out to admin
				PrintStream outputToAdmin = new PrintStream(socket.getOutputStream());
			switch(AdminReq.requestFromAdmin) 
				{
				case("JOKE"):
					{
						ClientWorker.state = 0;
						outputToAdmin.println("Server changed to JOKE_MODE");
						System.out.println("Server sends Admin confirmation for the changes");
						System.out.flush();
						break;
					}
				case("PROVERB"):
					{
						ClientWorker.state = 1;
						outputToAdmin.println("Server changed to PROVERB_MODE");
						System.out.println("Server sends Admin confirmation for the changes");
						System.out.flush();
						break;
					}
				default:
					{
						System.out.println("Error in Joke/Proverb Mode switch\n");
					}
				}
				this.socket.close();
			}
			catch (Exception e)
			{
				System.out.println("Error in Server\n");
				e.printStackTrace();
			}
		}	
		catch (IOException e)
		{
			System.out.println("Error in socket\n");
		}
	}
}
