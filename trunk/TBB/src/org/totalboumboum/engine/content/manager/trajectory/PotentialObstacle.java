package org.totalboumboum.engine.content.manager.trajectory;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.movehigh.SpecificMoveHigh;
import org.totalboumboum.engine.content.feature.action.movelow.SpecificMoveLow;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CombinatoricsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PotentialObstacle
{	private MoveZone moveZone;
	double tileDimension;

	public PotentialObstacle(Sprite sprite, MoveZone moveZone)
	{	this.sprite = sprite;
		this.moveZone = moveZone;
		tileDimension = RoundVariables.scaledTileDimension;
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
		double distX = RoundVariables.level.getHorizontalPixelDistance(posX,currentX);
		double distY = RoundVariables.level.getVerticalPixelDistance(posY,currentY);
		spriteDistance = distX+distY;
		// if there's already an intersection between the sprite and this potential obstacle
		if(!CombinatoricsTools.isRelativelyGreaterOrEqualTo(distX,tileDimension) 
				&& !CombinatoricsTools.isRelativelyGreaterOrEqualTo(distY,tileDimension))
		//if(distX<tileDimension && distY<tileDimension)
		{	contactX = currentX;
			contactY = currentY;
			contactDistance = -1;
		}
		// else we need to process the intersection point (contact point)
		else
		{	// special case : sprite located in an angle of the obstacle safe zone
			if(CombinatoricsTools.isRelativelyEqualTo(distX,tileDimension) 
					&& CombinatoricsTools.isRelativelyEqualTo(distY,tileDimension))
			{	double interX[] = {RoundVariables.level.normalizePositionX(posX - tileDimension), RoundVariables.level.normalizePositionX(posX + tileDimension)};
				double interY[] = {RoundVariables.level.normalizePositionY(posY - tileDimension), RoundVariables.level.normalizePositionY(posY + tileDimension)};
				double distanceX[] = {RoundVariables.level.getHorizontalPixelDistance(interX[0], currentX), RoundVariables.level.getHorizontalPixelDistance(interX[1], currentX)};
				double distanceY[] = {RoundVariables.level.getVerticalPixelDistance(interY[0], currentY), RoundVariables.level.getVerticalPixelDistance(interY[1], currentY)};
				double deltaX = RoundVariables.level.getDeltaX(currentX,targetX);
				double deltaY = RoundVariables.level.getDeltaY(currentY,targetY);
				if(CombinatoricsTools.isRelativelyEqualTo(distanceX[0],0) 
						&& CombinatoricsTools.isRelativelyEqualTo(distanceY[0],0) 
						&& CombinatoricsTools.isRelativelyGreaterThan(deltaX,0)
						&& CombinatoricsTools.isRelativelyGreaterThan(deltaY,0)
					|| CombinatoricsTools.isRelativelyEqualTo(distanceX[0],0) 
						&& CombinatoricsTools.isRelativelyEqualTo(distanceY[1],0) 
						&& CombinatoricsTools.isRelativelyGreaterThan(deltaX,0)
						&& CombinatoricsTools.isRelativelySmallerThan(deltaY,0)
					|| CombinatoricsTools.isRelativelyEqualTo(distanceX[1],0) 
						&& CombinatoricsTools.isRelativelyEqualTo(distanceY[0],0) 
						&& CombinatoricsTools.isRelativelySmallerThan(deltaX,0)
						&& CombinatoricsTools.isRelativelyGreaterThan(deltaY,0)
					|| CombinatoricsTools.isRelativelyEqualTo(distanceX[1],0) 
						&& CombinatoricsTools.isRelativelyEqualTo(distanceY[1],0) 
						&& CombinatoricsTools.isRelativelySmallerThan(deltaX,0)
						&& CombinatoricsTools.isRelativelySmallerThan(deltaY,0))
				
				{	contactX = currentX;
					contactY = currentY;
					contactDistance = 0;
				}
			}
			// intersection with a vertical side of the obstacle safe zone
			{	double interX[] = {RoundVariables.level.normalizePositionX(posX - tileDimension), RoundVariables.level.normalizePositionX(posX + tileDimension)};
				// for each side
				for(int i=0;i<interX.length;i++)
				{	// the side has to be on the sprite way
					double interSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaX(currentX,interX[i]));
					if(interSignum==0)
						interSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaX(currentX,posX));
					double targetSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaX(currentX,targetX));
					if(interSignum==targetSignum)				
					{	Double interY = moveZone.projectVertically(interX[i]);
						// if there's an intersection point between the side and the trajectory 
						if(interY!=null)
						{	double projectionDist = RoundVariables.level.getVerticalPixelDistance(posY,interY);
							double sourceDist = RoundVariables.level.getHorizontalPixelDistance(currentX,interX[i]) + RoundVariables.level.getVerticalPixelDistance(currentY,interY);
							// smaller source-intersection distance and (critical projection distance or equal distance and diagonal trajectory) 
							if(sourceDist<contactDistance && 
								(!CombinatoricsTools.isRelativelyGreaterOrEqualTo(projectionDist,tileDimension)
								|| CombinatoricsTools.isRelativelyEqualTo(projectionDist,tileDimension) && CombinatoricsTools.isRelativelyEqualTo(RoundVariables.level.getDeltaY(posY,interY)/RoundVariables.level.getDeltaX(posX,interX[i]),moveZone.getTrajectoryA())))
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
			{	double interY[] = {RoundVariables.level.normalizePositionY(posY - tileDimension), RoundVariables.level.normalizePositionY(posY + tileDimension)};
				// for each side
				for(int i=0;i<interY.length;i++)
				{	// the side has to be on the sprite way
					double interSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaY(currentY,interY[i]));
					if(interSignum==0)
						interSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaY(currentY,posY));
					double targetSignum = CombinatoricsTools.relativeSignum(RoundVariables.level.getDeltaY(currentY,targetY));
					if(interSignum==targetSignum)						
					{	Double interX = moveZone.projectHorizontally(interY[i]);
						// is there an intersection point between side and trajectory 
						if(interX!=null)
						{	double projectionDist = RoundVariables.level.getHorizontalPixelDistance(posX,interX);
							double sourceDist = RoundVariables.level.getHorizontalPixelDistance(currentX,interX) + RoundVariables.level.getVerticalPixelDistance(currentY,interY[i]);
							// smaller source-intersection distance and (critical projection distance or equal distance and diagonal trajectory) 
							if(sourceDist<contactDistance && 
								(!CombinatoricsTools.isRelativelyGreaterOrEqualTo(projectionDist,tileDimension)
								|| CombinatoricsTools.isRelativelyEqualTo(projectionDist,tileDimension) && CombinatoricsTools.isRelativelyEqualTo(RoundVariables.level.getDeltaY(posY,interY[i])/RoundVariables.level.getDeltaX(posX,interX),moveZone.getTrajectoryA())))
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
		double down = RoundVariables.level.normalizePositionY(sprite.getCurrentPosY() + tileDimension);
		double up = RoundVariables.level.normalizePositionY(sprite.getCurrentPosY() - tileDimension);
		double left = RoundVariables.level.normalizePositionX(sprite.getCurrentPosX() - tileDimension);
		double right = RoundVariables.level.normalizePositionX(sprite.getCurrentPosX() + tileDimension);
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
	{	// init
		boolean result;
		Direction usedDirection = moveZone.getUsedDirection();
		Sprite source = moveZone.getSourceSprite();
		Circumstance actorCircumstance = new Circumstance();
		Circumstance targetCircumstance = new Circumstance();
		targetCircumstance.initCircumstance();
		
		// contact
		double distX = RoundVariables.level.getHorizontalPixelDistance(sprite.getCurrentPosX(),moveZone.getCurrentX());
		double distY = RoundVariables.level.getVerticalPixelDistance(sprite.getCurrentPosY(),moveZone.getCurrentY());
		Contact contact = Contact.COLLISION;
		if(!CombinatoricsTools.isRelativelyGreaterOrEqualTo(distX,tileDimension) && !CombinatoricsTools.isRelativelyGreaterOrEqualTo(distY,tileDimension))
			// intersection iff the distance is relatively smaller than the tile size  
			contact = Contact.INTERSECTION;
		actorCircumstance.setContact(contact);
		
		// tile position
		Tile currentTile = RoundVariables.level.getTile(moveZone.getCurrentX(),moveZone.getCurrentY());
		Tile spriteTile = sprite.getTile();
		TilePosition tilePosition = TilePosition.NEIGHBOR;
		if(currentTile == spriteTile)
			tilePosition = TilePosition.SAME;
		actorCircumstance.setTilePosition(tilePosition);

		// orientation
		Orientation orientation;
		if(usedDirection==Direction.NONE)
			orientation = Orientation.NONE;
		else
		{	Direction relativeDir = RoundVariables.level.getCompositeFromSprites(moveZone,sprite);
			// actor facing target
			if(relativeDir.hasCommonComponent(usedDirection))
				orientation = Orientation.FACE;
			// actor back to target
			else if(relativeDir.hasCommonComponent(usedDirection.getOpposite()))
				orientation = Orientation.BACK;
			// no direction
			else if(relativeDir==Direction.NONE)
				orientation = Orientation.NEUTRAL;
			// other directions
			else
				orientation = Orientation.OTHER;
		}
		actorCircumstance.setOrientation(orientation);
		
		// action
		SpecificAction specificAction;
		if(source.isOnGround())
			specificAction = new SpecificMoveLow(source,usedDirection);
		else
			specificAction = new SpecificMoveHigh(source,usedDirection);
		
		// testing the action
		//	TODO ça serait plus logique d'utiliser le résultat de la modulation
		//	(ça tiendrait compte d'interactions entre les différents modulateurs). 
		//	mais ça serait aussi plus long, donc à voir...
		result = sprite.isThirdPreventing(specificAction,actorCircumstance,targetCircumstance);
				
/* NOTE OLD VERSION		
		// with intersection
		if(!CalculusTools.isRelativelyGreaterOrEqualTo(distX,tileDimension) && !CalculusTools.isRelativelyGreaterOrEqualTo(distY,tileDimension))
//		if(distX<tileDimension && distY<tileDimension)
		{	Tile currentTile = GameVariables.level.getTile(moveZone.getCurrentX(),moveZone.getCurrentY());
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
				//  TODO ça serait plus logique d'utiliser le résultat de la modulation (ça tiendrait
				//  compte d'interactions entre les différents modulateurs). mais ça serait aussi plus long,
				//  donc à voir... (même remarque 20 lignes dessous)
				if(sprite.isThirdPreventing(specificAction))
				{	double deltaX = GameVariables.level.getDeltaX(moveZone.getCurrentX(),sprite.getCurrentPosX());
					double deltaY = GameVariables.level.getDeltaY(moveZone.getCurrentY(),sprite.getCurrentPosY());
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
*/
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	return sprite.toString();		
	}
}
