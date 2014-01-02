package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v3.Agent;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * Cf. la documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/** zone */
	AiZone zone;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	
		super(ai);
		ai.checkInterruption();
		zone=ai.getZone();
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
	
	Set<AiTile> selectedTiles = new HashSet<AiTile>();
	AiModeHandler<Agent> modeHandler = ai.getModeHandler();
	AiMode mode = modeHandler.getMode();

	if(mode==AiMode.COLLECTING)
	{
		selectedTiles.addAll(ai.getReachableTiles(ai.getZone().getOwnHero().getTile()));
		for(AiItem item:zone.getItems())
		{
			ai.checkInterruption();
			if(item.getType().equals(AiItemType.EXTRA_BOMB) ||
					item.getType().equals(AiItemType.EXTRA_FLAME) ||
					item.getType().equals(AiItemType.EXTRA_SPEED) ||
					item.getType().equals(AiItemType.GOLDEN_BOMB) ||
					item.getType().equals(AiItemType.GOLDEN_FLAME) ||
					item.getType().equals(AiItemType.GOLDEN_SPEED) ||
					item.getType().equals(AiItemType.RANDOM_EXTRA))
			{
				if(!selectedTiles.contains(item.getTile()))
					selectedTiles.add(item.getTile());
			}
		}
	}
	else
	{
		Enemy e = new Enemy(ai);
			AiHero hero=e.selectEnemy();
			if(!ai.getZone().getRemainingOpponents().isEmpty())
			{
				//print("***********************************select tiles"+ tileCalculationHandler.getReachableTiles(zone.getOwnHero().getTile()));
			selectedTiles.addAll(ai.getReachableTiles(zone.getOwnHero().getTile()));
			selectedTiles.add(hero.getTile());
			for(AiTile neighbor : hero.getTile().getNeighbors())
			{
				ai.checkInterruption();
				if(neighbor.isCrossableBy(ai.getZone().getOwnHero()))
				{
					selectedTiles.add(neighbor);
				}
			}
			}
					
	}
	return selectedTiles;
	}
	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nom de la 1ère catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_1 = "BlockEnemy";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_2 = "ItemVisible";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_3 = "ItemHidden";
	/** Nom de la 4ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_4 = "MalusVisible";
	/** Nom de la 5ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String CAT_NAME_5 = "EnemyInaccessible";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
		//AiTile enemyTile=null;
		//EnemyHandler e = new EnemyHandler(ai);
		
		//AiHero enemy = e.SelectEnemy();
		/*if(!ai.getZone().getRemainingOpponents().isEmpty())
			enemyTile=enemy.getTile();*/
		
	
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		// pour le mode attaque, on n'avait défini qu'une seule catégorie possible dans le fichier XML de préférences
		if(mode==AiMode.ATTACKING)
			if(ai.getZone().getOwnHero().getBombNumberMax()==0)
				result=getCategory(CAT_NAME_4);
			else if(ai.isBlockingEnemy(tile))
				result = getCategory(CAT_NAME_1);
			else
				result=getCategory(CAT_NAME_5);
		// pour le mode collecte, on avait le choix entre 2 categories differentes
		else
		{	// par exemple ici, on utilise la présence d'un item pour déterminer le cas
			// là encore, cela n'a pas de sens en termes de conception de l'agent,
			// il s'agit seulement d'illustrer comme le code source peut être écrit
			//AiZone zone = ai.getZone();
			//AiHero ownHero = zone.getOwnHero();
			//AiTile currentTile = ownHero.getTile();
			List<AiItem> items = tile.getItems();
			boolean visibleCheck=false;
			if(!items.isEmpty())
				for(AiItem item:tile.getItems())
				{
					ai.checkInterruption();
					if(item.getType().equals(AiItemType.EXTRA_BOMB) ||
							item.getType().equals(AiItemType.EXTRA_FLAME) ||
							item.getType().equals(AiItemType.EXTRA_SPEED) ||
							item.getType().equals(AiItemType.GOLDEN_BOMB) ||
							item.getType().equals(AiItemType.GOLDEN_FLAME) ||
							item.getType().equals(AiItemType.GOLDEN_SPEED) ||
							item.getType().equals(AiItemType.RANDOM_EXTRA))
					{
						visibleCheck=true;
						result=getCategory(CAT_NAME_2);
					}
					if(!visibleCheck)
						result=getCategory(CAT_NAME_4);	
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
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		// pour ne pas afficher la préférence des cases non-sélectionnées
		outputWorstPref = false;
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		

	}
}
