/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.asilizeryuce.v4.Agent;
/**
 * Cette critere est pour savoir est-ce qu'on peut entrer la categorie demi-triangle.
 * elle nous donne les valeur preferences selon les positions ou on peut poser des bombes.
 * la valeur 0 est pour la case ou on peut poser la premiere bombe,  1 est est pour la 
 * case ou on peut poser la deuxieme bombe et 2 pour les cases qu'on ne peut pas poser 
 * des bombes selon cette critere
 * 
 * @author Emre Asil
 * @author Tülin İzer
 * @author Miray Yüce
 *
 */
public class Triangle extends AiCriterionInteger<Agent>{
	/** Nom de ce critère */
	public static final String NAME = "TRIANGLE";
	
	/**
	 * Crée un critère entier.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public Triangle(Agent ai) {
		super(ai, NAME, 0, 2);
		ai.checkInterruption();
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) {
		ai.checkInterruption();

		int result = 2;
		
		
		if(tile.equals(ai.enemyHandler.bombFirst) && tile.getBombs().isEmpty()) return 0;
		
		if(tile.equals(ai.enemyHandler.bombSecond)&& tile.getBombs().isEmpty()) return 1;

		return result;
	}
}
