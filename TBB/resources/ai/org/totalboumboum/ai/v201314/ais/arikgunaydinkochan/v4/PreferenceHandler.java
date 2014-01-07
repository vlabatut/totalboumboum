package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails..
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	 
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();	
	}
		
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();			
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
	}
	
	
	/**
	 * 
	 *  
	 *  
	 *  */
	private final double SPEED_LIMIT = 180;
	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		
		ArrayList<AiTile> accessibleTiles1=ai.accessibleTiles;

		result.addAll(accessibleTiles1);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String ATTACK= "Attack";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String RECHERCHE_ITEMS = "RechercheItems";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String COLLECT_ITEMS = "CollectItems";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		if(mode==AiMode.ATTACKING)
		//pour le mode collecte, on avait le choix entre 2 categories differentes
		{
			result = getCategory(ATTACK);
		}
		else
		{	
			AiZone zone = ai.getZone() ;
		
			int itemsNumber = zone.getItems().size();
			
			if(itemsNumber>0){
				AiHero myHero = zone.getOwnHero();
				int mybombs 	 = myHero.getBombNumberMax();
				int mybombsRange = myHero.getBombRange();
				double mySpeed 	 = myHero.getWalkingSpeed();
				
				boolean needOfExtraBomb  = (mybombs < 3);
				boolean needOfExtraFlame = (mybombsRange < 4);
				boolean needOfExtraSpeed = (mySpeed < SPEED_LIMIT );
				if((needOfExtraSpeed  &&(ai.itemVisible(AiItemType.EXTRA_SPEED)||ai.itemVisible(AiItemType.GOLDEN_SPEED)))
				 ||(needOfExtraFlame &&(ai.itemVisible(AiItemType.EXTRA_FLAME)||ai.itemVisible(AiItemType.GOLDEN_FLAME)))
				 ||(needOfExtraBomb &&(ai.itemVisible(AiItemType.EXTRA_BOMB)||ai.itemVisible(AiItemType.GOLDEN_BOMB)))){
					result = getCategory(COLLECT_ITEMS);
				}
				else
					result = getCategory(RECHERCHE_ITEMS);
			}
			else
				result = getCategory(RECHERCHE_ITEMS);		
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();	
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();	
	}
}
