/*
 * <copyright>
 *  
 *  Copyright 1997-2004 BBNT Solutions, LLC
 *  under sponsorship of the Defense Advanced Research Projects
 *  Agency (DARPA).
 * 
 *  You can redistribute this software and/or modify it under the
 *  terms of the Cougaar Open Source License as published on the
 *  Cougaar Open Source Website (www.cougaar.org).
 * 
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 *  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  
 * </copyright>
 */
package org.cougaar.lib.uiframework.ui.ohv;

import org.apache.xerces.parsers.DOMParser;


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.TreeSet;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import java.io.IOException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

import org.cougaar.lib.uiframework.ui.ohv.util.*;

/**
 *  Determines hierarchical relationships from a list of directed links.
 *  Build one via addOrg.  Roots will be determined.
 */
class OrgHierGenerator {
    HashtableSet orgs // clid gives hs of ohrs  (clids in society)
      =new HashtableSet();
    HashSet orgRefs  // every clid referenced as sup or sub
      =new HashSet();
    // key is superior; list is that guy's direct reports
    HashtableSet superiors  // clid gives hs of clids
      =new HashtableSet();
    // key is a direct report; list is that guy's direct superiors
    HashtableSet subordinates  // clid gives hs of clids
      =new HashtableSet();
    HashSet roots;  // leave initialized as null

     OrgHierGenerator() {}
     OrgHierGenerator(Collection c) {
        OrgHierRelationship ohr;
        for (Iterator citer=c.iterator(); citer.hasNext(); ) {
          ohr=(OrgHierRelationship)citer.next();
          addNode(ohr);
        }
     }
     void addNode(OrgHierRelationship ohr) {
	 try {
        orgs.put(ohr.getId(), ohr);
        orgRefs.add(ohr.getId());
        if (ohr.hasSuperior()) {
	    String sup=ohr.getOtherId();
	    if (sup==null) sup="NULL";
          superiors.put(sup, ohr.getId());
          subordinates.put(ohr.getId(), sup);
          orgRefs.add(sup);
        }
	 } catch (NullPointerException npe) {
	     System.err.println("Attempt to add ohr Node with null superior: "+ohr);
	     System.err.println("Check the society configuration...Ignoring this and Recovering...");
	 }
     }

     /**
      Returns cached copy of roots.
     **/
     public HashSet getRoots() {
        if (roots==null) {
          roots=generateRoots();
        }
        return roots;
     }

     /**
      Generates and returns cached copy of roots.
     **/
     public HashSet generateRoots() {
        // every org that does not have a superior is a root
        String orgId;
        Set subs=subordinates.keySet();
        if (roots==null) {
          roots=new HashSet();
        } else {
          roots.clear();
        }
        for (Iterator orit=orgRefs.iterator(); orit.hasNext(); ) {
            orgId=(String)orit.next();
            if (!subs.contains(orgId)) {
              roots.add(orgId);
            }
        }
        //System.out.println("generateRoots orgRefs"+orgRefs);
        //System.out.println("generateRoots orgRefs"+orgRefs);
        return roots;
     }

     /**
      Returns set of orgId's referenced, but not added to society.
     **/
     public Set getUnresolvedReferences() {
      Set rcSet=new HashSet();
        // every org that does not have a superior is a root
        String orgId;
        Set socOrgs=orgs.keySet();
        for (Iterator orit=orgRefs.iterator(); orit.hasNext(); ) {
            orgId=(String)orit.next();
            if (!socOrgs.contains(orgId)) {
              rcSet.add(orgId);
            }
        }
        return rcSet;
     }

    /**
      @returns a HashSet of clids
    **/
    public HashSet getSubordinates(String sup)
    {
	    return (HashSet)superiors.get(sup);
    }
    /**
      @returns a HashSet of clids
    **/
    public HashSet getSuperiors(String sub)
    {
	    return (HashSet)subordinates.get(sub);
    }

    /**
      @returns a Set of clids
    **/
    public Set getOrganizations()
    {
	    return orgs.keySet();
    }
    /**
      @returns a Set of clids
    **/
    public Set getSuperiors()
    {
	    return superiors.keySet();
    }
    /**
      @returns a Set of clids
    **/
    public Set getSubordinates()
    {
	    return subordinates.keySet();
    }

  }

