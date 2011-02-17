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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.tools.calculus.CombinatoricsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class TrajectoryManager
{
	/////////////////////////////////////////////////////////////////
	// INIT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public TrajectoryManager(Sprite sprite)
	{	this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite dirig� par ce TrajectoryManager */
	protected Sprite sprite;

	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * change l'animation en cours d'affichage
	 */
	public abstract void updateGesture(Gesture gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration);

	/////////////////////////////////////////////////////////////////
	// BINDING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite getBoundToSprite()
	{	return sprite.getBoundToSprite();
	}
	
	protected boolean isBoundToSprite()
	{	return sprite.isBoundToSprite();
	}

	/**
	 * modifie la position absolue courante en fonction du boundToSprite.
	 * Cette m�thode doit imp�rativement �tre appel�e juste avant un changement de gesture.
	 * @param newSprite
	 */
	public abstract void setBoundToSprite(Sprite newSprite);
	
	/////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position X pr�c�dente (absolue) */
	protected double previousPosX;
	/** position Y pr�c�dente (absolue) */
	protected double previousPosY;
	/** position Z pr�c�dente (absolue) */
	protected double previousPosZ;

	/**
	 * m�thode appel�e � chaque it�ration
	 * met � jour le d�placement et la position relative.
	 * attention : dans le cas d'une r�p�tition, le dernier point
	 * de la trajectoire sert de premier point � la r�p�tition suivante.
	 * donc si on veut un cycle parfait, il faut rajouter un dernier point ramenant ou premier 
	 */
	public abstract void update();

	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps total �coul� de puis le d�but de la trajectoire */
	protected double currentTime = 0;
	/** dur�e totale effective de la trajectoire */
	protected double totalDuration = 0;
	
	/**
	 * renvoie la dur�e totale pr�vue pour la trajectoire.
	 * @return
	 */
	public double getTotalDuration()
	{	return totalDuration;
	}
	
	public double getCurrentTime()
	{	return currentTime;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract double getCurrentSpeed();
	
	/////////////////////////////////////////////////////////////////
	// POSITION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position X absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	protected double currentPosX = 0;
	/** position Y absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	protected double currentPosY = 0;
	/** position Z absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	protected double currentPosZ = 0;
	/** indique si la trajectoire a impliqu� (pour le moment) que le sprite ait d�coll� du sol */ 
	protected boolean hasFlied;
	
	public double getCurrentPosX()
	{	double result = currentPosX;
		if(isBoundToSprite())
			result = result + sprite.getBoundToSprite().getCurrentPosX();
		return result;
	}
	public void setCurrentPosX(double posX)
	{	currentPosX = posX;
	}
	
	public double getCurrentPosY()
	{	double result = currentPosY;
		if(isBoundToSprite())
			result = result + sprite.getBoundToSprite().getCurrentPosY();
		return result;
	}
	public void setCurrentPosY(double posY)
	{	currentPosY = posY;
	}
	
	public double getCurrentPosZ()
	{	double result = currentPosZ;
		if(isBoundToSprite())
			result = result + sprite.getBoundToSprite().getCurrentPosZ();
		return result;
	}
	public void setCurrentPosZ(double posZ)
	{	currentPosZ = posZ;
	}
	
	public boolean isOnGround()
	{	return CombinatoricsTools.isRelativelyEqualTo(currentPosZ,0);
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** direction de d�placement courante */
	protected Direction currentDirection = Direction.NONE;

	public Direction getActualDirection()
	{	Direction result = currentDirection;
		if(currentDirection==Direction.NONE)
		{	double dx = currentPosX-previousPosX;
			double dy = currentPosY-previousPosY;
			result = Direction.getCompositeFromDouble(dx, dy);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<Sprite> collidedSprites = new ArrayList<Sprite>();
	
	public void addCollidedSprite(Sprite collidedSprite)
	{	collidedSprites.add(collidedSprite);
	}
	public void removeCollidedSprite(Sprite collidedSprite)
	{	collidedSprites.remove(collidedSprite);
	}	

	public boolean isColliding()
	{	return !collidedSprites.isEmpty();
	}
	public boolean isCollidingSprite(Sprite s)
	{	return collidedSprites.contains(s);
	}

	/////////////////////////////////////////////////////////////////
	// INTERSECTIONS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<Sprite> intersectedSprites = new ArrayList<Sprite>();
	
	public void addIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.add(intersectedSprite);
	}
	public void removeIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.remove(intersectedSprite);
	}
	
	public boolean isIntersectingSprite(Sprite s)
	{	return intersectedSprites.contains(s);
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract TrajectoryManager copy(Sprite sprite);
}
