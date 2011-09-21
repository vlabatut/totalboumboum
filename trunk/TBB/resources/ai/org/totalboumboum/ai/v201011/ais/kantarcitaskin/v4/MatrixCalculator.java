package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
@SuppressWarnings("unused")
public class MatrixCalculator
{
	
	/**
	 * La classe qui calcule les matrice en mode collect et en mode attaque
	 * 
	 * */
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
	
	/**
	 * Constructeur de la classe
	 * 
	 * @param zone
	 * 		la zone du jeu
	 * @param mode
	 * 		la mode de l'hero
	 * @param ai
	 * 		AI	 * 
	 * 
	 * */
	public MatrixCalculator(AiZone zone, int mode, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	
	}
	
	/**
	 * Initialise la matrice puis ajoute les valeurs des item qui se trouve dans le jeu
	 * 
	 * @return matrix
	 * 		la matrice numerique qui represent la zone du jeu
	 * */
	public double[][] matrixConstruction ()
	{
		double[][] matrix = new double[zone.getHeight()][zone.getWidth()];
		for(int i=0; i<zone.getHeight(); i++)
		{
			for(int j=0; j<zone.getWidth(); j++)
			{
				matrix[i][j]=0;
			}
		}
		
			bonusEffect(matrix);
			wallEffect(matrix);
			heroEffect(matrix);
			bombEffect(matrix);
	
		return matrix;
	}
	
	
	/**
	 * Ajoute l'effet des murs destructible. Dans le deux mode l'effet est le meme
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * 
	 * */
	public void wallEffect(double[][] matrix)
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
	

	/**
	 * Ajoute l'effet des advesaire. Dans le deux mode l'effet se varie et 
	 * on le controle. 
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * 
	 * */
	public void heroEffect(double[][] matrix)
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
					if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() ==0)
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
	

	/**
	 * Ajoute l'effet des bombes. Dans le deux mode l'effet est le meme
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * 
	 * */
	public void bombEffect(double[][] matrix)
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
	
	/**
	 * Calcule le temps d'explosion estime d'une bombe
	 * 
	 * @param bomb
	 * 		la bombe dont le temps d'explosion est calculé
	 * @return resutl
	 * 		le temps d'explosion estimé
	 * */
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

	/**
	 * Ajoute l'effet des bonus. Dans le deux mode l'effet se varie et on les ajoute dans les calcules
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * 
	 * */
	public void bonusEffect(double[][] matrix) 
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
	
	

	/**
	 * Ajoute l'effet des blasts. Dans le deux mode l'effet est le meme
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * 
	 * */
	public void fireEffect(double[][] matrix)
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

	/**
	 * Donne la valeur envoyé à la case qu'on indique
	 * 
	 * @param item
	 * 		l'item, prorietaire de valeur
	 * @param valeur
	 * 		valeur calculé de cette cas
	 * @param matrix
	 * 		matrice representant la zone du jeu 
	 *
	 * */
	public void setDefItemValeurs(AiTile item, int valeur, double[][] matrix)
	{
		int i = item.getLine();
		int j = item.getCol();
		if(zone.getTile(i, j).isCrossableBy(zone.getOwnHero()))
			matrix[i][j] = matrix[i][j] + valeur;
		
	}
}