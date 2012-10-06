package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v6;

import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class MatrixCalculator
{
	
	/**
	 * La classe qui calcule les matrice en mode collect et en mode attaque
	 * 
	 * */
	//Les contantes
	int BOMB = -400;
	int FIRE = -200;
	int BLAST = -100;
	int WALL = 10;
	int ENEMY1 = -20;
	int BONUS_COLLECT = 20;
	int BONUS_ATTACK = 5;
	int ENEMY2= 100;
	int ENEMY_ATTACK= 100;
	int mode;
	//LEs percepts
	ArtificialIntelligence ai;
	AiZone zone;
	Move move;
	
	
	/**
	 * Constructeur de la classe
	 * 
	 * @param zone
	 * 		la zone du jeu
	 * @param mode
	 * 		la mode de l'hero
	 * @param ai
	 * 		AI	 * 
	 * @param move 
	 * @throws StopRequestException 
	 * 
	 * */
	public MatrixCalculator(AiZone zone, int mode, ArtificialIntelligence ai, Move move) throws StopRequestException
	{
		ai.	checkInterruption();
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
		this.move=move;
	
	}
	
	/**
	 * Initialise la matrice puis ajoute les valeurs des item qui se trouve dans le jeu
	 * 
	 * @throws StopRequestException 
	 * */
	public void matrixConstruction () throws StopRequestException
	{
		ai.	checkInterruption();
		move.matrix = new double[zone.getHeight()][zone.getWidth()];
		for(int i=0; i<zone.getHeight(); i++)
		{
			ai.	checkInterruption();
			for(int j=0; j<zone.getWidth(); j++)
			{
				ai.	checkInterruption();
				move.matrix[i][j]=0;
			}
		}
		
			bonusEffect(move.matrix);
			wallEffect(move.matrix);
			heroEffect(move.matrix);
			bombEffect(move.matrix);
	
	}
	
	
	/**
	 * Ajoute l'effet des murs destructible. Dans le deux mode l'effet est le meme
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * @throws StopRequestException 
	 * 
	 * */
	public void wallEffect(double[][] matrix) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiBlock> wallsit =((KantarciTaskin) this.ai).destructibles.iterator();
		AiBlock wall;
		while (wallsit.hasNext()) 
		{
			ai.	checkInterruption();
			int nbBlast = zone.getOwnHero().getBombRange();
			wall = wallsit.next();
			int i=1;
			AiTile wallNeig = wall.getTile().getNeighbor(Direction.UP); 
			if(wall.isDestructible())
			{
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					setDefItemValeurs(wallNeig, WALL);
					wallNeig = wallNeig.getNeighbor(Direction.UP);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.LEFT);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					setDefItemValeurs(wallNeig, WALL);
					wallNeig = wallNeig.getNeighbor(Direction.LEFT);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.DOWN);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					setDefItemValeurs(wallNeig, WALL);
					wallNeig = wallNeig.getNeighbor(Direction.DOWN);
					i++;
				}
				wallNeig = wall.getTile().getNeighbor(Direction.RIGHT);
				i=1;
				while(i<nbBlast+1 && wallNeig.isCrossableBy(zone.getOwnHero())) 
				{
					ai.	checkInterruption();
					setDefItemValeurs(wallNeig, WALL);
					wallNeig = wallNeig.getNeighbor(Direction.RIGHT);
					
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
	 * @throws StopRequestException 
	 * 
	 * */
	public void heroEffect(double[][] matrix) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiHero> heroList = ((KantarciTaskin) this.ai).heroes.iterator();
		AiHero hero;
		AiTile heroNeig;
		int val;
		while (heroList.hasNext()) 
		{
			ai.	checkInterruption();
			hero = heroList.next();
			if(hero!= zone.getOwnHero())
			{	
				if(mode==1)
				{
					if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() ==0)
					{
						//setDefItemValeurs(hero.getTile(), ENEMY2);
						val=ENEMY2;
					}
					else
					{
						//setDefItemValeurs(hero.getTile(), ENEMY1);
						val=ENEMY1;
					}
				}
				else
				{
					//setDefItemValeurs(hero.getTile(), ENEMY_ATTACK, matrix);
					val=ENEMY_ATTACK;
				}
				
				int i=1;
				int nbBlast = hero.getBombRange();
				heroNeig = hero.getTile();
				
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX()+1, hero.getPosY()))).isCrossableBy(zone.getOwnHero())))
				{
					ai.	checkInterruption();
					setDefItemValeurs(heroNeig, val);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX()-1, hero.getPosY()))).isCrossableBy(zone.getOwnHero())))
				{
					ai.	checkInterruption();
					setDefItemValeurs(heroNeig, val);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX(), hero.getPosY()+1))).isCrossableBy(zone.getOwnHero())))
				{
					ai.	checkInterruption();
					setDefItemValeurs(heroNeig, val);
					i++;
				}
				heroNeig = hero.getTile();
				i=1;
				while(i<nbBlast+1 && ((heroNeig = heroNeig.getNeighbor(zone.getDirection(hero.getPosX(), hero.getPosY(), hero.getPosX(), hero.getPosY()-1))).isCrossableBy(zone.getOwnHero())))
				{
					ai.	checkInterruption();
					setDefItemValeurs(heroNeig, val);
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
	 * @throws StopRequestException 
	 * 
	 * */
	public void bombEffect(double[][] matrix) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiBomb> bombList =((KantarciTaskin) this.ai).bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			ai.	checkInterruption();
			bomb = bombList.next();
			int line = bomb.getLine();
			int col = bomb.getCol();
			matrix[line][col] = -100;
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				ai.	checkInterruption();
				blast=blasts.next();
				double distance = zone.getPixelDistance(blast.getPosX(), blast.getPosY(), zone.getOwnHero().getPosX(), zone.getOwnHero().getPosY());
				double speed = zone.getOwnHero().getWalkingSpeed();
				double temp=distance/speed;
				setDefItemValeurs(blast, (int)(BOMB - (tempExplosion(bomb)+temp/100)));
				//setDefItemValeurs(blast, (int)(BOMB * bomb.getTime()),matrix);
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
	 * @throws StopRequestException 
	 * */
	public double tempExplosion (AiBomb bomb) throws StopRequestException
	{
		ai.	checkInterruption();
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
	 * @throws StopRequestException 
	 * 
	 * */
	public void bonusEffect(double[][] matrix) throws StopRequestException 
	{
		ai.	checkInterruption();
		double temps;
		double tempshero;
		Iterator<AiItem> bonusList =((KantarciTaskin) this.ai).bonus.iterator();
		AiItem bonus;
		while (bonusList.hasNext())
		{
			ai.	checkInterruption();
			bonus = bonusList.next();
			if(mode==1)
				setDefItemValeurs(bonus.getTile(), BONUS_COLLECT);
			else
				setDefItemValeurs(bonus.getTile(), BONUS_ATTACK);
			Iterator<AiHero> heroList = ((KantarciTaskin) this.ai).heroes.iterator();
			AiHero hero;
			while (heroList.hasNext()) 
			{
				ai.	checkInterruption();
				hero = heroList.next();
				if(zone.getTileDistance(bonus.getTile(), hero.getTile()) < zone.getTileDistance(bonus.getTile(), zone.getOwnHero().getTile()))
				{	
					temps=-(zone.getTileDistance(bonus.getTile(), hero.getTile()))*	hero.getWalkingSpeed();
				    tempshero=-(zone.getTileDistance(bonus.getTile(), zone.getOwnHero().getTile()))*zone.getOwnHero().getWalkingSpeed();
					setDefItemValeurs(bonus.getTile(),(int)((tempshero-temps)/100) );
				}
			}
			
		}
	}
	
	

	/**
	 * Ajoute l'effet des blasts. Dans le deux mode l'effet est le meme
	 * 
	 * @param matrix
	 * 		la matrice representant la zone du jeu
	 * @throws StopRequestException 
	 * 
	 * */
	public void fireEffect(double[][] matrix) throws StopRequestException
	{
		ai.	checkInterruption();
		Iterator<AiFire> fireList = ((KantarciTaskin) this.ai).fires.iterator();
		AiFire fire;
		
		while(fireList.hasNext())
		{
			ai.	checkInterruption();
			fire = fireList.next();
			setDefItemValeurs(fire.getTile(), FIRE);
		}
	}

	/**
	 * Donne la valeur envoyé à la case qu'on indique
	 * 
	 * @param item
	 * 		l'item, prorietaire de valeur
	 * @param valeur
	 * 		valeur calculé de cette cas
	 * @throws StopRequestException 
	 *
	 * */
	public void setDefItemValeurs(AiTile item, int valeur) throws StopRequestException
	{
		ai.	checkInterruption();
		int i = item.getLine();
		int j = item.getCol();
		if(zone.getTile(i, j).isCrossableBy(zone.getOwnHero()))
			move.matrix[i][j] = move.matrix[i][j] + valeur;
		
	}

}