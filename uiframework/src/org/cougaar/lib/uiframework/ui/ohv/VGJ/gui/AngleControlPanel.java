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
/*
	File: AngleControlPanel.java
	9/3/96  Larry Barowski

  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
*/


   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;


   import java.awt.Panel;
   import java.awt.Button;
   import java.awt.Event;
   import java.awt.Label;

   import EDU.auburn.VGJ.util.DPoint;

/**
 *   An AngleControl, along with a label and buttons for XY plane,
 *   YZ plane, XZ plane.
 *   </p>Here is the <a href="../gui/AngleControlPanel.java">source</a>.
 *
**/

   class AngleControlPanel extends LPanel
      {
      public static int	ANGLE = 38793;
      public static int	DONE = 38794;
   
      private AngleControl angle_;
   
      public AngleControlPanel(int width, int height)
         {
	 super();

	 constraints.insets.top = constraints.insets.bottom = 0;
	 addLabel("Viewing Angles", 0, 0, 1.0, 0.0, 0, 0);
	 constraints.insets.top = constraints.insets.bottom = 0;
         angle_ = new AngleControl(width, height);
	 addComponent(angle_, 0, 0, 1.0, 1.0, 3, 0);
	 constraints.insets.top = constraints.insets.bottom = 0;
	 addLabel("Plane:", 1, -1, 0.0, 0.0, 0, 0);
         addButton("XY", 1, 0, 1.0, 0.0, 0, 0);
         addButton("XZ", 1, 0, 1.0, 0.0, 0, 0);
         addButton("YZ", 0, 0, 1.0, 0.0, 0, 0);

	 finish();
         }
   
   
      public boolean handleEvent(Event event)
      {
         if(event.target instanceof AngleControl) {
            if(event.id == AngleControl.ANGLE) {
               DPoint	angles = (DPoint)event.arg;
               postEvent(new Event((Object)this, ANGLE,
                  new DPoint(angles.x, angles.y)));
               }
	    else if(event.id == AngleControl.DONE) {
               DPoint	angles = (DPoint)event.arg;
               postEvent(new Event((Object)this, DONE,
                  new DPoint(angles.x, angles.y)));
	    }
         }
         return super.handleEvent(event);
      }
   
   
      public boolean action(Event event, Object what)
         {
         if(event.target instanceof Button)
            {
            if(((String)what).equals("XY"))
               {
               angle_.setAngles(0.0, Math.PI / 2.0);
               postEvent(new Event((Object)this, DONE,
                  new DPoint(0.0, Math.PI / 2.0)));
               }
            else if(((String)what).equals("XZ"))
               {
               angle_.setAngles(0.0, 0.0);
               postEvent(new Event((Object)this, DONE,
                  new DPoint(0.0, 0.0)));
               }
            else if(((String)what).equals("YZ"))
               {
               angle_.setAngles(Math.PI / 2.0, 0.0);
               postEvent(new Event((Object)this, DONE,
                  new DPoint(Math.PI / 2.0, 0.0)));
               }
            }
         return true;
         }
      }


