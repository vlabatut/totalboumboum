package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.sprite.item.Item;

public class AiItem extends AiSprite<Item>
{
	public AiItem(AiTile tile, Item sprite)
	{	super(tile,sprite);
		initType();
	}

	@Override
	void update()
	{	super.update();
	}

	@Override
	void finish()
	{	super.finish();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String type;
	
	public String getType()
	{	return type;	
	}
	private void initType()
	{	Item item = getSprite();
		type = item.getItemName();		
	}
	
}
