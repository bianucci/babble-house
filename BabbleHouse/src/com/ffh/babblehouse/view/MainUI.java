package com.ffh.babblehouse.view;

import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Title("MainUI")
public class MainUI extends VerticalLayout implements View{
	Navigator navigator;
	
	public MainUI(Navigator navigator){
		// Injecting the navigator
		this.navigator = navigator;
	}
	
	// Every time MainUI is invoked, this method is executed before rendering components
	@Override
	public void enter(ViewChangeEvent event) {
		//Here goes the Main page components
		Label label = new Label("MainUI");
		this.addComponent(label);
	}
}