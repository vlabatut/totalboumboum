package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v6;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class Path {
	HacibektasogluIlgar hi;
	AiZone zone;
	AiHero notreHeros;
	AiHero adversaires[];
	int height;
	int width;
	
	public Path(HacibektasogluIlgar hi,AiHero ourHero,AiHero adversaires[]) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;
		zone=hi.zone;
		height=zone.getHeight();
		width=zone.getWidth();
		this.notreHeros=ourHero;
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
		hi.checkInterruption();
		
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
		hi.checkInterruption();
		boolean resultat=false;	

		for(int i=0;i<path.getLength();i++)
		{
			hi.checkInterruption();
			if(path.getTile(i).equals(tile)) resultat=true;
			
		}

		return resultat;
	}
	
	/**
	 * cette methode sert à controller les tiles voisines pourque ne pas mettre une bombe 
	 * si tous les tiles voisines sont <0
	 * 
	 * @param matriceTotal
	 * @param tilesPossibles 
	 * @return
	 * @throws StopRequestException
	 */
	public boolean voisineControl(double matriceTotal[][],AiPath tilesPossibles) throws StopRequestException
	{
		hi.checkInterruption();
		boolean resultat=true;
		int a=0;
		List<AiTile> voisines=notreHeros.getTile().getNeighbors();
		
		for(int i=0;i<voisines.size();i++)
		{
			hi.checkInterruption();
			if(matriceTotal[voisines.get(i).getLine()][voisines.get(i).getCol()]<0)
				a++;
			else
				tilesPossibles.addTile(voisines.get(i));

		}
		
		if(a==4)
		{
			resultat=false;
		}
		
		return resultat;
		
	}
	
