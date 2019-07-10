package it.unisa.adc.chat;




import org.junit.Test;

import it.unisa.adc.chat.*;
import junit.framework.TestCase;


public class AnonymousChatTest extends TestCase {


	private AnonymousChatImpl peer0,peer1, peer2,peer3;
	private MessageListenerImpl listener0,listener1, listener2,listener3;



	@Test
	public void testMethods() throws Exception {

		listener0 = new MessageListenerImpl(0);
		listener1 = new MessageListenerImpl(1); 
		listener2 = new MessageListenerImpl(2);	
		listener3 = new MessageListenerImpl(3);

		peer0 = new AnonymousChatImpl(0, "127.0.0.1", listener0);
		peer1 = new AnonymousChatImpl(1, "127.0.0.1", listener1);
		peer2 = new AnonymousChatImpl(2, "127.0.0.1", listener2);
		peer3 = new AnonymousChatImpl(3, "127.0.0.1", listener3);

		String room = "Unisa";
		String msg = "Hello, guys!";
		Integer valueGame = 3;

		createRoomTest(room);
		joinRoomTest(room);
		sendMessageTest(room, msg);
		leaveRoomTest(room);
		leaveNetworkTest(room);
		createChallengeTest(room, valueGame);
		joinChallengeTest(room, valueGame);
		sendMessageChallangeTest(room, msg);
		leaveGameTest(room);

	}

	public void createRoomTest(String room) {


		assertEquals(true, peer0.createRoom(room)); //room created -> true
		assertEquals(false, peer0.createRoom(room)); //room already created -> false
	}

	public void joinRoomTest(String room) {

		assertEquals(true,peer0.joinRoom(room) ); //join for peer true
		assertEquals(true,peer1.joinRoom(room) );
		assertEquals(true,peer2.joinRoom(room) );
		assertEquals(true,peer3.joinRoom(room) );
		assertEquals(false,peer0.joinRoom(room)); //join for peer false, he's already in room	
	}

	public void leaveRoomTest(String room) {
		assertEquals(true, peer0.leaveRoom(room)); //leave peer true
		assertEquals(false, peer0.leaveRoom(room)); //leave peer false -> peer has already leave the room
	}

	public void sendMessageTest(String room, String msg) {
		assertEquals(true, peer2.sendMessage(room,msg));   //true because peer2 in in the room
		assertEquals(false, peer1.sendMessage("Home",msg)); //false because the room doesn't exist
		
	}

	public void leaveNetworkTest(String room) {
		assertEquals(true, peer2.leaveNetwork()); // peer leave network

	}


	public void createChallengeTest(String room, Integer valueGame){

		assertEquals(true, peer3.createChallenge(room, valueGame));  // a peer in the room can create challenge
		assertEquals(false, peer0.createChallenge(room, valueGame)); // peer not in the room cannot create a challenge
	}

	public void joinChallengeTest(String room, Integer valueTrue){
		assertEquals(true, peer2.joinChallenge(room, valueTrue)); //peer true because is in a room 
		assertEquals(false, peer0.joinChallenge(room, 6)); //peer false because is not in a room 

	}

	public void verifyWinTest(String room){
		assertEquals(true, peer2.verifyWin(room)); // true because value of peer2 is the correct one
		assertEquals(false, peer3.verifyWin(room)); // false because peer3 doesn't guess the number
	}


	public void sendMessageChallangeTest(String room, String msg){

		assertEquals(true, peer2.sendChallengeMessage(room, msg)); //peer who play will send message about its game's result 


	}

	public void leaveGameTest(String room){
		assertEquals(true, peer2.leaveGame(room));   //leave peer Challenge true
		assertEquals(false, peer0.leaveGame(room));  //leave peer false -> peer has already leave the room
	}







}
