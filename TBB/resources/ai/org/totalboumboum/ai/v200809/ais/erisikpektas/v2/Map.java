package org.totalboumboum.ai.v200809.ais.erisikpektas.v2;

import java.util.Collection;

import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğa Erişik
 * @author Abdurrahman Pektaş
 *
 */
@SuppressWarnings("deprecation")
public class Map {

	@SuppressWarnings("unused")
	private AiZone map;
	private Collection<AiHero> adversaires;

	private AiHero bomberman;

	private Collection<AiBomb> bombes;
	private Collection<AiBlock> blocks;
	private Collection<AiItem> objets;
	private Collection<AiFire> feu;
	public int width;
	public int height;

	private int xadversaire, yadversaire;
	private Etat matrix[][];

	public Map(AiZone zone) {
		this.map = zone;
		this.bomberman = zone.getOwnHero();

		this.adversaires = zone.getHeroes();
		this.bombes = zone.getBombs();
		this.blocks = zone.getBlocks();
		this.objets = zone.getItems();
		this.feu = zone.getFires();
		this.width = zone.getWidth();
		this.height = zone.getHeight();

		remplir();
	}

	// nous remplaçons notre map
	private void remplir() {

		// premieremnt on met letat libre pour partout
		matrix = new Etat[width][height];
		int i, j;
		// Initialisation
		for (i = 0; i < width; i++) {
			for (j = 0; j < height; j++) {
				matrix[i][j] = Etat.LIBRE;
			}
		}

		// on met les murs

		Iterator<AiBlock> itbl = blocks.iterator();
		AiBlock bl;

		while (itbl.hasNext()) {
			bl = itbl.next();
			xadversaire = bl.getCol();
			yadversaire = bl.getLine();

			if (!bl.isDestructible())
				matrix[xadversaire][yadversaire] = Etat.INDESTRUCTIBLES;
			else
				matrix[xadversaire][yadversaire] = Etat.DESTRUCTIBLES;

		}

		// les bombes

		// les feus des explosions

		this.getfires();

		// remplaçons les bonus

		Iterator<AiItem> itemit = objets.iterator();
		AiItem item;

		while (itemit.hasNext()) {
			item = itemit.next();

			xadversaire = item.getCol();
			yadversaire = item.getLine();

			if (matrix[xadversaire][yadversaire] != Etat.FEU)
				matrix[xadversaire][yadversaire] = Etat.POINT;

		}
		// les bombes et les flammes et les dangers==>intersection de plusieurs
		// flammes

		AiTile casebombe;
		AiBomb b;
	

		Iterator<AiBomb> danger = bombes.iterator();

		while (danger.hasNext()) {
			// APPEL OBLIGATOIRE
			b = danger.next();
			casebombe = b.getTile();
			xadversaire = b.getCol();
			yadversaire = b.getLine();
			

			AiBlock right = casebombe.getNeighbor(Direction.RIGHT).getBlock();
			AiBlock up = casebombe.getNeighbor(Direction.UP).getBlock();
			AiBlock left = casebombe.getNeighbor(Direction.LEFT).getBlock();
			AiBlock down = casebombe.getNeighbor(Direction.DOWN).getBlock();

			if (left == null) {
				for (int q = 1; q <= b.getRange(); q++) {

					/*
					 * if (xadversaire - q > 0 && matrix[xadversaire -
					 * q][yadversaire] == Etat.POINT){
					 * 
					 * while (xadversaire -q >0&&q<=b.getRange()){
					 * 
					 * matrix[xadversaire -q][yadversaire] = Etat.POINTFLAMMES;
					 * q++; } break;
					 * 
					 * } else
					 */

					if (xadversaire - q < width
							&& (matrix[xadversaire - q][yadversaire] == Etat.BOMBE))
						matrix[xadversaire - q][yadversaire] = Etat.BOMBE;
					else if (xadversaire - q > 0
							&& (matrix[xadversaire - q][yadversaire] == Etat.FLAMMES || matrix[xadversaire
									- q][yadversaire] == Etat.DANGER))
						matrix[xadversaire - q][yadversaire] = Etat.DANGER;
					else if (xadversaire - q < width
							&& (matrix[xadversaire - q][yadversaire] == Etat.FEU))
						matrix[xadversaire - q][yadversaire] = Etat.FEUFLAMMES;

					else if (xadversaire - q > 0
							// && matrix[xadversaire - q][yadversaire] !=
							// Etat.POINTFLAMMES

							&& matrix[xadversaire - q][yadversaire] != Etat.INDESTRUCTIBLES
							&& matrix[xadversaire - q][yadversaire] != Etat.DESTRUCTIBLES)
						matrix[xadversaire - q][yadversaire] = Etat.FLAMMES;
					else
						break;

				}
			}

			if (right == null) {
				for (int q = 1; q <= b.getRange(); q++) {
					/*
					 * if ((xadversaire + q < width && matrix[xadversaire +
					 * q][yadversaire] == Etat.POINT)){
					 * 
					 * while (xadversaire + q < width &&q<=b.getRange()){
					 * 
					 * matrix[xadversaire + q][yadversaire] = Etat.POINTFLAMMES;
					 * q++; } break;
					 * 
					 * } else
					 */if (xadversaire + q < width
							&& (matrix[xadversaire + q][yadversaire] == Etat.BOMBE))
						matrix[xadversaire + q][yadversaire] = Etat.BOMBE;
					else if (xadversaire + q < width
							&& (matrix[xadversaire + q][yadversaire] == Etat.FEU))
						matrix[xadversaire + q][yadversaire] = Etat.FEUFLAMMES;
					else if (xadversaire + q < width
							&& (matrix[xadversaire + q][yadversaire] == Etat.FLAMMES || matrix[xadversaire
									+ q][yadversaire] == Etat.DANGER))
						matrix[xadversaire + q][yadversaire] = Etat.DANGER;
					else if (xadversaire + q < width

							&& matrix[xadversaire + q][yadversaire] != Etat.INDESTRUCTIBLES
							&& matrix[xadversaire + q][yadversaire] != Etat.DESTRUCTIBLES)
						matrix[xadversaire + q][yadversaire] = Etat.FLAMMES;
					else
						break;

				}
			}
			if (down == null) {
				for (int q = 1; q <= b.getRange(); q++) {

					/*
					 * if (yadversaire +q<height&&
					 * matrix[xadversaire][yadversaire+q] == Etat.POINT){
					 * 
					 * while (yadversaire +q >0&&q<=b.getRange()){
					 * 
					 * matrix[xadversaire ][yadversaire+q] = Etat.POINTFLAMMES;
					 * q++; } break;
					 * 
					 * } else
					 */
					if (yadversaire + q < height
							&& (matrix[xadversaire][yadversaire + q] == Etat.BOMBE))
						matrix[xadversaire][yadversaire + q] = Etat.BOMBE;
					else if (yadversaire + q < height
							&& (matrix[xadversaire][yadversaire + q] == Etat.FEU))
						matrix[xadversaire][yadversaire + q] = Etat.FEUFLAMMES;
					else if (yadversaire + q < height
							&& (matrix[xadversaire][yadversaire + q] == Etat.FLAMMES || matrix[xadversaire][yadversaire
									+ q] == Etat.DANGER))
						matrix[xadversaire][yadversaire + q] = Etat.DANGER;
					else if (yadversaire + q < height
							// && matrix[xadversaire][yadversaire + q] !=
							// Etat.POINTFLAMMES

							&& matrix[xadversaire][yadversaire + q] != Etat.INDESTRUCTIBLES
							&& matrix[xadversaire][yadversaire + q] != Etat.DESTRUCTIBLES)
						matrix[xadversaire][yadversaire + q] = Etat.FLAMMES;
					else
						break;

				}
			}
			if (up == null) {
				for (int q = 1; q <= b.getRange(); q++) {

					/*
					 * if (yadversaire - q > 0 &&
					 * matrix[xadversaire][yadversaire-q] == Etat.POINT){
					 * 
					 * while (yadversaire -q >0&&q<=b.getRange()){
					 * 
					 * matrix[xadversaire ][yadversaire-q] = Etat.POINTFLAMMES;
					 * q++; } break;
					 * 
					 * } else
					 */
					if (yadversaire - q < height
							&& (matrix[xadversaire][yadversaire - q] == Etat.BOMBE))
						matrix[xadversaire][yadversaire - q] = Etat.BOMBE;
					else if (yadversaire - q < height
							&& (matrix[xadversaire][yadversaire - q] == Etat.FEU))
						matrix[xadversaire][yadversaire - q] = Etat.FEUFLAMMES;
					else if (yadversaire - q > 0
							&& (matrix[xadversaire][yadversaire - q] == Etat.FLAMMES || matrix[xadversaire][yadversaire
									- q] == Etat.DANGER))
						matrix[xadversaire][yadversaire - q] = Etat.DANGER;
					else if (yadversaire - q > 0
							// && matrix[xadversaire][yadversaire - q] !=
							// Etat.POINTFLAMMES

							&& matrix[xadversaire][yadversaire - q] != Etat.INDESTRUCTIBLES
							&& matrix[xadversaire][yadversaire - q] != Etat.DESTRUCTIBLES)
						matrix[xadversaire][yadversaire - q] = Etat.FLAMMES;
					else
						break;

				}
			}

		}
		this.getbombs();
		// et enfin les adversaires

		Iterator<AiHero> it = adversaires.iterator();
		AiHero hero;

		while (it.hasNext()) {

			hero = it.next();
			xadversaire = hero.getCol();
			yadversaire = hero.getLine();

			if (hero != bomberman) {

				if (matrix[xadversaire][yadversaire] != Etat.FEU
						&& matrix[xadversaire][yadversaire] != Etat.FLAMMES
						&& matrix[xadversaire][yadversaire] != Etat.BOMBE
						&& matrix[xadversaire][yadversaire] != Etat.DANGER)
					matrix[xadversaire][yadversaire] = Etat.ADVERSAIRE;

			}
		}

	}

