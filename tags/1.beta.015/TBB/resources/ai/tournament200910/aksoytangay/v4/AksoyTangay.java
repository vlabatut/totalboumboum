package tournament200910.aksoytangay.v4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;



import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * >> ce texte est � remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui d�finit son comportement.
 * n'h�sitez pas � d�composer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile �
 * d�bugger, modifier, relire, comprendre, etc.
 */
public class AksoyTangay extends ArtificialIntelligence
{	
	
	private Zone zone = null;
	
	private AiHero ownHero = null;
	
	public AiHero getOwnHero() {
		return ownHero;
	}


	
	public AiTile getOwnHeroTile() {
		return ownHeroTile;
	}



	private AiZone percepts = null;
	
	@SuppressWarnings("unused")
	private State stateMatrix[][] = null;
	
	private EscapeManager escapeManager = null;
	
	private PathManager pathManager = null;
	
	private BonusManager bonusManager = null;
	
	private StrategyManager strategyManager = null;
	
	private boolean keyBomb = true;
	
	private AiTile ownHeroTile;
	
	/** m�thode appel�e par le moteur du jeu pour obtenir une action d'IA */
	@Override
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
			
		if(ownHero == null)
			init();
		
		AiAction result = new AiAction(AiActionName.NONE);
		Direction direction = Direction.NONE;
			
		ownHeroTile = ownHero.getTile();
//		System.out.println("reel tile: "+ownHeroTile);
		
		//temppp
		List<AiBomb> bombs = percepts.getBombs();
		@SuppressWarnings("unused")
		AiBomb bom;
		Iterator<AiBomb> it = bombs.iterator();
		while(it.hasNext())
		{
			bom = it.next();
//			System.out.println("normal dur!!!!!!!! = "+bom.getNormalDuration());
		}
		
		
		//algo commence
		if(strategyManager!=null)
		{
			
			if(strategyManager.finishedTTypeEscape())
				strategyManager = null;
			else
				direction = strategyManager.typeDefiningAndGettingDirection();
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		else if(escapeManager!=null)
		{
			if(severalDangers(ownHeroTile))
			{	
				strategyManager = new StrategyManager(this, percepts, zone, pathManager, escapeManager);
				direction = strategyManager.typeDefiningAndGettingDirection();
			}
			else
			{
				if(escapeManager.finishedPath())
					escapeManager = null;
				else
					direction = escapeManager.getDirectionToEscape();
			}
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		else if(isDangerous(ownHeroTile))
		{
			escapeManager = new EscapeManager(this, percepts, pathManager);
			if(severalDangers(ownHeroTile))
			{
				strategyManager = new StrategyManager(this, percepts, zone, pathManager, escapeManager);
				direction = strategyManager.typeDefiningAndGettingDirection();
			}
			else
				direction = escapeManager.getDirectionToEscape();
			//system.out.println("direction : "+ direction.toString());
			
			result = new AiAction(AiActionName.MOVE, direction);
		}
		else
		{
			direction = bonusManager.getDirectionToCollectBonus();
			
			if(pathManager.temp == false)
			{
				//system.out.println("ilk");
				 
				if(keyBomb|| strategyManager.hasExplosed())
				{
					//system.out.println("iki");
					keyBomb = true;
					if(bonusManager.finishedPath())
					{
						//system.out.println("��!!");
						result = new AiAction(AiActionName.DROP_BOMB);
						//keyBomb = false;
					}
				}
				else if(!bonusManager.finishedPath())
					result = new AiAction(AiActionName.MOVE, direction);
			}
			else if(!bonusManager.finishedPath() || pathManager.temp == true)
				result = new AiAction(AiActionName.MOVE, direction);
		}
		
			
			
		
			
				
		return result;
		
	}
	
		
	/** methode qui teste si le case est en danger ou pas
	 * 
	 *  @param tile : AiTile
	 *  
	 *  @return la situation de danger de case
	 *  
	 *  @throws StopRequestException
	 */
	public boolean isDangerous(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		
		stateMatrix = zone.updateMatrix();
		State state = zone.getMatrix()[tile.getLine()][tile.getCol()];
		/*
		System.out.println("isdangereous.akstan");
		if(state==State.SURE)
			System.out.println("--sure--");
		else if(state==State.DANGER)
			System.out.println("--danger--" + stateMatrix[tile.getCol()][tile.getLine()]);
		*/
		if(state==State.DANGER)
		{
			//system.out.println("////NOW IN DANGER////////");
			return true;
		}
		
		else
			return false;
		
	}
	
	public boolean severalDangers(AiTile tile) throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		if(tile==null)
			return false;
		
		stateMatrix = zone.updateMatrix();
		State state = zone.getMatrix()[tile.getLine()][tile.getCol()];
		/*
		System.out.println("isdangereous.akstan");
		if(state==State.SURE)
			System.out.println("--sure--");
		else if(state==State.DANGER)
			System.out.println("--danger--" + stateMatrix[tile.getCol()][tile.getLine()]);
		*/
		if(state==State.SEVERALDANGERS)
		{
			//system.out.println("////NOW IN severalDANGERs////////");
			return true;
		}
		
		else
			return false;
		
	}
	
	private void init() throws StopRequestException
	{
		checkInterruption();
		
		percepts = getPercepts();
		ownHero = percepts.getOwnHero();
		zone = new Zone(percepts, this);
		stateMatrix = zone.getMatrix();
		
		pathManager = new PathManager(this, percepts);
		//escapeManager = new EscapeManager(this,percepts, pathManager);
		bonusManager = new BonusManager(this, percepts, pathManager);
		
		
		//System.out.println("init.akstan");
	}
	
	//silinecek!!
	
	@SuppressWarnings("unused")
	private ArrayList<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> neighbors = getPercepts().getTile(tile.getLine(), tile.getCol()).getNeighbors();
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isDangerous(t))
				result.add(t);
		}
		return result;
	}
	
	
}