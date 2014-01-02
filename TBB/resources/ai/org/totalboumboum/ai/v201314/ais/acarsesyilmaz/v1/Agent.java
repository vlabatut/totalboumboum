package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v1;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Classe principale de notre agent
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	
		checkInterruption();
		
		// active/désactive la sortie texte
		verbose = false;
	}
	
	/**
	 * Initialise autre choses que les percepts et gestionnaires si nécessaire
	 */
	@Override
	protected void initOthers()
	{	
		checkInterruption();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initialise respectivement les percepts
	 */
	@Override
	protected void initPercepts()
	{	
		checkInterruption();		
	}
	
	/**
	 * Met à jour respectivement les percepts
	 */
	@Override
	protected void updatePercepts()
	{	
		checkInterruption();		
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	/**
	 * Initialise respectivement les gestionnaires
	 */
	@Override
	protected void initHandlers()
	{	
		checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
	}

	/**
	 * Renvoie le gestionnaire de préférence de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de mode de cet agent.
	 */
	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	
		checkInterruption();
		return modeHandler;
	}

	/**
	 * Renvoie le gestionnaire de posage de bombe de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de préférence de cet agent.
	 */
	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	
		checkInterruption();
		return preferenceHandler;
	}

	/**
	 * Renvoie le gestionnaire de déplacement de cet agent.
	 * 
	 * @return
	 * 		Le gestionnaire de posage de bombe de cet agent.
	 */
	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	
		checkInterruption();
		return bombHandler;
	}

	/**
	 * Indique si l'agent a déjà été initialisé ou pas
	 * 
	 * @return
	 * 		Le gestionnaire de déplacement de cet agent.
	 */
	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	
		checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	@Override
	protected void updateOutput()
	{	
		checkInterruption();

		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();		
	}

	
	/**
	 * 
	 * Description : 
	 * 		Cette méthode calcule le niveau de disponibilité d'une case donnée en paramètre. 
	 * 		Il regarde pour chaque case voisin, s'il y a un mur, une bombe, une explosion ou
	 * 		bien une future explosion, respectivement par les méthodes "getBlocks", "getBombs",
	 * 		"getFires" et "WillBurn". D'où le niveau de disponibilité diminue ou agmente au
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
	public int checkIfSurrounded(AiTile tile)
	{				
		checkInterruption();
		int result = 4;
		
		for (AiTile neighborsTile : tile.getNeighbors())
		{
			checkInterruption();
			
			if (neighborsTile.getBlocks().isEmpty() && neighborsTile.getBombs().isEmpty() && neighborsTile.getFires().isEmpty() && !willBurn(neighborsTile))
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
		checkInterruption();
		boolean control = false;
		
		for (AiBomb bombReg : getZone().getBombs()) {		
			checkInterruption();			
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
		checkInterruption();
		boolean reachable;		
		AiTile checkTile;
		
		boolean control=false;
		
		if(ifExistsAnyoneButUs(tile))
		{
			control = true;
		}
		else
		{
			for(Direction direction: Direction.getPrimaryValues())
			{	
				checkInterruption();
				checkTile = tile.getNeighbor(direction);
				reachable = true;
				
				for(int i = 1;i<=this.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
				{							
					checkInterruption();		
					if(reachable)
					{
						if(!checkTile.getBlocks().isEmpty())
						{
							reachable = false;
						}								
						else if(ifExistsAnyoneButUs(checkTile))
						{
							control = true;
						}		
					}
				checkTile = checkTile.getNeighbor(direction);
				}
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
		checkInterruption();
		boolean reachable;		
		AiTile checkTile;		
		boolean control = false;	
		
		for(Direction direction: Direction.getPrimaryValues())
		{	
			checkInterruption();
			checkTile = tile.getNeighbor(direction);
			reachable = true;
			
			for(int i = 1;i<=this.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
			{							
				checkInterruption();		
				if(reachable)
				{
					if(!checkTile.getBlocks().isEmpty())
					{
						reachable = false;
					}								
					else
					{
						for(Direction sideDirection: Direction.getPrimaryValues())
						{	
							checkInterruption();
							if(!Direction.areOpposite(direction, sideDirection)&& direction != sideDirection&&ifExistsAnyoneButUs(checkTile.getNeighbor(sideDirection)))
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
		checkInterruption();
		boolean control=false;
		
		if((tile.getHeroes().contains(this.getZone().getOwnHero()) && tile.getHeroes().size() > 1) || (!tile.getHeroes().contains(this.getZone().getOwnHero()) && tile.getHeroes().size() != 0))
		{
			control = true;
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
		
		checkInterruption();
		boolean control = false;		
		AiTile checkTile;
		boolean reachable;
		
		for(Direction direction: Direction.getPrimaryValues())
		{	
			checkInterruption();			
			if(willBurn(tile))
			{
			checkTile = tile.getNeighbor(direction);
			reachable = true;
			
			for(int i = 1;i<=this.getZone().getOwnHero().getBombRange() && !control && reachable; i++)
			{							
				checkInterruption();		
				if(reachable)
				{
					if(!checkTile.getBlocks().isEmpty())
					{
						reachable = false;
					}								
					else if((checkIfCanKill(checkTile) || checkIfCanLock(checkTile)) && checkTile.getBombs().isEmpty())
					{
						control = true;
					}		
				}
			checkTile = checkTile.getNeighbor(direction);
			}
			}
		}					
		
		return control;
	}


	/**
	 * 
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
		checkInterruption();
		long duration = 10000;
		
		for (AiBomb bombReg : getZone().getBombs()) {		
			checkInterruption();
			
			if(bombReg.getBlast().contains(tile) && duration > getRealDuration(bombReg,0))
			{
				duration = getRealDuration(bombReg,0);
			}			
		}		
		return duration;
	}
	
	
	/**
	 * 
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
		checkInterruption();
		long duration = bomb.getNormalDuration() - bomb.getElapsedTime();		
		long buffer;
		
		for (AiBomb bombReg : getZone().getBombs().subList(index, getZone().getBombs().size())) 
		{			
			checkInterruption();
			buffer = getRealDuration(bombReg,getZone().getBombs().indexOf(bombReg)+1);
			
			if(bombReg.getBlast().contains(bomb.getTile()) && buffer < duration)
			{
				duration = buffer;
			}	
		}
		return duration;		
	}
			
}
