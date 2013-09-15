package org.totalboumboum.ai.v201213.ais.saglamseven.v4;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterATTPertinence;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterDistance;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterMenace;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterPertinence;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterMurDest;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterSecure;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.CriterTemps;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.BonusCriter;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion.MalusCriter;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Esra Sa?lam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<SaglamSeven> {

	/**
	 * 
	 */
	private final int NEAR_ENEMY_HERO_COUNT_LIMIT = 1;

	/**
	 * @param ai
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	protected UtilityHandler(SaglamSeven ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		// à compléter
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException {
		ai.checkInterruption();

		// à surcharger si nécessaire, pour réinitialiser certaines
		// structures de données à chaque itération

		// cf. la Javadoc dans AiUtilityHandler pour une description de la
		// méthode
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiZone zone = ai.getZone();
		AiHero ownHero = zone.getOwnHero();
		List<AiTile> listTiles = zone.getTiles();
		AiTile tile = null;

		if (this.ai.getModeHandler().getMode() == AiMode.ATTACKING) {

			AiHero ourhero = this.ai.getZone().getOwnHero();
			AiTile ourtile = ourhero.getTile();
			// Set<AiTile> result = new TreeSet<AiTile>();
			AiTile aitile = ourtile;

			Queue<AiTile> qe = new LinkedList<AiTile>();
			qe.add(aitile);
			while (!qe.isEmpty()) {
				ai.checkInterruption();
				aitile = qe.poll();
				for (Direction direction : Direction.getPrimaryValues()) {
					ai.checkInterruption();
					if (aitile.getNeighbor(direction).getBombs().isEmpty()
							&& aitile.getNeighbor(direction).getBlocks()
									.isEmpty()
							&& !qe.contains(aitile.getNeighbor(direction))
							&& !result.contains(aitile.getNeighbor(direction))
							&& !aitile.getNeighbor(direction).equals(ourtile)) {
						qe.add(aitile.getNeighbor(direction));
					}
				}
				if (!qe.isEmpty()) {

					aitile = qe.peek();

					result.add(aitile);

				} else {
					break;
				}
			}

			result.add(ourtile);

		} else {
			for (int i = 0; i < listTiles.size(); i++) {
				ai.checkInterruption();
				tile = listTiles.get(i);
				if (tile.isCrossableBy(ownHero, false, false, false, false,
						true, true)) {
					result.add(tile);
				}
			}
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		new BonusCriter(ai);
		new CriterDistance(ai);
		new CriterMenace(ai);
		new CriterMurDest(ai);
		new CriterPertinence(ai);
		new CriterTemps(ai);
		new MalusCriter(ai);
		new CriterATTPertinence(ai);
		new CriterSecure(ai);

	}

	// ///////////////////////////////////////////////////////////////
	// CASE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	private final String ItemVisibleBONUS = "ItemVisibleBONUS";

	/**
	 * 
	 */
	private final String PresenceUnEnnemi = "PresenceUnEnnemi";

	/**
	 * 
	 */
	private final String MurDestructibles = "MurDestructible";

	/**
	 * 
	 */
	private final String MurDestAtt = "MurDestAtt";

	/**
	 * 
	 */
	private final String ItemVisibleMALUS = "ItemVisibleMALUS";

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();
		Set<AiUtilityCriterion<?, ?>> criteria;
		// ******* Mode Collecte**********
		// on définit un premier cas utilisant les deux premiers critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(CriterMenace.NAME));
		criteria.add(criterionMap.get(CriterPertinence.NAME));
		// criteria.add(criterionMap.get(CriterPertinence.NAME));
		criteria.add(criterionMap.get(CriterTemps.NAME));
		criteria.add(criterionMap.get(CriterDistance.NAME));
		AiUtilityCase caseItemVisibleBonus = new AiUtilityCase(ai,ItemVisibleBONUS, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(MalusCriter.NAME));
		AiUtilityCase caseItemVisibleMalus = new AiUtilityCase(ai,ItemVisibleMALUS, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(CriterMenace.NAME));
		criteria.add(criterionMap.get(CriterMurDest.NAME));
		criteria.add(criterionMap.get(CriterDistance.NAME));
		AiUtilityCase caseMurDestructibles = new AiUtilityCase(ai,MurDestructibles, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(CriterSecure.NAME));
		criteria.add(criterionMap.get(CriterATTPertinence.NAME));
		AiUtilityCase casePresenceUnEnnemi = new AiUtilityCase(ai,PresenceUnEnnemi, criteria);

		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(CriterSecure.NAME));
		criteria.add(criterionMap.get(CriterMurDest.NAME));
		criteria.add(criterionMap.get(CriterDistance.NAME));
		AiUtilityCase caseMurDestAtt = new AiUtilityCase(ai, MurDestAtt,criteria);

		caseMap.put(ItemVisibleBONUS, caseItemVisibleBonus);
		caseMap.put(ItemVisibleMALUS, caseItemVisibleMalus);
		caseMap.put(MurDestructibles, caseMurDestructibles);
		caseMap.put(MurDestAtt, caseMurDestAtt);
		caseMap.put(PresenceUnEnnemi, casePresenceUnEnnemi);

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();

		AiUtilityCase result = null;
		AiZone zone = ai.getZone();
		// AiHero ownHero = zone.getOwnHero();
		// AiTile ownTile = ownHero.getTile();
		// int count = HERO_COUNT;
		int heroCount = 0;
		Set<AiTile> destBlockTileL = selectTiles();
		destBlockTileL.clear();
		boolean destKont = false;
		List<AiBlock> destBlockL = zone.getDestructibleBlocks();

		for (int i = 0; i < destBlockL.size(); i++) {
			ai.checkInterruption();
			destBlockTileL.add(destBlockL.get(i).getTile());
		}
		List<AiTile> neigBL = tile.getNeighbors();
		for (int i = 0; i < neigBL.size(); i++) {
			ai.checkInterruption();
			if (destBlockTileL.contains(neigBL.get(i))) {
				destKont = true;
				break;
			}
		}

		if (ai.modeHandler.getMode() == AiMode.COLLECTING) {
			if (!tile.getItems().isEmpty()) {
				for (AiItem item : tile.getItems()) {
					ai.checkInterruption();
					if (item.getType().equals(AiItemType.EXTRA_BOMB)
							|| item.getType().equals(AiItemType.EXTRA_FLAME)
							|| item.getType().equals(AiItemType.EXTRA_SPEED)
							|| item.getType().equals(AiItemType.GOLDEN_BOMB)
							|| item.getType().equals(AiItemType.GOLDEN_FLAME)
							|| item.getType().equals(AiItemType.GOLDEN_SPEED)
							|| item.getType().equals(AiItemType.RANDOM_EXTRA)) {
						result = caseMap.get(ItemVisibleBONUS);
					} else if (item.getType().equals(AiItemType.NO_BOMB)
							|| item.getType().equals(AiItemType.NO_FLAME)
							|| item.getType().equals(AiItemType.NO_SPEED)
							|| item.getType().equals(AiItemType.ANTI_BOMB)
							|| item.getType().equals(AiItemType.ANTI_FLAME)
							|| item.getType().equals(AiItemType.ANTI_SPEED)
							|| item.getType().equals(AiItemType.RANDOM_NONE)) {
						result = caseMap.get(ItemVisibleMALUS);
					}
				}
			} else if (destKont) { // count > HERO_COUNT){
				result = caseMap.get(MurDestructibles);
			}

		}
		// mode attaque
		else if (ai.modeHandler.getMode() == AiMode.ATTACKING) {

			// boolean voisinageDopponent = false;
			for (AiTile tt : this.ai.getAccessibleTiles()) {
				ai.checkInterruption();
				if (!tt.getHeroes().isEmpty())
					heroCount++;
			}

			if (heroCount > NEAR_ENEMY_HERO_COUNT_LIMIT) { // if(voisinageDopponent){
				result = caseMap.get(PresenceUnEnnemi);
			} else {
				result = caseMap.get(MurDestAtt);
			}
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// REFERENCE /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCombination combi;
		AiMode mode;

		// on commence avec le mode collecte
		{
			mode = AiMode.COLLECTING;
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 39);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 38);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 37);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 36);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 35);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 34);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 33);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 32);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 31);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 30);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 29);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 28);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 27);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 26);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 25);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 24);
			}
			
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 23);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 22);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 19);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 21);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 18);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 17);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 15);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleBONUS));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterPertinence) criterionMap.get(CriterPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterTemps) criterionMap.get(CriterTemps.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 13);
			}
			
			
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleMALUS));
				combi.setCriterionValue((MalusCriter) criterionMap.get(MalusCriter.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 0);
			}
			{	combi = new AiUtilityCombination(caseMap.get(ItemVisibleMALUS));
				combi.setCriterionValue((MalusCriter) criterionMap.get(MalusCriter.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 9);
			}

			
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 51);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 50);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 49);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 48);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 47);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 46);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 45);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 44);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 43);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 42);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 41);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 40);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 20);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 16);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 14);

			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 12);

			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 11);

			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestructibles));
				combi.setCriterionValue((CriterMenace) criterionMap.get(CriterMenace.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 10);

			}

		}

		// on traite maintenant le mode attaque
		{
			mode = AiMode.ATTACKING;

			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 23);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 22);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 21);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 20);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 19);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 18);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 17);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 16);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 15);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 14);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 13);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 12);
			}
			
			
			
			{	combi = new AiUtilityCombination(caseMap.get(PresenceUnEnnemi));
				combi.setCriterionValue((CriterATTPertinence) criterionMap.get(CriterATTPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 11);
			}
			{	combi = new AiUtilityCombination(caseMap.get(PresenceUnEnnemi));
				combi.setCriterionValue((CriterATTPertinence) criterionMap.get(CriterATTPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, 10);
			}
			{	combi = new AiUtilityCombination(caseMap.get(PresenceUnEnnemi));
				combi.setCriterionValue((CriterATTPertinence) criterionMap.get(CriterATTPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 5);
			}
			{	combi = new AiUtilityCombination(caseMap.get(PresenceUnEnnemi));
				combi.setCriterionValue((CriterATTPertinence) criterionMap.get(CriterATTPertinence.NAME), Boolean.FALSE);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 6);
			}
			{	combi = new AiUtilityCombination(caseMap.get(PresenceUnEnnemi));
				combi.setCriterionValue((CriterATTPertinence) criterionMap.get(CriterATTPertinence.NAME), Boolean.TRUE);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, 3);
			}
			
			
			
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 8);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),1);
				defineUtilityValue(mode, combi, 7);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 6);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 3);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 4);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 1);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),0);
				defineUtilityValue(mode, combi, 2);
			}
			{	combi = new AiUtilityCombination(caseMap.get(MurDestAtt));
				combi.setCriterionValue((CriterMurDest) criterionMap.get(CriterMurDest.NAME), 2);
				combi.setCriterionValue((CriterSecure) criterionMap.get(CriterSecure.NAME),Boolean.TRUE);
				combi.setCriterionValue((CriterDistance) criterionMap.get(CriterDistance.NAME),2);
				defineUtilityValue(mode, combi, 1);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		// à redéfinir, si vous voulez afficher d'autres informations
	}
}
