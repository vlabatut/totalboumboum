package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.CinarYalcin;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class AdjacentCase extends AiUtilityCriterionInteger<CinarYalcin>
{	/** Nom de ce critère */
	public static final String NAME = "AdjacentCase";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AdjacentCase(CinarYalcin ai) throws StopRequestException
	{	super(ai,NAME,0,3);
		ai.checkInterruption();
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		int result = 0;
		
		AiMode mode=this.ai.getMode();

		if (mode == AiMode.COLLECTING) {
			for (AiTile currentNeighbors : tile.getNeighbors()) {
				ai.checkInterruption();
				for (AiBlock currentblock : currentNeighbors.getBlocks()) {
					ai.checkInterruption();

					if (currentblock.isDestructible()) {
						result = result + 1;
					}
				}
			}
		}

		else {
			for (AiTile currentNeighbors : tile.getNeighbors()) {
				ai.checkInterruption();
				if (!currentNeighbors.getBlocks().isEmpty()) {
					result = result + 1;
				}
			}

		}

		return result;
	}
}
