package org.totalboumboum.engine.container.tile;

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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.event.AbstractEvent;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.StepImage;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.game.round.RoundVariables;

/**
 * This class represents a tile
 * of the level.
 * 
 * @author Vincent Labatut
 */
public class Tile
{	
	/**
	 * Creates a tile using the specified data.
	 * 
	 * @param level
	 * 		Level containing this tile.
	 * @param row
	 * 		Row of this tile in the level.
	 * @param col
	 * 		Column of this tile in the level.
	 * @param posX
	 * 		X position of the tile in pixels. 
	 * @param posY
	 * 		Y position of the tile in pixels. 
	 */
	public Tile(Level level,int row, int col, double posX, double posY)
	{	this.level = level;
		this.posX = posX;
		this.posY = posY;
		this.row = row;
		this.col = col;
		//
		bombs = new ArrayList<Bomb>(0);
		heroes = new ArrayList<Hero>(0);
		fires = new ArrayList<Fire>(0);
		blocks = new ArrayList<Block>(0);
		items = new ArrayList<Item>(0);
		floors = new ArrayList<Floor>(0);
		
		// init draw switches (all true)
		drawSwitches = new HashMap<Role, Boolean>();
		for(Role role: Role.values())
			drawSwitches.put(role,true);
	}
		
    /////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates all sprites having
	 * the specified role.
	 * 
	 * @param role
	 * 		Role of the targetted sprites.
	 */
	public void updateSprites(Role role)
	{	if(role==Role.BLOCK)
			updateSprites(blocks);
		else if(role==Role.BOMB)
			updateSprites(bombs);
		else if(role==Role.FIRE)
			updateSprites(fires);
		else if(role==Role.FLOOR)
			updateSprites(floors);
		else if(role==Role.HERO)
			updateSprites(heroes);
		else if(role==Role.ITEM)
			updateSprites(items);
	}
	
	/**
	 * Updates the specified list of sprites.
	 * 
	 * @param <T>
	 * 		Type of sprite to be updated.
	 * @param sprites
	 * 		List of sprites to be updated.
	 */
	private <T extends Sprite> void updateSprites(List<T> sprites)
	{	int i=0;
		while(i<sprites.size())
		{	T temp = sprites.get(i);
			temp.update();
			// only increment if the current sprite didn't leave the tile
			if(i<sprites.size() && sprites.get(i)==temp)
				i++;
		}
	}

    /////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates, for each role, if this kind of sprite should be drawn */
	private Map<Role,Boolean> drawSwitches;

	/**
	 * Changes the flag regarding the drawing
	 * of the specified kind of sprite.
	 * 
	 * @param role
	 * 		Concerned kind of sprite.
	 * @param value
	 * 		Whether or not it should be drawn.
	 */
	public void setDrawSwitch(Role role, boolean value)
	{	drawSwitches.put(role,value);
	}
	
	/**
	 * Draws the sprites corresponding to the
	 * specified parameters.
	 * 
	 * @param role
	 * 		Role of the types to be drawn.
	 * @param g
	 * 		Object used for drawing.
	 * @param flat
	 * 		Flat sprites (eg. items).
	 * @param onGround
	 * 		Sprites currently on-ground.
	 * @param shadow
	 * 		Whether the shadow should be drawn (or not).
	 */
	public void drawSprites(Role role, Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	if(role==Role.BLOCK)
			drawSprites(blocks,g,flat,onGround,shadow);
		else if(role==Role.BOMB)
			drawSprites(bombs,g,flat,onGround,shadow);
		else if(role==Role.FIRE)
			drawSprites(fires,g,flat,onGround,shadow);
		else if(role==Role.FLOOR)
			drawSprites(floors,g,flat,onGround,shadow);
		else if(role==Role.HERO)
			drawSprites(heroes,g,flat,onGround,shadow);
		else if(role==Role.ITEM)
			drawSprites(items,g,flat,onGround,shadow);
	}

	/**
	 * Draws the sprites corresponding to the
	 * specified parameters.
	 * 
	 * @param <T>
	 * 		Type of the sprites to be drawn.
	 * @param sprites
	 * 		List of the sprites to be drawn.
	 * @param g
	 * 		Object used for drawing.
	 * @param flat
	 * 		Flat sprites (eg. items).
	 * @param onGround
	 * 		Sprites currently on-ground.
	 * @param shadow
	 * 		Whether the shadow should be drawn (or not).
	 */
	private <T extends Sprite> void drawSprites(List<T> sprites, Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	for(int i=0;i<sprites.size();i++)
		{	T tempS = sprites.get(i);
			AbstractAbility temp = tempS.modulateStateAbility(StateAbilityName.SPRITE_FLAT);
			if((((temp.isActive())==flat) && (tempS.isOnGround()==onGround))
				|| ((tempS.isOnGround()==false) && (onGround==false)))
				if(shadow)
					drawShadow(g,tempS);
				else
					drawSprite(g,tempS);
		}
	}

