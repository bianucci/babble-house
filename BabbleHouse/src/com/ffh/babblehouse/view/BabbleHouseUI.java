package com.ffh.babblehouse.view;

import javax.servlet.annotation.WebServlet;

import com.ffh.babblehouse.controller.BusinessObjects.BoBase;
import com.ffh.babblehouse.controller.BusinessObjects.BoProduct;
import com.ffh.babblehouse.controller.repositories.ProductRepository;
import com.ffh.babblehouse.model.DtoProduct;
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
		exampleTest();
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
		
		DtoProduct loadedProduct = boProduct.SelectById(DtoProduct.class, p1.getId());
		
		// Filling textarea, for example, with data from the DB
		System.out.println("Id: " + loadedProduct.getId());
		System.out.println("Name: " + loadedProduct.getName());
		System.out.println("Price: " + loadedProduct.getPrice());
		
	}
}