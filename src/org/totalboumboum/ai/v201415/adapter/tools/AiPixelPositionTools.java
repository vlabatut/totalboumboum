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

import org.totalboumboum.ai.v201415.adapter.data.AiSprite;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.data.internal.AiDataZone;
import org.totalboumboum.ai.v201415.adapter.model.full.AiSimBlock;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.tools.computing.ApproximationTools;
import org.totalboumboum.tools.level.PositionTools;

/**
 * Ensemble de méthodes liées au calcul de positions exprimées en pixels.
 * <br/>
 * Cet objet est initialisé automatiquement par l'API et disponible
 * via l'objet zone concerné (que ce soir {@link AiDataZone} ou {@link AiSimBlock}).
 * <br/>
 * <b>Attention :</b> Cette classe ne doit pas être instanciée par le concepteur 
 * de l'agent : récupérez une instance existante, grâce à l'objet zone concerné.
 *  
 * @author Vincent Labatut
 */
public final class AiPixelPositionTools extends AiAbstractTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiPixelPositionTools(AiZone zone)
	{	super(zone);
	
		this.leftX = zone.getPixelLeftX();
		this.topY = zone.getPixelTopY();
		this.height = zone.getPixelHeight();
		this.width = zone.getPixelWidth();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Abscisse des pixels du bord gauche */
	private double leftX;
	/** Ordonnée des pixels du bord supérieur */
	private double topY;
	/** Hauteur en pixels */
	private double height;
	/** Largeur en pixels */
	private double width;
	
	/////////////////////////////////////////////////////////////////
	// NORMALIZATION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Prend n'importe quelles coordonnées exprimées en pixels et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x
	 * 		Abscisse.
	 * @param y
	 * 		Ordonnée.
	 * @return	
	 * 		Un tableau contenant les versions normalisées de x et y.
	 */
	public double[] normalizePosition(double x, double y)
	{	return PositionTools.normalizePosition(x,y,leftX,topY,height,width);
	}

	/**
	 * Prend n'importe quelle abscisse exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x
	 * 		Abscisse.
	 * @return	
	 * 		La version normalisée de x.
	 */
	public double normalizePositionX(double x)
	{	return PositionTools.normalizePositionX(x,leftX,width);
	}
	
	/**
	 * Prend n'importe quelle ordonnée exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param y
	 * 		Ordonnée.
	 * @return	
	 * 		La version normalisée de y.
	 */
	public double normalizePositionY(double y)
	{	return PositionTools.normalizePositionY(y,topY,height);
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Teste si les deux sprites passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param sprite1
	 * 		Le premier sprite.
	 * @param sprite2
	 * 		Le second sprite.
	 * @return	
	 * 		{@code true} ssi les deux sprites sont au même endroit.
	 */
	public boolean hasSamePosition(AiSprite sprite1, AiSprite sprite2)
	{	boolean result;
		double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		result = hasSamePosition(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Teste si le sprite passé en paramètre occupent le
	 * centre de la case passée en paramètre, au pixel près.
	 * 
	 * @param sprite
	 * 		Le sprite concerné.
	 * @param tile
	 * 		La case à traiter.
	 * @return	
	 * 		{@code true} ssi le sprite est au centre de la case.
	 */
	public boolean hasSamePosition(AiSprite sprite, AiTile tile)
	{	boolean result;	
		double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		result = hasSamePosition(x1,y1,x2,y2);
		return result;
	}

	/**
	 * Teste si les deux points passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param x1
	 * 		L'abscisse de la première position.
	 * @param y1
	 * 		L'ordonnée de la première position.
	 * @param x2
	 * 		L'abscisse de la seconde position.
	 * @param y2
	 * 		L'ordonnée de la seconde position.
	 * @return	
	 * 		{@code true} ssi les deux positions sont équivalentes au pixel près.
	 */
	public boolean hasSamePosition(double x1, double y1, double x2, double y2)
	{	boolean result = true;	
		result = result && ApproximationTools.isRelativelyEqualTo(x1,x2);
		result = result && ApproximationTools.isRelativelyEqualTo(y1,y2);
		return result;
	}

	/**
	 * Teste si les deux emplacements passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param location1
	 * 		Le premier emplacement.
	 * @param location2
	 * 		Le second emplacement.
	 * @return	
	 * 		Renvoie {@code true} ssi les deux emplacement sont équivalents au pixel près.
	 */
	public boolean hasSamePosition(AiLocation location1, AiLocation location2)
	{	double x1 = location1.getPosX();
		double y1 = location1.getPosY();
		double x2 = location2.getPosX();
		double y2 = location2.getPosY();
		boolean result = hasSamePosition(x1,y1,x2,y2);
		return result;
	}
}
