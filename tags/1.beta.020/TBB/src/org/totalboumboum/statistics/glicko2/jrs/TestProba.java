package org.totalboumboum.statistics.glicko2.jrs;

/** 
 *
 * @author Derek Hilder
 */
public class TestProba {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	
		RankingService rankingService = new RankingService();

		// player 1
		double mean1 = 1500;
		double stdev1 = 50;
		PlayerRating pr1 = new PlayerRating("1",mean1,stdev1,0);
		
		// player 2
		double mean2 = 1600;
		double stdev2 = 50;
		PlayerRating pr2 = new PlayerRating("2",mean2,stdev2,0);
		
		rankingService.calculateProbabilityOfWin(pr1, pr2);
	}

}
