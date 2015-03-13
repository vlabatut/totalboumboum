package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings({ "unused", "deprecation" })
public class KayaYerinde extends ArtificialIntelligence{
	
	/** */
	private Matris matris;
	/** */
	private Rakip rakip;
	/** */
	private Bonus bonus;
	/** */
	private Duvar duvar;
	/** */
	private Escape escape;
	/** */
	private AiHero ownHero;
	/** */
	private long time;
	
	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		if(ownHero==null)
		{
			ownHero=getPercepts().getOwnHero();
			matris=new Matris(this);
			rakip=new Rakip(this);
			bonus=new Bonus(this);
			duvar=new Duvar(this);
			escape=new Escape(this);
			time=System.currentTimeMillis();
		}
		
		long time2=System.currentTimeMillis();

		AiAction result=new AiAction(AiActionName.NONE);
		
		
		matris.refresh();
		rakip.matrisAyarla(matris);
		bonus.matrisAyarla(matris);
		duvar.matrisAyarla(matris);
		escape.matrisAyarla(matris);

		
		result=rakip.getNextAction();
		if(result.getName()==AiActionName.NONE)
		{
			rakip=new Rakip(this);
			result=bonus.getNextAction();
		}
		if(result.getName()==AiActionName.NONE)
		{
			bonus=new Bonus(this);
			result=duvar.getNextAction();
		}
		if(result.getName()==AiActionName.NONE)
		{
			duvar=new Duvar(this);
			result=escape.getNextAction();
		}
		return result;
		
	}
	
	/**
	 * 
	 * @return
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public AiHero getOwnHero() throws StopRequestException
	{
		checkInterruption();
		return ownHero;
	}


}
