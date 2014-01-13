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
import org.totalboumboum.tools.level.DistanceTileTools;
import org.totalboumboum.tools.level.PositionTools;

/**
 * Ensemble de méthodes liées au calcul de distances exprimées en cases.
 * <br/>
 * Cet objet est initialisé automatiquement par l'API et disponible
 * via l'objet zone concerné (que ce soir {@link AiDataZone} ou {@link AiSimBlock}).
 * <br/>
 * <b>Attention :</b> Cette classe ne doit pas être instanciée par le concepteur 
 * de l'agent : récupérez une instance existante, grâce à l'objet zone concerné.
 *  
 * @author Vincent Labatut
 */
public final class AiTileDistanceTools extends AiAbstractTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiTileDistanceTools(AiZone zone)
	{	super(zone);
	
		this.height = zone.getHeight();
		this.width = zone.getWidth();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Hauteur en cases */
	private int height;
	/** Largeur en cases */
	private int width;
	
	/////////////////////////////////////////////////////////////////
	// DISTANCE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan entre les cases de coordonnées
	 * (row1,col1) et (row2,col2), exprimée en cases.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param  row1
	 * 		Ligne de la première case.
	 * @param col1
	 * 		Colonne de la première case.
	 * @param  row2
	 * 		Ligne de la seconde case.
	 * @param  col2
	 * 		Colonne de la seconde case.
	 * @param  direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(int row1, int col1, int row2, int col2, Direction direction)
	{	int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,direction,height,width);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les cases de coordonnées
	 * (row1,col1) et (row2,col2), exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param row1
	 * 		Ligne de la première case.
	 * @param  col1
	 * 		Colonne de la première case.
	 * @param  row2
	 * 		Ligne de la seconde case.
	 * @param col2
	 * 		Colonne de la seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(int row1, int col1, int row2, int col2)
	{	int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,Direction.NONE,height,width);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param tile1
	 * 		Première case.
	 * @param tile2
	 * 		Seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(AiTile tile1, AiTile tile2)
	{	int result = getDistance(tile1,tile2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param tile1
	 * 		Première case.
	 * @param tile2
	 * 		Seconde case.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(AiTile tile1, AiTile tile2, Direction direction)
	{	int row1 = tile1.getRow();
		int col1 = tile1.getCol();
		int row2 = tile2.getRow();
		int col2 = tile2.getCol();
		int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,height,width);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases.
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
	public int getDistance(AiSprite sprite1, AiSprite sprite2)
	{	int result = getDistance(sprite1,sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
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
	public int getDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
	{	AiTile tile1 = sprite1.getTile();
		AiTile tile2 = sprite2.getTile();
		int result = getDistance(tile1,tile2);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en cases,
	 *  entre les cases contenant
	 * les emplacements passés en paramètres.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location1
	 * 		Emplacement de la première case.
	 * @param location2
	 * 		Emplacement de la seconde case.
	 * @param  direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(AiLocation location1, AiLocation location2, Direction direction)
	{	AiTile tile1 = location1.getTile();
		AiTile tile2 = location2.getTile();
		int result = getDistance(tile1.getRow(),tile1.getCol(),tile2.getRow(),tile2.getCol(),direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimées en cases,
	 * entre les cases contenant les emplacements passés en paramètres.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param location1
	 * 		Emplacement de la première case.
	 * @param location2
	 * 		Emplacement de la seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getDistance(AiLocation location1, AiLocation location2)
	{	AiTile tile1 = location1.getTile();
		AiTile tile2 = location2.getTile();
		int result = getDistance(tile1.getRow(),tile1.getCol(),tile2.getRow(),tile2.getCol());
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TRANSLATIONS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule le résultat de la translation de coordonnées
	 * (drow,dcol) appliquée à la case (row,col). Les coordonnées
	 * résultantes sont normalisées pour être dans la zone de jeu.
	 * <br/>
	 * Il est recommandé d'utiliser cette méthode pour effectuer
	 * des soustractions/additions sur les coordonnées des cases, 
	 * plutôt que directement utiliser les opérateurs + et -, afin
	 * d'éviter de sortir de la zone.
	 * 
	 * @param row
	 * 		Abscisse du point original.
	 * @param col
	 * 		Ordonnée du point original.
	 * @param drow
	 * 		Translation sur l'axe des abscisses.
	 * @param dcol
	 * 		Translation sur l'axe des ordonnées.
	 * @return
	 * 		Un tableau contenant les coordonnées du résultat de la translation.
	 */
	public int[] getTranslatation(int row, int col, int drow, int dcol)
	{	row = row + drow;
		col = col + dcol;
		int result[] = PositionTools.normalizePosition(row,col,height,width);
		return result;
	}
}
