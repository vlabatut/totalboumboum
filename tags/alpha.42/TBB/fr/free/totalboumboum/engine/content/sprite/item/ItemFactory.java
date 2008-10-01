package fr.free.totalboumboum.engine.content.sprite.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;
import fr.free.totalboumboum.engine.content.sprite.item.ItemEventManager;
import fr.free.totalboumboum.engine.loop.Loop;


public class ItemFactory extends SpriteFactory<Item>
{	
	private ArrayList<AbstractAbility> itemAbilities;
	private String itemName;
	
	public ItemFactory(Level level, String itemName)
	{	super(level);
		this.itemName = itemName;
	}
	
	public void setItemAbilities(ArrayList<AbstractAbility> itemAbilities)
	{	this.itemAbilities = itemAbilities;
	}
	
	public Item makeSprite()
	{	// init
		Item result = new Item(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// item ability
		result.addItemAbilities(itemAbilities);
		// event
		EventManager eventManager = new ItemEventManager(result);
		result.setEventManager(eventManager);
		// result
//		result.initGesture();
		result.setItemName(itemName);
		return result;
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
