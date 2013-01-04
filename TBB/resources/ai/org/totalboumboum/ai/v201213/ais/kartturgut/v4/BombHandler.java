package org.totalboumboum.ai.v201213.ais.kartturgut.v4;



import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 

 * @author Yunus Kart
 * @author Siyabend Turgut
 */

public class BombHandler extends AiBombHandler<KartTurgut>
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
	protected BombHandler(KartTurgut ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		
		verbose = true;
		
		
	}
	/** */
	private AiHero notreHero;
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		boolean onPeutBomber=false;
		boolean mur=false;
		int flamme= notreHero.getBombRange();
		AiTile notreTile = notreHero.getTile();
	    		
		if(notreHero.getBombNumberMax()>0){
			if(onPeutBomber)
			{
				if(ai.modeHandler.isCollectPossible()){
					
					int i;
					for(i=0;i<flamme;i++){
		            	ai.checkInterruption();
		            	if(notreTile.getNeighbor(Direction.UP).getBlocks()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.UP);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.DOWN).getBlocks()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.DOWN);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.LEFT).getBlocks()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.LEFT);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.RIGHT).getBlocks()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.RIGHT);
					}
					if(mur){
						return true;
					}
					
				}
				else
				{
					int i;
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.UP).getHeroes()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.UP);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.DOWN).getHeroes()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.DOWN);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.LEFT).getHeroes()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.LEFT);
					}
					for(i=0;i<flamme;i++){
						ai.checkInterruption();
						if(notreTile.getNeighbor(Direction.RIGHT).getHeroes()!=null){
							mur=true;
						}
						notreTile=notreTile.getNeighbor(Direction.RIGHT);
					}
					if(mur){
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
	/**
	 * @throws StopRequestException 
	 * 
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();

	}

	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
}
