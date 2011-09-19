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

/**
 * Cette classe repr�sente un chemin qu'un agent peut emprunter
 * dans la zone de jeu. Le chemin est d�crit par une s�quence de cases,
 * et un point de d�part exprim� en pixels. Un temps d'attente suppl�mentaire
 * peut �tre associ� à chaque case.<br/>
 * Diverses op�rations sont possibles sur un ou plusieurs chemins : modification,
 * comparaisons, calculs vari�s, etc.
 * 
 * @author Vincent Labatut
 *
 */
public class AiPath implements Comparable<AiPath>
{	
    /////////////////////////////////////////////////////////////////
	// STARTING POINT	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse du point de d�part pr�cis du chemin, exprim� en pixels (il doit �tre contenu dans la première case, bien s�r) */
	private double startX;
	/** ordonn�e du point de d�part pr�cis du chemin, exprim� en pixels (il doit �tre contenu dans la première case, bien s�r) */
	private double startY;
	
	/**
	 * renvoie l'abscisse du point de d�part de chemin,
	 * exprim�e en pixels.
	 * 
	 * @return
	 * 		l'abscisse du point de d�part
	 */
	public double getStartX()
	{	return startX;
	}

	/**
	 * renvoie l'ordonn�e du point de d�part de chemin,
	 * exprim�e en pixels.
	 * 
	 * @return
	 * 		l'ordonn�e du point de d�part
	 */
	public double getStartY()
	{	return startY;
	}

	/**
	 * modifie la position du point de d�part de chemin,
	 * exprim�e en pixels.
	 * 
	 * @param startY
	 * 		la nouvelle ordonn�e du point de d�part
	 * @param startY
	 * 		la nouvelle ordonn�e du point de d�part
	 */
	public void setStart(double startX, double startY)
	{	this.startX = startX;
		this.startY = startY;
	}

	/**
	 * permet de v�rifier que le point de d�part est
	 * bien contenu dans la première case du chemin.
	 * si ce n'est pas le cas, il est corrig� en utilisant
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
	/** liste des pauses associ�es à chaque case (attention : ce temps n'inclut pas la dur�e n�cessaire à la travers�e de la case) */
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
	 * Renvoie la liste de pauses assoc�es aux cases constituant ce chemin.
	 * Les pauses sont exprim�es en ms.
	 * 
	 * @return	
	 * 		la liste de pauses du chemin
	 */
	public List<Long> getPauses()
	{	return pauses;	
	}
	
	/**
	 * Renvoie la case dont la position est pass�e en param�tre
	 *
	 * @param index
	 * 		la position de la case demand�e
	 * @return	
	 * 		la case occupant la position indiqu�e dans ce chemin
	 */
	public AiTile getTile(int index)
	{	return tiles.get(index);	
	}
	
	/**
	 * Renvoie la pause associ�e à la case 
	 * dont la position est pass�e en param�tre.
	 * La pause est exprim�e en ms.
	 *
	 * @param index
	 * 		la position de la case demand�e
	 * @return	
	 * 		la case occupant la position indiqu�e dans ce chemin
	 */
	public Long getPause(int index)
	{	return pauses.get(index);	
	}
	
	/**
	 * Ajoute dans ce chemin la case pass�e en param�tre, 
	 * en l'ins�rant à la fin de la s�quence de cases
	 * 
	 * @param tile
	 * 		la case à ins�rer
	 */
	public void addTile(AiTile tile)
	{	addTile(tile,0);
	}
	
	/**
	 * Ajoute dans ce chemin la case pass�e en param�tre, 
	 * en l'ins�rant à la fin de la s�quence de cases et
	 * en lui associant la pause sp�cifi�e (en ms). 
	 * 
	 * @param tile
	 * 		la case à ins�rer
	 */
	public void addTile(AiTile tile, long pause)
	{	tiles.add(tile);
		if(tiles.size()==1)
			checkStartingPoint();
		pauses.add(pause);
	}
	
	/**
	 * Ajoute dans ce chemin la case pass�e en param�tre, 
	 * en l'ins�rant à la position pass�e en param�tre.
	 * 
	 * @param index
	 * 		position de la case à ins�rer
	 * @param tile
	 * 		la case à ins�rer
	 */
	public void addTile(int index, AiTile tile)
	{	addTile(index,tile,0);
	}
	
