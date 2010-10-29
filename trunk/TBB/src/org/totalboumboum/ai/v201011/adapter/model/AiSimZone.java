package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CalculusTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale pour la simulation de l'�volution du jeu.</br>
 * 
 * L'ensemble des objets repr�sente un �tat du jeu et ne peut (volontairement) pas �tre modifi�.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimZone
{	
	/**
	 * construit une simulation du niveau pass� en param�tre,
	 * du point de vue du joueur pass� en param�tre.
	 * 
	 * @param zone	zone d'origine de la simulation
	 */
	public AiSimZone(AiZone zone)
	{	// init matrix, tiles and lists
		AiTile[][] m = zone.getMatrix();
		height = zone.getHeight();
		width = zone.getWidth();
		matrix = new AiSimTile[height][width];
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	for(int colIndex=0;colIndex<width;colIndex++)
			{	// tile
				AiTile tile = m[lineIndex][colIndex];
				AiSimTile aiTile = new AiSimTile(tile,this);
				matrix[lineIndex][colIndex] = aiTile;
				// blocks
				List<AiSimBlock> blocks = aiTile.getBlocks();
				blockList.addAll(blocks);
				// bombs
				List<AiSimBomb> bombs = aiTile.getBombs();
				bombList.addAll(bombs);
				// fires
				List<AiSimFire> fires = aiTile.getFires();
				fireList.addAll(fires);
				// floors
				List<AiSimFloor> floors = aiTile.getFloors();
				floorList.addAll(floors);
				// heroes
				List<AiSimHero> heroes = aiTile.getHeroes();
				heroList.addAll(heroes);
				// items
				List<AiSimItem> items = aiTile.getItems();
				itemList.addAll(items);
			}
		}
		
		// set own hero
		AiHero oh = zone.getOwnHero();
		PredefinedColor color = oh.getColor();
		ownHero = null;
		Iterator<AiSimHero> it = heroList.iterator();
		while(ownHero==null && it.hasNext())
		{	AiSimHero htemp = it.next();
			if(htemp.getColor()==color)
				ownHero = htemp;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps �coul� depuis le d�but du jeu */
	private long totalTime = 0;
	
	/**
	 * renvoie le temps total �coul� depuis le d�but du jeu
	 * 
	 * @return	le temps total �coul� exprim� en millisecondes
	 */
	public long getTotalTime()
	{	return totalTime;		
	}

	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice repr�sentant la zone et tous les sprites qu'elle contient */
	private AiSimTile[][] matrix;
	/** hauteur totale de la zone de jeu exprim�e en cases (ie: nombre de lignes) */
	private int height;
	/** largeur totale de la zone de jeu exprim�e en cases (ie: nombre de colonnes) */
	private int width;
	
	/** 
	 * renvoie la hauteur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeight()
	{	return height;	
	}
	
	/** 
	 * renvoie la largeur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth()
	{	return width;	
	}
	
	/** 
	 * renvoie la case voisine de la case pass�e en param�tre,
	 * dans la direction sp�cifi�e (en consid�rant le fait que le niveau
	 * est ferm�.
	 *  
	 *  @param line	ligne de la case � traite
	 *  @param col	colonne de la case � traiter
	 *  @param direction	direction de la case voisine relativement � la case de r�f�rence
	 *  @return	la case voisine dans la direction pr�cis�e
	 */
	public AiSimTile getNeighborTile(int line, int col, Direction direction)
	{	AiSimTile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 

		if(p[0]==Direction.LEFT)
			c = (col+width-1)%width;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%width;
		else
			c = col;

		if(p[1]==Direction.UP)
			l = (line+height-1)%height;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%height;
		else
			l = line;

		result = matrix[l][c];
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la case situ�e dans la zone � la position pass�e en param�tre.
	 *   
	 *  @param	line	num�ro de la ligne contenant la case � renvoyer
	 *  @param	col	num�ro de la colonne contenant la case � renvoyer
	 *  @return	case situ�e aux coordonn�es sp�cifi�es en param�tres
	 */
	public AiSimTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	/**
	 * renvoie la case qui contient le pixel pass� en param�tre
	 *   
	 *  @param	x	abscisse du pixel concern�
	 *  @param	y	ordonn�e du pixel concern�
	 *  @return	case contenant le pixel situ� aux coordonn�es sp�cifi�es en param�tres
	 */
	public AiSimTile getTile(double x, double y)
	{	Tile tile = RoundVariables.level.getTile(x,y);
		int line = tile.getLine();
		int col = tile.getCol();
		AiSimTile result = matrix[line][col];
		return result;
	}
		
	/**
	 * renvoie la direction de la case target relativement � la case source.
	 * Par exemple, la case target de coordonn�es (5,5) est � droite de
	 * la case source de coordonn�es (5,6).</br>
	 * 
	 * Cette fonction peut �tre utile quand on veut savoir dans quelle direction
	 * il faut se d�placer pour aller de la case source � la case target.</br>
	 * 
	 * ATTENTION 1 : si les deux cases ne sont pas des voisines directes (ie. ayant un cot� commun),
	 * il est possible que cette m�thode renvoie une direction composite,
	 * c'est � dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. R�f�rez-vous � 
	 * la classe Direction pour plus d'informations sur ces valeurs.</br>
	 *  
	 * ATTENTION 2 : comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette m�thode renvoie la direction du plus court chemin (sans consid�rer les �ventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le r�sultat sera
	 * RIGHT, car LEFT permet �galement d'atteindre la case, mais en parcourant un chemin plus long.
	 * <br><t> S>>>>>>>>>>T  distance=11
	 * <br><t>>S..........T> distance=1
	 * 
	 * @param source	case de r�f�rence
	 * @param target	case dont on veut connaitre la direction
	 * @return	la direction de target par rapport � source
	 */
	public Direction getDirection(AiSimTile source, AiSimTile target)
	{	// differences
		int dx = target.getCol()-source.getCol();
		int dy = target.getLine()-source.getLine();
		
		// direction
		Direction temp = Direction.getCompositeFromDouble(dx,dy);
		Direction tempX = temp.getHorizontalPrimary();
		Direction tempY = temp.getVerticalPrimary();
		
		// distances
		int distDirX = Math.abs(dx);
		int distIndirX = getWidth()-distDirX;
		if(distDirX>distIndirX)
			tempX = tempX.getOpposite();
		int distDirY = Math.abs(dy);
		int distIndirY = getHeight()-distDirY;
		if(distDirY>distIndirY)
			tempY = tempY.getOpposite();
		
		// result
		Direction result = Direction.getComposite(tempX,tempY);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiSimBlock> blockList = new ArrayList<AiSimBlock>();
	
	/** 
	 * renvoie la liste des blocks contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public List<AiSimBlock> getBlocks()
	{	return blockList;	
	}
	
	/** 
	 * renvoie la liste des blocks destructibles contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs destructibles contenus dans cette zone
	 */
	public List<AiSimBlock> getDestructibleBlocks()
	{	List<AiSimBlock> result = new ArrayList<AiSimBlock>();

		for(AiSimBlock block: blockList)
		{	if(block.isDestructible())
				result.add(block);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe des bombes contenues dans cette zone */
	private final List<AiSimBomb> bombList = new ArrayList<AiSimBomb>();
	
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public List<AiSimBomb> getBombs()
	{	return bombList;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe des feux contenus dans cette zone */
	private final List<AiSimFire> fireList = new ArrayList<AiSimFire>();
	
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public List<AiSimFire> getFires()
	{	return fireList;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe des sols contenus dans cette zone */
	private final List<AiSimFloor> floorList = new ArrayList<AiSimFloor>();

	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public List<AiSimFloor> getFloors()
	{	return floorList;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe de tous les personnages contenus dans cette zone */
	private final List<AiSimHero> heroList = new ArrayList<AiSimHero>();
	/** liste externe des personnages restant encore dans cette zone */
	private final List<AiSimHero> remainingHeroList = new ArrayList<AiSimHero>();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone,
	 * y compris ceux qui ont �t� �limin�s. 
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public List<AiSimHero> getHeroes()
	{	return heroList;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone
	 */
	public List<AiSimHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu, et sauf le personnage contr�l� par l'IA.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone, sauf celui de l'IA
	 */
	public List<AiSimHero> getRemainingOpponents()
	{	List<AiSimHero> result = new ArrayList<AiSimHero>(remainingHeroList);
		result.remove(ownHero);
		return result;	
	}
		
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste externe des items contenus dans cette zone */
	private final List<AiSimItem> itemList = new ArrayList<AiSimItem>();
	/** nombre d'items cach�s, i.e. pas encore ramass�s */
	private int hiddenItemsCount;
	
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	public List<AiSimItem> getItems()
	{	return itemList;	
	}
	
	/**
	 * renvoie le nombre d'items cach�s restant dans le niveau.
	 * Il s'agit des items qui sont encore cach�s dans des blocs, 
	 * et qui n'ont pas �t� ramass�s. Cette information permet de
	 * savoir s'il est encore n�cessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * 
	 * @return	le nombre d'items restant � d�couvrir
	 */
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;
		//TODO must be updated manually
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contr�l� par l'IA */
	private AiSimHero ownHero;

	/** 
	 * renvoie le personnage qui est contr�l� par l'IA
	 */
	public AiSimHero getOwnHero()
	{	return ownHero;	
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la direction pour aller du sprite source au sprite target.
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau)
	 * La direction peut �tre NONE si jamais les deux sprites sont au m�me endroit
	 * 
	 * @param source	sprite de d�part
	 * @param target	sprite de destination
	 * @return	la direction pour aller de source vers target
	 */
	public Direction getDirection(AiSimSprite source, AiSimSprite target)
	{	Direction result;
		if(source==null || target==null)
			result = Direction.NONE;
		else
		{	double x1 = source.getPosX();
			double y1 = source.getPosY();
			double x2 = target.getPosX();
			double y2 = target.getPosY();
			result = getDirection(x1,y1,x2,y2);
		}
		return result;		
	}
	
	/**
	 * Calcule la direction pour aller du sprite � la case pass�s en param�tres.
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau)
	 * La direction peut �tre NONE si jamais les deux sprites sont au m�me endroit
	 * 
	 * @param sprite	sprite en d�placement
	 * @param tile	case de destination
	 * @return	la direction pour aller du sprite vers la case
	 */
	public Direction getDirection(AiSimSprite sprite, AiSimTile tile)
	{	Direction result;
		if(sprite==null || tile==null)
			result = Direction.NONE;
		else
		{	double x1 = sprite.getPosX();
			double y1 = sprite.getPosY();
			double x2 = tile.getPosX();
			double y2 = tile.getPosY();
			result = getDirection(x1,y1,x2,y2);
		}
		return result;
	}
	
	/**
	 * Calcule la direction pour aller de la position (x1,y1) � la position (x2,y2)
	 * Le niveau est consid�r� comme cyclique, i.e. le bord de droite est 
	 * reli� au bord de gauche, et le bord du haut est reli� au bord du bas.
	 * Cette m�thode consid�re la direction correspondant � la distance la plus
	 * courte (qui peut correspondre � un chemin passant par les bords du niveau).
	 * La direction peut �tre NONE si jamais les deux positions sont �quivalentes.
	 * 
	 * @param x1	premi�re position horizontale en pixels
	 * @param y1	premi�re position verticale en pixels
	 * @param x2	seconde position horizontale en pixels
	 * @param y2	seconde position verticale en pixels
	 * @return	la direction correspondant au chemin le plus court
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	double dx = RoundVariables.level.getDeltaX(x1,x2);
		if(CalculusTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = RoundVariables.level.getDeltaY(y1,y2);
		if(CalculusTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. Attention, le 
	 * niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param line1	ligne de la premi�re case
	 * @param col1	colonne de la premi�re case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	int result = RoundVariables.level.getTileDistance(line1,col1,line2,col2,direction);
		return result;
	}

	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. 
	 * Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param line1	ligne de la premi�re case
	 * @param col1	colonne de la premi�re case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2)
	{	int result = RoundVariables.level.getTileDistance(line1, col1, line2, col2, Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 */
	public int getTileDistance(AiSimTile tile1, AiSimTile tile2)
	{	int result = getTileDistance(tile1,tile2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiSimTile tile1, AiSimTile tile2, Direction direction)
	{	int line1 = tile1.getLine();
		int col1 = tile1.getCol();
		int line2 = tile2.getLine();
		int col2 = tile2.getCol();
		int result = RoundVariables.level.getTileDistance(line1,col1,line2,col2);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public int getTileDistance(AiSimSprite sprite1, AiSimSprite sprite2)
	{	int result = getTileDistance(sprite1,sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiSimSprite sprite1, AiSimSprite sprite2, Direction direction)
	{	AiSimTile tile1 = sprite1.getTile();
		AiSimTile tile2 = sprite2.getTile();
		int result = getTileDistance(tile1,tile2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonn�e du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonn�e du second point
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	double result = RoundVariables.level.getPixelDistance(x1,y1,x2,y2);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonn�e du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonn�e du second point
	 * @param direction	direction � consid�rer
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = RoundVariables.level.getPixelDistance(x1,y1,x2,y2,direction);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en pixels. Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public double getPixelDistance(AiSimSprite sprite1, AiSimSprite sprite2)
	{	double result = getPixelDistance(sprite1, sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres, exprim�e en pixels. 
	 * Attention, le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction indiqu�e par le 
	 * param�tre direction, qui peut correspondre � un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public double getPixelDistance(AiSimSprite sprite1, AiSimSprite sprite2, Direction direction)
	{	double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		double result = RoundVariables.level.getPixelDistance(x1,y1,x2,y2,direction);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// PIXEL DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// COORDINATE NORMALIZING	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * prend n'importe quelles coordonn�es exprim�es en pixels et les normalise
	 * de mani�re � ce qu'elles appartiennent � la zone de jeu. Si les coordonn�es
	 * d�signent une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param x	abscisse
	 * @param y	ordonn�e
	 * @return	un tableau contenant les versions normalis�es de x et y
	 */
	public double[] normalizePosition(double x, double y)
	{	return RoundVariables.level.normalizePosition(x, y);
	}

	/**
	 * prend n'importe quelle abscisse exprim�e en pixels et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� gauche et le
	 * c�t� droit sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param x	abscisse
	 * @return	la version normalis�e de x
	 */
	public double normalizePositionX(double x)
	{	return RoundVariables.level.normalizePositionX(x);
	}
	
	/**
	 * prend n'importe quelle ordonn�e exprim�e en pixels et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� haut et le
	 * c�t� bas sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param y	ordonn�e
	 * @return	la version normalis�e de y
	 */
	public double normalizePositionY(double y)
	{	return RoundVariables.level.normalizePositionY(y);
	}
	
	/**
	 * prend n'importe quelles coordonn�es exprim�es en cases et les normalise
	 * de mani�re � ce qu'elles appartiennent � la zone de jeu. Si les coordonn�es
	 * d�signent une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @param col	colonne de la case
	 * @return	un tableau contenant les versions normalis�es de line et col
	 */
	public int[] normalizePosition(int line, int col)
	{	return RoundVariables.level.normalizePosition(line, col);
	}

	/**
	 * prend n'importe quelle abscisse exprim�e en cases et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� gauche et le
	 * c�t� droit sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param col	colonne de la case
	 * @return	la version normalis�e de col
	 */
	public int normalizePositionCol(int col)
	{	return RoundVariables.level.normalizePositionCol(col);
	}

	/**
	 * prend n'importe quelle ordonn�e exprim�e en cases et la normalise
	 * de mani�re � ce qu'elle appartienne � la zone de jeu. Si la coordonn�e
	 * d�signe une position situ�e en dehors de la zone de jeu, cette m�thode
	 * utilise la propri�t� cyclique du niveau (i.e. le c�t� haut et le
	 * c�t� bas sont reli�s) pour d�terminer une position
	 * �quivalente situ�e dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @return	la version normalis�e de line
	 */
	public int normalizePositionLine(int line)
	{	return RoundVariables.level.normalizePositionLine(line);
	}
	
	/////////////////////////////////////////////////////////////////
	// SAME PIXEL POSITION		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si les deux sprites pass�s en param�tres occupent la
	 * m�me position au pixel pr�s
	 * 
	 * @param sprite1	le premier sprite
	 * @param sprite2	le second sprite
	 * @return	vrai ssi les deux sprites sont au m�me endroit
	 */
	public boolean hasSamePixelPosition(AiSimSprite sprite1, AiSimSprite sprite2)
	{	boolean result;
		double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * teste si le sprite pass� en param�tre occupent le
	 * centre de la case pass�e en param�tre, au pixel pr�s
	 * 
	 * @param sprite	le sprite
	 * @param tile	la case
	 * @return	vrai ssi le sprite est au centre de la case
	 */
	public boolean hasSamePixelPosition(AiSimSprite sprite, AiSimTile tile)
	{	boolean result;	
		double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}

	/**
	 * teste si les deux points pass�s en param�tres occupent la
	 * m�me position au pixel pr�s
	 * 
	 * @param x1	l'abscisse de la premi�re position
	 * @param y1	l'ordonn�e de la premi�re position
	 * @param x2	l'abscisse de la seconde position
	 * @param y21	l'ordonn�e de la seconde position
	 * @return	vrai ssi les deux positions sont �quivalentes au pixel pr�s
	 */
	public boolean hasSamePixelPosition(double x1, double y1, double x2, double y2)
	{	boolean result = true;	
		result = result && CalculusTools.isRelativelyEqualTo(x1,x2);
		result = result && CalculusTools.isRelativelyEqualTo(y1,y2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimZone)
		{	
//			AiZone zone = (AiZone)o;	
//			result = level==zone.level && player==zone.player;
			result = this==o;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cette simulation (une fois que l'IA n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].finish();
		matrix = null;
		
		// sprites
		blockList.clear();
		bombList.clear();
		fireList.clear();
		floorList.clear();
		heroList.clear();
		itemList.clear();
		ownHero = null;
	}
}
