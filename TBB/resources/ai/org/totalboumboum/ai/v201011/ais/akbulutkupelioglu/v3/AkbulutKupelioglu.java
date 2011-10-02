package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.mode.Mode;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.strategy.Strategy;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util.Matrix;

/**
 * The main class for AkbulutKupelioglu AI. It receives the perceptions sent by
 * the engine, selects a mode, calculates a matrix, chooses an action and sends
 * it to the game engine.
 * 
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
@SuppressWarnings("deprecation")
public class AkbulutKupelioglu extends ArtificialIntelligence
{

	private AiZone zone;
	private AiOutput output;
	private Strategy strategy = null;
	private Mode mode = null;
	//private InformationManager informationManager = null;

	
	public void init() throws StopRequestException {
		checkInterruption();
		ModeSelector.reset(this);
	};
	
	/** method called by the game engine to get our action*/
	public AiAction processAction() throws StopRequestException
	{
		checkInterruption();

		zone = getPercepts();
		output = getOutput();	
		
		//Mode decision
		mode = ModeSelector.selectMode(zone, this);
		
		//bomb decision
		BombDecider bombDecider = BombDecider.getInstance(zone, this);
		boolean bomb = bombDecider.willBomb();
		
		//Matrix calculation
		Matrix interest = mode.calculateMatrix();

		//Outputs
		outputMatrix(interest);

		//action decision
		ActionDecider actionDecider = ActionDecider.getInstance(interest, zone, this);
		AiAction result = actionDecider.decide(bomb); 
		
		return result;
	}
	private void outputMatrix(Matrix interest) throws StopRequestException
	{
		checkInterruption();
		int i, j;
		for(i = 0; i < interest.getHeight(); i++)
		{
			checkInterruption();
			for(j = 0; j < interest.getWidth(); j++)
			{
				checkInterruption();
				output.setTileText(i, j, Integer.toString(interest.getElement(
						i, j)));
			}
		}
	}

	public Strategy getStrategy() throws StopRequestException
	{
		checkInterruption();
		return strategy;
	}

	public void setStrategy(Strategy strategy) throws StopRequestException
	{
		checkInterruption();
		this.strategy = strategy;
	}

	public Mode getMode() throws StopRequestException
	{
		checkInterruption();
		return mode;
	}

	public void setMode(Mode mode) throws StopRequestException
	{
		checkInterruption();
		this.mode = mode;
	}

}
