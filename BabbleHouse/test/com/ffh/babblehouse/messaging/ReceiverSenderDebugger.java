package com.ffh.babblehouse.messaging;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jssc.SerialPort;
import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.Service.ServiceType;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage;
import com.ffh.babblehouse.controller.BBNodes.UARTMessageProtos.UARTMessage.Type;
import com.google.protobuf.InvalidProtocolBufferException;

public class ReceiverSenderDebugger {

	private class Receiver extends Thread {

		String s = "APP_INIT_UARTAPP_INIT_SYSTEMAPP_START_NETWORKJOINE DNETWORKAPP_INIT_"
				+ "ENDPOINTAPP_IDLEDATA_INAPP_ZGBE_RCVDAPP_UART_SEND\n\r\r\nAPP_SEND_SE"
				+ "RVICE_RESPONSE cBL assembleUartMessage UTTP ZB_SENT_DATA_FAILED  iAct "
				+ "ACT1OFF ACT1ON APP_SEND_SERVICE_REQST APP_UART_RCVD APP_READ_ADC vn rtviowsex";
		private SerialPort serialPort;
		private JTextArea receivedUART;

		public Receiver(SerialPort serialPort, JTextArea receivedUART) {
			super();
			this.serialPort = serialPort;
			this.receivedUART = receivedUART;
		}

		@Override
		public void run() {
			super.run();
			for (;;) {
				try {
					receive();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void receive() throws SerialPortException {
			byte[] buffer = null;
			buffer = serialPort.readBytes(1);
			String string = new String(buffer);
			if (s.contains(string)) {
				receivedUART.append(string);
			} else {
				int size = (int) buffer[0];
				if (size != 0) {
					receivedUART.append(size + "");
					byte[] isRead = serialPort.readBytes(size);
					UARTMessage m = null;
					try {
						m = UARTMessageProtos.UARTMessage.parseFrom(isRead);
					} catch (InvalidProtocolBufferException e) {
						System.out.println(new String(buffer));
					}
					if (m != null)
						receivedUART.append(m.toString());
				}
			}
		}
	}

	public ReceiverSenderDebugger(final SerialPort serialPort, boolean isSender) {
		JFrame frame = new JFrame(serialPort.getPortName());
		JTextArea textArea = new JTextArea(99, 1);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane scrollPane = new JScrollPane(textArea);

		frame.setLayout(new BorderLayout(10, 10));

		if (isSender) {
			JButton jButton = new JButton("SEND");
			jButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					sendMessage(serialPort);
				}
			});
			frame.add(jButton, BorderLayout.NORTH);
		}
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.setBounds(100, 100, 400, 600);
		frame.setVisible(true);

		Receiver r = new Receiver(serialPort, textArea);
		r.start();
	}

	public static void sendMessage(SerialPort serialPort) {
		int value = (Math.random() > 0.5) ? 1 : 0;
		int sId = (int) (Math.random() * 3) + 1;

		Service service = Service.newBuilder()
				.setInfo("" + System.currentTimeMillis()).setServiceId(sId)
				.setServiceType(ServiceType.ACTUATOR).setServiceGroupId(1)
				.setValue(value).build();

		UARTMessage uartMessage = UARTMessage.newBuilder()
				.setType(Type.SERVICE).setService(service).build();

		byte[] message = uartMessage.toByteArray();
		int length = message.length;
		try {
			serialPort.writeByte((byte) length);
			serialPort.writeBytes(message);

			System.err.println("SENT MESSAGE:" + service.getInfo());
			System.err.println("Size " + length);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
			throws InvalidProtocolBufferException, SerialPortException {
		SerialPort port3 = new SerialPort("COM3");
		port3.openPort();// Open serial port
		port3.setParams(38400, 8, 1, 0);// Set params.
		new ReceiverSenderDebugger(port3, true);

		SerialPort port4 = new SerialPort("COM4");
		port4.openPort();// Open serial port
		port4.setParams(38400, 8, 1, 0);// Set params.
		new ReceiverSenderDebugger(port4, false);
	}
}
