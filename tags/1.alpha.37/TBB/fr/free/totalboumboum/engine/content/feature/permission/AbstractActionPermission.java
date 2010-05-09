package fr.free.totalboumboum.engine.content.feature.permission;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.Sprite;


public abstract class AbstractActionPermission extends AbstractPermission
{	
	/**
	 * action concernée par cette permission
	 */
	protected GeneralAction action;
	/** 
	 * abilités necessaires à l'acteur pour avoir la permission  
	 */
	private ArrayList<AbstractAbility> actorRestrictions;
	/** 
	 * abilités necessaires à la cible pour avoir la permission  
	 */
	private ArrayList<AbstractAbility> targetRestrictions;
	
	public AbstractActionPermission(GeneralAction action)
	{	super();
		this.action = action;	
		this.actorRestrictions = new ArrayList<AbstractAbility>();
		this.targetRestrictions = new ArrayList<AbstractAbility>();
	}
	
	public GeneralAction getAction()
	{	return action;			
	}
	
	public void addActorRestriction(AbstractAbility ability)
	{	actorRestrictions.add(ability);
	}

	public void addTargetRestriction(AbstractAbility ability)
	{	targetRestrictions.add(ability);
	}
	
	public String toString()
	{	String result = action.toString();
		result = result + "<"+strength+","+frame+">";
		// actor restrictions
		result = result + "[actorRestr:";
		Iterator<AbstractAbility> i = actorRestrictions.iterator();
		while(i.hasNext())
		{	AbstractAbility ab = i.next();
			result = result + ab.toString();
		}
		result = result + "]";
		// actor restrictions
		result = result + "[targetRestr:";
		i = targetRestrictions.iterator();
		while(i.hasNext())
		{	AbstractAbility ab = i.next();
			result = result + ab.toString();
		}
		result = result + "]";
		//
		return result;
	}
	
	public boolean isAllowingAction(SpecificAction specificAction)
	{	boolean result;
		// action
		result = action.subsume(specificAction);
		// actor restrictions
		{	Sprite actor = specificAction.getActor();
			Iterator<AbstractAbility> i = actorRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = actor.getAbility((ActionAbility)ab);
				else
					ab = actor.getAbility((StateAbility)ab);
				result = ab.isActive();
			}
		}
		// target restrictions
		{	Sprite target = specificAction.getTarget();
			Iterator<AbstractAbility> i = targetRestrictions.iterator();
			while(i.hasNext() && result)
			{	AbstractAbility ab = i.next();
				if(ab instanceof ActionAbility)
					ab = target.getAbility((ActionAbility)ab);
				else
					ab = target.getAbility((StateAbility)ab);
				result = ab.isActive();
			}
		}
		//	
		return result;		
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// action
			action.finish();
			action = null;
			// actor restrictions
			{	Iterator<AbstractAbility> it = actorRestrictions.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// target restrictions
			{	Iterator<AbstractAbility> it = targetRestrictions.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}

