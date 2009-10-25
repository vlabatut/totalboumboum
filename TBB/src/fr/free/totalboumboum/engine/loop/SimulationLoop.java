package fr.free.totalboumboum.engine.loop;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.ai.AbstractAiManager;
import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.level.Players;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.gather.SpecificGather;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactory;
import fr.free.totalboumboum.engine.content.sprite.hero.HeroFactoryLoader;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.control.SystemControl;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundVariables;
import fr.free.totalboumboum.statistics.detailed.StatisticEvent;
import fr.free.totalboumboum.tools.CalculusTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.StringTools.TimeUnit;

public class SimulationLoop extends Loop
{	private static final long serialVersionUID = 1L;

	public SimulationLoop(Round round)
	{	super(round);
	}	

	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, InstantiationException, InvocationTargetException, NoSuchMethodException
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void run()
	{	
	}

	public void playerOut(Player player)
	{	// TODO reuse this stuff VV
		int index = players.indexOf(player);
		round.playerOut(index);
		panel.playerOut(index);	
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;	
			// system listener
			panel.removeKeyListener(systemControl);
			// players
			Iterator<Player> i = players.iterator();
			while(i.hasNext())
			{	Player temp = i.next();
				panel.removeKeyListener(temp.getSpriteControl());
				temp.finish();
				i.remove();
			}
			// panel
//			panel.finish();
			panel = null;
			// level
			level.finish();
			level = null;
			// round
			round = null;
			// controm
			systemControl.finish();
			systemControl = null;
		}		
	}	
}
