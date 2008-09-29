package fr.free.totalboumboum.engine.container.level;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetLoader;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.theme.ThemeLoader;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class HollowLevel
{
    private Level level;

    private boolean displayForceAll;
	private boolean displayMaximize;
	private int globalHeight;
	private int globalWidth;
	private int visibleHeight;
	private int visibleWidth;
	private int visibleUpLine;
	private int visibleLeftCol;
	private Zone zone;
	private String instancePath;
	private String themePath;
	private String itemPath;
	private String bombsetPath;
	private Players players;
	
/*	
	public String getInstancePath() 
	{	return instancePath;
	}
	public void setInstancePath(String instancePath)
	{	this.instancePath = instancePath;
	}
*/	
	public HollowLevel(String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		loadLevelElement(individualFolder,root);
    }
    
    private void loadLevelElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		Element element;
		String content;
		
		// display
		element = root.getChild(XmlTools.ELT_DISPLAY);
		content = element.getAttribute(XmlTools.ATT_FORCE_ALL).getValue().trim();
		displayForceAll = Boolean.parseBoolean(content);
		content = element.getAttribute(XmlTools.ATT_MAXIMIZE).getValue().trim();
		displayMaximize = Boolean.parseBoolean(content);
		
		// global size
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		globalHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		globalWidth = Integer.parseInt(content);
		// visible size
		element = root.getChild(XmlTools.ELT_VISIBLE_DIMENSION);
		content = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		visibleHeight = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		visibleWidth = Integer.parseInt(content);
		// visible position
		element = root.getChild(XmlTools.ELT_VISIBLE_POSITION);
		content = element.getAttribute(XmlTools.ATT_UPLINE).getValue().trim();
		visibleUpLine = Integer.parseInt(content);
		content = element.getAttribute(XmlTools.ATT_LEFTCOL).getValue().trim();
		visibleLeftCol = Integer.parseInt(content);
		
		// instance
		element = root.getChild(XmlTools.ELT_INSTANCE);
		String instanceName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		instancePath = FileTools.getInstancesPath()+File.separator+instanceName;

		// players locations
		players = PlayersLoader.loadPlayers(folder);

		// bombset
		bombsetPath = instancePath + File.separator+FileTools.FOLDER_BOMBS;

		// itemset
		itemPath = instancePath + File.separator+FileTools.FOLDER_ITEMS;

		// theme
		element = root.getChild(XmlTools.ELT_THEME);
		String themeName = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String themeFolder = instancePath + File.separator + FileTools.FOLDER_THEMES;
		themePath = themeFolder + File.separator+themeName;

		// zone
		zone = ZoneLoader.loadZone(folder,globalHeight,globalWidth);
	}
    
    public Players getPlayers()
    {	return players;    	
    }
    public String getInstancePath()
    {	return instancePath;
    }
    
    private void initLevel(Loop loop)
	{	// init
    	level = new Level(loop);
//level.setInstancePath(instancePath);
		Dimension panelDim = level.getConfiguration().getPanelDimension();
    	double sizeX = panelDim.width;
    	double sizeY = panelDim.height;
		// matrix
		Tile[][] matrix = new Tile[globalHeight][globalWidth];
		level.setMatrix(matrix);
		
		// visible part
		if(displayForceAll)
		{	visibleWidth = globalWidth;
			visibleHeight = globalHeight;
			visibleLeftCol = 0;
			visibleUpLine = 0;
		}
		
		// zoom factor
		double standardTileDimension = GameConstants.STANDARD_TILE_DIMENSION;
		double trueRatioX = sizeX/visibleWidth/standardTileDimension;
		double trueRatioY = sizeY/visibleHeight/standardTileDimension;
		double trueZoom = Math.min(trueRatioX,trueRatioY);
		double ratioX = (int)(sizeX/visibleWidth)/standardTileDimension;
		double ratioY = (int)(sizeY/visibleHeight)/standardTileDimension;
		double zoomFactor = Math.min(ratioX,ratioY);
		loop.setZoomFactor(zoomFactor);
//configuration.setZoomFactor(1.0f);
		
		// position
		double posX = sizeX/2;
		double posY = sizeY/2;
		double tileDimension = level.getTileDimension();
		double visibleLeftX = posX - visibleWidth*tileDimension/2/* + tileDimension/2*/;
		double visibleUpY = posY - visibleHeight*tileDimension/2 /*+ tileDimension/2*/;
		double globalLeftX = posX - globalWidth*tileDimension/2;
		double globalUpY = posY - globalHeight*tileDimension/2;
    	level.setTilePositions(globalWidth,globalHeight,globalLeftX,globalUpY);
		
//NOTE il y a une ligne horizontale dans les borders au dessus du niveau (forcer le zoomFactor à 1 pour la faire apparaitre)		
		// border
		double downBorderY;
		double horizontalBorderHeight;
		double verticalBorderY;
		double leftBorderX;
		double rightBorderX;
		double verticalBorderHeight;
		double verticalBorderWidth;
		double horizontalBorderX = 0;
		double upBorderY = 0;
		double horizontalBorderWidth = sizeX;
		if(displayMaximize)
		{	downBorderY = globalUpY+globalHeight*tileDimension+1;
			if(globalUpY>0)
				horizontalBorderHeight = globalUpY;
			else
				horizontalBorderHeight = 0;
			verticalBorderY = horizontalBorderHeight+1;
			rightBorderX = globalLeftX+globalWidth*tileDimension+1;
			if(globalLeftX>0)
				verticalBorderWidth = globalLeftX;
			else
				verticalBorderWidth = 0;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		else
		{	downBorderY = visibleUpY+visibleHeight*tileDimension+1;
			horizontalBorderHeight = visibleUpY;
			verticalBorderY = visibleUpY;
			rightBorderX = visibleLeftX+visibleWidth*tileDimension+1;
			verticalBorderWidth = visibleLeftX;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		leftBorderX = 0;
		double values[] = {horizontalBorderX,upBorderY,downBorderY,horizontalBorderHeight,horizontalBorderWidth,verticalBorderY,leftBorderX,rightBorderX,verticalBorderHeight,verticalBorderWidth};
		level.setBorders(values);
	}

    public void loadBombset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Bombset bombset = BombsetLoader.loadBombset(bombsetPath,level);
		level.setBombset(bombset);
    }

    public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Itemset itemset = ItemsetLoader.loadItemset(itemPath,level);
		level.setItemset(itemset);
    }

    public void loadTheme() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// theme
    	Theme theme = ThemeLoader.loadTheme(themePath,level);
		level.setTheme(theme);
		// zone
		Tile[][] matrix = level.getMatrix();
		Itemset itemset = level.getItemset();
		double globalLeftX = level.getGlobalLeftX();
		double globalUpY = level.getGlobalUpY();
		double tileDimension = level.getTileDimension();
    	String[][] mFloors = zone.getFloorMatrix();
		String[][] mBlocks = zone.getBlockMatrix();
		String[][] mItems = zone.getItemMatrix();
		// tiles
		for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	double x = globalLeftX + tileDimension/2 + col*tileDimension;
				double y = globalUpY + tileDimension/2 + line*tileDimension;
				if(mFloors[line][col]==null)
					matrix[line][col] = new Tile(level,line,col,x,y,theme.makeFloor());
				else
					matrix[line][col] = new Tile(level,line,col,x,y,theme.makeFloor(mFloors[line][col]));
				if(mBlocks[line][col]!=null)
					matrix[line][col].addSprite(theme.makeBlock(mBlocks[line][col]));
				if(mItems[line][col]!=null)
				{	Item item = itemset.makeItem(mItems[line][col]);
					if(matrix[line][col].hasBlock())
						matrix[line][col].getBlock().setHiddenSprite(item);
					else
					{	matrix[line][col].addSprite(item);
						item.initGesture();
					}
				}
			}
		}
	}
}
