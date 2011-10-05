package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
 * Cette classe est charg�e de simuler l'�volution d'une zone.
 * Pour cela, une mod�le doit d'abord �tre initialis� avec une zone de d�part,
 * obtenue simplement � partir des percepts de l'agent.<br/>
 * Pour pr�server la coh�rence de la zone, l'utilisateur ne peut 
 * pas la modifier directement, mais seulement � travers les 
 * m�thodes propos�es dans cette classe. Il peut :<ul>
 * 		<li> r�aliser un ou plusieurs pas de simulation et obtenir la zone r�sultante.</li>
 * 		<li> demander � un des joueurs de poser une bombe</li>
 * 		<li> modifier la direction de d�placement d'un joueur, ou arr�ter son d�placement</li>
 * 		<li> demander � une bombe d'exploser</li></ul>
 * Au cours de la simulation, une nouvelle zone est calcul�e et stock�e
 * en interne : l'utilisateur peut alors y acc�der et l'utiliser. Si
 * de nouveaux pas de simulation sont effectu�s, cette zone interne est 
 * remplac�e par le r�sultats de ces simulations.<br/>
 * L'utilisateur peut �galement r�cup�rer le temps �coul� entre deux simulations.<br/>
 * Il faut souligner que les pas de simulation sont d�termin�s de fa�on �v�nementielle.
 * En d'autres termes, un pas se termine quand un �v�nement se produit. Les 
 * �v�nements consid�r�s par cette classe sont :<ul>
 * 		<li> la disparition ou l'apparition d'un sprite (ex : une bombe qui a explos�, un item qui apparait)
 * 		<li> un changement d'�tat (ex : un mur qui commence � br�ler)
 * 		<li> un changement de case (ex : un joueur se d�pla�ant d'une case � une autre)
 * 		<li> la fin d'un d�placement (ex : un joueur qui se retrouve bloqu� par un mur)</ul>
 * D�s qu'un de ces �v�nements se produit, le pas de simulation se termine.
 * Le mod�le donne acc�s � la liste des sprites qui ont �t� impliqu�s dans un des �v�nements
 * causant la fin du pas de simulation.<br/>
 * Vous pouvez observer une illustration du fonctionnement de ce mod�le en ex�cutant
 * la classe AiModelTest. Notez toute fois que cette classe de test cr�e la zone
 * en partant de rien, alors que les agents disposent de leurs percepts.
 * Pour cette raison, elle utilise pour initialiser la zone des m�thodes 
 * auxquelles les agents n'ont pas acc�s.
 * 
 * @author Vincent Labatut
 *
 */
public class AiModel
{	
	/**
	 * initialise le mod�le avec la zone pass�e en param�tre.
	 * 
	 * @param currentZone
	 * 		la zone courante, qui servira de point de d�part � la simulation
	 */
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
	 * @param simulateItemsAppearing
	 * 		si vrai, les items apparaitront lors de la simulation
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
	 * @return	
	 * 		l'objet AiZone issu de la simulation pr�c�dente 
	 */
	public AiZone getPreviousZone()
	{	return previous;
	}

	/**
	 * renvoie la zone issue de la derni�re simulation
	 * 
	 * @return	
	 * 		l'objet AiZone issu de la derni�re simulation
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
	 * @return	
	 * 		une liste de sprites
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
	 * @return	
	 * 		un entier long repr�sentant une dur�e
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
	 * @param hero
	 * 		le personnage sur lequel porte la condition
	 * @return	
	 * 		vrai si le changement d'�tat est d� � un d�placement (et pas � un accident)
	 */
	public boolean simulate(AiHero hero)
	{	// init
		boolean result = false;
		AiSimHero simHero;
		long totalDuration = 0;
		AiSimZone previousZone = current;
		simHero = current.getSpriteById(hero);

		if(simHero.getState().getName()!=AiStateName.ENDED)
		{	boolean found = false;
			do
			{	// simulate
				simulate();
				
				// update total duration
				totalDuration = totalDuration + duration;
				
				// check if the hero was among the limit sprites
				simHero = current.getSpriteById(hero);
				found = limitSprites.contains(simHero);
			}
			while(!found);
			
			// check if the hero is still safe
			AiState state = simHero.getState();
			AiStateName name = state.getName();
			result = name==AiStateName.FLYING || name==AiStateName.MOVING || name==AiStateName.STANDING;
			
			// update duration to reflect the whole process
			duration = totalDuration;
			// same thing with the previous zone
			previous = previousZone;
		}
			
		return result;
	}
	
	/**
	 * effectue une simulation pour la dur�e sp�cifi�e.
	 * la zone obtenue est accessible par getCurrentZone.
	 * 
	 * @param requestedDuration
	 * 		la dur�e de la simulation � effectuer
	 */
	public void simulate(long requestedDuration)
	{	// init
		long totalDuration = 0;
		previous = current;
		
		while(totalDuration<requestedDuration)
		{	// create a copy of the current zone
			current = new AiSimZone(current);
			
			// retrieve all sprites remaining in the zone
			List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
			sprites.addAll(current.getInternalBlocks());
			sprites.addAll(current.getInternalBombs());
			sprites.addAll(current.getInternalFires());
			sprites.addAll(current.getInternalFloors());
			sprites.addAll(current.getInternalHeroes());
			sprites.addAll(current.getInternalItems());
		
			// set iteration duration
			processDuration(sprites);
			duration = Math.min(duration,requestedDuration-totalDuration);
			
			// apply events for the resulting minimal time
			toBeDetonated.clear();
			updateSprites(sprites,duration);
		
			// update detonating bombs
			for(AiSimBomb bomb: toBeDetonated)
				detonateBomb(bomb);
		
			// update the resulting zone
			current.updateTime(duration);
			totalDuration = totalDuration + duration;
		}
	}
	
	/**
	 * calcule l'�tat suivant de la zone si les �tats sp�cifi�s en param�tres
	 * sont appliqu�s � la zone courante. en l'absence d'�tat sp�cifi�, le sprite
	 * continue � faire ce qu'il faisait d�j� (br�ler, se d�placer, etc.).
	 * par cons�quent, la map contenant les �tats sp�cifi�s peut �tre vide. 
	 * La m�thode renvoie l'�tat obtenu � la fin du prochain �v�nement 
	 * (i.e. celui qui se termine le plus vite). Les �v�nement consid�r�s sont :
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
	public void simulate()
	{	// create a copy of the current zone
		previous = current;
		current = new AiSimZone(previous);
		
		// retrieve all sprites remaining in the zone
		List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
		sprites.addAll(current.getInternalBlocks());
		sprites.addAll(current.getInternalBombs());
		sprites.addAll(current.getInternalFires());
		sprites.addAll(current.getInternalFloors());
		sprites.addAll(current.getInternalHeroes());
		sprites.addAll(current.getInternalItems());
		
		// process iteration duration
		processDuration(sprites);
		
		// apply events for the resulting minimal time
		toBeDetonated.clear();
		updateSprites(sprites,duration);
		
		// update detonating bombs
		for(AiSimBomb bomb: toBeDetonated)
			detonateBomb(bomb);
		
		// update the resulting zone
		current.updateTime(duration);
	}
	
	/**
	 * Calcule le temps minimal avant le prochain changement d'�tat de chaque sprite.
	 * Ce laps de temps sera ensuite appliqu� uniform�ment � chaque sprite.
	 * 
	 * @param sprites
	 * 		liste des sprites � traiter
	 */
	private void processDuration(List<AiSimSprite> sprites)
	{	// init
		duration = Long.MAX_VALUE;
		limitSprites = new ArrayList<AiSimSprite>();
		
		for(AiSimSprite sprite: sprites)
		{	
if(sprite instanceof AiSimHero)
	System.out.print("");
if(sprite instanceof AiSimBomb)
	System.out.print("");
			// process the sprite next state
			AiSimState state = sprite.getState();
			// process the time remaining before the next change (be it of state, tile, etc.)
			if(state.getName()!=AiStateName.ENDED)
			{	long changeTime = processChangeTime(current,sprite);
				if(changeTime>0) //zero means there's nothing to do (e.g.: moving towards an obstacle) and should therefore be ignored
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
		
		// if no change at all: return a zero duration
		if(duration==Long.MAX_VALUE)
			duration = 0;
	}
	
	/**
	 * calcule combien de temps il va falloir au sprite sp�cifi� pour sortir
	 * de son �tat courant. si le sprite br�le, il s'agit de savoir pendant
	 * combien de temps encore. s'il se d�place, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particuli�re � son activit� et la m�thode renvoie Long.MAX_VALUE.
	 * 
	 * @param current	
	 * 		la zone courante
	 * @param sprite	
	 * 		le sprite � traiter
	 * @return	
	 * 		la dur�e pendant laquelle le sprite va rester � cet �tat
	 */
	private long processChangeTime(AiSimZone current, AiSimSprite sprite)
	{	long result = Long.MAX_VALUE;
		AiSimState state = sprite.getState();
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
				{	offset = tileSize/2;
					//because tile centers are not actually tile centers
					if(direction==Direction.LEFT || direction==Direction.UP)
						offset++; 
				}
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
	 * @param duration	
	 * 		dur�e du pas de simulation
	 */
	private void updateSprites(List<AiSimSprite> sprites, long duration)
	{	
		for(AiSimSprite sprite: sprites)
		{	// init
			AiSimState state = sprite.getState();
			AiStateName name = state.getName();
			
			// this shouldn't happen
			if(name==AiStateName.ENDED)
			{	// remove from zone
				current.removeSprite(sprite);
			}
			
			// block
			if(sprite instanceof AiSimBlock)
			{	AiSimBlock block = (AiSimBlock)sprite;
				updateBlock(block,duration);
			}
			
			// bomb
			else if(sprite instanceof AiSimBomb)
			{	AiSimBomb bomb = (AiSimBomb)sprite;
				updateBomb(bomb,duration);
			}
			
			// fire
			else if(sprite instanceof AiSimFire)
			{	AiSimFire fire = (AiSimFire)sprite;
				updateFire(fire,duration);
			}
			
			// floor
			else if(sprite instanceof AiSimFloor)
			{	AiSimFloor floor = (AiSimFloor)sprite;
				updateFloor(floor,duration);
			}
			
			// hero
			else if(sprite instanceof AiSimHero)
			{	AiSimHero hero = (AiSimHero)sprite;
				updateHero(hero,duration);
			}
			
			// item
			else if(sprite instanceof AiSimItem)
			{	AiSimItem item = (AiSimItem)sprite;
				updateItem(item,duration);
			}
		}
	}
	
	/**
	 * d�place le sprite pass�e en param�tre en fonction de sa direction courante,
	 * et pour la dur�e de simulation sp�cifi�e.
	 * 
	 * @param sprite
	 * 		le sprite � d�placer
	 * @param duration
	 * 		la dur�e du pas de simulation
	 */
	private void moveSprite(AiSimSprite sprite, long duration)
	{	//NOTE same simplification than before: we suppose the levels are all grids, 
		// meaning at least one coordinate is at the center of a tile
		// plus the sprite moves in a primary direction (DOWN, LEFT, RIGHT, UP: no composite)
		
		// init misc
		AiSimState state = sprite.getState();
		Direction direction = state.getDirection();
		
		// init location
		AiSimTile tile = sprite.getTile();
		double posX = sprite.getPosX();
		double posY = sprite.getPosY();
		double posZ = sprite.getPosZ();
		
		// init speed
		double currentSpeed = sprite.getCurrentSpeed();
		if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			currentSpeed = bomb.getSlidingSpeed();
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			currentSpeed = hero.getWalkingSpeed();
		}
		
		// compute move
		double allowed = currentSpeed*duration/1000;
		int dir[] = direction.getIntFromDirection();
		double tileSize = tile.getSize();
		double tileX0 = tile.getPosX();
		double tileY0 = tile.getPosY();
		AiSimTile neighborTile = tile.getNeighbor(direction);
		double offset = 0;
		if(neighborTile.isCrossableBy(sprite)) //deal with obstacles
		{	offset = tileSize/2;
			//because tile centers are not actually tile centers
			if(direction==Direction.LEFT || direction==Direction.UP)
			{	offset++; 
				allowed++;
			}
		}
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
		{	tile.removeSprite(sprite);
			newTile.addSprite(sprite);
			sprite.setTile(newTile);
		}
		
		// a hero might pick an item in the new tile
		if(sprite instanceof AiSimHero && !newTile.equals(tile))
		{	AiSimHero hero = (AiSimHero)sprite;
			List<AiSimItem> items = new ArrayList<AiSimItem>(newTile.getInternalItems()); 
			for(AiSimItem item: items)
			{	AiStateName itemStateName = item.getState().getName();
				if(itemStateName==AiStateName.STANDING)
					pickItem(hero,item);
			}
		}

		// update location
		sprite.setPos(posX,posY,posZ);
		
		// update speed
		sprite.setCurrentSpeed(currentSpeed);
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
	 * @param duration	
	 * 		la dur�e � prendre en compte
	 */
	private void updateBlock(AiSimBlock block, long duration)
	{	// init
		AiSimState state = block.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;

		// block is burning
		if(name==AiStateName.BURNING)
		{	long burningDuration = block.getBurningDuration();
			if(time>=burningDuration) //NOTE problem for re-spawning sprites (but it's only an approximation, after all...)
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// possibly release items
				releaseItem(block);
				// remove from zone
				current.removeSprite(block);
			}	
		}
		
		// blocks can't move (at least for now)
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// NOTE useless for the moment
			//moveSprite(block);
		}
		
		// block just stands
		else if(name==AiStateName.STANDING)
		{	// nothing special to do
		}
		
		// always update the state
		newState = new AiSimState(name,direction,time);
		block.setState(newState);
	}
	
	/**
	 * permet � un bloc d'�ventuellement lib�rer un item
	 * � la fin de sa destruction.
	 * 
	 * @param block
	 * 		le bloc � traiter
	 */
	private void releaseItem(AiSimBlock block)
	{	if(simulateItemsAppearing && current.getHiddenItemsCount()>0)
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
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<AiSimBomb> toBeDetonated = new ArrayList<AiSimBomb>();
	
	/**
	 * calcule l'�tat du sprite � la fin de la dur�e sp�cifi�e,
	 * � partir de l'�tat courant.
	 * 
	 * @param bomb
	 * 		le sprite concern� (sa repr�sentation initiale)
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateBomb(AiSimBomb bomb, long duration)
	{	// init
		AiSimState state = bomb.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;
		
		// bomb is burning
		if(name==AiStateName.BURNING)
		{	long burningDuration = bomb.getBurningDuration();
			if(time>=burningDuration)
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// possibly update owner
				AiSimHero owner = bomb.getOwner();
				if(owner!=null)
					owner.updateBombNumberCurrent(-1);
				// remove from zone
				current.removeSprite(bomb);
			}
			// update time
			long elapsedTime = bomb.getTime() + duration;
			bomb.setTime(elapsedTime);
		}
		
		// bomb is moving: process changes in location/tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// update location
			moveSprite(bomb,duration);
			// check for detonation
			if(name==AiStateName.MOVING)
			{	long normalDuration = bomb.getNormalDuration();
				if(bomb.hasCountdownTrigger() && time>=normalDuration)
				{	// update state
//					name = AiStateName.BURNING;
//					direction = Direction.NONE;
//					time = 0;
					// update speed
//					bomb.setCurrentSpeed(0);
					// add to detonate list
					//detonateBomb(bomb);
					toBeDetonated.add(bomb);
				}
				// update time
				long elapsedTime = bomb.getTime() + duration;
				bomb.setTime(elapsedTime);
			}
			else
			{	// update time
				bomb.setTime(0);
			}
		}
		
		// bomb just stands
		if(name==AiStateName.STANDING)
		{	// check for detonation
			long normalDuration = bomb.getNormalDuration();
			if(bomb.hasCountdownTrigger() && time>=normalDuration)
			{	// update state
//				name = AiStateName.BURNING;
//				direction = Direction.NONE;
//				time = 0;
				// add to detonate list
				//detonateBomb(bomb);
				toBeDetonated.add(bomb);
			}
			// update time
			long elapsedTime = bomb.getTime() + duration;
			bomb.setTime(elapsedTime);
		}

		// always update the state
		newState = new AiSimState(name,direction,time);
		bomb.setState(newState);
	}

	/**
	 * permet de solliciter l'explosion d'une bombe.
	 * <b>Note :</b> la bombe n'explosera que si c'est possible,
	 * auquel cas la fonction renvoie la valeur vrai (et sinon
	 * elle renvoie la valeur faux).
	 * 
	 * @param bomb
	 * 		la bombe � faire exploser
	 * @return
	 * 		vrai si le mod�le a pu faire exploser la bombe
	 */
	public boolean applyDetonateBomb(AiBomb bomb)
	{	boolean result = false;
		
		// get bomb
		AiSimBomb simBomb = current.getSpriteById(bomb);
		if(simBomb!=null)
		{	// detonate the bomb
			result = detonateBomb(simBomb);
		}
		
		return result;
	}
	
	/**
	 * permet de solliciter l'explosion d'une bombe.
	 * <b>Note :</b> la bombe n'explosera que si c'est possible,
	 * auquel cas la fonction renvoie la valeur vrai (et sinon
	 * elle renvoie la valeur faux).
	 * 
	 * @param bomb
	 * 		la bombe � faire exploser
	 * @return
	 * 		vrai si le mod�le a pu faire exploser la bombe
	 */
	private boolean detonateBomb(AiSimBomb bomb)
	{	// init
		AiSimState state = bomb.getState();
		AiStateName name = state.getName();
		boolean result = false;
		
		// check if the bomb can actually detonate
		if(name==AiStateName.STANDING || name==AiStateName.MOVING)
		{	result = true;
			// update state
			name = AiStateName.BURNING;
			Direction direction = Direction.NONE;
			long time = 0;
			AiSimState newState = new AiSimState(name,direction,time);
			bomb.setState(newState);

			// update speed (possibly)
			bomb.setCurrentSpeed(0);
			
			// process each tile in the blast
			List<AiTile> blast = bomb.getBlast();
			for(AiTile tile: blast)
			{	AiSimTile simTile = (AiSimTile) tile;
				burnTile(simTile,bomb);
			}
		}
		
		return result;
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
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateFire(AiSimFire fire, long duration)
	{	// init
		AiSimState state = fire.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;

		// fire is burning (only natural, I guess)
		if(name==AiStateName.BURNING)
		{	long burningDuration = fire.getBurningDuration();
			if(time>=burningDuration)
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// remove from zone
				current.removeSprite(fire);
			}	
		}

		// fire can't move
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// fire can't just stand: it burns
		else if(name==AiStateName.STANDING)
		{	// useless here
		}
		
		// always update the state
		newState = new AiSimState(name,direction,time);
		fire.setState(newState);
		// and elapsed time
		long elapsedTime = fire.getTime() + duration;
		fire.setTime(elapsedTime);
	}

	/**
	 * br�le une case et son contenu, � la suite d'une explosion
	 * 
	 * @param tile
	 * 		la case � br�ler
	 * @param detonatingBomb
	 * 		la bombe qui a propovoqu� l'explosion
	 */
	private void burnTile(AiSimTile tile, AiSimBomb detonatingBomb)
	{	AiFire firePrototype = detonatingBomb.getFirePrototype();
		
		// if the fire can appear, we affect it to the tile
		if(tile.isCrossableBy(firePrototype) || tile.equals(detonatingBomb.getTile()))
		{	// create sprite
			AiSimFire fire = new AiSimFire(firePrototype,tile);
			current.addSprite(fire);
			// set properties
			fire.setId(createNewId());
			AiSimState state = new AiSimState(AiStateName.BURNING,Direction.NONE,0);
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
		{	if(!bomb.equals(detonatingBomb))
			{	AiStateName name = bomb.getState().getName();
				if(bomb.hasExplosionTrigger() && (name==AiStateName.STANDING || name==AiStateName.MOVING))
					detonateBomb(bomb);
			}
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
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateFloor(AiSimFloor floor, long duration)
	{	// init
		AiSimState state = floor.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;

		// floor is burning
		if(name==AiStateName.BURNING)
		{	long burningDuration = floor.getBurningDuration();
			if(time>=burningDuration) //NOTE problem for re-spawning blocks (but it's only an approximation, after all...)
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// remove from zone
				current.removeSprite(floor);
			}	
		}
		
		// floors can't move (at least for now)
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// floor just stands
		else if(name==AiStateName.STANDING)
		{	// nothing to do
		}
		
		// always update the state
		newState = new AiSimState(name,direction,time);
		floor.setState(newState);
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
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateHero(AiSimHero hero, long duration)
	{	// init
		AiSimState state = hero.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;

		// hero is burning
		if(name==AiStateName.BURNING)
		{	long burningDuration = hero.getBurningDuration();
			if(time>=burningDuration) //NOTE problem for games in which heroes can be reborn
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// possibly release items
				releaseItems(hero);
				// remove from zone
				current.removeSprite(hero);
			}	
		}
		
		// hero is moving : process changes in location/tile
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// update location
			moveSprite(hero,duration);
		}
		
		// hero just stands
		else if(name==AiStateName.STANDING)
		{	// nothing to do
		}
		
		// always update the state
		newState = new AiSimState(name,direction,time);
		hero.setState(newState);
	}

	/**
	 * permet de solliciter un personnage pour qu'il pose une bombe
	 * dans la case sp�cifi�e. La m�thode renvoie la bombe pos�e,
	 * ou bien null en cas d'impossibilit�.
	 * <b>Note :</b> cette m�thode permet de poser des bombes
	 * � distance, ce qui peut s'av�rer pratique dans certaines situations.
	 * 
	 * @param hero
	 * 		le personnage que l'on veut voir poser une bombe
	 * @param tile
	 * 		la case qui devra contenir la bombe.
	 * @return
	 * 		la bombe qui a �t� pos�e, ou null si c'�tait impossible.
	 */
	public AiBomb applyDropBomb(AiHero hero, AiTile tile)
	{	AiBomb result = null;
	
		// get the tile
		int line = tile.getLine();
		int col = tile.getCol();
		AiSimTile simTile = current.getTile(line,col);
		
		// get the hero
		AiSimHero simHero = current.getSpriteById(hero);
		if(simHero!=null)
		{	// drop the bomb
			result = dropBomb(simTile,simHero);
		}
		
		return result;
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
	private AiSimBomb dropBomb(AiSimTile tile, AiSimHero hero)
	{	AiSimBomb result = null;
		
		// check if the hero can drop a bomb
		int dropped = hero.getBombNumberCurrent();
		int max = hero.getBombNumberMax();
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
				hero.updateBombNumberCurrent(+1);
				
				// check for fire
				if(tile.getFires().size()>0 && result.hasExplosionTrigger())
					detonateBomb(result);
			}
		}
		
		return result;
	}

	/**
	 * permet de solliciter un personnage pour qu'il pose une bombe
	 * dans la case sp�cifi�e. La m�thode renvoie la bombe pos�e,
	 * ou bien null en cas d'impossibilit�. La bombe est pos�e
	 * dans la case actuellement occup�e par le personnage.
	 * 
	 * @param hero
	 * 		le personnage que l'on veut voir poser une bombe
	 * @return
	 * 		la bombe qui a �t� pos�e, ou null si c'�tait impossible.
	 */
	public AiBomb applyDropBomb(AiHero hero)
	{	AiBomb result = null;
		// get hero
		AiSimHero simHero = current.getSpriteById(hero);
		if(simHero!=null)
		{	// drop bomb
			AiTile tile = simHero.getTile();
			result = applyDropBomb(hero,tile);
		}
		return result;
	}
	
	/**
	 * permet de solliciter un changement de direction de la part du personnage.
	 * si la direction n'est pas NONE, alors l'�tat du personnage devient MOVING.
	 * sinon, il devient STANDING. Bien s�r, s'il y a des obstacles, le personnage
	 * ne pourra pas effectivement se d�placer dans la direction sp�cifi�e.
	 * 
	 * @param hero
	 * 		le personnage � d�placer
	 * @param direction
	 * 		la direction du d�placement
	 * @return
	 * 		vrai si le changement de direction a pu �tre effectu�, faux sinon 
	 */
	public boolean applyChangeHeroDirection(AiHero hero, Direction direction)
	{	boolean result = false;
	
		// get hero
		AiSimHero simHero = current.getSpriteById(hero);
		if(simHero!=null)
		{	// check state
			AiSimState state = simHero.getState();
			long time = state.getTime();
			AiStateName name = state.getName();
			if(direction.isPrimary() && 
				(name==AiStateName.MOVING || name==AiStateName.STANDING))
			{	result = true;
				if(direction==Direction.NONE)
					name = AiStateName.STANDING;
				else
					name = AiStateName.MOVING;
				AiSimState newState = new AiSimState(name, direction, time);
				simHero.setState(newState);
			}
		}
		return result;
	}
	
	/**
	 * impl�mente le comportement d'un personnage ramassant
	 * un item qui se trouve dans la case dans laquelle il
	 * vient d'entrer.
	 *  
	 * @param hero
	 * 		le personnage entrant dans la case
	 * @param item
	 * 		l'item � ramasser
	 */
	private void pickItem(AiSimHero hero, AiSimItem item)
	{	AiItemType type = item.getType();
		if(type==AiItemType.EXTRA_BOMB)
		{	hero.updateBombNumberMax(+1);
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
	
	/**
	 * impl�mente le comportement d'un personnage rel�chant tous 
	 * ses items quand il est �limin�.
	 * 
	 * @param hero
	 * 		le personnage rel�chant tous ses items
	 */
	private void releaseItems(AiSimHero hero)
	{
		// NOTE items could be released here... (to be completed)
	}
	
	/**
	 * supprime tous les personnage de la zone.
	 * ceci permet de la simplifier, et ainsi d'utiliser la zone
	 * seulement pour pr�voir comment les explosions vont �voluer,
	 * sans se soucier des d�placements des personnages.
	 */
	public void applyRemoveHeroes()
	{	List<AiSimHero> heroes = current.getInternalHeroes();
		for(AiSimHero hero: heroes)
			current.removeSprite(hero);
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
	 * @param duration
	 * 		la dur�e � prendre en compte
	 */
	private void updateItem(AiSimItem item, long duration)
	{	// init
		AiSimState state = item.getState(),newState;
		AiStateName name = state.getName();
		Direction direction = state.getDirection();
		long time = state.getTime() + duration;

		// item is burning (too bad!)
		if(name==AiStateName.BURNING)
		{	long burningDuration = item.getBurningDuration();
			if(time>=burningDuration) //NOTE problem for re-spawning sprites (but it's only an approximation, after all...)
			{	// update state
				name = AiStateName.ENDED;
				direction = Direction.NONE;
				time = 0;
				// remove from zone
				current.removeSprite(item);
			}	
		}
		
		// items can't move (at least for now)
		else if(name==AiStateName.FLYING || name==AiStateName.MOVING)
		{	// useless here
		}
		
		// item just stands
		else if(name==AiStateName.STANDING)
		{	// nothing to do
		}
		
		// always update the state
		newState = new AiSimState(name,direction,time);
		item.setState(newState);
	}
}