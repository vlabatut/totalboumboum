package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.*;
//import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
//import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

public class DropBomb 
{
	ArtificialIntelligence ai;
	AiZone zone;
	int mode;
	int[][] matrix;
	//List<AiHero> heroes;
	public DropBomb(AiZone zone, int mode, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	}
	public boolean decisionOfBomb() throws StopRequestException
	{
		ai.checkInterruption();
		
		boolean result=false; // Decision
		int range = (zone.getOwnHero().getBombRange());//la portée nos bombes
		List<AiTile> tempBlast = calculeBlast(range);//La portée virtuelle de notre bombe de futur 
		
		if(	zone.getOwnHero().getTile().getBombs().isEmpty() && (zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() != 0))
		{
			if(mode == 0)
			{
				try {
					if(safeArea(zone.getOwnHero(),range,tempBlast) > 1)
					{
						if(enemyAvaible(tempBlast))
						{
							result = true;
							//System.out.println("bomb pour enemy");
						}
						else
						{
							if(bloque(zone.getOwnHero()))
							{
								result = true;
								//System.out.println("bomb pour mur");
							}
						}
					}
				} catch (StopRequestException e) {
					// 
					e.printStackTrace();
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}	
			}
			else
			{	
				if(isSafe(zone.getOwnHero().getTile()))
				{
					try {
						if(safeArea(zone.getOwnHero(),range,tempBlast) > 1)
						{
							if(bonusRiskAvailbe(tempBlast))
							{
								result = true;
								//System.out.println("bombe pour bonus");
							}
							else
							{
								if(wallAvaible(tempBlast))
								{
									result=true;
									//System.out.println("bombe pour mur collect");
								}
							}
						}
					} catch (StopRequestException e) {
						// 
						e.printStackTrace();
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}	
				}
			}		
		}
			
		
	
		return result;
	}
	
	//La methode qui controle s'il existe un bonus mais plus proche à l'adversaire que nous. 
	private boolean bonusRiskAvailbe(List<AiTile> tempBlast)
	{
		List<AiItem> bonus = new ArrayList<AiItem>();
		bonus = zone.getItems();
		List<AiBlock> walls = zone.getBlocks();
		List<AiBomb> bombs = zone.getBombs();
		List<AiHero> heroes = zone.getHeroes();
		List<AiFire> fires =zone.getFires();
		bonus.removeAll(bombs);
		bonus.removeAll(walls);
		bonus.removeAll(heroes);
		bonus.removeAll(fires);
		boolean result = false;
		
		Iterator<AiItem> bonusList = bonus.iterator();
		AiItem bns;
		while (bonusList.hasNext())
		{
			bns = bonusList.next();
			Iterator<AiHero> heroList = heroes.iterator();
			AiHero hero;
			while (heroList.hasNext()) 
			{
				hero = heroList.next();
				if(zone.getTileDistance(bns.getTile(), hero.getTile()) < zone.getTileDistance(bns.getTile(), zone.getOwnHero().getTile()))
				{	
					result = true;
				}
			}
		}
		return result;
	}
	
	//La methode qui control si l'héro qu'on lui envoie par parametre est bloque entre les murs et les blasts
	private boolean bloque(AiHero ownHero) 
	{
		boolean result = true;
		List<AiTile> neig = new ArrayList<AiTile>();
		neig = ownHero.getTile().getNeighbors();
		Iterator<AiTile> neigbours = neig.iterator();
		AiTile tile;
		int control=0;
		while(neigbours.hasNext())
		{
			tile=neigbours.next();
			if(tile.isCrossableBy(ownHero));
				control = control+1;
		}
		if(control<2)
			result = false;
		
		return result;
	}
	
