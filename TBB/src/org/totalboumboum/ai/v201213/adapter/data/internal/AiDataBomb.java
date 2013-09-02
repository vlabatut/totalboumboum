package org.totalboumboum.ai.v201213.adapter.data.internal;

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
import java.util.Collections;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiStopType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.engine.container.explosionset.Explosion;
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
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.TrajectoryDirection;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Représente une bombe du jeu, ie un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
final class AiDataBomb extends AiDataSprite<Bomb> implements AiBomb
{	
	/**
	 * Crée une représentation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param sprite
	 * 		Sprite à représenter.
	 */
	protected AiDataBomb(AiDataTile tile, Bomb sprite)
	{	super(tile,sprite);
		
		initFirePrototype();
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
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		updateWorking();
		updateBlast();
		updateTime();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// FIRE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Exemple de feu que la bombe peut générer */
	private AiFire firePrototype;
	
	/**
	 * Initialise l'exemple de feu que cette bombe peut générer.
	 */
	private void initFirePrototype()
	{	Bomb bomb = getSprite();
		ExplosionManager explosionManager = bomb.getExplosionManager();
		Explosion explosion = explosionManager.getExplosion();
		Fire fire = explosion.makeFire(null,bomb.getTile());
		firePrototype = new AiDataFire(null,fire);
	}
	
	@Override
	public AiFire getFirePrototype()
	{	return firePrototype;
	}

	@Override
	public long getExplosionDuration()
	{	return firePrototype.getBurningDuration();
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
	
	/**
	 * Initialisation des délais liés à l'explosion de la bombe
	 */
	private void initFuseParameters()
	{	Bomb bomb = getSprite();
		
		// délai normal avant l'explosion 
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();
		}	
		// déclenchement par compte à rebours
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			countdownTrigger = ability.isActive();
		}
		// déclenchement par télécommande
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_CONTROL);
			remoteControlTrigger = ability.isActive();
		}
		// déclenchement par le feu
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_COMBUSTION);
			explosionTrigger = ability.isActive();
		}
		// latence de la bombe en cas de détonation déclenchée par explosion
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_EXPLOSION_LATENCY);
			latencyDuration = (long)ability.getStrength();
		}
		// probabilité de panne
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
			failureProbability = ability.getStrength();
		}
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
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	/** Liste des cases qui vont subir l'explosion de la bombe */
	private final List<AiTile> blast = new ArrayList<AiTile>();
	/** Version immuable de la liste blast */
	private final List<AiTile> externalBlast = Collections.unmodifiableList(blast);
	/** Type du feu généré : normal ou pénétrant */
	private boolean penetrating;
	
	@Override
	public int getRange()
	{	return range;	
	}
	
	/**
	 * Initialise la portée de la bombe.
	 */
	private void initRange()
	{	Bomb bomb = getSprite();
		range = bomb.getFlameRange();
	}
	
	@Override
	public boolean isPenetrating()
	{	return penetrating;	
	}
	
	@Override
	public List<AiTile> getBlast()
	{	return externalBlast;
	}

	/**
	 * Met à jour la liste des cases qui seront touchées par
	 * l'explosion de cette bombe.
	 */
	private void updateBlast()
	{	// blast
		Bomb bomb = getSprite();
		List<Tile> tiles = bomb.getExplosionManager().makeExplosion(true); 
		blast.clear();
		for(Tile tile: tiles)
		{	int row = tile.getRow();
			int col = tile.getCol();
			AiDataTile t = getTile().getZone().getTile(row,col);
			blast.add(t);
		}
		
		// penetrating
		penetrating = bomb.getExplosionManager().isPenetrating();
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si cette bombe fonctionne normalement (i.e. si elle n'est pas tombée en panne) */
	private boolean working;
	
	@Override
	public boolean isWorking()
	{	return working;	
	}
	
	/**
	 * Met à jour l'indicateur de panne de cette bombe.
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
	/** Couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
	private PredefinedColor color;
	
	@Override
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/**
	 * Initialise la couleur de la bombe.
	 */
	private void initColor()
	{	Bomb sprite = getSprite();
		color = sprite.getColor();	
	}

	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public AiHero getOwner()
	{	AiZone zone = tile.getZone();
		AiHero result = zone.getHeroByColor(color);
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

	@Override
	protected void updateSpeed()
	{	// current speed
		super.updateSpeed();
	
		// sliding speed (in general)
		Sprite sprite = getSprite();
		double speedCoeff = sprite.getGroundSpeedCoeff()[0];
		Gesture sliding = getSprite().getGesturePack().getGesture(GestureName.SLIDING);
		TrajectoryDirection slidingRight = sliding.getTrajectoryDirection(Direction.RIGHT);
		double totalShift = slidingRight.getTotalXShift();
		double totalDuration = slidingRight.getTotalDuration();
		double basicSpeed = totalShift/totalDuration * 1000; // pas sûr de ce x1000 (absent de AiHero)
		slidingSpeed = speedCoeff*basicSpeed;
//if(slidingSpeed==0)
//	System.out.print("");
		//System.out.println(getSprite().getColor()+": walkingSpeed="+walkingSpeed);
	}

	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Temps écoulé depuis que le compte à rebours a (re)commencé, exprimé en ms */
	private long time = 0;
	
	@Override
	public long getElapsedTime()
	{	return time;	
	}
	
	/**
	 * Met à jour le temps écoulé depuis que le 
	 * compte à rebours a (re)commencé.
	 */
	private void updateTime()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		
		if(gesture==GestureName.STANDING || gesture==GestureName.STANDING_FAILING
			|| gesture==GestureName.OSCILLATING || gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.SLIDING || gesture==GestureName.SLIDING
			|| gesture==GestureName.BURNING)
		{	long elapsedTime = getTile().getZone().getElapsedTime();
			time = time + elapsedTime;
		}
		else if(gesture==GestureName.APPEARING
				|| gesture==GestureName.BOUNCING
//				|| gesture==GestureName.DISAPPEARING
				|| gesture==GestureName.ENTERING
				|| gesture==GestureName.HIDING
				|| gesture==GestureName.LANDING
				|| gesture==GestureName.NONE
				|| gesture==GestureName.PREPARED)
		{	time = 0;		
		}
//		System.out.println(sprite.getId()+":"+time+"/"+normalDuration+" ("+gesture+")");
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

	/** 
	 * Met à jour les différentes caractéristiques de cette bombe
	 * concernant la gestion des collisions avec les autres sprites.
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
			List<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			List<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
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
