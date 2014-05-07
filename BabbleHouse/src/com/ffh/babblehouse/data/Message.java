package com.ffh.babblehouse.data;

public class Message {
	private transient int Msg_type;
	private transient int Zid_sender;
	private transient int Zid_receive;
	private transient String Payload;
	public Message(int msg_type, int  zid_sender,
			int  zid_receive, String payload) {
		super();
		Msg_type = msg_type;
		Zid_sender = zid_sender;
		Zid_receive = zid_receive;
		Payload = payload;
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
