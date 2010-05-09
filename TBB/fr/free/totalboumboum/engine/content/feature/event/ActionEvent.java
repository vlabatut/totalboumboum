package fr.free.totalboumboum.engine.content.feature.event;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;

public class ActionEvent extends AbstractEvent
{	
	
	
	
	public static final String ENV_COMBUSTION = "ENV_COMBUSTION";
	public static final String ENV_ITEM_ENCOUNTERED = "ENV_ITEM_ENCOUNTERED";
//	public static final String ENV_TERMINATED_BOMB = "ENV_TERMINATED_BOMB";
	
	private SpecificAction action;
	
	public ActionEvent(SpecificAction action)
	{	this.action = action;	
	}
	
	public SpecificAction getAction()
	{	return action;	
	}
	
	public void finish()
	{	if(!finished)
		{	super.finish();
			// action
			action.finish();
			action = null;
		}
	}
}
