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
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * Les sprites sont forcément des blocs, des items ou
 * des bombes (pas de feu ni de personnages).
 * 
 * @author Vincent Labatut
 */
public abstract class AiSuddenDeathEvent implements Comparable<AiSuddenDeathEvent>, Serializable
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Instant associé à cet évènement */
	protected long time;
	
	/**
	 * Renvoie l'instant associé à cet évènement,
	 * i.e. le moment auquel l'évènement va se produire
	 * au cours de la partie, exprimé par rapport au 
	 * début de la partie.
	 * 
	 * @return
	 * 		Instant exprimé en ms.
	 */
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la liste de cases concernées par
	 * cet évènement de mort subite.
	 * <br/>
	 * <b>Note :</b> la liste est générée à la demande,
	 * elle peut donc être modifiée par l'agent.
	 * 
	 * @return
	 * 		Une liste de cases.
	 */
	public List<AiTile> getTiles()
	{	List<AiTile> result = new ArrayList<AiTile>();
		for(AiSprite sprite: getSprites())
		{	AiTile tile = sprite.getTile();
			if(!result.contains(tile))
				result.add(tile);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste de sprites apparaissant
	 * à l'instant associé à cet évènement. On
	 * peut récupérer la position des sprites simplement
	 * en accédant à leur case.
	 * <br/>
	 * <b>Note :</b> la list renvoyées n'est pas modifiable.
	 * Toute tentative de modification provoquera une 
	 * {@link UnsupportedOperationException}. 
	 * 
	 * @return	
	 * 		Liste des sprites de cet évènement de mort subite.
	 */
	public abstract List<AiSprite> getSprites();

	/**
	 * Renvoie la liste des sprites contenus dans
	 * cet évènement et qui vont apparaitre dans la
	 * case passée en paramètre. La liste retournée en
	 * résultat peut être vide si aucun sprite n'apparait
	 * dans cette case lors de cet évènement.
	 * <br/>
	 * <b>Note :</b> la liste est générée à la demande,
	 * elle peut donc être modifiée par l'agent.
	 * 
	 * @param tile
	 * 		La case sur laquelle la recherche se concentre.
	 * @return
	 * 		La liste des sprites qui vont apparaître dans cette case.
	 */
	public List<AiSprite> getSpritesForTile(AiTile tile)
	{	List<AiSprite> result = new ArrayList<AiSprite>();
		List<AiSprite> sprites = getSprites();
		for(AiSprite sprite: sprites)
		{	if(sprite.getTile().equals(tile))
				result.add(sprite);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARABLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int compareTo(AiSuddenDeathEvent o)
	{	long time2 = o.getTime();
		int result = (int)Math.signum(time - time2);
		return result;
	}

	@Override
	public int hashCode()
	{	Long temp = new Long(time);
		return temp.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{	boolean result = false;
		
		if(obj!=null)
		{	if(obj instanceof AiSuddenDeathEvent)
			{	AiSuddenDeathEvent se = (AiSuddenDeathEvent) obj;
				result = time==se.getTime();
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append(time + ":");
		for(AiSprite sprite: getSprites())
			result.append(" " + sprite);
		return result.toString();
	}
}
