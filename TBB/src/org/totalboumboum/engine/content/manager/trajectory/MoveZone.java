package org.totalboumboum.engine.content.manager.trajectory;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.computing.ApproximationTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MoveZone
{	private boolean vertical;
	
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
	public MoveZone(Sprite source, double currentX, double currentY, double targetX, double targetY, Direction initialDirection, Direction usedDirection, double fuel)
	{	this.source = source;
		this.initialDirection = initialDirection;
		this.usedDirection = usedDirection;
		previousDirection = Direction.NONE;
		this.fuel = fuel;
		this.currentX = currentX;
		this.currentY = currentY;
		this.targetX = targetX;
		this.targetY = targetY;
		collidedSprites = new ArrayList<Sprite>();
		intersectedSprites = new ArrayList<Sprite>();
		processLine();
	}
	
	/////////////////////////////////////////////////////////////////
	// CURRENT POSITION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double currentX;
	private double currentY;
	
	public double getCurrentX()
	{	return currentX;
	}
	
	public double getCurrentY()
	{	return currentY;
	}

	/////////////////////////////////////////////////////////////////
	// FUEL				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double fuel;
	
	public double getFuel()
	{	return fuel;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** first wanted direction */ 
	private Direction initialDirection;
	/** direction currently used */
	private Direction usedDirection;
	/** previously used direction. only used for ergonomic reasons, when the sprite is at some obstacle exact corner with a composite direction : which primary direction must be used?*/
	private Direction previousDirection;
	
	public Direction getInitialDirection()
	{	return initialDirection;
	}
	
	public Direction getUsedDirection()
	{	return usedDirection;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite source;
	
	public Sprite getSourceSprite()
	{	return source;
	}
	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORY POSITIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	private double sourceX;
//	private double sourceY;
	private double targetX;
	private double targetY;
/*	
	public double getSourceX()
	{	return sourceX;
	}
	
	public double getSourceY()
	{	return sourceY;
	}
*/	
	public double getTargetX()
	{	return targetX;
	}
	
	public double getTargetY()
	{	return targetY;
	}
	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORY LINE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double a;
	private Double b;
	
	public double getTrajectoryA()
	{	return a;
	}
	
	public double getTrajectoryB()
	{	return b;
	}
	
	private void processLine()
	{	vertical = currentX==targetX;
		if(vertical)
		{	a = currentX;
			b = null;
		}
		else
		{	a = RoundVariables.level.getDeltaY(targetY,currentY)/RoundVariables.level.getDeltaX(targetX,currentX);
			if(a==-0.0)
				a = 0.0;
			b = currentY - a*currentX;
		}		
	}
	
	public Double projectHorizontally(double y)
	{	Double x;
		if(vertical)
			x = a;
		else if(a==0)
			x = b;
		else
			x = (y-b)/a;
		x = RoundVariables.level.normalizePositionX(x);
		return x;
	}
	
	public Double projectVertically(double x)
	{	Double y;
		if(vertical)
			y = null;
		else
			y = a*x + b;
		y = RoundVariables.level.normalizePositionY(y);
		return y;
	}

	/////////////////////////////////////////////////////////////////
	// COLLIDED SPRITES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Sprite> collidedSprites;
	
	public List<Sprite> getCollidedSprites()
	{	return collidedSprites;		
	}

	private void addCollidedSprite(Sprite s)
	{	if(!collidedSprites.contains(s))
			collidedSprites.add(s);		
	}

	/////////////////////////////////////////////////////////////////
	// INTERSECTED SPRITES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Sprite> intersectedSprites;
	
	public List<Sprite> getIntersectedSprites()
	{	return intersectedSprites;		
	}

	private void addIntersectedSprite(Sprite s)
	{	if(!intersectedSprites.contains(s))
			intersectedSprites.add(s);		
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Defines a list of tiles approximating the move zone,
	 * i.e. tiles intersecting the trajectory, +/- a safe margin
	 * corresponfing to a tile size.
	 */
	private List<Tile> getCrossedTiles()
	{	List<Tile> result = new ArrayList<Tile>();
		// init
		double tileDimension = RoundVariables.scaledTileDimension;
		double upleftX = RoundVariables.level.normalizePositionX(Math.min(currentX,targetX) - tileDimension);
		double upleftY = RoundVariables.level.normalizePositionY(Math.min(currentY,targetY) - tileDimension);
		double downrightX = RoundVariables.level.normalizePositionX(Math.max(currentX,targetX) + tileDimension);
		double downrightY = RoundVariables.level.normalizePositionY(Math.max(currentY,targetY) + tileDimension);
		Tile upleftTile = RoundVariables.level.getTile(upleftX,upleftY);
		Tile downrightTile = RoundVariables.level.getTile(downrightX,downrightY);
		// process
		int width = RoundVariables.level.getGlobalWidth();
		int height = RoundVariables.level.getGlobalHeight();
		int sRow = upleftTile.getRow();
		int tRow = (downrightTile.getRow()+1)%height;
		int sCol = upleftTile.getCol();
		int tCol = (downrightTile.getCol()+1)%width;
		for(int row=sRow;row!=tRow;row=(row+1)%height)
		{	for(int col=sCol;col!=tCol;col=(col+1)%width)
			{	Tile temp = RoundVariables.level.getTile(row,col);
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Defines the list of potential obstacles included in the crossed tiles.
	 * They're ordered in function of the distance to the trajectory source
	 * (the secondary criterion being the distance between the sprite and the obstacle) 
	 */
	private List<PotentialObstacle> getCrossedSprites()
	{	List<PotentialObstacle> result = new ArrayList<PotentialObstacle>();
		List<Tile> tiles = getCrossedTiles();
		for(Tile t: tiles)
		{	List<Sprite> temp = t.getSprites();
			for(Sprite s: temp)
			{	
//if(s instanceof Item)
//	System.out.println();
				if(s!=source)
				{	PotentialObstacle o = new PotentialObstacle(s,this);
					if(o.hasIntersection())
						result.add(o);
				}
			}
		}
		Collections.sort(result,new Comparator<PotentialObstacle>()
		{	@Override
			public int compare(PotentialObstacle arg0, PotentialObstacle arg1)
			{	int result;
				double dist0 = arg0.getContactDistance();
				double dist1 = arg1.getContactDistance();
				if(dist0>dist1)
					result = +1;
				else if(dist0<dist1)
					result = -1;
				else //if(dist0==dist1)
				{	double d0 = arg0.getSpriteDistance(); 
					double d1 = arg1.getSpriteDistance();
					result = 0;
					if(d0>d1)
						result = +1;
					else if(d0<d1)
						result = -1;
					else //if(d0==d1)
						result = 0;
				}
				return result;
			}
		});
		return result;
	}
	
	public void applyMove()
	{	List<PotentialObstacle> potentialObstacles = getCrossedSprites();
//System.out.println(potentialObstacles.size());	
		boolean goOn = usedDirection!=Direction.NONE;
		while(potentialObstacles.size()>0 && goOn)
		{	PotentialObstacle po = potentialObstacles.get(0);
//if(po.getSprite() instanceof Bomb)
//	System.out.println();
			// is it an intersected obstacle?
			if(po.getContactDistance()<0)
				addIntersectedSprite(po.getSprite()); //NOTE and what if the obstacle is bypassed? shouldn't be in the intersected list
			// is the potential obstacle an actual obstacle?
			if(po.isActualObstacle())
			{	
//System.out.println("PotentialObstacle:"+po.getSprite().getCurrentPosX()+","+po.getSprite().getCurrentPosY()+"("+po.getSprite().getRole()+")");					
				bypassObstacle(po);
				goOn = canMove();
				// process the new trajectory and obstacles
				if(goOn)
				{	processLine();
					potentialObstacles = getCrossedSprites();					
				}
			}
			else
				potentialObstacles.remove(0);
		}
		if(goOn)
		{	// move towards the destination
			moveToPoint(targetX,targetY);
		}
	}
	
	/**
	 * Check if the sprite can move again, depending on its previous
	 * moves and the current direction.
	 * @return
	 */
	private boolean canMove()
	{	boolean result = usedDirection!=Direction.NONE;
		result = result && fuel>0;
		return result;
	}
	
	/**
	 * Check if the sprite has arrived to its destination yet.
	 * @return
	 */
	private boolean hasArrived()
	{	boolean result = true;
		double distX = RoundVariables.level.getHorizontalPixelDistance(currentX,targetX);
		result = result && ApproximationTools.isRelativelyEqualTo(distX,0);
		double distY = RoundVariables.level.getVerticalPixelDistance(currentY,targetY);
		result = result && ApproximationTools.isRelativelyEqualTo(distY,0);
		return result;
	}
		
	/**
	 * Make the sprite avoid an obstacle, if there's enough fuel and no
	 * other critical obstacles.
	 * @param po
	 */
	private void bypassObstacle(PotentialObstacle po)
	{	// move to the contact point
		moveToContactPoint(po);
		// if the sprite can still move
		if(canMove())
		{	// if the initital direction is composite
			if(initialDirection.isComposite())
				bypassObstacleCompositeDirection(po);
			// if the initial direction is simple
			else
				bypassObstacleSimpleDirection(po);
		}
	}
	
	private void bypassObstacleCompositeDirection(PotentialObstacle po)
	{	// init
		double contactX = po.getContactX();
		double contactY = po.getContactY();
		double obstacleX = po.getSprite().getCurrentPosX();
		double obstacleY = po.getSprite().getCurrentPosY();
		
		// process the new direction according to the obstacle position
		double horizontalDistance = RoundVariables.level.getHorizontalPixelDistance(contactX,obstacleX);
		double verticalDistance = RoundVariables.level.getVerticalPixelDistance(contactY,obstacleY);
		Direction dir;
		if(po.getContactDistance()<0)
		{	double deltaX = RoundVariables.level.getDeltaX(currentX,obstacleX);
			double deltaY = RoundVariables.level.getDeltaY(currentY,obstacleY);
			Direction d = Direction.getCompositeFromDouble(deltaX,deltaY);
			dir = usedDirection.drop(d);
//if(dir.isComposite())
//	System.out.println();
		}
		else
		{	if(horizontalDistance==verticalDistance && previousDirection.isPrimary()) //workaround, for ergonomics purpose
				dir = usedDirection.drop(previousDirection);
			else if(verticalDistance<horizontalDistance)
				dir = usedDirection.getVerticalPrimary();
			else //if(horizontalDistance<verticalDistance)
				dir = usedDirection.getHorizontalPrimary();
		}
		// if the sprite is not completely blocked
		if(dir!=Direction.NONE)
		{	// process safe position
			double avoid[] = po.getSafePosition(currentX,currentY,dir);
			// try to reach it
			MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],initialDirection,dir,fuel);
			mz.applyMove();
			currentX = mz.getCurrentX();
			currentY = mz.getCurrentY();
			fuel = mz.getFuel();
			previousDirection = mz.getUsedDirection();;
			if(!mz.hasArrived())
				usedDirection = mz.getUsedDirection();
			else
			{
/*				
				double dx = targetX - currentX;
				double dy = targetY - currentY;
				usedDirection = Direction.getCompositeFromDouble(dx, dy);
*/				
				int tmp[] = usedDirection.getIntFromDirection();
				double tmp2[] = new double[2];
				if(tmp[0]!=0 && tmp[1]!=0)
				{	tmp2[0] = tmp[0]*Math.sqrt(fuel);
					tmp2[1] = tmp[1]*Math.sqrt(fuel);
				}
				else
				{	tmp2[0] = tmp[0]*fuel;
					tmp2[1] = tmp[1]*fuel;
				}					
				targetX = currentX + tmp2[0];
				targetY = currentY + tmp2[1];
				
			}
			for(Sprite s: mz.getCollidedSprites())
				addCollidedSprite(s);
			for(Sprite s: mz.getIntersectedSprites())
				addIntersectedSprite(s);
		}
		else
		{	previousDirection = usedDirection;
			usedDirection = Direction.NONE;
		}
	}

	private void bypassObstacleSimpleDirection(PotentialObstacle po)
	{	// if the sprite is not currently avoiding an obstacle
		if(usedDirection == initialDirection)
		{	// process the new direction (perpendicular)
			Direction dir;
			double dist;
			if(usedDirection.isHorizontal())
			{	double dy = RoundVariables.level.getDeltaY(po.getSprite().getCurrentPosY(),currentY);
				dist = Math.abs(dy);
				dir = Direction.getVerticalFromDouble(dy);
			}
			else
			{	double dx = RoundVariables.level.getDeltaX(po.getSprite().getCurrentPosX(),currentX);
				dist = Math.abs(dx);
				dir = Direction.getHorizontalFromDouble(dx);
			}
			// has the sprite an assistance?
			StateAbility ability = source.modulateStateAbility(StateAbilityName.SPRITE_MOVE_ASSISTANCE);
			double tolerance = ability.getStrength();
			double margin = tolerance*RoundVariables.scaledTileDimension;
			if(tolerance==0)
				margin = Double.MAX_VALUE;
			if(dir!=Direction.NONE && ApproximationTools.isRelativelyGreaterThan(dist,margin))
			{	// process safe position
				double avoid[] = po.getSafePosition(currentX,currentY,dir);
				// check if it's worth moving in this direction (i.e. no other obstacles in the way)
				int d[] = initialDirection.getIntFromDirection();
				double tX = RoundVariables.level.normalizePositionX(avoid[0]+d[0]);
				double tY = RoundVariables.level.normalizePositionY(avoid[1]+d[1]);
				MoveZone fake = new MoveZone(source,avoid[0],avoid[1],tX,tY,initialDirection,initialDirection,2);
				fake.applyMove();
				// then try to avoid the obstacle
				if(fake.hasArrived())
				{	MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],initialDirection,dir,fuel);
					mz.applyMove();
					currentX = mz.getCurrentX();
					currentY = mz.getCurrentY();
					fuel = mz.getFuel();
					previousDirection = mz.getUsedDirection();;
					if(!mz.hasArrived())
						usedDirection = dir;
					else
					{	int tmp[]=usedDirection.getIntFromDirection();
						double tmp2[] = {tmp[0]*fuel,tmp[1]*fuel};
						targetX = RoundVariables.level.normalizePositionX(currentX + tmp2[0]);
						targetY = RoundVariables.level.normalizePositionY(currentY + tmp2[1]);
					}
					for(Sprite s: mz.getCollidedSprites())
						addCollidedSprite(s);
					for(Sprite s: mz.getIntersectedSprites())
						addIntersectedSprite(s);
				}
				else
				{	previousDirection = usedDirection;				
					usedDirection = Direction.NONE;				
				}
			}
			else
			{	previousDirection = usedDirection;
				usedDirection = Direction.NONE;
			}
		}
		else
		{	previousDirection = usedDirection;
			usedDirection = Direction.NONE;		
		}
	}
	
	/**
	 * Process the move from the current position to a contact
	 * position. The path is supposed to be obstacle-free. 
	 * The fuel and current position variables are updated.
	 * Note that for fuel issues, this sprite may not arrive
	 * to the specified point.
	 * @param po
	 */
	private void moveToContactPoint(PotentialObstacle po)
	{	double interX = po.getContactX();
		double interY = po.getContactY();
		boolean reached = moveToPoint(interX,interY);
		if(reached)
		{	Sprite s = po.getSprite();
			addCollidedSprite(s);
		}
	}
	
	/**
	 * Move the sprite to the precised point,
	 * or stop before according to the remaining fuel.
	 * No obstacle is supposed to stand on the way.
	 * 
	 * @param x
	 * @param y
	 */
	private boolean moveToPoint(double destX, double destY)
	{	boolean result;
		double deltaX = RoundVariables.level.getDeltaX(currentX,destX);
		double deltaY = RoundVariables.level.getDeltaY(currentY,destY);
		double dist = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
		// enough fuel
		if(fuel>=dist)
		{	fuel = fuel - dist;
			currentX = destX;
			currentY = destY;
			result = true;
		}
		// must stop before contact
		else
		{	result = false;
			// special case : vertical trajectory
			if(vertical)
			{	deltaY = fuel*Math.signum(deltaY);
				fuel = 0;
				currentY = RoundVariables.level.normalizePositionY(currentY + deltaY);
			}
			else
			{	// else : must solve equation (intersection point)
				// 		line: y' = ax' + b
				//		circle: (x'-x)²+(y'-y)² = fuel²
				//		>> (x'-x)²+(ax'+b-y)² = fuel²
				//		>> x'²+x²-2x'x+(ax'+b)²+y²-2(ax'+b)y - fuel² = 0
				//		>> x'²+x²-2x'x+a²x'²+b²+2abx'+y²-2ax'y-2by - fuel² = 0
				//		>> x'²(1+a²) + x'(2ab-2x-2ay) + (x²+y²+b²-2by-fuel²) = 0
				double discA = a*a + 1;
				double discB = 2*a*b - 2*currentX - 2*a*currentY;
				double discC = currentX*currentX + currentY*currentY + b*b - 2*b*currentY - fuel*fuel;
				double discDelta = discB*discB - 4*discA*discC;
				// enough fuel for computation
				if(discDelta>=0)
				{	double x1 = (-discB + Math.sqrt(discDelta))/(2*discA);
					double x2 = (-discB - Math.sqrt(discDelta))/(2*discA);
					Direction moveDir = Direction.getHorizontalFromDouble(deltaX);
					Direction tempDir = Direction.getHorizontalFromDouble(x1-currentX);
					double solutionX = x1;
					if(tempDir!=moveDir)
						solutionX = x2;
					double solutionY = a*solutionX + b;
					currentX = RoundVariables.level.normalizePositionX(solutionX);
					currentY = RoundVariables.level.normalizePositionY(solutionY);
				}
				fuel = 0; 
/*				
				deltaX = Math.signum(deltaX)*fuelX;
				deltaY = Math.signum(deltaY)*fuelY;
				double yForX = deltaX*a+b;
				double absYforX = Math.abs(yForX);
				double xForY;
				if(a==0)
					xForY = 0;
				else
					xForY = (deltaY-b)/a;
				double absXforY = Math.abs(xForY);
				if(absYforX>fuelY)
				{	fuelX = fuelX - absXforY;
					fuelY = fuelY - fuelY;
					currentX = currentX + xForY;
					currentY = currentY + deltaY;
				}
				else // if(distXforY>fuelX)
				{	fuelX = fuelX - fuelX;
					fuelY = fuelY - absYforX;
					currentX = currentX + deltaX;
					currentY = currentY + yForX;
				}
*/				
			}
		}
		return result;
	}
}
