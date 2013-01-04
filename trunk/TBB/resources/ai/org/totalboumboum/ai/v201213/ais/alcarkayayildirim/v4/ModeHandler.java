package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v4;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

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
	{	this.ai.checkInterruption();
		
		boolean result = true;
	
		int myHeroBombNumber = ai.getZone().getOwnHero().getBombNumberMax();
		int myHeroBombRange = ai.getZone().getOwnHero().getBombRange();
		
		int zoneBombBonusCount = 0, zoneFlameBonusCount = 0;
		int hiddenBombBonusCount = ai.getZone().getHiddenItemsCount(AiItemType.EXTRA_BOMB);
		int hiddenExtraFlameBonusCount = ai.getZone().getHiddenItemsCount(AiItemType.EXTRA_FLAME);
		
		for (AiTile tile : this.ai.utilityHandler.selectTiles()) {
				ai.checkInterruption();
				for (AiItem item : tile.getItems()) {
					ai.checkInterruption();
				if(item.getType().equals(AiItemType.EXTRA_BOMB)) zoneBombBonusCount++;
				if(item.getType().equals(AiItemType.EXTRA_FLAME)) zoneFlameBonusCount++;
			}
			
		}
		

		boolean bombEnough = myHeroBombNumber>=2;
		boolean flameEnough = myHeroBombRange > 2;
		//on a 2 bombe et la portée est 3, en collecte
		//sinon en attaque
		if(bombEnough && flameEnough)
		{
			result = true;
		}else
		{
		   if(bombEnough)
		   {
			   if(hiddenExtraFlameBonusCount>0 || zoneFlameBonusCount>0)
			   {
				   result = false;
			   }else
			   {
				   result = true;
			   }			   
		   }else
		   {
			   if(flameEnough)
			   {
				   
				   if(zoneBombBonusCount>0 || hiddenBombBonusCount>0)
				   {
					   result = false;
				   }else
				   {
					   result = true;
				   }
				   
			   }else
			   {
				   if(hiddenBombBonusCount>0 || zoneFlameBonusCount>0 || zoneBombBonusCount>0 || hiddenBombBonusCount>0)
				   {
					   result = false;
				   }else
				   {
					   result = true;
				   }	
			   }
		   }
			
		}
		
		return result;
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
