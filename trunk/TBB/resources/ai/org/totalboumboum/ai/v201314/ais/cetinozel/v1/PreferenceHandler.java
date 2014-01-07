package org.totalboumboum.ai.v201314.ais.cetinozel.v1;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**

 
 * C'est la class de notre PreferenceHandler
 * 
 * @author Hakan Çetin
 * @author Yiğit Özel
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

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		

	
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
		
		// ici, à titre d'exemple, on sélectionne aléatoirement 10 cases
		// bien sûr ceci n'a pas de sens au niveau de la conception d'un agent
		// c'est seulement pour illustrer cette étape du traitement en termes de code source.
		List<AiTile> tiles = ai.getZone().getTiles();
		
		result.addAll(tiles);
		

	
		
		
		
		/*tiles = new ArrayList<AiTile>(tiles);
		Collections.shuffle(tiles);
		result.addAll(tiles.subList(0,Math.min(tiles.size(),10)));*/
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "EffetDomino";

	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = getCategory(CAT_NAME_1);

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
