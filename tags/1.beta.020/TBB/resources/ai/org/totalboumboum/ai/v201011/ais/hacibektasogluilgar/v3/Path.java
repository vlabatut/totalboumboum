package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v3;

/**
 * @author Elif Nurdan �LGAR 
 * @author Engin Hac�bekta�o�lu
 *
 */

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
	 * cette methode prends le mod d'une integer 
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
	 * on utilise cette method pour faire distinction de tiles qui sont deja calcul�
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
	 * cette methode sert � controller les tiles voisines pourque ne pas mettre une bombe 
	 * si tous les tiles voisines sont <0
	 * 
	 * @param matriceTotal
	 * @param chemin
	 * @return
	 * @throws StopRequestException
	 */
	public boolean voisineControl(double matriceTotal[][],AiPath chemin) throws StopRequestException
	{
		notreIA.checkInterruption();
		boolean resultat=true;
		int a=0;
		List<AiTile> voisines=ourHero.getTile().getNeighbors();
		for(int i=0;i<voisines.size();i++)
		{
			if(matriceTotal[voisines.get(i).getLine()][voisines.get(i).getLine()]<=0)
				a++;
		}
		if(a==4)
		{
			resultat=false;
		}
		
		return resultat;
		
	}
	
/**
 * cette methode est utilis� pendant la decision de poser une bombe ou pas
 * il sert � voir si on mets une bombe est-ce qu'il reste aucune tile pour fuir
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
			if(!tileEstDejaAppartientAuPath(tile, path) && !tile.equals(ourHero.getTile()))
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
	
	/**
	 * on a essay� d'appliquer l'algorithme de largeur d'abord
	 * au debut le method prend une path qui a seulement le tile du hero et apres il regarde ses voisines
	 * et si le vosine est inpassable ou bien si il est deja utilis� on ne l'ajoute pas au path
	 * sinon on l'ajoute en regardent sa valeur de matriceTotale pour pouvoire decider si �a peut etre une cible ou pas.
	 * 
	 * @param matriceExistance le matrice qui est utilis� pour comprendre si ce tile est deja utilise ou pas
	 * il a comme valeurs : 
	 * 0 : n'est pas utilise
	 * 1 : utilis� et peut etre une cible
	 * -1 : utilis� mais ne peut pas etre une cible
	 *
	 * @param matriceTotal le matrice qui est deja calcule d'apres la zone 
	 * @param pilePath le path qu'on va l'utiliser comme une pile 
	 * @throws StopRequestException
	 */
	public void cible(double matriceExistance[][],double matriceTotal[][],AiPath pilePath) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		List<AiTile> voisines=pilePath.getFirstTile().getNeighbors();

		for(int i=0;i<voisines.size();i++)
		{
			if(matriceExistance[voisines.get(i).getLine()][voisines.get(i).getCol()]==0
					&& voisines.get(i).isCrossableBy(ourHero))
			{
				pilePath.addTile(voisines.get(i));
				
				if(matriceTotal[voisines.get(i).getLine()][voisines.get(i).getCol()]<-50)
				{
					// le cas qu'on ne prefere pas comme cible
					matriceExistance[voisines.get(i).getLine()][voisines.get(i).getCol()]=-1;
				}
				else
					// le cas qu'on peut voir comme cible
					matriceExistance[voisines.get(i).getLine()][voisines.get(i).getCol()]=1;
				
			}
		}
		
		// on supprime le tile qu'on a utilis�
		pilePath.removeTile(0);
		
		if(!pilePath.isEmpty())
		{
			cible(matriceExistance, matriceTotal, pilePath);
		}
	}
	
	/**
	 * on calcule tous les tiles accesibles et apres on prend celui qui a meilleur note
	 * apres on donne la meilleur tile � Astar pourqu'il trouv� la meilleur route pour aller au cible
	 * � la fin on y bouge
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

		double matriceExistance[][]= new double[height][width];
		
		AiPath path=new AiPath();
		path.addTile(ourHero.getTile());

		// initialisation du matriceExistance
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();	
				matriceExistance[i][j]=0;
			}
		}
		if(matrice[ourHero.getLine()][ourHero.getCol()]<-50)
			matriceExistance[ourHero.getLine()][ourHero.getCol()]=-1;
		else matriceExistance[ourHero.getLine()][ourHero.getCol()]=1;

		// on trouve tous les tiles accesible de notre positision
		
		cible(matriceExistance, matrice, path);
		
		AiTile tileCible=ourHero.getTile();
		
		double valeurMax=-50;
		
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();	
				if(matriceExistance[i][j]==1)
				{
					if(matrice[i][j]>valeurMax)
					{
						valeurMax=matrice[i][j];
						tileCible=zone.getTile(i, j);
					}
				}
			}
		}
		if(matrice[tileCible.getLine()][tileCible.getCol()]==matrice[ourHero.getLine()][ourHero.getCol()])
			tileCible=ourHero.getTile();
		

		final double matriceFinal[][]=matrice;
		
		CostCalculator cost=new CostCalculator() {
			
			@Override
			public double processCost(AiTile start, AiTile end)
					throws StopRequestException {
				// 
				double resultat=0;
				if(matriceFinal[end.getLine()][end.getCol()]==-50)
				{
					resultat=5;
				}
				else if(matriceFinal[end.getLine()][end.getCol()]==-100)
				{
					resultat=100;
				}
				return resultat;
			}
		};
		//CostCalculator cost=new BasicCostCalculator();
		HeuristicCalculator heuristique=new BasicHeuristicCalculator();
		Astar astar=new Astar(notreIA, ourHero, cost, heuristique);
		
		AiPath pathAstar=new AiPath();
		if(tileCible!=null)
			pathAstar=astar.processShortestPath(zone.getTile(line, col), tileCible);
		
		return pathAstar;
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
		if(notreCible==null) System.out.println("cible est null");
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
