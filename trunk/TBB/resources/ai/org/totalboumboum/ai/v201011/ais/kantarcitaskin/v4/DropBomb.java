package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v4;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings("unused")
public class DropBomb 
{
	ArtificialIntelligence ai;
	AiZone zone;
	int mode;
	int[][] matrix;
	
	/**
	 * Constructeur de la classe DropBomb
	 * @param zone 
	 * 		la zone du jeu
	 * @param mode 
	 * 		mode de l'agent
	 * @param ai
	 * 		AI correspondant
	 * 
	 * */
	public DropBomb(AiZone zone, int mode, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	}
	
	/**
	 * Methode qui decide de poser une bombe ou pas. la decision est pris en regardant deux modes 
	 * different et en trouvant les cases sur. s'il n'y a pas de cases sur on ne pose jamais 
	 * de bombe. Sinon on essai de poser une bombe dans le cadre de but de mode.
	 * 
	 * @return result
	 * 		vrai si la decision est poser une bombe faux sinon
	 * */
	public boolean decisionOfBomb() throws StopRequestException
	{
		ai.checkInterruption();
		
		boolean result=false; // Decision
		int range = zone.getOwnHero().getBombRange();//la portée de nos bombes
		List<AiTile> tempBlast = calculeBlast(range);//La portée virtuelle de notre bombe de futur 
		
		if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() != 0 && isSafe(zone.getOwnHero().getTile()))
		{
			if(mode == 0)
			{
					if(safeArea(zone.getOwnHero(),range,tempBlast) > 2)
					{
						if(enemyAvaible(tempBlast))
						{
							result = true;
							//System.out.println("bomb pour enemy");
						}
						else
						{
							if(blockade(zone.getOwnHero()))
							{
								result = true;
								//System.out.println("bomb pour mur");
							}
						}
					}
					
			}
			else
			{	
				if(isSafe(zone.getOwnHero().getTile()))
				{
				
						if(safeArea(zone.getOwnHero(),range,tempBlast) > 2)
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
						
				}
			}		
		}	
		return result;
	}
	
	
	/**
	 * Regarde s'il y a des bonus dans notre area d'effet de bombe qui est plus proche a un 
	 * adversaire 
	 * 
	 * @param tempBlast
	 * 		la liste des cases de la porté de notre bombe virtuelle
	 * @return result
	 * 		vrai s'il y'en a un, faux sinon
	 * */
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
	
	/**
	 * Regarde si l'agent est bloque et si oui dans combien de coté.
	 * 
	 * @param ownHero
	 * 		agent qu'on veut tester s'il est bloquer
	 * 
	 * @return result
	 * 		vraie s'il es bloque, faux sinon
	 * */
	private boolean blockade(AiHero ownHero) 
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
	
	
	/**
	 * Teste si la case qu'on a envoye se trouve dans le danger d'une bombe dans la zone.
	 * 
	 * @param tile
	 * 		la case qu'on veut tester
	 * 
	 * @return result
	 * 		vrai s'elle est en danger, faux sinon
	 * 
	 * */
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
					result = false;		
			}	
		}	
		return result;
	}
	
	
	/**
	 * Teste s'il se trouve de mur destructible dans la porté d'une bombe virtuelle
	 * 
	 * @param tempBlast
	 *		 la liste des cases de la porté de notre bombe virtuelle
	 *
	 *@return result
	 *		vrai s'il y'en a un, faux sinon
	 * */
	public boolean wallAvaible(List<AiTile> tempBlast)
	{
		boolean resultat = false;
		
		List<AiBlock> walls = zone.getDestructibleBlocks();	
		AiTile temp;
		AiBlock wall;
		Iterator<AiBlock> wallsit = walls.iterator();
		while(wallsit.hasNext())
		{
			wall=wallsit.next();
			Iterator<AiTile> blast = tempBlast.iterator();
			while(blast.hasNext())
			{
				temp=blast.next();
				if(wall.getTile().getLine() == temp.getLine() && wall.getTile().getCol() == temp.getCol())
				{
					if(isSafe(wall.getTile()) && isOpen(zone.getOwnHero().getTile(), wall.getTile()))
						resultat=true;
				}
			}
			
		}
		return resultat;		
	}
	

	private boolean isOpen(AiTile tile, AiTile tile2)
	{
		boolean result = true;
		AiTile tempo = tile;
		if(tile.getLine()-tile2.getLine()!= 0 && tile.getCol()-tile2.getCol()!= 0)
			result = false;
		else
		{
			int line = tile.getLine()-tile2.getLine();
			int col = tile.getCol()-tile2.getCol();
			if(line == 0)
			{
				if(col < 0)
				{
					while (tempo.getCol()!= tile2.getCol())
					{
						tempo = tempo.getNeighbor(Direction.RIGHT);
						if(!tempo.isCrossableBy(zone.getOwnHero()))
							result = false;
					}
					
				}
				else
				{
					while (tempo.getCol()!= tile2.getCol())
					{
						tempo = tempo.getNeighbor(Direction.LEFT);
						if(!tempo.isCrossableBy(zone.getOwnHero()))
							result = false;
					}
				}
			}
			else
			{
				if(col < 0)
				{
					while (tempo.getLine()!= tile2.getLine())
					{
						tempo = tempo.getNeighbor(Direction.UP);
						if(!tempo.isCrossableBy(zone.getOwnHero()))
							result = false;
					}
				}
				else
				{
					while (tempo.getLine()!= tile2.getLine())
					{
						tempo = tempo.getNeighbor(Direction.DOWN);
						if(!tempo.isCrossableBy(zone.getOwnHero()))
							result = false;
					}
				}
			}
		}
			
		
		return false;
	}

	/**
	 * Teste s'il se trouve des adversaire dans la porté d'une bombe virtuelle
	 * 
	 * @param tempBlast
	 *		 la liste des cases de la porté de notre bombe virtuelle
	 *
	 *@return result
	 *		vrai s'il y'en a un, faux sinon
	 * */
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
					if(isOpen(zone.getOwnHero().getTile(),hero.getTile()))
						result=true;
				}
			}	
		}
		
		return result;
	}
	
	
	/**
	 * Teste s'il se trouve des cases sur à alles apres avois poser une bombe.
	 * il prend une caré au tille de la range et enleve les cases de la porté virtuelle. puis 
	 * s'il se trouve des cases sur (en utilisen isSafe) il calcule les cases surs.
	 * 
	 * @param hero
	 * 		hero qu'on veut teste
	 * @param range
	 * 		la porté de la bombe de hero
	 * @param blast
	 * 		la liste des cases de la bombe virtuelle
	 * 
	 * @return restult
	 * 		le nombre des cases sur
	 * 			
	 * */
	public int safeArea(AiHero hero, int range, List<AiTile> blast) 
	{
		int result = 0;
		AiTile temp;
		List<AiTile> area = new ArrayList<AiTile>();
		area = calculeArea(1);
		AiTile pas;
		Iterator<AiTile> areaList = area.iterator();
		while(areaList.hasNext())
		{	
			pas = areaList.next();
			if(pas.isCrossableBy(hero))
			{
				Iterator<AiTile> blastTempo = blast.iterator();
				while(blastTempo.hasNext())
				{
					temp=blastTempo.next();
					if(temp.getLine()!= pas.getLine() || temp.getCol()!=pas.getCol())
					{
						if(isSafe(pas))
							result = result+1;
					}
				}
			}
		}
		return result;
	}
	
	
	/**
	 * Calcule la liste des cases de la portée de bombe d'un hero
	 * 
	 * @param range
	 * 		la portee de la bombe d'un hero
	 * 
	 * @return tempo
	 * 		la liste des cases de la portee
	 * 
	 * */
	
	public List<AiTile> calculeBlast(int range)
	{
		AiTile tempo;
		List<AiTile> blast = new ArrayList<AiTile>();
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()-1, zone.getOwnHero().getPosY()))); 
		for(int i=1;i<range;i++)
		{	
			blast.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX()-1, tempo.getPosY()))));
			tempo =tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX()-1, tempo.getPosY())));
			
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()+1, zone.getOwnHero().getPosY()))); 
		for(int i=1;i<range;i++)
		{	
			blast.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX()+1, tempo.getPosY()))));
			tempo =tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX()+1, tempo.getPosY())));
		}
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY()-1))); 
		for(int i=1;i<range;i++)
		{	
			blast.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX(), tempo.getPosY()-1))));
			tempo =tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX(), tempo.getPosY()-1)));
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY()+1))); 
		for(int i=1;i<range;i++)
		{	
			blast.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX(), tempo.getPosY()+1))));
			tempo =tempo.getNeighbor((zone.getDirection(tempo.getPosX(), tempo.getPosY(),tempo.getPosX(), tempo.getPosY()+1)));
			
		}	
		return blast;
	}
	

	/**
	 * Calcule la caré de taille range*range
	 * 
	 * @param range
	 * 		la portée de la bombe d'un hero
	 * @return tempo 
	 *		la liste des cases de l'area
	 * */
	public List<AiTile> calculeArea1(int range)
	{
		List<AiTile> blast = new ArrayList<AiTile>();
		AiTile tempo;
		List<AiTile> blasttempo = new ArrayList<AiTile>();
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()+1, zone.getOwnHero().getPosY())));
		for(int i=1;i<range+1;i++)
		{	
			blasttempo.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(),tempo.getPosY(),tempo.getPosX()-1, tempo.getPosY()))));
			tempo = tempo.getNeighbor((zone.getDirection(tempo.getPosX(),tempo.getPosY(),tempo.getPosX()-1, tempo.getPosY())));
		}
		tempo = zone.getOwnHero().getTile().getNeighbor((zone.getDirection(zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY(),zone.getOwnHero().getPosX()+1, zone.getOwnHero().getPosY())));
		for(int i=1;i<range+1;i++)
		{	blasttempo.add(tempo.getNeighbor((zone.getDirection(tempo.getPosX(),tempo.getPosY(),tempo.getPosX()+1, tempo.getPosY()))));
			tempo = tempo.getNeighbor((zone.getDirection(tempo.getPosX(),tempo.getPosY(),tempo.getPosX()+1, tempo.getPosY())));
		}
		Iterator<AiTile> blastList = blasttempo.iterator();
		AiTile temp;
		while(blastList.hasNext())
		{
			temp=blastList.next();
			tempo = temp;
			for(int i=1;i<range+1;i++)
			{	
				blast.add(tempo.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()-i))));
				tempo = tempo.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()-i)));
			}	
			tempo = temp;
			for(int i=1;i<range+1;i++)
			{	
				blast.add(tempo.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()+i))));
				tempo = tempo.getNeighbor((zone.getDirection(temp.getPosX(), temp.getPosY(),temp.getPosX(), temp.getPosY()+i)));
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
	public List<AiTile> calculeArea(int range)
	{
		List<AiTile> result = new ArrayList<AiTile>();
		
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.RIGHT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.RIGHT));

		return result;
	}

}
