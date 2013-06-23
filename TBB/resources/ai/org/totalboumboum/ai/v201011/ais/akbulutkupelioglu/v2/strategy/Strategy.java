package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

public class Strategy
{
	private AiTile goalCase = null;
	private AiAction goalAction = null;
	private int goalValue;
	public AkbulutKupelioglu monIa;
	
	public Strategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
	}
	
	public AiTile getGoalCase() throws StopRequestException
	{
		monIa.checkInterruption();
		return goalCase;
	}
	public void setGoalCase(AiTile goalCase) throws StopRequestException
	{
		monIa.checkInterruption();
		this.goalCase = goalCase;
	}
	public AiAction getGoalAction() throws StopRequestException
	{
		monIa.checkInterruption();
		return goalAction;
	}
	public void setGoalAction(AiAction goalAction) throws StopRequestException
	{
		monIa.checkInterruption();
		this.goalAction = goalAction;
	}
	public int getGoalValue() throws StopRequestException
	{
		monIa.checkInterruption();
		return goalValue;
	}
	public void setGoalValue(int goalValue) throws StopRequestException
	{
		monIa.checkInterruption();
		this.goalValue = goalValue;
	}
	
	
}
