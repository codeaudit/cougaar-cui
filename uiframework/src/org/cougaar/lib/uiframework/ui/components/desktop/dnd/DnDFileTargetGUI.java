/*
 * <copyright>
 *  Copyright 2003 BBNT Solutions, LLC
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
package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.*;
import java.awt.datatransfer.*;

import java.io.Serializable;
import java.io.*;
import java.net.URLConnection;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import java.util.Vector;

import org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI;
import org.cougaar.lib.uiframework.ui.components.desktop.CDesktopFrame;

import org.cougaar.lib.uiframework.ui.components.desktop.FileInfo;

public class DnDFileTargetGUI extends org.cougaar.lib.uiframework.ui.components.desktop.ComponentFactory implements org.cougaar.lib.uiframework.ui.components.desktop.CougaarDesktopUI, DropTarget
{
  private JTextArea textArea = new JTextArea();
  private Color background = textArea.getBackground();
  protected static String ls = System.getProperty("line.separator");

  // Drag & Drop supporting class
  private DragAndDropSupport dndSupport = new DragAndDropSupport();



  // ------------------- DragSource Interface ----------------------------  

  public Vector getTargetComponents()
  {
    Vector components = new Vector(1);
    components.add(textArea);
    
    return(components);
  }

  public boolean dropToSubComponents()
  {
    return(true);
  }

  public boolean readyForDrop(Component componentAt, Point location, DataFlavor flavor)
  {
    return(true);
  }

  public void showAsDroppable(Component componentAt, Point location, DataFlavor flavor, boolean show, boolean droppable)
  {
		if(show)
		{
		  if (droppable)
		  {
			  textArea.setBackground(Color.green);
		  }
		  else
		  {
			  textArea.setBackground(Color.red);
			}
		}
		else
		{
			textArea.setBackground(background);
		}
  }

  public void dropData(Component componentAt, Point location, DataFlavor flavor, Object data)
  {
  	
  	//FileInfo f = (FileInfo)data;
  	FileInfo childInfo = (FileInfo) data;
    String urlString = childInfo.getUrl();
    if(urlString != null)
    {
	  	File xferFile = childInfo.getDropFile();  // source
	  	String path = xferFile.getPath();
	  	File file = xferFile.getAbsoluteFile();
	  	int fileLength = (int) childInfo.getLength();
	  	//FileOutputStream fos = getOutputStream(childInfo);
	  	File newFile = new File("temporaryFile");
	  	
	    System.out.println("%%%% get remote file url is " + urlString);
	    byte[] myByte = null;
	    URLConnection urlConOut = getPSPConnection(urlString, "/FINDFILE.PSP");
	    try
	    {
	      // Send the parameter (GET/POST data) to the specified URL
	      
	      FileOutputStream fos = new FileOutputStream(newFile);
	      DataOutputStream server = new DataOutputStream(urlConOut.getOutputStream());
				server.writeBytes(path);
				System.out.println("%%%% sent request to read " + path + " of length " + fileLength);
	      
	      InputStream is = urlConOut.getInputStream();
	      ObjectInputStream ois = new ObjectInputStream(is);
	      int fileTotal = 0;
	      while(fileTotal < fileLength)
	      {
	      	System.out.println("%%%% read object from psp");
	      	myByte = (byte[]) ois.readObject();
		      fos.write(myByte);
		      fileTotal += myByte.length;
		      System.out.println("got byteArray,  " + fileTotal);
	      }
	      fileTextArea(newFile);
	      
	    }
	  
		  catch (Exception e)
		  {
		      System.err.println ("jTree sending exception: " + e.toString());
		      e.printStackTrace();
		  }

	}
	else
	{
		fileTextArea(childInfo.getDropFile());
  }
 }

  public Vector getSupportedDataFlavors(Component componentAt, Point location)
  {
    Vector flavors = new Vector(1);
    flavors.add(ObjectTransferable.getDataFlavor(FileInfo.class));
    
    
    return(flavors);
  }

  public void fileTextArea(File file)
  {
  	try
    {
     java.io.BufferedReader invFile = new java.io.BufferedReader ( new java.io.FileReader (file));
     String lineString = null;
     while ( (lineString = invFile.readLine()) != null)
     {
     	//System.out.println("line " + lineString);
     	textArea.append(ls + lineString);
     }
    }
    catch(Exception eee)
    {
     eee.printStackTrace();
     //System.out.println("%%%% file not found");
    }
  }


  public void install(CDesktopFrame f)
  {
  	JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(textArea, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(panel);
    
    f.getContentPane().add(scrollPane);


    // Add the drop target
    dndSupport.addDropTarget(this);
  }






  
  
  
  
  public URLConnection getPSPConnection(String urlString, String psp)
  {
  	URLConnection urlCon = null;
  	try
    {
    	String uriString = urlString + psp;
      URL url = new URL(uriString);
      urlCon = url.openConnection();

      // Setup the communication parameters with the specifed URL
      urlCon.setDoOutput(true);
      urlCon.setDoInput(true);
      urlCon.setAllowUserInteraction(false);
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
    return urlCon;
  }
  
  
	public String getToolDisplayName()
	{
	  return("DnD File Target UI");
	}

	public CougaarDesktopUI create()
	{
	  return(this);
	}

  public boolean supportsPlaf()
  {
    return(true);
  }

  public void install(JFrame f)
  {
    throw(new RuntimeException("install(JFrame f) not supported"));
  }

  public void install(JInternalFrame f)
  {
    throw(new RuntimeException("install(JInternalFrame f) not supported"));
  }

  public boolean isPersistable()
  {
    return(false);
  }

  public Serializable getPersistedData()
  {
    return(null);
  }

  public void setPersistedData(Serializable data)
  {
  }

  public String getTitle()
  {
    return("DnD Target Test UI");
  }

  public Dimension getPreferredSize()
  {
    return(new Dimension(400, 600));
  }

  public boolean isResizable()
  {
    return(true);
  }
}
