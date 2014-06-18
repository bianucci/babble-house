package com.ffh.babblehouse.view;

import java.util.List;

import com.ffh.babblehouse.controller.BusinessObjects.BoUDR;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class AddUserDefinedRuleComponent extends CustomComponent {

	//region AutoGenerated variables
	
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private GridLayout mainLayout;
	@AutoGenerated
	private ComboBox deviceComboBox;
	@AutoGenerated
	private Label deviceLabel;
	@AutoGenerated
	private ComboBox greaterThanMaximumComboBox;
	@AutoGenerated
	private Label greaterThanMaximumLabel;
	@AutoGenerated
	private ComboBox smallerThanMinimumComboBox;
	@AutoGenerated
	private Label smallerThanMinimumLabel;
	@AutoGenerated
	private TextField maximumValueTextField;
	@AutoGenerated
	private Label maximumValueLabel;
	@AutoGenerated
	private TextField minimumValueTextField;
	@AutoGenerated
	private Label minimumValueLabel;
	@AutoGenerated
	private ComboBox sensorComboBox;
	@AutoGenerated
	private Label SensorLabel;
	//endregion AutoGenerated variables
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 * @param serviceGroupList 
	 */
	List<DtoServiceGroup> serviceGroupList;
	BoUDR boUDR = new BoUDR();
	
	public AddUserDefinedRuleComponent(List<DtoServiceGroup> serviceGroupList) {

		this.serviceGroupList = serviceGroupList;
		
		buildMainLayout();
		setCompositionRoot(mainLayout);

		this.setSizeUndefined();
		
		fillComboBoxes();
	}

	public DtoUDR saveData() {
		try {
		
			int minValue = Integer.parseInt(minimumValueTextField.getValue());
			int maxValue = Integer.parseInt(maximumValueTextField.getValue());
			int sensorComboBoxId = (Integer) sensorComboBox.getValue();
			int deviceComboBoxId = (Integer) deviceComboBox.getValue();
			int lessMinState = smallerThanMinimumComboBox.getValue().equals("Activate") ? 1 : 0;
			int greatMaxState = greaterThanMaximumComboBox.getValue().equals("Activate") ? 1 : 0;
		
			
			DtoUDR dtoUDR = new DtoUDR();
			
			for(DtoServiceGroup dtoServiceGroup : serviceGroupList){
				
				for(DtoSensor dtoSensor : dtoServiceGroup.getSensors())
					if(dtoSensor.getId() != sensorComboBoxId)
						continue;
					else
						dtoUDR.setDtoSensor(dtoSensor);

				for(DtoDevice dtoDevice : dtoServiceGroup.getDevices())
					if(dtoDevice.getId() != deviceComboBoxId)
						continue;
					else
						dtoUDR.setDtoDevice(dtoDevice);
			}
						
			dtoUDR.setMinValue(minValue);
			dtoUDR.setMaxValue(maxValue );
			dtoUDR.setLessMinState(lessMinState);
			dtoUDR.setGreatMaxState(greatMaxState);
			
			boUDR.SaveOrUpdate(dtoUDR);
			
			Notification.show("Rule added successfully.", Type.TRAY_NOTIFICATION);
			return dtoUDR;
			
		} catch (NullPointerException e) {

			Notification.show("Please select sensor and actuator", Type.ERROR_MESSAGE);
		} catch (NumberFormatException e) {

			Notification.show("Minimum and maximum values must be filled with numbers", Type.ERROR_MESSAGE);
		}
		return null;
	}
	
	private void fillComboBoxes() {
		//greaterThanMaximumComboBox.setReadOnly(true);
		greaterThanMaximumComboBox.addItem("Activate");
		greaterThanMaximumComboBox.addItem("Deactivate");
		greaterThanMaximumComboBox.setValue("Activate");

		//smallerThanMinimumComboBox.setReadOnly(true);
		smallerThanMinimumComboBox.addItem("Activate");
		smallerThanMinimumComboBox.addItem("Deactivate");
		smallerThanMinimumComboBox.setValue("Deactivate");
		
		for(DtoServiceGroup dtoServiceGroup : serviceGroupList){
			List<DtoSensor> sensorList = dtoServiceGroup.getSensors();
			List<DtoDevice> deviceList = dtoServiceGroup.getDevices();
			
			for(DtoSensor dtoSensor :  sensorList){
				sensorComboBox.addItem( dtoSensor.getId() );
				sensorComboBox.setItemCaption( dtoSensor.getId(), dtoSensor.getSensorName() );
			}
			
			for(DtoDevice dtoDevice : deviceList){
				deviceComboBox.addItem(dtoDevice.getId());
				deviceComboBox.setItemCaption( dtoDevice.getId(), dtoDevice.getDeviceName() );
			}
		}
	}

	@AutoGenerated
	private GridLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new GridLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setColumns(2);
		mainLayout.setRows(6);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// SensorLabel
		SensorLabel = new Label();
		SensorLabel.setImmediate(false);
		SensorLabel.setWidth("-1px");
		SensorLabel.setHeight("-1px");
		SensorLabel.setValue("Sensor");
		mainLayout.addComponent(SensorLabel, 0, 0);
		mainLayout.setComponentAlignment(SensorLabel, new Alignment(48));
		
		// sensorComboBox
		sensorComboBox = new ComboBox();
		sensorComboBox.setImmediate(false);
		sensorComboBox.setWidth("-1px");
		sensorComboBox.setHeight("-1px");
		mainLayout.addComponent(sensorComboBox, 1, 0);
		mainLayout.setComponentAlignment(sensorComboBox, new Alignment(33));
		
		// minimumValueLabel
		minimumValueLabel = new Label();
		minimumValueLabel.setImmediate(false);
		minimumValueLabel.setWidth("-1px");
		minimumValueLabel.setHeight("-1px");
		minimumValueLabel.setValue("Minimum Value");
		mainLayout.addComponent(minimumValueLabel, 0, 1);
		mainLayout.setComponentAlignment(minimumValueLabel, new Alignment(48));
		
		// minimumValueTextField
		minimumValueTextField = new TextField();
		minimumValueTextField.setImmediate(false);
		minimumValueTextField.setWidth("-1px");
		minimumValueTextField.setHeight("-1px");
		minimumValueTextField.setMaxLength(3);
		mainLayout.addComponent(minimumValueTextField, 1, 1);
		mainLayout.setComponentAlignment(minimumValueTextField, new Alignment(
				33));
		
		// maximumValueLabel
		maximumValueLabel = new Label();
		maximumValueLabel.setImmediate(false);
		maximumValueLabel.setWidth("-1px");
		maximumValueLabel.setHeight("-1px");
		maximumValueLabel.setValue("Maximum Value");
		mainLayout.addComponent(maximumValueLabel, 0, 2);
		mainLayout.setComponentAlignment(maximumValueLabel, new Alignment(48));
		
		// maximumValueTextField
		maximumValueTextField = new TextField();
		maximumValueTextField.setImmediate(false);
		maximumValueTextField.setWidth("-1px");
		maximumValueTextField.setHeight("-1px");
		maximumValueTextField.setMaxLength(3);
		mainLayout.addComponent(maximumValueTextField, 1, 2);
		mainLayout.setComponentAlignment(maximumValueTextField, new Alignment(
				33));
		
		// smallerThanMinimumLabel
		smallerThanMinimumLabel = new Label();
		smallerThanMinimumLabel.setImmediate(false);
		smallerThanMinimumLabel.setWidth("-1px");
		smallerThanMinimumLabel.setHeight("-1px");
		smallerThanMinimumLabel.setValue("When smaller than minimum");
		mainLayout.addComponent(smallerThanMinimumLabel, 0, 3);
		mainLayout.setComponentAlignment(smallerThanMinimumLabel,
				new Alignment(48));
		
		// smallerThanMinimumComboBox
		smallerThanMinimumComboBox = new ComboBox();
		smallerThanMinimumComboBox.setImmediate(false);
		smallerThanMinimumComboBox.setWidth("-1px");
		smallerThanMinimumComboBox.setHeight("-1px");
		mainLayout.addComponent(smallerThanMinimumComboBox, 1, 3);
		mainLayout.setComponentAlignment(smallerThanMinimumComboBox,
				new Alignment(33));
		
		// greaterThanMaximumLabel
		greaterThanMaximumLabel = new Label();
		greaterThanMaximumLabel.setImmediate(false);
		greaterThanMaximumLabel.setWidth("-1px");
		greaterThanMaximumLabel.setHeight("-1px");
		greaterThanMaximumLabel.setValue("When greater than maximum");
		mainLayout.addComponent(greaterThanMaximumLabel, 0, 4);
		mainLayout.setComponentAlignment(greaterThanMaximumLabel,
				new Alignment(48));
		
		// greaterThanMaximumComboBox
		greaterThanMaximumComboBox = new ComboBox();
		greaterThanMaximumComboBox.setImmediate(false);
		greaterThanMaximumComboBox.setWidth("-1px");
		greaterThanMaximumComboBox.setHeight("-1px");
		mainLayout.addComponent(greaterThanMaximumComboBox, 1, 4);
		mainLayout.setComponentAlignment(greaterThanMaximumComboBox,
				new Alignment(33));
		
		// deviceLabel
		deviceLabel = new Label();
		deviceLabel.setImmediate(false);
		deviceLabel.setWidth("-1px");
		deviceLabel.setHeight("-1px");
		deviceLabel.setValue("Device");
		mainLayout.addComponent(deviceLabel, 0, 5);
		mainLayout.setComponentAlignment(deviceLabel, new Alignment(48));
		
		// deviceComboBox
		deviceComboBox = new ComboBox();
		deviceComboBox.setImmediate(false);
		deviceComboBox.setWidth("-1px");
		deviceComboBox.setHeight("-1px");
		mainLayout.addComponent(deviceComboBox, 1, 5);
		mainLayout.setComponentAlignment(deviceComboBox, new Alignment(33));
		
		return mainLayout;
	}

}
