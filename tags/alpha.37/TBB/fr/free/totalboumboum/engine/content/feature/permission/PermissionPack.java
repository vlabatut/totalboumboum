package fr.free.totalboumboum.engine.content.feature.permission;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.Sprite;


public class PermissionPack
{	private HashMap<String,PermissionGesture> gestures;
	
	public PermissionPack()
	{	gestures = new HashMap<String,PermissionGesture>();
	}

	public StateModulation getModulation(String gestureName, StateModulation permission)
	{	PermissionGesture gesture = gestures.get(gestureName);
		StateModulation result = null;
		if(gesture!=null)
			result = gesture.getModulation(permission);
		return result;
	}
	public ArrayList<StateModulation> getModulations(String gestureName)
	{	PermissionGesture gesture = gestures.get(gestureName);
		ArrayList<StateModulation> result = new ArrayList<StateModulation>();
		if(gesture!=null)
			result = gesture.getModulations();
		return result;
	}
	public ActorPermission getActorPermission(String gestureName, SpecificAction action)
	{	PermissionGesture gesture = gestures.get(gestureName);
		ActorPermission result = null;
		if(gesture!=null)
			result = gesture.getActorPermission(action);
		return result;
	}
	public TargetPermission getTargetPermission(String gestureName, SpecificAction action)
	{	PermissionGesture gesture = gestures.get(gestureName);
		TargetPermission result = null;
		if(gesture!=null)
			result = gesture.getTargetPermission(action);
		return result;
	}
	public ThirdPermission getThirdPermission(String gestureName, SpecificAction action)
	{	PermissionGesture gesture = gestures.get(gestureName);
		ThirdPermission result = null;
		if(gesture!=null)
			result = gesture.getThirdPermission(action);
		return result;
	}
	
	public void addPermissionGesture(PermissionGesture permissionGesture)
	{	gestures.put(permissionGesture.getName(), permissionGesture);
	}
	public void setPermissionGesture(String name,PermissionGesture permissionGesture)
	{	gestures.put(name,permissionGesture);
	}
	
	public void setSprite(Sprite sprite)
	{	Iterator<Entry<String,PermissionGesture>> i = gestures.entrySet().iterator();
		while(i.hasNext())
		{	Entry<String,PermissionGesture> temp = i.next();
			PermissionGesture perm = temp.getValue();
			perm.setSprite(sprite);
		}
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<String,PermissionGesture>> it = gestures.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,PermissionGesture> t = it.next();
					PermissionGesture temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
