package org.totalboumboum.ai.v200910.ais.calisirguner.v2;

import java.util.Collection;
import java.util.List;

import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * on la pris des bleus de l'annee precedente et on la change un peu cf les flammes
 * 
 * @version 2
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
		this.height = zone.getHeigh();

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
		// pour les flammes
		
		AiBomb b;
	

		Iterator<AiBomb> danger = bombes.iterator();

		while (danger.hasNext()) {
			// APPEL OBLIGATOIRE
			b = danger.next();
			List <AiTile> Blast=b.getBlast();
			Iterator<AiTile> tile=Blast.iterator();
			AiTile feufutur;
			while (tile.hasNext())
			{feufutur=tile.next();
			if (matrix[feufutur.getCol()][feufutur.getLine()]!=Etat.DESTRUCTIBLES && matrix[feufutur.getCol()][feufutur.getLine()]!=Etat.INDESTRUCTIBLES)
			matrix[feufutur.getCol()][feufutur.getLine()]=Etat.FLAMMES;
			}
				
		}
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
						)
					matrix[xadversaire][yadversaire] = Etat.ADVERSAIRE;

			}
		}

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

		public boolean isWalkable(int x1, int y1) {

		boolean resultat = false;

		if (matrix[x1][y1] != Etat.INDESTRUCTIBLES&& matrix[x1][y1] != Etat.BOMBE
				&& matrix[x1][y1] != Etat.DESTRUCTIBLES

		)
			resultat = true;

		return resultat;
	}

}
