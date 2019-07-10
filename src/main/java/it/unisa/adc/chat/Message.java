package it.unisa.adc.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

import net.tomp2p.peers.PeerAddress;

public class Message implements Serializable {

	private static final long serialVersionUID = -6785859773397922306L;
	private String nameRoom;
	private String message;
	private LocalDateTime dateOrigin;
	private PeerAddress destination;

	public Message(String nameRoom, String message) {
		super();
		this.nameRoom = nameRoom;
		this.message = message;
		this.destination = null;
		this.dateOrigin = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return dateOrigin + ":" + message;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (dateOrigin == null) {
			if (other.dateOrigin != null)
				return false;
		} else if (!dateOrigin.equals(other.dateOrigin))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (nameRoom == null) {
			if (other.nameRoom != null)
				return false;
		} else if (!nameRoom.equals(other.nameRoom))
			return false;
		return true;
	}

	public PeerAddress getDestination() {
		return destination;
	}

	public void setDestination(PeerAddress destination) {
		this.destination = destination;
	}

	public String getNameRoom() {
		return nameRoom;
	}

	public void setNameRoom(String nameRoom) {
		this.nameRoom = nameRoom;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDateOrigin() {
		return dateOrigin;
	}

	public void setDateOrigin(LocalDateTime dateOrigin) {
		this.dateOrigin = dateOrigin;
	}

}

