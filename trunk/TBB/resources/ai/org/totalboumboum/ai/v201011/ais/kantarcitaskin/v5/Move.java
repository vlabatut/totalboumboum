package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class Move 
{
	/**
	 * Classe qui calcule la direction de movement en trouvant le chemin le plus court
	 * */
	AiZone zone;
	int mode;
	ArtificialIntelligence ai;
	double[][] matrix;
		
	/**
	 * Constructeur
	 * 
	 * @param zone
	 * 		la zone du jeu
	 * @param matrix
	 * 		la zone numerique du jeu
	 * @param ai
	 * 		AI
	 * @throws StopRequestException 
	 * 
	 * */
	public Move(AiZone zone, int mode, ArtificialIntelligence ai) throws StopRequestException
	{
		ai.	checkInterruption();
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	}
	
	/**
	 *trouve la direction de l'action se deplacer. Pour cela elle utilise l'algo A* 
	 *et puis la fonction de cout Cost et la matrice numerique qui represent la zone du jeu
	 *S'il n'y a pas de direction elle renvoi Direction.NONE, si n'y a pas de path 
	 *elle renvoi Direction.NONE aussi.
	 *
	 *
	 * @return resultat
	 * 		la direction de l'action se deplacer
	 * */
	public Direction getDirection() throws StopRequestException
	{	
		ai.	checkInterruption();
		matrix = new double[zone.getHeight()][zone.getWidth()];
		MatrixCalculator collect = new MatrixCalculator(zone,mode,ai);
		matrix = collect.matrixConstruction();
		
		Cost  cost = new Cost(matrix, this.ai);
		//BasicCostCalculator cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(ai, zone.getOwnHero(), cost, heuristic);
		AiPath path = null;
		Direction resultat= Direction.NONE;
		AiTile target = targetTile();
		for(int i=0; i<zone.getHeight(); i++)
		{
			ai.	checkInterruption();
			for(int j=0; j<zone.getWidth(); j++)
			{
				ai.checkInterruption();
				org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = ai.getOutput();
				output.setTileText(i, j, ""+matrix[i][j]);	
			
			}
		}	
		List<AiTile> safe = new ArrayList<AiTile>();
		List<AiTile> areaSafe = accesibleArea(zone.getOwnHero().getTile(),safe);
		//areaSafe.remove(zone.getOwnHero().getTile());
		Iterator<AiTile> area = areaSafe.iterator();
		AiTile tempo = null, target1 = target, target2 = target;
		double n=0;
		while(area.hasNext())
		{
			ai.	checkInterruption();
			tempo = area.next();
			if(matrix[tempo.getLine()][tempo.getCol()] > n)
			{
				n = matrix[tempo.getLine()][tempo.getCol()];
				target2= target1;
				target1 = zone.getTile(tempo.getLine(), tempo.getCol());

			}
			else
			{
				if(matrix[tempo.getLine()][tempo.getCol()] == n)
				{
						if(zone.getPixelDistance(tempo.getPosX(),tempo.getPosY(), target.getPosX(),target.getPosY())< zone.getPixelDistance(target1.getPosX(),target1.getPosY(), target.getPosX(),target.getPosY()))
				
						//if(zone.getPixelDistance(tempo.getPosX(),tempo.getPosY(), target.getPosX(),target.getPosY())< zone.getPixelDistance(target1.getPosX(),target1.getPosY(), target.getPosX(),target.getPosY()))
						//if(zone.getTileDistance(tempo, target)<zone.getTileDistance(target1, target))
						{
							n = matrix[tempo.getLine()][tempo.getCol()];
							target2 = target1;
							target1 = zone.getTile(tempo.getLine(), tempo.getCol());
						}
						
					
				}
			}
			org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = ai.getOutput();
			output.setTileText(tempo.getLine(),tempo.getCol(), ""+matrix[tempo.getLine()][tempo.getCol()]);	
				
		}

		
		if(target == target1)
		{
			try 
			{
				path = astar.processShortestPath(zone.getOwnHero().getTile(), target);
			} catch (Exception e) {
				// 
			}
		}
		else
		{
			
			try 
			{
				if(target1!= zone.getOwnHero().getTile())
				{
					path = astar.processShortestPath(zone.getOwnHero().getTile(), target1);
				}
				else
					path = astar.processShortestPath(zone.getOwnHero().getTile(), target2);
				
			} catch (Exception e2)
			{
			}
			
		}
		if(path.getTiles().size()!=0)
		{
			path.removeTile(0);
			if(path.getTiles().size()==0)
			{
					resultat = Direction.NONE;
			}
			else
			{
					resultat = zone.getDirection(zone.getOwnHero().getTile(), path.getFirstTile());
					
			}
		}
		else
			resultat = Direction.NONE;
		if(resultat != Direction.NONE)
		{		
			if(matrix[zone.getOwnHero().getLine()][zone.getOwnHero().getCol()]>=0 && matrix[zone.getOwnHero().getTile().getNeighbor(resultat).getLine()][zone.getOwnHero().getTile().getNeighbor(resultat).getCol()] < -150 )
				resultat = Direction.NONE;
		}
			
			
		return resultat;
		
		
	}

	/***
	 * Cherche la case dont la valeur est plus eleve.S'elle sont tout les memes alors 
	 * il renvoi la case de l'hero correspondant
	 * 
	 * @return resultat
	 * 		la case dont la valeur est plus eleve ou bien la case de l'hero
	 * 
	 * */
	
	public AiTile targetTile() throws StopRequestException
	{
		ai.	checkInterruption();
		int max=0, maxi=0, maxj=0;
		AiTile resultat;
		for (int i=0;i<zone.getHeight();i++)
		{
			ai.	checkInterruption();
			for(int j=0;j<zone.getWidth();j++)
			{
				ai.	checkInterruption();
				if(matrix[i][j]>max)
				{
					maxi=i;
					maxj=j;
					max = (int) matrix[i][j];
				}
				else
				{
					if(matrix[i][j]==max)
					{
						if(zone.getPixelDistance(zone.getTile(i, j).getPosX(), zone.getTile(i, j).getPosY(), zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY())<zone.getPixelDistance(zone.getTile(maxi, maxj).getPosX(), zone.getTile(maxi, maxj).getPosY(), zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY()))
						{
							maxi=i;
							maxj=j;
							max = (int) matrix[i][j];
						}
						
					}
				}
			
			}
			if(maxi==0 && maxj==0)
			{
				resultat = zone.getOwnHero().getTile();
			}
		}
		
		resultat= zone.getTile(maxi, maxj);
		return resultat;
	}
	
	/**
	 * Teste si la case qu'on a envoye se trouve dans le danger d'une bombe dans la zone.
	 * 
	 * @param tile
	 * 		la case qu'on veut tester
	 * 
	 * @return result
	 * 		vrai s'elle est en danger, faux sinon
	 * @throws StopRequestException 
	 * 
	 * */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{
		ai.	checkInterruption();
		boolean result = true;
		List<AiBomb> bombs = this.zone.getBombs();
		Iterator<AiBomb> bombList = bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			ai.	checkInterruption();
			bomb = bombList.next();
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				ai.	checkInterruption();
				blast=blasts.next();
				if(blast.getLine() == tile.getLine() && blast.getCol() == tile.getCol())
					result = false;		
			}	
		}	
		return result;
	}
	
	/*public List<AiTile> safeArea(AiHero hero, int range, List<AiTile> blast) throws StopRequestException 
	{
		ai.	checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> area = new ArrayList<AiTile>();
		int npixel = (int) (zone.getOwnHero().getWalkingSpeed()*zone.getOwnHero().getBombRange());
		int n = (int) (zone.getPixelWidth()/zone.getWidth());
		int ncase = npixel / (n) ;
		System.out.println(ncase);
		area = calculeArea(ncase);
		List<AiTile> safe = new ArrayList<AiTile>();
		result = accesibleArea(zone.getOwnHero().getTile(),area,safe);
		return result;
	}*/
	
	
	/**
	 * Calcule la liste des cases de la portÃ©e de bombe d'un hero
	 * 
	 * @param range
	 * 		la portee de la bombe d'un hero
	 * 
	 * @return tempo
	 * 		la liste des cases de la portee
	 * @throws StopRequestException 
	 * 
	 * */	
	public List<AiTile> calculeBlast(int range) throws StopRequestException
	{
		ai.	checkInterruption();
		AiTile tempo;
		List<AiTile> blast = new ArrayList<AiTile>();
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.RIGHT);
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.UP); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.UP);
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.LEFT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.LEFT);
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.DOWN); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.DOWN);
		}	
		return blast;
	}
	

	/*
	 * Calcule la carÃ© de taille range*range
	 * 
	 * @param range
	 * 		la portÃ©e de la bombe d'un hero
	 * @return tempo 
	 *		la liste des cases de l'area
	 * @throws StopRequestException 
	 * */
	/*public List<AiTile> calculeArea(int range) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> blast = new ArrayList<AiTile>();
		AiTile tempo;
		List<AiTile> blasttempo = new ArrayList<AiTile>();
		blasttempo.add(zone.getOwnHero().getTile());
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blasttempo.add(tempo);
			tempo =tempo.getNeighbor(Direction.RIGHT);
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.LEFT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blasttempo.add(tempo);
			tempo =tempo.getNeighbor(Direction.LEFT);
		}	
		Iterator<AiTile> blastList = blasttempo.iterator();
		AiTile temp;
		while(blastList.hasNext())
		{
			ai.	checkInterruption();
			temp=blastList.next();
			tempo = temp;
			tempo =tempo.getNeighbor(Direction.DOWN); 
			for(int i=1;i<range;i++)
			{	
				ai.	checkInterruption();
				blast.add(tempo);
				tempo =tempo.getNeighbor(Direction.DOWN);
			}	
			tempo = temp;
			tempo = tempo.getNeighbor(Direction.UP); 
			for(int i=1;i<range;i++)
			{	
				ai.	checkInterruption();
				blast.add(tempo);
				tempo =tempo.getNeighbor(Direction.UP);
			}		
		}
		Iterator<AiTile> blastsecond = blast.iterator();
		while(blastsecond.hasNext())
		{
			ai.	checkInterruption();
			temp=blastsecond.next();
			blasttempo.add(temp);
		}
		return blasttempo;
		
	}*/
	
	
	/***
	 * Calcules recursivement les cases qu'un hero peut passer dans une car�e déjà determine�. 
	 * Alors elle elemine les mur non destructible et puis les chemins que le hero associé ne
	 * peut pas acceder. Donc elle retourne une liste des cases que le hero associé peut 
	 * acceder. S'il n'y a pas de case alors elle retourne null.
	 * 
	 * @param tile 
	 * 		tile du hero qu'on veut trouve son chemin
	 * @param area
	 * 		une car�e qu'on estime pour un hero pour qu'il puisse acceder à ses cases dans le temps d'explosion d'une bombe
	 * @param safe
	 * 		liste des cases sur. Au d�but c'est null.
	 * @return result
	 * 		une liste des cases accesible, sinon une liste null
	 * @throws StopRequestException
	 */
	public List<AiTile> accesibleArea(AiTile tile, List<AiTile> area, List<AiTile> safe) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> neigs = new ArrayList<AiTile>();
		AiTile tempoNeig;
		if(area.contains(tile))
		{
			neigs = tile.getNeighbors();
			Iterator<AiTile> neigList = neigs.iterator();
			while (neigList.hasNext())
			{
				ai.	checkInterruption();
				tempoNeig = neigList.next();
				if(safe.contains(tempoNeig))
					result = safe;
				else
				{
					if(tempoNeig.isCrossableBy(zone.getOwnHero()) )
					{
						safe.add(tempoNeig);
						result = accesibleArea(tempoNeig, area, safe);
					}
				}
				
			}
		}
		return result;
	}
	
	/***
	 * Calcules toutes les casesrecursivement qu'un hero peut passer. 
	 * Alors elle elemine les mur non destructible et puis les chemins que le hero associé ne
	 * peut pas acceder. Donc elle retourne une liste des cases que le hero associé peut 
	 * acceder. S'il n'y a pas de case alors elle retourne null.
	 * 
	 * @param tile 
	 * 		tile du hero qu'on veut trouve son chemin
	 * @param safe
	 * 		liste des cases sur. Au d�but c'est null.
	 * @return result
	 * 		une liste des cases accesible, sinon une liste null
	 * @throws StopRequestException
	 */
	public List<AiTile> accesibleArea(AiTile tile, List<AiTile> safe) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> neigs = new ArrayList<AiTile>();
		AiTile tempoNeig;
			neigs = tile.getNeighbors();
			Iterator<AiTile> neigList = neigs.iterator();
			while (neigList.hasNext())
			{
				ai.	checkInterruption();
				tempoNeig = neigList.next();
				if(safe.contains(tempoNeig))
					result = safe;
				else
				{
					if(tempoNeig.isCrossableBy(zone.getOwnHero()) )
					{
						safe.add(tempoNeig);
						result = accesibleArea(tempoNeig, safe);
					}
				}
				
			
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
