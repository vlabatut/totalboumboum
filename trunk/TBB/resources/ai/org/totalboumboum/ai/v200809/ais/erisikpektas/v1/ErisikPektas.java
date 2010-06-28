package org.totalboumboum.ai.v200809.ais.erisikpektas.v1;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doga Erisik
 * @author Abdurrahman Pektas
 *
 */
public class ErisikPektas extends ArtificialIntelligence {
	/** la case occupée actuellement par le personnage */
	private AiTile caseactuelle;
	/** la case sur laquelle on veut aller */
	private AiTile caseprochaine = null;
	/** larea du jeu */
	private AiZone tous;
	/** les autres joueurs */
	Collection<AiHero> autres;
	/** x et y de lautre */
	int xadversaire;
	int yadversaire;
	/** notrre x y */
	int x, y;
	int res = 0;

	AiAction result = new AiAction(AiActionName.NONE);
	/** reponse de notre rechercher pour une case secure */
	AiTile reponse;

	/**
	 * nous allons lutiliser dans notre fonk de courir qui sert a aller a un
	 * lieu secure
	 */
	boolean impasse = false;
	/** au cas de l'existances des bombes ou flammes danger nest pas 0 */
	int danger = 0;

	/** qd nous voulons que notre hero attend ous allons lutiliser */
	boolean arret = false;

	/** pour notre fonc de temps des bombes */
	boolean h = false;
	int[][] b = new int[50][50];

	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		// nous accedons a notre hero
		tous = getPercepts();
		AiHero bomberman = tous.getOwnHero();

		x = bomberman.getCol();
		y = bomberman.getLine();
		
		//qd nous sommes en dessus en xml et qd nos couleurs sont les memes avec lautre les 2 fonc suivants envoient les memes valeurs
//		 System.out.println(bomberman.getTile());
//		 System.out.println(tous.getHeroes().iterator().next().getTile());

		// on accede a la case ou se trouve notre bomberman
		caseactuelle = bomberman.getTile();
		// si nous sommes pas mortes et nous navons pas tue lautre
		if (bomberman != null && tous.getHeroes().iterator().next() != null) {

			// sil n'a pas encore commence a s'agir nous linitialisons
			if (caseprochaine == null) {

				init();
			}
			// sil existe des feus ou des bombes dans la zone de jeu il faut
			// essayer de senfuire
			danger = getPercepts().getBombs().size()
					+ getPercepts().getFires().size();
			// si le nombre des bombes+les feus est superieur a 0
			if (danger != 0) {
				// nous courons pour sechapper
				court();

				// si notre case est sure pas de danger donc nous attendons et
				// mais si arret nest pas true cela veut dire quil veut laisser
				// une bombe
				if ((caseactuelle == reponse || controle_flammes(caseactuelle))
						&& arret == false) {
					result = new AiAction(AiActionName.NONE);
				}
				// System.out.println((getPercepts().getTile(
				// tous.getHeigh()-1,tous.getWidth()-1)));

			}
			// sil nya pas de danger et caseprochaine nest pas nulle nous
			// avançons en suivant lautre joueur
			else if (caseprochaine == caseactuelle) {
//				System.out.println("prozhaine");

				poursuivre();

			}
			// definissons notre direction selon notre caseactuelle et prochaine
			Direction direction = getPercepts().getDirection(caseactuelle,
					caseprochaine);

			// temps=loop.getTotalTime();

			// et le controle des directions composites pour que le jeu ne jette
			// pas dexceptions
			if (direction != Direction.NONE && direction != Direction.DOWNLEFT
					&& direction != Direction.DOWNRIGHT
					&& direction != Direction.UPLEFT
					&& direction != Direction.UPRIGHT) {
				result = new AiAction(AiActionName.MOVE, direction);

			}
			// nous refaisons arret=false pour qu'il entre a if ci-dessus
			arret = false;
			// System.out.println(result.getDirection());

		}

