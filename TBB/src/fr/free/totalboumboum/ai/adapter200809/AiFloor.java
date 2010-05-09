package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.sprite.floor.Floor;

public class AiFloor extends AiSprite<Floor>
{
	AiFloor(AiTile tile, Floor sprite)
	{	super(tile,sprite);		
	}

	@Override
	void update(AiTile tile)
	{	super.update(tile);
	}

	@Override
	void finish()
	{	super.finish();
	}
}
