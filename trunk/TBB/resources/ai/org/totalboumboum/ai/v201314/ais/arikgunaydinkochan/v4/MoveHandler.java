package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected MoveHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
			
		// dans cette classe, on aura généralement besoin d'un objet de type Astar.
		// à titre d'exemple, on construit ici un objet Astar très simple (pas forcément très efficace)
		// pour des raisons de rapidité, il est recommandé de créer l'objet Astar une seule fois, 
		// et non pas à chaque itération. Cela permet aussi d'éviter certains problèmes de mémoire.
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		
		{
		CostCalculator cc = new TimeCostCalculator(ai,ownHero);
		HeuristicCalculator hc = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator sc = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_ONEBRANCH);
		astar = new Astar(ai, ownHero, cc, hc, sc);
		}
		
		{
		CostCalculator acc = new ApproximateCostCalculator(ai,ownHero);
		HeuristicCalculator hc = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator asc = new ApproximateSuccessorCalculator(ai);
		astarApproximation = new Astar(ai, ownHero, acc, hc, asc);
		}	
    }
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** A titre d'exemple, je stocke la zone de jeu, car on en a souvent besoin */
	private AiZone zone = null; 
	/** A titre d'exemple, je stocke le sprite controlé par cet agent, car on en a aussi souvent besoin */
	private AiHero ownHero = null; 
	/** Il est nécessaire de stocker l'objet Astar, si on ne veut pas devoir le re-créer à chaque itération */
	private Astar astar = null;
	/** Il est nécessaire de stocker l'objet Astar */
	private Astar astarApproximation = null;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination()
	{	ai.checkInterruption();
		AiTile result = null;
		
		AiTile myTile = ownHero.getTile();
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(ai.getZone().getRemainingOpponents());
		if(!heroes.isEmpty()){
		
		
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		
		
		boolean accessible = ai.accessibleHero();
		
		AiTile accessibleHeroTile =null;	//
		int accessibleDistance = 100;
		
		AiTile nonAccessibleHeroTile =null;	//
		int nonAccessibleDistance = 100;
	
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());
		List<AiTile> tiles = preferences.get(minPref);
		
	
		//
		if(mode==AiMode.ATTACKING){
				List<AiTile> danger =ai.dangereousTiles();
				if(danger.contains(myTile)){
					
					if(ai.change){
						ai.tile = tiles.get(0);	
						ai.change=false;
						}
					if(!tiles.contains(ai.tile)){ 
						result =tiles.get(0);
						ai.change=true;
						}
					else  result = ai.tile;
					
				}	
				else{
						//
						if(!heroes.isEmpty()){
								for(AiHero hero : heroes){
										ai.checkInterruption();
											AiTile heroTile = hero.getTile();
											int d=zone.getTileDistance(myTile,heroTile);
											//
											if(ai.accessibleTiles.contains(heroTile)){
													if(accessibleDistance>d){
															accessibleHeroTile=heroTile;
															accessibleDistance=d;
													}
											}
											//
											else {	
													if(nonAccessibleDistance>d){
															nonAccessibleHeroTile=heroTile;
															nonAccessibleDistance=d;
													}
											}	
								}
						}		
						
						//
						if(accessible){
							
								//
								if(heroes.size()==1){
									result =accessibleHeroTile;
								}
								//
								else if(heroes.size()>1){	
									result =accessibleHeroTile;
								}
						}
						//	
						else{
								result =nonAccessibleHeroTile;
						}
				}
		}
		//
		else{
			if(ai.change){
				ai.tile = tiles.get(0);	
				ai.change=false;
				}
			if(!tiles.contains(ai.tile)){ 
				result =tiles.get(0);
				ai.change=true;
				}
			else  result = ai.tile;
			
		}

		}
		else result=myTile;
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiPath processCurrentPath()
	{	ai.checkInterruption();
		AiPath result = null;
		AiLocation startLocation = new AiLocation(ownHero);
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		boolean accessible = ai.accessibleHero();
		
		// pour l'exemple, voici la manière la plus simple d'utiliser A*
		// là encore, le but est d'illustrer l'utilisation de l'API, et non pas la conception d'un agent
		AiTile endTile = getCurrentDestination();		// cette case correspond à celle sélectionnée dans la méthode processCurrentDestination
		try
		{	// on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch
		
			if(mode==AiMode.ATTACKING){			
				List<AiTile> danger =ai.dangereousTiles();
				if(danger.contains(ai.getZone().getOwnHero().getTile())){
					result = astar.startProcess(startLocation,endTile);
				}
				else
				{
					if(accessible)
							result = astar.startProcess(startLocation,endTile);
					else{
						
							result = astarApproximation.startProcess(startLocation,endTile);
					}
				}
			}
			else{
						result = astar.startProcess(startLocation,endTile);
			}
			
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();		// il ne faut PAS afficher la trace (cf. le manuel de l'API, la partie sur les contraintes de programmation)
			// l'exception est levée seulement si astar atteint certaines limites avant de trouver un chemin
			// cela ne veut donc pas dire qu'il y a eu une erreur, ou que le chemin n'existe pas.
			// ça veut seulement dire que ça prend trop de temps/mémoire de trouver ce chemin
			// dans ce cas là, il faudrait, dans ce bloc catch, effectuer un traitement spécial pour résoudre
			// ce problème. Ici, pour l'exemple, on se contente de construire un chemin vide (ce qui n'est pas très malin)
			result = new AiPath();
		}
		ai.currentPath=result;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction processCurrentDirection()
	{	ai.checkInterruption();
		Direction result = Direction.NONE;
		
		
		List<AiTile> blasttiles = new ArrayList<AiTile>();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		bombs=ai.getZone().getBombs();
		for( AiBomb item : bombs  ){
			ai.checkInterruption();
			blasttiles.addAll(item.getBlast()) ;	
		}
			
		AiPath path = getCurrentPath();
		
		
		if(path==null || path.getLength()<2)		// cas où le chemin est vide, ou bien ne contient que la case courante
			result = Direction.NONE;
		else if(ai.dangereousTiles().contains(path.getLocation(1).getTile())
				&&!ai.dangereousTiles().contains(ai.getZone().getOwnHero().getTile())){
					result= Direction.NONE;
			
		}
		else
		{	
			AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);	// ici, j'utilise une méthode de l'API pour calculer la direction pour aller de la 1ère vers la 2nde case
			//ai.previousPath=path;
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
