package org.totalboumboum.ai.v200809.ais.akpolatsener.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Cem Akpolat
 * @author Emre Sener
 *
 */
public class Neighbors {

	/** colonne et ligne d'une bombe */
	int bombCol;
	int bombLine;
	
	/** la classe principale d'IA */
	AkpolatSener sa;

	/** zone du jeu */
	AiZone zone;
	
	/** cas actuel */
	AiTile currentTile;

	/** un XTile temporaire */
	XTile tempTile;

	/** liste des bombes autour les cases voisines */
	List<AiTile> bombTiles = new ArrayList<AiTile>();

	

	
	/** initialise les champs */
	public Neighbors(AkpolatSener sa, AiZone zone, AiTile tile)	throws StopRequestException 
	{
		sa.checkInterruption();
		
		this.sa = sa;
		this.currentTile = tile;
		this.zone = zone;
	}

	/**
	 * renvoie la liste des tout voisins
	 * @return liste des voisins sans condition
	 * @throws StopRequestException
	 */
	public List<AiTile> findAllNeighbors() throws StopRequestException 
	{
		sa.checkInterruption();

		Iterator<AiTile> iter = zone.getNeighborTiles(currentTile).iterator();

		List<AiTile> tiles = new ArrayList<AiTile>();
		
		while (iter.hasNext())
		{
			sa.checkInterruption();
			
			tiles.add(iter.next());
		}
		return tiles;
	}

	/**
	 * renvoie la liste des cas voisins qui ne contient aucune obstacle
	 * @return la liste des voisins propres
	 * @throws StopRequestException
	 */
	public List<AiTile> findCleanNeighbors() throws StopRequestException {
		sa.checkInterruption();

		//on prend toutes les voisins et on en elimine
		List<AiTile> tiles = findAllNeighbors();

		List<AiTile> cleanTiles = new ArrayList<AiTile>();

		Iterator<AiTile> iter = tiles.iterator();

		while (iter.hasNext()) 
		{
			sa.checkInterruption();

			AiTile tempTile = iter.next();

			if (tempTile.getBlock() == null && tempTile.getBombs().isEmpty() && tempTile.getFires().isEmpty()) 
			{
				cleanTiles.add(tempTile);
			}
		}

		return cleanTiles;
	}

	/**
	 * la liste des cases voisines propres qui ne sont pas dans la port�e d'une bombe
	 * @return la liste des cases voisines propres qui ne sont pas dans la port�e d'une bombe
	 * @throws StopRequestException
	 */
	public List<AiTile> findNeighborsNotInBombRange()	throws StopRequestException
	{
		sa.checkInterruption();

		List<AiTile> noBombTiles = new ArrayList<AiTile>();
		
		bombTiles.clear();
		
		//premierement on obtient les cases propres
		List<AiTile> tiles = findCleanNeighbors();
		Iterator<AiTile> iter = tiles.iterator();

		//on considere que la port�e d'une bombe est 20 cases
		int bombRange = 10;

		while (iter.hasNext()) {
			sa.checkInterruption();
			AiTile tempTile = iter.next();

			if (!isInBombRange(tempTile, bombRange))
				noBombTiles.add(tempTile);
		}

		return noBombTiles;

	}

	/**
	 * donne la distance directe entre deux cases
	 * 
	 * @param t1 
	 * @param t2
	 * @return distance entre ces 2 cases
	 * @throws StopRequestException
	 */
	public double distanceBetweenTwoTiles(AiTile t1, AiTile t2)	throws StopRequestException 
	{
		sa.checkInterruption();
		
		int X = t1.getCol() - t2.getCol();
		int Y = t1.getLine() - t2.getLine();
		double distance = Math.sqrt(X ^ 2 + Y ^ 2);

		return distance;
	}

