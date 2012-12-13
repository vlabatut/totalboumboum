package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v200910.adapter.data.AiHero;

import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;

import org.totalboumboum.engine.content.feature.Direction;

/**
 * >> Our bomberman can be considered as a bit defensive one because of the
 * strategies choosen while attacking and defending. Also it is giving big
 * importance not to die even in collecting the bonus and attack by tryig to
 * choose the most secure cases.
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */

/* We made a better explanation about the use of each method in the classes. */

@SuppressWarnings("deprecation")
public class DemirciDuzokErgok extends ArtificialIntelligence {

	/** */
	private AiZone IA_ZONE;

	/** */
	private AiHero our_bomberman;

	/** */
	private AiTile our_tile;
	/** */
	private int q = 0;
	/** */
	private int q_test = 0;
	/** */
	private int our_x;
	/** */
	private int our_y;
	/** */
	private Safety_Map safe_map;

	/** */
	int numb_bonus = 0;

	/** */
	List<AiTile> possibleDest_w;
	/** */
	List<AiTile> possible_Dest_bonus;

	/**
	 * we declared the global variable to seperate the actions exploring the
	 * walls when the enemy is not accessible and accessing to the enemie
	 */
	private long kkk = GlobalClass.globalData;

	/** */
	Direction moveDir;

	/**
	 * The classes for defense,collecting bonus,exploring walls and ,attacking
	 * the enemies
	 */
	private Bonus_Manager bonusmanager = null;
	/** */
	private Escape_Manager escapemanager = null;
	/** */
	private Wall_Manager wallmanager = null;
	/** */
	private Enemie_Manager enemiemanager = null;
	/** */
	private Wall_Manager_2 wallmanager2 = null;

	/** */
	AiPath path_b;

	/* méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		IA_ZONE = getPercepts();

		our_bomberman = IA_ZONE.getOwnHero();
		our_tile = our_bomberman.getTile();

		our_x = our_tile.getCol();
		our_y = our_tile.getLine();

		AiAction result = new AiAction(AiActionName.NONE);

		if (our_bomberman.hasEnded() == false) {

			q = 0;
			q_test = 0;
			moveDir = Direction.NONE;
			safe_map = new Safety_Map(IA_ZONE);

			int k = 0;
			for (int i = 0; i < IA_ZONE.getHeight(); i++) {
				for (int j = 0; j < IA_ZONE.getWidth(); j++) {
					if (safe_map.returnMatrix()[i][j] == safe_map.DEST_WALL)
						k++;
				}
			}

			/*
			 * If we are not in safe case, we will escape:
			 */
			if (escapemanager != null) {

				if (escapemanager.arrive_or_not() == true) {

					escapemanager = null;

				} else {

					moveDir = escapemanager.direcition_updt();
					// System.out.println("kacisa devam");
				}
			}

