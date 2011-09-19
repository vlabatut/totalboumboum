package org.totalboumboum.ai.v200910.ais.findiksirin.v4;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
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
		
		
		if(ownHero == null)
			init();
		// si le personnage n'est pas elimine
		if(!ownHero.hasEnded())
		{	// on met a jour la position de l'ia dans la zone
			updateLocation();

			Direction moveDir = Direction.NONE;
			
			// on met a jour le safetyManager
			safetyManager.update();
			
			if(dropBomb){
				dropBomb=false;		
			}
			
			//T'as un chemin pour se fuir?
			if(escapeManager!=null)
			{
				//T'es arrive à la fin du chemin de fuir?
				if(escapeManager.hasArrived())
				{
					//initialiser  le chemin du fuir
					escapeManager=null;
				}
				//T'es arrive à la fin du chemin de fuir?
				else
				{
					//continues sur le chemin de fuir
					moveDir = escapeManager.update();
				}
			}
			/*
			else if(safetyManager.hasNoWhereToGo(currentTile) && isSafe(currentTile))
			{
				escapeManager = new EscapeManager(this);
				moveDir=Direction.NONE;
			}
			*/
			//T'as aucun chemin pour se fuir et si t'es en danger
			else if(!safetyManager.isSafe(currentTile))
			{
				attackManager=null;
				bonusManager=null;
				escapeManager = new EscapeManager(this);
				moveDir = escapeManager.update();
			}
			
			//T'as aucun chemin pour se fuir et si tu n'es pas en danger
			else
			{
				//t'as un chemin d'attaque?
				if(attackManager!=null)
				{
					//t'es arrive sur le chemin de fuir?
					//if(attackManager.hasArrived())
					//{
						if(attackManager.decidedToBomb()){
								dropBomb=true;
								attackManager=null;
							}

						else
						{
							attackManager=null;
						}
					//}
					//
					//else
					//{
					//	moveDir= attackManager.update();
					//}

				}
				//tu n'as pas de chemin d'attaque?
				else
				{
					//t'as des assez bombes?
					if(ownHero.getBombNumber()>4 || noWall)
					{
						
						attackManager = new AttackManager(this);			
						//moveDir = attackManager.update();
					}
					//tu n'as pas d'assez bombes?
					else
					{
						//t'as un chemin de bonus?
						if(bonusManager!=null)
						{							
							//t'es arriver sur le chemin de bonus?
							if(bonusManager.hasArrived())
							{
								if(bonusManager.isOnBonusDestruction() && isOurBombExplosed() && !bonusManager.isBonusOver()){
									dropBomb=true;
									bombPos=currentTile;
									bonusManager=null;
								}
								else if(bonusManager.isBonusOver()){
								noWall=true;	
								}
								else
								bonusManager=null;
							}
							//tu n'es pas arriver sur le chemin de bonus?
							else
							{
								if(bonusManager.isPathSecure()){
								moveDir = bonusManager.update();
								}
								else
								{
									bonusManager=null;		
								}
							}
						}
						//tu n'as pas de chemin de bonus?
						else
						{
							bonusManager = new BonusManager(this);
							moveDir = bonusManager.update();
						}
					}
				}
			}
		
			if (dropBomb)
				result=new AiAction(AiActionName.DROP_BOMB);
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
	private AttackManager attackManager = null;
	
//
	private boolean dropBomb=false;
	//private List<AiTile> ourBombs=null;
	private AiTile bombPos=null;
	private boolean noWall=false;
	
	/*
	private void addBomb(AiTile tile) throws StopRequestException{
		checkInterruption();
		ourBombs.add(tile);
	}
	*/
	
	private boolean isOurBombExplosed() throws StopRequestException{
		checkInterruption();
		boolean resultat=true;
		if(bombPos!=null){
			List <AiBomb> b= bombPos.getBombs();
			if(b.isEmpty())
			{
				bombPos=null;
				resultat=true;
			}
			else
			{
				resultat=false;
			}
		}
		return resultat;
	}
	

	/*
	private boolean isOurAllBombsExplosed() throws StopRequestException{
		checkInterruption();
		boolean resultat=true;
		AiTile tempBomb=null;
		if(ourBombs!=null){
			if(!ourBombs.isEmpty()){
				Iterator <AiTile> b= ourBombs.iterator();
				while(b.hasNext()){
				checkInterruption();
				tempBomb=b.next();	
				if(tempBomb.getBombs().isEmpty())
					{
						ourBombs.remove(tempBomb);
					}
				}	
			}
			resultat=(ourBombs.isEmpty());
		}
		return resultat;
	}
	 */
	
	
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
	public AttackManager getAttackManager() throws StopRequestException
	{	checkInterruption();	
		return attackManager;		
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
	
	@SuppressWarnings("unused")
	private AiBomb getOurBomb(AiTile currentTile) throws StopRequestException{
		checkInterruption();
		AiBomb tempBomb=null;
		AiBomb resultat=null;
		List <AiBomb> bombs= zone.getBombs();
		Iterator <AiBomb> i = bombs.iterator();
		while(i.hasNext()){
			checkInterruption();
			tempBomb=i.next();
			if(tempBomb.getTile()==currentTile )
			{
				resultat=tempBomb;
			}
		}
		return resultat;
	}
	
}
