package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2;

import java.util.ArrayList;

import java.util.Map;


import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;

import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;




/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
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

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;

	}
/**
 * la valeur qui est retournée par la fonction considerBombing.
 */
	boolean result;

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();
		AiZone zone = ai.getZone();

		result = false;
		Map<AiTile, Integer> zonepref = ai.getPreferenceHandler()
				.getPreferencesByTile();
		AiHero ownHero = zone.getOwnHero();
		AiTile herotile = ownHero.getTile();

		int value = zonepref.get(herotile);
		
		if (herotile.getBombs().isEmpty()
				&& (ownHero.getBombNumberCurrent() != ownHero
						.getBombNumberMax())) {
			//Si l'adversaire est innacessible
			if (ai.preferenceHandler.flag == true)
				//Si la prochaine case sur le chemin contient un mur
				if (ai.moveHandler.futiletorun == true)
					result = true;
			//on fait une simulation pour voir si il y a une case ou on peut s'enfuir
			//apres avoir posé notre bombe.
			AiSimZone fakezone = new AiSimZone(zone);
			ArrayList<AiTile> fakeescape = ai.moveHandler.escape;
			AiSimHero fakehero = fakezone.getOwnHero();
			AiSimBomb fakebomb = fakezone.createBomb(fakehero.getTile(),
					fakehero);
			
			//les valeur des cases ou on veut poser une bombe. A changer. plus tard on utilisera les criteres pour déterminer cela.
			if (zonepref.containsValue(2) || zonepref.containsValue(4)) {
				if (value < 6)
					result = true;}
			else if(value<8)
				result=true;
			

			if (value == 26|| value==28)
				result = true;
			
			
			
		/*	if(ai.moveHandler.f)
			if(ai.preferenceHandler.adversaire.getBombNumberMax()!=ai.preferenceHandler.adversaire.getBombNumberCurrent())
					result=false;
			*/
			

			if (result == true) {
				if (fakeescape != null) {
					if (!fakeescape.isEmpty())
						for (AiTile t : fakebomb.getBlast()) {
							ai.checkInterruption();
							if (fakeescape.contains(t) || fakeescape.contains(fakebomb.getTile()))
								fakeescape.remove(t);

						}

					if (fakeescape.isEmpty())
						result = false;
				} else
					result = false;
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
