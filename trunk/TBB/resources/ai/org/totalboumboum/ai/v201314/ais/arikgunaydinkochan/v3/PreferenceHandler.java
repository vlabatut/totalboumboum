package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails..
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
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
	private static String CAT_NAME_1 = "CATEGORY1";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "CATEGORY2";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	//private static String CAT_NAME_3 = "THIRD_CATEGORY";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		if(mode==AiMode.ATTACKING)
		//pour le mode collecte, on avait le choix entre 2 categories differentes
		{
			result = getCategory(CAT_NAME_1);
		}
		else
		{	
			result = getCategory(CAT_NAME_2);
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
