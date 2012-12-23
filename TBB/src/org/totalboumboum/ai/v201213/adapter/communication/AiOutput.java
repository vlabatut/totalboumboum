package org.totalboumboum.ai.v201213.adapter.communication;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;

/**
 * Cette classe permet d'accéder à la sortie graphique de l'agent.
 * Il est ainsi possible d'afficher, en cours de jeu :
 * <ul>
 * 		<li>Des chemins, sous forme de lignes droites.</li>
 * 		<li>Des couleurs en transparence sur les cases.</li>
 * 		<li>Du texte dans les cases.</li>
 * </ul> 
 * A noter que l'objet {@code AiOutput} de l'agent est réinitialisé
 * à chaque itération, donc il faut systématiquement le modifier
 * pour avoir un affichage en continu.
 * 
 * @author Vincent Labatut
 */
public class AiOutput
{
	/**
	 * Construit un objet représentant la sortie graphique
	 * de l'agent.
	 * 
	 * @param zone
	 * 		La zone concernée.
	 */
	@SuppressWarnings("unchecked")
	public AiOutput(AiZone zone)
	{	this.zone = zone;
		tileColors = new ArrayList[zone.getHeight()][zone.getWidth()];
		tileTexts = new List[zone.getHeight()][zone.getWidth()];
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Efface toutes les données spécifiées précédemment.
	 */
	public void reinit()
	{	reinitPaths();
		reinitTileColors();
		reinitTileTexts();
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Représentation interne de la zone */
	private AiZone zone;
		
	/////////////////////////////////////////////////////////////////
	// PATHS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de chemins à afficher par dessus la zone de jeu */
	private final Map<AiPath,Color> paths = new HashMap<AiPath,Color>();
	/** Vue externe immuable de paths */
	private final Map<AiPath,Color> externalPaths = Collections.unmodifiableMap(paths);
	
	/**
	 * Réinitialise les chemins à afficher.
	 */
	private void reinitPaths()
	{	paths.clear();
	}
	
	/**
	 * Rajoute un chemin dans la liste des chemins à afficher.
	 * La représentation graphique d'un chemin est une ligne
	 * suivant les centres des cases traversées par le chemin. 
	 * 
	 * @param path
	 * 		Chemin à afficher.
	 * @param color
	 * 		Couleur associée à ce chemin.
	 */
	public void addPath(AiPath path, Color color)
	{	if(color!=null && path!=null && !path.isEmpty())
			paths.put(path,color);
	}
	
	/**
	 * Renvoie la liste des chemins à afficher.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas destinée à être utilisée par
	 * les agents, mais par le moteur du jeu. Toute tentative de modification
	 * provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		Une liste de chemins.
	 */
	public Map<AiPath,Color> getPaths()
	{
		return externalPaths;
	}

	/////////////////////////////////////////////////////////////////
	// TILES COLORING	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Couleurs pour remplir certaines cases du jeu */
	private List<Color> tileColors[][];
	
	/**
	 * Réinitialise les couleurs des cases
	 * ({@code null} = aucune couleur initialement).
	 */
	private void reinitTileColors()
	{	for(int row=0;row<zone.getHeight();row++)
			for(int col=0;col<zone.getWidth();col++)
				tileColors[row][col] = new ArrayList<Color>();
	}
	
	/**
	 * Modifie la couleur d'une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * La valeur {@code null} correspond à une absence de couleur.
	 * <br/>
	 * Si on veut affecter plusieurs couleurs à une case,
	 * il faut plutôt utiliser les méthodes {@code addTileColor}.
	 * 
	 * @param tile
	 * 		Case à colorer.
	 * @param color
	 * 		Couleur du coloriage.
	 */
	public void setTileColor(AiTile tile, Color color)
	{	int row = tile.getRow();
		int col = tile.getCol();
		setTileColor(row,col,color);		
	}

	/**
	 * Modifie la couleur d'une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * La valeur {@code null} correspond à une absence de couleur.
	 * <br/>
	 * Si on veut affecter plusieurs couleurs à une case,
	 * il faut plutôt utiliser les méthodes {@code addTileColor}.
	 * 
	 * @param row
	 * 		Ligne de la case à colorier.
	 * @param col
	 * 		Colonne de la case à colorier.
	 * @param color
	 * 		Couleur du coloriage.
	 */
	public void setTileColor(int row, int col, Color color)
	{	tileColors[row][col].clear();
		if(color!=null)
			tileColors[row][col].add(color);	
	}

	/**
	 * Ajoute une couleur à une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * 
	 * @param tile
	 * 		Case à colorier
	 * @param color
	 * 		Couleur du coloriage
	 */
	public void addTileColor(AiTile tile, Color color)
	{	int row = tile.getRow();
		int col = tile.getCol();
		addTileColor(row,col,color);
	}

	/**
	 * Modifie la couleur d'une case, qui sera affichée
	 * en transparence par dessus la zone de jeu.
	 * La valeur {@code null} correspond à une absence de couleur.
	 * <br/>
	 * Si on veut affecter plusieurs couleurs à une case,
	 * il faut plutôt utiliser les méthodes {@code addTileColor}.
	 * 
	 * @param row
	 * 		Ligne de la case à colorier
	 * @param col
	 * 		Colonne de la case à colorier
	 * @param color
	 * 		Couleur du coloriage
	 */
	public void addTileColor(int row, int col, Color color)
	{	if(color!=null)
			tileColors[row][col].add(color);	
	}

	/**
	 * Renvoie les couleurs à utiliser pour colorier les cases.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas destinée à être utilisée par
	 * les agents, mais par le moteur du jeu. La matrice renvoyée ne doit
	 * surtout pas être modifiée directement par un agent.
	 * 
	 * @return
	 * 		Une matrice de couleurs.
	 */
	public List<Color>[][] getTileColors()
	{	return tileColors;
	}

	/////////////////////////////////////////////////////////////////
	// TEXTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Détermine si le texte sera affiché en normalement ou en gras */
	private boolean bold = false;
	/**Texte à afficher sur les cases de la zone de jeu */
	private List<String> tileTexts[][];

	/**
	 * Change le mode d'affichage du texte : gras ou pas.
	 * 
	 * @param bold
	 * 		La valeur vrai indique que l'affichage sera effectué en gras.
	 */
	public void setBold(boolean bold)
	{	this.bold = bold;
	}
	
	/**
	 * Permet de savoir si le mode d'affichage courant
	 * du teste est en gras ou pas.
	 * 
	 * @return
	 * 		{@code true} ssi le mode d'affichage courant du texte est en gras.
	 */
	public boolean isBold()
	{	return bold;
	}
	
	/**
	 * Réinitialise les textes associés aux cases.
	 */
	private void reinitTileTexts()
	{	for(int row=0;row<zone.getHeight();row++)
			for(int col=0;col<zone.getWidth();col++)
				tileTexts[row][col] = new ArrayList<String>();
	}
	
	/**
	 * Modifie le texte associé à une case. Permet
	 * par exemple d'afficher des heuristiques, des couts
	 * en temps réel.
	 * 
	 * @param tile
	 * 		Case associée au texte.
	 * @param text
	 * 		Texte à afficher sur cette case.
	 */
	public void setTileText(AiTile tile, String text)
	{	int row = tile.getRow();
		int col = tile.getCol();
		setTileText(row,col,text);
	}
	
	/**
	 * Modifie le texte associé à une case. Permet
	 * par exemple d'afficher des heuristiques, des coûts
	 * en temps réel.
	 * 
	 * @param row
	 * 		Ligne de la case associée au texte.
	 * @param col
	 * 		Colonne de la case associée au texte.
	 * @param text
	 * 		Texte à afficher sur cette case.
	 */
	public void setTileText(int row, int col, String text)
	{	tileTexts[row][col].clear();
		tileTexts[row][col].add(text);	
	}

	/**
	 * Modifie le texte associé à une case. Permet
	 * par exemple d'afficher des heuristiques, des coûts
	 * en temps réel.
	 * 
	 * @param row
	 * 		Ligne de la case associée au texte.
	 * @param col	
	 * 		Colonne de la case associée au texte.
	 * @param texts	
	 * 		Tableau de textes à afficher sur cette case.
	 */
	public void setTileTexts(int row, int col, String texts[])
	{	tileTexts[row][col].clear();
		for(String text: texts)
			tileTexts[row][col].add(text);		
	}

	/**
	 * Renvoie les textes à afficher sur les cases.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas destinée à être utilisée par
	 * les agents, mais par le moteur du jeu. La matrice renvoyée ne doit
	 * surtout pas être modifiée directement par un agent.
	 * 
	 * @return
	 * 		Une matrice de textes.
	 */
	public List<String>[][] getTileTexts()
	{	return tileTexts;
	}
}
