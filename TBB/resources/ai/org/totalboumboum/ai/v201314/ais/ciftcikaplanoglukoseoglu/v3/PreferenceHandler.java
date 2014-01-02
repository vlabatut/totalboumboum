package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Distance;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.DistanceEnemy;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v3.criterion.Threat;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent. Cf. la
 * documentation de {@link AiPreferenceHandler} pour plus de détails.
 * 
 * 
 * 
 * @author Özkan Çiftçi
 * @author Akın Kaplanoğlu
 * @author Erol Köseoğlu
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent> {

	/**
	 * Les cases qui sont accessibles a notre agent.
	 */
	public ArrayList<AiTile> accessibleTiles;

	/**
	 * la valeur booléene qui nous permet de comprendre si l'adversaire est
	 * accessible.
	 */
	public boolean flag;
	/**
	 * la valeur booléene qui nous permet de comprendre si l'adversaire est
	 * accessible et notre case est menacee par un bombe.
	 */
	
	public boolean flag1;
	/**
	 * l'adversaire le plus proche
	 */

	public AiHero adversaire;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = true;

	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() {
		ai.checkInterruption();

	}
	
	/**
	 * la liste qui nous permet de trouver les cases pour nous enfuir
	 * 
	 */

	public ArrayList<AiTile> escape=new ArrayList<AiTile>();
	
	/**
	 * la liste qui nous permet de trouver les cases pour nous enfuir 
	 * et cette case est menaceé par une bombe.
	 */
	
	public ArrayList<AiTile> escape1=new ArrayList<AiTile>();

	/**
	 * L'objet qui nous permet de prendre la valeur du critere distance.
	 */
	
	public Distance d=new Distance(ai);
	/**
	 * L'objet qui nous permet de prendre la valeur du critere threat.
	 */
	
	public Threat th=new Threat(ai);
	/**
	 * la fonction récursive qui nous permet de prendre les cases accessibles.
	 * 
	 * @param sourceTile
	 *            la case concernée(normalement la case qui contient notre
	 *            agent).
	 */
	public void fillAccessibleTilesBy(AiTile sourceTile) {
		ai.checkInterruption();
if(d.fetchValue(sourceTile))
	if(th.fetchValue(sourceTile)==0)
		escape.add(sourceTile);
		else if(th.fetchValue(sourceTile)==1)
			escape1.add(sourceTile);


		AiHero hero = ai.getZone().getOwnHero();
		if (sourceTile.isCrossableBy(hero)) {
			this.accessibleTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Nous permet de définir notre méthode de sélection des cases.
	 * 
	 * 
	 * @return result renvoie la liste des cases qui ne contiennent ni de bombes
	 *         ni de murs. qui sont traversable par notre agent.
	 */
	@Override
	protected Set<AiTile> selectTiles() {
		ai.checkInterruption();
		flag = false;
		flag1=false;
		escape.clear();escape1.clear();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile herotile = ai.getZone().getOwnHero().getTile();
		this.accessibleTiles = new ArrayList<AiTile>();
		this.accessibleTiles.clear();
		fillAccessibleTilesBy(herotile);
		result.addAll(accessibleTiles);
		DistanceEnemy de = new DistanceEnemy(ai);
		List<AiHero> tokill=ai.getZone().getRemainingOpponents();
		adversaire = de.closesttarget();
		if (adversaire==null)
			adversaire=ai.getZone().getOwnHero();
		int count=0;
	
	
		for(AiHero h:tokill){
			ai.checkInterruption();
			if (accessibleTiles.contains(h.getTile()))
		 count++;
			}
			
			if(count==0)
				{if(th.fetchValue(herotile) == 0 )
				{result.add(adversaire.getTile());
				flag = true;
			
				}
		
	else if(th.fetchValue(herotile) == 1)
		flag1=true;}
	
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CATEGORY /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Nom de la 1ère catégorie (doit être similaire à celui défini dans le
	 * fichier XML)
	 */
	private static String CAT_NAME_1 = "CROSSABLE_TILE";
	/**
	 * Nom de la 2ème catégorie
	 */
	private static String CAT_NAME_2 = "COMINGTOGETYOU";
	
	/**
	 * Nom de la 3ème catégorie
	 */
	private static String CAT_NAME_3 = "ITEM_TILE";

	/**
	 * Définit le traitement de la case par rapport a nos catégorie.
	 * 
	 * @param tile
	 *            la case concernée.
	 * @return result renvoie un String,le nom de la catégorie, qui permet
	 *         d'associer la case a une catégorie.
	 * 
	 */
	@Override
	protected AiCategory identifyCategory(AiTile tile) {
		ai.checkInterruption();
		
		AiCategory result = null;

		AiModeHandler<Agent> modeHandler = ai.getModeHandler();
		AiMode mode = modeHandler.getMode();
		/*
		 * 1ere categorie: La case vide qui ne contient ni bombe ni mur.
		 */
		if (mode == AiMode.ATTACKING)
			
				if (tile.isCrossableBy(ai.getZone().getOwnHero())
						|| tile.equals(adversaire.getTile()))
					result = getCategory(CAT_NAME_1);
		
		if (mode == AiMode.COLLECTING){
			if ( !tile.getItems().isEmpty() )
			{
				
				for(AiItem item: tile.getItems())
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
//						Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
//						criteria.add(criterionMap.get(Distance.NAME));
//						criteria.add( criterionMap.get(Danger.NAME) );
//						criteria.add( criterionMap.get(Necessity.NAME) );
//						criteria.add( criterionMap.get(Concurrence.NAME) );
//						result = new AiUtilityCase(ai, BONUS_VISIBLE, criteria);
						result = getCategory(CAT_NAME_3);
						
					}}}
			else{
				result=getCategory("CAT_NAME_2");
			}
		
		
		}
		
			
		/*
		 * 2eme categorie: La case qui contient l'adversaire le plus proche dans
		 * le cas ou ce dernier est inacessible.
		 */
		
			if (!accessibleTiles.contains(adversaire.getTile())) {
				if (tile.equals(adversaire.getTile()))
					result = getCategory(CAT_NAME_2);

			}

		// pas de mode collecte pour l'instant.

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();

	}
}
