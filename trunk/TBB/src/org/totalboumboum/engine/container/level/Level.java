package org.totalboumboum.engine.container.level;

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

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.event.AbstractEvent;
import org.totalboumboum.engine.content.manager.trajectory.MoveZone;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CombinatoricsTools;
import org.totalboumboum.tools.calculus.LevelsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Level
{	public Level(VisibleLoop loop)				
	{	this.loop = loop;
	}
	
     /////////////////////////////////////////////////////////////////
	// LOOP					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private VisibleLoop loop;

	public VisibleLoop getLoop()
	{	return loop;	
	}
	
    /////////////////////////////////////////////////////////////////
	// THEME				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*
 TODO semblait inutile, dc désactivé 
 (également dans HollowLevel.loadTheme() et Level.finish())
	private Theme theme;

	public void setTheme(Theme theme)
	{	this.theme = theme;
	}
	public Theme getTheme()
	{	return theme;
	}
*/

	/////////////////////////////////////////////////////////////////
	// SIZE & LOCATION in TILES		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int globalWidth;
	private int globalHeight;
	private double pixelLeftX; // pas central
	private double pixelTopY; // pas central
	private double pixelWidth;
	private double pixelHeight;

	public void setTilePositions(int globalWidth, int globalHeight, double globalLeftX, double globalUpY)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
		this.pixelLeftX = globalLeftX;
		this.pixelTopY = globalUpY;
		this.pixelWidth = globalWidth*RoundVariables.scaledTileDimension;
		this.pixelHeight = globalHeight*RoundVariables.scaledTileDimension;
	}
	public double getGlobalLeftX()
	{	return pixelLeftX;
	}
	public double getGlobalUpY()
	{	return pixelTopY;
	}
	public int getGlobalWidth()
	{	return globalWidth;
	}
	public int getGlobalHeight()
	{	return globalHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Tile matrix[][];
	private List<Tile> tileList;
	
	public Tile[][] getMatrix()
	{	return matrix;
	}
	
	public void setMatrix(Tile matrix[][])
	{	this.matrix = matrix;
	}
	
	public Tile getTile(int l, int c)
	{	return matrix[l][c];	
	}
	
	public void initTileList()
	{	tileList = new ArrayList<Tile>();
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				tileList.add(matrix[line][col]);		
	}
	
	public List<Tile> getTileList()
	{	return tileList;
	}

    /////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getPixelDistance(Sprite s1, Sprite s2, Direction direction)
	{	return LevelsTools.getPixelDistance(s1,s2,direction,pixelHeight,pixelWidth);
	}
	public double getPixelDistance(Sprite s1, Sprite s2)
	{	return LevelsTools.getPixelDistance(s1,s2,pixelHeight,pixelWidth);
	}
	
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	return LevelsTools.getPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
	}
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	return LevelsTools.getPixelDistance(x1,y1,x2,y2,pixelHeight,pixelWidth);
	}

	public double getHorizontalPixelDistance(double x1, double x2, Direction direction)
	{	return LevelsTools.getHorizontalPixelDistance(x1,x2,direction,pixelWidth);
	}
	public double getHorizontalPixelDistance(double x1, double x2)
	{	return LevelsTools.getHorizontalPixelDistance(x1,x2,pixelWidth);
	}
	
	public double getVerticalPixelDistance(double y1, double y2, Direction direction)
	{	return LevelsTools.getVerticalPixelDistance(y1,y2,direction,pixelHeight);
	}
	public double getVerticalPixelDistance(double y1, double y2)
	{	return LevelsTools.getVerticalPixelDistance(y1,y2,pixelHeight);
	}
	
    /////////////////////////////////////////////////////////////////
	// TILE DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getTileDistance(Sprite s1, Sprite s2, Direction direction)
	{	return LevelsTools.getTileDistance(s1,s2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(Sprite s1, Sprite s2)
	{	return LevelsTools.getTileDistance(s1,s2,Direction.NONE,globalHeight,globalWidth);
	}
	public int getTileDistance(Tile tile1, Tile tile2, Direction direction)
	{	return LevelsTools.getTileDistance(tile1,tile2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(Tile tile1, Tile tile2)
	{	return LevelsTools.getTileDistance(tile1,tile2,Direction.NONE,globalHeight,globalWidth);
	}

	public int getTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	return LevelsTools.getTileDistance(line1,col1,line2,col2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(int line1, int col1, int line2, int col2)
	{	return LevelsTools.getTileDistance(line1,col1,line2,col2,Direction.NONE,globalHeight,globalWidth);
	}

	public int getHorizontalTileDistance(int col1, int col2, Direction direction)
	{	return LevelsTools.getHorizontalTileDistance(col1,col2,direction,globalWidth);
	}
	public int getHorizontalTileDistance(int col1, int col2)
	{	return LevelsTools.getHorizontalTileDistance(col1,col2,Direction.NONE,globalWidth);
	}
	
	public double getVerticalTileDistance(int line1, int line2, Direction direction)
	{	return LevelsTools.getVerticalTileDistance(line1,line2,direction,globalHeight);
	}
	public double getVerticalTileDistance(int line1, int line2)
	{	return LevelsTools.getVerticalTileDistance(line1,line2,Direction.NONE,globalHeight);
	}
	
    /////////////////////////////////////////////////////////////////
	// BOMBSET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	private Bombset bombset;

	public Bombset getBombset()
	{	return bombset;		
	}
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;
	}
*/	
	/////////////////////////////////////////////////////////////////
	// FIRESET MAP			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	private FiresetMap firesetMap;

	public FiresetMap getFiresetMap()
	{	return firesetMap;	
	}
	public void setFiresetMap(FiresetMap firesetMap)
	{	this.firesetMap = firesetMap;
	}
*/	
	/////////////////////////////////////////////////////////////////
	// ITEMSET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	private Itemset itemset;

	public Itemset getItemset()
	{	return itemset;	
	}
	public void setItemset(Itemset itemset)
	{	this.itemset = itemset;
	}
*/	
	/////////////////////////////////////////////////////////////////
	// SPRITES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** all the sprites currently present in the level (even the hidden ones) */
	private List<Sprite> spritesList = new ArrayList<Sprite>();;
	private HashMap<Integer,Sprite> spritesMap = new HashMap<Integer,Sprite>();;

	public void removeSprite(Sprite sprite)
	{	spritesList.remove(sprite);
		spritesMap.remove(sprite.getId());
		//TODO should be called any time a sprite is supressed from the game
	}
/*	
	public void addHero(Hero hero, int line, int col)
	{	matrix[line][col].addSprite(hero);
		hero.setCurrentPosX(matrix[line][col].getPosX());
		hero.setCurrentPosY(matrix[line][col].getPosY());
	}
*/	
	/**
	 * add a sprite for the first time in the level
	 */
	public void insertSpriteTile(Sprite sprite)
	{	spritesList.add(sprite);
		spritesMap.put(sprite.getId(),sprite);
		sprite.getTile().addSprite(sprite);
	}
	
	public List<Sprite> getSprites()
	{	return spritesList;
	}
	
	public Sprite getSprite(int id)
	{	return spritesMap.get(id);
	}
	
	public void changeSpriteId(Sprite sprite, int newId)
	{	int oldId = sprite.getId();
		sprite.setId(newId);
		spritesMap.remove(oldId);
		spritesMap.put(newId,sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * processes the direction from the sprite s1 to the sprite s2, considering the 
	 * level is cyclic (i.e. using the shortest path).
	 */
	public Direction getDirection(Sprite s1, Sprite s2)
	{	return LevelsTools.getDirection(s1,s2,pixelHeight,pixelWidth);
	}
	
	/**
	 * processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	return LevelsTools.getDirection(x1,y1,x2,y2,pixelHeight,pixelWidth);
	}

	/**
	 * processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getHorizontalDirection(double x1, double x2)
	{	return LevelsTools.getHorizontalDirection(x1,x2,pixelWidth);
	}

	/**
	 * processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getVerticalDirection(double x1, double x2)
	{	return LevelsTools.getVerticalDirection(x1,x2,pixelHeight);
	}

	/////////////////////////////////////////////////////////////////
	// APPROXIMATE DIRECTIONS	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * processes the direction from s1 to s2, considering approximate 
	 * distance (i.e. it will be NONE even if s1 and s2 are just relatively close).
	 * It also considers the level circularity, i.e. it will choose the
	 * directions corresponding to the shortest distances. 
	 */
	public Direction getCompositeFromSprites(Sprite s1, Sprite s2)
	{	return LevelsTools.getCompositeFromSprites(s1,s2);
	}

	/**
	 * processes the direction from the sprite in the specified MoveZone to the specified obstacle.
	 * Approximate distance is considered (i.e. it will be NONE even if s1 and s2 
	 * are just relatively close). It also considers the level circularity, 
	 * i.e. it will choose the directions corresponding to the shortest distances 
	 */
	public Direction getCompositeFromSprites(MoveZone mz, Sprite obstacle)
	{	return LevelsTools.getCompositeFromSprites(mz,obstacle);
	}
	
	/////////////////////////////////////////////////////////////////
	// DELTAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public double getDeltaX(double x1, double x2, Direction direction)
	{	return LevelsTools.getDeltaX(x1,x2,direction,pixelWidth);
	}
	
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public double getDeltaX(double x1, double x2)
	{	return LevelsTools.getDeltaX(x1,x2,Direction.NONE,pixelWidth);
	}

	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public double getDeltaY(double y1, double y2, Direction direction)
	{	return LevelsTools.getDeltaY(y1,y2,direction,pixelHeight);
	}
	
	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public double getDeltaY(double y1, double y2)
	{	return LevelsTools.getDeltaY(y1,y2,Direction.NONE,pixelHeight);
	}

	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Tile getTile(double x, double y)
	{	Tile result = null;
		x = CombinatoricsTools.round(x);
		y = CombinatoricsTools.round(y);
		double difX = x-pixelLeftX;
		double difY = y-pixelTopY;
		double rX = difX/RoundVariables.scaledTileDimension;
		double rY = difY/RoundVariables.scaledTileDimension;
		int rdX = (int)rX;//(int)Math.round(rX);
		int rdY = (int)rY;//(int)Math.round(rY);
		int c = rdX%globalWidth;
		int l = rdY%globalHeight;
		result = matrix[l][c];
		return result;
	}	
	public Tile getNeighborTile(int line, int col, Direction direction)
	{	Tile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+globalWidth-1)%globalWidth;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%globalWidth;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+globalHeight-1)%globalHeight;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%globalHeight;
		else
			l = line;
		//
		result = matrix[l][c];
		return result;
	}
	public List<Tile> getNeighborTiles(int line, int col)
	{	List<Tile> resultat = new ArrayList<Tile>();
		resultat.add(getNeighborTile(line, col, Direction.LEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWNLEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWN));
		resultat.add(getNeighborTile(line, col, Direction.DOWNRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.RIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPLEFT));
		resultat.add(getNeighborTile(line, col, Direction.UP));		
		return resultat;
	}	

	public int[] normalizePosition(int line, int col)
	{	return LevelsTools.normalizePosition(line,col,globalHeight,globalWidth);
	}
	public int normalizePositionLine(int line)
	{	return LevelsTools.normalizePositionLine(line,globalHeight);
	}
	public int normalizePositionCol(int col)
	{	return LevelsTools.normalizePositionLine(col,globalHeight);
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double[] normalizePosition(double x, double y)
	{	return LevelsTools.normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	public double normalizePositionX(double x)
	{	return LevelsTools.normalizePositionX(x,pixelLeftX,pixelWidth);
	}
	public double normalizePositionY(double y)
	{	return LevelsTools.normalizePositionY(y,pixelTopY,pixelHeight);
	}
	public boolean isInsidePosition(double x, double y)
	{	return LevelsTools.isInsidePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	public boolean isInsidePositionX(double x)
	{	return LevelsTools.isInsidePositionX(x,pixelLeftX,pixelWidth);
	}
	public boolean isInsidePositionY(double y)
	{	return LevelsTools.isInsidePositionY(y,pixelTopY,pixelHeight);
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private double horizontalBorderX;
	@SuppressWarnings("unused")
	private double upBorderY;
	@SuppressWarnings("unused")
	private double downBorderY;
	@SuppressWarnings("unused")
	private double horizontalBorderHeight;
	@SuppressWarnings("unused")
	private double horizontalBorderWidth;
	@SuppressWarnings("unused")
	private double verticalBorderY;
	@SuppressWarnings("unused")
	private double leftBorderX;
	@SuppressWarnings("unused")
	private double rightBorderX;
	@SuppressWarnings("unused")
	private double verticalBorderHeight;
	@SuppressWarnings("unused")
	private double verticalBorderWidth;
	
	public void setBorders(double values[])
	{	int i = 0;
		horizontalBorderX = values[i++];
		upBorderY = values[i++];
		downBorderY = values[i++];
		horizontalBorderHeight = values[i++];
		horizontalBorderWidth = values[i++];
		verticalBorderY = values[i++];
		leftBorderX = values[i++];
		rightBorderX = values[i++];
		verticalBorderHeight = values[i++];
		verticalBorderWidth = values[i++];
	}

	/////////////////////////////////////////////////////////////////
	// IN GAME METHODS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	private long time = 0;
//	private long startTime = -1;
	
	public void update()
	{	
//time = time + getConfiguration().getMilliPeriod();
//if(startTime<0) startTime = System.currentTimeMillis();
		// update floors
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.FLOOR);		

		// update items
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.ITEM);		

		// update blocks
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.BLOCK);		

		// update bombs
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.BOMB);		

		// update fires
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.FIRE);		

		// update heroes
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].updateSprites(Role.HERO);		
	}

	//NOTE optimisation : à effectuer seulement pour les tiles visibles
	public void draw(Graphics g)
	{	boolean flat = true;
		boolean onGround = true;
		boolean shadow = false;
		
		// only the on-ground flat sprites (they don't have shadow)
		flat = true;
		onGround = true;
		shadow = false;
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,flat,onGround,shadow);
		
		// the rest line by line
		// first: ground shadows from the last line (for graphical reasons)
		flat = false;
		onGround = true;
		shadow = true;
		for(int col=0;col<globalWidth;col++)
			matrix[globalHeight-1][col].drawSelection(g,flat,onGround,shadow);
		// then the rest
		for(int line=0;line<globalHeight;line++)
		{	// shadows from the non-flat on-ground sprites
			flat = false;
			onGround = true;
			shadow = true;
			if(line<globalHeight-1)
			{	for(int col=0;col<globalWidth;col++)
					matrix[line][col].drawSelection(g,flat,onGround,shadow);
			}
			/*
			 * the non-flat on-ground sprites themselves:
			 * each different kind is processed separately for graphical reasons
			 */
			flat = false;
			onGround = true;
			shadow = false;
			// floor
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.FLOOR,g,flat,onGround,shadow);
			// fires
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.FIRE,g,flat,onGround,shadow);
			// item
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.ITEM,g,flat,onGround,shadow);
			// block
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.BLOCK,g,flat,onGround,shadow);
			// bombs
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.BOMB,g,flat,onGround,shadow);
			// heroes
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSprites(Role.HERO,g,flat,onGround,shadow);
			
			// shadows from the in-air sprites
			flat = false;
			onGround = false;
			shadow = true;
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,flat,onGround,shadow);
			// the in-air sprites themselves
			flat = false;
			onGround = false;
			shadow = false;
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,flat,onGround,shadow);
		}
	}
