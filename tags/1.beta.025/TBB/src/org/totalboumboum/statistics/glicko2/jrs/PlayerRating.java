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

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

/** A class representing a player's rating, as computed by the ranking service.
  * <p>
  * The ranking service does not compute true ratings, but rather gives an
  * estimate of the player's rating in the form of a confidence interval. The
  * interval is specified by a <i>mean</i> rating, and a rating <i>deviation</i>
  * which expresses the uncertainty in the rating. Additionally, ratings are
  * assigned a volatility, which reflects how consistently the rating has 
  * changed over time.
  *
  * @author Derek Hilder
  */
public class PlayerRating implements Comparable<PlayerRating>, Serializable {
    private static final long serialVersionUID = 1L;

    private static final double GLICKO2_SCALE = 173.7178;
    
    protected String playerId;
    protected double rating;
    protected double ratingDeviation;
    protected double ratingVolatility;
    protected int roundcount = 0;
    
    
    /** Create a PlayerRating instance using rating parameters specified in the
      * standard Glicko scale.
      *
      * @param playerId 
      *     The unique identifier for the player to whom the rating applies.
      * @param rating 
      *     The mean rating of the player's skill.
      * @param ratingDeviation
      *     The deviation in the rating.
      * @param ratingVolatility 
      *     The volatility of the rating.
      *
      * @todo
      *     Should the player's id be passed in too?
      */
    public PlayerRating(String playerId, double rating, double ratingDeviation, double ratingVolatility) {
        this(playerId, rating, ratingDeviation, ratingVolatility, false);
    }
    
    /** Create a PlayerRating instance using the specified rating parameters.
      * 
      * @param playerId 
      *     The unique identifier for the player to whom the rating applies.
      * @param rating 
      *     The mean rating of the player's skill.
      * @param ratingDeviation
      *     The deviation in the rating.
      * @param ratingVolatility 
      *     The volatility of the rating.
      * @param glicko2Scale 
      *     If <code>true</code>, the parameters are assumed to have been specified
      *     in the Glicko2 scale.
      *
      * @todo
      *     Should the player's id be passed in too?
      */
    public PlayerRating(String playerId, double rating, double ratingDeviation, 
                        double ratingVolatility, boolean glicko2Scale) 
    {
        this.playerId = playerId;
        if (glicko2Scale) {
            setGlicko2Rating(rating);
            setGlicko2RatingDeviation(ratingDeviation);
        }
        else {
            setRating(rating);
            setRatingDeviation(ratingDeviation);
        }
        setRatingVolatility(ratingVolatility);
    }
    
    /** Get the Id of the player being rated.
     * 
     * @return 
     *     An <code>Object</code> that uniquely identifies the player.
     */
   public String getPlayerId() {
       return playerId;
   }

   /** Modify the Id of the player being rated.
    * 
    * @param	newId 
    *     A <code>String</code> that uniquely identifies the player.
    * 
    * @author Vincent Labatut
    */
   public void setPlayerId(String newId)
   {	this.playerId = newId;
   }

    /** Get the mean rating of the player's skill.
      * 
      * @return 
      *     The player's rating.
      */
    public double getRating() {
        return rating;
    }

    /** Get the mean rating of the player's skill in the
      * Glicko2 scale.
      * 
      * @return 
      *     The player's rating, in the Glicko2 scale.
      */
    double getGlicko2Rating() {
        // TO DO: cache this result?
        return (rating - 1500) / GLICKO2_SCALE;
    }
    
    /** Set the player's rating.
      * 
      * @param rating 
      *     A standard glicko rating.
      */
    void setRating(double rating) {
    	
    	// TODO modified by Vincent to avoid incorrect values
        if(Double.isInfinite(rating) || Double.isNaN(rating))
        	rating = Double.parseDouble(System.getProperty("jrs.defaultRating", "1500"));
        else if(rating<0)
        	rating = 0;
        
        this.rating = rating;

    }
    
    /** Set the player's rating in the Glicko2 scale.
      * 
      * @param glicko2Rating 
      *     A Glicko2 rating.
      */
    void setGlicko2Rating(double glicko2Rating) {
        double ratingBis = (GLICKO2_SCALE * glicko2Rating) + 1500;
        
        // TODO added by Vincent to force a check on the rating value
        setRating(ratingBis);
    }

    /** Get the rating deviation.
      * 
      * @return 
      *     A standard Glicko rating deviation.
      */
    public double getRatingDeviation() {
        return ratingDeviation;
    }
    
    /** Get the rating deviation in the Glicko2 scale.
      * 
      * @return 
      *     A Glicko2 rating deviation.
      */
    double getGlicko2RatingDeviation() {
        // TO DO: cache this result?
        return ratingDeviation / GLICKO2_SCALE;
    }

