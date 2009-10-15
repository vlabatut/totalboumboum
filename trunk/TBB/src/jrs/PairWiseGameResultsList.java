/*
 * PairWiseGameResultsList.java
 *
 */

package jrs;

import java.util.*;

/** A list of <code>PairWiseGameResult</code> objects. It extends 
  * <code>ArrayList</code> to provide support for computing the weighting of
  * the results.
  *
  * @author Derek Hilder
  */
class PairWiseGameResultsList extends ArrayList {
    
    private int numberOfGamesPlayed = 0;
    private int numberOfPairWiseGameResults = 0;
    
    /**
      * 
      * @param o 
      * @return 
      */
    public boolean add(Object o) {
        numberOfPairWiseGameResults++;
        return super.add(o);
    }
    
    /**
      */
    public void incrementNumberOfGamesPlayed() {
        numberOfGamesPlayed++;
    }
    
    /**
      * 
      * @return 
      */
    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }
    
    /**
      * 
      * @return 
      */
    public int getNumberOfPairWiseGameResults() {
        return numberOfPairWiseGameResults;
    }
    
    /**
      * 
      * @return 
      */
    public double getWeighting() {
        return (double)numberOfGamesPlayed/numberOfPairWiseGameResults;
    }
}
