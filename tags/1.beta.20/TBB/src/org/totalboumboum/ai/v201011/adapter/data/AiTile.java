package org.totalboumboum.ai.v201011.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * repr�sente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AiTile
{	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract AiZone getZone();
	
	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ligne de la zone contenant cette case */
	protected int line;
	/** colonne de la zone contenant cette case */
	protected int col;
		
	/** 
	 * renvoie le num�ro de la ligne contenant cette case
	 * 
	 * @return	
	 * 		la ligne de cette case
	 */
	public int getLine()
	{	return line;	
	}

	/** 
	 * renvoie le num�ro de la colonne contenant cette case
	 *  
	 * @return	
	 * 		la colonne de cette case
	 */
	public int getCol()
	{	return col;	
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position de la case en pixels */
	protected double posX;
	/** position de la case en pixels */
	protected double posY;
	
	/** 
	 * renvoie l'abscisse de la case en pixels
	 * 
	 * @return	
	 * 		l'abscisse de cette case
	 */
	public double getPosX()
	{	return posX;	
	}
	
	/** 
	 * renvoie l'ordonn�e de la case en pixels
	 * 
	 * @return	
	 * 		l'ordonn�e de cette case
	 */
	public double getPosY()
	{	return posY;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** longueur d'un c�t� de la case en pixels */
	protected double size;
		
	/** 
	 * renvoie la taille de la case en pixels
	 * 
	 * @return	
	 * 		longueur d'un c�t� de la case en pixels
	 */
	public double getSize()
	{	return size;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les blocks �ventuellement contenus dans cette case
	 */
	public abstract List<AiBlock> getBlocks();

	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les bombes �ventuellement contenues dans cette case
	 */
	public abstract List<AiBomb> getBombs();

	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les feux �ventuellement contenus dans cette case
	 */
	public abstract List<AiFire> getFires();

	/** 
	 * renvoie les sols de cette case 
	 * (il y a forc�ment au moins un sol)
	 * 
	 * @return	
	 * 		les sols contenus dans cette case
	 */
	public abstract List<AiFloor> getFloors();

	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les personnages �ventuellement contenus dans cette case
	 */
	public abstract List<AiHero> getHeroes();

	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les items �ventuellement contenus dans cette case
	 */
	public abstract List<AiItem> getItems();
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite pass� en param�tre est capable de traverser
	 * cette case. Sinon, cela signifie qu'elle contient au moins un
	 * obstacle que le personnage ne peut pas traverser. 
	 * Tous les sprites ne sont pas sensibles aux m�mes obstacles,
	 * cela d�pend � la fois du type des sprites consid�r�s (Hero,
	 * Bomb, Item, Block, etc) et des pouvoirs courants (passer � travers
	 * les murs, passer � travers les bombes, etc). Le feu peut constituer
	 * un obstacle, notamment pour les sprite de type Hero.</br>
	 * cf. les m�thodes de m�me nom dans les classes filles de AiSprite
	 * 
	 *  @param sprite
	 *  	le sprite qui veut traverser cette case
	 *  @param ignoreBlocks
	 *  	si vrai, la fonction ne consid�re pas les blocks comme des obstacles
	 *  @param ignoreBombs
	 *  	si vrai, la fonction ne consid�re pas les bombes comme des obstacles
	 *  @param ignoreFires
	 *  	si vrai, la fonction ne consid�re pas le feu comme un obstacle
	 *  @param ignoreFloors
	 *  	si vrai, la fonction ne consid�re pas les sols comme des obstacles (ce qu'ils sont rarement, de toute fa�on)
	 *  @param ignoreHeroes
	 *  	si vrai, la fonction ne consid�re pas les personnages comme des obstacles
	 *  @param ignoreItems
	 *  	si vrai, la fonction ne consid�re pas les items comme des obstacles
	 *  @return	
	 *  	vrai ssi ce sprite , � cet instant, peut traverser cette case
	 */
	public abstract boolean isCrossableBy(AiSprite sprite, 
			boolean ignoreBlocks, boolean ignoreBombs, boolean ignoreFires, boolean ignoreFloors, boolean ignoreHeroes, boolean ignoreItems);
	
	/**
	 * Comme isCrossableBy, mais consid�re toujours le feu
	 * 
	 *  @param sprite
	 *  	le sprite qui veut traverser cette case
	 *  @return	
	 *  	vrai ssi ce sprite , � cet instant, peut traverser cette case
	 */
	public abstract boolean isCrossableBy(AiSprite sprite);
	
	/////////////////////////////////////////////////////////////////
	// NEIGHBORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie le voisin de cette case pass�e en param�tre, situ� dans la direction
	 * pass�e en param�tre. 
	 * <b>ATTENTION :</b> seulement les directions primaires sont
	 * utilis�es (UP, RIGHT, DOWN, LEFT) : pas de direction composite (UPLEFT, etc.).
	 * Dans le cas contraire, la fonction renvoie null.</br>
	 * <b>ATTENTION :</b> les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @param direction
	 * 		direction dans laquelle le voisin se trouve
	 * @return	
	 * 		le voisin de cette case, situ� dans la direction indiqu�e (ou null si la direction n'est pas primaire)
	 */
	public abstract AiTile getNeighbor(Direction direction);
	
	/**
	 * renvoie la liste des voisins de cette case.
	 * Il s'agit des voisins directs situ�s en haut, � gauche, en bas et � droite.</br>
	 * 
	 * <b>ATTENTION :</b>les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @return	
	 * 		la liste des voisins situ�s en haut, � gauche, en bas et � droite de la case pass�e en param�tre
	 */
	public abstract List<AiTile> getNeighbors();

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiTile)
		{	
			AiTile t = (AiTile)o;	
			int line = t.getLine();
			int col = t.getCol();
			result = line==this.line && col==this.col;
		}
		return result;
	}
	
	@Override
    public int hashCode()
    {	AiZone zone = getZone();
		int height = zone.getHeight();
		int result = col + height*line;
    	return result;
    }
	
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+line+";"+col+")");
		return result.toString();
	}
}