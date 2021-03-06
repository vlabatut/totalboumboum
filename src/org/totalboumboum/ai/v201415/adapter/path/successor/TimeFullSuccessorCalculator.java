package org.totalboumboum.ai.v201415.adapter.path.successor;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201415.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201415.adapter.data.AiBlock;
import org.totalboumboum.ai.v201415.adapter.data.AiBomb;
import org.totalboumboum.ai.v201415.adapter.data.AiFire;
import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiState;
import org.totalboumboum.ai.v201415.adapter.data.AiStateName;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.ai.v201415.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201415.adapter.path.search.AiSearchNode;
import org.totalboumboum.ai.v201415.adapter.tools.AiBombTools;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette implémentation de la fonction successeur permet de traiter explicitement
 * le temps. L'algorithme va considérer non seulement un déplacement dans les
 * 4 cases voisines, mais aussi l'action d'attendre. On considèrera cette possibilité
 * quand au moins l'une des cases voisines de l'agent est occupée par du feu,
 * menacée par du feu, contient un obstacle en train d'exploser, ou sur le point
 * d'exploser.Par exemple, cette approche permet de considérer le cas où le 
 * joueur va attendre qu'un feu présent dans une case voisine, et qui l'empêche de 
 * passer, disparaisse. Par conséquent, on envisagera aussi de passer sur des cases 
 * que l'algorithme a déjà traitées.
 * <br>
 * Le temps de traitement sera donc beaucoup plus long que pour les autres fonctions
 * successeurs. Cette approche ne doit donc <b>pas être utilisée souvent</b>, car elle
 * va vraisemblablement ralentir l'agent significativement. Si vous voulez effectuer
 * un calcul moins lourd, vous pouvez utiliser la classe {@link TimePartialSuccessorCalculator}
 * à la place. Elle repose sur un modèle plus simple, qui est moins lourd à
 * traiter, mais qui en contre partie est moins précis et amènera des prédictions
 * moins fiables.
 * <br/>
 * Afin de pouvoir contrôler (partiellement) le temps nécessaire au traitement,
 * le paramètre {@link #searchMode} permet de limiter l'exploration de l'arbre 
 * de recherche :
 * <ul>
 * 		<li>{@link SearchMode#MODE_NOTREE} : quand on retombe sur une case déjà explorée,
 * 			on ne la traite de nouveau que si le nouveau chemin est meilleur
 * 			(en termes de coût) que l'ancien. Cette limite est celle employée
 * 			dans les versions non-temporelles des fonctions successeurs proposées
 * 			dans le même package. C'est la plus restrictive, donc c'est celle
 * 			qui permet de raccourcir le plus le traitement. Mais les chemins trouvés
 * 			ne seront pas forcément optimaux, puisqu'elle entraine que des chemins
 * 			différents ne peuvent pas se croiser. En fait, ça concerne surtout les
 * 			chemins assez longs et/ou compliqués (boucles, attentes, etc.).</li>
 * 		<li>{@link SearchMode#MODE_NOBRANCH} : quand on retombe sur une case déjà explorée
 * 			dans la même branche, on ne la traite pas. La limite est moins forte
 * 			que la précédente, donc le gain de temps sera moins important. Par 
 * 			contre, les chemins longs/compliqués seront mieux traités.</li>
 * 		<li>{@link SearchMode#MODE_ONEBRANCH} : quand on retombe sur une case déjà explorée
 * 			deux fois dans la même branche, on ne la traite plus. C'est la même
 * 			chose que {@code MODE_NOBRANCH}, en un peu moins strict. Ce mode est recommandé
 * 			lors de la recherche de cycle avec A*.</li>
 * 		<li>{@link SearchMode#MODE_ALL} : aucune limite, la recherche est effectuée sans
 * 			restriction. C'est donc dans ce cas que les calculs seront les plus
 * 			longs. Par contre, l'optimalité des chemins obtenus est garantie.</li>
 * </ul>
 * Cette classe nécessite que le temps soit considéré aussi par les autres
 * fonctions, donc il faut l'utiliser conjointement à :
 * <ul>
 * 		<li>Fonctions de coût :
 * 			<ul>
 * 				<li>{@link TimeCostCalculator}</li>
 * 			</ul>
 * 		</li> 
 * 		<li>Fonction heuristiques :
 * 			<ul>
 * 				<li>{@link NoHeuristicCalculator}</li>
 * 				<li>{@link TimeHeuristicCalculator}</li>
 * 			</ul>
 * 		</li> 
 * </ul>
 * 
 * @author Vincent Labatut
 */
public class TimeFullSuccessorCalculator extends SuccessorCalculator
{
	/**
	 * Crée une nouvelle fonction successeur basée sur le temps.
	 * La vitesse de déplacement utilisée lors de l'application
	 * de A* sera celle du personnage passé en paramètre.
	 * 
	 * @param ai
	 * 		Agent de référence pour gérer les interruptions.
	 * @param hero
	 * 		Personnage de référence pour calculer la durée des déplacements.
	 * @param searchMode 
	 * 		Le type de recherche à effectuer (cf. le type interne {@link SearchMode}).
	 */
	public TimeFullSuccessorCalculator(ArtificialIntelligence ai, AiHero hero, SearchMode searchMode)
	{	super(ai);
		this.hero = hero;
		this.searchMode = searchMode;
	}
	
	@Override
	public void init(AiSearchNode root)
	{	processedTiles.clear();
	}

	/////////////////////////////////////////////////////////////////
	// SEARCH MODE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Mode de recherche courant de cette fonction successeur */
	private SearchMode searchMode = SearchMode.MODE_ALL;

	/**
	 * Renvoie le mode
	 * de recherche 
	 * actuel.
	 * 
	 * @return
	 * 		Mode de recherche courant.
	 */
	public SearchMode getSearchMode()
	{	return searchMode;
	}
	
	/////////////////////////////////////////////////////////////////
	// SECURITY DELAY	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Délai de sécurité pour la case de destination */
	private long securityDelay = 0l;

	/**
	 * Change le délai de sécurité pour la case de destination.
	 * Ce délai correspond à la durée pendant laquelle cette case
	 * doit rester sans explosion, après l'arrivée prévue de l'agent.
	 * <br/>
	 * Ceci permet de s'assurer que l'agent pourra passer un certain 
	 * temps en sécurité dans la case de destination. La valeur par 
	 * défaut est de 0 (aucune garantie). En cas de modification,
	 * on peut suggérer d'utiliser une durée correspondant au temps
	 * nécessaire à l'agent pour traverser une case. Ceci garantit
	 * à l'agent qu'il aura le temps de parcourir toute la case pour
	 * s'enfuir en cas d'explosion.
	 * 
	 * @param delay
	 * 		Le délai de sécurité à respecter.
	 */
	public void setSecurityDelay(long delay)
	{	securityDelay = delay;
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage concerné par la recherche de chemin */
	private AiHero hero;

	/**
	 * Renvoie le personnage utilisé
	 * pour calculer les actions possibles.
	 * 
	 * @return
	 * 		Le personnage de référence.
	 */
	public AiHero getHero()
	{	return hero;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la map correspondant au noeud
	 * de recherche passé en paramètre. La map
	 * est créée et ajoutée dans la map principale si elle 
	 * n'existe pas déjà.
	 * <br/>
	 * La map renvoyée dépend également du mode de recherche
	 * courant.
	 * 
	 * @param localRoot
	 * 		La racine locale dont on veut la matrice.
	 * @return
	 * 		La map associée à la racine locale spécifiée
	 * 		(qui peut avoir été créée pour l'occasion).
	 */
	private Map<AiTile,AiSearchNode> getProcessedTiles(AiSearchNode localRoot)
	{	Map<AiTile,AiSearchNode> result;
		if(searchMode==SearchMode.MODE_ALL)
			// pas de limite, donc pas de map
			result = new HashMap<AiTile, AiSearchNode>();
		else
		{	if(searchMode==SearchMode.MODE_NOTREE)
				// une seule map pour tout l'arbre
				// (et sinon une map par branche)
				localRoot = null;
			result = processedTiles.get(localRoot);
			if(result==null)
			{	result = new HashMap<AiTile,AiSearchNode>();
				processedTiles.put(localRoot,result);
			}
		}
		return result;
	}

	/**
	 * Renvoie la map correspondant au noeud
	 * de recherche passé en paramètre. La map
	 * est créée et ajoutée dans la map principale si elle 
	 * n'existe pas déjà.
	 * <br/>
	 * La map renvoyée dépend également du mode de recherche
	 * courant.
	 * 
	 * @param localRoot
	 * 		La racine locale dont on veut la matrice.
	 * @return
	 * 		La map associée à la racine locale spécifiée
	 * 		(qui peut avoir été créée pour l'occasion).
	 */
	private Map<AiTile,Integer> getProcessedTilesCounts(AiSearchNode localRoot)
	{	Map<AiTile,Integer> result;
		if(searchMode==SearchMode.MODE_ALL)
			// pas de limite, donc pas de map
			result = new HashMap<AiTile, Integer>();
		else
		{	if(searchMode==SearchMode.MODE_NOTREE)
				// une seule map pour tout l'arbre
				// (et sinon une map par branche)
				localRoot = null;
			result = processedTilesCounts.get(localRoot);
			if(result==null)
			{	result = new HashMap<AiTile,Integer>();
				processedTilesCounts.put(localRoot,result);
			}
		}
		return result;
	}

	/** 
	 * Fonction successeur considérant à la fois les 4 cases 
	 * voisines de la case courante, comme pour {@link BasicSuccessorCalculator},
	 * mais aussi la possibilité d'attendre dans la case courante.
	 * <br/>
	 * Autre différence : les cases déjà traversées sont considérées,
	 * car le chemin peut inclure des retours en arrière pour éviter
	 * des explosions.
	 * <br/>
	 * <b>Note :</b> la liste est générée à la demande, elle peut
	 * être modifiée sans causer de problème.
	 * 
	 * @param node	
	 * 		Le noeud de recherche courant.
	 * @return	
	 * 		La liste des noeuds fils.
	 */
	@Override
	public List<AiSearchNode> processSuccessors(AiSearchNode node)
	{	ai.checkInterruption();
		
		// init
		AiLocation location = node.getLocation();
		AiTile tile = location.getTile();
		AiZone zone = location.getZone();
//AiHero h = zone.getHeroByColor(hero.getColor());
//AiTile t = h.getTile();
//if(!t.equals(tile))
//	System.out.print("");
		List<AiSearchNode> result = new ArrayList<AiSearchNode>();
		
		// on màj la map des cases visitées
		AiSearchNode localRoot = node.getLocalRoot();
		Map<AiTile,AiSearchNode> procTiles = getProcessedTiles(localRoot);
		Map<AiTile,Integer> procTilesCounts = getProcessedTilesCounts(localRoot);
		// pour la restriction sur l'arbre, on compare les coûts
		if(searchMode==SearchMode.MODE_NOTREE)
		{	AiSearchNode n = procTiles.get(tile);
			if(n==null)
				procTiles.put(tile,node);
			else
			{	double c = n.getCost();
				double cost = node.getCost();
				if(cost<c)
					procTiles.put(tile,node);
			}
		}
		// sinon, on met à jour la map systématiquement
		else
		{	procTiles.put(tile,node);
			Integer val = procTilesCounts.get(tile);
			if(val==null)
				val = 0;
			val++;
			procTilesCounts.put(tile, val);
		}

		// on considère chaque déplacement possible
		for(Direction direction: orderedDirections)
		{	// on récupère la case cible
			AiTile neighbor = tile.getNeighbor(direction);
			
			// on teste si on a le droit de la traiter,
			// en fonction du mode de recherche sélectionné
			boolean process = false;
			if(searchMode==SearchMode.MODE_ALL)
			{	// pas de limite
				process = true;
			}
			else if(searchMode==SearchMode.MODE_ONEBRANCH)
			{	// case pas déjà traitée plus d'une fois dans la même branche
				Integer val = procTilesCounts.get(neighbor);
				process = val==null || val<=1;
			}
			else if(searchMode==SearchMode.MODE_NOBRANCH)
			{	// case pas déjà traitée dans la même branche
				process = procTiles.get(neighbor)==null;
			}
			else //if(searchMode==MODE_NOTREE)
			{	AiSearchNode n = procTiles.get(neighbor);
				// case pas déjà traitée
				if(n==null)
					process = true;
				// ou alors avec un coût supérieur
				else
				{	AiSearchNode p = n.getParent();
					if(process=p!=null)
					{	// on fait une approximation du coût des fils à partir de ceux des pères
						double c = p.getCost();
						double cost = node.getCost();
						process = cost<c;
					}
				}
			}

			// on rajoute quelques tests indépendants du mode
			{	// on teste si on la case passée est égale à la case future
				// >> si c'est le cas (étant donné qu'il n'y a donc d'attente dans la case présente),
				//    ça correspond à une oscillation complètement inutile, qui doit être empêchée.
				//    du type : A -(right)- B -(left)- A
				//    par contre, on garde les A -(right)- B -(none)- B -(left)- A
				if(process)
				{	AiSearchNode prevNode = node.getParent();
					if(prevNode!=null)
					{	AiTile prevTile = prevNode.getLocation().getTile();
						process = !prevTile.equals(neighbor);
					}
				}
				
				// on teste s'il y a un obstacle "artificiel" (adversaire ou malus)
				if(process)
				{	process = (!considerOpponents || !containsOpponent(neighbor))
						&& (!considerMaluses || !containsMalus(neighbor));
				}
			}
			
			// si on a le droit de traiter la case
			if(process)
			{	// on applique le modèle pour obtenir la zone résultant de l'action
				AiFullModel model = new AiFullModel(zone);
				model.applyChangeHeroDirection(hero,direction);
				
				// on simule jusqu'au changement d'état du personnage : 
				// soit le changement de case, soit l'élimination
				boolean safe = model.simulate(hero);
				long duration = model.getDuration();
				
				// si le joueur a survécu et si une action a bien eu lieu
				if(safe && duration>0)
				{	// on récupère la nouvelle case occupée par le personnage
					AiZone futureZone = model.getCurrentZone();
					AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
					AiTile futureTile = futureHero.getTile();
					AiLocation futureLocation = new AiLocation(futureHero.getPosX(),futureHero.getPosY(),futureZone);
					
					// on teste si l'action a bien réussi : s'agit-il de la bonne case ?
					if(futureTile.equals(neighbor))
					{	// s'il s'agit d'une case de destination, il peut y avoir un test supplémentaire
						if(securityDelay>0 && endTiles.contains(futureTile))
						{	model.simulate(securityDelay);
							futureZone = model.getCurrentZone();
							futureHero = futureZone.getHeroByColor(hero.getColor());
							AiState futureState = futureHero.getState();
							AiStateName futureStateName = futureState.getName();
							process = futureStateName!=AiStateName.BURNING && futureStateName!=AiStateName.ENDED;
						}
						else
							process = true;
						
						// on crée le noeud fils correspondant (qui sera traité plus tard)
						if(process)
						{	AiSearchNode child = new AiSearchNode(futureLocation,node,direction);
							result.add(child);
//if(!child.getLocation().getTile().equals(child.getLocation().getTile().getZone().getHeroByColor(hero.getColor()).getTile()))
//	System.out.print("");
						}
						// si le personnage ne survit pas à la durée de sécuritée, la case est ignorée (faudra arriver plus tard)
					}
					// si la case n'est pas la bonne : 
					// la case ciblée n'était pas traversable et l'action est à ignorer
				}
				// si le joueur n'est plus vivant dans la zone obtenue : 
				// la case ciblée n'est pas sûre et est ignorée
			}
			// si la case a déjà été visitée depuis la dernière pause,
			// on l'ignore car il est (à peu près) inutile d'y repasser
		}
		
		// on considère éventuellement l'action d'attente, 
		// si un obstacle temporaire est présent dans une case voisine
		// l'obstacle temporaire peut être : du feu, une bombe, un mur destructible, une menace d'explosion.
		long waitDuration = getWaitDuration(tile);
		if(waitDuration>0 && waitDuration<Long.MAX_VALUE)
		{	// on applique le modèle pour obtenir la zone résultant de l'action
			AiFullModel model = new AiFullModel(zone);
			model.applyChangeHeroDirection(hero,Direction.NONE);
			
			// on simule pendant la durée prévue
			model.simulate(waitDuration);
			long duration = model.getDuration();
			
			// si l'attente a bien eu lieu
			if(duration>0)
			{	// on récupère les nouvelles infos décrivant le personnage
				AiZone futureZone = model.getCurrentZone();
				AiHero futureHero = futureZone.getHeroByColor(hero.getColor());
				
				// si le perso est toujours en vie, on crée le noeud de recherche
				if(futureHero!=null)
				{	AiStateName name = futureHero.getState().getName();
					boolean safe = !name.equals(AiStateName.BURNING) && !name.equals(AiStateName.ENDED);
					if(safe)
					{	// on crée le noeud fils correspondant (qui sera traité plus tard)
						AiSearchNode child = new AiSearchNode(waitDuration,futureZone,node);
						// on l'ajoute au résultat courant
						result.add(child);
//if(!child.getLocation().getTile().equals(child.getLocation().getTile().getZone().getHeroByColor(hero.getColor()).getTile()))
//	System.out.print("");
					}
					// si le joueur n'est plus vivant dans la zone obtenue : 
					// l'attente n'était pas une action sûre, et n'est donc pas envisagée
				}
				// si le perso est null, alors c'est que le joueur n'est plus vivant (cf commentaire ci-dessus)
			}
			// si l'action n'a pas eu lieu : problème lors de la simulation (?) 
			// l'attente n'est pas envisagée comme une action pertinente 
		}
		// si le temps estimé d'attente est 0 ou +Inf, alors l'attente n'est pas envisagée

		return result;
	}

	/**
	 * Détermine le temps d'attente minimal lorsque
	 * le joueur est placé dans la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case à considérer.
	 * @return
	 * 		Un entier long représentant le temps d'attente minimal.	
	 */
	private long getWaitDuration(AiTile tile)
	{	// init
		AiZone zone = tile.getZone();
		long result = Long.MAX_VALUE;
		List<AiTile> neighbors = new ArrayList<AiTile>();
		for(Direction direction: orderedDirections)
		{	AiTile neighbor = tile.getNeighbor(direction);
			neighbors.add(neighbor);
		}
		
		// on s'intéresse d'abord aux obstacles concrets
		// on considère chaque case voisine une par une
		for(AiTile neighbor: neighbors)
		{	// s'il y a un obstacle concret
			if(!neighbor.isCrossableBy(hero))
			{	// s'il y a du feu
				List<AiFire> fires = neighbor.getFires();
				long fireDuration = 0;
				for(AiFire fire: fires)
				{	long duration = fire.getBurningDuration() - fire.getState().getTime();
					//long duration = fire.getBurningDuration() - fire.getTime();
					if(duration>fireDuration)
						fireDuration = duration;
				}
				if(fireDuration>0 && fireDuration<result)
					result = fireDuration;
				
				// s'il y a un mur en train de brûler
				List<AiBlock> blocks = neighbor.getBlocks();
				long blockDuration = 0;
				for(AiBlock block: blocks)
				{	long duration = block.getBurningDuration() - block.getState().getTime();
					if(duration>blockDuration)
						blockDuration = duration;
				}
				if(blockDuration>0 && blockDuration<result)
					result = blockDuration;
				// les autres obstacles (murs normaux, bombes) n'ont pas besoin d'être traités
				// car on s'en occupe lorsqu'on gère les cases menacées par des bombes
			}
		}
		
		// on s'intéresse ensuite aux menaces provenant des bombes
		// on considère chaque bombe une par une
		AiBombTools bombTools = zone.getBombTools();
		Map<AiBomb,Long> delays = bombTools.getDelaysByBombs();
		List<AiBomb> bombs = zone.getBombs();
		for(AiBomb bomb: bombs)
		{	List<AiTile> blast = bomb.getBlast();
			List<AiTile> neigh = new ArrayList<AiTile>(neighbors);
			neigh.retainAll(blast);
			// on vérifie si la bombe menace une des cases voisines
			if(!neigh.isEmpty())
			{	// si c'est le cas, on récupère la description de la bombe
				AiStateName stateName = bomb.getState().getName();
				// on peut ignorer les bombes déjà en train de brûler, car elles cohabitent avec du feu
				if(stateName.equals(AiStateName.STANDING)
				// on peut aussi ignorer les bombes qui ne sont pas à retardement,
				// car on ne peut pas prédire quand elles vont exploser
					&& bomb.hasCountdownTrigger())
				{	//long bombDuration = Math.max(0,bomb.getNormalDuration()-bomb.getTime());
//if(delays.get(bomb)==null)
//	System.out.print("");
					long duration = delays.get(bomb) + bomb.getExplosionDuration();
					//long duration = delays.get(bomb);
					if(duration<result)
						result = duration;
				}
			}
		}
		
		return result;
	}

	@Override
	public boolean isThreatened(AiSearchNode node)
	{	boolean result = false;
		AiLocation location = node.getLocation();
		AiTile tile = location.getTile();
		AiZone zone = location.getZone();
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	AiBomb bomb = it.next();
			List<AiTile> blast = bomb.getBlast();
			result = blast.contains(tile);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String getZoneRepresentation(AiSearchNode node)
	{	AiZone zone = node.getLocation().getZone();
		String result = zone.toString();
		return result;
	}
}
