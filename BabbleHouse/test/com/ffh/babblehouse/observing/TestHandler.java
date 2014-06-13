package com.ffh.babblehouse.observing;

import jssc.SerialPort;

import com.ffh.babblehouse.controller.BBNodes.Connector;
import com.ffh.babblehouse.controller.BBNodes.Receiver;
import com.ffh.babblehouse.controller.BBNodes.ServiceGroupQueue;
import com.ffh.babblehouse.controller.BusinessObjects.ExampleStateChangedHandler;
import com.ffh.babblehouse.model.DtoServiceGroup;

public class TestHandler {

	public static void main(String[] args) {
		// TODO Auto-generated method s
		Connector newConnector = Connector.getInstance();


		SerialPort serialPort = newConnector.getserialPort();
		
		 //Sender newSender = new Sender(serialPort);
	//	newSender.SenderMessage(1222, 34);
		/*
		 * Receiver reader = new Receiver(); reader.serialPort.openPort();
		 * reader.start(); reader.serialPort.setParams(38400, 8, 1, 0);
		 */

		Receiver newReceiver = new Receiver(
				serialPort);
		newReceiver.start();
		ExampleStateChangedHandler newObjectHandler= new ExampleStateChangedHandler(newReceiver);
		
		// get service group list

		ServiceGroupQueue 	newServiceGroupQueue= ServiceGroupQueue.getInstance();
		for (DtoServiceGroup dtoServiceGroup : newServiceGroupQueue.getDtoServiceGroupList()){
			System.out.println("Beacon Message received");
			System.out.println(dtoServiceGroup.getName());
			System.out.println(dtoServiceGroup.getSensors().size()+"-------"+"number of sensor");
		}

	}

}
