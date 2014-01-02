package org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v1.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/** 
 * Cette critere entier pour savoir que est-ce que la case est 
 * propre de tuer l'ennemi, qu'on veut mettre une bombe 
 *  
 * @author Emre Asıl 
 * @author Tülin İzer 
 * @author Miray Yüce 
 */
public class EnemyInDanger extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "ENNEMI_EN_DANGER";

	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public EnemyInDanger(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS 		     /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();
		int result = 2;

		if (this.ai.getEnemyInColumn(tile) == 0)
			return 0;

		if (this.ai.getEnemyInRow(tile) == 0)
			return 0;

		if (this.ai.getEnemyInRow(tile.getNeighbor(Direction.DOWN)) == 0)
			return 1;
		
		if (this.ai.getEnemyInRow(tile.getNeighbor(Direction.UP)) == 0)
			return 1;
		
		if (this.ai.getEnemyInColumn(tile.getNeighbor(Direction.LEFT)) == 0)
			return 1;
		
		if (this.ai.getEnemyInColumn(tile.getNeighbor(Direction.RIGHT)) == 0)
			return 1;

		return result;
	}
}
