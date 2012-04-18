
// GorillasTracker.java
// Andrew Davison, Feb 2012, ad@fivedots.psu.ac.th

/* Track Kinect users by displaying the coloured outline
   of their bodies, skeleton limbs, and a rotatable
   gorilla head on each one. (The head can rotate around the 
    z-axis.)

   Based on UserTrackerApplication.java
   from the Java OpenNI UserTracker sample

   Usage:
      > java GorillasTracker

   ========== Changes (December 2011) ================

   Added gesture detection to the application, which look for the starting/stopping
   of basic gestures (defined in the SkeletonActs class), and more high-level
   gestures made up of sequences of the basic gestures (in the ActionSequences
   class). 

   The Skeletons class creates the detectors, and keeps them updated.
   They notify TrackerPanel by having it implement the GesturesWatcher interface. 
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class GorillasTracker extends JFrame 
{
  private TrackerPanel trackPanel; 


  public GorillasTracker()
  {
    super("Gorillas Tracker");

    Container c = getContentPane();
    c.setLayout( new BorderLayout() );   

    trackPanel = new TrackerPanel();
    c.add( trackPanel, BorderLayout.CENTER);

    addWindowListener( new WindowAdapter() {
      public void windowClosing(WindowEvent e)
      { trackPanel.closeDown();  }
    });

    pack();  
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
  } // end of GorillasTracker()


  // -------------------------------------------------------

  public static void main( String args[] )
  {  new GorillasTracker();  }

} // end of GorillasTracker class
