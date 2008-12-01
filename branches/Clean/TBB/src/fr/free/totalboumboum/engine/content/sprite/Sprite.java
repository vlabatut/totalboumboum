package fr.free.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.AbstractEvent;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.permission.ActorPermission;
import fr.free.totalboumboum.engine.content.feature.permission.StateModulation;
import fr.free.totalboumboum.engine.content.feature.permission.TargetPermission;
import fr.free.totalboumboum.engine.content.feature.permission.ThirdPermission;
import fr.free.totalboumboum.engine.content.manager.AbilityManager;
import fr.free.totalboumboum.engine.content.manager.AnimeManager;
import fr.free.totalboumboum.engine.content.manager.BombsetManager;
import fr.free.totalboumboum.engine.content.manager.ControlManager;
import fr.free.totalboumboum.engine.content.manager.DelayManager;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.manager.ExplosionManager;
import fr.free.totalboumboum.engine.content.manager.ItemManager;
import fr.free.totalboumboum.engine.content.manager.PermissionManager;
import fr.free.totalboumboum.engine.content.manager.TrajectoryManager;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.control.ControlCode;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.statistics.StatisticEvent;

/* 
 * Sprite possédant un status :
 * softwall, hero, bomb, item...
 */

public abstract class Sprite
{	protected Level level;
	// managers
	protected AbilityManager abilityManager;
	protected AnimeManager animeManager;
	protected BombsetManager bombsetManager;
	protected ControlManager controlManager;
	protected DelayManager delayManager;
	protected EventManager eventManager;
	protected ExplosionManager explosionManager;
	protected ItemManager itemManager;
	protected PermissionManager permissionManager;
	protected TrajectoryManager trajectoryManager;
	//
	protected String currentGesture;
	protected Tile tile;
	/** le sprite ne doit pas être mise à jour, mais il existe encore */
	protected Sprite hiddenSprite;
	/** le sprite est terminé, mais le Tile ne l'a pas encore effectivement supprimé*/
	protected boolean ended;
	protected boolean toBeRemovedFromTile;
	protected Sprite toBeRemovedFromSprite;
	protected Sprite boundToSprite;
	protected Sprite owner;
	//
	protected ArrayList<Sprite> boundSprites;
	//
	protected double speedCoeff = 1;
	// 
	private String name;
	
