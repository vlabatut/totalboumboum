package fr.free.totalboumboum.data.configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class Configuration
{	
	public Configuration() throws ParserConfigurationException, SAXException, IOException
	{	// engine
		setFps(100);
		setSpeedCoeff(1);
		// display
		setBorderColor(Color.BLACK);
		setSmoothGraphics(true);
		// panel
		setPanelDimension(750,600);
		// profiles
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int fps;
	private long milliPeriod;
	private long nanoPeriod;
	//NOTE speedcoeff � descendre au niveau de loop, car il peut d�pendre du level
	private double speedCoeff;

	public void setFps(int fps)
	{	this.fps = fps;
		milliPeriod = (long) 1000.0 / fps;
		nanoPeriod = milliPeriod * 1000000L;
	}
	public int getFps()
	{	return fps;
	}
	public long getMilliPeriod()
	{	return milliPeriod;
	}
	public long getNanoPeriod()
	{	return nanoPeriod;
	}

	public double getSpeedCoeff()
	{	return speedCoeff;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Color borderColor;
	private boolean smoothGraphics;

	public void setSmoothGraphics(boolean smoothGraphics)
	{	this.smoothGraphics = smoothGraphics;		
	}
	public boolean getSmoothGraphics()
	{	return smoothGraphics;		
	}
	
	public Color getBorderColor()
	{	return borderColor;
	}
	public void setBorderColor(Color borderColor)
	{	this.borderColor = borderColor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Dimension panelDimension;
	
	public void setPanelDimension(int width, int height)
	{	panelDimension = new Dimension(width,height);
	}
	public Dimension getPanelDimension()
	{	return panelDimension;	
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<String> profiles = new ArrayList<String>();
	
	public ArrayList<String> getProfiles()
	{	return profiles;	
	}
	
	public void addProfile(String profile)
	{	if(!profiles.contains(profile))
			profiles.add(profile);
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament = null;
	
	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
}