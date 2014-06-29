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

	static int[] lastValues = new int[30];

	List<DtoUDR> userDefinedRules = new ArrayList<DtoUDR>();

	public TestUI(String title) throws HeadlessException, SerialPortException {
		super(title);

		UARTConnector connector = new UARTConnector();
		Receiver receiver = new Receiver(connector.getserialPort());
		Sender s = new Sender(connector.getserialPort());
		ServiceMsgCreator c = new ServiceMsgCreator(s);

		TextAreaPrinter o = new TextAreaPrinter(c);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout(10, 10));

		contentPane.add(getBeaconTextArea(o), BorderLayout.WEST);
		contentPane.add(getMessageSenderPane(c), BorderLayout.NORTH);
		contentPane.add(getUDRPane(), BorderLayout.CENTER);
		contentPane.add(getMessageReceivedTextArea(o), BorderLayout.SOUTH);

		receiver.registerChangeHandler(o);
		receiver.start();
	}

	private Component getUDRPane() {
		final JPanel p = new JPanel(new GridLayout(12, 1, 10, 10));

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
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				userDefinedRules = new ArrayList<DtoUDR>();

				for (int i = 1; i < 11; i++) { // iterate through rows
					Container rowPanel = (Container) p.getComponent(i);
					JTextField lessMin = (JTextField) rowPanel.getComponent(0);
					JTextField min = (JTextField) rowPanel.getComponent(1);
					JTextField max = (JTextField) rowPanel.getComponent(2);
					JTextField greatMax = (JTextField) rowPanel.getComponent(3);
					JTextField sgidS = (JTextField) rowPanel.getComponent(4);
					JTextField idS = (JTextField) rowPanel.getComponent(5);
					JTextField sgidA = (JTextField) rowPanel.getComponent(6);
					JTextField idA = (JTextField) rowPanel.getComponent(7);

					if (lessMin.getText().length() == 0
							|| min.getText().length() == 0
							|| max.getText().length() == 0
							|| greatMax.getText().length() == 0
							|| sgidS.getText().length() == 0
							|| idS.getText().length() == 0
							|| sgidA.getText().length() == 0
							|| idA.getText().length() == 0) {
						continue;
					}

					DtoUDR udr = new DtoUDR();

					DtoDevice dtoDevice = new DtoDevice();
					dtoDevice.setId(Integer.valueOf(idA.getText()));
					DtoServiceGroup sA = new DtoServiceGroup();
					sA.setId(Integer.valueOf(sgidA.getText()));
					dtoDevice.setDtoServiceGroup(sA);

					DtoSensor dtoSensor = new DtoSensor();
					dtoSensor.setId(Integer.valueOf(idS.getText()));
					DtoServiceGroup sS = new DtoServiceGroup();
					sS.setId(Integer.valueOf(sgidS.getText()));
					dtoSensor.setDtoServiceGroup(sS);

					udr.setDtoDevice(dtoDevice);
					udr.setDtoSensor(dtoSensor);
					udr.setGreatMaxState(Integer.valueOf(greatMax.getText()));
					udr.setMaxValue(Integer.valueOf(max.getText()));
					udr.setLessMinState(Integer.valueOf(lessMin.getText()));
					udr.setMinValue(Integer.valueOf(min.getText()));

					userDefinedRules.add(udr);
				}
			}

		});
		p.add(saveButton);
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

	private Component getMessageReceivedTextArea(TextAreaPrinter o) {
		JTextArea textArea = new JTextArea(10, 1);
		textArea.setAutoscrolls(true);
		textArea.setRows(4);
		o.setServiceReceivedArea(textArea);
		return new JScrollPane(textArea);
	}

	private class TextAreaPrinter implements IBoStateChangedHandler {

		JTextArea serviceReceivedArea;
		JTextArea beaconReeivedArea;
		private ServiceMsgCreator c;

		public TextAreaPrinter(ServiceMsgCreator c) {
			this.c = c;
		}

		@Override
		public void sensorDataChanged(DtoSensor s) {
			serviceReceivedArea.append(s.toString());
			serviceReceivedArea.append("\n");

			for (DtoUDR u : userDefinedRules) {
				DtoDevice dtoDevice = u.getDtoDevice();
				dtoDevice.setValues(new ArrayList<DtoValue>());
				DtoValue v = new DtoValue();
				dtoDevice.addValue(v);

				if (udrViolated(u, s)) {
					if (s.getLatestValue().getValue() > u.getMaxValue()) {
						v.setValue(u.getGreatMaxState());
					} else if (s.getLatestValue().getValue() < u.getMinValue()) {
						v.setValue(u.getLessMinState());
					}
					int sgId = dtoDevice.getDtoServiceGroup().getId();
					int aId = dtoDevice.getId();
					lastValues[sgId * 10 + aId] = v.getValue();
					c.changeDeviceStatus(dtoDevice);
				}
			}
		}

		@Override
		public void deviceDataChanged(DtoDevice d) {
			serviceReceivedArea.append(d.toString());
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

		public boolean udrViolated(DtoUDR udr, DtoSensor sensor) {

			int sgId = udr.getDtoSensor().getDtoServiceGroup().getId();
			int sId = udr.getDtoSensor().getId();
			int aId = udr.getDtoDevice().getId();

			if (sgId == sensor.getDtoServiceGroup().getId()) {
				if (sId == sensor.getId()) {
					int lastValue = lastValues[sgId * 10 + aId];

					int value = sensor.getLatestValue().getValue();
					if (value > udr.getMaxValue()) {
						if (lastValue != udr.getGreatMaxState()) {
							return true;
						}
					} else if (value < udr.getMinValue()) {
						if (lastValue != udr.getLessMinState()) {
							System.out.println(lastValue + " in min");
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		TestUI ui = new TestUI("ZigBee Test UI");
		ui.setSize(800, 600);
		ui.setVisible(true);
		ui.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
