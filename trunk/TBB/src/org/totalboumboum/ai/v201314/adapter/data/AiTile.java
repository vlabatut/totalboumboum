package org.totalboumboum.ai.v201314.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.io.Serializable;
import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * Représente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 */
public abstract class AiTile implements Comparable<AiTile>, Serializable
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la zone contenant cette case.
	 * 
	 * @return
	 * 		La zone contenant cette case.
	 */
	public abstract AiZone getZone();
	
	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ligne de la zone contenant cette case */
	protected int row;
	/** Colonne de la zone contenant cette case */
	protected int col;
		
	/** 
	 * Renvoie le numéro de la ligne contenant cette case.
	 * 
	 * @return	
	 * 		La ligne de cette case.
	 */
	public int getRow()
	{	return row;	
	}

	/** 
	 * Renvoie le numéro de la colonne contenant cette case.
	 *  
	 * @return	
	 * 		La colonne de cette case.
	 */
	public int getCol()
	{	return col;	
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Position de la case en pixels */
	protected double posX;
	/** Position de la case en pixels */
	protected double posY;
	
	/** 
	 * Renvoie l'abscisse de la case en pixels.
	 * 
	 * @return	
	 * 		L'abscisse de cette case.
	 */
	public double getPosX()
	{	return posX;	
	}
	
	/** 
	 * Renvoie l'ordonnée de la case en pixels.
	 * 
	 * @return	
	 * 		L'ordonnée de cette case.
	 */
	public double getPosY()
	{	return posY;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Longueur d'un côté de la case en pixels */
	protected double size;
		
	/** 
	 * Renvoie la taille de la case en pixels.
	 * 
	 * @return	
	 * 		Longueur d'un côté de la case en pixels.
	 */
	public double getSize()
	{	return size;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les blocks éventuellement contenus dans cette case.
	 */
	public abstract List<AiBlock> getBlocks();

	/** 
	 * Renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les bombes éventuellement contenues dans cette case.
	 */
	public abstract List<AiBomb> getBombs();

	/** 
	 * Renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les feux éventuellement contenus dans cette case.
	 */
	public abstract List<AiFire> getFires();

	/** 
	 * Renvoie les sols de cette case 
	 * (il y a forcément au moins un sol).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les sols contenus dans cette case.
	 */
	public abstract List<AiFloor> getFloors();

	/** 
	 * Renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les personnages éventuellement contenus dans cette case.
	 */
	public abstract List<AiHero> getHeroes();

	/** 
	 * Renvoie la liste des items contenus dans cette case 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Les items éventuellement contenus dans cette case.
	 */
	public abstract List<AiItem> getItems();
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * cette case. Sinon, cela signifie qu'elle contient au moins un
	 * obstacle que le personnage ne peut pas traverser. 
	 * Tous les sprites ne sont pas sensibles aux mêmes obstacles,
	 * cela dépend à la fois du type des sprites considérés (Hero,
	 * Bomb, Item, Block, etc) et des pouvoirs courants (passer à travers
	 * les murs, passer à travers les bombes, etc). Le feu peut constituer
	 * un obstacle, notamment pour les sprite de type Hero.
	 * <br/>
	 * cf. les méthodes de même nom dans les classes filles de {@link AiSprite}.
	 * 
	 *  @param sprite
	 *  	Le sprite qui veut traverser cette case.
	 *  @param ignoreBlocks
	 *  	Si vrai, la fonction ne considère pas les blocks comme des obstacles.
	 *  @param ignoreBombs
	 *  	Si vrai, la fonction ne considère pas les bombes comme des obstacles.
	 *  @param ignoreFires
	 *  	Si vrai, la fonction ne considère pas le feu comme un obstacle.
	 *  @param ignoreFloors
	 *  	Si vrai, la fonction ne considère pas les sols comme des obstacles 
	 *  	(ce qu'ils sont rarement, de toute façon).
	 *  @param ignoreHeroes
	 *  	Si vrai, la fonction ne considère pas les personnages comme des obstacles.
	 *  @param ignoreItems
	 *  	Si vrai, la fonction ne considère pas les items comme des obstacles.
	 *  @return	
	 *  	Renvoie {@code true} ssi ce sprite, à cet instant, peut traverser cette case.
	 */
	public abstract boolean isCrossableBy(AiSprite sprite, 
			boolean ignoreBlocks, boolean ignoreBombs, boolean ignoreFires, boolean ignoreFloors, boolean ignoreHeroes, boolean ignoreItems);
	
	/**
	 * Comme {@link #isCrossableBy(AiSprite,boolean,boolean,boolean,boolean,boolean,boolean) isCrossableBy}, 
	 * mais considère tous les obstacles.
	 * 
	 *  @param sprite
	 *  	Le sprite qui veut traverser cette case.
	 *  @return	
	 *  	Renvoie {@code true} ssi ce sprite , à cet instant, peut traverser cette case.
	 */
	public abstract boolean isCrossableBy(AiSprite sprite);
	
	/////////////////////////////////////////////////////////////////
	// NEIGHBORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le voisin de cette case passée en paramètre, situé dans la direction
	 * passée en paramètre.
	 * <br/> 
	 * <b>ATTENTION :</b> seulement les directions primaires sont
	 * utilisées ({@link Direction#UP UP}, {@link Direction#RIGHT RIGHT}, {@link Direction#DOWN DOWN}, 
	 * {@link Direction#LEFT LET}) : pas de direction composite ({@link Direction#UPLEFT UPLEFT}, etc.).
	 * Dans le cas contraire, la fonction lève une {@link IllegalArgumentException}.
	 * <br/>
	 * <b>ATTENTION :</b> les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (row,0), le voisin de gauche est la case située à la position
	 * (row,width-1). même chose pour les bordures haut et bas.
	 * 
	 * @param direction
	 * 		Direction dans laquelle le voisin se trouve.
	 * @return	
	 * 		Le voisin de cette case, situé dans la direction indiquée.
	 *  
	 * @throws
	 * 		IllegalArgumentException ssi la direction est composite. 
	 */
	public abstract AiTile getNeighbor(Direction direction);
	
	/**
	 * Renvoie la liste des voisins de cette case.
	 * Il s'agit des voisins directs situés en haut, 
	 * à gauche, en bas et à droite.
	 * <br/>
	 * <b>Note :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * <br/>
	 * <b>ATTENTION :</b>les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (row,0), le voisin de gauche est la case située à la position
	 * (row,width-1). même chose pour les bordures haut et bas.
	 * <br/>
	 * <b>ATTENTION :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		La liste des voisins situés en haut, à gauche, en bas 
	 * 		et à droite de la case passée en paramètre.
	 */
	public abstract List<AiTile> getNeighbors();

	/////////////////////////////////////////////////////////////////
	// COMPARISONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiTile)
		{	
			AiTile t = (AiTile)o;	
			int row = t.getRow();
			int col = t.getCol();
			result = row==this.row && col==this.col;
		}
		return result;
	}
	
	@Override
    public int hashCode()
    {	AiZone zone = getZone();
		int height = zone.getHeight();
		int result = col + height*row;
    	return result;
    }

	@Override
    public int compareTo(AiTile tile)
    {	int result = row - tile.getRow();
    	if(result==0)
    		result = col - tile.getCol();
    	return result;
    }

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+row+";"+col+" - "+posX+";"+posY+")");
		return result.toString();
	}
}
