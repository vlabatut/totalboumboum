package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3;


import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Distance;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.DistanceEnemy;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.ExitNumber;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.NombreDestructible;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Threat;


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
		DistanceEnemy de = (DistanceEnemy) ai.preferenceHandler
				.getCriterion("DISTANCE_ENEMY");
		Threat th = (Threat) ai.preferenceHandler.getCriterion("THREAT");
		ExitNumber en = (ExitNumber) ai.preferenceHandler
				.getCriterion("EXIT_NUMBER");
		AiHero ownHero = zone.getOwnHero();
		AiTile herotile = ownHero.getTile();
		AiHero adversaire = ai.preferenceHandler.adversaire;
		AiTile advtile = adversaire.getTile();
		boolean flag = ai.preferenceHandler.flag;
		
		NombreDestructible nd=(NombreDestructible)ai.preferenceHandler.getCriterion("NUMBERDESTRUCTIBLE");
		Distance d=(Distance)ai.preferenceHandler.getCriterion("Distance");

		// on fait une simulation pour voir si il y a une case ou on peut
		// s'enfuir
		// apres avoir posé notre bombe.
		AiSimZone fakezone = new AiSimZone(zone);
	
		ArrayList<AiTile> fakeescape = ai.preferenceHandler.escape;
		
		AiSimHero fakehero = fakezone.getOwnHero();
		AiSimBomb fakebomb = fakezone.createBomb(fakehero.getTile(), fakehero);
		AiPath path = ai.moveHandler.getCurrentPath();
		
		if(ai.modeHandler.getMode()==AiMode.ATTACKING){
		if (herotile.getBombs().isEmpty()
				&& (ownHero.getBombNumberCurrent() != ownHero
						.getBombNumberMax())) {

			if (th.fetchValue(herotile) < 2
					&& fakebomb.getBlast().contains(advtile)
					&& !herotile.equals(advtile)) {
				if (en.fetchValue(advtile) == 1)
					result = true;
				else {

					if (en.fetchValue(advtile) == 2) {
						List<AiTile> corridorlike = advtile.getNeighbors();
						boolean count = false;
						boolean near = false;
						for (AiTile t : corridorlike) {
							ai.checkInterruption();
							if (t.isCrossableBy(adversaire)) {
								if (en.fetchValue(t) > 2)
									count = true;
								else if (en.fetchValue(t) == 1)
									near = true;
							}
						}
						if (!count && near)
							result = true;
					}

				}
			}

				if (de.fetchValue(herotile) == true)
				if (en.fetchValue(herotile) > 2)
					if (th.fetchValue(herotile) < 2)
						result = true;

			// Si la prochaine case sur le chemin contient un mur
			if (ai.moveHandler.futiletorun == true)
				result = true;

			if (result == true) {

				if(flag)
				{ArrayList<AiTile> enough=ai.preferenceHandler.accessibleTiles;
				enough.removeAll(fakebomb.getBlast());
				if (enough.isEmpty())
					result=false;
					if (ownHero.getBombNumberCurrent() == 1)
					result = false;
				}else
				if (fakeescape != null) {

					if (!fakeescape.isEmpty())
						for (AiTile t : fakebomb.getBlast()) {
							ai.checkInterruption();

							if (fakeescape.contains(t))
								fakeescape.remove(t);

						}

					if (fakeescape.isEmpty())
						result = false;
				} else
					result = false;

			}

			if (result == true)
				if (path != null)
					if (path.getLength() > 2) {
						AiTile nextTile = path.getLocation(1).getTile();
						AiTile secondTile = path.getLocation(2).getTile();

						if (en.fetchValue(nextTile) < 2
								&& (zone.getPixelDistance(adversaire.getPosX(),
										adversaire.getPosY(), secondTile) < zone
										.getPixelDistance(ownHero.getPosX(),
												ownHero.getPosY(), secondTile)))
							if (!flag)
								result = false;
					}

		}}
		
		// pour le mode collecte
		
		else{
			if(nd.fetchValue(herotile)>=2 && fakeescape!=null && d.fetchValue(herotile) ){
				result=true;
			}
			else{
				result=false;
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
