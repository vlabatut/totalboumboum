package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
public class MatrixCalculator
{
	//Les contantes
	int BOMB = -200;
	int FIRE = -200;
	int BLAST = -100;
	int WALL = 10;
	int ENEMY1 = -20;
	int BONUS_COLLECT = 100;
	int BONUS_ATTACK = 60;
	int ENEMY2= 20;
	int ENEMY_ATTACK= 30;
	int mode;
	//LEs percepts
	ArtificialIntelligence ai;
	AiZone zone;
	List<AiItem> bonus; 
	List<AiTile> walls;
	List<AiHero> heroes;
	List<AiBomb> bombs;
	List<AiFire> fires;
	
	//Methode Constructeur
	public MatrixCalculator(AiZone zone, int mode, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	
	}
	
	//Initialisation et modification de matrice
	public int[][] matrixConstruction ()
	{
		int[][] matrix = new int[zone.getHeight()][zone.getWidth()];
		for(int i=0; i<zone.getHeight(); i++)
		{
			for(int j=0; j<zone.getWidth(); j++)
			{
				matrix[i][j]=0;
			}
		}
		try 
		{
			bonusEffect(matrix);
			wallEffect(matrix);
			heroEffect(matrix);
			bombEffect(matrix);
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		return matrix;
	}
	
	//Ajoute les valeurs
	public void wallEffect(int[][] matrix)
	{
		List<AiBlock> walls = zone.getBlocks();
		Iterator<AiBlock> wallsit = walls.iterator();
		AiBlock wall;
		while (wallsit.hasNext()) 
		{
			int nbBlast = zone.getOwnHero().getBombRange();
			wall = wallsit.next();
			int i=1;
			AiTile wallNeig = wall.getTile(); 
			if(wall.isDestructible())
			{
				while(i<nbBlast+1) 
				{
					wallNeig = wallNeig.getNeighbor(zone.getDirection(wall.getPosX(), wall.getPosY(), wall.getPosX()+1, wall.getPosY()));
					if(wallNeig.isCrossableBy(zone.getOwnHero()))
						setDefItemValeurs(wallNeig, WALL, matrix);
					i++;
				}
				wallNeig = wall.getTile();
				i=1;
				while(i<nbBlast+1) 
				{
					wallNeig = wallNeig.getNeighbor(zone.getDirection(wall.getPosX(), wall.getPosY(), wall.getPosX()-1, wall.getPosY()));
					if(wallNeig.isCrossableBy(zone.getOwnHero()))
						setDefItemValeurs(wallNeig, WALL, matrix);
					i++;
				}
				wallNeig = wall.getTile();
				i=1;
				while(i<nbBlast+1) 
				{
					wallNeig = wallNeig.getNeighbor(zone.getDirection(wall.getPosX(), wall.getPosY(), wall.getPosX(), wall.getPosY()-1));
					if(wallNeig.isCrossableBy(zone.getOwnHero()))
						setDefItemValeurs(wallNeig, WALL, matrix);
					i++;
				}
				wallNeig = wall.getTile();
				i=1;
				while(i<nbBlast+1) 
				{
					wallNeig = wallNeig.getNeighbor(zone.getDirection(wall.getPosX(), wall.getPosY(), wall.getPosX(), wall.getPosY()+1));
					if(wallNeig.isCrossableBy(zone.getOwnHero()))
						setDefItemValeurs(wallNeig, WALL, matrix);
					i++;
				}
			}
			else
				matrix[wall.getLine()][wall.getCol()] = 0;
		}
	}
	
	//Ajoute les adversaires
	public void heroEffect(int[][] matrix)
	{
		List<AiHero> heroes = zone.getHeroes();
		Iterator<AiHero> heroList = heroes.iterator();
		AiHero hero;
		AiTile heroNeig;
		int val;
		while (heroList.hasNext()) 
		{
			hero = heroList.next();
			if(hero!= zone.getOwnHero())
			{	
				if(mode==1)
				{
					if(hero.getBombNumberMax()-hero.getBombNumberCurrent() ==0)
					{
						setDefItemValeurs(hero.getTile(), ENEMY2, matrix);
						val=ENEMY2;
					}
					else
					{
						setDefItemValeurs(hero.getTile(), ENEMY1, matrix);
						val=ENEMY1;
					}
				}
				else
				{
					setDefItemValeurs(hero.getTile(), ENEMY_ATTACK, matrix);
					val=ENEMY_ATTACK;
				}
				
				int i=1;
				int nbBlast = hero.getBombRange();
				heroNeig = hero.getTile();
				
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX()+1, hero.getPosY()))).isCrossableBy(zone.getOwnHero())))
				{
					setDefItemValeurs(heroNeig, val, matrix);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX()-1, hero.getPosY()))).isCrossableBy(zone.getOwnHero())))
				{
					setDefItemValeurs(heroNeig, val, matrix);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX(), hero.getPosY()+1))).isCrossableBy(zone.getOwnHero())))
				{
					setDefItemValeurs(heroNeig, val, matrix);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX(), hero.getPosY()-1))).isCrossableBy(zone.getOwnHero())))
				{
					setDefItemValeurs(heroNeig, val, matrix);
					i++;
				}
			}
			
		}
	}
	
	//Ajoute les bombes
	public void bombEffect(int[][] matrix)
	{
		List<AiBomb> bombs = this.zone.getBombs();
		Iterator<AiBomb> bombList = bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			bomb = bombList.next();
			int line = bomb.getLine();
			int col = bomb.getCol();
			matrix[line][col] = -100;
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				blast=blasts.next();
				double distance = zone.getPixelDistance(blast.getPosX(), blast.getPosY(), zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY());
				double speed = zone.getOwnHero().getWalkingSpeed();
				double temp=distance/speed;
				setDefItemValeurs(blast, (int)(BOMB - (tempExplosion(bomb)+temp/100)),matrix);
			}	
		}	
	}
	
	//Calcule le temps d'explosion d'une bombe
	public double tempExplosion (AiBomb bomb)
	{
		double result;
		double temp;
		temp=bomb.getTime();
		if(temp <(3*bomb.getExplosionDuration())/4)
			result=-100;
		else
			result=100;
		return result;
	}
	
	//Ajoute les bonus
	public void bonusEffect(int[][] matrix) throws StopRequestException, LimitReachedException
	{
		bonus = new ArrayList<AiItem>();
		bonus = zone.getItems();
		List<AiBlock> walls = zone.getBlocks();
		List<AiBomb> bombs = zone.getBombs();
		List<AiHero> heroes = zone.getHeroes();
		List<AiFire> fires =zone.getFires();
		bonus.removeAll(bombs);
		bonus.removeAll(walls);
		bonus.removeAll(heroes);
		bonus.removeAll(fires);
		double temps;
		double tempshero;
		Iterator<AiItem> bonusList = bonus.iterator();
		AiItem bonus;
		while (bonusList.hasNext())
		{
			bonus = bonusList.next();
			if(mode==1)
				setDefItemValeurs(bonus.getTile(), BONUS_COLLECT, matrix);
			else
				setDefItemValeurs(bonus.getTile(), BONUS_ATTACK, matrix);
			Iterator<AiHero> heroList = heroes.iterator();
			AiHero hero;
			while (heroList.hasNext()) 
			{
				hero = heroList.next();
				if(zone.getTileDistance(bonus.getTile(), hero.getTile()) < zone.getTileDistance(bonus.getTile(), zone.getOwnHero().getTile()))
				{	
					temps=-(zone.getTileDistance(bonus.getTile(), hero.getTile()))*	hero.getWalkingSpeed();
				    tempshero=-(zone.getTileDistance(bonus.getTile(), zone.getOwnHero().getTile()))*zone.getOwnHero().getWalkingSpeed();
					setDefItemValeurs(bonus.getTile(),(int)((tempshero-temps)/100) , matrix);
				}
			}
			
		}
	}
	
	
	//Ajoute les blasts
	public void fireEffect(int[][] matrix)
	{
		fires = zone.getFires();
		Iterator<AiFire> fireList = fires.iterator();
		AiFire fire;
		
		while(fireList.hasNext())
		{
			fire = fireList.next();
			setDefItemValeurs(fire.getTile(), FIRE, matrix);
		}
	}
	
	//Ajoute les valeurs des items dans la matrice
	public void setDefItemValeurs(AiTile item, int valeur, int[][] matrix)
	{
		int i = item.getLine();
		int j = item.getCol();
		if(zone.getTile(i, j).isCrossableBy(zone.getOwnHero()))
			matrix[i][j] = matrix[i][j] + valeur;
		
	}
}