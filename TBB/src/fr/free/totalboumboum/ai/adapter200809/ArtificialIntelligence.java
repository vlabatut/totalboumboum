package fr.free.totalboumboum.ai.adapter200809;

import java.util.concurrent.Callable;


public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiZone percepts;
	
	public AiZone getPercepts()
	{	return percepts;
	}
	public void setPercepts(AiZone percepts)
	{	this.percepts = percepts;
	}
}
