package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v6;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
//import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class Move 
{
	/** Classe qui calcule la direction de movement en trouvant le chemin le plus court */
	AiZone zone;
	/** */
	int mode;
	/** */
	ArtificialIntelligence ai;
	/** */
	double[][] matrix;
		
	/**
	 * Constructeur
	 * 
	 * @param zone
	 * 		la zone du jeu
	 * @param mode 
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
	 * @throws StopRequestException 
	 * */
	public Direction getDirection() throws StopRequestException
	{	
		ai.	checkInterruption();
		matrix = new double[zone.getHeight()][zone.getWidth()];
		MatrixCalculator collect = new MatrixCalculator(zone,mode,ai,this);
		collect.matrixConstruction();
		
		
		Cost  cost = new Cost(matrix, this.ai);
		//BasicCostCalculator cost = new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(ai, zone.getOwnHero(), cost, heuristic);
		AiPath path = null;
		Direction resultat= Direction.NONE;
	
		
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
		AiTile target= targetTile();
		
		
		List<AiTile> safe = new ArrayList<AiTile>();
		List<AiTile> areaSafe = accesibleArea(zone.getOwnHero().getTile(),safe);
		if(!areaSafe.contains(target))
			wallEffect(target);
		
		areaSafe.remove(zone.getOwnHero().getTile());
		Iterator<AiTile> area = areaSafe.iterator();
		AiTile tempo = null, target1 = target, target2 = target;
		double n=0, k=-10;
		if(matrix[zone.getOwnHero().getTile().getLine()][zone.getOwnHero().getTile().getCol()]<0)
		{
			while(area.hasNext() && k<0)
			{
				ai.	checkInterruption();
				tempo = area.next();
				if(matrix[tempo.getLine()][tempo.getCol()] >= 0 && tempo.isCrossableBy(zone.getOwnHero()))
				{
					k = matrix[tempo.getLine()][tempo.getCol()];
					target1 = zone.getTile(tempo.getLine(), tempo.getCol());
					org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = this.ai.getOutput();
					Color color = Color.MAGENTA;
					output.setTileColor(target1, color);
				}
			}
		}
		else if(matrix[zone.getOwnHero().getTile().getLine()][zone.getOwnHero().getTile().getCol()]>=0)
		{
			while(area.hasNext())
			{
				ai.	checkInterruption();
				tempo = area.next();
				org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = this.ai.getOutput();
				Color color = Color.RED;
				output.setTileColor(target1, color);
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
				output = ai.getOutput();
				output.setTileText(tempo.getLine(),tempo.getCol(), ""+matrix[tempo.getLine()][tempo.getCol()]);	
					
			}

		}
		
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
			
				try {
					path = astar.processShortestPath(zone.getOwnHero().getTile(), target);
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}
			
		}
		else
		{
			try {
				if(target1!= zone.getOwnHero().getTile())
				{
					path = astar.processShortestPath(zone.getOwnHero().getTile(), target1);
				}
				else
					path = astar.processShortestPath(zone.getOwnHero().getTile(), target2);
			} catch (LimitReachedException e) {
				// 
				e.printStackTrace();
			}	
			
		}
		if(path.getTiles().size()!=0)
		{
			org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = this.ai.getOutput();
			Color color = Color.BLACK;
			output.addPath(path, color);
		
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
			{
				resultat = Direction.NONE;
			}
			else
			{
				if(matrix[zone.getOwnHero().getLine()][zone.getOwnHero().getCol()]>-150 && matrix[zone.getOwnHero().getTile().getNeighbor(resultat).getLine()][zone.getOwnHero().getTile().getNeighbor(resultat).getCol()] < -150 )
				{
					resultat = Direction.NONE;
				}
			}
			
		}
			
			
		return resultat;
		
		
	}

	/***
	 * Cherche la case dont la valeur est plus eleve.S'elle sont tout les memes alors 
	 * il renvoi la case de l'hero correspondant
	 * 
	 * @return resultat
	 * 		la case dont la valeur est plus eleve ou bien la case de l'hero
	 * @throws StopRequestException 
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
		Iterator<AiBomb> bombList = ((KantarciTaskin) this.ai).bombs.iterator();
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
	 * Calcule la liste des cases de la portÃƒÂ©e de bombe d'un hero
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
	 * Calcule la carÃƒÂ© de taille range*range
	 * 
	 * @param range
	 * 		la portÃƒÂ©e de la bombe d'un hero
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
	 * Calcules recursivement les cases qu'un hero peut passer dans une carée déjà determineé. 
	 * Alors elle elemine les mur non destructible et puis les chemins que le hero associé ne
	 * peut pas acceder. Donc elle retourne une liste des cases que le hero associé peut 
	 * acceder. S'il n'y a pas de case alors elle retourne null.
	 * 
	 * @param tile 
	 * 		tile du hero qu'on veut trouve son chemin
	 * @param area
	 * 		une carée qu'on estime pour un hero pour qu'il puisse acceder à ses cases dans le temps d'explosion d'une bombe
	 * @param safe
	 * 		liste des cases sur. Au début c'est null.
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
	 * 		liste des cases sur. Au début c'est null.
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
	
	
	/**
	 * Valeur d'une mur: CONSTANT
	 * */
	int WALL = 10;
	
	/***
	 * Prend la case but en AiTile et ajoute aux murs destructible, une valeur associé a la distance entre 
	 * l'objectif et notre hero. 
	 * 
	 * @param target
	 * 		La case interessant
	 * @throws StopRequestException
	 */
	public void wallEffect(AiTile target) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiBlock> wallsit = ((KantarciTaskin) this.ai).destructibles.iterator();
		List<AiTile> control = new ArrayList<AiTile>();
		AiBlock wall;
		while (wallsit.hasNext()) 
		{
			ai.	checkInterruption();
			int nbBlast = 2;
			wall = wallsit.next();
			int i=1;
			AiTile wallNeig = wall.getTile().getNeighbor(Direction.UP); 
			if(wall.isDestructible() && isSafe(wall.getTile()))
			{
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					if(i==1)
					{
						setDefItemValeurs(wallNeig, (WALL), matrix);
						if(!control.contains(wallNeig))
						{
							control.add(wallNeig);
							setDefItemValeurs(wallNeig, (80 / ((Math.abs(target.getLine()-wall.getTile().getLine())+ Math.abs(target.getCol()-wall.getTile().getCol())))), matrix);
						}
					}
					else
						setDefItemValeurs(wallNeig, (WALL), matrix);
					wallNeig = wallNeig.getNeighbor(Direction.UP);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.LEFT);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					if(i==1 && isSafe(wallNeig))
					{
						setDefItemValeurs(wallNeig, (WALL), matrix);
						if(!control.contains(wallNeig))
						{
							control.add(wallNeig);
							setDefItemValeurs(wallNeig, (80 / ((Math.abs(target.getLine()-wall.getTile().getLine())+ Math.abs(target.getCol()-wall.getTile().getCol())))), matrix);
						}
					}
					else
						setDefItemValeurs(wallNeig, (WALL), matrix);
					wallNeig = wallNeig.getNeighbor(Direction.LEFT);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.DOWN);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					if(i==1 && isSafe(wallNeig))
					{
						setDefItemValeurs(wallNeig, (WALL), matrix);
						if(!control.contains(wallNeig))
						{
							control.add(wallNeig);
							setDefItemValeurs(wallNeig, (80 / ((Math.abs(target.getLine()-wall.getTile().getLine())+ Math.abs(target.getCol()-wall.getTile().getCol())))), matrix);
						}
					}
					else
						setDefItemValeurs(wallNeig, (WALL), matrix);
					wallNeig = wallNeig.getNeighbor(Direction.DOWN);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.RIGHT);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					if(i==1 && isSafe(wallNeig))
					{
						setDefItemValeurs(wallNeig, (WALL), matrix);
						if(!control.contains(wallNeig))
						{
							control.add(wallNeig);
							setDefItemValeurs(wallNeig, (80 / ((Math.abs(target.getLine()-wall.getTile().getLine())+ Math.abs(target.getCol()-wall.getTile().getCol())))), matrix);
						}
					}
					else
						setDefItemValeurs(wallNeig, (WALL), matrix);
					wallNeig = wallNeig.getNeighbor(Direction.RIGHT);
					
					i++;
				}
			}
			else
				matrix[wall.getLine()][wall.getCol()] = 0;
		}
	}
	
	/***
	 * Ajoute la valeur qu'on lui a passé par parametre à la matrice qu'on lui à donné.
	 * @param item
	 * 		l'item dont la case est interessant
	 * @param valeur
	 * 		la valeur à donné à la case choisi
	 * @param matrix
	 * 		matrice represantant la zone du jeu
	 * @throws StopRequestException
	 */
	public void setDefItemValeurs(AiTile item, int valeur, double[][] matrix) throws StopRequestException
	{
		ai.	checkInterruption();
		int i = item.getLine();
		int j = item.getCol();
		if(zone.getTile(i, j).isCrossableBy(zone.getOwnHero()))
			matrix[i][j] = matrix[i][j] + valeur;
		
	}
	
	
	/****
	 * Teste si le hero qu'On lui à passé en parametre se trouve sur une bombe
	 * 
	 * @param hero
	 * 		hero qu'on veut tester son position
	 * @return control 
	 * 		vrai si oui, faux sinon
	 * @throws StopRequestException 
	 */
	public boolean onBomb (AiHero hero) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiBomb> bombs = ((KantarciTaskin) this.ai).bombs.iterator();
		AiBomb temp;
		boolean control = false;
		while(bombs.hasNext() && !control)
		{
			ai.	checkInterruption();
			temp = bombs.next();
			if(temp.getTile()== hero.getTile())
				control = true;
		}
			
		return control;
	}
	
}
