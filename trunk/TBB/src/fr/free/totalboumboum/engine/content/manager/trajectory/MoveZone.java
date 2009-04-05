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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.CalculusTools;

//TODO y va y avoir des problèmes de normalisation des coordonnées (utiliser mod pour fermer la zone)
// (valable pour les coordonnées pixel, mais aussi pour les cases !)

//TODO gérer le changement de case, cf la v2 du déplacement

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
	public MoveZone(Sprite source, double currentX, double currentY, double targetX, double targetY, Level level, Direction initialDirection, Direction usedDirection, double fuel)
	{	this.level = level;
		this.source = source;
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
	// LEVEL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Level level;
	
	public Level getLevel()
	{	return level;	
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
	private Direction initialDirection;
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
		{	a = (currentY-targetY)/(currentX-targetX);
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
		return x;
	}
	
	public Double projectVertically(double x)
	{	Double y;
		if(vertical)
			y = null;
		else
			y = a*x + b;
		return y;
	}

	/////////////////////////////////////////////////////////////////
	// COLLIDED SPRITES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Sprite> collidedSprites;
	
	public ArrayList<Sprite> getCollidedSprites()
	{	return collidedSprites;		
	}

	private void addCollidedSprite(Sprite s)
	{	if(!collidedSprites.contains(s))
		collidedSprites.add(s);		
	}

	/////////////////////////////////////////////////////////////////
	// INTERSECTED SPRITES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Sprite> intersectedSprites;
	
	public ArrayList<Sprite> getIntersectedSprites()
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
	private ArrayList<Tile> getCrossedTiles()
	{	ArrayList<Tile> result = new ArrayList<Tile>();
		// init
		double tileDimension = level.getTileDimension();
		double upleftX = Math.min(currentX,targetX) - tileDimension;
		double upleftY = Math.min(currentY,targetY) - tileDimension;
		double downrightX = Math.max(currentX,targetX) + tileDimension;
		double downrightY = Math.max(currentY,targetY) + tileDimension;
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
			{	if(s!=source)
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
	{	
		
if(currentX<168 && usedDirection==Direction.UPLEFT)
	System.out.println();
		
		ArrayList<PotentialObstacle> potentialObstacles = getCrossedSprites();
		boolean goOn = usedDirection!=Direction.NONE;
		while(potentialObstacles.size()>0 && goOn)
		{	PotentialObstacle po = potentialObstacles.get(0);
			// is it an intersected obstacle?
			if(po.getContactDistance()<0)
			{	addIntersectedSprite(po.getSprite());
				// is the potential obstacle an actual obstacle?
				if(po.isActualObstacle())
				{	// get all the intersected obstacles
					ArrayList<PotentialObstacle> temp = new ArrayList<PotentialObstacle>();
					temp.add(po);
					potentialObstacles.remove(0);
					boolean goOn2 = true;
					int i = 0;
					while(i<potentialObstacles.size() && goOn2)
					{	PotentialObstacle po2 = potentialObstacles.get(i);
						if(po2.getContactDistance()<0)
						{	addIntersectedSprite(po2.getSprite());
							if(po2.isActualObstacle())
							{	temp.add(po2);
								potentialObstacles.remove(i);
							}
							else
								i++;
						}
						else
							goOn2 = false;
					}
					// go through them
					updateDirection(temp);
					goOn = canMove();
				}
				else
					potentialObstacles.remove(0);
			}
			// else the obstacle may have to be bypassed
			else
			{	// is the potential obstacle an actual obstacle?
				if(po.isActualObstacle())
				{	
System.out.println("PotentialObstacle:"+po.getSprite().getCurrentPosX()+","+po.getSprite().getCurrentPosY());					
					
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
		result = result && CalculusTools.isRelativelyEqualTo(currentX,targetX,level.getLoop());
		result = result && CalculusTools.isRelativelyEqualTo(currentY,targetY,level.getLoop());
		return result;
	}
	
	/**
	 * update the used direction according to the current intersected obstacles.
	 * (their directions relatively to the sprite are dropped)
	 * @param pos
	 */
	private void updateDirection(ArrayList<PotentialObstacle> pos)
	{	Loop loop = level.getLoop();
		// determine the closest obstacle for each primary direction
		Sprite down = null;
		double downDist = Double.MAX_VALUE;
		Sprite left = null;
		double leftDist = Double.MAX_VALUE;
		Sprite right = null;
		double rightDist = Double.MAX_VALUE;
		Sprite up = null;
		double upDist = Double.MAX_VALUE;
		for(PotentialObstacle po: pos)
		{	Sprite s = po.getSprite();
			// horizontal component
			double dx = s.getCurrentPosX() - currentX;
			Direction dH = Direction.getHorizontalFromDouble(dx);
			if(dH!=Direction.NONE && usedDirection.getHorizontalPrimary()==dH)
			{	double absDelta = Math.abs(dx);
				if(dH==Direction.LEFT && !CalculusTools.isRelativelyGreaterOrEqualThan(absDelta,leftDist,loop))
				//if(dH==Direction.LEFT && absDelta<leftDist)
				{	leftDist = absDelta;
					left = s;
				}
				else if(dH==Direction.RIGHT && !CalculusTools.isRelativelyGreaterOrEqualThan(absDelta,rightDist,loop))
				//else if(dH==Direction.RIGHT && absDelta<rightDist)
				{	rightDist = absDelta;
					right = s;
				}
			}
			// vertical component
			double dy = s.getCurrentPosY() - currentY;
			Direction dV = Direction.getVerticalFromDouble(dy);
			if(dV!=Direction.NONE && usedDirection.getVerticalPrimary()==dV)
			{	double absDelta = Math.abs(dy);
				if(dV==Direction.DOWN && !CalculusTools.isRelativelyGreaterOrEqualThan(absDelta,downDist,loop))
				//if(dV==Direction.DOWN && absDelta<downDist)
				{	downDist = absDelta;
					down = s;
				}
				else if(dV==Direction.UP && !CalculusTools.isRelativelyGreaterOrEqualThan(absDelta,upDist,loop))
				//else if(dV==Direction.UP && absDelta<upDist)
				{	upDist = absDelta;
					up = s;
				}
			}
		}
		// change used direction
		if(down!=null)
		{	previousDirection = usedDirection;
			usedDirection = usedDirection.drop(Direction.DOWN);
			addCollidedSprite(down);
		}
		if(left!=null)
		{	previousDirection = usedDirection;
			usedDirection = usedDirection.drop(Direction.LEFT);
			addCollidedSprite(left);
		}
		if(right!=null)
		{	previousDirection = usedDirection;
			usedDirection = usedDirection.drop(Direction.RIGHT);
			addCollidedSprite(right);
		}
		if(up!=null)
		{	previousDirection = usedDirection;
			usedDirection = usedDirection.drop(Direction.UP);
			addCollidedSprite(up);
		}
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
		{	// if the initital direction was composite
			if(initialDirection.isComposite())
				bypassObstacleCompositeDirection(po);
			// if the initial direction was simple
			else
				bypassObstacleSimpleDirection(po);
		}
	}
	
	private void bypassObstacleCompositeDirection(PotentialObstacle po)
	{	// process the new direction according to the obstacle position
		double verticalDistance = Math.abs(po.getContactY()-po.getSprite().getCurrentPosY());
		double horizontalDistance = Math.abs(po.getContactX()-po.getSprite().getCurrentPosX());
		Direction dir;
		if(horizontalDistance==verticalDistance && previousDirection.isPrimary())
			dir = usedDirection.drop(previousDirection);
		else if(verticalDistance<horizontalDistance)
			dir = usedDirection.getVerticalPrimary();
		else //if(horizontalDistance<verticalDistance)
			dir = usedDirection.getHorizontalPrimary();
		// if the sprite is not completely blocked
		if(dir!=Direction.NONE)
		{	// process safe position
			double avoid[] = po.getSafePosition(currentX,currentY,dir);
			// try to avoid it
			MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],level,initialDirection,dir,fuel);
			mz.applyMove();
			currentX = mz.getCurrentX();
			currentY = mz.getCurrentY();
			fuel = mz.getFuel();
			previousDirection = mz.getUsedDirection();;
			if(!mz.hasArrived())
				usedDirection = mz.getUsedDirection();
			else
			{
//if(currentX<168 && usedDirection==Direction.UPLEFT)
//	System.out.println();
				
/*				
				double dx = targetX - currentX;
				double dy = targetY - currentY;
				usedDirection = Direction.getCompositeFromDouble(dx, dy);
*/				
				
				int tmp[]=usedDirection.getIntFromDirection();
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
			{	double dy = currentY - po.getSprite().getCurrentPosY();
				dist = Math.abs(dy);
				dir = Direction.getVerticalFromDouble(dy);
			}
			else
			{	double dx = currentX - po.getSprite().getCurrentPosX();
				dist = Math.abs(dx);
				dir = Direction.getHorizontalFromDouble(dx);
			}
			// has the sprite an assistance?
			StateAbility ability = source.computeAbility(StateAbility.SPRITE_MOVE_ASSISTANCE);
			double tolerance = ability.getStrength();
			double margin = tolerance*level.getTileDimension();
			if(tolerance==0)
				margin = Double.MAX_VALUE;
			if(dir!=Direction.NONE && CalculusTools.isRelativelyGreaterThan(dist,margin,level.getLoop()))
			{	// process safe position
				double avoid[] = po.getSafePosition(currentX,currentY,dir);
				// check if it's worth moving in this direction (i.e. no other obstacles in the way)
				int d[] = initialDirection.getIntFromDirection();
				MoveZone fake = new MoveZone(source,avoid[0],avoid[1],avoid[0]+d[0],avoid[1]+d[1],level,initialDirection,initialDirection,2);
				fake.applyMove();
				// then try to avoid the obstacle
				if(fake.hasArrived())
				{	MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],level,initialDirection,dir,fuel);
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
			collidedSprites.add(s);
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
		double deltaX = destX-currentX;
		double deltaY = destY-currentY;
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
				currentY = currentY + deltaY;
			}
			else
			{	// else : must solve equation (interstion point)
				// 		line: y = ax + b
				//		circle: (x-currentX)²+(y-currentY)² = fuel²
				double discA = a*a + 1;
				double discB = 2*a*b - 2*currentX - 2*a*currentY;
				double discC = b*b + currentX*currentX - 2*b*currentY + currentY*currentY - fuel*fuel;
				double discDelta = discB*discB - 4*discA*discC;
				double x1 = (-discB + Math.sqrt(discDelta))/(2*discA);
				double x2 = (-discB - Math.sqrt(discDelta))/(2*discA);
				Direction moveDir = Direction.getHorizontalFromDouble(deltaX);
				Direction tempDir = Direction.getHorizontalFromDouble(x1-currentX);
				double solutionX = x1;
				if(tempDir!=moveDir)
					solutionX = x2;
				double solutionY = a*solutionX + b;
				// update
				fuel = 0; 
				currentX = solutionX;
				currentY = solutionY;			
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
