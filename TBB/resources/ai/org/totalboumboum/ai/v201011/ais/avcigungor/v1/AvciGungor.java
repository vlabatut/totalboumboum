package org.totalboumboum.ai.v201011.ais.avcigungor.v1;

import java.awt.Color;
import java.text.NumberFormat;
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
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Ibrahim Avcı
 * @author Burak Güngör
 */
@SuppressWarnings("unused")
public class AvciGungor extends ArtificialIntelligence {
	
	private final int MUR_DESTRUCTIBLE = 3;
	private final int BOMBE = 0;
	private final int ADVERSAIRE = 5;
	private final int BONUS = 10;
	private final int BLAST = -1;
	private final int OBSTACLE = 2;
	private final int CASE_SUR = 1;

	// private AiPath nextMove = null;
	// chemin a suivre pour ramasser le bonus
	// private AiPath nextMoveBonus = null;
	// notre hero sur la zone
	private AiHero ourHero;
    public AiOutput aio;
	private boolean checkPath = true;
	private int matriceCollecte[][];
	AiZone gameZone = getPercepts();
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException { // avant tout:
																	// test
	
										// d'interruption
		checkInterruption();
		// La perception instantanement de l'environnement
		
		initialiseMatriceCollecte(matriceCollecte, gameZone);
		// Le resultat du traitement indiquant l'action suivante
		AiAction result = new AiAction(AiActionName.NONE);

		this.ourHero = gameZone.getOwnHero();
		aio.setTileText(3, 5, "asdd");
		aio.setTileColor(3, 5, Color.BLUE);
		return result;
	}

	private void initialiseMatriceCollecte(int[][] matriceCollecte,
			AiZone gameZone) throws StopRequestException {
		checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				matriceCollecte[i][j] = CASE_SUR;
				//System.out.println(matriceCollecte[i][j]);
			}
		}

	}

	private void emplirBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			if (item.getType().name() == "extrabomb") {
				matrice[item.getLine()][item.getCol()] = BONUS;

			} else if (item.getType().name() == "extraflame")
				matrice[item.getLine()][item.getCol()] = BONUS;

		}
	}

	private void emplirFire(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = OBSTACLE;
		}
	}

	private void emplirHero(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiHero> heros = gameZone.getRemainingHeroes();
		for (int i = 0; i < heros.size(); i++) {
			checkInterruption();
			if (ourHero.getLine() != heros.get(i).getLine()
					&& ourHero.getCol() != heros.get(i).getCol())
				matrice[heros.get(i).getLine()][heros.get(i).getCol()] = ADVERSAIRE;
		}

	}

	private void emplirBombe(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = BOMBE;
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i)
						.getCol()] = BLAST;
			}
		}
	}

	private void emplirBlock(int[][] matrice, AiZone gameZone)
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
				matrice[block.getLine()][block.getCol()] = OBSTACLE;
		}
	}

	private void updateOutput() throws StopRequestException
	{	ArtificialIntelligence ai;
	checkInterruption(); //APPEL OBLIGATOIRE
	
		AiOutput output = getOutput();
	
		
		// couleurs des cases
		for(int line=0;line<gameZone.getHeight();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<gameZone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				
				Color color = null;			
				if(matriceCollecte[line][col]==0)
					color = Color.YELLOW;
				else if(matriceCollecte[line][col]==CASE_SUR)
					color = Color.WHITE;
				else if(matriceCollecte[line][col]>0)
					color = Color.RED;
				else if(matriceCollecte[line][col]<0)
					color = Color.BLACK;
				output.setTileColor(line,col,color);
			}
		}
		
		// texte des cases
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(0);
		for(int line=0;line<gameZone.getHeight();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<gameZone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				
				String text = null;			
				if(matriceCollecte[line][col]==CASE_SUR)
					text = "\u221E";
				else 
					text = nf.format(matriceCollecte[line][col]); 
				output.setTileText(line,col,text);
			}
		}
	}
	
}
