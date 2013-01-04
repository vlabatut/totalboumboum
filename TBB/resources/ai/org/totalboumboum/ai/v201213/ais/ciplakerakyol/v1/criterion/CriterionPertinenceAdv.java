package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;
 
import java.util.List;
 
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;

 
/**
    Critere de pertinence l'adversaire.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionPertinenceAdv extends AiUtilityCriterionInteger<CiplakErakyol>
{    /** Nom de ce critère */
    public static final String NAME = "Pertinent";
     
    /**
     * Crée un nouveau critère entier.
     * 
     * @param ai
     *         l'agent concerné. 
     * 
     * @throws StopRequestException     
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    public CriterionPertinenceAdv(CiplakErakyol ai) throws StopRequestException
    {    super(ai, NAME, 0, 10); // limit de distance=10
        ai.checkInterruption();
    }
     
    /**
     * 
     * @param z1
     * @param z2
     * @return ?
     * @throws StopRequestException
     */
    protected int getDistance(AiTile z1, AiTile z2) throws StopRequestException
    {
        int distance;
        ai.checkInterruption();
        distance=Math.abs(z1.getRow()-z2.getRow())+Math.abs(z1.getCol()-z2.getCol()); //manhatton
                 
        return distance;
         
    }
    
    /////////////////////////////////////////////////////////////////
    // PROCESS                    /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile) throws StopRequestException
    {    ai.checkInterruption();
     
        AiZone oyunAlani=ai.getZone();
        List<AiHero> ajanlar=oyunAlani.getHeroes();
        int mesafe=10;
        ajanlar.remove(oyunAlani.getOwnHero());
         
        if(ajanlar!= null){
            for(int i = 0 ; i< ajanlar.size();i++){
                ai.checkInterruption();
                int tilemesafesi = getDistance(tile, ajanlar.get(i).getTile());
                 
                if( tilemesafesi< mesafe){
                    mesafe = tilemesafesi;
                }
            }
        }
        return mesafe;
         
    }
} 