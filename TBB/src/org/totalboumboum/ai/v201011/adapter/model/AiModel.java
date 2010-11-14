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

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiState;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
class AiModel
{	
	public AiModel(AiZone currentZone)
	{	// init the model with a copy of the current zone
		this.current = new AiSimZone(currentZone,true);
		// no previous zone for now
		previous = null;
	}	
	
	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** fait apparaître aléatoirement des items lors de la destruction des murs */
	private boolean simulateItemsAppearing = false;

	/**
	 * détermine si l'apparition d'items sera simulée lors de la destruction de murs.
	 * à noter que la probabilité d'apparition dépends du nombre de murs restants
	 * et d'items cachés restant à l'instant de l'explosion.
	 * de même, le type d'item dépend de la distribution des items restants
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
	/** zone issue de la simulation précédente */
	private AiSimZone previous;
	/** zone issue de la dernière simulation */
	private AiSimZone current;
	
	/**
	 * renvoie la zone issue de la simulation précédente
	 * 
	 * @return	l'objet AiZone issu de la simulation précédente 
	 */
	public AiZone getPreviousZone()
	{	return previous;
	}

	/**
	 * renvoie la zone issue de la dernière simulation
	 * 
	 * @return	l'objet AiZone issu de la dernière simulation
	 */
	public AiZone getCurrentZone()
	{	return current;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des sprites dont le changement d'état marque la fin de la dernière simulation */
	private List<AiSimSprite> limitSprites;
	
	/**
	 * renvoie la liste des sprites dont le changement d'état
	 * marque la fin de la dernière simulation
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
	/** durée écoulée entre les zones previous et current */ 
	private long duration;
	
	/**
	 * renvoie la durée écoulée entre les zones previous et current
	 * 
	 * @return	un entier long représentant une durée
	 */
	public long getDuration()
	{	return duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * effectue des simulations, en gardant pour chaque sprite l'action courante,
	 * et ce jusqu'à ce que le personnage spécifié ait changé d'état.
	 * Cette méthode est particulièrement utile quand on veut savoir quel sera
	 * l'état estimé de la zone quand le personnage que l'on controle passera
	 * dans la case suivante.
	 * <b>Attention:</b> le changement d'état peut aussi être du au fait que le 
	 * personnage a commencé à brûler
	 * 
	 * @param hero0	le personnage sur lequel porte la condition
	 * @return	vrai si le changement d'état est dû à un déplacement (et pas à un accident)
	 */
	public boolean predictZoneUntilCondition(AiSimHero hero0)
	{	// init
		PredefinedColor color0 = hero0.getColor();
		HashMap<AiSprite,AiState> specifiedStates = new HashMap<AiSprite, AiState>();
		
		AiSimHero hero = null;
		do
		{	// simulate
			predictZone(specifiedStates);
			// check if the hero was among the limit sprites
			Iterator<AiSimSprite> it = limitSprites.iterator();
			while(hero==null && it.hasNext())
			{	AiSimSprite sprite = it.next();
				if(sprite instanceof AiSimHero)
				{	AiSimHero h = (AiSimHero)sprite;
					PredefinedColor color = h.getColor();
					if(color0==color)
						hero = h;
				}
			}
		}
		while(hero==null);
		
		// check if the hero is still safe
		AiSimState state = hero.getState();
		AiStateName name = state.getName();
		boolean result = name==AiStateName.FLYING || name==AiStateName.MOVING || name==AiStateName.STANDING;
		return result;
	}
	
	/**
	 * calcule l'état suivant de la zone si les états spécifiés en paramètres
	 * sont appliqués à la zone courante. La méthode renvoie l'état obtenu
	 * à la fin de l'action la plus courte. Les actions considérées sont :
	 * 		- la disparition/apparition d'un sprite
	 * 		- un changement d'état
	 * 		- un changement de case
	 * Les modifications sont appliquées aux zones internes. l'utilisateur peut récupérer
	 * la nouvelle zone mise à jour avec getCurrentZone. Il peut également récupérer la liste
	 * de sprites qui ont provoqué la fin de la mise à jour à la suite d'une action, avec getLimitSprites.
	 * Il peut aussi récupérer la durée qui s'est écoulée (en temps simulé) depuis la dernière simulation, 
	 * avec getDuration.
	 * 
	 * @param specifiedStates	map associant un état à un sprite, permettant de forcer un sprite à prendre un certain état 
	 */
	public void predictZone(HashMap<AiSprite,AiState> specifiedStates)
	{	// create a new, empty zone
		AiSimZone result = new AiSimZone(current,false);
		HashMap<AiSimSprite,AiSimState> statesMap = new HashMap<AiSimSprite, AiSimState>();
		
		// init specified states
		HashMap<AiSimSprite,AiSimState> localSpecifiedStates = new HashMap<AiSimSprite, AiSimState>();
		for(Entry<AiSprite,AiState> entry: specifiedStates.entrySet())
		{	AiSprite sprite = entry.getKey();
			AiSimSprite simSprite = current.getSpriteById(sprite);
			AiState state = entry.getValue();
			AiSimState simState = new AiSimState(state);
			localSpecifiedStates.put(simSprite,simState);
		}
		
		// list all sprites
		List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
		sprites.addAll(current.getInternalBlocks());
		sprites.addAll(current.getInternalBombs());
		sprites.addAll(current.getInternalFires());
		sprites.addAll(current.getInternalFloors());
		sprites.addAll(current.getInternalHeroes());
		sprites.addAll(current.getInternalItems());
		
		// first review all the bombs to detect those on the point of exploding
		// and refresh the next state of the concerned tiles (ie those on the explosion path)
		List<AiSimTile> burntTiles = new ArrayList<AiSimTile>();
		List<AiSimBomb> explodedBombs = new ArrayList<AiSimBomb>();
		for(AiSimBomb bomb: current.getInternalBombs())
			checkExplosion(bomb,localSpecifiedStates,explodedBombs,burntTiles);
		
		// list the sprites incoming state: specified or automatically processed from the current state,
		// also process the minimal time needed for a sprite state change (when using the new state)
		duration = Long.MAX_VALUE;
		limitSprites = new ArrayList<AiSimSprite>();
		for(AiSimSprite sprite: sprites)
		{	AiSimState state;
			// get the specified new state for this sprite
if(sprite instanceof AiSimHero)
	System.out.print("");
if(sprite instanceof AiSimBomb)
	System.out.print("");
			AiSimState temp = localSpecifiedStates.get(sprite);
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
		
		if(duration==Long.MAX_VALUE)
			duration = 0;
		
		// apply events for the resulting minimal time
		for(Entry<AiSimSprite,AiSimState> entry: statesMap.entrySet())
		{	AiSimSprite sprite0 = entry.getKey();
if(sprite0 instanceof AiSimHero)
	System.out.print("");
if(sprite0 instanceof AiSimBomb)
	System.out.print("");
			AiSimState state = entry.getValue();
			applyState(sprite0,state,result,duration);
		}
		
		// update the resulting zone
		result.update(current,duration);
		
		// update internal zones
		previous = current;
		current = result;
	}
	
	/**
	 * détermine si une bombe va exploser ou pas, et les conséquences de cette explosion
	 * 
	 * @param bomb
	 * @param specifiedStates
	 * @param explodedBombs
	 * @param burntTiles
	 */
	private void checkExplosion(AiSimBomb bomb, HashMap<AiSimSprite,AiSimState> specifiedStates, List<AiSimBomb> explodedBombs, List<AiSimTile> burntTiles)
	{	if(!explodedBombs.contains(bomb))
		{	// get the next state for this bomb
			AiSimState state = specifiedStates.get(bomb);
			if(state==null)
			{	state = processNewState(bomb);
				specifiedStates.put(bomb,state);
			}
			
			// if the bomb starts burning, its explosion must be created
			if(state.getName()==AiStateName.BURNING && state.getTime()==0)
			{	explodedBombs.add(bomb);
				List<AiTile> blast = bomb.getBlast();
				for(AiTile tile: blast)
				{	AiSimTile simTile = (AiSimTile)tile;
					if(!burntTiles.contains(simTile))
					{	//AiSimFire fire = bomb.createFire(simTile);
//TODO compléter ici : état d feu, ajouter à la case/niveau, à la liste de sprites, etc.
						burntTiles.add(simTile);
						List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
						sprites.addAll(simTile.getInternalBlocks());
						sprites.addAll(simTile.getInternalBombs());
						sprites.addAll(simTile.getInternalFires());
						sprites.addAll(simTile.getInternalFloors());
						sprites.addAll(simTile.getInternalHeroes());
						sprites.addAll(simTile.getInternalItems());
						for(AiSimSprite s: sprites)
							checkBurning(s,specifiedStates,explodedBombs,burntTiles);
					}
				}
			}
		}
	}
	
	/**
	 * détermine le nouvel état pour des sprites touchés par une explosion
	 * 
	 * @param sprite
	 * @param specifiedStates
	 * @param explodedBombs
	 * @param burntTiles
	 */
	private void checkBurning(AiSimSprite sprite, HashMap<AiSimSprite,AiSimState> specifiedStates, List<AiSimBomb> explodedBombs, List<AiSimTile> burntTiles)
	{	if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			if(bomb.hasExplosionTrigger()) //NOTE simplification: there actually is a latency before the bomb explodes
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(bomb,state);
			}
			checkExplosion(bomb,specifiedStates,explodedBombs,burntTiles);
		}
		else if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;	
			if(block.isDestructible())
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(block,state);
			}
		}
		else if(sprite instanceof AiSimFire)
		{	//AiFire Fire = (AiFire)sprite;	
			// useless here
		}
		else if(sprite instanceof AiSimFloor)
		{	//AiFloor floor = (AiFloor)sprite;	
			//nothing to do
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;	
			if(!hero.hasThroughFires())
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(hero,state);
			}
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;	
			// NOTE simplification, because some items are not actually destructed but just moved away
			AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
			specifiedStates.put(item,state);
		}
	}
	
	/**
	 * calcule le nouvel état du sprite passé en paramètre,
	 * quand aucun état n'a été explicitement spécifié pour lui.
	 * 
	 * @param sprite	le sprite à traiter
	 * @return	son nouvel état
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
	 * calcule combien de temps il va falloir au sprite spécifié pour sortir
	 * de l'état qui lui a été assigné. si le sprite brule, il s'agit de savoir pendant
	 * combien de temps encore. s'il se déplace, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particulière à son activité.
	 * 
	 * @param current	la zone courante
	 * @param sprite	le sprite à traiter
	 * @param state	le nouvel état de ce sprite
	 * @return	la durée pendant laquelle le sprite va rester à cet état
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
					result = 1;
				else
					result = (long)temp;
			}
			// it can also be a bomb waiting to explode
			if(sprite instanceof AiSimBomb)
			{	AiSimBomb bomb = (AiSimBomb) sprite;
				if(bomb.hasCountdownTrigger())
				{	long normalDuration = bomb.getNormalDuration();
					result = normalDuration - state.getTime();
					if(result<0)
						result = 0;
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
	 * permet de générer des id pour les sprites créés lors des simulation
	 * @return
	 */
	private int createNewId()
	{	int result = spriteId;
		spriteId--;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimBlock block, AiSimState newState, long duration)
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimBomb bomb, AiSimState newState, long duration)
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimFire fire, AiSimState newState, long duration)
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

	protected void burnTile(AiSimTile tile, AiSimBomb bomb)
	{	AiFire firePrototype = bomb.getFirePrototype();
		
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
		for(AiBlock block: tile.getBlocks())
		{	AiSimBlock simBlock = (AiSimBlock)block;
			if(simBlock.isDestructible())
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				simBlock.setState(state);
			}
		}
		
		// bombs
		for(AiBomb tempBomb: tile.getBombs())
		{	AiSimBomb simBomb = (AiSimBomb)tempBomb;
			if(simBomb.hasExplosionTrigger())
				detonateBomb(simBomb);
		}

		// heroes
		for(AiHero hero: tile.getHeroes())
		{	AiSimHero simHero = (AiSimHero)hero;
			if(!simHero.hasThroughFires())
			{	Direction direction = hero.getState().getDirection();
				AiSimState state = new AiSimState(AiStateName.BURNING,direction,0);
				simHero.setState(state);
			}
		}
		
		// items
		for(AiItem item: tile.getItems())
		{	AiSimItem simItem = (AiSimItem)item;
			//if(simItem.isDestructible())
			{	AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				simItem.setState(state);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimFloor floor, AiSimState newState, long duration)
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimHero hero, AiSimState newState, long duration)
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
			// NOTE items could be released here...
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
				tile.addSprite(hero);
				hero.setTile(tile);
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
	 * Crée une nouvelle bombe appartenant au personnage passé en paramètre.
	 * La bombe est placée au centre de la case passée en paramètre.
	 * Le compteur de bombe du personnage est incrémenté.
	 * Si jamais la case contient déjà un objet empêchant de poser la bombe,
	 * celle-ci n'est pas créée et la fonction renvoie la valeur null.
	 * Sinon (si la création est possible) alors la fonction renvoie la bombe créée.
	 * Si jamais le personnage ne peut pas poser de bombes pour l'instant,
	 * la bombe n'est pas créée et la valeur null est renvoyée. 
	 * @param 
	 * 		tile	case qui contiendra la bombe nouvellement créée
	 * @param 
	 * 		hero	personnage à qui la bombe appartiendra (ce qui détermine ses propriétés)
	 * @return
	 * 		la bombe si elle a pu être créée, ou null si ce n'est pas possible 
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
	 * comme l'autre méthode dropBomb, mais celle ci dépose la bombe
	 * dans la case du personnage passé en paramètre
	 * 
	 * @param 
	 * 		hero	le personnage devant déposer la bombe
	 * 
	 * @return
	 * 		la bombe déposée, ou null si il était impossible de la poser
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param time	la durée à prendre en compte
	 */
	private void simulateSprite(AiSimItem item, AiSimState newState, long duration)
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

	/*
	 * NOTE
	 * dans AiZone, ça serait bien d'avoir la liste des temps d'explosion des cases
	 * >> voire le truc détaillé avec le début/fin de chaque explosion ?
	 * 
	 * pour les méthodes destinées aux étudiants (public), remplacer
	 * un sprite/tile par le même sprite dans la zone courante, si besoin
	 * pour les méthodes appelées en interne, on peut supposer que c'est inutile
	 */
}
