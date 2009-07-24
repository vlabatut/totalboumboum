package fr.free.totalboumboum.engine.content.feature;

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
import java.util.Locale;

import org.jdom.Attribute;
import org.jdom.Element;

import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.tools.CalculusTools;
import fr.free.totalboumboum.tools.XmlTools;

public enum Direction
{
	NONE,RIGHT,DOWNRIGHT,DOWN,DOWNLEFT,LEFT,UPLEFT,UP,UPRIGHT;
	
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
	
	/*
	 * case 0 = direction horizontale, case 1 = direction verticale
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

	public Direction getPrimary()
	{	Direction result = Direction.NONE;
		if(isHorizontal())
			result = getHorizontalPrimary();
		else if(isVertical())
			result = getVerticalPrimary();
		return result;	
	}
	
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
	
	public static ArrayList<Direction> getAllPrimaries()
	{	ArrayList<Direction> result = new ArrayList<Direction>();
		result.add(DOWN);
		result.add(LEFT);
		result.add(RIGHT);
		result.add(UP);
		return result;
	}

	public boolean isNeighbor(Direction direction)
	{	return this==direction || getNext()==direction || getPrevious()==direction;
	}
	
	public boolean isPrimary()
	{	boolean result;
		result = this==DOWN || this==LEFT || this==RIGHT || this==UP; 
		return result;
	}

	/**
	 * d�termine si une direction est vraiment composite
	 * (et pas seulement une principale ou NONE)
	 * @return
	 */
	public boolean isComposite()
	{	boolean result;
		result = this==UPRIGHT || this==DOWNLEFT || this==DOWNRIGHT || this==UPLEFT; 
		return result;
	}
	
	/**
	 * returns the opposite direction (RIGHT for LEFT, UPRIGHT for DOWNLEFT, etc)
	 * @return
	 */
	public Direction getOpposite()
	{	Direction result = NONE;
		if(this!=NONE)
			result = getNext().getNext().getNext().getNext();
		return result;
	}
	
	public boolean containsPrimary(Direction d)
	{	boolean result = getHorizontalPrimary() == d;
		if(!result)
			result = getVerticalPrimary() == d;
		return result;
	}
	
	public static boolean areOpposite(Direction d1, Direction d2)
	{	boolean result = false;
		result = d1 == d2.getOpposite();
		return result;
	}
	
	public boolean isVertical()
	{	return this==DOWN || this==UP;
	}

	public boolean isHorizontal()
	{	return this==LEFT || this==RIGHT;
	}
	
	/**
	 * les directions pass�es en param�tres doivent �tre primaires ou NONE :
	 * la premi�re est horizontale et la seconde est verticale
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
	
	public static Direction getHorizontalFromDouble(double dx)
	{	Direction result = NONE;
		if(dx>0)
			result = RIGHT;
		else if(dx<0)
			result = LEFT;
		return result;
	}

	public static Direction getVerticalFromDouble(double dy)
	{	Direction result = NONE;
		if(dy>0)
			result = DOWN;
		else if(dy<0)
			result = UP;
		return result;
	}

	public static Direction getCompositeFromDouble(double dx, double dy)
	{	Direction horizontal = getHorizontalFromDouble(dx);
		Direction vertical = getVerticalFromDouble(dy);
		Direction result = getComposite(horizontal,vertical);		
		return result;
	}
	
	/*
	 * direction from s1 to s2.
	 * warning: consider approximate distance, i.e. will be NONE
	 * if s1 and s2 are relatively close.
	 */
	public static Direction getCompositeFromSprites(Sprite s1, Sprite s2)
	{	Direction result;
		if(s1==null || s2==null)
			result = Direction.NONE;
		else
		{	double dx = s2.getCurrentPosX()-s1.getCurrentPosX();
			double dy = s2.getCurrentPosY()-s1.getCurrentPosY();
			if(CalculusTools.isRelativelyEqualTo(dx,0) && CalculusTools.isRelativelyEqualTo(dy,0))
				result = Direction.NONE;
			else
				result = getCompositeFromDouble(dx,dy);
		}
		return result;
	}
/*	
	public static Direction getCompositeFromSprite(AbstractSprite s, Tile tile)
	{	Direction result;
		double dx = tile.getPosX()-s.getCurrentPosX();
		double dy = tile.getPosY()-s.getCurrentPosY();
		result = getCompositeFromDouble(dx, dy);
		return result;
	}
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
/*	
	public static Direction getDirectionFromString(String string)
	{	Direction result = NONE;
		Direction values[] = values();
		int i=0;
		while(i<values.length && result==NONE)
			if(values[i].getString().equalsIgnoreCase(string))
				result = values[i];
			else
				i++;
		return result;
	}
*/
	
	// TODO � utiliser dans tous les loaders
	/**
	 * load a direction value.
	 * the XML value SOME represents any direction except NONE. 
	 * the XML value ANY represents any direction including NONE. 
	 */
	public static ArrayList<Direction> loadDirectionsAttribute(Element root, String attName)
	{	ArrayList<Direction> result = new ArrayList<Direction>();
		Attribute attribute = root.getAttribute(attName);
		String directionStr = attribute.getValue().trim().toUpperCase(Locale.ENGLISH);
		String[] directionsStr = directionStr.split(" ");
		for(String str: directionsStr)
		{	if(str.equalsIgnoreCase(XmlTools.VAL_SOME))
			{	result.add(Direction.UP);
				result.add(Direction.UPRIGHT);
				result.add(Direction.RIGHT);
				result.add(Direction.DOWNRIGHT);
				result.add(Direction.DOWN);
				result.add(Direction.DOWNLEFT);
				result.add(Direction.LEFT);
				result.add(Direction.UPLEFT);
			}
			else if(str.equalsIgnoreCase(XmlTools.VAL_ANY))
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