/*
 * GameMatch.java
 *
 */

package fr.free.totalboumboum.game.statistics.glicko2.jrs;

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
public class GameMatch<T> implements Comparable<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private Game<T> game;
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
    GameMatch(Game<T> game, double aveRating, double aveRatingDeviation, double aveProbabilityOfDraw) {
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
    @SuppressWarnings("unchecked")
	public int compareTo(T o) {
        GameMatch<T> gm = (GameMatch<T>)o;
        return (new Double(aveProbabilityOfDraw).compareTo(new Double(gm.aveProbabilityOfDraw))) * -1;
    }

    /** Get the ids of the players in the game being matched against.
      * 
      * @return 
      *     A Set of Objects representing the players' ids.
      */
    public Set<T> getPlayerIds() {
        return game.getParticipantIds();
    }

    public Game<T> getGame() {
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
