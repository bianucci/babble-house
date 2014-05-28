package com.ffh.babblehouse.controller.BBNodes;
import java.io.InputStream;
import java.io.ObjectInputStream;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class Connector implements Iconnector{
String PortName;
private int Baudrate;
private int Stopbits;
private int Databits;
private int Parity_none;
private SerialPort serialPort;

public Connector( int Baudrate,int Stopbits,int Databits,int Parity_none){
	this.Baudrate=Baudrate;
	this.Databits= Databits;
	this.Stopbits=Stopbits;
	this.Parity_none= Parity_none;
}
	
// return available port 	
private void AvailblePort(){
		
	String[] portNames = SerialPortList.getPortNames();
    for(int i = 0; i < portNames.length; i++){
       
        PortName=portNames[i];
    }	
    
	}
// open com port for connection 
public SerialPort PortConnection(){
	AvailblePort();  // set availble port
	 serialPort = new SerialPort(PortName);
	
	 try { 
        	
        // open port if its closed
        	if(serialPort.closePort()){
        		 
            serialPort.openPort();//Open serial port
        	
          //serialPort.setParams(9600, 8, 1, 0);//Set params.
        	}
          serialPort.setParams(Baudrate, Databits, Stopbits, Parity_none, false, true);
        	
}catch (SerialPortException ex) {
	System.out.println(ex);
}
  return serialPort;      
	
}


}




