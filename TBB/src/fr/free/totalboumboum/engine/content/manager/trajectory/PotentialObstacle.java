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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.movehigh.SpecificMoveHigh;
import fr.free.totalboumboum.engine.content.feature.gesture.action.movelow.SpecificMoveLow;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.CalculusTools;

public class PotentialObstacle
{	private MoveZone moveZone;
	private Level level;
	double tileDimension;

	public PotentialObstacle(Sprite sprite, MoveZone moveZone)
	{	this.sprite = sprite;
		this.moveZone = moveZone;
		level = moveZone.getLevel();
		tileDimension = level.getTileDimension();
		initContact();
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// DISTANCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double contactDistance;
	private double spriteDistance;
	
	public double getContactDistance()
	{	return contactDistance;		
	}
	public double getSpriteDistance()
	{	return spriteDistance;		
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTACT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double contactX;
	private Double contactY;
	
	public double getContactX()
	{	return contactX;
	}
	
	public double getContactY()
	{	return contactY;
	}
	
	/**
	 * Process the intersection points between this obstacle safe zone limit and the trajectory.
	 * Only the point closest to the trajectory source is keeped.
	 * Note that there may be no intersection point at all.
	 */
	private void initContact()
	{	contactDistance = Double.MAX_VALUE;
		contactX = null;
		contactY = null;
		double posX = sprite.getCurrentPosX();
		double posY = sprite.getCurrentPosY();
//		double a = moveZone.getTrajectoryA();
//		double b = moveZone.getTrajectoryB();
		double currentX = moveZone.getCurrentX();
		double currentY = moveZone.getCurrentY();		
		double targetX = moveZone.getTargetX();
		double targetY = moveZone.getTargetY();		
		double distX = level.getHorizontalDistance(posX,currentX);
		double distY = level.getVerticalDistance(posY,currentY);
		spriteDistance = distX+distY;
		// if there's already an intersection between the sprite and this potential obstacle
		Loop loop = level.getLoop();
		if(!CalculusTools.isRelativelyGreaterOrEqualTo(distX,tileDimension,loop) 
				&& !CalculusTools.isRelativelyGreaterOrEqualTo(distY,tileDimension,loop))
		//if(distX<tileDimension && distY<tileDimension)
		{	contactX = currentX;
			contactY = currentY;
			contactDistance = -1;
		}
		// else we need to process the intersection point (contact point)
		else
		{	// special case : sprite located in an angle of the obstacle safe zone
			if(CalculusTools.isRelativelyEqualTo(distX,tileDimension,loop) 
					&& CalculusTools.isRelativelyEqualTo(distY,tileDimension,loop))
			{	double interX[] = {level.normalizePositionX(posX - tileDimension), level.normalizePositionX(posX + tileDimension)};
				double interY[] = {level.normalizePositionY(posY - tileDimension), level.normalizePositionY(posY + tileDimension)};
				double distanceX[] = {level.getHorizontalDistance(interX[0], currentX), level.getHorizontalDistance(interX[1], currentX)};
				double distanceY[] = {level.getVerticalDistance(interY[0], currentY), level.getVerticalDistance(interY[1], currentY)};
				double deltaX = level.getDeltaX(currentX,targetX);
				double deltaY = level.getDeltaY(currentY,targetY);
				if(CalculusTools.isRelativelyEqualTo(distanceX[0],0,loop) 
						&& CalculusTools.isRelativelyEqualTo(distanceY[0],0,loop) 
						&& CalculusTools.isRelativelyGreaterThan(deltaX,0,loop)
						&& CalculusTools.isRelativelyGreaterThan(deltaY,0,loop)
					|| CalculusTools.isRelativelyEqualTo(distanceX[0],0,loop) 
						&& CalculusTools.isRelativelyEqualTo(distanceY[1],0,loop) 
						&& CalculusTools.isRelativelyGreaterThan(deltaX,0,loop)
						&& CalculusTools.isRelativelySmallerThan(deltaY,0,loop)
					|| CalculusTools.isRelativelyEqualTo(distanceX[1],0,loop) 
						&& CalculusTools.isRelativelyEqualTo(distanceY[0],0,loop) 
						&& CalculusTools.isRelativelySmallerThan(deltaX,0,loop)
						&& CalculusTools.isRelativelyGreaterThan(deltaY,0,loop)
					|| CalculusTools.isRelativelyEqualTo(distanceX[1],0,loop) 
						&& CalculusTools.isRelativelyEqualTo(distanceY[1],0,loop) 
						&& CalculusTools.isRelativelySmallerThan(deltaX,0,loop)
						&& CalculusTools.isRelativelySmallerThan(deltaY,0,loop))
				
				{	contactX = currentX;
					contactY = currentY;
					contactDistance = 0;
				}
			}
			// intersection with a vertical side of the obstacle safe zone
			{	double interX[] = {level.normalizePositionX(posX - tileDimension), level.normalizePositionX(posX + tileDimension)};
				// for each side
				for(int i=0;i<interX.length;i++)
				{	// the side has to be on the sprite way
					double interSignum = CalculusTools.relativeSignum(level.getDeltaX(currentX,interX[i]),loop);
					if(interSignum==0)
						interSignum = CalculusTools.relativeSignum(level.getDeltaX(currentX,posX),loop);
					double targetSignum = CalculusTools.relativeSignum(level.getDeltaX(currentX,targetX),loop);
					if(interSignum==targetSignum)				
					{	Double interY = moveZone.projectVertically(interX[i]);
						// if there's an intersection point between the side and the trajectory 
						if(interY!=null)
						{	double projectionDist = level.getVerticalDistance(posY,interY);
							double sourceDist = level.getHorizontalDistance(currentX,interX[i]) + level.getVerticalDistance(currentY,interY);
							// smaller source-intersection distance and (critical projection distance or equal distance and diagonal trajectory) 
							if(sourceDist<contactDistance && 
								(!CalculusTools.isRelativelyGreaterOrEqualTo(projectionDist,tileDimension,loop)
								|| CalculusTools.isRelativelyEqualTo(projectionDist,tileDimension,loop) && CalculusTools.isRelativelyEqualTo(level.getDeltaY(posY,interY)/level.getDeltaX(posX,interX[i]),moveZone.getTrajectoryA(),loop)))
							//if(projectionDist<tileDimension && sourceDist<distance)
							{	contactX = interX[i];
								contactY = interY;
								contactDistance = sourceDist;
							}
						}
					}
				}
			}
			// intersection with an horizontal side of the obstacle safe zone
			{	double interY[] = {level.normalizePositionY(posY - tileDimension), level.normalizePositionY(posY + tileDimension)};
				// for each side
				for(int i=0;i<interY.length;i++)
				{	// the side has to be on the sprite way
					double interSignum = CalculusTools.relativeSignum(level.getDeltaY(currentY,interY[i]),loop);
					if(interSignum==0)
						interSignum = CalculusTools.relativeSignum(level.getDeltaY(currentY,posY),loop);
					double targetSignum = CalculusTools.relativeSignum(level.getDeltaY(currentY,targetY),loop);
					if(interSignum==targetSignum)						
					{	Double interX = moveZone.projectHorizontally(interY[i]);
						// is there an intersection point between side and trajectory 
						if(interX!=null)
						{	double projectionDist = level.getHorizontalDistance(posX,interX);
							double sourceDist = level.getHorizontalDistance(currentX,interX) + level.getVerticalDistance(currentY,interY[i]);
							// smaller source-intersection distance and (critical projection distance or equal distance and diagonal trajectory) 
							if(sourceDist<contactDistance && 
								(!CalculusTools.isRelativelyGreaterOrEqualTo(projectionDist,tileDimension,loop)
								|| CalculusTools.isRelativelyEqualTo(projectionDist,tileDimension,loop) && CalculusTools.isRelativelyEqualTo(level.getDeltaY(posY,interY[i])/level.getDeltaX(posX,interX),moveZone.getTrajectoryA(),loop)))
							//if(projectionDist<tileDimension && sourceDist<distance)
							{	contactX = interX;
								contactY = interY[i];
								contactDistance = sourceDist;
							}
						}
					}
				}
			}
		}
	}
	public boolean hasIntersection()
	{	return contactX != null;			
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Process which point on the limit of this obstacle safe zone will be reached if
	 * the direction "move" is following from the position (x,y). This position
	 * is supposed to be a contact point, i.e. a point on the enveloppe of
	 * this obstacle safe zone, or a point inside this safe zone. The direction is supposed
	 * to be simple (not composite).
	 * 
	 * @param x	starting x position
	 * @param y	starting y position
	 * @param move	moving direction
	 * @return	(x,y) position of the safe point
	 */
	public double[] getSafePosition(double x, double y, Direction move)
	{	double result[] = {0,0};
		double down = level.normalizePositionY(sprite.getCurrentPosY() + tileDimension);
		double up = level.normalizePositionY(sprite.getCurrentPosY() - tileDimension);
		double left = level.normalizePositionX(sprite.getCurrentPosX() - tileDimension);
		double right = level.normalizePositionX(sprite.getCurrentPosX() + tileDimension);
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
		else if(move==Direction.UP)
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
		double distX = level.getHorizontalDistance(sprite.getCurrentPosX(),moveZone.getCurrentX());
		double distY = level.getVerticalDistance(sprite.getCurrentPosY(),moveZone.getCurrentY());
		Loop loop = level.getLoop();
		// with intersection
		if(!CalculusTools.isRelativelyGreaterOrEqualTo(distX,tileDimension,loop) && !CalculusTools.isRelativelyGreaterOrEqualTo(distY,tileDimension,loop))
//		if(distX<tileDimension && distY<tileDimension)
		{	Tile currentTile = level.getTile(moveZone.getCurrentX(),moveZone.getCurrentY());
			Tile spriteTile = sprite.getTile();
			// sprite and potential obstacle in the same tile : not an obstacle
			if(currentTile == spriteTile)
				result = false;
			// sprite and potential obstacle not in the same tile : depends on the ability and/or direction
			else
			{	// non-blocking sprite : it's not an obstacle
				SpecificAction specificAction;
				if(source.isOnGround())
					specificAction = new SpecificMoveLow(source,usedDirection);
				else
					specificAction = new SpecificMoveHigh(source,usedDirection);
				/*
				 *  TODO ça serait plus logique d'utiliser le résultat de la modulation (ça tiendrait
				 *  compte d'interactions entre les différents modulateurs). mais ça serait aussi plus long,
				 *  donc à voir... (même remarque 20 lignes dessous)
				 */
				if(sprite.isThirdPreventing(specificAction))
				{	double deltaX = level.getDeltaX(moveZone.getCurrentX(),sprite.getCurrentPosX());
					double deltaY = level.getDeltaY(moveZone.getCurrentY(),sprite.getCurrentPosY());
					Direction dir = Direction.getCompositeFromDouble(deltaX,deltaY);
					if(dir.hasCommonComponent(usedDirection))
						result = true;
					else
						result = false;
				}
				// blocking sprite and moving towards the potential obstacle : it's an obstacle
				else
					result = false;
			}
		}
		// no intersection : depends only on the potential obstacle properties
		else
		{	SpecificAction specificAction;
			if(source.isOnGround())
				specificAction = new SpecificMoveLow(source,usedDirection);
			else
				specificAction = new SpecificMoveHigh(source,usedDirection);
			result = sprite.isThirdPreventing(specificAction);
		}
		return result;
	}
}
