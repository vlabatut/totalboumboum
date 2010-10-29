package org.totalboumboum.ai.v201011.adapter.data;

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
 * repr�sente une bombe du jeu, ie un objet que les joueurs peuvent d�poser
 * pour d�truire les murs et �liminer les autre joueurs.
 * 
 * @author Vincent Labatut
 *
 */
public class AiBomb extends AiSprite<Bomb>
{	
	/**
	 * cr�e une repr�sentation de la bombe pass�e en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � repr�senter
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
	 * initialisation des d�lais li�s � l'explosion de la bombe
	 */
	private void initFuseParameters()
	{	Bomb bomb = getSprite();
	
		// d�lai normal avant l'explosion 
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();		
		}	
		// compte � rebours
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			countdownTrigger = ability.isActive();
		}
		// t�l�commande
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
		// dur�e de l'explosion
		{	ExplosionManager explosionManager = bomb.getExplosionManager();
			explosionDuration = explosionManager.getExplosionDuration();
		}
		// probabilit� de panne
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_FAILURE_PROBABILITY);
			failureProbability = ability.getStrength();
		}
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
	/** liste des cases qui vont subir l'explosion de la bombe */
	private final List<AiTile> blast = new ArrayList<AiTile>();
	/** type du feu g�n�r� : normal ou p�n�trant */
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
	 * initialise la port�e de la bombe
	 */
	private void initRange()
	{	Bomb bomb = getSprite();
		range = bomb.getFlameRange();
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
	 * � l'instant o� cette m�thode est invoqu�e. Si un des obstacles � l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer, il faut r�-ex�cuter cette m�thode pour avoir le
	 * nouveau souffle de la bombe dans ce nouvel environnement.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touch�es par la flamme de cette bombe 
	 */
	public List<AiTile> getBlast()
	{	return blast;
	}

	/**
	 * met � jour la liste des cases qui seront touch�es par
	 * l'explosion de cette bombe
	 */
	private void updateBlast()
	{	// blast
		Bomb bomb = getSprite();
		List<Tile> tiles = bomb.getExplosionManager().makeExplosion(true); 
		blast.clear();
		for(Tile tile: tiles)
		{	int line = tile.getLine();
			int col = tile.getCol();
			AiTile t = getTile().getZone().getTile(line,col);
			blast.add(t);
		}
		
		// penetrating
		penetrating = bomb.getExplosionManager().isPenetrating();
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
	 * met � jour l'indicateur de panne de cette bombe
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
	
	/**
	 * met � jour le temps �coul� depuis que la bombe a �t� pos�e
	 */
	private void updateTime()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		
		if(gesture==GestureName.STANDING || gesture==GestureName.STANDING_FAILING
			|| gesture==GestureName.OSCILLATING || gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.SLIDING || gesture==GestureName.SLIDING)
		{	long elapsedTime = getTile().getZone().getElapsedTime();
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
		//System.out.println(sprite.getId()+":"+time+"/"+normalDuration);
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
	 * indique si ce bloc arr�te les personnages.
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te les personnages
	 */
	public AiStopType hasStopHeroes()
	{	return stopHeroes;
	}
	
	/**
	 * indique si ce bloc arr�te les explosions.
	 * <b>ATTENTION :</b> cette m�thode ne devrait pas �tre utilis�e directement par l'IA,
	 * elle est destin�e au calcul des mod�les simulant l'�volution du jeu.
	 * utilisez plutot isCrossableBy().
	 * 
	 * @return	une valeur AiStopType indiquant si ce bloc arr�te le feu
	 */
	public AiStopType hasStopFires()
	{	return stopFires;
	}
	
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

	/** 
	 * met � jour les diff�rentes caract�ristiques de cette bombe
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
	public boolean isCrossableBy(AiSprite<?> sprite)
	{	// par d�faut, on bloque
		boolean result = false;
		// si le sprite consid�r� est un personnage
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
		// si le sprite consid�r� est un feu
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
