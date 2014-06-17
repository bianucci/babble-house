package com.ffh.babblehouse.view;

import com.ffh.babblehouse.controller.BusinessObjects.BoDevice;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class GeneralInfoComponent extends CustomComponent {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Table table_1;
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	DtoServiceGroup dtoServiceGroup;
	
	BoDevice boDevice = new BoDevice();
	public enum TurnDevice{
		On,
		Off
	}
	
	public GeneralInfoComponent(DtoServiceGroup dtoServiceGroup){
		this.dtoServiceGroup = dtoServiceGroup; 
		
		buildMainLayout();
		fillView();
		setCompositionRoot(mainLayout);
	}
	
	private void fillView() {

		fillTableHeaders();
				
		fillTableBody();
	}

	private void refillTable(){
		table_1.removeAllItems();
		fillTableBody();
	}
	
	private void fillTableBody() {
		// The Table item identifier for the row.
		int itemId = 0; 
		
		// Fills table with sensors
		for(DtoSensor dtoSensor: dtoServiceGroup.getSensors())
			table_1.addItem(convertDtoSensorToArray(dtoSensor),itemId++);

		// Fills table with devices
		for(final DtoDevice dtoDevice: dtoServiceGroup.getDevices()){
			itemId++;
			Button button = getTurnOnOrOffButton(dtoDevice,itemId);
			table_1.addItem(convertDtoDeviceToArray(dtoDevice, button),itemId);
		}
	}

	private Object[] convertDtoDeviceToArray(final DtoDevice dtoDevice, Button button) {
		String status = "";
		if(dtoDevice.getLatestValue().getValue() == 1)
			status = "Active";
		else if(dtoDevice.getLatestValue().getValue() == 0)
			status = "Inactive";
		else 
			status = "ERROR OCCURED";
		
		return new Object[] {
				String.valueOf(dtoDevice.getId()), 
				dtoDevice.getDeviceName(),
				"Type : " + dtoDevice.getType().getName(),
				status, 
				button
		};
	}

	private Object[] convertDtoSensorToArray(DtoSensor dtoSensor) {
		return new Object[] {
				String.valueOf(dtoSensor.getId()), 
				dtoSensor.getSensorName(),
				"Unit : " + dtoSensor.getMeasuringUnit().getUnit_name(),
				String.valueOf(dtoSensor.getLatestValue().getValue()),
				null
		};
	}

	private Button getTurnOnOrOffButton(final DtoDevice dtoDevice, int itemId) {
		// Check the value of an Actuator if it's on or off
		if(dtoDevice.getLatestValue().getValue() == 1 ) {
			Button buttonAction = new Button("Turn off");
			buttonAction.addClickListener( getTurnOnOrOffEvent(dtoDevice, itemId, TurnDevice.Off) );
			return buttonAction;
		}
		else if(dtoDevice.getLatestValue().getValue() == 0){
			Button buttonAction = new Button("Turn on");
			buttonAction.addClickListener( getTurnOnOrOffEvent(dtoDevice, itemId, TurnDevice.On) );
			return buttonAction;
		} else 
			return null;
	}

	private ClickListener getTurnOnOrOffEvent(final DtoDevice dtoDevice, final int itemId, final TurnDevice turn) {
		return new Button.ClickListener() {
				public void buttonClick(ClickEvent event) {
			    	
			    	showTurningNotification(dtoDevice, turn);
			    	
			    	// Save change to DB
					if(turn == TurnDevice.On)
			    		boDevice.addDeviceValue(dtoDevice, 1);
					else if(turn == TurnDevice.Off)
						boDevice.addDeviceValue(dtoDevice, 0);
														
			    	// Repainting the table
			    	refillTable();
			    }


				private void showTurningNotification(final DtoDevice dtoDevice, final TurnDevice turn) {
					String message = "Turning " + dtoDevice.getDeviceName() + " ";
			    	if(turn == TurnDevice.On)
			    		message += "On";
			    	else if(turn == TurnDevice.Off)
			    		message += "Off";
			    	
			    	Notification.show(message);
				} 
		};
	}

	private void fillTableHeaders() {
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */
		table_1.addContainerProperty("Item id", String.class,  null);
		table_1.addContainerProperty("Item name", String.class,  null); //DtoServiceGroup/status
		table_1.addContainerProperty("Description",  String.class,  null); // DtoType -
		table_1.addContainerProperty("Status",       String.class, null); //<DtoValue> - status of actuator
		table_1.addContainerProperty("Action",       Button.class, null); // Switch on/off Button
	}
	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// table_1
		table_1 = new Table();
		table_1.setImmediate(false);
		table_1.setWidth("100.0%");
		table_1.setHeight("100.0%");
		mainLayout.addComponent(table_1,
				"top:20.0px;right:240.0px;bottom:101.0px;left:160.0px;");
		
		return mainLayout;
	}}
