package org.totalboumboum.ai.v201112.ais.gungorkavus.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v1.GungorKavus;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Eyüp Burak Güngör
 * @author Umit Kavus
 */
@SuppressWarnings("deprecation")
public class VDMDestMPBPertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDMDestMPBPertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);

		// init agent
		this.ai = ai;
	}

	/**
	 * 
	 * @param d
	 * @param e
	 * @param f
	 * @param g
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int distance(int d,int e,int f,int g) throws StopRequestException
	{
		ai.checkInterruption();
		int resultat = Math.abs(d-f)+Math.abs(e-g);

		return resultat;

	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

	/////////////////////////////////////////////////////////////////
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;

	

	AiZone zone = ai.getZone();
	List<AiItem> item = zone.getItems();
	AiHero ownHero = zone.getOwnHero();

	if(item.size()==0){
		result = true;
	}else
	{
		int shortest = distance(item.get(0).getCol(), item.get(0).getRow(), ownHero.getCol(), ownHero.getRow()); ;
		AiItem sItem = item.get(0);	
		for(int i = 0;i<item.size()-1;i++){
			ai.checkInterruption();
			if(shortest > distance(item.get(i+1).getCol(), item.get(i+1).getRow(), ownHero.getCol(), ownHero.getRow()))
			{
				shortest = distance(item.get(i+1).getCol(), item.get(i+1).getRow(), ownHero.getCol(), ownHero.getRow());
				sItem = item.get(i+1);
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