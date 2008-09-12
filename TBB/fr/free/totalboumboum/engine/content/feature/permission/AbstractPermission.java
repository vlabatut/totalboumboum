package fr.free.totalboumboum.engine.content.feature.permission;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;

public abstract class AbstractPermission
{	
	protected String gestureName; //debug
	
	/** 
	 * modification de la force de l'abileté
	 */
	protected float strength;
	/** 
	 * masquage de la force
	 */
	protected boolean frame;
	
	public AbstractPermission()
	{	strength = 0;
		frame = false;
	}
	
	public boolean getFrame()
	{	return frame;
	}
	public void setFrame(boolean frame)
	{	this.frame = frame;
	}	

	public float getStrength()
	{	return strength;
	}
	public void setStrength(float strength)
	{	this.strength = strength;
	}
	
	public void setGestureName(String gestureName)
	{	this.gestureName = gestureName;		
	}
	public String getGestureName()
	{	return gestureName;		
	}

	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}	
}
