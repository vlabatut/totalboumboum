package fr.free.totalboumboum.engine.container.level.info;

import java.io.Serializable;

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

public class LevelInfo implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// FOLDER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder;

	public String getPack()
	{	return pack;
	}
	
	public void setPack(String pack)
	{	this.pack = pack;
	}

	/////////////////////////////////////////////////////////////////
	// PACK				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String pack;
	
	public String getFolder()
	{	return folder;
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String instance;
	
	public String getInstance()
	{	return instance;
	}
	
	public void setInstance(String instance)
	{	this.instance = instance;
	}
	
	/////////////////////////////////////////////////////////////////
	// THEME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String theme;
	
	public String getTheme()
	{	return theme;
	}
	public void setTheme(String theme)
	{	this.theme = theme;
	}

	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String author;

	public String getAuthor()
	{	return author;
	}
	
	public void setAuthor(String author)
	{	this.author = author;
	}

	/////////////////////////////////////////////////////////////////
	// TITLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String title;

	public String getTitle()
	{	return title;
	}
	
	public void setTitle(String title)
	{	this.title = title;
	}

	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String source;
	
	public String getSource()
	{	return source;
	}
	
	public void setSource(String source)
	{	this.source = source;
	}
			
	/////////////////////////////////////////////////////////////////
	// GLOBAL DIMENSIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int globalHeight;	
	private int globalWidth;
	
	public int getGlobalHeight()
	{	return globalHeight;
	}
	public void setGlobalHeight(int globalHeight)
	{	this.globalHeight = globalHeight;
	}
	
	public int getGlobalWidth()
	{	return globalWidth;
	}
	public void setGlobalWidth(int globalWidth)
	{	this.globalWidth = globalWidth;
	}
	
	/////////////////////////////////////////////////////////////////
	// VISIBLE DIMENSIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int visibleHeight;
	private int visibleWidth;
	
	public int getVisibleHeight()
	{	return visibleHeight;
	}
	public void setVisibleHeight(int visibleHeight)
	{	this.visibleHeight = visibleHeight;
	}
	
	public int getVisibleWidth()
	{	return visibleWidth;
	}
	public void setVisibleWidth(int visibleWidth)
	{	this.visibleWidth = visibleWidth;
	}
	
	/////////////////////////////////////////////////////////////////
	// VISIBLE POSITION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int visiblePositionUpLine;
	private int visiblePositionLeftCol;

	public int getVisiblePositionUpLine()
	{	return visiblePositionUpLine;
	}
	public void setVisiblePositionUpLine(int visiblePositionUpLine)
	{	this.visiblePositionUpLine = visiblePositionUpLine;
	}
	
	public int getVisiblePositionLeftCol()
	{	return visiblePositionLeftCol;
	}
	public void setVisiblePositionLeftCol(int visiblePositionLeftCol)
	{	this.visiblePositionLeftCol = visiblePositionLeftCol;
	}

	/////////////////////////////////////////////////////////////////
	// VISIBILITY OPTIONS		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean forceAll;
	private boolean maximize;
	
	public boolean getForceAll()
	{	return forceAll;
	}
	public void setForceAll(boolean forceAll)
	{	this.forceAll = forceAll;
	}
	
	public boolean getMaximize()
	{	return maximize;
	}
	public void setMaximize(boolean maximize)
	{	this.maximize = maximize;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIEW IMAGE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String preview;
	
	public String getPreview()
	{	return preview;
	}
	
	public void setPreview(String preview)
	{	this.preview = preview;
	}
}
