package org.totalboumboum.ai.v201213.ais.guneysharef.v4;

import java.util.LinkedList;
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
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion.Competition;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion.Convenience;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion.DistA;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion.Distance;
import org.totalboumboum.ai.v201213.ais.guneysharef.v4.criterion.NbrDW;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Melis Güney
 * @author Seli Sharef
 */
@SuppressWarnings("deprecation")
public class UtilityHandler extends AiUtilityHandler<GuneySharef>
{	
	/**
	 * boolean
	 */
	boolean herocontrol=true;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	protected UtilityHandler(GuneySharef ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
	
		
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Réinitialise les structures de données modifiées à chaque itération
	 */
	protected void resetCustomData() throws StopRequestException
	{	ai.checkInterruption();
		
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * pour la selection des cases qu'on veut calculer l'utilité
	 */
	@Override
	protected Set<AiTile> selectTiles() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		AiHero h=this.ai.getZone().getOwnHero();
		AiTile t=h.getTile();
		AiTile t2=t;
		
		Queue<AiTile> q = new LinkedList<AiTile>();
		q.add(t2);
		while(!q.isEmpty()){
			ai.checkInterruption();
			t2=q.poll();
			for(Direction direction : Direction.getPrimaryValues()){
				ai.checkInterruption();
				if(t2.getNeighbor(direction).getBombs().isEmpty()
						&&t2.getNeighbor(direction).getBlocks().isEmpty()
						&&!q.contains(t2.getNeighbor(direction))
						&&!result.contains(t2.getNeighbor(direction))
						&&!t2.getNeighbor(direction).equals(t)){
					q.add(t2.getNeighbor(direction));
				}
			}
			if(!q.isEmpty()){
				t2=q.peek();
				result.add(t2);
				
			}
			else
				break;
		}
		result.add(t);
		
		
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Initialise tous les critères
	 */
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();
		
	new Competition(ai);
	new Distance(ai);
	new Convenience(ai);
	new NbrDW(ai);
	new DistA(ai);


	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**Mode de collecte */
	private final String CASE_NAME1 = "Visible item";
	/** */
	private final String CASE_NAME2 = "Tile close to a destructible wall(s)";
	/** */
	private final String CASE_NAME3 = "Tile close to indestructible wall(s)";
	
	/**Mode d'attaque*/
	private final String CASE_NAME4 = "Tile close to an enemy";
	/** */
	private final String CASE_NAME5 = "Block enemy";
	
	/**
	 * 
	 */
	private final String CASE_NAME6 = "Empty tile";
	/**
	 * on determine les criteres pour chaque cas
	 */
	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	

		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Competition.NAME));
		criteria.add(criterionMap.get(Convenience.NAME));
		AiUtilityCase case1=new AiUtilityCase(ai,CASE_NAME1,criteria);
			

		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Competition.NAME));
		criteria.add(criterionMap.get(NbrDW.NAME));
		AiUtilityCase case2=new AiUtilityCase(ai,CASE_NAME2,criteria);


		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Competition.NAME));
		AiUtilityCase case3=new AiUtilityCase(ai,CASE_NAME3,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(DistA.NAME));
		AiUtilityCase case4=new AiUtilityCase(ai,CASE_NAME4,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		AiUtilityCase case5=new AiUtilityCase(ai,CASE_NAME5,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		AiUtilityCase case6=new AiUtilityCase(ai,CASE_NAME6,criteria);
		
		caseMap.put(CASE_NAME1, case1);
		caseMap.put(CASE_NAME2, case2);
		caseMap.put(CASE_NAME3, case3);
		caseMap.put(CASE_NAME4, case4);
		caseMap.put(CASE_NAME5, case5);
		caseMap.put(CASE_NAME6, case6);
		
	}
	

	@Override
	/**
	 * prend une case en paramètre, et identifie le cas correspondant
	 */
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		

		
		
		if(!this.ai.getZone().getRemainingOpponents().isEmpty()){
			for(AiHero h :this.ai.getZone().getRemainingOpponents()){
				ai.checkInterruption();
				if(this.ai.utilityHandler.selectTiles().contains(h.getTile())){
					herocontrol=false;
					break;
				}
			}
		}
		
		AiMode m=this.ai.modeHandler.getMode();
		if(m.equals(AiMode.COLLECTING)){
			if(tile.getItems().contains(AiItemType.EXTRA_BOMB)||tile.getItems().contains(AiItemType.EXTRA_FLAME)){
				

				return result= caseMap.get(CASE_NAME1);
				
			}
			for(Direction direction: Direction.getPrimaryValues()){
				ai.checkInterruption();
				for(AiBlock b:tile.getNeighbor(direction).getBlocks()){
					ai.checkInterruption();
					if(b.isDestructible()){
						
					
						return result= caseMap.get(CASE_NAME2);
					}
				}
			}
			
			for(Direction direction: Direction.getPrimaryValues()){
				ai.checkInterruption();
				for(AiBlock b:tile.getNeighbor(direction).getBlocks()){
					ai.checkInterruption();
					if(!b.isDestructible()){
					
						return result= caseMap.get(CASE_NAME3);
					}
				}
			}
			
			return result= caseMap.get(CASE_NAME6);
		}
		
		if(m.equals(AiMode.ATTACKING)){
			for(Direction direction: Direction.getPrimaryValues()){
				ai.checkInterruption();
					if(this.ai.getAnEnemyInMyRange(tile, direction, 0)){
					
						return result= caseMap.get(CASE_NAME4);
					}
				
			}
			if(this.ai.block(tile)){
	
				return result= caseMap.get(CASE_NAME5);
			}
			
			
			return result= caseMap.get(CASE_NAME6);
		
		
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * on identifier les conbinaisons
	 */
	protected void initReferenceUtilities() throws StopRequestException
	{	ai.checkInterruption();
	
		// on affecte les valeurs d'utilité
		int utility;
		AiUtilityCombination combi;
		AiMode mode;
		
		// on commence avec le mode collecte
		{	mode = AiMode.COLLECTING;
			utility = 1;
			
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME3));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		    }
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{
			combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
			combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.FALSE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.FALSE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME2));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
			combi.setCriterionValue((NbrDW)criterionMap.get(NbrDW.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.TRUE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.TRUE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.TRUE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.FALSE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}
			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.TRUE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}

			{	
				combi = new AiUtilityCombination(caseMap.get(CASE_NAME1));

				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Competition)criterionMap.get(Competition.NAME),Boolean.FALSE);
				combi.setCriterionValue((Convenience)criterionMap.get(Convenience.NAME),Boolean.TRUE);

				defineUtilityValue(mode, combi, utility);

				utility++;
			}


		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 1;

			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME6));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
			defineUtilityValue(mode, combi, utility);
			utility++;
			}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));
			combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}

			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),1);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),2);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}
			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME4));
			combi.setCriterionValue((DistA)criterionMap.get(DistA.NAME),3);
			defineUtilityValue(mode, combi, utility);
			utility++;
		}


			{	combi = new AiUtilityCombination(caseMap.get(CASE_NAME5));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
				utility++;
			}









			

		}
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * met a jours les sorties graphiques de l'agent
	 */
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();		
	}
}
