package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.Builder;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.ffh.babblehouse.model.DtoDevice;

public class ServiceMsgCreator implements IBBDataBridge{
	private int serviceGroupId;
	private ServiceType serviceType;
	private int serviceId;
	private int deviceStatus;
	private UARTMessage uartMessage;
	private Isender sender;
	
	public ServiceMsgCreator(Isender sender ){
		this.sender=sender;
		
	}
	private void createServiceMsg(){
		Builder serviceBuilder = Service.newBuilder();
		serviceBuilder.setInfo("" + System.currentTimeMillis());
		serviceBuilder.setServiceId(serviceId);
		serviceBuilder.setServiceType(serviceType);
		serviceBuilder.setServiceGroupId(serviceGroupId);
		serviceBuilder.setValue(deviceStatus);
		Service service = serviceBuilder.build();
		 uartMessage = UARTMessage.newBuilder()
				.setType(Type.SERVICE).setService(service).build();
		 sender.SenderMessage(uartMessage);

	}

	@Override
	public void changeDeviceStatus(DtoDevice dtoDevice) {
		this.deviceStatus=dtoDevice.getLatestValue().getValue();
		this.serviceId= dtoDevice.getId();
		this.serviceType=ServiceType.ACTUATOR;
		this.serviceGroupId=dtoDevice.getDtoServiceGroup().getId();
		createServiceMsg();
	}

}
