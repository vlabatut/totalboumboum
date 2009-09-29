package fr.free.totalboumboum.ai.adapter200910.path;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

public class AiPath
{
	
    /////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<AiTile> tiles = new ArrayList<AiTile>();
	
	/**
	 * renvoie la liste de cases constituant ce chemin
	 * 
	 * @return	la liste de cases du chemin
	 */
	public ArrayList<AiTile> getTiles()
	{	return tiles;	
	}
	
	/**
	 * renvoie la case dont la position est passée en paramètre
	 *
	 * @param index	la position de la case demandée
	 * @return	la case occupant la position indiquée dans ce chemin
	 */
	public AiTile getTile(int index)
	{	return tiles.get(index);	
	}
	
	/**
	 * ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la fin de la séquence de cases
	 * 
	 * @param tile	la case à insérer
	 */
	public void addTile(AiTile tile)
	{	tiles.add(tile);		
	}
	
	/**
	 * ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la position passée en paramètre.
	 * 
	 * @param index	position de la case à insérer
	 * @param tile	la case à insérer
	 */
	public void addTile(int index, AiTile tile)
	{	tiles.add(index,tile);	
	}
	
	/**
	 * remplace la case dont la position est passée en paramètre par
	 * la case passée en paramètre, dans ce chemin.
	 * 
	 * @param index	position de la case à remplacer
	 * @param tile	la nouvelle case
	 */
	public void setTile(int index, AiTile tile)
	{	tiles.set(index,tile);	
	}
	
	/**
	 * supprime de ce chemin la case dont la position est passée en paramètre
	 * 
	 * @param index	position de la case à supprimer
	 */
	public void removeTile(int index)
	{	tiles.remove(index);	
	}
	
	public void removeTile(AiTile tile)
	{	tiles.remove(tile);		
	}
	
	/**
	 * renvoie la longueur (en cases) de ce chemin
	 * 
	 * @return	la longueur de ce chemin
	 */
	public int getLength()
	{	return tiles.size();	
	}
	
	/**
	 * teste si ce chemin a une longueur non-nulle
	 * 
	 * @return	vrai ssi le chemin ne contient aucune case
	 */
	public boolean isEmpty()
	{	return tiles.size()==0;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'il est strictement plus long que ce dernier.
	 * 
	 * @param object	le chemin à comparer
	 * @return	vrai ssi ce chemin est plus long que celui passé en paramètre
	 */
	public boolean isLongerThan(AiPath path)
	{	int l1 = tiles.size();
		int l2 = path.getLength();
		boolean result = l1>l2;
		return result;		
	}

	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'il est strictement plus court que ce dernier.
	 * 
	 * @param object	le chemin à comparer
	 * @return	vrai ssi ce chemin est plus court que celui passé en paramètre
	 */
	public boolean isShortestThan(AiPath path)
	{	int l1 = tiles.size();
		int l2 = path.getLength();
		boolean result = l1<l2;
		return result;		
	}

	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'ils sont parfaitement identiques.
	 * 
	 * @param object	le chemin à comparer
	 * @return	vrai ssi les 2 ce chemin est identique à celui passé en paramètre
	 */
	@Override
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof AiPath)
		{	AiPath path = (AiPath)object;
			result = true;
			Iterator<AiTile> it1 = tiles.iterator();
			Iterator<AiTile> it2 = path.getTiles().iterator();
			while(result && it1.hasNext() && it2.hasNext())
			{	AiTile t1 = it1.next();
				AiTile t2 = it2.next();
				result = t1==t2;
			}
			if(it1.hasNext() || it2.hasNext())
				result = false;
		}		
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "[";
		for(AiTile tile: tiles)
			result = result + " ("+tile.getLine()+","+tile.getCol()+")";
		result = result + " ]";
		return result;
	}
}
