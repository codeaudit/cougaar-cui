package org.cougaar.lib.uiframework.ui.components.desktop.dnd;

import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DnDConstants;

import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureRecognizer;

import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceEvent;

import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;

import java.awt.datatransfer.DataFlavor;

import java.awt.Component;
import java.awt.Container;

import java.util.Hashtable;
import java.util.Vector;

import org.cougaar.lib.uiframework.ui.components.desktop.dnd.ObjectTransferable;

public class DragAndDropSupport
{
  private Hashtable targets = new Hashtable(1);
  private Hashtable sources = new Hashtable(1);
  
  public void addDragSource(DragSource source)
  {
    sources.put(source, new DGListener(source));
  }

  public void addDropTarget(DropTarget target)
  {
    targets.put(targets, new DTListener(target));
  }

  public void removeDragSource(DragSource source)
  {
    ((DGListener)sources.remove(source)).remove();
  }

  public void removeDropTarget(DropTarget target)
  {
    ((DTListener)targets.remove(target)).remove();
  }

  public void enableDragSource(DragSource source, boolean active)
  {
    ((DGListener)sources.get(source)).setSourceActive(active);
  }

  public void enableDropTarget(DropTarget target, boolean active)
  {
    ((DTListener)targets.get(target)).setTargetActive(active);
  }
}

// ------------------  Drop Target operations  -----------------

class DTListener implements DropTargetListener
{
  private DropTarget target = null;
  private Vector dropTargetList = new Vector(1);
  
  public DTListener(DropTarget target)
  {
    this.target = target;
    
    boolean dropToSubComponents = target.dropToSubComponents();
    Vector components = target.getTargetComponents();
    for (int i=0, isize=components.size(); i<isize; i++)
    {
      if (dropToSubComponents)
      {
        setDropTargets((Component)components.elementAt(i));
      }
      else
      {
        dropTargetList.add(new java.awt.dnd.DropTarget((Component)components.elementAt(i), DnDConstants.ACTION_MOVE, this, true));
      }
    }
  }

  private void setDropTargets(Component parent)
  {
  	if (parent instanceof Container)
  	{
  		Component[] componentList = ((Container)parent).getComponents();
  		for (int i=0; i<componentList.length; i++)
  		{
  			setDropTargets(componentList[i]);
  		}
  	}
  	
    dropTargetList.add(new java.awt.dnd.DropTarget(parent, DnDConstants.ACTION_MOVE, this, true));
  }

  public void remove()
  {
    java.awt.dnd.DropTarget dropTarget = null;
    for (int i=0, isize=dropTargetList.size(); i<isize; i++)
    {
      dropTarget = (java.awt.dnd.DropTarget)dropTargetList.elementAt(i);
      dropTarget.removeDropTargetListener(this);
      dropTarget.setComponent(null);
    }
  }

  public void setTargetActive(boolean active)
  {
    for (int i=0, isize=dropTargetList.size(); i<isize; i++)
    {
      ((java.awt.dnd.DropTarget)dropTargetList.elementAt(i)).setActive(active);
    }
  }

	public void dragEnter(DropTargetDragEvent e)
	{
    checkDroppable(e);
	}

	public void dragOver(DropTargetDragEvent e)
	{
    checkDroppable(e);
	}

	public void dropActionChanged(DropTargetDragEvent e)
	{
    checkDroppable(e);
	}
  
	public void dragExit(DropTargetEvent e)
	{
		target.showAsDroppable(false, false);
	}

	private boolean isDroppable(DropTargetDragEvent e)
	{
		if ((e.getDropAction() & DnDConstants.ACTION_MOVE) != 0)
		{
		  Vector supportedFlavors = target.getSupportedDataFlavors();
		  for (int i=0, isize=supportedFlavors.size(); i<isize; i++)
		  {
    		if(e.isDataFlavorSupported((DataFlavor)supportedFlavors.elementAt(i)) == true)
    		{
    			return(target.readyForDrop(e.getLocation()));
    		}
    	}
		}

		return(false);
	}

