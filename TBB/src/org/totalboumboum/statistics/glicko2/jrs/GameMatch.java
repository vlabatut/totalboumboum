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

import java.io.Serializable;
import java.util.Set;

/** This class encapsulates information about how a player matches up against the
  * players in a game.
  * <p>
  * Typically, the game would be in the process of being gathered, and an instance
  * of this class would be retrieved from the RankingService.getGameMatchDetails method
  * by the game server to determine if the game is suitable for a player. 
  *
  * @author Derek Hilder
  */
public class GameMatch implements Comparable<GameMatch>, Serializable {
    private static final long serialVersionUID = 1L;

    private Game game;
    private double aveRating;
    private double aveRatingDeviation;
    private double aveProbabilityOfDraw;

    /** Create a GameMatch object.
      * 
      * @param aveRating 
      *     The average rating of the players in the game being matched.
      * @param aveRatingDeviation 
      *     The average rating deviation of the players in the game being matched.
      * @param aveProbabilityOfDraw 
      *     The average probability that each player in the game would draw
      *     against the player requesting the matche.
      */
    GameMatch(Game game, double aveRating, double aveRatingDeviation, double aveProbabilityOfDraw) {
        this.game = game;
        this.aveRating = aveRating;
        this.aveRatingDeviation = aveRatingDeviation;
        this.aveProbabilityOfDraw = aveProbabilityOfDraw;
    }

    /** Compare this object to specified object. Specifically, if this object
      * has a greater average probability of a draw than the other object, it will
      * be considered greater. If the average probability of a draw is less than the
      * other object's, then it will be considered less. If both objects have
      * the same average probability of a draw, then they are considered equal.
      * 
      * @param o 
      *     The object to compare this object to.
      * @return 
      *     An int less than 0 indicates this object is less than the object specified.
      *     An int greater than 0 indicates this object is greater than the object specified.
      *     0 indicates the objects are equal.
      */
    public int compareTo(GameMatch gm) {
        return (new Double(aveProbabilityOfDraw).compareTo(new Double(gm.aveProbabilityOfDraw))) * -1;
    }

    /** Get the ids of the players in the game being matched against.
      * 
      * @return 
      *     A Set of Objects representing the players' ids.
      */
    public Set<String> getPlayerIds() {
        return game.getParticipantIds();
    }

    public Game getGame() {
        return game;
    }
    
    /** Get the average rating of each player in the game being matched against.
      * 
      * @return 
      *     The average rating.
      */
    public double getAveRating() {
        return aveRating;
    }

    /** Get the average rating deviation of each player in the game being matched against.
      * 
      * @return 
      *     The average rating deviation.
      */
    public double getAveRatingDeviation() {
        return aveRatingDeviation;
    }

    /** Get the average probability that each player in the game would draw 
      * against the player requesting the matche.
      * 
      * @return 
      *     The average probability of a draw.
      */
    public double getAveProbabilityOfDraw() {
        return aveProbabilityOfDraw;
    }
    
}
