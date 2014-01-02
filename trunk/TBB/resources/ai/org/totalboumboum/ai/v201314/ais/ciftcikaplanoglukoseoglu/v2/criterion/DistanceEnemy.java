package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.criterion;

import java.util.List;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.Agent;


/**
 * 
 * Cette classe va nous permettre de traiter une case par rapport au critere
 * binaire "distance a l'adversaire".
 * 
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class DistanceEnemy extends AiCriterionBoolean<Agent> {
	/** Nom du critère */
	public static final String NAME = "DISTANCE_ENEMY";
	/** L'adversaire le plus proche */
	public AiHero adversaire;
	
	/**
	 * Crée le critère binaire distance a l'ennemi.
	 * 
	 * @param ai
	 *            l'agent concerné.
	 */
	public DistanceEnemy(Agent ai) {
		super(ai, NAME);
		ai.checkInterruption();
	}

	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param aitile 
	 * 		la premiere case.
	 * @param  aitile1
	 *            la deuxieme case.
	 * @return result 
	 * renvoie la distance(int) entre les deux cases en Tile. Ne prend pas 
	 * la distance circulaire. 
	 */
	public int getDist(AiTile aitile, AiTile aitile1) {
		ai.checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}
	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 *           
	 * @return result 
	 * renvoie l'agent adverse le plus proche de notre agent.
	 */
	public AiHero closesttarget(){
		ai.checkInterruption();
		int distm =300;
		int dist;
		// on prend les agents adverse dans la zone.
		AiZone zone=ai.getZone();
		List<AiHero> heroes = zone.getRemainingOpponents();

		AiHero adversaire = null;
		// on prend l'adversaire le plus proche.
		if (!heroes.isEmpty()) {
			for (AiHero h : heroes) {
				ai.checkInterruption();
				
				dist = getDist(zone.getOwnHero().getTile(), h.getTile());
				if (dist < distm) {
					distm = dist;
					adversaire = h;
				}
			}}
			return adversaire;
	}
	

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Définit le traitement de la case par rapport a notre critere.
	 * 
	 * @param tile
	 *            la case concernée.
	 * @return result renvoie la valeur booléene qui est obtenu par le
	 *         traitement. Si l'adversaire est au plus a trois case de distance
	 *         de la case concernée, la valeur est vrai. Sinon elle est fausse.
	 */
	@Override
	public Boolean processValue(AiTile tile) {
		ai.checkInterruption();
		boolean result = false;
		AiHero adversaire= closesttarget();
		if (adversaire==null)
			adversaire=ai.getZone().getOwnHero();
		
			AiTile atl = adversaire.getTile();
			// on prend notre distance a l'adversaire avec la fonction getDist.
			
			int longeur = getDist(atl, tile);
			if (longeur < 4)
				result = true;
		

		return result;
	}
}
