package org.cougaar.lib.uiframework.ui.components.desktop;

import java.util.*;

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

import org.cougaar.lib.uiframework.ui.components.desktop.dnd.*;

/** This class stores all family data that we use in the example. You should
note that no error checking is done, but you probably will want to set that up
in your scheme especially if you are using beans! */
public class FileInfo implements Transferable, Serializable {

  //final public static DataFlavor INFO_FLAVOR =
    //new DataFlavor(FileInfo.class, "Personal Information");
    
   final public static DataFlavor INFO_FLAVOR =
    ObjectTransferable.getDataFlavor(FileInfo.class);

  static DataFlavor flavors[] = {INFO_FLAVOR };
  private Vector textArea = null;
  private File dropFile = null;
  public static final boolean MALE = true;
  public static final boolean FEMALE = !MALE;

  private String Name = null;
  private boolean Gender = MALE;
  private FileInfo Parent = null;
  private Vector Children = null;
  private String destination = null;
  private byte[] byteArray;
  private String url;
  private long length = 0;
  //public String state = "begin";
  public boolean addFile = false;

  public FileInfo(String name, boolean gender) {
    Children = new Vector();
    Name = name;
    Gender = gender;
  }

  public String getName() {
    return Name;
  }
  public int getFileLength()
  {
  	return  byteArray.length;
  }
  
  public void setName(String name) {
    Name = name;
  }
  
  public void setUrl(String port)
  {
  	url = port;
  }
  
  public String getUrl()
  {
  	return url;
  }
  public void setTextArea(Vector text)
  {
  	textArea = text;
  }
  
  public Vector getTextArea()
  {
  	return textArea;
  }
  
  public File getDropFile()
  {
  	return dropFile;
  }
  
  public void setDropFile(File file)
  {
  	dropFile = file;
  }
  
  public boolean getGender() {
    return Gender;
  }

  public void setGender(boolean gender) {
    Gender = gender;
  }
  
  public void setByteArray(byte[] array)
  {
  	byteArray = array;
  }
  
  public byte[] getByteArray()
  {
  	return byteArray;
  }
  
  public boolean isMale() {
    return getGender() == MALE;
  }

  public void add(FileInfo info) {
    info.setParent(this);
    Children.add(info);
  }

  public void remove(FileInfo info) {
    info.setParent(null);
    Children.remove(info);
  }

  public FileInfo getParent() {
    return Parent;
  }

  public void setParent(FileInfo parent) {
    Parent = parent;
  }


  public void setDestination(String dest)
  {
  	destination = dest;
  }
  
  public String getDestination()
  {
  	return destination;
  }
  
  public void setLength(long len)
  {
  	length = len;
  }
  
  public long getLength()
  {
  	return length;
  }
  
  public Vector getChildren() {
    return Children;
  }

  public Object clone() {
    return new FileInfo(Name, Gender);
  }

  public String toString() {
    return Name;
  }

  // --------- Transferable --------------

  public boolean isDataFlavorSupported(DataFlavor df) {
    return df.equals(INFO_FLAVOR);
  }

  /** implements Transferable interface */
  public Object getTransferData(DataFlavor df)
      throws UnsupportedFlavorException, IOException {
    if (df.equals(INFO_FLAVOR)) {
      return this;
    }
    else throw new UnsupportedFlavorException(df);
  }

  /** implements Transferable interface */
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  // --------- Serializable --------------

   private void writeObject(java.io.ObjectOutputStream out) throws IOException {
     out.defaultWriteObject();
   }

   private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
     in.defaultReadObject();
   }
}
