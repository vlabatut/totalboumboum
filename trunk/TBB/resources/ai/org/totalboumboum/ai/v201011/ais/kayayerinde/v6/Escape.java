package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import java.util.ArrayList;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings("deprecation")
public class Escape {
	/** */
	private KayaYerinde onder;
	/** */
	private Matris matris;
	/** */
	private Astar astar;
	/** */
	private boolean running;
	
	/**
	 * 
	 * @param onder
	 * @throws StopRequestException
	 */
	public Escape(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		this.onder=onder;
		astar=new Astar(onder);
		running=false;
	}
	
	/**
	 * 
	 * @param matris
	 * @throws StopRequestException
	 */
	public void matrisAyarla(Matris matris) throws StopRequestException
	{
		onder.checkInterruption();
		this.matris=matris;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiAction getNextAction() throws StopRequestException
	{
		onder.checkInterruption();
		AiAction result=new AiAction(AiActionName.NONE);
		if(matris.getVarlikMatrisi()[onder.getOwnHero().getLine()][onder.getOwnHero().getCol()]<Matris.BONUSDANGER)
			return result;
		System.out.println("bakıyorum");
		if(running)
		{
			if(astar.yoldaMi()==1)
				return astar.getNextAction(matris);
			astar.sictinKapat();
			running=false;
		}
		if(!running)
		{
			ArrayList<AiTile> safes=matris.getFrees();
			safes.addAll(matris.getBonus());
			for(AiTile t:safes)
			{
				onder.checkInterruption();
				if(astar.yolBul(onder.getOwnHero().getTile(), t,Matris.FREE, matris));
				{
					if(astar.yoldaMi()==1)
					{
						running=true;
						result=astar.getNextAction(matris);
						break;
					}
				}
			}
			if(!running)
			{
				astar.sictinAc();
				for(AiTile t:safes)
				{
					onder.checkInterruption();
					if(astar.yolBul(onder.getOwnHero().getTile(), t,Matris.FREE, matris));
					{
						if(astar.yoldaMi()==1)
						{
							running=true;
							result=astar.getNextAction(matris);
							break;
						}
					}
				}
			}
		}
		return result;
	}
}
