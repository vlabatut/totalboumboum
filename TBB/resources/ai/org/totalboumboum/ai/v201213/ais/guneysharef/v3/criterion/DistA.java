package org.totalboumboum.ai.v201213.ais.guneysharef.v3.criterion;


import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.guneysharef.v3.GuneySharef;

/** 
 * @author Melis Güney
 * @author Seli Sharef
 */
public class DistA extends AiUtilityCriterionInteger<GuneySharef>
{	/** Nom de ce critère */
	public static final String NAME = "DisatanceAdversaire";
	/**
	 * nombre de cases pour proche
	 */
	public static final int Proche=3;
	/**
	 * nombre de cases pour loins
	 */
	public static final int Loins=10;
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistA(GuneySharef ai) throws StopRequestException
	{	super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Calcule et renvoie la valeur de critère pour la case passée en paramètre. 
	 */
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		int result = 0;
		AiZone z=this.ai.getZone();
		AiHero h=z.getOwnHero();
		
		for(AiHero h2 : z.getRemainingOpponents()){
			ai.checkInterruption();
			if(this.ai.getDist(h2.getTile(), tile)<=Proche)
				result=3;
			if(this.ai.getDist(h2.getTile(), tile) > Proche && this.ai.getDist(h.getTile(), tile) < Loins )
				result=2;
			if(this.ai.getDist(h2.getTile(), tile) >= Loins)
				result=1;
		}
				
		
		
		
		
		
		return result;
	}
}
