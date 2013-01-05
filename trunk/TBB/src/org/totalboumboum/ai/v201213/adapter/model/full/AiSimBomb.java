package org.totalboumboum.ai.v201213.adapter.model.full;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStopType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Simule une bombe du jeu, i.e. un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 */
public final class AiSimBomb extends AiSimSprite implements AiBomb
{	
	/**
	 * Crée une simulation de la bombe passée en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification de la bombe.
	 * @param tile
	 * 		Case contenant la bombe.
	 * @param posX
	 * 		Abscisse de la bombe.
	 * @param posY
	 * 		Ordonnée de la bombe.
	 * @param posZ
	 * 		Hauteur de la bombe.
	 * @param state
	 * 		État de la bombe.
	 * @param burningDuration
	 * 		Durée de combustion de la bombe.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement de la bombe.
	 * @param slidingSpeed
	 * 		Vitesse de déplacement au sol de la bombe.
	 * @param countdownTrigger
	 * 		Capacité à exploser en fonction du temps.
	 * @param remoteControlTrigger
	 * 		Capacité à exploser sur commande à distance.
	 * @param explosionTrigger
	 * 		Capacité à exploser au contact du feu.
	 * @param normalDuration
	 * 		Durée totale avant explosion (bombe à retardement seulement).
	 * @param latencyDuration
	 * 		Latence entre le contact avec le feu et l'explosion.
	 * @param failureProbability
	 * 		Probabilité de tomber en panne.
	 * @param firePrototype
	 * 		Exemple de feu généré par la bombe quand elle explose.
	 * @param stopHeroes
	 * 		Capacité à bloquer les personnages.
	 * @param stopFires
	 * 		Capacité à bloquer le feu.
	 * @param throughItems
	 * 		Capacité à traverser les items.
	 * @param range
	 * 		Portée de l'explosion.
	 * @param penetrating
	 * 		Nature pénétrante du feu.
	 * @param color
	 * 		Couleur de la bombe.
	 * @param working
	 * 		État de fonctionnement de la bombe.
	 * @param time
	 * 		Temps écoulé depuis le dépôt de la bombe.
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
	 * Crée une simulation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	
	 * 		Case devant contenir le sprite.
	 * @param bomb	
	 * 		Sprite à simuler.
	 */
	protected AiSimBomb(AiSimTile tile, AiBomb bomb)
	{	super(tile,bomb);
	
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
		time = bomb.getElapsedTime();
		slidingSpeed = bomb.getSlidingSpeed();
	}

	/////////////////////////////////////////////////////////////////
	// FUSE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Déclenchement par compte à rebours */
	private boolean countdownTrigger;
	/** Déclenchement par télécommande */
	private boolean remoteControlTrigger;
	/** Déclenchement par explosion */
	private boolean explosionTrigger;
	/** Délai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;
	/** Latence de la bombe quand son explosion est déclenchée par une autre bombe */
	private long latencyDuration;
	/** Probabilité que la bombe tombe en panne quand elle devrait exploser */
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
	
	/**
	 * Indique à la bombe qu'elle a été
	 * touchée par du feu et devra donc
	 * exploser.
	 */
	protected void fireTriggerBomb()
	{	countdownTrigger = true;
		failureProbability = 0;
		normalDuration = latencyDuration;
		time = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Exemple de feu que la bombe peut générer */
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
	/** Portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	/** Portée de la bombe avant de prendre un malus genre no-flame */
	protected int savedRange = -1;
	/** Indique si la flamme produite par cette bombe est capable de traverser les murs */
	private boolean penetrating;
	
	@Override
	public int getRange()
	{	return range;	
	}
	
	/**
	 * Met à jour la portée de cette bombe
	 * (méthode utilisée seulement lors de la simulation).
	 * 
	 * @param delta	
	 * 		La modification à apporter à la portée de cette bombe.
	 */
	protected void updateRange(int delta)
	{	range = range + delta;
	}
	
	/**
	 * Renvoie la portée enregistrée avant de prendre
	 * un malus, genre no-flame. Utilisée lors de la
	 * simulation.
	 * 
	 * @return
	 * 		La portée avant malus.
	 */
	protected int getSavedRange()
	{	return savedRange;	
	}
	
	/**
	 * Enregistre la portée, avant de la modifier
	 * en raison d'un malus genre no-flame. Utilisée 
	 * lors de la simulation, pour restaurer la portée
	 * originale.
	 * <br/>
	 * Si la portée a déjà été enregistrée (i.e. plusieurs
	 * malus ramassés successivement), alors elle n'est
	 * pas ré-enregistrée, pour ne pas perdre la valeur pertinente.
	 */
	protected void recordRange()
	{	if(savedRange == -1)
			savedRange = range;
	}
	
	/**
	 * Restaure la portée, après qu'un malus genre 
	 * no-flame ait arrêté de l'affecter.
	 * <br/>
	 * L'enregistrement est ré-initialisé à -1, afin
	 * de permettre des sauvegardes ultérieures.
	 */
	protected void restoreRange()
	{	if(savedRange == -1)
		{	range = 1;
			savedRange = -1;
		}
		else
		{	range = savedRange;
			savedRange = -1;
		}
	}
	
	@Override
	public boolean isPenetrating()
	{	return penetrating;	
	}
	
	@Override
	public List<AiTile> getBlast()
	{	// init
		List<AiTile> result = new ArrayList<AiTile>();
		AiSimFire fire = new AiSimFire(null,firePrototype);
		
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
						List<AiBlock> blocks = tempTile.getBlocks();
						if(blocks.isEmpty() || blocks.get(0).isDestructible())
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
	/** Indique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne) */
	private boolean working;
	
	@Override
	public boolean isWorking()
	{	return working;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
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
	/** Vitesse de déplacement au sol de la bombe, exprimée en pixel/seconde */
	private double slidingSpeed;
	
	@Override
	public double getSlidingSpeed()
	{	return slidingSpeed;
	}

	/////////////////////////////////////////////////////////////////
	// TIME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Temps écoulé depuis que la bombe a été posée, exprimé en ms */
	private long time = 0;
	
	@Override
	public long getElapsedTime()
	{	return time;	
	}
	
	/**
	 * Modifie le temps écoulé.
	 * 
	 * @param time
	 * 		Nouveau temps écoulé, en ms.
	 */
	protected void setTime(long time)
	{	this.time = time;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si cette bombe laisse passer les joueurs */
	private AiStopType stopHeroes;
	/** Indique si cette bombe laisse passer le feu */
	private AiStopType stopFires;
	/** Indique si cette bombe peut traverser les items */
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
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
			if(hero.getTile().equals(getTile())) //simplification
				result = true;
			else if(stopHeroes==AiStopType.NO_STOP)
				result = true;
			else if(stopHeroes==AiStopType.WEAK_STOP)
				result = hero.hasThroughBombs();
			else if(stopHeroes==AiStopType.STRONG_STOP)
				result = false;
		}
		
		// si le sprite considéré est un feu
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire) sprite;
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
