# PORTFOLIO
Consists of projects that I have worked on throughout school. 

1. [ BLOCKCHAIN PROJECT. ](#bc)
2. [ JOKESERVER. ](#js)
3. [ MOVIE RENTAL SYSTEM PROJECT. ](#mrs)
4. [ TETRIS. ](#tetris)







<a name="bc"></a>
# BLOCKCHAIN PROJECT:

Summary: Each process will read their own designated file (BlockInput0.txt, BlockInput1.txt, BlockInput2.txt)
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





<a name="js"></a>
# JOKESERVER PROJECT:

Summary: Developed a pair of multi-threaded servers that accept input from multiple clients. A client connecting to the server will 
receive either jokes or proverbs depending on what state the server is currently in (Proverb or Jokes). 
In addition to the basic client-server model, I implemented a secondary administration channel to my server to control whether 
the server will be in Proverb state or Joke state, and manually maintain the state of all conversations between a server and a client 
within my distributed application.

List of files needed to run program:
	a. JokeServer.java
	b. JokeClient.java
	c. JokeClientAdmin.java

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








<a name="mrs"></a>
# MOVIE RENTAL SYSTEM PROJECT:

Summary: Developed a movie rental system that allowed the user to perform actions on a simple movie database, such as 'add movie', 
'delete movie', 'view available rentals', etc. The purpose of this project was to incorporate different design patterns 
that would help make a code maintainable and flexible. The design patterns I used were  Template design pattern, Factory pattern, 
State Pattern, and Command Pattern (Book: HeadFirst Design Patterns by Eric Freeman 1st Edition 2008). Unit tests were also added in 
Main folder. 

Tested on Windows 10 using Eclipse IDE 2019-12

Instructions to run this program: 
  1. Load up project on Eclipse
  2. Run code







<a name="tetris"></a>
# TETRIS PROJECT:

Summary: Developed the back-end and manipulated the front-end for the game Tetris with 11 other students. Both C# and C++ were used. 

List of files needed to run the program:
	a. Workload extension for Visual Studios 'Desktop Development with C++'
  b. Workload extension for Visual Studios '.NET Desktop Development'

Tested on Windows 10 using Visual Studios Enterprise 2019 version 16.6.1

Instructions to run this program: 
  1. Double-click CleanAll.Bat file to remove files that were produced from previous build
  2. Double-click Tetris.sln to open the project on Visual Studios and click Start at the top of the menu
  3. Build solution (upper left) 
  4. Start 

