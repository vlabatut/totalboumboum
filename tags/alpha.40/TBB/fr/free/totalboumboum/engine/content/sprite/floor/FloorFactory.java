package fr.free.totalboumboum.engine.content.sprite.floor;

import java.util.ArrayList;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;
import fr.free.totalboumboum.engine.loop.Loop;


public class FloorFactory extends SpriteFactory<Floor>
{	
	public FloorFactory(Level level)
	{	super(level);
	}	
	
	public Floor makeSprite()
	{	// init
		Floor result = new Floor(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// event
		EventManager eventManager = new FloorEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
