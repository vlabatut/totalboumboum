package org.totalboumboum.ai.v201213.ais.kartturgut.v4;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Duree;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Force;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Nombredemurs;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Nonconcurrence;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.kartturgut.v4.criterion.Securite;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Yunus Kart
 * @author Siyabend Turgut
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<KartTurgut> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(KartTurgut ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		verbose = false;
	}

	/** */
	private final String Case1 = "Bloquer_adversaire";
	/** */
	private final String Case2 = "Bonus_visible";
	/** */
	private final String Case3 = "Mur_menace";
	/** */
	private final String Case4 = "Mur_destructible";
	/** */
	private final String Case5 = "Malus_visible";
	/** */
	private final String Case6 = "Case_vide";
	/** */
	private final String Case7 = "Voisinage_adversaire";
	/** */
	private final String Case8 = "Avance";

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> resultat = new TreeSet<AiTile>();
		TileProcess tilepr = new TileProcess(this.ai);
		resultat.addAll(tilepr.getwalkableTiles());
		return resultat;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		Duree duree = new Duree(ai);
		Force force = new Force(ai);

		Nombredemurs nombredemurs = new Nombredemurs(ai);
		Nonconcurrence nonconcurrence = new Nonconcurrence(ai);
		Pertinence pertinence = new Pertinence(ai);
		Securite securite = new Securite(ai);

		// ///////////////////////////////////////////////////////////////
		// CASE /////////////////////////////////////
		// ///////////////////////////////////////////////////////////////

		Set<AiUtilityCriterion<?, ?>> criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(duree);
		criteria.add(pertinence);
		criteria.add(securite);
		AiUtilityCase casBloquerAdversaire = new AiUtilityCase(ai, Case1,
				criteria);

		Set<AiUtilityCriterion<?, ?>> criteria2 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria2.add(duree);
		criteria2.add(pertinence);
		criteria2.add(nonconcurrence);
		criteria2.add(securite);
		AiUtilityCase casBonusVisible = new AiUtilityCase(ai, Case2, criteria2);

		Set<AiUtilityCriterion<?, ?>> criteria3 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria3.add(duree);
		criteria3.add(nombredemurs);
		criteria3.add(securite);
		AiUtilityCase casMurMenace = new AiUtilityCase(ai, Case3, criteria3);

		Set<AiUtilityCriterion<?, ?>> criteria4 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria4.add(duree);
		criteria4.add(nombredemurs);
		criteria4.add(securite);
		AiUtilityCase casMurDestructible = new AiUtilityCase(ai, Case4,
				criteria4);

		Set<AiUtilityCriterion<?, ?>> criteria5 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria5.add(securite);
		AiUtilityCase casMalusVisible = new AiUtilityCase(ai, Case5, criteria5);

		Set<AiUtilityCriterion<?, ?>> criteria6 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria6.add(duree);
		criteria6.add(securite);
		AiUtilityCase casCaseVide = new AiUtilityCase(ai, Case6, criteria6);

		Set<AiUtilityCriterion<?, ?>> criteria7 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria7.add(duree);
		criteria7.add(force);
		criteria7.add(nonconcurrence);
		criteria7.add(securite);
		AiUtilityCase casVoisinageAdversaire = new AiUtilityCase(ai, Case7,
				criteria7);

		Set<AiUtilityCriterion<?, ?>> criteria8 = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria8.add(duree);
		criteria8.add(nombredemurs);
		criteria8.add(securite);
		AiUtilityCase casAvance = new AiUtilityCase(ai, Case8, criteria8);

		caseMap.put(Case1, casBloquerAdversaire);
		caseMap.put(Case2, casBonusVisible);
		caseMap.put(Case3, casMurMenace);
		caseMap.put(Case4, casMurDestructible);
		caseMap.put(Case5, casMalusVisible);
		caseMap.put(Case6, casCaseVide);
		caseMap.put(Case7, casVoisinageAdversaire);
		caseMap.put(Case8, casAvance);

		AiMode mode = AiMode.COLLECTING;

		AiUtilityCombination CollectComb;

		{	CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 57);
		}
		{	CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 56);
		}
		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 55);
		}
		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, true);

			defineUtilityValue(mode, CollectComb, 54);
		}
		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nonconcurrence, true);

			defineUtilityValue(mode, CollectComb, 53);
		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nonconcurrence, true);

			defineUtilityValue(mode, CollectComb, 52);
		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 51);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 50);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 49);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 48);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 47);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 46);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);

			defineUtilityValue(mode, CollectComb, 45);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 44);

		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 43);

		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 42);

		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 41);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 40);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 39);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 38);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 37);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, true);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 36);

		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 35);

		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, true);
			defineUtilityValue(mode, CollectComb, 34);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 33);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 32);

		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 31);

		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 30);

		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 29);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 28);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, true);
			defineUtilityValue(mode, CollectComb, 27);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, true);
			defineUtilityValue(mode, CollectComb, 26);
		}

		{
			CollectComb = new AiUtilityCombination(casCaseVide);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			defineUtilityValue(mode, CollectComb, 25);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 24);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 23);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 22);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 21);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 20);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode, CollectComb, 19);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode, CollectComb, 18);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 17);
		}

		{
			CollectComb = new AiUtilityCombination(casCaseVide);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			defineUtilityValue(mode, CollectComb, 16);
		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode, CollectComb, 15);
		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 14);
		}

		{
			CollectComb = new AiUtilityCombination(casBonusVisible);
			CollectComb.setCriterionValue(pertinence, false);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode, CollectComb, 13);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 12);
		}

		{
			CollectComb = new AiUtilityCombination(casMurMenace);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 11);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 10);
		}

		{
			CollectComb = new AiUtilityCombination(casMurDestructible);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode, CollectComb, 9);
		}

		{
			CollectComb = new AiUtilityCombination(casCaseVide);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			defineUtilityValue(mode, CollectComb, 8);
		}

		{
			CollectComb = new AiUtilityCombination(casCaseVide);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			defineUtilityValue(mode, CollectComb, 7);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, false);
			defineUtilityValue(mode, CollectComb, 6);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(pertinence, false);
			defineUtilityValue(mode, CollectComb, 5);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, false);
			defineUtilityValue(mode, CollectComb, 4);
		}

		{
			CollectComb = new AiUtilityCombination(casBloquerAdversaire);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(pertinence, false);
			defineUtilityValue(mode, CollectComb, 3);
		}

		{
			CollectComb = new AiUtilityCombination(casMalusVisible);
			CollectComb.setCriterionValue(securite, true);
			defineUtilityValue(mode, CollectComb, 2);
		}

		{
			CollectComb = new AiUtilityCombination(casMalusVisible);
			CollectComb.setCriterionValue(securite, false);
			defineUtilityValue(mode, CollectComb, 1);

		}
		AiMode mode2 = AiMode.ATTACKING;
		AiUtilityCombination AttackComb;

		{
			AttackComb = new AiUtilityCombination(casBloquerAdversaire);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(duree, true);
			defineUtilityValue(mode2, AttackComb, 38);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 37);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 36);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 35);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 34);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 33);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 32);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 31);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 30);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 29);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 28);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 27);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 26);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, true);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 25);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, true);
			defineUtilityValue(mode2, AttackComb, 24);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 23);
		}

		{
			AttackComb = new AiUtilityCombination(casVoisinageAdversaire);
			AttackComb.setCriterionValue(force, false);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(nonconcurrence, false);
			defineUtilityValue(mode2, AttackComb, 22);
		}

		{
			AttackComb = new AiUtilityCombination(casBloquerAdversaire);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(duree, true);
			defineUtilityValue(mode2, AttackComb, 21);
		}

		{
			AttackComb = new AiUtilityCombination(casBloquerAdversaire);
			AttackComb.setCriterionValue(securite, true);
			AttackComb.setCriterionValue(duree, false);
			defineUtilityValue(mode2, AttackComb, 20);
		}

		{
			AttackComb = new AiUtilityCombination(casBloquerAdversaire);
			AttackComb.setCriterionValue(securite, false);
			AttackComb.setCriterionValue(duree, false);
			defineUtilityValue(mode2, AttackComb, 19);
		}

		{
			AttackComb = new AiUtilityCombination(casCaseVide);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, true);
			defineUtilityValue(mode2, AttackComb, 18);
		}

		{
			AttackComb = new AiUtilityCombination(casCaseVide);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, true);
			defineUtilityValue(mode2, AttackComb, 17);
		}

		{
			AttackComb = new AiUtilityCombination(casCaseVide);
			AttackComb.setCriterionValue(duree, false);
			AttackComb.setCriterionValue(securite, false);
			defineUtilityValue(mode2, AttackComb, 16);
		}

		{
			AttackComb = new AiUtilityCombination(casCaseVide);
			AttackComb.setCriterionValue(duree, true);
			AttackComb.setCriterionValue(securite, false);
			defineUtilityValue(mode2, AttackComb, 15);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode2, AttackComb, 14);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode2, AttackComb, 13);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode2, AttackComb, 12);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode2, AttackComb, 11);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode2, AttackComb, 10);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, true);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode2, AttackComb, 9);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode2, AttackComb, 8);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode2, AttackComb, 7);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, true);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode2, AttackComb, 6);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 3);
			defineUtilityValue(mode2, AttackComb, 5);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 2);
			defineUtilityValue(mode2, AttackComb, 4);
		}

		{
			CollectComb = new AiUtilityCombination(casAvance);
			CollectComb.setCriterionValue(duree, false);
			CollectComb.setCriterionValue(securite, false);
			CollectComb.setCriterionValue(nombredemurs, 1);
			defineUtilityValue(mode2, AttackComb, 3);
		}

		{
			CollectComb = new AiUtilityCombination(casMalusVisible);
			CollectComb.setCriterionValue(securite, true);
			defineUtilityValue(mode2, AttackComb, 2);

		}

		{
			CollectComb = new AiUtilityCombination(casMalusVisible);
			CollectComb.setCriterionValue(securite, false);
			defineUtilityValue(mode2, AttackComb, 1);

		}

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();

		AiMode mode = ai.modeHandler.getMode();

		if (mode == AiMode.COLLECTING) {
			if (tile.getItems().isEmpty())
				return this.caseMap.get(Case6);
			else
				return this.caseMap.get(Case2);
		} else

			return this.caseMap.get(Case7);

	}

	// ///////////////////////////////////////////////////////////////
	// REFERENCE /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();

	}

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();

	}

	@Override
	protected void initReferenceUtilities() throws StopRequestException {
		ai.checkInterruption();
		
	}
}
