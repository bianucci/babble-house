package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoValue;

public interface IBBDataBridge {
	void changeDeviceStatus(DtoDevice dtoDevice);
}
