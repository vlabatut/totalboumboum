package fr.free.totalboumboum.gui.data.configuration;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.data.language.Language;
import fr.free.totalboumboum.gui.data.language.LanguageLoader;

public class GuiConfiguration
{	
	public GuiConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	// language
		setLanguage(LanguageLoader.loadLanguage("english"));
		// font
		setFont(new Font("Arial",Font.PLAIN,10));
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME CONFIGURATION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Configuration gameConfiguration;
		
	public void setGameConfiguration(Configuration configuration)
	{	this.gameConfiguration = configuration;	
	}
	public Configuration getGameConfiguration()
	{	return gameConfiguration;	
	}
		
	/////////////////////////////////////////////////////////////////
	// LANGUAGE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Language language;
		
	public void setLanguage(Language language)
	{	this.language = language;	
	}
	public Language getLanguage()
	{	return language;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FONT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Font font;
	
	public Font getFont()
	{	return font;	
	}
	public void setFont(Font font)
	{	this.font = font;	
	}

	/////////////////////////////////////////////////////////////////
	// BACKGROUND		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage background;
	
	public BufferedImage getBackground()
	{	return background;	
	}
	public void setBackground(BufferedImage background)
	{	this.background = background;	
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Dimension getPanelDimension()
	{	return gameConfiguration.getPanelDimension();	
	}

	/////////////////////////////////////////////////////////////////
	// GAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AbstractTournament getCurrentTournament()
	{	return gameConfiguration.getTournament();	
	}

	public Match getCurrentMatch()
	{	return gameConfiguration.getTournament().getCurrentMatch();	
	}

	public Round getCurrentRound()
	{	return gameConfiguration.getTournament().getCurrentMatch().getCurrentRound();	
	}
}