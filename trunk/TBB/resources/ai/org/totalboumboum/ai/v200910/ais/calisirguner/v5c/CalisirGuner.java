package org.totalboumboum.ai.v200910.ais.calisirguner.v5c;

//

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * et on sest profité des classes de groupe bleu de l'annee precedente
 * 
 * @version 5.c
 * 
 * @author Emre Çalışır
 * @author Burak Ozgen Güner
 */
@SuppressWarnings("deprecation")
public class CalisirGuner extends ArtificialIntelligence {
	/** */
	private AiZone zone;
	/** la case occupée actuellement par le personnage */
	private AiTile caseactuelle;
	/** la case sur laquelle on veut aller */
	private AiTile pasprochain = null;
	/** */
	private boolean premiere = true;

	/** */
	private boolean bombe = false;
	/** */
	private boolean bonus = false;
	/** */
	private boolean arrive = false;
	/** */
	private boolean attaque0 = false;
	/** */
	private boolean attaque1 = false;
	/** */
	private boolean adv = false;
	/** */
	AiAction result = new AiAction(AiActionName.NONE);
	/** */
	private AiTile resultat;
	/** */
	Astar fuite;

	/** larea du jeu */
	private Map map;
	/** */
	private AiHero bomberman;

	/**
	 * méthode appelée par le moteur du jeu pour obtenir une action de votre IA
	 */
	@Override
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		zone = getPercepts();
		bomberman = zone.getOwnHero();
		caseactuelle = bomberman.getTile();
		Direction dir = Direction.NONE;
		map = new Map(zone, this);
		// System.out.println(map.actoString());
		if (bomberman != null) {
			if (!test_sur(caseactuelle)) {
				fuite();
				// System.out.println(map.actoString());

			} else if (zone.getItems().size() > 0
					&& (bomberman.getBombRange() < 3 || bomberman
							.getBombNumber() < 3)) {
				collection();
				// System.out.println(bomberman.getBombRange());
				// System.out.println(bomberman.getBombCount());
			}
			if (map.return_risque()[caseactuelle.getCol()][caseactuelle
					.getLine()] < 1500 && caseactuelle.getBombs().size() == 0) {
				/*
				 * if (map.return_risque()[caseactuelle.getCol()][caseactuelle
				 * .getLine()]!=1)
				 * System.out.println(map.return_risque()[caseactuelle
				 * .getCol()][caseactuelle .getLine()]);
				 */
				attaque();

			}
			// System.out.println(attaque0);
			if (test_sur(caseactuelle) && !attaque0) {
				attaque1();

			}
			if ((zone.getRemainingHeroes().size() < 4 || !yatildest())
					&& !bonus && test_sur(caseactuelle) && !attaque0) {
				adversaires();
				// System.out.println(adv);
			}

			if (!bonus && test_sur(caseactuelle) && !attaque0 && !adv) {
				explosion();
				// System.out.println("ezplo");
			}
			if (!bombe && pasprochain != null) {

				dir = zone.getDirection(caseactuelle, pasprochain);
			}
			// pour les directions composites
			if (dir != Direction.NONE && dir != Direction.DOWNLEFT
					&& dir != Direction.DOWNRIGHT && dir != Direction.UPLEFT
					&& dir != Direction.UPRIGHT) {
				result = new AiAction(AiActionName.MOVE, dir);
			} else if (bombe) {
				result = new AiAction(AiActionName.DROP_BOMB);

			} else if (!bombe) {

				result = new AiAction(AiActionName.NONE);
				intersection();
			}
			// if (result.equals(AiActionName.NONE))
			// collection();
			// System.out.println(map.murstoString());
			bonus = false;
			bombe = false;
			attaque0 = false;
			attaque1 = false;
			adv = false;
		}
		return result;
	}

	/**
	 * si qqn est proche de nous un max de 2 cases nous faisons nos controles et
	 * meetons un bombe
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void attaque1() throws StopRequestException {
		checkInterruption();

		int x = bomberman.getCol();
		int y = bomberman.getLine();
		if (map.returnMatrix()[bomberman.getCol()][bomberman.getLine()] == Etat.ADVERSAIRE
				|| (bomberman.getCol() + 1 < zone.getWidth() && map
						.returnMatrix()[x + 1][y] == Etat.ADVERSAIRE)
				|| (y - 1 > 0 && map.returnMatrix()[x][y - 1] == Etat.ADVERSAIRE)
				|| (y + 1 < zone.getHeight() && map.returnMatrix()[x][y + 1] == Etat.ADVERSAIRE)
				|| (x - 1 > 0 && map.returnMatrix()[x - 1][y] == Etat.ADVERSAIRE)
				|| (bomberman.getCol() + 2 < zone.getWidth() && map
						.returnMatrix()[x + 2][y] == Etat.ADVERSAIRE)
				|| (y - 2 > 0 && map.returnMatrix()[x][y - 2] == Etat.ADVERSAIRE)
				|| (y + 2 < zone.getHeight() && map.returnMatrix()[x][y + 2] == Etat.ADVERSAIRE)
				|| (x - 2 > 0 && map.returnMatrix()[x - 2][y] == Etat.ADVERSAIRE)

		)

			attaque1 = true;
		if (attaque1) {
			map.setbombeposs(zone.getOwnHero().getCol(), zone.getOwnHero()
					.getLine(), bomberman.getBombRange(), false);

			if (!cases_sures(caseactuelle).isEmpty()) {
				bombe = true;
				// System.out.println(map.returnMatrix());
				// System.out.println("attaque1");
			}
		}

	}

	/*
	 * private void attaque() throws StopRequestException { // avant tout :
	 * testd'interruption checkInterruption(); AiTile at=null;
	 * 
	 * 
	 * for (int i=0;i<map.width;i++){ checkInterruption(); for (int
	 * j=0;j<map.height;j++){ checkInterruption(); map.setbombeposs(i, j,
	 * bomberman.getBombRange(),true); List<AiHero> hero = new
	 * ArrayList<AiHero>(); hero=zone.getRemainingHeroes();
	 * Iterator<AiHero>it=hero.iterator(); AiHero temp; while (it.hasNext() &&
	 * !attaque0){ //System.out.println(at); temp=it.next(); if
	 * ((!test_chemin(zone.getTile(j, i))||
	 * longueur(bomberman.getCol(),bomberman.getLine(),i,j)<6 ) &&
	 * temp!=bomberman && cases_sures(temp.getTile()).isEmpty() &&
	 * !cases_sures(zone.getTile(j,i)).isEmpty()) { at=zone.getTile(j, i);
	 * attaque0=true; System.out.println("attag"+at);
	 * 
	 * }
	 * 
	 * }map.remplir(zone);}} if (attaque0){ if ( at!=null && at!=caseactuelle)
	 * {resultat=at; //System.out.println(at); chemin(); } else
	 * if(at==caseactuelle){ map.setbombeposs(zone.getOwnHero().getCol(),
	 * zone.getOwnHero() .getLine(), bomberman.getBombRange(),true);
	 * 
	 * if (!cases_sures(caseactuelle).isEmpty()) { bombe = true;
	 * System.out.println("attaque0"); } }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	/**
	 * si en laisssan t un bombe on laisse un adversaire entre les bombes et
	 * bombes nous le mettons si on pourra nous echapper
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void attaque() throws StopRequestException {
		checkInterruption();

		int i = bomberman.getCol();
		int j = bomberman.getLine();

		List<AiHero> hero = new ArrayList<AiHero>();
		hero = zone.getRemainingHeroes();
		Iterator<AiHero> it = hero.iterator();
		AiHero temp;
		while (it.hasNext() && !attaque0) {
			checkInterruption();
			// System.out.println(at);
			temp = it.next();
			if (!cases_sures(temp.getTile()).isEmpty()) {
				map.setbombeposs(i, j, bomberman.getBombRange(), true);
				// System.out.println(cases_sures(caseactuelle).toString());
				if (temp != null && temp != bomberman
						&& cases_sures(temp.getTile()).isEmpty()
						&& !test_sur(temp.getTile())) {
					attaque0 = true;
					// System.out.println(temp.getColor());

				}
			}
		}

		if (attaque0) {

			if (!cases_sures(caseactuelle).isEmpty()) {
				bombe = true;
				// System.out.println("attaque0");

			}
		} else
			map.remplir(zone);

	}

	/**
	 * pour exploser les murs on trouve les case ou on pourra exploser un max
	 * des murs grace a notre matrice des murs //nous utilisons cette fonction
	 * qd nous avons plus de 2 adversaires car sinon il perd bcp de temps en
	 * mettant de bombes // et il ne les laisse pas par le but dacceder en
	 * adversaire
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void explosion() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		int opt = 0;
		int res = 0;
		AiTile choix = null;
		// System.out.println(map.murstoString());
		for (int i = 0; i < map.width; i++) {
			// avant tout : test d'interruption
			checkInterruption();
			for (int j = 0; j < map.height; j++) {
				// avant tout : test d'interruption
				checkInterruption();
				opt = map.return_murs()[i][j];
				if (res < opt && !test_chemin(zone.getTile(j, i))) {
					res = opt;
					choix = zone.getTile(j, i);
				} else if (res == opt
						&& res != 0
						&& !test_chemin(zone.getTile(j, i))
						&& longueur(bomberman.getCol(), bomberman.getLine(),
								choix.getCol(), choix.getLine()) > longueur(
								bomberman.getCol(), bomberman.getLine(), i, j)) {
					res = opt;
					choix = zone.getTile(j, i);
				}
			}
		}
		resultat = choix;
		// System.out.println(resultat);
		if (!arrive)
			chemin();
		else {
			map.setbombeposs(zone.getOwnHero().getCol(), zone.getOwnHero()
					.getLine(), bomberman.getBombRange(), false);

			if (!cases_sures(caseactuelle).isEmpty()) {
				bombe = true;
				// System.out.println("asd");
				arrive = false;
			}
		}
	}

	/**
	 * il prend les adversaires dans la zone et essaie de les acceder, il les
	 * suit et qd il rencontre un mur il met un bombe etc..
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void adversaires() throws StopRequestException {
		checkInterruption();
		AiTile res = null;
		Iterator<AiHero> it = getPercepts().getRemainingHeroes().iterator();
		// System.out.println(zone.getRemainingHeroes().toString());

		List<AiHero> destination = new ArrayList<AiHero>();

		AiHero temp;
		while (it.hasNext()) {
			checkInterruption();
			temp = it.next();
			// System.out.println(temp);
			if (temp != bomberman)
				destination.add(temp);

		}

		if (!destination.isEmpty()) {
			Iterator<AiHero> iterator = destination.iterator();
			AiTile option;
			while (iterator.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIR

				option = iterator.next().getTile();
				adv = true;
				if (res == null
						|| longueur(bomberman.getCol(), bomberman.getLine(),
								res.getCol(), res.getLine()) > longueur(
								bomberman.getCol(), bomberman.getLine(),
								option.getCol(), option.getLine()))
					res = option;
			}
			if (res != null && res != caseactuelle) {
				Astar dest = new Astar(map, bomberman.getCol(),
						bomberman.getLine(), res.getCol(), res.getLine(), this);
				AiTile prochaine = null;

				if (dest != null && dest.findPathreach()) {

					Deque<Integer> deque = dest.getPath();

					if (!deque.isEmpty()) {
						int tempo = deque.poll();
						prochaine = zone.getTile(deque.poll(), tempo);
					}
					if (prochaine == caseactuelle) {

						if (!deque.isEmpty()) {
							int tempo = deque.poll();
							prochaine = zone.getTile(deque.poll(), tempo);

						}
					}
					if (map.returnMatrix()[prochaine.getCol()][prochaine
							.getLine()] == Etat.DESTRUCTIBLES) {
						map.setbombeposs(bomberman.getCol(),
								bomberman.getLine(), bomberman.getBombRange(),
								false);
						if (!cases_sures(caseactuelle).isEmpty()
								&& bomberman.getBombCount() < 2) {
							bombe = true;
							// System.out.println("explo");

						}
					} else
						pasprochain = prochaine;
				}

			}
		}
	}

	/**
	 * pour les bonus on lutilise qd on a moins de 3 bonus de nimporte quel
	 * bonus pour nee pas empecher lattaque
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void collection() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		List<AiItem> destination = new ArrayList<AiItem>();

		destination = zone.getItems();
		if (!destination.isEmpty()) {
			Iterator<AiItem> iterator = destination.iterator();
			AiTile option;
			// comm eon est ecrit celui ce qu'on a moins de 2 est comme but
			AiItemType type = null;
			if (bomberman.getBombNumber() < 2)
				type = AiItemType.EXTRA_BOMB;
			else if (bomberman.getBombRange() < 2)
				type = AiItemType.EXTRA_FLAME;

			while (iterator.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE

				option = iterator.next().getTile();
				if (!test_chemin(option)) {
					if (premiere) {
						resultat = option;
						premiere = false;
					}

					bonus = true;

					if (type != null) {
						if (type == AiItemType.EXTRA_BOMB
								&& option.getItems().iterator().next()
										.getType() == AiItemType.EXTRA_BOMB)
							resultat = option;
						if (type == AiItemType.EXTRA_FLAME
								&& option.getItems().iterator().next()
										.getType() == AiItemType.EXTRA_FLAME)
							resultat = option;

					}

					else if (longueur(bomberman.getCol(), bomberman.getLine(),
							resultat.getCol(), resultat.getLine()) > longueur(
							bomberman.getCol(), bomberman.getLine(),
							option.getCol(), option.getLine()))
						resultat = option;
				}
			}
			// System.out.println(resultat);
			if (bonus)
				chemin();

		}
	}

	/**
	 * ppour nous enfuire
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void fuite() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		resultat = meilleur();
		chemin();
	}

	/**
	 * pour utiliser notre astar il nous donne le chemin a la case sur choisie
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	void chemin() throws StopRequestException {
		checkInterruption();
		// avant tout : test d'interruption
		checkInterruption();
		premiere = true;

		if (resultat != null && resultat != caseactuelle) {
			Astar dest = new Astar(map, bomberman.getCol(),
					bomberman.getLine(), resultat.getCol(), resultat.getLine(),
					this);

			AiTile prochaine = null;

			if (dest != null && dest.findPath()) {

				Deque<Integer> deque = dest.getPath();

				if (!deque.isEmpty()) {
					int temp = deque.poll();
					prochaine = zone.getTile(deque.poll(), temp);
				}
				if (prochaine == caseactuelle) {

					if (!deque.isEmpty()) {
						int temp = deque.poll();
						prochaine = zone.getTile(deque.poll(), temp);

					}
				}
			}
			pasprochain = prochaine;
		}

	}

	/**
	 * pour trover la case que le chemin est le plus sur on profite de matrice
	 * // de risques quon a defini dans map
	 * 
	 * @return ?
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private AiTile meilleur() throws StopRequestException {
		checkInterruption();
		AiTile meilleur_resultat = null;
		Astar meilleur_astar;

		Collection<AiTile> destination = cases_sures(caseactuelle);
		Iterator<AiTile> iterator = destination.iterator();
		AiTile option;
		int opt = 0;
		int res = 100000000;

		if (!destination.isEmpty()) {

			while (iterator.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				option = iterator.next();
				opt = 0;

				meilleur_astar = new Astar(map, bomberman.getCol(),
						bomberman.getLine(), option.getCol(), option.getLine(),
						this);
				if (meilleur_astar.findPath()) {
					Deque<Integer> deque = meilleur_astar.getPath();

					while (!deque.isEmpty()) {
						// avant tout : test d'interruption
						checkInterruption();
						int temp = deque.poll();
						opt = opt + map.return_risque(temp, deque.poll());
					}
					if (opt < res) {
						res = opt;
						meilleur_resultat = option;

					} else if (opt == res) {
						if (cases_sures(option).size() > cases_sures(
								meilleur_resultat).size()
								|| (ferme(meilleur_resultat) > ferme(option))) {
							meilleur_resultat = option;

						}

						if (opt == res
								&& cases_sures(option).size() == cases_sures(
										meilleur_resultat).size()
								&& (ferme(meilleur_resultat) == ferme(option))) {

							if (longueur(bomberman.getCol(),
									bomberman.getLine(),
									meilleur_resultat.getCol(),
									meilleur_resultat.getLine()) > longueur(
									bomberman.getCol(), bomberman.getLine(),
									option.getCol(), option.getLine()))
								meilleur_resultat = option;

							// System.out.println(res);
						}
					}

				}

			}
		}
		// System.out.println(res);
		return meilleur_resultat;

	}

	/**
	 * on trouve les cases surs donc les cases qui ne sont pas menacés par des
	 * flammes bombes feus
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private Collection<AiTile> cases_sures(AiTile tile)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		Collection<AiTile> destination = new ArrayList<AiTile>();

		for (int line = 0; line < zone.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if (tile.getCol() != col || tile.getLine() != line) {
					fuite = new Astar(map, tile.getCol(), tile.getLine(), col,
							line, this);

					if (test_sur(zone.getTile(line, col))
							&& zone.getTile(line, col).getBlocks().size() == 0) {
						// sil ya du chemin
						if (fuite.findPath())

							destination.add(zone.getTile(line, col));
					}
				}
			}
		}

		return destination;

	}

	/**
	 * si une case est entoure par des bombes et murs ce nest pas une case quon
	 * doit choisir donc nous utilisons
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	// cette methode pour vor si la case est comme une impasse
	private int ferme(AiTile tile) throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		int res = 0;
		if (bomberman.getLine() + 1 < zone.getWidth()
				&& (tile.getNeighbor(Direction.RIGHT).getBlocks().size() > 0
						|| map.returnMatrix()[bomberman.getCol() + 1][bomberman
								.getLine()] == Etat.FLAMMES
						|| map.returnMatrix()[bomberman.getCol() + 1][bomberman
								.getLine()] == Etat.DANGER || map
						.return_accessibilite()[bomberman.getCol() + 1][bomberman
						.getLine()] == Etat.INACCESSIBLE))
			res++;
		if (bomberman.getLine() + 1 < zone.getHeight()
				&& (tile.getNeighbor(Direction.DOWN).getBlocks().size() > 0
						|| map.returnMatrix()[bomberman.getCol()][bomberman
								.getLine() + 1] == Etat.FLAMMES
						|| map.returnMatrix()[bomberman.getCol()][bomberman
								.getLine() + 1] == Etat.DANGER || map
						.return_accessibilite()[bomberman.getCol()][bomberman
						.getLine() + 1] == Etat.INACCESSIBLE))
			res++;
		if (bomberman.getCol() - 1 > 0
				&& (tile.getNeighbor(Direction.LEFT).getBlocks().size() > 0
						|| map.returnMatrix()[bomberman.getCol() - 1][bomberman
								.getLine()] == Etat.FLAMMES
						|| map.returnMatrix()[bomberman.getCol() - 1][bomberman
								.getLine()] == Etat.DANGER || map
						.return_accessibilite()[bomberman.getCol() - 1][bomberman
						.getLine()] == Etat.INACCESSIBLE))
			res++;
		if (bomberman.getLine() - 1 > 0
				&& (tile.getNeighbor(Direction.UP).getBlocks().size() > 0
						|| map.returnMatrix()[bomberman.getCol()][bomberman
								.getLine() - 1] == Etat.FLAMMES
						|| map.returnMatrix()[bomberman.getCol()][bomberman
								.getLine() - 1] == Etat.DANGER || map
						.return_accessibilite()[bomberman.getCol()][bomberman
						.getLine() - 1] == Etat.INACCESSIBLE))
			res++;
		return res;

	}

	/**
	 * distance euclidien
	 * 
	 * @param x
	 *            Description manquante !
	 * @param y
	 *            Description manquante !
	 * @param x1
	 *            Description manquante !
	 * @param y2
	 *            Description manquante !
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	int longueur(int x, int y, int x1, int y2) throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();

		return (int) Math.sqrt((y2 - y) * (y2 - y) + (x1 - x) * (x1 - x));

	}

	/**
	 * pour controler si on est en securite
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private boolean test_sur(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (tile.getBombs().size() > 0
				|| tile.getFires().size() > 0
				|| tile.getBlocks().size() > 0
				|| map.returnMatrix()[tile.getCol()][tile.getLine()] == Etat.FLAMMES
				|| map.returnMatrix()[tile.getCol()][tile.getLine()] == Etat.FLAMMESPOSS
				|| map.returnMatrix()[tile.getCol()][tile.getLine()] == Etat.BOMBEPOSS
				|| map.returnMatrix()[tile.getCol()][tile.getLine()] == Etat.DANGER
				|| map.return_accessibilite()[tile.getCol()][tile.getLine()] == Etat.INACCESSIBLE)
			return false;

		else
			return true;
	}

	/**
	 * sil ya des flammes dans le chmin cest pas la peine de nous risquer pour
	 * un adversaire u un bonus
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private boolean test_chemin(AiTile tile) throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		boolean res = true;
		if (tile == caseactuelle && !bonus && !attaque0) {
			arrive = true;

		}

		Astar bonus_astar = new Astar(map, bomberman.getCol(),
				bomberman.getLine(), tile.getCol(), tile.getLine(), this);
		if (bonus_astar != null && tile != caseactuelle
				&& bonus_astar.findPath()) {
			res = false;
			Deque<Integer> deque = bonus_astar.getPath();
			// sil yaura dexplosion sur le chemin cest pas la peine deessayer de
			// prendre le bonus
			while (!deque.isEmpty()) {
				// avant tout : test d'interruption
				checkInterruption();
				int temp = deque.poll();
				int temp2 = deque.poll();
				if (map.returnMatrix()[temp][temp2] == Etat.FLAMMES
						|| map.returnMatrix()[temp][temp2] == Etat.DANGER)
					res = true;

			}
		}
		return res;
	}

	/**
	 * pour controler sil ya encore des murs destructibles quon peut acceder
	 * 
	 * @return ? Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private boolean yatildest() throws StopRequestException {
		checkInterruption();
		boolean res = false;
		Iterator<AiBlock> it = zone.getBlocks().iterator();
		while (it.hasNext() && !res) {
			checkInterruption();
			AiBlock temp = it.next();
			Astar a = new Astar(map, bomberman.getCol(), bomberman.getLine(),
					temp.getCol(), temp.getLine(), this);
			if (temp.isDestructible() && a.findPathreach())
				res = true;
		}
		return res;
	}

	/**
	 * si one est en intersection de deux bombes on choist daller a la portee de
	 * celui qui a encore plue de temps que lautre a son explosion
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	private void intersection() throws StopRequestException {
		checkInterruption();
		if (map.returnMatrix()[bomberman.getCol()][bomberman.getLine()] == Etat.DANGER) {
			int res = map
					.return_risque(bomberman.getCol(), bomberman.getLine());

			if (map.return_accessibilite()[bomberman.getCol() + 1][bomberman
					.getLine()] == Etat.ACCESSIBLE
					&& map.return_risque(bomberman.getCol() + 1,
							bomberman.getLine()) < res) {
				res = map.return_risque(bomberman.getCol() + 1,
						bomberman.getLine());
				result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
			} else if (map.return_accessibilite()[bomberman.getCol()][bomberman
					.getLine() + 1] == Etat.ACCESSIBLE
					&& map.return_risque(bomberman.getCol(),
							bomberman.getLine() + 1) < res) {
				res = map.return_risque(bomberman.getCol(),
						bomberman.getLine() + 1);
				result = new AiAction(AiActionName.MOVE, Direction.DOWN);
			} else if (map.return_accessibilite()[bomberman.getCol()][bomberman
					.getLine() - 1] == Etat.ACCESSIBLE
					&& map.return_risque(bomberman.getCol(),
							bomberman.getLine() - 1) < res) {
				res = map.return_risque(bomberman.getCol(),
						bomberman.getLine() - 1);
				result = new AiAction(AiActionName.MOVE, Direction.UP);
			} else if (map.return_accessibilite()[bomberman.getCol() - 1][bomberman
					.getLine()] == Etat.ACCESSIBLE
					&& map.return_risque(bomberman.getCol() - 1,
							bomberman.getLine()) < res) {
				res = map.return_risque(bomberman.getCol() - 1,
						bomberman.getLine());
				result = new AiAction(AiActionName.MOVE, Direction.LEFT);
			}
		}
	}
}
