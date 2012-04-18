
// GesturesWatcher.java
// Andrew Davison, December 2011, ad@fivedots.coe.psu.ac.th

/* used to ensure that a watcher class can be contacted when
   a skeleton gesture starts (or stops)
*/

public interface GesturesWatcher 
{
  void pose(int userID, GestureName gest, boolean isActivated);
}
