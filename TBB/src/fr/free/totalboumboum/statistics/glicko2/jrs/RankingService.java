package fr.free.totalboumboum.statistics.glicko2.jrs;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.LegendreGaussIntegrator;
import org.apache.commons.math.analysis.integration.RombergIntegrator;
import org.apache.commons.math.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math.analysis.integration.TrapezoidIntegrator;

import fr.free.totalboumboum.statistics.GameStatistics;

/** The ranking service.
  * <p>
  * This class implements a ranking service that supports match-making and 
  * leaderboards. Game servers would typically instantiate this class (or a
  * subclass), then regisister each player with the service. As games are 
  * played, the results are posted to the service and the players are rated
  * according to their relative performances. The game server can then use 
  * this service to get a ranking of the players to construct a leaderboard,
  * and can get a list of players or games that are the best match for a
  * specified player.
  * <p>
  * The current implementation is based on the Glicko-2 algorithm, with 
  * extensions to support multiplayer and team based games.
  *
  * @author Derek Hilder
  */
public class RankingService implements Serializable {
    private static final long serialVersionUID = 1L;
        
    // For timing the caculatePlayerRatings() method.
    public static long timeToComputeLatestPlayerRatings;
    
    /** A map of player ratings indexed by player id. 
      * (ie. <code>Map&lt;Object, PlayerRating&gt;</code>)
      */
    HashMap<Integer,PlayerRating> playerRatings;
    
    /** A map of each players' list of game results for the current rating
      * period, indexed by player id.
      * (ie. <code>Map&lt;Object, List&lt;PairWiseGameResult&gt;&gt;</code>)
      */
    HashMap<Integer,PairWiseGameResultsList> currentPeriodGameResults;
        
    /** List of RankingServiceListeners to notify when rating periods begin
      * and end.
      */
    ArrayList<RankingServiceListener> listeners;
    
    /** The number of rating periods computed. */
    int periodCount;
    
    
    /** Creates an instance of the Ranking Service. The service will not
      * end the rating period automatically. Rather, the <code>endPeriod()</code>
      * method must be called explicitly to end the period and update the
      * players' ratings.
      */
    public RankingService() {
        playerRatings = new HashMap<Integer, PlayerRating>();
        currentPeriodGameResults = new HashMap<Integer, PairWiseGameResultsList>();
        listeners = new ArrayList<RankingServiceListener>();
        periodCount = 0;
    }
    
    /** Request that the ranking service end the current rating period. 
      * This will cause the ranking service to compute new player ratings based
      * on the results posted during the period, then a new rating period will
      * begin. <code>RankingServiceListeners</code> registered with this service
      * will be notified that the current period is ending, and then again when
      * the new period begins.
      */
    public void endPeriod() {
            
    	// TODO added by Vincent to save the previous rankings
    	GameStatistics.updatePreviousRankings();
    	
        // End the current rating period
        playerRatings = computePlayerRatings(playerRatings, currentPeriodGameResults);
        clearResults();

        // Notify listeners that the period has ended.
        Iterator<RankingServiceListener> listenersIter = listeners.iterator();
        while (listenersIter.hasNext()) {
            RankingServiceListener listener = (RankingServiceListener)listenersIter.next();
            listener.endRatingPeriod(RankingService.this, periodCount);
        }

        periodCount++;

        // Notify listeners that a new period is beginning
        listenersIter = listeners.iterator();
        while (listenersIter.hasNext()) {
            RankingServiceListener listener = (RankingServiceListener)listenersIter.next();
            listener.beginRatingPeriod(RankingService.this, periodCount);
        }
    }
    
    /** Register an implementation of the RankingServiceListener interface to
      * be notified when rating periods begin and end.
      * 
      * @param listener 
      *     An instance of an object implmenting the RankingServiceListener 
      *     interface.
      */
    public void addListener(RankingServiceListener listener) {
        listeners.add(listener);
    }
    
    // TO DO: removeListener(RankingServiceListener listener) ?
    
    /** Register a player with the Ranking Service. The system defaults will 
      * be used for the player's initial rating, rating deviation and rating volatility.
      * 
      * @param playerId 
      *     A unique identifier for the player.
      */
    public void registerPlayer(Integer playerId) {
        
        double rating = Double.parseDouble(System.getProperty("jrs.defaultRating", "1500"));
        double ratingDeviation = Double.parseDouble(System.getProperty("jrs.defaultRatingDeviation", "350"));
        double ratingVolatility = Double.parseDouble(System.getProperty("jrs.defaultRatingVolatility", "0.06"));
        PlayerRating playerRating = new PlayerRating(playerId, rating, ratingDeviation, ratingVolatility);
        
        registerPlayer(playerId, playerRating);
    }
    
