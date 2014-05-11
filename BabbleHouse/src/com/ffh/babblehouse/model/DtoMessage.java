package com.ffh.babblehouse.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class DtoMessage{
	@Id 
	@GeneratedValue
	protected int id;
	
	private transient int Msg_type;
	private transient int Zid_sender;
	private transient int Zid_receive;
	private transient String Payload;
	
	public DtoMessage(int msg_type, int  zid_sender,
			int  zid_receive, String payload) {
		super();
		Msg_type = msg_type;
		Zid_sender = zid_sender;
		Zid_receive = zid_receive;
		Payload = payload;
	}
	
	public int getId() { 
		return id; 
	}
	
	public int getMsg_type() {
		return Msg_type;
	}
	public int getZid_sender() {
		return Zid_sender;
	}
	public int getZid_receive() {
		return Zid_receive;
	}
	public String getPayload() {
		return Payload;
	}

}