	/**
	 * cest le plus difficle condition a obtenir et naturellemnt cest le plus
	 * sur
	 */
	public boolean isWalkable(int x1, int y1) {

		boolean resultat = false;

		if (matrix[x1][y1] == Etat.POINT || matrix[x1][y1] == Etat.ADVERSAIRE
				|| matrix[x1][y1] == Etat.LIBRE

		)
			resultat = true;

		return resultat;
	}

	/** nous allons lutiliser pour senfuire car on peut passer par les flmmes */
	public boolean isRunnable(int x1, int y1) {

		boolean resultat = false;

		if (matrix[x1][y1] != Etat.BOMBE && matrix[x1][y1] != Etat.BOMBEPOSS
				&& matrix[x1][y1] != Etat.DANGER
				&& matrix[x1][y1] != Etat.DESTRUCTIBLES
				&& matrix[x1][y1] != Etat.FEU
				&& matrix[x1][y1] != Etat.INDESTRUCTIBLES
				&& matrix[x1][y1] != Etat.BOMBEDANGER
				&& matrix[x1][y1] != Etat.FEUFLAMMES

		// &&matrix[x1][y1]!=Etat.POINTFLAMMES
		)

			resultat = true;
		return resultat;
	}

	/**
	 * on va utiliser cette methode pour voir sil ya qqch quon peut acceder en
	 * laiissant des bombes car elle peut avoir des murs dest
	 */
	public boolean isReachable(int x1, int y1) {

		boolean resultat = false;

		if (matrix[x1][y1] != Etat.BOMBE && matrix[x1][y1] != Etat.BOMBEPOSS
				&& matrix[x1][y1] != Etat.DANGER && matrix[x1][y1] != Etat.FEU
				&& matrix[x1][y1] != Etat.FLAMMES
				&& matrix[x1][y1] != Etat.FLAMMESPOSS
				&& matrix[x1][y1] != Etat.INDESTRUCTIBLES
				&& matrix[x1][y1] != Etat.BOMBEDANGER
				&& matrix[x1][y1] != Etat.FEUFLAMMES)

			resultat = true;
		return resultat;
	}

