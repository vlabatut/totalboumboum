package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;

import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.AdjacentCase;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.Distance;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.Pertinent;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.PeutTuer;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.Securite;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v1.criterion.TypeItem;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class UtilityHandler extends AiUtilityHandler<CinarYalcin>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected UtilityHandler(CinarYalcin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	
		// TODO à compléter
	}

	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à surcharger si nécessaire, pour réinitialiser certaines
		// structures de données à chaque itération
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
	AiHero ourhero = this.ai.getZone().getOwnHero();
	AiTile ourtile = ourhero.getTile();
	Set<AiTile> result = new TreeSet<AiTile>();
	AiTile aitile = ourtile;

	Queue<AiTile> qe = new LinkedList<AiTile>();
	qe.add(aitile);
	while (!qe.isEmpty()) {
		ai.checkInterruption();
		aitile = qe.poll();
		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			if (aitile.getNeighbor(direction).getBombs().isEmpty()
					&& aitile.getNeighbor(direction).getBlocks().isEmpty()
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

	return result;

	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();
		
		// TODO à compléter afin d'initialiser les critères 
		// la méthode est appelée une seule fois
		
		// le traitement défini ici utilise les classes
		// définissant des critères, données en exemple dans
		// le package v1.criterion. Il s'agit seulement d'un 
		// exemple, vous devez définir vos propres critères !
		
		// cf. la Javadoc dans AiUtilityHandler pour une description de la méthode

		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new TypeItem(ai);
		new Concurrence(ai);
		new Distance(ai);
		new Pertinent(ai);
		new AdjacentCase(ai);
		new Securite(ai);
		new PeutTuer(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Mode collect cases */
	private final String CASE_NAME1 = "Item Visible";
	/** */
	private final String CASE_NAME2 = "Voisinage d’un Mur Destructible Menace";
	/** */
	private final String CASE_NAME3 = "Voisinage d’un Mur Destructible";
	/** Mode attack cases */
	private final String CASE_NAME4 = "Le Piege aux Ennemies";
	/** */
	private final String CASE_NAME5 = "Attaquer a Ennemie";
	/** */
	private final String CASE_NAME6 = "Case vide";

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		// Item Visible
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(TypeItem.NAME));
		criteria.add(criterionMap.get(Concurrence.NAME));
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Pertinent.NAME));
		new AiUtilityCase(ai,CASE_NAME1,criteria);
		
			
		// Voisinage d'un Mur Destructible Menace
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Concurrence.NAME));
		criteria.add(criterionMap.get(Distance.NAME));
		new AiUtilityCase(ai,CASE_NAME2,criteria);

		// Voisinage d'un Mur Destructible
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(AdjacentCase.NAME));
		new AiUtilityCase(ai,CASE_NAME3,criteria);
		
		// Le Piege aux Ennemies
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Securite.NAME));
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(AdjacentCase.NAME));
		new AiUtilityCase(ai,CASE_NAME4,criteria);
	    
		 // Attaquer a Ennemi
		 criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		 criteria.add(criterionMap.get(Securite.NAME));
		 criteria.add(criterionMap.get(Distance.NAME));
		 criteria.add(criterionMap.get(PeutTuer.NAME));
		 new AiUtilityCase(ai,CASE_NAME5,criteria);
		 
		// Case Vide
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
	    new AiUtilityCase(ai,CASE_NAME6,criteria);
		 
		
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
	AiUtilityCase result = null;
	
	

	// We control if we have enemies in the list of tiles returned by
	// SelectTiles.

	AiMode mode = this.ai.modeHandler.getMode();
	// Tile identification for collect mode

	if (mode.equals(AiMode.COLLECTING)) {
		boolean value=false;
		boolean value1=false;
		
		for (Direction direction : Direction.getPrimaryValues()) {
			if(!value){
			ai.checkInterruption();
			AiTile currentNeighbor = tile.getNeighbor(direction);
			value = this.ai.getWallInDanger(currentNeighbor);
			}
		}
		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			for (AiBlock currentBlock : tile.getNeighbor(direction)
					.getBlocks()) {
				if(!value1){
					ai.checkInterruption();
					if (currentBlock.isDestructible()) {
						value1=true;
					}
				}
			}
		}
		
		if (tile.getItems().size()>0) {
			//Item visible
			result = caseMap.get(CASE_NAME1);
		}
		else if(value==true){	
					// Destructible wall in danger 
						result = caseMap.get(CASE_NAME2);
					  }
		else if(value1==true){
				    // Destructible wall				
						result = caseMap.get(CASE_NAME3);	
				       }
		else{
			//Tile emty
			result=caseMap.get(CASE_NAME6);
		}
	 }
	
	else
	{
		boolean value2=false;
		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			if(value2==false)
			value2=this.ai.getAnEnemyInMyRange(tile, direction, 0);
		}
		// Block enemy identification
		if (this.ai.simBlock(tile)) {
			
			result = caseMap.get(CASE_NAME4);
		}
		else if(value2==true)
		{
			result = caseMap.get(CASE_NAME5);
		}
		else
		{
			result=caseMap.get(CASE_NAME6);
		}	
	}
	
	return result;
	}

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
	
		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			utility = 1;
			// le premier cas a un critère binaire et un entier [1,3],
			// donc il y a 6 combinaisons possibles
			// ici, pour l'exemple, on leur affecte des utilités
			// en respectant l'ordre des valeurs, mais bien sûr
			// ce n'est pas du tout obligatoire
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
		    }
			{	// on crée la combinaison (vide pour l'instant)  c9
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a10
				combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c8
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b6
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c7
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a9
				combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b5
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		   }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c6
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		   }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a8
				combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c5
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a7
				combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c4
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b4
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a6
				combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c3
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a5
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b3
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a4
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c2
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c1
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a3
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b2
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));//b1
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a2
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a1
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			/////////////////////////////////////////////////////////////////////////////////////
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a11
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a12
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a13
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a14
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a15
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a16
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a17
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a18
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a19
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a20
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a21
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a22
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a23
			combi.setCriterionValue((TypeItem)criterionMap.get(TypeItem.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			defineUtilityValue(mode, combi, 0);
	    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			defineUtilityValue(mode, combi, 0);
	    }
			
			// le deuxième cas a un critère entier [1,3] 
			// et un critère chaine de caractères à 5 valeurs
			// possibles, donc ça fait 15 combinaisons au total.
			// la définition de l'utilité de ces combinaisons
			// se fait de la même façon que ci dessus
			
			// ......
			// etc. pour les 14 autres combinaisons (qui doivent toutes être définies
			// afin de leur associer une valeur d'utilité à chacune)
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;
			// pour simplifier, on ne met qu'un seul cas : le troisième
			// il n'a qu'un seul critère, défini sur un domaine de 5 valeurs
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
	    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e6
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d8
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e5
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d7
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e4
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d6
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d5
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e3
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d4
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}	
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d3
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e2
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e1
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d2
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d1
				combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			//////////////////////////////////////////////////////////////////////
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d9
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d10
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d11
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d12
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d13
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d14
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d15
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d16
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),3);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d17
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),2);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));//d18
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((AdjacentCase)criterionMap.get(AdjacentCase.NAME),1);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e7
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e8
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e9
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e10
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e11
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//e12
			combi.setCriterionValue((Securite)criterionMap.get(Securite.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((PeutTuer)criterionMap.get(PeutTuer.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			defineUtilityValue(mode, combi, 0);
	    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			defineUtilityValue(mode, combi, 0);
	    }
			
		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
