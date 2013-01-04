package org.totalboumboum.ai.v201213.ais.ciplakerakyol.v2;
 
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
        verbose = true;
         
    }
 
    /////////////////////////////////////////////////////////////////
    // PROCESSING                /////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    @Override
    protected boolean hasEnoughItems() throws StopRequestException
    {    ai.checkInterruption();

    AiZone oyunAlani =ai.getZone(); //la zone de jeu
    AiHero ajan = oyunAlani.getOwnHero(); //notre agent
    int bombaSayisi = ajan.getBombNumberMax(); // le nombre des bombes de notre agent
    int bombaMenzili = ajan.getBombRange(); //La portée des bombes que notre agent peux posser. 
    if(bombaSayisi < 3 && bombaMenzili < 2)
        //If the bomb number is less than 3 and bomb range is less than 2, 
        //it means our agent is in collection mode.
        return false; //mode collecte
    else
        return true;  //mode attaque
    }
     
    @Override
    protected boolean isCollectPossible() throws StopRequestException
    {    
    ai.checkInterruption();

    boolean sonuc=false;
    AiZone oyunAlani =ai.getZone(); //prend la zone current
    //AiHero ajan = oyunAlani.getOwnHero(); //l'agent
    List<AiItem> item=oyunAlani.getItems(); //les nombre des tous items 
     
    int bonusSayisi = item.size();
    int gizliBonus = oyunAlani.getHiddenItemsCount(); //les items caches
    int toplamZaman = (int) oyunAlani.getTotalTime(); //totaltemp
    int zamanLimiti = (int) (oyunAlani.getLimitTime()/4); 
    
    if(toplamZaman>=zamanLimiti)
    	sonuc= false;
		// s'il n'y a pas des items caches et des items ouverts dans la zone
		//l'agent ne peux pas ramasser
	else if(bonusSayisi>0 || gizliBonus>0)
		sonuc= true;
 
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