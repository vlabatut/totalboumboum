package fr.free.totalboumboum.ai.adapter200809;

import java.util.concurrent.Callable;


public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean stopRequest = false;
	
	public synchronized void stopRequest()
	{	stopRequest = true;		
	}
	
	protected synchronized void checkInterruption() throws StopRequestException
	{	if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public AiAction call()
	{	AiAction result;
		try
		{	result = processAction();		
		}
		catch (StopRequestException e)
		{	result = new AiAction(AiActionName.NONE);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiZone percepts;
	
	public abstract AiAction processAction() throws StopRequestException;
	
	public AiZone getPercepts()
	{	return percepts;
	}
	public void setPercepts(AiZone percepts)
	{	this.percepts = percepts;
	}
	
	void finish()
	{	percepts = null;
	}
}
