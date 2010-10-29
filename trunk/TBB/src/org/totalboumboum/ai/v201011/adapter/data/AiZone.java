package org.totalboumboum.ai.v201011.adapter.data;

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

import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * repr�sente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'IA a acc�s.</br>
 * 
 * A chaque fois que l'IA est sollicit�e par le jeu pour conna�tre l'action
 * qu'elle veut effectuer, cette repr�sentation est mise � jour. L'IA ne re�oit
 * pas une nouvelle AiZone : l'AiZone existante est modifi�e en fonction de l'�volution
 * du jeu. De la m�me fa�on, les cases (AiTile) restent les m�mes, ainsi que les sprites et
 * les autres objets. Si l'IA a besoin d'une trace des �tats pr�c�dents du jeu, son
 * concepteur doit se charger de l'impl�menter lui-m�me.
 * 
 * @author Vincent Labatut
 *
 */
public interface AiZone
{	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le temps total �coul� depuis le d�but du jeu
	 * 
	 * @return	le temps total �coul� exprim� en millisecondes
	 */
	public long getTotalTime();
	
	/**
	 * renvoie le temps �coul� depuis la mise � jour pr�c�dente
	 * de l'IA consid�r�e.
	 * 
	 * @return	le temps �coul� exprim� en millisecondes
	 */
	public long getElapsedTime();
	
	/**
	 * renvoie la dur�e maximale de la partie
	 * (elle peut �ventuellement durer moins longtemps)
	 * 
	 * @return	la dur�e maximale de la partie
	 */
	public long getLimitTime();
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la matrice de cases repr�sentant la zone de jeu
	 * 
	 * @return	la matrice correspondant � la zone de jeu
	 */
	public AiTile[][] getMatrix();

	/** 
	 * renvoie la hauteur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeight();
	
	/** 
	 * renvoie la largeur totale (y compris les �ventuelles cases situ�es hors de l'�cran)
	 * de la zone de jeu exprim�e en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth();
	
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
	public AiTile getTile(int line, int col);
	
	/**
	 * renvoie la case qui contient le pixel pass� en param�tre
	 *   
	 *  @param	x	abscisse du pixel concern�
	 *  @param	y	ordonn�e du pixel concern�
	 *  @return	case contenant le pixel situ� aux coordonn�es sp�cifi�es en param�tres
	 */
	public AiTile getTile(double x, double y);
		
	/**
	 * renvoie la direction de la case target relativement � la case source.
	 * Par exemple, la case target de coordonn�es (5,5) est � droite de
	 * la case source de coordonn�es (5,6).</br>
	 * 
	 * Cette fonction peut �tre utile quand on veut savoir dans quelle direction
	 * il faut se d�placer pour aller de la case source � la case target.</br>
	 * 
	 * <b>ATTENTION 1 :</b> si les deux cases ne sont pas des voisines directes (ie. ayant un cot� commun),
	 * il est possible que cette m�thode renvoie une direction composite,
	 * c'est � dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. R�f�rez-vous � 
	 * la classe Direction pour plus d'informations sur ces valeurs.</br>
	 *  
	 * <b>ATTENTION 2 :</b> comme les niveaux sont circulaires, il y a toujours deux directions possibles.
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
	public Direction getDirection(AiTile source, AiTile target);
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des blocks contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public List<AiBlock> getBlocks();
	
	/** 
	 * renvoie la liste des blocks destructibles contenus dans cette zone
	 * (la liste peut �tre vide). 
	 * 
	 * @return	liste de tous les blocs destructibles contenus dans cette zone
	 */
	public List<AiBlock> getDestructibleBlocks();
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public List<AiBomb> getBombs();
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public List<AiFire> getFires();

	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public List<AiFloor> getFloors();
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des personnages contenus dans cette zone,
	 * y compris ceux qui ont �t� �limin�s. 
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public List<AiHero> getHeroes();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone
	 */
	public List<AiHero> getRemainingHeroes();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont �t� �limin�s ou qui ne sont pas actuellement
	 * en jeu, et sauf le personnage contr�l� par l'IA.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone, sauf celui de l'IA
	 */
	public List<AiHero> getRemainingOpponents();

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut �tre vide)
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	public List<AiItem> getItems();
	
	/**
	 * renvoie le nombre d'items cach�s restant dans le niveau.
	 * Il s'agit des items qui sont encore cach�s dans des blocs, 
	 * et qui n'ont pas �t� ramass�s. Cette information permet de
	 * savoir s'il est encore n�cessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * 
	 * @return	le nombre d'items restant � d�couvrir
	 */
	public int getHiddenItemsCount();
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie le personnage qui est contr�l� par l'IA
	 */
	public AiHero getOwnHero();
	
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
	public Direction getDirection(AiSprite source, AiSprite target);
	
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
	public Direction getDirection(AiSprite sprite, AiTile tile);
	
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
	public Direction getDirection(double x1, double y1, double x2, double y2);

	/////////////////////////////////////////////////////////////////
	// TILE DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, 
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
	public int getTileDistance(int line1, int col1, int line2, int col2, Direction direction);

	/**
	 * renvoie la distance de Manhattan entre les cases de coordonn�es
	 * (line1,col1) et (line2,col2), exprim�e en cases. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param line1	ligne de la premi�re case
	 * @param col1	colonne de la premi�re case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2);
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2);
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases pass�es en param�tres,
	 * exprim�e en cases. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premi�re case
	 * @param sprite2	seconde case
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2, Direction direction);
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. 
   	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2);
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en cases. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction
	 * indiqu�e par le param�tre direction, qui peut correspondre � un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2, Direction direction);
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, 
	 * i.e. le bord de droite est reli� au bord de gauche, et le bord du haut 
	 * est reli� au bord du bas. Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonn�e du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonn�e du second point
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2);
	
	/**
	 * renvoie la distance de Manhattan entre les points de coordonn�es
	 * (x1,y1) et (x2,y2), exprim�e en pixels. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, 
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
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction);
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres,
	 * exprim�e en pixels. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance la plus courte
	 * (qui peut correspondre � un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2);
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites pass�s en param�tres, exprim�e en pixels. 
	 * <b>ATTENTION :</b> le niveau est consid�r� comme cyclique, i.e. le bord de droite 
	 * est reli� au bord de gauche, et le bord du haut est reli� au bord du bas. 
	 * Cette m�thode consid�re la distance dans la direction indiqu�e par le 
	 * param�tre direction, qui peut correspondre � un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction � consid�rer
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2, Direction direction);

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
	public double[] normalizePosition(double x, double y);

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
	public double normalizePositionX(double x);
	
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
	public double normalizePositionY(double y);
	
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
	public int[] normalizePosition(int line, int col);

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
	public int normalizePositionCol(int col);

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
	public int normalizePositionLine(int line);
	
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
	public boolean hasSamePixelPosition(AiSprite sprite1, AiSprite sprite2);
	
	/**
	 * teste si le sprite pass� en param�tre occupent le
	 * centre de la case pass�e en param�tre, au pixel pr�s
	 * 
	 * @param sprite	le sprite
	 * @param tile	la case
	 * @return	vrai ssi le sprite est au centre de la case
	 */
	public boolean hasSamePixelPosition(AiSprite sprite, AiTile tile);

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
	public boolean hasSamePixelPosition(double x1, double y1, double x2, double y2);
}
