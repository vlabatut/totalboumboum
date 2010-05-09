package fr.free.totalboumboum.engine.container.itemset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.bomb.BombFactory;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.content.sprite.item.ItemFactory;


public class Itemset
{	
	// components
	private HashMap<String,ItemFactory> itemFactories;

	public Itemset(HashMap<String,ItemFactory> itemFactories) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.itemFactories = itemFactories;
	}
	
	public Item makeItem(String name)
	{	Item result = null;
		ItemFactory itemFactory = itemFactories.get(name);
		result = itemFactory.makeSprite();
		return result;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<Entry<String,ItemFactory>> it = itemFactories.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,ItemFactory> t = it.next();
					ItemFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}