/*	
	private void drawBorders(Graphics g)
	{	if(horizontalBorderHeight>0)
		{	Color temp = g.getColor();
			g.setColor(configuration.getBorderColor());
			g.fillRect((int)horizontalBorderX, (int)upBorderY, (int)horizontalBorderWidth, (int)horizontalBorderHeight);
			g.fillRect((int)horizontalBorderX, (int)downBorderY, (int)horizontalBorderWidth, (int)horizontalBorderHeight);
			g.setColor(temp);
		}
		if(verticalBorderWidth>0)
		{	Color temp = g.getColor();
			g.setColor(configuration.getBorderColor());
			g.fillRect((int)leftBorderX, (int)verticalBorderY, (int)verticalBorderWidth, (int)verticalBorderHeight);
			g.fillRect((int)rightBorderX, (int)verticalBorderY, (int)verticalBorderWidth, (int)verticalBorderHeight);
			g.setColor(temp);
		}
	}
*/

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void spreadEvent(AbstractEvent event)
	{	for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[line][col];
				tile.spreadEvent(event);
			}
		}
	}

	public void spreadEvent(AbstractEvent event, Role role)
	{	for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[line][col];
				tile.spreadEvent(event,role);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ENTRY				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getEntryDuration(Role role)
	{	double result = 0;
		for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[line][col];
				List<Sprite> list = tile.getSprites(role);
				for(Sprite sprite: list)
				{	StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_ENTRY_DURATION);
					double duration = ability.getStrength();
					if(duration>result)
						result = duration;
				}
			}
		}
		return result;
	}	
}
