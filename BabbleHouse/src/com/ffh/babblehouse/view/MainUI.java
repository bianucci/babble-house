package com.ffh.babblehouse.view;

import java.util.List;

import com.ffh.babblehouse.controller.BusinessObjects.BoDevice;
import com.ffh.babblehouse.controller.BusinessObjects.BoSensor;
import com.ffh.babblehouse.controller.BusinessObjects.BoServiceGroup;
import com.ffh.babblehouse.controller.repositories.SensorRepository;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoValue;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainUI extends CustomComponent implements View{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private VerticalLayout BodyVerticalLayout;
	@AutoGenerated
	private TabSheet MainPageTab;
	@AutoGenerated
	private VerticalLayout UserDefinedRulesTab;
	@AutoGenerated
	private HorizontalLayout ContentHorizontalLayout;
	@AutoGenerated
	private Table UDRTable;
	@AutoGenerated
	private HorizontalLayout OptionsHorizontalLayout;
	@AutoGenerated
	private Button AddRuleButton;
	@AutoGenerated
	private Button RemoveRuleButton;
	@AutoGenerated
	private HorizontalLayout MainPageTabHorizontalLayout;
	@AutoGenerated
	private Panel GeneralInformationPanel;
	@AutoGenerated
	private VerticalLayout GeneralInformationVerticalLayout;
	@AutoGenerated
	private Panel ServiceGroupsPanel;
	@AutoGenerated
	private VerticalLayout verticalLayout_3;
	@AutoGenerated
	private Tree ServiceGroupTree;
	@AutoGenerated
	private HorizontalLayout HeaderHorizontalLayout;
	@AutoGenerated
	private Button LogOffButton;
	
	private GeneralInfo GenInfo = new GeneralInfo();
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	
	Navigator navigator;
	BoServiceGroup boServiceGroup = new BoServiceGroup();
	Chart sensorChart = new Chart(ChartType.LINE);
	
	public MainUI(Navigator navigator) {
		this.navigator = navigator;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Automatic design building
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		// Makes the UDR table selectable for user
		UDRTable.setSelectable(true);
		
		// Adds icons to buttons		
		MainPageTab.getTab(MainPageTabHorizontalLayout)			.setIcon(new ThemeResource("img/BlackHouse.ico"));
		MainPageTab.getTab(UserDefinedRulesTab)					.setIcon(new ThemeResource("img/gear.ico"));
		LogOffButton											.setIcon(new ThemeResource("img/logoff_.ico"));
		
		// Generate Service Groups Tree
		generateTree(boServiceGroup.getServiceGrouplist());
		
		//TODO this call will be removed
		generateTable();
		
//		List<DtoValue> valueList = new SensorRepository().getLastSensorValues(1, 2);
//		
//		for(DtoValue value : valueList)
//			System.out.println(value.getValue());
	}

	private void fillChart(String itemName, String measuringUnit, List<DtoValue> valueList){
		// Removing old Charts
		GeneralInformationVerticalLayout.removeComponent(sensorChart);
		
		// Configuring Sensor Chart
		sensorChart = new Chart(ChartType.LINE);
		Configuration sensorChartConf = sensorChart.getConfiguration();
		sensorChartConf.setTitle("Last Records of " + itemName + " (Max: 24)");
		ListSeries series = new ListSeries(itemName);
		
		for(DtoValue value : valueList){
			series.addData(value.getValue());
			System.out.println(value.getValue());
		}
		sensorChartConf.addSeries(series);
		YAxis yAxis = new YAxis();
		yAxis.setTitle(measuringUnit);
		sensorChartConf.addyAxis(yAxis);
		
		GeneralInformationVerticalLayout.addComponent(sensorChart);
		sensorChart.setVisible(true);
		
	}
	
	private void generateTree(List<DtoServiceGroup> serviceGroupList){

		final Object[] servicegroups = boServiceGroup.convertToArray(serviceGroupList);
		
		for(int i=0; i<servicegroups.length;i++){
			Object[] inner = (Object[]) servicegroups[i];
			
			String Parent = (String) inner[0];
			String Child = "";
			
			ServiceGroupTree.addItem(Parent);
			int room = i+1;
						
			for(int j=1;j<inner.length;j++){
				if(!inner[j].equals("Sensors") && !inner[j].equals("Actuators")){
					ServiceGroupTree.addItem(inner[j]);
					ServiceGroupTree.setParent(inner[j],Child);
					ServiceGroupTree.setChildrenAllowed(inner[j], false);
				}
				else{
					Child = (String) inner[j];
					
					if(Child.equals("Sensors"))
						Child = "Sensors in room " + room;
					else if(Child.equals("Actuators")) 
						Child = "Actuators in room " + room;
					else 
						ServiceGroupTree.addItem("READING ERROR");
					
					ServiceGroupTree.addItem(Child);
					ServiceGroupTree.setParent(Child, Parent);
				}
			}
			ServiceGroupTree.expandItemsRecursively(Parent);
		}
		
		ServiceGroupTree.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				String itemName = "";
				try {
					itemName = event.getProperty().getValue().toString();
				} catch (Exception e) {
					// Do nothing
				}
				  
				
				// TODO Make Calls to BO only if X time has passed. Normally values can be gotten from normal variables 
				// Maybe add a list with known recent values
				if(!ServiceGroupTree.areChildrenAllowed(itemName)){
					GenInfo.setVisible(false);
					
					if(itemName.toLowerCase().contains("sensor"))
						fillChart(itemName, "Measuring unit", new BoSensor().getLastSensorValuesByName(itemName, 24));
					else if(itemName.toLowerCase().contains("actuator"))
						fillChart(itemName, "Measuring unit", new BoDevice().getLastDeviceValuesByName(itemName, 24));
				}
				else
				{	
					sensorChart.setVisible(false);
					if(ServiceGroupTree.isRoot(itemName)){
						GeneralInformationVerticalLayout.addComponent(GenInfo);
						GenInfo.setVisible(true);
					}
				}
			}
		});
	}		
	
	//TODO this method will be removed
	private void generateTable(){
		
		UDRTable.addContainerProperty("Id", String.class, null);
		UDRTable.addContainerProperty("Sensor",  String.class, null);
		UDRTable.addContainerProperty("Actuator",  String.class, null);
		UDRTable.addContainerProperty("Status",  String.class, null);
		        
		// Add a few other rows using shorthand addItem()
		UDRTable.addItem(new Object[]{"12", "2" , "4", "true" }, 1);
		UDRTable.addItem(new Object[]{"23", "3", "5", "false"}, 2);
		UDRTable.addItem(new Object[]{"35", "4", "1", "false"}, 3);
		
		// Uncomment to set the maximum amount of elements to be show at once (adds an arrow to scroll down)
		//UDRTable.setPageLength(2);
	}
	
	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(true);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// HeaderHorizontalLayout
		HeaderHorizontalLayout = buildHeaderHorizontalLayout();
		mainLayout.addComponent(HeaderHorizontalLayout);
		mainLayout.setComponentAlignment(HeaderHorizontalLayout, new Alignment(
				20));
		
		// BodyVerticalLayout
		BodyVerticalLayout = buildBodyVerticalLayout();
		mainLayout.addComponent(BodyVerticalLayout);
		mainLayout.setExpandRatio(BodyVerticalLayout, 0.8f);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildHeaderHorizontalLayout() {
		// common part: create layout
		HeaderHorizontalLayout = new HorizontalLayout();
		HeaderHorizontalLayout.setImmediate(false);
		HeaderHorizontalLayout.setWidth("100.0%");
		HeaderHorizontalLayout.setHeight("-1px");
		HeaderHorizontalLayout.setMargin(true);
		
		// LogOffButton
		LogOffButton = new Button();
		LogOffButton.setCaption("Log off");
		LogOffButton.setImmediate(true);
		LogOffButton.setWidth("-1px");
		LogOffButton.setHeight("-1px");
		HeaderHorizontalLayout.addComponent(LogOffButton);
		HeaderHorizontalLayout.setComponentAlignment(LogOffButton,
				new Alignment(34));
		
		return HeaderHorizontalLayout;
	}

	@AutoGenerated
	private VerticalLayout buildBodyVerticalLayout() {
		// common part: create layout
		BodyVerticalLayout = new VerticalLayout();
		BodyVerticalLayout.setImmediate(true);
		BodyVerticalLayout.setWidth("100.0%");
		BodyVerticalLayout.setHeight("100.0%");
		BodyVerticalLayout.setMargin(false);
		
		// MainPageTab
		MainPageTab = buildMainPageTab();
		BodyVerticalLayout.addComponent(MainPageTab);
		
		return BodyVerticalLayout;
	}

	@AutoGenerated
	private TabSheet buildMainPageTab() {
		// common part: create layout
		MainPageTab = new TabSheet();
		MainPageTab.setImmediate(true);
		MainPageTab.setWidth("100.0%");
		MainPageTab.setHeight("100.0%");
		
		// MainPageTabHorizontalLayout
		MainPageTabHorizontalLayout = buildMainPageTabHorizontalLayout();
		MainPageTab.addTab(MainPageTabHorizontalLayout, "Main Page", null);
		
		// UserDefinedRulesTab
		UserDefinedRulesTab = buildUserDefinedRulesTab();
		MainPageTab.addTab(UserDefinedRulesTab, "User Defined Rules", null);
		
		return MainPageTab;
	}

	@AutoGenerated
	private HorizontalLayout buildMainPageTabHorizontalLayout() {
		// common part: create layout
		MainPageTabHorizontalLayout = new HorizontalLayout();
		MainPageTabHorizontalLayout.setImmediate(false);
		MainPageTabHorizontalLayout.setWidth("100.0%");
		MainPageTabHorizontalLayout.setHeight("100.0%");
		MainPageTabHorizontalLayout.setMargin(true);
		
		// ServiceGroupsPanel
		ServiceGroupsPanel = buildServiceGroupsPanel();
		MainPageTabHorizontalLayout.addComponent(ServiceGroupsPanel);
		MainPageTabHorizontalLayout.setExpandRatio(ServiceGroupsPanel, 0.2f);
		
		// GeneralInformationPanel
		GeneralInformationPanel = buildGeneralInformationPanel();
		MainPageTabHorizontalLayout.addComponent(GeneralInformationPanel);
		MainPageTabHorizontalLayout.setExpandRatio(GeneralInformationPanel,
				0.8f);
		
		return MainPageTabHorizontalLayout;
	}

	@AutoGenerated
	private Panel buildServiceGroupsPanel() {
		// common part: create layout
		ServiceGroupsPanel = new Panel();
		ServiceGroupsPanel.setCaption("Service Groups");
		ServiceGroupsPanel.setImmediate(true);
		ServiceGroupsPanel.setWidth("100.0%");
		ServiceGroupsPanel.setHeight("100.0%");
		
		// verticalLayout_3
		verticalLayout_3 = buildVerticalLayout_3();
		ServiceGroupsPanel.setContent(verticalLayout_3);
		
		return ServiceGroupsPanel;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_3() {
		// common part: create layout
		verticalLayout_3 = new VerticalLayout();
		verticalLayout_3.setImmediate(true);
		verticalLayout_3.setWidth("-1px");
		verticalLayout_3.setHeight("100.0%");
		verticalLayout_3.setMargin(false);
		
		// ServiceGroupTree
		ServiceGroupTree = new Tree();
		ServiceGroupTree.setImmediate(true);
		ServiceGroupTree.setWidth("-1px");
		ServiceGroupTree.setHeight("100.0%");
		verticalLayout_3.addComponent(ServiceGroupTree);
		
		return verticalLayout_3;
	}

	@AutoGenerated
	private Panel buildGeneralInformationPanel() {
		// common part: create layout
		GeneralInformationPanel = new Panel();
		GeneralInformationPanel.setCaption("General Information");
		GeneralInformationPanel.setImmediate(false);
		GeneralInformationPanel.setWidth("100.0%");
		GeneralInformationPanel.setHeight("100.0%");
		
		// GeneralInformationVerticalLayout
		GeneralInformationVerticalLayout = new VerticalLayout();
		GeneralInformationVerticalLayout.setImmediate(false);
		GeneralInformationVerticalLayout.setWidth("100.0%");
		GeneralInformationVerticalLayout.setHeight("100.0%");
		GeneralInformationVerticalLayout.setMargin(false);
		GeneralInformationPanel.setContent(GeneralInformationVerticalLayout);
		//GeneralInformationVerticalLayout.addComponent(GenInfo);
		
		return GeneralInformationPanel;
	}

	@AutoGenerated
	private VerticalLayout buildUserDefinedRulesTab() {
		// common part: create layout
		UserDefinedRulesTab = new VerticalLayout();
		UserDefinedRulesTab.setImmediate(false);
		UserDefinedRulesTab.setWidth("100.0%");
		UserDefinedRulesTab.setHeight("100.0%");
		UserDefinedRulesTab.setMargin(true);
		
		// OptionsHorizontalLayout
		OptionsHorizontalLayout = buildOptionsHorizontalLayout();
		UserDefinedRulesTab.addComponent(OptionsHorizontalLayout);
		UserDefinedRulesTab.setComponentAlignment(OptionsHorizontalLayout,
				new Alignment(6));
		
		// ContentHorizontalLayout
		ContentHorizontalLayout = buildContentHorizontalLayout();
		UserDefinedRulesTab.addComponent(ContentHorizontalLayout);
		UserDefinedRulesTab.setExpandRatio(ContentHorizontalLayout, 0.8f);
		UserDefinedRulesTab.setComponentAlignment(ContentHorizontalLayout,
				new Alignment(48));
		
		return UserDefinedRulesTab;
	}

	@AutoGenerated
	private HorizontalLayout buildOptionsHorizontalLayout() {
		// common part: create layout
		OptionsHorizontalLayout = new HorizontalLayout();
		OptionsHorizontalLayout.setImmediate(false);
		OptionsHorizontalLayout.setWidth("-1px");
		OptionsHorizontalLayout.setHeight("-1px");
		OptionsHorizontalLayout.setMargin(false);
		OptionsHorizontalLayout.setSpacing(true);
		
		// RemoveRuleButton
		RemoveRuleButton = new Button();
		RemoveRuleButton.setCaption("Remove selected rule");
		RemoveRuleButton.setImmediate(true);
		RemoveRuleButton.setWidth("-1px");
		RemoveRuleButton.setHeight("-1px");
		OptionsHorizontalLayout.addComponent(RemoveRuleButton);
		OptionsHorizontalLayout.setComponentAlignment(RemoveRuleButton,
				new Alignment(6));
		
		// AddRuleButton
		AddRuleButton = new Button();
		AddRuleButton.setCaption("Add new rule");
		AddRuleButton.setImmediate(true);
		AddRuleButton.setWidth("-1px");
		AddRuleButton.setHeight("-1px");
		OptionsHorizontalLayout.addComponent(AddRuleButton);
		
		return OptionsHorizontalLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildContentHorizontalLayout() {
		// common part: create layout
		ContentHorizontalLayout = new HorizontalLayout();
		ContentHorizontalLayout.setImmediate(false);
		ContentHorizontalLayout.setWidth("100.0%");
		ContentHorizontalLayout.setHeight("100.0%");
		ContentHorizontalLayout.setMargin(false);
		
		// UDRTable
		UDRTable = new Table();
		UDRTable.setImmediate(false);
		UDRTable.setWidth("-1px");
		UDRTable.setHeight("-1px");
		ContentHorizontalLayout.addComponent(UDRTable);
		ContentHorizontalLayout.setComponentAlignment(UDRTable, new Alignment(
				20));
		
		return ContentHorizontalLayout;
	}
}
