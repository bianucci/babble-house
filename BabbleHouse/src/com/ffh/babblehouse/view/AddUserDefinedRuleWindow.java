package com.ffh.babblehouse.view;

import java.util.List;

import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoUDR;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
public class AddUserDefinedRuleWindow extends Window{
	
	VerticalLayout content;
	AddUserDefinedRuleComponent addUserDefinedRuleComponent;
	List<DtoServiceGroup> serviceGroupList;
	DtoUDR dtoUDR;
	MainUI mainUI;
	
	public AddUserDefinedRuleWindow(MainUI mainUI, List<DtoServiceGroup> serviceGroupList) {
        super("Add a new user defined rule"); // Set window caption
        this.mainUI = mainUI;
        this.serviceGroupList = serviceGroupList; 
        
        setWindowConfiguration();
        
        setWindowContent();
    }
	
	private void setWindowContent() {
		// Some basic content for the window
		addUserDefinedRuleComponent = new AddUserDefinedRuleComponent(serviceGroupList);
		content = new VerticalLayout();
        content.addComponent(addUserDefinedRuleComponent);
        
        setContent(content);
        
        addButtons();

	}

	private void addButtons() {
		Button confirmButton = new Button("Confirm");
		Button cancelButton = new Button("Cancel");
		
        confirmButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				dtoUDR = addUserDefinedRuleComponent.saveData();
				close();
				
				mainUI.addUserDefinedRulesComponent();
			}
		});
        
		
        cancelButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
        
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSpacing(true);
        buttonsLayout.setMargin(true);
        buttonsLayout.setWidth("100%");
        buttonsLayout.addComponent(confirmButton);
        buttonsLayout.addComponent(cancelButton);
        buttonsLayout.setComponentAlignment(confirmButton, Alignment.MIDDLE_RIGHT);
        buttonsLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_LEFT);
        
        content.addComponent(buttonsLayout);
	}

	private void setWindowConfiguration() {
		center();
        // enable the close button
        setClosable(true);
        this.setResizable(false);
	}
}
