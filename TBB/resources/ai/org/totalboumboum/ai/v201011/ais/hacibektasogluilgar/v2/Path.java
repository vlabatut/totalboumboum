package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v2;


import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class Path {
	HacibektasogluIlgar notreIA;
	AiZone zone;
	AiHero ourHero;
	AiHero adversaires[];
	int height;
	int width;
	Maps maps;
	
	public Path(HacibektasogluIlgar hi,AiHero ourHero,AiHero adversaires[]) throws StopRequestException
	{
		
		this.notreIA=hi;
		notreIA.checkInterruption();
		zone=notreIA.zone;
		height=zone.getHeight();
		width=zone.getWidth();
		
		this.ourHero=ourHero;
		this.adversaires=adversaires;

	}
	
	/**
	 * cette function prends mod d'une interger 
	 * @param nombre
	 * @param mode
	 * @return
	 * @throws StopRequestException
	 */
	private int mod(int nombre,int mode) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		if(nombre<0) return nombre+mode;
		else if(nombre>=mode) return nombre-mode;
		else return nombre;
	}
	
	/**
	 * on utilise cette method pour faire distinction de tiles qui sont deja calculé
	 * @param tile
	 * @param path
	 * @return
	 * @throws StopRequestException
	 */
	private boolean tileEstDejaAppartientAuPath(AiTile tile, AiPath path) throws StopRequestException
	{
		notreIA.checkInterruption();
		boolean resultat=false;	

		for(int i=0;i<path.getLength();i++)
		{
			notreIA.checkInterruption();
			if(path.getTile(i).equals(tile)) resultat=true;
			
		}

		return resultat;
	}
/**
 * hayali bomba konuldu�u zaman t�m etki alan� -10 olacak
 * -100 olmayan ve -10 olan yerleri taray�p e�er pozitif bir de�er bulursak 
 * oras� bizim i�in ula��labilir bir yer olacak
 *   
 * @param line
 * @param col
 * @param matrice
 * @return
 * @throws StopRequestException
 */
	public void plusProcheAccesible(int line, int col,double[][] matrice, int longueur,AiPath path) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		if(matrice[line][col]>=0)
		{
			AiTile tile=zone.getTile(line, col);
			if(!tileEstDejaAppartientAuPath(tile, path))
			{
				path.addTile(tile);
			}
			
		}
		else if(longueur==0 || matrice[line][col]==-100)
		{}
		else 
		{
			plusProcheAccesible(mod(line-1,height), col, matrice, longueur-1, path);
			plusProcheAccesible(mod(line+1,height), col, matrice, longueur-1, path);
			plusProcheAccesible(line, mod(col-1,width), matrice, longueur-1, path);
			plusProcheAccesible(line, mod(col+1,width), matrice, longueur-1, path);
		}
	}

	
	
	public void plusProcheAccesibleV2(int line, int col,double[][] matrice, int longueur,AiPath path) throws StopRequestException
	{
		notreIA.checkInterruption();
		if(longueur==0 || (matrice[line][col]==-100 && !zone.getTile(line, col).isCrossableBy(ourHero)) )
		{
			
		}
		else if(matrice[line][col]>=0)
		{
			AiTile tile=zone.getTile(line, col);
			if(!tileEstDejaAppartientAuPath(tile, path))
			{
				path.addTile(tile);
			}
			plusProcheAccesibleV2(mod(line-1,height), col, matrice, longueur-1, path);
			plusProcheAccesibleV2(mod(line+1,height), col, matrice, longueur-1, path);
			plusProcheAccesibleV2(line, mod(col-1,width), matrice, longueur-1, path);
			plusProcheAccesibleV2(line, mod(col+1,width), matrice, longueur-1, path);
	
		}
	}
	
	/**
	 * �nce t�m alan� longeur 10 ile tar�ycak 
	 * sonra buldu�u tile lerin en b�y���n� hedef tile se�icek 
	 * sonra Astar algo yu uyguluycak 
	 * en sonunda a�a��daki action a buldu�u path� vericek ve bitcek 
	 * 
	 * @param line
	 * @param col
	 * @param matrice
	 * @return
	 * @throws StopRequestException 
	 * @throws LimitReachedException 
	 */
	public AiPath trouverCible(int line, int col,double[][] matrice) throws StopRequestException, LimitReachedException
	{
		notreIA.checkInterruption();
		
		AiPath path=new AiPath();
//      on trouve des tiles accesible de la position du notre hero		
		plusProcheAccesibleV2(line, col, matrice,10, path);
		
		
//		on trouve le tile qui a une valeur maximale;
		double maxValeur=-1;
		AiTile tileCible=zone.getTile(line, col);
		
		for(int i=0;i<path.getLength();i++)
		{
			int tileLine=path.getTile(i).getLine();
			int tileColonne=path.getTile(i).getCol();
			
			if(matrice[tileLine][tileColonne]>maxValeur)
			{
				
				maxValeur=matrice[tileLine][tileColonne];
				tileCible=path.getTile(i);
			}
		}
		
		CostCalculator cost=new BasicCostCalculator();
		HeuristicCalculator heuristique=new BasicHeuristicCalculator();
		Astar astar=new Astar(notreIA, ourHero, cost, heuristique);
		
		path=astar.processShortestPath(zone.getTile(line, col), tileCible);
		
		return path;
	}
	
	
	/**
	 * Methode calculant la nouvelle action a effectuer
	 * (methode de groupe rouge de l'annee dernier mais on l'a modifi�)
	 * @param nextMove
	 * 			Le chemin precis a suivre.
	 *  
	 * @return la nouvelle action de notre hero dans ce chemin
	 * 
	 * @throws StopRequestException
	 */
	public AiAction newAction(AiPath nextMove,AiHero uneHero) throws StopRequestException {
		notreIA.checkInterruption();
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		AiTile notreCible=nextMove.getLastTile();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;		
		AiAction result = new AiAction(AiActionName.NONE);
		boolean check = true;

		//Si on a arrive notre cible
		if(notreCible.equals(uneHero.getTile())){
			
			check=false;
		}
		
		if(tiles.size()>1){
			dx = (tiles.get(1).getLine()) - (uneHero.getLine());
			// calcul de deplacement sur l'ordonne par rapport a la position de
			// l'hero et la premiere
			// case du chemin le plus court.
			dy = (tiles.get(1).getCol()) - (uneHero.getCol());
			check = true;
		
		if (tiles.get(0).getBlocks().size() != 0) {

			notreCible= null;
			check = false;
		}
		
		// Determine la direction ou le hero va se deplacer.
		if (check) {
			if (dx < 0 && dy == 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UP);
			} else if (dx < 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UPLEFT);
			} else if (dx == 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.LEFT);
			} else if (dx > 0 && dy == 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWN);
			} else if (dx > 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
			} else if (dx == 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
			} else if (dx > 0 && dy < 0) {
				result = new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
			} else if (dx < 0 && dy > 0) {
				result = new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
			} else {
				result = new AiAction(AiActionName.NONE);
			}
		}
		}
		
		return result;
	}
	
}
