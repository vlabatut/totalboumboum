package org.totalboumboum.ai.v200910.ais.calisirguner.v5c;

import java.util.Collection;
import java.util.List;

import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * on la pris des bleus de l'annee precedente et on la change un peu cf les flammes et es matrices de risque et accesibilite et murs
 * 
 * @version 5.c
 * 
 * @author Emre Calisir
 * @author Burak Ozgen Guner
 *
 */
public class Map {

	@SuppressWarnings("unused")
	private AiZone map;
	private Collection<AiHero> adversaires;

	private AiHero bomberman;
	private CalisirGuner source; // pour chechinterruption
	private Collection<AiBomb> bombes;
	private Collection<AiBlock> blocks;
	private Collection<AiItem> objets;
	private Collection<AiFire> feu;
	public int width;
	public int height;

	private int xadversaire, yadversaire;
	private Etat matrix[][];
	private Etat accessibilite[][];
	private int risque[][];
	private int murs[][];

	public Map(AiZone zone, CalisirGuner source) throws StopRequestException {
		source.checkInterruption();
		this.map = zone;
		this.bomberman = zone.getOwnHero();
		this.adversaires = zone.getRemainingHeroes();
		this.bombes = zone.getBombs();
		this.blocks = zone.getBlocks();
		this.objets = zone.getItems();
		this.feu = zone.getFires();
		this.width = zone.getWidth();
		this.height = zone.getHeight();
		this.source = source;

		remplir(zone);
	}

