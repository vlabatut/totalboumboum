package org.totalboumboum.ai.v201213.ais.oralozugur.v1;

import java.util.Set;
import java.util.TreeSet;


import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCase;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCombination;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterion;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.AvanceItem;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.BestWall;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.Concurrence;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.Danger;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.Distance;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.NbMurDetrui;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.Necessity;
import org.totalboumboum.ai.v201213.ais.oralozugur.v1.criterion.PeutTuerEnnemi;


/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * Cf. la documentation de {@link AiUtilityHandler} pour plus de détails.
 * 
 * 
 * @author Buğra Oral
 * @author Ceyhun Özuğur
 */
public class UtilityHandler extends AiUtilityHandler<OralOzugur>
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
	protected UtilityHandler(OralOzugur ai) throws StopRequestException
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
	
		Set<AiTile> result = new TreeSet<AiTile>();
		AiHero ourhero = ai.getZone().getOwnHero();
		AiTile ourtile = ourhero.getTile();
		
		result.addAll( ai.getAccessibleTilesFrom(ourtile) );
		

		return result;	
		
	}

	/////////////////////////////////////////////////////////////////
	// CRITERIA					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initCriteria() throws StopRequestException
	{	ai.checkInterruption();
		
		

		// on définit les critères, qui sont automatiquement insérés dans criterionMap
		new AvanceItem(ai);
		new BestWall(ai);
		new Concurrence(ai);
		new Danger(ai);
		new Distance(ai);
		new NbMurDetrui(ai);
		new Necessity(ai);
		new PeutTuerEnnemi(ai);
	}
	
	/////////////////////////////////////////////////////////////////
	// CASE						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	
	////////
	//MODE COLLECTE
	//////////
	/**
	 * Ce calcule est faite s’il existe un item bonus dans la case qu’on est en train de calculer leur utilité.
	 */
	private final String BONUS_VISIBLE = "Bonus Visible";
	/**
	 * Ce calcule est faite pour les cases qui ne contient pas un bonus ou malus.
	 */
	private final String RECHERCHE_ITEM = "Recherche Item";
	/**
	 * Si il y a un malus dans la case on prend ce cas. Il y a seulement une critère qui est le danger.
	 */
	private final String MALUS_VISIBLE = "Malus Visible";
	
	
	////////
	//MODE ATTAQUE
	//////////
	/**
	 * Ce cas est valable seulement si il n’ya pas d’ennemi dans nos tuiles accessibles.
	 */
	private final String AVANCE_ENNEMI = "Avance Ennemi";
	/**
	 * 
	 */
	private final String QUARTIER_ENNEMI = "Quartier Ennemi";

	@Override
	protected void initCases() throws StopRequestException
	{	ai.checkInterruption();
		Set<AiUtilityCriterion<?,?>> criteria;
	
		//COLLECTE
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Danger.NAME));
		criteria.add(criterionMap.get(Necessity.NAME));
		criteria.add(criterionMap.get(Concurrence.NAME));
		new AiUtilityCase(ai,BONUS_VISIBLE,criteria);
			
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Distance.NAME));
		criteria.add(criterionMap.get(Danger.NAME));
		criteria.add(criterionMap.get(NbMurDetrui.NAME));
		new AiUtilityCase(ai,RECHERCHE_ITEM,criteria);

		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Danger.NAME));
		new AiUtilityCase(ai,MALUS_VISIBLE,criteria);
		
		//ATTAQUE
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(BestWall.NAME));
		criteria.add(criterionMap.get(AvanceItem.NAME));
		criteria.add(criterionMap.get(Danger.NAME));
		new AiUtilityCase(ai,AVANCE_ENNEMI,criteria);
		
		criteria = new TreeSet<AiUtilityCriterion<?,?>>();
		criteria.add(criterionMap.get(Danger.NAME));
		criteria.add(criterionMap.get(PeutTuerEnnemi.NAME));
		new AiUtilityCase(ai,QUARTIER_ENNEMI,criteria);

	}

	@Override
	protected AiUtilityCase identifyCase(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiUtilityCase result = null;
		/*
		AvanceItem avanceItem = new AvanceItem(ai);
		BestWall bestWall = new BestWall(ai);
		Concurrence concurrence =  new Concurrence(ai);
		Danger danger =  new Danger(ai);
		Distance distance = new Distance(ai);
		NbMurDetrui nbMurDetrui = new NbMurDetrui(ai);
		Necessity necessity = new Necessity(ai);
		PeutTuerEnnemi peutTuerEnnemi = new PeutTuerEnnemi(ai);
		*/
		// mode collecte
				if ( ai.getModeHandler().getMode().equals( AiMode.COLLECTING ) )
				{
					if ( !tile.getItems().isEmpty() )
					{
						boolean bonus_check=false;
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
								Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
								criteria.add(criterionMap.get(Distance.NAME));
								criteria.add( criterionMap.get(Danger.NAME) );
								criteria.add( criterionMap.get(Necessity.NAME) );
								criteria.add( criterionMap.get(Concurrence.NAME) );
								result = new AiUtilityCase(ai, BONUS_VISIBLE, criteria);
								bonus_check=true;
							}
					
							
						}
						
						 if(!bonus_check){
							Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
							criteria.add( criterionMap.get(Danger.NAME) );
							result = new AiUtilityCase(ai, MALUS_VISIBLE, criteria);
						}
					}

					else
					{
						Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
						criteria.add (criterionMap.get(Distance.NAME) );
						criteria.add(criterionMap.get(Danger.NAME));
						criteria.add( criterionMap.get(NbMurDetrui.NAME) );
						result = new AiUtilityCase(ai, RECHERCHE_ITEM, criteria );

					}

				}
				// mode attaque
				if ( ai.getModeHandler().getMode().equals( AiMode.ATTACKING ) )
				{
					
					if (this.ai.isEnemiesAccessible()) {
								Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
								criteria.add (criterionMap.get(PeutTuerEnnemi.NAME) );
								criteria.add(criterionMap.get(Danger.NAME));
								result = new AiUtilityCase(ai, QUARTIER_ENNEMI, criteria );
							
						}
					 else {
						Set<AiUtilityCriterion<?,?>> criteria = new TreeSet<AiUtilityCriterion<?,?>>();
						criteria.add(criterionMap.get(BestWall.NAME));
						criteria.add(criterionMap.get(AvanceItem.NAME));
						criteria.add(criterionMap.get(Danger.NAME));
						result = new AiUtilityCase(ai, AVANCE_ENNEMI, criteria );
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
			utility = 0;

			{	//A1
				utility = 38;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A2
				utility = 37;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A3
				utility = 32;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A4
				utility = 31;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A5
				utility = 28;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A6
				utility = 25; 
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A7
				utility = 24;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A8
				utility = 23;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A9
				utility = 19;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A10
				utility = 18;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A11
				utility = 14;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A12
				utility = 13;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.TRUE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A13
				utility = 9; 
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A14
				utility = 8;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A15
				utility = 5; 
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//A16
				utility = 4;
				combi = new AiUtilityCombination(caseMap.get(BONUS_VISIBLE));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((Necessity)criterionMap.get(Necessity.NAME),Boolean.FALSE);
				combi.setCriterionValue((Concurrence)criterionMap.get(Concurrence.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B1
				utility = 35;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),3);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B2
				utility = 34;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),2);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B3
				utility = 30;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),3);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B4
				utility = 29;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),2);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B5
				utility = 27;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),1);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B6
				utility = 26;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),1);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B7
				utility = 22;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),0);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B8
				utility = 21;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),0);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B9
				utility = 16;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),3);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B10
				utility = 15;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),2);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B11
				utility = 11;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),3);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B12
				utility = 10;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),2);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B13
				utility = 7;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),1);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B14
				utility = 6;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),1);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B15
				utility = 3;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),0);
				defineUtilityValue(mode, combi, utility);
			}
			{	//B16
				utility = 2;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),0);
				defineUtilityValue(mode, combi, utility);
			}
			{	//F1
				utility = 36;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),4);
				defineUtilityValue(mode, combi, utility);
			}
			{	//F2
				utility = 33;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),4);
				defineUtilityValue(mode, combi, utility);
			}
			{	//F3
				utility = 17;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),4);
				defineUtilityValue(mode, combi, utility);
			}
			{	//F4
				utility = 12;
				combi = new AiUtilityCombination(caseMap.get(RECHERCHE_ITEM));
				combi.setCriterionValue((Distance)criterionMap.get(Distance.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				combi.setCriterionValue((NbMurDetrui)criterionMap.get(NbMurDetrui.NAME),4);
				defineUtilityValue(mode, combi, utility);
			}
			{	//C1
				utility = 20;
				combi = new AiUtilityCombination(caseMap.get(MALUS_VISIBLE));
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//C2
				utility = 1;
				combi = new AiUtilityCombination(caseMap.get(MALUS_VISIBLE));
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}

		}
		
		// on traite maintenant le mode attaque
		{	mode = AiMode.ATTACKING;
			utility = 0;
			{	//D1
				utility = 11;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D2
				utility = 10;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D3
				utility = 9;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D4
				utility = 8;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D5
				utility = 4;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D6
				utility = 3;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.TRUE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D7
				utility = 2;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.TRUE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//D8
				utility = 1;
				combi = new AiUtilityCombination(caseMap.get(AVANCE_ENNEMI));
				combi.setCriterionValue((BestWall)criterionMap.get(BestWall.NAME),Boolean.FALSE);
				combi.setCriterionValue((AvanceItem)criterionMap.get(AvanceItem.NAME),Boolean.FALSE);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E1
				utility = 14;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),2);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E2
				utility = 13;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),1);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E3
				utility = 12;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),0);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.FALSE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E4
				utility = 7;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),2);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E5
				utility = 6;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),1);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
			}
			{	//E6
				utility = 5;
				combi = new AiUtilityCombination(caseMap.get(QUARTIER_ENNEMI));
				combi.setCriterionValue((PeutTuerEnnemi)criterionMap.get(PeutTuerEnnemi.NAME),0);
				combi.setCriterionValue((Danger)criterionMap.get(Danger.NAME),Boolean.TRUE);
				defineUtilityValue(mode, combi, utility);
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
