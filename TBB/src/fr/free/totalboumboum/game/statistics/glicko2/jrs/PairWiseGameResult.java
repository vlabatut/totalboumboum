/*
 * PairWiseGameResult.java
 *
 */

package fr.free.totalboumboum.game.statistics.glicko2.jrs;

import java.io.Serializable;

/** This class represents the results of a game against a single opponent. It
  * is used internally by the RankingService class. Clients of the system should
  * use GameResults objects to specify the results of a game.
  *
  * @author Derek Hilder
  */
class PairWiseGameResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private T opponentId;
    
    /** The value of the game result. For example, 1 for a win and 0 for a loss. */
    private double score;
    
    /**
     * Creates a new instance of PairWiseGameResult
     */
    PairWiseGameResult(T opponentId, double score) {
        this.opponentId = opponentId;
        this.score = score;
    }

    /**
     * 
     * @return 
     */
    T getOpponentId() {
        return opponentId;
    }

    /**
     * 
     * @return 
     */
    double getScore() {
        return score;
    }
}
