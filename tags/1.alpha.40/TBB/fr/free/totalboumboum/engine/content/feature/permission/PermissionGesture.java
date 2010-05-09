package fr.free.totalboumboum.engine.content.feature.permission;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;


public class PermissionGesture
{	
	private ArrayList<StateModulation> stateModulations;
	private ArrayList<ActorPermission> actorPermissions;
	private ArrayList<TargetPermission> targetPermissions;
	private ArrayList<ThirdPermission> thirdPermissions;
	private String name; //debug
	
	public PermissionGesture()
	{	stateModulations = new ArrayList<StateModulation>();
		actorPermissions = new ArrayList<ActorPermission>();
		targetPermissions = new ArrayList<TargetPermission>();
		thirdPermissions = new ArrayList<ThirdPermission>();
	}

	public void addModulation(StateModulation modulation)
	{	stateModulations.add(modulation);
		modulation.setGestureName(name);
	}
	public void addPermission(AbstractActionPermission permission)
	{	if(permission instanceof ActorPermission)
			actorPermissions.add((ActorPermission)permission);
		else if(permission instanceof TargetPermission)
			targetPermissions.add((TargetPermission)permission);
		else if(permission instanceof ThirdPermission)
			thirdPermissions.add((ThirdPermission)permission);
		permission.setGestureName(name);
	}

	public StateModulation getModulation(StateModulation modulation)
	{	StateModulation result = null;
		int index = stateModulations.indexOf(modulation);
		if(index>=0)
			result = stateModulations.get(index);
		return result;
	}
	public ArrayList<StateModulation> getModulations()
	{	return stateModulations;
	}
	public ActorPermission getActorPermission(SpecificAction action)
	{	ActorPermission result = null;
		Iterator<ActorPermission> i = actorPermissions.iterator();
		while(i.hasNext() && result==null)
		{	ActorPermission perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	public TargetPermission getTargetPermission(SpecificAction action)
	{	TargetPermission result = null;
		Iterator<TargetPermission> i = targetPermissions.iterator();
		while(i.hasNext() && result==null)
		{	TargetPermission perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	public ThirdPermission getThirdPermission(SpecificAction action)
	{	ThirdPermission result = null;
		Iterator<ThirdPermission> i = thirdPermissions.iterator();
		while(i.hasNext() && result==null)
		{	ThirdPermission perm = i.next();
			if(perm.isAllowingAction(action))
				result = perm;
		}
		return result;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public void setSprite(Sprite sprite)
	{	Class<?> c = sprite.getClass();
		// actor
		Iterator<ActorPermission> ia = actorPermissions.iterator();
		while(ia.hasNext())
		{	ActorPermission temp = ia.next();
			temp.getAction().setActor(c);
		}
		// target
		Iterator<TargetPermission> it = targetPermissions.iterator();
		while(it.hasNext())
		{	TargetPermission temp = it.next();
			temp.getAction().setTarget(c);
		}
	}	

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// actor permissions
			{	Iterator<ActorPermission> it = actorPermissions.iterator();
				while(it.hasNext())
				{	ActorPermission temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// state permissions
			{	Iterator<StateModulation> it = stateModulations.iterator();
				while(it.hasNext())
				{	StateModulation temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// target permissions
			{	Iterator<TargetPermission> it = targetPermissions.iterator();
				while(it.hasNext())
				{	TargetPermission temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// third permissions
			{	Iterator<ThirdPermission> it = thirdPermissions.iterator();
				while(it.hasNext())
				{	ThirdPermission temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
	
	public PermissionGesture copy()
	{	PermissionGesture result = new PermissionGesture();
		// actor permissions
		{	Iterator<ActorPermission> it = actorPermissions.iterator();
			while(it.hasNext())
			{	ActorPermission temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		// state permissions
		{	Iterator<StateModulation> it = stateModulations.iterator();
			while(it.hasNext())
			{	StateModulation temp = it.next().copy();
				result.addModulation(temp);
			}
		}
		// target permissions
		{	Iterator<TargetPermission> it = targetPermissions.iterator();
			while(it.hasNext())
			{	TargetPermission temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		// third permissions
		{	Iterator<ThirdPermission> it = thirdPermissions.iterator();
			while(it.hasNext())
			{	ThirdPermission temp = it.next().copy();
				result.addPermission(temp);
			}
		}
		//
		result.finished = finished;
		result.name = name;
		return result;
	}
}
