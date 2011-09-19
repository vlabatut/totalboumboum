package org.totalboumboum.ai.v201011.ais.ozdokerozen.v1;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class OzdokerOzen extends ArtificialIntelligence
{	
	 //pour les sortie dans API
	private AiOutput ecran;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		//on a re�u la zone du jeu
		AiZone gameZone = getPercepts();
		
		ecran=getOutput();
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		
		// la longueur de la zone
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// System.out.println(width+"X"+height);
		
		// la matrice de la zone
		int[][] matrice = new int[height][width];
		
		
		// initialisation et calculation de matrice de la collecte avec des fonctions
		//de gorupe rouge l'annee dernier
		this.initialiseMatrice(matrice, gameZone);
		this.fillBombsMatrice(matrice, gameZone);
		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);
	
		//on ecrit le matrice dans le console
		//pour controle
		/*
		for(int i=0;i<height;i++){
			System.out.print(" {");
			for(int j=0;j<width;j++){
			System.out.print("("+matrice[i][j]+"),");
			}
			System.out.print(" }\n");
			
		}
		cizgi();
		*/
		return result;
	}

	//on fait une essaie pour affiche dans le console
	public void cizgi() throws StopRequestException{
		// avant tout : test d'interruption
		checkInterruption();
		System.out.println("------------------");	

	}
	
	/**
	 * Methode initialisant notre matrice de zone avant la remplissage. Chaque
	 * case est initialement considere comme 1.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */	
	private void initialiseMatrice(int[][] matrice, org.totalboumboum.ai.v201011.adapter.data.AiZone gameZone)
	throws StopRequestException {
		checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			checkInterruption();
				for (int j = 0; j < width; j++) {
					checkInterruption();
					//butun alanlar� guvenli dusunuyoruz! 
					matrice[i][j] = 1;
				}
		}
	}
	
	/**
	* Methode remplissant les cases de notre matrice de zone possedant une
	 * bombe et les cases qui sont dans la portee de ces bombes. Ces nouveaux
	 * cases sont represente par -1.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBombsMatrice(int[][] matrice,AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = -1;
			//pour les API, on a donn� une color
			ecran.setTileColor(bomb.getLine(), bomb.getCol(), Color.BLACK);
			//la zone de dange
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 1; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = -3;
				ecran.setTileColor(inScopeTiles.get(i).getLine(),inScopeTiles.get(i).getCol(), Color.DARK_GRAY);
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une mur.
	 * Les cases des mures non-destructibles sont representees par
	 * 0 et destructibles par 5.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBlocksMatrice(int[][] matrice, AiZone gameZone)
	throws StopRequestException {
	checkInterruption();
	List<AiBlock> blocks = gameZone.getBlocks();
	Iterator<AiBlock> iteratorBlocks = blocks.iterator();
	while (iteratorBlocks.hasNext()) {
	checkInterruption();
	AiBlock block = iteratorBlocks.next();
	if (block.isDestructible()){
		matrice[block.getLine()][block.getCol()] = 5;
		//pour les API, on a donn� une color
		ecran.setTileColor(block.getLine(), block.getCol(), Color.LIGHT_GRAY);}
	else
		matrice[block.getLine()][block.getCol()] = 0;
		ecran.setTileColor(block.getLine(), block.getCol(), Color.orange);
}
}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * flamme. Les cases des flammes sont represente par -2
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillFiresMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = -2;
			//pour les API, on a donn� une color
			ecran.setTileColor(fire.getLine(), fire.getCol(), Color.white);
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * item. Les cases des items sont represente par 10.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillItemsBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = 10;
			//pour les API, on a donn� une color
			ecran.setTileColor(item.getLine(), item.getCol(), Color.CYAN);
		}
	}
}
