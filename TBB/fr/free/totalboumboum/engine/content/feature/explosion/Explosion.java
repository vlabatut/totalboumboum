package fr.free.totalboumboum.engine.content.feature.explosion;

import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

public class Explosion
{
	Fireset fireset;

	public Explosion()
	{	
	}
	
	public void setFireset(Fireset fireset)
	{	this.fireset = fireset;	
	}
	
	public Fire makeFire(String name)
	{	Fire result = null;
		result = fireset.makeFire(name);
		return result;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// fireset
			if(fireset!=null)
			{	fireset.finish();
				fireset = null;
			}
		}
	}
}
