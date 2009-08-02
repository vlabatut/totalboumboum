package fr.free.totalboumboum.engine.content.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.Circumstance;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.AbstractEvent;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.Gesture;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.OtherModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.TargetModulation;
import fr.free.totalboumboum.engine.content.feature.gesture.modulation.ThirdModulation;
import fr.free.totalboumboum.engine.content.manager.ability.AbilityManager;
import fr.free.totalboumboum.engine.content.manager.anime.AnimeManager;
import fr.free.totalboumboum.engine.content.manager.bombset.BombsetManager;
import fr.free.totalboumboum.engine.content.manager.control.ControlManager;
import fr.free.totalboumboum.engine.content.manager.delay.DelayManager;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.manager.explosion.ExplosionManager;
import fr.free.totalboumboum.engine.content.manager.item.ItemManager;
import fr.free.totalboumboum.engine.content.manager.modulation.ModulationManager;
import fr.free.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.control.ControlCode;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.statistics.StatisticEvent;

/* 
 * Sprite possédant un status :
 * softwall, hero, bomb, item...
 */

public abstract class Sprite
{	
	public Sprite()
	{	ended = false;
		boundSprites = new ArrayList<Sprite>();
		toBeRemovedFromTile = false;
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

	public void setGesturePack(GesturePack gesturePack)
	{	this.gesturePack = gesturePack;	
	}
	
	/*
	 * change le gesture, la direction de l'animation et la direction des touches
	 * l'animation n'est réinitialisée que si le gesture est modifié
	 */
	public void setGesture(GestureName gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	if(!ended)
		{	currentGesture = gesturePack.getGesture(gesture);
			modulationManager.updateGesture(currentGesture,spriteDirection);
			animeManager.updateGesture(currentGesture,spriteDirection,reinit,forcedDuration);
			trajectoryManager.updateGesture(currentGesture,spriteDirection,controlDirection,reinit,forcedDuration);
		}
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
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		if(tile!=null)
			result = result+"("+tile.getLine()+","+tile.getCol()+")";
		result = result+"("+getCurrentPosX()+","+getCurrentPosY()+","+getCurrentPosZ()+")";
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite owner;
	
	public Sprite getOwner()
	{	return owner;
		//NOTE à modifier pour recherche récursivement l'owner final (mais peut être est-ce déjà fait ailleurs)
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
					GameVariables.level.getTile(getCurrentPosX(), getCurrentPosY()).addSprite(this);
				// s'il y a un nouveau boundToSprite : on connecte ce sprite à ce boundToSprite
				else
					boundToSprite.addBoundSprite(this);
				this.boundToSprite = boundToSprite;
			}
		}	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOUND SPRITES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArrayList<Sprite> boundSprites;
	
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
	{	if(!ended)
		{	abilityManager.update();
			bombsetManager.update();
			controlManager.update();
//			eventManager.update();
			delayManager.update();
			itemManager.update();
			animeManager.update();
			trajectoryManager.update();
			/*
			 * NOTE : il est important que le trajectoryManager soit updaté en dernier
			 * comme ça, un changement de case arrive après avoir traité tous les évènements
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
	}
	
	/////////////////////////////////////////////////////////////////
	// ENDED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le sprite est terminé, mais le Tile ne l'a pas encore effectivement supprimé*/
	protected boolean ended;
	
	public void endSprite()
	{	ended = true;
		toBeRemovedFromTile = true;
	}
	
	public boolean isEnded()
	{	return ended;	
	}

	/////////////////////////////////////////////////////////////////
	// REMOVE FROM TILE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean toBeRemovedFromTile;
	
	public void setToBeRemovedFromTile(boolean toBeRemovedFromTile)
	{	this.toBeRemovedFromTile = toBeRemovedFromTile;	
	}
	
	public boolean isToBeRemovedFromTile()
	{	return toBeRemovedFromTile;	
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
	
	public void setTile(Tile tile)
	{	this.tile = tile;
		setToBeRemovedFromTile(false);
		String eventName = EngineEvent.TILE_LOWENTER;
		if(!isOnGround())
			eventName = EngineEvent.TILE_HIGHENTER;
		EngineEvent event = new EngineEvent(eventName,this,null,trajectoryManager.getActualDirection());
		tile.spreadEvent(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED COEFF		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected double speedCoeff = 1;
	
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
	public void decrementUse(AbstractAbility ability, int delta)
	{	abilityManager.decrementUse(ability, delta);
	}
	
	/////////////////////////////////////////////////////////////////
	// ANIMES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected AnimeManager animeManager;

	public void setAnimeManager(AnimeManager animeManager)
	{	this.animeManager = animeManager;
	}
	
	public BufferedImage getCurrentImage()
	{	return animeManager.getCurrentImage();	
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
	{	if(!ended)
			controlManager.putControlCode(controlCode);
	}
	
	public void putControlEvent(ControlEvent controlEvent)
	{	if(!ended)
			controlManager.putControlEvent(controlEvent);
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
	{	if(!ended)
		{	if(event instanceof ActionEvent)
				eventManager.processEvent((ActionEvent)event);
			else if(event instanceof ControlEvent)
				eventManager.processEvent((ControlEvent)event);
			else if(event instanceof EngineEvent)
				eventManager.processEvent((EngineEvent)event);
		}
	}
	
	public void spreadEvent(AbstractEvent e)
	{	Iterator<Sprite> i = boundSprites.iterator();
		while(i.hasNext())
			i.next().processEvent(e);		
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	GameVariables.loop.addStatisticEvent(event);
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ExplosionManager explosionManager;

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
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ItemManager itemManager;

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
	{	double posX = tile.getPosX();
		double posY = tile.getPosY();
		setCurrentPosX(posX);
		setCurrentPosY(posY);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Player player;

	public Player getPlayer()
	{	return player;
	}
	
	public void setPlayer(Player player)
	{	this.player = player;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public long getLoopTime()
	{	return GameVariables.loop.getTotalTime();		
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
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
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
			modulationManager.finish();
			modulationManager = null;
			trajectoryManager.finish();
			trajectoryManager = null;
			// misc
			boundToSprite = null;
			toBeRemovedFromSprite = null;
			owner = null;
			player = null;
			name = null;
		}
	}	
}