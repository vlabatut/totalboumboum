package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings({ "unused", "deprecation" })
public class Bomb {
	
	/** */
	private KayaYerinde onder;
	/** */
	private Astar astar;
	
	/**
	 * 
	 * @param onder
	 * @throws StopRequestException
	 */
	public Bomb(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
	}
	
	//bomba bırakırsam kaçabilir miyim
}
