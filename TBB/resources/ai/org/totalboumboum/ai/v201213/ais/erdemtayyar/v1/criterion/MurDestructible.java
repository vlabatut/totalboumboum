package org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.ErdemTayyar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Crée un nouveau critère entier, pour les murs destructibles.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 * 
 * 
 */
public class MurDestructible extends AiUtilityCriterionInteger<ErdemTayyar> {
	/** */
	public static final String NAME = "MurDestructible";

	
	//Constructor

	/**
	 * 
	 *
	 * C'est un critere binaire.
	 * On initialise la valeur dont la domaine de définition est  TRUE et FALSE.
	 * Si on a besoin d'un item sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public MurDestructible(ErdemTayyar ai) throws StopRequestException { // init
																			// nom
																			// +
																			// bornes
																			// du
																			// domaine
																			// de
																			// définition
		super(ai, NAME, 0, 2);
		ai.checkInterruption();

	}


	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int count = 0;
		int result = 0;

		for (AiBlock currentBlock : tile.getNeighbor(Direction.UP).getBlocks()) {
			ai.checkInterruption();
			if (currentBlock.isDestructible())
				count++;
		}
		for (AiBlock currentBlock : tile.getNeighbor(Direction.DOWN)
				.getBlocks()) {
			ai.checkInterruption();
			if (currentBlock.isDestructible())
				count++;
		}
		for (AiBlock currentBlock : tile.getNeighbor(Direction.LEFT)
				.getBlocks()) {
			ai.checkInterruption();
			if (currentBlock.isDestructible())
				count++;
		}
		for (AiBlock currentBlock : tile.getNeighbor(Direction.RIGHT)
				.getBlocks()) {
			ai.checkInterruption();
			if (currentBlock.isDestructible())
				count++;
		}

		if (count == 0)
			result = 0;
		else if (count == 1)
			result = 1;
		else if (count == 2 || count == 3)
			result = 2;

		return result;
	}
}