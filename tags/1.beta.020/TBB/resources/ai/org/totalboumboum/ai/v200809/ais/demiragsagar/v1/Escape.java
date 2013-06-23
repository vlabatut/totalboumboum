package org.totalboumboum.ai.v200809.ais.demiragsagar.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Dogus Burcu Demirag
 * @author Zeynep Sagar
 *
 */
public class Escape {
	private List<AiTile> bombes;
	
	public Escape(List<AiTile> bombes) {
		this.bombes=bombes;
	}

	public int[][] getMatrice(AiZone zone) {
		int[][] maMatrice = new int[16][16];
		int etki;
		int i, j;
		for (j = 0; j < 15; j++)
			for (i = 0; i < 16; i++)
				if (hasWall(zone.getTile(j, i)))
					maMatrice[i][j] = -1;
				else
					maMatrice[i][j] = 0;
		for (AiTile t : bombes) {
			int x = t.getCol();
			int y = t.getLine();
			try {

				// System.out.println("Bomba:"+t.getCol()+" "+t.getLine());
				boolean up = false, down = false, left = false, right = false;
				maMatrice[x][y]++;
				for (etki = 1; etki <= 5; etki++) {
					if (x + etki < 13)
						if (maMatrice[x + etki][y] != -1 && right == false)
							maMatrice[x + etki][y]++;
						else
							right = true;
					if (x - etki > 3)
						if (maMatrice[x - etki][y] != -1 && left == false)
							maMatrice[x - etki][y]++;
						else
							left = true;
					if (y + etki < 12)
						if (maMatrice[x][y + etki] != -1 && down == false)
							maMatrice[x][y + etki]++;
						else
							down = true;
					if (y - etki > 2)
						if (maMatrice[x][y - etki] != -1 && up == false)
							maMatrice[x][y - etki]++;
						else
							up = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return maMatrice;

	}

	private boolean hasWall(AiTile tile) {
		boolean result;
		AiBlock block = tile.getBlock();
		result = block != null;
		return result;
	}

	// public ArrayList<AiTile> getIntersection(AiZone zone) {
	// int matrice[][] = this.getMatrice(zone);
	// int i, j;
	// ArrayList<AiTile> mesTiles = new ArrayList<AiTile>();
	// for (i = 0; i < 16; i++)
	// for (j = 0; j < 15; j++)
	// if (matrice[i][j] > 1)
	// mesTiles.add(zone.getTile(i, j));
	// return mesTiles;
	// }

	public List<AiTile> getIntersection(AiZone zone) {
		int matrice[][] = this.getMatrice(zone);
		int i, j;
		List<AiTile> mesTiles = new ArrayList<AiTile>();
		for (i = 0; i < 16; i++)
			for (j = 0; j < 15; j++)
				if (matrice[i][j] > 1)
					mesTiles.add(zone.getTile(j, i));
		return mesTiles;
	}

	private boolean isClear(AiTile tile) {

		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block == null && bombs.size() == 0 && fires.size() == 0;
		return result;
	}

	public AiTile getEscape(AiTile courant, AiZone zone) {
		int[][] matrice =this.getMatrice(zone);
		NoeudComparator monComp = new NoeudComparator();
		PriorityQueue<Node> kuyruk = new PriorityQueue<Node>(1,monComp);
		kuyruk.offer(new Node(courant,0));
		Node temp = kuyruk.poll();
		//System.out.flush();
		while (matrice[temp.tile.getCol()][temp.tile.getLine()] != 0) {
			//System.out.println(temp.getLine()+":"+temp.getCol()+"="+matrice[temp.getCol()][temp.getLine()]);
			if (isClear(temp.tile.getNeighbor(Direction.DOWN)))
				kuyruk.offer(new Node(temp.tile.getNeighbor(Direction.DOWN),temp.heuristic+1));
			if (isClear(temp.tile.getNeighbor(Direction.LEFT)))
				kuyruk.offer(new Node(temp.tile.getNeighbor(Direction.LEFT),temp.heuristic+1));
			if (isClear(temp.tile.getNeighbor(Direction.UP)))
				kuyruk.offer(new Node(temp.tile.getNeighbor(Direction.UP),temp.heuristic+1));
			if (isClear(temp.tile.getNeighbor(Direction.RIGHT)))
				kuyruk.offer(new Node(temp.tile.getNeighbor(Direction.RIGHT),temp.heuristic+1));
			temp = kuyruk.poll();
		}
		//System.out.println(temp.tile.getLine()+":"+temp.tile.getCol()+"="+matrice[temp.tile.getCol()][temp.tile.getLine()]);
		matrice[courant.getCol()][courant.getLine()]=-3;
		matrice[temp.tile.getCol()][temp.tile.getLine()]=-2;
	//	printMatrice(matrice);
		return temp.tile;
	}
	
	public void printMatrice(int maMatrice[][])
	{
		int i,j;
		System.out.println("basladi");
		for(j=0;j<15;j++) {
			for(i=0;i<16;i++) {
				if(maMatrice[i][j]>=0)
					System.out.print(" "+maMatrice[i][j]);
				else
					System.out.print(maMatrice[i][j]);
				}
			System.out.println();
		}
	}

}