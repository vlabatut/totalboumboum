package org.totalboumboum.ai.v201112.adapter.path;

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
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.astarcopy.AstarLocation;

/**
 * Cette classe représente un chemin qu'un agent peut emprunter
 * dans la zone de jeu. Le chemin est décrit par une séquence d'emplacements,
 * représentés par des objets {@link AstarLocation}. Un temps d'attente 
 * supplémentaire peut être associé à chaque étape.<br/>
 * Diverses opérations sont possibles sur un ou plusieurs chemins : modification,
 * comparaisons, calculs variés, etc.
 * <b>Attention :</b> les chemins étaient gérés différemment les années
 * précédentes : il s'agissait simplement de suites de cases, et non pas
 * de suites d'emplacements. De plus, les temps de pause n'étaient pas considérés.
 * Par conséquent, l'utilisation des chemins dans cette API est légèrement 
 * différente de ce qui se faisait les années précédentes.
 * 
 * @author Vincent Labatut
 */
public class AiPath implements Comparable<AiPath>
{	
	/////////////////////////////////////////////////////////////////
	// LOCATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des emplacements composant le chemin */
	private final List<AstarLocation> locations = new ArrayList<AstarLocation>();
	/** liste des pauses associées à chaque emplacement (attention : ce temps n'inclut pas le temps de déplacement) */
	private final List<Long> pauses = new ArrayList<Long>();
	
	/**
	 * Renvoie la liste de emplacements constituant ce chemin
	 * 
	 * @return	
	 * 		la liste de emplacements du chemin
	 */
	public List<AstarLocation> getLocations()
	{	return locations;	
	}
	
	/**
	 * Renvoie la liste de pauses associées aux emplacement constituant ce chemin.
	 * Les pauses sont exprimées en ms.
	 * 
	 * @return	
	 * 		la liste de pauses du chemin
	 */
	public List<Long> getPauses()
	{	return pauses;	
	}
	
	/**
	 * Renvoie l'emplacement dont l'index est passé en paramètre
	 *
	 * @param index
	 * 		La position de l'emplacement demandé dans le chemin.
	 * @return	
	 * 		L'emplacement occupant la position indiquée dans ce chemin
	 */
	public AstarLocation getTile(int index)
	{	return locations.get(index);	
	}
	
	/**
	 * Renvoie la pause associée à l'emplacement 
	 * dont la position est passée en paramètre.
	 * La pause est exprimée en ms.
	 *
	 * @param index
	 * 		la position de l'emplacement demandée
	 * @return	
	 * 		la case occupant la position indiquée dans ce chemin
	 */
	public Long getPause(int index)
	{	return pauses.get(index);	
	}
	
	/**
	 * Ajoute dans ce chemin l'emplacement passé en paramètre, 
	 * en l'insérant à la fin de la séquence d'emplacements.
	 * 
	 * @param location
	 * 		l'emplacement à insérer
	 */
	public void addTile(AstarLocation location)
	{	addTile(location,0);
	}
	
