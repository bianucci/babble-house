package com.ffh.babblehouse;

import javax.servlet.annotation.WebServlet;

import com.pi4j.io.gpio.*;
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
		
		// Begin of Pin interaction
		GpioController gpioController = GpioFactory.getInstance();
		//Set GPIO 0 to LOW
		final GpioPinDigitalOutput pin = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00,"My LED", PinState.LOW);
		// Create a button
		Button buttonOnOff = new Button("On/Off", new Button.ClickListener () {
			// Actions performed when button is pressed
			public void buttonClick(Button.ClickEvent event){
				pin.toggle();
			}
		});
				
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		layout.addComponent(buttonOnOff);
	}

}