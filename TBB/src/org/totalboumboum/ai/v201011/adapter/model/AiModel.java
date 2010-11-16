package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.Map.Entry;

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Vincent Labatut
 *
 */
class AiModel
{	
	public AiModel(AiZone currentZone)
	{	// init the model with a copy of the current zone
		this.current = new AiSimZone(currentZone);
		
		// no previous zone for now
		previous = null;
	}	
	
	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fait appara�tre al�atoirement des items lors de la destruction des murs */
	private boolean simulateItemsAppearing = false;

	/**
	 * d�termine si l'apparition d'items sera simul�e lors de la destruction de murs.
	 * � noter que la probabilit� d'apparition d�pends du nombre de murs restants
	 * et d'items cach�s restant � l'instant de l'explosion.
	 * de m�me, le type d'item d�pend de la distribution des items restants
	 * 
	 * @param 
	 * 		simulateItemsAppearing	si vrai, les items apparaitront lors de la simulation
	 */
	public void setSimulateItemsAppearing(boolean simulateItemsAppearing)
	{	this.simulateItemsAppearing = simulateItemsAppearing;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** zone issue de la simulation pr�c�dente */
	private AiSimZone previous;
	/** zone issue de la derni�re simulation */
	private AiSimZone current;
	
	/**
	 * renvoie la zone issue de la simulation pr�c�dente
	 * 
	 * @return	l'objet AiZone issu de la simulation pr�c�dente 
	 */
	public AiZone getPreviousZone()
	{	return previous;
	}

	/**
	 * renvoie la zone issue de la derni�re simulation
	 * 
	 * @return	l'objet AiZone issu de la derni�re simulation
	 */
	public AiZone getCurrentZone()
	{	return current;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des sprites dont le changement d'�tat marque la fin de la derni�re simulation */
	private List<AiSimSprite> limitSprites;
	
	/**
	 * renvoie la liste des sprites dont le changement d'�tat
	 * marque la fin de la derni�re simulation
	 * 
	 * @return	une liste de sprites
	 */
	public List<AiSprite> getLimitSprites()
	{	List<AiSprite> result = new ArrayList<AiSprite>(limitSprites);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// DURATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** dur�e �coul�e entre les zones previous et current */ 
	private long duration;
	
	/**
	 * renvoie la dur�e �coul�e entre les zones previous et current
	 * 
	 * @return	un entier long repr�sentant une dur�e
	 */
	public long getDuration()
	{	return duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * effectue des simulations, en gardant pour chaque sprite l'action courante,
	 * et ce jusqu'� ce que le personnage sp�cifi� ait chang� d'�tat.
	 * Cette m�thode est particuli�rement utile quand on veut savoir quel sera
	 * l'�tat estim� de la zone quand le personnage que l'on controle passera
	 * dans la case suivante.
	 * <b>Attention:</b> le changement d'�tat peut aussi �tre d� au fait que le 
	 * personnage a commenc� � br�ler.
	 * 
	 * @param 
	 * 		hero	le personnage sur lequel porte la condition
	 * @return	
	 * 		vrai si le changement d'�tat est d� � un d�placement (et pas � un accident)
	 */
	public boolean simulateUntilCondition(AiHero hero)
	{	// init
		HashMap<AiSprite,AiState> specifiedStates = new HashMap<AiSprite, AiState>();
		
		boolean found = false;
		do
		{	// simulate
			simulate(specifiedStates);
			// check if the hero was among the limit sprites
			found = limitSprites.contains(hero);
		}
		while(!found);
		
		// check if the hero is still safe
		hero = current.getSpriteById(hero); //get the latest representation of the sprite
		AiState state = hero.getState();
		AiStateName name = state.getName();
		boolean result = name==AiStateName.FLYING || name==AiStateName.MOVING || name==AiStateName.STANDING;
		return result;
	}
	
	/**
	 * calcule l'�tat suivant de la zone si les �tats sp�cifi�s en param�tres
	 * sont appliqu�s � la zone courante. La m�thode renvoie l'�tat obtenu
	 * � la fin du prochain �v�nement (i.e. celui qui se termine le plus vite). 
	 * Les �v�nement consid�r�s sont :
	 * 		- la disparition d'un sprite (ex : une bombe qui a explos�)
	 * 		- l'apparition d'un sprite (ex : un item qui apparait � la suite de l'explosion d'un mur)
	 * 		- un changement d'�tat (ex : un mur qui commence � br�ler)
	 * 		- un changement de case (ex : un joueur passant d'une case � une autre)
	 * 		- la fin d'un d�placement (ex : un joueur qui se retrouve bloqu� par un mur)
	 * Les modifications sont appliqu�es aux zones internes. l'utilisateur peut r�cup�rer
	 * la nouvelle zone mise � jour avec getCurrentZone. Il peut �galement r�cup�rer la liste
	 * de sprites qui ont provoqu� la fin de la mise � jour � la suite d'une action, avec getLimitSprites.
	 * Il peut aussi r�cup�rer la dur�e qui s'est �coul�e (en temps simul�) depuis la derni�re simulation, 
	 * avec getDuration.
	 * 
	 * @param specifiedStates	
	 * 		map associant un �tat � un sprite, permettant de forcer un sprite � prendre un certain �tat 
	 */
	public void simulate(HashMap<AiSprite,AiState> specifiedStates)
	{	// create a copy of the current zone
		previous = current;
		current = new AiSimZone(previous);
		
		// converting the specified states
		HashMap<AiSimSprite,AiState> localSpecifiedStates = new HashMap<AiSimSprite, AiState>();
		for(Entry<AiSprite,AiState> entry: specifiedStates.entrySet())
		{	AiSprite sprite = entry.getKey();
			AiState state = entry.getValue();
			AiSimSprite simSprite = current.getSpriteById(sprite);
			localSpecifiedStates.put(simSprite,state);
		}
		
		// update detonating bombs (done first cause it can mess up the whole zone)
		for(AiSimBomb bomb: current.getInternalBombs())
		{	AiSimState state0 = bomb.getState();
			AiStateName name0 = state0.getName();
			AiState state = localSpecifiedStates.get(bomb);
			boolean detonate = false;
			if(name0==AiStateName.STANDING || name0==AiStateName.MOVING)
			{	if(state!=null)
					detonate = state.getName()==AiStateName.BURNING;
				else
				{	long normalDuration = bomb.getNormalDuration();
					long time0 = state0.getTime();
					detonate = bomb.hasCountdownTrigger() && time0>=normalDuration;
				}
			}
			if(detonate)
				detonateBomb(bomb);
		}
		
		// retrieve all sprites remaining in the zone
		List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
		sprites.addAll(current.getInternalBlocks());
		sprites.addAll(current.getInternalBombs());
		sprites.addAll(current.getInternalFires());
		sprites.addAll(current.getInternalFloors());
		sprites.addAll(current.getInternalHeroes());
		sprites.addAll(current.getInternalItems());
		
		// process iteration duration
		HashMap<AiSimSprite,AiSimState> statesMap = new HashMap<AiSimSprite, AiSimState>();
		processDuration(sprites,localSpecifiedStates,statesMap);
		
		// apply events for the resulting minimal time
		updateSprites(statesMap,duration);
		
		// update the resulting zone
		current.updateTime(duration);
	}
	/**
	 * TODO chemins � g�n�raliser
	 * 	- associer un temps avec chaque case (attente) ?
	 *  - structure abstraite, on peut acc�der aux cases ou aux pixels
	 *  - le chemin est entier, on passe le perso en param�tre et on nous dit la case suivante (null si pas sur le chemin)
	 *    voire la direction � prendre pour suivre le chemin
	 *  - � voir comment �a peut �tre repr�sent� derri�re...
	 */
	
	/*
	 * NOTE
	 * dans AiZone, �a serait bien d'avoir la liste des temps d'explosion des cases
	 * >> voire le truc d�taill� avec le d�but/fin de chaque explosion ?
	 * 
	 * pour les m�thodes destin�es aux �tudiants (public), remplacer
	 * un sprite/tile par le m�me sprite dans la zone courante, si besoin
	 * pour les m�thodes appel�es en interne, on peut supposer que c'est inutile
	 */
	
	/**
	 * TODO
	 * - modifier simulateUntilCondition pour ne pas avoir de pb pr retrouver le hero dans la liste
	 * - d�finir deux versions diff�rentes de simulate (une externe, une interne)
	 * - revoir si tous les �v�nements sp�cifi�s dans la doc sont bien impl�ment�s
	 * 		p-� : pouvoir pr�ciser sur quels �v�nements on veut s'arr�ter ?
	 */
	
	/**
	 * d�termine comment chaque sprite va �voluer en termes de changement d'�tats,
	 * et calcule le temps minimal avant le prochain changement d'�tat.
	 * ce laps de temps sera ensuite appliqu� uniform�ment � chaque sprite.
	 * 
	 * @param sprites
	 * 		liste des sprites � traiter
	 * @param specifiedStates
	 * 		liste des �tats d�j� sp�cifi�s
	 */
	private void processDuration(List<AiSimSprite> sprites, HashMap<AiSimSprite,AiState> specifiedStates, HashMap<AiSimSprite,AiSimState> statesMap)
	{	// init
		duration = Long.MAX_VALUE;
		limitSprites = new ArrayList<AiSimSprite>();
		
		for(AiSimSprite sprite: sprites)
		{	AiSimState state;
			// get the specified new state for this sprite
if(sprite instanceof AiSimHero)
	System.out.print("");
if(sprite instanceof AiSimBomb)
	System.out.print("");
			AiState temp = specifiedStates.get(sprite);
			if(temp!=null)
				state = new AiSimState(temp);
			// or get an automatically processed one if no specified state is available
			else
				state = processNewState(sprite);
			// add to map
			statesMap.put(sprite,state);
			// then process the time remaining before the next state change
			if(state.getName()!=AiStateName.ENDED)
			{	long changeTime = processChangeTime(current,sprite,state);
				if(changeTime>0) //zero means there's nothing to do, e.g.: moving towards an obstacle
				{	// new min time
					if(changeTime<duration)
					{	duration = changeTime;
						limitSprites.clear();
						limitSprites.add(sprite);
					}
					// equals existing min time
					else if(changeTime==duration)
						limitSprites.add(sprite);
				}
			}
		}
		
		// no state change means no change at all
		if(duration==Long.MAX_VALUE)
			duration = 0;
	}
	
	/**
	 * calcule le nouvel �tat du sprite pass� en param�tre,
	 * quand aucun �tat n'a �t� explicitement sp�cifi� pour lui.
	 * 
	 * @param sprite	le sprite � traiter
	 * @return	son nouvel �tat
	 */
	private AiSimState processNewState(AiSimSprite sprite)
	{	// previous state
		AiSimTile tile0 = sprite.getTile();
		AiSimState state0 = sprite.getState();
		long time0 = state0.getTime();
		AiStateName name0 = state0.getName();
		Direction direction0 = state0.getDirection();
		// result
		AiStateName name = name0;
		Direction direction = direction0;
		long time = time0;
		
		// sprite might have to disappear (block, bomb, fire, hero, item) after finishing burning
		if(name0==AiStateName.BURNING)
		{	long burningDuration = sprite.getBurningDuration();
			if(time0>=burningDuration) //NOTE problem for re-spawning sprites (but it's only an approximation, after all...)
			{	name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
			}
		}
		
		// a bomb might have to explode
		else if(sprite instanceof AiSimBomb)
		{	if(name0==AiStateName.STANDING || name0==AiStateName.MOVING)
			{	AiSimBomb bomb = (AiSimBomb) sprite;
				long normalDuration = bomb.getNormalDuration();
				if(bomb.hasCountdownTrigger()) //only for time bombs
				{	if(time0>=normalDuration)
					{	name = AiStateName.BURNING;
						direction = Direction.NONE;
						time = 0;
					}
				}
			}
		}

		// an item might have to disappear if it's been picked
		else if(sprite instanceof AiSimItem)
		{	if(name0==AiStateName.STANDING)
			{	if(tile0.getHeroes().size()>0)
				{	name = AiStateName.ENDED;
					direction = Direction.NONE;
					time = 0;
				}
			}
		}

		AiSimState result = new AiSimState(name,direction,time);
		return result;
	}
	
	/**
	 * calcule combien de temps il va falloir au sprite sp�cifi� pour sortir
	 * de l'�tat qui lui a �t� assign�. si le sprite br�le, il s'agit de savoir pendant
	 * combien de temps encore. s'il se d�place, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particuli�re � son activit� et la m�thode renvoie 0.
	 * 
	 * @param current	
	 * 		la zone courante
	 * @param sprite	
	 * 		le sprite � traiter
	 * @param state	
	 * 		le nouvel �tat de ce sprite
	 * @return	
	 * 		la dur�e pendant laquelle le sprite va rester � cet �tat
	 */
	private long processChangeTime(AiSimZone current, AiSimSprite sprite, AiSimState state)
	{	long result = Long.MAX_VALUE;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		
		// sprite burns: how long before it finishes burning?
		if(name==AiStateName.BURNING)
		{	long burningDuration = sprite.getBurningDuration();
			long elapsedTime = state.getTime();
			result = burningDuration - elapsedTime;
		}
		
		// sprite ended : should not be considered anymore
		else if(name==AiStateName.ENDED)
		{	result = Long.MAX_VALUE;
		}

		// sprite moves (on the ground or in the air): how long before it reaches the next tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	if(direction==Direction.NONE)
				result = Long.MAX_VALUE;
			else
			{	//NOTE simplification here: we suppose the levels are all grids, 
				// meaning at least one coordinate is at the center of a tile
				double speed = sprite.getCurrentSpeed();
				if(sprite instanceof AiSimHero)
				{	AiSimHero hero = (AiSimHero) sprite;
					speed = hero.getWalkingSpeed();
				}
				else if(sprite instanceof AiSimBomb)
				{	AiSimBomb bomb = (AiSimBomb) sprite;
					speed = bomb.getSlidingSpeed();
				}
				int dir[] = direction.getIntFromDirection();
				AiSimTile tile = sprite.getTile();
				double tileSize = tile.getSize();
				double posX = sprite.getPosX();
				double posY = sprite.getPosY();
				double tileX = tile.getPosX();
				double tileY = tile.getPosY();
				AiSimTile neighborTile = tile.getNeighbor(direction);
				double offset = 0;
				if(neighborTile.isCrossableBy(sprite)) //deal with obstacles
					offset = tileSize/2;
				double goalX = current.normalizePositionX(tileX+dir[0]*offset);
				double goalY = current.normalizePositionY(tileY+dir[1]*offset);
				double manDist = Math.abs(posX-goalX)+Math.abs(posY-goalY);
				double temp = 1000*manDist/speed;
				if(temp<1)
					result = 0;
				else
					result = (long)temp;
			}
			// it can also be a bomb waiting to explode
			if(sprite instanceof AiSimBomb)
			{	AiSimBomb bomb = (AiSimBomb) sprite;
				if(bomb.hasCountdownTrigger())
				{	long normalDuration = bomb.getNormalDuration();
					long temp = normalDuration - state.getTime();
					if(temp<0)
						temp = 0;
					if(temp<result)
						result = temp;
				}
			}
		}
		
		// sprites just stands doing nothing special
		else if(name==AiStateName.STANDING)
		{	// it can also be a bomb waiting to explode
			if(sprite instanceof AiSimBomb)
			{	AiSimBomb bomb = (AiSimBomb) sprite;
				if(bomb.hasCountdownTrigger())
				{	long normalDuration = bomb.getNormalDuration();
					result = normalDuration - state.getTime();
					if(result<0)
						result = 0;
				}
			}
			else
				result = Long.MAX_VALUE;
		}
	
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** compteur d'id */
	private int spriteId = Integer.MAX_VALUE; 

	/**
	 * permet de g�n�rer des id pour les sprites cr��s lors des simulation
	 * 
	 * @return
	 * 		une nouvelle id de sprite
	 */
	private int createNewId()
	{	int result = spriteId;
		spriteId--;
		return result;
	}

	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param statesMap	
	 * 		la liste des �tats � appliquer aux sprites (calcul�s automatiquement ou sp�cifi�s par l'utilisateur)
	 * @param time	
	 * 		la dur�e � prendre en compte
	 */
	private void updateSprites(HashMap<AiSimSprite,AiSimState> statesMap, long duration)
	{	
		for(Entry<AiSimSprite,AiSimState> entry: statesMap.entrySet())
		{	// init
			AiSimSprite sprite = entry.getKey();
			AiSimState state = entry.getValue();
			
			// block
			if(sprite instanceof AiSimBlock)
			{	AiSimBlock block = (AiSimBlock)sprite;
				updateBlock(block,state,duration);
			}
			
			// bomb
			else if(sprite instanceof AiSimBlock)
			{	AiSimBomb bomb = (AiSimBomb)sprite;
				updateBomb(bomb,state,duration);
			}
			
			// fire
			else if(sprite instanceof AiSimFire)
			{	AiSimFire fire = (AiSimFire)sprite;
				updateFire(fire,state,duration);
			}
			
			// floor
			else if(sprite instanceof AiSimFloor)
			{	AiSimFloor floor = (AiSimFloor)sprite;
				updateFloor(floor,state,duration);
			}
			
			// hero
			else if(sprite instanceof AiSimHero)
			{	AiSimHero hero = (AiSimHero)sprite;
				updateHero(hero,state,duration);
			}
			
			// item
			else if(sprite instanceof AiSimItem)
			{	AiSimItem item = (AiSimItem)sprite;
				updateItem(item,state,duration);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param block
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState	
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration	
	 * 		la dur�e � prendre en compte
	 */
	private void updateBlock(AiSimBlock block, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		
		// always increment the time spent in this state
		long time = newState.getTime() + duration;
		newState.setTime(time);

		// block is burning
		if(name==AiStateName.BURNING)
		{	// update
			block.setCurrentSpeed(0);
			block.setState(newState);
		}
		
		// block has finished burning: might release an item
		else if(name==AiStateName.ENDED)
		{	// update
			block.setCurrentSpeed(0);
			block.setState(newState);
			// remove from zone
			current.removeSprite(block);
			// possibly release an item
			if(simulateItemsAppearing && current.getHiddenItemsCount()>0)
			{	// select item type
				HashMap<AiItemType, Double> probas = current.getHiddenItemsProbas();
				double p = Math.random();
				double total = 0;
				Iterator<Entry<AiItemType,Double>> it = probas.entrySet().iterator();
				AiItemType itemType = null;
				do
				{	Entry<AiItemType,Double> entry = it.next();
					AiItemType type = entry.getKey();
					double value = entry.getValue();
					total = total + value;
					if(p<=total)
						itemType = type;
				}
				while(itemType==null && it.hasNext());
				// create item
				int id = createNewId();
				AiSimTile tile = block.getTile();
				double posX = tile.getPosX();
				double posY = tile.getPosY();
				double posZ = 0;
				AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
				long burningDuration = 0; //can't retrieve it
				double currentSpeed = 0;
				AiStopType stopBombs = AiStopType.WEAK_STOP;
				AiStopType stopFires = AiStopType.WEAK_STOP;
				AiSimItem item = new AiSimItem(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed,itemType,stopBombs,stopFires);
				// update zone
				current.addSprite(item);
				current.updateHiddenItemsCount(itemType);
			}
		}
		
		// blocks can't move (at least for now)
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// block just stands
		else if(name==AiStateName.STANDING)
		{	// update
			block.setState(newState);
		}
	}

	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param bomb
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateBomb(AiSimBomb bomb, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		
		// always increment the time spent in this state
		long time = newState.getTime() + duration;
		newState.setTime(time);

		// bomb is burning
		if(name==AiStateName.BURNING)
		{	// update
			bomb.setCurrentSpeed(0);
			bomb.setState(newState);
		}
		
		// bomb has finished burning : should disappear
		else if(name==AiStateName.ENDED)
		{	// update
			bomb.setCurrentSpeed(0);
			bomb.setState(newState);
			AiSimHero owner = bomb.getOwner();
			// update owner
			if(owner!=null)
				owner.updateBombNumber(-1);
			// remove from zone
			current.removeSprite(bomb);
		}
		
		// bomb is moving : process changes in location/tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	//NOTE same simplification than before: we suppose the levels are all grids, 
			// meaning at least one coordinate is at the center of a tile
			// plus players move in a primary direction (DOWN, LEFT, RIGHT, UP: no composite) 
			AiSimTile tile = bomb.getTile();
			Direction direction = newState.getDirection();
			double posX = bomb.getPosX();
			double posY = bomb.getPosY();
			double posZ = bomb.getPosZ();
			double currentSpeed = bomb.getSlidingSpeed();
			double allowed = currentSpeed*duration/1000;
			int dir[] = direction.getIntFromDirection();
			double tileSize = tile.getSize();
			double tileX0 = tile.getPosX();
			double tileY0 = tile.getPosY();
			AiSimTile neighborTile = tile.getNeighbor(direction);
			double offset = 0;
			if(neighborTile.isCrossableBy(bomb)) //deal with obstacles
				offset = tileSize/2;
			double goalX = current.normalizePositionX(tileX0+dir[0]*offset);
			double goalY = current.normalizePositionY(tileY0+dir[1]*offset);
			double dx = Math.abs(posX-goalX);
			double dy = Math.abs(posY-goalY);
			double manDist = dx+dy;
			if(manDist<=allowed)
			{	posX = goalX;
				posY = goalY;
				if(offset==0)
					currentSpeed = 0;
			}
			else
			{	if(dx>dy && dy>0)
				{	double temp = Math.min(allowed,dy);
					posY = posY+dir[1]*temp;
					allowed = allowed - temp;
					posX = posX+dir[0]*allowed;
				}
				else
				{	double temp = Math.min(allowed,dx);
					posX = posX+dir[0]*temp;
					allowed = allowed - temp;
					posY = posY+dir[1]*allowed;
				}
			}
			// update tile
			AiSimTile newTile = current.getTile(posX,posY);
			if(!newTile.equals(tile))
			{	tile.removeSprite(bomb);
				tile.addSprite(bomb);
				bomb.setTile(tile);
			}
			
			// update location
			bomb.setPos(posX,posY,posZ);
			
			// update state
			bomb.setCurrentSpeed(currentSpeed);
			bomb.setState(newState);
		}
		
		// bomb just stands
		else if(name==AiStateName.STANDING)
		{	// update
			bomb.setCurrentSpeed(0);
			bomb.setState(newState);
		}
	}

	public void detonateBomb(AiBomb bomb)
	{	// get the bomb
		AiSimBomb simBomb = current.getSpriteById(bomb);
		// detonate the bomb
		detonateBomb(simBomb);
	}
	
	protected void detonateBomb(AiSimBomb bomb)
	{	// process each tile in the blast
		List<AiTile> blast = bomb.getBlast();
		for(AiTile tile: blast)
		{	AiSimTile simTile = (AiSimTile) tile;
			burnTile(simTile,bomb);
		}
		
		// update the bomb owner
//		AiSimHero hero = bomb.getOwner();
//		hero.updateBombNumber(1);
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param fire
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateFire(AiSimFire fire, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		
		// always increment the time spent in this state
		long time = newState.getTime() + duration;
		newState.setTime(time);

		// fire is burning
		if(name==AiStateName.BURNING)
		{	// update
			fire.setState(newState);
		}
		
		// fire has finished burning : should disappear
		else if(name==AiStateName.ENDED)
		{	// update
			fire.setState(newState);
			// remove from zone
			current.removeSprite(fire);
		}

		// fire can't move
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// fire can't just stand: it burns
		else if(name==AiStateName.STANDING)
		{	// useless here
		}
	}

	protected void burnTile(AiSimTile tile, AiSimBomb detonatingBomb)
	{	AiFire firePrototype = detonatingBomb.getFirePrototype();
		
		// if the fire can appear, we affect it to the tile
		if(tile.isCrossableBy(firePrototype))
		{	// create sprite
			AiSimFire fire = new AiSimFire(firePrototype,tile);
			current.addSprite(fire);
			// set properties
			fire.setId(createNewId());
			AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
			fire.setState(state);
		}
		
		// in any case, the tile content should be burned
		
		// blocks
		for(AiSimBlock block: tile.getInternalBlocks())
		{	AiStateName name = block.getState().getName();
			if(block.isDestructible() && name==AiStateName.STANDING)
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				block.setState(state);
			}
		}
		
		// bombs
		for(AiSimBomb bomb: tile.getInternalBombs())
		{	AiStateName name = bomb.getState().getName();
			if(bomb.hasExplosionTrigger() && (name==AiStateName.STANDING || name==AiStateName.MOVING))
				detonateBomb(bomb);
		}

		// heroes
		for(AiSimHero hero: tile.getInternalHeroes())
		{	AiStateName name = hero.getState().getName();
			if(!hero.hasThroughFires() && (name==AiStateName.STANDING || name==AiStateName.MOVING))
			{	Direction direction = hero.getState().getDirection();
				AiSimState state = new AiSimState(AiStateName.BURNING,direction,0);
				hero.setState(state);
			}
		}
		
		// items
		for(AiSimItem item: tile.getInternalItems())
		{	AiStateName name = item.getState().getName();
			//if(simItem.isDestructible())
			if(name==AiStateName.STANDING)
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				item.setState(state);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param floor
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateFloor(AiSimFloor floor, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		
		// always increment the time spent in this state
		long time = newState.getTime() + duration;
		newState.setTime(time);

		// hero is burning
		if(name==AiStateName.BURNING)
		{	// update
			floor.setState(newState);
		}
		
		// floor is disappearing (how?)
		else if(name==AiStateName.ENDED)
		{	// update
			floor.setState(newState);
			// remove from zone
			current.removeSprite(floor);
		}
		
		// floors can't move (at least for now)
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// floor just stands
		else if(name==AiStateName.STANDING)
		{	// update
			floor.setState(newState);
		}
	}

	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param hero
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateHero(AiSimHero hero, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		
		// always increment the time spent in this state
		long time = newState.getTime() + duration;
		newState.setTime(time);

		// hero is burning
		if(name==AiStateName.BURNING)
		{	// update
			hero.setCurrentSpeed(0);
			hero.setState(newState);
		}
		
		// hero has finished burning (or did somehow die)
		else if(name==AiStateName.ENDED)
		{	// update
			hero.setCurrentSpeed(0);
			hero.setState(newState);
			// remove from zone
			current.removeSprite(hero);
			// NOTE items could be released here... (to be completed)
		}
		
		// hero is moving : process changes in location/tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	//NOTE same simplification than before: we suppose the levels are all grids, 
			// meaning at least one coordinate is at the center of a tile
			// plus players move in a primary direction (DOWN, LEFT, RIGHT, UP: no composite) 
			AiSimTile tile = hero.getTile();
			Direction direction = newState.getDirection();
			double posX = hero.getPosX();
			double posY = hero.getPosY();
			double posZ = hero.getPosZ();
			double currentSpeed = hero.getWalkingSpeed();
			double allowed = currentSpeed*duration/1000;
			int dir[] = direction.getIntFromDirection();
			double tileSize = tile.getSize();
			double tileX0 = tile.getPosX();
			double tileY0 = tile.getPosY();
			AiSimTile neighborTile = tile.getNeighbor(direction);
			double offset = 0;
			if(neighborTile.isCrossableBy(hero)) //deal with obstacles
				offset = tileSize/2;
			double goalX = current.normalizePositionX(tileX0+dir[0]*offset);
			double goalY = current.normalizePositionY(tileY0+dir[1]*offset);
			double dx = Math.abs(posX-goalX);
			double dy = Math.abs(posY-goalY);
			double manDist = dx+dy;
			if(manDist<=allowed)
			{	posX = goalX;
				posY = goalY;
				if(offset==0)
					currentSpeed = 0;
			}
			else
			{	if(dx>dy && dy>0)
				{	double temp = Math.min(allowed,dy);
					posY = posY+dir[1]*temp;
					allowed = allowed - temp;
					posX = posX+dir[0]*allowed;
				}
				else
				{	double temp = Math.min(allowed,dx);
					posX = posX+dir[0]*temp;
					allowed = allowed - temp;
					posY = posY+dir[1]*allowed;
				}
			}
			// update tile
			AiSimTile newTile = current.getTile(posX,posY);
			if(!newTile.equals(tile))
			{	tile.removeSprite(hero);
				newTile.addSprite(hero);
				hero.setTile(newTile);
			}
			
			// might pick an item in the new tile
			if(!newTile.equals(tile))
			{	List<AiSimItem> items = newTile.getInternalItems(); 
				for(AiSimItem item: items)
				{	AiStateName itemStateName = item.getState().getName();
					if(itemStateName==AiStateName.STANDING)
						pickItem(hero,item);
				}
			}
			
			// update location
			hero.setPos(posX,posY,posZ);
			
			// update state
			hero.setCurrentSpeed(currentSpeed);
			hero.setState(newState);
		}
		
		// hero just stands
		else if(name==AiStateName.STANDING)
		{	// update
			hero.setCurrentSpeed(0);
			hero.setState(newState);
		}
	}

	public AiBomb dropBomb(AiTile tile, AiHero hero)
	{	// get the tile
		int line = tile.getLine();
		int col = tile.getCol();
		AiSimTile simTile = current.getTile(line,col);
		
		// get the hero
		AiSimHero simHero = current.getSpriteById(hero);
		
		// drop the bomb
		AiBomb bomb = dropBomb(simTile,simHero);
		
		return bomb;
	}

	/**
	 * Cr�e une nouvelle bombe appartenant au personnage pass� en param�tre.
	 * La bombe est plac�e au centre de la case pass�e en param�tre.
	 * Le compteur de bombe du personnage est incr�ment�.
	 * Si jamais la case contient d�j� un objet emp�chant de poser la bombe,
	 * celle-ci n'est pas cr��e et la fonction renvoie la valeur null.
	 * Sinon (si la cr�ation est possible) alors la fonction renvoie la bombe cr��e.
	 * Si jamais le personnage ne peut pas poser de bombes pour l'instant,
	 * la bombe n'est pas cr��e et la valeur null est renvoy�e. 
	 * 
	 * @param tile
	 * 		case qui contiendra la bombe nouvellement cr��e
	 * @param hero
	 * 		personnage � qui la bombe appartiendra (ce qui d�termine ses propri�t�s)
	 * @return
	 * 		la bombe si elle a pu �tre cr��e, ou null si ce n'est pas possible 
	 */
	protected AiSimBomb dropBomb(AiSimTile tile, AiSimHero hero)
	{	AiSimBomb result = null;
		
		// check if the hero can drop a bomb
		int dropped = hero.getBombCount();
		int max = hero.getBombNumber();
		if(dropped<max)
		{	// then check if the tile can host the bomb
			AiBomb bomb = hero.getBombPrototype();
			if(tile.isCrossableBy(bomb,false,false,true,false,true,false))
			{	// create the new bomb
				result = new AiSimBomb(bomb,tile);
				current.addSprite(result);
				// set its properties
				result.setId(createNewId());
				AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
				result.setState(state);
				
				// update the hero
				hero.updateBombNumber(+1);
				
				// check for fire
				if(tile.getFires().size()>0 && bomb.hasExplosionTrigger())
					detonateBomb(bomb);
			}
		}
		
		return result;
	}

	/**
	 * comme l'autre m�thode dropBomb, mais celle ci d�pose la bombe
	 * dans la case du personnage pass� en param�tre
	 * 
	 * @param hero
	 * 		le personnage devant d�poser la bombe
	 * @return
	 * 		la bombe d�pos�e, ou null si il �tait impossible de la poser
	 */
	protected AiBomb dropBomb(AiHero hero)
	{	AiTile tile = hero.getTile();
		return dropBomb(tile,hero);
	}
	
	protected void pickItem(AiSimHero hero, AiSimItem item)
	{	AiItemType type = item.getType();
		if(type==AiItemType.EXTRA_BOMB)
		{	hero.updateBombNumber(1);
		}
		else if(type==AiItemType.EXTRA_FLAME)
		{	hero.updateBombRange(1);
		}
		else if(type==AiItemType.MALUS)
		{	// nothing to do (can't know what the effect is)
			// or maybe deal with the contagious aspect?
		}
		else if(type==AiItemType.OTHER)
		{	// nothing to do
		}
		else if(type==AiItemType.PUNCH)
		{	// NOTE to be completed
		}
		
		// remove item
		AiSimState state = new AiSimState(AiStateName.ENDED,Direction.NONE,0);
		item.setState(state);
		current.removeSprite(item);
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param item
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param newState
	 * 		le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateItem(AiSimItem item, AiSimState newState, long duration)
	{	// init
		AiStateName name = newState.getName();
		AiStateName name0 = item.getState().getName();
		
		// might have already been ended while being picked up by a player
		if(name0!=AiStateName.ENDED)
		{	// always increment the time spent in this state
			long time = newState.getTime() + duration;
			newState.setTime(time);
	
			// item is burning (too bad!)
			if(name==AiStateName.BURNING)
			{	// update
				item.setCurrentSpeed(0);
				item.setState(newState);
			}
			
			// item has finished burning
			else if(name==AiStateName.ENDED)
			{	// update
				item.setState(newState);
				// remove from zone
				current.removeSprite(item);
			}
			
			// items can't move (at least for now)
			else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
			{	// useless here
			}
			
			// item just stands
			else if(name==AiStateName.STANDING)
			{	// update
				item.setState(newState);
			}
		}
	}
}
