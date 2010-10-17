package org.totalboumboum.ai.v201011.adapter.communication;

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


import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiOutput
{
	public AiOutput(AiZone zone)
	{	this.zone = zone;
		tileColors = new Color[zone.getHeight()][zone.getWidth()];
		tileTexts = new String[zone.getHeight()][zone.getWidth()];
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void reinit()
	{	reinitPaths();
		reinitTileColors();
		reinitTileTexts();
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiZone zone;
		
	/////////////////////////////////////////////////////////////////
	// PATHS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste de chemin à afficher par dessus la zone de jeu */
	private final List<AiPath> paths = new ArrayList<AiPath>();
	/** couleur des chemins à afficher */
	private final List<Color> pathColors = new ArrayList<Color>();
	
	/**
	 * réinitialise les chemins à afficher
	 */
	private void reinitPaths()
	{	paths.clear();
		pathColors.clear();
	}
	
	/**
	 * rajoute un chemin dans la liste des chemins à afficher.
	 * La représentation graphique d'un chemin est une ligne
	 * suivant les centres des cases traversées par le chemin 
	 * 
	 * @param path	chemin à afficher
	 * @param color	couleur associée à ce chemin
	 */
	public void addPath(AiPath path, Color color)
	{	if(color!=null && path!=null && !path.isEmpty())
		{	paths.add(path);
			pathColors.add(color);
		}
	}
	
	/**
	 * renvoie la liste des chemins à afficher
	 * 
	 * @return	une liste de chemins
	 */
	public List<AiPath> getPaths()
	{
		return paths;
	}

	/**
	 * renvoie la liste des couleurs associées aux chemins
	 * 
	 * @return	une liste de couleurs
	 */
	public List<Color> getPathColors()
	{
		return pathColors;
	}

	/////////////////////////////////////////////////////////////////
	// TILES COLORING	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleurs pour remplir certaines cases du jeu */
	private Color tileColors[][];
	
	/**
	 * initialise les couleurs des cases
	 * (null = aucune couleur initialement)
	 */
	private void reinitTileColors()
	{	for(int line=0;line<zone.getHeight();line++)
			for(int col=0;col<zone.getWidth();col++)
				tileColors[line][col] = null;
	}
	
	/**
	 * modifie la couleur d'une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * La valeur null correspond à une absence de couleur.
	 * 
	 * @param tile	case à colorier
	 * @param color	couleur du coloriage
	 */
	public void setTileColor(AiTile tile, Color color)
	{	int line = tile.getLine();
		int col = tile.getCol();
		setTileColor(line,col,color);		
	}

	/**
	 * modifie la couleur d'une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * La valeur null correspond à une absence de couleur.
	 * 
	 * @param line	ligne de la case à colorier
	 * @param col	colonne de la case à colorier
	 * @param color	couleur du coloriage
	 */
	public void setTileColor(int line, int col, Color color)
	{	tileColors[line][col] = color;	
	}

	/**
	 * renvoie les couleurs à utiliser pour colorier les cases
	 * 
	 * @return	une matrice de couleurs
	 */
	public Color[][] getTileColors()
	{	return tileColors;
	}

	/////////////////////////////////////////////////////////////////
	// TEXTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** texte à afficher sur les cases de la zone de jeu */
	private String tileTexts[][];
	
	/**
	 * réinitialise les textes associés aux cases
	 */
	private void reinitTileTexts()
	{	for(int line=0;line<zone.getHeight();line++)
			for(int col=0;col<zone.getWidth();col++)
				tileTexts[line][col] = null;
	}
	
	/**
	 * modifie le texte associé à une case. Permet
	 * par exemple d'afficher des heuristiques, des couts
	 * en temps réel.
	 * 
	 * @param tile	case associée au texte
	 * @param text	texte à afficher sur cette case
	 */
	public void setTileText(AiTile tile, String text)
	{	int line = tile.getLine();
		int col = tile.getCol();
		setTileText(line,col,text);
	}
	
	/**
	 * modifie le texte associé à une case. Permet
	 * par exemple d'afficher des heuristiques, des couts
	 * en temps réel.
	 * 
	 * @param line	ligne de la case associée au texte
	 * @param col	colonne de la case associée au texte
	 * @param text	texte à afficher sur cette case
	 */
	public void setTileText(int line, int col, String text)
	{	tileTexts[line][col] = text;	
	}

	/**
	 * renvoie les textes à afficher sur les cases
	 * 
	 * @return	une matrice de textes
	 */
	public String[][] getTileTexts()
	{	return tileTexts;
	}

}
