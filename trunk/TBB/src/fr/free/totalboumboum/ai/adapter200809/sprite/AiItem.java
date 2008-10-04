package fr.free.totalboumboum.ai.adapter200809.sprite;

import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class AiItem extends AiSprite<Item>
{
	public AiItem(AiTile tile, Item sprite)
	{	super(tile,sprite);
		initType(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String type;
	
	public String getType()
	{	return type;	
	}
	private void initType(Item item)
	{	type = item.getItemName();		
	}
	
}
