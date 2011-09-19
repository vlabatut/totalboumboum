package org.totalboumboum.engine.content.sprite;

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
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.feature.event.AbstractEvent;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.StepImage;
import org.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import org.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import org.totalboumboum.engine.content.manager.ability.AbilityManager;
import org.totalboumboum.engine.content.manager.anime.AnimeManager;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.delay.DelayManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.modulation.ModulationManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangeAnimeEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangePositionEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.tools.images.PredefinedColor;

/** 
 * Sprite poss�dant un status :
 * softwall, hero, bomb, item...
 * 
 * @author Vincent Labatut
 *
 */
public abstract class Sprite implements Comparable<Sprite>
{	
	public Sprite()
	{	this(idCount);
		idCount ++;
	}

	public Sprite(int id)
	{	this.id = id;
		boundSprites = new ArrayList<Sprite>();
		toBeRemovedFromSprite = null;
		boundToSprite = null;
		currentGesture = null;
	}

	public void initSprite(Tile tile)
	{	this.tile = tile;
		center();
		eventManager.initGesture();
	}
	
	/////////////////////////////////////////////////////////////////
	// ID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static int idCount = 0;
	private int id;
	
	public int getId()
	{	return id;	
	}

	public void setId(int id)
	{	this.id = id;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARABLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
    public int compareTo(Sprite sprite)
	{	int result = id - sprite.getId();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public void setName(String name)
	{	this.name = name; 	
	}
	
	public String getName()
	{	return name;	
	}

	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract Role getRole();

	/////////////////////////////////////////////////////////////////
	// GESTURE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GesturePack gesturePack;
	protected Gesture currentGesture;

	public GesturePack getGesturePack()
	{	return gesturePack;	
	}
	
	public void setGesturePack(GesturePack gesturePack)
	{	this.gesturePack = gesturePack;	
	}
	
	/*
	 * change le gesture, la direction de l'animation et la direction des touches
	 * l'animation n'est r�initialisée que si le gesture est modifié
	 */
	public void setGesture(GestureName gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	// record event
		SpriteChangeAnimeEvent event = new SpriteChangeAnimeEvent(this);
		if(currentGesture==null || currentGesture.getName()!=gesture)
			event.setChange(SpriteChangeAnimeEvent.SPRITE_EVENT_GESTURE,gesture);
		if(spriteDirection!=animeManager.getCurrentDirection())
			event.setChange(SpriteChangeAnimeEvent.SPRITE_EVENT_DIRECTION,spriteDirection);
		if(reinit)
			event.setChange(SpriteChangeAnimeEvent.SPRITE_EVENT_REINIT,true);
		if(forcedDuration!=0)
			event.setChange(SpriteChangeAnimeEvent.SPRITE_EVENT_DURATION,forcedDuration);
		RoundVariables.writeEvent(event);
			
		// update gesture
		currentGesture = gesturePack.getGesture(gesture);
		modulationManager.updateGesture(currentGesture,spriteDirection);
		animeManager.updateGesture(currentGesture,spriteDirection,reinit,forcedDuration);
		trajectoryManager.updateGesture(currentGesture,spriteDirection,controlDirection,reinit,forcedDuration);
	}
	public void setGesture(GestureName gesture, Direction spriteDirection, Direction controlDirection, boolean reinit)
	{	setGesture(gesture, spriteDirection, controlDirection, reinit, 0);
	}
/*	
	public void initGesture(String gesture, Direction spriteDirection, double forcedDuration)
	{	if(!ended)
		{	eventManager.initGesture(gesture, spriteDirection);
			setGesture(gesture,spriteDirection,Direction.NONE,true,forcedDuration);
		}
	}
	public void initGesture(String gesture, Direction spriteDirection)
	{	initGesture(gesture,spriteDirection,0);		
	}
*/	
	public Gesture getCurrentGesture()
	{	return currentGesture;
	}
	
	public PredefinedColor getColor()
	{	return gesturePack.getColor();		
	}
	
	/////////////////////////////////////////////////////////////////
	// REPLAY EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void processChangeAnimeEvent(SpriteChangeAnimeEvent event)
	{	HashMap<String,Object> changes = event.getChanges();
		Direction direction = (Direction) changes.get(SpriteChangeAnimeEvent.SPRITE_EVENT_DIRECTION);
		if(direction==null)
			direction = getCurrentFacingDirection();
		Double duration = (Double) changes.get(SpriteChangeAnimeEvent.SPRITE_EVENT_DURATION);
		if(duration==null)
			duration = 0d;
		GestureName gestureName = (GestureName) changes.get(SpriteChangeAnimeEvent.SPRITE_EVENT_GESTURE);
		if(gestureName==null)
			gestureName = currentGesture.getName();
		Boolean reinit = (Boolean) changes.get(SpriteChangeAnimeEvent.SPRITE_EVENT_REINIT);
		if(reinit==null)
			reinit = false;
		setGesture(gestureName,direction,Direction.NONE,reinit,duration);
	}
	
	public void processChangePositionEvent(SpriteChangePositionEvent event, double zoomCoef)
	{	HashMap<String,Object> changes = event.getChanges();
		Double x = (Double) changes.get(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_X);
		if(x!=null)
			setCurrentPosX(x*zoomCoef);
		Double y = (Double) changes.get(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_Y);
		if(y!=null)
			setCurrentPosY(y*zoomCoef);
		Double z = (Double) changes.get(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_Z);
		if(z!=null)
			setCurrentPosZ(z*zoomCoef);
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		if(tile!=null)
			result = result+"["+name+"]("+tile.getLine()+","+tile.getCol()+")";
		result = result+"("+getCurrentPosX()+","+getCurrentPosY()+","+getCurrentPosZ()+")";
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite owner;
	
	public Sprite getOwner()
	{	return owner;
		//NOTE à modifier pour recherche r�cursivement l'owner final (mais peut être est-ce déjà fait ailleurs)
	}
	
	public void setOwner(Sprite owner)
	{	this.owner = owner;
	}

	/////////////////////////////////////////////////////////////////
	// BOUND TO SPRITE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite boundToSprite;
	
	public Sprite getBoundToSprite()
	{	return boundToSprite;
	}
	
	public boolean isBoundToSprite()
	{	return boundToSprite!=null;
	}
	
	public void setBoundToSprite(Sprite boundToSprite)
	{	// traitement seulement si le nouveau boundToSprite li� est différent de l'ancien
		if(this.boundToSprite!=boundToSprite)
		{	// on met à jour le trajectoryManager
			trajectoryManager.setBoundToSprite(boundToSprite);
			// s'il n'y a pas d'ancien boundToSprite : on d�connecte ce sprite de sa tile
			if(this.boundToSprite==null)
				changeTile(null);
			// s'il y a un ancien boundToSprite : on d�connecte ce sprite de ce boundToSprite 
			else
				setToBeRemovedFromSprite(this.boundToSprite);
			// s'il n'y a pas de nouveau boundToSprite : on connecte ce sprite à une Tile
			if(boundToSprite==null)
				changeTile(RoundVariables.level.getTile(getCurrentPosX(),getCurrentPosY()));
			// s'il y a un nouveau boundToSprite : on connecte ce sprite à ce boundToSprite
			else
				boundToSprite.addBoundSprite(this);
			this.boundToSprite = boundToSprite;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BOUND SPRITES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<Sprite> boundSprites;
	
	public boolean hasBoundSprite()
	{	return boundSprites.size()>0;	
	}
	
	public Iterator<Sprite> getBoundSprites()
	{	return boundSprites.iterator();	
	}
	
	public void addBoundSprite(Sprite boundSprite)
	{	boundSprites.add(boundSprite);		
	}
	
	public void removeBoundSprite(Sprite boundSprite)
	{	boundSprites.remove(boundSprite);		
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// update the sprite
	public void update()
	{	abilityManager.update();
		bombsetManager.update();
		controlManager.update();
//			eventManager.update();
		delayManager.update();
		itemManager.update();
		animeManager.update();
		trajectoryManager.update();
		/*
		 * NOTE : il est important que le trajectoryManager soit updat� en dernier
		 * comme �a, un changement de case arrive apr�s avoir traité tous les évènements
		 * (raisons de synchro)
		 */
//System.out.println("sx,sy:"+getPositionX()+";"+getPositionY()+" - tx,ty:"+tile.getLine()+";"+tile.getCol());
		Iterator<Sprite> i = boundSprites.iterator();
		while(i.hasNext())
		{	Sprite temp = i.next();
			temp.update();
			if(temp.getToBeRemovedFromSprite()==this)
			{	i.remove();
				temp.setToBeRemovedFromSprite(null);
			}
		}
	}
	
	public void spriteEnded()
	{	itemManager.releaseAllItems();
		bombsetManager.triggerAllBombs();
	}
	
	/////////////////////////////////////////////////////////////////
	// REMOVE FROM SPRITE		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite toBeRemovedFromSprite;
	
	public void setToBeRemovedFromSprite(Sprite toBeRemovedFromSprite)
	{	this.toBeRemovedFromSprite = toBeRemovedFromSprite;	
	}
	
	public Sprite getToBeRemovedFromSprite()
	{	return toBeRemovedFromSprite;	
	}

	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Tile tile;
	
	public Tile getTile()
	{	return tile;
	}
	
	public void changeTile(Tile newTile)
	{	if(tile!=newTile)
		{	// remove from previous tile
			if(tile!=null)
			{	String eventName = EngineEvent.TILE_HIGH_EXIT;
				if(isOnGround())
					eventName = EngineEvent.TILE_LOW_EXIT;
				EngineEvent event = new EngineEvent(eventName,this,null,getActualDirection());
				tile.spreadEvent(event);
				tile.removeSprite(this);				
			}
			// add in new tile
			if(newTile!=null)
			{	tile = newTile;
				tile.addSprite(this);
				if(!tile.containsPoint(getCurrentPosX(),getCurrentPosY()))
				{	setCurrentPosX(tile.getPosX());
					setCurrentPosY(tile.getPosY());
				}
				String eventName = EngineEvent.TILE_HIGH_ENTER;
				if(isOnGround())
					eventName = EngineEvent.TILE_LOW_ENTER;
				EngineEvent event = new EngineEvent(eventName,this,null,getActualDirection());
				tile.spreadEvent(event);
			}
		}
	}
	
/*	
	public void setTile(Tile tile)
	{	this.tile = tile;
		setToBeRemovedFromTile(false);
	}
*/	
	/////////////////////////////////////////////////////////////////
	// SPEED COEFF		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double speedAbility = 1;
	protected double speedAbilityCoef = 1;
	
	/**
	 * S'il y  a un boundToSprite, son speedCoeff est renvoy�.
	 * Sinon, c'est le produit entre le speedCoeff du sprite
	 * et celui du jeu.
	 * @return
	 */
	public double getCurrentSpeedCoeff()
	{	double result;
		if(boundToSprite==null)
		{	result = Configuration.getEngineConfiguration().getSpeedCoeff();
			
			// NOTE limitation to WALKING, or not?
			if(currentGesture.getName()==GestureName.WALKING
				|| currentGesture.getName()==GestureName.CARRYING
				|| currentGesture.getName()==GestureName.STANDING
				|| currentGesture.getName()==GestureName.HOLDING
				|| currentGesture.getName()==GestureName.PUSHING)
			{	double[] temp = getGroundSpeedCoeff();
				speedAbilityCoef = temp[0];
				speedAbility = temp[1];
				result = result*speedAbilityCoef;
			}
		}
		else
			result = boundToSprite.getCurrentSpeedCoeff();
		return result;
	}
	
	public double[] getGroundSpeedCoeff()
	{	double[] result = new double[2];
	
		StateAbility ability = modulateStateAbility(StateAbilityName.HERO_WALK_SPEED_MODULATION);
		int speed = (int)ability.getStrength();
		if(speed==speedAbility)
		{	result[0] = speedAbilityCoef;
			result[1] = speedAbility;
		}
		else
		{	double speedAbility = speed;
			double speedAbilityCoef = 1;
			int delta = -(int)Math.signum(speed);
			String name;
			do
			{	name = StateAbilityName.getHeroWalkSpeed(speed);
				if(name!=null) // i.e. if speed is not zero
				{	ability = modulateStateAbility(name);
					if(ability.isActive())
						speedAbilityCoef = ability.getStrength();
					else
						speed = speed + delta;
				}
			}
			while(name!=null && !ability.isActive());
			result[0] = speedAbilityCoef;
			result[1] = speedAbility;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getCurrentSpeed()
	{	return trajectoryManager.getCurrentSpeed();
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected AbilityManager abilityManager;

	public void setAbilityManager(AbilityManager abilityManager)
	{	this.abilityManager = abilityManager;
	}
	
	public StateAbility getAbility(String ability)
	{	return abilityManager.getAbility(ability);
	}
	
/*	public StateAbility getAbility(String name)
	{	return abilityManager.getAbility(name);
	}
*/	
	public ActionAbility getAbility(SpecificAction action)
	{	return abilityManager.getAbility(action);
	}
	public ActionAbility getAbility(GeneralAction action)
	{	return abilityManager.getAbility(action);
	}
	
/*	public ActionAbility getAbility(AbstractAction action)
	{	return abilityManager.getAbility(action);
	}
*/	
	public void modifyUse(AbstractAbility ability, int delta)
	{	abilityManager.modifyUse(ability, delta);
	}
	
	public void addDirectAbility(AbstractAbility ability)
	{	abilityManager.addDirectAbility(ability);
	}
	
	public List<AbstractAbility> getDirectAbilities()
	{	return abilityManager.getDirectAbilities();		
	}
	
	/////////////////////////////////////////////////////////////////
	// ANIMES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected AnimeManager animeManager;

	public void setAnimeManager(AnimeManager animeManager)
	{	this.animeManager = animeManager;
	}
	
	public List<StepImage> getCurrentImages()
	{	return animeManager.getCurrentImages();	
	}
	
	public Direction getCurrentFacingDirection()
	{	
//if(animeManager.getCurrentDirection()==null)
//	while(true)System.out.println(name);
		return animeManager.getCurrentDirection();
	}
	
	public boolean hasShadow()
	{	return animeManager.hasShadow();
	}
	
	public StepImage getShadow()
	{	return animeManager.getShadow();	
	}
	
	public double getBoundHeight()
	{	return animeManager.getBoundHeight();
	}
	
	public double getAnimeTotalDuration()
	{	return animeManager.getTotalDuration();
	}
	
	public double getAnimeCurrentTime()
	{	return animeManager.getCurrentTime();
	}

	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected BombsetManager bombsetManager;

	public void setBombsetManager(BombsetManager bombsetManager)
	{	this.bombsetManager = bombsetManager;
	}
	
	public BombsetManager getBombsetManager()
	{	return bombsetManager;	
	}
	
	public Bomb makeBomb()
	{	return bombsetManager.makeBomb();
	}
	
	public void dropBomb(SpecificDrop action)
	{	bombsetManager.dropBomb(action);
	}
	
	public void triggerBomb()
	{	bombsetManager.triggerBomb();
	}
	
	public LinkedList<Bomb> getDroppedBombs()
	{	return bombsetManager.getDroppedBombs();		
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ControlManager controlManager;

	public void setControlManager(ControlManager controlManager)
	{	this.controlManager = controlManager;
	}
	
	public void setControlSettings(ControlSettings controlSettings)
	{	controlManager.setControlSettings(controlSettings);
	}
	
	public void putControlCode(ControlCode controlCode)
	{	controlManager.putControlCode(controlCode);
	}
	
	public void putControlEvent(ControlEvent controlEvent)
	{	controlManager.putControlEvent(controlEvent);
	}

	/////////////////////////////////////////////////////////////////
	// DELAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DelayManager delayManager;

	public void setDelayManager(DelayManager delayManager)
	{	this.delayManager = delayManager;
	}
	
	public void addDelay(String name,double duration)
	{	delayManager.addDelay(name,duration);
	}

	public void addIterDelay(String name, int iterations)
	{	delayManager.addIterDelay(name,iterations);
	}
	
	public void removeDelay(String name)
	{	delayManager.removeDelay(name);
	}
	
	public double getDelay(String name)
	{	return delayManager.getDelay(name);
	}
	
	public boolean hasDelay(String name)
	{	return delayManager.hasDelay(name);
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected EventManager eventManager;

	public void setEventManager(EventManager eventManager)
	{	this.eventManager = eventManager;
	}
	
	public void processEvent(AbstractEvent event)
	{	if(event instanceof ActionEvent)
			eventManager.processEvent((ActionEvent)event);
		else if(event instanceof ControlEvent)
			eventManager.processEvent((ControlEvent)event);
		else if(event instanceof EngineEvent)
			eventManager.processEvent((EngineEvent)event);
	}
	
	public void spreadEvent(AbstractEvent e)
	{	Iterator<Sprite> i = boundSprites.iterator();
		while(i.hasNext())
			i.next().processEvent(e);		
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	RoundVariables.loop.addStatisticEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ExplosionManager explosionManager;

	public void setExplosionManager(ExplosionManager explosionManager)
	{	this.explosionManager = explosionManager;
	}
	
	public ExplosionManager getExplosionManager()
	{	return explosionManager;
	}
	
	public List<Tile> makeExplosion(boolean fake)
	{	return explosionManager.makeExplosion(fake);	
	}
	
	public void setFlameRange(int flameRange)
	{	explosionManager.setFlameRange(flameRange);		
	}	
	
	public int getFlameRange()
	{	return explosionManager.getFlameRange();		
	}	
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ItemManager itemManager;

	public void setItemManager(ItemManager itemManager)
	{	this.itemManager = itemManager;
	}
	
	public void collectItem(Item item)
	{	itemManager.collectItem(item);	
	}
	
	public void receiveItem(Item item)
	{	itemManager.receiveItem(item);	
	}
	
	public void addInitialItem(Item item)
	{	itemManager.addInitialItem(item);	
	}
	
	public void releaseRandomItem()
	{	itemManager.releaseRandomItem();	
	}
	
	public void releaseLastItem()
	{	itemManager.releaseLastItem();	
	}
	
/*	public ArrayList<Item> dropAllItems()
	{	return itemManager.dropAllItems();
	}
*/	
	public List<AbstractAbility> getItemsAbilities()
	{	return itemManager.getItemAbilities();	
	}
	
	public void reinitInitialItems()
	{	itemManager.reinitInitialItems();	
	}

	public void transmitAllItems(Sprite target)
	{	itemManager.transmitAllItems(target);	
	}

	public void startItemManager()
	{	itemManager.start();		
	}
	
	/////////////////////////////////////////////////////////////////
	// MODULATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ModulationManager modulationManager;

	public void setModulationManager(ModulationManager modulationManager)
	{	this.modulationManager = modulationManager;
	}
	
/*	public SelfModulation getSelfModulation(StateAbilityName name)
	{	return modulationManager.getSelfModulation(name);
	}
*/	
	public OtherModulation getOtherModulation(String name, Sprite modulated)
	{	return modulationManager.getOtherModulation(name,modulated);
	}
	
/*	public ActorModulation getActorModulation(SpecificAction action)
	{	return modulationManager.getActorModulation(action);
	}
*/	
	public TargetModulation getTargetModulation(SpecificAction action)
	{	return modulationManager.getTargetModulation(action);
	}
	
	public ThirdModulation getThirdModulation(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return modulationManager.getThirdModulation(action,actorCircumstances,targetCircumstances);
	}
	public ThirdModulation getThirdModulation(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return modulationManager.getThirdModulation(action,actorProperties,targetProperties,actorCircumstances,targetCircumstances);
	}
	
	public StateAbility modulateStateAbility(String name)
	{	return modulationManager.modulateStateAbility(name);
	}
	
	public ActionAbility modulateAction(SpecificAction action)
	{	return modulationManager.modulateAction(action);
	}
	
	public boolean isTargetPreventing(SpecificAction action)
	{	return modulationManager.isTargetPreventing(action);
	}

	public boolean isThirdPreventing(SpecificAction action, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return modulationManager.isThirdPreventing(action,actorCircumstances,targetCircumstances);
	}
	public boolean isThirdPreventing(GeneralAction action, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances)
	{	return modulationManager.isThirdPreventing(action,actorProperties,targetProperties,actorCircumstances,targetCircumstances);
	}
	
/*	
	public ActionAbility computeCapacity(AbstractAction action)
	{	return modulationManager.computeCapacity(action);
	}
*/	
/*	public StateAbility computeCapacity(String name)
	{	return modulationManager.computeCapacity(name);
	}
*/
/*	public void combineActorModulation(SpecificAction specificAction, ActionAbility ability)
	{	modulationManager.combineActorModulation(specificAction,ability);
	}
*/	
/*	public void combineTargetModulation(SpecificAction specificAction, ActionAbility ability)
	{	modulationManager.combineTargetModulation(specificAction,ability);
	}
*/	
/*	public void combineThirdModulation(SpecificAction specificAction, ActionAbility ability)
	{	modulationManager.combineThirdModulation(specificAction,ability);
	}
*/	
/*	public void combineStateModulation(String name, StateAbility ability)
	{	modulationManager.combineStateModulation(name,ability);
	}
*/	
/*	public ArrayList<AbstractAbility> getModulationAbilities()
	{	return modulationManager.getModulationStateAbilities();	
	}
*/	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected TrajectoryManager trajectoryManager;

	public void setTrajectoryManager(TrajectoryManager trajectoryManager)
	{	this.trajectoryManager = trajectoryManager;
	}
	
	public Direction getActualDirection()
	{	return trajectoryManager.getActualDirection();	
	}
	
	public double getCurrentPosX()
	{	return trajectoryManager.getCurrentPosX();	
	}
	public void setCurrentPosX(double positionX)
	{	trajectoryManager.setCurrentPosX(positionX);
	}
	
	public double getCurrentPosY()
	{	return trajectoryManager.getCurrentPosY();	
	}
	
	public void setCurrentPosY(double positionY)
	{	trajectoryManager.setCurrentPosY(positionY);
	}
	
	public double getCurrentPosZ()
	{	return trajectoryManager.getCurrentPosZ();
	}
	public void setCurrentPosZ(double positionZ)
	{	trajectoryManager.setCurrentPosZ(positionZ);
	}
	
	public double getTrajectoryTotalDuration()
	{	return trajectoryManager.getTotalDuration();
	}
	
	public double getTrajectoryCurrentTime()
	{	return trajectoryManager.getCurrentTime();
	}
	
	public void addIntersectedSprite(Sprite intersectedSprite)
	{	trajectoryManager.addIntersectedSprite(intersectedSprite);
	}
	
	public void removeIntersectedSprite(Sprite intersectedSprite)
	{	trajectoryManager.removeIntersectedSprite(intersectedSprite);
	}
	
	public void addCollidedSprite(Sprite collidedSprite)
	{	trajectoryManager.addCollidedSprite(collidedSprite);
	}
	
	public void removeCollidedSprite(Sprite collidedSprite)
	{	trajectoryManager.removeCollidedSprite(collidedSprite);
	}
	
	public boolean isColliding()
	{	return trajectoryManager.isColliding();
	}
	
	public boolean isCollidingSprite(Sprite sprite)
	{	return trajectoryManager.isCollidingSprite(sprite);
	}
	
	public boolean isIntersectingSprite(Sprite sprite)
	{	return trajectoryManager.isIntersectingSprite(sprite);
	}
	
	public boolean isOnGround()
	{	return trajectoryManager.isOnGround();		
	}
	
	public void center()
	{	double posX = 0;
		double posY = 0;
		
		if(tile!=null)
		{	posX = tile.getPosX();
			setCurrentPosX(posX);
			posY = tile.getPosY();
			setCurrentPosY(posY);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractPlayer player;

	public AbstractPlayer getPlayer()
	{	return player;
	}
	
	public void setPlayer(AbstractPlayer player)
	{	this.player = player;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getLoopTime()
	{	return RoundVariables.loop.getTotalGameTime();		
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	public void enterRound(Direction dir)
	{	eventManager.enterRound(dir);		
	}
	
	public void enterRound()
	{	eventManager.enterRound(Direction.NONE);
	}
*/	
}