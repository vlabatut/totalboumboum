package org.totalboumboum.ai.v200809.ais.erisikpektas.v2c;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiStateName;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğa Erişik
 * @author Abdurrahman Pektaş
 *
 */
public class ErisikPektas extends ArtificialIntelligence {
	private Map map;
	/** la case occupée actuellement par le personnage */
	private AiTile caseactuelle;
	/** la case sur laquelle on veut aller */
	private AiTile caseprochaine = null;

	private AiTile casedestination = null;
	/** larea du jeu */
	private AiZone tous;
	/** les autres joueurs */
	Collection<AiHero> autres;
	/** nous */
	AiHero bomberman;

	/** notrre x y */
	private int x, y;
	/**
	 * une llong que nous allons utiliser pour prendre normalduratin dun bombe
	 * et une int pour le prendre une seule fois
	 */
	long bombtimer = 0;
	private int premier = 0;

	/** deux tableaux que nous allons utiliser pour stocker les temps des bombes nous  mettons 50 pour que ce soit grandecestun nombre aleatoire*/
	long[][] temps = new long[50][50];
	long[][] danger = new long[50][50];

	/** les listes pour jouer */
	private LinkedList<AiTile> choix;
	private LinkedList<Astar> choixas;
	private LinkedList<Astar> exploas;
	private LinkedList<AiTile> explo;
	private LinkedList<AiTile> moresafe;

	/** pour la classe etat */
	Etat etat;
	/** les Astar */
	/** pour la fonc choix */
	Astar choixAstar;
	Astar choice;
	/** pour la fonc court */
	Astar courtAstar;
	/** pour la fonc explosion */
	Astar explosionAstar;
	/** pour la fonc securtie */
	Astar securite;

	AiAction result = new AiAction(AiActionName.NONE);

	/** les boolean qui portent les noms de leurs fonctions */

	/** qd il choisit une destination il ne dot pas le changer */
	boolean court = false;
	/** sil peut attaquer il ne doit pas entrer a dautres fonctions */
	boolean attaque = false;
	/**
	 * de temps en temps il laisse un bombe qd il nest pas a cote dun mur et il
	 * se suicide cela sert a lempecher
	 */
	boolean explosion = false;
	/**
	 * nous lutilisons pous quil nentre pas a explosion sil ya bonus a collecter
	 */
	boolean choisir = false;
	/** il faut attaquer qd il y a qqn de proche */
	boolean alentours = false;
	boolean endanger = false;
	boolean aleatoire = false;
	boolean bouger = false;
	/** pour la fonc court la meilleure destination */
	AiTile response;

	/** si case prochaine est dangereuse cherchons un autre */
	AiTile pasdedanger;

	int h = 0;

	@SuppressWarnings("static-access")
	public AiAction processAction() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();

		tous = getPercepts();
		bomberman = tous.getOwnHero();
		map = new Map(tous,this);
		// s=map.returnMatrix();

		x = bomberman.getCol();
		y = bomberman.getLine();
		// on accede a la case ou se trouve notre bomberman
		caseactuelle = bomberman.getTile();

		if (bomberman != null) {
			Direction direction = Direction.NONE;
			// si cest la premiere fois il faut linitialisation
			if (caseprochaine == null) {

				init();
			}

			else
			// sinon il faut avancer
			{
				avancer();
			}

			// definissons notre direction selon notre caseactuelle et prochaine
			/*
			 * AiHero yo=tous.getHeroes().iterator().next(); if
			 * (yo.getColor()==PredefinedColor.WHITE)
			 * System.out.println(tous.getHeroes
			 * ().iterator().next().getState().getName());
			 */
			if (caseprochaine != null) {

				
				if (map.isNoWhereElse(caseprochaine.getCol(), caseprochaine
						.getLine()))
					if (!isdangerous(caseprochaine.getCol(), caseprochaine
							.getLine())) {
						direction = getPercepts().getDirection(caseactuelle,
								caseprochaine);

					} else {
						caseprochaine = sidanger(false);
						if (caseprochaine != null
								&& caseprochaine.getCol() < map.width
								&& caseprochaine.getLine() < map.height
								&& caseprochaine.getLine() > 0
								&& caseprochaine.getCol() > 0)
							direction = getPercepts().getDirection(
									caseactuelle, caseprochaine);

					}}


			// et le controle des directions composites pour que le jeu ne jette
			// pas dexceptions

			if (direction!=Direction.NONE&&!aleatoire && !attaque && !explosion && !alentours
					&& direction != Direction.DOWNLEFT
					&& direction != Direction.DOWNRIGHT
					&& direction != Direction.UPLEFT
					&& direction != Direction.UPRIGHT) {

				// System.out.println(map.returnMatrix() [
				// caseprochaine.getCol()][caseprochaine.getLine()]);

				result = new AiAction(AiActionName.MOVE, direction);
			}

		}

