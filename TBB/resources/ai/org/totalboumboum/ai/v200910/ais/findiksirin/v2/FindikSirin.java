package org.totalboumboum.ai.v200910.ais.findiksirin.v2;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class FindikSirin extends ArtificialIntelligence
{		
	/** méthode appelée par le moteur du jeu pour obtenir une action de notre IA */
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		boolean bomb=false;
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
					escapeManager = null;
				else
					moveDir = escapeManager.update();
			}
			
			// sinon si on est en danger, on s'enfuit
			else if(!safetyManager.isSafe(currentTile))
			{	escapeManager = new EscapeManager(this);
				moveDir = escapeManager.update();
			}
			// si on est en train de collecter un bonus, on continue
			else if(bonusManager!=null)
			{
				//si il n'y a pas un bonus exploré, on suivit au chemin a cote du mur destructible le plus proche
				if(bonusManager.noBonus()){
					//si on est arrive, on pose une bombe
					if(bonusManager.hasArrived())
					{
						bomb=true;
						bonusManager=null;
						escapeManager=null;
					}
					else
						moveDir = bonusManager.update();
				}
				else
				{
					if(bonusManager.hasArrived())
						bonusManager=null;
					else
						moveDir = bonusManager.update();	
				}
			}
			else if(bonusManager==null)
			{
				bonusManager = new BonusManager(this);
				moveDir = bonusManager.update();
			}
			//si on va pose une bombe
			if(bomb){
				//pose un bombe
				result=new AiAction(AiActionName.DROP_BOMB);
				//si on n'a pas un chemin a suivre
				if(escapeManager==null)
				{
					//on a posé un bombe, on actualise safetyManager
					safetyManager.update();
					//on obtient un nouveau chemin, pour s'enfuir de notre bombe
					escapeManager = new EscapeManager(this);
					escapeManager.update();
				}
				else
				{
					//on actualise safetyManager
					safetyManager.update();
					//on actualise escapeManager
					escapeManager.update();
				}
			}
			else
				result=new AiAction(AiActionName.MOVE,moveDir);
		}
		return result;
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
