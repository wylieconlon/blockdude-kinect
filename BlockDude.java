/** BlockDude for Kinect
 *  by Wylie Conlon
 *  4/18/12
 *
 *  modified from GorillasTracker.java
 *  provided by Andrew Davison, Feb 2012, ad@fivedots.psu.ac.th
 *
 *  all non-trivial modifications are tagged WYLIE as comments
 *
 *  based on UserTrackerApplication.java
 *  from the Java OpenNI UserTracker.java sample
**/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;


public class BlockDude extends JFrame {
  private TrackerPanel trackPanel; 
  
  public BlockDude()
  {
    super("BlockDude");

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
  }

  public static void main( String args[] )
  {  new BlockDude();  }

}
