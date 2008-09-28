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



public class PlayersLoader
{	
    public static void loadPlayers(String folder, Level result) throws ParserConfigurationException, SAXException, IOException
	{	
    	/* 
		 * NOTE tester ici si le level est suffisamment grand
		 * NOTE faut qu'il y ait au moins une config, à définir dans XSD
		 * attention, le numéro des joueurs ne doit pas dépasser maxPlayer-1
		 * NOTE il faut tester qu'il y a bien autant de locations de que de players indiqué dans la situation
		 */

		/*
		 * si le level ne supporte pas explicitement le nbre n de joueurs voulu,
		 * on prend la config pour la taille au dessus, et on utilise les n premières
		 * positions définies
		 */

    	// init
		Element root;
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// reading
		loadPlayersElement(root,result);
    }
    
    private static void loadPlayersElement(Element root, Level result)
    {	// init
    	Element element;
    	// locations
    	element = root.getChild(XmlTools.ELT_LOCATIONS);
    	loadLocationsElement(element,result);
    	// items
    	element = root.getChild(XmlTools.ELT_ITEMS);
    	loadItemsElement(element,result);
    }
    
    private static void loadLocationsElement(Element root, Level result)
    {	HashMap<Integer, PlayerLocation[]> locations = new HashMap<Integer, PlayerLocation[]>();
		List<Element> elements = root.getChildren(XmlTools.ELT_CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadCaseElement(temp,locations);
		}
		result.setPlayersLocations(locations);
    }
    
    private static void loadCaseElement(Element root, HashMap<Integer,PlayerLocation[]> result)
    {	String valStr = root.getAttribute(XmlTools.ATT_PLAYERS).getValue().trim();
		int value = Integer.valueOf(valStr);
		PlayerLocation[] locations = new PlayerLocation[value];
		List<Element> elements = root.getChildren(XmlTools.ELT_LOCATION);
		Iterator<Element> i = elements.iterator();
		int index = 0;
		while(i.hasNext())
		{	Element temp = i.next();
			PlayerLocation pl = new PlayerLocation();
			loadLocationElement(temp,pl);
			locations[index] = pl;
			index++;
		}
		result.put(value,locations);
    }
    	
    private static void loadLocationElement(Element root, PlayerLocation result)
    {	String str = root.getAttribute(XmlTools.ATT_PLAYER).getValue().trim();
		int number = Integer.valueOf(str);
		result.setNumber(number);
		str = root.getAttribute(XmlTools.ATT_COL).getValue().trim();
		int col = Integer.valueOf(str);
		result.setCol(col);
		str = root.getAttribute(XmlTools.ATT_LINE).getValue().trim();
		int line = Integer.valueOf(str);
		result.setLine(line);
    }
    
    private static void loadItemsElement(Element root, Level result)
    {	ArrayList<String> items = new ArrayList<String>();
    	List<Element> elements = root.getChildren(XmlTools.ELT_ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadItemElement(temp,items);
		}
		result.setPlayersItems(items);
    }
    
    private static void loadItemElement(Element root, ArrayList<String> items)
    {	String str = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	String nbrStr = root.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
    	int number = Integer.valueOf(nbrStr);
    	for(int i=0;i<number;i++)
    		items.add(str);
    }
    
}
