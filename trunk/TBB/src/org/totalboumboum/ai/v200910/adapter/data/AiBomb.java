package org.totalboumboum.ai.v200910.adapter.data;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import org.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.tools.images.PredefinedColor;


/**
 * représente une bombe du jeu, ie un objet que les joueurs peuvent d�poser
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */

public class AiBomb extends AiSprite<Bomb>
{	
	/**
	 * crée une représentation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiBomb(AiTile tile, Bomb sprite)
	{	super(tile,sprite);
		initFuseParameters();
		initRange();
		initColor();
		updateWorking();
		updateBlast();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateWorking();
		updateBlast();
		updateTime();
		updateCollisions();
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
	/** latence de la bombe quand son explosion est d�clench�e par une autre bombe */
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
	 * initialisation des délais liés à l'explosion de la bombe
	 */
	private void initFuseParameters()
	{	Bomb bomb = getSprite();
	
		// délai normal avant l'explosion 
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();		
		}	
		// compte à rebours
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			countdownTrigger = ability.isActive();
		}
		// télécommande
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_CONTROL);
			remoteControlTrigger = ability.isActive();
		}
		// feu
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_COMBUSTION);
			explosionTrigger = ability.isActive();
		}
		// latence de la bombe en cas de d�tonation d�clench�e par explosion
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_EXPLOSION_LATENCY);
			latencyDuration = (long)ability.getStrength();
		}
		// durée de l'explosion
		{	ExplosionManager explosionManager = bomb.getExplosionManager();
			explosionDuration = explosionManager.getExplosionDuration();
		}
		// probabilité de panne
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
			failureProbability = ability.getStrength();
		}
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
	 * renvoie la latence de cette bombe, dans le cas où elle peut être d�clench�e par
	 * une explosion. Cette latence représente le temps entre le moment o�
	 * la bombe est touch�e par l'explosion, et le moment où elle commence effectivement
	 * à exploser.
	 * 
	 * @return	la latence de la bombe pour une d�tonation d�clench�e par une autre explosion
	 */
	public long getLatencyDuration()
	{	return latencyDuration;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	/** liste des cases qui vont subir l'explosion de la bombe */
	private final List<AiTile> blast = new ArrayList<AiTile>();
	
	/**
	 * renvoie la portée de la bombe
	 * (ie. le nombre de cases occupées par sa flamme)
	 * @return	portée de la bombe
	 */
	public int getRange()
	{	return range;	
	}
	
	/**
	 * initialise la portée de la bombe
	 */
	private void initRange()
	{	Bomb bomb = getSprite();
		range = bomb.getFlameRange();
	}
	
	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser.
	 * Cette méthode tient compte de murs, items, etc., c'est à dire qu'elle
	 * ne donne que les cases qui seront touch�es si la bombe devait exploser
	 * à l'instant où cette méthode est invoquée. Si un des obstacles à l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer, il faut r�-exécuter cette méthode pour avoir le
	 * nouveau souffle de la bombe dans ce nouvel environnement.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touch�es par la flamme de cette bombe 
	 */
	public List<AiTile> getBlast()
	{	return blast;
	}

	/**
	 * met à jour la liste des cases qui seront touch�es par
	 * l'explosion de cette bombe
	 */
	private void updateBlast()
	{	Bomb bomb = getSprite();
		List<Tile> tiles = bomb.getExplosionManager().makeExplosion(true); 
		blast.clear();
		for(Tile tile: tiles)
		{	int line = tile.getLine();
			int col = tile.getCol();
			AiTile t = getTile().getZone().getTile(line,col);
			blast.add(t);
		}
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
	
	/**
	 * met à jour l'indicateur de panne de cette bombe
	 */
	private void updateWorking()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		if(gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.STANDING_FAILING)
			working = false;
		else
			working = true;
		
	}

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a posé la bombe 
	 * (pour certains niveaux sp�ciaux où les blocs peuvent générer des bombes)  
	 * 
	 * @return un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/**
	 * initialise la couleur de la bombe
	 */
	private void initColor()
	{	Bomb sprite = getSprite();
		color = sprite.getColor();	
	}

	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis que la bombe a été posée, exprimé en ms */
	private double time = 0;
	
	/**
	 * renvoie le temps écoulé depuis que la bombe a été posée,
	 * exprimé en millisecondes
	 * 
	 * @return	temps exprimé en ms
	 */
	public double getTime()
	{	return time;	
	}
	
	/**
	 * met à jour le temps écoulé depuis que la bombe a été posée
	 */
	private void updateTime()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		
		if(gesture==GestureName.STANDING || gesture==GestureName.STANDING_FAILING
			|| gesture==GestureName.OSCILLATING || gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.SLIDING || gesture==GestureName.SLIDING)
		{	double elapsedTime = getTile().getZone().getElapsedTime();
			time = time + elapsedTime;
		}
		else if(gesture==GestureName.APPEARING
				|| gesture==GestureName.BOUNCING
				|| gesture==GestureName.DISAPPEARING
				|| gesture==GestureName.ENTERING
				|| gesture==GestureName.HIDING
				|| gesture==GestureName.LANDING
				|| gesture==GestureName.NONE
				|| gesture==GestureName.PREPARED)
		{	time = 0;		
		}
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
	 * teste si cette bombe est capable de passer
	 * à travers les items
	 * 
	 * @return	vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;	
	}

	/** 
	 * met jour les différentes caractéristiques de cette bombe
	 * concernant la gestion des collisions avec les autres sprites
	 */
	private void updateCollisions()
	{	Bomb sprite = getSprite();
		
		// bloque les personnages
		{	GeneralAction generalAction = new GeneralMoveLow();
			generalAction.addActor(Role.HERO);
			generalAction.addDirection(Direction.RIGHT);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.COLLISION);
			actorCircumstance.addOrientation(Orientation.FACE);
			actorCircumstance.addTilePosition(TilePosition.NEIGHBOR);
			Circumstance targetCircumstance = new Circumstance();
			List<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			List<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_BOMB);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopHeroes = AiStopType.STRONG_STOP;
				else
					stopHeroes = AiStopType.WEAK_STOP;
			}
			else
				stopHeroes = AiStopType.NO_STOP;
		}

		// bloque le feu
		{	GeneralAction generalAction = new GeneralAppear();
			generalAction.addActor(Role.FIRE);
			generalAction.addDirection(Direction.NONE);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.INTERSECTION);
			actorCircumstance.addOrientation(Orientation.NEUTRAL);
			actorCircumstance.addTilePosition(TilePosition.SAME);
			Circumstance targetCircumstance = new Circumstance();
			ArrayList<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			ArrayList<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_BOMB);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopFires = AiStopType.STRONG_STOP;
				else
					stopFires = AiStopType.WEAK_STOP;
			}
			else
				stopFires = AiStopType.NO_STOP;
		}
	}	

	public boolean isCrossableBy(AiSprite<?> sprite)
	{	// par défaut, on bloque
		boolean result = false;
		// si le sprite considéré est un personnage
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
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
	void finish()
	{	super.finish();
	}
}
