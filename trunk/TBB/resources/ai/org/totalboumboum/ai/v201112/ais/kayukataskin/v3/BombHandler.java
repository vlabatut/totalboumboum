package org.totalboumboum.ai.v201112.ais.kayukataskin.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
public class BombHandler extends AiBombHandler<KayukaTaskin>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(KayukaTaskin ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
    	
    	ownHero=ai.getZone().getOwnHero();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiHero ownHero;
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		boolean canBomb=false;
		boolean wall=false;
		int range= ownHero.getBombRange();
		AiTile currentTile = ownHero.getTile();
	    		
		if(ownHero.getBombNumberMax()>0){
			if(canBomb)
			{
				if(ai.modeHandler.isCollectPossible()){
					
					int i;
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.UP).getBlocks()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.UP);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.DOWN).getBlocks()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.DOWN);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.LEFT).getBlocks()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.LEFT);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.RIGHT).getBlocks()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.RIGHT);
					}
					if(wall){
						return true;
					}
					
				}
				else
				{
					int i;
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.UP).getHeroes()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.UP);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.DOWN).getHeroes()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.DOWN);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.LEFT).getHeroes()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.LEFT);
					}
					for(i=0;i<range;i++){
						ai.checkInterruption();
						if(currentTile.getNeighbor(Direction.RIGHT).getHeroes()!=null){
							wall=true;
						}
						currentTile=currentTile.getNeighbor(Direction.RIGHT);
					}
					if(wall){
						return true;
					}
					
				}

			}
		
		
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
