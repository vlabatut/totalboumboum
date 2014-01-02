package org.totalboumboum.ai.v201314.ais.saylamsonmez.v4;


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
import org.totalboumboum.ai.v201314.ais.saylamsonmez.v4.Agent;


/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	

	/** zone de jeu */
	AiZone zone;
	/** pour acceder aux methodes de BlockingHandler */
	BlockingHandler blockingHandler;
	/** pour acceder aux methodes de TileCalculationHandler */
	TileCalculationHandler tileCalculationHandler;
	/** pour acceder aux methodes de EnemyHandler */
	EnemyHandler enemyHandler;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
		zone=ai.getZone();
		

	}
	
	/**
	 * Initialisation de gestionnaire
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected void initHandler(Agent ai){
		ai.checkInterruption();
		this.ai=ai;
		
		blockingHandler = ai.blockingHandler;
		tileCalculationHandler = ai.tileCalculationHandler;
		enemyHandler = ai.enemyHandler;
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
	@Override
	protected Set<AiTile> selectTiles()
	{	
		ai.checkInterruption();
	
	Set<AiTile> selectedTiles = new HashSet<AiTile>();
	AiModeHandler<Agent> modeHandler = ai.getModeHandler();
	AiMode mode = modeHandler.getMode();
	
	
	if(mode==AiMode.COLLECTING)
	{
		selectedTiles.addAll(tileCalculationHandler.getReachableTiles(ai.getZone().getOwnHero().getTile()));
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
			
			AiHero hero=enemyHandler.selectEnemy();

			
			if(!ai.getZone().getRemainingOpponents().isEmpty())
			{
		
			selectedTiles.addAll(tileCalculationHandler.getReachableTiles(zone.getOwnHero().getTile()));
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
	private static String BLOCK_ENEMY = "BlockEnemy";
	/** Nom de la 2ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String ITEM_VISIBLE = "ItemVisible";
	/** Nom de la 3ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String ITEM_HIDDEN = "ItemHidden";
	/** Nom de la 4ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String MALUS_VISIBLE = "MalusVisible";
	/** Nom de la 5ème catégorie (doit être similaire à celui défini dans le fichier XML) */
	private static String ENEMY_INACCESSIBLE = "EnemyInaccessible";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();
		AiCategory result = null;
	
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		// pour le mode attaque, on avait défini 3 catégorie possible dans le fichier XML de préférences
		if(mode==AiMode.ATTACKING)
			if(ai.getZone().getOwnHero().getBombNumberMax()==0)
				result=getCategory(MALUS_VISIBLE);
			else if(blockingHandler.isBlockingEnemy(tile))
				result = getCategory(BLOCK_ENEMY);
			else
				result=getCategory(ENEMY_INACCESSIBLE);
		// pour le mode collecte, on avait le choix entre 3 categories differentes
		else
		{	
			
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
						result=getCategory(ITEM_VISIBLE);
					}
					if(!visibleCheck)
						result=getCategory(MALUS_VISIBLE);	
				}
				
			else
			{
				result = getCategory(ITEM_HIDDEN);
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
