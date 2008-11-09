package fr.free.totalboumboum.engine.content.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class SpritePreviewLoader
{
	public static SpritePreview loadSpritePreview(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Element root = SpriteFactoryLoader.openFile(folder);
		SpritePreview result = new SpritePreview();
		loadNameElement(root,result);
		loadAuthorElement(root, result);
		loadSourceElement(root, result);
		loadPreviewElement(root,folder,result);
		return result;
	}

	private static void loadNameElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_GENERAL);
		String name = elt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);		
	}
	
	private static void loadAuthorElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_AUTHOR);
		String name = "N/A"; 
		if(elt!=null)
			name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, SpritePreview result)
	{	Element elt = root.getChild(XmlTools.ELT_SOURCE);
		String name = "N/A"; 
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
}
