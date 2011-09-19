package org.totalboumboum.ai.v201112.adapter.pathfinding.path;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe représente un chemin qu'un agent peut emprunter
 * dans la zone de jeu. Le chemin est décrit par une séquence de cases,
 * et un point de départ exprimé en pixels. Un temps d'attente supplémentaire
 * peut être associé à chaque case.<br/>
 * Diverses opérations sont possibles sur un ou plusieurs chemins : modification,
 * comparaisons, calculs variés, etc.
 * 
 * @author Vincent Labatut
 *
 */
public class AiPath implements Comparable<AiPath>
{	
    /////////////////////////////////////////////////////////////////
	// STARTING POINT	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse du point de départ pr�cis du chemin, exprimé en pixels (il doit être contenu dans la première case, bien s�r) */
	private double startX;
	/** ordonnée du point de départ pr�cis du chemin, exprimé en pixels (il doit être contenu dans la première case, bien s�r) */
	private double startY;
	
	/**
	 * renvoie l'abscisse du point de départ de chemin,
	 * exprimée en pixels.
	 * 
	 * @return
	 * 		l'abscisse du point de départ
	 */
	public double getStartX()
	{	return startX;
	}

	/**
	 * renvoie l'ordonnée du point de départ de chemin,
	 * exprimée en pixels.
	 * 
	 * @return
	 * 		l'ordonnée du point de départ
	 */
	public double getStartY()
	{	return startY;
	}

	/**
	 * modifie la position du point de départ de chemin,
	 * exprimée en pixels.
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
	 * permet de v�rifier que le point de départ est
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
	/** liste des pauses associées à chaque case (attention : ce temps n'inclut pas la dur�e nécessaire à la travers�e de la case) */
	private final List<Long> pauses = new ArrayList<Long>();
	
	/**
	 * Renvoie la liste de cases constituant ce chemin
	 * 
	 * @return	
	 * 		la liste de cases du chemin
	 */
	public List<AiTile> getTiles()
	{	return tiles;	
	}
	
	/**
	 * Renvoie la liste de pauses associées aux cases constituant ce chemin.
	 * Les pauses sont exprimées en ms.
	 * 
	 * @return	
	 * 		la liste de pauses du chemin
	 */
	public List<Long> getPauses()
	{	return pauses;	
	}
	
	/**
	 * Renvoie la case dont la position est passée en paramètre
	 *
	 * @param index
	 * 		la position de la case demandée
	 * @return	
	 * 		la case occupant la position indiqu�e dans ce chemin
	 */
	public AiTile getTile(int index)
	{	return tiles.get(index);	
	}
	
	/**
	 * Renvoie la pause associée à la case 
	 * dont la position est passée en paramètre.
	 * La pause est exprimée en ms.
	 *
	 * @param index
	 * 		la position de la case demandée
	 * @return	
	 * 		la case occupant la position indiqu�e dans ce chemin
	 */
	public Long getPause(int index)
	{	return pauses.get(index);	
	}
	
