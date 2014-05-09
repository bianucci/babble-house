package com.ffh.babblehouse.view;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("babblehouse")
public class BabbleHouseUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = BabbleHouseUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);
		
		// The 2 lines below are examples and will be removed.
		Button buttonOnOff = new Button("On/Off");
		layout.addComponent(buttonOnOff);
//			Button buttonOnOff = new Button("On/Off", new Button.ClickListener () {
//			// Actions performed when button is pressed
//			BoLed boLed = new BoLed();
//			int i = 0 ;
//			public void buttonClick(Button.ClickEvent event){
//				boLed.toggleDtoPinStatus();
//				boLed.setDtoId(i++);
//				layout.addComponent(boLed.getDtoId());
//				layout.addComponent(boLed.getDtoPinStatus());
//			}
//		});
		
//		layout.addComponent(buttonOnOff);
	}

}