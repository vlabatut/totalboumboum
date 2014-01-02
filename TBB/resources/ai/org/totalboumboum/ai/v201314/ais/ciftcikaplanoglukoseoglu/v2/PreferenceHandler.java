package org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.criterion.DistanceEnemy;
import org.totalboumboum.ai.v201314.ais.ciftcikaplanoglukoseoglu.v2.criterion.Threat;
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
	 * la fonction récursive qui nous permet de prendre les cases accessibles.
	 * 
	 * @param sourceTile
	 *            la case concernée(normalement la case qui contient notre
	 *            agent).
	 */
	public void fillAccessibleTilesBy(AiTile sourceTile) {
		ai.checkInterruption();

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

		Set<AiTile> result = new TreeSet<AiTile>();
		AiTile herotile = ai.getZone().getOwnHero().getTile();
		this.accessibleTiles = new ArrayList<AiTile>();
		this.accessibleTiles.clear();
		fillAccessibleTilesBy(herotile);
		result.addAll(accessibleTiles);
		DistanceEnemy de = new DistanceEnemy(ai);
		adversaire = de.closesttarget();
		if (adversaire==null)
			adversaire=ai.getZone().getOwnHero();
		Threat th = (Threat) getCriterion("THREAT");
		
			if (!accessibleTiles.contains(adversaire.getTile())
					&& th.fetchValue(herotile) == 0) {
				result.add(adversaire.getTile());
				flag = true;
			}

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
		if (mode == AiMode.ATTACKING || mode == AiMode.COLLECTING)
			
				if (tile.isCrossableBy(ai.getZone().getOwnHero())
						|| tile.equals(adversaire.getTile()))
					result = getCategory(CAT_NAME_1);
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
