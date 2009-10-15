/*
 * ResultsBasedRankingService.java
 *
 */

package fr.free.totalboumboum.game.statistics.glicko2.jrs;

import java.util.*;

/** A subclass of <code>RankingService</code> that updates players' ratings
  * after a specified number of results has been posted.
  * <p>
  * The Glicko-2 system works best when the number of pair-wise game results
  * posted in a rating period is moderate to large. That is, an average of at
  * least 10 to 15 results. To accomodate this, the <code>postResults()</code>
  * method ends the current rating period after an average number of pair-wise
  * game results have been posted for each player. This average number is 
  * specified by the <code>jrs.aveGamesPerPeriod</code> property.
  *
  * @author Derek Hilder
  */
public class ResultsBasedRankingService<T> extends RankingService<T> {
    private static final long serialVersionUID = 1L;
    
    /** Creates a new instance of ResultsBasedRankingService */
    public ResultsBasedRankingService() {
        super();
    }
    
    /** Post the results of a game to the system. These results will be used
      * to generate pair-wise game results for each player in the game. If the
      * average number of pair-wise game results for each player exceeds a 
      * certain limit (as specified by the <code>jrs.aveGamesPerPeriod</code>
      * property), the period will end and the players' ratings will be updated.
      * 
      * @param gameResults
      *     An instance of GameResults indicating the results of a game.
      */
    public synchronized void postResults(GameResults gameResults) {

        super.postResults(gameResults);
        
        int numResultsThisPeriod = 0;
        Iterator iter = currentPeriodGameResults.values().iterator();
        while (iter.hasNext()) {
            int numPlayerResults = ((List)iter.next()).size();
            numResultsThisPeriod += numPlayerResults;
        }
        int numPlayers = getPlayers().size();
        int aveGamesPerPeriod = Integer.parseInt(System.getProperty("jrs.aveGamesPerPeriod", "15"));
        if ((double)numResultsThisPeriod/numPlayers >= aveGamesPerPeriod) {
            endPeriod();
        }
    }
    
}
