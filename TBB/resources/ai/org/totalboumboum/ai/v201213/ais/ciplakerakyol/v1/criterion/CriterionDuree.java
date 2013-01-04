
package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;

/**
 * Critère: Durée
 *
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionDuree extends AiUtilityCriterionInteger<CiplakErakyol>
{    /** Nom de ce critère */
    public static final String NAME = "Duree";
   
    /**
     * Crée un nouveau critère binaire.
     *
     * @param ai
     *         l'agent concerné.
     *
     * @throws StopRequestException   
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    public CriterionDuree(CiplakErakyol ai) throws StopRequestException
    {    super(ai,NAME,0,10); //soit temp limit est 10
        ai.checkInterruption();
    }
   
    /////////////////////////////////////////////////////////////////
    // PROCESS                    /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile) throws StopRequestException
    {    ai.checkInterruption();
       
    /*
     * 		ai.checkInterruption();
		int result = 2;
		
		int i;
		AiItem bonus;
		AiZone zone=ai.getZone();
		AiTile bizimCase=ai.getZone().getOwnHero().getTile(); //prend la valeur du tile de hero 
		List<AiItem> bonuslar=tile.getItems();// tous les items dans le tile 
		List<AiItem> alanBonus=tile.getItems();
	    //la distance entre le tile de l'agent et le tile
		double mesafeCase=zone.getTileDistance(bonus.getTile(),zone.getOwnHero().getTile());
		double mesafe=zone.getTileDistance(tile, bizimCase); 
		double hiz= ai.getZone().getOwnHero().getWalkingSpeed();
		double bizimZaman=(double)mesafe/hiz;
		double onlarinZaman=(double)mesafeCase/hiz;

		if (==1) {
			result = 1;
		}
		return result;
     */
    
        AiZone oyunAlani=ai.getZone();
        AiHero ajan=oyunAlani.getOwnHero();
        AiTile yerimiz=ajan.getTile();
       
        double mesafe= oyunAlani.getTileDistance(yerimiz, tile); //distance
        double hiz= ajan.getWalkingSpeed(); //vitesse
       
        double zaman= mesafe/hiz; //on calcule le temps car la vitesse
       
        return (int) zaman;

       
    }
}
