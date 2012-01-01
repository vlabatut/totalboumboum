package org.totalboumboum.ai.v201112.ais._simplet;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201112.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Le déplacement utilise trois algorithmes de recherche :
 * A* configuré de façon précise pour trouver un chemin
 * de déplacement direct, A* configuré de façon approximative
 * pour trouver un chemin de déplacement indirect (passant
 * à travers des murs), et dijkstra pour trouver des chemins
 * de fuite.
 * <br/>
 * L'algorithme est à peu près le suivant (<i>à ne pas utiliser dans vos agents</i>) :
 * <ul>
 * 		<li>si on est menacé par une bombe, on cherche un chemin de fuite.</li>
 * 		<li>si on a déjà un chemin de fuite, on le suit
 * 		<li>sinon (pas de menace, pas de fuite)
 * 			<ul>
 * 				<li>on cherche un chemin direct vers la destination</li>
 * 				<li>s'il n'y a pas de chemin direct
 *					<ul>
 *						<li>s'il y a déjà un chemin indirect, on le suit</li>
 *						<li>sinon on en cherche un</li>
 *						<li>on identifie le prochain mur à faire exploser</li>
 *						<li>si on est dessus, on le fait exploser</li>
 *						<li>sinon on se déplace vers lui : on calcule le chemin direct pour y aller</li>
 *					</ul>
 *				</li>
 *			</ul>
 *		</li>
 * </ul>
 * <b>Attention</b> : cette approche est très mauvaise, et en plus elle
 * ne respecte pas l'algorithme général vu en cours. En effet, elle se
 * permet d'identifier des chemins de fuite qui deviennent prioritaires
 * sur l'objectif déterminé grâce aux valeurs d'utilité. Vous ne devez
 * pas utiliser cette approche, l'essentiel du travail doit se faire
 * lors du calcul d'utilité. Quand votre agent est en danger, il doit
 * bien sûr fuir mais cette fuite doit dépendre complètement de l'objectif.
 * En d'autres termes, le chemin de fuite doit l'amener à l'objectif. Ici,
 * ce n'est pas le cas (et c'est ça le problème) : quand il est en danger,
 * l'agent Simplet prend un objectif secondaire (une case sûre) et s'y rend.
 * Ceci est formellement interdit dans vos agents.
 * 
 * @author Vincent Labatut
 */
