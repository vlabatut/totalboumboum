package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.criterion;
/*
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
*/
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2.CiplakErakyol;
import org.totalboumboum.engine.content.feature.Direction;

/**
 *Cette criteria est pour trouver les nombre des murs destructibles
 *
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionNombredeMur extends AiUtilityCriterionInteger<CiplakErakyol>
{   
	/** Nom de ce critère */
    public static final String NAME = "NombreDesMurs";
    /**
	 * une seul mur
	 */
    public static final int bir = 1; 
    /**
	 * deux murs
	 */
    public static final int iki = 2; 
    /**
	 * il y a trois murs
	 */
    public static final int uc= 3; 
    /**
	 * il n'y a pas de mur
	 */
    public static final int sifir = 0; 
  
    /**
     * 
     *
     * @param ai
     *         l'agent concerné.
     *
     * @throws StopRequestException   
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    public CriterionNombredeMur(CiplakErakyol ai) throws StopRequestException
    {    super(ai,NAME,0,3);
        ai.checkInterruption();
    }

    /////////////////////////////////////////////////////////////////
    // PROCESS                    /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile) throws StopRequestException
    {    ai.checkInterruption();
   
        int arttir =sifir, sonuc=sifir; 
       
        for( AiBlock currentBlock: tile.getNeighbor(Direction.UP).getBlocks())
        {
            ai.checkInterruption();
            if(currentBlock.isDestructible()) arttir++;
        }
        for( AiBlock currentBlock: tile.getNeighbor(Direction.DOWN).getBlocks())
        {
            ai.checkInterruption();
            if(currentBlock.isDestructible()) arttir++;
        }
        for( AiBlock currentBlock: tile.getNeighbor(Direction.LEFT).getBlocks())
        {
            ai.checkInterruption();
            if(currentBlock.isDestructible()) arttir++;
        }
        for( AiBlock currentBlock: tile.getNeighbor(Direction.RIGHT).getBlocks())
        {
            ai.checkInterruption();
            if(currentBlock.isDestructible()) arttir++;
        }
       
        if ( arttir == sifir )
            sonuc = bir;
        else if ( arttir == bir || arttir == iki )
            sonuc=iki;
        else if ( arttir == uc ) 
        	sonuc=uc;
       
        return sonuc;
    }
}