		if ((explosion || aleatoire)
				&& result.getName() == (AiActionName.DROP_BOMB)) {
			if ((map.returnMatrix()[tous.getOwnHero().getCol() + 1][tous
					.getOwnHero().getLine()] == etat.DESTRUCTIBLES
					|| map.returnMatrix()[tous.getOwnHero().getCol()][tous
							.getOwnHero().getLine() - 1] == etat.DESTRUCTIBLES
					|| map.returnMatrix()[tous.getOwnHero().getCol() - 1][tous
							.getOwnHero().getLine()] == etat.DESTRUCTIBLES || map
					.returnMatrix()[tous.getOwnHero().getCol()][tous
					.getOwnHero().getLine() + 1] == etat.DESTRUCTIBLES)) {
				result = new AiAction(AiActionName.DROP_BOMB);
			} else
				result = new AiAction(AiActionName.NONE);

		}
		explosion = false;
		aleatoire = false;
		bouger = false;
		alentours = false;

		// System.out.println(result.getName());

		return result;
	}

	// pour linitialisation de bomberman

	private void init() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		caseprochaine = caseactuelle;

	}

	// pour laiisser de la bombe

	@SuppressWarnings("static-access")
	public AiAction laisser() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		map.setbombeposs(tous.getOwnHero().getCol(), tous.getOwnHero()
				.getLine(), bomberman.getBombRange());
		AiAction bombe;
		if (!securite(x, y, 0).isEmpty()) {
			court = false;
			if ((explosion || aleatoire)) {
				if ((map.returnMatrix()[tous.getOwnHero().getCol() + 1][tous
						.getOwnHero().getLine()] == etat.DESTRUCTIBLES
						|| map.returnMatrix()[tous.getOwnHero().getCol()][tous
								.getOwnHero().getLine() - 1] == etat.DESTRUCTIBLES
						|| map.returnMatrix()[tous.getOwnHero().getCol() - 1][tous
								.getOwnHero().getLine()] == etat.DESTRUCTIBLES || map
						.returnMatrix()[tous.getOwnHero().getCol()][tous
						.getOwnHero().getLine() + 1] == etat.DESTRUCTIBLES))
					bombe = new AiAction(AiActionName.DROP_BOMB);
				else
					bombe = new AiAction(AiActionName.NONE);
			} else
				bombe = new AiAction(AiActionName.DROP_BOMB);
		} else {
			map.removebombe();
			bombe = new AiAction(AiActionName.NONE);
		}
		return bombe;
	}

	/**
	 * fonction qui sert a avancer selon le statut de jeu sil est en danger il
	 * senfuit(court()) sil ya desbonus il les ramasse sil ya un autre hero il
	 * le suit(choisir) si les bonus et les heros se trouvent derrieres les murs
	 * destructibles il laiise des bombes( exlploser()) sil existe un autre
	 * heros proche au notre il laisse des bombes grace a la fonc alentours
	 */

	@SuppressWarnings("static-access")
	public void avancer() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		if (tous.getBombs().size() != 0 && premier == 0) {
			premier++;
			bombtimer = tous.getBombs().iterator().next().getNormalDuration();
		}
		setdangerous();

		// if (isdangerous(1,13))
		// System.out.println(bombtimer);

		// System.out.println(getdangerous(1,13));

		/*
		 * if (reus < 3 && ((caseprochaine.getBombs().size() > 0 &&
		 * caseprochaine .getBombs().iterator().next().getColor() != bomberman
		 * .getColor()) || (caseactuelle.getBombs().size() > 0 && caseactuelle
		 * .getBombs().iterator().next().getColor() != bomberman .getColor())))
		 * {
		 * 
		 * result = new AiAction(AiActionName.PUNCH); qx=0; punch = true;
		 * 
		 * 
		 * }
		 * 
		 * morte = false;
		 */

		/**
		 * sil se trouve un autre bomber si proche au notre il controle sil
		 * poourrait senfuire et si oui il laisse un bombe
		 */
		if (alentours() && (map.isWalkable(x, y)) && !attaque) {
			// danger=new LinkedList<AiTile>();
			map.setbombeposs(x, y, bomberman.getBombRange());
			map.setdanger();
			if (securite(x, y, 0).isEmpty()) {
				map.removebombe();
			} else {

				court = false;

				// System.out.println("alen");
				alentours = true;
				result = laisser();

			}

		}

		endanger = false;
		map.getbombs();
		map.getfires();
		/**
		 * normallement nous ne pouvons pas marcher en case de danger donc nous
		 * avons cree une autre type de courir qui utilise un autre type de
		 * secur,te
		 */
		if (map.returnMatrix()[x][y] == etat.DANGER

				|| (caseactuelle != caseprochaine && caseprochaine.getBombs()
						.size() > 0)) {
			map.getbombs();
			// System.out.println("courtdanger");
			endanger = true;
			court();
			court = true;

			choisir = false;

		}

		// si on est en danger (dans la range des flammes)
		else if (!controle2(caseactuelle)
				&& (!attaque || caseactuelle.getBombs().size() > 0)) {
			// if(caseactuelle.getBombs().size()>0)
			// System.out.println("court");
			/*
			 * if (securite(x,y).isEmpty() ) mort(); else
			 */

			h++;
			if (h > 5)
				endanger = true;
			if (endanger)
				h = 0;

			court();
			// System.out.println(casedestination);
			choisir = false;

			court = true;

		}

		/**
		 * si on nest pas en danger et il nya personne autour de nous il faut
		 * verifier si on peut acceder a un bonus ou adversaire
		 */

		else if (!alentours && !attaque && !endanger) {
			// System.out.println("choisir");
			court = false;

			choisir();
			choisir = false;

		}

		Deque<Integer> pencilcase = null;

		// nous utilisons notre classe astar pour aller a la destination
		if (casedestination != null && casedestination.getCol() < map.width
				&& casedestination.getLine() < map.height
				&& casedestination != caseactuelle
				&& controle2(casedestination) && (court || !choix.isEmpty())
				&& !alentours) {

			courtAstar = new Astar(map, x, y, casedestination.getCol(),
					casedestination.getLine(),this);

			if ((court && !endanger && courtAstar.findPath())
					|| (court && endanger && courtAstar.findPathdang())
					|| (!court)) {

				// nous obtenons la route de la destination de la fonction
				// choisir()
				if (court == false && choice != null) {

					pencilcase = choice.getPath();

				}

				// nous obtenons la route de la destination de la fonction
				// court()
				else {
					pencilcase = courtAstar.getPath();
					/*
					 * if (endanger) System.out.println(pencilcase);
					 */
				}
			}

			if (pencilcase != null && !pencilcase.isEmpty()) {
				AiTile procpos;

				int temp = pencilcase.poll();
				procpos = tous.getTile(pencilcase.poll(), temp);
				choisir = true;
				if (procpos == (caseactuelle)) {
					choisir = false;
					if (!pencilcase.isEmpty()) {
						temp = pencilcase.poll();
						choisir = true;

						procpos = tous.getTile(pencilcase.poll(), temp);
					} else
						choisir = false;
				}
				if (Math.abs(procpos.getCol() - caseactuelle.getCol())
						+ Math.abs(procpos.getLine() - caseactuelle.getLine()) == 1)
					caseprochaine = procpos;
				else
					court = false;
				if (isdangerous(caseprochaine.getCol(), caseprochaine.getLine()))
					caseprochaine = caseactuelle;

			} else {

				result = new AiAction(AiActionName.MOVE, Direction.NONE);

			}

		}// nous sommes venus a la destination donc arretons
		else if ((casedestination == caseactuelle || pencilcase == null)
				&& !alentours && controle2(caseactuelle)) {

			court = false;

			// System.out.println("controlle");

			result = new AiAction(AiActionName.MOVE, Direction.NONE);
		}

		/**
		 * si on nest pas en danger, il nya rien a acceder et il nya personee
		 * autour de nous on controle si on peut acceder a qqch en explosant les
		 * murs destructibles
		 */
		if (!choisir && !alentours && !attaque && !court) {
			court = false;
			// System.out.println("explosion");

			explosion();

		}
		attaque = false;
		if (tous.getHeroes().size() == 2)
			attaque(false);
		else
			attaque(true);

		/*if (!attaque && !alentours && controle2(caseactuelle) && !choisir
				&& !bouger && map.isWalkable(x, y)
				&& tous.getItems().size() == 0 && !explosion) {
			map.setbombeposs(x, y, bomberman.getBombRange());
			map.setdanger();
			if (!securite(x, y, 0).isEmpty())
				result = laisser();
			else
				map.removebombe();
			aleatoire = true;
			// System.out.println(aleatoire);
		}*/

		// if(bomberman.getState().getName()==AiStateName.STANDING &&
		// getdangerous(x, y)>1200)
		// caseprochaine=sidanger(true);
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOS FONCTIONS POUR ACCEDER AU BONUS ET LES COLLECTER
	// OU POUR ACCEDER AUX ADVERSAIRES//
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public AiTile sidanger(boolean selection) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (!isdangerous(x, y) && !selection) {
			// System.out.println("seuriteye girrriddidiiin");
			pasdedanger = tous.getTile(y, x);
		} else if (x + 1 < map.width && map.isNoWhereElse(x + 1, y)
				&& !isdangerous(x + 1, y)) {
			pasdedanger = tous.getTile(y, x + 1);
			// System.out.println("x+1");

		}

		else if (x - 1 > 0 && map.isNoWhereElse(x - 1, y)
				&& !isdangerous(x - 1, y)) {
			pasdedanger = tous.getTile(y, x - 1);
			// System.out.println("sx-1");

		} else if (y + 1 < map.height && map.isNoWhereElse(x, y + 1)
				&& !isdangerous(x, y + 1)) {
			// System.out.println("y+1");
			pasdedanger = tous.getTile(y + 1, x);

		} else if (y - 1 > 0 && map.isNoWhereElse(x, y - 1)
				&& !isdangerous(x, y - 1)) {
			pasdedanger = tous.getTile(y - 1, x);
			// System.out.println("sy-1");
		}
		return pasdedanger;
	}

	/**
	 * pour controler sil se trouve un bonus ou un adversaire dans le jeu quon
	 * peut acceder en explosant les murs destructibles
	 */
	@SuppressWarnings("static-access")
	public void explosion() throws StopRequestException {
		checkInterruption();

		exploas = new LinkedList<Astar>();
		explo = new LinkedList<AiTile>();
		// danger=new LinkedList<AiTile>();

		Astar fin = null;

		for (int i = 0; i < map.width; i++) {
			checkInterruption();
			for (int j = 0; j < map.height; j++) {
				checkInterruption();
				if ((tous.getTile(j, i).getItem() != null || map.returnMatrix()[i][j] == etat.ADVERSAIRE
						&& controle2(tous.getTile(j, i)))
						&& (i != x || j != y) && existe(tous.getTile(j, i))) {
					explosionAstar = new Astar(map, x, y, i, j,this);

					if (explosionAstar.findPathexp()) {

						explo.add(tous.getTile(j, i));
						exploas.add(explosionAstar);

					}
				}
			}
		}
		if (!explo.isEmpty()) {

			Iterator<AiTile> it = explo.iterator();
			Iterator<Astar> ita = exploas.iterator();
			AiTile reponse = null;
			int reponsex = 120, reponsey = 120;

			AiTile possible;
			Astar reponsestar = null;
			Astar reponsestarad = null;

			Astar possiblestar;
			while (it.hasNext()) {
				checkInterruption();
				possible = it.next();
				possiblestar = ita.next();
				// nous pprenons le plus proche au cas des plusieurs possiblite
				if (Math.sqrt((reponsex - x) * (reponsex - x) + (reponsey - y)
						* (reponsey - y)) > Math.sqrt((x - possible.getCol())
						* (x - possible.getCol()) + (y - possible.getLine())
						* (y - possible.getLine()))) {
					// notre priorite sont les bonus donc nous creons deux a
					// star pour les adv et les bonus
					if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.POINT) {
						reponse = possible;
						reponsestar = possiblestar;
						reponsex = reponse.getCol();
						reponsey = reponse.getLine();

					}
					if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.ADVERSAIRE) {
						reponsex = possible.getCol();
						reponsey = possible.getLine();
						reponsestarad = possiblestar;

					}
				}
				// si les lointudes des deux possiblite sont egales notre
				// bombermman ne peut pas choisir et il change son but
				// a toutes les appels de cette fonc donc on a mis reponsex <=
				// reponsey && x<y pou faire plus difficile
				// le changement car sinon il fait tjrs les memes mvts
				else if (Math.sqrt((reponsex - x) * (reponsex - x)
						+ (reponsey - y) * (reponsey - y)) == Math
						.sqrt((x - possible.getCol()) * (x - possible.getCol())
								+ (y - possible.getLine())
								* (y - possible.getLine()))) {

					if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.POINT
							&& reponsex <= reponsey && x < y) {
						reponse = possible;
						reponsestar = possiblestar;
						reponsex = reponse.getCol();
						reponsey = reponse.getLine();

					} else if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.ADVERSAIRE
							&& reponsex <= reponsey && x < y) {
						reponsex = possible.getCol();
						reponsey = possible.getLine();
						reponsestarad = possiblestar;

					}

				}

			}
			// sil ny a aucun bonus nous prenons ladversaire le plus proche
			// comme la destination
			if (reponse == null) {

				fin = reponsestarad;
				// sinon cets le bonus
			} else {

				fin = reponsestar;

			}

		}
		// cette partie sert a aller jusqua la destination ou exploser les
		// obstacles
		AiTile temporaire = null;
		if (fin != null) {
			Deque<Integer> w = fin.getPath();

			if (!w.isEmpty()) {

				int temp = w.poll();
				temporaire = tous.getTile(w.poll(), temp);

				if (temporaire.equals(caseactuelle)) {

					if (!w.isEmpty()) {
						temp = w.poll();

						temporaire = tous.getTile(w.poll(), temp);
					} else
						temporaire = null;
				}
				// sil y a un mur destructibles on controle si on peut laisser
				// un bombe et courir si oui on le fait sinon on fai t rien
				if (temporaire != null
						&& ((tous.getTile(temporaire.getLine(),
								temporaire.getCol()).getBlock() != null && tous
								.getTile(temporaire.getLine(),
										temporaire.getCol()).getBlock()
								.isDestructible() == true) || map
								.returnMatrix()[temporaire.getCol()][temporaire
								.getLine()] == etat.ADVERSAIRE)) {

					map.setbombeposs(x, y, bomberman.getBombRange());
					map.setdanger();
					if (securite(x, y, 0).isEmpty()) {
						map.removebombe();
						result = new AiAction(AiActionName.MOVE, Direction.NONE);

					} else {

						if (Math.abs(temporaire.getCol() - x)
								+ Math.abs(temporaire.getLine() - y) == 1)
							explosion = true;
						result = laisser();
					}

				} else {
					if (temporaire != null && controle2(temporaire)) {

						caseprochaine = temporaire;
						bouger = true;

					} else {
						result = new AiAction(AiActionName.MOVE, Direction.NONE);

					}
				}
			}

		} else {
			result = new AiAction(AiActionName.MOVE, Direction.NONE);

		}

	}

	/**
	 * cette fonction sert a controler sil se trouve un bonus ou adversaire quon
	 * peut acceder en marchant par des cases sures
	 */
	@SuppressWarnings("static-access")
	public void choisir() throws StopRequestException {
		checkInterruption();
		choix = new LinkedList<AiTile>();
		choixas = new LinkedList<Astar>();

		int reponsex = 120, reponsey = 120;

		for (int i = 0; i < map.width; i++) {
			checkInterruption();
			for (int j = 0; j < map.height; j++) {
				checkInterruption();
				// i j hata veriyo eger destination ayn bulundugu yerse
				if ((tous.getTile(j, i).getItem() != null || (map
						.returnMatrix()[i][j] == etat.ADVERSAIRE
						&& tous.getHeroes().size() == 2 && tous.getItems()
						.size() == 0))
						&& existe(tous.getTile(j, i))
						&& controle2(tous.getTile(j, i)) && (i != x || j != y)) {

					choixAstar = new Astar(map, x, y, i, j,this);

					if (choixAstar.findPathchoix()) {

						choix.add(tous.getTile(j, i));
						choixas.add(choixAstar);

					}
				}
			}
		}

		/**
		 * cest la meme chose qu'explosion sauf quon trouvait la route dans la
		 * fonc pour explosion et ici on lenvoie a la fonc davancer(). donc le
		 * bonus est notre priorite mais sil ny a aucun bonus dans le jeu on
		 * prend ladversaire plus proche comme destination
		 */
		if (!choix.isEmpty()) {

			Iterator<AiTile> it = choix.iterator();
			Iterator<Astar> ita = choixas.iterator();
			AiTile reponse = null;

			AiTile possible;
			Astar reponsestar = null;
			Astar reponsestarad = null;

			Astar possiblestar;
			while (it.hasNext()) {
				checkInterruption();
				possible = it.next();
				possiblestar = ita.next();
				if (Math.sqrt((reponsex - x) * (reponsex - x) + (reponsey - y)
						* (reponsey - y)) > Math.sqrt((x - possible.getCol())
						* (x - possible.getCol()) + (y - possible.getLine())
						* (y - possible.getLine()))) {
					if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.POINT) {
						reponse = possible;
						reponsestar = possiblestar;
						reponsex = reponse.getCol();
						reponsey = reponse.getLine();

					} else if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.ADVERSAIRE) {
						reponsex = possible.getCol();
						reponsey = possible.getLine();
						reponsestarad = possiblestar;

					}
				} else if (Math.sqrt((reponsex - x) * (reponsex - x)
						+ (reponsey - y) * (reponsey - y)) == Math
						.sqrt((x - possible.getCol()) * (x - possible.getCol())
								+ (y - possible.getLine())
								* (y - possible.getLine()))) {

					if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.POINT
							&& reponsex <= reponsey && x < y) {
						reponse = possible;
						reponsestar = possiblestar;
						reponsex = reponse.getCol();
						reponsey = reponse.getLine();

					} else if (map.returnMatrix()[possible.getCol()][possible
							.getLine()] == etat.ADVERSAIRE
							&& reponsex <= reponsey && x < y) {
						reponsex = possible.getCol();
						reponsey = possible.getLine();
						reponsestarad = possiblestar;

					}
				}
			}
			if (reponse == null) {

				choice = reponsestarad;
				if (reponsex != 120)
					casedestination = tous.getTile(reponsey, reponsex);

			} else {

				choice = reponsestar;

				casedestination = tous.getTile(reponse.getLine(), reponse
						.getCol());

			}

		}

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOS FONCTIONS SUR LES TEMPS DEXPLOSION DES BOMBES //
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * cest une fonc qui collecte les temps passe des la creation des bombes et
	 * des flmmes feus
	 */

	@SuppressWarnings("static-access")
	private void setdangerous() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		map.getbombs();
		map.getfires();

		for (int col = 0; col < tous.getWidth(); col++) {
			checkInterruption(); // APPEL OBLIGATOIRE

			for (int line = 0; line < tous.getHeight(); line++) {
				checkInterruption(); // APPEL OBLIGATOIRE

				if (temps[col][line] == '\0'
						|| map.returnMatrix()[col][line] == etat.LIBRE) {
					temps[col][line] = 0;
					danger[col][line] = 0;
				} else if (danger[col][line] == 0
						&& System.currentTimeMillis() - temps[col][line] > bombtimer)
					temps[col][line] = System.currentTimeMillis();

				if (map.returnMatrix()[col][line] == etat.DANGER) {
					if (danger[col][line] == 0)
						danger[col][line] = System.currentTimeMillis();

				}

				else if (map.returnMatrix()[col][line] == etat.FLAMMES
						|| tous.getTile(line, col).getBombs().size() > 0
						|| map.returnMatrix()[col][line] == etat.BOMBEDANGER) {
					if (temps[col][line] == 0) {
						temps[col][line] = System.currentTimeMillis();
					}

				}
				if (danger[col][line] != 0 && temps[col][line] != 0
						&& map.returnMatrix()[col][line] == etat.FLAMMES) {
					temps[col][line] = danger[col][line];
					danger[col][line] = 0;

				}

				// if(temps[col][line]>0)
				// System.out.println("y"+line+"x"+col+temps[col][line]);

			}
		}

	}

	private boolean isdangerous(int xx, int yy) throws StopRequestException

	{
		checkInterruption(); // APPEL OBLIGATOIRE
		boolean resultat = false;

		if (temps[xx][yy] != 0
				&& ((System.currentTimeMillis() - temps[xx][yy] > (bombtimer / 6) * 5 && System
						.currentTimeMillis()
						- temps[xx][yy] < bombtimer) || System
						.currentTimeMillis()
						- temps[xx][yy] > bombtimer))

			resultat = true;

		return resultat;
	}

	// pour prendre le temps ecoule depuis la creation de la bombe
	private long getdangerous(int xx, int yy) throws StopRequestException

	{ // avant tout : test d'interruption
		checkInterruption();

		long resultat = 0;
		if (temps[xx][yy] != 0)

			resultat = System.currentTimeMillis() - temps[xx][yy];

		return resultat;

	}

	/** trouve le dern,er bombe mis */

	private void getnewest() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		moresafe = new LinkedList<AiTile>();
		setdangerous();
		long min = 1000000;
		for (int col = 0; col < tous.getWidth(); col++) {
			checkInterruption(); // APPEL OBLIGATOIRE

			for (int line = 0; line < tous.getHeight(); line++) {
				checkInterruption(); // APPEL OBLIGATOIRE

				if (temps[col][line] != 0
						&& getdangerous(col, line) < min
						&& Math.sqrt((x - col) * (x - col) + (y - line)
								* (y - line)) < 10) {
					min = getdangerous(col, line);
				}
			}
		}

		for (int col = 0; col < tous.getWidth(); col++) {
			checkInterruption(); // APPEL OBLIGATOIRE

			for (int line = 0; line < tous.getHeight(); line++) {
				checkInterruption(); // APPEL OBLIGATOIRE

				if (temps[col][line] != 0
						&& Math.abs(getdangerous(col, line)) - min < 25)

					moresafe.add(tous.getTile(line, col));

			}
		}
		// System.out.println(moresafe);

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOS FONCTIONS POUR LES CONTROLES ET LA SECURITE //
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * la fonction qui controle la securite dune case grace a la fonc de //
	 * isWalkable de la classe map
	 */
	private boolean controle2(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		boolean resultat = false;

		if (map.isWalkable(tile.getCol(), tile.getLine())
				&& tile.getBombs().size() == 0)
			resultat = true;

		return resultat;

	}

	/**
	 * la fonction qui envoit les lieux surs quon peut senfuire au cas //
	 * dinsecurite quand selection es 0 la destination est accessible seulment
	 * par des flammes // ou des cases sures, sil est 1 on peut passer par des
	 * cases definies comme danger aussi sil est 2 on ne peut que passer par des
	 * cases sures
	 */
	private Collection<AiTile> securite(int c, int l, int selection)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		map.getbombs();
		map.getfires();
		Collection<AiTile> sure = new ArrayList<AiTile>();
		// height et width sont inverses pour etre compatible avec la fonc
		// getTile(line,col)
		AiTile jeu[][] = new AiTile[tous.getHeight()][tous.getWidth()];

		for (int line = 0; line < tous.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < tous.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				jeu[line][col] = (getPercepts().getTile(line, col));

			}
		}

		for (int line = 0; line < tous.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < tous.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if (c != col || l != line) {
					securite = new Astar(map, c, l, col, line,this);

					// si cest sures et si on peut y acceder par un chemin
					// suffisamment sur(nous pouvons marcher sur les flammes )
					if (controle2(jeu[line][col])) {
						if ((selection == 0 && securite.findPath())
								|| (selection == 1 && securite.findPathdang())
								|| (selection == 2 && securite.findPathchoix()))

							sure.add(jeu[line][col]);
					}
				}
			}
		}

		// System.out.println(sure);

		return sure;

	}

	// si ladversaire ou le bonus existe encore ( il nest pas en train de
	// burnir)
	public boolean existe(AiTile tile) throws StopRequestException {
		checkInterruption();

		boolean resultat = false;
		if (tile.getItem() != null) {
			if (tile.getItem().getState().getName() == AiStateName.BURNING)
				resultat = false;
			else
				resultat = true;

		} else if (tile.getHeroes().size() != 0 && bomberman.getTile() != tile) {

			if (tile.getHeroes().size() != 0
					&& tile.getHeroes().iterator().next().getState().getName() == AiStateName.BURNING) {
				resultat = false;

			} else
				resultat = true;
		}
		return resultat;

	}

	public boolean impasse(AiTile tile) throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();

		boolean resultat = false;
		if (tile.getNeighbor(Direction.RIGHT).getBombs().size() > 0
				|| tile.getNeighbor(Direction.RIGHT).getBlock() != null) {
			if (tile.getNeighbor(Direction.UP).getBombs().size() > 0
					|| tile.getNeighbor(Direction.UP).getBlock() != null) {
				if (tile.getNeighbor(Direction.DOWN).getBombs().size() > 0
						|| tile.getNeighbor(Direction.DOWN).getBlock() != null) {
					if (tile.getNeighbor(Direction.LEFT).getBombs().size() > 0
							|| tile.getNeighbor(Direction.LEFT).getBlock() != null)
						resultat = true;
				}
			}
		}

		return resultat;

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOS FONCTIONS POUR ATTAQUER //
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * on simule une bombe et si ladversaire na pas dendroit pour senfuire et si
	 * on la on met ce bombe
	 */
	public void attaque(boolean at) throws StopRequestException {

		checkInterruption();

		Astar attack;

		Iterator<AiHero> it = tous.getHeroes().iterator();
		AiHero hero = null;

		if (tous.getTile(y, x).getBombs().size() == 0) {
			while (!securite(x, y, 0).isEmpty() && it.hasNext() && !attaque) {
				checkInterruption(); // APPEL OBLIGATOIRE
				hero = it.next();
				if (hero != bomberman&&existe(hero.getTile())
						&& !securite(hero.getCol(), hero.getLine(), 0)
								.isEmpty()) {
					attack = new Astar(map, x, y, hero.getCol(), hero.getLine(),this);
					map.setbombeposs(x, y, bomberman.getBombRange());

					if ((at && hero.getTile() != caseactuelle
							&& attack.findPathdang() && securite(hero.getCol(),
							hero.getLine(), 0).size() == 0)
							|| (!at && hero.getTile() != caseactuelle
									&& attack.findPathdang() && securite(
									hero.getCol(), hero.getLine(), 2).size() == 0)) {

						if (securite(x, y, 0).size() != 0
								&& getdangerous(x, y) < (bombtimer ) / 2 &&
								(x+1<map.width &&(tous.getTile(y, x).getNeighbor(Direction.RIGHT).getBombs().size()==0
								||(tous.getTile(y, x).getNeighbor(Direction.RIGHT).getBombs().size()>0 &&
										tous.getTile(y, x).getNeighbor(Direction.RIGHT).getBombs().iterator().next().getColor()!=bomberman.getColor()	)))
										 &&
								(x-1>0 &&(tous.getTile(y, x).getNeighbor(Direction.LEFT).getBombs().size()==0
								||(tous.getTile(y, x).getNeighbor(Direction.LEFT).getBombs().size()>0 &&
										tous.getTile(y, x).getNeighbor(Direction.LEFT).getBombs().iterator().next().getColor()!=bomberman.getColor()	)))
										 &&
								(y+1<map.height &&(tous.getTile(y, x).getNeighbor(Direction.DOWN).getBombs().size()==0
								||(tous.getTile(y, x).getNeighbor(Direction.DOWN).getBombs().size()>0 &&
										tous.getTile(y, x).getNeighbor(Direction.DOWN).getBombs().iterator().next().getColor()!=bomberman.getColor()	)))
										 &&
								(y-1>0  &&(tous.getTile(y, x).getNeighbor(Direction.UP).getBombs().size()==0
								||(tous.getTile(y, x).getNeighbor(Direction.UP).getBombs().size()>0 &&
										tous.getTile(y, x).getNeighbor(Direction.UP).getBombs().iterator().next().getColor()!=bomberman.getColor()	)))
							) {

							// System.out.println("attaque");
							result = laisser();
							attaque = true;

						} else
							map.removebombe();
					} else
						map.removebombe();
				}
			}
		}

	}

	// controler sil ya quelquun autour de nous
	@SuppressWarnings("static-access")
	public boolean alentours() throws StopRequestException {
		checkInterruption();
		boolean resultat = false;
		if (map.returnMatrix()[bomberman.getCol()][bomberman.getLine()] == etat.ADVERSAIRE
				|| (x + 1 < tous.getWidth()
						&& map.returnMatrix()[x + 1][y] == etat.ADVERSAIRE && existe(tous
						.getTile(y, x + 1)))
				|| (y - 1 > 0
						&& map.returnMatrix()[x][y - 1] == etat.ADVERSAIRE && existe(tous
						.getTile(y - 1, x)))
				|| (y + 1 < tous.getHeight()
						&& map.returnMatrix()[x][y + 1] == etat.ADVERSAIRE && existe(tous
						.getTile(y + 1, x)))
				|| (x - 1 > 0
						&& map.returnMatrix()[x - 1][y] == etat.ADVERSAIRE && existe(tous
						.getTile(y, x - 1)))
		// || (x+2<tous.getWidth() && map.returnMatrix()[x + 2][y] ==
		// etat.ADVERSAIRE)
		// ||(y-2>0 && map.returnMatrix()[x][y - 2] == etat.ADVERSAIRE)
		// || ( y+2<tous.getHeigh() &&map.returnMatrix()[x][y + 2] ==
		// etat.ADVERSAIRE)
		// || (x-2>0 &&map.returnMatrix()[x - 2][y] == etat.ADVERSAIRE )
		)
			resultat = true;

		return resultat;

	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOS FONCTIONS POUR SENFUIRE //
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// la fonc qui sert a courir des dangers
	@SuppressWarnings("static-access")
	private void court() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		// securite est notre fonction qui nous envoye la liste des lieux sures
		// si endanger cela veut dire quon peut passer par des cases dangers
		// sinon non
		Collection<AiTile> sure = null;
		if (!endanger)
			sure = securite(x, y, 0);
		else {
			sure = securite(x, y, 1);
		}
		Iterator<AiTile> sureit = sure.iterator();

		// nous accedons a lautre

		int xr = 1000, yr = 10001;
		long repo = 500000;
		int element = 1000;

		if (!sure.isEmpty()) {
			// cela sert a lui empecher de changer son idee mais de temps en
			// temps il court a un bombe
			if (!court || endanger) {

				AiTile possible;

				int xp, yp;

				while (sureit.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (response != null) {
						xr = response.getCol();
						yr = response.getLine();
					}
					getnewest();
					possible = sureit.next();
					xp = possible.getCol();
					yp = possible.getLine();
					int dx = xp - x;
					int dy = yp - y;
					int dxr = xr - x;
					int dyr = yr - y;

					// pour la plus proche case sure quon peut acceder par le
					// plus sur chemin
					Astar astarpos = new Astar(map, x, y, xp, yp,this);

					if ((!endanger && astarpos.findPath())
							|| (endanger && astarpos.findPathdang())) {
						int essai = 0;

						Deque<Integer> dequepos = astarpos.getPath();
						long dan = 0;
						while (!dequepos.isEmpty() && dan != -1) {

							// avant tout : test d'interruption
							checkInterruption();
							int tempx = dequepos.poll(), tempy = dequepos
									.poll();
							essai++;

							// on truve le plus sur grace a la fonc
							// getdangereuse() qui sert a nous informer sur le
							// temps ecoule
							// des la mise de bombe qui influence la case donne
							// mais en generale ça marche pas bien car les
							// flammes ne
							// sont pas encore cree a -ce emps-la donc jai mis
							// des nombres moi-meme

							if (map.returnMatrix()[tempx][tempy] == etat.FLAMMESPOSS
									|| map.returnMatrix()[tempx][tempy] == etat.FLAMMES) {

								if (!moresafe.isEmpty()
										&& moresafe.contains(tous.getTile(
												tempy, tempx)))
									dan = dan + getdangerous(tempx, tempy) + 5;
								else
									dan = dan + 3 * getdangerous(tempx, tempy)
											+ 100;
							} else if (map.returnMatrix()[tempx][tempy] == etat.DANGER) {
								if (!moresafe.isEmpty()
										&& moresafe.contains(tous.getTile(
												tempy, tempx))) {
									dan = dan + getdangerous(tempx, tempy)
											+ 1000;
								} else
									dan = dan + 3 * getdangerous(tempx, tempy)
											+ 3000;
							}
							if (essai==1 && isdangerous(tempx, tempy) )
								dan=-1;
							else if (getdangerous(tempx, tempy) > (bombtimer * 2 / 3)
									&& essai < 3)
								dan = -1;
							else if (getdangerous(tempx, tempy) > bombtimer / 2
									&& essai > 3) { // System.out.println(essai);
								dan = -1;
							}

						}
						if (dan != -1 || getdangerous(x, y) > bombtimer / 2) {
							if (repo > dan) {
								repo = dan;
								element = essai;
								response = possible;
								// System.out.println(repo);

								// au cas degalite prenons la plus proche
							} else if (repo == dan
							/*
							 * && Math.sqrt(dx dx + dy dy) < Math.sqrt(dxr dxr +
							 * dyr dyr)
							 */&& element > essai) {

								response = possible;

							} else if (repo == dan
									&& Math.sqrt(dx * dx + dy * dy) == Math
											.sqrt(dxr * dxr + dyr * dyr)
									&& securite(xr, yr, 2).size() < securite(
											xp, yp, 2).size()) {

								response = possible;

							}
						}
					}
				}
			}
			// System.out.println(response);
			casedestination = response;

			// 

		}

		// else mort();
	}

}