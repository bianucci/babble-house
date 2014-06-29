package com.ffh.babblehouse.controller.BBNodes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.model.DtoDevice;

public class ServiceMsgCreator implements IBBDataBridge {
	private UARTMessage uartMessage;
	private ISender sender;

	Thread asyncSender = new Thread() {
		public void run() {
			while (true) {
				int size = queue.size();
				if (size > 0) {
					createServiceMsg(queue.get(0)); //FIFO
					queue.remove(0);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	};

	private List<DtoDevice> queue = new CopyOnWriteArrayList<DtoDevice>();

	public ServiceMsgCreator(ISender sender) {
		this.sender = sender;
	}

	private void createServiceMsg(DtoDevice d) {
		Builder serviceBuilder = Service.newBuilder();
		serviceBuilder.setInfo("" + System.currentTimeMillis());
		serviceBuilder.setServiceId(d.getId());
		serviceBuilder.setServiceType(ServiceType.ACTUATOR);
		serviceBuilder.setServiceGroupId(d.getDtoServiceGroup().getId());
		serviceBuilder.setValue(d.getLatestValue().getValue());
		Service service = serviceBuilder.build();
		uartMessage = UARTMessage.newBuilder().setType(Type.SERVICE)
				.setService(service).build();
		try {
			sender.sendMessage(uartMessage);
			System.out.println(uartMessage);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void changeDeviceStatus(DtoDevice dtoDevice) {
		if (!asyncSender.isAlive()) {
			asyncSender.start();
		}
		queue.add(dtoDevice);
		System.out.println(dtoDevice);
	}
}
