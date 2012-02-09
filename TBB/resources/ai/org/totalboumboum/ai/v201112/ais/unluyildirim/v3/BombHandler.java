package org.totalboumboum.ai.v201112.ais.unluyildirim.v3;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
/**
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<UnluYildirim>
{	
	
	static int BOMBNUMBER = 0 ;
	static int TEMPNUMBER = 10 ;
	
	protected BombHandler(UnluYildirim ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();

		verbose = false;
    	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
    AiHero myhero ;
    AiZone zone = ai.getZone();
    myhero = zone.getOwnHero();
	{
		ai.checkInterruption();	
		HashMap<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
		TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
		Iterator<Float> it1 = values.descendingIterator();

		float utility = it1.next();
		List<AiTile> tiles = utilitiesByValue.get(utility);

		
		
		//Si le tile que l'agent arrive ne possede des bonus , ça veut dire que l'agent vient pour la détruire.
		if(tiles.get(0).getItems().isEmpty())
		{
			ai.checkInterruption();	
			
			if(tiles.get(0) == myhero.getTile() && myhero.getBombNumberCurrent()==BOMBNUMBER  && ai.moveHandler.isBombing &&  ai.moveHandler.canRun)
			{
				
				ai.checkInterruption();	
				
				
				//L'agent attend un peu pour poser une autre bomb
			try {
				Thread.sleep(myhero.getBombDuration()/TEMPNUMBER);
			} catch (InterruptedException e) {
				
				//e.printStackTrace();
			}
		
			return true;
			}
		
		}
	}
	try {
		Thread.sleep(myhero.getBombDuration()/10);
	} catch (InterruptedException e) {
		
		e.printStackTrace();
	}        	 
	return false;
		

	}
	
	
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	
	}
}
