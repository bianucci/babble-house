/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.controller.BusinessObjects.IBoStateChangedHandler;

//import java.util.Observer;

/**
 * 
 * @author zziwa
 */
public interface IChangeReceiver {
	public void registerChangeHander(IBoStateChangedHandler o);
	public void valueChanged(Object o);
}
