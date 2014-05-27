package com.ffh.babblehouse.view;

import javax.servlet.annotation.WebServlet;

import com.ffh.babblehouse.controller.BusinessObjects.BoProduct;
import com.ffh.babblehouse.model.DtoProduct;
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
        navigator.addView("TEST", new MainUI(navigator));
	}

	// TODO This method will be removed
	public void exampleTest(){
		
		// ------------------------   Filling objects   --------------------------------
		
		// Example on how to fill a DTO with data from fields on our views
		DtoProduct p1 = new DtoProduct(); 
		// "shirt1" can be filled from a textfield, for example
		p1.setName("shirt1"); 
		// "15" can be filled from a textfield, for example
		p1.setPrice(15);  
		
		// --------
		
		DtoProduct p2 = new DtoProduct();
		p2.setName("shirt2"); 
		p2.setPrice(20);
		
		// --------
		
		DtoProduct p3 = new DtoProduct();
		p3.setName("shirt3"); 
		p3.setPrice(25);
		
		// --------------------  Requesting Logics from BO layer -----------------------
		
		
		// Call our Business Object to deal with the logics
		BoProduct boProduct = new BoProduct();
		
		// Saving it to the database
		boProduct.SaveOrUpdate(p1)
				 .SaveOrUpdate(p2)
				 .SaveOrUpdate(p3);
		
		// --------------------  Requesting Logics from BO layer -----------------------
		
		// Changing shirt1 name to be updated
		p1.setName("shirt10"); 
		
		boProduct.SaveOrUpdate(p1);
		
		// --------------------  Requesting Logics from BO layer -----------------------
		
		// removing shirt2 from DB
		boProduct.Delete(p2);
		
		// --------------------  Requesting Logics from BO layer -----------------------
		
		// Loading data from DB by Id
		DtoProduct loadedProduct = boProduct.SelectById(DtoProduct.class, p1.getId());
		
		// Filling textarea, for example, with data from the DB
		System.out.println("Id: " + loadedProduct.getId());
		System.out.println("Name: " + loadedProduct.getName());
		System.out.println("Price: " + loadedProduct.getPrice());
		
	}
}