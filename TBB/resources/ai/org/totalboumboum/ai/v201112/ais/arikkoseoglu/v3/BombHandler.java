package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<ArikKoseoglu>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected BombHandler(ArikKoseoglu ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
   	
    	//  à compléter
	}
	
	/**
	 * 
	 * @param ownHero
	 * @param gameArea
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	protected Boolean amIinDengeraous(AiHero ownHero, AiZone gameArea) throws StopRequestException{
		ai.checkInterruption();
		AiTile ownTile = ownHero.getTile();
		List<AiBomb> bombs = gameArea.getBombs();
		for(int i = 0 ; i< bombs.size(); i++){
			ai.checkInterruption();
			List<AiTile> scope = bombs.get(i).getBlast();
			if(scope.contains(ownTile)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metho calcul possibilite de fuir si on pose bomb 
	 * @param center 
	 * @param gameArea 
	 * @param hero 
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean possibleFuir(AiTile center, AiZone gameArea, AiHero hero) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		List<AiTile> selectedTiles = clearDuplicate(ai.utilityHandler.selectedTiles);
		// on ne teste pas la case de la cible, on la considère comme ok
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: Direction.getPrimaryValues())
		{	ai.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				if(neighbor.isCrossableBy(fire))
					result.add(neighbor);
				else
					blocked = true;
				i++;
			}
		}
		for(int i = 0 ; i<selectedTiles.size();i++){
			ai.checkInterruption();
			if(result.contains(selectedTiles.get(i)) || center.equals(selectedTiles.get(i))){
				selectedTiles.remove(i);
				i--;
			}
		}
		if(selectedTiles.size()>0){
			return true;
			/*if(isPathSecure(center,gameArea,hero,bombs)){
				return true;
			}*/
		}
		return false;
	}
	
	/**
	 * method supprimer les duplicates 
	 * @param tiles 
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 */
	private  List<AiTile> clearDuplicate(List<AiTile> tiles)throws StopRequestException {
		ai.checkInterruption();
		for(int i = 0 ; i<tiles.size();i++){
			ai.checkInterruption();
			AiTile temp = tiles.get(i);
			for(int j = i ; j< tiles.size() ; j++){
				ai.checkInterruption();
				if(temp.equals(tiles.get(j))){
					tiles.remove(j);
					j--;
				}
			}
		}
		return tiles;
	}
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		//  à compléter
	
		AiZone gameArea = ai.getZone();
		AiHero ownHero = gameArea.getOwnHero();
		AiTile hedef = ai.getMoveHandler().getCurrentDestination();
		
		if(possibleFuir(ownHero.getTile(), gameArea, ownHero)){
			//Collecte Mod
			if(ai.getModeHandler().getMode() == AiMode.COLLECTING){
				if(ownHero.getBombNumberMax()>0){
					//a.processValue(ownHero.getTile());
					if(hedef.equals(ownHero.getTile()) 
							&& gameArea.getTotalTime()>100
							&& !this.amIinDengeraous(ownHero,gameArea)
							&& ownHero.getBombNumberCurrent() <1){ //mauvais tactique
						
						return true;
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
			}
			else{//Attaque Mod
				if(ownHero.getBombNumberMax()>0){
					//a.processValue(ownHero.getTile());
					if(ai.utilityHandler.getPossibilityArriver()){
						if(hedef.equals(ownHero.getTile()) 
								&& !this.amIinDengeraous(ownHero,gameArea)
								&& gameArea.getTotalTime()>150){ 
							return true;
						}
						else{
							return false;
						}
					}else{
						if(hedef.equals(ownHero.getTile()) 
								&& !this.amIinDengeraous(ownHero,gameArea)
								&& gameArea.getTotalTime()>150
								&& ownHero.getBombNumberCurrent() <2){ //mauvais tactique
							return true;
						}
						else{
							return false;
						}
					}
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * @throws StopRequestException 
	 * 
	 */
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