	//La methode qui controle si la case passée en parametre se trouve dans la porté d'une bombe
	public boolean isSafe(AiTile tile)
	{
		boolean result = true;
		List<AiBomb> bombs = this.zone.getBombs();
		Iterator<AiBomb> bombList = bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			bomb = bombList.next();
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				blast=blasts.next();
				if(blast.getLine() == tile.getLine() && blast.getCol() == tile.getCol())
					result = true;
				
			}	
		}	
		return result;
	}
	
	//La methode controle s'il existe des murs dans les case qu'on lui a envoy�
	public boolean wallAvaible(List<AiTile> tempBlast)
	{
		boolean resultat = false;
		
		List<AiBlock> walls = zone.getBlocks();
		Iterator<AiBlock> wallsit = walls.iterator();
		
		AiTile temp;
		AiBlock wall;
		while(wallsit.hasNext())
		{
			wall=wallsit.next();
			if(wall.isDestructible())
			{
				Iterator<AiTile> blast = tempBlast.iterator();
				while(blast.hasNext())
				{
					temp=blast.next();
					if(wall.getTile().getLine() == temp.getLine() && wall.getTile().getCol() == temp.getCol())
					{
						if(isSafe(wall.getTile()))
							resultat=true;
					}
				}
			}
		}
		return resultat;		
	}
	
	//La methode qui control s'il existe des adversaire dans les cases qu'on lui a envoy�
	public boolean enemyAvaible(List<AiTile> tempBlast)
	{
		boolean result = false;
		AiTile temp;
		List<AiHero> heroes = zone.getHeroes();
		Iterator<AiHero> heroesit = heroes.iterator();
		AiHero hero;
		while(heroesit.hasNext())
		{
			hero = heroesit.next();
			Iterator<AiTile> blast = tempBlast.iterator();
			while(hero!=zone.getOwnHero() && blast.hasNext())
			{
				
				temp=blast.next();
				if(hero.getTile().getLine() == temp.getLine() && hero.getTile().getCol()==temp.getCol())
				{
					//if(safeArea(hero,hero.getBombRange()/4,calculeBlast(hero.getBombRange()/4))<((hero.getBombRange()*hero.getBombRange())/2)-zone.getOwnHero().getBombCount())
						result=true;
				}
			}	
		}
		
		return result;
	}
	
	//La methode qui controle s'il se trouve une case sur autour de hero
	public int safeArea(AiHero hero, int range, List<AiTile> blast) throws StopRequestException, LimitReachedException
	{
		int result = 0;
		/*Cost  cost = new Cost(matrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(ai, zone.getOwnHero(), cost, heuristic);*/
		AiTile temp;
		
		List<AiTile> area = new ArrayList<AiTile>();
		area = calculeArea(range);
		AiTile pas;
		Iterator<AiTile> areaList = area.iterator();
		while(areaList.hasNext())
		{	
			pas = areaList.next();
			//if(!astar.processShortestPath(hero.getTile(), pas).isEmpty())
			if(pas.isCrossableBy(hero))
			{
				Iterator<AiTile> blastTempo = blast.iterator();
				while(blastTempo.hasNext())
				{
					temp=blastTempo.next();
					if(temp.getLine()== pas.getLine() && temp.getCol()==temp.getCol())
					{
						if(isSafe(temp))
							result = result+1;
					}
				}
			}
		}
		return result;
	}
	
	
	//La methode qui calcule la liste des cases d'une bombe futur de notre hero
	public List<AiTile> calculeBlast(int range)
	{
		List<AiTile> blast = new ArrayList<AiTile>();
		for(int i=1;i<range+1;i++)
		{	
			blast.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()-i, zone.getOwnHero().getPosY()))));
		}		
		for(int i=1;i<range+1;i++)
		{	
			blast.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()+i, zone.getOwnHero().getPosY()))));
		}	
		for(int i=1;i<range+1;i++)
		{	
			blast.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY()-i))));
		}	
		for(int i=1;i<range+1;i++)
		{	
			blast.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY()+i))));
		}	
		return blast;
	}
	
	//La methode qui calcule les cases qu'on peut aller dans le temp d'une explosion de notre bombe.
	public List<AiTile> calculeArea(int range)
	{
		List<AiTile> blast = new ArrayList<AiTile>();

		List<AiTile> blasttempo = new ArrayList<AiTile>();
		for(int i=1;i<range+1;i++)
		{	
			blasttempo.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()-i, zone.getOwnHero().getPosY()))));
			
		}		
		for(int i=1;i<range+1;i++)
		{	
			blasttempo.add(zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()+i, zone.getOwnHero().getPosY()))));
		}	
		Iterator<AiTile> blastList = blasttempo.iterator();
		AiTile temp;
		while(blastList.hasNext())
		{
			temp=blastList.next();
			for(int i=1;i<range+1;i++)
			{	
				blast.add(temp.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()-i))));
			}	
			for(int i=1;i<range+1;i++)
			{	
				blast.add(temp.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()+i))));
			}	
		}
		Iterator<AiTile> blastsecond = blast.iterator();
		while(blastsecond.hasNext())
		{
			temp=blastsecond.next();
			blasttempo.add(temp);
		}
		return blasttempo;
		
	}
}
