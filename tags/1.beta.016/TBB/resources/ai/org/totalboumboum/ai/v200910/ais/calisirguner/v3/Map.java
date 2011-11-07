package org.totalboumboum.ai.v200910.ais.calisirguner.v3;
//on la pris des bleus de l'annee precedente et on la change un peu cf les flammes et es matrices de risque et accesibilite

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
import org.totalboumboum.engine.content.feature.Direction;











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
	private Etat accessibilite[][];
	private int risque[][];

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

	// nous rempla�ons notre map
	private void remplir() {
		accessibilite=new Etat [width][height];
		risque=new int [width][height];
		// premieremnt on met letat libre pour partout
		matrix = new Etat[width][height];
		
		int i, j;
		// Initialisation
		for (i = 0; i < width; i++) {
			for (j = 0; j < height; j++) {
				matrix[i][j] = Etat.LIBRE;
				accessibilite[i][j]=Etat.ACCESSIBLE;
			}
		}
		
		
		// on met les murs

		Iterator<AiBlock> itbl = blocks.iterator();
		AiBlock bl;

		while (itbl.hasNext()) {
			bl = itbl.next();
			xadversaire = bl.getCol();
			yadversaire = bl.getLine();
			accessibilite[xadversaire][yadversaire]=Etat.INACCESSIBLE;

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
			if (matrix[feufutur.getCol()][feufutur.getLine()]!=Etat.DESTRUCTIBLES && matrix[feufutur.getCol()][feufutur.getLine()]!=Etat.INDESTRUCTIBLES){
			matrix[feufutur.getCol()][feufutur.getLine()]=Etat.FLAMMES;
			//on va utiliser pour trouver le meilleur case sur la matrice de risque
			risque[feufutur.getCol()][feufutur.getLine()]=(int) b.getTime()/50;
			//si cest proche a son explosion on le rend inaccessible
			if (b.getTime()>b.getNormalDuration()*5/6 && b.isWorking())
				accessibilite[feufutur.getCol()][feufutur.getLine()]=Etat.INACCESSIBLE;}
			}
		}
		// les feus des explosions

		this.getfires();

		// rempla�ons les bonus

		Iterator<AiItem> itemit = objets.iterator();
		AiItem item;

		while (itemit.hasNext()) {
			item = itemit.next();

			xadversaire = item.getCol();
			yadversaire = item.getLine();
			risque[xadversaire][yadversaire]=-10;

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
			risque[xadversaire][yadversaire]=-5;

			if (hero != bomberman) {

				if (matrix[xadversaire][yadversaire] != Etat.FEU
						&& matrix[xadversaire][yadversaire] != Etat.FLAMMES
						&& matrix[xadversaire][yadversaire] != Etat.BOMBE
						)
					matrix[xadversaire][yadversaire] = Etat.ADVERSAIRE;

			}
		}
		if(matrix[bomberman.getCol()][bomberman.getLine()]!=Etat.FLAMMES )
		matrix[bomberman.getCol()][bomberman.getLine()]=Etat.NOUS;

	}


	public void getbombs() {
		Iterator<AiBomb> itbo = bombes.iterator();
		AiBomb bo;

		while (itbo.hasNext()) {
			bo = itbo.next();
			xadversaire = bo.getCol();
			yadversaire = bo.getLine();
			if (matrix[xadversaire][yadversaire]!= Etat.NOUS ){
			matrix[xadversaire][yadversaire] = Etat.BOMBE;
			
			accessibilite[xadversaire][yadversaire]=Etat.INACCESSIBLE;

		}}
	}

	public void getfires() {
		Iterator<AiFire> itfeu = feu.iterator();
		AiFire feu;

		while (itfeu.hasNext()) {
			feu = itfeu.next();
			xadversaire = feu.getCol();
			yadversaire = feu.getLine();
			accessibilite[xadversaire][yadversaire]=Etat.INACCESSIBLE;

			matrix[xadversaire][yadversaire] = Etat.FEU;

		}
	}
	/**
	 * on va lutiliser pour le cotrole si cest possible de laisser un bombe et
	 * puis courir donc on cree un bombe imaginaire
	 */

	public void setbombeposs(int x1, int y1, int range) {
		matrix[x1][y1] = Etat.BOMBEPOSS;
		accessibilite[x1][y1]=Etat.INACCESSIBLE;
		

		List<AiBlock> dr = bomberman.getTile().getNeighbor(Direction.RIGHT).getBlocks();
		List<AiBlock> ht = bomberman.getTile().getNeighbor(Direction.UP).getBlocks();
		List<AiBlock> gc = bomberman.getTile().getNeighbor(Direction.LEFT)
				.getBlocks();
		List<AiBlock> bs = bomberman.getTile().getNeighbor(Direction.DOWN)
				.getBlocks();

		if (gc == null) {
			for (int q = 1; q <= range; q++) {
				if (x1 - q > 0
						&& (matrix[x1 - q][y1] == Etat.FLAMMES ))
					accessibilite[x1 - q][y1] = Etat.INACCESSIBLE;
				else if (x1 - q > 0
						&& matrix[x1 - q][y1] != Etat.INDESTRUCTIBLES
						&& matrix[x1 - q][y1] != Etat.DESTRUCTIBLES){
					matrix[x1 - q][y1] = Etat.FLAMMESPOSS;
					
			 }
				else
					break;

			}
		}
		if (dr == null) {
			for (int q = 1; q <= range; q++) {
				if (x1 + q < width
						&& (matrix[x1 + q][y1] == Etat.FLAMMES ))
					accessibilite[x1 + q][y1] = Etat.INACCESSIBLE;
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
						&& (matrix[x1][y1 + q] == Etat.FLAMMES ))
					accessibilite[x1][y1+q] = Etat.INACCESSIBLE;
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
						&& (matrix[x1][y1 - q] == Etat.FLAMMES ))
					accessibilite[x1 ][y1-q] = Etat.INACCESSIBLE;
				else  if (y1 - q > 0
						&& matrix[x1][y1 - q] != Etat.INDESTRUCTIBLES
						&& matrix[x1][y1 - q] != Etat.DESTRUCTIBLES)
					matrix[x1][y1 - q] = Etat.FLAMMESPOSS;
				else
					break;

			}
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
	
	public String risquetoString() {
		String result = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				result += "(" + j + "," + i + ")" + risque[j][i] + "   ";
			result += "\n";
		}

		return result;
	}
	public String actoString() {
		String result = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				result += "(" + j + "," + i + ")" + accessibilite[j][i] + "   ";
			result += "\n";
		}

		return result;
	}

		public boolean isWalkable(int x1, int y1) {

		boolean resultat = false;


		if (accessibilite[x1][y1]==Etat.ACCESSIBLE)
				
		
			resultat = true;

		return resultat;
	}
		int return_risque (int x,int y){
			return risque[x][y];}
		
		int [][]return_risque (){
			return risque;}
		Etat [][]return_accessibilite (){
			return accessibilite;}

}