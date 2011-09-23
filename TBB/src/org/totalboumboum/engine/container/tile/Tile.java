package org.totalboumboum.engine.container.tile;

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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * 
 * @author Vincent Labatut
 *
 */
public class Tile
{		
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
	}
		
    /////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	 * dessine un sprite sans son ombre
	 */
	private void drawSprite(Graphics g, Sprite s)
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

		// bound sprites
		if(s.hasBoundSprite())
		{	Iterator<Sprite> i = s.getBoundSprites();
			while(i.hasNext())
			{	Sprite temp = i.next();
				drawSprite(g,temp);
			}			
		}
	}

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
	 * trace l'ombre d'un sprite (et pas le sprite)
	 */
	private void drawShadow(Graphics g, Sprite s)
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
		
	/////////////////////////////////////////////////////////////////
	// HERO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Hero> heroes;

	private void addHero(Hero hero)
	{	heroes.add(hero);		
//		hero.setTile(this);
//		level.addSprite(hero);
	}
	
	private void removeHero(Hero hero)
	{	heroes.remove(hero);	
	}

	public List<Hero> getHeroes()
	{	return heroes;
	}

    /////////////////////////////////////////////////////////////////
	// BOMB					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Bomb> bombs;

	private void addBomb(Bomb bomb)
	{	bombs.add(bomb);		
//		bomb.setTile(this);
//		level.addSprite(bomb);
	}
	
	private void removeBomb(Bomb bomb)
	{	bombs.remove(bomb);	
	}

	public List<Bomb> getBombs()
	{	return bombs;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Fire> fires;

	private void addFire(Fire fire)
	{	fires.add(fire);
//		fire.setTile(this);
//		level.addSprite(fire);
	}
	
	private void removeFire(Fire fire)
	{	fires.remove(fire);	
	}

	public List<Fire> getFires()
	{	return fires;
	}

	/////////////////////////////////////////////////////////////////
	// BLOCK				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Block> blocks;

	private void addBlock(Block block)
	{	blocks.add(block);		
//		block.setTile(this);
//		level.addSprite(block);
	}

	private void removeBlock(Block block)
	{	blocks.remove(block);	
	}
	
	public List<Block> getBlocks()
	{	return blocks;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Item> items;

	private void addItem(Item item)
	{	items.add(item);		
//		item.setTile(this);
//		level.addSprite(item);
	}

	private void removeItem(Item item)
	{	items.remove(item);	
	}
	
	public List<Item> getItems()
	{	return items;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOOR				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Floor> floors;

	private void addFloor(Floor floor)
	{	floors.add(floor);		
//		floor.setTile(this);
//		level.addSprite(floor);
	}

	private void removeFloor(Floor floor)
	{	floors.remove(floor);	
	}

	public List<Floor> getFloors()
	{	return floors;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL SPRITES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	public void spreadEvent(AbstractEvent event)
	{	Role[] roles = {Role.FLOOR,Role.HERO,Role.BOMB,Role.FIRE,Role.BLOCK,Role.ITEM}; 
		for(Role role: roles)
			spreadEvent(event,role);
	}

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
	private int row;
	private int col;
	
	public int getRow()
	{	return row;
	}
	
	public int getCol()
	{	return col;
	}
	
	public Tile getNeighbor(Direction direction)
	{	Tile result;
		result = level.getNeighborTile(row,col,direction);
		return result;
	}
	
	public boolean isNeighbor(Tile n)
	{	Tile nd = getNeighbor(Direction.DOWN);
		Tile nl = getNeighbor(Direction.LEFT);
		Tile nr = getNeighbor(Direction.RIGHT);
		Tile nu = getNeighbor(Direction.UP);
		boolean result = n==nd || n==nl || n==nr || n==nu;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL POSITION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double posX;
	private double posY;
	
	public double getPosX()
	{	return posX;
	}
	
	public double getPosY()
	{	return posY;
	}

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
	private Level level;
	
    /////////////////////////////////////////////////////////////////
	// STRING		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		result = result+"("+row+","+col+")";
		result = result+"("+posX+","+posY+")";
		return result;
	}
}
