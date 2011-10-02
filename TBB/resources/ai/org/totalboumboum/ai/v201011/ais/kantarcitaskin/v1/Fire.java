package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings({ "unused", "deprecation" })
public class Fire
{
	private AiZone zone; // La zone de jeu de cette instance
	private final int FIRE = -100;
  
	
	public void fillMatrix(int [][] matrix) throws StopRequestException
	{
		
		Collection<AiFire> fire = zone.getFires();
		Iterator<AiFire> iteratorFire = fire.iterator();
		while (iteratorFire.hasNext()) 
		{
			AiFire fires = iteratorFire.next();
			matrix[fires.getLine()][fires.getCol()] = FIRE;
			
		}
	}

}
