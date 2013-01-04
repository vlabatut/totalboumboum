package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion;
 
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
 
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.CiplakErakyol;
 
/**
 *   Critere de pertinence l'adversaire.
 * 
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionPertinenceAdv extends AiUtilityCriterionBoolean<CiplakErakyol>
{    /** Nom de ce critère */
    public static final String NAME = "PertinentAdv";
     
    /**
     * @param ai
     *         l'agent concerné. 
     * 
     * @throws StopRequestException     
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    public CriterionPertinenceAdv(CiplakErakyol ai) throws StopRequestException
    {    super(ai, NAME); 
        ai.checkInterruption();
         
        this.ai=ai;
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
   //protected CiplakErakyol ai;
    /////////////////////////////////////////////////////////////////
    // PROCESS                    /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Boolean processValue(AiTile tile) throws StopRequestException
    {    
    	ai.checkInterruption();
      	boolean result = false;

    	AiZone zone = ai.getZone();
    	AiHero ajan = zone.getOwnHero();
    	int outremerGuc = ajan.getBombNumberMax()*50+ajan.getBombRange()*70; //notre force
    	int rakipGuc = 0;
    	
    	Set<AiHero> opponentL = new TreeSet<AiHero>();
    	List<AiTile> tileNL = tile.getNeighbors();

    	List<AiHero> remainingOp = zone.getRemainingHeroes();

    	for(int i = 0;i<tileNL.size();i++)
    	{
    		ai.checkInterruption();

    		for(int j = 0;j<tileNL.get(i).getHeroes().size();j++)
    		{
    			ai.checkInterruption();
    			if(remainingOp.contains(tileNL.get(i).getHeroes().get(j)))
    				opponentL.add(tileNL.get(i).getHeroes().get(j));
    		}
    	}
    	
    	Iterator<AiHero> heroIt = opponentL.iterator();

    	while(heroIt.hasNext())
    	{
    		ai.checkInterruption();

    		AiHero opponent = heroIt.next();
    		rakipGuc = opponent.getBombNumberMax()*50+opponent.getBombRange()*70;
		
    		if(outremerGuc >= rakipGuc)
    			result = true;
    	}
    	return result;
         
    }
} 