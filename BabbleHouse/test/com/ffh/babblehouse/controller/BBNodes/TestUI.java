package com.ffh.babblehouse.controller.BBNodes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jssc.SerialPortException;

import com.ffh.babblehouse.controller.BusinessObjects.IBoStateChangedHandler;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.ffh.babblehouse.model.DtoValue;

public class TestUI extends JFrame {
	private static final JTextField SG_ID = new JTextField();
	private static final JTextField S_ID = new JTextField();
	private static final JTextField VAL = new JTextField();

	private static final long serialVersionUID = 5113673743020770296L;

	List<DtoUDR> userDefinedRules = new ArrayList<DtoUDR>();

	public TestUI(String title) throws HeadlessException, SerialPortException {
		super(title);

		UARTConnector connector = new UARTConnector();
		Receiver receiver = new Receiver(connector.getserialPort());
		Sender s = new Sender(connector.getserialPort());
		ServiceMsgCreator c = new ServiceMsgCreator(s);

		TextAreaPrinter o = new TextAreaPrinter();

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout(10, 10));

		contentPane.add(getBeaconTextArea(o), BorderLayout.WEST);
		contentPane.add(getMessageSenderPane(c), BorderLayout.NORTH);
		contentPane.add(getUDRPane(), BorderLayout.CENTER);
		contentPane.add(getMessageReceivedTextArea(o), BorderLayout.SOUTH);

		receiver.registerChangeHandler(o);
		receiver.start();
	}

	private Component getMessageReceivedTextArea(TextAreaPrinter o) {
		JTextArea textArea = new JTextArea(10, 1);
		textArea.setAutoscrolls(true);
		textArea.setRows(4);
		o.setServiceReceivedArea(textArea);
		return new JScrollPane(textArea);
	}

	private Component getUDRPane() {
		JPanel p = new JPanel(new GridLayout(12, 1, 10, 10));

		Container udrColLabels = new JPanel(new GridLayout(1, 8, 10, 10));
		udrColLabels.add(new JLabel("lessMin"));
		udrColLabels.add(new JLabel("Min"));
		udrColLabels.add(new JLabel("Max"));
		udrColLabels.add(new JLabel("greatMax"));
		udrColLabels.add(new JLabel("SGID_S"));
		udrColLabels.add(new JLabel("Sensor"));
		udrColLabels.add(new JLabel("SGID_A"));
		udrColLabels.add(new JLabel("Actuator"));
		p.add(udrColLabels);

		for (int i = 0; i < 10; i++) {
			Container udrConfPane = new JPanel(new GridLayout(1, 8, 10, 10));
			for (int j = 0; j < 8; j++) {
				udrConfPane.add(new JTextField());
			}
			p.add(udrConfPane);
		}
		p.add(new JButton("Save"));
		return p;
	}

	private Component getMessageSenderPane(final IBBDataBridge db) {

		JPanel p = new JPanel();
		JButton sendButton = new JButton("Send UARTMessage");
		p.add(sendButton);
		p.add(new JLabel("SrvcGrp Id: ", JLabel.RIGHT));
		p.add(SG_ID);
		p.add(new JLabel("Service Id: ", JLabel.RIGHT));
		p.add(S_ID);
		p.add(new JLabel("Value: ", JLabel.RIGHT));
		p.add(VAL);
		p.setLayout(new GridLayout(1, 7));

		sendButton.addActionListener(new ActionListener() {
			DtoDevice dtoDevice = new DtoDevice() {
				public DtoValue getLatestValue() {
					DtoValue value = new DtoValue();
					value.setValue(Integer.valueOf(VAL.getText()));
					return value;
				};
				public int getId() {
					return Integer.valueOf(S_ID.getText());
				};
				public DtoServiceGroup getDtoServiceGroup() {
					DtoServiceGroup sg = new DtoServiceGroup();
					sg.setId(Integer.valueOf(SG_ID.getText()));
					return sg;
				};
			};
			@Override
			public void actionPerformed(ActionEvent e) {
				db.changeDeviceStatus(dtoDevice);
			}
		});

		return p;
	}

	private Component getBeaconTextArea(TextAreaPrinter o) {
		JTextArea textArea = new JTextArea(1, 20);
		o.setBeaconReeivedArea(textArea);
		return new JScrollPane(textArea);
	}

	public static void main(String[] args) throws HeadlessException,
			SerialPortException {
		TestUI ui = new TestUI("ZigBee Test UI");
		ui.setSize(800, 600);
		ui.setVisible(true);
	}

	private class TextAreaPrinter implements IBoStateChangedHandler {

		JTextArea serviceReceivedArea;
		JTextArea beaconReeivedArea;

		@Override
		public void sensorDataChanged(DtoSensor updatedDtoSensor) {
			serviceReceivedArea.append(updatedDtoSensor.toString());
			serviceReceivedArea.append("\n");
		}

		@Override
		public void deviceDataChanged(DtoDevice updatedDtoDevice) {
			serviceReceivedArea.append(updatedDtoDevice.toString());
			serviceReceivedArea.append("\n");
		}

		@Override
		public void newServiceGroupArrived(DtoServiceGroup newServiceGroup) {
			beaconReeivedArea.append(newServiceGroup.asFancyString());
			beaconReeivedArea.append("\n");
		}

		public void setBeaconReeivedArea(JTextArea beaconReeivedArea) {
			this.beaconReeivedArea = beaconReeivedArea;
		}

		public void setServiceReceivedArea(JTextArea serviceReceivedArea) {
			this.serviceReceivedArea = serviceReceivedArea;
		}
	}
}
