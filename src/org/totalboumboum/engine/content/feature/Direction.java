package org.totalboumboum.engine.content.feature;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.tools.computing.ApproximationTools;
import org.totalboumboum.tools.xml.XmlNames;

/**
 * Symbols representing move directions: up, down, etc.
 * <br/> 
 * Note we distinguish primary <i>directions</i> (down, left, right, up)
 * and <i>composite</i> directions (upleft, downright, etc.).
 * 
 * @author Vincent Labatut
 */
public enum Direction implements Serializable
{
	/////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** No direction at all */
	NONE,
	/** Primary right direction */
	RIGHT,
	/** Composite direction containing both down and right */
	DOWNRIGHT,
	/** Primary down direction */
	DOWN,
	/** Composite direction containing both down and left */
	DOWNLEFT,
	/** Primary left direction */
	LEFT,
	/** Composite direction containing both up and right */
	UPLEFT,
	/** Primary up direction */
	UP,
	/** Composite direction containing both up and right */
	UPRIGHT;
	
	/////////////////////////////////////////////////////////////////
	// ORDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the next direction according to the following (cycling) order:
	 * {@link #UP},{@link #UPRIGHT},{@link #RIGHT},{@link #DOWNRIGHT},{@link #DOWN},
	 * {@link #DOWNLEFT},{@link #LEFT},{@link #UPLEFT},{@link #UP}.
	 * 
	 * @return
	 * 		The next direction.
	 */
	public Direction getNext()
	{	Direction result;
		switch(this)
		{	case RIGHT:
				result = DOWNRIGHT;
				break;
			case DOWNRIGHT:
				result = DOWN;
				break;
			case DOWN:
				result = DOWNLEFT;
				break;
			case DOWNLEFT:
				result = LEFT;
				break;
			case LEFT:
				result = UPLEFT;
				break;
			case UPLEFT:
				result = UP;
				break;
			case UP:
				result = UPRIGHT;
				break;
			case UPRIGHT:
				result = RIGHT;
				break;
			default:
				result = NONE;
				break;
		}
		return result;
	}

	/**
	 * Returns the previous direction according to the following (cycling) order:
	 * {@link #UP},{@link #UPRIGHT},{@link #RIGHT},{@link #DOWNRIGHT},{@link #DOWN},
	 * {@link #DOWNLEFT},{@link #LEFT},{@link #UPLEFT},{@link #UP}.
	 * 
	 * @return
	 * 		The previous direction.
	 */
	public Direction getPrevious()
	{	Direction result;
		switch(this)
		{	case RIGHT:
				result = UPRIGHT;
				break;
			case DOWNRIGHT:
				result = RIGHT;
				break;
			case DOWN:
				result = DOWNRIGHT;
				break;
			case DOWNLEFT:
				result = DOWN;
				break;
			case LEFT:
				result = DOWNLEFT;
				break;
			case UPLEFT:
				result = LEFT;
				break;
			case UP:
				result = UPLEFT;
				break;
			case UPRIGHT:
				result = UP;
				break;
			default:
				result = NONE;
				break;
		}
		return result;
	}
	
	/**
	 * Tests if this direction is located just after or just before the specified one,
	 * according to the following (cycling) order:
	 * {@link #UP},{@link #UPRIGHT},{@link #RIGHT},{@link #DOWNRIGHT},{@link #DOWN},
	 * {@link #DOWNLEFT},{@link #LEFT},{@link #UPLEFT},{@link #UP}.
	 * 
	 * @param direction 
	 * 		Direction of interest.
	 * @return
	 * 		{@code true} iff the specified direction is right before or after this one.
	 */
	public boolean isConsecutive(Direction direction)
	{	return this==direction || getNext()==direction || getPrevious()==direction;
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the opposite direction ({@link #RIGHT} for {@link #LEFT}, 
	 * {@link #UPRIGHT} for {@link #DOWNLEFT}, etc).
	 * 
	 * @return
	 * 		Opposite direction.
	 */
	public Direction getOpposite()
	{	Direction result = NONE;
		if(this!=NONE)
			result = getNext().getNext().getNext().getNext();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PRIMARY DIRECTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Gets the horizontal component of this direction
	 * (can be {@link #NONE}, e.g. for {@link #DOWN}).
	 * 
	 * @return
	 * 		A primary direction, or {@link #NONE}.
	 */
	public Direction getHorizontalPrimary()
	{	Direction result;
		if(isHorizontal())
			result = this;
		else if(this==DOWNRIGHT)
			result = RIGHT;
		else if(this==DOWNLEFT)
			result = LEFT;
		else if(this==UPLEFT)
			result = LEFT;
		else if(this==UPRIGHT)
			result = RIGHT;
		else
			result = NONE;
		return result;
	}

	/**
	 * Gets the vertical component of this direction
	 * (can be {@link #NONE}, e.g. for {@link #RIGHT}).
	 * 
	 * @return
	 * 		A primary direction, or {@link #NONE}.
	 */
	public Direction getVerticalPrimary()
	{	Direction result;
		if(isVertical())
			result = this;
		else if(this==DOWNRIGHT)
			result = DOWN;
		else if(this==DOWNLEFT)
			result = DOWN;
		else if(this==UPLEFT)
			result = UP;
		else if(this==UPRIGHT)
			result = UP;
		else
			result = NONE;
		return result;
	}
	
	/**
	 * Returns an array with both primary components of this direction:
	 * <ul>
	 * 		<li>result[0]: horizontal direction</li>
	 * 		<li>result[1]: vertical direction</li>
	 * </ul>
	 * 
	 * @return
	 * 		An array of two primary direction (or {@link #NONE}).
	 */
	public Direction[] getPrimaries()
	{	Direction result[] = new Direction[2];
		result[0] = NONE;
		result[1] = NONE;
		//
		if(isPrimary())
		{	if(isHorizontal())
				result[0] = this;
			else
				result[1] = this;
		}
		else if(this!=NONE)
		{	if(getPrevious().isHorizontal())
			{	result[0] = getPrevious();
				result[1] = getNext();
			}
			else
			{	result[0] = getNext();
				result[1] = getPrevious();
			}
		}
		return result;
	}

	/**
	 * Returns the next primary direction according to the following (cycling) order:
	 * {@link #UP},{@link #RIGHT},{@link #DOWN},{@link #LEFT},{@link #UP}.
	 * 
	 * @return
	 * 		A primary direction, or {@link #NONE}.
	 */
	public Direction getNextPrimary()
	{	Direction result;
		if(isPrimary())
			result = getNext().getNext();
		else if(this==NONE)
			result = NONE;
		else
			result = getNext();
		return result;
	}
	
	/**
	 * Returns the previous primary direction according to the following (cycling) order:
	 * {@link #UP},{@link #RIGHT},{@link #DOWN},{@link #LEFT},{@link #UP}.
	 * 
	 * @return
	 * 		A primary direction, or {@link #NONE}.
	 */
	public Direction getPreviousPrimary()
	{	Direction result;
		if(isPrimary())
			result = getPrevious().getPrevious();
		else if(this==NONE)
			result = NONE;
		else
			result = getPrevious();
		return result;
	}
	
	/**
	 * Returns the list of all primary directions
	 * (i.e.: {@link #DOWN}, {@link #LEFT}, {@link #RIGHT} and {@link #UP})
	 * 
	 * @return
	 * 		List of directions.
	 */
	public static List<Direction> getPrimaryValues()
	{	List<Direction> result = new ArrayList<Direction>();
		result.add(DOWN);
		result.add(LEFT);
		result.add(RIGHT);
		result.add(UP);
		return result;
	}
	
	/**
	 * Tests if this direction is primary (and not composite).
	 * <br/>
	 * {@link #NONE} is <i>not</i> a primary direction.
	 * 
	 * @return
	 * 		{@code true} iff this direction is primary.
	 */
	public boolean isPrimary()
	{	boolean result;
		result = this==DOWN || this==LEFT || this==RIGHT || this==UP; 
		return result;
	}

	/**
	 * Tests if this direction is only a vertical primary direction
	 * (and not a composite one, nor {@link #NONE}).
	 * 
	 * @return
	 * 		{@code true} iff this direction is {@link #DOWN} or {@link #UP}.
	 */
	public boolean isVertical()
	{	return this==DOWN || this==UP;
	}

	/**
	 * Tests if this direction is only a horizontal primary direction
	 * (and not a composite one, or {@link #NONE}).
	 * 
	 * @return
	 * 		{@code true} iff this direction is {@link #LEFT} or {@link #RIGHT}.
	 */
	public boolean isHorizontal()
	{	return this==LEFT || this==RIGHT;
	}
	
	/**
	 * Returns the horizontal direction corresponding to the specified delta
	 * (will be {@link #NONE} iff dx=0).
	 * 
	 * @param dx 
	 * 		Difference of horizontal positions.
	 * @return
	 * 		An horizontal direction.
	 */
	public static Direction getHorizontalFromDouble(double dx)
	{	Direction result = NONE;
		if(dx>0)
			result = RIGHT;
		else if(dx<0)
			result = LEFT;
		return result;
	}

	/**
	 * Returns the vertical direction corresponding to the specified delta
	 * (will be {@link #NONE} iff dy=0).
	 * 
	 * @param dy
	 * 		Difference of vertical positions.
	 * @return
	 * 		A vertical direction.
	 */
	public static Direction getVerticalFromDouble(double dy)
	{	Direction result = NONE;
		if(dy>0)
			result = DOWN;
		else if(dy<0)
			result = UP;
		return result;
	}
	/////////////////////////////////////////////////////////////////
	// COMPOSITE DIRECTIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Tests if this direction is composite (and not a primary or {@link #NONE}).
	 * 
	 * @return
	 * 		{@code true} iff this direction is composite.
	 */
	public boolean isComposite()
	{	boolean result;
		result = this==UPRIGHT || this==DOWNLEFT || this==DOWNRIGHT || this==UPLEFT; 
		return result;
	}

	/**
	 * Rests if this direction contains the specified primary component.
	 * 
	 * @param d 
	 * 		A primary direction.
	 * @return
	 * 		{@code true} iff this direction contains the specified direction.
	 */
	public boolean containsPrimary(Direction d)
	{	boolean result = getHorizontalPrimary() == d;
		if(!result)
			result = getVerticalPrimary() == d;
		return result;
	}

	/**
	 * Uses the both specified primary (or {@link #NONE}) directions to
	 * build and return a composite direction.
	 * <br/>
	 * The first specified direction must be horizontal and the second vertical.
	 * 
	 * @param p1 
	 * 		Horizontal direction.
	 * @param p2 
	 * 		Vertical direction.
	 * @return 
	 * 		Direction resulting of the merge of the specified directions.
	 */
	public static Direction getComposite(Direction p1, Direction p2)
	{	Direction resultat = NONE;
		if(p1==NONE)
			resultat = p2;
		else if(p2==NONE)
			resultat = p1;
		else if(p1.getNextPrimary()==p2)
			resultat = p1.getNext();
		else if(p1.getPreviousPrimary()==p2)
			resultat = p1.getPrevious();
		return resultat;
	}
	
	/**
	 * Uses both specified deltas to build and return a composite direction.
	 * <br/>
	 * dx represents the difference in location relatively to the x axis,
	 * whereas dy is for the y axis. 
	 * <br/>
	 * The comparison is not relative, i.e. even if dx is almost zero, it's still not zero
	 * and a direction can be processed.
	 * 
	 * @param dx 
	 * 		Difference of horizontal positions.
	 * @param dy 
	 * 		Difference of vertical positions.
	 * @return 
	 * 		Direction resulting of the merge of the specified differences.
	 */
	public static Direction getCompositeFromDouble(double dx, double dy)
	{	Direction horizontal = getHorizontalFromDouble(dx);
		Direction vertical = getVerticalFromDouble(dy);
		Direction result = getComposite(horizontal,vertical);		
		return result;
	}
	
	/**
	 * Uses both specified deltas to build and return a composite direction.
	 * <br/>
	 * dx represents the difference in location relatively to the x axis,
	 * whereas dy is for the y axis.
	 * <br/>
	 * The comparison is relative, i.e. if dx is almost zero, it's considered
	 * to be zero, and the corresponding direction is NONE.
	 * 
	 * @param dx 
	 * 		Difference of horizontal positions.
	 * @param dy 
	 * 		Difference of vertical positions.
	 * @return 
	 * 		Direction resulting of the merge of the specified differences.
	 */
	public static Direction getCompositeFromRelativeDouble(double dx, double dy)
	{	// horizontal component
		Direction horizontal;
		if(ApproximationTools.isRelativelyEqualTo(dx,0))
			horizontal = Direction.NONE;
		else
			horizontal = getHorizontalFromDouble(dx);
		// vertical component
		Direction vertical;
		if(ApproximationTools.isRelativelyEqualTo(dy,0))
			vertical = Direction.NONE;
		else
			vertical = getVerticalFromDouble(dy);
		// compose the components
		Direction result = getComposite(horizontal,vertical);
		return result;
	}
	
	/**
	 * Tests if this direction and the specified one have common components.
	 * 
	 * @param d 
	 * 		Direction of interest.
	 * @return 
	 * 		{@code true} iff both direction have at least one common component.
	 */
	public boolean hasCommonComponent(Direction d)
	{	boolean result = false; 
		Direction thisH = getHorizontalPrimary();
		Direction thisV = getVerticalPrimary();
		Direction dH = d.getHorizontalPrimary();
		Direction dV = d.getVerticalPrimary();
		if(thisH==dH && thisH!=NONE)
			result = true;
		else if(thisV==dV && thisV!=NONE)
			result = true;
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// TRANSFORMATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the direction resulting from adding the specified
	 * direction to this direction.
	 * 
	 * @param direction 
	 * 		Additional component.
	 * @return 
	 * 		Resulting direction.
	 */
	public Direction put(Direction direction)
	{	Direction result;
		Direction p1[] = getPrimaries();
		Direction p2[] = direction.getPrimaries();
		Direction p3[] = new Direction[2];
		//
		if(p1[0]==p2[0] || p2[0]==NONE)
			p3[0] = p1[0];
		else
			p3[0] = p2[0];
		//
		if(p1[1]==p2[1] || p2[1]==NONE)
			p3[1] = p1[1];
		else
			p3[1] = p2[1];
		//
		result = getComposite(p3[0],p3[1]);
		return result;
	}	

	/**
	 * Processes the direction resulting from removing the specified
	 * direction from this direction.
	 * 
	 * @param direction 
	 * 		Component to be removed.
	 * @return 
	 * 		Resulting direction.
	 */
	public Direction drop(Direction direction)
	{	Direction result;
		Direction p1[] = getPrimaries();
		Direction p2[] = direction.getPrimaries();
		Direction p3[] = new Direction[2];
		//
		if(p1[0]!=p2[0] || p2[0]==NONE)
			p3[0] = p1[0];
		else
			p3[0] = NONE;
		//
		if(p1[1]!=p2[1] || p2[1]==NONE)
			p3[1] = p1[1];
		else
			p3[1] = NONE;
		//
		result = getComposite(p3[0],p3[1]);
		return result;
	}	
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Tests if the specified directions are opposite, 
	 * eg. {@link #RIGHT} and {@link #LEFT}, {@link #UPRIGHT} and {@link #DOWNLEFT}, etc. 
	 * 
	 * @param d1 
	 * 		First direction.
	 * @param d2 
	 * 		Second direction.
	 * @return 
	 * 		{@code true} iff both specified directions are opposite.
	 */
	public static boolean areOpposite(Direction d1, Direction d2)
	{	boolean result = false;
		result = d1 == d2.getOpposite();
		return result;
	}
	
	/**
	 * Returns an array of two integer values corresponding to
	 * a numeric representation of this direction.
	 * <ul>
	 * 		<li>result[0]: +1={@link #RIGHT}, -1={@link #LEFT}</li>
	 * 		<li>result[1]: +1={@link #DOWN}, -1={@link #UP}</li>
	 * </ul>
	 * 
	 * @return 
	 * 		Array of 2 integers representing the direction components.
	 */
	public int[] getIntFromDirection()
	{	int result[] = new int[2];
		Direction p[] = getPrimaries();
		// horizontal
		if(p[0]==LEFT)
			result[0] = -1;
		else if(p[0]==RIGHT)
			result[0] = +1;
		else
			result[0] = 0;
		// vertical
		if(p[1]==UP)
			result[1] = -1;
		else if(p[1]==DOWN)
			result[1] = +1;
		else
			result[1] = 0;
		//
		return result;
	}
	
	/**
	 * Returns a random primary direction.
	 * 
	 * @return
	 * 		A randomly drawn primary direction.
	 */
	public static Direction getRandomPrimaryDirection()
	{	Direction result = NONE;
		int val = (int)(Math.random()*4);
		switch(val)
		{	case 0:
				result = LEFT;
				break;
			case 1:
				result = DOWN;
				break;
			case 2:
				result = RIGHT;
				break;
			case 3:
				result = UP;
				break;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// LOADING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO à utiliser dans tous les loaders
	/**
	 * Loads a direction value from an XML element.
	 * <ul>
	 * 		<li>The XML value {@code SOME} represents any direction except {@link #NONE}.</li> 
	 * 		<li>The XML value {@code ANY} represents any direction including {@link #NONE}.</li>
	 * <ul> 
	 * 
	 * @param root 
	 * 		XML element.
	 * @param attName 
	 * 		Attribute name.
	 * @return 
	 * 		List of directions described by the element.
	 */
	public static List<Direction> loadDirectionsAttribute(Element root, String attName)
	{	List<Direction> result = new ArrayList<Direction>();
		Attribute attribute = root.getAttribute(attName);
		String directionStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
		String[] directionsStr = directionStr.split(" ");
		for(String str: directionsStr)
		{	if(str.equalsIgnoreCase(XmlNames.VAL_SOME))
			{	result.add(Direction.UP);
				result.add(Direction.UPRIGHT);
				result.add(Direction.RIGHT);
				result.add(Direction.DOWNRIGHT);
				result.add(Direction.DOWN);
				result.add(Direction.DOWNLEFT);
				result.add(Direction.LEFT);
				result.add(Direction.UPLEFT);
			}
			else if(str.equalsIgnoreCase(XmlNames.VAL_ANY))
			{	result.add(Direction.UP);
				result.add(Direction.UPRIGHT);
				result.add(Direction.RIGHT);
				result.add(Direction.DOWNRIGHT);
				result.add(Direction.DOWN);
				result.add(Direction.DOWNLEFT);
				result.add(Direction.LEFT);
				result.add(Direction.UPLEFT);
				result.add(Direction.NONE);
			}
			else
			{	Direction direction = Direction.valueOf(str);
				result.add(direction);
			}
		}
		return result;
	}
}