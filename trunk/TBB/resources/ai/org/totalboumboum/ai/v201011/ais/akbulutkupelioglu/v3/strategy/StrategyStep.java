package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class StrategyStep
{
	private AiTile tile;
	private AiAction action;
	private int value;
	private AkbulutKupelioglu monIa;
	
	
	public StrategyStep(AiTile tile, AiAction action, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.tile = tile;
		this.action = action;
	}


	public AiTile getTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return tile;
	}


	public void setTile(AiTile tile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.tile = tile;
	}


	public AiAction getAction() throws StopRequestException
	{
		monIa.checkInterruption();
		return action;
	}


	public void setAction(AiAction action) throws StopRequestException
	{
		monIa.checkInterruption();
		this.action = action;
	}


	public int getValue() throws StopRequestException
	{
		monIa.checkInterruption();
		return value;
	}


	public void setValue(int value) throws StopRequestException
	{
		monIa.checkInterruption();
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "[" + action.getName().toString() + "@" + tile.toString() + "(" + value +")]";
	}
}
