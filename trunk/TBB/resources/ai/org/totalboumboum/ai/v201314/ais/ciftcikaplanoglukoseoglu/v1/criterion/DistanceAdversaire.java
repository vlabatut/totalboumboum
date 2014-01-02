package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v1.Agent;




/**
 * 
 * Cette classe va nous permettre de traiter une case par rapport au
 * critere binaire "distance a l'adversaire".
 * 
 * 
 *
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class DistanceAdversaire extends AiCriterionBoolean<Agent>
{	/** Nom du critère */
public static final String NAME = "DISTANCE_ADVERSAIRE";

    /**
     * Crée le critère binaire distance a l'ennemi.
     *
     * @param ai
     * 		l'agent concerné.
     */
    public DistanceAdversaire(Agent ai)
    {	super(ai,NAME);
        ai.checkInterruption();
    }

    /////////////////////////////////////////////////////////////////
    // PROCESS					/////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    /**
     * Définit le traitement de la case par rapport a notre critere.
     *
     * @param tile
     * 		la case concernée.
     * @return result
     *  renvoie la valeur booléene qui est obtenu par le traitement.
     *Si l'adversaire est au plus a trois case de distance de
     *la case concernée, la valeur est vrai. Sinon elle est fausse.
     */
    @Override
    public Boolean processValue(AiTile tile)
    {	ai.checkInterruption();
        boolean result=false;
        int distm=150;
        int dist;
        //on prend les agents adverse dans la zone.
        List<AiHero> heroes= tile.getZone().getRemainingOpponents();
      
        AiHero adversaire=null;
        //on prend l'adversaire le plus proche.
        for(AiHero h:heroes)
        {ai.checkInterruption();
        dist=tile.getZone().getTileDistance(h, tile.getZone().getOwnHero());
        if(dist<distm)
        	distm=dist;
        adversaire=h;
        }
     
        
       
        AiTile atl=adversaire.getTile();
        //on prend notre distance a l'adversaire
         int longeur= tile.getZone().getTileDistance(atl,tile);
        if (longeur<4)
            result=true;
   
        return result;
    }
}
