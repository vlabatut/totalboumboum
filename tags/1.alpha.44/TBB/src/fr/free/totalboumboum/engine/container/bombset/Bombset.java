package fr.free.totalboumboum.engine.container.bombset;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;


public class Bombset
{	private ArrayList<BombFactory> bombFactories;
	private ArrayList<ArrayList<StateAbility>> requiredAbilities;
	
	public Bombset()
	{	bombFactories = new ArrayList<BombFactory>();
		requiredAbilities = new ArrayList<ArrayList<StateAbility>>();
	}
	
	private void setBombFactories(ArrayList<BombFactory> bombFactories)
	{	this.bombFactories = bombFactories;
	}
	private void setRequiredAbilities(ArrayList<ArrayList<StateAbility>> requiredAbilities)
	{	this.requiredAbilities = requiredAbilities;
	}
		
	public void addBombFactory(BombFactory bombFactory, ArrayList<StateAbility> abilities)
	{	bombFactories.add(bombFactory);
		requiredAbilities.add(abilities);
	}
	
	public Bomb makeBomb(Sprite sprite)
	{	Bomb result = null;
		Iterator<ArrayList<StateAbility>> i = requiredAbilities.iterator();
		int ind = 0;
		while(result==null && i.hasNext())
		{	ArrayList<StateAbility> abilities = i.next();
			Iterator<StateAbility> j = abilities.iterator();
			boolean goOn = true;
			while(goOn && j.hasNext())
			{	StateAbility ability = j.next();
				StateAbility tp = sprite.getAbility(ability);
				if(tp==null || !tp.isActive())
					goOn = false;
			}
			if(goOn)
			{	BombFactory bf = bombFactories.get(ind);
				result = bf.makeSprite();
				result.setOwner(sprite);
			}
			else
				ind++;
		}
		return result;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<BombFactory> it = bombFactories.iterator();
				while(it.hasNext())
				{	BombFactory temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// abilities
			{	Iterator<ArrayList<StateAbility>> it = requiredAbilities.iterator();
				while(it.hasNext())
				{	ArrayList<StateAbility> temp = it.next();
					Iterator<StateAbility> it2 = temp.iterator();
					while(it2.hasNext())
					{	StateAbility temp2 = it2.next();
						temp2.finish();
						it2.remove();
					}
					it.remove();
				}
			}
		}
	}
}