	/**
	 * Ajoute dans ce chemin la case passée en paramètre, 
	 * en l'insérant à la fin de la séquence de cases et
	 * en lui associant la pause spécifiée (en ms). 
	 * 
	 * @param tile
	 * 		la case à insérer
	 */
	public void addTile(AstarLocation tile, long pause)
	{	locations.add(tile);
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
	public void addTile(int index, AstarLocation tile)
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
	public void addTile(int index, AstarLocation tile, long pause)
	{	locations.add(index,tile);
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
	public void setTile(int index, AstarLocation tile)
	{	locations.set(index,tile);
	}
	
	/**
	 * Remplace la case dont la position est passée en paramètre par
	 * la case passée en paramètre, dans ce chemin. La pause
	 * est également remplacée par la pause spécifiée en paramètre
	 * (et exprimée en ms).
	 * 
	 * @param index
	 * 		position de la case à remplacer
	 * @param tile
	 * 		la nouvelle case
	 * @param pause
	 * 		la nouvelle pause associée à cette case.
	 */
	public void setTile(int index, AstarLocation tile, long pause)
	{	locations.set(index,tile);
		pauses.set(index,pause);
	}
	
	/**
	 * Remplace la pause de la case dont la position est passée en paramètre par
	 * la durée passée en paramètre, dans ce chemin.
	 * 
	 * @param index
	 * 		position de la case dont la pause est à remplacer
	 * @param pause
	 * 		la nouvelle pause
	 */
	public void setPause(int index, long pause)
	{	pauses.set(index,pause);
	}
	
	/**
	 * Supprime de ce chemin la case dont la position est passée en paramètre
	 * (supprime également l'éventuelle pause associée).
	 * 
	 * @param index
	 * 		position de la case à supprimer
	 */
	public void removeTile(int index)
	{	locations.remove(index);
		pauses.remove(index);
	}
	
	/**
	 * Supprime de ce chemin la case passée en paramètre,
	 * ainsi que (éventuellement) la pause qui lui était
	 * associée.
	 * 
	 * @param tile
	 * 		la case à supprimer
	 */
	public void removeTile(AstarLocation tile)
	{	int index = locations.indexOf(tile);
		locations.remove(index);
		pauses.remove(index);
	}
	
	/**
	 * Renvoie la longueur (en cases) de ce chemin.<br/>
	 * <b>Attention :</b> si le chemin contient plusieurs
	 * fois la même case, elle sera comptée autant de fois.
	 * 
	 * @return	
	 * 		La longueur de ce chemin, en cases.
	 */
	public int getLength()
	{	return locations.size();
	}
	
	/**
	 * Teste si ce chemin a une longueur non-nulle.
	 * 
	 * @return	
	 * 		Renvoie {@code true} ssi le chemin ne contient aucune case.
	 */
	public boolean isEmpty()
	{	return locations.size()==0;
	}
	
	/**
	 * Renvoie la dernière case du chemin,
	 * ou {@code null} s'il n'y a pas de case dans ce chemin
	 * 
	 * @return	
	 * 		La dernière case du chemin ou {@code null} en cas d'erreur.
	 */
	public AstarLocation getLastTile()
	{	AstarLocation result = null;
		if(!locations.isEmpty())
			result = locations.get(locations.size()-1);
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
	public AstarLocation getFirstTile()
	{	AstarLocation result = null;
		if(!locations.isEmpty())
			result = locations.get(0);
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
	 * Si la même case apparait consécutivement plusieurs fois, elle n'est comptée
	 * qu'une seule. Le calcul ne tient pas compte des éventuels obstacles.
	 * 
	 * @return	
	 * 		Un entier correspondant à la distance totale du chemin en cases.
	 */
	public int getTileDistance()
	{	int result = 0;
		AstarLocation previous = null;
		AiZone zone = null;
		
		for(AstarLocation tile: locations)
		{	if(previous==null)
			{	zone = tile.getTile().getZone();
			}
			else
			{	int dist = zone.getTileDistance(previous,tile);
				result = result + dist;
			}
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
		AstarLocation previous = null;
		AiZone zone = null;

		for(AstarLocation tile: locations)
		{	if(previous==null)
			{	zone = tile.getTile().getZone();
			}
			else
			{	double dist = zone.getPixelDistance(previous,tile);
				result = result + dist;
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
	 * de déplacement du joueur est constante. On tient compte des pauses.
	 * Le résultat est donc une estimation du temps qui sera réellement nécessaire 
	 * au joueur, puisque différents facteurs peuvent venir invalider ces hypothèses.
	 *   
	 * @param hero
	 * 		Le personnage qui parcourt le chemin.
	 * @return	
	 * 		Le temps nécessaire au personnage pour parcourir ce chemin.
	 */
	public long getDuration(AiHero hero)
	{	long result = 0;
		if(locations.size()>1)
		{	// on considère le temps de déplacement
			double speed = hero.getWalkingSpeed();
			double distance = getPixelDistance();
			result = Math.round(distance/speed * 1000);
			// on ajout le temps des pauses
			for(Long pause: pauses)
				result = result + pause;
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
	 * et renvoie vrai s'ils sont constitués de la même séquence de cases
	 *  et de pauses.<br/>
	 * <b>Remarque :</b> on ne considère donc pas le point de départ.
	 * 
	 * @param object
	 * 		Le chemin à comparer.
	 * @return	
	 * 		Renvoie {@code true} ssi ce chemin est identique à celui passé en paramètre.
	 */
	@Override
	public boolean equals(Object object)
	{	boolean result = false;
		if(object instanceof AiPath)
		{	AiPath path = (AiPath)object;
			result = getLength()==path.getLength();
			int i=0;
			while(result && i<locations.size())
			{	AstarLocation loc1 = locations.get(i);
				AstarLocation loc2 = path.getTile(i);
				long pause1 = pauses.get(i);
				long pause2 = path.getPause(i);
				result = loc1.equals(loc2) && pause1==pause2;
				i++;
			}
		}		
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "{";
		for(int i=0;i<locations.size();i++)
		{	AstarLocation tile = locations.get(i);
			long pause = pauses.get(i);
			result = result + " ["+tile+";"+pause+"]";
		}
		result = result + " }";
		return result;
	}
}
