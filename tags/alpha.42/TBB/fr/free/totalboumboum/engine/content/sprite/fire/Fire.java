package fr.free.totalboumboum.engine.content.sprite.fire;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;


public class Fire extends Sprite
{	public Fire(Level level)
	{	super(level);
	}	

	public void consumeTile(Tile tile)
	{	ArrayList<Sprite> sprites = tile.getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext())
		{	Sprite ts = i.next();
			consumeSprite(ts);
		}	
	}
	
	public void consumeSprite(Sprite sprite)
	{	SpecificAction specificAction = new SpecificAction(AbstractAction.CONSUME,this,sprite,Direction.NONE);
		ActionAbility ability = computeAbility(specificAction);
		if(ability.isActive())
		{	ActionEvent e = new ActionEvent(specificAction);
			sprite.processEvent(e);
		}
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			owner = null;
		}
	}
}
