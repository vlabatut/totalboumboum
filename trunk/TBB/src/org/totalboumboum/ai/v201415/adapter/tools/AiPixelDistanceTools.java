package org.totalboumboum.ai.v201415.adapter.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.ai.v201415.adapter.data.AiSprite;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.computing.ApproximationTools;
import org.totalboumboum.tools.level.DeltaTools;
import org.totalboumboum.tools.level.DistancePixelTools;
import org.totalboumboum.tools.level.PositionTools;

/**
 * Ensemble de méthodes liées au calcul de distances exprimées en pixels.
 * <br/>
 * Cet objet est initialisé automatiquement par l'API et disponible
 * via le champ distances de n'importe quel gestionnaire, ou
 * de la classe {@code Agent} elle-même.
 * <br/>
 * <b>Attention :</b> Cette classe ne doit pas être instanciée par le concepteur 
 * de l'agent : récupérez une instance via {@code Agent} ou un gestionnaire.
 *  
 * @author Vincent Labatut
 */
public final class AiPixelDistanceTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiPixelDistanceTools(AiZone zone)
	{	this.zone = zone;
	
		this.leftX = zone.getPixelLeftX();
		this.topY = zone.getPixelTopY();
		this.height = zone.getPixelHeight();
		this.width = zone.getPixelWidth();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone utilisant cet objet */
	private AiZone zone;
	/** Abscisse des pixels du bord gauche */
	private double leftX;
	/** Ordonnée des pixels du bord supérieur */
	private double topY;
	/** Hauteur en pixels */
	private double height;
	/** Largeur en pixels */
	private double width;
	
	/////////////////////////////////////////////////////////////////
	// COMPOSITE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(double x1, double y1, double x2, double y2)
	{	double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,leftX,topY,height,width);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,leftX,topY,height,width);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en pixels.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiSprite sprite1, AiSprite sprite2)
	{	double result = getDistance(sprite1, sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction indiquée par le 
	 * paramètre direction, qui peut correspondre à un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
	{	double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,leftX,topY,height,width);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les emplacements passés
	 * en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location1
	 * 		Emplacement du premier point.
	 * @param location2
	 * 		Emplacement du second point.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiLocation location1, AiLocation location2, Direction direction)
	{	double posX1 = location1.getPosX();
		double posY1 = location1.getPosY();
		double posX2 = location2.getPosX();
		double posY2 = location2.getPosY();
		double result = getDistance(posX1,posY1,posX2,posY2,direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les emplacements passés
	 * en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param location1
	 * 		Emplacement du premier point.
	 * @param location2
	 * 		Emplacement du second point.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiLocation location1, AiLocation location2)
	{	double posX1 = location1.getPosX();
		double posY1 = location1.getPosY();
		double posX2 = location2.getPosX();
		double posY2 = location2.getPosY();
		double result = getDistance(posX1,posY1,posX2,posY2);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre les coordonnées et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(double x1, double y1, AiTile tile, Direction direction)
	{	double result = 0;
		AiTile tile0 = zone.getTile(x1,y1);
		if(!tile0.equals(tile))
		{	result = result + getHorizontalDistance(x1, tile, direction);
			result = result + getVerticalDistance(y1, tile, direction);
		}
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre les coordonnées et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(double x1, double y1, AiTile tile)
	{	double result = getDistance(x1,y1,tile,Direction.NONE);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre l'emplacement et la case passés en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location
	 * 		Emplacement du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiLocation location, AiTile tile, Direction direction)
	{	double x = location.getPosX();
		double y = location.getPosY();
		double result = getDistance(x,y,tile,direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre l'emplacement et la case passés en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param location
	 * 		Emplacement du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getDistance(AiLocation location, AiTile tile)
	{	double x = location.getPosX();
		double y = location.getPosY();
		double result = getDistance(x,y,tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PRIMARY				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * et seulement pour l'axe horizontal, entre la coordonnée
	 * spécifiée et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getHorizontalDistance(double x1, AiTile tile, Direction direction)
	{	double result = 0;
		double yCenter = tile.getPosY();
		AiTile t = zone.getTile(x1, yCenter);
		if(!tile.equals(t))
		{	List<Direction> directions = new ArrayList<Direction>();
			if(direction==Direction.NONE)
			{	directions.add(Direction.LEFT);
				directions.add(Direction.RIGHT);
			}
			else
				directions.add(direction);
			
			double xCenter = tile.getPosX();
			double dim = AiTile.getSize();
			result = Double.MAX_VALUE;
			for(Direction dir: directions)
			{	double d = - dir.getIntFromDirection()[0];
				double x2 = xCenter + d*dim/2;
				if(d>0)	
					x2--;
				double temp = getDistance(x1, yCenter, x2, yCenter, dir);
				if(temp<result)
					result = temp;
			}
			
//			double x2L = xCenter - dim/2 - 1;
//			double x2R = xCenter + dim/2;
//			double dxL = x2L - x1;
//			double dxR = x2R - x1;
//			double absDxL = Math.abs(dxL);
//			double absDxR = Math.abs(dxR);
//			double dx;
//			double direct;
//			if(absDxL<absDxR)
//			{	dx = dxL;
//				direct = absDxL;
//			}
//			else
//			{	dx = dxR;
//				direct = absDxR;
//			}
//			double indirect = pixelWidth - direct - dim;
//			Direction dir = direction.getHorizontalPrimary();
//			if(dir==Direction.NONE)
//				result = Math.min(direct,indirect);
//			else
//			{	Direction d = Direction.getHorizontalFromDouble(dx);
//				if(dir==d)
//					result = direct;
//				else
//					result = indirect;
//			}		
		}
		
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * et seulement pour l'axe vertical, entre la coordonnée
	 * spécifiée et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getVerticalDistance(double y1, AiTile tile, Direction direction)
	{	double result = 0;
		double xCenter = tile.getPosX();
		AiTile t = zone.getTile(xCenter, y1);
		if(!tile.equals(t))
		{	List<Direction> directions = new ArrayList<Direction>();
			if(direction==Direction.NONE)
			{	directions.add(Direction.DOWN);
				directions.add(Direction.UP);
			}
			else
				directions.add(direction);
			
			double yCenter = tile.getPosY();
			double dim = AiTile.getSize();
			result = Double.MAX_VALUE;
			for(Direction dir: directions)
			{	double d = - dir.getIntFromDirection()[1];
				double y2 = yCenter + d*dim/2;
				if(d>0)	
					y2--;
				double temp = getDistance(xCenter, y1, xCenter, y2, dir);
				if(temp<result)
					result = temp;
			}
			
//			double y2U = yCenter - dim/2 - 1;
//			double y2D = yCenter + dim/2;
//			double dyU = y2U - y1;
//			double dyD = y2D - y1;
//			double absDyU = Math.abs(dyU);
//			double absDyD = Math.abs(dyD);
//			double dy;
//			double direct;
//			if(absDyU<absDyD)
//			{	dy = dyU;
//				direct = absDyU;
//			}
//			else
//			{	dy = dyD;
//				direct = absDyD;
//			}
//			double indirect = pixelWidth - direct - dim;
//			Direction dir = direction.getVerticalPrimary();
//			if(dir==Direction.NONE)
//				result = Math.min(direct,indirect);
//			else
//			{	Direction d = Direction.getVerticalFromDouble(dy);
//				if(dir==d)
//					result = direct;
//				else
//					result = indirect;
//			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// DIFFERENCE				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la différence entre les coordonnées de deux points,
	 * exprimée en pixels. Le résultat est un tableau contenant
	 * deux valeurs : la première pour l'axe des abscisse et la seconde
	 * pour celui des ordonnées. Les valeurs correspondent à des
	 * distances signées, le signe dépendant de la position relative
	 * des deux points. On a x1+deltax = x2 et y1+deltay = y2.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @param direction
	 * 		La direction dans laquelle calculer la difference.
	 * @return
	 * 		Un tableau de deux réels.
	 */
	public double[] getDifference(double x1, double y1, double x2, double y2, Direction direction)
	{	double dx = DeltaTools.getDeltaX(x1,x2,direction.getHorizontalPrimary(),leftX,width);
		if(ApproximationTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = DeltaTools.getDeltaY(y1,y2,direction.getVerticalPrimary(),topY,height);
		if(ApproximationTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		double result[] = {dx,dy};
		return result;
	}
	
	/**
	 * Comme {@link #getDifference(double, double, double, double, Direction)},
	 * mais on considère automatiquement la direction correspondant à la plus
	 * petite distance.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @return
	 * 		Un tableau de deux réels.
	 */
	public double[] getDifference(double x1, double y1, double x2, double y2)
	{	double result[] = getDifference(x1,y1,x2,y2,Direction.NONE);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TRANSLATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule le résultat de la translation de coordonnées
	 * (dx,dy) appliquée au point (x,y). Les coordonnées
	 * résultantes sont normalisées pour être dans la zone de jeu.
	 * <br/>
	 * Il est recommandé d'utiliser cette méthode pour effectuer
	 * des soustractions/additions sur les coordonnées, plutot
	 * que directement utiliser les opérateurs + et -, afin
	 * d'éviter de sortir de la zone.
	 * 
	 * @param x
	 * 		Abscisse du point original.
	 * @param y
	 * 		Ordonnée du point original.
	 * @param dx
	 * 		Translation sur l'axe des abscisses.
	 * @param dy
	 * 		Translation sur l'axe des ordonnées.
	 * @return
	 * 		Un tableau contenant les coordonnées du résultat de la translation.
	 */
	public double[] getTranslatation(double x, double y, double dx, double dy)
	{	x = x + dx;
		y = y + dy;
		double result[] = PositionTools.normalizePosition(x,y,leftX,topY,height,width);
		return result;
	}
	
}
