package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v1;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;


/**
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * Classe gérant le calcul des valeurs de préférence de l'agent. En particulier, elle implémente la méthode 
 update de l'algorithme général. 
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent> {
	/**
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		
		verbose = false;

		
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
 *les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
* resetData(). 
*Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
 l'appeler.
	 */
	protected void resetCustomData() {
		ai.checkInterruption();

		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
 les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
 resetData(). 
Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
 l'appeler.
	 */
	protected Set<AiTile> selectTiles() {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		
		List<AiTile> tiles = ai.getZone().getTiles();
		

		for (AiTile tiless : tiles) {
			ai.checkInterruption();
			if (tiless.isCrossableBy(ai.getZone().getOwnHero()))
				result.add(tiless);

		}

		

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CATEGORY /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Nom de la 1ère catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_1 = "La Coince";
	/**
	 * Nom de la 2ème catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_2 = "La Sortie";
	
	
	

	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne seulement 
 les structures de données créées par le concepteur : les structures imposées sont réinitialisées par 
 resetData(). 
Cette méthode est appelée automatiquement, vous (le concepteur de l'agent) n'avez pas besoin de 
 l'appeler.
	 */
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		AiCategory result = getCategory(CAT_NAME_1);
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		int distance = 0;
		List<AiBomb> bombs=ai.getZone().getBombs();

		if (mode == AiMode.ATTACKING)
			
			for (AiHero adversaire : ai.getZone().getRemainingOpponents()) {
				ai.checkInterruption();
				distance = ai.getZone().getTileDistance(
						ai.getZone().getOwnHero().getTile(),
						adversaire.getTile());
			
				
				if(bombs==null && distance<=10) result = getCategory(CAT_NAME_2);
				else  result = getCategory(CAT_NAME_1);
				

		
			}
		
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire. 
Ici, on affiche la valeur numérique de la préférence dans chaque case, et on colorie la case en fonction de 
 cette valeur : la couleur dépend du mode (bleu pour la collecte, rouge pour l'attaque) et son intensité 
 dépend de la préférence (clair pour une préférence faible, foncé pour une préférence élevée). 
Cette méthode peut être surchargée si vous voulez afficher les informations différemment, ou d'autres 
 informations. A noter que cette méthode n'est pas appelée automatiquement : elle doit l'être par la 
 fonction surchargeant ArtificialIntelligence.updateOutput() si vous désirez l'utiliser. 
	 */
	public void updateOutput() {
		ai.checkInterruption();

		
		super.updateOutput();

	
	}
}
