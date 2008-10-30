package fr.free.totalboumboum.ai;

import java.util.ArrayList;

public class AiPreview
{
	public AiPreview(String pack, String folder)
	{	this.pack = pack;
		this.folder = folder;
		name = "Artificial Intelligence";
		pack = "tournament200809";		
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
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
	private final ArrayList<String> authors = new ArrayList<String>();
	
	public ArrayList<String> getAuthors()
	{	return authors;
	}
	
	public void addAuthor(String author)
	{	authors.add(author);
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> notes = new ArrayList<String>();

	public void setNotes(ArrayList<String> notes)
	{	this.notes.addAll(notes);
	}
	public ArrayList<String> getNotes()
	{	return notes;
	}
}
