package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion.DestructableWall;

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
alors on ne va pas poser une bombe la bas car c’est redondant. 
Deuxièmement "Si l’agent n’a pas la capacité de poser une bombe (par exemple parce qu’il a déjà posé le maximum autorisé)" alors notre
agent va continuer son déplacement sans posant du bombe. 
Si le case qu'on se trouve est minpref on change la condition a true . Mais on ne pose pas 
le bombe encore.On doit encore regarder aux autres choses.
Si on a tous ces conditions la , on regarde si l'ennemie est inaccessible .
On controle si il y a des wall destructibles ou pas. Puis si notre hero ne se trouve pas a cote du mur 
destructible il ne va pas poser un bombe  . Si c'est un mur destructible , il pose 
un bombe pour creer un chemin vers lui.
Si l'ennemie est accessible et si on a tous les premieres trois conditions, on controle
dernierement : Est qu'on peut fuire apres qu'on pose le bombe. Si on ne peut pas fuire
on ne met pas le bombe . 
	 */
		ai.checkInterruption();
		ArrayList<AiTile> tempAcces=ai.access;
		boolean result = false;
		
		if(!ai.zone.getRemainingOpponents().isEmpty())
			// si il y a un ennemie dans la zone :
	{
			//on prends les valueurs des preferences
		Map<Integer, List<AiTile>> preferences = ai.preferenceHandler.getPreferencesByValue();
			// les tiles et les valeurs prefs des tiles sont associé.
		Map<AiTile, Integer> pref = ai.preferenceHandler.getPreferencesByTile();
		//on prend le meilleur tile d'apres ces valueurs.
		int minPref = Collections.min(preferences.keySet());
		//est-ce qu'il y a un bombe dans le current tile
		if (ai.myHero.getTile().getBombs().isEmpty()) {
			//est-ce qu'on a au moins un bombe qu'on peut poser 
			if (ai.myHero.getBombNumberMax() - ai.myHero.getBombNumberCurrent() > 0)
			{ //si il est minpref , 
				if (pref.get(ai.myHero.getTile()) == minPref) {
						result = true;
				}
				// on controle si l'ennemie est accessible
				if(!ai.enemyAccessible){
					// si il n'est pas accessible , on controle si il y a des wall destructibles
					//ou pas. Si il y 'en a des wall Destructibles
					DestructableWall dW=new DestructableWall(ai);
					//si notre hero ne se trouve pas a cote du mur destructible 
					// il ne va pas poser un bombe  
					if(dW.fetchValue(ai.myHero.getTile())){
						result=false;
					}					
				}
				 // ici on controle le bombe qu'on va poser tue nous meme . 
				// Si c'est le cas on ne met pas de bombe.
						AiSimZone simzone=new AiSimZone(ai.getZone());
						AiSimHero simHero=simzone.getOwnHero();
						AiSimBomb simbomb=simzone.createBomb(simHero.getTile(), simHero);
						List<AiBomb> listBomb=ai.getZone().getBombs();
								tempAcces.removeAll(simbomb.getBlast());
								for(AiBomb bomb:listBomb){
					    			ai.checkInterruption();
								tempAcces.removeAll(bomb.getBlast());
								}
								if(tempAcces.isEmpty())
								result=false;				
								else if(simbomb.getBlast().contains(ai.nearestEnemy.getTile()))	{								
									if(ai.dangerMap.get(simbomb.getTile())!=2)
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
