/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ffh.babblehouse.observing;

import com.ffh.babblehouse.model.DtoDevice;
import com.ffh.babblehouse.model.DtoSensor;


/**
 *
 * @author zziwa
 */
public interface Observer {
    public void updateDtoDevice(DtoDevice dtoSensor );
    public void  updateSensor(DtoSensor dtoSensor);
   
}