    /** Register a player with the Ranking Service using the specified player
      * rating.
      * 
      * @param playerId 
      *     A unique identifier for the player.
      */
    public synchronized void registerPlayer(Integer playerId, PlayerRating playerRating) {
        playerRatings.put(playerId, playerRating);
        currentPeriodGameResults.put(playerId, new PairWiseGameResultsList());
    }
    
    /** Deregister the player from the ranking service. Once deregistered, the
      * player's rating information will not be available, and the player will
      * not be considered for match-making.
      * 
      * @param playerId 
      *     A unique identifier for the player.
      *
      * @author
      *     Vincent
      */
    public void deregisterPlayer(Integer playerId)
    {	// get the list of opponents
    	Set<Integer> set = new HashSet<Integer>();
    	PairWiseGameResultsList results = currentPeriodGameResults.get(playerId);
    	for(PairWiseGameResult result: results)
    	{	Integer opponentId = result.getOpponentId();
    		set.add(opponentId);
    	}
    	// remove the player in the opponentss' results
    	for(Integer opponentId: set)
    	{	PairWiseGameResultsList opponentResults = currentPeriodGameResults.get(opponentId);
    		Iterator<PairWiseGameResult> it = opponentResults.iterator();
    		while(it.hasNext())
    		{	PairWiseGameResult opponentResult = it.next();
    			if(opponentResult.getOpponentId().equals(playerId))
    				it.remove();
    		}
    	}
    	// remove the player's ratings
    	playerRatings.remove(playerId);
    	// remove the player's results
    	currentPeriodGameResults.remove(playerId);
    }
    
    /** Get a list of the ids of the players registered with the service.
      * 
      * @return 
      *     A Set of Objects representing the ids of the players.
      */
    public synchronized Set<Integer> getPlayers() {
        return Collections.unmodifiableSet(playerRatings.keySet());
    }

