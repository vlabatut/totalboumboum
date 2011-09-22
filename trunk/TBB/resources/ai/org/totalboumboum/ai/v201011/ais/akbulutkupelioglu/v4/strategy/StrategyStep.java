package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.strategy;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * Represents a step to be executed to achieve a certain goal determined by a strategy.
 * It consists of an action to be executed, a tile where that action should be executed, and a
 * value, which can be used to give weights to certain strategies.
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class StrategyStep
{
	private AiTile tile;
	private AiAction action;
	private int value;
	private AkbulutKupelioglu monIa;
	
	
	/**
	 * Creates a new StrategyStep instance.
	 * @param tile The tile to which the hero should travel.
	 * @param action The action to take when the hero travels to the tile.
	 * @param ia AkbulutKupelioglu using this.
	 * @throws StopRequestException
	 */
	public StrategyStep(AiTile tile, AiAction action, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		this.tile = tile;
		this.action = action;
	}


	/**
	 * Gets the goal tile.
	 * @return The goal tile.
	 * @throws StopRequestException
	 */
	public AiTile getTile() throws StopRequestException
	{
		monIa.checkInterruption();
		return tile;
	}


	/**
	 * Sets the goal tile.
	 * @param tile The tile to be set as goal tile.
	 * @throws StopRequestException
	 */
	public void setTile(AiTile tile) throws StopRequestException
	{
		monIa.checkInterruption();
		this.tile = tile;
	}


	/**
	 * Gets the action to be executed.
	 * @return The action to be executed.
	 * @throws StopRequestException
	 */
	public AiAction getAction() throws StopRequestException
	{
		monIa.checkInterruption();
		return action;
	}


	/**
	 * Sets the action to be executed.
	 * @param action The action to be set.
	 * @throws StopRequestException
	 */
	public void setAction(AiAction action) throws StopRequestException
	{
		monIa.checkInterruption();
		this.action = action;
	}


	/**
	 * Gets the value of this step.
	 * @return The value of this step.
	 * @throws StopRequestException
	 */
	public int getValue() throws StopRequestException
	{
		monIa.checkInterruption();
		return value;
	}


	/**
	 * Sets the value of this step.
	 * @param value The value to be set.
	 * @throws StopRequestException
	 */
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
