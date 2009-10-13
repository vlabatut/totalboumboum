package fr.free.totalboumboum.game.statistics.glicko2;

import java.io.BufferedReader;
import java.io.FileReader;

import jrs.ResultsBasedRankingService;

public class InitGlicko {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	
		ResultsBasedRankingService rankingService = new ResultsBasedRankingService();
		
		BufferedReader br = new BufferedReader(new FileReader(dataStorePath));
		String line;
		while ((line = br.readLine()) != null) {
		String[] fields = line.split("\\|");
		String playerId = fields[0];
		String password = fields[1];
		double rating = Double.parseDouble(fields[2]);
		double ratingDeviation = Double.parseDouble(fields[3]);
		double ratingVolatility = Double.parseDouble(fields[4]);
		userCredentials.put(playerId, password);
		PlayerRating playerRating =
		new PlayerRating(playerId, rating, ratingDeviation,
		ratingVolatility);
		rankingService.registerPlayer(playerId, playerRating);
		}
		
	}

}
