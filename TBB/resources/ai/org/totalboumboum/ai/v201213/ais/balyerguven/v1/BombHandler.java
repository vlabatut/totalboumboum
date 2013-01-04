package org.totalboumboum.ai.v201213.ais.balyerguven.v1;


import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
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
	protected AiHero ownHero;
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
	 * @return Bool
	 * @throws StopRequestException
	 */
	public boolean isDanger() throws StopRequestException
	{	
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero=zone.getOwnHero();
		
		AiTile ownTile = ownHero.getTile();
		
		List<AiBomb> bombs = zone.getBombs();
		
		for(int i = 0 ; i< bombs.size(); i++){
			ai.checkInterruption();
			List<AiTile> scope = bombs.get(i).getBlast();
			if(scope.contains(ownTile)){
				return true;
			}
		}
		return false;
	}
	
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
	
	ai.checkInterruption();
	
	zone = ai.getZone();
	ownHero=zone.getOwnHero();
	AiTile target = ai.getMoveHandler().getCurrentDestination();

	if(ai.getModeHandler().getMode() == AiMode.COLLECTING){
		
		if(ownHero.getBombNumberMax()>0){
			
			if(target.equals(ownHero.getTile()) 
					&& !this.isDanger()
					&& ownHero.getBombNumberCurrent() <2){ 
				
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	//pour le mode attaque
	else{
		
		if(ownHero.getBombNumberMax()>0){
			
				if(target.equals(ownHero.getTile()) 
						&& !this.isDanger()){ 
					return true;
				}
				else{
					return false;
				}
			}
				else{
					return false;
				}
			}
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
