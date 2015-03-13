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
import java.util.*;

/** A list of <code>PairWiseGameResult</code> objects. It extends 
  * <code>ArrayList</code> to provide support for computing the weighting of
  * the results.
  *
  * @author Derek Hilder
  */
@SuppressWarnings("javadoc")
class PairWiseGameResultsList extends ArrayList<PairWiseGameResult> {
    private static final long serialVersionUID = 1L;
    
    private int numberOfGamesPlayed = 0;
    private int numberOfPairWiseGameResults = 0;
    
    /**
      * 
      * @param o 
      * @return 
      */
    @Override
	public boolean add(PairWiseGameResult o) {
        numberOfPairWiseGameResults++;
        return super.add(o);
    }
    
    /**
      */
    public void incrementNumberOfGamesPlayed() {
        numberOfGamesPlayed++;
    }
    
    /**
      * 
      * @return 
      */
    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }
    
    /**
      * 
      * @return 
      */
    public int getNumberOfPairWiseGameResults() {
        return numberOfPairWiseGameResults;
    }
    
    /**
      * 
      * @return 
      */
    public double getWeighting() {
        return (double)numberOfGamesPlayed/numberOfPairWiseGameResults;
    }
    
    /**
     * export stats data to a text file, in make stat classes changes easier
     * @author 
     * 		Vincent
     */
    public void exportToText(PrintWriter writer)
    {	writer.print(numberOfGamesPlayed);
		writer.print(";"+numberOfPairWiseGameResults);
    	writer.println();
    	
    	for(PairWiseGameResult result: this)
    		result.exportToText(writer);
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
		numberOfGamesPlayed = Integer.parseInt(texts[t++]);
		numberOfPairWiseGameResults = Integer.parseInt(texts[t++]);

		for(int i=0;i<numberOfPairWiseGameResults;i++)
		{	PairWiseGameResult result = new PairWiseGameResult("",0);
			result.importFromText(scanner);
			super.add(result);
		}
	}
}
