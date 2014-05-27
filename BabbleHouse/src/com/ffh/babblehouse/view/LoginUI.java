package com.ffh.babblehouse.view;

import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.ffh.babblehouse.controller.BusinessObjects.BoUser;
import com.ffh.babblehouse.model.DtoUser;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
@Title("LoginUI")
public class LoginUI extends DefaultVerticalLoginForm implements View{
	Navigator navigator;
	String MAINUI;
	
	public LoginUI(Navigator navigator, String MAINUI){
		// Injecting the navigator
		this.navigator = navigator;
		// Injecting MAINUI name
		this.MAINUI = MAINUI;
	}
	
    @Override
    protected void login(String userName, String password) {
    	if(userName.length()<6 || userName.length() > 12 || password.length() < 6 || password.length() > 20)
    		Notification.show("Username and/or Password length is too long or too short",Type.ERROR_MESSAGE); 
    	else
    	{
        	// FIlling Dto with data from View
        	DtoUser userBeingVerified = new DtoUser();
        	userBeingVerified.setUserName(userName);
        	userBeingVerified.setPassword(password);
        	
        	// Creating a Business Object
        	BoUser boUser = new BoUser();
        	
        	// Requesting action from BO
        	try {
				if(boUser.Validate(userBeingVerified))
					navigator.navigateTo(MAINUI);
				else
					Notification.show("Could not Log in. Possibly wrong user name and/or password.",Type.ERROR_MESSAGE);
			}catch (Exception e) {
				Notification.show(e.getMessage() ,Type.ERROR_MESSAGE);
			}
    	}
    }
	
	@Override
	public void enter(ViewChangeEvent event) {
		Notification welcome = new Notification("Welcome to BabbleHouse!!!",Type.HUMANIZED_MESSAGE);
		welcome.setDelayMsec(3000);
		welcome.setPosition(Position.TOP_CENTER);
		welcome.show(this.getUI().getPage());
	}

}
