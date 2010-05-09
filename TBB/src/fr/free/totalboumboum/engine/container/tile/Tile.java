package fr.free.totalboumboum.engine.container.tile;

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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.event.AbstractEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class Tile
{	private int line,col;
	private double posX,posY;
	
	private Level level;
	private Floor floor;
	private Block block;
	private ArrayList<Bomb> bombs;
	private ArrayList<Hero> heroes;
	private ArrayList<Fire> fires;
	private Item item;
	
	public Tile(Level level,int line, int col, double posX, double posY, Floor floor)
	{	this.level = level;
		this.posX = posX;
		this.posY = posY;
		this.line = line;
		this.col = col;
		//
		bombs = new ArrayList<Bomb>(0);
		heroes = new ArrayList<Hero>(0);
		fires = new ArrayList<Fire>(0);
		block = null;
		item = null;
		setFloor(floor);
	}
		
	public boolean containsPoint(double x, double y)
	{	boolean result;
result = level.getTile(x, y)==this;
/*	
		result = CalculusTools.isRelativelyGreaterOrEqualThan(x,posX-getDimension()/2);
		result = result && CalculusTools.isRelativelySmallerThan(x,posX+getDimension()/2); 
		result = result && CalculusTools.isRelativelyGreaterOrEqualThan(y,posY-getDimension()/2); 
		result = result && CalculusTools.isRelativelySmallerThan(y,posY+getDimension()/2);
*/		 
		return result;
	}
	
	public void update()
	{	// floor?
		floor.update();
		// heroes
		{	int i=0;
			while(i<heroes.size())
			{	Hero temp = heroes.get(i);
				@SuppressWarnings("unused")
				double prevPosX = temp.getCurrentPosX();
				@SuppressWarnings("unused")
				double prevPosY = temp.getCurrentPosY();
				temp.update();
				double tempPosX = temp.getCurrentPosX();
				double tempPosY = temp.getCurrentPosY();
				if(temp.isToBeRemovedFromTile())
				{	heroes.remove(i);
					level.removeSprite(temp);
					//NOTE à compléter (défaite)
				}
				else if(!containsPoint(tempPosX, tempPosY))
				{	Tile newTile = level.getTile(tempPosX,tempPosY);
//System.out.println("Tile.update>"+"tile:"+posX+";"+posY+" - case:"+line+";"+col);		
//System.out.println("Tile.update>"+"sprite:"+tempPosX+";"+tempPosY+" - newCase:"+newTile.getLine()+";"+newTile.getCol());
					heroes.remove(i);
					newTile.addHero(temp);		
				}
				else
					i++;
			}
		}

		// bombs
		{	int i=0;
			while(i<bombs.size())
			{	Bomb temp = bombs.get(i);
				@SuppressWarnings("unused")
				double prevPosX = temp.getCurrentPosX();
				@SuppressWarnings("unused")
				double prevPosY = temp.getCurrentPosY();
				temp.update();
				double tempPosX = temp.getCurrentPosX();
				double tempPosY = temp.getCurrentPosY();
				if(temp.isToBeRemovedFromTile())
				{	bombs.remove(i);
					level.removeSprite(temp);
					//NOTE à compléter (?)
				}
				else if(!containsPoint(tempPosX, tempPosY))
				{	Tile newTile = level.getTile(tempPosX,tempPosY);
//System.out.println("Tile.update>"+"tile:"+posX+";"+posY+" - case:"+line+";"+col);		
//System.out.println("Tile.update>"+"sprite:"+tempPosX+";"+tempPosY+" - newCase:"+newTile.getLine()+";"+newTile.getCol());
					bombs.remove(i);
					newTile.addBomb(temp);		
				}
				else
					i++;
			}
		}
		
		// fires
		{	int i=0;
			while(i<fires.size())
			{	Fire temp = fires.get(i);
				temp.update();
				if(temp.isToBeRemovedFromTile())
				{	fires.remove(i);
					level.removeSprite(temp);
					//NOTE à compléter (?)
				}
				i++;
			}
		}
		// block
		if(block!=null)
		{	block.update();
			if(block.isToBeRemovedFromTile())
			{	level.removeSprite(block); //NOTE deux lignes inversées, idem pr item
				block = null;			
				//NOTE à compléter (?)
			}
		}		
		// item
		if(item!=null)
		{	item.update();
			if(item.isToBeRemovedFromTile())
			{	level.removeSprite(item); //NOTE deux lignes inversées, idem pr block
				item = null;				
				//NOTE à compléter (?)
			}
		}		
	}
	
	/**
	 * dessine un sprite sans son ombre
	 */
	private void drawSprite(Graphics g, Sprite s)
	{	BufferedImage image = s.getCurrentImage();
		if(image!=null)
		{	double pX = s.getCurrentPosX()+s.getXShift();
			double pY = s.getCurrentPosY()+s.getYShift();
			double pZ = s.getCurrentPosZ();
			pX = pX - ((double)image.getWidth())/2;
			pY = pY - image.getHeight() + getDimension()/2;
			//
			ArrayList<Double> listX = new ArrayList<Double>(0);
			ArrayList<Double> listY = new ArrayList<Double>(0);
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
		// position
		if(!(s instanceof Floor))
		{	if(s instanceof Hero)
				g.setColor(Color.WHITE);
			else if(s instanceof Block)
				g.setColor(Color.GRAY);
			else if(s instanceof Bomb)
				g.setColor(Color.WHITE);
			else if(s instanceof Item)
				g.setColor(Color.WHITE);
			else if(s instanceof Fire)
				g.setColor(Color.BLACK);
			if(getLevel().getLoop().getShowSpritesPositions()==1)
			{	// coordonnées
				Font font = new Font("Dialog", Font.BOLD, 12);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				String text = "("+line+","+col+")";
				Rectangle2D box = metrics.getStringBounds(text, g);
				int x = (int)Math.round(s.getCurrentPosX()-box.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()+box.getHeight()/2);
				g.drawString(text, x, y);
			}
			else if(getLevel().getLoop().getShowSpritesPositions()==2)
			{	// coordonnées
				Font font = new Font("Dialog", Font.BOLD, 12);
				g.setFont(font);
				FontMetrics metrics = g.getFontMetrics(font);
				DecimalFormat nf = new DecimalFormat("000.00") ;
				String textX = nf.format(s.getCurrentPosX());
				String textY = nf.format(s.getCurrentPosY());
				String textZ = nf.format(s.getCurrentPosZ());
				Rectangle2D boxX = metrics.getStringBounds(textX, g);
				Rectangle2D boxY = metrics.getStringBounds(textY, g);
				Rectangle2D boxZ = metrics.getStringBounds(textZ, g);
				int x = (int)Math.round(s.getCurrentPosX()-boxX.getWidth()/2);
				int y = (int)Math.round(s.getCurrentPosY()-boxY.getHeight()/2);
				g.drawString(textX, x, y);
				x = (int)Math.round(s.getCurrentPosX()-boxY.getWidth()/2);
				y = (int)Math.round(s.getCurrentPosY()+boxY.getHeight()/2);
				g.drawString(textY, x, y);
				x = (int)Math.round(s.getCurrentPosX()-boxZ.getWidth()/2);
				y = (int)Math.round(s.getCurrentPosY()+boxY.getHeight()/2+boxZ.getHeight());
				g.drawString(textZ, x, y);
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
	
	public void drawFloor(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	AbstractAbility temp = floor.getAbility(StateAbility.SPRITE_FLAT);
		if(floor!=null && ((temp!=null && temp.isActive()) == flat) && (floor.isOnGround() == onGround))
		{	if(shadow)
				drawShadow(g,floor);
			else
				drawSprite(g,floor);
		}
	}
	public void drawItem(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	if(item!=null)
		{	
//if(fires.size()>0)
//	System.out.println();
			AbstractAbility temp = item.getAbility(StateAbility.SPRITE_FLAT);
			if(item!=null && ((temp!=null && temp.isActive()) == flat) && (item.isOnGround() == onGround))
				if(shadow)
					drawShadow(g,item);
				else
					drawSprite(g, item);
		}
	}
	public void drawBlock(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	if(block!=null)
		{	AbstractAbility temp = block.getAbility(StateAbility.SPRITE_FLAT);
			if(block!=null && ((temp!=null && temp.isActive()) == flat) && (block.isOnGround() == onGround))
				if(shadow && line!=level.getGlobalHeight()-1) //NOTE a préciser : permet d'éviter que l'ombre de la dernière ligne de blocs soit portée sur la première
					drawShadow(g,block);
				else
					drawSprite(g, block);
		}
	}
	public void drawFires(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	for(int i=0;i<fires.size();i++)
		{	Fire tempS = fires.get(i);
			AbstractAbility temp = tempS.getAbility(StateAbility.SPRITE_FLAT);
			if(((temp!=null && temp.isActive()) == flat) && (tempS.isOnGround() == onGround))
				if(shadow)
					drawShadow(g,tempS);
				else
					drawSprite(g, tempS);
		}
	}
	public void drawBombs(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	for(int i=0;i<bombs.size();i++)
		{	Bomb tempS = bombs.get(i);
			AbstractAbility temp = tempS.getAbility(StateAbility.SPRITE_FLAT);
			if(((temp.isActive()) == flat) && (tempS.isOnGround() == onGround))
				if(shadow)
					drawShadow(g,tempS);
				else
					drawSprite(g, tempS);
		}
	}
	public void drawHeroes(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	for(int i=0;i<heroes.size();i++)
		{	Hero tempS = heroes.get(i);
			AbstractAbility temp = tempS.getAbility(StateAbility.SPRITE_FLAT);
			if(((temp.isActive()) == flat) && (tempS.isOnGround() == onGround))
				if(shadow)
					drawShadow(g,tempS);
				else
					drawSprite(g,tempS);
		}
	}
	
	public void drawSelection(Graphics g, boolean flat, boolean onGround, boolean shadow)
	{	// floor
		drawFloor(g,flat,onGround,shadow);
		// item
		drawItem(g,flat,onGround,shadow);
		// block
		drawBlock(g,flat,onGround,shadow);
		// fires
		drawFires(g,flat,onGround,shadow);
		// bombs
		drawBombs(g,flat,onGround,shadow);
		// heroes
		drawHeroes(g,flat,onGround,shadow);
	}	
	
	/**
	 * trace l'ombre d'un sprite (et pas le sprite)
	 */
	private void drawShadow(Graphics g, Sprite s)
	{	BufferedImage image = s.getShadow();
		if(image!=null)
		{	double pX = s.getCurrentPosX()+s.getShadowXShift();
			double pY = s.getCurrentPosY()+s.getShadowYShift();
			pX = pX - ((double)image.getWidth())/2;
			pY = pY - image.getHeight() + getDimension()/2;
			//
			ArrayList<Double> listX = new ArrayList<Double>(0);
			ArrayList<Double> listY = new ArrayList<Double>(0);
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
	
	public void addSprite(Sprite sprite)
	{	if(sprite instanceof Block)
			setBlock((Block) sprite);
		else if(sprite instanceof Bomb)
			addBomb((Bomb) sprite);		
		else if(sprite instanceof Fire)
			addFire((Fire) sprite);		
		else if(sprite instanceof Floor)
			setFloor((Floor) sprite);
		else if(sprite instanceof Hero)
			addHero((Hero) sprite);
		else if(sprite instanceof Item)
			setItem((Item) sprite);
	}
	private void addHero(Hero hero)
	{	heroes.add(hero);		
		hero.setTile(this);
		level.addSprite(hero);
	}
	public ArrayList<Hero> getHeroes()
	{	return heroes;
	}

	private void addBomb(Bomb bomb)
	{	bombs.add(bomb);		
		bomb.setTile(this);
		level.addSprite(bomb);
	}
	public ArrayList<Bomb> getBombs()
	{	return bombs;
	}
	
	private void addFire(Fire fire)
	{	fires.add(fire);
		fire.setTile(this);
		level.addSprite(fire);
	}
	public ArrayList<Fire> getFires()
	{	return fires;
	}

	private void setBlock(Block block)
	{	this.block = block;
		block.setCurrentPosX(posX);
		block.setCurrentPosY(posY);
		block.setTile(this);
		level.addSprite(block);
	}
	public Block getBlock()
	{	return block;
	}
	public boolean hasBlock()
	{	return block!=null;
	}

	public Item getItem()
	{	return item;
	}
	private void setItem(Item item)
	{	this.item = item;
		item.setCurrentPosX(posX);
		item.setCurrentPosY(posY);
		item.setTile(this);
		level.addSprite(item);
	}
/*	
	public void removeItem()
	{	item = null;		
	}
*/
	public Floor getFloor()
	{	return floor;
	}
	private void setFloor(Floor floor)
	{	this.floor = floor;
		floor.setCurrentPosX(posX);
		floor.setCurrentPosY(posY);
		floor.setTile(this);
//		level.addSprite(floor);
	}
	

	public ArrayList<Sprite> getSprites()
	{	ArrayList<Sprite> result = new ArrayList<Sprite>();
		// floor
		result.add(floor);
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
		if(block!=null)
		{	result.add(block);
		}		
		// item
		if(item!=null)
		{	result.add(item);
		}		
		return result;
	}
	
	public void spreadEvent(AbstractEvent event)
	{	ArrayList<Sprite> sprites = getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext())
		{	Sprite sprt = i.next();
			sprt.processEvent(event);
		}
	}

	public int getLine()
	{	return line;
	}
	public int getCol()
	{	return col;
	}
	public double getPosX()
	{	return posX;
	}
	public double getPosY()
	{	return posY;
	}
	
	public Tile getNeighbour(Direction direction)
	{	Tile result;
		result = level.getNeighbourTile(line,col,direction);
		return result;
	}
	public boolean isNeighbour(Tile n)
	{	Tile nd = getNeighbour(Direction.DOWN);
		Tile nl = getNeighbour(Direction.LEFT);
		Tile nr = getNeighbour(Direction.RIGHT);
		Tile nu = getNeighbour(Direction.UP);
		boolean result = n==nd || n==nl || n==nr || n==nu;
		return result;
	}
	
	public Level getLevel()
	{	return level;		
	}
	
	public double getDimension()
	{	return level.getTileDimension();		
	}
		
	public String toString()
	{	String result;
		result = getClass().getSimpleName();
		result = result+"("+line+","+col+")";
		result = result+"("+posX+","+posY+")";
		return result;
	}
	

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// block
			if(block!=null)
			{	block.finish();
				block = null;
			}
			// bombs
			{	Iterator<Bomb> it = bombs.iterator();
				while(it.hasNext())
				{	Bomb temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// fires
			{	Iterator<Fire> it = fires.iterator();
				while(it.hasNext())
				{	Fire temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// floor
			if(floor!=null)
			{	floor.finish();
				floor = null;
			}
			// heroes
			{	Iterator<Hero> it = heroes.iterator();
				while(it.hasNext())
				{	Hero temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// item
			if(item!=null)
			{	item.finish();
				item= null;
			}
			// misc
			level = null;
		}
	}
}
