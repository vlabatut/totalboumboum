package fr.free.totalboumboum.engine.container.zone;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.tools.FileTools;

public class ZoneInit {

	/**
	 * allows to programmatically initialize a zone,
	 * in order to help designing new levels
	 */
	public static void main(String[] args) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	createClassicZone(15,21,"temp","level",1);
	}

	private static void createClassicZone(int height, int width, String pack, String level, int border) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// init
		String folder = FileTools.getLevelsPath()+File.separator+pack+File.separator+level;
		File folderFile = new File(folder);
		folderFile.mkdirs();
		Zone zone = new Zone(width,height);
		
		// fill zone
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	ZoneTile tile = new ZoneTile(line,col);
				// floor
				tile.setFloor("regular");
				// block
				if(line<border || line>=height-border || col<border || col>=width-border)
					// build the border
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"border");				
				else if(line==border || line==height-border-1 || col==border || col==width-border-1)
					// fill the borderline with softwalls
					tile.setBlock(Theme.DEFAULT_GROUP+Theme.GROUP_SEPARATOR+"softwall");
				else if((col-border)%2==1 && (line-border)%2==1)
					// put a block if it fits the regular pattern
					tile.setBlock("hardwalls"+Theme.GROUP_SEPARATOR+"regular");				
				// add to zone	
				zone.addTile(tile);
			}
		}
		
		// save zone
		ZoneSaver.saveZone(folder,zone);
	}
}
