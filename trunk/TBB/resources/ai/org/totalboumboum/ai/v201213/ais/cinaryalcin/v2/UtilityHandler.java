package org.totalboumboum.ai.v201213.ais.cinaryalcin.v2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.AdjacentTile;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.Distance;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.Pertinent;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.BlockEnemy;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.Security;
import org.totalboumboum.ai.v201213.ais.cinaryalcin.v2.criterion.ItemType;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
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
	
		
	}

	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		AiHero ourhero = this.ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		int bombrange=ourhero.getBombRange();
		Set<AiTile> result = new TreeSet<AiTile>();
		if(this.ai.modeHandler.getMode().equals(AiMode.COLLECTING))
		{
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
		}
		else
		{
			for(AiHero hero:ai.getZone().getRemainingOpponents())
			{	
				ai.checkInterruption();
				if(!result.contains(hero.getTile()))
				result.add(hero.getTile());
				for(Direction direction:Direction.getPrimaryValues())
				{
					ai.checkInterruption();
					int i=0;
					AiTile tileh=hero.getTile();
					while(i<=bombrange && tileh.getNeighbor(direction).isCrossableBy(ourhero, false, false, false, false, true, true))
					{	
						ai.checkInterruption();
						if(!result.contains(tileh.getNeighbor(direction)))
						result.add(tileh.getNeighbor(direction));
						i++;
						tileh=tileh.getNeighbor(direction);
					}
					
				}
				
			}
			
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	
		ai.checkInterruption();

		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new ItemType(ai);
		new Concurrence(ai);
		new Distance(ai);
		new Pertinent(ai);
		new AdjacentTile(ai);
		new Security(ai);
		new BlockEnemy(ai);
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
	private final String CASE_NAME4 = "Attaquer a Ennemie";
	/** */
	private final String CASE_NAME5 = "Case vide";

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		// Item Visible
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(ItemType.NAME));
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
		criteria.add(criterionMap.get(AdjacentTile.NAME));
		new AiUtilityCase(ai,CASE_NAME3,criteria);
	    
		 // Attaquer a Ennemi
		 criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		 criteria.add(criterionMap.get(Security.NAME));
		 criteria.add(criterionMap.get(Distance.NAME));
		 criteria.add(criterionMap.get(BlockEnemy.NAME));
		 new AiUtilityCase(ai,CASE_NAME4,criteria);
		 
		// Case Vide
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
	    new AiUtilityCase(ai,CASE_NAME5,criteria);
		 	
	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	
		ai.checkInterruption();
		AiUtilityCase result = null;
		AiMode mode = this.ai.modeHandler.getMode();
		
		// Tile identification for collect mode
		if (mode.equals(AiMode.COLLECTING)) {
				for(AiItem item:tile.getItems())
				{
					ai.checkInterruption();
					if (ai.Bonus.contains(item.getType())||
							ai.SBonus.contains(item.getType()))
						//Item visible
						result = caseMap.get(CASE_NAME1);
				}
				
				if(result==null){
					boolean value=false;
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if(!value){
						ai.checkInterruption();
						AiTile currentNeighbor = tile.getNeighbor(direction);
						value = this.ai.willBeDestroyed(currentNeighbor);
						}
					}
				
					if(value==true){	
							// Destructible wall in danger 
								result = caseMap.get(CASE_NAME2);
					}
				    else{
				    	boolean value1=false;
				    	for (Direction direction : Direction.getPrimaryValues()) {
							ai.checkInterruption();
							for (AiBlock currentBlock : tile.getNeighbor(direction)
									.getBlocks()) {
								ai.checkInterruption();
								if(!value1){
									ai.checkInterruption();
									if (currentBlock.isDestructible()) {
										value1=true;
									}
								}
							}
						}
				    	if(value1==true){
						    	// Destructible wall				
								result = caseMap.get(CASE_NAME3);	
						 }
				    	else{
				    		//Tile empty
				    		result=caseMap.get(CASE_NAME5);
				    	}
				    }
			 }
		}
		
		else
		{			
			// Attack to enemies
			result = caseMap.get(CASE_NAME4);		
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
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//vide
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				defineUtilityValue(mode, combi, utility);
				utility++;
		    }
			{	// on crée la combinaison (vide pour l'instant)  c9
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
				// on affecte les valeurs de chaque critère
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),1);
				// on rajoute la combinaison dans la map, avec son utilité
				defineUtilityValue(mode, combi, utility);
				// on incrémente l'utilité pour la combinaison suivante
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a10
				combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c8
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),2);
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
				combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),1);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a9
				combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
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
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		   }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a8
				combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c5
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a7
				combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c4
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),2);
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
				combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c3
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a5
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
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
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c2
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));//c1
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((AdjacentTile)criterionMap.get(AdjacentTile.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a3
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
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
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a1
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			/////////////////////////////////////////////////////////////////////////////////////
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a11
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a12
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.TRUE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a13
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a14
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a15
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a16
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a17
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a18
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a19
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a20
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a21
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a22
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));//a23
			combi.setCriterionValue((ItemType)criterionMap.get(ItemType.NAME),Boolean.FALSE);
			combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((Pertinent)criterionMap.get(Pertinent.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			defineUtilityValue(mode, combi, 0);
	    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));//vide
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			defineUtilityValue(mode, combi, 0);
	    }
			
			
		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;
			// pour simplifier, on ne met qu'un seul cas 
			
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
				combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.TRUE);
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
				combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
		}	
			
			//////////////////////////////////////////////////////////////////////
			
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
		{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),0);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
		}
		{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
		}
		{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, 0);
	}
		{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),1);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, 0);
	}
		{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((Security)criterionMap.get(Security.NAME),Boolean.FALSE);
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),2);
			combi.setCriterionValue((BlockEnemy)criterionMap.get(BlockEnemy.NAME),Boolean.TRUE);
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
	}
}
