package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
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
	/**
	 * Réinitialise les structures de données modifiées à chaque itération. Cette méthode concerne
	 * seulement les structures de données créées par le concepteur : les structures imposées sont 
	 * réinitialisées.
	 */
	@Override
	protected void resetCustomData()
	{	
		ai.checkInterruption();
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
		
		AiHero ownHero = ai.getZone().getOwnHero();
		AiTile ownTile = ownHero.getTile();

		if(!ai.securityHandler.isEnemyReachable())	
		{
			if(ai.getModeHandler().getMode() == AiMode.COLLECTING || ai.securityHandler.isDangerAround())
			{
				ai.securityHandler.updateAccessibleTiles();
				result.addAll(ai.securityHandler.getAccessibleTiles(ownTile));	
			}
			else
			{
				for(AiHero enemy : ai.getZone().getRemainingOpponents())
				{
					ai.checkInterruption();
					if(!result.contains(enemy.getTile()))
					{					
						result.add(enemy.getTile());					
					}
					for(AiTile neighbour : enemy.getTile().getNeighbors())
					{
						ai.checkInterruption();
						if(!result.contains(neighbour) && neighbour.isCrossableBy(ownHero))
						{							
							result.add(neighbour);							
						}
					}			
				}
			}
		}	
		else
		{
			ai.securityHandler.updateAccessibleTiles();
			result.addAll(ai.securityHandler.getAccessibleTiles(ownTile));			
		}
		
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	

	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "MODE_ATTACK";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "MODE_COLLECT";	
		
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
	{	
		ai.checkInterruption();
		AiCategory result = null;	

		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
				
		
		if(mode==AiMode.ATTACKING)
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
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant les données de ce gestionnaire.
	 */
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		super.outputWorstPref = false;
		super.updateOutput();		
	}
}
