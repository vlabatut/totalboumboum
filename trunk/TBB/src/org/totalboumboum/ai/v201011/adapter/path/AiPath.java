package org.totalboumboum.ai.v201011.adapter.path;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AiPath
{	
    /////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<AiTile> tiles = new ArrayList<AiTile>();
	
	/**
	 * renvoie la liste de cases constituant ce chemin
	 * 
	 * @return	
	 * 		la liste de cases du chemin
	 */
	public List<AiTile> getTiles()
	{	return tiles;	
	}
	
	/**
	 * renvoie la case dont la position est passée en paramètre
	 *
	 * @param index
	 * 		la position de la case demandée
	 * @return	
	 * 		la case occupant la position indiquée dans ce chemin
	 */
	public AiTile getTile(int index)
	{	return tiles.get(index);	
	}
	
	/**
	 * ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la fin de la séquence de cases
	 * 
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(AiTile tile)
	{	tiles.add(tile);		
	}
	
	/**
	 * ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la position passée en paramètre.
	 * 
	 * @param index
	 * 		position de la case à insérer
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(int index, AiTile tile)
	{	tiles.add(index,tile);	
	}
	
	/**
	 * remplace la case dont la position est passée en paramètre par
	 * la case passée en paramètre, dans ce chemin.
	 * 
	 * @param index
	 * 		position de la case à remplacer
	 * @param tile
	 * 		la nouvelle case
	 */
	public void setTile(int index, AiTile tile)
	{	tiles.set(index,tile);	
	}
	
	/**
	 * supprime de ce chemin la case dont la position est passée en paramètre
	 * 
	 * @param index
	 * 		position de la case à supprimer
	 */
	public void removeTile(int index)
	{	tiles.remove(index);	
	}
	
	/**
	 * supprime de ce chemin la case passée en paramètre
	 * 
	 * @param tile
	 * 		la case à supprimer
	 */
	public void removeTile(AiTile tile)
	{	tiles.remove(tile);		
	}
	
	/**
	 * renvoie la longueur (en cases) de ce chemin
	 * 
	 * @return	
	 * 		la longueur de ce chemin
	 */
	public int getLength()
	{	return tiles.size();	
	}
	
	/**
	 * teste si ce chemin a une longueur non-nulle
	 * 
	 * @return	
	 * 		vrai ssi le chemin ne contient aucune case
	 */
	public boolean isEmpty()
	{	return tiles.size()==0;
	}
	
	/**
	 * renvoie la dernière case du chemin,
	 * ou null s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		la dernière case du chemin ou null en cas d'erreur
	 */
	public AiTile getLastTile()
	{	AiTile result = null;
		if(!tiles.isEmpty())
			result = tiles.get(tiles.size()-1);
		return result;
	}
	
	/**
	 * renvoie la première case du chemin,
	 * ou null s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		la première case du chemin ou null en cas d'erreur
	 */
	public AiTile getFirstTile()
	{	AiTile result = null;
		if(!tiles.isEmpty())
			result = tiles.get(0);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// DISTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie la distance de Manhattan, exprimée en cases, correspondant à ce chemin
	 * 
	 * @return	
	 * 		un entier correspondant à la distance totale du chemin en cases
	 */
	public int getTileDistance()
	{	int result = 0;
		if(tiles.size()>1)
			result = tiles.size()-1;
		return result;	
	}

	/**
	 * renvoie la distance de Manhattan, exprimée en pixels, correspondant à ce chemin
	 * 
	 * @return	
	 * 		un réel correspondant à la distance totale du chemin en pixels
	 */
	public double getPixelDistance()
	{	double result = 0;
		Iterator<AiTile> it = tiles.iterator();
		if(it.hasNext())
		{	AiTile tile = it.next();
			AiZone zone = tile.getZone();
			while(it.hasNext())
			{	AiTile temp = it.next();
				double x1 = tile.getPosX();
				double y1 = tile.getPosY();
				double x2 = temp.getPosX();
				double y2 = temp.getPosY();
				double dist = zone.getPixelDistance(x1,y1,x2,y2);
				result = result + dist;
				tile = temp;
			}
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * calcule le temps approximatif nécessaire au personnage passé en paramètre
	 * pour parcourir ce chemin. Le temps est exprimé en millisecondes, et 
	 * on suppose qu'il n'y a pas d'obstacle sur le chemin et que la vitesse
	 * de déplacement du joueur est constante. C'est donc une estimation du temps
	 * qui sera réellement nécessaire au joueur, puisque différents facteurs peuvent
	 * venir invalider ces hypothèses.
	 *   
	 * @param hero
	 * 		le personnage qui parcourt le chemin
	 * @return	
	 * 		le temps nécessaire au personnage pour parcourir ce chemin
	 */
	public long getDuration(AiHero hero)
	{	long result = 0;
		if(tiles.size()>1)
		{	double speed = hero.getWalkingSpeed();
			int tileDist = getTileDistance();
			double tilePixelSize = tiles.get(0).getSize();
			double pixelDist = tileDist*tilePixelSize;
			result = Math.round(pixelDist/speed * 1000);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'il est strictement plus long que ce dernier.
	 * 
	 * @param path
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi ce chemin est plus long que celui passé en paramètre
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
	 * @param path
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi ce chemin est plus court que celui passé en paramètre
	 */
	public boolean isShorterThan(AiPath path)
	{	int l1 = tiles.size();
		int l2 = path.getLength();
		boolean result = l1<l2;
		return result;		
	}

	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'ils sont parfaitement identiques,
	 * i.e. sont constitués de la même séquence de cases.
	 * 
	 * @param object
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi les 2 ce chemin est identique à celui passé en paramètre
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
				result = t1.equals(t2);
			}
			if(it1.hasNext() || it2.hasNext())
				result = false;
		}		
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "[";
		for(AiTile tile: tiles)
			result = result + " ("+tile.getLine()+","+tile.getCol()+")";
		result = result + " ]";
		return result;
	}
}
