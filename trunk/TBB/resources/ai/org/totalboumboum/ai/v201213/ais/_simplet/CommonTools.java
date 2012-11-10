package org.totalboumboum.ai.v201213.ais._simplet;

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

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiLocation;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe contient quelques méthodes
 * utilisées par les différents gestionnaires.
 * 
 * @author Vincent Labatut
 */
public class CommonTools extends AiAbstractHandler<Simplet>
{	
	/**
	 * Initialise la classe avec l'IA
	 * passée en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected CommonTools(Simplet ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le personnage contrôlé par l'agent */
	public AiHero ownHero = null;
	/** La zone courante */
	public AiZone zone = null;
	/** La case occupée en ce moment */
	public AiTile currentTile = null;
	/** La vitesse de déplacement courante de l'agent */
	public double currentSpeed = 0; 
	
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On met à jour quelques variables.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		
		currentTile = ownHero.getTile();
		currentSpeed = ownHero.getWalkingSpeed();
	}
	
	/////////////////////////////////////////////////////////////////
	// METHODS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indentifie l'ensemble des cases situées au plus à un rayon
	 * donné d'une case centrale, et dans lesquelles on pourra 
	 * poser une bombe.
	 * 
	 * @param center
	 * 		La case centrale.
	 * @param hero
	 * 		Le personnage à considérer.
	 * @return 
	 * 		L'ensemble des cases concernées.
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public Set<AiTile> getTilesForRadius(AiTile center, AiHero hero) throws StopRequestException
	{	ai.checkInterruption();
		// init
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		
		// on ne teste pas la case de la cible, on la considère comme ok
		result.add(center);
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: Direction.getPrimaryValues())
		{	ai.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		
		return result;
	}

	/**
	 * Calcule combien de murs une bombe posée dans
	 * la case passée en paramètre menaçerait.
	 * 
	 * @param center
	 * 		Le centre de l'explosion à envisager.
	 * @return
	 * 		L'ensemble des cases contenant des murs touchés par l'explosion.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public Set<AiTile> getThreatenedSoftwallTiles(AiTile center) throws StopRequestException
	{	ai.checkInterruption();
		// init
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = ownHero.getBombRange();
		AiFire fire = ownHero.getBombPrototype().getFirePrototype();
		
		// on ne teste pas la case de la cible, on la considère comme ok
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: Direction.values())
		{	ai.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				blocked = !neighbor.isCrossableBy(fire);
				List<AiBlock> blocks = neighbor.getBlocks();
				if(!blocks.isEmpty())
				{	AiBlock block = blocks.get(0);
					// si le mur est destructible, on le met dans la liste
					if(block.isDestructible())
						result.add(neighbor);
				}
				i++;
			}
		}
		
		return result;
	}
	
	/**
	 * Renvoie la distance (en cases) entre la case passée
	 * en paramètre et la cible courante.
	 * 
	 * @param sourceTile
	 * 		La case à considérer
	 * @return
	 * 		La distance entre la case considérée et la cible courante.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public int getDistanceToTarget(AiTile sourceTile) throws StopRequestException
	{	ai.checkInterruption();	
		int result = Integer.MAX_VALUE;
		AiHero target = ai.targetHandler.target;
		if(target!=null)
		{	AiTile targetTile = target.getTile();
			result = zone.getTileDistance(sourceTile,targetTile);
		}
		return result;
	}
	
	/**
	 * Détermine s'il y a déjà une bombe entre
	 * la case passée en paramètre et la cible courante.
	 * On suppose que la cible et la case sont soit
	 * sur la même ligne, soit sur la même colonne.
	 * 
	 * @param sourceTile
	 * 		La case à traiter.
	 * @return
	 * 		{@code true} ssi il y a une bombe entre la case passée
	 * 		en paramètre et la cible.
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public boolean hasMiddleBomb(AiTile sourceTile) throws StopRequestException
	{	ai.checkInterruption();	
		
		boolean result = false;
		AiHero target = ai.targetHandler.target;
		if(target!=null)
		{	AiTile targetTile = target.getTile();
			Direction direction = zone.getDirection(sourceTile,targetTile);
			if(direction.isComposite())
				throw new IllegalArgumentException("The tile must be on the same row or column than the current target");
			AiTile temp = sourceTile;
			while(!temp.equals(targetTile) && !result)
			{	ai.checkInterruption();	
				
				List<AiBomb> bombs = temp.getBombs();
				result = !bombs.isEmpty();
				temp = temp.getNeighbor(direction);
			}
		}
		return result;
	}
	
	/**
	 * Détermine si la case passée en paramètre
	 * est menacée par une bombe à l'instant présent.
	 * 
	 * @param tile
	 * 		La case à considérer
	 * @return
	 * 		{@code true} ssi la case est à portée d'une bombe.
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
/*	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		long crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getTime();
			// on ne traite que les bombes menaçante : soit pas temporelles, soit
			// temporelles avec moins de temps restant que pour traverser une case
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
*/
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		// on calcule le temps nécessaire pour se rendre sur la case suivante du chemin
		AiPath currentPath = ai.moveHandler.getCurrentPath();
		long totalDuration = -1;
		if(currentPath!=null && currentPath.getLength()>1)
		{	long waitDuration = currentPath.getPause(0);
			AiLocation currentLocation = new AiLocation(ownHero);
			AiLocation nextLocation = currentPath.getLocation(1);
			double distance = zone.getPixelDistance(currentLocation,nextLocation);
			long moveDuration = Math.round(1000*distance/currentSpeed);
			totalDuration = waitDuration + moveDuration;
		}
		
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getElapsedTime();
			// on ne traite que les bombes menaçantes : soit pas temporelles, soit
			// temporelles avec moins de temps restant que pour traverser une case
			if(!bomb.hasCountdownTrigger() || totalDuration<0 || timeRemaining<totalDuration)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
}
