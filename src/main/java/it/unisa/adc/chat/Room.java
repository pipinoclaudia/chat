package it.unisa.adc.chat;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;


import net.tomp2p.peers.PeerAddress;

public class Room implements Serializable {
	
	private static final long serialVersionUID = 818810810051412014L;
	private String name;
	private HashSet<PeerAddress> peers;
	
	public Room(String name) {
		this.name=name;
		peers=new HashSet<PeerAddress>();
	}

	public HashSet<PeerAddress> getPeers() {
		return peers;
	}
	
	public void setPeers(HashSet<PeerAddress> peers) {
		this.peers = peers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPeer(PeerAddress p) {
		this.peers.add(p);
	}
	
	public boolean removePeer(PeerAddress p) {
		return peers.remove(p);
	}


}
