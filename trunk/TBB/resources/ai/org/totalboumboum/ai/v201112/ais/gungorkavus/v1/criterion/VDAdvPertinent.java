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
public class VDAdvPertinent extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "Pertinent";

	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @throws StopRequestException	
	 * Au cas où le moteur demande la terminaison de l'agent.
	 */
	public VDAdvPertinent(GungorKavus ai) throws StopRequestException
	{	// init nom
		super(NAME);

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
	// PROCESS	 /////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = false;


	AiZone zone = ai.getZone();
	AiHero ownHero = zone.getOwnHero();
	int ownForce = ownHero.getBombNumberMax()*50+ownHero.getBombRange();
	int opForce = 0;

	List<AiHero> opponent = tile.getHeroes();

	for(int i = 0;i<opponent.size();i++){
		ai.checkInterruption();
		opForce = opponent.get(i).getBombNumberMax()*50+opponent.get(i).getBombRange()*70;
		if(opForce<=ownForce&&opponent.get(i).getColor()!=ownHero.getColor()){
			result = true;
		}
	}



	return result;
	}
}