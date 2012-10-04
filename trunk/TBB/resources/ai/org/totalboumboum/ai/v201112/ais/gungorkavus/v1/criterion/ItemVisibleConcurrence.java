package org.totalboumboum.ai.v201112.ais.gungorkavus.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
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
public class ItemVisibleConcurrence extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Concur";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai 
	 * 		?
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ItemVisibleConcurrence(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);

		// init agent
		this.ai = ai;
	}

	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected GungorKavus ai;

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
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;

	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();
	List<AiHero> opponents = zone.getRemainingOpponents();

	for(int i = 0;i<opponents.size();i++)
	{
		ai.checkInterruption();
		
		if(distance(tile.getCol(), tile.getRow(), ownHero.getCol(), ownHero.getRow())>distance(tile.getCol(), tile.getRow(), opponents.get(i).getCol(), opponents.get(i).getRow()))
		{
			result = true;
		}	
	}

	return result;
	}
}