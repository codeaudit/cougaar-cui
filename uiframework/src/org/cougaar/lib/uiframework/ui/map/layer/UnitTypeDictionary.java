/*
 * <copyright>
 * Copyright 1997-2001 Defense Advanced Research Projects
 * Agency (DARPA) and ALPINE (a BBN Technologies (BBN) and
 * Raytheon Systems Company (RSC) Consortium).
 * This software to be used only in accordance with the
 * COUGAAR licence agreement.
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.map.layer;

import java.util.*;
import java.io.*;


public class UnitTypeDictionary {
    public UnitTypeDictionary() {
	initializeDefaults();
    }
    public void reset() {
	dictionary=new Properties();
    }
    public void reset(InputStream input) throws IOException {
	reset();
	load(input);
    }
    public void load(InputStream input) throws IOException {
	dictionary.load(input);
    }
    public String getUnitType(String unitName) { 
	String type = dictionary.getProperty(unitName);
	if (type==null) { 
	    type="unknown"; 
	}
	return type;
    }
    Properties dictionary=new Properties();
    
    private void initializeDefaults() {
	dictionary.setProperty("23INBN","infantry");
	dictionary.setProperty("30INBN","infantry");
	dictionary.setProperty("31INBN","infantry");
	dictionary.setProperty("3-7-INBN","infantry");
	dictionary.setProperty("3-69-ARBN","armored");
	dictionary.setProperty("3ID","other");
	dictionary.setProperty("1BDE","other");
    }
    
}
