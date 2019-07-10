package it.unisa.adc.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import net.tomp2p.peers.PeerAddress;

public class Challenge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<PeerAddress> gamers ;
	private PeerAddress leader;
	private HashMap<PeerAddress, Integer> gamers_selection ;
	private String _room_game ;
	private Integer choosed;
	
	
	public Challenge(String _room_game, PeerAddress leader, Integer choosed){ 
		
		
		this.choosed = choosed;
		this._room_game = _room_game;
		this.leader = leader;
		this.gamers = new ArrayList<PeerAddress>();
		this.gamers_selection = new HashMap<PeerAddress, Integer>();
		
	}
	
	
	public ArrayList<PeerAddress> getGamers() {
		return gamers;
	}






	public void setGamers(ArrayList<PeerAddress> gamers) {
		this.gamers = gamers;
	}






	public PeerAddress getLeader() {
		return leader;
	}





	public void setLeader(PeerAddress leader) {
		this.leader = leader;
	}






	public HashMap<PeerAddress, Integer> getGamers_selection() {
		return gamers_selection;
	}






	public void setGamers_selection(HashMap<PeerAddress, Integer> gamers_selection) {
		this.gamers_selection = gamers_selection;
	}






	public String get_room_game() {
		return _room_game;
	}






	public void set_room_game(String _room_game) {
		this._room_game = _room_game;
	}


	




	public Integer getChoosed() {
		return choosed;
	}


	public void setChoosed(Integer choosed) {
		this.choosed = choosed;
	}


	public PeerAddress addGamer(PeerAddress peerAddress , Integer selection){
		
		gamers.add(peerAddress);
		gamers_selection.put(peerAddress,selection);
		
		return peerAddress;
		
		
		
	}
	
	
}
