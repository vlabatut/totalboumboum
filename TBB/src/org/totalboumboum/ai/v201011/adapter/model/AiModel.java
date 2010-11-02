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
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
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
public final class AiModel
{	
	public AiModel(AiZone currentZone)
	{	this.current = currentZone;
		previous = null;
	}	
	
	/////////////////////////////////////////////////////////////////
	// ZONES				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** zone issue de la simulation pr�c�dente */
	private AiZone previous;
	/** zone issue de la derni�re simulation */
	private AiZone current;
	
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
	private List<AiSprite> limitSprites;
	
	/**
	 * renvoie la liste des sprites dont le changement d'�tat
	 * marque la fin de la derni�re simulation
	 * 
	 * @return	une liste de sprites
	 */
	public List<AiSprite> getLimitSprites()
	{	return limitSprites;
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
	 * <b>Attention:</b> le changement d'�tat peut aussi �tre du au fait que le 
	 * personnage a commenc� � br�ler
	 * 
	 * @param hero0	le personnage sur lequel porte la condition
	 * @return	vrai si le changement d'�tat est d� � un d�placement (et pas � un accident)
	 */
	public boolean predictZoneUntilCondition(AiHero hero0)
	{	// init
		PredefinedColor color0 = hero0.getColor();
		HashMap<AiSprite,AiState> specifiedStates = new HashMap<AiSprite, AiState>();
		
		AiHero hero = null;
		do
		{	// simulate
			predictZone(specifiedStates);
			// check if the hero was among the limit sprites
			Iterator<AiSprite> it = limitSprites.iterator();
			while(hero==null && it.hasNext())
			{	AiSprite sprite = it.next();
				if(sprite instanceof AiHero)
				{	AiHero h = (AiHero)sprite;
					PredefinedColor color = h.getColor();
					if(color0==color)
						hero = h;
				}
			}
		}
		while(hero==null);
		
		// check if the hero is still safe
		AiState state = hero.getState();
		AiStateName name = state.getName();
		boolean result = name==AiStateName.FLYING || name==AiStateName.MOVING || name==AiStateName.STANDING;
		return result;
	}
	
	/**
	 * calcule l'�tat suivant de la zone si les �tats sp�cifi�s en param�tres
	 * sont appliqu�s � la zone courante. La m�thode renvoie l'�tat obtenu
	 * � la fin de l'action la plus courte. Les actions consid�r�es sont :
	 * 		- la disparition/apparition d'un sprite
	 * 		- un changement d'�tat
	 * 		- un changement de case
	 * Les modifications sont appliqu�es aux zones internes. l'utilisateur peut r�cup�rer
	 * la nouvelle zone mise � jour avec getCurrentZone. Il peut �galement r�cup�rer la liste
	 * de sprites qui ont provoqu� la fin de la mise � jour � la suite d'une action, avec getLimitSprites.
	 * Il peut aussi r�cup�rer la dur�e qui s'est �coul�e (en temps simul�) depuis la derni�re simulation, 
	 * avec getDuration.
	 * 
	 * @param specifiedStates	map associant un �tat � un sprite, permettant de forcer un sprite � prendre un certain �tat 
	 */
	public void predictZone(HashMap<AiSprite,AiState> specifiedStates)
	{	// create a new, empty zone
		AiSimZone result = new AiSimZone(current);
		HashMap<AiSprite,AiSimState> statesMap = new HashMap<AiSprite, AiSimState>();
		
		// list all sprites
		List<AiSprite> sprites = new ArrayList<AiSprite>();
		sprites.addAll(current.getBlocks());
		sprites.addAll(current.getBombs());
		sprites.addAll(current.getFires());
		sprites.addAll(current.getFloors());
		sprites.addAll(current.getRemainingHeroes());
		sprites.addAll(current.getItems());
		
		// first review all the bombs to detect those on the point of exploding
		// and refresh the next state of the concerned tiles (ie those on the explosion path)
		List<AiTile> burntTiles = new ArrayList<AiTile>();
		List<AiBomb> explodedBombs = new ArrayList<AiBomb>();
		for(AiBomb bomb: current.getBombs())
			checkExplosion(bomb,specifiedStates,explodedBombs,burntTiles);
		
		// list the sprites incoming state: specified or automatically processed from the current state,
		// also process the minimal time needed for a sprite state change (when using the new state)
		duration = Long.MAX_VALUE;
		limitSprites = new ArrayList<AiSprite>();
		for(AiSprite sprite: sprites)
		{	AiSimState state;
			// get the specified new state for this sprite
if(sprite instanceof AiHero)
	System.out.println();
			AiState temp = specifiedStates.get(sprite);
			if(temp!=null)
				state = new AiSimState(temp);
			// or get an automatically processed one if no specified state is available
			else
				state = processNewState(sprite);
			// add to map
			statesMap.put(sprite,state);
			// then process the time remaining before the next state change
			if(state.getName()!=AiStateName.ENDED && state.getName()!=AiStateName.STANDING)
			{	long changeTime = processChangeTime(current,sprite,state);
				if(changeTime>0) //zero means there's nothing to do, eg: moving towards an obstacle
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
		
		// apply events for the resulting minimal time
		for(Entry<AiSprite,AiSimState> entry: statesMap.entrySet())
		{	AiSprite sprite0 = entry.getKey();
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
	 * d�termine si une bombe va exploser ou pas, et les cons�quences de cette explosion
	 * 
	 * @param bomb
	 * @param specifiedStates
	 * @param explodedBombs
	 * @param burntTiles
	 */
	private void checkExplosion(AiBomb bomb, HashMap<AiSprite,AiState> specifiedStates, List<AiBomb> explodedBombs, List<AiTile> burntTiles)
	{	if(!explodedBombs.contains(bomb))
		{	// get the next state for this bomb
			AiState state = specifiedStates.get(bomb);
			if(state==null)
			{	state = processNewState(bomb);
				specifiedStates.put(bomb,state);
			}
			
			// if the bomb starts burning, its explosion must be created
			if(state.getName()==AiStateName.BURNING && state.getTime()==0)
			{	explodedBombs.add(bomb);
				List<AiTile> blast = bomb.getBlast();
				for(AiTile tile: blast)
				{	if(!burntTiles.contains(tile))
					{	burntTiles.add(tile);
						List<AiSprite> sprites = new ArrayList<AiSprite>();
						sprites.addAll(tile.getBlocks());
						sprites.addAll(tile.getBombs());
						sprites.addAll(tile.getFires());
						sprites.addAll(tile.getFloors());
						sprites.addAll(tile.getHeroes());
						sprites.addAll(tile.getItems());
						for(AiSprite s: sprites)
							checkBurning(s,specifiedStates,explodedBombs,burntTiles);
					}
				}
			}
		}
	}
	
	/**
	 * d�termine le nouvel �tat pour des sprites touch�s par une explosion
	 * 
	 * @param sprite
	 * @param specifiedStates
	 * @param explodedBombs
	 * @param burntTiles
	 */
	private void checkBurning(AiSprite sprite, HashMap<AiSprite,AiState> specifiedStates, List<AiBomb> explodedBombs, List<AiTile> burntTiles)
	{	if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb)sprite;
			if(bomb.hasExplosionTrigger()) //NOTE simplification: there actually is a latency before the bomb explodes
			{	AiState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(bomb,state);
			}
			checkExplosion(bomb,specifiedStates,explodedBombs,burntTiles);
		}
		else if(sprite instanceof AiBlock)
		{	AiBlock block = (AiBlock)sprite;	
			if(block.isDestructible())
			{	AiState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(block,state);
			}
		}
		else if(sprite instanceof AiFire)
		{	//AiFire Fire = (AiFire)sprite;	
			// useless here
		}
		else if(sprite instanceof AiFloor)
		{	//AiFloor floor = (AiFloor)sprite;	
			//nothing to do
		}
		else if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero)sprite;	
			if(!hero.hasThroughFires())
			{	AiState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
				specifiedStates.put(hero,state);
			}
		}
		else if(sprite instanceof AiItem)
		{	AiItem item = (AiItem)sprite;	
			// NOTE simplification, because some items are not actually destructed but just moved away
			AiState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
			specifiedStates.put(item,state);
		}
	}
	
	/**
	 * calcule le nouvel �tat du sprite pass� en param�tre,
	 * quand aucun �tat n'a �t� explicitement sp�cifi� pour lui.
	 * 
	 * @param sprite	le sprite � traiter
	 * @return	son nouvel �tat
	 */
	private AiSimState processNewState(AiSprite sprite)
	{	// previous state
		AiTile tile0 = sprite.getTile();
		AiState state0 = sprite.getState();
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
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb) sprite;
			long normalDuration = bomb.getNormalDuration();
			if(normalDuration>0) //only for time bombs
			{	if(time0>=normalDuration)
				{	name = AiStateName.BURNING;
					direction = Direction.NONE;
					time = 0;
				}
			}
		}