	/**
	 * Ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la fin de la séquence de cases
	 * 
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(AiTile tile)
	{	addTile(tile,0);
	}
	
	/**
	 * Ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la fin de la séquence de cases et
	 * en lui associant la pause spécifiée (en ms). 
	 * 
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(AiTile tile, long pause)
	{	tiles.add(tile);
		if(tiles.size()==1)
			checkStartingPoint();
		pauses.add(pause);
	}
	
	/**
	 * Ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la position passée en paramètre.
	 * 
	 * @param index
	 * 		position de la case à insérer
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(int index, AiTile tile)
	{	addTile(index,tile,0);
	}
	
	/**
	 * Ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la position passée en paramètre.
	 * 
	 * @param index
	 * 		position de la case à insérer
	 * @param tile
	 * 		la case à insérer
	 * @param pause
	 * 		la pause associée à la case à insérer
	 */
	public void addTile(int index, AiTile tile, long pause)
	{	tiles.add(index,tile);
		if(index==0)
			checkStartingPoint();
		pauses.add(index,pause);
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
	 * Remplace la case dont la position est passée en paramètre par
	 * la case passée en paramètre, dans ce chemin. La pause
	 * est �galement remplacée par la pause spécifiée en paramètre
	 * (et exprimée en ms).
	 * 
	 * @param index
	 * 		position de la case à remplacer
	 * @param tile
	 * 		la nouvelle case
	 * @param pause
	 * 		la nouvelle pause associée à cette case.
	 */
	public void setTile(int index, AiTile tile, long pause)
	{	tiles.set(index,tile);
		if(index==0)
			checkStartingPoint();
		pauses.set(index,pause);
	}
	
	/**
	 * Supprime de ce chemin la case dont la position est passée en paramètre
	 * (supprime �galement l'éventuelle pause associée).
	 * 
	 * @param index
	 * 		position de la case à supprimer
	 */
	public void removeTile(int index)
	{	tiles.remove(index);
		if(index==0)
			checkStartingPoint();
		pauses.remove(index);
	}
	
	/**
	 * Supprime de ce chemin la case passée en paramètre,
	 * ainsi que (éventuellement) la pause qui lui �tait
	 * associée.
	 * 
	 * @param tile
	 * 		la case à supprimer
	 */
	public void removeTile(AiTile tile)
	{	int index = tiles.indexOf(tile);
		tiles.remove(index);
		if(index==0)
			checkStartingPoint();
		pauses.remove(index);
	}
	
	/**
	 * Renvoie la longueur (en cases) de ce chemin.<br/>
	 * <b>Attention :</b> si le chemin contient plusieurs
	 * fois la même case, elle sera compt�e autant de fois.
	 * 
	 * @return	
	 * 		La longueur de ce chemin, en cases.
	 */
	public int getLength()
	{	return tiles.size();
	}
	
	/**
	 * Teste si ce chemin a une longueur non-nulle.
	 * 
	 * @return	
	 * 		Renvoie {@code true} ssi le chemin ne contient aucune case.
	 */
	public boolean isEmpty()
	{	return tiles.size()==0;
	}
	
	/**
	 * Renvoie la dernière case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		La dernière case du chemin ou {@code null} en cas d'erreur.
	 */
	public AiTile getLastTile()
	{	AiTile result = null;
		if(!tiles.isEmpty())
			result = tiles.get(tiles.size()-1);
		return result;
	}
	
	/**
	 * Renvoie la pause associée à la dernière case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		La pause associée à la dernière case du chemin ou {@code null} en cas d'erreur.
	 */
	public Long getLastPause()
	{	Long result = null;
		if(!pauses.isEmpty())
			result = pauses.get(pauses.size()-1);
		return result;
	}
	
	/**
	 * Renvoie la première case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin.
	 * 
	 * @return	
	 * 		La première case du chemin ou {@code null} en cas d'erreur.
	 */
	public AiTile getFirstTile()
	{	AiTile result = null;
		if(!tiles.isEmpty())
			result = tiles.get(0);
		return result;
	}
	
	/**
	 * Renvoie la pause associée à la première case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin.
	 * 
	 * @return	
	 * 		La pause associée à la première case du chemin ou {@code null} en cas d'erreur.
	 */
	public Long getFirstPause()
	{	Long result = null;
		if(!pauses.isEmpty())
			result = pauses.get(0);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// DISTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan, exprimée en cases, correspondant à ce chemin.
	 * Si la même case apparait consécutivement plusieurs fois, elle n'est compt�e
	 * qu'une seule. Le calcul ne tient pas compte des éventuels obstacles.
	 * 
	 * @return	
	 * 		Un entier correspondant à la distance totale du chemin en cases.
	 */
	public int getTileDistance()
	{	int result = 0;
		AiTile previous = null;
		for(AiTile tile: tiles)
		{	if(previous==null || !tile.equals(previous))
				result++;
			previous = tile;
		}
		return result;	
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, correspondant à ce chemin.
	 * On utilise le point de départ pour démarrer le calcul, donc pas nécessairement
	 * le centre de la première case. Le point d'arrivée est forcément un point à la périphérie 
	 * de la dernière case. Le calcul ne tient pas compte des éventuels obstacles.
	 * On suppose que le personnage passe par le centre de chaque case.
	 * 
	 * @return	
	 * 		Un réel correspondant à la distance totale du chemin en pixels.
	 */
	public double getPixelDistance()
	{	double result = 0;
		AiTile previous = null;
		Double previousX = null;
		Double previousY = null;
		for(AiTile tile: tiles)
		{	if(previous==null)
			{	previousX = startX;
				previousY = startY;
			} 
			else if(!tile.equals(previous))
			{	AiZone zone = tile.getZone();
				Direction direction = zone.getDirection(previous,tile);
				double centerX = tile.getPosX();
				double centerY = tile.getPosY();
				double dist = zone.getPixelDistance(startX,startY,centerX,centerY);
				result = result + dist;
				x1 = x2;
				y1 = y2;
			}
			previous = tile;
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
