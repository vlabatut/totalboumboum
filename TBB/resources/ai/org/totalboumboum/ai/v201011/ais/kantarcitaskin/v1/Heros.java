package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings({ "unused", "deprecation" })
public class Heros 
{
	private AiZone zone; // La zone de jeu de cette instance
	private final int HEROES =-10;
	
	
	public void fillMatrix(int [][] matrix) throws StopRequestException
	{
		
		Collection<AiHero> hero = zone.getHeroes();
		Iterator<AiHero> iteratorhero = hero.iterator();
		while (iteratorhero.hasNext()) 
		{
			AiHero heroes = iteratorhero.next();
			matrix[heroes.getLine()][heroes.getCol()] = HEROES;
			
		}
	}

}
