package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2;

//import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
//import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 *
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/**
	 * Construit un gestionnaire de préférence pour l'agent passé en paramètre.
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
	 * Réinitialise les structures de données modifiées à chaque itération. 
	 */
	@Override
	protected void resetCustomData()
	{	
		ai.checkInterruption();
		// cf. la Javadoc dans AiPreferenceHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Fait une séléction des cases
	 * 
	 * @return
	 * 		renvoie un ensemble de cases
	 */
	@Override
	protected Set<AiTile> selectTiles()
	{	
		ai.checkInterruption();	
	
		Set<AiTile> result = new TreeSet<AiTile>();			
		List<AiHero> herosList = ai.getZone().getHeroes();
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		
		
		if(mode==AiMode.ATTACKING)
		{
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
		}
		else
		{
			for(AiItem item : ai.getZone().getItems())
			{
				ai.checkInterruption();	
				for(AiTile tile : item.getTile().getNeighbors())
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
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_3 = "MODE_COLLECTE";
	
	
	
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
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();		
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		AiTile currentTile = ownHero.getTile();

		if(mode==AiMode.ATTACKING)
		{
			if(ai.getTimeLeft(currentTile)<=2)
			{
				result = getCategory(CAT_NAME_2);
			}
			else
			{
				result = getCategory(CAT_NAME_1);
			}
		}
		else
		{
			if(ai.getTimeLeft(currentTile)<=2)
			{
				result = getCategory(CAT_NAME_2);
			}
			else
			{
				result = getCategory(CAT_NAME_3);
			}
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
