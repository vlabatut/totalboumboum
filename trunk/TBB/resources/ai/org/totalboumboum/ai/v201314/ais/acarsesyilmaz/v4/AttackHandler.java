package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe qui contient les méthodes concernant l'attaque de notre agent.
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
@SuppressWarnings("deprecation")
public class AttackHandler extends AiAbstractHandler<Agent>
{
	/**
	 * Appel à la classe AttackHandler
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public AttackHandler(Agent ai) 
	{ 
		super(ai); 
		ai.checkInterruption(); 
	}
	
	/**
	 * 
	 * Description : 
	 * 		Cette méthode regarde si à l’entourage d’une distance égale à la portée de la
	 * 		bombe de la case donnée en paramètre, il y a au moins une case qui contient un
	 * 		adversaire ou non. Pour cela, elle verifie pour chaque directions, tous les 
	 * 		cases qui sont à une distance inférieure ou égale à la portée de la bombe de 
	 * 		notre agent, mais en eliminant les cases qui suivent d'une direction s'il 
	 * 		recontre une case contenant un mur.
	 * 	 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur booléen selon notre variable "control" qui donne true a 
	 * 		l'existance d'un agent adversaire dans les cases voisins selon une distance 
	 * 		egale a la portée de la case donnée en parametre et false sinon.
	 * 		
	 */
	public boolean checkIfCanKill(AiTile tile)
	{
		ai.checkInterruption();
		boolean reachable;		
		AiTile checkTile;
		
		boolean control=false;
		
		for(Direction direction: Direction.getPrimaryValues())
		{	
			ai.checkInterruption();
			checkTile = tile.getNeighbor(direction);
			reachable = true;
			
			for(int i = 1;i<=ai.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
			{							
				ai.checkInterruption();		
				if(reachable)
				{
					if(!checkTile.getBlocks().isEmpty() || !checkTile.getBombs().isEmpty() || !checkTile.getItems().isEmpty())
					{
						reachable = false;
					}								
					else if(ai.securityHandler.ifExistsAnyoneButUs(checkTile))
					{
						control = true;
					}		
				}
			checkTile = checkTile.getNeighbor(direction);
			}
		}		
	
		return control;
	}		
	
	/**
	 * 
	 * Description:
	 * 		Cette méthode regarde s'il existe au moin un agent adversaire à l’entourage des 
	 * 		cases qui sont à distance égale à la portée de la bombe de la case donnée en 
	 * 		paramètre. Pour chaque direction, elle regarde les voisins et vérifie s'il y a 
	 * 		un agent adversaire, mais en eliminant les cases qui suivent d'une direction s'il 
	 * 		recontre une case contenant un mur.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur booléen selon notre variable "control" qui donne true a 
	 * 		l'existance d'un agent adversaire dans les cases voisins des cases qui sont 
	 * 		a une distance egale a la portée de la case donnée en parametre.		 
	 */
	public boolean checkIfCanLock(AiTile tile)
	{
		ai.checkInterruption();
		boolean reachable;		
		AiTile checkTile;		
		boolean control = false;	
		
		for(Direction direction: Direction.getPrimaryValues())
		{	
			ai.checkInterruption();
			checkTile = tile.getNeighbor(direction);
			reachable = true;
			
			for(int i = 1;i<=ai.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
			{							
				ai.checkInterruption();		
				if(reachable)
				{
					if(!checkTile.getBlocks().isEmpty() || !checkTile.getItems().isEmpty() || !checkTile.getBombs().isEmpty())
					{
						reachable = false;
					}								
					else
					{
						for(Direction sideDirection: Direction.getPrimaryValues())
						{	
							ai.checkInterruption();
							if(!Direction.areOpposite(direction, sideDirection)&& direction != sideDirection && ai.securityHandler.ifExistsAnyoneButUs(checkTile.getNeighbor(sideDirection)))
							{									
								control = true;
							}
						}
					}		
				}
			checkTile = checkTile.getNeighbor(direction);
			}
		}		
		
		return control;
	}
	
	/**
	 * 
	 * Description : 
	 * 		Cette méthode verifie les mêmes conditions que les méthode "can kill" et
	 * 		"can lock" pour la case donnée en paramètre, mais regarde aussi s'il y a
	 * 		aussi une bombe dans les cases voisins qui sont à distance égale à la 
	 * 		portée de la bombe de la case concernée.
	 * 
	 * @param tile
	 * 		La case sélectionnée
	 * 
	 * @return
	 * 		Retourne une valeur booléen selon notre variable "control" qui donne true
	 * 		a l'existance d'une bombe dans les cases voisins des cases qui sont a une 
	 * 		distance egale a la portée de la case donnée en paramètre.
	 */
	public boolean isTriangle(AiTile tile)
	{		
		ai.checkInterruption();
		boolean control = false;		
		AiTile checkTile;
		boolean reachable;
		boolean nearBomb = false;
		
		for(AiTile neighbour : tile.getNeighbors())
		{
			ai.checkInterruption();
			if(!neighbour.getBombs().isEmpty())
			{
				nearBomb = true;
			}
		}		
		
		if(!ai.securityHandler.ifExistsAnyoneButUs(tile) && !nearBomb)
		{			
			for(Direction direction: Direction.getPrimaryValues())
			{	
				ai.checkInterruption();			
				if(ai.securityHandler.willBurn(tile))
				{
				checkTile = tile.getNeighbor(direction);
				reachable = true;
				
					for(int i = 1;i<=ai.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
					{							
						ai.checkInterruption();		
						if(reachable)
						{
							if(!checkTile.getBlocks().isEmpty() || !checkTile.getItems().isEmpty() || !checkTile.getBombs().isEmpty())
							{
								reachable = false;
							}								
							else if((checkIfCanKill(checkTile) || checkIfCanLock(checkTile)))
							{
								control = true;
							}		
						}
					checkTile = checkTile.getNeighbor(direction);
					}
				}
			}
		}
		else
		{
			control = false;
		}
		
		return control;
	}

	
	/**
	 * Description :
	 * 		Cette méthode controle si on peut mettre une bombe ou non.
	 * 		C'est-à-dire elle regarde si on a encore de bombe.
	 * 
	 * @return
	 * 		La valeur booléen de ce methode.
	 * 		true si on peut et false sinon
	 */
	public boolean canBomb()
	{
		ai.checkInterruption();
		AiHero ownHero = ai.getZone().getOwnHero();
		return ownHero.getBombNumberMax()-ownHero.getBombNumberCurrent()>0;
	}

}
