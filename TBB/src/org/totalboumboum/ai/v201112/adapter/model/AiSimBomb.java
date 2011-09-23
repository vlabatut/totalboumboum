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
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiStopType;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule une bombe du jeu, i.e. un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimBomb extends AiSimSprite implements AiBomb
{	
	/**
	 * crée une simulation de la bombe passée en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		numéro d'identification de la bombe
	 * @param tile
	 * 		case contenant la bombe
	 * @param posX
	 * 		abscisse de la bombe
	 * @param posY
	 * 		ordonnée de la bombe
	 * @param posZ
	 * 		hauteur de la bombe
	 * @param state
	 * 		état de la bombe
	 * @param burningDuration
	 * 		durée de combustion de la bombe
	 * @param currentSpeed
	 * 		vitesse courante de déplacement de la bombe
	 * @param slidingSpeed
	 * 		vitesse de déplacement au sol de la bombe
	 * @param countdownTrigger
	 * 		capacité à exploser en fonction du temps
	 * @param remoteControlTrigger
	 * 		capacité à exploser sur commande à distance
	 * @param explosionTrigger
	 * 		capacité à exploser au contact du feu
	 * @param normalDuration
	 * 		durée totale avant explosion (bombe à retardement seulement)
	 * @param latencyDuration
	 * 		latence entre le contact avec le feu et l'explosion
	 * @param failureProbability
	 * 		probabilité de tomber en panne
	 * @param firePrototype
	 * 		exemple de feu généré par la bombe quand elle explose
	 * @param stopHeroes
	 * 		capacité à bloquer les personnages
	 * @param stopFires
	 * 		capacité à bloquer le feu
	 * @param throughItems
	 * 		capacité à traverser les items
	 * @param range
	 * 		portée de l'explosion
	 * @param penetrating
	 * 		nature pénétrante du feu
	 * @param color
	 * 		couleur de la bombe
	 * @param working
	 * 		état de fonctionnement de la bombe
	 * @param time
	 * 		temps écoulé depuis le dépôt de la bombe
	 */
	protected AiSimBomb(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed, double slidingSpeed,
			boolean countdownTrigger, boolean remoteControlTrigger, boolean explosionTrigger,
			long normalDuration, long latencyDuration, float failureProbability, AiFire firePrototype,
			AiStopType stopHeroes, AiStopType stopFires, boolean throughItems,
			int range, boolean penetrating,
			PredefinedColor color, boolean working, long time)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		
		// fuse
		this.firePrototype = firePrototype;
		this.countdownTrigger = countdownTrigger;
		this.remoteControlTrigger = remoteControlTrigger;
		this.explosionTrigger = explosionTrigger;
		this.normalDuration = normalDuration;
		this.latencyDuration = latencyDuration;
		this.failureProbability = failureProbability;
		
		// collisions
		this.stopHeroes = stopHeroes;
		this.stopFires = stopFires;
		this.throughItems = throughItems;
		
		// range
		this.range = range;
		this.penetrating = penetrating;

		// misc
		this.color = color;
		this.working = working;
		this.time = time;
		this.slidingSpeed = slidingSpeed;
	}	

	/**
	 * crée une simulation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param bomb	
	 * 		sprite à simuler
	 * @param tile	
	 * 		case devant contenir le sprite
	 */
	protected AiSimBomb(AiBomb bomb, AiSimTile tile)
	{	super(bomb,tile);
	
		// fuse
		countdownTrigger = bomb.hasCountdownTrigger();
		remoteControlTrigger = bomb.hasRemoteControlTrigger();
		explosionTrigger = bomb.hasExplosionTrigger();
		normalDuration = bomb.getNormalDuration();
		latencyDuration = bomb.getLatencyDuration();
		failureProbability = bomb.getFailureProbability();
		firePrototype = bomb.getFirePrototype();

		// collisions
		stopHeroes = bomb.hasStopHeroes();
		stopFires = bomb.hasStopFires();
		throughItems = bomb.hasThroughItems();
		
		// range
		range = bomb.getRange();
		penetrating = bomb.isPenetrating();
		
		// misc
		color = bomb.getColor();
		working = bomb.isWorking();
		time = bomb.getTime();
		slidingSpeed = bomb.getSlidingSpeed();
	}

	/////////////////////////////////////////////////////////////////
	// FUSE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** déclenchement par compte à rebours */
	private boolean countdownTrigger;
	/** déclenchement par télécommande */
	private boolean remoteControlTrigger;
	/** déclenchement par explosion */
	private boolean explosionTrigger;
	/** délai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;
	/** latence de la bombe quand son explosion est déclenchée par une autre bombe */
	private long latencyDuration;
	/** probabilité que la bombe tombe en panne quand elle devrait exploser */
	private float failureProbability;
	
	@Override
	public float getFailureProbability()
	{	return failureProbability;	
	}
	
	@Override
	public boolean hasCountdownTrigger()
	{	return countdownTrigger;
	}
	
	@Override
	public boolean hasRemoteControlTrigger()
	{	return remoteControlTrigger;
	}
	
	@Override
	public boolean hasExplosionTrigger()
	{	return explosionTrigger;
	}
	
	@Override
	public long getNormalDuration()
	{	return normalDuration;
	}

	@Override
	public long getLatencyDuration()
	{	return latencyDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** exemple de feu que la bombe peut générer */
	private AiFire firePrototype;
	
	@Override
	public AiFire getFirePrototype()
	{	return firePrototype;
	}
	
	@Override
	public long getExplosionDuration()
	{	return firePrototype.getBurningDuration();
	}

	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	/** indique si la flamme produite par cette bombe est capable de traverser les murs */
	private boolean penetrating;
	
	@Override
	public int getRange()
	{	return range;	
	}
	
	/**
	 * met à jour la portée de cette bombe
	 * (méthode utilisée seulement lors de la simulation)
	 * 
	 * @param delta	
	 * 		la modification à apporter à la portée de cette bombe
	 */
	protected void updateRange(int delta)
	{	range = range + delta;
	}
	
	@Override
	public boolean isPenetrating()
	{	return penetrating;	
	}
	
	@Override
	public List<AiTile> getBlast()
	{	// init
		List<AiTile> result = new ArrayList<AiTile>();
		AiSimFire fire = new AiSimFire(firePrototype,null);
		
		// center
		result.add(tile);
		
		// branches
		boolean blocked[] = {false,false,false,false};
		AiSimTile tiles[] = {tile,tile,tile,tile};
		Direction directions[] = {Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP};
		List<AiSimTile> processed = new ArrayList<AiSimTile>();
		processed.add(tile);
		boolean goOn = true;
		int length = 1;
		while(goOn && length<=range)
		{	goOn = false;
			// increase the explosion
			for(int i=0;i<directions.length;i++)
			{	if(!blocked[i])
				{	// get the tile
					Direction direction = directions[i];
					AiSimTile tempTile = tiles[i].getNeighbor(direction);
					tiles[i] = tempTile;
					if(!processed.contains(tempTile))
					{	processed.add(tempTile);
						blocked[i] = !tempTile.isCrossableBy(fire);
						goOn = goOn || !blocked[i];
						result.add(tempTile);
					}
				}
			}
			length++;
		}
	
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** inidique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne) */
	private boolean working;
	
	@Override
	public boolean isWorking()
	{	return working;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
	private PredefinedColor color;
	
	@Override
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AiSimHero getOwner()
	{	AiSimZone zone = tile.getZone();
		AiSimHero result = zone.getHeroByColor(color);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** vitesse de déplacement au sol de la bombe, exprimée en pixel/seconde */
	private double slidingSpeed;
	
	@Override
	public double getSlidingSpeed()
	{	return slidingSpeed;
	}

	/////////////////////////////////////////////////////////////////
	// TIME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis que la bombe a été posée, exprimé en ms */
	private long time = 0;
	
	@Override
	public long getTime()
	{	return time;	
	}
	
	/**
	 * modifie le temps écoulé
	 * 
	 * @param time
	 * 		temps écoulé
	 */
	protected void setTime(long time)
	{	this.time = time;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si cette bombe laisse passer les joueurs */
	private AiStopType stopHeroes;
	/** indique si cette bombe laisse passer le feu */
	private AiStopType stopFires;
	/** indique si cette bombe peut traverser les items */
	private boolean throughItems;

	@Override
	public AiStopType hasStopHeroes()
	{	return stopHeroes;
	}
	
	@Override
	public AiStopType hasStopFires()
	{	return stopFires;
	}
	
	@Override
	public boolean hasThroughItems()
	{	return throughItems;	
	}

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	// par défaut, on bloque
		boolean result = false;
		
		// si le sprite considéré est un personnage
		if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero) sprite;
			if(hero.getTile()==getTile()) //simplification
				result = true;
			else if(stopHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBombs();
			else if(stopHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		
		// si le sprite considéré est un feu
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire) sprite;
			if(stopFires==AiStopType.NO_STOP)
				result = true;
			else if(stopFires==AiStopType.WEAK_STOP)
				result = fire.hasThroughBombs();
			else if(stopFires==AiStopType.STRONG_STOP)
				result = false;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Bomb: [");
		result.append(super.toString());
		result.append(" - clr: "+color);
		result.append(" - wrk: "+working);
		result.append(" - dur.: "+normalDuration);
		result.append(" - rge: "+range);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void finish()
	{	super.finish();
	}
}
