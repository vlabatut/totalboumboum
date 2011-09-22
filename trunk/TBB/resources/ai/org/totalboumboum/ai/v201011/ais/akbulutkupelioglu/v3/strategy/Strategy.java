package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy;

import java.util.Stack;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class Strategy
{

	public AkbulutKupelioglu monIa;
	public Stack<StrategyStep> waypoints;
	public AiPath path;
	
	public Strategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		waypoints = new Stack<StrategyStep>();
	}

	public Stack<StrategyStep> getWaypoints() throws StopRequestException
	{
		monIa.checkInterruption();
		return waypoints;
	}

	public void setWaypoints(Stack<StrategyStep> waypoints) throws StopRequestException
	{
		monIa.checkInterruption();
		this.waypoints = waypoints;
	}

	public AiPath getPath() throws StopRequestException
	{
		monIa.checkInterruption();
		return path;
	}

	public void setPath(AiPath path) throws StopRequestException
	{
		monIa.checkInterruption();
		this.path = path;
	}
	

	
	
}
