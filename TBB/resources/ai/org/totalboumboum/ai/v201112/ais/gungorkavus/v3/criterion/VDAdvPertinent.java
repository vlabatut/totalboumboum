package org.totalboumboum.ai.v201112.ais.gungorkavus.v3.criterion;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.gungorkavus.v3.GungorKavus;

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
		ai.checkInterruption();
		// init agent
		this.ai = ai;
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
		int ownForce = ownHero.getBombNumberMax()*50+ownHero.getBombRange()*70;
		int opForce = 0;

		Set<AiHero> opponentL = new TreeSet<AiHero>();
		List<AiTile> tileNL = tile.getNeighbors();

		List<AiHero> remainingOp = zone.getRemainingHeroes();

		for(int i = 0;i<tileNL.size();i++){
			ai.checkInterruption();

			for(int j = 0;j<tileNL.get(i).getHeroes().size();j++){
				ai.checkInterruption();

				if(remainingOp.contains(tileNL.get(i).getHeroes().get(j)))
					opponentL.add(tileNL.get(i).getHeroes().get(j));

			}

		}

		Iterator<AiHero> heroIt = opponentL.iterator();

		while(heroIt.hasNext()){
			ai.checkInterruption();

			AiHero opponent = heroIt.next();
			opForce = opponent.getBombNumberMax()*50+opponent.getBombRange()*70;
			if(opForce<=ownForce){
				result = true;
			}

		}



		return result;
	}
}