package fr.free.totalboumboum.engine.container.fireset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;


public class Fireset
{	private HashMap<String,FireFactory> fireFactories;
	
	public Fireset()
	{	fireFactories = new HashMap<String,FireFactory>();
	}
	
	public void addFireFactory(String name, FireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
	}
	
	public Fire makeFire(String name)
	{	Fire result = null;
		FireFactory fireFactory = fireFactories.get(name);
		result = fireFactory.makeSprite();
		return result;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<Entry<String,FireFactory>> it = fireFactories.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,FireFactory> t = it.next();
					FireFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