	private boolean isDroppable(DropTargetDropEvent e)
	{
		if ((e.getDropAction() & DnDConstants.ACTION_MOVE) != 0)
		{
		  Vector supportedFlavors = target.getSupportedDataFlavors();
		  for (int i=0, isize=supportedFlavors.size(); i<isize; i++)
		  {
    		if(e.isDataFlavorSupported((DataFlavor)supportedFlavors.elementAt(i)) == true)
    		{
    			return(target.readyForDrop(e.getLocation()));
    		}
    	}
		}

		return(false);
	}

  private void checkDroppable(DropTargetDragEvent e)
  {
		if(isDroppable(e))
		{
  		target.showAsDroppable(true, true);
  		e.acceptDrag(e.getDropAction()); 
		}
		else
		{
			target.showAsDroppable(true, false);
			e.rejectDrag();
		}
  }

	public void drop(DropTargetDropEvent e)
	{
		target.showAsDroppable(false, false);

		if(isDroppable(e))
		{
		  try
		  {
  			e.acceptDrop(DnDConstants.ACTION_MOVE);
  			target.dropData(e.getTransferable().getTransferData(e.getTransferable().getTransferDataFlavors()[0]));
  			e.dropComplete(true);
  		}
  		catch (Throwable t)
  		{
  		  t.printStackTrace();
  		}
		}
		else
		{
			e.rejectDrop();      		
		}
	}
}



// ------------------  Drag Source operations  -----------------

class DGListener implements DragGestureListener
{
  // Should make this static
	private DragSourceListener dsListener = new DSListener();
	private DragSource source = null;

  private Vector recognizerList = new Vector(1);

  private boolean active = true;

	public DGListener(DragSource source)
	{
	  this.source = source;

    boolean dragFromSubComponents = source.dragFromSubComponents();
    Vector components = source.getSourceComponents();
    for (int i=0, isize=components.size(); i<isize; i++)
    {
      if (dragFromSubComponents)
      {
        setDragSource((Component)components.elementAt(i));
      }
      else
      {
        recognizerList.add(java.awt.dnd.DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer((Component)components.elementAt(i), DnDConstants.ACTION_MOVE, this));
      }
    }
	}

	private void setDragSource(Component parent)
	{
		if (parent instanceof Container)
		{
			Component[] componentList = ((Container)parent).getComponents();
			for (int i=0; i<componentList.length; i++)
			{
				setDragSource(componentList[i]);
			}
		}

    recognizerList.add(java.awt.dnd.DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(parent, DnDConstants.ACTION_MOVE, this));
	}

  public void remove()
  {
    DragGestureRecognizer recognizer = null;
    for (int i=0, isize=recognizerList.size(); i<isize; i++)
    {
      recognizer = (DragGestureRecognizer)recognizerList.elementAt(i);
      recognizer.removeDragGestureListener(this);
      recognizer.setComponent(null);
    }
  }

  public void setSourceActive(boolean active)
  {
    this.active = active;
  }

	public void dragGestureRecognized(DragGestureEvent e)
	{
		if ((active) && ((e.getDragAction() & DnDConstants.ACTION_MOVE) != 0))
		{
  		try
  		{
  		  Object data = source.getData(e.getDragOrigin());
  		  if (data != null)
  		  {
  			  e.startDrag(java.awt.dnd.DragSource.DefaultCopyNoDrop, new ObjectTransferable(data), dsListener);
  			}
  		}
  		catch (InvalidDnDOperationException ex)
  		{
  			ex.printStackTrace();
  		}
    }
	}

  private class DSListener implements DragSourceListener
  {
  	public void dragEnter(DragSourceDragEvent e)
  	{
  		DragSourceContext context = e.getDragSourceContext();
  
  		if((e.getDropAction() & DnDConstants.ACTION_MOVE) != 0)
  		{
  			context.setCursor(java.awt.dnd.DragSource.DefaultCopyDrop);	  
  		}
  		else
  		{
  			context.setCursor(java.awt.dnd.DragSource.DefaultCopyNoDrop);	  	
  		}
  	}
  
  	public void dragDropEnd(DragSourceDropEvent e)
  	{
  	  source.dragDropEnd(e.getDropSuccess());
  	}
  
  	public void dragOver(DragSourceDragEvent e)
  	{
  	}
  
  	public void dragExit(DragSourceEvent e)
  	{
  	}
  
  	public void dropActionChanged (DragSourceDragEvent e)
  	{
  	}
  }
}
