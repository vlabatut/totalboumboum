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

import java.util.Iterator;

/** A subclass of <code>RankingService</code> that updates players' ratings
  * a regular intervals.
  * <p>
  * Additional methods are provided for this type of ranking service to allow
  * for starting, stopping, pausing and resuming the updates.
  *
  * @author Derek Hilder
  */
public class TimerBasedRankingService extends RankingService {
    private static final long serialVersionUID = 1L;

	/** The thread that periodically requests rating period updates. */
    private Thread updateTimer;
    
    /** The number of seconds between periodic updates. If 0, rating 
      * period updates will be triggered after a certain number of games.
      */
    private long updatePeriod;
    
    /** Flag used to indicate that the periodic update timer is running. */
    private boolean updating;
    
    /** Flag used to pause and resume the periodic updating. */
    private boolean paused;
    
    
    /** Creates an instance of the Ranking Service that updates player ratings
      * at regular intervals. Updating will not begin util the <code>startUpdating</code>
      * method is called.
      *
      * @param updatePeriod 
      *     The number of seconds between rating periods.
      */
    public TimerBasedRankingService(long updatePeriod) {
        
        super();
        
        this.updatePeriod = updatePeriod;

        // Start a timer thread to do periodic updates of player ratings
        updateTimer = new Thread() {
            public void run() {
                updating = true;
                while (updating) {

                    if (!paused) {
                        // Notify listeners that a new period is beginning
                        Iterator<RankingServiceListener> listenersIter = listeners.iterator();
                        while (listenersIter.hasNext()) {
                            RankingServiceListener listener = (RankingServiceListener)listenersIter.next();
                            listener.beginRatingPeriod(TimerBasedRankingService.this, periodCount);
                        }
                    }

                    try {
                        Thread.sleep(TimerBasedRankingService.this.updatePeriod * 1000);
                    }
                    catch (InterruptedException ie) {
                    }

                    if (!paused) {

                        playerRatings = computePlayerRatings(playerRatings, currentPeriodGameResults);
                        clearResults();

                        // Notify listeners that the period has ended.
                        Iterator<RankingServiceListener> listenersIter = listeners.iterator();
                        while (listenersIter.hasNext()) {
                            RankingServiceListener listener = (RankingServiceListener)listenersIter.next();
                            listener.endRatingPeriod(TimerBasedRankingService.this, periodCount);
                        }

                        periodCount++;
                    }
                }
            }
        };
    }
    
    /** Request that the ranking service end the current rating period. 
      * This will cause the ranking service to compute new player ratings based
      * on the results posted during the period, then a new rating period will
      * begin.
      */
    public void endPeriod() {
        updateTimer.interrupt();
    }
    
    /** Start performing periodic updates of the player ratings. This method
      * should be called after instatiating the RankingService with an
      * updatePeriod greater than 0.
      */
    public void startUpdating() {
        updateTimer.start();
    }
    
    /** Stop performing periodic updates of the player ratings. Once updating
      * has been stopped, it cannot be started again.
      */
    public void stopUpdating() {
        updating = false;
        try {
            updateTimer.join();
        }
        catch (InterruptedException ie) {
        }
    }
    
    /** Suspend periodic updating of the player ratings. This only applies to
      * RankingService instances whose updatePeriod is greater than 0.
      */
    public void pauseUpdating() {
        paused = true;
    }
    
    /** Resume periodic updating of the player ratings. This only applies to
      * RankingService instances whose updatePeriod is greater than 0.
      */
    public void resumeUpdating() {
        paused = false;
    }
}
