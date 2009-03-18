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
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.tools.CalculusTools;

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
	private double sourceX;
	private double sourceY;
	private double targetX;
	private double targetY;
	
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
	
	/////////////////////////////////////////////////////////////////
	// TRAJECTORY LINE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
				if(canMove())
					goOn = false;
			}
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
			double margin = tolerance*GameConstants.STANDARD_TILE_DIMENSION;
			if(tolerance<0)
				margin = Double.MAX_VALUE;
			if(CalculusTools.isRelativelyGreaterThan(dist,margin,level.getLoop()))
			{	// process safe position
				double avoid[] = po.getSafePosition(currentX,currentY,dir);
				// check if it's worth moving in this direction (i.e. no obstacle coming)
//TODO here				
				// try to avoid it
				MoveZone mz = new MoveZone(source,currentX,currentY,avoid[0],avoid[1],level,initialDirection,dir,fuelX,fuelY);
				mz.applyMove();
				currentX = mz.getCurrentX();
				currentY = mz.getCurrentY();
				fuelX = mz.getFuelX();
				fuelY = mz.getFuelY();
				if(!hasArrived())
					usedDirection = dir;
			}
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
		double deltaX = currentX-interX;
		double absDeltaX = Math.abs(deltaX);
		double deltaY = currentY-interY;
		double absDeltaY = Math.abs(deltaY);
		// enough fuel
		if(fuelX>=absDeltaX && fuelY>=absDeltaY)
		{	fuelX = fuelX - absDeltaX;
			fuelY = fuelY - absDeltaY;
			currentX = interX;
			currentY = interY;
//TODO fire event			
		}
		// must stop before contact
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
}
