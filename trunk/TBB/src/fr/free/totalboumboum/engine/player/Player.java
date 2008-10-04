package fr.free.totalboumboum.engine.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.ai.AiLoader;
import fr.free.totalboumboum.ai.InterfaceAi;
import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.ControlSettings;
import fr.free.totalboumboum.data.profile.PredefinedColor;
import fr.free.totalboumboum.data.profile.Profile;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactory;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactoryLoader;
import fr.free.totalboumboum.engine.control.PlayerControl;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;

public class Player
{	
	private Profile profile;
	/** sprite */
	private Sprite sprite;
	/** round */
	private Level level;
	/** artificial intelligence */
	private AbstractAiManager<?> ai = null;
	/** control */
	private PlayerControl spriteControl;
	/** current color */
	private PredefinedColor color;
	
	public Player(Profile profile, Level level, ArrayList<AbstractAbility> ablts, PermissionPack permissions, TrajectoryPack trajectories) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.level = level;
		configuration = level.getConfiguration();
		this.profile = profile;
		// sprite
		color = this.profile.getSpriteColor();
		String folder = FileTools.getHeroesPath()+File.separator+this.profile.getSpritePack();
		folder = folder + File.separator+this.profile.getSpriteName();
		HeroFactory tempHeroFactory = HeroFactoryLoader.loadHeroFactory(folder,level,color,ablts,permissions,trajectories);
		sprite = tempHeroFactory.makeSprite();
		sprite.initGesture();
		sprite.setControlSettings(this.profile.getControlSettings());
		sprite.setPlayer(this);
		spriteControl = new PlayerControl(this);
	}

	public void initAi() throws FileNotFoundException, IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{	// artificial intelligence
    	if(this.profile.getAiName() != null)
    	{	ai = AiLoader.loadAi(profile.getAiName(), profile.getAiPackname());
    		ai.init(level.getInstance(),this);
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

	public AbstractAiManager<?> getArtificialIntelligence()
	{	return ai;
	}

	public Sprite getSprite()
	{	return sprite;
	}
	public PredefinedColor getColor()
	{	return color;
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
