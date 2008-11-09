package fr.free.totalboumboum.engine.content.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class SpritePreviewLoader
{
	public static SpritePreview loadHeroPreview(String packName, String spriteName) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String folder = FileTools.getHeroesPath()+File.separator+packName+File.separator+spriteName;
		Element root = SpriteFactoryLoader.openFile(folder);
		SpritePreview result = loadSpriteElement(root,folder);
		result.setPack(packName);
		result.setFolder(spriteName);
		return result;
	}

	public static SpritePreview loadSpritePreview(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Element root = SpriteFactoryLoader.openFile(folder);
		SpritePreview result = loadSpriteElement(root,folder);
		result.setPack(null);//TODO à compléter
		result.setFolder(new File(folder).getName());
		return result;
	}

	private static SpritePreview loadSpriteElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException
	{	SpritePreview result = new SpritePreview();
		loadNameElement(root,result);
		loadAuthorElement(root, result);
		loadSourceElement(root, result);
		loadPreviewElement(root,folder,result);
		loadColors(folder,result);
		return result;
	}
	
	private static void loadNameElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_GENERAL);
		String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);		
	}
	
	private static void loadAuthorElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_AUTHOR);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_SOURCE);
		String name = null; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setSource(name);		
	}
	
	private static void loadPreviewElement(Element root, String folder, SpritePreview result) throws IOException
	{	Element prev = root.getChild(XmlTools.ELT_PREVIEW);
		String filePath = folder+File.separator+prev.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		BufferedImage image = ImageTools.loadImage(filePath,null);
		result.setImage(image);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadColors(String folder, SpritePreview result) throws ParserConfigurationException, SAXException, IOException
	{	String folderPath = folder+File.separator+FileTools.FILE_ANIMES;
		File dataFile = new File(folderPath+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_DATA);
		if(dataFile.exists())
		{	// opening
			String schemaFolder = FileTools.getSchemasPath();
			File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ANIMES+FileTools.EXTENSION_SCHEMA);
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			Element elt = root.getChild(XmlTools.ELT_COLORS);
			if(elt!=null)
			{	
//				PredefinedColor colorValues[] = PredefinedColor.values();
//				List<PredefinedColor> colorList = Arrays.asList(colorValues);
				List<Element> clrs = elt.getChildren();
				Iterator<Element> it = clrs.iterator();
		    	while(it.hasNext())
		    	{	Element temp = it.next();
		    		String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		    		name = name.toUpperCase(Locale.ENGLISH);
		    		PredefinedColor color = PredefinedColor.valueOf(name);
		    		result.addColor(color);
		    	}				
			}
		}
	}
}