		return result;

	}

	// pour linitialisation de bomberman

	private void init() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		caseprochaine = caseactuelle;

	}

	// pour laiisser de la bombe

	public AiAction laisser() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		AiAction bombe = new AiAction(AiActionName.DROP_BOMB);

		return bombe;
	}

	// pour suivre l'autre joueur qd on omet les laisser et leurs if dans cette
	// fonc il suit le promeneur mais maintenant qd il laiisse une bombe
	// il passe au cas de courir mais on est tjrs proche de lui
	// qd son x est inferiure que le notre nous chosissons le voisin de
	// caseatuelle qui a un x inferieur,et cest valable pour y et les superiorites
	// nous faisons aussi les controles de ces cases-la avant de les envoyer
	// comme caseprochaine

	private void poursuivre() throws StopRequestException {

		checkInterruption(); // APPEL OBLIGATOIRE

		AiTile caseadversaire = null;
		autres = tous.getHeroes();
		Iterator<AiHero> adversaire = autres.iterator();
		AiHero temporaire = adversaire.next();
		// bombeadv=temporaire.getBombCount()+bombeadv;

		caseadversaire = temporaire.getTile();
		// pou le prochaine possible
		AiTile procpossible;

		xadversaire = temporaire.getCol();
		yadversaire = temporaire.getLine();
		// nous allons les utiliser pour dropbomb
		int difx = xadversaire - x;
		int dify = yadversaire - y;
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(
				caseactuelle);
		Iterator<AiTile> it = neighbors.iterator();

		// de temps en temps notre fonc entre dans le premier if et par ex dit
		// que notre x est superieur que l'adv et apres il entre
		// dans cette condition mais apres dans le 2eme if il voit que cette
		// voisin donc ici cest la case voisine gauche nest pas accessible
		// car le controle retourne false et il sort de la fonc mais il repete
		// tjrs les mems choses jusqua l'adversaire nous approche
		// autant que son x nest plus sup que le notre. donc cela cause une
		// boucle il va en haut et returne en bas et ce boolean libre empeche
		// cette chose la car libre reste false nous trouvons un autre case a
		// suivre
		boolean libre = false;

		if (xadversaire < x && (x - xadversaire > y - yadversaire)) {
			libre = false;
			// si on a suffisamment de bombes et si on est sur la meme case ou
			// on est suffisamment proche nus laissons une bombe
			if ((caseadversaire == caseactuelle || (difx * difx + dify * dify) <= 3)
					&& getPercepts().getOwnHero().getBombNumber() > 0) {
				libre = true;
				result = laisser();

			} else {
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();

					if (controle(procpossible)) {
						if (procpossible.getCol() < x) {
							libre = true;
//							System.out.println("<x");
							caseprochaine = procpossible;

						}
					}
				}

			}
			// nous repetons les memes choses nous avons ce -x + xadversaire > y
			// - yadversaire car je ne veux pas qu'il le suit avec un trajet
			// droit
		} else if (x < xadversaire && (-x + xadversaire > y - yadversaire)) {
			libre = false;

			if ((caseadversaire == caseactuelle || (difx * difx + dify * dify) <= 3)
					&& getPercepts().getOwnHero().getBombNumber() > 0) {
				libre = true;
				result = laisser();

			} else {
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {
						if (procpossible.getCol() > x) {
							libre = true;
//							System.out.println("x<" + procpossible);
							caseprochaine = procpossible;

						}
					}
				}
			}
		}

		else if (y > yadversaire && (x - xadversaire < y - yadversaire)) {
			libre = false;

			if ((caseadversaire == caseactuelle || (difx * difx + dify * dify) <= 3)
					&& getPercepts().getOwnHero().getBombNumber() > 0) {
				libre = true;
				result = laisser();

			} else {
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {
						if (procpossible.getLine() < y) {
							libre = true;
//							System.out.println("<y" + procpossible);

							caseprochaine = procpossible;

						}

					}
				}

			}

		}

		else if (yadversaire > y && (x - xadversaire < -y + yadversaire)) {
			libre = false;

			if ((caseadversaire == caseactuelle || (difx * difx + dify * dify) <= 3)
					&& getPercepts().getOwnHero().getBombNumber() > 0) {
				libre = true;
				result = laisser();

			} else {
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {
						if (procpossible.getLine() > y) {
							libre = true;
//							System.out.println(">y" + procpossible);
							caseprochaine = procpossible;

						}
					}

				}
			}

		}
		// si libre est false nous determinons un autre case a aller
		if (!libre) {
			neighbors = getPercepts().getNeighborTiles(caseactuelle);
			it = neighbors.iterator();
			while (it.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE

				procpossible = it.next();
				if (controle(procpossible)) {

					caseprochaine = procpossible;

				}

			}

		}

	}

	// pour controler si la case passe en parametre contient un danger comme feu
	// bombe ou il est bloque
	private boolean controle(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		boolean resultat;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();

		resultat = block == null && bombs.size() == 0 && fires.size() == 0;

		return resultat;

	}

	// en trouvant un lieu sur il faut qu'il nest pas menace par un bombe et ses
	// flammes
	private boolean controle_flammes(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		boolean resultat;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();

		resultat = block == null && bombs.size() == 0 && fires.size() == 0
				&& !flammes(tile);
		return resultat;

	}

	// notre fonc de sechapper
	// le principe est de prendre les listes des cases secure et de tirer un
	// case et essayer d'y arriver
	// la recherche de route est tjrs la meme si x est < on choisit la case de
	// petit x
	private void court() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		AiTile procpossible;
		// securite est notre fonction qui nous envoye la liste des lieux sures
		Collection<AiTile> sure = securite();
		Iterator<AiTile> sureit = sure.iterator();

		// nous accedons a luatre
		autres = tous.getHeroes();
		Iterator<AiHero> adversaire = autres.iterator();
		AiHero temporaire = adversaire.next();
		xadversaire = temporaire.getCol();
		yadversaire = temporaire.getLine();
		int difx = xadversaire - x;
		int dify = yadversaire - y;
		int xr, yr;

		// sans impasse il entre tjrs dans cette partie-la et il recommence a
		// rechercher et puis naturellement il truve un autre
		// et notre bember essaye d'y aller cette fois-ci et il s'etonne et
		// agissent aleatoirement
		if (impasse == false) {

			reponse = sureit.next();

			AiTile possible;

			int xp, yp;

			while (sureit.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				xr = reponse.getCol();
				yr = reponse.getLine();
				possible = sureit.next();
				xp = possible.getCol();
				yp = possible.getLine();
				int dx = xp - x;
				int dy = yp - y;
				int dxr = xr - x;
				int dyr = yr - y;

				// pour la plus proche case sure
				if (Math.sqrt(dx * dx + dy * dy) < Math.sqrt(dxr * dxr + dyr
						* dyr)) {

					reponse = possible;

				}

			}

		}
