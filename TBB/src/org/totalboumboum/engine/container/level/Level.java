package org.totalboumboum.engine.container.level;

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

import java.awt.Color;
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
import org.totalboumboum.tools.levels.DeltaTools;
import org.totalboumboum.tools.levels.DirectionTools;
import org.totalboumboum.tools.levels.DistancePixelTools;
import org.totalboumboum.tools.levels.DistanceTileTools;
import org.totalboumboum.tools.levels.PositionTools;

/**
 * This class represents the in-game zone.
 * 
 * @author Vincent Labatut
 */
public class Level
{	
	/**
	 * Creates a new level for the specified loop.
	 * 
	 * @param loop
	 * 		Loop in charge of running this level.
	 */
	public Level(VisibleLoop loop)				
	{	this.loop = loop;
	}
	
     /////////////////////////////////////////////////////////////////
	// LOOP					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Loop in charge of running the game */
	private VisibleLoop loop;

	/**
	 * Returns the loop running this level.
	 * 
	 * @return
	 * 		Loop handling this level.
	 */
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
	/** Level width in tiles */
	private int globalWidth;
	/** Level height in tiles */
	private int globalHeight;

	/**
	 * Pixel positions of the tiles.
	 * 
	 * @param globalWidth
	 * 		Width in tiles.
	 * @param globalHeight
	 * 		Height in tiles.
	 * @param globalLeftX
	 * 		X coord of the centers of the leftest tiles.
	 * @param globalUpY
	 * 		Y coord of the centers of the uppest tiles.
	 */
	public void setTilePositions(int globalWidth, int globalHeight, double globalLeftX, double globalUpY)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
		this.pixelLeftX = globalLeftX;
		this.pixelTopY = globalUpY;
		this.pixelWidth = globalWidth*RoundVariables.scaledTileDimension;
		this.pixelHeight = globalHeight*RoundVariables.scaledTileDimension;
	}
	
	/**
	 * Returns the level width in tiles.
	 * 
	 * @return
	 * 		Level width expressed in tiles.
	 */
	public int getGlobalWidth()
	{	return globalWidth;
	}
	
	/**
	 * Returns the level height in tiles.
	 * 
	 * @return
	 * 		Level height expressed in tiles.
	 */
	public int getGlobalHeight()
	{	return globalHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// SIZE & LOCATION in PIXELS	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Leftest pixel in the level */
	private double pixelLeftX; // pas central
	/** Uppest pixel in the level */
	private double pixelTopY; // pas central
	/** Level width in pixels */
	private double pixelWidth;
	/** Level height in pixels */
	private double pixelHeight;

	/**
	 * Leftest pixel coordinate.
	 * 
	 * @return
	 * 		X coordinate of the left level edge.
	 */
	public double getPixelLeftX()
	{	return pixelLeftX;
	}
	
	/**
	 * Uppest pixel coordinate.
	 * 
	 * @return
	 * 		Y coordinate of the top level edge.
	 */
	public double getPixelTopY()
	{	return pixelTopY;
	}
	
	/**
	 * Returns the level width in pixels.
	 * 
	 * @return
	 * 		Level width expressed in pixels.
	 */
	public double getPixelWidth()
	{	return pixelWidth;
	}
	
	/**
	 * Returns the level height in pixels.
	 * 
	 * @return
	 * 		Level height expressed in pixels.
	 */
	public double getPixelHeight()
	{	return pixelHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** All tiles constituting the zone presented as a matrix */
	private Tile matrix[][];
	/** All tiles constituting the zone presented as a list */
	private List<Tile> tileList;
	
	/**
	 * Returns the matrix containing all zone tiles.
	 * 
	 * @return
	 * 		Matrix of tiles.
	 */
	public Tile[][] getMatrix()
	{	return matrix;
	}
	
	/**
	 * Changes the tile matrix of this level.
	 * 
	 * @param matrix
	 * 		New tile matrix.
	 */
	public void setMatrix(Tile matrix[][])
	{	this.matrix = matrix;
	}
	
	/**
	 * Returns the tile located at the specified positions.
	 * 
	 * @param row
	 * 		Tile row.
	 * @param col
	 * 		Tile column.
	 * @return
	 * 		The corresponding tile.
	 */
	public Tile getTile(int row, int col)
	{	return matrix[row][col];	
	}
	
	/**
	 * Initializes the list containing all the level tiles.
	 */
	public void initTileList()
	{	tileList = new ArrayList<Tile>();
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				tileList.add(matrix[row][col]);		
	}
	
	/**
	 * Returns the list containing all the level tiles.
	 * 
	 * @return
	 * 		List of {@code AiTile} objects.
	 */
	public List<Tile> getTileList()
	{	return tileList;
	}

    /////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getPixelDistance(Sprite s1, Sprite s2, Direction direction)
	{	return DistancePixelTools.getPixelDistance(s1,s2,direction,pixelHeight,pixelWidth);
	}
	public double getPixelDistance(Sprite s1, Sprite s2)
	{	return DistancePixelTools.getPixelDistance(s1,s2,pixelHeight,pixelWidth);
	}
	
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	return DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	return DistancePixelTools.getPixelDistance(x1,y1,x2,y2,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	public double getHorizontalPixelDistance(double x1, double x2, Direction direction)
	{	return DistancePixelTools.getHorizontalPixelDistance(x1,x2,direction,pixelLeftX,pixelWidth);
	}
	public double getHorizontalPixelDistance(double x1, double x2)
	{	return DistancePixelTools.getHorizontalPixelDistance(x1,x2,pixelLeftX,pixelWidth);
	}
	
	public double getVerticalPixelDistance(double y1, double y2, Direction direction)
	{	return DistancePixelTools.getVerticalPixelDistance(y1,y2,direction,pixelTopY,pixelHeight);
	}
	public double getVerticalPixelDistance(double y1, double y2)
	{	return DistancePixelTools.getVerticalPixelDistance(y1,y2,pixelTopY,pixelHeight);
	}
	
    /////////////////////////////////////////////////////////////////
	// TILE DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getTileDistance(Sprite s1, Sprite s2, Direction direction)
	{	return DistanceTileTools.getTileDistance(s1,s2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(Sprite s1, Sprite s2)
	{	return DistanceTileTools.getTileDistance(s1,s2,globalHeight,globalWidth);
	}
	public int getTileDistance(Tile tile1, Tile tile2, Direction direction)
	{	return DistanceTileTools.getTileDistance(tile1,tile2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(Tile tile1, Tile tile2)
	{	return DistanceTileTools.getTileDistance(tile1,tile2,globalHeight,globalWidth);
	}

	public int getTileDistance(int row1, int col1, int row2, int col2, Direction direction)
	{	return DistanceTileTools.getTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
	}
	public int getTileDistance(int row1, int col1, int row2, int col2)
	{	return DistanceTileTools.getTileDistance(row1,col1,row2,col2,globalHeight,globalWidth);
	}

	public int getHorizontalTileDistance(int col1, int col2, Direction direction)
	{	return DistanceTileTools.getHorizontalTileDistance(col1,col2,direction,globalWidth);
	}
	public int getHorizontalTileDistance(int col1, int col2)
	{	return DistanceTileTools.getHorizontalTileDistance(col1,col2,globalWidth);
	}
	
	public double getVerticalTileDistance(int row1, int row2, Direction direction)
	{	return DistanceTileTools.getVerticalTileDistance(row1,row2,direction,globalHeight);
	}
	public double getVerticalTileDistance(int row1, int row2)
	{	return DistanceTileTools.getVerticalTileDistance(row1,row2,globalHeight);
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
	public void addHero(Hero hero, int row, int col)
	{	matrix[row][col].addSprite(hero);
		hero.setCurrentPosX(matrix[row][col].getPosX());
		hero.setCurrentPosY(matrix[row][col].getPosY());
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

	/**
	 * Like insertSpriteTile, but no insertion
	 * in the tile (the sprite is not really
	 * a part of the game, yet).
	 * 
	 * @param sprite
	 * 		The sprite to be inserted.
	 */
	public void addSprite(Sprite sprite)
	{	spritesList.add(sprite);
		spritesMap.put(sprite.getId(),sprite);
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
	{	return DirectionTools.getDirection(s1,s2,pixelHeight,pixelWidth);
	}
	
	/**
	 * processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	return DirectionTools.getDirection(x1,y1,x2,y2,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getHorizontalDirection(double x1, double x2)
	{	return DirectionTools.getHorizontalDirection(x1,x2,pixelLeftX,pixelWidth);
	}

	/**
	 * processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public Direction getVerticalDirection(double y1, double y2)
	{	return DirectionTools.getVerticalDirection(y1,y2,pixelTopY,pixelHeight);
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
	{	return DirectionTools.getCompositeFromSprites(s1,s2);
	}

	/**
	 * processes the direction from the sprite in the specified MoveZone to the specified obstacle.
	 * Approximate distance is considered (i.e. it will be NONE even if s1 and s2 
	 * are just relatively close). It also considers the level circularity, 
	 * i.e. it will choose the directions corresponding to the shortest distances 
	 */
	public Direction getCompositeFromSprites(MoveZone mz, Sprite obstacle)
	{	return DirectionTools.getCompositeFromSprites(mz,obstacle);
	}
	
	/////////////////////////////////////////////////////////////////
	// DELTAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public double getDeltaX(double x1, double x2, Direction direction)
	{	return DeltaTools.getDeltaX(x1,x2,direction,pixelLeftX,pixelWidth);
	}
	
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public double getDeltaX(double x1, double x2)
	{	return DeltaTools.getDeltaX(x1,x2,pixelLeftX,pixelWidth);
	}

	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public double getDeltaY(double y1, double y2, Direction direction)
	{	return DeltaTools.getDeltaY(y1,y2,direction,pixelTopY,pixelHeight);
	}
	
	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public double getDeltaY(double y1, double y2)
	{	return DeltaTools.getDeltaY(y1,y2,pixelTopY,pixelHeight);
	}

	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Tile getTile(double x, double y)
	{	int[] coord = PositionTools.getTile(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth,globalHeight,globalWidth);
		Tile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	public Tile getNeighborTile(int row, int col, Direction direction)
	{	int[] coord = PositionTools.getNeighborTile(row,col,direction,globalHeight,globalWidth);
		Tile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	public List<Tile> getNeighborTiles(int row, int col)
	{	List<Tile> resultat = new ArrayList<Tile>();
		resultat.add(getNeighborTile(row, col, Direction.LEFT));
		resultat.add(getNeighborTile(row, col, Direction.DOWNLEFT));
		resultat.add(getNeighborTile(row, col, Direction.DOWN));
		resultat.add(getNeighborTile(row, col, Direction.DOWNRIGHT));
		resultat.add(getNeighborTile(row, col, Direction.RIGHT));
		resultat.add(getNeighborTile(row, col, Direction.UPRIGHT));
		resultat.add(getNeighborTile(row, col, Direction.UPLEFT));
		resultat.add(getNeighborTile(row, col, Direction.UP));		
		return resultat;
	}	

	public int[] normalizePosition(int row, int col)
	{	return PositionTools.normalizePosition(row,col,globalHeight,globalWidth);
	}
	public int normalizePositionRow(int row)
	{	return PositionTools.normalizePositionRow(row,globalHeight);
	}
	public int normalizePositionCol(int col)
	{	return PositionTools.normalizePositionRow(col,globalHeight);
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double[] normalizePosition(double x, double y)
	{	return PositionTools.normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	public double normalizePositionX(double x)
	{	return PositionTools.normalizePositionX(x,pixelLeftX,pixelWidth);
	}
	public double normalizePositionY(double y)
	{	return PositionTools.normalizePositionY(y,pixelTopY,pixelHeight);
	}
	public boolean isInsidePosition(double x, double y)
	{	return PositionTools.isInsidePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	public boolean isInsidePositionX(double x)
	{	return PositionTools.isInsidePositionX(x,pixelLeftX,pixelWidth);
	}
	public boolean isInsidePositionY(double y)
	{	return PositionTools.isInsidePositionY(y,pixelTopY,pixelHeight);
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Don't remember what this is...*/
	private double horizontalBorderX;
	/** This neither...*/
	private double verticalBorderY;
	
	/** Horizontal coordinate of the end of the left vertical border */
	private double leftBorderX;
	/** Horizontal oordinate of the beginning end of the right vertical border */
	private double rightBorderX;
	/** Height of the horizontal border, in pixels */
	private double horizontalBorderHeight;
	/** Width of the horizontal border, in pixels */
	private double horizontalBorderWidth;

	/** Vertical coordinate of the end of the up horizontal border */
	private double upBorderY;
	/** Vertical coordinate of the beginning of the down horizontal border */
	private double downBorderY;
	/** Height of the vertical border, in pixels */
	private double verticalBorderHeight;
	/** Width of the vertical border, in pixels */
	private double verticalBorderWidth;
	
	/**
	 * Initializes the border variable for this level.
	 * 
	 * @param values
	 * 		Border variables/
	 */
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

	/**
	 * Possibly draws the level borders, in order
	 * to cover sprites passing from one side to the other.
	 *  
	 * @param g
	 * 		Object used for drawing.
	 */
	private void drawBorder(Graphics g)
	{	g.setColor(Color.BLACK);
		
		if(horizontalBorderHeight>0 && horizontalBorderWidth>0)
		{	g.fillRect(0, 0, 
				(int)Math.round(horizontalBorderWidth), (int)Math.round(horizontalBorderHeight));
			g.fillRect(0, (int)Math.round(downBorderY), 
				(int)Math.round(horizontalBorderWidth), (int)Math.round(horizontalBorderHeight));
		}
		
		if(verticalBorderHeight>0 && verticalBorderWidth>0)
		{	g.fillRect(0, (int)Math.round(horizontalBorderHeight), 
				(int)Math.round(verticalBorderWidth), (int)Math.round(verticalBorderHeight));
			g.fillRect((int)Math.round(rightBorderX), (int)Math.round(horizontalBorderHeight), 
				(int)Math.round(verticalBorderWidth), (int)Math.round(verticalBorderHeight));
		}
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
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.FLOOR);		

		// update items
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.ITEM);		

		// update blocks
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.BLOCK);		

		// update bombs
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.BOMB);		

		// update fires
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.FIRE);		

		// update heroes
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].updateSprites(Role.HERO);		
	}

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
	{	for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].setDrawSwitch(role,value);
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
		for(int row=0;row<globalHeight;row++)
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSelection(g,flat,onGround,shadow);
		
		// the rest row by row
		// first: ground shadows from the last row (for graphical reasons)
		flat = false;
		onGround = true;
		shadow = true;
		for(int col=0;col<globalWidth;col++)
			matrix[globalHeight-1][col].drawSelection(g,flat,onGround,shadow);
		// then the rest
		for(int row=0;row<globalHeight;row++)
		{	// shadows from the non-flat on-ground sprites
			flat = false;
			onGround = true;
			shadow = true;
			if(row<globalHeight-1)
			{	for(int col=0;col<globalWidth;col++)
					matrix[row][col].drawSelection(g,flat,onGround,shadow);
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
				matrix[row][col].drawSprites(Role.FLOOR,g,flat,onGround,shadow);
			// fires
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSprites(Role.FIRE,g,flat,onGround,shadow);
			// item
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSprites(Role.ITEM,g,flat,onGround,shadow);
			// block
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSprites(Role.BLOCK,g,flat,onGround,shadow);
			// bombs
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSprites(Role.BOMB,g,flat,onGround,shadow);
			// heroes
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSprites(Role.HERO,g,flat,onGround,shadow);
			
			// shadows from the in-air sprites
			flat = false;
			onGround = false;
			shadow = true;
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSelection(g,flat,onGround,shadow);
			// the in-air sprites themselves
			flat = false;
			onGround = false;
			shadow = false;
			for(int col=0;col<globalWidth;col++)
				matrix[row][col].drawSelection(g,flat,onGround,shadow);
		}
		
		// draw borders
		drawBorder(g);
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
	{	for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[row][col];
				tile.spreadEvent(event);
			}
		}
	}

	public void spreadEvent(AbstractEvent event, Role role)
	{	for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[row][col];
				tile.spreadEvent(event,role);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ENTRY				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getEntryDuration(Role role)
	{	double result = 0;
		for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[row][col];
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
