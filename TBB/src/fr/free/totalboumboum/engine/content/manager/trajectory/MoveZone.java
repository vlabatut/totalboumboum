package fr.free.totalboumboum.engine.content.manager.trajectory;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

//TODO y va y avoir des problèmes de normalisation des coordonnées (utiliser mod pour fermer la zone)
// (valable pour les coordonnées pixel, mais aussi pour les cases !)

public class MoveZone
{	private Level level;
	private boolean vertical;
	
	/**
	 *  Create a new move zone for a trajectory going from (x1,y1) to (x2,y2).
	 *  The corresponding line equation is processed.
	 *  
	 *  @param	source	moving sprite
	 *  @param	sourceX	starting x position
	 *  @param	sourceY	starting y position
	 *  @param	targetX	theoretical arriving x position  
	 *  @param	targetY	theoretical arriving y position
	 *  @param	level	current level
	 *  @param	initialDirection	requested move direction
	 *  @param	usedDirection	possibily different direction resulting from bypassing an obstacle
	 *  @param	fuelX	remaining X distance  
	 *  @param	fuelY	remaining Y distance  
	 */
	public MoveZone(Sprite source, double sourceX, double sourceY, double targetX, double targetY, Level level, Direction initialDirection, Direction usedDirection, double fuelX, double fuelY)
	{	this.level = level;
		this.source = source;
		this.initialDirection = initialDirection;
		this.usedDirection = usedDirection;
		this.fuelX = fuelX;
		this.fuelY = fuelY;
		this.currentX = sourceX;
		this.currentY = sourceY;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.targetX = targetX;
		this.targetY = targetY;
		vertical = sourceY==targetY;
		if(vertical)
		{	a = sourceX;
			b = Double.NaN;
		}
		else
		{	a = (sourceY-targetY)/(sourceX-targetX);
			b = sourceY - a*sourceX;
		}
	}
	
	private double currentX;
	private double currentY;
	public double getCurrentX()
	{	return currentX;
	}
	public double getCurrentY()
	{	return currentY;
	}

	private double fuelX;
	private double fuelY;
	public double getFuelX()
	{	return fuelX;
	}
	public double getFuelY()
	{	return fuelY;
	}

	private Direction initialDirection;
	private Direction usedDirection;
	public Direction getInitialDirection()
	{	return initialDirection;
	}
	public Direction getUsedDirection()
	{	return usedDirection;
	}
	
	private Sprite source;
	public Sprite getSourceSprite()
	{	return source;
	}
	
	private double sourceX,sourceY,targetX,targetY;
	public double getSourceX()
	{	return sourceX;
	}
	public double getSourceY()
	{	return sourceY;
	}
	public double getTargetX()
	{	return targetX;
	}
	public double getTargetY()
	{	return targetY;
	}
	
	private double a;
	private double b;
	public double getTrajectoryA()
	{	return a;
	}
	public double getTrajectoryB()
	{	return b;
	}
	
	public double projectHorizontally(double y)
	{	double x;
		if(vertical)
			x = a;
		else if(a==0)
			x = b;
		else
			x = (y-b)/a;
		return x;
	}
	public double projectVertically(double x)
	{	double y;
		if(vertical)
			y = Double.NaN;
		else
			y = a*x + b;
		return y;
	}

	/**
	 * Defines a list of tiles approximating the move zone,
	 * i.e. tiles intersecting the trajectory, +/- a safe margin
	 * corresponfing to a tile size.
	 */
	private ArrayList<Tile> getCrossedTiles()
	{	ArrayList<Tile> result = new ArrayList<Tile>();
		// init
		double upleftX = Math.min(sourceX,targetX) - GameConstants.STANDARD_TILE_DIMENSION;
		double upleftY = Math.min(sourceY,targetY) - GameConstants.STANDARD_TILE_DIMENSION;
		double downrightX = Math.max(sourceX,targetX) + GameConstants.STANDARD_TILE_DIMENSION;
		double downrightY = Math.max(sourceY,targetY) + GameConstants.STANDARD_TILE_DIMENSION;
		Tile upleftTile = level.getTile(upleftX,upleftY);
		Tile downrightTile = level.getTile(downrightX,downrightY);
		// process
		int sLine = upleftTile.getLine();
		int tLine = downrightTile.getLine();
		int sCol = upleftTile.getCol();
		int tCol = downrightTile.getCol();
		for(int line=sLine;line<=tLine;line++)
		{	for(int col=sCol;col<=tCol;col++)
			{	Tile temp = level.getTile(line,col);
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Defines the list of potential obstacles included in the crossed tiles.
	 * They're ordered in function of the distance to the trajectory source
	 */
	private ArrayList<PotentialObstacle> getCrossedSprites()
	{	ArrayList<PotentialObstacle> result = new ArrayList<PotentialObstacle>();
		ArrayList<Tile> tiles = getCrossedTiles();
		for(Tile t: tiles)
		{	ArrayList<Sprite> temp = t.getSprites();
			for(Sprite s: temp)
			{	PotentialObstacle o = new PotentialObstacle(s,this);
				if(o.hasIntersection())
					result.add(o);
			}
		}
		Collections.sort(result,new Comparator<PotentialObstacle>()
		{	@Override
			public int compare(PotentialObstacle arg0, PotentialObstacle arg1)
			{	int resultat;
				double dist0 = arg0.getDistance();
				double dist1 = arg1.getDistance();
				if(dist0>dist1)
					resultat = +1;
				else if(dist0<dist1)
					resultat = -1;
				else
					resultat = 0;
				return resultat;
			}
		});
		return result;
	}
	
	public void applyMove()
	{	ArrayList<PotentialObstacle> potentialObstacles = getCrossedSprites();
		Iterator<PotentialObstacle> it = potentialObstacles.iterator();
		boolean goOn = usedDirection!=Direction.NONE;
		while(it.hasNext() && goOn)
		{	PotentialObstacle po = it.next();
			// is the potential obstacle an actual obstacle?
			if(po.isActualObstacle())
			{	// it must be bypassed
				bypassObstacle(po);
				if(fuelX==0 || fuelY==0 || usedDirection==Direction.NONE)
					goOn = false;
			}
		}
	}
	
	private void bypassObstacle(PotentialObstacle po)
	{	double verticalDistance = Math.abs(po.getIntersectionY()-po.getSprite().getCurrentPosY());
		double horizontalDistance = Math.abs(po.getIntersectionX()-po.getSprite().getCurrentPosX());
		if(verticalDistance<horizontalDistance)
			usedDirection = usedDirection.getVerticalPrimary();
		else
			usedDirection = usedDirection.getHorizontalPrimary();
		//TODO
		
		//TODO
	}
}
