package fr.free.totalboumboum.engine.content.sprite.hero;

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


public class HeroFactory extends SpriteFactory<Hero>
{	
	public HeroFactory(Level level)
	{	super(level);
	}	
	
	public Hero makeSprite()
	{	// init
		Hero result = new Hero(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// delay
//		double value = configuration.getHeroSetting(Configuration.HERO_SETTING_WAIT_DELAY);
//		result.addDelay(DelayManager.DL_WAIT,value);
		// event
		EventManager eventManager = new HeroEventManager(result);
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