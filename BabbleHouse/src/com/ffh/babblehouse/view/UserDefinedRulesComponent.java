package com.ffh.babblehouse.view;

import java.util.List;

import com.ffh.babblehouse.controller.BusinessObjects.BoUDR;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class UserDefinedRulesComponent extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	//region AutoGenerated variables

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	@AutoGenerated
	private Table userDefinedRulesTable;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Button removeRuleButton;
	@AutoGenerated
	private Button addRuleButton;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	//endregion AutoGenerated variables
	
	UI ui;
	BoUDR boUDR = new BoUDR();
	List<DtoServiceGroup> serviceGroupList;
	
	public UserDefinedRulesComponent(UI ui, List<DtoServiceGroup> serviceGroupList) {
		this.ui = ui;
		this.serviceGroupList = serviceGroupList;
		
		buildMainLayout();
		setCompositionRoot(mainLayout);

		buildCustomComponents();
	}
	
	private void buildCustomComponents() {
		buttonsConfiguration();
		
		// Sets the table configurations
		tableConfiguration();
		
		// Adds the table header
		fillTableHeader();
		
		// Adds values from DB in table
		fillTableBody();
	}

	private void buttonsConfiguration() {
	
		removeRuleButton.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
								
				if(userDefinedRulesTable.getValue() != null){
					int itemId = (Integer) userDefinedRulesTable.getValue();
					// Removes selected table item from DB
					boUDR.deleteById(itemId);
					
					// Removes selected table item from UI
					userDefinedRulesTable.removeItem(itemId);
				}
				else
					Notification.show("An item must be selected to be removed.",Type.ERROR_MESSAGE);
			}
		});
		
		addRuleButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ui.addWindow(new AddUserDefinedRuleWindow(serviceGroupList));
			}
		});
	}

	private void tableConfiguration() {
		// Uncomment to set the maximum amount of elements to be show at once (adds an arrow to scroll down)
		//userDefinedRulesTable.setPageLength(2);
		
		// Makes the UDR table selectable for user
		userDefinedRulesTable.setSelectable(true);
		
		// Send changes in selection immediately to server.
		userDefinedRulesTable.setImmediate(true);
	}

	private void fillTableBody() {
		List<DtoUDR> dtoUDRList = boUDR.getUDRList();
		
		for(DtoUDR dtoUDR : dtoUDRList)
			userDefinedRulesTable.addItem(dtoUDR.toArray(),dtoUDR.getId());
	}	

	private void fillTableHeader() {
		userDefinedRulesTable.addContainerProperty("Min Value", Integer.class, null);
		userDefinedRulesTable.addContainerProperty("Max Value",  Integer.class, null);
		userDefinedRulesTable.addContainerProperty("Component",  String.class, null);
		userDefinedRulesTable.addContainerProperty("When smaller than Min Value",  String.class, null);
		userDefinedRulesTable.addContainerProperty("When greater than Max Value",  String.class, null);
	}

	//region AutoGenerated methods

	
@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		mainLayout.addComponent(horizontalLayout_1);
		mainLayout.setComponentAlignment(horizontalLayout_1, new Alignment(6));
		
		// verticalLayout_2
		verticalLayout_2 = buildVerticalLayout_2();
		mainLayout.addComponent(verticalLayout_2);
		mainLayout.setExpandRatio(verticalLayout_2, 1.0f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(true);
		horizontalLayout_1.setSpacing(true);
		
		// addRuleButton
		addRuleButton = new Button();
		addRuleButton.setCaption("Add new Rule");
		addRuleButton.setImmediate(false);
		addRuleButton.setWidth("-1px");
		addRuleButton.setHeight("-1px");
		horizontalLayout_1.addComponent(addRuleButton);
		horizontalLayout_1.setComponentAlignment(addRuleButton, new Alignment(
				48));
		
		// removeRuleButton
		removeRuleButton = new Button();
		removeRuleButton.setCaption("Remove Selected Rule");
		removeRuleButton.setImmediate(false);
		removeRuleButton.setWidth("-1px");
		removeRuleButton.setHeight("-1px");
		horizontalLayout_1.addComponent(removeRuleButton);
		horizontalLayout_1.setComponentAlignment(removeRuleButton,
				new Alignment(34));
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_2() {
		// common part: create layout
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("100.0%");
		verticalLayout_2.setHeight("100.0%");
		verticalLayout_2.setMargin(true);
		
		// userDefinedRulesTable
		userDefinedRulesTable = new Table();
		userDefinedRulesTable.setImmediate(false);
		userDefinedRulesTable.setWidth("-1px");
		userDefinedRulesTable.setHeight("-1px");
		verticalLayout_2.addComponent(userDefinedRulesTable);
		verticalLayout_2.setComponentAlignment(userDefinedRulesTable,
				new Alignment(20));
		
		return verticalLayout_2;
	}


	//endregion AutoGenerated methods
}
