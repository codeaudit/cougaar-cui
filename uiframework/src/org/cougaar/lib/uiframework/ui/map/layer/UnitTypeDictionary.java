/*
 * <copyright>
 *  Copyright 1997-2003 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects Agency (DARPA).
 * 
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the Cougaar Open Source License as published by
 *  DARPA on the Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THE COUGAAR SOFTWARE AND ANY DERIVATIVE SUPPLIED BY LICENSOR IS
 *  PROVIDED 'AS IS' WITHOUT WARRANTIES OF ANY KIND, WHETHER EXPRESS OR
 *  IMPLIED, INCLUDING (BUT NOT LIMITED TO) ALL IMPLIED WARRANTIES OF
 *  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, AND WITHOUT
 *  ANY WARRANTIES AS TO NON-INFRINGEMENT.  IN NO EVENT SHALL COPYRIGHT
 *  HOLDER BE LIABLE FOR ANY DIRECT, SPECIAL, INDIRECT OR CONSEQUENTIAL
 *  DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE OF DATA OR PROFITS,
 *  TORTIOUS CONDUCT, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 *  PERFORMANCE OF THE COUGAAR SOFTWARE.
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
