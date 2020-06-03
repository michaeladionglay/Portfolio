/*
_____________________________________________________________________________________________
Name: Michaela Dionglay
Date: 04/18/2020
Java version: 1.8.0_221-b11
Command-line compilation instructions: javac JokeClientAdmin.java

Instructions to run this program: 

=Depends on what you want. Must indicate what server to connect to in arguments=

1. no argument? default server localhost used, port 5050; WILL NOT CONNECT TO SECONDARY SERVER
>java JokeClientAdmin

2. 1 argument? admin will connect to the server ipaddress at port 5050; WILL NOT CONNECT TO SECONDARY SERVER
>java JokeClientAdmin <ipaddress>

3. 2 arguments? admin can switch between primary and secondary servers at port 5051
>java JokeClientAdmin localhost <ipaddress> 		ex. 140.192.1.9
or 
>java JokeClientAdmin localhost localhost

IP address: 10.0.0.186
List of files needed to run program:
	a. JokeServer.java
	b. JokeClient.java
	c. JokeClientAdmin.java
_____________________________________________________________________________________________
 */

//package JokeServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class JokeClientAdmin {
		static String serverName;
		static String serverName2;
		static String currentServer;
		static Socket AdminSocket;
		static int port = 5050;
		static int port2 = 5051;
		static int currentPort;
		static boolean SecondaryExists = false;
		static boolean SecondaryAdminActivated = false;
		public static void main (String args[]) {
			if (args.length < 1) {//default localhost
				serverName = "localhost";
				currentServer = serverName;
				currentPort = port;//default
				System.out.println("Michaela Dionglay's JokeClientAdmin.\n");
				System.out.format("Using server: " + serverName + " and listening to port: %s" + "\n", port);
			}
			if (args.length == 1) {//adminclient will connect to this ipaddress
				serverName = args[0];
				currentServer = serverName;
				currentPort = port;//default
				System.out.println("Michaela Dionglay's JokeClientAdmin.\n");
				System.out.format("Using server one: " + serverName + " and listening to port: %s" + "\n", port);
			}
			if (args.length > 1) {
				serverName = args[0];
				currentServer = serverName;//default is primary first server
				serverName2 = args[1];
				currentPort = port;//primary port
				//secondary is 5051
				System.out.println("Michaela Dionglay's JokeClientAdmin.\n");
				System.out.format("Using server one: " + serverName + " and server two: " + serverName2 + " and listening to port: %s" + "\n", port);
				SecondaryExists = true;
			}

				
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));//get user input from client side
				try
				{

					String choice;
					System.out.println("Currently communicating with " + currentServer + ": ");
					do //continue to ask admin what mode to set until the admin type quit
					{

							System.out.println("(1) Enter 's' if you want to switch to the other server");//give user option to switch servers
							System.out.println("(1) Enter 'JOKE' to change" + currentServer + " to joke mode");
							System.out.println("(2) Enter 'PROVERB' to change " + currentServer + " to proverb mode");
							System.out.println("(3) Enter 'quit' to exit");
							choice = in.readLine();
							if(choice.compareTo("s") == 0) {//if user chooses serverName 1, secondary is deactivated
								if(serverName2 == null) {
									System.out.println("No secondary server being used...");
									System.out.println();
									continue;
								}
								SecondaryAdminActivated = !SecondaryAdminActivated;//SWITCHES BETWEEN SERVERS!!!!!!
								if(SecondaryAdminActivated) 
								{//if current is primary, change to secondary server
									currentServer = serverName2;
									currentPort = port2;
								}
								else if(!SecondaryAdminActivated)
								{//if current is secondary, change to primary
									currentServer = serverName;
									currentPort = port;
								}
								System.out.println("Now communicating with: " + currentServer + " in port: " + currentPort);
								continue;
							}

						if(choice.indexOf("quit") < 0) {//if user just presses enter (or any key as long as its not 's' or 'quit')
							AdminClientRequestToServer(choice);
						}
					}
					while (choice.indexOf("quit") < 0);
					System.out.println ("Cancelled by user request.\n");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}

	
	public static void AdminClientRequestToServer(String AdminRequest) {
		String serverReply;
		while(serverIsOpen() == false) {//connects socket to server
			System.out.println("Server is currently preoccupied \n");
		}
			try 
			{
				BufferedReader FromServer = new BufferedReader(new InputStreamReader(AdminSocket.getInputStream()));//stream created to input reply from server
				ObjectOutputStream ToServer = new ObjectOutputStream(AdminSocket.getOutputStream());//stream created to output object request to server
				
				AdminRequest req = new AdminRequest(AdminRequest, "Admin");//creates object to prepare to be sent to the server with Admin as a username
				ToServer.writeObject(req);
				ToServer.flush();
				//Admin reads what the joke the server says
				for (int i = 1; i <=3; i++){
					serverReply = FromServer.readLine();//reads server reply
					if(serverReply != null) {
						System.out.println(serverReply);
					}
				}
			}
			catch(IOException e) {
				System.out.println("Error in Input from Server");
				e.printStackTrace();
			}
		}

	
	public static boolean serverIsOpen(){
			try 
			{
				AdminSocket = new Socket(currentServer, currentPort);
				return true;
			}
			catch (IOException e) {
				System.out.println("Connection to thread error\n");
				return false;
			}
		}
	
}



class AdminRequest implements Serializable{//a class admin can use to send to the server.
							//requires the class to be serializable if object is going in stream
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//requires an object to be serializable when sending through a socket
	public String userName;
	public String requestFromAdmin;
	
	AdminRequest( String AR, String UN){//request from client
		this.userName = UN;
		this.requestFromAdmin = AR;
	}
	
	public String toString() {
		if(userName != null) {
			return this.requestFromAdmin + ";" + this.userName;
		}
		else {
			return this.requestFromAdmin;
		}
	}
}

