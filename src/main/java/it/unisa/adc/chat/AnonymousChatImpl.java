package it.unisa.adc.chat;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDirect;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;

public class AnonymousChatImpl implements AnonymousChat {
	final private Peer peer;
	final private PeerDHT _dht;
	final private int DEFAULT_MASTER_PORT=4000;
	//private ArrayList<String> rooms;
	private HashMap<String, Room> rooms;
	private HashMap<String, Challenge> challanges;
	private Logger logger = Logger.getLogger("AnonymousChatImpl");
	


	public AnonymousChatImpl(int _id, String _master_peer,  final MessageListener _listener) throws Exception{
		//rooms = new ArrayList<String>();
		rooms = new HashMap<String, Room>();
		challanges = new HashMap<String, Challenge>();
		peer= new PeerBuilder(Number160.createHash(_id)).ports(DEFAULT_MASTER_PORT+_id).start();
		_dht = new PeerBuilderDHT(peer).start();	
		FutureBootstrap fb = peer.bootstrap().inetAddress(InetAddress.getByName(_master_peer)).ports(DEFAULT_MASTER_PORT).start();
		fb.awaitUninterruptibly();
		if(fb.isSuccess()) {
			peer.discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
		logger.addHandler(new FileHandler(System.getProperty("user.dir")+"\\log\\logger_"+_id+".log",true));
		logger.setUseParentHandlers(false);

		peer.objectDataReply(new ObjectDataReply()
		{
			public Object reply(PeerAddress sender, Object request) throws Exception {
			
				return _listener.parseMessage(request);
			}
		});
	}

	@Override
	public boolean createRoom(String _room_name) { 
		try {
			Room room = new Room(_room_name);
			FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				//rooms.add(_room_name);
				rooms.put(_room_name, new Room(_room_name));
				logger.info("room added "+_room_name);
				_dht.put(Number160.createHash(_room_name)).data(new Data(room)).start().awaitUninterruptibly();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean joinRoom(String _room_name) { 
		try {
			FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) 
					return false;

				Room room; 
				room = (Room) futureGet.dataMap().values().iterator().next().object();

				if(!room.getPeers().contains(peer.peerAddress())) {
					room.addPeer(peer.peerAddress());
					_dht.put(Number160.createHash(_room_name)).data(new Data(room)).start().awaitUninterruptibly();
					rooms.put(_room_name, room);
					return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean leaveRoom(String _room_name) { 

		try {
			FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty() ) return false;
				Room room = (Room) futureGet.dataMap().values().iterator().next().object();
				if(room.getPeers().contains(peer.peerAddress())) {
					room.removePeer(peer.peerAddress());
					_dht.put(Number160.createHash(_room_name)).data(new Data(room)).start().awaitListenersUninterruptibly();
					rooms.remove(_room_name);
					return true;
				}
				else
					return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean sendMessage(String _room_name,String _text_message) {
		try {

			
		
			FutureGet futureGet = _dht.get(Number160.createHash(_room_name)).start();
			futureGet.awaitUninterruptibly();
			Message mex= new Message(_room_name,_text_message);
			
			if (futureGet.isSuccess() && !futureGet.isEmpty()) {
			
				Room room = (Room) futureGet.dataMap().values().iterator().next().object();
				
				
				
			
				if(!room.getPeers().contains(peer.peerAddress())) 
					return false;
				
				if( room.getPeers().size()<2) {
					return false;
				} else {
					
					for(PeerAddress p: room.getPeers()) {
						
						if(!p.equals(peer.peerAddress())) {
						
							mex.setDestination(p);
							sendToPeer(mex,p);
						}
					}
					return true;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}return false;
		
	}

	public void sendToPeer(Message mex,PeerAddress destPeer) {
		FutureDirect futureDirect = _dht.peer().sendDirect(destPeer).object(mex).start();
		
		futureDirect.addListener(new BaseFutureAdapter<FutureDirect>() {
			public void operationComplete(FutureDirect future) throws Exception {
				if (future.isSuccess()) {
					logger.info("Send to "+mex.getDestination());
				}
			}
		});
		futureDirect.awaitUninterruptibly();
	}

	@Override
	public boolean leaveNetwork() {
		boolean flag = true;
		if(rooms.size()==0)
			return false;

		if(!flag)
			return false;
		else
			_dht.peer().announceShutdown().start().awaitUninterruptibly();
		return true;
	}


	// metodi aggiutivi

	public boolean createChallenge(String _room_name, Integer choosed) { 
		try {
			String _challenge_name = _room_name+"_challenge";
			Challenge challenge = new Challenge(_challenge_name, peer.peerAddress(), choosed);
			FutureGet futureGet = _dht.get(Number160.createHash(_challenge_name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess() && futureGet.isEmpty()) {
				//rooms.add(_room_name);
				challanges.put(_challenge_name, new Challenge(_room_name, peer.peerAddress(), choosed));
				logger.info("challange added "+_challenge_name);
				_dht.put(Number160.createHash(_challenge_name)).data(new Data(challenge)).start().awaitUninterruptibly();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean joinChallenge(String _room_name, Integer _value) { 
		try {
			
			
			FutureGet futureGetRoom = _dht.get(Number160.createHash(_room_name)).start();
			futureGetRoom.awaitUninterruptibly();
			Room room1; 
			room1 = (Room) futureGetRoom.dataMap().values().iterator().next().object();

			if(!room1.getPeers().contains(peer.peerAddress())) {
				return false;
			}
			 
			
			String _challenge_name = _room_name+"_challenge";
			FutureGet futureGet = _dht.get(Number160.createHash(_challenge_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) 
					return false;

				Challenge room; 
				room = (Challenge) futureGet.dataMap().values().iterator().next().object();

				if(!room.getGamers().contains(peer.peerAddress())) {
					room.getGamers().add(peer.peerAddress());
					room.getGamers_selection().put(peer.peerAddress(), _value);
					_dht.put(Number160.createHash(_challenge_name)).data(new Data(room)).start().awaitUninterruptibly();
			
					challanges.put(_challenge_name, room);
					return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean verifyWin(String _room_name) { 
		try {
			
			String _challenge_name = _room_name+"_challenge";
			
			FutureGet futureGet = _dht.get(Number160.createHash(_challenge_name)).start();
			futureGet.awaitUninterruptibly();

			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty()) 
					return false;

				Challenge room; 
				room = (Challenge) futureGet.dataMap().values().iterator().next().object();

				if(room.getGamers().contains(peer.peerAddress())) {
					Integer _value = room.getGamers_selection().get(peer.peerAddress());

					if(_value.equals(room.getChoosed()))
						return true;
				}else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean leaveGame(String _room_name) { 

		try {
			
			String _challenge_name = _room_name+"_challenge";
			FutureGet futureGet = _dht.get(Number160.createHash(_challenge_name)).start();
			futureGet.awaitUninterruptibly();
			if (futureGet.isSuccess()) {
				if(futureGet.isEmpty() ) return false;
				Challenge room = (Challenge) futureGet.dataMap().values().iterator().next().object();
				if(room.getGamers().contains(peer.peerAddress())) {
					room.getGamers().remove(peer.peerAddress());
					_dht.put(Number160.createHash(_challenge_name)).data(new Data(room)).start().awaitListenersUninterruptibly();
					challanges.remove(_challenge_name);
					return true;
				}
				else
					return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public boolean sendChallengeMessage(String _room_name, String _text_message) { 
		try {
			String _challenge_name = _room_name+"_challenge";


			FutureGet futureGet = _dht.get(Number160.createHash(_challenge_name)).start();
			futureGet.awaitUninterruptibly();
			Message mex= new Message(_challenge_name,_text_message);
			if (futureGet.isSuccess()) {



				Challenge room = (Challenge) futureGet.dataMap().values().iterator().next().object();
				if(!room.getGamers().contains(peer.peerAddress())) {
					return false;
				} else {
					for(PeerAddress p: room.getGamers()) {
						sendToPeer(mex,p);

					}
					sendToPeer(mex, room.getLeader());
					
					return true;
				}



			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}



}