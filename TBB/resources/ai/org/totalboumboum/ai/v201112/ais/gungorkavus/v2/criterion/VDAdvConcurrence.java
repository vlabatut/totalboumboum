package org.totalboumboum.ai.v201112.ais.gungorkavus.v2.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
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
public class VDAdvConcurrence extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Concur";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDAdvConcurrence(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}

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
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		
		ai.checkInterruption();
		boolean result = false;

	

	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();
	List<AiHero> zOpponent = zone.getRemainingOpponents();
	double ownDistance = distance(tile.getCol(), tile.getRow(), ownHero.getCol(), ownHero.getRow());
	int c=0;

	
	if(zOpponent.size()>1)
	{
		
		
		for(int i=0;i<zOpponent.size();i++)
		{
			ai.checkInterruption();
			
			if(ownDistance>distance(tile.getCol(),tile.getRow(),zOpponent.get(i).getCol(),zOpponent.get(i).getRow()))
			{
				c++;
			}
		}
		
		if(c>=1)
			result=true;
		
	}
	
	
	return result;
	}
}