	/**
	 * Draws the sprites corresponding to the 
	 * specified parameters.
	 * 
	 * @param g
	 * 		Object used for drawing. 
	 * @param flat
	 * 		Flat sprites (ex. floors)
	 * @param onGround
	 * 		Currently on-ground sprites.
	 * @param shadow
	 * 		Whether the shadow should be drawn or not.
	 */
	public void drawSelection(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	// floor
		drawSprites(Role.FLOOR,g,flat,onGround,shadow);
		// item
		drawSprites(Role.ITEM,g,flat,onGround,shadow);
		// block
		drawSprites(Role.BLOCK,g,flat,onGround,shadow);
		// fires
		drawSprites(Role.FIRE,g,flat,onGround,shadow);
		// bombs
		drawSprites(Role.BOMB,g,flat,onGround,shadow);
		// heroes
		drawSprites(Role.HERO,g,flat,onGround,shadow);
	}	

	/**
	 * Draws a sprite without its shadow.
	 * 
	 * @param g
	 * 		Object used for drawing. 
	 * @param s 
	 * 		Sprite to be drawn.
	 */
	private void drawSprite(Graphics g, Sprite s)
	{	Role role = s.getRole();
		if(drawSwitches.get(role))
		{	List<StepImage> images =  s.getCurrentImages();
//if(images.size()>1)
//	System.err.println("ligne");
			for(int j=0;j<images.size();j++)
			{	StepImage stepImage = images.get(j);
				BufferedImage image = stepImage.getImage();
				double xShift = stepImage.getXShift();
				double yShift = stepImage.getYShift();
				double pX = s.getCurrentPosX() + xShift;
				double pY = s.getCurrentPosY() + yShift;
				double pZ = s.getCurrentPosZ();
				pX = pX - ((double)image.getWidth())/2;
				pY = pY - image.getHeight() + RoundVariables.scaledTileDimension/2;
				//
				List<Double> listX = new ArrayList<Double>(0);
				List<Double> listY = new ArrayList<Double>(0);
				listX.add(pX);
				listY.add(pY);
				// déborde à gauche
				if(!level.isInsidePositionX(pX))
				{	double temp = level.normalizePositionX(pX);
					listX.add(temp);
					listY.add(pY);			
				}
				// déborde à droite
				if(!level.isInsidePositionX(pX+image.getWidth()))
				{	double temp = level.normalizePositionX(pX+image.getWidth())-image.getWidth();
					listX.add(temp);
					listY.add(pY);			
				}
				// déborde en haut
				if(!level.isInsidePositionY(pY))
				{	double temp = level.normalizePositionY(pY);
					int li = listX.size();
					for(int i=0;i<li;i++)
					{	listX.add(listX.get(i));
						listY.add(temp);			
					}
				}
				// déborde en bas
				if(!level.isInsidePositionY(pY+image.getHeight()))
				{	double temp = level.normalizePositionY(pY+image.getHeight())-image.getHeight();
					int li = listX.size();
					for(int i=0;i<li;i++)
					{	listX.add(listX.get(i));
						listY.add(temp);			
					}
				}
				for(int i=0;i<listX.size();i++)
					g.drawImage(image, new Double(listX.get(i)).intValue(), new Double(listY.get(i)-pZ).intValue(), null);		
	//				g.drawImage(image, new Long(Math.round(listX.get(i))).intValue(), new Long(Math.round(listY.get(i)-pZ)).intValue(), null);		
			}
		}
	
		// bound sprites
		if(s.hasBoundSprite())
		{	Iterator<Sprite> i = s.getBoundSprites();
			while(i.hasNext())
			{	Sprite temp = i.next();
				drawSprite(g,temp);
			}			
		}
	}

