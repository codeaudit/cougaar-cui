package org.cougaar.lib.uiframework.ui.components.desktop;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.io.Serializable;

import java.awt.dnd.*;

import javax.swing.*;
import javax.swing.event.*;

import org.cougaar.lib.uiframework.ui.util.CougaarUI;
import org.cougaar.lib.uiframework.ui.components.graph.*;
import org.cougaar.lib.uiframework.ui.components.*;

import org.cougaar.lib.uiframework.ui.inventory.MenuUtility;

import org.cougaar.util.OptionPane;

import org.cougaar.lib.uiframework.ui.components.desktop.dnd.*;

public class DnDUI implements CougaarUI
{
	
	Container frame = null;
	private String rootPath = null;
	DnDSourceFileGUI srctest = null;
	
  public DnDUI()
  {
  
  }
  
  public void install(CDesktopFrame installFrame)
  {
  	frame = installFrame;
  	installFrame.setJMenuBar(new JMenuBar());
  	//rootPath = System.getProperty("user.dir");
  	rootPath = null;
  	buildTree(installFrame.getContentPane(), installFrame.getJMenuBar());
  
  
   /* installFrame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        
        if (frame instanceof JFrame)
        {
          System.exit(0);
        }
        else if (frame instanceof JInternalFrame)
        {
      }
      
      }
    });	*/
  
      
          
  	installFrame.show();
    installFrame.validate();
 }
 
  public void install(JFrame installFrame)
	{
		frame = installFrame;
		installFrame.setJMenuBar(new JMenuBar());
		//rootPath = System.getProperty("user.dir");
		rootPath = null;
		buildTree(installFrame.getContentPane(), installFrame.getJMenuBar());
		
		
		installFrame.show();
	  installFrame.validate();
	}
	
	public void install(JInternalFrame installFrame)
	{
		frame = installFrame;
		//installFrame.setJMenuBar(new JMenuBar());
		//rootPath = System.getProperty("user.dir");
		rootPath = null;
		buildTree(installFrame.getContentPane(), installFrame.getJMenuBar());
		
		
		installFrame.show();
	  installFrame.validate();
	}
	
	public void buildTree(Container contentPane, JMenuBar menuBar)
  {
  	//createMenuAndDialogs(contentPane, menuBar);
  	srctest = new DnDSourceFileGUI();
  	srctest.setRootPath(rootPath);
  	srctest.install((CDesktopFrame)frame);
  	
  	
  }
  
  private void createMenuAndDialogs(final Container contentPane, JMenuBar menuBar)
  {
  	JMenu            fileMenu = new JMenu("File"); 
  	JMenuItem        menuItem;

	menuItem = new JMenuItem("Open");
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		JFileChooser      fc = new JFileChooser();

		//fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int               result = fc.showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
		    String      newPath = fc.getSelectedFile().getPath();
        System.out.println("file is " + newPath);
		    //new TreeTableExample2(newPath);
		    rootPath = newPath;
		    //srctest = new DnDSourceFileGUI();
		    srctest.removeOldHierarchy((CDesktopFrame)frame);
  	    srctest.setRootPath(newPath);
      	srctest.install((CDesktopFrame)frame);
		    
		}
	    }
	});
	fileMenu.add(menuItem);
	fileMenu.addSeparator();
	menuBar.add(fileMenu);
  }
  public boolean supportsPlaf()
  {
    return(true);
  }

}