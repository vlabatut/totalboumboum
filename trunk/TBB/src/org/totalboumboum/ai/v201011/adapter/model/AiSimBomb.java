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
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiStopType;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule une bombe du jeu, i.e. un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimBomb extends AiSimSprite
{	
	/**
	 * 
	 * crée une simulation de la bombe passée en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param tile	case contenant le sprite
	 * @param countdownTrigger
	 * @param remoteControlTrigger
	 * @param explosionTrigger
	 * @param normalDuration
	 * @param explosionDuration
	 * @param latencyDuration
	 * @param failureProbability
	 * @param stopHeroes
	 * @param stopFires
	 * @param throughItems
	 * @param range
	 * @param color
	 * @param working
	 * @param time
	 */
	public AiSimBomb(AiSimTile tile, boolean countdownTrigger, boolean remoteControlTrigger, boolean explosionTrigger,
			long normalDuration, long explosionDuration, long latencyDuration, float failureProbability,
			AiStopType stopHeroes, AiStopType stopFires, boolean throughItems,
			int range, boolean penetrating,
			PredefinedColor color, boolean working, long time)
	{	super(tile);
		
		// fuse
		this.countdownTrigger = countdownTrigger;
		this.remoteControlTrigger = remoteControlTrigger;
		this.explosionTrigger = explosionTrigger;
		this.normalDuration = normalDuration;
		this.explosionDuration = explosionDuration;
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
	}	

	/**
	 * crée une simulation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param sprite	sprite à simuler
	 * @param tile	case devant contenir le sprite
	 */
	AiSimBomb(AiBomb sprite, AiSimTile tile)
	{	super(sprite,tile);
	
		// fuse
		countdownTrigger = sprite.hasCountdownTrigger();
		remoteControlTrigger = sprite.hasRemoteControlTrigger();
		explosionTrigger = sprite.hasExplosionTrigger();
		normalDuration = sprite.getNormalDuration();
		explosionDuration = sprite.getExplosionDuration();
		latencyDuration = sprite.getLatencyDuration();
		failureProbability = sprite.getFailureProbability();

		// collisions
		stopHeroes = sprite.hasStopHeroes();
		stopFires = sprite.hasStopFires();
		throughItems = sprite.hasThroughItems();
		
		// range
		range = sprite.getRange();
		penetrating = sprite.isPenetrating();
		
		// misc
		color = sprite.getColor();
		working = sprite.isWorking();
		time = sprite.getTime();
	}

	/**
	 * construit une simulation de la bombe passée en paramètre,
	 * (donc une simple copie) et la place dans la case indiquée.
	 * 
	 * @param sprite	simulation à copier
	 * @param tile	simulation de la case devant contenir la copie
	 */
	public AiSimBomb(AiSimBomb sprite, AiSimTile tile)
	{	super(sprite,tile);
		
		// fuse
		countdownTrigger = sprite.countdownTrigger;
		remoteControlTrigger = sprite.remoteControlTrigger;
		explosionTrigger = sprite.explosionTrigger;
		normalDuration = sprite.normalDuration;
		explosionDuration = sprite.explosionDuration;
		latencyDuration = sprite.latencyDuration;
		failureProbability = sprite.failureProbability;
	
		// collisions
		stopHeroes = sprite.stopHeroes;
		stopFires = sprite.stopFires;
		throughItems = sprite.throughItems;
		
		// range
		range = sprite.range;
		penetrating = sprite.penetrating;
		
		// misc
		color = sprite.color;
		working = sprite.working;
		time = sprite.time;
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
	/** durée de l'explosion (entre l'apparition et la disparition des flammes) */
	private long explosionDuration;
	/** latence de la bombe quand son explosion est déclenchée par une autre bombe */
	private long latencyDuration;
	/** probabilité que la bombe tombe en panne quand elle devrait exploser */
	private float failureProbability;
	
	/**
	 * renvoie la probabilité que la bombe tombe en panne quand elle devrait exploser
	 * @return	une mesure de probabilité
	 */
	public float getFailureProbability()
	{	return failureProbability;	
	}
	
	/**
	 * indique si l'explosion de la bombe dépend d'un compte à rebours
	 * @return	vrai si la bombe dépend d'un compte à rebours
	 */
	public boolean hasCountdownTrigger()
	{	return countdownTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe dépend d'une télécommande
	 * @return	vrai si la bombe dépend d'une télécommande
	 */
	public boolean hasRemoteControlTrigger()
	{	return remoteControlTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe dépend d'un contact avec du feu
	 * @return	vrai si la bombe explose au contact du feu
	 */
	public boolean hasExplosionTrigger()
	{	return explosionTrigger;
	}
	
	/**
	 * renvoie le délai normal avant l'explosion de la bombe.
	 * Ce délai ne tient pas compte des pannes éventuelles.
	 * Ce délai n'est pas défini pour tous les types de bombes
	 * 
	 * @return	le délai normal avant explosion exprimé en millisecondes
	 */
	public long getNormalDuration()
	{	return normalDuration;
	}

	/**
	 * renvoie la durée de l'explosion de cette bombe.
	 * Cette durée comprend l'apparition des flammes,
	 * la durée de vie des flammes, et leur disparition.
	 * Cette valeur n'est pas forcément constante, et peut varier d'une bombe à l'autre.
	 * 
	 * @return	la durée de l'explosion
	 */
	public long getExplosionDuration()
	{	return explosionDuration;
	}

	/**
	 * renvoie la latence de cette bombe, dans le cas où elle peut être déclenchée par
	 * une explosion. Cette latence représente le temps entre le moment où
	 * la bombe est touchée par l'explosion, et le moment où elle commence effectivement
	 * à exploser.
	 * 
	 * @return	la latence de la bombe pour une détonation déclenchée par une autre explosion
	 */
	public long getLatencyDuration()
	{	return latencyDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	/** indique si la flamme produite par cette bombe est capable de traverser les murs */
	private boolean penetrating;
	
	/**
	 * renvoie la portée de la bombe
	 * (ie. le nombre de cases occupées par sa flamme)
	 * @return	portée de la bombe
	 */
	public int getRange()
	{	return range;	
	}
	
	/**
	 * indique si le feu émis par la bombe peut traverser les murs
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutôt getBlast().
	 * 
	 * @return	vrai si le feu peut traverser les murs
	 */
	public boolean isPenetrating()
	{	return penetrating;	
	}
	
	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser
	 * (y compris la case contenant la bombe elle-même). 
	 * Cette méthode tient compte de murs, items, etc., c'est à dire qu'elle
	 * ne donne que les cases qui seront touchées si la bombe devait exploser
	 * à l'instant où cette méthode est invoquée.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touchées par la flamme de cette bombe 
	 */
	public List<AiSimTile> getBlast()
	{	// init
		List<AiSimTile> result = new ArrayList<AiSimTile>();
		AiSimFire fire = new AiSimFire(tile,penetrating,penetrating,penetrating);
		
		// center
		if(tile.isCrossableBy(fire))
		{	result.add(tile);
		
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
		}
	
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** inidique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne) */
	private boolean working;
	
	/**
	 * indique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne)
	 * 
	 * @return	vrai si cette bombe marche, faux si elle est en panne
	 */
	public boolean isWorking()
	{	return working;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a posé la bombe 
	 * (pour certains niveaux spéciaux où les blocs peuvent générer des bombes)  
	 * 
	 * @return un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis que la bombe a été posée, exprimé en ms */
	private long time = 0;
	
	/**
	 * renvoie le temps écoulé depuis que la bombe a été posée,
	 * exprimé en millisecondes
	 * 
	 * @return	temps exprimé en ms
	 */
	public long getTime()
	{	return time;	
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

	/**
	 * teste si cette bombe est capable de passer à travers les items
	 * <b>ATTENTION :</b> cette méthode ne devrait pas être utilisée directement par l'IA,
	 * elle est destinée au calcul des modèles simulant l'évolution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;	
	}

	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
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
	void finish()
	{	super.finish();
	}
}
