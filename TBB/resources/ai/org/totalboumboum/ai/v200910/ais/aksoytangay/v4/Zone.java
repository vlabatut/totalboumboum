package org.totalboumboum.ai.v200910.ais.aksoytangay.v4;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;

/**
 * cette class sert a definir ce qui existe dans chaque case.
 * 
 * @version 4
 * 
 * @author Cihan Aksoy
 * @author Necmi Murat Tangay
 *
 */
@SuppressWarnings("deprecation")
public class Zone {
	
	//variable pour faire de l'appel
	private AksoyTangay myAI;
	//variable de la zone de jeu
	private AiZone zone;
	//variable de notre héro
	private AiHero ownHero;
	
	//variables qu'on va utiliser pour tenir l'information de la zone
	private Collection<AiHero> ennemies;
	private Collection<AiBomb> bombs;
	private Collection<AiTile> tiles;
	private Collection<AiFire> fires;
	private Collection<AiBlock> blocks;
	private Collection<AiItem> items;
	
	//variables des taille de la zone
	public int width;
	public int height;
	
	//variable qu'on va remplir avec les collections
	private State matrix[][];
	
	/**
	 * La methode constructeur.
	 * 
	 * @param zone: Aizone
	 * 
	 * @throws StopRequestException
	 */
	public Zone(AiZone zone, AksoyTangay myAI) throws StopRequestException
	{
		// avant tout : test d'interruption
		myAI.checkInterruption();
		//on prend les infos de la zone du jeu
		this.setZone(zone);
		this.myAI = myAI;
		ownHero = zone.getOwnHero();
		ennemies = zone.getHeroes();
		bombs = zone.getBombs();
		fires = zone.getFires();
		blocks = zone.getBlocks();
		items = zone.getItems();
		
		width = zone.getWidth();
		height = zone.getHeight();
		
		//on rempli la matrice d'état par rapport aux ces infos
		fillMatrix();
		
	}
	
	
	/**
	 * La methode qui rempli la matrice d'état avec les valeurs qui vient de la
	 * zone.
	 * 
	 * @throws StopRequestException
	 */
	private void fillMatrix() throws StopRequestException
	{
		// avant tout : test d'interruption
		myAI.checkInterruption();
		
		//posons variable SURE pour chaque case
		matrix = new State[height][width];
		for (int i = 0; i < height; i++) {
			myAI.checkInterruption();
			for (int j = 0; j < width; j++) {
				myAI.checkInterruption();
				matrix[i][j] = State.SURE;				
			}
		}
		
		//maintenant commençons a remplir la matrice avec les autres valeurs
		
		// ENNEMIES 					/////////////////////////////////////
		Iterator<AiHero> itrHero = ennemies.iterator();
		AiHero tmpHero;
		while(itrHero.hasNext())
		{
			myAI.checkInterruption();
			tmpHero = itrHero.next();
			
			if(tmpHero != ownHero)
				matrix[tmpHero.getLine()][tmpHero.getCol()] = State.ADVERSAIRE;
			
			//System.out.println(tmpHero.getBombNumber());
			//System.out.println("heigh : "+zone.getHeigh()+"width : "+zone.getWidth());
			

		}
		
		// BOMBS    					/////////////////////////////////////
		Iterator<AiBomb> itrBomb = bombs.iterator();
		AiBomb tmpBomb;
		while(itrBomb.hasNext())
		{
			myAI.checkInterruption();
			tmpBomb = itrBomb.next();
			
			matrix[tmpBomb.getLine()][tmpBomb.getCol()] = State.BOMBE;
			
			// DANGER    					/////////////////////////////////////
			tiles = tmpBomb.getBlast();
			Iterator<AiTile> itrTile = tiles.iterator();
			AiTile tmpTile;
			while(itrTile.hasNext())
			{
				myAI.checkInterruption();
				tmpTile = itrTile.next();
				
				if(matrix[tmpTile.getLine()][tmpTile.getCol()] == State.DANGER)
					matrix[tmpTile.getLine()][tmpTile.getCol()] = State.SEVERALDANGERS;
				else
					matrix[tmpTile.getLine()][tmpTile.getCol()] = State.DANGER;
			}
			
		}
					
		// FIRES    					/////////////////////////////////////
		Iterator<AiFire> itrFire = fires.iterator();
		AiFire tmpFire;
		while(itrFire.hasNext())
		{
			myAI.checkInterruption();
			tmpFire = itrFire.next();
			
			matrix[tmpFire.getLine()][tmpFire.getCol()] = State.FLAMME;
		}
		
		// BLOCKS    					/////////////////////////////////////
		Iterator<AiBlock> itrBlock = blocks.iterator();
		AiBlock tmpBlock;
		while(itrBlock.hasNext())
		{
			myAI.checkInterruption();
			tmpBlock = itrBlock.next();
			
			if(tmpBlock.isDestructible())
				matrix[tmpBlock.getLine()][tmpBlock.getCol()] = State.DESTRUCTIBLE;
			else
				matrix[tmpBlock.getLine()][tmpBlock.getCol()] = State.INDESTRUCTIBLE;
		}
		
		// ITEMS     					/////////////////////////////////////
		Iterator<AiItem> itrItem = items.iterator();
		AiItem tmpItem;
		while(itrItem.hasNext())
		{
			myAI.checkInterruption();
			tmpItem = itrItem.next();
			
			if(tmpItem.getType() == AiItemType.EXTRA_BOMB)
				matrix[tmpItem.getLine()][tmpItem.getCol()] = State.EXTRA_BOMBE;
			else if(tmpItem.getType() == AiItemType.EXTRA_FLAME)
				matrix[tmpItem.getLine()][tmpItem.getCol()] = State.EXTRA_FLAMME;
			else if(tmpItem.getType() == AiItemType.MALUS)
				matrix[tmpItem.getLine()][tmpItem.getCol()] = State.MALUS;
		}
		//System.out.println("fillmatrix.zone");
	}
	
	/**
	 * La methode qui renvoie la matrice d'état.
	 * 
	 * @throws StopRequestException
	 */
	public State[][] getMatrix() throws StopRequestException
	{
		//System.out.println("getmatrix.zone");
		return matrix;
	}
	
	public State[][] updateMatrix() throws StopRequestException
	{
		myAI.checkInterruption();
		
		//System.out.println("updMat.zone");
		fillMatrix();
		
		return matrix;
	}
	
	public void setZone(AiZone zone) {
		this.zone = zone;
	}

	public AiZone getZone() {
		return zone;
	}
	
	public Collection<AiBomb> getBombs() {
		return bombs;
	}
}
