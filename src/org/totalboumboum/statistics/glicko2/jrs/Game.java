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

import java.util.Set;

/** An interface that defines the properties of a game that the ranking service
  * makes use of. Clients of the ranking service can design their game state 
  * object to implement this interface, or they can use the default implementation
  * provided by the <code>RankingService.getGameMatches(Object, Map)</code> method.
  *
  * @author Derek Hilder
  */
@SuppressWarnings("javadoc")
public interface Game {
    
    /** Get the ID of the game. This should uniquely identify the game to
      * an instance of the ranking service.
      */
    public String getId();
    
    /** The the ids of the players participating in the game.
      *
      * @returns
      *     A Set of Objects representing the ids of the partipants.
      */
    public Set<String> getParticipantIds();
    
}