	// nous remplaçons notre map
	protected void remplir(AiZone zone) throws StopRequestException {
		source.checkInterruption();
		// on initialise les matrices
		accessibilite = new Etat[width][height];
		risque = new int[width][height];
		murs = new int[width][height];
		// premieremnt on met letat libre pour partout
		matrix = new Etat[width][height];

		int i, j;
		// Initialisation
		for (i = 0; i < width; i++) {
			source.checkInterruption();
			for (j = 0; j < height; j++) {
				source.checkInterruption();
				matrix[i][j] = Etat.LIBRE;
				accessibilite[i][j] = Etat.ACCESSIBLE;
				risque[i][j] = 1;
				murs[i][j] = 0;
			}
		}
		// les feus des explosions

		this.getfires();

		// remplaçons les bonus

		Iterator<AiItem> itemit = objets.iterator();
		AiItem item;

		while (itemit.hasNext()) {
			source.checkInterruption();
			item = itemit.next();

			xadversaire = item.getCol();
			yadversaire = item.getLine();
			if (bomberman.getBombNumber() < 3 || bomberman.getBombRange() < 3)
				risque[xadversaire][yadversaire] = -25;

			if (matrix[xadversaire][yadversaire] != Etat.FEU)
				matrix[xadversaire][yadversaire] = Etat.POINT;

		}

		// on met les murs

		Iterator<AiBlock> itbl = blocks.iterator();
		AiBlock bl;

		while (itbl.hasNext()) {
			source.checkInterruption();
			bl = itbl.next();
			xadversaire = bl.getCol();
			yadversaire = bl.getLine();
			accessibilite[xadversaire][yadversaire] = Etat.INACCESSIBLE;
			risque[xadversaire][yadversaire] = 20000;

			if (!bl.isDestructible())
				matrix[xadversaire][yadversaire] = Etat.INDESTRUCTIBLES;
			else
				matrix[xadversaire][yadversaire] = Etat.DESTRUCTIBLES;

		}
		// on cree la matrice des murs pour trouver le lieu ou on va exploser un
		// max de murs
		for (int q = 0; q < width; q++) {
			source.checkInterruption();
			for (int w = 0; w < height; w++) {
				source.checkInterruption();
				if (matrix[q][w] == Etat.DESTRUCTIBLES) {
					murs[q][w] = -1;
					for (int b = 1; b < bomberman.getBombRange(); b++) {
						source.checkInterruption();
						if (b + q < width
								&& matrix[b + q][w] != Etat.INDESTRUCTIBLES
								&& matrix[b + q][w] != Etat.DESTRUCTIBLES
								&& matrix[b + q][w] != Etat.POINT) {
							murs[q + b][w]++;
						} else
							break;
					}
					// on augmente les valeurs de matrices pendant quon est en portee du bombe
					for (int b = 1; b < bomberman.getBombRange(); b++) {
						source.checkInterruption();
						if (b + w < height
								&& matrix[q][b + w] != Etat.INDESTRUCTIBLES
								&& matrix[q][w + b] != Etat.DESTRUCTIBLES
								&& matrix[q][w + b] != Etat.POINT) {
							murs[q][w + b]++;
						} else
							break;
					}
					for (int b = 1; b < bomberman.getBombRange(); b++) {
						source.checkInterruption();
						if (q - b > 0
								&& matrix[q - b][w] != Etat.INDESTRUCTIBLES
								&& matrix[q - b][w] != Etat.DESTRUCTIBLES
								&& matrix[q - b][w] != Etat.POINT) {
							murs[q - b][w]++;
						} else
							break;

					}
					for (int b = 1; b < bomberman.getBombRange(); b++) {
						source.checkInterruption();
						if (w - b > 0
								&& matrix[q][w - b] != Etat.INDESTRUCTIBLES
								&& matrix[q][w - b] != Etat.DESTRUCTIBLES
								&& matrix[q][w - b] != Etat.POINT) {
							murs[q][w - b]++;
						} else
							break;

					}

				}

			}
		}
		// pour les flammes

		AiBomb b;

		Iterator<AiBomb> danger = bombes.iterator();

		while (danger.hasNext()) {
			source.checkInterruption();
			// APPEL OBLIGATOIRE
			b = danger.next();
			List<AiTile> Blast = b.getBlast();
			Iterator<AiTile> tile = Blast.iterator();
			AiTile feufutur;
			while (tile.hasNext()) {
				source.checkInterruption();
				feufutur = tile.next();
				/*
				 * if
				 * (matrix[feufutur.getCol()][feufutur.getLine()]==Etat.FLAMMES)
				 * matrix[feufutur.getCol()][feufutur.getLine()]=Etat.DANGER;
				 * else
				 */if (matrix[feufutur.getCol()][feufutur.getLine()] != Etat.DESTRUCTIBLES
						&& matrix[feufutur.getCol()][feufutur.getLine()] != Etat.INDESTRUCTIBLES) {
					matrix[feufutur.getCol()][feufutur.getLine()] = Etat.FLAMMES;
					// on va utiliser pour trouver le meilleur case sur la
					// matrice de risque
					if (risque[feufutur.getCol()][feufutur.getLine()] < (int) b
							.getTime() )
						risque[feufutur.getCol()][feufutur.getLine()] = (int) b
								.getTime() ;
					// si cest proche a son explosion on le rend inaccessible
					if (b.getTime() >= b.getNormalDuration() * 8 / 9 && b.isWorking())
						accessibilite[feufutur.getCol()][feufutur.getLine()] = Etat.INACCESSIBLE;
					else if(!b.isWorking() && b.getTime()>=(b.getNormalDuration()*14)/10)
						accessibilite[feufutur.getCol()][feufutur.getLine()] = Etat.INACCESSIBLE;
				 }
			}
		}

		this.getbombs();
		// et enfin les adversaires

		Iterator<AiHero> it = adversaires.iterator();
		AiHero hero;

		while (it.hasNext()) {
			source.checkInterruption();

			hero = it.next();
			xadversaire = hero.getCol();
			yadversaire = hero.getLine();

			if (hero != bomberman) {

				if (matrix[xadversaire][yadversaire] != Etat.FEU
						&& matrix[xadversaire][yadversaire] != Etat.FLAMMES
						&& matrix[xadversaire][yadversaire] != Etat.BOMBE
						&& matrix[xadversaire][yadversaire] != Etat.BOMBEPOSS
						&& matrix[xadversaire][yadversaire] != Etat.FLAMMESPOSS
						&& matrix[xadversaire][yadversaire] != Etat.DANGER)
					matrix[xadversaire][yadversaire] = Etat.ADVERSAIRE;
				risque[xadversaire][yadversaire] = -15;

			}
		}
		if (matrix[bomberman.getCol()][bomberman.getLine()] != Etat.FLAMMES
				&& matrix[bomberman.getCol()][bomberman.getLine()] != Etat.BOMBEPOSS
				&& matrix[bomberman.getCol()][bomberman.getLine()] != Etat.FLAMMESPOSS
				&& matrix[bomberman.getCol()][bomberman.getLine()] != Etat.DANGER
				&& matrix[bomberman.getCol()][bomberman.getLine()] != Etat.ADVERSAIRE)
			matrix[bomberman.getCol()][bomberman.getLine()] = Etat.NOUS;

	}

