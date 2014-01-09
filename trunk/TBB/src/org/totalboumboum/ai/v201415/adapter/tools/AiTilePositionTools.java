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

import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.tools.level.PositionTools;

/**
 * Ensemble de méthodes liées au calcul de positions exprimées en cases.
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
public final class AiTilePositionTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiTilePositionTools(AiZone zone)
	{	this.zone = zone;
		
		this.height = zone.getHeight();
		this.width = zone.getWidth();
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone utilisant cet objet */
	@SuppressWarnings("unused")
	private AiZone zone;
	/** Hauteur en cases */
	private int height;
	/** Largeur en cases */
	private int width;
	
	/////////////////////////////////////////////////////////////////
	// NORMALIZATION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Prend n'importe quelles coordonnées exprimées en cases et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param row
	 * 		Ligne de la case.
	 * @param col
	 * 		Colonne de la case.
	 * @return	
	 * 		Un tableau contenant les versions normalisées de row et col.
	 */
	public int[] normalizePosition(int row, int col)
	{	return PositionTools.normalizePosition(row,col,height,width);
	}

	/**
	 * Prend n'importe quelle abscisse exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param col
	 * 		Colonne de la case.
	 * @return	
	 * 		La version normalisée de col.
	 */
	public int normalizePositionCol(int col)
	{	return PositionTools.normalizePositionCol(col,width);
	}

	/**
	 * Prend n'importe quelle ordonnée exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param row
	 * 		Ligne de la case.
	 * @return	
	 * 		La version normalisée de row.
	 */
	public int normalizePositionRow(int row)
	{	return PositionTools.normalizePositionRow(row,height);
	}

	/////////////////////////////////////////////////////////////////
	// POSITION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
}
