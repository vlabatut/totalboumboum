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
 * Cette classe représente un chemin qu'un agent peut emprunter
 * dans la zone de jeu. Le chemin est décrit par une séquence de cases,
 * et un point de départ exprimé en pixels.
 * Diverses opérations sont possibles : modification du chemin,
 * comparaisons, différents calculs, etc.
 * 
 * @author Vincent Labatut
 *
 */
public class AiPath implements Comparable<AiPath>
{	
    /////////////////////////////////////////////////////////////////
	// STARTING POINTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse du point de départ précis du chemin, exprimé en pixel (il doit être contenu dans la première case, bien sûr) */
	private double startX;
	/** ordonnée du point de départ précis du chemin, exprimé en pixel (il doit être contenu dans la première case, bien sûr) */
	private double startY;
	
	/**
	 * renvoie l'abscisse du point de départ de chemin,
	 * exprimée en pixel.
	 * 
	 * @return
	 * 		l'abscisse du point de départ
	 */
	public double getStartX()
	{	return startX;
	}

	/**
	 * renvoie l'ordonnée du point de départ de chemin,
	 * exprimée en pixel.
	 * 
	 * @return
	 * 		l'ordonnée du point de départ
	 */
	public double getStartY()
	{	return startY;
	}

	/**
	 * modifie la position du point de départ de chemin,
	 * exprimée en pixel.
	 * 
	 * @param startY
	 * 		la nouvelle ordonnée du point de départ
	 * @param startY
	 * 		la nouvelle ordonnée du point de départ
	 */
	public void setStart(double startX, double startY)
	{	this.startX = startX;
		this.startY = startY;
	}

	/**
	 * permet de vérifier que le point de départ est
	 * bien contenu dans la première case du chemin.
	 * si ce n'est pas le cas, il est corrigé en utilisant
	 * le centre de la première case du chemin. 
	 */
	public void checkStartingPoint()
	{	if(!tiles.isEmpty())
		{	AiTile firstTile = tiles.get(0);
			if(tiles.size()==1)
			{	startX = firstTile.getPosX();
				startY = firstTile.getPosX();
			}
			else
			{	AiZone zone = firstTile.getZone();
				AiTile currentTile = zone.getTile(startX,startY);
				if(!currentTile.equals(firstTile))
				{	startX = firstTile.getPosX();
					startY = firstTile.getPosX();
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des cases composant le chemin */
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
		if(tiles.size()==1)
			checkStartingPoint();
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
		if(index==0)
			checkStartingPoint();
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
		if(index==0)
			checkStartingPoint();
	}
	
	/**
	 * supprime de ce chemin la case dont la position est passée en paramètre
	 * 
	 * @param index
	 * 		position de la case à supprimer
	 */
	public void removeTile(int index)
	{	tiles.remove(index);
		if(index==0)
			checkStartingPoint();
	}
	
	/**
	 * supprime de ce chemin la case passée en paramètre
	 * 
	 * @param tile
	 * 		la case à supprimer
	 */
	public void removeTile(AiTile tile)
	{	int index = tiles.indexOf(tile);
		tiles.remove(tile);
		if(index==0)
			checkStartingPoint();
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
	 * renvoie la distance de Manhattan, exprimée en pixels, correspondant à ce chemin.
	 * on utilise le point de départ pour démarrer le calcul, donc pas nécessairement
	 * le centre de la première case. par contre, le point d'arrivée est forcément
	 * le centre de la première case.
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
			double x1 = startX;
			double y1 = startY;
			while(it.hasNext())
			{	tile = it.next();
				double x2 = tile.getPosX();
				double y2 = tile.getPosY();
				double dist = zone.getPixelDistance(x1,y1,x2,y2);
				result = result + dist;
				x1 = x2;
				y1 = y2;
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
			double distance = getPixelDistance();
			result = Math.round(distance/speed * 1000);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'il est strictement plus long que ce dernier.
	 * Cette méthode ne doit plus être utilisée, il faut plutot se servir de compareTo.
	 * 
	 * @param path
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi ce chemin est plus long que celui passé en paramètre
	 */
	@Deprecated
	public boolean isLongerThan(AiPath path)
	{	int l1 = tiles.size();
		int l2 = path.getLength();
		boolean result = l1>l2;
		return result;		
	}

	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'il est strictement plus court que ce dernier.
	 * Cette méthode ne doit plus être utilisée, il faut plutot se servir de compareTo.
	 * 
	 * @param path
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi ce chemin est plus court que celui passé en paramètre
	 */
	@Deprecated
	public boolean isShorterThan(AiPath path)
	{	int l1 = tiles.size();
		int l2 = path.getLength();
		boolean result = l1<l2;
		return result;		
	}

	@Override
	public int compareTo(AiPath path)
	{	int result = 0;
		double dist1 = getPixelDistance();
		double dist2 = path.getPixelDistance();
		if(dist1>dist2)
			result = +1;
		else if(dist1<dist2)
			result = -1;
		return result;
	}

	/**
	 * Compare ce chemin à celui passé en paramètre, 
	 * et renvoie vrai s'ils sont parfaitement identiques,
	 * i.e. sont constitués de la même séquence de cases.
	 * <b>Remarque :</b> on ne considère donc pas le point de départ 
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
