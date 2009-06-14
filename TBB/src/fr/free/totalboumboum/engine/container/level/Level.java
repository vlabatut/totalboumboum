package fr.free.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.CalculusTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.StringTools;

public class Level
{	public Level(Loop loop)				
	{	this.loop = loop;
	}
	
    /////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String instance;
	
    public String getInstance()
    {	return instance;
    }
    public String getInstancePath()
    {	String result = FileTools.getInstancesPath()+File.separator+instance;
    	return result;
    }
    public void setInstancePath(String instance)
    {	this.instance = instance;
    }

    /////////////////////////////////////////////////////////////////
	// LOOP					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Loop loop;

	public Loop getLoop()
	{	return loop;	
	}
	
    /////////////////////////////////////////////////////////////////
	// THEME				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*
 TODO semblait inutile, dc désactivé 
 (également dans HollowLevel.loadTheme() et Level.finish())
	private Theme theme;

	public void setTheme(Theme theme)
	{	this.theme = theme;
	}
	public Theme getTheme()
	{	return theme;
	}
*/

	/////////////////////////////////////////////////////////////////
	// SIZE & LOCATION in TILES		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int globalWidth;
	private int globalHeight;
	private double pixelLeftX; // pas central
	private double pixelUpY; // pas central
	private double tileDimension;
	private double pixelWidth;
	private double pixelHeight;

	public void setTilePositions(int globalWidth, int globalHeight, double globalLeftX, double globalUpY)
	{	this.globalWidth = globalWidth;
		this.globalHeight = globalHeight;
		this.pixelLeftX = globalLeftX;
		this.pixelUpY = globalUpY;
		this.pixelWidth = globalWidth*tileDimension;
		this.pixelHeight = globalHeight*getTileDimension();
	}
	public double getGlobalLeftX()
	{	return pixelLeftX;
	}
	public double getGlobalUpY()
	{	return pixelUpY;
	}
	public int getGlobalWidth()
	{	return globalWidth;
	}
	public int getGlobalHeight()
	{	return globalHeight;
	}
	
	public double getTileDimension()
	{	return tileDimension;	
	}
	public void setTileDimension(double tileDimension)
	{	this.tileDimension = tileDimension;
	}

	/////////////////////////////////////////////////////////////////
	// TILES MATRIX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Tile matrix[][];

	public Tile[][] getMatrix()
	{	return matrix;
	}
	public void setMatrix(Tile matrix[][])
	{	this.matrix = matrix;
	}
	public Tile getTile(int l, int c)
	{	return matrix[l][c];	
	}

