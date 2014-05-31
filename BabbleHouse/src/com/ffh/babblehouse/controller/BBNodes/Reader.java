package com.ffh.babblehouse.controller.BBNodes;

import jssc.SerialPort;
import jssc.SerialPortException;



public class Reader  implements IReader {
	 Iconnector iconnector;
	private static  int FistTimebytesread=1;
	private int MessagebyteLength=0;
	private byte[] combufferInfo;
	private byte[] combuffer;
	SerialPort serialPort;
	public Reader(Iconnector iconnector){
		this.iconnector=iconnector;
		 serialPort= iconnector.PortConnection();
		
	}
	
	// read byte stream from the returned open com port
	
	

	public  int readComBufferFirstTime(){
		
	        String comdata="";
			
			try {
				combufferInfo = serialPort.readBytes(FistTimebytesread);
				comdata= combufferInfo.toString();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//Read 10 bytes from serial port

		
		return Integer.parseInt(comdata);
		
	}
	public byte[] readMessageBytes(){
		
		MessagebyteLength=readComBufferFirstTime();
		try {
			combuffer = serialPort.readBytes(MessagebyteLength);
			
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Read 10 bytes from serial port
		
		return combuffer;
	}
	// start thread 
	public int getLength(){
	return	MessagebyteLength ;
	}
	
}
