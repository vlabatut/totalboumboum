package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.ErdemTayyar;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Crée un nouveau critère entier, pour les murs destructibles.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 * 
 * 
 */
@SuppressWarnings("deprecation")
public class MurDestructible extends AiUtilityCriterionInteger<ErdemTayyar> {
	/**
	 * name
	 *  */
	public static final String NAME = "MurDestructible";

	// Constructor

	/**
	 * 
	 * C'est un critere a 3 valeur. On initialise la valeur dont la domaine de
	 * définition est 0 ,1 ou 2. 0 s'İl n'y a pas de mur, 1 s'il existe 1, et 2 s'İl y a plusieurs murs destructibles.
	 * 
	 * @param ai
	 * 		information manquante !?	
	 * @throws StopRequestException
	 * 		information manquante !?	
	 */
	public MurDestructible(ErdemTayyar ai) throws StopRequestException {
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

		
		if (count == 1)
			result = 1;
		else if (count == 2 || count == 3)
			result = 2;

		return result;
	}
}