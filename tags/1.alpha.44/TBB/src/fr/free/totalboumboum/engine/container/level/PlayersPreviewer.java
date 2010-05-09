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
import fr.free.totalboumboum.tools.XmlTools;



public class PlayersPreviewer
{	
    public static void previewPlayers(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// init
		Element root;
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// reading
		previewPlayersElement(root,result);
    }
    
    private static void previewPlayersElement(Element root, LevelPreview result)
    {	// init
    	Element element;
    	// items
    	element = root.getChild(XmlTools.ELT_ITEMS);
    	previewItemsElement(element,result);
    }
    
    private static void previewItemsElement(Element root, LevelPreview result)
    {	HashMap<String,Integer> initialItems = new HashMap<String, Integer>();
    	List<Element> elements = root.getChildren(XmlTools.ELT_ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String str = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
			String nbrStr = temp.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
			int number = Integer.valueOf(nbrStr);
			initialItems.put(str,number);
		}
		result.setInitialItems(initialItems);
    }
}
