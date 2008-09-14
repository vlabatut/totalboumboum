package fr.free.totalboumboum.engine.container.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.tools.CalculusTools;
import fr.free.totalboumboum.tools.StringTools;


public class Level
{	private Loop loop;
	private HashMap<Integer, PlayerLocation[]> playersLocations;
	private Tile matrix[][];
	private Theme theme;
	private Itemset itemset;
	private Bombset bombset;
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();;
	private String instancePath;
	
	private double sizeX;
	private double sizeY;
	private double trueZoom;

	// position et taille du niveau
	private double posX;
	private double posY;
	private int globalWidth;
	private int globalHeight;
	private double globalLeftX; // pas central
	private double globalUpY; // pas central

	// partie visible
	private int visibleWidth;
	private int visibleHeight;
	private int visibleLeftCol;
	private int visibleUpLine;
	private double visibleLeftX;
	private double visibleUpY;
	
	// borders
	private double horizontalBorderX;
	private double upBorderY;
	private double downBorderY;
	private double horizontalBorderHeight;
	private double horizontalBorderWidth;
	private double verticalBorderY;
	private double leftBorderX;
	private double rightBorderX;
	private double verticalBorderHeight;
	private double verticalBorderWidth;
	
	
	public Level(Loop loop)				
	{	this.loop = loop;
		configuration = loop.getConfiguration();
		// dimension
		Dimension dim = configuration.getPanelDimension();
		this.sizeX = dim.getWidth();
		this.sizeY = dim.getHeight();
			
	}
	
