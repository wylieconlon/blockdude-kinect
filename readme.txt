
Kinect Chapter 13. FAAST-style Body Gestures

From the website:

  Kinect Open Source Programming Secrets
  http://fivedots.coe.psu.ac.th/~ad/kinect/

  Dr. Andrew Davison
  Dept. of Computer Engineering
  Prince of Songkla University
  Hat yai, Songkhla 90112, Thailand
  E-mail: ad@fivedots.coe.psu.ac.th


If you use this code, please mention my name, and include a link
to the website.

Thanks,
  Andrew

============================

This directory contains 6 Java files:
  * GorillasTracker.java, TrackerPanel.java, Skeletons.java,
    GesturesWatcher.java, SkeletonsGestures.java, GestureSequences.java

One image file:
  * gorilla.png

Two batch files:
  *  compile.bat
  *  run.bat
     - make sure they refer to the correct location for OpenNI;



----------------------------
Before Compilation/Execution:

You need to download and install:
    1.	OpenNI
    2.	SensorKinect driver
    3.	NITE

For details, read section 3 of Chapter 2, or installInfo.txt
in this directory.


----------------------------
Compilation:

> compile *.java
    // you must have OpenNI, the SensorKinect driver, and NITE installed;

----------------------------
Execution:

> run GorillasTracker
    // you must have OpenNI, the SensorKinect driver, and NITE installed;
    // remember to assume the "psi" position, so tracking can start;
    // Gesture detection information is printed to stdout

---------------------------------
Changing the Detected Gestures

The gesture detectors called by this application are specified in the
checkGests() method in the SkeletonsGestures class.

Currently only leaning and turning left detection is switched on, but
methods are included for detecting hands close together, an assortment of
right hand positions, right hand waving (in GestureSequences), hip 
touching, and a raised left hand. 

Uncomment the detectors that you want to use, or add new ones :)

---------------------------------
Last updated: 2nd March 2012
