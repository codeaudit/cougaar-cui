/* **********************************************************************
 *
 *  Clark Software Engineering, Ltd.
 *  5100 Springfield St. Ste 308
 *  Dayton, OH 45431-1263
 *  (937) 256-7848
 *
 *  Copyright (C) 2001
 *  This software is subject to copyright protection under the laws of
 *  the United States and other countries.
 *
 */

package org.cougaar.lib.uiframework.ui.map.layer.cgmicon;

import java.util.Hashtable;
import java.util.StringTokenizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

public class OMCGMIcons
{

  public String loadFile = "cgmload.txt";

  private Hashtable iconsByName = new Hashtable(37);

  public OMCGMIcons() throws IOException
  {
    try
    {
      loadUp (loadFile);
    }
    catch (FileNotFoundException fnfe)
    {
      System.err.println ("default load file, cgmload.txt, not found, no icons loaded");
    }

  }

  public OMCGMIcons (String loadMe) throws FileNotFoundException, IOException
  {
    loadUp (loadMe);
  }

  private void loadUp (String loadMe) throws FileNotFoundException, IOException
  {

    BufferedReader br = new BufferedReader ( new InputStreamReader (new FileInputStream (loadMe)));

    String line = br.readLine();

    OMCGM omcgmVal;
    while (line != null)
    {

      // tab separated file, like from Excel
      StringTokenizer stok = new StringTokenizer (line, "\t");

      String idkey = stok.nextToken();

      String fileName = stok.nextToken();

      try
      {
        String unitSize = stok.nextToken();

        // if we catch an exception above this next line won't be called
        omcgmVal = new OMCGM ( fileName, unitSize );

      }

      catch ( java.util.NoSuchElementException nsee)
      {
        omcgmVal = new OMCGM (fileName);

      }

      iconsByName.put(idkey, omcgmVal);

      line = br.readLine();
    }

  }

  public OMCGM get (String idkey)
  {
    return (OMCGM) iconsByName.get(idkey);
  }

}