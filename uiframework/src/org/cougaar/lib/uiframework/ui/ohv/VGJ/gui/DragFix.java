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
	File: DragFix.java
	3/7/97    Larry Barowski

  The following comment is to comply with GPLv2:
     This source file was modified during February 2001.
*/


   package org.cougaar.lib.uiframework.ui.ohv.VGJ.gui;



   import java.awt.Event;
   import java.awt.Component;



/**
 *	Drag fix queues events and eliminates repeated mouse
 *      drag events and scrollbar events (most annoying on Win95).
 *      Be sure the Component doesn't post an event to itself
 *      after every mouse drag or scrollbar event, or this will do
 *      no good. I suggest getParent().postEvent() instead.</p>
 *
 *      The constructor for the Component that uses it should have
 *      (to be safe, as the first line):</p>
 *
 *      <PRE>dragFix_ = new DragFix(this);  // dragFix_ is a member variable.</PRE>
 *
 *
 *      <br><br>The handleEvent() function should look like this:</p>
 *
 *      <PRE>public boolean handleEvent(Event e)
 *      {
 *         if(e.id == DragFix.QUEUED)
 *         {
 *            deal with (Event)e.arg
 *            if necessary, super.handleEvent((Event)e.arg);
 *            if necessary, getParent().postEvent((Event)e.arg);
 *            return true;
 *         }
 *         dragFix_.queueEvent(e);
 *         return true;
 *      }</PRE>
 *
 *     <br><br>and removeNotify should look like this, to kill the thread
 *     immediately - otherwise it will be there (asleep) until
 *     finalize() gets called (if ever):</p>
 *
 *     <PRE>public synchronized void removeNotify()
 *     {
 *        dragFix_.killThread();
 *        super.removeNotify();
 *     }</pre></p><br><br>
 *
 *
 *
 *	</p>Here is the <a href="../gui/DragFix.java">source</a>.
 *
**/
   public class DragFix implements Runnable
   {
      private Component user_;
      private Thread thread_;
   
      private Event[] eventQueue_ = null;
      private int eventQueueHead_ = 0, eventQueueTail_ = 0;
      private int eventQueueSize_ = 20;  // More than enough, in most cases.
      private final static int eventQueueLimit_ = 2000;
      private Object lock_ = new Object();
   
      public final static int QUEUED = 179583;
   
      public DragFix(Component user)
      {
         user_ = user;
         eventQueue_ = new Event[eventQueueSize_ + 1];
      
         thread_ = new Thread(this);
         thread_.start();
      }
   
   
   
      protected void finalize()
      {
         killThread();
      }
   
   
      public void killThread()
      {
         thread_.stop();
      }
   
   
   
   
      /**
      * Queue or ignore an event. Call this from handleEvent()
      * and return true.
      **/
      public void queueEvent(Event e)
      {
         Queue(e);
      }
   
   
   
   
      /**
      * Queue or dequeue an event.
      **/
      private Event Queue(Event e)
      {
         synchronized(lock_)
         {
            if(e == null)
            // Dequeue
            {
               if(eventQueueHead_ != eventQueueTail_)  // Not empty.
               {
                  eventQueueTail_ = (eventQueueTail_ + 1) % eventQueueSize_;
                  return eventQueue_[eventQueueTail_];
               }
               return null;  // To indicate empty.
            }
         
         // Add to queue.
            if(eventQueueHead_ != eventQueueTail_ && e.target ==
            eventQueue_[eventQueueHead_].target)
            {
               int newid = eventQueue_[eventQueueHead_].id;
               if((e.id == Event.MOUSE_DRAG &&
               newid == Event.MOUSE_DRAG) ||
               ((e.id == Event.SCROLL_ABSOLUTE ||
               e.id == Event.SCROLL_LINE_DOWN ||
               e.id == Event.SCROLL_LINE_UP ||
               e.id == Event.SCROLL_PAGE_DOWN ||
               e.id == Event.SCROLL_PAGE_UP) &&
               (newid == Event.SCROLL_ABSOLUTE ||
               newid == Event.SCROLL_LINE_DOWN ||
               newid == Event.SCROLL_LINE_UP ||
               newid == Event.SCROLL_PAGE_DOWN ||
               newid == Event.SCROLL_PAGE_UP)))
               {
               // Eliminate the previous mouse drag or scroll.
               // This assumes all scroll handling will use
               // absolute positions, instead of "clicks".
                  eventQueue_[eventQueueHead_] = e;
                  return null;
               }
            }
         
            eventQueue_[(eventQueueHead_ + 1) % eventQueueSize_] = e;
            eventQueueHead_ = (eventQueueHead_ + 1) % eventQueueSize_;
         
            lock_.notify();
         
            if((eventQueueHead_ + 1) % eventQueueSize_ ==
            eventQueueTail_)
            {
            // Queue is full, grow it.
               int new_size = eventQueueSize_ * 2;
               if(new_size > eventQueueLimit_)
                  throw new Error("DragFix event queue size limit " +
                     "exceeded.");
            
               Event[] new_queue = new Event[new_size + 1];
               int i = 0, j;
               for(j = eventQueueTail_; j != eventQueueHead_;
               j = (j + 1) % eventQueueSize_, i++)
                  new_queue[i] = eventQueue_[j];
               new_queue[i] = eventQueue_[j];
            
               eventQueue_ = new_queue;
               eventQueueHead_ = i;
               eventQueueTail_ = 0;
               eventQueueSize_ = new_size;
            }
            return null;
         }
      }
   
   
   /**
   * Process queued events.
   **/
      public synchronized void run()
      {
         Event e;
         while(true)
         {
         
            // Process events until the queue is empty.
            while((e = Queue(null)) != null)
            {
               user_.postEvent(new Event(e, QUEUED, e));
            }
         
            // Now that it is empty, wait for the next event.
            synchronized(lock_)
            {
               try {
                  lock_.wait(); }
                  catch(InterruptedException ex)
                  {}
            }
         }
      }
   }