			/*
			 * if we are in safe case and if we dont have bonus enough for
			 * attacking the enemie we collect the bonuses: For us,to be enough
			 * armored to attack an enemie is to have 3 bombs.
			 */
			else if (safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman
					.getCol()] != safe_map.SAFE_CASE
					&& safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman
							.getCol()] != safe_map.ENEMIE) {

				escapemanager = new Escape_Manager(this);
				moveDir = escapemanager.direcition_updt();

			}

			else if (safe_map.returnMatrix()[our_y][our_x] == safe_map.SAFE_CASE
					&& (our_bomberman.getBombNumber() < 3)/*
														 * && k>0 &&
														 * our_bomberman
														 * .getBombCount()<2
														 */) {
				// System.out.println("bonus");

				boolean r = false;
				for (int i = 0; i < IA_ZONE.getHeight(); i++) {
					for (int j = 0; j < IA_ZONE.getWidth(); j++) {
						if (safe_map.returnMatrix()[i][j] == safe_map.BONUS)
							r = true;
					}
				}

				/*
				 * If we can access bonus directly,we will go to collect it
				 */
				if (r == true && q_test == 0) {

					if (bonusmanager != null) {
						if (bonusmanager.accessiblePath() == true) {
							if (bonusmanager.hasArrived_b()) {

								bonusmanager = null;

							} else {

								moveDir = bonusmanager.direcition_updt_b();

							}
						} else {
							q_test = 1;
							bonusmanager = null;

						}
					} else {
						// q_test=1;
						bonusmanager = new Bonus_Manager(this);
						moveDir = bonusmanager.direcition_updt_b();
						// System.out.println("c");
					}

				}

				/*
				 * The bonus exists but it is in range of fire or the path which
				 * we will cross for it is in range of bombs. It is dangerous to
				 * try to access for it, so we break walls to find new bonus
				 * which are not dangerous.
				 */
				if (r == true && q_test == 1) {
					bonusmanager = null;
					if (wallmanager != null) {

						if (wallmanager.hasArrived_b()) {

							if (wallmanager.canesc() == true) {

								q = 1;

								wallmanager = null;
							} else {
								q = 0;
								wallmanager = null;
							}

						}

						else {
							// System.out.println("b");
							moveDir = wallmanager.direcition_updt_b();

						}

					} else {
						q = 0;
						wallmanager = new Wall_Manager(this);
						moveDir = wallmanager.direcition_updt_b();

					}
				}

				/*
				 * If no bonus exists in the zone(bonuses which are visible) we
				 * break walls to find bonus
				 */

				else if (r == false && our_bomberman.getBombCount() < 3) {

					if (wallmanager != null) {

						if (wallmanager.hasArrived_b()) {

							if (wallmanager.canesc() == true) {

								q = 1;
								wallmanager = null;
							} else {
								q = 0;

								wallmanager = null;
							}

						}

						else {

							moveDir = wallmanager.direcition_updt_b();

						}

					} else {
						q = 0;
						wallmanager = new Wall_Manager(this);
						moveDir = wallmanager.direcition_updt_b();

					}
				}

			}

			/*
			 * Here, we attack or explore walls to access enemie.
			 */

			else if (IA_ZONE.getOwnHero().getBombCount() < 3) {
				if (enemiemanager != null) {

					if (enemiemanager.accessiblePath() == false && kkk != 1) {

						// This Method is for breaking walls when the enemie is
						// not accessible
						Break_Walls_For_Enemie();
					}
					// We can access the enemie so we attack:
					else {
						kkk = 1;

						if (enemiemanager.hasArrived_b()) {

							if (enemiemanager.canesc() == true) {

								q = 1;
								enemiemanager = null;

							} else {
								q = 0;
								enemiemanager = null;

							}
						} else {

							moveDir = enemiemanager.direcition_updt_b();
							q = 0;
						}

					}

				}

				else {
					enemiemanager = new Enemie_Manager(this);
					moveDir = enemiemanager.direcition_updt_b();
					q = 0;

				}
			}

			if (q == 0)
				result = new AiAction(AiActionName.MOVE, moveDir);
			else if (q == 1)
				result = new AiAction(AiActionName.DROP_BOMB);
		}
		return result;
	}

	/** Method for breaking walls when the enemie is not accessible.
	 * 
	 * @throws StopRequestException
	 */
	private void Break_Walls_For_Enemie() throws StopRequestException {

		if (wallmanager2 != null && IA_ZONE.getOwnHero().getBombCount() < 2) {

			if (wallmanager2.hasArrived_b()) {

				if (wallmanager2.canesc() == true) {
					q = 1;
					enemiemanager = null;
					wallmanager2 = null;
				} else {
					q = 0;
					enemiemanager = null;
					wallmanager2 = null;
				}

			}

			else {
				moveDir = wallmanager2.direcition_updt_b();

				q = 0;
			}

		} else {
			q = 0;
			wallmanager2 = new Wall_Manager_2(this);
			moveDir = wallmanager2.direcition_updt_b();

		}
	}

}
