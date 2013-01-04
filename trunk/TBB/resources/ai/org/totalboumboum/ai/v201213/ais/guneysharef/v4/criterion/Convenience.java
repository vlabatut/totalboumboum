package org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.GuneySharef;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
public class Convenience extends AiUtilityCriterionBoolean<GuneySharef>
{	/** Nom de ce critère */
	public static final String NAME = "PERTİNENCE";
	/**
	 *Limite de nombre de bombe qu'on a besoin 
	 */
	public static final int NbrBombe = 2;
	/**
	 * Limite de nombre de flame qu'on a besoin 
	 */
	public static final int RangeBombe = 3;
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Convenience(GuneySharef ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai=ai;
	}
	
	@Override
	/**
	 * Calcule et renvoie la valeur de critère pour la case passée en paramètre. 
	 */
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		AiHero h=this.ai.getZone().getOwnHero();
	
		if(h.getBombNumberMax()<NbrBombe)
			if(tile.getItems().contains(AiItemType.EXTRA_BOMB))
				result = true;
		
		if(h.getBombRange()<RangeBombe)
			if(tile.getItems().contains(AiItemType.EXTRA_FLAME))
				result = true;
			
	
		return result;
	}
}
