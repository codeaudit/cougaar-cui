package org.cougaar.lib.uiframework.ui.components.desktop;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.dnd.peer.*;
import java.awt.event.*;
import java.io.*;



public class FileNode extends DefaultMutableTreeNode implements Serializable
{
	private boolean explored = false;
	public boolean isDir = true;
	private long length = 0;
	public FileNode(File file)
	{
		setUserObject(file);
		isFileDirectory();
		length = file.length();
	}
	public FileNode()
	{
		
	}
	
	public boolean getAllowedChildren(){return isDirectory();}
	public boolean isLeaf(){ return !isDirectory();}
	public File getFile() {return (File)getUserObject();}
	
	public boolean isExplored() {return explored;}
	public boolean isFileDirectory()
	{
		File file  = getFile();
		
		if(file == null)
		{
			return true;
		}
		else
		{
			return file.isDirectory();
		}
		
	}
	public boolean isDirectory()
	{
		/*File file  = getFile();
		if(file == null)
		  return true;
		return file.isDirectory();*/
		return isDir;
	}
  public void setDirectory(boolean isdir)
  {
  	isDir = isdir;
  }
  
  public long length()
  {
  	return length;
  }
  public String toString()
  {
  	File file = (File) getUserObject();
		String filename = null;
		if(file == null)
		  filename = "Empty";
		else
		  filename = file.toString();
		int index = filename.lastIndexOf("\\");
		
		return (index != -1 && index != filename.length() - 1) ? filename.substring(index + 1) : filename;
	}
	
	public void explore()
	{
		if(!isExplored())
		{
			File file = getFile();
			isDir = isFileDirectory();
			File[] children = file.listFiles();
			if(children != null)
			for(int i = 0; i < children.length; i++)
			{
				FileNode next = new FileNode(children[i]);
				next.isDir = next.isFileDirectory();
				//System.out.println("%%%% file " + children[i].getName());
				//System.out.println("%%%% file " + children[i].length());
				//System.out.println("%%%% next is dir " + next.isDirectory());
				add(next);
				
			}
			explored = true;
		}
	}
}
		