public class MoveHandler extends AiMoveHandler<Simplet>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(Simplet ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		
		long before = print("    > init path-finding objects");
		// astar précis
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); // on assimile la traversée d'un adversaire à un détour de 1 seconde
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
			astarPrecise = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		// astar approximation
		{	CostCalculator costCalculator = new ApproximateCostCalculator(ai,ownHero);
			HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,ownHero);
			SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
			astarApproximation = new Astar(ai,ownHero, costCalculator, heuristicCalculator, successorCalculator);
		}
		// dijkstra
		{	CostCalculator costCalculator = new TimeCostCalculator(ai,ownHero);
			costCalculator.setOpponentCost(1000); // on assimile la traversée d'un adversaire à un détour de 1 seconde
			SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOBRANCH);
			dijkstra = new Dijkstra(ai,ownHero, costCalculator,successorCalculator);
		}
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		print("    < path-finding objects initialized duration="+elapsed);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La zone courante */
	protected AiZone zone = null;
	/** Le personnage contrôlé par l'agent */
	protected AiHero ownHero = null;
	/** La case courante */
	protected AiTile currentTile = null;
	/** L'objet A* utilisé pour le calcul des chemins directs */
	protected Astar astarPrecise = null;
	/** L'objet A* utilisé pour le calcul des chemins indirects */
	protected Astar astarApproximation = null;
	/** L'objet dijkstra utilisé pour le calcul des chemins de fuite */
	protected Dijkstra dijkstra = null;
	/** Le chemin indirect courant */
	protected AiPath indirectPath = null;
	/** La case de fuite courante */
	protected AiTile safeDestination = null;
	/** Indique si l'agent doit poser une bombe pour dégager le chemin indirect */
	protected boolean secondaryBombing = false;
	/** Indique si l'agent doit poser une bombe sur l'objectif */
	protected boolean bombDestination = false;
	
	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		
		// si nécessaire, on change la destination courante
		{	long before = print("    > entering updateDestination");
			updateDestination();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < exiting updateDestination duration="+elapsed);
		}
		
		// on cherche un chemin vers cette destination
		{	long before = print("    > entering updatePath");
			updatePath();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < exiting updatePath duration="+elapsed);
		}
		
		// on utilise le chemin pour déterminer la direction de déplacement
		{	long before = print("    > entering updateDirection");
			updateDirection();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < exiting updateDirection duration="+elapsed);
		}
		
		return currentDirection;
	}

	/**
	 * Met à jour la case de destination de l'agent. Là encore,
	 * c'est très simple et très peu efficace : on garde tout
	 * le temps le même objectif jusqu'à ce qu'il soit complètement
	 * obsolète. 
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	private void updateDestination() throws StopRequestException
	{	ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();
		print("      currentTile="+currentTile+" currentDestination="+currentDestination);
		
		// on détermine si on doit changer/initialiser la destination
		boolean changeDestination = false;
		// pas du tout de destination courante
		if(currentDestination==null)
		{	print("      there is no current destination >> we update it");
			changeDestination = true;
		}
		// destination courante atteinte
		else if(currentTile.equals(currentDestination))
		{	print("      the current destination has been reached and needs to be changed");
			changeDestination = true;
		}	
		// destination courante obsolète
		else
		{	// on récupère les utilités par case
			HashMap<AiTile,Float> utilitiesByTile = ai.utilityHandler.getUtilitiesByTile();
			// on récupère l'utilité de la destination courante
			Float destinationUtility = utilitiesByTile.get(currentDestination);
			
			// si la destination est obsolète, il faut la changer
			if(destinationUtility==null)
			{	changeDestination = true;
				print("      current destination is obsolete and must be changed: destinationUtility="+destinationUtility);
			}
		}
		
		if(changeDestination)
		{	// on récupère simplement les utilités classées
			print("      retrieve utilities");
			HashMap<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
			TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
			HashMap<AiTile,Boolean> bombTiles = ai.utilityHandler.bombTiles;
			
			// on part de l'utilité maximale et on descend jusqu'à trouver une destination
			Iterator<Float> it1 = values.descendingIterator();
			boolean goOn = true;
			while(it1.hasNext() && goOn)
			{	ai.checkInterruption();	
				
				// on récupère la valeur d'utilité
				float utility = it1.next();
				print("        processing utility="+utility);
			
				// puis les cases qui possèdent cette utilité
				List<AiTile> tiles = utilitiesByValue.get(utility);
				for(AiTile tile: tiles)
				{	ai.checkInterruption();
					
					print("          +"+tile);
				}
				// on les mélange pour introduire du hasard (elles se valent toutes)
				Collections.shuffle(tiles);
			
				// on en choisit une différente de la case courante (si possible, sinon on continue)
//				Iterator<AiTile> it2 = tiles.iterator();
//				while(it2.hasNext() && goOn)
//				{	currentDestination = it2.next();
//					bombDestination = bombTiles.get(currentDestination);
//					goOn = currentDestination==null || currentTile.equals(currentDestination);
//				}
if(!tiles.isEmpty())
{	currentDestination = tiles.get(0);
	bombDestination = bombTiles.get(currentDestination);
	goOn = false;
}
				print("        currentDestination="+currentDestination+" bombDestination="+bombDestination);
			}
			
			if(currentDestination==null)
			{	print("      could not find any new destination");
				bombDestination = false;
			}
			else
				print("      new destination all set: currentDestination="+currentDestination+" bombDestination="+bombDestination);
		}
		
		else
		{	print("      no need to update the destination");
		}
	}
	
	/**
	 * Met à jour le chemin à suivre par cet agent,
	 * en utilisant l'algorithme suivant :
	 * - reset chemin direct
	 * - si pas de destination sûre && on est menacé par une bombe
	 * 		- on trouve une destination sûre + chemin direct
	 * 		  (pour ça on remplace les autres joueurs par des murs destructibles) 
	 * - sinon, s'il y a une destination sûre courante
	 * 		- si on est arrivé
	 * 			- on reset la destination sûre
	 * 		- sinon 
	 * 			- on continue à suivre le chemin vers elle
	 * - sinon
	 * 		- on cherche un chemin vers la destination primaire
	 * 			- s'il y a un chemin :
	 * 				- on reset le chemin indirect 
	 * 				- on suit le chemin direct
	 * 			- sinon
	 * 				- s'il n'y a pas déjà un chemin indirect
	 * 					- on en cherche un
	 * 				- on cherche la prochaine case bloquée
	 * 				- si on est déjà dessus
	 *					- on pose une bombe
	 * 				- sinon
	 * 					- on cherche un chemin direct vers la case bloquée
	 * <b>Algorithme non-conforme avec ce qui est demandé dans le projet,
	 * donc à ne pas utiliser pour vos agents.</b>
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption();
		CommonTools commonTools = ai.commonTools;
		secondaryBombing = false;
		boolean isThreatened = commonTools.isTileThreatened(currentTile);
		// on réinitialise le chemin courant
		currentPath = null;
		
		// si on est menacé par une bombe
		// mais qu'il n'y a pas encore de destination sûre >> on en trouve une
		if(isThreatened && safeDestination==null)
		{	print("      there's a threat and no safe tile: currentTile="+currentTile+" safeDestination="+safeDestination);
			
			// on trouve une destination sûre et le chemin direct qui va avec
			long before = print("        we look for both a safe tile and its path using dijkstra");
			AiLocation startLocation = new AiLocation(ownHero);
			try
			{	currentPath = dijkstra.processEscapePath(startLocation);
			}
			catch (LimitReachedException e)
			{	//e.printStackTrace();
			}
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			if(currentPath==null || currentPath.isEmpty())
			{	print("        no safe tile could be found! duration="+elapsed+" currentPath="+currentPath);
			}
			else
			{	AiLocation lastLocation = currentPath.getLastLocation();
				safeDestination = lastLocation.getTile();
				print("        the escape tile is: secondaryDestination="+safeDestination+" duration="+elapsed);
				print("        the escape path is: currentPath="+currentPath);
			}
		}
		
		// sinon, s'il y a une destination sûre courante
		else if(safeDestination!=null)
		{	print("      there's a safe tile defined, so we try to go there: currentTile="+currentTile+" safeDestination="+safeDestination);
			
			// si on est arrivé
			if(currentTile.equals(safeDestination))
			{	print("        actually, we're already there: currentTile="+currentTile+" safeDestination="+safeDestination);
				// on réinitialise la destination sûre
				safeDestination = null;
			}
			
			// sinon on met simplement à jour le chemin vers elle
			else
			{	long before = print("        we use astar to find a path from currentTile="+currentTile+" to safeDestination="+safeDestination);
				AiLocation startLocation = new AiLocation(ownHero);
				try
				{	currentPath = astarPrecise.processShortestPath(startLocation,safeDestination);
				}
				catch (LimitReachedException e)
				{	//e.printStackTrace();
				}
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				if(currentPath==null || currentPath.isEmpty())
				{	print("        no path could be found! duration="+elapsed+" currentPath="+currentPath);
				}
				else
				{	print("        the escape path is: duration="+elapsed+" currentPath="+currentPath);
				}
			}
		}
		
		// sinon (pas de menace, pas de chemin de fuite à suivre)
		else
		{	print("      there's no threat, so we don't have to escape (yet) currentTile="+currentTile+" currentDestination="+currentDestination);
			AiLocation startLocation = new AiLocation(ownHero);
		
			// on calcule un chemin direct vers la destination courante
			long before = print("        we use astar to find a path from startLocation="+startLocation+" to currentDestination="+currentDestination);
			try
			{	currentPath = astarPrecise.processShortestPath(startLocation,currentDestination);
			}
			catch (LimitReachedException e)
			{	//e.printStackTrace();
			}
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			
			// si on a trouvé un chemin direct
			if(currentPath!=null && !currentPath.isEmpty())
			{	print("        a direct path was found: duration="+elapsed+" currentPath="+currentPath);
				indirectPath = null;
				safeDestination = null;
			}
			
			// sinon : aucun chemin direct n'existe
			else
			{	print("        no direct path was found: duration="+elapsed+" currentPath="+currentPath);
				print("        we need a indirect path");
			
				// s'il n'y a pas de chemin secondaire, on en trouve un
				if(indirectPath==null)
				{	before = print("        no existing indirect path, so using astar to find one from startLocation="+startLocation+" to currentDestination="+currentDestination);
					try
					{	indirectPath = astarApproximation.processShortestPath(startLocation,currentDestination);
					}
					catch (LimitReachedException e)
					{	//e.printStackTrace();
					}
					after = System.currentTimeMillis();
					elapsed = after - before;
					print("          astar finished: duration="+elapsed+" secondaryPath="+indirectPath);
				}
				else
					print("        there's already a secondary path: secondaryPath="+indirectPath);
			
				// on cherche la prochaine case bloquée sur le chemin secondaire
				Iterator<AiLocation> it = indirectPath.getLocations().iterator();
				AiTile blockedTile = null;
				AiTile previousTile = null;
				while(it.hasNext() && blockedTile==null)
				{	ai.checkInterruption();
					
					// la case est bloquante si elle contient un bloc (forcément destructible)
					AiLocation location = it.next();
					AiTile tile = location.getTile();
					List<AiBlock> blocks = tile.getBlocks();
					// alors on prend celle d'avant
					if(!blocks.isEmpty())
						blockedTile = previousTile;
					previousTile = tile;
				}
				print("        next blocked tile on the secondary path: blockedTile="+blockedTile);
				
				// s'il n'y a pas de case bloquée, alors le bloquage est dû à une explosion, 
				// et on se contente d'attendre qu'elle disparaisse
				if(blockedTile==null)
				{	print("        there actually no blocked tile, so we wait for the end of an explosion");
				}
				
				// sinon : on a une case bloquée
				else
				{	// si elle contient une bombe, il faut aussi attendre qu'elle explose
					List<AiBomb> bombs = blockedTile.getBombs();
					if(!bombs.isEmpty())
					{	print("        there's already a bomb in the blocked tile, so we need to wait for it to explode");
					}
					
					// sinon, on doit aller sur la case bloquée
					else
					{	print("        no bomb in the blocked tile, so we go there");
						safeDestination = blockedTile;
						
						// peut-être est-on déjà arrivé?
						if(safeDestination.equals(currentTile))
						{	print("        we're actually already there so we can let the BombHandler drop a bomb");
							secondaryBombing = true;
						}
						
						// sinon cherche le chemin
						else
						{	before = print("        looking for a path using astar: currentTile="+currentTile+" secondaryDestination="+safeDestination);
							try
							{	currentPath = astarPrecise.processShortestPath(startLocation,safeDestination);
							}
							catch (LimitReachedException e)
							{	//e.printStackTrace();
							}
							after = System.currentTimeMillis();
							elapsed = after - before;
							if(currentPath==null || currentPath.isEmpty())
								print("        no path could be found! duration="+elapsed);
							else
								print("        astar finished: duration="+elapsed+" currentPath="+currentPath);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Met à jour la direction de déplacement en fonction
	 * du chemin courant.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	private void updateDirection() throws StopRequestException
	{	ai.checkInterruption();
		
		// init
		currentDirection = Direction.NONE;
		
		// si un chemin a été trouvé
		if(currentPath!=null)
		{	print("      processing the path currentPath="+currentPath);
			
			// temps d'attente éventuel
			long wait = currentPath.getFirstPause();
			print("      wait found: waitDuration="+wait);
			
			// si on ne bouge pas
			if(currentPath.getLength()<2 || wait>0)
				print("      stay/wait in the same tile: currentDirection="+currentDirection);
			
			// si on bouge
			else
			{	// position actuelle
				AiLocation source = currentPath.getFirstLocation();
				// position suivante
				AiLocation target = currentPath.getLocation(1);
				print("      no wait: source="+source+" target="+target);
				
				// si la case est similaire (attente) la direction sera NONE
				currentDirection = zone.getDirection(source.getTile(),target.getTile());
				print("      new direction: currentDirection="+currentDirection);
			}
		}
		
		// si aucun chemin n'a été trouvé
		else
			print("      no path, so no direction: currentDirection="+currentDirection);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// d'abord on fait le traitement par défaut
		super.updateOutput();
		
		// et on rajoute les données secondaires (éventuellement) en vert
		AiOutput output = ai.getOutput();
		Color color = Color.GREEN;
		// destination
		if(outputDestination && safeDestination!=null)
			output.addTileColor(safeDestination,color);
		// path
		if(outputPath && indirectPath!=null)
			output.addPath(indirectPath,color);
	}
}
