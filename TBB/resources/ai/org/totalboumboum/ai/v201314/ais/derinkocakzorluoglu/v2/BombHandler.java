package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2.criterion.HasItems;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class BombHandler extends AiBombHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		
	/*
Premièrement nous allons regarder "Si la case occupée par notre agent contient déjà une bombe " ou pas . Si elle contient
alors on ne va pas poser une bombe la bas car c’est redondant. Deuxièmement "Si l’agent n’a pas la
capacité de poser une bombe (par exemple parce qu’il a déjà posé le maximum autorisé)" alors notre
agent va continuer son déplacement sans posant du bombe. Troisièmement on va regarder si on est dans la case
qui a le plus petit valeur préférentiel. ATTENTION : On a change ça . Notre bomberman dans notre 
rapport precedent ne marchait pas tout a fait dans la meme maniere .
	 */
		ai.checkInterruption();
		boolean result = false;
		AiHero myHero = ai.getZone().getOwnHero();
		Map<Integer, List<AiTile>> preferences = ai.preferenceHandler.getPreferencesByValue();
		Map<AiTile, Integer> preferencese = ai.preferenceHandler.getPreferencesByTile();
		int minPref = Collections.min(preferences.keySet());
		if (myHero.getTile().getBombs().isEmpty()) {
			if (myHero.getBombNumberLimit() - myHero.getBombNumberCurrent() > 0) {
				
				if (preferencese.get(myHero.getTile()) == minPref) {
					HasItems hi = new HasItems(ai);
					//on va utiliser ça dans la collecte mode , car notre
					//destination sera egal a la case ou il ya un item 
					//on ne va pas exploiter l'item
					if (hi.fetchValue(myHero.getTile()) != 0) {
						result = true;
					}
					if(myHero.getBombRange()>ai.getZone().getTileDistance(myHero.getTile(),ai.nearestEnemy().getTile())){
						result =true;
					}
				}
			}
		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}
