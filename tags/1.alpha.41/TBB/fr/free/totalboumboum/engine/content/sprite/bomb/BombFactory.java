package fr.free.totalboumboum.engine.content.sprite.bomb;

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


public class BombFactory extends SpriteFactory<Bomb>
{	
	private String bombName;
	
	public BombFactory(Level level, String bombName)
	{	super(level);
		this.bombName = bombName;
	}	
	
	public Bomb makeSprite()
	{	// init
		Bomb result = new Bomb(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// delay
//		value = configuration.getBombSetting(Configuration.BOMB_SETTING_LIFETIME);
//		result.addDelay(DelayManager.DL_EXPLOSION,value);
		// event
		EventManager eventManager = new BombEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		result.setBombName(bombName);
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
