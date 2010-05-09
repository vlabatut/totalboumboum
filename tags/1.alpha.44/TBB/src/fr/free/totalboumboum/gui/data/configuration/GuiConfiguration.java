package fr.free.totalboumboum.gui.data.configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
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
