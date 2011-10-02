package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.strategy;

import java.util.Stack;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * Represents a strategy to be executed by the AI.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public abstract class Strategy
{

	/**
	 *  The AkbulutKupelioglu using this.
	 */
	public AkbulutKupelioglu monIa;
	/**
	 * The steps to be executed for this strategy.
	 */
	public Stack<StrategyStep> waypoints;
	/**
	 * A path to take, if necessary.
	 */
	public AiPath path;
	
	/**
	 * Creates a new Strategy instance.
	 * @param ia The AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public Strategy(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		waypoints = new Stack<StrategyStep>();
	}

	/**
	 * Gets the waypoints to a certain goal.
	 * @return A stack of StrategyStep objects, forming a waypoint.
	 * @throws StopRequestException
	 */
	public Stack<StrategyStep> getWaypoints() throws StopRequestException
	{
		monIa.checkInterruption();
		return waypoints;
	}
	
	
	/**
	 * Gets the path associated to this strategy, if any.
	 * @return The path associated, or null.
	 * @throws StopRequestException
	 */
	public AiPath getPath() throws StopRequestException
	{
		monIa.checkInterruption();
		return path;
	}

	/**
	 * Sets the path associated to this strategy.
	 * @param path The path to be set.
	 * @throws StopRequestException
	 */
	public void setPath(AiPath path) throws StopRequestException
	{
		monIa.checkInterruption();
		this.path = path;
	}
	

	
	
}
