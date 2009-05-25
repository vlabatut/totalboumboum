package fr.free.totalboumboum.engine.content.feature.ability;

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

public abstract class AbstractAbility
{	
	public static final String MAXIMUM_VALUE = "maximum";
	
	protected Level level;
	
	/** 
	 * numeric parameter (ability is disabled if <=0) 
	 */
	protected float strength;
	protected float max;
	/** 
	 * framing ability (true) or normal one (false)
	 * framing means the existing ability level is framed by this ability's one 
	 */
	protected boolean frame;
	/** 
	 * maximum number of uses for this ability
	 * negative uses means no use limit 
	 * */
	protected int uses;
	/** 
	 * time limit for using this ability
	 * negative time means no time limit 
	 * */
	protected double time;

	
	public AbstractAbility(Level level)
	{	this.level = level;
		max = Float.MAX_VALUE;
		strength = 0;
		time = -1;
		uses = -1;
	}
	protected void setMax(float max)
	{	this.max = max;
	}
	
	public abstract String getName();
	
	public boolean isActive()
	{	return strength>0;		
	}
	public float getStrength()
	{	float result = strength;
		if(strength>max)
			result = max;
		return result;
	}
	public void modifyStrength(float delta)
	{	this.strength = this.strength + delta;
	}
	public void setStrength(float strength)
	{	this.strength = strength;
	}

	public boolean getFrame()
	{	return frame;
	}
	public void setFrame(boolean frame)
	{	this.frame = frame;
	}	

	public int getUses()
	{	return uses;
	}
	public void setUses(int uses)
	{	this.uses = uses;
	}
	public void decrementUse(int delta)
	{	if(uses>=delta)
			uses = uses - delta;
		else if(uses>0)
			uses = 0;
	}

	public double getTime()
	{	return time;
	}
	public void setTime(double time)
	{	this.time = time;
	}
	public void decrementTime(double delta)
	{	if(time>=delta)
			time = time - delta;
		else if(time>0)
			time = 0;
	}

	public abstract AbstractAbility copy();
	
	/**
	 * combine the strength of the Ability parameter
	 * with the strength of this Ability,
	 * according to the framing effects.
	 * If both abilities are framing, the minimal
	 * value is used. 
	 */
	public void combine(AbstractAbility ability)
	{	float newStrength;
		if(frame)
			if(ability.getFrame())
				newStrength = Math.min(strength, ability.getStrength());
			else
				newStrength = strength;
		else
			if(ability.getFrame())
				newStrength = ability.getStrength();
			else
				newStrength = strength+ability.getStrength();
		strength = newStrength;
	}
	
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// level
			level = null;
		}
	}
}
