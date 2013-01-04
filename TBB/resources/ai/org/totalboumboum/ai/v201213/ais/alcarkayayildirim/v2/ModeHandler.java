package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v2;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * Our mode handler class.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class ModeHandler extends AiModeHandler<AlcarKayaYildirim>
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
	protected ModeHandler(AlcarKayaYildirim ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
		int myHeroBombNumber = ai.getZone().getOwnHero().getBombNumberMax();
		int myHeroBombRange = ai.getZone().getOwnHero().getBombRange();
		
		//on a 2 bombe et la portée est 3, en collecte
		//sinon en attaque
		if(myHeroBombNumber>=2 && myHeroBombRange > 2)
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
		int zoneItemListVisible = ai.getZone().getItems().size();
		int zoneItemListHidden = ai.getZone().getHiddenItemsCount();
		
		if(zoneItemListVisible>0 || zoneItemListHidden>0)
		{
			return true;
		}
		
		return false;
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