/**
 * cette methode est utilisé pendant la decision de poser une bombe ou pas
 * il sert à voir si on mets une bombe est-ce qu'il reste aucune tile pour fuir
 * 
 * @param line
 * @param col
 * @param matriceEtat
 * @param matrice
 * @param longueur
 * @param path
 * @throws StopRequestException
 */
	public void plusProcheAccesible(int line, int col,Etats[][] matriceEtat,double[][] matrice, int longueur,AiPath path) throws StopRequestException
	{
		hi.checkInterruption();
		
		if(matrice[line][col]>=0 && matriceEtat[line][col]!=Etats.BONUS )
		{
			AiTile tile=zone.getTile(line, col);
			if(!tileEstDejaAppartientAuPath(tile, path) && !tile.equals(notreHeros.getTile()))
			{
				path.addTile(tile);
			}
			
		}
		else if(longueur==0 || matrice[line][col]<=-100 || !zone.getTile(line, col).isCrossableBy(notreHeros))
		{}
		else 
		{
			plusProcheAccesible(mod(line-1,height), col, matriceEtat, matrice, longueur-1, path);
			plusProcheAccesible(mod(line+1,height), col, matriceEtat, matrice, longueur-1, path);
			plusProcheAccesible(line, mod(col-1,width), matriceEtat, matrice, longueur-1, path);
			plusProcheAccesible(line, mod(col+1,width), matriceEtat, matrice, longueur-1, path);
		}
	}
	
	/**
	 * on a essayé d'appliquer l'algorithme de largeur d'abord
	 * au debut le method prend une path qui a seulement le tile du hero et apres il regarde ses voisines
	 * et si le vosine est inpassable ou bien si il est deja utilisé on ne l'ajoute pas au path
	 * sinon on l'ajoute en regardent sa valeur de matriceTotale pour pouvoire decider si ça peut etre une cible ou pas.
	 * 
	 * @param matriceMarqueur le matrice qui est utilisé pour comprendre si ce tile est deja utilise ou pas
	 * il a comme valeurs : 
	 * 0 : n'est pas utilise
	 * 1 : utilisé et peut etre une cible
	 * -1 : utilisé mais ne peut pas etre une cible
	 *
	 * @param matriceTotal le matrice qui est deja calcule d'apres la zone 
	 * @param pilePath le path qu'on va l'utiliser comme une pile 
	 * @throws StopRequestException
	 */
	public void cible(double matriceMarqueur[][],double matriceTotal[][],AiPath pilePath) throws StopRequestException
	{
		hi.checkInterruption();
		
		List<AiTile> voisines=pilePath.getFirstTile().getNeighbors();

		for(int i=0;i<voisines.size();i++)
		{
			hi.checkInterruption();
			if(matriceMarqueur[voisines.get(i).getLine()][voisines.get(i).getCol()]==0
					&& voisines.get(i).isCrossableBy(notreHeros))
			{
				pilePath.addTile(voisines.get(i));
				
				if(matriceTotal[voisines.get(i).getLine()][voisines.get(i).getCol()]<0)
				{
					// le cas qu'on ne prefere pas comme cible
					matriceMarqueur[voisines.get(i).getLine()][voisines.get(i).getCol()]=-1;
				}
				else
					// le cas qu'on peut voir comme cible
					matriceMarqueur[voisines.get(i).getLine()][voisines.get(i).getCol()]=1;
				
			}
		}
		
		// on supprime le tile qu'on a utilisé
		pilePath.removeTile(0);
		
		if(!pilePath.isEmpty())
		{
			cible(matriceMarqueur, matriceTotal, pilePath);
		}
	}

	/**
	 * d'apres le cible on trouve une chemin en utilisent astar
	 * @param line
	 * @param col
	 * @param matrice
	 * @param tileCible
	 * @return
	 * @throws StopRequestException
	 */
	public AiPath trouverChemin(int line,int col,double[][] matrice, AiTile tileCible ) throws StopRequestException
	{
		hi.checkInterruption();
		final double matriceFinal[][]=matrice;
		
		CostCalculator cost=new CostCalculator() {
			
			@Override
			public double processCost(AiTile start, AiTile end)
					throws StopRequestException {
				hi.checkInterruption();
				
				double resultat=0;
				
				if(matriceFinal[end.getLine()][end.getCol()]==0)
				{
					resultat=1;
				}
				if(matriceFinal[end.getLine()][end.getCol()]>0)
				{
					resultat=-matriceFinal[end.getLine()][end.getCol()];
				}				
				else if(matriceFinal[end.getLine()][end.getCol()]==-50)
				{
					resultat=10000;
				}
				else if(matriceFinal[end.getLine()][end.getCol()]==-100)
				{
					resultat=20000;
				}
				else if(matriceFinal[end.getLine()][end.getCol()]==-150)
				{
					resultat=50000;
				}
				return resultat;
			}
		};
//		CostCalculator cost=new PixelCostCalculator();
		HeuristicCalculator heuristique=new PixelHeuristicCalculator();
		Astar astar=new Astar(hi, notreHeros, cost, heuristique);
		
		AiPath pathAstar=new AiPath();
		if(tileCible!=null)
			try {
				pathAstar=astar.processShortestPath(zone.getTile(line, col), tileCible);
			} catch (LimitReachedException e) {
				pathAstar.addTile(notreHeros.getTile());
			}
		return pathAstar; 
	}
	
	
	/**
	 * on calcule tous les tiles accesibles et apres on prend celui qui a meilleur note
	 * apres on donne la meilleur tile à Astar pourqu'il trouvé la meilleur route pour aller au cible
	 * à la fin on y bouge
	 * 
	 * @param line
	 * @param col
	 * @param matrice
	 * @return
	 * @throws StopRequestException 
	 * @throws LimitReachedException 
	 */
	public AiPath trouverCible(int line, int col,double[][] matrice) throws StopRequestException
	{
		hi.checkInterruption();
		
		double matriceMarqueur[][]= new double[height][width];
		
		AiPath path=new AiPath();
		path.addTile(notreHeros.getTile());

		// initialisation du matriceExistance
		for(int i=0;i<height;i++)
		{
			hi.checkInterruption();
			for(int j=0;j< width;j++)
			{
				hi.checkInterruption();	
				matriceMarqueur[i][j]=0;
			}
		}
		if(matrice[notreHeros.getLine()][notreHeros.getCol()]<0)
			matriceMarqueur[notreHeros.getLine()][notreHeros.getCol()]=-1;
		else matriceMarqueur[notreHeros.getLine()][notreHeros.getCol()]=1;

		// on trouve tous les tiles accesible de notre positision
		cible(matriceMarqueur, matrice, path);

		
		AiTile tileCible=notreHeros.getTile();
		
		double valeurMax=-50;
		
		for(int i=0;i<height;i++)
		{
			hi.checkInterruption();
			for(int j=0;j< width;j++)
			{
				hi.checkInterruption();	
				if(matriceMarqueur[i][j]==1)
				{
					if(matrice[i][j]>=valeurMax)
					{
						valeurMax=matrice[i][j];
						tileCible=zone.getTile(i, j);
					}
				}
			}
		}
			if(matrice[tileCible.getLine()][tileCible.getCol()]==matrice[notreHeros.getLine()][notreHeros.getCol()])
			tileCible=notreHeros.getTile();
		
		// on control les 4 voisines tiles pour que l'ia ne bouge pas s'il y a des bombes autour de lui
		
//		Maps map=new Maps(hi);
		
			AiPath tilesSecure=new AiPath();
		if(!voisineControl(matrice,tilesSecure) && matrice[notreHeros.getLine()][notreHeros.getCol()]>=0)
		{
			tileCible=notreHeros.getTile();
		}
		
		AiPath pathAstar=trouverChemin(line, col, matrice, tileCible);
		
		AiOutput output = hi.getOutput();
		Color color = Color.BLACK;
		output.addPath(pathAstar, color);

		
//		if(!voisineControl(matrice) && matrice[ourHero.getLine()][ourHero.getCol()]>=0)
//			pathAstar=new AiPath();
		
		return pathAstar;
	}
	
	
	/**
	 * Methode calculant la nouvelle action a effectuer
	 * (methode de groupe rouge de l'annee dernier mais on l'a modifié)
	 * @param nextMove
	 * 			Le chemin precis a suivre.
	 *  
	 * @return la nouvelle action de notre hero dans ce chemin
	 * 
	 * @throws StopRequestException
	 */
	public AiAction nouvelleAction(AiPath nextMove,AiHero uneHero,double[][] matriceTotal) throws StopRequestException {
		hi.checkInterruption();
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
		
		
		/*
		 *  si le case suivant est un case dangeraux 
		 *  alors notre hero va attendre jusqu'il voit le danger se passer
		 */
		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]>=0 
				&&  matriceTotal[tiles.get(1).getLine()][tiles.get(1).getCol()]<0)
		{
			check=false;
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

