package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v2.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
public class VDMDestPertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";
	
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestPertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
	public int distance(int d,int e,int f,int g) throws StopRequestException{
		ai.checkInterruption();
		
		int resultat = Math.abs(d-f)+Math.abs(e-g);
		
		return resultat;
		
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
	
	
		
	AiZone zone = ai.getZone();
	List<AiItem> item = zone.getItems();
	AiHero ownHero = zone.getOwnHero();

	if(item.size()==0){
		result = true;
	}else
	{
		double shortest = distance(item.get(0).getCol(), item.get(0).getRow(), ownHero.getCol(), ownHero.getRow()); ;
		for(int i = 0;i<item.size()-1;i++){
			ai.checkInterruption();
			if(shortest > distance(item.get(i+1).getCol(), item.get(i+1).getRow(), ownHero.getCol(), ownHero.getRow()))
			{
				shortest =  distance(item.get(i+1).getCol(), item.get(i+1).getRow(), ownHero.getCol(), ownHero.getRow());
			}
		}

		if(shortest>3)
		{
			result=true;

		}
	}
		
		
		
	
		return result;
	}
}