	/**
	 * Ajoute dans ce chemin la case pass�e en param�tre, 
	 * en l'ins�rant à la position pass�e en param�tre.
	 * 
	 * @param index
	 * 		position de la case à ins�rer
	 * @param tile
	 * 		la case à ins�rer
	 * @param pause
	 * 		la pause associ�e à la case à ins�rer
	 */
	public void addTile(int index, AiTile tile, long pause)
	{	tiles.add(index,tile);
		if(index==0)
			checkStartingPoint();
		pauses.add(index,pause);
	}
	
	/**
	 * remplace la case dont la position est pass�e en param�tre par
	 * la case pass�e en param�tre, dans ce chemin.
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
	 * Remplace la case dont la position est pass�e en param�tre par
	 * la case pass�e en param�tre, dans ce chemin. La pause
	 * est �galement remplac�e par la pause sp�cifi�e en param�tre
	 * (et exprim�e en ms).
	 * 
	 * @param index
	 * 		position de la case à remplacer
	 * @param tile
	 * 		la nouvelle case
	 * @param pause
	 * 		la nouvelle pause associ�e à cette case.
	 */
	public void setTile(int index, AiTile tile, long pause)
	{	tiles.set(index,tile);
		if(index==0)
			checkStartingPoint();
		pauses.set(index,pause);
	}
	
	/**
	 * Supprime de ce chemin la case dont la position est pass�e en param�tre
	 * (supprime �galement l'�ventuelle pause associ�e).
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
	 * Supprime de ce chemin la case pass�e en param�tre,
	 * ainsi que (�ventuellement) la pause qui lui �tait
	 * associ�e.
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
	 * Renvoie la pause associ�e à la dernière case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		La pause associ�e à la dernière case du chemin ou {@code null} en cas d'erreur.
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
	 * Renvoie la pause associ�e à la première case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin.
	 * 
	 * @return	
	 * 		La pause associ�e à la première case du chemin ou {@code null} en cas d'erreur.
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
	 * Renvoie la distance de Manhattan, exprim�e en cases, correspondant à ce chemin.
	 * Si la même case apparait consécutivement plusieurs fois, elle n'est compt�e
	 * qu'une seule. Le calcul ne tient pas compte des �ventuels obstacles.
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
	 * Renvoie la distance de Manhattan, exprim�e en pixels, correspondant à ce chemin.
	 * On utilise le point de d�part pour d�marrer le calcul, donc pas n�cessairement
	 * le centre de la première case. Le point d'arriv�e est forc�ment un point à la p�riph�rie 
	 * de la dernière case. Le calcul ne tient pas compte des �ventuels obstacles.
	 * 
	 * @return	
	 * 		Un r�el correspondant à la distance totale du chemin en pixels.
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
				double centerX = tile.getPosX();
				double centerY = tile.getPosY();
				double dist = zone.getPixelDistance(x1,y1,x2,y2);
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
	 * calcule le temps approximatif n�cessaire au personnage pass� en param�tre
	 * pour parcourir ce chemin. Le temps est exprim� en millisecondes, et 
	 * on suppose qu'il n'y a pas d'obstacle sur le chemin et que la vitesse
	 * de déplacement du joueur est constante. C'est donc une estimation du temps
	 * qui sera r�ellement n�cessaire au joueur, puisque diff�rents facteurs peuvent
	 * venir invalider ces hypoth�ses.
	 *   
	 * @param hero
	 * 		le personnage qui parcourt le chemin
	 * @return	
	 * 		le temps n�cessaire au personnage pour parcourir ce chemin
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
	 * Compare ce chemin à celui pass� en param�tre, 
	 * et renvoie vrai s'ils sont parfaitement identiques,
	 * i.e. sont constitu�s de la même s�quence de cases.
	 * <b>Remarque :</b> on ne consid�re donc pas le point de d�part 
	 * 
	 * @param object
	 * 		le chemin à comparer
	 * @return	
	 * 		vrai ssi les 2 ce chemin est identique à celui pass� en param�tre
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
