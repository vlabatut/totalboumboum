package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
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
		CostCalculator cc = new TimeCostCalculator(ai,ownHero);
		HeuristicCalculator hc = new TimeHeuristicCalculator(ai,ownHero);
		SuccessorCalculator sc = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_ONEBRANCH);
		astar = new Astar(ai, ownHero, cc, hc, sc);
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
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected AiTile processCurrentDestination()
	{	ai.checkInterruption();
		AiTile result = null;
		
		// ici, à titre d'exemple, on se contente de prendre la case dont la préférence est maximale
		// c'est une approche simpliste, ce n'est pas forcément la meilleure (sûrement pas, d'ailleurs)
		// c'est seulement pour montrer un exemple en termes de programmation (et non pas de conception d'agent)
		AiPreferenceHandler<Agent> preferenceHandler = ai.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler.getPreferencesByValue();
		int minPref = Collections.min(preferences.keySet());	// ATTENTION : ici il faudrait tester qu'il y a au moins une valeur dans la map (sinon : NullPointerException !)
		List<AiTile> tiles = preferences.get(minPref);			// on récupère la liste de cases qui ont la meilleure préférence
		
		AiTile myTile = ownHero.getTile();
		
		List<AiTile> controlTiles =ai.accessibleTiles;
		List<AiTile> blastTiles = new ArrayList<AiTile>();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		bombs=ai.getZone().getBombsByColor(PredefinedColor.GREY);
		for( AiBomb bomb : bombs  ){
			ai.checkInterruption();
			blastTiles.addAll(bomb.getBlast()) ;	
		}
		controlTiles.removeAll(blastTiles);
		
		//int bombDistance =100;
		
		AiTile nonAccessibleHeroTile =null;	
		int nonAccessibleDistance = 100;
		
		AiTile accessibleHeroTile =null;	
		int accessibleDistance = 100;
		
		int tileDistance =100;
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();	
		heroes.addAll(zone.getRemainingOpponents());
		
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
						else {	
								if(nonAccessibleDistance>d){
										nonAccessibleHeroTile=heroTile;
										nonAccessibleDistance=d;
								}
						}	
				}
		
				for(AiTile t :tiles){
					ai.checkInterruption();
						if(accessibleDistance<100){
								int d = zone.getTileDistance(t,accessibleHeroTile);
								if(d<tileDistance){
										result=t;
								}
						}
						else{
								int d = zone.getTileDistance(t,nonAccessibleHeroTile);
								if(d<tileDistance){
										result=t;
										tileDistance=d;
								}
						}	
				}
				
				
		}
		else{
			if(ai.change2){
				ai.tile2 = tiles.get(0);									// on prend la première de la liste (arbitrairement)
				ai.change2=false;
				}
			if(!tiles.contains(ai.tile2)){ 
				result =tiles.get(0);
				ai.change2=true;
				}
			else  result = ai.tile2; 
		}
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
		
		// pour l'exemple, voici la manière la plus simple d'utiliser A*
		// là encore, le but est d'illustrer l'utilisation de l'API, et non pas la conception d'un agent
		AiTile endTile = getCurrentDestination();		// cette case correspond à celle sélectionnée dans la méthode processCurrentDestination
		try
		{	// on doit OBLIGATOIREMENT exécuter astar dans un bloc try/catch
			result = astar.startProcess(startLocation,endTile);
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
			
		
		
		// dans cette méthode, on doit utiliser les calculs précédents (accessibles via
		// getCurrentDestination et getCurrentPath) pour choisir la prochaine direction
		// à suivre pour notre agent. à titre d'exemple, on se content ici de prendre
		// la direction de la case suivante, ce qui n'est pas forcément la meilleure chose à faire.
		// là encore, il s'agit d'un exemple de programmation, et non d'un exemple de conception.
		AiPath path = getCurrentPath();
		
		
		if(path==null || path.getLength()<2)		// cas où le chemin est vide, ou bien ne contient que la case courante
			result = Direction.NONE;
		else if(blasttiles.contains(path.getLocation(1).getTile())&&!blasttiles.contains(ai.getZone().getOwnHero().getTile())){
			/*
			Direction passage = Direction.NONE;
			
			AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			passage = zone.getDirection(currentTile, nextTile);
			
			if(passage==Direction.UP){
				result=Direction.DOWN;
			}
			else if(passage==Direction.DOWN){
				result=Direction.UP;
			}
			else if(passage==Direction.LEFT){
				result=Direction.RIGHT;
			}
			else if(passage==Direction.RIGHT){
				result=Direction.LEFT;
			}
			*/
			result= Direction.NONE;
		}
		else
		{	AiLocation nextLocation = path.getLocation(1);		// un chemin est une séquence de AiLocation (position en pixel), chaque AiLocation contient la AiTile correspondant à la position.
			AiTile nextTile = nextLocation.getTile();
			AiTile currentTile = ownHero.getTile();
			result = zone.getDirection(currentTile, nextTile);	// ici, j'utilise une méthode de l'API pour calculer la direction pour aller de la 1ère vers la 2nde case
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