	/**
	 * on peut passer par des danger et des flammes on la cree car qd qqn met
	 * deux bombes en meme temps on //ne bouge pas car on voit comme on na pas
	 * de lieu pour se cacher
	 */
	public boolean isNoWhereElse(int x1, int y1) {

		boolean resultat = false;

		if (matrix[x1][y1] != Etat.BOMBE && matrix[x1][y1] != Etat.BOMBEPOSS
				&& matrix[x1][y1] != Etat.FEU
				&& matrix[x1][y1] != Etat.DESTRUCTIBLES

				&& matrix[x1][y1] != Etat.INDESTRUCTIBLES
				&& matrix[x1][y1] != Etat.BOMBEDANGER
				&& matrix[x1][y1] != Etat.FEUFLAMMES

		)

			resultat = true;
		return resultat;
	}

	/**
	 * on va lutiliser pour le cotrole si cest possible de laisser un bombe et
	 * puis courir donc on cree un bombe imaginaire
	 */

	public void setbombeposs(int x1, int y1, int range) {
		matrix[x1][y1] = Etat.BOMBEPOSS;

		AiBlock dr = bomberman.getTile().getNeighbor(Direction.RIGHT)
				.getBlock();
		AiBlock ht = bomberman.getTile().getNeighbor(Direction.UP).getBlock();
		AiBlock gc = bomberman.getTile().getNeighbor(Direction.LEFT)
				.getBlock();
		AiBlock bs = bomberman.getTile().getNeighbor(Direction.DOWN)
				.getBlock();

		if (gc == null) {
			for (int q = 1; q <= range; q++) {

				if (x1 - q > 0
						&& (matrix[x1 - q][y1] == Etat.FLAMMES || matrix[x1 - q][y1] == Etat.DANGER))
					matrix[x1 - q][y1] = Etat.DANGER;
				else if (x1 - q > 0
						&& matrix[x1 - q][y1] != Etat.INDESTRUCTIBLES
						&& matrix[x1 - q][y1] != Etat.DESTRUCTIBLES)
					matrix[x1 - q][y1] = Etat.FLAMMESPOSS;
				else
					break;

			}
		}
		if (dr == null) {
			for (int q = 1; q <= range; q++) {
				if (x1 + q < width
						&& (matrix[x1 + q][y1] == Etat.FLAMMES || matrix[x1 + q][y1] == Etat.DANGER))
					matrix[x1 + q][y1] = Etat.DANGER;
				else if (x1 + q < width
						&& matrix[x1 + q][y1] != Etat.INDESTRUCTIBLES
						&& matrix[x1 + q][y1] != Etat.DESTRUCTIBLES)
					matrix[x1 + q][y1] = Etat.FLAMMESPOSS;
				else
					break;

			}
		}
		if (bs == null) {
			for (int q = 1; q <= range; q++) {
				if (y1 + q < height
						&& (matrix[x1][y1 + q] == Etat.FLAMMES || matrix[x1][y1
								+ q] == Etat.DANGER))
					matrix[x1][y1 + q] = Etat.DANGER;
				else if (y1 + q < height
						&& matrix[x1][y1 + q] != Etat.INDESTRUCTIBLES
						&& matrix[x1][y1 + q] != Etat.DESTRUCTIBLES)
					matrix[x1][y1 + q] = Etat.FLAMMESPOSS;
				else
					break;

			}
		}
		if (ht == null) {
			for (int q = 1; q <= range; q++) {
				if (y1 - q > 0
						&& (matrix[x1][y1 - q] == Etat.FLAMMES || matrix[x1][y1
								- q] == Etat.DANGER))
					matrix[x1][y1 - q] = Etat.DANGER;
				else if (y1 - q > 0
						&& matrix[x1][y1 - q] != Etat.INDESTRUCTIBLES
						&& matrix[x1][y1 - q] != Etat.DESTRUCTIBLES)
					matrix[x1][y1 - q] = Etat.FLAMMESPOSS;
				else
					break;

			}
		}

	}

