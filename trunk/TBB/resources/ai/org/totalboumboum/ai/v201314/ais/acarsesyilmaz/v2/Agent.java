package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2;

import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
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
	{	checkInterruption();
	}
	
	/**
	 * Initialise autre choses que les percepts et gestionnaires si nécessaire
	 */
	@Override
	protected void initOthers()
	{	checkInterruption();
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * Initialise respectivement les percepts
	 */
	@Override
	protected void initPercepts()
	{	checkInterruption();
	}
	
	/**
	 * Met à jour respectivement les percepts
	 */
	@Override
	protected void updatePercepts()
	{	checkInterruption();
		
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;	
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
	{	checkInterruption();
		
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
	{	checkInterruption();
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
	{	checkInterruption();
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
	{	checkInterruption();
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
	{	checkInterruption();
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
		AiOutput output = getOutput();
		output.setTextSize(3);
		moveHandler.updateOutput();
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
			
	

	/**
	 * 
	 * Description : 
	 * 		Cette méthode calcule la distance de l'ennemi.
	 * 
	 * @param tile
	 * 	    La case sélectionnée
	 * 
	 * @return
	 * 	    Retroune une valeur booléen.
	 * 		true si la distance est inférieur ou égale a 2 cases
	 * 		false sinon
	 */
	public boolean isCloseToEnemy(AiTile tile)
	{		
		checkInterruption();
		boolean result = false;		
		for(AiHero enemy : getZone().getRemainingOpponents())
		{		
			checkInterruption();
			if(Math.pow(tile.getCol() - enemy.getCol(),2) <= 4 && Math.pow(tile.getRow() - enemy.getRow(),2) <= 4 && !result)
			{
				result = true;
			}
		}		
		
		return result;		
	}
	

	/**
	 * 
	 * Description : 
	 * 		Cette méthode calcule la distance de l'ennemi.
	 * 
	 * @param tile
	 * 	    La case sélectionnée
	 * 
	 * @param distance
	 * 	    Un distance un case
	 * 
	 * @return
	 * 	    Retroune une valeur booléen.
	 * 		true si la distance est inférieur ou égale a la distance donnée
	 * 		false sinon
	 */
	public boolean isCloseToUs(AiTile tile,int distance)
	{		
		checkInterruption();
		boolean result = false;		
		if(Math.pow(tile.getCol() - getZone().getOwnHero().getCol(),2) <= distance*distance && Math.pow(tile.getRow() - getZone().getOwnHero().getRow(),2) <= distance*distance && !result)
		{
				result = true;
		}				
		
		return result;		
	}
	

	/**
	 * Description : 
	 * 		Cette méthode fait des calcule par les types de bonus : bombe, flame et speed.
	 *  	Il donne la valeur obtenu avec la soustraction de la moyenne d'un type de bonus
	 *  	possédé par les ennemies avec le nombre de bonus de ce type qu'on possede en utilisant 
	 *  	la méthode "calculateInterest". "calculateInterest" renvoie donc une double array 
	 *  	de longueur 3.
	 *  
	 * @return
	 * 		renvoie un double array de longueur 3 dont
	 * 		result[0] pour le type bombe
	 * 		result[1] pour le type flame
	 * 		result[2] pour le type speed
	 * 	
	 */
	public double[] calculateInterest()
	{		
		checkInterruption();		
		
		double[] result = new double[3];

		int bombSum,flameSum,speedSum;
		bombSum = flameSum = speedSum = 0;
		double bombDifference,flameDifference,speedDifference;
		
		List<AiHero> ennemies =  getZone().getRemainingOpponents();
		
		if(ennemies.isEmpty())
		{
			result[0] = result[1] = result[2] = 0;
		}
		else
		{
			for(AiHero ennemi : ennemies)
			{		
				checkInterruption();
				bombSum += ennemi.getBombNumberLimit();
				speedSum += ennemi.getWalkingSpeed();
				flameSum += ennemi.getBombRange();			
			}	
			bombDifference =(double)(bombSum/ennemies.size())-getZone().getOwnHero().getBombNumberLimit();
			flameDifference =(double)(flameSum/ennemies.size())-getZone().getOwnHero().getBombRange();
			speedDifference =(double)(speedSum/ennemies.size())-getZone().getOwnHero().getWalkingSpeed();
			speedDifference = speedDifference / 75;

			result[0] = bombDifference;
			result[1] = flameDifference;
			result[2] = speedDifference;		
		}		
		return result;	}
		

	/**
	 * Description : 
	 * 		Cette méthode fait des calcule par les types de bonus : bombe, flame et speed.
	 *  	Il donne la valeur obtenu avec la soustraction de la moyenne d'un type de bonus
	 *  	possédé par les ennemies avec le nombre de bonus de ce type qu'on possede en utilisant 
	 *  	la méthode "calculateInterest". "calculateInterest" renvoie donc une double array 
	 *  	de longueur 3 et la cette méthode fait un classement par rapport au type qu'on est le
	 *  	plus intéressé avec un int array.
	 * 
	 * @return
	 * 		renvoie un int array de longueur 3
	 * 		difference[0] pour la calcule du type bombe
	 *  	difference[1] pour la calcule du type flame
	 *   	difference[2] pour la calcule du type speed
	 */
	public int[] interestOrder()
	{
		checkInterruption();		
		
		double[] difference = new double[3];
		
		difference = calculateInterest();
		
		int[] result = new int[3];	
		
		if( difference[0] > difference[1])
		{
			result[0]++;
		}
		else if( difference[0] != difference[1])
		{
			result[1]++;
		}
		if( difference[0] > difference[2])
		{
			result[0]++;
		}
		else if( difference[0]!=difference[2])
		{
			result[2]++;
		}
		if(difference[1] > difference[2])
		{
			result[1]++;
		}
		else if(difference[1]!=difference[2])
		{
			result[2]++;
		}	
		
		
		return result;
	}	
	

	/**
	 * Description : 
	 * 		Cette méthode vérifie s'il existe un golden item sur la case ou non
	 * 
	 * @param tile
	 * 		la case sélectionnée
	 * 
	 * @return
	 * 		renvoie une valeur booléen
	 * 		true s'il existe et false sinon
	 */
	public boolean isGoldenExists(AiTile tile)
	{		
		checkInterruption();
		boolean result;			
		
		if(tile.getItems().isEmpty())
		{
			result = false;
		}
		else
		{
			if(tile.getItems().get(0).getType() == AiItemType.GOLDEN_BOMB || tile.getItems().get(0).getType() == AiItemType.ANTI_FLAME || tile.getItems().get(0).getType() == AiItemType.ANTI_SPEED)
			{
				result = true;
			}	
			else
			{
				result = false;
			}
		}
		return result;			
	}

	
}

