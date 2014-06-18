package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.model.DtoMeasuringUnit;

public class UnitUtil {

	public static DtoMeasuringUnit getUnitForInfot(String info) {
		
		String unitName = null;
if(info=="Temp"){
	unitName="°C";
}else if(info=="Light"){
	unitName="nm";
}else if(info=="Humidity"){
	
	unitName="g/m3";
}
	

		

		
		DtoMeasuringUnit u = new DtoMeasuringUnit();
		u.setUnit_name(unitName);
		return u;
	}

}
