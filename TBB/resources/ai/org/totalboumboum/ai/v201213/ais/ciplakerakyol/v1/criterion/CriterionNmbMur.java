package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.ciplakerakyol.v1.CiplakErakyol;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est un simple exemple de
 * critère chaîne de caractères. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que le domaine
 * de définition est spécifiés dans l'appel au constructeur
 * ({@code super(nom,inf,sup)}).
 *
 * @author Hazal Çıplak
 * @author Şebnem Erakyol
 */
public class CriterionNmbMur extends AiUtilityCriterionInteger<CiplakErakyol>
{    /** Nom de ce critère */
    public static final String NAME = "Nombre De Murs";
    /** */
    public static final int alt = 1; //nombre de min mur destructible
    /** */
    public static final int ortanca = 2; //nombre moyen murs destrublibles
    /** */
    public static final int ust = 3; //nombre top(max) murs destructibles
    /** */
    public static final int bos = 0; //vide
   
   
    /** Domaine de définition */
/*    public static final Set<String> DOMAIN = new TreeSet<String>(Arrays.asList
        (    alt,
            ortanca,
            ust,
            bos
           
    ));*/
   
    /**
     * Crée un nouveau critère entier.
     *
     * @param ai
     *         l'agent concerné.
     *
     * @throws StopRequestException   
     *         Au cas où le moteur demande la terminaison de l'agent.
     */
    public CriterionNmbMur(CiplakErakyol ai) throws StopRequestException
    {    super(ai,NAME,1,3);
        ai.checkInterruption();
    }
   

    /////////////////////////////////////////////////////////////////
    // PROCESS                    /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    public Integer processValue(AiTile tile) throws StopRequestException
    {    ai.checkInterruption();
   
        int arttir = bos, sonuc=bos; //sonuc est le resultat
       
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
       
        if ( arttir == bos )
            sonuc = alt;
        else if ( arttir == alt || arttir == ortanca )
            sonuc=ortanca;
        else if ( arttir == ust ) sonuc=ust;
       
        return sonuc;
    }
}