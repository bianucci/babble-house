package com.ffh.babblehouse.view;

import java.util.List;

import com.ffh.babblehouse.controller.BusinessObjects.BoDevice;
import com.ffh.babblehouse.controller.BusinessObjects.BoSensor;
import com.ffh.babblehouse.controller.BusinessObjects.BoServiceGroup;
import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;
import com.ffh.babblehouse.model.DtoServiceGroup;
import com.ffh.babblehouse.model.DtoValue;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.annotations.AutoGenerated;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MainUI extends CustomComponent implements View{

	//region AutoGenerated variables
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
	
	//endregion AutoGenerated variables
	
	GeneralInfoComponent GenInfo;
	Navigator navigator;
	BoServiceGroup boServiceGroup = new BoServiceGroup();
	BoSensor boSensor = new BoSensor();
	BoDevice boDevice = new BoDevice();
	List<DtoServiceGroup> serviceGroupList;
	Chart sensorChart = new Chart(ChartType.LINE);

	public MainUI(Navigator navigator) {
		this.navigator = navigator;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// Automatic design building
		buildMainLayout();
		setCompositionRoot(mainLayout);
		
		// Adds icons to buttons
		fillButtonsWithIcons();
		serviceGroupList = boServiceGroup.getServiceGrouplist();
		
		// Generate Service Groups Tree
		generateTree();
		
		addUserDefinedRulesComponent();
		
	}

	private void fillChart(String itemName, String measuringUnit, List<DtoValue> valueList, int maximumAmount){
		// Removing old Charts
		GeneralInformationVerticalLayout.removeComponent(sensorChart);
		
		// Configuring Sensor Chart
		sensorChart = new Chart(ChartType.SPLINE);
		Configuration sensorChartConf = sensorChart.getConfiguration();
		sensorChartConf.getxAxis().setType(AxisType.DATETIME);
		sensorChartConf.setTitle("Last Records of " + itemName + " (Max: "+ maximumAmount +")");
		DataSeries series = new DataSeries(itemName);
		
		// Printing data into Chart
		for(DtoValue value : valueList){
			
			series.add(new DataSeriesItem(value.getCurrentTimestamp(),value.getValue()));
			
		}
		// Adding Measuring unit to Y axis
		sensorChartConf.addSeries(series);
		YAxis yAxis = new YAxis();
		yAxis.setTitle(measuringUnit);
		sensorChartConf.addyAxis(yAxis);
		
		// Assigning chart to vertical layout ( General information )
		GeneralInformationVerticalLayout.addComponent(sensorChart);
		
		// Setting chart as visible
		sensorChart.setVisible(true);
	}
	
	private void generateTree(){

		final Object[] servicegroups = boServiceGroup.convertToArray(serviceGroupList);
		
		fillTree(servicegroups);
		
		ServiceGroupTree.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {

				String itemName = "";
				
				try {
					itemName = event.getProperty().getValue().toString();
				} catch (Exception e) {
					// Do nothing
				}
				
				GeneralInformationVerticalLayout.removeAllComponents();

				
				if(!ServiceGroupTree.areChildrenAllowed(itemName)){
					//region IF it is a leaf
					int maximumAmount = 24;
					if(itemName.toLowerCase().contains("sensor")){
						DtoSensor dtoSensor = boSensor.getSensorByName(itemName,maximumAmount);
						if(dtoSensor!=null)
							fillChart(dtoSensor.getSensorName(), dtoSensor.getMeasuringUnit().getUnit_name(), dtoSensor.getValues(),maximumAmount);
						else
							Notification.show("Sensor not found in DB", Type.ERROR_MESSAGE);
					}
					else if(itemName.toLowerCase().contains("actuator")){
						DtoDevice dtoDevice = boDevice.getDeviceByName(itemName,maximumAmount);
						if(dtoDevice != null)
							fillChart(dtoDevice.getDeviceName(), "Measuring unit", dtoDevice.getValues(),maximumAmount);
						else
							Notification.show("Device not found in DB", Type.ERROR_MESSAGE);
					}//endregion IF it is a leaf
				} 
				else
				{	
					if(ServiceGroupTree.isRoot(itemName)){
						//region IF it is a root
						DtoServiceGroup dtoServiceGroup = boServiceGroup.getServiceGroupByName(itemName);
						GenInfo = new GeneralInfoComponent(dtoServiceGroup);
						GeneralInformationVerticalLayout.addComponent(GenInfo);
						//endregion IF it is a root
					}	
				}
			}
		});
	}
	
	private void fillTree(final Object[] servicegroups) {
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
	}		
	
	protected void addUserDefinedRulesComponent(){
		UserDefinedRulesTab.removeAllComponents();
		UserDefinedRulesTab.addComponent(new UserDefinedRulesComponent(this,this.getUI(),serviceGroupList));
	}
	
	private void fillButtonsWithIcons() {
		MainPageTab.getTab(MainPageTabHorizontalLayout)			.setIcon(new ThemeResource("img/BlackHouse.ico"));
		MainPageTab.getTab(UserDefinedRulesTab)					.setIcon(new ThemeResource("img/gear.ico"));
		LogOffButton											.setIcon(new ThemeResource("img/logoff_.ico"));
	}
	
	//region Auto generated methods
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
		UserDefinedRulesTab = new VerticalLayout();
		UserDefinedRulesTab.setImmediate(false);
		UserDefinedRulesTab.setWidth("100.0%");
		UserDefinedRulesTab.setHeight("100.0%");
		UserDefinedRulesTab.setMargin(true);
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
		
		return GeneralInformationPanel;
	}
	//endregion Auto generated methods
}
