package org.totalboumboum.ai.v201112.ais.demirsazan.v3;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 *  Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Serdil Demir
 * @author Gökhan Sazan
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<DemirSazan>
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
	protected BombHandler(DemirSazan ai) throws StopRequestException
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
	
		AiZone gameArea = ai.getZone();
		AiHero ownHero = gameArea.getOwnHero();
		AiTile hedef = ai.getMoveHandler().getCurrentDestination();
		CommonTools commonTools = new CommonTools(ai);
		if(commonTools.possibleFuir(ownHero.getTile(), gameArea,ownHero, ai.utilityHandler.selectedTiles)){
			if(ai.getModeHandler().getMode() == AiMode.COLLECTING){
				if(ownHero.getBombNumberMax()>0){
					if(hedef.equals(ownHero.getTile()) && gameArea.getTotalTime()>100&& !commonTools.amIinDengeraous(ownHero,gameArea)&& ownHero.getBombNumberCurrent()<2){
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
			else{
				if(ownHero.getBombNumberMax()>0){
					if(commonTools.isPossibleArriver()){
						if(hedef.equals(ownHero.getTile())&& !commonTools.amIinDengeraous(ownHero,gameArea)&& gameArea.getTotalTime()>300){
							return true;
						}
						else{
							return false;
						}
					}else{
						if(hedef.equals(ownHero.getTile()) && !commonTools.amIinDengeraous(ownHero,gameArea) && gameArea.getTotalTime()>300){
							return true;
						}
						else{
							return false;
						}
					}
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
	}
}
