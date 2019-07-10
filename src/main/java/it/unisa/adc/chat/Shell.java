package it.unisa.adc.chat;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class Shell {

	@Option(name="-m", aliases="--masterip", usage="the master peer ip address", required=true)
	private static String master;

	@Option(name="-id", aliases="--identifierpeer", usage="the unique identifier for this peer", required=true)
	private static int id;

	public static void main(String[] args) throws Exception {

		class MessageListenerImpl implements MessageListener{
			int peerid;

			public MessageListenerImpl(int peerid)
			{
				this.peerid=peerid;

			}
			public Object parseMessage(Object obj) {

				TextIO textIO = TextIoFactory.getTextIO();
				TextTerminal terminal = textIO.getTextTerminal();
				terminal.printf("\n"+peerid+"] (Direct Message Received) "+obj+"\n\n");
				return "success";
			}

		}

		Shell example = new Shell();
		final CmdLineParser parser = new CmdLineParser(example);  
		try  
		{  
			parser.parseArgument(args);  
			TextIO textIO = TextIoFactory.getTextIO();
			TextTerminal terminal = textIO.getTextTerminal();
			AnonymousChatImpl peer = new AnonymousChatImpl(id, master, new MessageListenerImpl(id));

			terminal.printf("\n Staring peer id: %d on master node: %s\n", id, master);
			while(true) {
				printMenu(terminal);

				int option = textIO.newIntInputReader().withMaxVal(7).withMinVal(1).read("Option");
				switch (option) {
				//1 - Create room
				case 1: terminal.printf("\n Insert room name \n");
				String name = textIO.newStringInputReader().withDefaultValue("default_room").read("Name:");
				if(peer.createRoom(name))
					terminal.printf("\n Room %s successfully created \n",name);
				else
					terminal.printf("\n Error in room creation \n");
				break;

				//2 - Join room	
				case 2: terminal.printf("\n Enter room name to join \n");
				String sname = textIO.newStringInputReader().withDefaultValue("default_room").read("Name:");

				if(peer.joinRoom(sname))
					terminal.printf("\n Successfully joined to %s\n",sname);
				else
					terminal.printf("\n Error joining room %s \n" , sname);
				break;


				// 3 - Send message	
				case 3: terminal.printf("\n Insert room named \n");
				String roomName = textIO.newStringInputReader().withDefaultValue("default_room").read(" Name:");
				terminal.printf("\n Enter message \n");
				String message = textIO.newStringInputReader().withDefaultValue("default_message").read(" Message:");
				if(peer.sendMessage(roomName,message))
					terminal.printf("\n Successfully sent message in room %s\n",roomName);
				else
					terminal.printf("\n Error sending message \n");
				break;


				// 4 - Leave room
				case 4: terminal.printf("\n Enter name room to leave \n");
				roomName = textIO.newStringInputReader().withDefaultValue("default_room").read("Name:");
				if(peer.leaveRoom(roomName))
					terminal.printf("\n Successfully exited to room %s\n",roomName);
				else
					terminal.printf("\n Error in leaving room \n");
				break;


				// 5 - Leave network
				case 5: terminal.printf("\n Are you sure to leave the network? \n");
				boolean exit = textIO.newBooleanInputReader().withDefaultValue(false).read("exit?");
				if(exit) {
					peer.leaveNetwork();
					System.exit(0);
				}


				// 6- Create Challenge	
				case 6: terminal.printf("\n Enter name room where you want start the challenge \n");
				roomName = textIO.newStringInputReader().withDefaultValue("default_room").read("Name:");

				terminal.printf("\n Enter a number from 1 to 10 and players will try to guess it. \n");
				int choosed = textIO.newIntInputReader().read("Number: ");
				if(peer.createChallenge(roomName, choosed))
					terminal.printf("\n Successfully created game to room %s\n",roomName);
				else
					terminal.printf("\n You can create just one Challenge! YOU HAVE ALREADY CREATED IT. \n");

				String messagePlayers = "A player has started a challenge in room "+roomName+". Press 7 to Join the challenge\n";
				if(peer.sendMessage(roomName,messagePlayers)){
					terminal.printf("\n Successfully sent message to all users of the room %s\n",roomName);
				}else
					terminal.printf("\n Error in sending message challenge  in the room \n");
				break;


				// 7 - Join challenge
				case 7: terminal.printf("\n Enter the name of the room to join the challenge\n");


				String snameChallenge = textIO.newStringInputReader().withDefaultValue("default_room").read("Name:");
				terminal.printf("\n Successfully joined challenge to %s , choose a number from 1 to 10\n",snameChallenge);
				Integer _guess = textIO.newIntInputReader().read("Number: ");
				if(peer.joinChallenge(snameChallenge, _guess)){

					terminal.printf("\n Successfully started game\n");
					if(peer.verifyWin(snameChallenge)){
						terminal.printf("\n Winner in the game of the room %s\n",snameChallenge);
						if(peer.sendChallengeMessage(snameChallenge,"Winner in the game of the room "+snameChallenge)){
							
							terminal.printf("\n Successfully sent message to all users of the room %s\n",snameChallenge);
							
							
						}else
							terminal.printf("\n Error in sending message challenge  in the room \n");
					}else{
						terminal.printf("\n Loser in the room %s \n", snameChallenge);
						if(peer.sendChallengeMessage(snameChallenge,"Loser in the game of the room "+snameChallenge)){
							terminal.printf("\n Successfully sent message to all users of the room %s\n",snameChallenge);
						}else
							terminal.printf("\n Error in sending message challenge  in the room \n");
					}
					
					
					if(peer.leaveRoom(snameChallenge))
						terminal.printf("\n Successfully exited to challenge in room %s\n",snameChallenge);
					else
						terminal.printf("\n Error in leaving challenge room \n");
				}else
					terminal.printf("\n Error in starting game\n");



				break;

				default : break;
				}




			}

		}  
		catch (CmdLineException clEx)  
		{  
			System.err.println("ERROR: Unable to parse command-line options: " + clEx);  
		}  


	}
	public static void printMenu(TextTerminal terminal) {
		terminal.printf("\n 1 - Create room \n ");
		terminal.printf("\n 2 - Join room \n");
		terminal.printf("\n 3 - Send message \n");
		terminal.printf("\n 4 - Leave room \n");
		terminal.printf("\n 5 - Leave network \n");
		terminal.printf("\n 6 - Start Challenge \n");
		terminal.printf("\n 7 - Join Challenge \n");
		
	}

}