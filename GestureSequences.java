/** Complex Gesture Sequences
 *  modified by Wylie Conlon
 **/

// GestureSequences.java
// Andrew Davison, December 2011, ad@fivedots.psu.ac.th

/* GestureSequences stores gesture sequences for each user, and detects 
   more complex gestures by looking for specified sub-sequences.

   If a complex gesture sub-sequence is found, it is deleted from the user's
   sequence, including any other gestures intersperced between the
   sub-sequence gestures in the sequence.

   GestureSequences is mostly called from the Skeleton class, but SkeletonsGestures
   calls GestureSequences.addUserGest() to add an gesture to a user's sequence.
*/

import java.util.*;


public class GestureSequences
{
  /* complex gesture sub-sequences that are looked for in the user's 
     full gesture sequence.
  */

  // Lifting gestures
  // WYLIE

  // lifting one hand in front of body
  private final static GestureName[] RH_LIFT =
  		{ GestureName.RH_DOWN, GestureName.RH_STRAIGHT, GestureName.RH_UP };
  private final static GestureName[] LH_LIFT =
  		{ GestureName.LH_DOWN, GestureName.LH_STRAIGHT, GestureName.LH_UP };


  // arms extending
  // WYLIE
  private final static GestureName[] RH_EXTEND = 
  		{ GestureName.RH_BENT, GestureName.RH_STRAIGHT };
  private final static GestureName[] LH_EXTEND = 
  		{ GestureName.LH_BENT, GestureName.LH_STRAIGHT };

  private GesturesWatcher watcher;
      // object that is notified of a complex gesture by calling its pose() method

  private HashMap<Integer, ArrayList<GestureName>> userGestSeqs;

  public GestureSequences(GesturesWatcher gw) {
    watcher = gw;
    userGestSeqs = new HashMap<Integer, ArrayList<GestureName>>();
  }

  public void addUser(int userID) {
  	// create a new empty gestures sequence for a user
  	userGestSeqs.put(new Integer(userID), new ArrayList<GestureName>());
  } 

  public void removeUser(int userID)
  // remove the gesture sequence for this user
  {  userGestSeqs.remove(userID); }  



  public void addUserGest(int userID, GestureName gest)
  // called from SkeletonsGestures: add an gesture to the end of the user's sequence
  {
    ArrayList<GestureName> gestsSeq = userGestSeqs.get(userID);
    if (gestsSeq == null)
      System.out.println("No gestures sequence for user " + userID);
    else
      gestsSeq.add(gest);
  }



  public void checkSeqs(int userID)
  /* look for gesture sub-sequences in the user's full gesture sequence,
     and notify the watcher */
  {
    ArrayList<GestureName> gestsSeq = userGestSeqs.get(userID);
    if (gestsSeq != null)
      checkSeq(userID, gestsSeq);
  }



  private void checkSeq(int userID, ArrayList<GestureName> gestsSeq)
  /* look for gesture sub-sequences. If one is found, then the part
     of the user's gesture sequence containing the sub-sequence is deleted.
  */
  {
	// WYLIE
	
	// look for one-handed lifts
	int endPos = findSubSeq(gestsSeq, RH_LIFT);
  	if (endPos != -1) {
  		watcher.pose(userID, GestureName.RH_LIFT, true);
  		purgeSeq(gestsSeq, endPos);
  	}
	
	endPos = findSubSeq(gestsSeq, LH_LIFT);
  	if (endPos != -1) {
  		watcher.pose(userID, GestureName.LH_LIFT, true);
  		purgeSeq(gestsSeq, endPos);
  	}

  	// look for extension gestures
	endPos = findSubSeq(gestsSeq, RH_EXTEND);
  	if (endPos != -1) {
  		watcher.pose(userID, GestureName.RH_EXTEND, true);
  		purgeSeq(gestsSeq, endPos);
  	}

  	endPos = findSubSeq(gestsSeq, LH_EXTEND);
  	if (endPos != -1) {
  		watcher.pose(userID, GestureName.LH_EXTEND, true);
  		purgeSeq(gestsSeq, endPos);
  	}
  }  // end of checkSeq()



  private int findSubSeq(ArrayList<GestureName> gestsSeq, GestureName[] gests)
  /* Try to find all the gests[] array GestureName objects inside the list,
     and return the position *after* the last object, or -1. The array elements
     do not have to be stored contigiously in the list.
  */
  {
    int pos = 0;
    for(GestureName gest : gests) {   // iterate through the array
      while (pos < gestsSeq.size()) {  // find the gesture in the list
        if (gest == gestsSeq.get(pos))
          break;
        pos++;
      }
      if (pos == gestsSeq.size())
        return -1;
      else
        pos++;   // carry on, starting with next gesture in list
    }
    return pos;
  }  // end of findSubSeq()




  private void purgeSeq(ArrayList<GestureName> gestsSeq, int pos)
  /* remove all the elements in the seq between the positions
     0 and pos-1  */
  {
    for (int i=0; i < pos; i++) {
      if (gestsSeq.isEmpty())
        return;
      gestsSeq.remove(0);
    }
  }  // end of purgeSeq()



  private void printSeq(ArrayList<GestureName> gestsSeq)
  {
    if (gestsSeq.isEmpty())
      System.out.println("Sequence is empty");
    else {
      System.out.print("Sequence: ");
      for(GestureName gest : gestsSeq)
        System.out.print(gest + " ");
      System.out.println();
    }
  }  // end of printSeq() 


}  // end of GestureSequences class

