package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
 * Classe représentant la description
 * brève d'un agent.
 * 
 * @author Vincent Labatut
 *
 */
public class AiPreview
{
	/**
	 * Construction d'une description pour
	 * l'agent dont le paquetage et le dossier
	 * sont passés en paramètres.
	 * 
	 * @param pack
	 * 		Paquetage de l'agent.
	 * @param folder
	 * 		Dossier de l'agent.
	 */
	public AiPreview(String pack, String folder)
	{	this.pack = pack;
		this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Dossier contenant l'agent */
	private String folder;

	/**
	 * Renvoie le dossier contenant l'agent.
	 * 
	 * @return
	 * 		Dossier contenant l'agent.
	 */
	public String getFolder()
	{	return folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// PACK				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Paquetage contenant l'agent */
	private String pack;

	/**
	 * Renvoie le paquetage contenant l'agent.
	 * 
	 * @return
	 * 		Paquetage de l'agent.
	 */
	public String getPack()
	{	return pack;
	}	
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Autheur(s) de l'agent */
	private final List<String> authors = new ArrayList<String>();
	
	/**
	 * Renvoie la liste des auteurs de l'agent
	 * 
	 * @return
	 * 		Liste des auteurs de l'agent.
	 */
	public List<String> getAuthors()
	{	return authors;
	}
	
	/**
	 * Rajoute un auteur à la liste.
	 * 
	 * @param author
	 * 		Auteur à rajouter à la liste.
	 */
	public void addAuthor(String author)
	{	authors.add(author);
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Notes de description de l'agent */
	private final List<String> notes = new ArrayList<String>();

	/**
	 * Modifie les notes de description de l'agent
	 * 
	 * @param notes
	 * 		Nouvelles notes de description.
	 */
	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	
	/**
	 * Renvoie les notes de description de l'agent.
	 * 
	 * @return
	 * 		Notes de description de l'agent.
	 */
	public List<String> getNotes()
	{	return notes;
	}
}
