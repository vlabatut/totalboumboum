package org.totalboumboum.ai.v201213.ais.cinaryalcin.v2;


import java.util.List;




import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;


import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.CinarYalcin;

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
	
	AiHero ourhero = this.ai.getZone().getOwnHero();
	AiTile ourtile = ourhero.getTile();
	AiTile tileBakilan = ourtile;

	boolean resultat = false;
	boolean controlBlock = true;
	AiMode mode = this.ai.modeHandler.getMode();

	// If we have bombs in the hand
	if ((ourhero.getBombNumberCurrent() < ourhero.getBombNumberMax())&&(ourtile.getBombs().isEmpty())) {
	
		if (this.ai.updateSafeTiles(ourhero, null, true).size() > 0) {

			if (mode.equals(AiMode.COLLECTING)) {

			//if (ai.guvenliBolgeler()) {
				//3 murs peut destrictible 
				if(	ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)==21)
					resultat=true;
				//2 murs peut destructible
				else if(ai.getUtilityHandler().getUtilitiesByTile().get(ourtile)==20)
					resultat=true;
				else{
				// pour exploser les murs dans notre range.
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
				
					List<AiBlock> blockList = tileBakilan.getBlocks();
					
					for(int j=0;(j<ourhero.getBombRange()&&(resultat==false)&&(controlBlock==true));j++)
					{	
						ai.checkInterruption();
						if(blockList.size()>0){
						// Si l y a une mur desturictible dans notre bombe range on peut poser.
						for (AiBlock block : blockList) {
							ai.checkInterruption();
							if(!block.isDestructible())
							{
								controlBlock=false;
								
							}
							else if(!(ai.willBeDestroyed(tileBakilan)))					
							{	
								ai.checkInterruption();
								return resultat = true;
							}
							}
						}
			
						tileBakilan=tileBakilan.getNeighbor(direction);
						blockList=tileBakilan.getBlocks();
						
						ai.checkInterruption();
						}
					controlBlock=true;
					tileBakilan=ourtile;
				}
			}
				
				List<AiTile> tileNeighbors=ourtile.getNeighbors();
				
				//Pour exploser le malus dans notre zone de jeu
				
			for(AiTile oneOfTiles : tileNeighbors)
			{
					ai.checkInterruption();
					for (AiItem itemType : oneOfTiles.getItems()) 
					{
						ai.checkInterruption();
						if(ai.Malus.contains(itemType)&&!(ai.willBeDestroyed(oneOfTiles)))
							resultat = true;
					}
			}
				
		}else if (mode.equals(AiMode.ATTACKING)) {
			
		
				if(ai.isBlockingEnemy(ourtile))
					resultat = true;
				else 
				{
					if(ai.getDist(ourtile,ai.getNearestEnemy().getTile())<2)
					{
						resultat=true;
					}
				}
			}

		}

	}
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