	public void getbombs() throws StopRequestException {
		source.checkInterruption();
		Iterator<AiBomb> itbo = bombes.iterator();
		AiBomb bo;

		while (itbo.hasNext()) {
			source.checkInterruption();
			bo = itbo.next();
			xadversaire = bo.getCol();
			yadversaire = bo.getLine();
			if (matrix[xadversaire][yadversaire] != Etat.NOUS) {
				matrix[xadversaire][yadversaire] = Etat.BOMBE;

				accessibilite[xadversaire][yadversaire] = Etat.INACCESSIBLE;

			}
		}
	}

	public void getfires() throws StopRequestException {
		source.checkInterruption();
		Iterator<AiFire> itfeu = feu.iterator();
		AiFire feu;

		while (itfeu.hasNext()) {
			source.checkInterruption();
			feu = itfeu.next();
			xadversaire = feu.getCol();
			yadversaire = feu.getLine();
			accessibilite[xadversaire][yadversaire] = Etat.INACCESSIBLE;

			matrix[xadversaire][yadversaire] = Etat.FEU;

		}
	}

	/**
	 * on va lutiliser pour le cotrole si cest possible de laisser un bombe et
	 * puis courir donc on cree un bombe imaginaire
	 * 
	 * @throws StopRequestException
	 */

	public void setbombeposs(int x1, int y1, int range, boolean bo)
			throws StopRequestException {
		source.checkInterruption();
		matrix[x1][y1] = Etat.BOMBEPOSS;
		accessibilite[x1][y1] = Etat.INACCESSIBLE;

		List<AiBlock> dr = bomberman.getTile().getNeighbor(Direction.RIGHT)
				.getBlocks();
		List<AiBlock> ht = bomberman.getTile().getNeighbor(Direction.UP)
				.getBlocks();
		List<AiBlock> gc = bomberman.getTile().getNeighbor(Direction.LEFT)
				.getBlocks();
		List<AiBlock> bs = bomberman.getTile().getNeighbor(Direction.DOWN)
				.getBlocks();

		if (gc.size() == 0) {
			for (int q = 1; q <= range; q++) {
				source.checkInterruption();
				if (x1 - q > 0
						&& (matrix[x1 - q][y1] == Etat.FLAMMES || (matrix[x1
								- q][y1] == Etat.DANGER)) && !bo)
					accessibilite[x1 - q][y1] = Etat.INACCESSIBLE;
				if (x1 - q > 0 && matrix[x1 - q][y1] != Etat.INDESTRUCTIBLES
						&& matrix[x1 - q][y1] != Etat.DESTRUCTIBLES) {

					matrix[x1 - q][y1] = Etat.FLAMMESPOSS;

				} else
					break;

			}
		}
		if (dr.size() == 0) {
			for (int q = 1; q <= range; q++) {
				source.checkInterruption();
				if (x1 + q < width
						&& (matrix[x1 + q][y1] == Etat.FLAMMES || matrix[x1 + q][y1] == Etat.DANGER)
						&& !bo) {

					accessibilite[x1 + q][y1] = Etat.INACCESSIBLE;
					matrix[x1 + q][y1] = Etat.FLAMMESPOSS;

				} else if (x1 + q < width
						&& matrix[x1 + q][y1] != Etat.INDESTRUCTIBLES
						&& matrix[x1 + q][y1] != Etat.DESTRUCTIBLES) {
					matrix[x1 + q][y1] = Etat.FLAMMESPOSS;
				} else
					break;

			}
		}
		if (bs.size() == 0) {
			for (int q = 1; q <= range; q++) {
				source.checkInterruption();
				if (y1 + q < height
						&& (matrix[x1][y1 + q] == Etat.FLAMMES || matrix[x1][y1
								+ q] == Etat.DANGER) && !bo) {
					matrix[x1][y1 + q] = Etat.FLAMMESPOSS;

					accessibilite[x1][y1 + q] = Etat.INACCESSIBLE;
				} else if (y1 + q < height
						&& matrix[x1][y1 + q] != Etat.INDESTRUCTIBLES
						&& matrix[x1][y1 + q] != Etat.DESTRUCTIBLES) {
					matrix[x1][y1 + q] = Etat.FLAMMESPOSS;
				} else
					break;

			}
		}
		if (ht.size() == 0) {
			for (int q = 1; q <= range; q++) {
				source.checkInterruption();
				if (y1 - q > 0
						&& (matrix[x1][y1 - q] == Etat.FLAMMES || matrix[x1][y1
								- q] == Etat.DANGER) && !bo)
					accessibilite[x1][y1 - q] = Etat.INACCESSIBLE;
				if (y1 - q > 0 && matrix[x1][y1 - q] != Etat.INDESTRUCTIBLES
						&& matrix[x1][y1 - q] != Etat.DESTRUCTIBLES) {
					matrix[x1][y1 - q] = Etat.FLAMMESPOSS;

				} else
					break;

			}
		}

	}

