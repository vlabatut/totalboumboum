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

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CalculusTools;

/**
 * représente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'IA a accès.</br>
 * 
 * A chaque fois que l'IA est sollicitée par le jeu pour connaître l'action
 * qu'elle veut effectuer, cette représentation est mise à jour. L'IA ne reçoit
 * pas une nouvelle AiZone : l'AiZone existante est modifiée en fonction de l'évolution
 * du jeu. De la même façon, les cases (AiTile) restent les mêmes, ainsi que les sprites et
 * les autres objets. Si l'IA a besoin d'une trace des états précédents du jeu, son
 * concepteur doit se charger de l'implémenter lui-même.
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AiZone
{	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis le début du jeu */
	protected long totalTime = 0;
	/** temps écoulé depuis la mise à jour précédente de l'IA considérée */
	protected long elapsedTime = 0;
	/** durée maximale de la partie */
	protected long limitTime = 0;
	
	/**
	 * renvoie le temps total écoulé depuis le début du jeu
	 * 
	 * @return	le temps total écoulé exprimé en millisecondes
	 */
	public long getTotalTime()
	{	return totalTime;		
	}
	
	/**
	 * renvoie le temps écoulé depuis la mise à jour précédente
	 * de l'IA considérée.
	 * 
	 * @return	le temps écoulé exprimé en millisecondes
	 */
	public long getElapsedTime()
	{	return elapsedTime;		
	}
	
	/**
	 * renvoie la durée maximale de la partie
	 * (elle peut éventuellement durer moins longtemps)
	 * 
	 * @return	la durée maximale de la partie
	 */
	public long getLimitTime()
	{	return limitTime;		
	}
	
	/////////////////////////////////////////////////////////////////
	// META DATA		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** rangs des joueurs pour la manche en cours (ces rangs peuvent évoluer) */
	protected final HashMap<AiHero,Integer> roundRanks = new HashMap<AiHero, Integer>();
	/** rangs des joueurs pour la rencontre en cours (ces rangs n'évoluent pas pendant la manche) */
	protected final HashMap<AiHero,Integer> matchRanks = new HashMap<AiHero, Integer>();
	/** rangs des joueurs au classement global du jeu (ces rangs n'évoluent pas pendant la manche) */
	protected final HashMap<AiHero,Integer> statsRanks = new HashMap<AiHero, Integer>();

	/**
	 * Renvoie le classement du personnage passé en paramètre, pour la manche en cours.
	 * Ce classement est susceptible d'évoluer d'ici la fin de la manche actuellement jouée, 
	 * par exemple si ce joueur est éliminé.
	 * 
	 * @param hero	le personnage considéré
	 * @return	son classement dans la manche en cours
	 */
	public int getRoundRank(AiHero hero)
	{	return roundRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage passé en paramètre, pour la rencontre en cours.
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @param hero	le personnage considéré
	 * @return	son classement dans la rencontre en cours
	 */
	public int getMatchRank(AiHero hero)
	{	return matchRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage passé en paramètre, dans le classement général du jeu (Glicko-2)
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @param hero	le personnage considéré
	 * @return	son classement général (Glicko-2)
	 */
	public int getStatsRank(AiHero hero)
	{	return statsRanks.get(hero);
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** hauteur totale de la zone de jeu exprimée en cases (ie: nombre de lignes) */
	protected int height;
	/** largeur totale de la zone de jeu exprimée en cases (ie: nombre de colonnes) */
	protected int width;

	/**
	 * renvoie la matrice de cases représentant la zone de jeu
	 * 
	 * @return	la matrice correspondant à la zone de jeu
	 */
	public abstract AiTile[][] getMatrix();

	/** 
	 * renvoie la hauteur totale (y compris les éventuelles cases situées hors de l'écran)
	 * de la zone de jeu exprimée en cases (ie: nombre de lignes)
	 *  
	 *  @return	hauteur de la zone
	 */
	public int getHeight()
	{	return height;	
	}
	
	/** 
	 * renvoie la largeur totale (y compris les éventuelles cases situées hors de l'écran)
	 * de la zone de jeu exprimée en cases (ie: nombre de colonnes)
	 *  
	 *  @return	largeur de la zone
	 */
	public int getWidth()
	{	return width;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la case située dans la zone à la position passée en paramètre.
	 *   
	 *  @param	line	numéro de la ligne contenant la case à renvoyer
	 *  @param	col	numéro de la colonne contenant la case à renvoyer
	 *  @return	case située aux coordonnées spécifiées en paramètres
	 */
	public abstract AiTile getTile(int line, int col);
	
	/**
	 * renvoie la case qui contient le pixel passé en paramètre
	 *   
	 *  @param	x	abscisse du pixel concerné
	 *  @param	y	ordonnée du pixel concerné
	 *  @return	case contenant le pixel situé aux coordonnées spécifiées en paramètres
	 */
	public abstract AiTile getTile(double x, double y);
		
	/**
	 * renvoie la direction de la case target relativement à la case source.
	 * Par exemple, la case target de coordonnées (5,5) est à droite de
	 * la case source de coordonnées (5,6).</br>
	 * 
	 * Cette fonction peut être utile quand on veut savoir dans quelle direction
	 * il faut se déplacer pour aller de la case source à la case target.</br>
	 * 
	 * <b>ATTENTION 1 :</b> si les deux cases ne sont pas des voisines directes (ie. ayant un coté commun),
	 * il est possible que cette méthode renvoie une direction composite,
	 * c'est à dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. Référez-vous à 
	 * la classe Direction pour plus d'informations sur ces valeurs.</br>
	 *  
	 * <b>ATTENTION 2 :</b> comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette méthode renvoie la direction du plus court chemin (sans considérer les éventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le résultat sera
	 * RIGHT, car LEFT permet également d'atteindre la case, mais en parcourant un chemin plus long.
	 * <br><t> S>>>>>>>>>>T  distance=11
	 * <br><t>>S..........T> distance=1
	 * 
	 * @param source	case de référence
	 * @param target	case dont on veut connaitre la direction
	 * @return	la direction de target par rapport à source
	 */
	public Direction getDirection(AiTile source, AiTile target)
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
	/** 
	 * renvoie la liste des blocks contenus dans cette zone
	 * (la liste peut être vide). 
	 * 
	 * @return	liste de tous les blocs contenus dans cette zone
	 */
	public abstract List<AiBlock> getBlocks();
	
	/** 
	 * renvoie la liste des blocks destructibles contenus dans cette zone
	 * (la liste peut être vide). 
	 * 
	 * @return	liste de tous les blocs destructibles contenus dans cette zone
	 */
	public abstract List<AiBlock> getDestructibleBlocks();
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut être vide)
	 * 
	 * @return	liste de toutes les bombes contenues dans cette zone
	 */
	public abstract List<AiBomb> getBombs();
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut être vide)
	 * 
	 * @return	liste de tous les feux contenus dans cette zone
	 */
	public abstract List<AiFire> getFires();

	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des sols contenus dans cette zone 
	 * 
	 * @return	liste de tous les sols contenus dans cette zone
	 */
	public abstract List<AiFloor> getFloors();
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des personnages contenus dans cette zone,
	 * y compris ceux qui ont été éliminés. 
	 * 
	 * @return	liste de tous les joueurs contenus dans cette zone
	 */
	public abstract List<AiHero> getHeroes();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont été éliminés ou qui ne sont pas actuellement
	 * en jeu.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone
	 */
	public abstract List<AiHero> getRemainingHeroes();
	
	/** 
	 * renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont été éliminés ou qui ne sont pas actuellement
	 * en jeu, et sauf le personnage contrôlé par l'IA.
	 * 
	 * @return	liste de tous les joueurs encore contenus dans cette zone, sauf celui de l'IA
	 */
	public abstract List<AiHero> getRemainingOpponents();

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des items apparents contenus dans cette zone 
	 * (la liste peut être vide)
	 * 
	 * @return	liste de tous les items contenus dans cette zone
	 */
	public abstract List<AiItem> getItems();
	
	/**
	 * renvoie le nombre d'items cachés restant dans le niveau.
	 * Il s'agit des items qui sont encore cachés dans des blocs, 
	 * et qui n'ont pas été ramassés. Cette information permet de
	 * savoir s'il est encore nécessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * 
	 * @return	le nombre d'items restant à découvrir
	 */
	public abstract int getHiddenItemsCount();
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie le personnage qui est contrôlé par l'IA
	 */
	public abstract AiHero getOwnHero();
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la direction pour aller du sprite source au sprite target.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être NONE si jamais les deux sprites sont au même endroit
	 * 
	 * @param source	sprite de départ
	 * @param target	sprite de destination
	 * @return	la direction pour aller de source vers target
	 */
	public Direction getDirection(AiSprite source, AiSprite target)
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
	 * Calcule la direction pour aller du sprite à la case passés en paramètres.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être NONE si jamais les deux sprites sont au même endroit
	 * 
	 * @param sprite	sprite en déplacement
	 * @param tile	case de destination
	 * @return	la direction pour aller du sprite vers la case
	 */
	public Direction getDirection(AiSprite sprite, AiTile tile)
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
	 * Calcule la direction pour aller de la position (x1,y1) à la position (x2,y2)
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau).
	 * La direction peut être NONE si jamais les deux positions sont équivalentes.
	 * 
	 * @param x1	première position horizontale en pixels
	 * @param y1	première position verticale en pixels
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
	 * renvoie la distance de Manhattan entre les cases de coordonnées
	 * (line1,col1) et (line2,col2), exprimée en cases. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param line1	ligne de la première case
	 * @param col1	colonne de la première case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 * @param direction	direction à considérer
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	int result = RoundVariables.level.getTileDistance(line1,col1,line2,col2,direction);
		return result;
	}

	/**
	 * renvoie la distance de Manhattan entre les cases de coordonnées
	 * (line1,col1) et (line2,col2), exprimée en cases. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param line1	ligne de la première case
	 * @param col1	colonne de la première case
	 * @param line2	ligne de la seconde case
	 * @param col2	colonne de la seconde case
	 */
	public int getTileDistance(int line1, int col1, int line2, int col2)
	{	int result = RoundVariables.level.getTileDistance(line1, col1, line2, col2, Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	première case
	 * @param sprite2	seconde case
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2)
	{	int result = getTileDistance(tile1,tile2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	première case
	 * @param sprite2	seconde case
	 * @param direction	direction à considérer
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2, Direction direction)
	{	int line1 = tile1.getLine();
		int col1 = tile1.getCol();
		int line2 = tile2.getLine();
		int col2 = tile2.getCol();
		int result = RoundVariables.level.getTileDistance(line1,col1,line2,col2);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases. 
   	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2)
	{	int result = getTileDistance(sprite1,sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction à considérer
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
	{	AiTile tile1 = sprite1.getTile();
		AiTile tile2 = sprite2.getTile();
		int result = getTileDistance(tile1,tile2);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonnée du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonnée du second point
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	double result = RoundVariables.level.getPixelDistance(x1,y1,x2,y2);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1	abscisse du premier point
	 * @param y1	ordonnée du premier point
	 * @param x2	abscisse du second point
	 * @param y2	ordonnée du second point
	 * @param direction	direction à considérer
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = RoundVariables.level.getPixelDistance(x1,y1,x2,y2,direction);
		if(CalculusTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en pixels. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2)
	{	double result = getPixelDistance(sprite1, sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * renvoie la distance de Manhattan entre les deux sprites passés en paramètres, exprimée en pixels. 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction indiquée par le 
	 * paramètre direction, qui peut correspondre à un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1	premier sprite
	 * @param sprite2	second sprite
	 * @param direction	direction à considérer
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
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
	 * prend n'importe quelles coordonnées exprimées en pixels et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x	abscisse
	 * @param y	ordonnée
	 * @return	un tableau contenant les versions normalisées de x et y
	 */
	public double[] normalizePosition(double x, double y)
	{	return RoundVariables.level.normalizePosition(x, y);
	}

	/**
	 * prend n'importe quelle abscisse exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x	abscisse
	 * @return	la version normalisée de x
	 */
	public double normalizePositionX(double x)
	{	return RoundVariables.level.normalizePositionX(x);
	}
	
	/**
	 * prend n'importe quelle ordonnée exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param y	ordonnée
	 * @return	la version normalisée de y
	 */
	public double normalizePositionY(double y)
	{	return RoundVariables.level.normalizePositionY(y);
	}
	
	/**
	 * prend n'importe quelles coordonnées exprimées en cases et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @param col	colonne de la case
	 * @return	un tableau contenant les versions normalisées de line et col
	 */
	public int[] normalizePosition(int line, int col)
	{	return RoundVariables.level.normalizePosition(line, col);
	}

	/**
	 * prend n'importe quelle abscisse exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param col	colonne de la case
	 * @return	la version normalisée de col
	 */
	public int normalizePositionCol(int col)
	{	return RoundVariables.level.normalizePositionCol(col);
	}

	/**
	 * prend n'importe quelle ordonnée exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param line	ligne de la case
	 * @return	la version normalisée de line
	 */
	public int normalizePositionLine(int line)
	{	return RoundVariables.level.normalizePositionLine(line);
	}
	
	/////////////////////////////////////////////////////////////////
	// SAME PIXEL POSITION		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * teste si les deux sprites passés en paramètres occupent la
	 * même position au pixel près
	 * 
	 * @param sprite1	le premier sprite
	 * @param sprite2	le second sprite
	 * @return	vrai ssi les deux sprites sont au même endroit
	 */
	public boolean hasSamePixelPosition(AiSprite sprite1, AiSprite sprite2)
	{	boolean result;
		double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * teste si le sprite passé en paramètre occupent le
	 * centre de la case passée en paramètre, au pixel près
	 * 
	 * @param sprite	le sprite
	 * @param tile	la case
	 * @return	vrai ssi le sprite est au centre de la case
	 */
	public boolean hasSamePixelPosition(AiSprite sprite, AiTile tile)
	{	boolean result;	
		double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}

	/**
	 * teste si les deux points passés en paramètres occupent la
	 * même position au pixel près
	 * 
	 * @param x1	l'abscisse de la première position
	 * @param y1	l'ordonnée de la première position
	 * @param x2	l'abscisse de la seconde position
	 * @param y21	l'ordonnée de la seconde position
	 * @return	vrai ssi les deux positions sont équivalentes au pixel près
	 */
	public boolean hasSamePixelPosition(double x1, double y1, double x2, double y2)
	{	boolean result = true;	
		result = result && CalculusTools.isRelativelyEqualTo(x1,x2);
		result = result && CalculusTools.isRelativelyEqualTo(y1,y2);
		return result;
	}
}
