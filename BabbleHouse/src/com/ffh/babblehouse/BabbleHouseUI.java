package com.ffh.babblehouse;

import javax.servlet.annotation.WebServlet;

import com.pi4j.io.gpio.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
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
		
		Button buttonOnOff = new Button("On/Off", new Button.ClickListener () {
			// Actions performed when button is pressed
			public void buttonClick(Button.ClickEvent event){
				
				layout.addComponent(new Label("Test")); 
				//pin.toggle();
			}
		});
		
		layout.addComponent(buttonOnOff);
	}

}