package org.cougaar.lib.uiframework.ui.components.desktop;

public abstract class ComponentFactory implements java.io.Serializable
{
	public abstract String getToolDisplayName();
	public abstract CougaarDesktopUI create();
}
