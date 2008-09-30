package fr.free.totalboumboum.engine.container.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewer;

public class LevelPreview
{
	
	/////////////////////////////////////////////////////////////////
	// VISUAL PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private BufferedImage visualPreview = null;
	
	public BufferedImage getVisualPreview()
	{	return visualPreview;
	}
	public void setVisualPreview(BufferedImage visualPreview)
	{	this.visualPreview = visualPreview;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMSET PREVIEW	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private HashMap<String,BufferedImage> itemsetPreview;

	public HashMap<String, BufferedImage> getItemsetPreview()
	{	return itemsetPreview;
	}
	public void setItemsetPreview(HashMap<String, BufferedImage> itemsetPreview)
	{	this.itemsetPreview = itemsetPreview;
	}

	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private HashMap<String,Integer> initialItems;

	public HashMap<String, Integer> getInitialItems()
	{	return initialItems;
	}
	public void setInitialItems(HashMap<String, Integer> initialItems)
	{	this.initialItems = initialItems;
	}
/*	
	/////////////////////////////////////////////////////////////////
	// LEVEL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private HashMap<String,Integer> levelItems;

	public HashMap<String, Integer> getLevelItems()
	{	return levelItems;
	}
	public void setLevelItems(HashMap<String, Integer> levelItems)
	{	this.levelItems = levelItems;
	}
*/	
}
