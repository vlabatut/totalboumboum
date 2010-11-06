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
	
	/**
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprite0	le sprite concerné (sa représentation initiale)
	 * @param state	le nouvel état de ce sprite (à appliquer jusqu'à la fin du temps imparti)
	 * @param result	la zone à mettre à jour
	 * @param time	la durée à prendre en compte
	 */
	private void applyState(AiSimSprite sprite0, AiSimState state, AiSimZone result, long duration)
	{	// previous state
		//AiState state0 = sprite0.getState();
		AiSimTile tile0 = sprite0.getTile();
		int line0 = tile0.getLine();
		int col0 = tile0.getCol();
		long burningDuration = sprite0.getBurningDuration();
		double currentSpeed = sprite0.getCurrentSpeed();
		double posX0 = sprite0.getPosX();
		double posY0 = sprite0.getPosY();
		double posZ0 = sprite0.getPosZ();
		
		// next state
		AiStateName name = state.getName();
		long time = state.getTime() + duration;
		Direction direction = state.getDirection();
		int line = line0;
		int col = col0;
		double posX = posX0;
		double posY = posY0;
		double posZ = posZ0;
		AiSimTile tile = result.getTile(line,col);
		AiSimSprite sprite;
		
		if(name==AiStateName.BURNING)
		{	state = new AiSimState(name,direction,time);
			currentSpeed = 0;
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
			// TODO if it's a bomb : should set the explosion
			// or is it better to set it before, in the main function ?
		}
		
		else if(name==AiStateName.ENDED)
		{	if(sprite0 instanceof AiSimBlock)
			{	// NOTE we can make an item appear here...
			}
		}
		
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	//NOTE same simplification than begfore: we suppose the levels are all grids, 
			// meaning at least one coordinate is at the center of a tile
			double allowed = sprite0.getCurrentSpeed()*duration/1000;
			if(sprite0 instanceof AiSimHero)
			{	AiSimHero hero = (AiSimHero) sprite0;
				allowed = hero.getWalkingSpeed()*duration/1000;
			}
			int dir[] = direction.getIntFromDirection();
			double tileSize = tile0.getSize();
			double tileX0 = tile0.getPosX();
			double tileY0 = tile0.getPosY();
			AiSimTile neighborTile = tile.getNeighbor(direction);
			double offset = 0;
			if(neighborTile.isCrossableBy(sprite0)) //deal with obstacles
				offset = tileSize/2;
			double goalX = result.normalizePositionX(tileX0+dir[0]*offset);
			double goalY = result.normalizePositionY(tileY0+dir[1]*offset);
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
			tile = result.getTile(posX,posY);
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
		}
		
		else if(name==AiStateName.STANDING)
		{	state = new AiSimState(name,direction,time);
			currentSpeed = 0;
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
		}
	}
	
	/**
	 * crée une nouvelle version du sprite en appliquant le nouvel état au sprite existant
	 * 
	 * @param sprite	sprite existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sprite correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimSprite applyStateSprite(AiSimSprite sprite, AiSimTile tile, long duration, 
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimSprite result = null;
		
		if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			result = applyStateBlock(block,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			result = applyStateBomb(bomb,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			result = applyStateFire(fire,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			result = applyStateFloor(floor,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			result = applyStateHero(hero,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			result = applyStateItem(item,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
	
		return result;
	}
	
	/**
	 * crée une nouvelle version du block en appliquant le nouvel état au sprite existant
	 * 
	 * @param sprite	block existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le block correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimBlock applyStateBlock(AiSimBlock block, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean destructible = block.isDestructible();
		AiStopType stopHeroes = block.hasStopHeroes();
		AiStopType stopFires = block.hasStopFires();
		AiSimBlock result = new AiSimBlock(block.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				destructible,stopHeroes,stopFires);
		return result;
	}

	/**
	 * crée une nouvelle version de la bombe en appliquant le nouvel état au sprite existant
	 * 
	 * @param bombe	bombe existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	la bombe correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimBomb applyStateBomb(AiSimBomb bomb, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean countdownTrigger = bomb.hasCountdownTrigger();
		boolean remoteControlTrigger = bomb.hasRemoteControlTrigger();
		boolean explosionTrigger = bomb.hasExplosionTrigger();
		long normalDuration = bomb.getNormalDuration();
		long explosionDuration = bomb.getExplosionDuration();
		long latencyDuration = bomb.getLatencyDuration();
		float failureProbability = bomb.getFailureProbability();
		AiStopType stopHeroes = bomb.hasStopHeroes();
		AiStopType stopFires = bomb.hasStopFires();
		boolean throughItems = bomb.hasThroughItems();
		int range = bomb.getRange();
		boolean penetrating = bomb.isPenetrating();
		PredefinedColor color = bomb.getColor();
		boolean working = bomb.isWorking();
		long time = bomb.getTime() + duration;
		
		AiSimBomb result = new AiSimBomb(bomb.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,explosionDuration,latencyDuration,failureProbability,
				stopHeroes,stopFires,throughItems,range,penetrating,color,working,time);
		return result;
	}

	/**
	 * crée une nouvelle version du feu en appliquant le nouvel état au sprite existant
	 * 
	 * @param feu	feu existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le feu correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimFire applyStateFire(AiSimFire fire, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean throughBlocks = fire.hasThroughBlocks();
		boolean throughBombs = fire.hasThroughBombs();
		boolean throughItems = fire.hasThroughItems();
		AiSimFire result = new AiSimFire(fire.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			throughBlocks,throughBombs,throughItems);
		return result;
	}

	/**
	 * crée une nouvelle version du sol en appliquant le nouvel état au sprite existant
	 * 
	 * @param sol	sol existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sol correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimFloor applyStateFloor(AiSimFloor floor, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimFloor result = new AiSimFloor(floor.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		return result;
	}

	/**
	 * crée une nouvelle version du personnage en appliquant le nouvel état au sprite existant
	 * 
	 * @param personnage	personnage existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le personnage correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimHero applyStateHero(AiSimHero hero, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	int bombRange = hero.getBombRange();
		int bombNumber = hero.getBombNumber();
		int bombCount = hero.getBombCount();
		boolean throughBlocks = hero.hasThroughBlocks();
		boolean throughBombs = hero.hasThroughBombs();
		boolean throughFires = hero.hasThroughFires();
		PredefinedColor color = hero.getColor();
		double walkingSpeed = hero.getWalkingSpeed();
		
		AiSimHero result = new AiSimHero(hero.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			bombRange,bombNumber,bombCount,
			throughBlocks,throughBombs,throughFires,
			color,walkingSpeed);
		return result;
	}

	/**
	 * crée une nouvelle version de l'item en appliquant le nouvel état au sprite existant
	 * 
	 * @param item	item existant
	 * @param tile	case du sprite existant
	 * @param duration	durée du nouvel état
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonnée
	 * @param posZ nouvelle altitude
	 * @param state	nouvel état
	 * @param burningDuration	nouvelle durée de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le item correspondant aux nouvelles descriptions passées en paramètres
	 */
	private AiSimItem applyStateItem(AiSimItem item, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiItemType type = item.getType();
		AiStopType stopBombs = item.hasStopBombs();
		AiStopType stopFires = item.hasStopFires();
		AiSimItem result = new AiSimItem(item.getId(),tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			type,stopBombs,stopFires);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TOOLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * fonction permettant de définir le nouvel état d'un sprite, afin
	 * de l'utiliser dans le modèle pour générer l'état suivant de la zone
	 * 
	 * @param name	nom de l'état
	 * @param direction	direction de l'action
	 * @return	l'état correspondant aux paramètres reçus
	 */
	public static AiSimState generateState(AiStateName name, Direction direction)
	{	long time = 0;
		AiSimState result = new AiSimState(name,direction,time);
		return result;
	}
}
