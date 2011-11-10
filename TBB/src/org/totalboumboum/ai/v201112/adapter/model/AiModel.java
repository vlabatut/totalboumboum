package org.totalboumboum.ai.v201112.adapter.model;

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

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiState;
import org.totalboumboum.ai.v201112.adapter.data.AiStateName;
import org.totalboumboum.ai.v201112.adapter.data.AiStopType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est chargée de simuler l'évolution d'une zone.
 * Pour cela, une modèle doit d'abord être initialisé avec une zone de départ,
 * obtenue simplement à partir des percepts de l'agent.<br/>
 * Pour prèserver la cohérence de la zone, l'utilisateur ne peut 
 * pas la modifier directement, mais seulement à travers les 
 * méthodes proposées dans cette classe. Il peut :<ul>
 * 		<li> réaliser un ou plusieurs pas de simulation et obtenir la zone résultante.</li>
 * 		<li> demander à un des joueurs de poser une bombe</li>
 * 		<li> modifier la direction de déplacement d'un joueur, ou arrêter son déplacement</li>
 * 		<li> demander à une bombe d'exploser</li></ul>
 * Au cours de la simulation, une nouvelle zone est calculée et stockée
 * en interne : l'utilisateur peut alors y accéder et l'utiliser. Si
 * de nouveaux pas de simulation sont effectués, cette zone interne est 
 * remplacée par le résultats de ces simulations.<br/>
 * L'utilisateur peut également récupérer le temps écoulé entre deux simulations.<br/>
 * Il faut souligner que les pas de simulation sont déterminés de façon évènementielle.
 * En d'autres termes, un pas se termine quand un évènement se produit. Les 
 * évènements considérés par cette classe sont :<ul>
 * 		<li> la disparition ou l'apparition d'un sprite (ex : une bombe qui a explosé, un item qui apparait)
 * 		<li> un changement d'état (ex : un mur qui commence à brûler)
 * 		<li> un changement de case (ex : un joueur se déplaçant d'une case à une autre)
 * 		<li> la fin d'un déplacement (ex : un joueur qui se retrouve bloqué par un mur)</ul>
 * Dès qu'un de ces évènements se produit, le pas de simulation se termine.
 * Le modèle donne accès à la liste des sprites qui ont été impliqués dans un des évènements
 * causant la fin du pas de simulation.<br/>
 * Vous pouvez observer une illustration du fonctionnement de ce modèle en exécutant
 * la classe AiModelTest. Notez toute fois que cette classe de test crée la zone
 * en partant de rien, alors que les agents disposent de leurs percepts.
 * Pour cette raison, elle utilise pour initialiser la zone des méthodes 
 * auxquelles les agents n'ont pas accès.
 * 
 * @author Vincent Labatut
 *
 */
public class AiModel
{	
	/**
	 * initialise le modèle avec la zone passée en paramètre.
	 * 
	 * @param currentZone
	 * 		la zone courante, qui servira de point de départ à la simulation
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
	/** Fait apparaître aléatoirement des items lors de la destruction des murs */
	private boolean simulateItemsAppearing = false;

	/**
	 * Détermine si l'apparition d'items sera simulée lors de la destruction de murs.
	 * à noter que la probabilité d'apparition dépends du nombre de murs restants
	 * et d'items cachés restant à l'instant de l'explosion.
	 * De même, le type d'item dépend de la distribution des items restants.<br/>
	 * Par défaut, cette option est désactivée.
	 * 
	 * @param simulateItemsAppearing
	 * 		Si vrai, les items apparaitront lors de la simulation.
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
	 * @return	
	 * 		l'objet AiZone issu de la simulation précédente 
	 */
	public AiZone getPreviousZone()
	{	return previous;
	}

	/**
	 * renvoie la zone issue de la dernière simulation
	 * 
	 * @return	
	 * 		l'objet AiZone issu de la dernière simulation
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
	/** durée écoulée entre les zones previous et current */ 
	private long duration;
	
	/**
	 * renvoie la durée écoulée entre les zones previous et current
	 * 
	 * @return	
	 * 		un entier long représentant une durée
	 */
	public long getDuration()
	{	return duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Effectue des simulations, en gardant pour chaque sprite l'action courante,
	 * et ce jusqu'à ce que le personnage spécifié ait changé d'état.
	 * Cette méthode est particulièrement utile quand on veut savoir quel sera
	 * l'état estimé de la zone quand le personnage que l'on contrôle passera
	 * dans la case suivante.<br/>
	 * <b>Attention:</b> le changement d'état peut aussi être dû au fait que le 
	 * personnage a commencé à brûler. A noter aussi que la simulation s'arrête
	 * quand aucun évènement ne se produit, ou quand le personnage ne fait rien
	 * alors qu'il devrait (ex : bloqué contre un mur alors qu'il est censé
	 * se déplacer).
	 * 
	 * @param hero
	 * 		Le personnage sur lequel porte la condition
	 * @return	
	 * 		Renvoie {@code true} ssi le changement d'état est dû à un déplacement (et pas à un accident).
	 */
	public boolean simulate(AiHero hero)
	{	// init
		boolean result = false;
		long totalDuration = 0;
		duration = 0;
		AiSimZone previousZone = current;
		AiSimHero simHero = current.getSpriteById(hero);

		if(simHero.getState().getName()!=AiStateName.ENDED)
		{	boolean found;
			boolean blocked;
			do
			{	// simulate
				simulate();
				
				// update total duration
				totalDuration = totalDuration + duration;
				
				// check if the hero was among the limit sprites
				simHero = current.getSpriteById(hero);
				found = simHero!=null && limitSprites.contains(simHero);
				
				// check if the hero was supposed to move, and could not
				blocked = !found && simHero.getState().getName()==AiStateName.MOVING
					&& hero.getPosX()==simHero.getPosX() && hero.getPosY()==simHero.getPosY();
			}
			// stop if the hero is in the limit, or nothing was done, or the hero couldn't move
			while(!found && duration>0 && !blocked);
			
			// if the hero was blocked: we have to come back to the previous simulation step
			if(blocked)
			{	current = previous;
				totalDuration = totalDuration - duration;
			}
			
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
	 * Effectue des simulations, en gardant pour chaque sprite l'action courante,
	 * et ce jusqu'à la prochaine explosion. Plus exactement, la simulation
	 * s'arrête lorsque le feu actuellement contenu dans la zone disparait. S'il
	 * n'y a pas encore de feu, elle s'arrête quand le prochain feu disparait.<br/>
	 * Cette méthode est particulièrement utile quand on veut savoir quel sera
	 * l'état estimé de la zone après l'explosion de la prochaine bombe.
	 * <b>Attention :</b> s'il n'y a ni bombe ni feu dans la zone courante,
	 * la simulation s'arrête tout de suite (afin de ne pas boucler à l'infini).
	 */
	public void simulateUntilFire()
	{	// init
		long totalDuration = 0;
		duration = 0;
		AiSimZone previousZone = current;

		// only if there's at least one bomb or fire in the current zone
		if(!current.getBombs().isEmpty() || !current.getFires().isEmpty())
		{	boolean found;
			do
			{	// simulate
				simulate();
				
				// update total duration
				totalDuration = totalDuration + duration;
				
				// check if the limit sprites contain some fire
				Iterator<AiSimSprite> it = limitSprites.iterator();
				found = false;
				while(!found && it.hasNext())
				{	AiSimSprite sprite = it.next();
					found = sprite instanceof AiSimFire;
				}
			}
			// stop if some fire is in the limit or nothing was done
			while(!found && duration>0);
			
			// update duration to reflect the whole process
			duration = totalDuration;
			// same thing with the previous zone
			previous = previousZone;
		}
	}
	
	/**
	 * Effectue une simulation pour la durée spécifiée.
	 * La zone obtenue est accessible par {@link #getCurrentZone}.
	 * Si la simulation atteint un état stable (i.e. aucun
	 * sprite n'est plus actif) avant la fin du temps demandé,
	 * alors elle s'arrête mais le champ {@code duration}
	 * contient la durée effectivement simulée (par opposition
	 * avec la durée spécifiée via le paramètre {@code requestedDuration}.
	 * 
	 * @param requestedDuration
	 * 		La durée de la simulation à effectuer
	 */
	public void simulate(long requestedDuration)
	{	// init
		long totalDuration = 0;
		previous = current;
		duration = 0;
		AiSimZone previousZone = current;
		double lastDuration = 1;
		
		while(totalDuration<requestedDuration && lastDuration>0)
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
			lastDuration = duration;
		}
		
		// update duration to reflect the whole process
		duration = totalDuration;
		// same thing with the previous zone
		previous = previousZone;
	}
	
	/**
	 * Calcule l'état suivant de la zone. Les sprites
	 * continuent à faire ce qu'il faisaient déjà (brûler, se déplacer, etc.).<br/> 
	 * La méthode renvoie l'état obtenu à la fin du prochain évènement 
	 * (i.e. celui qui se termine le plus vite). Les évènement considérés sont :
	 * <ul>
	 * 		<li>la disparition d'un sprite (ex : une bombe qui a explosé)</li>
	 * 		<li>l'apparition d'un sprite (ex : un item qui apparait à la suite de l'explosion d'un mur)</li>
	 * 		<li>un changement d'état (ex : un mur qui commence à brûler)</li>
	 * 		<li>un changement de case (ex : un joueur passant d'une case à une autre)</li>
	 * 		<li>la fin d'un déplacement (ex : un joueur qui se retrouve bloqué par un mur)</li>
	 * </ul>
	 * <b>Attention :</b> si l'option {@code considerExplosionOnly} est activée,
	 * alors seules les explosions de bombes sont considérées. Plus précisément,
	 * on considère que seule la disparition du feu est un évènement (et non pas
	 * son apparition).<br/>
	 * Les modifications sont appliquées à la zone en interne. L'utilisateur peut récupérer
	 * la nouvelle zone mise à jour avec {@link #getCurrentZone}. Il peut également récupérer la liste
	 * de sprites qui ont provoqué la fin de la mise à jour à la suite d'une action, avec {@link #getLimitSprites}.
	 * Il peut aussi récupérer la durée qui s'est écoulée (en temps simulé) depuis la dernière simulation, 
	 * avec {@link #getDuration}.
	 */
	public void simulate()
	{	duration = 0;
		// create a copy of the current zone
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
	 * Calcule le temps minimal avant le prochain changement d'état de chaque sprite.
	 * Ce laps de temps sera ensuite appliqué uniformêment à chaque sprite.
	 * 
	 * @param sprites
	 * 		liste des sprites à traiter
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
				// a zero change time means there's nothing to do 
				// (e.g.: moving towards an obstacle) and should therefore be ignored
				if(changeTime>0)
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
	 * calcule combien de temps il va falloir au sprite spécifié pour sortir
	 * de son état courant. si le sprite brûle, il s'agit de savoir pendant
	 * combien de temps encore. s'il se déplace, il s'agit de savoir combien de
	 * temps il va lui falloir pour changer de case. s'il ne fait rien, il n'y a
	 * pas de limite particuliére à son activité et la méthode renvoie {@code Long.MAX_VALUE}.
	 * 
	 * @param current	
	 * 		la zone courante
	 * @param sprite	
	 * 		le sprite à traiter
	 * @return	
	 * 		la durée pendant laquelle le sprite va rester à cet état
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
					result = (long)Math.ceil(temp);	// we take the ceiling value so that there is actually enough time to reach the location
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
					result = normalDuration - bomb.getTime();
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
	 * Calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param sprites	
	 * 		La liste des sprites à traiter.
	 * @param duration	
	 * 		Durée du pas de simulation
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
	 * déplace le sprite passée en paramètre en fonction de sa direction courante,
	 * et pour la durée de simulation spécifiée.
	 * 
	 * @param sprite
	 * 		le sprite à déplacer
	 * @param duration
	 * 		la durée du pas de simulation
	 */
	private void moveSprite(AiSimSprite sprite, long duration)
	{	// NOTE same simplification than before: we suppose the levels are all grids, 
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
//				allowed++;
			}
		}
		double goalX = current.normalizePositionX(tileX0+dir[0]*offset);
		double goalY = current.normalizePositionY(tileY0+dir[1]*offset);
		double xSign = Math.signum(goalX - posX);
		double ySign = Math.signum(goalY - posY);
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
				posY = posY+ySign*temp;
				allowed = allowed - temp;
				posX = posX+xSign*allowed;
			}
			else
			{	double temp = Math.min(allowed,dx);
				posX = posX+xSign*temp;
				allowed = allowed - temp;
				posY = posY+ySign*allowed;
			}
		}
		
		// update tile
		AiSimTile newTile = current.getTile(posX,posY);
		if(!newTile.equals(tile))
		{	tile.removeSprite(sprite);
			newTile.addSprite(sprite);
			sprite.setTile(newTile);
			// by security
			// (due to approximation, it's possible the sprite was not put in this list before)
			if(!limitSprites.contains(sprite))
				limitSprites.add(sprite);
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param block
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration	
	 * 		la durée à prendre en compte
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
	 * permet à un bloc d'éventuellement libérer un item
	 * à la fin de sa destruction.
	 * 
	 * @param block
	 * 		le bloc à traiter
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param bomb
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration
	 * 		la durée à prendre en compte
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
	 * 		la bombe à faire exploser
	 * @return
	 * 		vrai si le modèle a pu faire exploser la bombe
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
	 * 		la bombe à faire exploser
	 * @return
	 * 		vrai si le modèle a pu faire exploser la bombe
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param fire
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration
	 * 		la durée à prendre en compte
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
	 * brûle une case et son contenu, à la suite d'une explosion
	 * 
	 * @param tile
	 * 		la case à brûler
	 * @param detonatingBomb
	 * 		la bombe qui a propovoqué l'explosion
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param floor
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration
	 * 		la durée à prendre en compte
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param hero
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration
	 * 		la durée à prendre en compte
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
	 * dans la case spécifiée. La méthode renvoie la bombe posée,
	 * ou bien null en cas d'impossibilité.
	 * <b>Note :</b> cette méthode permet de poser des bombes
	 * à distance, ce qui peut s'avérer pratique dans certaines situations.
	 * 
	 * @param hero
	 * 		le personnage que l'on veut voir poser une bombe
	 * @param tile
	 * 		la case qui devra contenir la bombe.
	 * @param force
	 * 		si ce paramètre est {@code true}, le modèle pose une bombe même si l'agent concerné n'en a plus à poser.
	 * @return
	 * 		la bombe qui a été posée, ou null si c'était impossible.
	 */
	public AiBomb applyDropBomb(AiHero hero, AiTile tile, boolean force)
	{	AiBomb result = null;
	
		// get the tile
		int row = tile.getRow();
		int col = tile.getCol();
		AiSimTile simTile = current.getTile(row,col);
		
		// get the hero
		AiSimHero simHero = current.getSpriteById(hero);
		if(simHero!=null)
		{	// drop the bomb
			result = dropBomb(simTile,simHero,force);
		}
		
		return result;
	}

	/**
	 * crée une nouvelle bombe appartenant au personnage passé en paramètre.
	 * La bombe est placée au centre de la case passée en paramètre.
	 * Le compteur de bombes du personnage est incrémenté.
	 * Si jamais la case contient déjà un objet empêchant de poser la bombe,
	 * celle-ci n'est pas créée et la fonction renvoie la valeur {@code null}.
	 * Sinon (si la création est possible) alors la fonction renvoie la bombe créée.
	 * Si jamais le personnage ne peut pas poser de bombes pour l'instant,
	 * la bombe n'est pas créée et la valeur {@code null} est renvoyée. 
	 * 
	 * @param tile
	 * 		case qui contiendra la bombe nouvellement créée
	 * @param hero
	 * 		personnage à qui la bombe appartiendra (ce qui détermine ses propriétés)
	 * @param force
	 * 		si ce paramètre est {@code true}, le modèle pose une bombe même si l'agent concerné n'en a plus à poser.
	 * @return
	 * 		la bombe si elle a pu être créée, ou null si ce n'est pas possible 
	 */
	private AiSimBomb dropBomb(AiSimTile tile, AiSimHero hero, boolean force)
	{	AiSimBomb result = null;
		
		// check if the hero can drop a bomb
		int dropped = hero.getBombNumberCurrent();
		int max = hero.getBombNumberMax();
		if(dropped<max || force)
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
	 * dans la case spécifiée. La méthode renvoie la bombe posée,
	 * ou bien {@code null} en cas d'impossibilité. La bombe est posée
	 * dans la case actuellement occupée par le personnage.
	 * 
	 * @param hero
	 * 		le personnage que l'on veut voir poser une bombe
	 * @param force
	 * 		si ce paramètre est {@code true}, le modèle pose une bombe même si l'agent concerné n'en a plus à poser.
	 * @return
	 * 		la bombe qui a été posée, ou null si c'était impossible.
	 */
	public AiBomb applyDropBomb(AiHero hero, boolean force)
	{	AiBomb result = null;
		// get hero
		AiSimHero simHero = current.getSpriteById(hero);
		if(simHero!=null)
		{	// drop bomb
			AiTile tile = simHero.getTile();
			result = applyDropBomb(hero,tile,force);
		}
		return result;
	}
	
	/**
	 * Permet de solliciter un changement de direction de la part du personnage.
	 * si la direction n'est pas {@link Direction#NONE NONE}, alors l'état du personnage devient {@link AiStateName#MOVING MOVING}.
	 * sinon, il devient {@link AiStateName#STANDING STANDING}. Bien sûr, s'il y a des obstacles, le personnage
	 * ne pourra pas effectivement se déplacer dans la direction spécifiée.
	 * 
	 * @param hero
	 * 		le personnage à déplacer
	 * @param direction
	 * 		la direction du déplacement
	 * @return
	 * 		Renvoie {@code true} si le changement de direction a pu être effectué, {@code false} sinon 
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
			if((direction.isPrimary() || direction==Direction.NONE) && 
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
	 * implémente le comportement d'un personnage ramassant
	 * un item qui se trouve dans la case dans laquelle il
	 * vient d'entrer.
	 *  
	 * @param hero
	 * 		le personnage entrant dans la case
	 * @param item
	 * 		l'item à ramasser
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
	 * implémente le comportement d'un personnage relâchant tous 
	 * ses items quand il est éliminé.
	 * 
	 * @param hero
	 * 		le personnage relâchant tous ses items
	 */
	private void releaseItems(AiSimHero hero)
	{
		// NOTE items could be released here... (to be completed)
	}
	
	/**
	 * supprime tous les personnage de la zone.
	 * ceci permet de la simplifier, et ainsi d'utiliser la zone
	 * seulement pour prévoir comment les explosions vont évoluer,
	 * sans se soucier des déplacements des personnages.
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
	 * calcule l'état du sprite à la fin de la durée spécifiée,
	 * à partir de l'état courant.
	 * 
	 * @param item
	 * 		le sprite concerné (sa représentation initiale)
	 * @param duration
	 * 		la durée à prendre en compte
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
