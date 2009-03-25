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

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.permission.ThirdPermission;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class PotentialObstacle
{	private MoveZone moveZone;
	
	public PotentialObstacle(Sprite sprite, MoveZone moveZone)
	{	this.sprite = sprite;
		this.moveZone = moveZone;
		initIntersection();
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// DISTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double distance;
	
	public double getDistance()
	{	return distance;		
	}
	
	/////////////////////////////////////////////////////////////////
	// INTERSECTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double intersectionX;
	private double intersectionY;
	
	public double getIntersectionX()
	{	return intersectionX;
	}
	
	public double getIntersectionY()
	{	return intersectionY;
	}
	
	/**
	 * Process the intersection points between this obstacle safe zone limit and the trajectory.
	 * Only the point closest to the trajectory source is keeped.
	 * Note that there may be no intersection point at all.
	 */
	private void initIntersection()
	{	distance = Double.MAX_VALUE;
		intersectionX = Double.NaN;
		intersectionY = Double.NaN;
		double posX = sprite.getCurrentPosX();
		double posY = sprite.getCurrentPosY();
//		double a = moveZone.getTrajectoryA();
//		double b = moveZone.getTrajectoryB();
		double currentX = moveZone.getCurrentX();
		double currentY = moveZone.getCurrentY();		
		double distX = Math.abs(posX-currentX);
		double distY = Math.abs(posY-currentY);
		// if there's already an intersection between the sprite and this potential obstacle
		if(distX<GameConstants.STANDARD_TILE_DIMENSION && distY<GameConstants.STANDARD_TILE_DIMENSION)
		{	intersectionX = currentX;
			intersectionY = currentY;
			distance = 0;
		}
		// else we need to process the intersection point (contact point)
		else
		{	// intersection with a vertical side of the obstacle safe zone
			{	double interX[] = {posX - GameConstants.STANDARD_TILE_DIMENSION, posX + GameConstants.STANDARD_TILE_DIMENSION};
				// for each side
				for(int i=0;i<interX.length;i++)
				{	double interY = moveZone.projectVertically(interX[i]);
					// is there an intersection point between side and trajectory 
					if(interY!=Double.NaN)
					{	double projectionDist = Math.abs(posY - interY);
						double sourceDist = Math.abs(currentX - interX[i]) + Math.abs(currentY - interY);
						// critical projection distance and smaller source-intersection distance 
						if(projectionDist<GameConstants.STANDARD_TILE_DIMENSION && sourceDist<distance)
						{	intersectionX = interX[i];
							intersectionY = interY;
							distance = projectionDist;
						}
					}
				}
			}
		}
		// intersection with an horizontal side of the obstacle safe zone
		{	double interY[] = {posY - GameConstants.STANDARD_TILE_DIMENSION, posY + GameConstants.STANDARD_TILE_DIMENSION};
			// for each side
			for(int i=0;i<interY.length;i++)
			{	double interX = moveZone.projectHorizontally(interY[i]);
				// is there an intersection point between side and trajectory 
				if(interX!=Double.NaN)
				{	double projectionDist = Math.abs(posX - interX);
					double sourceDist = Math.abs(currentX - interX) + Math.abs(currentY - interY[i]);
					// critical distance, and smaller than the current distance
					if(projectionDist<GameConstants.STANDARD_TILE_DIMENSION && sourceDist<distance)
					{	intersectionX = interX;
						intersectionY = interY[i];
						distance = projectionDist;
					}
				}
			}
		}
	}
	public boolean hasIntersection()
	{	return intersectionX != Double.NaN;			
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Process which corner of this obstacle safe zone will be reached if
	 * following the direction "move" from the position (x,y). This position
	 * is supposed to be a contact point, i.e. a point on the enveloppe of
	 * this obstacle safe zone.
	 * 
	 * @param x	starting x position
	 * @param y	starting y position
	 * @param move	moving direction
	 * @return	(x,y) position of the safe corner
	 */
	public double[] getSafePosition(double x, double y, Direction move)
	{	double result[] = {0,0};
		double down = sprite.getCurrentPosY() + GameConstants.STANDARD_TILE_DIMENSION;
		double up = sprite.getCurrentPosY() - GameConstants.STANDARD_TILE_DIMENSION;
		double left = sprite.getCurrentPosX() - GameConstants.STANDARD_TILE_DIMENSION;
		double right = sprite.getCurrentPosX() + GameConstants.STANDARD_TILE_DIMENSION;
		if(move==Direction.DOWN)
		{	result[0] = x;
			result[1] = down;		
		}
		else if(move==Direction.LEFT)
		{	result[0] = left;
			result[1] = y;		
		} 
		else if(move==Direction.RIGHT)
		{	result[0] = right;
			result[1] = y;		
		} 
		if(move==Direction.DOWN)
		{	result[0] = x;
			result[1] = up;		
		} 
		return result;
	}
	
	/**
	 * Determines if the potential obstacle is an actual obstacle.
	 * If there's no intersection between the current MoveZone position and
	 * the potential obstacle, the result depends on the latter permissions.
	 * If there's intersection, it depends on their respective tiles. If they're
	 * in the same tile, the potential obstacle is not an actual one. If they're 
	 * in different tiles, it depends on the move direction. If the source is 
	 * moving even partially towards the potential obstacle, it's an actual obstacle.
	 * Else, it's not an obstacle.
	 * 
	 * @return
	 */
	public boolean isActualObstacle()
	{	boolean result;
		Direction usedDirection = moveZone.getUsedDirection();
		Sprite source = moveZone.getSourceSprite();
		double distX = Math.abs(sprite.getCurrentPosX()-moveZone.getCurrentX());
		double distY = Math.abs(sprite.getCurrentPosY()-moveZone.getCurrentY());
		// with intersection
		if(distX<GameConstants.STANDARD_TILE_DIMENSION && distY<GameConstants.STANDARD_TILE_DIMENSION)
		{	Tile currentTile = moveZone.getLevel().getTile(moveZone.getCurrentX(),moveZone.getCurrentY());
			Tile spriteTile = sprite.getTile();
			// sprite and potential obstacle in the same tile : not an obstacle
			if(currentTile == spriteTile)
				result = false;
			// sprite and potential obstacle not in the same tile : depends on the direction
			else
			{	// moving towards the potential obstacle : it's an obstacle
				double deltaX = sprite.getCurrentPosX()-moveZone.getCurrentX();
				double deltaY = sprite.getCurrentPosY()-moveZone.getCurrentY();
				Direction dir = Direction.getCompositeFromDouble(deltaX,deltaY);
				if(dir.hasCommonComponent(usedDirection))
					result = true;
				else
					result = false;
			}
		}
		// no intersection : depends only on the potential obstacle properties
		else
		{	String act = AbstractAction.MOVELOW;
			if(!sprite.isOnGround())
				act = AbstractAction.MOVEHIGH;
			SpecificAction specificAction = new SpecificAction(act,source,null,usedDirection);
			ThirdPermission permission = sprite.getThirdPermission(specificAction);
			result = permission==null;
		}
		return result;
	}
}
