package fr.free.totalboumboum.engine.container.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewer;

public class LevelPreview
{

	public LevelPreview(LevelDescription levelDescription)
	{	// init
		String folder = levelDescription.getPath();
		// level preview
		try
		{	levelPreview = LevelPreviewer.previewLevel(folder);
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		// itemset preview
		itemsetPreview = ItemsetPreviewer.previewItemset(folderPath);
		// initial items preview
		
	}
	
	private BufferedImage levelPreview = null;
	
	public BufferedImage getLevelPreview()
	{	return levelPreview;
	}
	public void setLevelPreview(BufferedImage levelPreview)
	{	this.levelPreview = levelPreview;
	}
	
	private HashMap<String,BufferedImage> itemsetPreview;

	public HashMap<String, BufferedImage> getItemsetPreview()
	{	return itemsetPreview;
	}
	public void setItemsetPreview(HashMap<String, BufferedImage> itemsetPreview)
	{	this.itemsetPreview = itemsetPreview;
	}

	private HashMap<String,Integer> initialItems;

	public HashMap<String, Integer> getInitialItems()
	{	return initialItems;
	}
	public void setInitialItems(HashMap<String, Integer> initialItems)
	{	this.initialItems = initialItems;
	}
	

}
