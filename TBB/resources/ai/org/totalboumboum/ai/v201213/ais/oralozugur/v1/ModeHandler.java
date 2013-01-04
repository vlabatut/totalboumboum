package org.totalboumboum.ai.v201213.ais.oralozugur.v1;

import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. 
 * Cf. la documentation de {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class ModeHandler extends AiModeHandler<OralOzugur>
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
	/**
	 * Si l'agent a plus de bombe que ce nombre, alors il suffit.
	 * 
	 */
	private  int	BOMB_NUMBER_LIMIT;
	/**
	 * Si l'agent a plus de puissance que ce nombre , alors il suffit
	 */
	private  int	BOMB_RANGE_LIMIT;
	/**
	 * Le nombre d'item reste dans le zone pour finir le mode collecte.
	 */
	private final int ITEM_LEFT_CONTROL=1;
	
	/**
	 * La vitesse neccessaire pour sortir de la mode collecte
	 * 
	 * 
	 */
	private final int   SPEED_LIMIT=4;
	
	
	/**
	 * @param ai
	 * @throws StopRequestException
	 */
	protected ModeHandler(OralOzugur ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		//setting limit values
		int height=ai.getZone().getHeight();
		int width=ai.getZone().getWidth();
		if(width>height)
			BOMB_RANGE_LIMIT=2*width/3;
		else 
			BOMB_RANGE_LIMIT=2*height/3;
		if(ai.getZone().getTiles().size()>54)
			BOMB_NUMBER_LIMIT=4;
		else 
			BOMB_NUMBER_LIMIT=3;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone=ai.getZone();
		AiHero hero=zone.getOwnHero();
		if(hero.getBombNumberCurrent()>=BOMB_NUMBER_LIMIT && hero.getBombRange()>=BOMB_RANGE_LIMIT && hero.getCurrentSpeed()>SPEED_LIMIT)
			return true;
		else
			return false;
		
		
	
		
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
		AiZone zone= ai.getZone();
		AiHero hero= zone.getOwnHero();
		boolean result=true;
		if(zone.getHiddenItemsCount()>ITEM_LEFT_CONTROL || zone.getItems().size()>0)
		{
			boolean needOfextraBomb=(hero.getBombNumberCurrent()<BOMB_NUMBER_LIMIT) ? true : false;
			boolean needOfextraFlame=(hero.getBombRange()<BOMB_RANGE_LIMIT);
			boolean needOfextraSpeed=(hero.getCurrentSpeed()<SPEED_LIMIT);
			boolean extraBomb_done=false;	
			boolean extraFlame_done=false;
			boolean extraSpeed_done=false;
			if(needOfextraBomb==false||(zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB)<=0 && zone.getHiddenItemsCount(AiItemType.GOLDEN_BOMB)<=0))
				 extraBomb_done=true;
			if(needOfextraFlame==false||(zone.getHiddenItemsCount(AiItemType.EXTRA_FLAME)<=0&&zone.getHiddenItemsCount(AiItemType.GOLDEN_FLAME)<=0))
				 extraFlame_done=true;
			if(needOfextraSpeed==false||(zone.getHiddenItemsCount(AiItemType.EXTRA_SPEED)<=0 && zone.getHiddenItemsCount(AiItemType.GOLDEN_SPEED)<=0))
				 extraSpeed_done=true;
			if(extraBomb_done&&extraFlame_done&&extraSpeed_done)
				 result=false;
				 
			
		}
		else
			result=false;
		
		
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
