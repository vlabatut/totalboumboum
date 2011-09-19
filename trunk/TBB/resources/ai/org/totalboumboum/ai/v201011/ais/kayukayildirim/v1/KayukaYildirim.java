package org.totalboumboum.ai.v201011.ais.kayukayildirim.v1;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

@SuppressWarnings("unused")
public class KayukaYildirim extends ArtificialIntelligence
{	
	private final int BOMBE = -100;
	private final int PORTEBOMBE=-100;
	private final int MUR_DESTRUCTIBLE = -500;
	private final int MUR_INDESTRUCTIBLE = -500;
	private final int FLAMME = -500;
	private final int ADVERSAIRE = -20;
	private final int BONUS_EXTRA_BOMBE = 20;
	private final int BONUS_EXTRA_FLAMME = 20;
	private int[][] matriceCol;
	private AiHero notre_hero;
	AiZone gameZone;
	AiOutput output;
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(notre_hero==null)
		initPerso();
		updateMatriceCollecte(matriceCol,gameZone);
		colorerMatriceCollecte(matriceCol,gameZone);
		AiAction result = new AiAction(AiActionName.NONE);
		Color color = Color.cyan;
		output.setTileColor(3,5,color);
		return result;
	}
	/*initialisation de getZone, notre_hero et matriceCollecte(par la
	 methode initialiseMatriceCollecte)*/
	private void initPerso()throws StopRequestException
	{
		checkInterruption();
		gameZone =getPercepts();
		notre_hero=gameZone.getOwnHero();
		initialiseMatriceCollecte(matriceCol, gameZone);
	}


//Metod qui initialise une matrice en donnant 0 a tous les coordonnees du matrice
	private void initialiseMatriceCollecte(int[][] matriceCollecte, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		int height=gameZone.getHeight();
		int width=gameZone.getWidth();
		for(int i=0; i<height; i++){
			checkInterruption();
			for(int j=0; j<width; j++){
				checkInterruption();
				matriceCollecte[i][j] = 0;
				
			}
		}
	}
	//Affichage a l`ecran de la matrice collecte en colorant les cases.
	private void colorerMatriceCollecte(int[][] matriceCollecte, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		int height=gameZone.getHeight();
		int width=gameZone.getWidth();
		for(int i=0; i<height; i++){
			checkInterruption();
			for(int j=0; j<width; j++){
				checkInterruption();
				Color color = null;			
				if(matriceCollecte[i][j]==0)
					color = Color.WHITE;
				else if(matriceCollecte[i][j]>0)
					color = Color.GREEN;
				else if(matriceCollecte[i][j]<0)
					color = Color.RED;
				output.setTileColor(i,j,color);
				
			}
		}
	}
	//Methode qui remplace les spirits et les adversaires dans la matrice
	private void updateMatriceCollecte(int[][] marticeCollecte, AiZone gameZone)throws StopRequestException{
		checkInterruption();
		placeBombsMatriceCollecte(marticeCollecte,gameZone);
		placeBonusMatriceCollecte(marticeCollecte,gameZone);
		placeFlammesMatriceCollecte(marticeCollecte,gameZone);
		placeBlocksMatriceCollecte(marticeCollecte,gameZone);
		placeAdversaireMatriceCollecte(marticeCollecte,gameZone);
		}
	//Methode qui place les valeurs des bombes a la matrice
	private void placeBombsMatriceCollecte(int[][] matriceCollecte, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matriceCollecte[bomb.getLine()][bomb.getCol()] = BOMBE;
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
			while (iteratorScope.hasNext()) {
				checkInterruption();
				matriceCollecte[iteratorScope.next().getLine()][iteratorScope
						.next().getCol()] = PORTEBOMBE;
			}
		}
	}
	//Methode qui place les valeurs des bonus a la matrice
	private void placeBonusMatriceCollecte(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			if (item.getType().name() == "extrabomb") {
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_BOMBE;

			} else if (item.getType().name() == "extraflame") {
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_FLAMME;
	
			}
		}
}
	//Methode qui place les valeurs des flammes a la matrice
	private void placeFlammesMatriceCollecte(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = FLAMME;
		}
	}
	//Methode qui place les valeurs des blocks a la matrice
	private void placeBlocksMatriceCollecte(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible())
				matrice[block.getLine()][block.getCol()] = MUR_DESTRUCTIBLE;
			else
				matrice[block.getLine()][block.getCol()] = MUR_INDESTRUCTIBLE;
		}
}
	//Methode qui place les valeurs des adversaire a la matrice
	private void placeAdversaireMatriceCollecte(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiHero> heros = gameZone.getRemainingHeroes();
		for (int i = 0; i < heros.size(); i++) {
			checkInterruption();
			if (notre_hero.getLine() != heros.get(i).getLine()
					&& notre_hero.getCol() != heros.get(i).getCol())
				matrice[heros.get(i).getLine()][heros.get(i).getCol()] = ADVERSAIRE;
		}

	}
}

