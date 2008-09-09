package fr.free.totalboumboum.data.profile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import com.sun.xml.internal.ws.wsdl.writer.document.Port;

import fr.free.totalboumboum.engine.content.feature.anime.Colormap;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class PortraitsLoader
{
	protected static Portraits loadPortraits(String spriteFolderPath, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		String folderPath = spriteFolderPath+File.separator+FileTools.FOLDER_PORTRAITS;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_PORTRAITS+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PORTRAITS+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		//
		Portraits result = new Portraits();
		loadPortraitsElement(root,folderPath,color,result);
		completePortraits(result);
		return result;
	}
	
	private static void loadPortraitsElement(Element root, String folderPath, PredefinedColor color, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// colors
		String localFilePath = folderPath;
    	Colormap colormap = null;
    	String colorFolder = null;
		Object obj;
		Element elt = root.getChild(XmlTools.ELT_COLORS);;
		if(elt!=null && color!=null)
		{	obj = ImageTools.loadColorsElement(elt,localFilePath,color);
			if(obj instanceof Colormap)
				colormap = (Colormap)obj;
			else
				colorFolder = (String)obj;
		}		
		if(colorFolder!=null)
			localFilePath = localFilePath+File.separator + colorFolder;
		
		// ingame
		Element ingame = root.getChild(XmlTools.ELT_INGAME);
		loadIngameElement(ingame,localFilePath,colormap,result);
		
		// outgame
		Element outgame = root.getChild(XmlTools.ELT_OUTGAME);
		loadOutgameElement(outgame,localFilePath,colormap,result);
	}
	
	private static void loadIngameElement(Element root, String folderPath, Colormap colormap, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// folder
		String folder = folderPath;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
		{	String f = attribute.getValue().trim();
			folder = folder + File.separator + f;			
		}
		// portraits
		List<Element> elements = root.getChildren(XmlTools.ELT_PORTRAIT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase();
			String file = temp.getAttribute(XmlTools.ATT_FILE).getValue().trim();
			String imagePath = folder + File.separator + file;
			BufferedImage image = ImageTools.loadImage(imagePath,colormap);
			result.addIngamePortrait(name, image);
		}
	}

	private static void loadOutgameElement(Element root, String folderPath, Colormap colormap, Portraits result) throws IOException, ParserConfigurationException, SAXException
	{	// folder
		String folder = folderPath;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
		{	String f = attribute.getValue().trim();
			folder = folder + File.separator + f;			
		}
		// portraits
		List<Element> elements = root.getChildren(XmlTools.ELT_PORTRAIT);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase();
			String file = temp.getAttribute(XmlTools.ATT_FILE).getValue().trim();
			String imagePath = folder + File.separator + file;
			BufferedImage image = ImageTools.loadImage(imagePath,colormap);
			result.addOutgamePortrait(name,image);
		}
	}
	
	/**
	 * complete the portraits if some images are missing
	 * @param result
	 */
	private static void completePortraits(Portraits result)
	{	// square images
		{	// creation
			int width=30,height=30;
			BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.createGraphics();
			drawAbsentImage(g,width,height);
			// verification
			if(!result.containsIngamePortrait(Portraits.INGAME_LOST))
				result.addIngamePortrait(Portraits.INGAME_LOST,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_NORMAL))
				result.addIngamePortrait(Portraits.INGAME_NORMAL,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_OUT))
				result.addIngamePortrait(Portraits.INGAME_OUT,image);
			if(!result.containsIngamePortrait(Portraits.INGAME_WON))
				result.addIngamePortrait(Portraits.INGAME_WON,image);
			if(!result.containsOutgamePortrait(Portraits.OUTGAME_HEAD))
				result.addOutgamePortrait(Portraits.OUTGAME_HEAD,image);
		}		
		// rectangular images
		{	// creation
			int width=30,height=60;
			BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.createGraphics();
			g.setColor(Color.BLACK);
			drawAbsentImage(g,width,height);
			// verification
			if(!result.containsIngamePortrait(Portraits.OUTGAME_BODY))
				result.addIngamePortrait(Portraits.OUTGAME_BODY,image);
		}				
	}
	
	private static void drawAbsentImage(Graphics g, int width, int height)
	{	// red cross
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		int strokeSize = width/5;
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.drawLine(0+strokeSize,0+strokeSize,width-strokeSize,height-strokeSize);
        g2d.drawLine(0+strokeSize,height-strokeSize,width-strokeSize,0+strokeSize);
        
        // question mark
        g.setColor(Color.BLACK);
		Font font = new Font("Arial",Font.PLAIN,height);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "?";
		Rectangle2D box = metrics.getStringBounds(text,g);
		int x = (int)Math.round(width/2-box.getWidth()/2);
		int y = (int)Math.round(height/2+box.getHeight()/3);
		g.drawString(text,x,y);

		g.dispose();
	}
}
