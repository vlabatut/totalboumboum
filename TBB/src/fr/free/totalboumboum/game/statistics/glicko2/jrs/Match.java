/*
 * Match.java
 *
 */

package fr.free.totalboumboum.game.statistics.glicko2.jrs;

import java.io.Serializable;

/** This class encapsulates information about how a player matches up against the
  * player that requested the match. 
  *
  * @author Derek Hilder
  */
public class Match<T> implements Comparable<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private T playerId;
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
    Match(T playerId, double rating, double ratingDeviation, double probabilityOfDraw) {
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
        return (compareTo(o) == 0);
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
    @SuppressWarnings("unchecked")
	public int compareTo(Object o) {
        Match<T> m = (Match<T>)o;
        return (new Double(probabilityOfDraw).compareTo(new Double(m.probabilityOfDraw))) * -1;
    }

    /** Get the id of the player being matched against.
      * 
      * @return 
      *     A player id.
      */
    public Object getPlayerId() {
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
    