	// ,l envoie la matrice de map
	public Etat[][] returnMatrix() throws StopRequestException {
		source.checkInterruption();
		return matrix;
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				result += "(" + j + "," + i + ")" + matrix[j][i] + "   ";
			result += "\n";
		}

		return result;
	}

	public String risquetoString() throws StopRequestException {
		source.checkInterruption();
		String result = "";
		for (int i = 0; i < height; i++) {
			source.checkInterruption();
			for (int j = 0; j < width; j++)
			{	source.checkInterruption();
				result += "(" + j + "," + i + ")" + risque[j][i] + "   ";
			
			}
			result += "\n";
		}

		return result;
	}

	public String actoString() throws StopRequestException {
		source.checkInterruption();
		String result = "";
		for (int i = 0; i < height; i++) {
			source.checkInterruption();
			for (int j = 0; j < width; j++)
			{	source.checkInterruption();
				result += "(" + j + "," + i + ")" + accessibilite[j][i] + "   ";
			}
			result += "\n";
		}

		return result;
	}

	public String murstoString() throws StopRequestException {
		source.checkInterruption();
		String result = "";
		for (int i = 0; i < height; i++) {
			source.checkInterruption();
			for (int j = 0; j < width; j++)
			{	source.checkInterruption();
				result += "(" + j + "," + i + ")" + murs[j][i] + "   ";
			}
			result += "\n";
		}

		return result;
	}

	public boolean isWalkable(int x1, int y1) throws StopRequestException {
		source.checkInterruption();
		boolean resultat = false;

		if (accessibilite[x1][y1] == Etat.ACCESSIBLE)

			resultat = true;

		return resultat;
	}
//pour voir si on peut acceder a un adversaire en explosant les murs
	public boolean isReachable(int x1, int y1) throws StopRequestException {
		source.checkInterruption();
		boolean resultat = false;

		if ((accessibilite[x1][y1] == Etat.ACCESSIBLE || matrix[x1][y1] == Etat.DESTRUCTIBLES)
				&& matrix[x1][y1] != Etat.FLAMMES
				&& matrix[x1][y1] != Etat.FLAMMESPOSS
				&& matrix[x1][y1] != Etat.DANGER)

			resultat = true;

		return resultat;
	}

	int return_risque(int x, int y) throws StopRequestException {
		source.checkInterruption();
		return risque[x][y];
	}

	int[][] return_risque() throws StopRequestException {
		source.checkInterruption();
		return risque;
	}

	Etat[][] return_accessibilite() throws StopRequestException {
		source.checkInterruption();
		return accessibilite;
	}

	int[][] return_murs() throws StopRequestException {
		source.checkInterruption();
		return murs;
	}
}
