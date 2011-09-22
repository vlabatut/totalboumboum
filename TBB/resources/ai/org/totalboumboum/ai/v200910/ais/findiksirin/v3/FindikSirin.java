package org.totalboumboum.ai.v200910.ais.findiksirin.v3;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Ali Fındık
 * @author Göknur Şırın
 */
public class FindikSirin extends ArtificialIntelligence
{		
	/** méthode appelée par le moteur du jeu pour obtenir une action de notre IA */
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		
		if(ownHero == null)
			init();
		// si le personnage n'est pas elimine
		if(!ownHero.hasEnded())
		{	// on met a jour la position de l'ia dans la zone
			updateLocation();
			Direction moveDir = Direction.NONE;
			
			// on met a jour le safetyManager
			safetyManager.update();
			
			// si on est en train de fuir, on continue
			if(escapeManager!=null)
			{	if(escapeManager.hasArrived())
				{
					escapeManager = null;
					moveDir=searchBonus();
				}
				else
					moveDir = escapeManager.update();
			}

			else
			{
				// sinon si on est en danger, on s'enfuit
				if(!safetyManager.isSafe(currentTile))
				{
					escapeManager = new EscapeManager(this);
					moveDir = escapeManager.update();
				}
				
				// si on n'est pas en danger
				else
				{
					moveDir=searchBonus();
				}
			}
			//si on va pose une bombe
			if(dropBomb){
				//pose un bombe
				result=new AiAction(AiActionName.DROP_BOMB);
				dropBomb=false;
			}
			else
				result=new AiAction(AiActionName.MOVE,moveDir);
		}
		return result;
	}
	
	//on gere le fait de collecter bonus 
	private Direction searchBonus() throws StopRequestException{
		checkInterruption();
		
		Direction moveDir = Direction.NONE;
		if(bonusManager!=null)
		{
			//si on est arrive
			if(bonusManager.hasArrived())
			{
				if(bonusManager.isOnBonusDestruction())
				{
					//on pose un bomb
					bonusManager=null;
					dropBomb=true;
				}
				else
				{
					bonusManager=null;
				}
			}
			else
				moveDir = bonusManager.update();	
		}
		else
		{
			bonusManager = new BonusManager(this);
			moveDir = bonusManager.update();
		}
		return moveDir;
	}

	//initialisation
	private void init() throws StopRequestException
	{	checkInterruption();

		zone = getPercepts();
		ownHero = zone.getOwnHero();
		updateLocation();
		safetyManager = new SafetyManager(this);
	}

//notre personnage et son acces
	private AiHero ownHero = null;
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption();
		return ownHero;
	}

// la zone de jeu
	private AiZone zone = null;
	public AiZone getZone() throws StopRequestException
	{	checkInterruption();	
		return zone;
	}
	
///LES DEFINITIONS 
	private EscapeManager escapeManager = null;
	private SafetyManager safetyManager = null;
	private BonusManager bonusManager = null;
	
//
	private boolean dropBomb=false;
	
	
//LES METHODES D'ACCES DES GESTIONNAIRES DE TRAITEMENT
	public SafetyManager getSafetyManager() throws StopRequestException
	{	checkInterruption();	
		return safetyManager;		
	}
	public EscapeManager getEscapeManager() throws StopRequestException
	{	checkInterruption();	
		return escapeManager;		
	}
	public BonusManager getBonusManager() throws StopRequestException
	{	checkInterruption();	
		return bonusManager;		
	}

//LES METHODES DE CONTROLE DE SECURITE
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	checkInterruption();	
		return safetyManager.getSafetyLevel(tile);		
	}	
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
			
		return safetyManager.isSafe(tile);
	}
	
	
//CASE ET POSITION OCCUPE (current tile and position)
	private AiTile currentTile = null;
	private double currentX;
	private double currentY;
	
	public AiTile getCurrentTile() throws StopRequestException
	{	checkInterruption();
		return currentTile;
	}	

	public double getCurrentX() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentX;
	}
	public double getCurrentY() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE	
		return currentY;
	}

	private void updateLocation() throws StopRequestException
	{	checkInterruption();
		currentTile = ownHero.getTile();
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();				
	}
	
}
