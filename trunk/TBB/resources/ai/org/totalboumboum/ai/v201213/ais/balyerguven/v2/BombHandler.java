package org.totalboumboum.ai.v201213.ais.balyerguven.v2;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
/**
 * 
 * @author BalyerGuven
 *
 */

public class BombHandler extends AiBombHandler<BalyerGuven>
{	
 
	
	/**
	 * 
	 */
	public AiTile startTile=ai.getZone().getOwnHero().getTile();
	/** */
	protected AiZone zone;
	/** */
	protected AiHero myHero;
	/** */
	protected AiTile ownTile;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	/**
	 * @param ai 
	 * @throws StopRequestException  */


	protected BombHandler(BalyerGuven ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
    	
	}

	
	/////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * @param tile 
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean canRun(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		zone = ai.getZone();
		myHero=zone.getOwnHero();
		AiFire fire = myHero.getBombPrototype().getFirePrototype();
		int range = myHero.getBombRange();
		List<AiTile> posibleTile = clearDuplicate(ai.utilityHandler.posibleTiles);

		for(Direction d: Direction.getPrimaryValues())
		{	ai.checkInterruption();
			AiTile neighbor = tile;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		for(int i = 0 ; i<posibleTile.size();i++){
			ai.checkInterruption();
			if(result.contains(posibleTile.get(i)) || tile.equals(posibleTile.get(i))){
				posibleTile.remove(i);
				i--;
			}
		}
		
		
		if(posibleTile.size()>0){
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param tiles
	 * @return List tiles
	 * @throws StopRequestException
	 */
	private  List<AiTile> clearDuplicate(List<AiTile> tiles)throws StopRequestException {
		ai.checkInterruption();
		for(int i = 0 ; i<tiles.size();i++){
			ai.checkInterruption();
			AiTile temp = tiles.get(i);
			for(int j = i ; j< tiles.size() ; j++){
				ai.checkInterruption();
				if(temp.equals(tiles.get(j))){
					tiles.remove(j);
					j--;
				}
			}
		}
		return tiles;
	}
	

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
	
	ai.checkInterruption();
	boolean result=false;
	zone = ai.getZone();
	myHero=zone.getOwnHero();
	
	if(canRun(myHero.getTile())){
		if(myHero.getBombNumberMax()>0){
			
			if(zone.getTotalTime()>500 && myHero.getBombNumberCurrent()<2){
		
			result=true;
			}
			else{
				result=false;
			}
		}
		
		else{
			
			result=false;
		}
	}
	else{
		
		result=false;
	}
	
	return result;
	}


	


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
