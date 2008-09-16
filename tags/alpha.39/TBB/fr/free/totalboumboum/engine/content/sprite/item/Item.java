package fr.free.totalboumboum.engine.content.sprite.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;


public class Item extends Sprite
{	/** abilities given by this item */
	private ArrayList<AbstractAbility> itemAbilities;
	private String itemName;
	
	public Item(Level level)
	{	super(level);
	itemAbilities = new ArrayList<AbstractAbility>();
	}

	public ArrayList<AbstractAbility> getItemAbilities()
	{	return itemAbilities;
	}
	public void addItemAbilities(ArrayList<AbstractAbility> abilities)
	{	Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
			addItemAbility(i.next());
	}
	public void addItemAbility(AbstractAbility ability)
	{	AbstractAbility copy = ability.copy();
		itemAbilities.add(copy);
	}
	
	public String getItemName()
	{	return itemName;
	}
	public void setItemName(String itemName)
	{	this.itemName = itemName;
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
			// item abilities
			{	Iterator<AbstractAbility> it = itemAbilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
