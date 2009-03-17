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
	
	private Sprite sprite;
	public Sprite getSprite()
	{	return sprite;
	}
	
	
	private double distance;
	private double intersectionX,intersectionY;
	public double getDistance()
	{	return distance;		
	}
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
		double sourceX = moveZone.getSourceX();
		double sourceY = moveZone.getSourceY();
		// intersection with a vertical side of the obstacle safe zone
		{	double interX[] = {posX - GameConstants.STANDARD_TILE_DIMENSION, posX + GameConstants.STANDARD_TILE_DIMENSION};
			// for each side
			for(int i=0;i<interX.length;i++)
			{	double interY = moveZone.projectVertically(interX[i]);
				// is there an intersection point between side and trajectory 
				if(interY!=Double.NaN)
				{	double projectionDist = Math.abs(posY - interY);
					double sourceDist = Math.abs(sourceX - interX[i]) + Math.abs(sourceY - interY);
					// critical projection distance and smaller source-intersection distance 
					if(projectionDist<GameConstants.STANDARD_TILE_DIMENSION && sourceDist<distance)
					{	intersectionX = interX[i];
						intersectionY = interY;
						distance = projectionDist;
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
					double sourceDist = Math.abs(sourceX - interX) + Math.abs(sourceY - interY[i]);
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
	
	public boolean isActualObstacle()
	{	boolean result;
		Sprite source = moveZone.getSourceSprite();
		Direction dir = moveZone.getInitialDirection();
		String act = AbstractAction.MOVELOW;
		if(!sprite.isOnGround())
			act = AbstractAction.MOVEHIGH;
		SpecificAction specificAction = new SpecificAction(act,source,null,dir);
		ThirdPermission permission = sprite.getThirdPermission(specificAction);
		result = permission==null;
		return result;
	}
}
