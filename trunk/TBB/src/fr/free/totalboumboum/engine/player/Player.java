package fr.free.totalboumboum.engine.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tournament200708.Adapter;

import fr.free.totalboumboum.ai.InterfaceAI;
import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.ControlSettings;
import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.data.profile.ProfileLoader;
import fr.free.totalboumboum.data.statistics.StatisticMatch;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactory;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactoryLoader;
import fr.free.totalboumboum.engine.control.PlayerControl;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class Player
{	
	private Profile profile;
	/** sprite */
	private Sprite sprite;
	/** round */
	private Level level;
	/** artificial intelligence */
	private InterfaceAI ai = null;
	/** control */
	private PlayerControl spriteControl;
	/** current color */
	private PredefinedColor color;
	
	public Player(Profile profile, Level level, ArrayList<AbstractAbility> ablts, PermissionPack permissions, TrajectoryPack trajectories) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	this.level = level;
		configuration = level.getConfiguration();
		this.profile = profile;
		// sprite
		String folder = FileTools.getHeroesPath()+File.separator+this.profile.getSpritePack();
		folder = folder + File.separator+this.profile.getSpriteName();
		HeroFactory tempHeroFactory = HeroFactoryLoader.loadHeroFactory(folder,level,this.profile.getSpriteColor(),ablts,permissions,trajectories);
		sprite = tempHeroFactory.makeSprite();
		sprite.initGesture();
		sprite.setControlSettings(this.profile.getControlSettings());
		sprite.setPlayer(this);
		spriteControl = new PlayerControl(this);
		// artificial intelligence
    	if(this.profile.getAiName() != null)
    	{	ai = new Adapter();
    		ai.setClass(this.profile.getAi());
			ai.setPlayer(this);
    	}
	}

    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;
	}

	public Loop getLoop()
	{	return level.getLoop();	
	}
	
	public Level getLevel()
	{	return level;	
	}
	
	public void update()
	{	if(ai!=null)
			ai.update();
	}
	
	public String getName()
	{	return profile.getName();
	}
	public ControlSettings getControlSettings()
	{	return profile.getControlSettings();
	}
	public PlayerControl getSpriteControl()
	{	return spriteControl;
	}

	public InterfaceAI getArtificialIntelligence()
	{	return ai;
	}

	public Sprite getSprite()
	{	return sprite;
	}
	
	private boolean playerOut = false;
	
	public void setOut()
	{	playerOut = true;
		getLoop().playerOut(this);	
	}
	public boolean isOut()
	{	return playerOut;	
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// control
			spriteControl.finish();
			spriteControl = null;
			// ai
			if(ai!=null)
			{	ai.finish();
				ai = null;
			}
			// misc
			color = null;
			level = null;
			profile = null;
			sprite = null;
		}
	}
}