	public void setdanger() {

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (matrix[i][j] == Etat.FLAMMES || matrix[i][j] == Etat.FEU
						|| matrix[i][j] == Etat.DANGER)
					matrix[i][j] = Etat.DANGER;
			}
		}
	}

	// qd on transforme un bombe possible a un bombe reele
	public void setbombe(int x1, int y1) {
		this.returnMatrix()[x1][y1] = Etat.BOMBE;

	}

	/**
	 * si cest pas possible de trouver un lieu sur apres avoir laisser une bombe
	 * imaginaire il faut lenlevere
	 */
	public void removebombe() {
		remplir();

	}

	/**
	 * au cas de danger il court a un bombe la cause est peut etre quil faut
	 * tjrs mettre a jour le map donc on a cree ces fonc-la
	 */
	public void getbombs() {
		Iterator<AiBomb> itbo = bombes.iterator();
		AiBomb bo;

		while (itbo.hasNext()) {
			bo = itbo.next();
			xadversaire = bo.getCol();
			yadversaire = bo.getLine();

			matrix[xadversaire][yadversaire] = Etat.BOMBE;

		}
	}

	public void getfires() {
		Iterator<AiFire> itfeu = feu.iterator();
		AiFire feu;

		while (itfeu.hasNext()) {
			feu = itfeu.next();
			xadversaire = feu.getCol();
			yadversaire = feu.getLine();

			matrix[xadversaire][yadversaire] = Etat.FEU;

		}
	}

	// ,l envoie la matrice de map
	public Etat[][] returnMatrix() {
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

}