	public Sprite(Level level)
	{	ended = false;
		hiddenSprite = null;
		boundSprites = new ArrayList<Sprite>();
		toBeRemovedFromTile = false;
		toBeRemovedFromSprite = null;
		boundToSprite = null;
		this.level = level;
		currentGesture = null;
	}
	
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		if(tile!=null)
			result = result+"("+tile.getLine()+","+tile.getCol()+")";
		result = result+"("+getCurrentPosX()+","+getCurrentPosY()+","+getCurrentPosZ()+")";
		return result;
	}
	
	public Sprite getOwner()
	{	return owner;
	}
	public void setOwner(Sprite owner)
	{	this.owner = owner;
	}

	public Sprite getBoundToSprite()
	{	return boundToSprite;
	}
	public boolean isBoundToSprite()
	{	return boundToSprite!=null;
	}
	public void setBoundToSprite(Sprite boundToSprite)
	{	if(!ended)
		{	// traitement seulement si le nouveau boundToSprite lié est différent de l'ancien
			if(this.boundToSprite!=boundToSprite)
			{	// on met à jour le trajectoryManager
				trajectoryManager.setBoundToSprite(boundToSprite);
				// s'il n'y a pas d'ancien boundToSprite : on déconnecte ce sprite de sa tile
				if(this.boundToSprite==null)
					setToBeRemovedFromTile(true);
				// s'il y a un ancien boundToSprite : on déconnecte ce sprite de ce boundToSprite 
				else
					setToBeRemovedFromSprite(this.boundToSprite);
				// s'il n'y a pas de nouveau boundToSprite : on connecte ce sprite à une Tile
				if(boundToSprite==null)
					getLevel().getTile(getCurrentPosX(), getCurrentPosY()).addSprite(this);
				// s'il y a un nouveau boundToSprite : on connecte ce sprite à ce boundToSprite
				else
					boundToSprite.addBoundSprite(this);
				this.boundToSprite = boundToSprite;
			}
		}	
	}
	
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
	
	public Sprite getHiddenSprite()
	{	return hiddenSprite;
	}
	public void setHiddenSprite(Sprite hiddenSprite)
	{	this.hiddenSprite = hiddenSprite;
	}
	
	/*
	 * change le gesture, la direction de l'animation et la direction des touches
	 * l'animation n'est réinitialisée que si le gesture est modifié
	 */
	public void setGesture(String gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	if(!ended)
		{	currentGesture = gesture;
			permissionManager.setGesture(gesture,spriteDirection);
			animeManager.setGesture(gesture,spriteDirection,reinit,forcedDuration);
			trajectoryManager.setGesture(gesture,spriteDirection,controlDirection,reinit,forcedDuration);
		}
	}
	public void setGesture(String gesture, Direction spriteDirection, Direction controlDirection, boolean reinit)
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
	public void initGesture()
	{	eventManager.initGesture();
	}
	public String getCurrentGesture()
	{	return currentGesture;
	}

	// update the sprite
	public void update()
	{	if(!ended)
		{	abilityManager.update();
			bombsetManager.update();
			controlManager.update();
//			eventManager.update();
			delayManager.update();
			itemManager.update();
			animeManager.update();
			trajectoryManager.update();
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
	}
	
	public void endSprite()
	{	ended = true;
		toBeRemovedFromTile = true;
	}
	public boolean isEnded()
	{	return ended;	
	}

	public void setToBeRemovedFromTile(boolean toBeRemovedFromTile)
	{	this.toBeRemovedFromTile = toBeRemovedFromTile;	
	}
	public boolean isToBeRemovedFromTile()
	{	return toBeRemovedFromTile;	
	}
	
	public void setToBeRemovedFromSprite(Sprite toBeRemovedFromSprite)
	{	this.toBeRemovedFromSprite = toBeRemovedFromSprite;	
	}
	public Sprite getToBeRemovedFromSprite()
	{	return toBeRemovedFromSprite;	
	}

	public Tile getTile()
	{	return tile;
	}
	public void setTile(Tile tile)
	{	this.tile = tile;
		setToBeRemovedFromTile(false);
		String eventName = EngineEvent.TILE_LOWENTER;
		if(!isOnGround())
			eventName = EngineEvent.TILE_HIGHENTER;
		EngineEvent event = new EngineEvent(eventName,this,null,trajectoryManager.getActualDirection());
		tile.spreadEvent(event);
	}
	
	public Level getLevel()
	{	return level;
	}
	
	public Loop getLoop()
	{	return level.getLoop();	
	}
	
	public void spreadEvent(AbstractEvent e)
	{	Iterator<Sprite> i = boundSprites.iterator();
		while(i.hasNext())
			i.next().processEvent(e);		
	}

	/**
	 * S'il y  a un boundToSprite, son speedCoeff est renvoyé.
	 * Sinon, c'est le produit entre le speedCoeff du sprite
	 * et celle du jeu.
	 * @return
	 */
	public double getSpeedCoeff()
	{	double result;
		if(boundToSprite==null)
			result = speedCoeff*Configuration.getEngineConfiguration().getSpeedCoeff();
		else
			result = boundToSprite.getSpeedCoeff();
		return result;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}

	// ABILITY MANAGER		/////////////////////////////////////////////
	public void setAbilityManager(AbilityManager abilityManager)
	{	this.abilityManager = abilityManager;
	}
	public StateAbility getAbility(StateAbility ability)
	{	return abilityManager.getAbility(ability);
	}
	public StateAbility getAbility(String name)
	{	return abilityManager.getAbility(name);
	}
	public ActionAbility getAbility(ActionAbility ability)
	{	return abilityManager.getAbility(ability);
	}
	public ActionAbility getAbility(AbstractAction action)
	{	return abilityManager.getAbility(action);
	}
	public void decrementUse(AbstractAbility ability, int delta)
	{	abilityManager.decrementUse(ability, delta);
	}
	
	// ANIME MANAGER		/////////////////////////////////////////////
	public void setAnimeManager(AnimeManager animeManager)
	{	this.animeManager = animeManager;
	}
	public BufferedImage getCurrentImage()
	{	return animeManager.getCurrentImage();	
	}
	public boolean hasShadow()
	{	return animeManager.hasShadow();
	}	
	public BufferedImage getShadow()
	{	return animeManager.getShadow();	
	}
	public double getXShift()
	{	return animeManager.getXShift();
	}
	public double getYShift()
	{	return animeManager.getYShift();
	}
	public double getShadowXShift()
	{	return animeManager.getShadowXShift();
	}
	public double getShadowYShift()
	{	return animeManager.getShadowYShift();
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
	public PredefinedColor getColor()
	{	return animeManager.getColor();
	}

	// BOMBSET MANAGER		/////////////////////////////////////////////
	public void setBombsetManager(BombsetManager bombsetManager)
	{	this.bombsetManager = bombsetManager;
	}
	public BombsetManager getBombsetManager()
	{	return bombsetManager;	
	}
	public Bomb makeBomb()
	{	return bombsetManager.makeBomb();
	}
	public void dropBomb(Bomb bomb)
	{	if(!ended)
			bombsetManager.dropBomb(bomb);
	}
	public void triggerBomb()
	{	if(!ended)
			bombsetManager.triggerBomb();
	}
	public LinkedList<Bomb> getDroppedBombs()
	{	return bombsetManager.getDroppedBombs();		
	}

	// CONTROL MANAGER		/////////////////////////////////////////////
	public void setControlManager(ControlManager controlManager)
	{	this.controlManager = controlManager;
	}
	public void setControlSettings(ControlSettings controlSettings)
	{	controlManager.setControlSettings(controlSettings);
	}
	public void putControlCode(ControlCode controlCode)
	{	if(!ended)
			controlManager.putControlCode(controlCode);
	}
	public void putControlEvent(ControlEvent controlEvent)
	{	if(!ended)
			controlManager.putControlEvent(controlEvent);
	}

	// DELAY MANAGER		/////////////////////////////////////////////
	public void setDelayManager(DelayManager delayManager)
	{	this.delayManager = delayManager;
	}
	public void addDelay(String name,double duration)
	{	delayManager.addDelay(name,duration);
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
	
	// EVENT MANAGER		/////////////////////////////////////////////
	public void setEventManager(EventManager eventManager)
	{	this.eventManager = eventManager;
	}
	public void processEvent(AbstractEvent event)
	{	if(!ended)
		{	if(event instanceof ActionEvent)
				eventManager.processEvent((ActionEvent)event);
			else if(event instanceof ControlEvent)
				eventManager.processEvent((ControlEvent)event);
			else if(event instanceof EngineEvent)
				eventManager.processEvent((EngineEvent)event);
		}
	}

	// EXPLOSION MANAGER	/////////////////////////////////////////////
	public void setExplosionManager(ExplosionManager explosionManager)
	{	this.explosionManager = explosionManager;
	}
	public void putExplosion()
	{	if(!ended)
			explosionManager.putExplosion();	
	}
	public void setFlameRange(int flameRange)
	{	explosionManager.setFlameRange(flameRange);		
	}	
	public int getFlameRange()
	{	return explosionManager.getFlameRange();		
	}	
	
	// ITEM MANAGER		/////////////////////////////////////////////
	public void setItemManager(ItemManager itemManager)
	{	this.itemManager = itemManager;
	}
	public void addItem(Item item)
	{	itemManager.addItem(item);	
	}
	public void addInitialItem(Item item)
	{	itemManager.addInitialItem(item);	
	}
	public Item dropRandomItem()
	{	return itemManager.dropRandomItem();	
	}
	public ArrayList<Item> dropAllItems()
	{	return itemManager.dropAllItems();
	}
	public ArrayList<AbstractAbility> getItemAbilities()
	{	return itemManager.getItemAbilities();	
	}

	// PERMISSION MANAGER		/////////////////////////////////////////////
	public void setPermissionManager(PermissionManager permissionManager)
	{	this.permissionManager = permissionManager;
	}
	public StateModulation getModulation(StateModulation modulation)
	{	return permissionManager.getModulation(modulation);
	}
	public ActorPermission getActorPermission(SpecificAction action)
	{	return permissionManager.getActorPermission(action);
	}
	public TargetPermission getTargetPermission(SpecificAction action)
	{	return permissionManager.getTargetPermission(action);
	}
	public ThirdPermission getThirdPermission(SpecificAction action)
	{	return permissionManager.getThirdPermission(action);
	}
	public StateAbility computeAbility(String name)
	{	return permissionManager.computeAbility(name);
	}
	public ActionAbility computeAbility(SpecificAction action)
	{	return permissionManager.computeAbility(action);
	}
	public ActionAbility computeCapacity(AbstractAction action)
	{	return permissionManager.computeCapacity(action);
	}
	public StateAbility computeCapacity(String name)
	{	return permissionManager.computeCapacity(name);
	}
	public void combineActorPermission(SpecificAction specificAction, ActionAbility ability)
	{	permissionManager.combineActorPermission(specificAction,ability);
	}
	public void combineTargetPermission(SpecificAction specificAction, ActionAbility ability)
	{	permissionManager.combineTargetPermission(specificAction,ability);
	}
	public void combineThirdPermission(SpecificAction specificAction, ActionAbility ability)
	{	permissionManager.combineThirdPermission(specificAction,ability);
	}
	public void combineStateModulation(String name, StateAbility ability)
	{	permissionManager.combineStateModulation(name,ability);
	}
	public ArrayList<AbstractAbility> getModulationAbilities()
	{	return permissionManager.getModulationAbilities();	
	}
	
	// TRAJECTORY MANAGER	/////////////////////////////////////////////
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
	public double getTrajectoryTotalDuration()
	{	return trajectoryManager.getTotalDuration();
	}
	public void addIntersectedSprite(Sprite intersectedSprite)
	{	trajectoryManager.addIntersectedSprite(intersectedSprite);
	}
	public void removeIntersectedSprite(Sprite intersectedSprite)
	{	trajectoryManager.removeIntersectedSprite(intersectedSprite);
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
	{	double posX = tile.getPosX();
		double posY = tile.getPosY();
		setCurrentPosX(posX);
		setCurrentPosY(posY);
	}
	
	// PLAYER	/////////////////////////////////////////////
	private Player player;

	public Player getPlayer()
	{	return player;
	}
	public void setPlayer(Player player)
	{	this.player = player;
	}
	
	
	public long getLoopTime()
	{	return getLoop().getTotalTime();		
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	getLoop().addStatisticEvent(event);
	}

	
	
	
	
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// bound sprites
			{	Iterator<Sprite> it = boundSprites.iterator();
				while(it.hasNext())
				{	Sprite temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// managers
			abilityManager.finish();
			abilityManager = null;
			animeManager.finish();
			animeManager = null;
			bombsetManager.finish();
			bombsetManager = null;
			controlManager.finish();
			controlManager = null;
			delayManager.finish();
			delayManager = null;
			eventManager.finish();
			eventManager = null;
			explosionManager.finish();
			explosionManager = null;
			itemManager.finish();
			itemManager = null;
			permissionManager.finish();
			permissionManager = null;
			trajectoryManager.finish();
			trajectoryManager = null;
			// hidden sprite
			if(hiddenSprite!=null)
			{	hiddenSprite.finish();
				hiddenSprite = null;
			}
			// misc
			boundToSprite = null;
			hiddenSprite = null;
			toBeRemovedFromSprite = null;
			owner = null;
			player = null;
			level = null;
			name = null;
		}
	}
	
	public void setName(String name)
	{	this.name = name; 	
	}
	public String getName()
	{	return name;	
	}
}