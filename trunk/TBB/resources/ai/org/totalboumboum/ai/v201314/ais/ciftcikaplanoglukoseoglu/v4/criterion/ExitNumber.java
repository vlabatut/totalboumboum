package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.criterion;

import java.util.*;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v4.Agent;

/**
 * Cette classe va nous permettre de traiter une case par rapport au critere
 * binaire "Nombre de sorties". Ce critere compte le nombre de
 * cases traversables adjacentes a la case traitée. Le critére est une valeur int.
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class ExitNumber extends AiCriterionInteger<Agent> {
	/** Nom de ce critère */
	public static final String NAME = "EXIT_NUMBER";

	

	/**
	 * Crée le critère entier "Nombre de sorties". Ce critere compte le nombre de
	 * cases traversables adjacentes a la case traitée. Le critére est une valeur int.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */

	public ExitNumber(Agent ai) {
		super(ai, NAME, 0, 4);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param tile
	 *            la case concernée.
	 * @return result renvoie la valeur entier qui est obtenu par le traitement.
	 *         Ici, la valeur est le nombre de cases adjacentes a la case
	 *         concerné qui sont traversable par notre agent.
	 */
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int result = 0;

		/*
		 * On regarde le nombre de sorties(de case voisines qui n'ont ni de mur
		 * ni de bombe) de la case traitée.
		 */
		List<AiTile> m = tile.getNeighbors();
		for (int i = 0; i < 4; i++) {
			ai.checkInterruption();
			if (m.get(i).getBombs().isEmpty()&&m.get(i).getBlocks().isEmpty() && result < 5) {
				result++;
			}
		}

		return result;
	}
}
