package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Dogus Burcu Demirag
 * @author Zeynep Sagar
 *
 */
public class Situation {

	int score = 0;
	Direction d;

	// AiAction action;

	public Situation(int score, Direction d) {
		this.score = score;
		this.d = d;
	}

}