		// an item might have to disappear if it's been picked
		else if(sprite instanceof AiItem)
		{	if(tile0.getHeroes().size()>0)
			{	name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
			}
		}

		AiSimState result = new AiSimState(name,direction,time);
		return result;
	}
	
	/**
	 * calcule combien de temps il va falloir au sprite sp�cifi� pour sortir
	 * de l'�tat qui lui a �t� assign�. si le sprite brule, il s'agit de savoir pendant
	 * combien de temps encore. s'il se d�place, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particuli�re � son activit�.
	 * 
	 * @param current	la zone courante
	 * @param sprite	le sprite � traiter
	 * @param state	le nouvel �tat de ce sprite
	 * @return	la dur�e pendant laquelle le sprite va rester � cet �tat
	 */
	private long processChangeTime(AiZone current, AiSprite sprite, AiState state)
	{	long result = Long.MAX_VALUE;
		AiStateName name = state.getName();
		
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
		{	Direction direction = state.getDirection();
			if(direction==Direction.NONE)
				result = Long.MAX_VALUE;
			else
			{	//NOTE simplification here: we suppose the levels are all grids, 
				// meaning at least one coordinate is at the center of a tile
				int dir[] = direction.getIntFromDirection();
				AiTile tile = sprite.getTile();
				double tileSize = tile.getSize();
				double posX = sprite.getPosX();
				double posY = sprite.getPosY();
				double tileX = tile.getPosX();
				double tileY = tile.getPosY();
				AiTile neighborTile = tile.getNeighbor(direction);
				double offset = 0;
				if(neighborTile.isCrossableBy(sprite)) //deal with obstacles
					offset = tileSize/2;
				double goalX = current.normalizePositionX(tileX+dir[0]*offset);
				double goalY = current.normalizePositionY(tileY+dir[1]*offset);
				double manDist = Math.abs(posX-goalX)+Math.abs(posY-goalY);
				result = (long)(manDist/sprite.getCurrentSpeed());
			}
		}
		
		// sprites just stands doing nothing special
		else if(name==AiStateName.STANDING)
		{	result = Long.MAX_VALUE;
		}
	
		return result;
	}
	
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param sprite0	le sprite concern� (sa repr�sentation initiale)
	 * @param state	le nouvel �tat de ce sprite (� appliquer jusqu'� la fin du temps imparti)
	 * @param result	la zone � mettre � jour
	 * @param time	la dur�e � prendre en compte
	 */
	private void applyState(AiSprite sprite0, AiSimState state, AiSimZone result, long duration)
	{	// previous state
		//AiState state0 = sprite0.getState();
		AiTile tile0 = sprite0.getTile();
		int line0 = tile0.getLine();
		int col0 = tile0.getCol();
		long burningDuration = sprite0.getBurningDuration();
		double currentSpeed = sprite0.getCurrentSpeed();
		double posX0 = sprite0.getPosX();
		double posY0 = sprite0.getPosY();
		double posZ0 = sprite0.getPosZ();
		
		// next state
		AiStateName name = state.getName();
		long time = state.getTime();
		Direction direction = state.getDirection();
		int line = line0;
		int col = col0;
		double posX = posX0;
		double posY = posY0;
		double posZ = posZ0;
		AiSimTile tile = result.getTile(line,col);
		AiSimSprite sprite;
		
		if(name==AiStateName.BURNING)
		{	time = time + duration;
			state = new AiSimState(name,direction,time);
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
			currentSpeed = 0;
			// TODO if it's a bomb : should set the explosion
			// or is it better to set it before, in the main function ?
		}
		
		else if(name==AiStateName.ENDED)
		{	if(sprite0 instanceof AiBlock)
			{	// NOTE we can make an item appear here...
			}
		}
		
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	//NOTE same simplification than begfore: we suppose the levels are all grids, 
			// meaning at least one coordinate is at the center of a tile
			double allowed = sprite0.getCurrentSpeed()*duration;
			if(sprite0 instanceof AiHero)
			{	AiHero hero = (AiHero) sprite0;
				allowed = hero.getWalkingSpeed()*duration;
			}
			int dir[] = direction.getIntFromDirection();
			double tileSize = tile0.getSize();
			double tileX0 = tile0.getPosX();
			double tileY0 = tile0.getPosY();
			AiTile neighborTile = tile.getNeighbor(direction);
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
		}
		
		else if(name==AiStateName.STANDING)
		{	time = time + duration;
			state = new AiSimState(name,direction,time);
			sprite = applyStateSprite(sprite0,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
			result.addSprite(sprite);
			currentSpeed = 0;
		}
	}
	
	/**
	 * cr�e une nouvelle version du sprite en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param sprite	sprite existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sprite correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimSprite applyStateSprite(AiSprite sprite, AiSimTile tile, long duration, 
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimSprite result = null;
		
		if(sprite instanceof AiBlock)
		{	AiBlock block = (AiBlock)sprite;
			result = applyStateBlock(block,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiBomb)
		{	AiBomb bomb = (AiBomb)sprite;
			result = applyStateBomb(bomb,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire)sprite;
			result = applyStateFire(fire,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiFloor)
		{	AiFloor floor = (AiFloor)sprite;
			result = applyStateFloor(floor,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero)sprite;
			result = applyStateHero(hero,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
		else if(sprite instanceof AiItem)
		{	AiItem item = (AiItem)sprite;
			result = applyStateItem(item,tile,duration,posX,posY,posZ,state,burningDuration,currentSpeed);
		}
	
		return result;
	}
	
	/**
	 * cr�e une nouvelle version du block en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param sprite	block existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le block correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimBlock applyStateBlock(AiBlock block, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean destructible = block.isDestructible();
		AiStopType stopHeroes = block.hasStopHeroes();
		AiStopType stopFires = block.hasStopFires();
		AiSimBlock result = new AiSimBlock(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				destructible,stopHeroes,stopFires);
		return result;
	}

	/**
	 * cr�e une nouvelle version de la bombe en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param bombe	bombe existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	la bombe correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimBomb applyStateBomb(AiBomb bomb, AiSimTile tile, long duration,
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
		
		AiSimBomb result = new AiSimBomb(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,explosionDuration,latencyDuration,failureProbability,
				stopHeroes,stopFires,throughItems,range,penetrating,color,working,time);
		return result;
	}

	/**
	 * cr�e une nouvelle version du feu en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param feu	feu existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le feu correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimFire applyStateFire(AiFire fire, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	boolean throughBlocks = fire.hasThroughBlocks();
		boolean throughBombs = fire.hasThroughBombs();
		boolean throughItems = fire.hasThroughItems();
		AiSimFire result = new AiSimFire(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			throughBlocks,throughBombs,throughItems);
		return result;
	}

	/**
	 * cr�e une nouvelle version du sol en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param sol	sol existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le sol correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimFloor applyStateFloor(AiFloor floor, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiSimFloor result = new AiSimFloor(tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		return result;
	}

	/**
	 * cr�e une nouvelle version du personnage en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param personnage	personnage existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le personnage correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimHero applyStateHero(AiHero hero, AiSimTile tile, long duration,
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
		
		AiSimHero result = new AiSimHero(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			bombRange,bombNumber,bombCount,
			throughBlocks,throughBombs,throughFires,
			color,walkingSpeed);
		return result;
	}

	/**
	 * cr�e une nouvelle version de l'item en appliquant le nouvel �tat au sprite existant
	 * 
	 * @param item	item existant
	 * @param tile	case du sprite existant
	 * @param duration	dur�e du nouvel �tat
	 * @param posX	nouvelle abscisse
	 * @param posY nouvelle ordonn�e
	 * @param posZ nouvelle altitude
	 * @param state	nouvel �tat
	 * @param burningDuration	nouvelle dur�e de combustion
	 * @param currentSpeed	nouvelle vitesse courante
	 * @return	le item correspondant aux nouvelles descriptions pass�es en param�tres
	 */
	private AiSimItem applyStateItem(AiItem item, AiSimTile tile, long duration,
			double posX, double posY, double posZ, 
			AiSimState state, long burningDuration, double currentSpeed)
	{	AiItemType type = item.getType();
		AiStopType stopBombs = item.hasStopBombs();
		AiStopType stopFires = item.hasStopFires();
		AiSimItem result = new AiSimItem(tile,posX,posY,posZ,state,burningDuration,currentSpeed,
			type,stopBombs,stopFires);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TOOLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * fonction permettant de d�finir le nouvel �tat d'un sprite, afin
	 * de l'utiliser dans le mod�le pour g�n�rer l'�tat suivant de la zone
	 * 
	 * @param name	nom de l'�tat
	 * @param direction	direction de l'action
	 * @return	l'�tat correspondant aux param�tres re�us
	 */
	public static AiState generateState(AiStateName name, Direction direction)
	{	long time = 0;
		AiState result = new AiSimState(name,direction,time);
		return result;
	}
}
