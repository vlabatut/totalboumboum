package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v4;
 
import java.util.List;
 
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
 
/**
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class ModeHandler extends AiModeHandler<CiplakErakyol>
{     
    /**
     * Construit un gestionnaire pour l'agent passé en paramètre.
     * 
     * @param ai     
     *         l'agent que cette classe doit gérer.
     * 
     * @throws StopRequestException     
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    protected ModeHandler(CiplakErakyol ai) throws StopRequestException
    {   
    	super(ai);
        ai.checkInterruption();
        verbose = false;
         
    }
 
	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException 
	{
		ai.checkInterruption();
		AiZone oyunAlani = ai.getZone(); 
		AiHero ajan = oyunAlani.getOwnHero(); 
		int bombaSayisi = ajan.getBombNumberMax();
		int bombaMenzili = ajan.getBombRange(); 
		
		if (bombaSayisi <2 || bombaMenzili > 2 )
			return true; // mode collecte
		else
			return false; // mode attaque
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException 
	{
		ai.checkInterruption();

		boolean sonuc = false;
		AiZone oyunAlani = ai.getZone(); // prend la zone current
		List<AiItem> item = oyunAlani.getItems(); // les nombre des tous items
		int bonusSayisi = item.size();
		int gizliBonus = oyunAlani.getHiddenItemsCount(); // les items caches
		// s'il n'y a pas des items caches et des items ouverts dans la zone
		// l'agent ne peux pas ramasser
		if (bonusSayisi > 0 || gizliBonus > 0)
			sonuc = true;

		return sonuc;
	}
 
    /////////////////////////////////////////////////////////////////
    // OUTPUT            /////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /**
     * Met à jour la sortie graphique.
     * 
     * @throws StopRequestException 
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    protected void updateOutput() throws StopRequestException
    {   
    	ai.checkInterruption(); 
    }
} 