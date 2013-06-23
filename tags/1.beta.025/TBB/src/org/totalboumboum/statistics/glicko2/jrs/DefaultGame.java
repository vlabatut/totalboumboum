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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** A default implementation of the <code>Game</code> interface. This class
  * can be used to obtain matches from the ranking service when the game's state
  * object cannot implement the <code>Game</code> interface.
  *
  * @see RankingService#orderByBestMatch(Object,Map)
  *
  * @author Derek Hilder
  * @author Vincent Labatut
  */
public class DefaultGame implements Game, Serializable {
    private static final long serialVersionUID = 1L;
    
    /** The game's ID */
    private String id;
    
    /** A Set of Objects representing the IDs of the players participating
      * in the game.
      */
    private HashSet<String> participantIds;
    
    /** Create an instance of a DefaultGame with no participants.
     *  The id is automatically processed
     *  
     * @author Vincent Labatut
     */
   public DefaultGame() {
       this(UUID.randomUUID().toString(), new HashSet<String>());
   }
   
    /** Create an instance of a DefaultGame with no participants.
      * 
      * @param id 
      *     The unique ID to assign to the game.
      */
    public DefaultGame(String id) {
        this(id, new HashSet<String>());
    }
    
    /** Create an instance of a DefaultGame with the specified list of
     * partipants.
     * 
     * @param participantIds 
     *     The IDs of the players participating in the game.
     *     
      * @author Vincent Labatut
    */
   public DefaultGame(Set<String> participantIds) {
       this.id = UUID.randomUUID().toString();
       this.participantIds = new HashSet<String>(participantIds);
   }

    /** Create an instance of a DefaultGame with the specified list of
      * partipants.
      * 
      * @param id 
      *     The unique ID to assign to the game.
      * @param participantIds 
      *     The IDs of the players participating in the game.
      */
    public DefaultGame(String id, Set<String> participantIds) {
        this.id = id;
        this.participantIds = new HashSet<String>(participantIds);
    }

    /** Get the game's ID.
      * 
      * @return 
      *     An Object representing the game's unique ID.
      */
    public String getId() {
        return id;
    }

    /** Get the IDs of the players participating in the game.
      * 
      * @return 
      *     A Set of Objects representing the IDs of the players participating
      *     in the game.
      */
    public Set<String> getParticipantIds() {
        return participantIds;
    }
}
