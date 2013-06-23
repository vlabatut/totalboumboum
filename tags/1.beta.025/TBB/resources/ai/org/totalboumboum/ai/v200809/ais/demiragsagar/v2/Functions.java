package org.totalboumboum.ai.v200809.ais.demiragsagar.v2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Doğus Burcu Demirağ
 * @author Zeynep Şagar
 *
 */
@SuppressWarnings("deprecation")
public class Functions {
	/** regarde si il existe un mur de type SOFT ou HARD
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean hasWall(AiTile tile) {
		boolean result;
		AiBlock block = tile.getBlock();
		result = block != null;
		return result;
	}
	/**
	 * La matrice de la zone 
	 * On voit -1 s'il ya un mur de type hard ou soft
	 * On voit 0 s'il n'ya pas de mur
	 * @param maMatrice 
	 * 		Description manquante !
	 */
	public static void printMatrice(long maMatrice[][]) {
		int i, j;
		System.out.println("Affichement de la matrice des bombes:");
//		for (j = 0; j < 15; j++) {
		for (j = 0; j < maMatrice[0].length; j++) { //adjustment
//			for (i = 0; i < 17; i++)
			for (i = 0; i < maMatrice.length; i++) //adjustment
				if (maMatrice[i][j] >= 0)
					System.out.print(" " + maMatrice[i][j]);
				else
					System.out.print(maMatrice[i][j]);
			System.out.println();
		}
	}
	/**
	 * Retourne true si deux nodes ont les meme coordonnees
	 * @param node1 
	 * 		Description manquante !
	 * @param node2 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean memeCordonnes(Node node1,Node node2) {
		return Functions.memeCordonnes(node1.getTile(),node2.getTile());
	}
	/**
	 * Retourne true si deux cases ont les meme coordonnees
	 * @param tile1 
	 * 		Description manquante !
	 * @param tile2 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean memeCordonnes(AiTile tile1,AiTile tile2) {
		return tile1.getCol()==tile2.getCol() && tile1.getLine()==tile2.getLine();
	}
	/**
	 * On donne une case cible(AiTile target) et cette fonction trouve dans ue liste des cases la case plus proche a cette case cible 
	 * @param target 
	 * 		Description manquante !
	 * @param tiles 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static AiTile TheCloserTile(AiTile target,List<AiTile> tiles)
	{
		AiTile minTile = null;
		int valeur=-1;
		if(!tiles.isEmpty())
		{
			for(AiTile temp : tiles)
			{
				int dist=Functions.trouveDistance(temp,target);
				if(valeur==-1 || (dist!=-1 && valeur>dist))
				{
					valeur=Functions.trouveDistance(temp,target);
					minTile=temp;
				}
			}
		}
		return minTile;
	}

	/** On trouve avec combien de cases on peut aller a une case cible
	 * 
	 * @param current
	 * 		Description manquante !
	 * @param target
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static int trouveDistance(AiTile current, AiTile target) {
		if(Functions.memeCordonnes(current, target)) return 0;
		AStar arbre = new AStar(current, target, false);
		arbre.formeArbre();
		LinkedList<Node> path=arbre.getPath();
		if (path == null)
			return -1;
		return path.size();
	}
	/**
	 * Trouver les noeuds fils d'une case
	 * @param courant 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static int ChildNodes(AiTile courant) {
		int result=0;
		if (!hasWall(courant.getNeighbor(Direction.UP)))
			result++;
		if (!hasWall(courant.getNeighbor(Direction.DOWN)))
			result++;				
		if (!hasWall(courant.getNeighbor(Direction.LEFT)))
			result++;			
		if (!hasWall(courant.getNeighbor(Direction.RIGHT)))
			result++;
		return result;
	}
	/**
	 * Cette fonction retourne true si une case ne contient pas de block ni de feu et nide bombe
	 * @param tile 
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static boolean isClear(AiTile tile){
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block == null && bombs.size() == 0 && fires.size() == 0;
		return result;
	}
//	public static int trouveDistance(int colon1, int line1, int colon2, int line2) {
//		AiTile case1 = this.zone.getTile(line1, colon1);
//		AiTile case2 = this.zone.getTile(line2, colon2);
//		return trouveDistance(case1,case2);
//	}


}