	/**
	 * determine si une case est dans la port�e d'une bombe
	 * @param tile
	 * @param bombRange
	 * @return true si elle est, false sinon
	 * @throws StopRequestException
	 */
	public boolean isInBombRange(AiTile tile, int bombRange) throws StopRequestException 
	{
		sa.checkInterruption();

		bombCol = 0;
		bombLine = 0;

		boolean result = false;

		if (tile == currentTile)
			bombTiles.clear();

		//on prend les coordonnees de la case
		int colTile = tile.getCol();
		int lineTile = tile.getLine();

		/*
		 * int zoneWidth = zone.getWidth()-1; int zoneHeight =
		 * zone.getHeigh()-1;
		 */

		/////////////////////////////////////////////////////////////////////////
		//on teste la risque d'une bombe de la gauche vers la droite de la case//
		/////////////////////////////////////////////////////////////////////////
		for (int col = 4; col < 13; col++) 
		{
			sa.checkInterruption();

			AiTile tempTile = zone.getTile(lineTile, col);

			if (!tempTile.getBombs().isEmpty()	|| !tempTile.getFires().isEmpty())
			{
				result = true;
				
				bombTiles.add(tempTile);
				bombCol = tempTile.getCol();
				break;
			}
		}

		// s'il existe des murs entre la bombe et la case actuelle, ça n'apporte pas des risques
		for (int col = 4; col < 13; col++) 
		{
			sa.checkInterruption();

			AiTile tempTile = zone.getTile(lineTile, col);

			for (int i = 0; i < bombTiles.size(); i++) 
			{
				sa.checkInterruption();
				
				double distToBomb = distanceBetweenTwoTiles(currentTile,bombTiles.get(i));

				double distToWall = distanceBetweenTwoTiles(tempTile,currentTile);

				if (tempTile.getBlock() != null && (distToWall < distToBomb)) {

					result = false;

					bombTiles.remove(bombTiles.get(i));

					break;
					
				} 
				else result = true;

			}

		}
		
		//////////////////////////////////////////////////////////////////////
		//on teste la risque d'une bombe de la haute vers la base de la case//
		//////////////////////////////////////////////////////////////////////
		for (int line = 3; line < 12; line++) {
			sa.checkInterruption();

			AiTile tempTile = zone.getTile(line, colTile);

			if (!tempTile.getBombs().isEmpty()
					|| !tempTile.getFires().isEmpty()) {
				result = true;
				
				bombTiles.add(tempTile);
				bombLine = tempTile.getLine();
				break;
			}

		}

		// s'il existe des murs entre la bombe et la case actuel, ça n'apporte pas des risques
		for (int line = 3; line < 12; line++) 
		{
			sa.checkInterruption();

			AiTile tempTile = zone.getTile(line, colTile);

			for (int i = 0; i < bombTiles.size(); i++) 
			{
				
				sa.checkInterruption();
				
				double distToBomb = distanceBetweenTwoTiles(tempTile, bombTiles.get(i));

				double distToWall = distanceBetweenTwoTiles(tempTile,currentTile);

				if (tempTile.getBlock() != null && (distToWall < distToBomb)) {

					result = false;

					bombTiles.remove(bombTiles.get(i));

					break;
				} else
					result = true;

			}

		}

		return result;

	}

	/**
	 * le nombre des voisins de la case donn�e
	 * @param tile
	 * @return le nombre des voisins de la case donn�e
	 * @throws StopRequestException
	 */
	public int getNeighborsNumber(AiTile tile) throws StopRequestException {
		sa.checkInterruption();
		
		Collection<AiTile> tiles = this.findCleanNeighbors();
		
		return tiles.size();
	}

	
	
	/**
	 * la distance directe entre la case donn�e et la bombe la plus proche
	 * @param tile
	 * @return la distance directe entre la case donn�e et la bombe la plus proche
	 * @throws StopRequestException
	 */
	public double getHypotenuseToBomb(AiTile tile) throws StopRequestException {
		sa.checkInterruption();
		int x;
		int y;

		double hypo = 10;
		double distance = 0;

		
		
		for (int i = 0; i < bombTiles.size(); i++) 
		{
			sa.checkInterruption();
			
			x = tile.getCol() - bombTiles.get(i).getCol();
			y = tile.getLine() - bombTiles.get(i).getLine();
			
			distance = Math.sqrt( x*x  + y*y );

			if (hypo > distance)
				hypo = distance;
		}

		return hypo;

	}

}
