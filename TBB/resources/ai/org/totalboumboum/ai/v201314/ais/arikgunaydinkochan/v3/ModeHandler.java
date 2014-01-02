package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe gérant les déplacements de l'agent. Cf. la documentation de
 * {@link AiModeHandler} pour plus de détails.
 * 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class ModeHandler extends AiModeHandler<Agent> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	protected ModeHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}

	/**
	 * list of accessibleTiles
	 * */
	ArrayList<AiTile> accessibleTiles1;

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() {
		ai.checkInterruption();

		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile myTile = myHero.getTile();
		// opponent agent
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(ai.getZone().getRemainingOpponents());

		int mybombs = myHero.getBombNumberMax();
		int mybombsRange = myHero.getBombRange();

		ai.accessibleTile();
		accessibleTiles1 = ai.accessibleTiles;

		boolean dangereousHero = false;
		boolean accessibleHero = false;

		// 3 tile mesafede düsman var mı?
		if (!heroes.isEmpty()) {
			for (AiHero hero : heroes) {
				ai.checkInterruption();
				if (dangereousHero == false) {
					AiTile heroTile = hero.getTile();
					if (accessibleTiles1.contains(heroTile)) {
						accessibleHero = true;
						if (ai.getZone().getTileDistance(myTile, heroTile) <= 3)
							dangereousHero = true;
					}
				}
			}
		}

		if (((dangereousHero == true) || ((mybombs >= 3 || mybombsRange >= 4) && accessibleHero == true))) { // atack
			return true;
		} else
			// belkide collect yapmak lazım
			return false;

	}

	@Override
	protected boolean isCollectPossible() {
		ai.checkInterruption();

		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(ai.getZone().getRemainingOpponents());

		boolean accessibleHero = false;

		accessibleTiles1 = ai.accessibleTiles;
		// erişebildigin kare sayisi az ise ve erişilebilen hero varsa
		if (ai.accessibleTiles.size() < 5) {
			if (!heroes.isEmpty()) {
				for (AiHero hero : heroes) {
					ai.checkInterruption();
					if (accessibleHero == false) {
						AiTile heroTile = hero.getTile();
						if (accessibleTiles1.contains(heroTile)) {
							accessibleHero = true;
						}
					}
				}

				if (accessibleHero)
					return false;
				else
					return true;

			}

		}

		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();

		boolean result = false;
		// zone da item var mi ? sizin ihtiyaciniz var mi ?
		if (zone.getHiddenItemsCount() > 0 || zone.getItems().size() > 0) {

			// System.out.println("zone de item var");
			int mybombs = myHero.getBombNumberMax();
			int mybombsRange = myHero.getBombRange();
			double mySpeed = myHero.getWalkingSpeed();

			boolean needOfExtraBomb = (mybombs < 3);
			boolean needOfExtraFlame = (mybombsRange < 4);
			boolean needOfExtraSpeed = (mySpeed < mySpeed * 2);

			boolean getBomb = false;
			boolean getFlame = false;
			boolean getSpeed = false;
			/*
			 * System.out.println("bomba sayisi	"+mybombs);
			 * System.out.println("bomba range		"+mybombsRange);
			 * System.out.println("hız				"+mySpeed);
			 * 
			 * System.out.println("bomb ihtiyac	"+needOfExtraBomb);
			 * System.out.println("flame ihtiyac	"+needOfExtraFlame);
			 * System.out.println("hız	ihtiyac		"+needOfExtraSpeed);
			 */
			if (needOfExtraBomb) {
				if (ai.itemVisible(AiItemType.EXTRA_BOMB)
						|| ai.itemVisible(AiItemType.GOLDEN_BOMB)) {
					getBomb = true;
					// System.out.println("bomba	 "+getBomb);
				} else if (ai.itemHidden(AiItemType.EXTRA_BOMB)
						|| ai.itemHidden(AiItemType.GOLDEN_BOMB)) {
					getBomb = true;
					// System.out.println("gizli bomb	"+getBomb);
				}
			}
			if (needOfExtraFlame) {
				if (ai.itemVisible(AiItemType.EXTRA_FLAME)
						|| ai.itemVisible(AiItemType.GOLDEN_FLAME)) {
					getFlame = true;
					// System.out.println("flame	"+getFlame);
				} else if (ai.itemHidden(AiItemType.EXTRA_FLAME)
						|| ai.itemHidden(AiItemType.GOLDEN_FLAME)) {
					getFlame = true;
					// System.out.println("gizliFlame	"+getFlame);
				}

			}
			if (needOfExtraSpeed) {
				if (ai.itemVisible(AiItemType.EXTRA_SPEED)
						|| ai.itemVisible(AiItemType.GOLDEN_SPEED)) {
					getSpeed = true;
					// System.out.println("Speed	"+getSpeed);
				} else if (ai.itemHidden(AiItemType.EXTRA_SPEED)
						|| ai.itemHidden(AiItemType.GOLDEN_SPEED)) {
					getSpeed = true;
					// System.out.println("GizliSpeed	"+getSpeed);
				}
			}

			if (getBomb || getFlame || getSpeed)
				result = true;

			// System.out.println("ihtiyac oldugunu algiladim");
		}

		// System.out.println("result "+result);
		return result;

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}
}
