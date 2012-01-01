package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiPreview
{
	public AiPreview(String pack, String folder)
	{	this.pack = pack;
		this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder;

	public String getFolder()
	{	return folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String pack;

	public String getPack()
	{	return pack;
	}	
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> authors = new ArrayList<String>();
	
	public List<String> getAuthors()
	{	return authors;
	}
	
	public void addAuthor(String author)
	{	authors.add(author);
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> notes = new ArrayList<String>();

	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	public List<String> getNotes()
	{	return notes;
	}
}
