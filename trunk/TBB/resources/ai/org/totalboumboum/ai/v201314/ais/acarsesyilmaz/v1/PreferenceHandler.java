package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v1;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
/*
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
*/
/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
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
		verbose = false;		
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne
	 * seulement les structures de données créées par le concepteur : les structures imposées sont 
	 * réinitialisées.
	 */
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();
		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Effectue une sélection sur les cases de la zone de jeu. 
	 * 
	 * @return
	 * 		L'ensemble des cases dont on veut calculer la préférence.
	 */
	@Override
	protected Set<AiTile> selectTiles()
	{	
		ai.checkInterruption();	
	
		Set<AiTile> result = new TreeSet<AiTile>();			
		List<AiHero> herosList = ai.getZone().getHeroes();
		
		
		for(AiHero hero : herosList)
		{		
			ai.checkInterruption();	
			for(AiTile tile : hero.getTile().getNeighbors())
			{	
				ai.checkInterruption();			
				if(tile.isCrossableBy(ai.getZone().getOwnHero()) && ai.getTimeLeft(tile) > 1350)
				{
				result.add(tile);
				}				
				
				for(AiTile tile2 : tile.getNeighbors())
				{
					ai.checkInterruption();			
					if(tile2.isCrossableBy(ai.getZone().getOwnHero()) && ai.getTimeLeft(tile2) > 1350)
					{
					result.add(tile2);
					}
				}
			}	
		}
		
		return result;
		
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "MODE_ATTAQUE";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "MODE_DEFENSE";
	
	/**
	 * Cette méthode prend une case en paramètre, et identifie la catégorie correspondante. 
	 * Bien sûr, le traitement dépend à la fois de la zone de jeu et du mode courant.
	 * 
	 * @param tile
	 * 		La case dont on veut identifier la catégorie.
	 * 
	 * @return
	 * 		Un objet représentant la catégorie correspondant à cette case.
	 */
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		
		
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		AiTile currentTile = ownHero.getTile();

		
		if(ai.getTimeLeft(currentTile)<=2)
		{
			result = getCategory(CAT_NAME_2);
		}
		else
		{
			result = getCategory(CAT_NAME_1);
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire.
	 */
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
	}
}