    /** Set the rating deviation.
      * 
      * @param ratingDeviation 
      *     A standard Glicko rating deviation.
      */
    void setRatingDeviation(double ratingDeviation) {
        
    	// TODO modified by Vincent to avoid incorrect values
        if(Double.isInfinite(ratingDeviation) || Double.isNaN(ratingDeviation) || ratingDeviation<0)
            ratingDeviation = Double.parseDouble(System.getProperty("jrs.defaultRatingDeviation", "350"));
        
        this.ratingDeviation = ratingDeviation;
    }
    
    /** Set the rating deviation in the Glicko2 scale.
      * 
      * @param ratingDeviation 
      *     A Glicko2 rating deviation.
      */
    void setGlicko2RatingDeviation(double glicko2RatingDeviation) {
        double ratingDeviationBis = GLICKO2_SCALE * glicko2RatingDeviation;
        
        // TODO added by Vincent to force a check on the deviation value
        setRatingDeviation(ratingDeviationBis);
 
    }

    /** Get the rating volatility.
      * 
      * @return 
      *     A rating volatility.
      */
    public double getRatingVolatility() {
        return ratingVolatility;
    }

    /** Set the rating volatility.
      * 
      * @param ratingVolatility 
      *     A rating volatility.
      */
    void setRatingVolatility(double ratingVolatility) {
    	
    	// TODO modified by Vincent to avoid incorrect values
        if(Double.isInfinite(ratingVolatility) || Double.isNaN(ratingVolatility) || ratingVolatility<0)
        	ratingVolatility = Double.parseDouble(System.getProperty("jrs.defaultRatingVolatility", "0.06"));
        
        this.ratingVolatility = ratingVolatility;
    }
    
    /**
     * Returns the number of rounds played since the last rating update
     * 
     * @return
     * 		an integer corresponding to a number of rounds
     * @author
     * 		Vincent
     */
    public int getRoundcount()
    {	return roundcount;    	
    }
    
    /**
     * Increments the number of rounds played since the last rating update
     * 
     * @author
     * 		Vincent
     */
    public void incrementRoundcount()
    {	roundcount++;    	
    }
    
    /**
     * Reset to zero the number of rounds played since the last rating update
     * 
     * @author
     * 		Vincent
     */
    public void reinitRoundcount()
    {	roundcount = 0;    	
    }
    
    /** Determine if the object is equals to this <code>PlayerRating</code> object.
      * 
      * @param o 
      *     The object to compare to.
      * @return 
      *     <code>true</code> if the objects are equal.
      */
    public boolean equals(Object o) {
        boolean result = false;
    	if(o instanceof PlayerRating)
        {	PlayerRating other = (PlayerRating)o;
        	result = compareTo(other)==0;
        }
        return result;
    }
    
    
    /** Compare the object to this <code>PlayerRating</code> object. The comparison
      * first considers the rating, and if both ratings are the same, then it
      * compares the rating deviation. An object with a larger rating deviation
      * is considered to be less than an object with a smaller rating deviation.
      * If both the rating and the rating deviation are equal, then the player Id
      * is compared. If the player Id object does not implement <code>Comparable</code>,
      * then the two objects are considered equal.
      * 
      * @param o 
      *     The object to compare to.
      * @return 
      *     <code>-1</code> if this object is less than the specified object,
      *     <code>0</code> if it is equal to the specified object, and
      *     <code>1</code> if it is greater than the specified object.
      */
    public int compareTo(PlayerRating other) {
        if (this.rating < other.rating) {
            return 1;
        }
        else if (this.rating > other.rating) {
            return -1;
        }
        else {
            if (this.ratingDeviation < other.ratingDeviation) {
                return -1;
            }
            else if (this.ratingDeviation > other.ratingDeviation) {
                return 1;
            }
            else {
                if (this.playerId instanceof Comparable) {
                    return playerId.compareTo(other.playerId);
                }
                else {
                    return 0;
                }
            }
        }
    }

    /**
     * export stats data to a text file, in order to make stat classes changes easier
     * @author 
     * 		Vincent
     */
    public void exportToText(PrintWriter writer)
    {	writer.print(playerId);
    	writer.print(";"+rating);
    	writer.print(";"+ratingDeviation);
    	writer.print(";"+ratingVolatility);
    	writer.print(";"+roundcount);
    	writer.println();
    }

    /**
     * import stats data from a text file, in order to make stat classes changes easier
     * @author 
     * 		Vincent
     */
	public void importFromText(Scanner scanner)
	{	String text = scanner.nextLine();
		String texts[] = text.split(";");
		int t = 0;

		playerId = texts[t++];
		rating = Double.parseDouble(texts[t++]);
		ratingDeviation = Double.parseDouble(texts[t++]);
		ratingVolatility = Double.parseDouble(texts[t++]);
		roundcount = Integer.parseInt(texts[t++]);
	}
}