    /** Post the results of a game to the system. These results will be used to
      * adjust the players' ratings accordingly.
      * 
      * @param gameResults
      *     An instance of <code>GameResults</code> indicating the results of a game.
      */
    public synchronized void postResults(GameResults gameResults) {

        double drawThreshhold = Double.parseDouble(System.getProperty("rs.drawThreshhold", "0"));
        
        if (gameResults.isTeamGame()) {

            // For each team, add a game result to each of its member's current
            // period results representing a 'head-to-head' matchup between the
            // player and each player on the other teams. For example, in a game
            // with 3 teams: AB, CD, EF, where AB scores 10, CD scores 5 and
            // EF scores 1, the following game results will be added for each 
            // player for this period:
            //
            // A beat C, A beat D, A beat E, A beat F
            // B beat C, B beat D, B beat E, B beat F
            // C lost to A, C lost to B, C beat E, C beat F
            // D lost to A, D lost to B, D beat E, D beat F
            // E lost to A, E lost to B, E lost to C, E lost to D
            // F lost to A, F lost to B, F lost to C, F lost to D
            
            Iterator<Integer> teamIds = gameResults.getTeams().iterator();
            while (teamIds.hasNext()) {
            	Integer teamId = teamIds.next();
                double teamScore = gameResults.getTeamResults(teamId);
                Iterator<Integer> teamMemberIds = gameResults.getTeamMembers(teamId).iterator();
                while (teamMemberIds.hasNext()) {
                    Integer teamMemberId = teamMemberIds.next();

                    // NOTE added by Vincent to keep track of the rounds played since last update
                	{	PlayerRating playerRating = getPlayerRating(teamMemberId);
                		playerRating.incrementRoundcount();
                	}
                    
                    PairWiseGameResultsList teamMemberCurrentPeriodResults = currentPeriodGameResults.get(teamMemberId);
                    ((PairWiseGameResultsList)teamMemberCurrentPeriodResults).incrementNumberOfGamesPlayed();
                    Iterator<Integer> opposingTeamIds = gameResults.getTeams().iterator();
                    while (opposingTeamIds.hasNext()) {
                    	Integer opposingTeamId = opposingTeamIds.next();
                        if (!opposingTeamId.equals(teamId)) {
                            double opposingTeamScore = gameResults.getTeamResults(opposingTeamId);
                            Iterator<Integer> opposingTeamMemberIds = gameResults.getTeamMembers(opposingTeamId).iterator();
                            while (opposingTeamMemberIds.hasNext()) {
                            	Integer opposingTeamMemberId = opposingTeamMemberIds.next();
                                if (Math.abs(teamScore - opposingTeamScore) <= drawThreshhold) {
                                    teamMemberCurrentPeriodResults.add(new PairWiseGameResult(opposingTeamMemberId, 0.5));
                                }
                                else if (teamScore > opposingTeamScore) {
                                    teamMemberCurrentPeriodResults.add(new PairWiseGameResult(opposingTeamMemberId, 1));
                                }
                                else if (teamScore < opposingTeamScore) {
                                    teamMemberCurrentPeriodResults.add(new PairWiseGameResult(opposingTeamMemberId, 0));
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            Iterator<Integer> playerIds = gameResults.getPlayers().iterator();
            while (playerIds.hasNext()) {
            	Integer playerId = playerIds.next();
            	
            	// NOTE added by Vincent to keep track of the rounds played since last update
            	{	PlayerRating playerRating = getPlayerRating(playerId);
            		playerRating.incrementRoundcount();
            	}
            	
                double playerScore = gameResults.getPlayerResults(playerId);
                
                // Compare this player's score to the score of every other
                // player in the game. If this player's score is greater, add
                // the winning result to the player's results list. If the score
                // is less than the opponents, add the losing result to the
                // player's results list. If the scores are the 'same' (ie. 
                // within the value of the rs.drawThreshhold property), add the
                // draw result to the player's results list.
                
                PairWiseGameResultsList playersCurrentPeriodResults = currentPeriodGameResults.get(playerId);
                ((PairWiseGameResultsList)playersCurrentPeriodResults).incrementNumberOfGamesPlayed();
                Iterator<Integer> opponentIds = gameResults.getPlayers().iterator();
                while (opponentIds.hasNext()) {
                	Integer opponentId = opponentIds.next();
                    if (!opponentId.equals(playerId)) {
                        double opponentScore = gameResults.getPlayerResults(opponentId);
                        if (Math.abs(playerScore - opponentScore) <= drawThreshhold) {
                            playersCurrentPeriodResults.add(new PairWiseGameResult(opponentId, 0.5));
                        }
                        else if (playerScore > opponentScore) {
                            playersCurrentPeriodResults.add(new PairWiseGameResult(opponentId, 1));
                        }
                        else if (playerScore < opponentScore) {
                            playersCurrentPeriodResults.add(new PairWiseGameResult(opponentId, 0));
                        }
                    }
                }
                
            }
        }
    }
    
    /** Compute a new rating and rating deviation for each registered player
      * given the specified game results for the current rating period.
      * <p>
      * This method uses an implementation of the Glicko2 algorithm.
      * 
      * @param prePeriodRatings 
      *     A Map of PlayerRating objects, indexed by player id, representing
      *     each player's rating at the beginning of the period. 
      * @param periodGameResults 
      *     A Map of PairWiseGameResult objects, indexed by player id, 
      *     representing the outcome of each game the player played in during
      *     the period.
      * @return 
      *     A HashMap of PlayerRating objects, indexed by player id, representing
      *     each player's rating at the end of the period. 
      *
      * @todo
      *     Currently, the g and E values for each player are calculated 
      *     numerous times. Measure the execution time of this method, then
      *     try calculating the values once and caching them, and see if there
      *     is much improvement in execution time.
      */
    HashMap<Integer,PlayerRating> computePlayerRatings(Map<Integer,PlayerRating> prePeriodRatings, Map<Integer,PairWiseGameResultsList> periodGameResults) {
        
        long start = System.currentTimeMillis();
        
        HashMap<Integer,PlayerRating> postPeriodPlayerRatings = new HashMap<Integer,PlayerRating>();
        
        // The Glicko2 system constant that constrains the change in volatility
        // over time. Reasonable values range from 0.3 to 1.2
        double T = Double.parseDouble(System.getProperty("jrs.glicko2SystemConstant", "1.0"));
                
        Iterator<Integer> playerIds = prePeriodRatings.keySet().iterator();
        while (playerIds.hasNext()) {
            
            Integer playerId = playerIds.next();
            PlayerRating prePeriodPlayerRating = (PlayerRating)prePeriodRatings.get(playerId);
            
        	// NOTE added by Vincent to keep track of the rounds played since last update
            prePeriodPlayerRating.reinitRoundcount();
            
            
            // Convert the ratings and RDs onto the Glicko-2 scale.
            double rating = prePeriodPlayerRating.getGlicko2Rating();
            double ratingDeviation = prePeriodPlayerRating.getGlicko2RatingDeviation();
            double ratingVolatility = prePeriodPlayerRating.getRatingVolatility();
            
            double postPeriodRating = rating;  
            double postPeriodRatingDeviation = ratingDeviation;
            double postPeriodRatingVolatility = ratingVolatility;
            
            List<PairWiseGameResult> gameResultsList = periodGameResults.get(playerId);
            if (gameResultsList == null || gameResultsList.isEmpty()) {
                // This is the special case where the player did not compete in any
                // games during the rating period. In this case the player's rating and
                // volatility parameters remains the same, but the rating deviation increases.
                postPeriodRatingDeviation = 
                    Math.sqrt(ratingDeviation*ratingDeviation + ratingVolatility*ratingVolatility);
            }
            else {
                // Compute the estimated variance of the player's rating based only
                // on game outcomes.
                double variance = 0;
                Iterator<PairWiseGameResult> gameResults = periodGameResults.get(playerId).iterator();
                while (gameResults.hasNext()) {
                    PairWiseGameResult gameResult = (PairWiseGameResult)gameResults.next();
                    PlayerRating opponentPlayerRating = (PlayerRating)prePeriodRatings.get(gameResult.getOpponentId());
                    double opponentRating = opponentPlayerRating.getGlicko2Rating();
                    double opponentRatingDeviation = opponentPlayerRating.getGlicko2RatingDeviation();

                    double g = g(opponentRatingDeviation);
                    double E = E(rating, opponentRating, opponentRatingDeviation);

                    variance += (g*g) * E * (1 - E);
                }
                variance = 1 / variance;

                // Compute the estimated improvement in rating by comparing the pre-period
                // rating to the performance rating based only on game outcomes
                double performanceRatingFromGameOutcomes = 0;
                gameResults = periodGameResults.get(playerId).iterator();
                while (gameResults.hasNext()) {
                    PairWiseGameResult gameResult = (PairWiseGameResult)gameResults.next();
                    PlayerRating opponentPlayerRating = (PlayerRating)prePeriodRatings.get(gameResult.getOpponentId());
                    double opponentRating = opponentPlayerRating.getGlicko2Rating();
                    double opponentRatingDeviation = opponentPlayerRating.getGlicko2RatingDeviation();

                    double g = g(opponentRatingDeviation);
                    double E = E(rating, opponentRating, opponentRatingDeviation);

                    performanceRatingFromGameOutcomes += g * (gameResult.getScore() - E);
                }
                double improvement = variance * performanceRatingFromGameOutcomes;

                // Determine the new value of the volatility using iteration.
                double t = ratingDeviation;
                double s = ratingVolatility;
                double v = variance;
                double D = improvement;
                double a = Math.log(s*s);
                double x = a;
                double prevX = 0;
                double ex = Math.exp(x);
                do {
                    double d = (t*t) + v + ex;
                    double h1 = -(x-a)/(T*T) - 0.5*ex/d + 0.5*ex*((D/d)*(D/d));
                    double h2 = -1/(T*T) - 0.5*ex*((t*t)+v)/(d*d) + 0.5*(D*D)*ex*((t*t)+v-ex)/(d*d*d);
                    prevX = x;
                    x = x - h1/h2;
                }
                while (Math.abs(x - prevX) > .000001);

                postPeriodRatingVolatility = Math.exp(x/2);

                // Update the rating deviation to the new pre-rating period value
                double updatedRatingDeviation = Math.sqrt((ratingDeviation*ratingDeviation) + 
                                                          (postPeriodRatingVolatility*postPeriodRatingVolatility));

                // Update the rating and rating deviation to the new, post period values.

                postPeriodRatingDeviation = 1 / Math.sqrt((1/(updatedRatingDeviation*updatedRatingDeviation)) +
                                                          (1/variance));
                
                if (Boolean.valueOf(System.getProperty("jrs.downweightResults", "true")).booleanValue()) {
                    PairWiseGameResultsList pairWiseGameResultsList =
                        (PairWiseGameResultsList)periodGameResults.get(playerId);
                    double ratingDeviationDelta = postPeriodRatingDeviation - ratingDeviation;
                    ratingDeviationDelta *= pairWiseGameResultsList.getWeighting();
                    postPeriodRatingDeviation = ratingDeviation + ratingDeviationDelta;
                }

                performanceRatingFromGameOutcomes = 0;
                gameResults = periodGameResults.get(playerId).iterator();
                while (gameResults.hasNext()) {
                    PairWiseGameResult gameResult = (PairWiseGameResult)gameResults.next();
                    PlayerRating opponentPlayerRating = (PlayerRating)prePeriodRatings.get(gameResult.getOpponentId());
                    double opponentRating = opponentPlayerRating.getGlicko2Rating();
                    double opponentRatingDeviation = opponentPlayerRating.getGlicko2RatingDeviation();

                    double g = g(opponentRatingDeviation);
                    double E = E(rating, opponentRating, opponentRatingDeviation);

                    performanceRatingFromGameOutcomes += g * (gameResult.getScore() - E);
                }
                postPeriodRating = 
                    rating + (postPeriodRatingDeviation*postPeriodRatingDeviation)*performanceRatingFromGameOutcomes;
                
                if (Boolean.valueOf(System.getProperty("jrs.downweightResults", "true")).booleanValue()) {
                    PairWiseGameResultsList pairWiseGameResultsList =
                        (PairWiseGameResultsList)periodGameResults.get(playerId);
                    double ratingDelta = postPeriodRating - rating;
                    ratingDelta *= pairWiseGameResultsList.getWeighting();
                    postPeriodRating = rating + ratingDelta;
                }
            }
            
            // Convert the ratings and rating deviations back to original scale,
            // then add the new rating to the post period ratings list.
            PlayerRating postPeriodPlayerRating = new PlayerRating(playerId, postPeriodRating, postPeriodRatingDeviation, postPeriodRatingVolatility, true);
            postPeriodPlayerRatings.put(playerId, postPeriodPlayerRating);
        }
        
        long stop = System.currentTimeMillis();
        timeToComputeLatestPlayerRatings = stop - start;
        
        return postPeriodPlayerRatings;
    }
    
    /** Function used internally by the Glicko-2 algorithm.
      * 
      * @param ratingDeviation 
      * @return 
      */
    private double g(double ratingDeviation) {
        return 1 / Math.sqrt(1 + ((3 * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI)));
    }
    
    /** Function used internally by the Glicko-2 algorithm.
      * 
      * @param playerRating 
      * @param opponentRating 
      * @param opponentRatingDeviation 
      * @return 
      */
    private double E(double playerRating, double opponentRating, double opponentRatingDeviation) {
        return 1 / (1 + Math.exp((g(opponentRatingDeviation) * -1) * (playerRating - opponentRating)));
    }
    
    /** Clear each players' list of game results.
      */
    synchronized void clearResults() {
        Iterator<Integer> playerIds = currentPeriodGameResults.keySet().iterator();
        while (playerIds.hasNext()) {
        	Integer playerId = playerIds.next();
            currentPeriodGameResults.put(playerId, new PairWiseGameResultsList());
        }
    }
    
    /** Get a player's rating.
      * 
      * @param playerId 
      *     A unique identifier for the player.
      * @return 
      *     An instance of PlayerRating representing the most recently computed
      *     rating for the player.
      */
    public synchronized PlayerRating getPlayerRating(Integer playerId) {
        return playerRatings.get(playerId);
    }
    
    /**
     * Get the ordered list of players' ratings
     * 
     * @return	
     * 		an SortedSet
     * @author 
     * 		Vincent Labatut
     */
    public synchronized SortedSet<PlayerRating> getSortedPlayerRatings()
    {	SortedSet<PlayerRating> result = new TreeSet<PlayerRating>();
		Iterator<Entry<Integer,PlayerRating>> it = playerRatings.entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,PlayerRating> entry = it.next();
			//int playerId = entry.getKey();
			PlayerRating playerRating = entry.getValue();
			result.add(playerRating);
		}
		return result;
    }
    
    /**
     * Process a player's rank (starting from rank 1, not zero)
     * 
     * @param playerId
      *     A unique identifier for the player.
     * @return
     * 		An integer representing the player's rank
     * @author 
     * 		Vincent Labatut
     */
    public synchronized int getPlayerRank(Integer playerId)
    {	SortedSet<PlayerRating> sortedRatings = getSortedPlayerRatings();
    	int result = 0;
		boolean done = false;
		Iterator<PlayerRating> it = sortedRatings.iterator();
		while(it.hasNext() && !done)
		{	result++;
			PlayerRating playerRating = it.next();
			if(playerRating.getPlayerId().equals(playerId))
				done = true;
		}
		return result;
    }
    
    /** From the list of all registered players, rank them according to their
      * likelyhood to draw against the specified player.
      *
      * @param playerId 
      *     The id of the player for whom the best match is being sought.
      * @return 
      *     A <code>Set</code> of <code>Match</code> objects, ordered from
      *     best to worst.
      */
    public Set<Match> getMatches(Integer playerId) {
        return getMatches(playerId, getPlayers());
    }
        
    /** From the list of all registered players, rank them according to their
      * likelyhood to draw against the specified player and return the top <i>n</i>,
      * where <i>n</i> is specified by the <code>numMatches</code> parameter.
      *
      * @param playerId 
      *     The id of the player for whom the best match is being sought.
      * @param numMatches 
      *     The number of matches to return.
      * @return 
      *     A <code>Set</code> of <code>Match</code> objects, ordered from
      *     best to worst.
      */
    public Set<Match> getMatches(Integer playerId, int numMatches) {
        return getMatches(playerId, getPlayers(), numMatches);
    }
        
    /** From the specified list of players, rank them according to their
      * likelyhood to draw against the specified player and return the 
      * rankings as Match objects.
      * 
      * @param playerId 
      *     The id of the player for whom the best match is being sought.
      * @param playerList
      *     The list of players to rank according to best match. 
      * @return 
      *     A <code>Set</code> of <code>Match</code> objects, ordered from
      *     best to worst.
      */
    public Set<Match> getMatches(Integer playerId, Set<Integer> playerList) {
        return getMatches(playerId, playerList, playerList.size());
    }
        
    /** From the specified list of players, rank them according to their
      * likelyhood to draw against the specified player and return the top <i>n</i>,
      * where <i>n</i> is specified by the <code>numMatches</code> parameter.
      * 
      * @param playerId 
      *     The id of the player for whom the best match is being sought.
      * @param playerList 
      *     The list of players to rank according to best match. 
      * @param numMatches 
      *     The number of matches to return.
      * @return 
      *     A <code>Set</code> of <code>Match</code> objects, ordered from
      *     best to worst.
      */
	public synchronized Set<Match> getMatches(Integer playerId, Set<Integer> playerList, int numMatches) {
        
        TreeSet<Match> matches = new TreeSet<Match>();
        
        PlayerRating playerRating = (PlayerRating)playerRatings.get(playerId);
        
        Iterator<Integer> pids = playerList.iterator();
        while (pids.hasNext()) {
        	Integer pid = pids.next();
            if (!pid.equals(playerId)) {
                PlayerRating pr = (PlayerRating)playerRatings.get(pid);
                double probOfDraw = 
                    calculateProbabilityOfDraw(playerRating.getRating(), playerRating.getRatingDeviation(),
                                               pr.getRating(), pr.getRatingDeviation());
                Match match = new Match(pid, pr.getRating(), pr.getRatingDeviation(), probOfDraw);
                matches.add(match);
            }
        }
        
        if ((matches.size() > 0) && (numMatches < matches.size())) {
            Match last = (Match)(matches.toArray()[numMatches]);
            matches = new TreeSet<Match>(matches.headSet(last));
        }
        
        return matches;
    }
    
    /** Determine the games which are the best match for the specified player.  
      * <p>
      * Specifically, given a list of player lists, compute the probability of
      * a draw between the specified player and each player in each list. For 
      * each list, average the probabilities and create an instance of
      * GameMatch. The GameMatch for each list is returned in order of best
      * match for the player.
      *
      * @param playerId 
      *     The id of the player for whom the best game match is being sought.
      * @param listOfPlayerLists
      *     A List of Sets containing player id Objects. Typically, these Sets
      *     would represent the players in a game that is being gathered.
      * @return
      *     A Set of GameMatch objects, ordered according to the probability
      *     that the specified player would draw against the other players in
      *     the game.
      *
      * @deprecated
      */
    public synchronized Set<GameMatch> getGameMatches(Integer playerId, List<Set<Integer>> listOfPlayerLists) {
        
        TreeSet<GameMatch> gameMatches = new TreeSet<GameMatch>();
        
        Iterator<Set<Integer>> playerLists = listOfPlayerLists.iterator();
        while (playerLists.hasNext()) {
            Set<Integer> playerList = playerLists.next();
            double ratingSum = 0;
            double ratingDeviationSum = 0;
            double probOfDrawSum = 0;
            Set<Match> matches = getMatches(playerId, playerList);
            Iterator<Match> matchesIter = matches.iterator();
            while (matchesIter.hasNext()) {
                Match match = (Match)matchesIter.next();
                ratingSum += match.getRating();
                ratingDeviationSum += match.getRatingDeviation();
                probOfDrawSum += match.getProbabilityOfDraw();
            }
            double aveRating = ratingSum / matches.size();
            double aveRatingDeviation = ratingDeviationSum / matches.size();
            double aveProbOfDraw = probOfDrawSum / matches.size();
            Game game = new DefaultGame(1, new HashSet<Integer>(playerList));
            GameMatch gameMatch = new GameMatch(game, aveRating, aveRatingDeviation, aveProbOfDraw);
            gameMatches.add(gameMatch);
        }
        
        return gameMatches;
    }
    
    /** Given a list of game participants, create a list of <code>DefaultGame</code> 
      * objects representing the lists, then order the games according to their probability
      * of a draw against the specified player.
      * <p>
      * This method should be used when it is not possible for a game's state
      * object to implement the <code>jrs.Game</code> interface. If the game's
      * state object does implement this interface, then the 
      * <code>orderByBestMatch(Object, Set)</code> method should be used.
      *
      * @param playerId
      * @param playerLists
      *     A Map of Sets containing the ids of players participating in each game.
      *     The map is indexed by (ie. the key is) an object representing the game's
      *     id.
      * @return
      *     An ordered Set of Game objects.
      */
    public LinkedHashSet<Game> orderByBestMatch(Integer playerId, Map<Integer,Set<Integer>> playerLists) {
        HashSet<Game> games = new HashSet<Game>();
        Iterator<Integer> gameIds = playerLists.keySet().iterator();
        while (gameIds.hasNext()) {
        	Integer gameId = gameIds.next();
            Set<Integer> playerList = playerLists.get(gameId);
            Game game = new DefaultGame(gameId, playerList);
            games.add(game);
        }
        return orderByBestMatch(playerId, games);
    }
    
    /** Given a list of games, order them according to their probability of a draw
      * against the specified player. 
      *
      * @param playerId
      * @param games
      *     A Set of Game objects.
      * @return
      *     An ordered Set of Game objects.
      */
    public synchronized LinkedHashSet<Game> orderByBestMatch(Integer playerId, Set<Game> games) {
        
        TreeMap<Double,Game> orderedGamesList = new TreeMap<Double, Game>();
        
        PlayerRating playerRating = getPlayerRating(playerId);
        
        Iterator<Game> gamesIter = games.iterator();
        while (gamesIter.hasNext()) {
            Game game = (Game)gamesIter.next();
            @SuppressWarnings("unused")
			Integer gameId = game.getId();
            @SuppressWarnings("unused")
			Set<Integer> playerList = game.getParticipantIds();
            double probOfDrawSum = 0;
            Iterator<Integer> participantIds = game.getParticipantIds().iterator();
            while (participantIds.hasNext()) {
            	Integer participantId = participantIds.next();
                PlayerRating participantPlayerRating = getPlayerRating(participantId);
                double probOfDraw = calculateProbabilityOfDraw(playerRating.getRating(), 
                                                               playerRating.getRatingDeviation(),
                                                               participantPlayerRating.getRating(), 
                                                               participantPlayerRating.getRatingDeviation());
                probOfDrawSum += probOfDraw;
            }
            double aveProbOfDraw = probOfDrawSum / game.getParticipantIds().size();
            orderedGamesList.put(new Double(-aveProbOfDraw), game);
        }
        
        return new LinkedHashSet<Game>(orderedGamesList.values());
    }
    
    /** Get the details of how the player matches up against the specified game.
      * 
      * @param playerId 
      *     The ID of the player to match against the game.
      * @param game 
      *     The game to match the player against.
      * @return 
      *     An instance of GameMatch which provides details about the game and
      *     how it matches up against the player.
      */
    public synchronized GameMatch getGameMatchDetails(Integer playerId, Game game) {

        double ratingSum = 0;
        double ratingDeviationSum = 0;
        double probOfDrawSum = 0;

        // Get the Match object that contains the probability
        // of a draw against each of the players in the game's
        // player list. 
        Set<Match> matches = getMatches(playerId, game.getParticipantIds());
        Iterator<Match> matchesIter = matches.iterator();
        while (matchesIter.hasNext()) {
            Match match = (Match)matchesIter.next();
            ratingSum += match.getRating();
            ratingDeviationSum += match.getRatingDeviation();
            probOfDrawSum += match.getProbabilityOfDraw();
        }
        
        // Compute the averages
        double aveRating = ratingSum / matches.size();
        double aveRatingDeviation = ratingDeviationSum / matches.size();
        double aveProbOfDraw = probOfDrawSum / matches.size();
        
        return new GameMatch(game, aveRating, aveRatingDeviation, aveProbOfDraw);
    }
    
    /** Calculate the probability of a draw between two players.
      * 
      * @param m1 
      *     The first player's rating
      * @param s1 
      *     The first player's rating deviation
      * @param m2 
      *     The second player's rating
      * @param s2 
      *     The second player's rating deviation
      * @return 
      *     A number between 0 and 1 indicating the probability that a game
      *     between the two players would result in a draw.
      */
    private double calculateProbabilityOfDraw(double m1, double s1, double m2, double s2) {
        
        double B = Double.parseDouble(System.getProperty("jrs.performanceVarianceAroundPlayerSkill", "50"));
        
        // c**2 = 2(B**2) + s1**2 + s2**2
        double c = Math.sqrt(2*(B*B) + (s1*s1) + (s2*s2));
        
        // d = 2(B**2)/(c**2)
        double d = 2 * (B*B) / (c*c);
        
        // p = exp( -((m1-m2)**2) / (2*(c**2)) ) * sqrt(d)
        double p = Math.exp( -((m1-m2)*(m1-m2)) / (2*(c*c)) ) * Math.sqrt(d);

        return p;
    }
    
    public double calculateProbabilityOfWin(PlayerRating pr1, PlayerRating pr2)
    {	double result = 0;
    
    	UnivariateRealFunction f = new CumulativeDistributionFunction(pr1,pr2);
    	double min = 0; //we consider the rating can't be negative
    	double max = 10000; // seems like a reasonable upper limit
    	LegendreGaussIntegrator i1 = new LegendreGaussIntegrator(5,10000);
    	RombergIntegrator i2 = new RombergIntegrator();
    	SimpsonIntegrator i3 = new SimpsonIntegrator();
    	TrapezoidIntegrator i4 = new TrapezoidIntegrator();
    	try
    	{	
long time = System.currentTimeMillis();    	
    		result = i1.integrate(f,min,max);
long time2 = System.currentTimeMillis();
System.out.println("LegendreGaussIntegrator ("+(time2-time)+"): "+result);
time = time2;
    		i2.integrate(f,min,max);
time2 = System.currentTimeMillis();
System.out.println("RombergIntegrator ("+(time2-time)+"): "+result);    	
time = time2;
    		i3.integrate(f,min,max);
time2 = System.currentTimeMillis();
System.out.println("SimpsonIntegrator ("+(time2-time)+"): "+result);    	
time = time2;
    		i4.integrate(f,min,max);
time2 = System.currentTimeMillis();
System.out.println("TrapezoidIntegrator ("+(time2-time)+"): "+result);    	
		}
    	catch (ConvergenceException e)
    	{	e.printStackTrace();
		}
    	catch (FunctionEvaluationException e)
    	{	e.printStackTrace();
		}
    	catch (IllegalArgumentException e)
    	{	e.printStackTrace();
		}
    	
    	
    	
    	double m = pr1.getRating()-pr2.getRating();
    	double s = Math.sqrt(Math.pow(pr1.getRatingDeviation(),2)+Math.pow(pr2.getRatingDeviation(),2));
    	double temp = -m/(s*Math.sqrt(2));
    	result = 0.5*(1+CumulativeDistributionFunction.erf(temp));
System.out.println("New: "+result);
  	
result = calculateProbabilityOfDraw(pr1.getRating(),pr1.getRatingDeviation(),pr2.getRating(),pr2.getRatingDeviation());    	
System.out.println("New: "+result);
    	
    	
    	return result;
    }
}
