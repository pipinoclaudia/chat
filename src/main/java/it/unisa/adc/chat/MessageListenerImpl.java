package it.unisa.adc.chat;

public class MessageListenerImpl implements MessageListener {
	
	private int peerid;
	private Message message = null;
	private volatile boolean lastMessage = false;
	
	public MessageListenerImpl(int peerid)
	{
		this.peerid=peerid;
	}
	
	public Object parseMessage(Object obj) {
		this.message = (Message)obj;
		lastMessage = true;
		System.out.println(peerid+"(Direct Message Received) "+obj);
		return "success";
	}
	
	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}


	public boolean isLastMessage() {
		return lastMessage;
	}


	public void setLastMessage(boolean lastMessage) {
		this.lastMessage = lastMessage;
	}
	
}