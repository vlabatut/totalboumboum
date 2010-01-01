package org.totalboumboum.statistics.glicko2.jrs;

/*
 * JRS Library
 * 
 * BSD License
 * Copyright (c) 2006-2007 JRS Project
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 		* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 		* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  	* Neither the name of the JRS Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * This library was modified by Vincent Labatut to be used in the Total Boum Boum project
 */

import java.util.Iterator;

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
public class ResultsBasedRankingService extends RankingService {
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
        Iterator<PairWiseGameResultsList> iter = currentPeriodGameResults.values().iterator();
        while (iter.hasNext()) {
            int numPlayerResults = iter.next().size();
            numResultsThisPeriod += numPlayerResults;
        }
        int numPlayers = getPlayers().size();
        int aveGamesPerPeriod = Integer.parseInt(System.getProperty("jrs.aveGamesPerPeriod", "15"));
        if ((double)numResultsThisPeriod/numPlayers >= aveGamesPerPeriod) {
            endPeriod();
        }
    }
    
}
