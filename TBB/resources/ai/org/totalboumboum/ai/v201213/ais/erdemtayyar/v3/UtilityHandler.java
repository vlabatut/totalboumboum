package org.totalboumboum.ai.v201213.ais.erdemtayyar.v3;

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.EnnemieDistance;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.Ennemie;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.MurDestructible;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.Pertinence;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.Securite;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v3.criterion.Temps;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent. Cf. la
 * documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 *
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
public class UtilityHandler extends AiUtilityHandler<ErdemTayyar> {

	/**
	 * The hero count at the initialisation.
	 */
	private final int Hcount = 0;

	/**
	 * If this number of enemies are accessible, tile may get QuartierEnnemi
	 * case.
	 */
	private final int HcountLimit = 1;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(ErdemTayyar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		
	}

	// ///////////////////////////////////////////////////////////////
	// DATA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException {
		ai.checkInterruption();

		
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	
	
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();

		result.addAll(this.ai.getTs().getAccessibleTiles());

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// CRITERIA /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException {
		ai.checkInterruption();

		
		new Securite(ai);
		new Pertinence(ai);
		new Concurrence(ai);
		new MurDestructible(ai);
		new Temps(ai);
		new Ennemie(ai);
		new EnnemieDistance(ai);

	}

	// ///////////////////////////////////////////////////////////////
	// CASE /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** noms du premier cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME1 = "ItemVisible";
	/** noms du deuxième cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME2 = "ItemCache";
	/** noms du trosième cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME3 = "AdversaireDirect";
	/** noms du quatrième cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME4 = "AdversaireIndirect";
	/** noms du cinquime cas, utilisé dans {@link #initCases} */
	private final String CASE_NAME5 = "Rien";

	@Override
	protected void initCases() throws StopRequestException {
		ai.checkInterruption();
		Set<AiUtilityCriterion<?, ?>> criteria;

		// on définit un premier cas utilisant les quatre critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(Pertinence.NAME));
		criteria.add(criterionMap.get(Concurrence.NAME));
		criteria.add(criterionMap.get(Temps.NAME));
		new AiUtilityCase(ai, CASE_NAME1, criteria);

		// on définit un deuxième cas utilisant les trois  critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(MurDestructible.NAME));
		criteria.add(criterionMap.get(Temps.NAME));
		new AiUtilityCase(ai, CASE_NAME2, criteria);

		// on définit un troiseme cas utilisant les trois  critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(Ennemie.NAME));
		criteria.add(criterionMap.get(Temps.NAME));
		new AiUtilityCase(ai, CASE_NAME3, criteria);
		// on définit un quatrieme cas utilisant les deux  critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(EnnemieDistance.NAME));
		new AiUtilityCase(ai, CASE_NAME4, criteria);
		// on définit un cinqueme cas utilisant les deux  critères
		criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		new AiUtilityCase(ai, CASE_NAME5, criteria);
		
		
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		AiUtilityCase result = null;
		int heroCount = 0;

		int count = Hcount;
		
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.UP ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.DOWN ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.LEFT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}
		for ( AiBlock currentBlock : tile.getNeighbor( Direction.RIGHT ).getBlocks() )
		{
			ai.checkInterruption();
			if ( currentBlock.isDestructible() ) count++;
		}


		// Mode Collecte
		Set<AiUtilityCriterion<?, ?>> criteria;
		if (this.ai.getModeHandler().getMode().equals(AiMode.COLLECTING)) {
			
			if (!tile.getItems().isEmpty()) {
				criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add(criterionMap.get(Securite.NAME));
				criteria.add(criterionMap.get(Pertinence.NAME));
				criteria.add(criterionMap.get(Concurrence.NAME));
				criteria.add(criterionMap.get(Temps.NAME));
				return new AiUtilityCase(ai, CASE_NAME1, criteria);
			}
		
			else if (count != Hcount) {
			criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
			criteria.add(criterionMap.get(Securite.NAME));
			criteria.add(criterionMap.get(MurDestructible.NAME));
			criteria.add(criterionMap.get(Temps.NAME));
			return new AiUtilityCase(ai, CASE_NAME2, criteria);
		}
			
		else if (tile.getItems().isEmpty())
		{
			criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
			criteria.add(criterionMap.get(Securite.NAME));
			return new AiUtilityCase(ai, CASE_NAME5, criteria);
		}
		}
			
			
				
		
		else
		// Mode Attaque
		{

			for (AiTile tt : this.ai.getTs().getAccessibleTiles()) {
				ai.checkInterruption();
				if (!tt.getHeroes().isEmpty())
					heroCount++;
			}

			if (heroCount > HcountLimit) {
				criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add(criterionMap.get(Securite.NAME));
				criteria.add(criterionMap.get(Ennemie.NAME));
				criteria.add(criterionMap.get(Temps.NAME));
				return new AiUtilityCase(ai, CASE_NAME3, criteria);

			}
			else {
				criteria = new TreeSet<AiUtilityCriterion<?, ?>>();
				criteria.add(criterionMap.get(Securite.NAME));
				criteria.add(criterionMap.get(EnnemieDistance.NAME));
				return new AiUtilityCase(ai, CASE_NAME4, criteria);

			}

		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// REFERENCE /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	@Override
	protected void initReferenceUtilities() throws StopRequestException {
		ai.checkInterruption();

		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		
		// on commence avec le mode collecte
		{
			mode = AiMode.COLLECTING;
			utility = 0;
			// le premier cas a un critère binaire et un entier [1,3],
			// donc il y a 6 combinaisons possibles
			// ici, pour l'exemple, on leur affecte des utilités
			// en respectant l'ordre des valeurs, mais bien sûr
			// ce n'est pas du tout obligatoire
			{ // on crée la combinaison (vide pour l'instant)
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;

			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 0);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 1);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue((MurDestructible) criterionMap
						.get(MurDestructible.NAME), 2);
				combi.setCriterionValue((Temps) criterionMap
						.get(Temps.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Pertinence) criterionMap.get(Pertinence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue(
						(Concurrence) criterionMap.get(Concurrence.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
			}

		}

		// on traite maintenant le mode attaque
		{
			mode = AiMode.ATTACKING;
			utility = 0;
			// pour simplifier, on ne met qu'un seul cas : le troisième
			// il n'a qu'un seul critère, défini sur un domaine de 5 valeurs
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME),0);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 0);
				combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.FALSE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME),1);
				combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.TRUE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME),2);
				combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.FALSE);
				
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 1);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME),2);
			    combi.setCriterionValue(
						(EnnemieDistance) criterionMap.get(EnnemieDistance.NAME), Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.FALSE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				combi.setCriterionValue(
						(Securite) criterionMap.get(Securite.NAME), 2);
				combi.setCriterionValue(
						(Ennemie) criterionMap.get(Ennemie.NAME),
						Boolean.TRUE);
				combi.setCriterionValue((Temps) criterionMap.get(Temps.NAME),
						Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
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
		
	}
}
