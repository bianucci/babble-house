package com.ffh.babblehouse.controller.BBNodes;
import java.util.List;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class GateWay implements IGateway  {
private int Baudrate;
private int Stopbits;
private int Databits;
private int Parity_none;


public GateWay(int baudrate, int stopbits, int databits, int parity_none) {
	super();
	this.Baudrate = baudrate;
	this.Stopbits = stopbits;
	this.Databits = databits;
	this.Parity_none = parity_none;
}




public String get_comport(){
	String commport="";
	String[] portNames = SerialPortList.getPortNames();
    for(int i = 0; i < portNames.length; i++){
        System.out.println(portNames[i]);
        commport=portNames[i];
    }
    return commport;
	
}

public byte[] comListner(){
	String comport=get_comport();
	SerialPort serialPort = new SerialPort(comport);
	byte[] buffer=new byte[0];
        try {
            serialPort.openPort();//Open serial port
          //serialPort.setParams(9600, 8, 1, 0);//Set params.
          serialPort.setParams(Baudrate, Stopbits, Databits, Parity_none, false, true);
            for(;;){
             buffer = serialPort.readBytes(10);//Read 10 bytes from serial port
           
            }
        }
        catch (SerialPortException ex) {
        	System.out.println(ex);
        }
        return buffer;
}



}