	public void setMatrixDimension(int globalWidth, int globalHeight, int visibleWidth, int visibleHeight, int visibleLeftCol, int visibleUpLine)
	{	// matrix
		this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
		matrix = new Tile[globalHeight][globalWidth];
		
		// visible part
		if(displayForceAll)
		{	visibleWidth = globalWidth;
			visibleHeight = globalHeight;
			this.visibleLeftCol = 0;
			this.visibleUpLine = 0;
		}
		else
		{	this.visibleWidth = visibleWidth;
			this.visibleHeight = visibleHeight;
			this.visibleLeftCol = visibleLeftCol;
			this.visibleUpLine = visibleUpLine;
		}
		
		// zoom factor
		double standardTileDimension = GameConstants.STANDARD_TILE_DIMENSION;
		double trueRatioX = sizeX/visibleWidth/standardTileDimension;
		double trueRatioY = sizeY/visibleHeight/standardTileDimension;
		trueZoom = Math.min(trueRatioX,trueRatioY);
		double ratioX = (int)(sizeX/visibleWidth)/standardTileDimension;
		double ratioY = (int)(sizeY/visibleHeight)/standardTileDimension;
		double zoomFactor = Math.min(ratioX,ratioY);
		loop.setZoomFactor(zoomFactor);
//configuration.setZoomFactor(1.0f);
		// position
		posX = sizeX/2;
		posY = sizeY/2;
		visibleLeftX = posX - visibleWidth*getTileDimension()/2/* + getTileDimension()/2*/;
		visibleUpY = posY - visibleHeight*getTileDimension()/2 /*+ getTileDimension()/2*/;
		globalLeftX = posX - globalWidth*getTileDimension()/2;
		globalUpY = posY - globalHeight*getTileDimension()/2;
//NOTE il y a une ligne horizontale dans les borders au dessus du niveau (forcer le zoomFactor à 1 pour la faire apparaitre)		
		// border
		horizontalBorderX = 0;
		upBorderY = 0;
		horizontalBorderWidth = sizeX;
		if(displayMaximize)
		{	downBorderY = globalUpY+globalHeight*getTileDimension()+1;
			if(globalUpY>0)
				horizontalBorderHeight = globalUpY;
			else
				horizontalBorderHeight = 0;
			verticalBorderY = horizontalBorderHeight+1;
			rightBorderX = globalLeftX+globalWidth*getTileDimension()+1;
			if(globalLeftX>0)
				verticalBorderWidth = globalLeftX;
			else
				verticalBorderWidth = 0;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		else
		{	downBorderY = visibleUpY+visibleHeight*getTileDimension()+1;
			horizontalBorderHeight = visibleUpY;
			verticalBorderY = visibleUpY;
			rightBorderX = visibleLeftX+visibleWidth*getTileDimension()+1;
			verticalBorderWidth = visibleLeftX;
			verticalBorderHeight = sizeY-2*horizontalBorderHeight+1;
		}
		leftBorderX = 0;
		
	}
	public void setItemset(Itemset itemset)
	{	this.itemset = itemset;
	}
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;
	}
	public void setTheme(Theme theme)
	{	// theme
		this.theme = theme;
	}
	public void setMatrix(String[][] mFloors, String[][] mBlocks, String[][] mItems)
	{	// tiles
		for(int line=0;line<globalHeight;line++)
		{	for(int col=0;col<globalWidth;col++)
			{	double x = globalLeftX + getTileDimension()/2 + col*getTileDimension();
				double y = globalUpY + getTileDimension()/2 + line*getTileDimension();
				if(mFloors[line][col]==null)
					matrix[line][col] = new Tile(this,line,col,x,y,theme.makeFloor());
				else
					matrix[line][col] = new Tile(this,line,col,x,y,theme.makeFloor(mFloors[line][col]));
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

	public void addHero(Hero hero, int line, int col)
	{	matrix[line][col].addSprite(hero);
		hero.setCurrentPosX(matrix[line][col].getPosX());
		hero.setCurrentPosY(matrix[line][col].getPosY());
	}
/*	
	public void setBlock(AbstractBlock block, int line, int col)
	{	matrix[line][col].setBlock(block);		
	}
*/
	public Tile[][] getMatrix()
	{	return matrix;
	}
	
	public void update()
	{	for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].update();		
	}

	//NOTE à effectuer seulement pour les tiles visibles
	public void draw(Graphics g)
	{	
		// only the on-ground flat sprites (they don't have shadow)
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,true,true,false);
		
		// the rest line by line
		for(int line=0;line<globalHeight;line++)
		{	// shadows from the non-flat on-ground sprites
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,false,true,true);
			/*
			 * the non-flat on-ground sprites themselves
			 * each different kind is processed separately for graphical reasons
			 */
			// floor
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawFloor(g,false,true,false);
			// fires
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawFires(g,false,true,false);
			// item
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawItem(g,false,true,false);
			// block
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawBlock(g,false,true,false);
			// bombs
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawBombs(g,false,true,false);
			// heroes
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawHeroes(g,false,true,false);
			
			// shadows from the in-air sprites
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,false,false,true);
			// the in-air sprites themselves
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].drawSelection(g,false,false,false);
		}
		// contours
		if(horizontalBorderHeight>0)
		{	Color temp = g.getColor();
			g.setColor(configuration.getBorderColor());
			g.fillRect((int)horizontalBorderX, (int)upBorderY, (int)horizontalBorderWidth, (int)horizontalBorderHeight);
			g.fillRect((int)horizontalBorderX, (int)downBorderY, (int)horizontalBorderWidth, (int)horizontalBorderHeight);
			g.setColor(temp);
		}
		if(verticalBorderWidth>0)
		{	Color temp = g.getColor();
			g.setColor(configuration.getBorderColor());
			g.fillRect((int)leftBorderX, (int)verticalBorderY, (int)verticalBorderWidth, (int)verticalBorderHeight);
			g.fillRect((int)rightBorderX, (int)verticalBorderY, (int)verticalBorderWidth, (int)verticalBorderHeight);
			g.setColor(temp);
		}
		if(loop.getShowGrid())
		{	g.setColor(Color.CYAN);
			// croix					
//			g.drawLine((int)posX, 0, (int)posX, configuration.getPanelDimensionY());
//			g.drawLine(0,(int)posY, configuration.getPanelDimensionX(), (int)posY);
			// grille
			for(int line=0;line<globalHeight;line++)
				for(int col=0;col<globalWidth;col++)
				{	Tile temp = matrix[line][col];
					g.drawLine((int)temp.getPosX(), (int)temp.getPosY(), (int)temp.getPosX(), (int)temp.getPosY());
					g.drawRect((int)(temp.getPosX()-getTileDimension()/2), (int)(temp.getPosY()-getTileDimension()/2), (int)getTileDimension(), (int)getTileDimension());
				}
		}
		if(loop.getShowTilesPositions()==1)
		{	// coordonnées
			g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 12);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			for(int line=0;line<globalHeight;line++)
				for(int col=0;col<globalWidth;col++)
				{	Tile temp = matrix[line][col];
					String text = "("+line+","+col+")";
					Rectangle2D box = metrics.getStringBounds(text, g);
					int x = (int)Math.round(temp.getPosX()-box.getWidth()/2);
					int y = (int)Math.round(temp.getPosY()+box.getHeight()/2);
					g.drawString(text, x, y);
				}
		}
		else if(loop.getShowTilesPositions()==2)
		{	// coordonnées
			g.setColor(Color.CYAN);
			Font font = new Font("Dialog", Font.PLAIN, 12);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			for(int line=0;line<globalHeight;line++)
				for(int col=0;col<globalWidth;col++)
				{	Tile temp = matrix[line][col];
					String textX = Double.toString(temp.getPosX());
					String textY = Double.toString(temp.getPosY());
					Rectangle2D boxX = metrics.getStringBounds(textX, g);
					Rectangle2D boxY = metrics.getStringBounds(textY, g);
					int x = (int)Math.round(temp.getPosX()-boxX.getWidth()/2);
					int y = (int)Math.round(temp.getPosY());
					g.drawString(textX, x, y);
					x = (int)Math.round(temp.getPosX()-boxY.getWidth()/2);
					y = (int)Math.round(temp.getPosY()+boxY.getHeight());
					g.drawString(textY, x, y);
				}
		}		
		// vitesse
		if(loop.getShowSpeed())
		{	g.setColor(Color.MAGENTA);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			String text = "Speed: "+configuration.getSpeedCoeff();
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(10+box.getHeight()/2);
			g.drawString(text, x, y);
		}
		// temps écoulé
		if(loop.getShowTime())
		{	g.setColor(Color.MAGENTA);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			long time = loop.getTotalTime();
			String text = "Time: "+StringTools.formatTimeWithHours(time);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(30+box.getHeight()/2);
			g.drawString(text, x, y);
		}
	}

	public int getVisibleLeftCol()
	{	return visibleLeftCol;
	}
	public int getVisibleUpLine()
	{	return visibleUpLine;
	}
	public int getVisibleWidth()
	{	return visibleWidth;
	}
	public int getVisibleHeight()
	{	return visibleHeight;
	}

	public int getGlobalWidth()
	{	return globalWidth;
	}
	public int getGlobalHeight()
	{	return globalHeight;
	}

	public Tile getTile(int l, int c)
	{	return matrix[l][c];	
	}
	
	public Tile getTile(double x, double y)
	{	
//System.out.println();		
//System.out.println("getTile>"+"globalPosition:"+globalLeftX+","+globalUpY);		
//System.out.println("getTile>"+"originalPos:"+x+","+y);		
		x = CalculusTools.round(x,loop);
		y = CalculusTools.round(y,loop);
//System.out.println("getTile>"+"rounded:"+x+","+y);		
		double difX = x-globalLeftX;
		double difY = y-globalUpY;
//System.out.println("getTile>"+"dif:"+difX+","+difY);		
		double rX = difX/getTileDimension();
		double rY = difY/getTileDimension();
//System.out.println("getTile>"+"rapport:"+rX+","+rY);		
		int rdX = (int)rX;//(int)Math.round(rX);
		int rdY = (int)rY;//(int)Math.round(rY);
//System.out.println("getTile>"+"round:"+rdX+","+rdY);		
//System.out.println("getTile>"+"globalSize:"+globalWidth+","+globalHeight);		
		int c = rdX%globalWidth;
		int l = rdY%globalHeight;
//System.out.println("getTile>"+"finalPos:"+l+","+c);		
		return matrix[l][c];
	}
	
	public Tile getNeighbourTile(int line, int col, Direction direction)
	{	Tile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+globalWidth-1)%globalWidth;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%globalWidth;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+globalHeight-1)%globalHeight;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%globalHeight;
		else
			l = line;
		//
		result = matrix[l][c];
		return result;
	}

	public ArrayList<Tile> getNeighbourTiles(int line, int col)
	{	ArrayList<Tile> resultat = new ArrayList<Tile>();
		resultat.add(getNeighbourTile(line, col, Direction.LEFT));
		resultat.add(getNeighbourTile(line, col, Direction.DOWNLEFT));
		resultat.add(getNeighbourTile(line, col, Direction.DOWN));
		resultat.add(getNeighbourTile(line, col, Direction.DOWNRIGHT));
		resultat.add(getNeighbourTile(line, col, Direction.RIGHT));
		resultat.add(getNeighbourTile(line, col, Direction.UPRIGHT));
		resultat.add(getNeighbourTile(line, col, Direction.UPLEFT));
		resultat.add(getNeighbourTile(line, col, Direction.UP));		
		return resultat;
	}
	
