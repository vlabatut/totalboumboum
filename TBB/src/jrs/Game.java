/*
 * Game.java
 *
 */

package jrs;

import java.util.*;

/** An interface that defines the properties of a game that the ranking service
  * makes use of. Clients of the ranking service can design their game state 
  * object to implement this interface, or they can use the default implementation
  * provided by the <code>RankingService.getGameMatches(Object, Map)</code> method.
  *
  * @author Derek Hilder
  */
public interface Game<T> {
    
    /** Get the ID of the game. This should uniquely identify the game to
      * an instance of the ranking service.
      */
    public T getId();
    
    /** The the ids of the players participating in the game.
      *
      * @return
      *     A Set of Objects representing the ids of the partipants.
      */
    public Set<T> getParticipantIds();
    
}
