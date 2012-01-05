package org.totalboumboum.ai.v201112.ais.demireloz.v2;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

public class ModeHandler extends AiModeHandler<DemirelOz>
{	public static final int BOMB_NUMBER=2;
	public static final int RANGE_NUMBER=3;


	protected ModeHandler(DemirelOz ai) throws StopRequestException
	{	super(ai);
	ai.checkInterruption();

	verbose = false;


	}

	@Override
	protected boolean hasEnoughItems()  throws StopRequestException
	{	ai.checkInterruption();
	AiZone zone =this.ai.getZone();
	AiHero ourhero =zone.getOwnHero();
	boolean result = false;
	
	if(ourhero.getBombNumberMax() >=BOMB_NUMBER && ourhero.getBombRange() >=RANGE_NUMBER )
	{
		result= true;
	}

	return result;	
	}


	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	AiZone zone =this.ai.getZone();
	AiHero ourhero =zone.getOwnHero();
	
	 
	boolean result = false;
	boolean result1=false;
	boolean needbomb=false;
	boolean needrange=false;

	if(ourhero.getBombNumberMax() <BOMB_NUMBER)
	{
		if(zone.getItems().contains(AiItemType.EXTRA_BOMB))
		{
			result1= true;

		}

		needbomb=true;

	}

	if(ourhero.getBombRange() <RANGE_NUMBER)
	{
		if(zone.getItems().contains(AiItemType.EXTRA_FLAME))
		{
			result1= true;

		}
		needrange=true;
	}


	if(zone.getItems().isEmpty()!=true && result1==true)
	{
		result =true;
	}
	if(needbomb)
	{
		if(zone.getHiddenItemsCount(AiItemType.EXTRA_BOMB) >0)
		{
			result = true;
		}
	}
	if(needrange)
	{
		if(zone.getHiddenItemsCount(AiItemType.EXTRA_FLAME) >0)
		{
			result = true;
		}
	}

	return result;
	}


	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();

	}
}
