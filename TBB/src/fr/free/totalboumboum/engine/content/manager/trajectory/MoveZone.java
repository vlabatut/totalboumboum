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
import fr.free.totalboumboum.tools.CalculusTools;

//TODO y va y avoir des probl�mes de normalisation des coordonn�es (utiliser mod pour fermer la zone)
// (valable pour les coordonn�es pixel, mais aussi pour les cases !)

//TODO g�rer le changement de case, cf la v2 du d�placement

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
	public MoveZone(Sprite source, double currentX, double currentY, double targetX, double targetY, Level level, Direction initialDirection, Direction usedDirection, double fuelX, double fuelY)
	{	this.level = level;
		this.source = source;
		this.initialDirection = initialDirection;
		this.usedDirection = usedDirection;
		this.fuelX = fuelX;
		this.fuelY = fuelY;
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
	private double fuelX;
	private double fuelY;
	
	public double getFuelX()
	{	return fuelX;
	}
	
	public double getFuelY()
	{	return fuelY;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Direction initialDirection;
	private Direction usedDirection;
	
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
		boolean goOn = usedDirection!=Direction.NONE;
		while(potentialObstacles.size()>0 && goOn)
		{	PotentialObstacle po = potentialObstacles.get(0);
			// is it an intersected obstacle?
			if(po.getDistance()<0)
			{	addIntersectedSprite(po.getSprite());
				// is the potential obstacle an actual obstacle?
				if(po.isActualObstacle())
				{	// get all the intersected obstacles
					ArrayList<PotentialObstacle> temp = new ArrayList<PotentialObstacle>();
					temp.add(po);
					potentialObstacles.remove(0);
					boolean goOn2 = true;
					while(potentialObstacles.size()>0 && goOn2)
					{	PotentialObstacle po2 = potentialObstacles.get(0);
						if(po.getDistance()<0)
						{	addIntersectedSprite(po.getSprite());
							if(po.isActualObstacle())
							{	temp.add(po2);
								potentialObstacles.remove(0);
							}
						}
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
		if(result && usedDirection.getVerticalPrimary()!=Direction.NONE)
			result = fuelY>0;
		if(result && usedDirection.getHorizontalPrimary()!=Direction.NONE)
			result = fuelX>0;
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
	{	// determine the closest obstacle for each primary direction
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
				if(dH==Direction.LEFT && absDelta<leftDist)
				{	leftDist = absDelta;
					left = s;
				}
				else if(dH==Direction.RIGHT&& absDelta<rightDist)
				{	rightDist = absDelta;
					right = s;
				}
			}
			// vertical component
			double dy = s.getCurrentPosY() - currentY;
			Direction dV = Direction.getVerticalFromDouble(dx);
			if(dV!=Direction.NONE && usedDirection.getVerticalPrimary()==dV)
			{	double absDelta = Math.abs(dy);
				if(dV==Direction.DOWN && absDelta<downDist)
				{	downDist = absDelta;
					down = s;
				}
				else if(dV==Direction.UP&& absDelta<upDist)
				{	upDist = absDelta;
					up = s;
				}
			}
		}
		// change used direction
		if(down!=null)
		{	usedDirection.drop(Direction.DOWN);
			addCollidedSprite(down);
		}
		if(left!=null)
		{	usedDirection.drop(Direction.LEFT);
			addCollidedSprite(left);
		}
		if(right!=null)
		{	usedDirection.drop(Direction.RIGHT);
			addCollidedSprite(right);
		}
		if(up!=null)
		{	usedDirection.drop(Direction.UP);
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
		double verticalDistance = Math.abs(po.getIntersectionY()-po.getSprite().getCurrentPosY());
		double horizontalDistance = Math.abs(po.getIntersectionX()-po.getSprite().getCurrentPosX());
		Direction dir;
		if(verticalDistance<horizontalDistance)
			dir = usedDirection.getVerticalPrimary();
		else
			dir = usedDirection.getHorizontalPrimary();
		// if the sprite is not completely blocked
		if(dir!=Direction.NONE)
		{	// process safe position
			double avoid[] = po.getSafePosition(currentX,currentY,dir);
			// try to avoid it
			MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],level,initialDirection,dir,fuelX,fuelY);
			mz.applyMove();
			currentX = mz.getCurrentX();
			currentY = mz.getCurrentY();
			fuelX = mz.getFuelX();
			fuelY = mz.getFuelY();
			if(!hasArrived())
				usedDirection = dir;
			for(Sprite s: mz.getCollidedSprites())
				addCollidedSprite(s);
			for(Sprite s: mz.getIntersectedSprites())
				addIntersectedSprite(s);
		}
		else
			usedDirection = Direction.NONE;		
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
				dir = Direction.getVerticalFromDouble(dx);
			}
			// has the sprite an assistance?
			StateAbility ability = source.computeAbility(StateAbility.SPRITE_MOVE_ASSISTANCE);
			double tolerance = ability.getStrength();
			double margin = tolerance*level.getTileDimension();
			if(tolerance<0)
				margin = Double.MAX_VALUE;
			if(dir!=Direction.NONE && CalculusTools.isRelativelyGreaterThan(dist,margin,level.getLoop()))
			{	// process safe position
				double avoid[] = po.getSafePosition(currentX,currentY,dir);
				// check if it's worth moving in this direction (i.e. no other obstacles in the way)
				int d[] = initialDirection.getIntFromDirection();
				MoveZone fake = new MoveZone(source,avoid[0],avoid[1],avoid[0]+d[0],avoid[1]+d[1],level,initialDirection,initialDirection,1,1);
				fake.applyMove();
				// then try to avoid the obstacle
				if(fake.hasArrived())
				{	MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],level,initialDirection,dir,fuelX,fuelY);
					mz.applyMove();
					currentX = mz.getCurrentX();
					currentY = mz.getCurrentY();
					fuelX = mz.getFuelX();
					fuelY = mz.getFuelY();
					if(!hasArrived())
						usedDirection = dir;
					for(Sprite s: mz.getCollidedSprites())
						addCollidedSprite(s);
					for(Sprite s: mz.getIntersectedSprites())
						addIntersectedSprite(s);
				}
			}
			else
				usedDirection = Direction.NONE; 
		}
		else
			usedDirection = Direction.NONE; 
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
	{	double interX = po.getIntersectionX();
		double interY = po.getIntersectionY();
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
		double deltaX = currentX-destX;
		double absDeltaX = Math.abs(deltaX);
		double deltaY = currentY-destY;
		double absDeltaY = Math.abs(deltaY);
		// enough fuel
		if(fuelX>=absDeltaX && fuelY>=absDeltaY)
		{	fuelX = fuelX - absDeltaX;
			fuelY = fuelY - absDeltaY;
			currentX = destX;
			currentY = destY;
			result = true;
		}
		// must stop before contact
		else
		{	result = false;
			if(vertical)
			{	fuelY = fuelY - absDeltaY;
				currentY = currentY + deltaY;
			}
			else
			{	deltaX = Math.signum(deltaX)*fuelX;
				deltaY = Math.signum(deltaY)*fuelY;
				double yForX = deltaX*a+b;
				double absYforX = Math.abs(yForX);
				double xForY = (deltaY-b)/a;
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
			}
		}
		return result;
	}
}