	/**
	 * Draws a sprite shadow, but not the sprite itself.
	 * 
	 * @param g
	 * 		Object used for drawing. 
	 * @param s 
	 * 		Sprite whose shadow should be drawn.
	 */
	private void drawShadow(Graphics g, Sprite s)
	{	Role role = s.getRole();
		if(drawSwitches.get(role))
		{	StepImage stepImage = s.getShadow();
			if(stepImage!=null)
			{	BufferedImage image = stepImage.getImage();
				double pX = s.getCurrentPosX()+stepImage.getXShift();
				double pY = s.getCurrentPosY()+stepImage.getYShift();
				pX = pX - ((double)image.getWidth())/2;
				pY = pY - image.getHeight() + RoundVariables.scaledTileDimension/2;
				//
				List<Double> listX = new ArrayList<Double>(0);
				List<Double> listY = new ArrayList<Double>(0);
				listX.add(pX);
				listY.add(pY);
				// déborde à gauche
				if(!level.isInsidePositionX(pX))
				{	double temp = level.normalizePositionX(pX);
					listX.add(temp);
					listY.add(pY);			
				}
				// déborde à droite
				if(!level.isInsidePositionX(pX+image.getWidth()))
				{	double temp = level.normalizePositionX(pX+image.getWidth())-image.getWidth();
					listX.add(temp);
					listY.add(pY);			
				}
				// déborde en haut
				if(!level.isInsidePositionY(pY))
				{	double temp = level.normalizePositionY(pY);
					int li = listX.size();
					for(int i=0;i<li;i++)
					{	listX.add(listX.get(i));
						listY.add(temp);			
					}
				}
				// déborde en bas
				if(!level.isInsidePositionY(pY+image.getHeight()))
				{	double temp = level.normalizePositionY(pY+image.getHeight())-image.getHeight();
					int li = listX.size();
					for(int i=0;i<li;i++)
					{	listX.add(listX.get(i));
						listY.add(temp);			
					}
				}
				for(int i=0;i<listX.size();i++)
					g.drawImage(image, new Double(listX.get(i)).intValue(), new Double(listY.get(i)).intValue(), null);		
			}
		}
	}
		
	/////////////////////////////////////////////////////////////////
	// HERO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Heroes contained in this tile */
	private List<Hero> heroes;

	/**
	 * Adds a new hero to this tile.
	 * 
	 * @param hero
	 * 		Sprite to be added.
	 */
	private void addHero(Hero hero)
	{	heroes.add(hero);		
//		hero.setTile(this);
//		level.addSprite(hero);
	}
	
	/**
	 * Adds the specified hero from this tile.
	 * 
	 * @param hero
	 * 		Sprite to be removed.
	 */
	private void removeHero(Hero hero)
	{	heroes.remove(hero);	
	}

	/**
	 * Returns the list of all heroes
	 * contained in this tile.
	 *  
	 * @return
	 * 		List of all heroes in this tile.
	 */
	public List<Hero> getHeroes()
	{	return heroes;
	}

    /////////////////////////////////////////////////////////////////
	// BOMB					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Bombs contained in this tile */
	private List<Bomb> bombs;

	/**
	 * Adds a new bomb to this tile.
	 * 
	 * @param bomb
	 * 		Sprite to be added.
	 */
	private void addBomb(Bomb bomb)
	{	bombs.add(bomb);		
//		bomb.setTile(this);
//		level.addSprite(bomb);
	}
	
	/**
	 * Adds the specified bomb from this tile.
	 * 
	 * @param bomb
	 * 		Sprite to be removed.
	 */
	private void removeBomb(Bomb bomb)
	{	bombs.remove(bomb);	
	}

