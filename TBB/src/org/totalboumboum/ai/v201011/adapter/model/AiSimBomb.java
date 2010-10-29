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
 * simule une bombe du jeu, i.e. un objet que les joueurs peuvent d�poser
 * pour d�truire les murs et �liminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimBomb extends AiSimSprite
{	
	/**
	 * 
	 * cr�e une simulation de la bombe pass�e en param�tre,
	 * avec les propri�t�s pass�es en param�tres.
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
	 * cr�e une simulation de la bombe pass�e en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param sprite	sprite � simuler
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
	 * construit une simulation de la bombe pass�e en param�tre,
	 * (donc une simple copie) et la place dans la case indiqu�e.
	 * 
	 * @param sprite	simulation � copier
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
	/** d�clenchement par compte � rebours */
	private boolean countdownTrigger;
	/** d�clenchement par t�l�commande */
	private boolean remoteControlTrigger;
	/** d�clenchement par explosion */
	private boolean explosionTrigger;
	/** d�lai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;
	/** dur�e de l'explosion (entre l'apparition et la disparition des flammes) */
	private long explosionDuration;
	/** latence de la bombe quand son explosion est d�clench�e par une autre bombe */
	private long latencyDuration;
	/** probabilit� que la bombe tombe en panne quand elle devrait exploser */
	private float failureProbability;
	
	/**
	 * renvoie la probabilit� que la bombe tombe en panne quand elle devrait exploser
	 * @return	une mesure de probabilit�
	 */
	public float getFailureProbability()
	{	return failureProbability;	
	}
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un compte � rebours
	 * @return	vrai si la bombe d�pend d'un compte � rebours
	 */
	public boolean hasCountdownTrigger()
	{	return countdownTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe d�pend d'une t�l�commande
	 * @return	vrai si la bombe d�pend d'une t�l�commande
	 */
	public boolean hasRemoteControlTrigger()
	{	return remoteControlTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un contact avec du feu
	 * @return	vrai si la bombe explose au contact du feu
	 */
	public boolean hasExplosionTrigger()
	{	return explosionTrigger;
	}
	
	/**
	 * renvoie le d�lai normal avant l'explosion de la bombe.
	 * Ce d�lai ne tient pas compte des pannes �ventuelles.
	 * Ce d�lai n'est pas d�fini pour tous les types de bombes
	 * 
	 * @return	le d�lai normal avant explosion exprim� en millisecondes
	 */
	public long getNormalDuration()
	{	return normalDuration;
	}

	/**
	 * renvoie la dur�e de l'explosion de cette bombe.
	 * Cette dur�e comprend l'apparition des flammes,
	 * la dur�e de vie des flammes, et leur disparition.
	 * Cette valeur n'est pas forc�ment constante, et peut varier d'une bombe � l'autre.
	 * 
	 * @return	la dur�e de l'explosion
	 */
	public long getExplosionDuration()
	{	return explosionDuration;
	}

	/**
	 * renvoie la latence de cette bombe, dans le cas o� elle peut �tre d�clench�e par
	 * une explosion. Cette latence repr�sente le temps entre le moment o�
	 * la bombe est touch�e par l'explosion, et le moment o� elle commence effectivement
	 * � exploser.
	 * 
	 * @return	la latence de la bombe pour une d�tonation d�clench�e par une autre explosion
	 */
	public long getLatencyDuration()
	{	return latencyDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** port�e de la bombe, ie. : nombre de cases occup�es par sa flamme */
	private int range;
	/** indique si la flamme produite par cette bombe est capable de traverser les murs */
	private boolean penetrating;
	
	/**
	 * renvoie la port�e de la bombe
	 * (ie. le nombre de cases occup�es par sa flamme)
	 * @return	port�e de la bombe
	 */
	public int getRange()
	{	return range;	
	}
	
	/**
	 * indique si le feu �mis par la bombe peut traverser les murs
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plut�t getBlast().
	 * 
	 * @return	vrai si le feu peut traverser les murs
	 */
	public boolean isPenetrating()
	{	return penetrating;	
	}
	
	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser
	 * (y compris la case contenant la bombe elle-m�me). 
	 * Cette m�thode tient compte de murs, items, etc., c'est � dire qu'elle
	 * ne donne que les cases qui seront touch�es si la bombe devait exploser
	 * � l'instant o� cette m�thode est invoqu�e.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touch�es par la flamme de cette bombe 
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
	/** inidique si cette bombe fonctionne normalement (ie si elle n'est pas tomb�e en panne) */
	private boolean working;
	
	/**
	 * indique si cette bombe fonctionne normalement (ie si elle n'est pas tomb�e en panne)
	 * 
	 * @return	vrai si cette bombe marche, faux si elle est en panne
	 */
	public boolean isWorking()
	{	return working;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur de la bombe (correspondant � celle du personnage qui l'a pos�e) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a pos� la bombe 
	 * (pour certains niveaux sp�ciaux o� les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return un symbole de type PredefinedColor repr�sentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps �coul� depuis que la bombe a �t� pos�e, exprim� en ms */
	private long time = 0;
	
	/**
	 * renvoie le temps �coul� depuis que la bombe a �t� pos�e,
	 * exprim� en millisecondes
	 * 
	 * @return	temps exprim� en ms
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
	 * teste si cette bombe est capable de passer � travers les items
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;	
	}

	@Override
	public boolean isCrossableBy(AiSimSprite sprite)
	{	// par d�faut, on bloque
		boolean result = false;
		// si le sprite consid�r� est un personnage
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
		// si le sprite consid�r� est un feu
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