    /////////////////////////////////////////////////////////////////
	// DISTANCES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * process the manhattan distance
	 */
	public double getDistance(Sprite s1, Sprite s2, Direction direction)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		double result = processDistance(x1,y1,x2,y2,direction);
		return result;
	}
	public double getDistance(Sprite s1, Sprite s2)
	{	return getDistance(s1,s2,Direction.NONE);
	}
	
	/**
	 * process the manhattan distance
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public double getDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	x1 = normalizePositionX(x1);
		y1 = normalizePositionY(y1);
		x2 = normalizePositionX(x2);
		y2 = normalizePositionY(y2);
		double result = processDistance(x1,y1,x2,y2,direction);
		return result;
	}
	public double getDistance(double x1, double y1, double x2, double y2)
	{	return getDistance(x1,y1,x2,y2,Direction.NONE);
	}

	public double getHorizontalDistance(double x1, double x2, Direction direction)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		double result = processHorizontalDistance(x1,x2,direction);
		return result;
	}
	public double getHorizontalDistance(double x1, double x2)
	{	return getHorizontalDistance(x1,x2,Direction.NONE);
	}
	
	public double getVerticalDistance(double x1, double x2, Direction direction)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		double result = processHorizontalDistance(x1,x2,direction);
		return result;
	}
	public double getVerticalDistance(double x1, double x2)
	{	return getVerticalDistance(x1,x2,Direction.NONE);
	}
	
	/**
	 * process the manhattan distance
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	private double processDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = 0;
		result = result + processHorizontalDistance(x1,x2,direction);
		result = result + processVerticalDistance(y1,y2,direction);
		return result;
	}
	
	private double processHorizontalDistance(double x1, double x2, Direction direction)
	{	double result;
		double dx = x2 - x1;
		double direct = Math.abs(dx);
		double indirect = pixelWidth - direct;
		Direction dir = direction.getHorizontalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getHorizontalFromDouble(dx);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}
	
	private double processVerticalDistance(double y1, double y2, Direction direction)
	{	double result;
		double dy = y2 - y1;
		double direct = Math.abs(dy);
		double indirect = pixelHeight - direct;
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getVerticalFromDouble(dy);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// BOMBSET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Bombset bombset;

	public Bombset getBombset()
	{	return bombset;		
	}
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMSET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Itemset itemset;

	public Itemset getItemset()
	{	return itemset;	
	}
	public void setItemset(Itemset itemset)
	{	this.itemset = itemset;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();;

	public void addSprite(Sprite sprite)
	{	sprites.add(sprite);
	}
	
	public void removeSprite(Sprite sprite)
	{	sprites.remove(sprite);
	}
	
	public void addHero(Hero hero, int line, int col)
	{	matrix[line][col].addSprite(hero);
		hero.setCurrentPosX(matrix[line][col].getPosX());
		hero.setCurrentPosY(matrix[line][col].getPosY());
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * processes the direction of s2 relatively to s1, if
	 * considering the shortest path.
	 */
	public Direction getDirection(Sprite s1, Sprite s2)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		Direction result = processDirection(x1,y1,x2,y2);
		return result;
	}
	
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	x1 = normalizePositionX(x1);
		y1 = normalizePositionY(y1);
		x2 = normalizePositionX(x2);
		y2 = normalizePositionY(y2);
		Direction result = processDirection(x1,y1,x2,y2);
		return result;
	}

	public Direction getHorizontalDirection(double x1, double x2)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		Direction result = processHorizontalDirection(x1,x2);
		return result;
	}

	public Direction getVerticalDirection(double x1, double x2)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		Direction result = processHorizontalDirection(x1,x2);
		return result;
	}

	private Direction processDirection(double x1, double y1, double x2, double y2)
	{	Direction temp;
		Direction result = Direction.NONE;	
		temp = processHorizontalDirection(x1,x2);
		result.put(temp);
		temp = processVerticalDirection(y1,y2);
		result.put(temp);
		return result;
	}
	
	private Direction processHorizontalDirection(double x1, double x2)
	{	Direction result;
		double dx = x2 - x1;
		result = Direction.getHorizontalFromDouble(dx);
		double hemi = ((double)pixelWidth)/2;
		if(Math.abs(dx)>hemi)
			result = result.getOpposite();
		return result;
	}
	
	private Direction processVerticalDirection(double y1, double y2)
	{	Direction result;
		double dy = y2 - y1;
		result = Direction.getVerticalFromDouble(dy);
		double hemi = ((double)pixelHeight)/2;
		if(Math.abs(dy)>hemi)
			result = result.getOpposite();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DELTAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getDeltaX(double x1, double x2, Direction direction)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		double result = processDeltaX(x1,x2,direction);
		return result;
	}
	public double getDeltaX(double x1, double x2)
	{	double result = getDeltaX(x1,x2,Direction.NONE);
		return result;
	}

	public double getDeltaY(double y1, double y2, Direction direction)
	{	y1 = normalizePositionY(y1);
		y2 = normalizePositionY(y2);
		double result = processDeltaY(y1,y2,direction);
		return result;
	}
	public double getDeltaY(double y1, double y2)
	{	double result = getDeltaY(y1,y2,Direction.NONE);
		return result;
	}

	private double processDeltaX(double x1, double x2, Direction direction)
	{	double result;
		double direct = x2 - x1;
		double absDirect = Math.abs(direct);
		double indirect = -1*Math.signum(direct)*(pixelWidth - absDirect);
		double absIndirect = Math.abs(indirect);
		Direction dir = direction.getHorizontalPrimary();
		if(dir==Direction.NONE)
		{	if(absDirect<=absIndirect)
				result = direct;
			else 
				result = indirect;		
		}
		else
		{	Direction d = Direction.getHorizontalFromDouble(direct);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

	private double processDeltaY(double y1, double y2, Direction direction)
	{	double result;
		double direct = y2 - y1;
		double absDirect = Math.abs(direct);
		double indirect = -1*Math.signum(direct)*(pixelHeight - absDirect);
		double absIndirect = Math.abs(indirect);
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
		{	if(absDirect<=absIndirect)
				result = direct;
			else 
				result = indirect;		
		}
		else
		{	Direction d = Direction.getVerticalFromDouble(direct);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Tile getTile(double x, double y)
	{	x = CalculusTools.round(x,loop);
		y = CalculusTools.round(y,loop);
		double difX = x-pixelLeftX;
		double difY = y-pixelUpY;
		double rX = difX/getTileDimension();
		double rY = difY/getTileDimension();
		int rdX = (int)rX;//(int)Math.round(rX);
		int rdY = (int)rY;//(int)Math.round(rY);
		int c = rdX%globalWidth;
		int l = rdY%globalHeight;
		return matrix[l][c];
	}	
	public Tile getNeighborTile(int line, int col, Direction direction)
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
	public ArrayList<Tile> getNeighborTiles(int line, int col)
	{	ArrayList<Tile> resultat = new ArrayList<Tile>();
		resultat.add(getNeighborTile(line, col, Direction.LEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWNLEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWN));
		resultat.add(getNeighborTile(line, col, Direction.DOWNRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.RIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPLEFT));
		resultat.add(getNeighborTile(line, col, Direction.UP));		
		return resultat;
	}	
	
	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double[] normalizePosition(double x, double y)
	{	double result[] = new double[2];
		result[0] = normalizePositionX(x);
		result[1] = normalizePositionY(y);
		//
		return result;
	}
	public double normalizePositionX(double x)
	{	double result = x;
		while(result<pixelLeftX)
			result = result + pixelWidth;
		while(result>pixelLeftX+pixelWidth)
			result = result - pixelWidth;
		return result;
	}
	public double normalizePositionY(double y)
	{	double result = y;
		while(result<pixelUpY)
			result = result + pixelHeight;
		while(result>pixelUpY+pixelHeight)
			result = result - pixelHeight;
		return result;
	}
	public boolean isInsidePosition(double x, double y)
	{	return isInsidePositionX(x) && isInsidePositionY(y);		
	}
	public boolean isInsidePositionX(double x)
	{	//NOTE comparaison relative?
		return x>=pixelLeftX && x<=pixelLeftX+pixelWidth;
	}
	public boolean isInsidePositionY(double y)
	{	//NOTE comparaison relative?
		return y>=pixelUpY && y<=pixelUpY+pixelHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// BORDERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private double horizontalBorderX;
	@SuppressWarnings("unused")
	private double upBorderY;
	@SuppressWarnings("unused")
	private double downBorderY;
	@SuppressWarnings("unused")
	private double horizontalBorderHeight;
	@SuppressWarnings("unused")
	private double horizontalBorderWidth;
	@SuppressWarnings("unused")
	private double verticalBorderY;
	@SuppressWarnings("unused")
	private double leftBorderX;
	@SuppressWarnings("unused")
	private double rightBorderX;
	@SuppressWarnings("unused")
	private double verticalBorderHeight;
	@SuppressWarnings("unused")
	private double verticalBorderWidth;
	
	public void setBorders(double values[])
	{	int i = 0;
		horizontalBorderX = values[i++];
		upBorderY = values[i++];
		downBorderY = values[i++];
		horizontalBorderHeight = values[i++];
		horizontalBorderWidth = values[i++];
		verticalBorderY = values[i++];
		leftBorderX = values[i++];
		rightBorderX = values[i++];
		verticalBorderHeight = values[i++];
		verticalBorderWidth = values[i++];
	}

	/////////////////////////////////////////////////////////////////
	// IN GAME METHODS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
//	private long time = 0;
//	private long startTime = -1;
	
	public void update()
	{	
//time = time + getConfiguration().getMilliPeriod();
//if(startTime<0) startTime = System.currentTimeMillis();
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
				matrix[line][col].update();		
	}

	public void draw(Graphics g)
	{	drawLevel(g);
		if(loop.getShowGrid())
			drawGrid(g);
		if(loop.getShowTilesPositions()>0)
			drawTilesPositions(g);
		if(loop.getShowSpeed())
			drawSpeed(g);
		if(loop.getShowTime())
			drawTime(g);
		if(loop.getShowFPS())
			drawFPS(g);
	}

	//NOTE optimisation : à effectuer seulement pour les tiles visibles
	private void drawLevel(Graphics g)
	{	// only the on-ground flat sprites (they don't have shadow)
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
	}
/*	
	private void drawBorders(Graphics g)
	{	if(horizontalBorderHeight>0)
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
	}
*/
	private void drawGrid(Graphics g)
	{	g.setColor(Color.CYAN);
		// croix					
//		g.drawLine((int)posX, 0, (int)posX, configuration.getPanelDimensionY());
//		g.drawLine(0,(int)posY, configuration.getPanelDimensionX(), (int)posY);
		// grille
		for(int line=0;line<globalHeight;line++)
			for(int col=0;col<globalWidth;col++)
			{	Tile temp = matrix[line][col];
				g.drawLine((int)temp.getPosX(), (int)temp.getPosY(), (int)temp.getPosX(), (int)temp.getPosY());
				g.drawRect((int)(temp.getPosX()-getTileDimension()/2), (int)(temp.getPosY()-getTileDimension()/2), (int)getTileDimension(), (int)getTileDimension());
			}
	}

	private void drawTilesPositions(Graphics g)
	{	// expressed in tiles
		if(loop.getShowTilesPositions()==1)
		{	g.setColor(Color.CYAN);
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
		// expressed in pixels
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
	}
		
	private void drawSpeed(Graphics g)
	{	g.setColor(Color.CYAN);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "Speed: "+Configuration.getEngineConfiguration().getSpeedCoeff();
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(10+box.getHeight()/2);
		g.drawString(text, x, y);
	}
	
	private void drawTime(Graphics g)
	{	// loop time
		{	g.setColor(Color.CYAN);
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
/*		
		// engine time
		{	g.setColor(Color.GREEN);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			String text = "Time: "+StringTools.formatTimeWithHours(time);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(50+box.getHeight()/2);
			g.drawString(text, x, y);
		}
		// actual time
		{	g.setColor(Color.MAGENTA);
			Font font = new Font("Dialog", Font.PLAIN, 18);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			long time = System.currentTimeMillis()-startTime;
			String text = "Time: "+StringTools.formatTimeWithHours(time);
			Rectangle2D box = metrics.getStringBounds(text, g);
			int x = 10;
			int y = (int)Math.round(70+box.getHeight()/2);
			g.drawString(text, x, y);
		}
*/		
	}

	private void drawFPS(Graphics g)
	{	g.setColor(Color.CYAN);
		Font font = new Font("Dialog", Font.PLAIN, 18);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		double fps = loop.getAverageFPS();
		String fpsStr = nf.format(fps); 
		double ups = loop.getAverageUPS();
		String upsStr = nf.format(ups);
		String thFps = Integer.toString(Configuration.getEngineConfiguration().getFps());
		String text = "FPS/UPS/Th: "+fpsStr+"/"+upsStr+"/"+thFps;
		Rectangle2D box = metrics.getStringBounds(text, g);
		int x = 10;
		int y = (int)Math.round(50+box.getHeight()/2);
		g.drawString(text, x, y);
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
			{	for(int col=0;col<globalWidth;col++)
				{	matrix[line][col].finish();
					matrix[line][col] = null;
				}
			}
			// sprites
			Iterator<Sprite> it = sprites.iterator();
			while(it.hasNext())
			{	Sprite temp = it.next();
				temp.finish();
				it.remove();
			}
			// theme
//			theme.finish();
//			theme = null;
		}
	}
}
