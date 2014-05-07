package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.data.Message;

public class Reader extends Thread implements IReader {

	IGateway gateway;
	public Reader(IGateway gateway){
		this.gateway=gateway;
	}
	
	@Override
	public Message readMessage() {
	
		return null;
	}
	
	
	private Message processMsg(){
		
		// message type
		
		int b100;
		
	byte[] readbyte=	gateway.comListner();
	String readybyte= readbyte.toString();
	int Msg_type;
	 int Zid_sender=readybyte.substring(0,3);
	 int Zid_receive;
	 String Payload;
	
	}
	
	

}
