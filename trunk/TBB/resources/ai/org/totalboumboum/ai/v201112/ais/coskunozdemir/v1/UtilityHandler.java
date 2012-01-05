package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.AttaPertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.ColPertinence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.Duree;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.NombreDeMurs;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.NonConcurrence;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v1.criterion.Securite;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class UtilityHandler extends AiUtilityHandler<CoskunOzdemir> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(CoskunOzdemir ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
;

		// TODO à compléter
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** noms des cas, utilisés dans {@link #initCriteria} */
	private final String ItemVisible = "ItemVisible";
	private final String VoisinageMurDest = "VoisinageMurDest";
	private final String QuartierEnnemi = "QuartierEnnemi";

	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		Set<AiTile> result = new TreeSet<AiTile>();

		for (AiTile currentTile : this.ai.getAccessibleTiles()) {
			result.add(currentTile);
		}

		return result;
	}

	@Override
	protected void initCriteria() throws StopRequestException {
		// TODO à compléter afin d'initialiser les critères
		// et les cas. la méthode est appelée une seule fois

		// TODO le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un
		// exemple, vous devez définir vos propres critères !

		// cf. la java doc dans AiUtilityHandler pour une description de la
		// méthode

		// on définit les critères
		ColPertinence colPertinence = new ColPertinence(ai);
		Duree duree = new Duree(ai);
		Securite securite = new Securite(ai);
		NonConcurrence nonConcurrence = new NonConcurrence(ai);
		NombreDeMurs nombreDeMurs = new NombreDeMurs(ai);
		AttaPertinence attaPertinence = new AttaPertinence(ai);

		// on définit un premier cas utilisant les deux premiers critères
		Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(colPertinence);
		criteria.add(duree);
		criteria.add(securite);
		criteria.add(nonConcurrence);
		AiUtilityCase CasItemVisible = new AiUtilityCase(ItemVisible, criteria);

		// on définit un sescond cas utilisant les deux derniers critères
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(duree);
		criteria.add(securite);
		criteria.add(nombreDeMurs);
		AiUtilityCase CasVoisinageMurDest = new AiUtilityCase(VoisinageMurDest,
				criteria);

		// on définit un sescond cas utilisant les deux derniers critères
		criteria = new TreeSet<AiUtilityCriterion<?>>();
		criteria.add(duree);
		criteria.add(attaPertinence);
		criteria.add(securite);
		AiUtilityCase CasQuartierEnnemi = new AiUtilityCase(QuartierEnnemi,
				criteria);

		// on met les cas dans la map prévue à cet effet
		// ceci permettra de les retrouver facilement plus tard,
		// en particulier dans la méthode identifyCase()
		cases.put(ItemVisible, CasItemVisible);
		cases.put(VoisinageMurDest, CasVoisinageMurDest);
		cases.put(QuartierEnnemi, CasQuartierEnnemi);

		// ////////Collect List//////////////////////////////

		AiUtilityCombination combiC;

		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 2);

		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 3);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 6);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 7);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 10);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 12);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 13);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 15);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 16);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 17);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 20);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 21);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, false);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 24);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, false);
			referenceUtilities.put(combiC, 26);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 27);
		}
		{
			combiC = new AiUtilityCombination(CasItemVisible);
			combiC.setCriterionValue(colPertinence, true);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nonConcurrence, true);
			referenceUtilities.put(combiC, 28);
		}
		// -----------------------------------------------------------------
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nombreDeMurs, 1);
			referenceUtilities.put(combiC, 1);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 1);
			referenceUtilities.put(combiC, 4);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nombreDeMurs, 1);
			referenceUtilities.put(combiC, 5);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nombreDeMurs, 2);
			referenceUtilities.put(combiC, 8);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 2);
			referenceUtilities.put(combiC, 9);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nombreDeMurs, 3);
			referenceUtilities.put(combiC, 11);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 1);
			referenceUtilities.put(combiC, 14);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, false);
			combiC.setCriterionValue(nombreDeMurs, 3);
			referenceUtilities.put(combiC, 18);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 2);
			referenceUtilities.put(combiC, 19);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 2);
			referenceUtilities.put(combiC, 22);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, false);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 3);
			referenceUtilities.put(combiC, 23);
		}
		{
			combiC = new AiUtilityCombination(CasVoisinageMurDest);
			combiC.setCriterionValue(duree, true);
			combiC.setCriterionValue(securite, true);
			combiC.setCriterionValue(nombreDeMurs, 3);
			referenceUtilities.put(combiC, 25);
		}
		//System.out.println(combiC);

		AiUtilityCombination combiA;

		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, false);
			combiA.setCriterionValue(attaPertinence, false);
			combiA.setCriterionValue(securite, false);
			referenceUtilities.put(combiA, 1);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, false);
			combiA.setCriterionValue(attaPertinence, true);
			combiA.setCriterionValue(securite, false);
			referenceUtilities.put(combiA, 2);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, true);
			combiA.setCriterionValue(attaPertinence, false);
			combiA.setCriterionValue(securite, false);
			referenceUtilities.put(combiA, 3);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, true);
			combiA.setCriterionValue(attaPertinence, true);
			combiA.setCriterionValue(securite, false);
			referenceUtilities.put(combiA, 4);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, false);
			combiA.setCriterionValue(attaPertinence, false);
			combiA.setCriterionValue(securite, true);
			referenceUtilities.put(combiA, 5);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, false);
			combiA.setCriterionValue(attaPertinence, true);
			combiA.setCriterionValue(securite, true);
			referenceUtilities.put(combiA, 6);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, true);
			combiA.setCriterionValue(attaPertinence, false);
			combiA.setCriterionValue(securite, true);
			referenceUtilities.put(combiA, 7);
		}
		{
			combiA = new AiUtilityCombination(CasQuartierEnnemi);
			combiA.setCriterionValue(duree, true);
			combiA.setCriterionValue(attaPertinence, true);
			combiA.setCriterionValue(securite, true);
			referenceUtilities.put(combiA, 8);
		}

		// ------------------------Attack List
		// -----------------------------------------
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		AiUtilityCase result = null;

		ColPertinence colPertinence = new ColPertinence(ai);
		Duree duree = new Duree(ai);
		Securite securite = new Securite(ai);
		NonConcurrence nonConcurrence = new NonConcurrence(ai);
		NombreDeMurs nombreDeMurs = new NombreDeMurs(ai);
		AttaPertinence attaPertinence = new AttaPertinence(ai);

		if (!this.ai.inModeAttack()) {
			if (!tile.getItems().isEmpty()) {
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(colPertinence);
				criteria.add(duree);
				criteria.add(securite);
				criteria.add(nonConcurrence);
				result = new AiUtilityCase(ItemVisible, criteria);
			} else if (tile.getNeighbor(Direction.DOWN).getBlocks() != null
					&& tile.getNeighbor(Direction.UP).getBlocks() != null
					&& tile.getNeighbor(Direction.LEFT).getBlocks() != null
					&& tile.getNeighbor(Direction.RIGHT).getBlocks() != null) {
				Set<AiUtilityCriterion<?>> criteria = new TreeSet<AiUtilityCriterion<?>>();
				criteria.add(duree);
				criteria.add(securite);
				criteria.add(nombreDeMurs);
				result = new AiUtilityCase(VoisinageMurDest, criteria);
			}

		}
		else
		{
			Set<AiUtilityCriterion<?>>  criteria = new TreeSet<AiUtilityCriterion<?>>();
			criteria.add(duree);
			criteria.add(attaPertinence);
			criteria.add(securite);
			result = new AiUtilityCase(QuartierEnnemi, criteria);
			
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		//super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
