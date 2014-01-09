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

import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Ensemble de méthodes liées au calcul de points de contacts entre
 * différents types d'objets et les cases. Ces méthodes sont notamment
 * utilisées lors du calcul de distances entre sprites et cases.
 * Elles sont, <i>a priori</i>, peu utiles <i>directement</i> lors du 
 * développement d'un agent. 
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
public final class AiContactPointTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiContactPointTools(AiZone zone)
	{	this.zone = zone;
	
		this.tileSize = AiTile.getSize();
		
		this.dirTools = zone.getDirectionTools();
		this.pixPosTools = zone.getPixelPositionTools();
		
		AiHero ownHero = zone.getOwnHero();
		this.ownColor = ownHero.getColor();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone utilisant cet objet */
	private AiZone zone;
	/** Couleur du personnage contrôlé */
	private PredefinedColor ownColor;
	/** Taille d'une case */
	private double tileSize;
	
	/////////////////////////////////////////////////////////////////
	// TOOLS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Outils dédiés au calcul de directions */
	private AiDirectionTools dirTools;
	/** Outils dédiés au calcul de positions (en pixels) */
	private AiPixelPositionTools pixPosTools;

	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le point de contact entre
	 * les centres des deux cases passées en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * <b>Attention :</b> le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> les deux cases doivent être 
	 * des voisines directes.
	 * 
	 * @param tile1
	 * 		La première case.
	 * @param tile2
	 * 		La seconde case.
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si les deux cases ne sont pas des voisines directes.
	 */
	public double[] getContactPoint(AiTile tile1, AiTile tile2)
	{	double x = 0;
		double y = 0;
		
		// on récupère les coordonnées des cases
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		
		// direction entre les points
		Direction direction = dirTools.getDirection(tile1,tile2);
		if(direction.isComposite())
			throw new IllegalArgumentException("The tiles must be direct neighbors ("+ownColor+" player)");
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tileSize/2;
			if(dir[0]<0)
				x--;
			y = tile1y;
		}
		else if(direction.isVertical())
		{	x = tile1x;
			y = tile1y + dir[1]*tileSize/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = pixPosTools.normalizePosition(x,y);
		return result;
	}
	
	/**
	 * Renvoie le point de contact entre
	 * les deux points dont les coordonnées sont
	 * passées en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * <b>Attention :</b> le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> les deux points doivent appartenir 
	 * à des cases voisines directes.
	 * 
	 * @param x1
	 * 		L'abscisse du premier point.
	 * @param y1
	 * 		L'ordonnée du premier point.
	 * @param x2
	 * 		L'abscisse du second point.
	 * @param y2
	 * 		L'ordonnée du second point.
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si les deux points n'appartiennent pas à des cases voisines.
	 */
	public double[] getContactPoint(double x1, double y1, double x2, double y2)
	{	double x = 0;
		double y = 0;
	
		// on récupère les cases
		AiTile tile1 = zone.getTile(x1,y1);
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		AiTile tile2 = zone.getTile(x2,y2);
		
		// direction entre les points
		Direction direction = dirTools.getDirection(tile1,tile2);
		if(direction.isComposite())
			throw new IllegalArgumentException("Points must be in direct neighbor tiles ("+ownColor+" player)");
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tileSize/2;
			if(dir[0]<0)
				x--;
			y = (y1 + y2) / 2;
		}
		else if(direction.isVertical())
		{	x = (x1 + x2) / 2;
			y = tile1y + dir[1]*tileSize/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = pixPosTools.normalizePosition(x,y);
		return result;
	}

	/**
	 * Renvoie le point de contact entre
	 * le point dont les coordonnées sont
	 * passées en paramètres et la case également
	 * passée en paramètre. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * <b>Attention :</b> le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> le point et la case passés
	 *  en paramètres doivent appartenir à des 
	 *  cases voisines directes.
	 * 
	 * @param x1
	 * 		L'abscisse du point.
	 * @param y1
	 * 		L'ordonnée du point.
	 * @param tile
	 * 		La case.
	 * @param manhattan
	 * 		Indique si la trajectoire doit être directe ({@code false}) ou si seuls
	 * 		les déplacements horizontaux et verticaux sont tolérés ({@code true}).
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le point et la case ne sont pas voisins.
	 */
	public double[] getContactPoint(double x1, double y1, AiTile tile, boolean manhattan)
	{	double x = 0;
		double y = 0;
		
		// on récupère les cases
		AiTile tile1 = zone.getTile(x1,y1);
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		double tile2x = tile.getPosX();
		double tile2y = tile.getPosY();
		
		// direction entre les points
		Direction direction = dirTools.getDirection(tile1,tile);
		if(direction.isComposite())
			throw new IllegalArgumentException("Points must be in direct neighbor tiles ("+ownColor+" player)");
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tileSize/2;
			if(dir[0]<0)
				x--;
			if(manhattan)
				y = tile2y;
			else
				y = (y1 + tile2y) / 2;
		}
		else if(direction.isVertical())
		{	if(manhattan)
				x = tile2x;
			else
				x = (x1 + tile2x) / 2;
			y = tile1y + dir[1]*tileSize/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = pixPosTools.normalizePosition(x,y);
		return result;
	}

	/**
	 * Renvoie le point de contact entre
	 * l'emplacement et la case passés en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * <b>Attention :</b> le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> l'emplacement et la case passés
	 *  en paramètres doivent appartenir à des 
	 *  cases voisines directes.
	 * 
	 * @param location
	 * 		L'emplacement.
	 * @param tile
	 * 		La case.
	 * @param manhattan
	 * 		Indique si la trajectoire doit être directe ({@code false}) ou si seuls
	 * 		les déplacements horizontaux et verticaux sont tolérés ({@code true}).
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si l'emplacement et la case ne sont pas voisins.
	 */
	public double[] getContactPoint(AiLocation location, AiTile tile, boolean manhattan)
	{	double x = 0;
		double y = 0;
		
		// on récupère les cases
		double x1 = location.getPosX();
		double y1 = location.getPosY();
		AiTile tile1 = location.getTile();
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		double tile2x = tile.getPosX();
		double tile2y = tile.getPosY();
		
		// direction entre les points
		Direction direction = dirTools.getDirection(tile1,tile);
		if(direction.isComposite())
			throw new IllegalArgumentException("Both location and tile must be direct neighbors ("+ownColor+" player)");
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tileSize/2;
			if(dir[0]<0)
				x--;
			if(manhattan)
				y = tile2y;
			else
				y = (y1 + tile2y) / 2;
		}
		else if(direction.isVertical())
		{	if(manhattan)
				x = tile2x;
			else
				x = (x1 + tile2x) / 2;
			y = tile1y + dir[1]*tileSize/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = pixPosTools.normalizePosition(x,y);
		return result;
	}
}
