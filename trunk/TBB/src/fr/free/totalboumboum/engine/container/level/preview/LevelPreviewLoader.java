package fr.free.totalboumboum.engine.container.level.preview;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;
import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewLoader;
import fr.free.totalboumboum.engine.container.level.info.LevelInfo;
import fr.free.totalboumboum.engine.container.level.info.LevelInfoLoader;
import fr.free.totalboumboum.engine.container.level.players.PlayersPreviewer;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.image.ImageTools;

public class LevelPreviewLoader
{	private static boolean previewItemset;
	private static boolean previewPlayers;
	private static boolean previewAllowedPlayersOnly;
	private static boolean previewImage;

    public static LevelPreview loadLevelPreview(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// parameters
    	previewItemset = true;
    	previewPlayers = true;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
    	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;
    }
    
    public static LevelPreview loadLevelPreviewWithoutItemset(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// parameters
    	previewItemset = false;
    	previewPlayers = true;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
    	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;
    }
    
    public static LevelPreview loadLevelPreviewOnlyAllowedPlayers(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// parameters
    	previewItemset = false;
    	previewPlayers = false;
    	previewAllowedPlayersOnly = true;
    	previewImage = false;
    	// load
       	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;    	
    }

    public static LevelPreview loadLevelPreviewOnlyImage(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// parameters
    	previewItemset = false;
    	previewPlayers = false;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
       	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;    	
    }

	private static LevelPreview loadLevelPreviewCommon(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = FileTools.getLevelsPath()+File.separator+pack+File.separator+folder;
		
		// loading
		LevelPreview result = new LevelPreview();
		loadLevelInfo(pack,folder,result);
		loadImage(individualFolder,result);
		loadPlayers(individualFolder,result);
		loadItemset(individualFolder,result);
		
		return result;
	}

	private static void loadLevelInfo(String pack, String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	LevelInfo levelInfo = LevelInfoLoader.loadLevelInfo(pack,folder);
		result.setLevelInfo(levelInfo);
	}
	
	private static void loadImage(String folder, LevelPreview result) throws IOException
	{	if(previewImage)
		{	String filename = result.getLevelInfo().getPreview();
			String filePath = folder+File.separator+filename;
			BufferedImage image = ImageTools.loadImage(filePath,null);
			result.setVisualPreview(image);
		}
	}
	
	private static void loadPlayers(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// players stuff preview
		if(previewPlayers)
			PlayersPreviewer.loadPlayers(folder,result);		
		else if(previewAllowedPlayersOnly)
			PlayersPreviewer.loadPlayersAllowed(folder,result);
	}
	
	private static void loadItemset(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	if(previewItemset)
		{	String instanceName = result.getLevelInfo().getInstance();
			String instanceFolder = FileTools.getInstancesPath()+File.separator+instanceName;		
			String itemFolder = instanceFolder + File.separator+FileTools.FOLDER_ITEMS;
			ItemsetPreview itemsetPreview = ItemsetPreviewLoader.loadItemsetPreview(itemFolder);
			result.setItemsetPreview(itemsetPreview);
		}
	}
}
