package org.totalboumboum.ai.v201213.ais.guneysharef.v3;

import java.util.ArrayList;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;


/**

 * @author Melis Güney
 * @author Seli Sharef
 */
public class BombHandler extends AiBombHandler<GuneySharef>
{	
	/**
	 * 
	 */
	public static final int BLOCK = 15;
	/**
	 * min valeur d'utilité d'une case sur le rang d'un ennemie
	 */
	public static final int MIN_TILE_IN_RANGE = 10;
	/**max valeur d'utilité d'une case sur le rang d'un ennemie */
	public static final int MAX_TILE_IN_RANGE = 13;
	/**  min valeur d'utilité des autres cases*/
	public static final int MIN_TILE_RANGE = 6;
	/** max valeur d'utilité des autres cases*/ 
	public static final int MAX_TILE_RANGE = 9;
	/** min valeur d'utilité pour le cas "case voisine d'un mur destructible */
	public static final int STRAIGHT = 30;

	/** min valeur d'utilité d'un case proche d'un ennemie */
	public static final int MIN_TILE_CLOSE_ENEMY = 2;
	/**max valeur d'utilité d'un case proche d'un ennemie */
	public static final int MAX_TILE_CLOSE_ENEMY = 29;
	/**
	 * boolean
	 */
	boolean resultat = false;
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(GuneySharef ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	/**
	 * Méthode permettant de déterminer si l'agent doit poser une bombe ou pas
	 */
	protected boolean considerBombing() throws StopRequestException
	{	
		ai.checkInterruption();
		AiHero h = this.ai.getZone().getOwnHero();
		AiTile t = h.getTile();
		
		
		AiMode mode = this.ai.modeHandler.getMode();
		boolean herocontrol = true;
		if (!this.ai.getZone().getRemainingOpponents().isEmpty()) {
			for(AiHero aihero : this.ai.getZone().getRemainingOpponents())
			{
				ai.checkInterruption();
				if (this.ai.utilityHandler.selectTiles().contains(
						aihero.getTile())) {
					herocontrol = false;
					break;
				}

			}
		}
		if(mode.equals(AiMode.COLLECTING))
		{
			if ((h.getBombNumberCurrent() < h.getBombNumberMax())
					&& t.getBombs().isEmpty()) {
				if (this.ai.getSafeTiles(h, null, true).size() > 0) {
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if (this.ai.controlOfDestructibleBlock(t,
								direction) == true) {

							return resultat = true;

						}
					}
					return resultat = false;
				}

			} else {
				return resultat = false;

			}
		}
		else if (mode.equals(AiMode.ATTACKING)) {
			if ((h.getBombNumberCurrent() < h.getBombNumberMax())
					&& t.getBombs().isEmpty()) {
				if (this.ai.getSafeTiles(h, null, true).size() > 0) {

					
					ArrayList<AiTile> list = new ArrayList<AiTile>();
					Map<AiTile, Float> hashmap;
					hashmap = this.ai.getUtilityHandler().getUtilitiesByTile();
					for (AiTile currentTile : hashmap.keySet()) {
						this.ai.checkInterruption();

						if (herocontrol == false) {

							if (this.ai.getUtilityHandler()
									.getUtilitiesByTile().get(currentTile) == BLOCK) {
								list.add(currentTile);
							}

							if (!list.isEmpty() && list.contains(t)) {
								return resultat = true;
							}
						}
					}
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();

						if (ai.getAnEnemyInMyRange(t, direction, 0) == true) {


							ArrayList<AiTile> list1 = new ArrayList<AiTile>();
							ArrayList<AiTile> list2 = new ArrayList<AiTile>();
							Map<AiTile, Float> hashmap1;
							hashmap1 = this.ai.getUtilityHandler()
									.getUtilitiesByTile();

							for (AiTile currentTile : hashmap1.keySet()) {
								this.ai.checkInterruption();
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_IN_RANGE
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_IN_RANGE) {

									list1.add(currentTile);

								}
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_RANGE
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_RANGE) {
									list2.add(currentTile);
								}

							}
							if (list.isEmpty() && !list1.isEmpty()
									&& list1.contains(t)) {
								return resultat = true;
							}

							if (list1.isEmpty() && !list2.isEmpty()
									&& list2.contains(t)) {
								return resultat = true;
							}

						}
					}

					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if (herocontrol == true
								&& this.ai.controlOfDestructibleBlock(t,
										direction) == true) {
		
							ArrayList<AiTile> list2 = new ArrayList<AiTile>();
							ArrayList<AiTile> list3 = new ArrayList<AiTile>();
							Map<AiTile, Float> hashmap2;
							hashmap2 = this.ai.getUtilityHandler()
									.getUtilitiesByTile();

							for (AiTile currentTile : hashmap2.keySet()) {
								this.ai.checkInterruption();
								if (this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= STRAIGHT) {
									list2.add(currentTile);

								}
								if ((this.ai.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) >= MIN_TILE_CLOSE_ENEMY
										&& this.ai.getUtilityHandler()
												.getUtilitiesByTile()
												.get(currentTile) <= MAX_TILE_CLOSE_ENEMY && this.ai
										.getUtilityHandler()
										.getUtilitiesByTile().get(currentTile) != 4)) {
									list3.add(currentTile);
								}
							}
							if (!list2.isEmpty() && list2.contains(t)) {

								return resultat = true;
							}
							if (list2.isEmpty() && !list3.isEmpty()
									&& list3.contains(t)) {

								return resultat = true;
							}
						}
					}
					return resultat = false;
				} else {
					return resultat = false;

				}
			} else {
				return resultat = false;

			}
		}
		
	
		return resultat;
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		
		
		
	}
		
	}