//		System.out.println(reponse);

		// pour qu'au prochaine fois il evite dentrer a la recherche ci dessus
		impasse = true;
		// cest la libre avec la meme logique que le precedent
		boolean libre = true;
		// nous comparons nos x, y et les x,y de reponse
		int reponsex = reponse.getCol();
		int reponsey = reponse.getLine();

		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(
				caseactuelle);
		// System.out.println(neighbors);
		Iterator<AiTile> it = neighbors.iterator();

		if (reponsex < x) {
			libre = false;
			// ici nous avons utilise getBombNumber() > 0 car si on na pas de
			// bombe nous ne connaissons pas qu'est-cequ'il va faire et
			// getBombCount()<5 car nous voulons tuer promeneur si vite et qd il
			// y a trop de bombes lautre joueur ne nous approche pas trop
			if (((difx * difx + dify * dify) <= 16)
					&& getPercepts().getOwnHero().getBombNumber() > 0
					&& getPercepts().getOwnHero().getBombCount() < 5) {
				libre = true;
				arret = true;
				result = laisser();

			} else {

				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE

					procpossible = it.next();
					if (controle(procpossible)) {

						if (procpossible.getCol() < x) {
							libre = true;

							// System.out.println("<x");
							caseprochaine = procpossible;

						}
					}// si le chemin est bloque il faut quon cherche un autre
						// reponse et comme notre x et y sont probablement
						// changes reponsex reponsey varieront aussi
					else {
						impasse = false;

					}
				}

			}

		} else if (x < reponsex) {
			libre = false;
			// ici cest plus grand car comme nous arretons qd nous trouvons un
			// lieu sur il faut que nous ayons un reflexe qd qqn nous approche
			if (((difx * difx + dify * dify) <= 16)
					&& getPercepts().getOwnHero().getBombNumber() > 0
					&& getPercepts().getOwnHero().getBombCount() < 5) {
				libre = true;
				arret = true;
				result = laisser();

			} else {
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {

						if (procpossible.getCol() > x) {
							libre = true;

							// System.out.println("x<");
							caseprochaine = procpossible;

						}
					} else {
						impasse = false;

					}
				}
			}

		}
		if (y > reponsey) {
			libre = false;

			if (((difx * difx + dify * dify) <= 16)
					&& getPercepts().getOwnHero().getBombNumber() > 0
					&& getPercepts().getOwnHero().getBombCount() < 5) {
				libre = true;
				arret = true;
				result = laisser();

			} else {
				// System.out.println("<y");
				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {
						if (procpossible.getLine() < y) {

							libre = true;
							// System.out.println("<y");

							caseprochaine = procpossible;

						}
					} else {
						impasse = false;

					}
				}
			}

		}

		else if (reponsey > y) {
			libre = false;

			if (((difx * difx + dify * dify) <= 16)
					&& getPercepts().getOwnHero().getBombNumber() > 0
					&& getPercepts().getOwnHero().getBombCount() < 5) {
				libre = true;
				// nous ajoutons arret=true pour qu'en haut notre result ne
				// change pas
				arret = true;
				result = laisser();

			} else {

				while (it.hasNext()) {
					checkInterruption(); // APPEL OBLIGATOIRE
					procpossible = it.next();
					if (controle(procpossible)) {
						if (procpossible.getLine() > y) {
							libre = true;
							// System.out.println(">y");

							caseprochaine = procpossible;

						}
					} else {
						impasse = false;

					}
				}
			}
		}// 89449
		if (!libre) {
			neighbors = getPercepts().getNeighborTiles(caseactuelle);
			it = neighbors.iterator();
			while (it.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE

				procpossible = it.next();
				if (controle(procpossible)) {

					caseprochaine = procpossible;

				}

			}

		}
		// System.out.println(caseactuelle);
		// si nous sommes en bonne lieu on fait impasse false pour les
		// prochaines fios
		if (caseactuelle == reponse) {
			impasse = false;

		}
	}

	// notre fonc de securite envoit les lieux surs. dabord nous formons la
	// matrice de jeu et puis nous mettons les cases selon la rponse de
	// controle_flammes
	private Collection<AiTile> securite() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		Collection<AiTile> sure = new ArrayList<AiTile>();
		// height et width sont inverses pour etre compatible avec la fonc
		// getTile(line,col)
		AiTile jeu[][] = new AiTile[tous.getHeigh()][tous.getWidth()];

		for (int line = 0; line < tous.getHeigh(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < tous.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				jeu[line][col] = (getPercepts().getTile(line, col));

			}
		}

		for (int line = 0; line < tous.getHeigh(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < tous.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if (controle_flammes(jeu[line][col]))
					sure.add(jeu[line][col]);
			}
		}

		// System.out.println(sure);
		return sure;

	}

	// cette fonction nous envoie les feus d'une bombe et donc nous choisissons
	// notre lieu sur selon ces informations
	private boolean flammes(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		boolean resultat = false;
		AiTile casebombe;
		AiBomb b;
		Collection<AiBomb> bombs = tous.getBombs();
		Iterator<AiBomb> danger = bombs.iterator();

		while (danger.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			b = danger.next();
			casebombe = b.getTile();
			// AiBombType abt = b.getType();

			AiBlock right = casebombe.getNeighbor(Direction.RIGHT).getBlock();
			AiBlock up = casebombe.getNeighbor(Direction.UP).getBlock();
			AiBlock left = casebombe.getNeighbor(Direction.LEFT).getBlock();
			AiBlock down = casebombe.getNeighbor(Direction.DOWN).getBlock();

			// s'il n'y a pas un mur a droite et si notre case voulue se trouve
			// a droite de cette bombe et il est dans son range et
			// s'ils se trouvent au meme ligne la case est inaccessible donc la
			// reponse est true
			if (b.isWorking()) {
				if ((((left == null && (tile.getCol() <= casebombe.getCol() && tile
						.getCol() >= casebombe.getCol() - b.getRange())) || ((right == null
						&& tile.getCol() >= casebombe.getCol() && tile.getCol() <= casebombe
						.getCol()
						+ b.getRange()))) && tile.getLine() == casebombe
						.getLine())
						|| (((up == null && (tile.getLine() <= casebombe
								.getLine() && tile.getLine() >= casebombe
								.getLine()
								- b.getRange())) || ((down == null
								&& tile.getLine() >= casebombe.getLine() && tile
								.getLine() <= casebombe.getLine()
								+ b.getRange()))) && tile.getCol() == casebombe
								.getCol()))

					resultat = true;
			}
			// si nous sommes sur un bombe il faut trouver la case convenable
			if (caseactuelle == casebombe) {

				if (controle(getPercepts().getNeighborTile(caseactuelle,
						Direction.RIGHT)))
					caseprochaine = getPercepts().getNeighborTile(
							caseactuelle, Direction.RIGHT);
				else if (controle(getPercepts().getNeighborTile(caseactuelle,
						Direction.DOWN)))
					caseprochaine = getPercepts().getNeighborTile(
							caseactuelle, Direction.DOWN);
				else if (controle(getPercepts().getNeighborTile(caseactuelle,
						Direction.LEFT)))
					caseprochaine = getPercepts().getNeighborTile(
							caseactuelle, Direction.LEFT);

				else if (controle(getPercepts().getNeighborTile(caseactuelle,
						Direction.UP)))
					caseprochaine = getPercepts().getNeighborTile(
							caseactuelle, Direction.UP);

			}
		}
		return resultat;

	}

}
/*
 * //fonction qui nous envoie le temps dexplosion dune bombe private boolean
 * dangereuse(int x1,int y1,boolean p) throws StopRequestException{
 * checkInterruption(); // APPEL OBLIGATOIRE boolean resultat=false;
 * 
 * 
 * 
 * 
 * if (!h){ for (int col=0;col<50;col++) {checkInterruption(); // APPEL
 * OBLIGATOIRE
 * 
 * for (int line=0;line<50;line++) {checkInterruption(); // APPEL OBLIGATOIRE if
 * (b[col][line]=='\0') {b[col][line]=0;}
 * 
 * }}} h=true;
 * 
 * if (b[x1][y1]==0){
 * 
 * b[x1][y1]=i;
 * 
 * }
 * 
 * 
 * else if( i-b[x1][y1]>300&& i-b[x1][y1]<2000 &&p)
 * 
 * resultat=true; else if ( i-b[x1][y1]>500 && i-b[x1][y1]<6000 &&!p)
 * 
 * resultat=true;
 * 
 * i++; for (int z=0;z<50;z++) {checkInterruption(); // APPEL OBLIGATOIRE
 * 
 * for (int d=0;d<50;d++) {checkInterruption(); // APPEL OBLIGATOIRE if
 * (i-b[z][d]>2000) b[z][d]=0;
 * 
 * }}
 * 
 * 
 * 
 * // System.out.println(b[x1][y1]); //System.out.println(i);
 * 
 * 
 * return resultat;
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * }
 */
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
