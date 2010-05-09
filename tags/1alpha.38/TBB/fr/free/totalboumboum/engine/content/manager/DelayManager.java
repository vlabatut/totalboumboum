package fr.free.totalboumboum.engine.content.manager;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;



public class DelayManager
{	public static final String DL_EXPLOSION = "DL_EXPLOSION";
	public static final String DL_LATENCY = "DL_LATENCY";
	public static final String DL_OSCILLATION = "DL_OSCILLATION";
	public static final String DL_SPAWN = "DL_SPAWN";
	public static final String DL_WAIT = "DL_WAIT";
	
	private Sprite sprite;
	private HashMap<String,Double> delays;

	public DelayManager(Sprite sprite)
	{	this.sprite = sprite;
		delays = new HashMap<String,Double>();
	}

	public void update()
	{	Iterator<Entry<String,Double>> i = delays.entrySet().iterator();
		while(i.hasNext())
		{	Entry<String,Double> temp = i.next();
			String name = temp.getKey();
			double duration = temp.getValue();
			double period = sprite.getConfiguration().getMilliPeriod();
			double speedCoeff = sprite.getConfiguration().getSpeedCoeff();
			duration = duration - period*speedCoeff;
			if(duration<=0)
			{	i.remove();
				sprite.processEvent(new EngineEvent(EngineEvent.DELAY_OVER,name));
			}
			else
				temp.setValue(duration);			
		}
	}
	
	public void addDelay(String name, double duration)
	{	delays.put(name, duration);		
	}
	public void removeDelay(String name)
	{	delays.remove(name);		
	}
	/**
	 * returns the current delay associated to the name parameter,
	 * or a negative value if no delay is associated to the name. 
	 * @param name
	 * @return	a double corresponding to a delay
	 */
	public double getDelay(String name)
	{	double result = -1;
		if(delays.containsKey(name))
			result = delays.get(name);
		return result;
	}
	public boolean hasDelay(String name)
	{	return delays.containsKey(name);		
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// delays
			{	Iterator<Entry<String,Double>> it = delays.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,Double> t = it.next();
					it.remove();
				}
			}
			// misc
			sprite = null;
		}
	}
}
