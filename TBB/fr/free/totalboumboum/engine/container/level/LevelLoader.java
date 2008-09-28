package fr.free.totalboumboum.engine.container.level;

//ImagesLoader.java
//Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The Imagesfile and images are stored in "Images/"
   (the IMAGE_DIR constant).

   ImagesFile Formats:

    o <fnm>                     // a single image file

    n <fnm*.ext> <number>       // a series of numbered image files, whose
                                // filenames use the numbers 0 - <number>-1

    s <fnm> <number>            // a strip file (fnm) containing a single row
                                // of <number> images

    g <name> <fnm> [ <fnm> ]*   // a group of files with different names;
                                // they are accessible via  
                                // <name> and position _or_ <fnm> prefix

    and blank lines and comment lines.

    The numbered image files (n) can be accessed by the <fnm> prefix
    and <number>. 

    The strip file images can be accessed by the <fnm>
    prefix and their position inside the file (which is 
    assumed to hold a single row of images).

    The images in group files can be accessed by the 'g' <name> and the
    <fnm> prefix of the particular file, or its position in the group.


    The images are stored as BufferedImage objects, so they will be 
    manipulated as 'managed' images by the JVM (when possible).
 */


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetLoader;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.theme.ThemeLoader;
import fr.free.totalboumboum.engine.container.tile.ValueTile;
import fr.free.totalboumboum.engine.container.tile.VariableTile;
import fr.free.totalboumboum.engine.container.tile.VariableTilesLoader;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;



public class LevelLoader
{	
	public static Level loadLevel(String folder, Loop loop) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Level result = loadLevelElement(individualFolder,root,loop);
		return result;
    }
    
    private static Level loadLevelElement(String folder, Element root, Loop loop) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		Level result = new Level(loop);
    	Element element;
		String content;
		
		// display
		element = root.getChild(XmlTools.ELT_DISPLAY);
		content = element.getAttribute(XmlTools.ATT_FORCE_ALL).getValue().trim();
		boolean displayForceAll = Boolean.parseBoolean(content);
		result.setDisplayForceAll(displayForceAll);
		content = element.getAttribute(XmlTools.ATT_MAXIMIZE).getValue().trim();
		boolean displayMaximize = Boolean.parseBoolean(content);
		result.setDisplayMaximize(displayMaximize);

		// global size
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int globalHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int globalWidth = Integer.parseInt(content);
		// visible size
		element = root.getChild(XmlTools.ELT_VISIBLE_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int visibleHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int visibleWidth = Integer.parseInt(content);
		// visible position
		element = root.getChild(XmlTools.ELT_VISIBLE_POSITION);
		content = element.getAttribute(XmlTools.ATT_UPLINE).getValue().trim();
		int visibleUpLine = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_LEFTCOL).getValue().trim();
		int visibleLeftCol = Integer.parseInt(content);
		// set matrix dimension
		result.setMatrixDimension(globalWidth, globalHeight, visibleWidth, visibleHeight, visibleLeftCol, visibleUpLine);		

		// instance
		element = root.getChild(XmlTools.ELT_INSTANCE);
		String instanceName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String instanceFolder = FileTools.getInstancesPath()+File.separator+instanceName;
		result.setInstancePath(instanceFolder);

		// players locations
		PlayersLoader.loadPlayers(folder,result);

		// bombset
		String bombsetFolder = instanceFolder + File.separator+FileTools.FOLDER_BOMBS;
		Bombset bombset = BombsetLoader.loadBombset(bombsetFolder,result);
		result.setBombset(bombset);
		loop.loadStepOver();

		// itemset
		String itemFolder = instanceFolder + File.separator+FileTools.FOLDER_ITEMS;
		Itemset itemset = ItemsetLoader.loadItemset(itemFolder,result);
		result.setItemset(itemset);
		loop.loadStepOver();

		// theme
		element = root.getChild(XmlTools.ELT_THEME);
		String themeName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String themeFolder = instanceFolder + File.separator + FileTools.FOLDER_THEMES;
		themeFolder = themeFolder + File.separator+themeName;
		Theme theme = ThemeLoader.loadTheme(themeFolder,result);
		result.setTheme(theme);
		loop.loadStepOver();

		// zone
		ZoneLoader.loadZone(folder,globalHeight,globalWidth,result);
		loop.loadStepOver();

		return result;
	}
}
