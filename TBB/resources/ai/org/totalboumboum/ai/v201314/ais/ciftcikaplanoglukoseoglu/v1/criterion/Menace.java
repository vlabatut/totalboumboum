package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v1.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v1.Agent;


import java.util.List;
import java.util.Map;

/**
 * Cette classe va nous permettre de traiter une case par rapport au
 * critere binaire "Menace".
 *
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class Menace extends AiCriterionBoolean<Agent>
{	/** on nomme le critère */
public static final String NAME = "MENACE";

    /**
     * Crée le critère binaire Menace.
     *
     * @param ai
     * 		l'agent concerné.
     */
    public Menace(Agent ai)
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
     * * @return result
     *  renvoie la valeur booléene qui est obtenu par le traitement.
     * Si la bombe est sur le point d'exploser, la valeur va devenir vrai.
     *  Sinon elle est fausse. 
     */
    @Override
    public Boolean processValue(AiTile tile)
    {	ai.checkInterruption();
        AiHero ownHero= tile.getZone().getOwnHero();
        boolean result = false;
        //On utilisera la distance de l'agent plus tard.
       // AiTile owntile=ownHero.getTile();
        
       
   Map<Long, List<AiBomb>>  chainebombs=tile.getZone().getBombsByDelays();
          for(Long b: chainebombs.keySet()){
        	  ai.checkInterruption();
        	  //on prend le temps des bombes en prenant compte des bombes enchainées.
        	  List<AiBomb> tumbomba=chainebombs.get(b);
        	  
        	  for(AiBomb k:tumbomba){
              	   ai.checkInterruption();
                       List<AiTile>   blasts=k.getBlast();
                       	/*on applique la formule qui permet notre fuite a retardement. 
                       Normalement on rajoute +1 car getRange() ne prend pas la position de la bombe.
                        On ne prend pas de risques et on rajoute 2 */
                          float formul=(float)((k.getRange()+2)/(ownHero.getWalkingSpeed()/(AiTile.getSize())))*1000 +k.getLatencyDuration();
                          //on compare le temps de la bombe avec le temps limite que l'on a trouvé avec la formule.
                          	if((formul>= b && blasts.contains(tile))){

                          		result=true;

                           }
                
                 }  
          }
          
         

 

        return result;
    }
}