//les limites ne doivent pas être les centres, ce qui simplifiera les calculs et 
//améliorera la lisibilité	
	
	public double[] normalizePosition(double x, double y)
	{	double result[] = new double[2];
		result[0] = normalizePositionX(x);
		result[1] = normalizePositionY(y);
		//
if(x!=result[0]){		
System.out.println("globalHeight:"+globalHeight+" globalWidth:"+globalWidth);		
System.out.println("globalLeftX:"+globalLeftX+" globalUpY:"+globalUpY);		
System.out.println("x:"+x+" y:"+y);		
System.out.println("resX:"+result[0]+" resY:"+result[1]);	
}	
		return result;
	}
	public double normalizePositionX(double x)
	{	double result = x;
		while(result<globalLeftX)
			result = result + globalWidth*getTileDimension();
		while(result>globalLeftX+globalWidth*getTileDimension())
			result = result - globalWidth*getTileDimension();
		return result;
	}
	public double normalizePositionY(double y)
	{	double result = y;
		while(result<globalUpY)
			result = result + globalHeight*getTileDimension();
		while(result>globalUpY+globalHeight*getTileDimension())
			result = result - globalHeight*getTileDimension();
		return result;
	}	
	
	public boolean isInsidePosition(double x, double y)
	{	return isInsidePositionX(x) && isInsidePositionY(y);		
	}
	public boolean isInsidePositionX(double x)
	{	//NOTE comparaison relative?
		return x>=globalLeftX && x<=globalLeftX+globalWidth*getTileDimension();
	}
	public boolean isInsidePositionY(double y)
	{	//NOTE comparaison relative?
//System.out.println("y:"+y+" debut:"+globalUpY+" fin:"+(globalUpY+globalHeight*getTileDimension())+" résultat:"+(y>=globalUpY && y<=globalUpY+globalHeight*getTileDimension()));		
		return y>=globalUpY && y<=globalUpY+globalHeight*getTileDimension();
	}

	public double getTileDimension()
	{	return loop.getScaledTileDimension();	
	}
	
	public Loop getLoop()
	{	return loop;	
	}
	
    private Configuration configuration;

    public Configuration getConfiguration()
	{	return configuration;		
	}
	
	public void addSprite(Sprite sprite)
	{	sprites.add(sprite);
	}
	public void removeSprite(Sprite sprite)
	{	sprites.remove(sprite);
	}
	
	public Bombset getBombset()
	{	return bombset;		
	}

	/////////////////////////////////////////////////////////////////
	// DISPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private boolean displayForceAll;
	private boolean displayMaximize;

	public boolean getDisplayForceAll()
	{	return displayForceAll;
	}
	public void setDisplayForceAll(boolean displayForceAll)
	{	this.displayForceAll = displayForceAll;
	}
	
	public boolean getDisplayMaximize()
	{	return displayMaximize;
	}
	public void setDisplayMaximize(boolean displayMaximize)
	{	this.displayMaximize = displayMaximize;
	}
	
	
	
	
	public HashMap<Integer, PlayerLocation[]> getPlayersLocations()
	{	return playersLocations;
	}
	public void setPlayersLocations(HashMap<Integer, PlayerLocation[]> playersLocations)
	{	this.playersLocations = playersLocations;
	}
	
	
	public String getInstancePath() 
	{	return instancePath;
	}
	public void setInstancePath(String instancePath)
	{	this.instancePath = instancePath;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// bombset
			bombset.finish();
			bombset = null;
			// itemset
			itemset.finish();
			itemset = null;
			// matrix
			for(int line=0;line<globalHeight;line++)
				for(int col=0;col<globalWidth;col++)
				{	matrix[line][col].finish();
					matrix[line][col] = null;
				}
			// sprites
			Iterator<Sprite> it = sprites.iterator();
			while(it.hasNext())
			{	Sprite temp = it.next();
				temp.finish();
				it.remove();
			}
			// theme
			theme.finish();
			theme = null;
			// misc
			playersLocations = null;
		}
	}
}
