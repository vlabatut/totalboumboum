/*
 * DefaultGame.java
 *
 */

package fr.free.totalboumboum.game.statistics.glicko2.jrs;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/** A default implementation of the <code>Game</code> interface. This class
  * can be used to obtain matches from the ranking service when the game's state
  * object cannot implement the <code>Game</code> interface.
  *
  * @see RankingService#orderByBestMatch(Object,Map)
  *
  * @author Derek Hilder
  */
public class DefaultGame<T> implements Game<T>, Serializable {
    private static final long serialVersionUID = 1L;

	/** The game's ID */
    private T id;
    
    /** A Set of Objects representing the IDs of the players participating
      * in the game.
      */
    private HashSet<T> participantIds;
    
    /** Create an instance of a DefaultGame with no participants.
      * 
      * @param id 
      *     The unique ID to assign to the game.
      */
    public DefaultGame(T id) {
        this(id, new HashSet<T>());
    }
    
    /** Create an instance of a DefaultGame with the specified list of
      * partipants.
      * 
      * @param id 
      *     The unique ID to assign to the game.
      * @param participantIds 
      *     The IDs of the players participating in the game.
      */
    public DefaultGame(T id, Set<T> participantIds) {
        this.id = id;
        this.participantIds = new HashSet<T>(participantIds);
    }

    /** Get the game's ID.
      * 
      * @return 
      *     An Object representing the game's unique ID.
      */
    public T getId() {
        return id;
    }

    /** Get the IDs of the players participating in the game.
      * 
      * @return 
      *     A Set of Objects representing the IDs of the players participating
      *     in the game.
      */
    public Set<T> getParticipantIds() {
        return participantIds;
    }
}
