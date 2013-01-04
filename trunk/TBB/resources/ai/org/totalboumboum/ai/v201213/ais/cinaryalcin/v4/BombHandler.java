package org.totalboumboum.ai.v201213.ais.cinaryalcin.v4;


import java.util.ArrayList;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v4.CinarYalcin;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class BombHandler extends AiBombHandler<CinarYalcin>
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
	protected BombHandler(CinarYalcin ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
	
	AiHero ourhero = ai.getZone().getOwnHero();
	AiTile ourtile = ourhero.getTile();
	

	boolean resultat = false;
	
	AiMode mode = this.ai.modeHandler.getMode();
	ai.wallWillDestroyedList=ai.wallWillBeDestroyed();

	// If we have bombs in the hand
	if ((ourhero.getBombNumberCurrent() < ourhero.getBombNumberMax())&&(ourtile.getBombs().isEmpty())) {
	
		if (!ai.updateSafeTiles(ourhero,null,true,true).isEmpty()&& !ai.containsNull(ai.updateSafeTiles(ourhero,null,true,true))) {

			if (mode.equals(AiMode.COLLECTING)) {

				ai.bombRangeTiles=ai.bombRangeTiles();
				ArrayList<AiTile> block=ai.numberOfBlock();
				//if (ai.guvenliBolgeler()) {
					//3 murs peut destrictible 
					if(	ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)==21)
						resultat=true;
					//2 murs peut destructible
					else if(ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)==20)
						resultat=true;
					else if(ai.numberOfItems()>0)
						{
							resultat=false;
						}
					else if((block.size()>0)&&(!ai.wallWillDestroyedList.containsAll(block))){
							resultat=true;
						}
					else if(ai.MalusCounter>0)
					{
						resultat=true;
					}
						
			}//if
				
		else if (mode.equals(AiMode.ATTACKING)) {
			boolean control=false;
				if(ai.isBlockingEnemy(ourtile))
					resultat = true;
				else 
				{
					if(ai.possibleDangerForEnemy(null))	
					{ 
						for (AiTile aiTile : ai.bombRangeTiles2(5)) {
							ai.checkInterruption();
							for (AiHero aiHero : ai.zone.getRemainingOpponents()) {
								ai.checkInterruption();
								if((aiHero.getTile()==aiTile)&&(control==false))
								{
									resultat=true;
									control=true;
								}
								
							 }
						 }
						
					  }//if
					else if(ai.MalusCounter>0)
					{
						resultat=true;
					}
					
					
					else{
						
						if(ai.anyEnemieInOurZone()==false)
						{
							if(ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)!=null)
							{
								if(ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)==4)
									resultat=true;
							}
						}
					}//else
						
				}//else
			}//else if

		}//if

	}//if
		return resultat;
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