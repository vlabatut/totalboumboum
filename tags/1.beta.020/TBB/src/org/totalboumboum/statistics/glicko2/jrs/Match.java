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

/** This class encapsulates information about how a player matches up against the
  * player that requested the match. 
  *
  * @author Derek Hilder
  */
public class Match implements Comparable<Match>, Serializable {
    private static final long serialVersionUID = 1L;

    private String playerId;
    private double rating;
    private double ratingDeviation;
    private double probabilityOfDraw;

    /** Create a Match object.
      * 
      * @param playerId 
      *     The id of the player being matched.
      * @param rating 
      *     The player's rating.
      * @param ratingDeviation 
      *     The player's rating deviation.
      * @param probabilityOfDraw 
      *     The proability that the player requesting the match will 
      *     draw against the player being matched.
      */
    Match(String playerId, double rating, double ratingDeviation, double probabilityOfDraw) {
        this.playerId = playerId;
        this.rating = rating;
        this.ratingDeviation = ratingDeviation;
        this.probabilityOfDraw = probabilityOfDraw;
    }

    /** Determine if this Match object is equivalent to the specified Object.
      * 
      * @param o 
      *     The Object to compare to.
      * @return 
      *     <code>true</code> if the objects are equal.
      */
    public boolean equals(Object o) {
        boolean result = false;
    	if(o instanceof Match)
        {	Match m = (Match)o;
        	result = compareTo(m)==0;
        }
    	return result;
    }

    /** Compare this object to specified object. Specifically, if this object
      * has a greater probability of a draw than the other object, it will
      * be considered greater. If the probability of a draw is less than the
      * other objects, then it will be considered less. If both objects have
      * the same probability of a draw, then they are considered equal.
      * 
      * @param o 
      *     The object to compare this object to.
      * @return 
      *     An int less than 0 indicates this object is less than the object specified.
      *     An int greater than 0 indicates this object is greater than the object specified.
      *     0 indicates the objects are equal.
      */
    public int compareTo(Match m) {
        return (new Double(probabilityOfDraw).compareTo(new Double(m.probabilityOfDraw))) * -1;
    }

    /** Get the id of the player being matched against.
      * 
      * @return 
      *     A player id.
      */
    public String getPlayerId() {
        return playerId;
    }

    /** Get the rating of the player being matched against.
      * 
      * @return 
      *     A rating.
      */
    public double getRating() {
        return rating;
    }

    /** Get the rating deviation of the player being matched against.
      * 
      * @return 
      *     A rating deviation.
      */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /** Get the probability of a draw between the player requesting the
      * match and the player being matched against.
      * 
      * @return 
      *     The probability of a draw.
      */
    public double getProbabilityOfDraw() {
        return probabilityOfDraw;
    }
}
    
