package com.ffh.babblehouse.view;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class GeneralInfo extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	DtoServiceGroup dtoServiceGroup;
	
	public GeneralInfo(DtoServiceGroup dtoServiceGroup){
		this.dtoServiceGroup = dtoServiceGroup; 
	}
	
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private Table table_1;
	public GeneralInfo() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
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
		
		
		/* Define the names and data types of columns.
		 * The "default value" parameter is meaningless here. */
		table_1.addContainerProperty("Service Group 1", String.class,  null); //DtoServiceGroup/status
		table_1.addContainerProperty("Type",  String.class,  null); // DtoType -
		table_1.addContainerProperty("Status",       String.class, null); //<DtoValue> - status of actuator
		
		 final ColumnGenerator generator = new ColumnGenerator() {
		        @Override
		        public Object generateCell(Table source, Object itemId,
		                Object columnId) {
		            return "off/on";
		        }
		 };
		    table_1.addGeneratedColumn("Change", generator);   
		    
		    /*Button button = new Button("editable", new ClickListener() {
		        @Override
		        public void buttonClick(ClickEvent event) {
		            table_1.setEditable(!table_1.isEditable());
		            if (table_1.isEditable())
		                table_1.removeGeneratedColumn("Change");
		            else
		                table_1.addGeneratedColumn("Change", generator);
		        }
		    });
		    mainLayout.addComponent(button);*/   
	    
		/* Add a few items in the table. */
	    // Create the table row.

		/* Add a few items in the table. */
		
		
		 // The Table item identifier for the row.
	    int itemId = 0;
	
		for(DtoSensor dtoSensor: dtoServiceGroup.getSensors())
			table_1.addItem(new Object[] {dtoSensor.getSensorName(),dtoSensor.getMeasuringUnit(),dtoSensor.getLatestValue()},itemId++);
		
		for(DtoDevice dtoDevice: dtoServiceGroup.getDevices())
			table_1.addItem(new Object[] {dtoDevice.getDeviceName(),dtoDevice.getType(),dtoDevice.getLatestValue()},itemId++);
		
		table_1.setVisible(true);
		return mainLayout;	

}}
