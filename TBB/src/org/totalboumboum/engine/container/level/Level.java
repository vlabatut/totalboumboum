package org.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Map;

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
import org.totalboumboum.tools.level.DeltaTools;
import org.totalboumboum.tools.level.DirectionTools;
import org.totalboumboum.tools.level.DistancePixelTools;
import org.totalboumboum.tools.level.DistanceTileTools;
import org.totalboumboum.tools.level.PositionTools;

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

	/**
	 * Checks if the three specified tiles are aligned.
	 * 
	 * @param t1
	 * 		First tile.
	 * @param t2
	 * 		Second tile.
	 * @param t3
	 * 		Third tile.
	 * @return
	 * 		{@code true} iff they are aligned.
	 */
	public boolean areAlignedTiles(Tile t1, Tile t2, Tile t3)
	{	int col1 = t1.getCol();
		int col2 = t2.getCol();
		int col3 = t3.getCol();
		int row1 = t1.getRow();
		int row2 = t2.getRow();
		int row3 = t3.getRow();
		
		boolean result = 
			col1==col2 && col1==col3
			|| row1==row2 && row1==row3;
		return result;
	}
	
	/**
	 * Same as {@link #areAlignedTiles}, but this time the three
	 * tiles must be in the specified order.
	 * 
	 * @param t1
	 * 		First tile.
	 * @param t2
	 * 		Second tile.
	 * @param t3
	 * 		Third tile.
	 * @return
	 * 		{@code true} iff they are aligned and ordered.
	 */
	public boolean areOrderAlignedTiles(Tile t1, Tile t2, Tile t3)
	{	int col1 = t1.getCol();
		int col2 = t2.getCol();
		int col3 = t3.getCol();
		int row1 = t1.getRow();
		int row2 = t2.getRow();
		int row3 = t3.getRow();
		
		boolean result = 
			col1==col2 && col1==col3 && (row1<row2 && row2<row3 || row1>row2 && row2>row3)
			|| row1==row2 && row1==row3 && (col1<col2 && col2<col3 || col1>col2 && col2>col3);
		return result;
	}
	
    /////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the sprites.
	 */
	public double getPixelDistance(Sprite s1, Sprite s2, Direction direction)
	{	return DistancePixelTools.getPixelDistance(s1,s2,direction,pixelHeight,pixelWidth);
	}
	
	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in pixels.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public double getPixelDistance(Sprite s1, Sprite s2)
	{	return DistancePixelTools.getPixelDistance(s1,s2,pixelHeight,pixelWidth);
	}
	
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the points.
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	return DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		Distance between the points.
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	return DistancePixelTools.getPixelDistance(x1,y1,x2,y2,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * horizontal axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the points.
	 */
	public double getHorizontalPixelDistance(double x1, double x2, Direction direction)
	{	return DistancePixelTools.getHorizontalPixelDistance(x1,x2,direction,pixelLeftX,pixelWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the horizontal axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @return 
	 * 		Distance between the points.
	 */
	public double getHorizontalPixelDistance(double x1, double x2)
	{	return DistancePixelTools.getHorizontalPixelDistance(x1,x2,pixelLeftX,pixelWidth);
	}
	
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * vertical axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the points.
	 */
	public double getVerticalPixelDistance(double y1, double y2, Direction direction)
	{	return DistancePixelTools.getVerticalPixelDistance(y1,y2,direction,pixelTopY,pixelHeight);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the vertical axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		Distance between the points.
	 */
	public double getVerticalPixelDistance(double y1, double y2)
	{	return DistancePixelTools.getVerticalPixelDistance(y1,y2,pixelTopY,pixelHeight);
	}
	
    /////////////////////////////////////////////////////////////////
	// TILE DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the sprites.
	 */
	public int getTileDistance(Sprite s1, Sprite s2, Direction direction)
	{	return DistanceTileTools.getTileDistance(s1,s2,direction,globalHeight,globalWidth);
	}

	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public int getTileDistance(Sprite s1, Sprite s2)
	{	return DistanceTileTools.getTileDistance(s1,s2,globalHeight,globalWidth);
	}

	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param tile1 
	 * 		First tile.
	 * @param tile2 
	 * 		Second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the sprites.
	 */
	public int getTileDistance(Tile tile1, Tile tile2, Direction direction)
	{	return DistanceTileTools.getTileDistance(tile1,tile2,direction,globalHeight,globalWidth);
	}

	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param tile1 
	 * 		First tile.
	 * @param tile2 
	 * 		Second tile.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public int getTileDistance(Tile tile1, Tile tile2)
	{	return DistanceTileTools.getTileDistance(tile1,tile2,globalHeight,globalWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first point.
	 * @param col1 
	 * 		Col coordinate of the first point.
	 * @param row2 
	 * 		Row coordinate of the second point.
	 * @param col2 
	 * 		Col coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the coordinates.
	 */
	public int getTileDistance(int row1, int col1, int row2, int col2, Direction direction)
	{	return DistanceTileTools.getTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second point.
	 * @param col2 
	 * 		Col coordinate of the second point.
	 * @return 
	 * 		Distance between the tiles.
	 */
	public int getTileDistance(int row1, int col1, int row2, int col2)
	{	return DistanceTileTools.getTileDistance(row1,col1,row2,col2,globalHeight,globalWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * horizontal axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param col2 
	 * 		Col coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the columns.
	 */
	public int getHorizontalTileDistance(int col1, int col2, Direction direction)
	{	return DistanceTileTools.getHorizontalTileDistance(col1,col2,direction,globalWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the horizontal axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param col2 
	 * 		Col coordinate of the second tile.
	 * @return 
	 * 		Distance between the columns.
	 */
	public int getHorizontalTileDistance(int col1, int col2)
	{	return DistanceTileTools.getHorizontalTileDistance(col1,col2,globalWidth);
	}
	
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * vertical axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @return 
	 * 		Distance between the rows.
	 */
	public double getVerticalTileDistance(int row1, int row2, Direction direction)
	{	return DistanceTileTools.getVerticalTileDistance(row1,row2,direction,globalHeight);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the vertical axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @return 
	 * 		Distance between the rows.
	 */
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
	/** List of all the sprites currently present in the level (even the hidden ones) */
	private List<Sprite> spritesList = new ArrayList<Sprite>();
	/** Map containing all sprites from this level */
	private Map<Integer,Sprite> spritesMap = new HashMap<Integer,Sprite>();;

	/**
	 * Removes a sprite from this level.
	 * 
	 * @param sprite
	 * 		Sprite to be removed.
	 */
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
	 * Adds a sprite for the first time in this level.
	 * 
	 * @param sprite
	 * 		Sprite to be added to this level.
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
	
	/**
	 * Returns the list of all sprites in this level.
	 * 
	 * @return
	 * 		List of all sprites.
	 */
	public List<Sprite> getSprites()
	{	return spritesList;
	}
	
	/**
	 * Returns the sprite whose id is specified.
	 * 
	 * @param id
	 * 		Id of the targeted sprite.
	 * @return
	 * 		Corresponding sprite.
	 */
	public Sprite getSprite(int id)
	{	return spritesMap.get(id);
	}
	
	/**
	 * Changes the id of the specified sprite.
	 * 
	 * @param sprite
	 * 		Concerned sprite.
	 * @param newId
	 * 		New id of this sprite.
	 */
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
	 * Processes the direction from the sprite {@code s1} to the sprite {@code s2}, considering the 
	 * level is cyclic (i.e. using the shortest path).
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @return
	 * 		The resulting direction. 
	 */
	public Direction getDirection(Sprite s1, Sprite s2)
	{	return DirectionTools.getDirection(s1,s2,pixelHeight,pixelWidth);
	}
	
	/**
	 * Processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		The resulting direction. 
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	return DirectionTools.getDirection(x1,y1,x2,y2,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * Processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @return 
	 * 		The resulting direction. 
	 */
	public Direction getHorizontalDirection(double x1, double x2)
	{	return DirectionTools.getHorizontalDirection(x1,x2,pixelLeftX,pixelWidth);
	}

	/**
	 * Processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		The resulting direction. 
	 */
	public Direction getVerticalDirection(double y1, double y2)
	{	return DirectionTools.getVerticalDirection(y1,y2,pixelTopY,pixelHeight);
	}

	/////////////////////////////////////////////////////////////////
	// APPROXIMATE DIRECTIONS	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the direction from s1 to s2, considering approximate 
	 * distance (i.e. it will be {@code NONE} even if s1 and s2 are just relatively close).
	 * It also considers the level circularity, i.e. it will choose the
	 * directions corresponding to the shortest distances.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @return 
	 * 		The resulting direction. 
	 */
	public Direction getCompositeFromSprites(Sprite s1, Sprite s2)
	{	return DirectionTools.getCompositeFromSprites(s1,s2);
	}

	/**
	 * Processes the direction from the sprite in the specified {@link MoveZone} to the specified obstacle.
	 * Approximate distance is considered (i.e. it will be {@code NONE} even if s1 and s2 
	 * are just relatively close). It also considers the level circularity, 
	 * i.e. it will choose the directions corresponding to the shortest distances.
	 *  
	 * @param mz
	 * 		Move zone.
	 * @param obstacle 
	 * 		Considered obstacle.
	 * @return 
	 * 		The resulting direction. 
	 */
	public Direction getCompositeFromSprites(MoveZone mz, Sprite obstacle)
	{	return DirectionTools.getCompositeFromSprites(mz,obstacle);
	}
	
	/////////////////////////////////////////////////////////////////
	// DELTAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the difference between x1 and x2, considering the level
	 * is cyclic and using the specified direction.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @return 
	 * 		The processed difference.
	 */
	public double getDeltaX(double x1, double x2, Direction direction)
	{	return DeltaTools.getDeltaX(x1,x2,direction,pixelLeftX,pixelWidth);
	}
	
	/**
	 * Returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @return 
	 * 		The processed difference.
	 */
	public double getDeltaX(double x1, double x2)
	{	return DeltaTools.getDeltaX(x1,x2,pixelLeftX,pixelWidth);
	}

	/**
	 * Returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @return 
	 * 		The processed difference.
	 */
	public double getDeltaY(double y1, double y2, Direction direction)
	{	return DeltaTools.getDeltaY(y1,y2,direction,pixelTopY,pixelHeight);
	}
	
	/**
	 * Returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		The processed difference.
	 */
	public double getDeltaY(double y1, double y2)
	{	return DeltaTools.getDeltaY(y1,y2,pixelTopY,pixelHeight);
	}

	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Get the tile corresponding to the specified position.
	 * 
	 * @param x
	 * 		x coordinate of the considered position.
	 * @param y
	 * 		y coordinate of the considered position.
	 * @return
	 * 		The tile at the specified location.
	 */
	public Tile getTile(double x, double y)
	{	int[] coord = PositionTools.getTile(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth,globalHeight,globalWidth);
		Tile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	/**
	 * Get the neighbor tile for the specified direction.
	 * 
	 * @param row
	 * 		Row of the reference tile.
	 * @param col
	 * 		Col of the reference tile.
	 * @param direction
	 * 		Direction of the neighbor.
	 * @return
	 * 		Neighbor tile.
	 */
	public Tile getNeighborTile(int row, int col, Direction direction)
	{	int[] coord = PositionTools.getNeighborTile(row,col,direction,globalHeight,globalWidth);
		Tile result = matrix[coord[0]][coord[1]];
		return result;
	}
	
	/**
	 * Gets all neighboring tiles of the specified tile.
	 * 
	 * @param row
	 * 		Row of the reference tile.
	 * @param col
	 * 		Col of the reference tile.
	 * @return
	 * 		Neighbor tiles.
	 */
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

	/**
	 * Normalizes the specified position, in order to get some coordinates
	 * actually contained in the level.
	 * 
	 * @param row
	 * 		Row number.
	 * @param col
	 * 		Col number.
	 * @return
	 * 		Normalized coordinates.
	 */
	public int[] normalizePosition(int row, int col)
	{	return PositionTools.normalizePosition(row,col,globalHeight,globalWidth);
	}

	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param row
	 * 		Row number.
	 * @return
	 * 		Normalized coordinate.
	 */
	public int normalizePositionRow(int row)
	{	return PositionTools.normalizePositionRow(row,globalHeight);
	}

	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param col
	 * 		Col number.
	 * @return
	 * 		Normalized coordinate.
	 */
	public int normalizePositionCol(int col)
	{	return PositionTools.normalizePositionRow(col,globalHeight);
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified position, in order to get some coordinates
	 * actually contained in the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param y
	 * 		Position on the y axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public double[] normalizePosition(double x, double y)
	{	return PositionTools.normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public double normalizePositionX(double x)
	{	return PositionTools.normalizePositionX(x,pixelLeftX,pixelWidth);
	}
	
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param y
	 * 		Position on the y axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public double normalizePositionY(double y)
	{	return PositionTools.normalizePositionY(y,pixelTopY,pixelHeight);
	}
	
	/**
	 * Checks if the specified position is inside the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param y
	 * 		Position on the y axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public boolean isInsidePosition(double x, double y)
	{	return PositionTools.isInsidePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}
	
	/**
	 * Checks if the specified position is inside the level,
	 * in terms of x axis.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public boolean isInsidePositionX(double x)
	{	return PositionTools.isInsidePositionX(x,pixelLeftX,pixelWidth);
	}
	
	/**
	 * Checks if the specified position is inside the level,
	 * in terms of x axis.
	 * 
	 * @param y
	 * 		Position on the y axis.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public boolean isInsidePositionY(double y)
	{	return PositionTools.isInsidePositionY(y,pixelTopY,pixelHeight);
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Don't remember what this is...*/
	@SuppressWarnings("unused")
	private double horizontalBorderX;
	/** This neither...*/
	@SuppressWarnings("unused")
	private double verticalBorderY;
	
	/** Horizontal coordinate of the end of the left vertical border */
	@SuppressWarnings("unused")
	private double leftBorderX;
	/** Horizontal oordinate of the beginning end of the right vertical border */
	private double rightBorderX;
	/** Height of the horizontal border, in pixels */
	private double horizontalBorderHeight;
	/** Width of the horizontal border, in pixels */
	private double horizontalBorderWidth;

	/** Vertical coordinate of the end of the up horizontal border */
	@SuppressWarnings("unused")
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
	
	/**
	 * Updates the sprite in this level.
	 */
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
	
	/**
	 * Draws the sprite from this level, using the specified
	 * graphics object.
	 * 
	 * @param g
	 * 		Graphics object used for drawing.
	 * 
	 * NOTE optimisation : à effectuer seulement pour les tiles visibles
	 */
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
	/**
	 * Fetch a game event to all sprites in this level.
	 * 
	 * @param event
	 * 		Event to fetch.
	 */
	public void spreadEvent(AbstractEvent event)
	{	for(int row=0;row<globalHeight;row++)
		{	for(int col=0;col<globalWidth;col++)
			{	Tile tile = matrix[row][col];
				tile.spreadEvent(event);
			}
		}
	}

	/**
	 * Fetch a game event to all sprites holding the
	 * specified role in this level.
	 * 
	 * @param event
	 * 		Event to fetch.
	 * @param role
	 * 		Targeted role.
	 */
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
	/**
	 * Returns the time needed by sprites of the specified
	 * role, to appear at the begining of a round.
	 * 
	 * @param role
	 * 		Targeted role.
	 * @return
	 * 		Time needed to appear.
	 */
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