	/**
	 * Returns the list of all bombs
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all bombs in this tile.
	 */
	public List<Bomb> getBombs()
	{	return bombs;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Fires contained in this tile */
	private List<Fire> fires;

	/**
	 * Adds a new fire to this tile.
	 * 
	 * @param fire
	 * 		Sprite to be added.
	 */
	private void addFire(Fire fire)
	{	fires.add(fire);
//		fire.setTile(this);
//		level.addSprite(fire);
	}
	
	/**
	 * Adds the specified fire from this tile.
	 * 
	 * @param fire
	 * 		Sprite to be removed.
	 */
	private void removeFire(Fire fire)
	{	fires.remove(fire);	
	}

	/**
	 * Returns the list of all fires
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all fires in this tile.
	 */
	public List<Fire> getFires()
	{	return fires;
	}

	/////////////////////////////////////////////////////////////////
	// BLOCK				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Blocks contained in this tile */
	private List<Block> blocks;

	/**
	 * Adds a new block to this tile.
	 * 
	 * @param block
	 * 		Sprite to be added.
	 */
	private void addBlock(Block block)
	{	blocks.add(block);		
//		block.setTile(this);
//		level.addSprite(block);
	}

	/**
	 * Adds the specified block from this tile.
	 * 
	 * @param block
	 * 		Sprite to be removed.
	 */
	private void removeBlock(Block block)
	{	blocks.remove(block);	
	}
	
	/**
	 * Returns the list of all blocks
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all blocks in this tile.
	 */
	public List<Block> getBlocks()
	{	return blocks;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Items contained in this tile */
	private List<Item> items;

	/**
	 * Adds a new item to this tile.
	 * 
	 * @param item
	 * 		Sprite to be added.
	 */
	private void addItem(Item item)
	{	items.add(item);		
//		item.setTile(this);
//		level.addSprite(item);
	}

	/**
	 * Adds the specified item from this tile.
	 * 
	 * @param item
	 * 		Sprite to be removed.
	 */
	private void removeItem(Item item)
	{	items.remove(item);	
	}
	
	/**
	 * Returns the list of all items
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all items in this tile.
	 */
	public List<Item> getItems()
	{	return items;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOOR				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Floors contained in this tile */
	private List<Floor> floors;

	/**
	 * Adds a new floor to this tile.
	 * 
	 * @param floor
	 * 		Sprite to be added.
	 */
	private void addFloor(Floor floor)
	{	floors.add(floor);		
//		floor.setTile(this);
//		level.addSprite(floor);
	}

	/**
	 * Adds the specified floor from this tile.
	 * 
	 * @param floor
	 * 		Sprite to be removed.
	 */
	private void removeFloor(Floor floor)
	{	floors.remove(floor);	
	}

	/**
	 * Returns the list of all floors
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all floors in this tile.
	 */
	public List<Floor> getFloors()
	{	return floors;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL SPRITES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Adds a new sprite to this tile.
	 * 
	 * @param sprite
	 * 		Sprite to add to the tile.
	 */
	public void addSprite(Sprite sprite)
	{	if(sprite instanceof Block)
			addBlock((Block) sprite);
		else if(sprite instanceof Bomb)
			addBomb((Bomb) sprite);		
		else if(sprite instanceof Fire)
			addFire((Fire) sprite);		
		else if(sprite instanceof Floor)
			addFloor((Floor) sprite);
		else if(sprite instanceof Hero)
			addHero((Hero) sprite);
		else if(sprite instanceof Item)
			addItem((Item) sprite);
	}

	/**
	 * Remove a sprite contained in this tile.
	 * 
	 * @param sprite
	 * 		Sprite to remove from the tile.
	 */
	public void removeSprite(Sprite sprite)
	{	if(sprite instanceof Block)
			removeBlock((Block) sprite);
		else if(sprite instanceof Bomb)
			removeBomb((Bomb) sprite);		
		else if(sprite instanceof Fire)
			removeFire((Fire) sprite);		
		else if(sprite instanceof Floor)
			removeFloor((Floor) sprite);
		else if(sprite instanceof Hero)
			removeHero((Hero) sprite);
		else if(sprite instanceof Item)
			removeItem((Item) sprite);
	}

	/**
	 * Returns the list of all sprites
	 * contained in this tile.
	 * 
	 * @return
	 * 		List of all sprites.
	 */
	public List<Sprite> getSprites()
	{	List<Sprite> result = new ArrayList<Sprite>();
		// floor
		{	Iterator<Floor> i = floors.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		// heroes
		{	Iterator<Hero> i = heroes.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		// bombs
		{	Iterator<Bomb> i = bombs.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		// fires
		{	Iterator<Fire> i = fires.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		// block
		{	Iterator<Block> i = blocks.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		// item
		{	Iterator<Item> i = items.iterator();
			while(i.hasNext())
				result.add(i.next());
		}
		return result;
	}

	/**
	 * Returns the list of all sprites
	 * contained in this tile, which have
	 * the specified role.
	 * 
	 * @param role
	 * 		Targetted role.
	 * @return
	 * 		List of all sprites.
	 */
	public List<Sprite> getSprites(Role role)
	{	List<Sprite> result = new ArrayList<Sprite>();
		// floor
		if(role==Role.FLOOR)
			result.addAll(floors);
		// heroes
		else if(role==Role.HERO)
			result.addAll(heroes);
		// bombs
		else if(role==Role.BOMB)
			result.addAll(bombs);
		// fires
		else if(role==Role.FIRE)
			result.addAll(fires);
		// block
		else if(role==Role.BLOCK)
			result.addAll(blocks);
		// item
		else if(role==Role.ITEM)
			result.addAll(items);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Spreads a game event to all the sprites
	 * contained in this tile.
	 * 
	 * @param event
	 * 		Event to be spread.
	 */
	public void spreadEvent(AbstractEvent event)
	{	Role[] roles = {Role.FLOOR,Role.HERO,Role.BOMB,Role.FIRE,Role.BLOCK,Role.ITEM}; 
		for(Role role: roles)
			spreadEvent(event,role);
	}

	/**
	 * Spreads a game event to all the sprites
	 * contained in this tile which have the
	 * specified role.
	 * 
	 * @param event
	 * 		Event to be spread.
	 * @param role 
	 * 		Targetted role.
	 */
	public void spreadEvent(AbstractEvent event, Role role)
	{	// floor
		if(role==Role.FLOOR)
			for(Sprite sprite: floors)
				sprite.processEvent(event);
		// heroes
		else if(role==Role.HERO)
			for(Sprite sprite: heroes)
				sprite.processEvent(event);
		// bombs
		else if(role==Role.BOMB)
			for(Sprite sprite: bombs)
				sprite.processEvent(event);
		// fires
		else if(role==Role.FIRE)
			for(Sprite sprite: fires)
				sprite.processEvent(event);
		// block
		else if(role==Role.BLOCK)
			for(Sprite sprite: blocks)
				sprite.processEvent(event);
		// item
		else if(role==Role.ITEM)
			for(Sprite sprite: items)
				sprite.processEvent(event);
	}

    /////////////////////////////////////////////////////////////////
	// MATRIX POSITION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Row index of this tile */ 
	private int row;
	/** Column index of this tile */ 
	private int col;
	
	/**
	 * Returns the row index of this tile.
	 * 
	 * @return
	 * 		Index of the row of this tile.
	 */
	public int getRow()
	{	return row;
	}
	
	/**
	 * Returns the column index of this tile.
	 * 
	 * @return
	 * 		Index of the column  of this tile.
	 */
	public int getCol()
	{	return col;
	}
	
	/**
	 * Returns the neighboring tile
	 * in the specified direction.
	 * 
	 * @param direction
	 * 		Direction to be considered.
	 * @return
	 * 		The corresponding heighbor.
	 */
	public Tile getNeighbor(Direction direction)
	{	Tile result;
		result = level.getNeighborTile(row,col,direction);
		return result;
	}
	
	/**
	 * Checks if the specified tile is
	 * a neighbor of this tile.
	 * 
	 * @param tile
	 * 		Tile to be checked.
	 * @return
	 * 		{@code true} iff the specified tile is a neighbor of this tile.
	 */
	public boolean isNeighbor(Tile tile)
	{	Tile nd = getNeighbor(Direction.DOWN);
		Tile nl = getNeighbor(Direction.LEFT);
		Tile nr = getNeighbor(Direction.RIGHT);
		Tile nu = getNeighbor(Direction.UP);
		boolean result = tile==nd || tile==nl || tile==nr || tile==nu;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL POSITION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** x position of the center of this tile */
	private double posX;
	/** y position of the center of this tile */
	private double posY;
	
	/**
	 * Returns the x position of the center of this tile.
	 * 
	 * @return
	 * 		x position of this tile.
	 */
	public double getPosX()
	{	return posX;
	}
	
	/**
	 * Returns the y position of the center of this tile.
	 * 
	 * @return
	 * 		y position of this tile.
	 */
	public double getPosY()
	{	return posY;
	}

	/**
	 * Checks if the specified location
	 * belongs to this tile.
	 * 
	 * @param x
	 * 		x position of the location to be tested.
	 * @param y
	 * 		y position of the location to be tested.
	 * @return
	 * 		{@code true} iff the location belongs to this tile.
	 */
	public boolean containsPoint(double x, double y)
	{	boolean result;
result = level.getTile(x,y)==this;
/*	
		result = CalculusTools.isRelativelyGreaterOrEqualThan(x,posX-getDimension()/2);
		result = result && CalculusTools.isRelativelySmallerThan(x,posX+getDimension()/2); 
		result = result && CalculusTools.isRelativelyGreaterOrEqualThan(y,posY-getDimension()/2); 
		result = result && CalculusTools.isRelativelySmallerThan(y,posY+getDimension()/2);
*/		 
		return result;
	}
	
    /////////////////////////////////////////////////////////////////
	// LEVEL		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Level containing this tile */
	private Level level;
	
    /////////////////////////////////////////////////////////////////
	// STRING		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		result = result+"("+row+","+col+")";
		result = result+"("+posX+","+posY+")";
		return result;
	}
}
