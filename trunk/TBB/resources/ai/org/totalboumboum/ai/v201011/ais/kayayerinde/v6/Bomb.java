package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;



@SuppressWarnings("unused")
public class Bomb {
	
	private KayaYerinde onder;
	private Astar astar;
	
	public Bomb(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
	}
	
	//bomba bırakırsam kaçabilir miyim
}
