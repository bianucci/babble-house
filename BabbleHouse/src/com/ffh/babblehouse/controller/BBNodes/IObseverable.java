/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ffh.babblehouse.controller.BBNodes;

import com.ffh.babblehouse.observing.Observer;

//import java.util.Observer;

/**
 *
 * @author zziwa
 */
public interface Subject {
 public void RegisterObserver(Observer o); 
   public void RemoveTheObserver(Observer o);
   public void NotifyObserever(Object o);   
}
