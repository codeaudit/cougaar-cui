package org.cougaar.lib.uiframework.ui.inventory;

import java.util.Hashtable;

public interface InventoryDataProvider
{
  public Hashtable getInventoryData(String organizationName, String assetName);
  public String getDefaultAssetName();
  public String getDefaultOrganizationName();
}
