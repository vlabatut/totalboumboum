package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v3;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe qui contient les méthodes concernant la sécurité de notre agent.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class SecurityHandler {

	/** Création d'une liste global des cases accessibles */
	private Set<AiTile> accessibleTiles;
	
	/** Crétion d'un agent ai */
	private Agent ai;

	/**
	 * Appel à la classe SecurityHandler
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public SecurityHandler(Agent ai) 
	{
		ai.checkInterruption();
		this.ai = ai;
		accessibleTiles = new TreeSet<AiTile>();
	}	

	/** Mise a jour des cases accessibles */
	public void updateAccessibleTiles()
	{
		ai.checkInterruption();
		accessibleTiles.clear();
		processAccessibleTiles(ai.getZone().getOwnHero().getTile());
	}
	
	/**
	 * 
	 * Description : 
	 * 		Cette méthode calcule le niveau de disponibilité d'une case donnée en paramètre. 
	 * 		Il regarde pour chaque case voisin, s'il y a un mur, une bombe, une explosion ou
	 * 		bien une future explosion, respectivement par les méthodes "getBlocks", "getBombs",
	 * 		"getFires" et "isDangerous". D'où le niveau de disponibilité diminue ou agmente au
	 * 		fur et à mesure. Une case qui est totalement disponible va renvoyer la valeur 0. 
	 * 		Une case qui est totalement indisponible va renvoyer la valeur 4.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur entier selon notre variable "result" qui donne le niveau 
	 * 		de disponibilité de la case sélectionnée. Par défaut, on suppose que la 
	 * 		case est complétement entourée par des cases qui diminuent sa disponibilité, 
	 * 		c'est-à-dire "result =4" et on le diminue au fur et à mesure si necessaire. 
	 */
	
	public int surroundLevel(AiTile tile)
	{				
		ai.checkInterruption();
		int result = 4;
		
		for (AiTile neighborsTile : tile.getNeighbors())
		{
			ai.checkInterruption();			
			if (neighborsTile.getBlocks().isEmpty() && neighborsTile.getBombs().isEmpty() && !willBurn(neighborsTile))
			{
				result--;
			}
		}	
		
		return result;		
	}

	/**
	 * 
	 * Description : 
	 * 		Cette méthode prend en paramètre une case et verifie si une explosion va avoir
	 * 		lieu ou non sur la case. Pour cela, elle prend la liste de tous les bombes qui
	 * 		se trouve sur la zone, puis à l'aide de la méthode "getBlast" elle prend les cases
	 * 		dans lesquelles une explosion va appraitre et enfin il vérifie si la case donnée 
	 * 		en paramètre est aussi un des cases affectés par une explosion.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur boléen selon notre variable "control" qui donne true si 
	 * 		une explosion va avoir lieu sur la case sélectionnée et false sinon. Pour cela 
	 * 		on regarde les explosions de tous les bombes qui se trouvent sur la zone.
	 */
	public boolean willBurn(AiTile tile)
	{
		ai.checkInterruption();
		boolean control = false;
				
		for (AiBomb bombReg : ai.getZone().getBombs()) 
		{		
			ai.checkInterruption();			
			if(bombReg.getBlast().contains(tile))
			{
				control = true;
			}
		}		
		
		return control;
	}

	/**
	 * 
	 * Description : 
	 * 		Cette méthode vérifie s'il y a agent adversaire dans la case  donnée en 
	 * 		paramètre.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur booléen selon notre variable "control" qui donne true 
	 * 		quand il y a un agent adversaire sur la case sélectionnée et false sinon.
	 */
	public boolean ifExistsAnyoneButUs(AiTile tile)
	{
		ai.checkInterruption();
		boolean control=false;
		
		for(AiHero enemy : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			if(enemy.getTile() == tile)
			{
				control = true;
			}
		}		
		
		return control;
	}

	/**
	 * Description : 
	 * 		Cette méthode calcule le temps restant de l'explosion dans la case donnée
	 * 		en paramètre.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur entier selon notre variable "duration" qui varie par
	 * 		rapport au temps restant de l'explosion dans la case donnée en paramètre.
	 * 		Par défaut, on suppose que "duration = 10000".
	 * 
	 */
	public long getTimeLeft(AiTile tile)
	{
		ai.checkInterruption();
		long duration = 10000;
		long tempDuration;
				
		if(willBurn(tile))
		{
			if(tile.getFires().isEmpty())
			{
				for (AiBomb bombReg : ai.getZone().getBombs()) 
				{		
					ai.checkInterruption();	
					if(ai.getZone().getBombs().size() < 10)
					{
						if(bombReg.getBlast().contains(tile) && duration > getRealDuration(bombReg,0))
						{									
							duration = getRealDuration(bombReg,0);
						}	
					}
					else
					{
						tempDuration = bombReg.getNormalDuration() - bombReg.getElapsedTime();							
						if(bombReg.getBlast().contains(tile) && duration > tempDuration)
						{									
							duration = tempDuration;
						}	
					}
				}
			}
			else
			{
				duration = 0;	
			}						
		}
		
		return duration;
	}

	/**
	 * Description : 
	 * 		Cette méthode calcule la valeur exacte du temps restant de l'explosion dans 
	 * 		la case donnée en paramètre.
	 * 
	 * @param bomb
	 * 		La bombe sélectionnée
	 * 
	 * @param index
	 *		Un entier qui représente l'index de la bombe donné par la fonction qui l'utilise 
	 *		pour ne pas avoir une exception de StackOverflow et pour ne pas recontroler 
	 *		recursivement les bombes qu'on avait déja controlés.
	 * 
	 * @return
	 * 		Retroune une valeur entier selon notre variable duration qui donne le temps de 
	 * 		l'explosion exacte qui reste d'une bombe. Parce que le temps varie si une bombe 
	 * 		est menacée par une autre bombe qui va explosée avant la bombe qu'on s'interesse.
	 */
	public long getRealDuration(AiBomb bomb,int index)
	{		
		ai.checkInterruption();
		long duration;
		duration = bomb.getNormalDuration() - bomb.getElapsedTime();		
		long buffer;
		
		for (AiBomb bombReg : ai.getZone().getBombs().subList(index, ai.getZone().getBombs().size())) 
		{			
			ai.checkInterruption();
			buffer = getRealDuration(bombReg,ai.getZone().getBombs().indexOf(bombReg)+1);
			
			if(bombReg.getBlast().contains(bomb.getTile()) && buffer < duration)
			{
				duration = buffer;
			}	
		}
	
	return duration;		
	}

	/** 
	 * Description : 
	 * 		Cette méthode fait une traitement des cases accessible
	 * 
	 * @param sourceTile
	 * 		Les cases a traiter
	 */
	public void processAccessibleTiles(AiTile sourceTile) 
	{
		ai.checkInterruption();		
		AiHero ownHero = ai.getZone().getOwnHero();		
		if (sourceTile.isCrossableBy(ownHero, false, false, true, false, true, true)) 
		{						
			accessibleTiles.add(sourceTile);								
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(ownHero) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.UP)))
			{
				processAccessibleTiles(sourceTile.getNeighbor(Direction.UP));
			}
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(ownHero) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.DOWN)))
			{
				processAccessibleTiles(sourceTile.getNeighbor(Direction.DOWN));
			}
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(ownHero) && !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.LEFT)))
			{
				processAccessibleTiles(sourceTile.getNeighbor(Direction.LEFT));
			}
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero)	&& !this.accessibleTiles.contains(sourceTile.getNeighbor(Direction.RIGHT)))
			{
				processAccessibleTiles(sourceTile.getNeighbor(Direction.RIGHT));
			}
		}	
	}

	/** 
	 * Description : 
	 * 		Cette méthode prend les cases accessibles et les met dans une liste d'arbre
	 * 
	 * @param sourceTile
	 * 		Les cases a traiter
	 * 
	 * @return 
	 * 		Renvoie la liste d'arbre
	 */
	public Set<AiTile> getAccessibleTiles(AiTile sourceTile)
	{
		ai.checkInterruption();
		return new TreeSet<AiTile>(accessibleTiles);
	}
		
	/** 
	 * Description : 
	 * 		Cette méthode trouve l'ennemi qui est la plus proche a nous sur la zone.
	 * 
	 * @param tile
	 * 		la case a traiter (la case ou notre agent se trouve)
	 * 
	 * @return
	 * 		Renvoie l'agent la plus proche
	 */
	public AiHero getClosestEnemy(AiTile tile)
	{
		ai.checkInterruption();
		AiHero closestEnemy = null;
		int minDistance = 1000;
		
		for(AiHero enemy : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			int distance = ai.getZone().getTileDistance(enemy.getTile(), tile);
			if(distance<minDistance)
			{
				closestEnemy = enemy;
				minDistance = distance;
			}
		}		
		return closestEnemy;
	}

	
	/**
	 * Description : 
	 * 		Cette méthode vérifie si un agent adversaire est accessible ou non
	 * 
	 * @return
	 * 		renvoie true si elle est accessible et false sinon.
	 */
	public boolean isEnemyReachable()
	{
		ai.checkInterruption();
		processAccessibleTiles(ai.getZone().getOwnHero().getTile());
		boolean result = false;
		
		for(AiTile tile : getAccessibleTiles(ai.getZone().getOwnHero().getTile()))
		{
			ai.checkInterruption();
			if(ifExistsAnyoneButUs(tile))
			{
				result = true;
			}
		}		
		return result;
	}

	
	/**
	 * Description :
	 * 		Cette méthode renvoie une valeur booléen. Elle regarde si les cases accessible
	 * 		par rapport a la case ou notre agent se trouve possede une bombe ou une flame.
	 * 
	 * @return
	 * 		Renvoie true si oui et false sinon.
	 */
	public boolean isDangerAround()
	{
		ai.checkInterruption();
		boolean result = false;
		
		for(AiTile tile : getAccessibleTiles(ai.getZone().getOwnHero().getTile()))
		{
			ai.checkInterruption();
			if(willBurn(tile) || !tile.getFires().isEmpty())
			{
				result = true;
				break;
			}
		}	
		return result;
	}


	/**
	 * Description :
	 * 		Cette méthode met les cases voisins de la case concérnée qui contient 
	 * 		ni des blocks ni des des bombes dans une liste
	 * 
	 * @param tile
	 * 		la case a traité
	 * 
	 * @return
	 * 		Renvoie la liste
	 */
	public ArrayList<AiTile> getFreeNeighbours(AiTile tile)
	{
		ai.checkInterruption();
		
		ArrayList<AiTile> result = new ArrayList<AiTile>();		
		
		for(AiTile neighbour : tile.getNeighbors())
		{		
			ai.checkInterruption();
			if (neighbour.getBlocks().isEmpty() && neighbour.getBombs().isEmpty())
			{
				result.add(neighbour);
			}		
		}
		return result;
	}


	/**
	 * Description : 
	 * 		Cette méthode permet de savoir si un ennemi est peut etre facilement tué. 
	 * 		C'est-à-dire, il regarde le niveau de disponibilité de la case de l'ennemi, 
	 * 		si c'est tres élevé il regarde s'il peut etre tuer si on pose une bombe.
	 * 
	 * @param tile
	 * 		la case a traiter
	 * 
	 * @param direction
	 * 		la direction
	 * 
	 * @param index
	 * 		une index de type integer
	 * 
	 * @return
	 * 		Renvoie true si oui et false sinon.
	 */
	public boolean isDeadEnd(AiTile tile,Direction direction,int index)
	{
		ai.checkInterruption();
		boolean result = false;		
		if(absoluteDeath(tile) || (ifExistsAnyoneButUs(tile) && willBurn(tile)))
		{
			result = true;
		}
		else if((surroundLevel(tile)>=2) && willBurn(tile))
		{
			for(AiTile neighbor : tile.getNeighbors())
			{
				ai.checkInterruption();
				if(ifExistsAnyoneButUs(neighbor))
				{
					result = true;
				}
			}
		}
		else if(!tile.getItems().isEmpty())
		{
			for(AiItem item : tile.getItems())
			{
				ai.checkInterruption();
				if(!item.getType().isBonus())
				{
					result = true;
				}
			}
		}
		
		return result;		
	}


	/**
	 * Description :
	 * 		Cette méthode fait une simulation : elle regarde si on aura un chemin apres 
	 * 		avoir posé une bombe sur une case.
	 * 
	 * @return
	 * 		Renvoie une valeur boléenne
	 */
	public boolean isBombingSafe()
	{
		ai.checkInterruption();
		boolean result = true;		
		AiHero ownHero = ai.getZone().getOwnHero();
		AiTile ownTile = ownHero.getTile();		
		AiSimZone simZone = new AiSimZone(ai.getZone());
		AiSimHero ownSimHero = simZone.getOwnHero();
		simZone.createBomb(ownTile,ownSimHero);		
		AiSimTile ownSimTile = ownSimHero.getTile();
		AiLocation location = new AiLocation(ownSimTile);	
	

		try {
			AiPath path = ai.moveHandler.getDijkstra().processEscapePath(location);
			
			if(path==null || path.getLength()<2)
			{
				result = false;
			}
			else
			{
				result = true;
			}
			
			
		} catch (LimitReachedException e) {
			result = false;
		}
		
		return result;
	}
	

	/**
	 * Description :
	 * 		Cette méthode trouve l'agent adversaire qui a une portée maximale de
	 * 		bombe et renvoie cette portée
	 * 
	 * @return
	 * 		Renvoie la portée maximale en case
	 */
	public int biggestFlame()
	{
		ai.checkInterruption();
		int result = 0;
		for(AiHero enemy : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			if(enemy.getBombRange() > result)
			{
				result = enemy.getBombRange();
			}
		}
		return result;
	}

	/**
	 * Description :
	 * 		Cette méthode augmente le cout des cases qui sont vérifie notre
	 * 		critere "isDeadEnd"
	 * 
	 * @return
	 * 		Renvoie les couts
	 */
	public double[][] processTileCosts()
	{
		ai.checkInterruption();
		double tileCosts[][] = new double[ai.getZone().getHeight()][ai.getZone().getWidth()];		
		for(AiTile tile : ai.getZone().getTiles())
		{
			ai.checkInterruption();
			if(tile.isCrossableBy(ai.getZone().getOwnHero()))
			{
				if(isDeadEnd(tile, Direction.NONE,biggestFlame()))
				{
					tileCosts[tile.getRow()][tile.getCol()] =  AiTile.getSize()*1000;					
				}
			}
		}			
		return tileCosts;
	}

	
	/**
	 * Description :
	 * 		Cette méthode permet a notre agent de ne pas se deplacer vers une case 
	 * 		en danger apres avoir posé une bombe grance a notre méthode "willBurn"
	 * 
	 * @param tile
	 * 		la case a traiter
	 * 
	 * @return
	 * 		Renvoie une valeur boléenne.
	 */
	public boolean absoluteDeath(AiTile tile)
	{
		ai.checkInterruption();
		boolean result = false;		
		if(willBurn(tile))
		{			
			if(getFreeNeighbours(tile).size() == 0)
			{
				return true;
			}
			else
			{
				boolean control = false;
				
				for(AiTile neighbor : tile.getNeighbors())
				{
					ai.checkInterruption();
					if(!willBurn(neighbor))
					{
						control = true;
					}
				}
				if(!control)
				{
					result = true;
				}				
			}
		}
		else
		{
			return false;
		}
		
		return result;
	}


}
