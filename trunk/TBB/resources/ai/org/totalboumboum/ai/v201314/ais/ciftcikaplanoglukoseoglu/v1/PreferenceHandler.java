package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v1;


import java.util.List;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;

import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 

 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
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
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
	
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();
		
		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	 /**
     * Nous permet de définir notre méthode de sélection des cases.
     *

     * @return result
     *  renvoie la liste des cases qui ne contiennent ni de bombes ni de murs.
     * qui sont traversable par notre agent.
     */
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		
		
		
		
		
		List<AiTile> tiles= ai.getZone().getTiles();
		
		
		
		//On sélectionne les cases vides de la zone et on les ajoute a la liste qui est renvoyée.
		for(AiTile t:tiles)
		{ai.checkInterruption();
			if(t.getBombs().isEmpty() && t.getBlocks().isEmpty())
			{
				result.add(t);
				
			}
		}
		
		
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "CASE_VIDE";
	
	

	 /**
     * Définit le traitement de la case par rapport a nos catégorie.
     *
     * @param tile
     * 		la case concernée.
     * @return result
     *  renvoie un String,le nom de la catégorie, qui permet d'associer la case a une catégorie. 
     * 
     */
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();

		AiCategory result = null;
		
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		/* pour le mode attaque, on n'a défini qu'une seule catégorie: La case vide qui ne contient
		ni bombe ni mur.*/
		if(mode==AiMode.ATTACKING)
		 if(tile.getBlocks().isEmpty() && tile.getBombs().isEmpty())
			result = getCategory(CAT_NAME_1);
		
		//pas de mode collecte pour l'instant.

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
