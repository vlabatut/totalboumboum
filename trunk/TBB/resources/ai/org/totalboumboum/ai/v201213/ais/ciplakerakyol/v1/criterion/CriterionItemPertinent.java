package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionItemPertinent extends AiUtilityCriterionBoolean<CiplakErakyol>
{	/** Nom de ce critère */
	public static final String NAME = "Pertinente";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionItemPertinent(CiplakErakyol ai) throws StopRequestException
	{	
		super( ai, NAME );
		ai.checkInterruption();
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		boolean result = false;
		AiZone zone=ai.getZone();
		AiHero ajan = zone.getOwnHero();
		int bombaSayisi = ajan.getBombNumberMax(); // le nombre des bombes de notre agent
	    int bombaMenzili = ajan.getBombRange(); //La portée des bombes que notre agent peux posser. 
	    
	    if(tile.getItems().contains(AiItemType.EXTRA_BOMB))
	    {
	    	if ( bombaSayisi < bombaMenzili )
			{
				result = true;
			}
			else
				result = false;
	    }
	    if(tile.getItems().contains(AiItemType.EXTRA_FLAME))
	    {
			if ( bombaSayisi > bombaMenzili )
			{
				result = true;
			}
			else
				result = false;
	    }

		return result;
	}
}
