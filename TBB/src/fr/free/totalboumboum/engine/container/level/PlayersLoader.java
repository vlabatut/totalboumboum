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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;



public class PlayersLoader
{	
    public static Players loadPlayers(String folder) throws ParserConfigurationException, SAXException, IOException
	{	
    	/* 
		 * NOTE tester ici si le level est suffisamment grand
		 * NOTE faut qu'il y ait au moins une config, � d�finir dans XSD
		 * attention, le num�ro des joueurs ne doit pas d�passer maxPlayer-1
		 * NOTE il faut tester qu'il y a bien autant de locations de que de players indiqu� dans la situation
		 */

		/*
		 * si le level ne supporte pas explicitement le nbre n de joueurs voulu,
		 * on prend la config pour la taille au dessus, et on utilise les n premi�res
		 * positions d�finies
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
		Players result = loadPlayersElement(root);
		return result;
    }
    
    private static Players loadPlayersElement(Element root)
    {	// init
    	Players result = new Players();
    	Element element;
    	// locations
    	element = root.getChild(XmlTools.ELT_LOCATIONS);
    	loadLocationsElement(element,result);
    	// items
    	element = root.getChild(XmlTools.ELT_ITEMS);
    	loadItemsElement(element,result);
    	// result
    	return result;
    }
    
    private static void loadLocationsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlTools.ELT_CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadCaseElement(temp,result);
		}
    }
    
    private static void loadCaseElement(Element root, Players result)
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
		result.addLocation(value,locations);
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
    
    private static void loadItemsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlTools.ELT_ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadItemElement(temp,result);
		}
    }
    
    private static void loadItemElement(Element root, Players result)
    {	String str = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	String nbrStr = root.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
    	int number = Integer.valueOf(nbrStr);
    	for(int i=0;i<number;i++)
    		result.addInitialItem(str);
    }
    
}
