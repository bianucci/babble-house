package com.ffh.babblehouse.view;

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


@SuppressWarnings("serial")
@Theme("reindeer")
public class BabbleHouseUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = BabbleHouseUI.class, widgetset = "com.ffh.babblehouse.view.widgetset.BabblehouseWidgetset")
	public static class Servlet extends VaadinServlet {
	}
    Navigator navigator;
    protected static final String MAINUI = "main";
  
	@Override
	protected void init(VaadinRequest request) {
		
        getPage().setTitle("Babble House");
        
        // Create a navigator to control the views
        navigator = new Navigator(this, this);
        
        // Create and register the views
        navigator.addView("", new LoginUI(navigator,MAINUI));
        navigator.addView(MAINUI, new MainUI(navigator));
	}
}