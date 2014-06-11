package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;


public interface IServiceExtractor {
	void extractservicemessage(DtoSensor o);
	void extractserviceMessage(DtoDevice o);
}
