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
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.computing.ApproximationTools;
import org.totalboumboum.tools.level.DeltaTools;

/**
 * Ensemble de méthodes liées au calcul de directions. 
 * <br/>
 * Cet objet est initialisé automatiquement par l'API et disponible
 * via l'objet zone concerné (que ce soir {@link AiDataZone} ou {@link AiSimBlock}).
 * <br/>
 * <b>Attention :</b> Cette classe ne doit pas être instanciée par le concepteur 
 * de l'agent : récupérez une instance existante, grâce à l'objet zone concerné.
 *  
 * @author Vincent Labatut
 */
public final class AiDirectionTools extends AiAbstractTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiDirectionTools(AiZone zone)
	{	super(zone);
	
		this.tHeight = zone.getHeight();
		this.tWidth = zone.getWidth();
		this.pLeftX = zone.getPixelLeftX();
		this.pTopY = zone.getPixelTopY();
		this.pHeight = zone.getPixelHeight();
		this.pWidth = zone.getPixelWidth();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Hauteur en cases */
	private int tHeight;
	/** Largeur en cases */
	private int tWidth;
	/** Abscisse des pixels du bord gauche */
	private double pLeftX;
	/** Ordonnée des pixels du bord supérieur */
	private double pTopY;
	/** Hauteur en pixels */
	private double pHeight;
	/** Largeur en pixels */
	private double pWidth;
	
	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la direction de la case target relativement à la case source.
	 * Par exemple, la case target de coordonnées (5,5) est à droite de
	 * la case source de coordonnées (5,6).
	 * <br/>
	 * Cette fonction peut être utile quand on veut savoir dans quelle direction
	 * il faut se déplacer pour aller de la case source à la case target.
	 * <br/>
	 * <b>ATTENTION 1 :</b> si les deux cases ne sont pas des voisines directes (ie. ayant un coté commun),
	 * il est possible que cette méthode renvoie une direction composite,
	 * c'est à dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. Référez-vous à 
	 * la classe {@link Direction} pour plus d'informations sur ces valeurs.
	 * <br/>
	 * <b>ATTENTION 2 :</b> comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette méthode renvoie la direction du plus court chemin (sans considérer les éventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le résultat sera
	 * {@link Direction#RIGHT RIGHT}, car {@link Direction#LEFT LEFT} permet également d'atteindre la case, 
	 * mais en parcourant un chemin plus long.
	 * <pre>
	 * <t> S>>>>>>>>>>T  distance=11
	 * <t>>S..........T> distance=1
	 * </pre>
	 * 
	 * @param source
	 * 		Case de référence.
	 * @param target
	 * 		Case dont on veut connaitre la direction.
	 * @return	
	 * 		La direction de {@code target} par rapport à {@code source}.
	 */
	public Direction getDirection(AiTile source, AiTile target)
	{	// differences
		int dc = target.getCol()-source.getCol();
		int dr = target.getRow()-source.getRow();
		
		// direction
		Direction temp = Direction.getCompositeFromDouble(dc,dr);
		Direction tempX = temp.getHorizontalPrimary();
		Direction tempY = temp.getVerticalPrimary();
		
		// distances
		int distDirX = Math.abs(dc);
		int distIndirX = tWidth-distDirX;
		if(distDirX>distIndirX)
			tempX = tempX.getOpposite();
		int distDirY = Math.abs(dr);
		int distIndirY = tHeight-distDirY;
		if(distDirY>distIndirY)
			tempY = tempY.getOpposite();
		
		// result
		Direction result = Direction.getComposite(tempX,tempY);
		return result;
	}
	
	/**
	 * Pareil que {@link #getDirection(double, double, double, double)}, mais 
	 * la méthode prend des emplacements en paramètres
	 * plutôt que des coordonnées.
	 * 
	 * @param source
	 * 		Emplacement de référence
	 * @param  target
	 * 		Emplacement dont on veut connaitre la direction
	 * @return	
	 * 		La direction de {@code target} par rapport à {@code source}.
	 */
	public Direction getDirection(AiLocation source, AiLocation target)
	{	double x1 = source.getPosX();
		double y1 = source.getPosY();
		double x2 = target.getPosX();
		double y2 = target.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Calcule la direction pour aller du sprite source au sprite target.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être {@code NONE} si jamais les deux sprites sont au même endroit.
	 * 
	 * @param source
	 * 		Sprite de départ.
	 * @param target
	 * 		Sprite de destination.
	 * @return	
	 * 		La direction pour aller de source vers target.
	 */
	public Direction getDirection(AiSprite source, AiSprite target)
	{	double x1 = source.getPosX();
		double y1 = source.getPosY();
		double x2 = target.getPosX();
		double y2 = target.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;		
	}
	
	/**
	 * Calcule la direction pour aller du sprite à la case passés en paramètres.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être {@code NONE} si jamais les deux sprites sont au même endroit
	 * 
	 * @param sprite
	 * 		Sprite en déplacement.
	 * @param tile
	 * 		Case de destination.
	 * @return	
	 * 		La direction pour aller du sprite vers la case.
	 */
	public Direction getDirection(AiSprite sprite, AiTile tile)
	{	double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Calcule la direction pour aller de la position (x1,y1) à la position (x2,y2)
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau).
	 * La direction peut être {@code NONE} si jamais les deux positions 
	 * sont équivalentes.
	 * 
	 * @param x1
	 * 		Première position horizontale en pixels.
	 * @param y1
	 * 		Première position verticale en pixels.
	 * @param x2
	 * 		Seconde position horizontale en pixels.
	 * @param y2
	 * 		Seconde position verticale en pixels.
	 * @return	
	 * 		La direction correspondant au chemin le plus court.
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	double dx = DeltaTools.getDeltaX(x1,x2,pLeftX,pWidth);
		if(ApproximationTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = DeltaTools.getDeltaY(y1,y2,pTopY,pHeight);
		if(ApproximationTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}
}
