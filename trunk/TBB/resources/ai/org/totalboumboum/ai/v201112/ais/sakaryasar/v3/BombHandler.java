package org.totalboumboum.ai.v201112.ais.sakaryasar.v3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class BombHandler extends AiBombHandler<SakarYasar>
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
	protected BombHandler(SakarYasar ai) throws StopRequestException
    {	super(ai);
    	ai.checkInterruption();
		
		verbose = false;
    	
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	ai.checkInterruption();
		boolean result = false;
		
		HashMap<AiTile,Float> hm = ai.utilityHandler.getUtilitiesByTile();
		
		//for collecting mode
		if(ai.modeHandler.getMode()==AiMode.COLLECTING){
			if(( hm.get(ownHero.getTile()) >=5)){
				if(isSafeToDropBomb(ownHero.getTile())){
					result =true;
				}
			}
		}
		//for attacking mode
		if(ai.modeHandler.getMode()==AiMode.ATTACKING){
			result = attackBombing();
		}	
		if(result && ownHero.getTile().getBombs().isEmpty()){
			ai.moveHandler.waitNextTerm = true;
		}
		return result;
	}

	//////////////////////////////////////////////////////////////////
	// Own Methods 		//////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	private AiZone zone = ai.getZone();
	private AiHero ownHero = zone.getOwnHero();
	/**
	 * method for checking safet of tiles in direction d 
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private boolean checkSafety(AiTile tile, Direction d) throws StopRequestException {
		ai.checkInterruption();

		boolean result = false;
		
		if ((d == Direction.LEFT || d == Direction.RIGHT)) {
			result = !hasBomb2(tile, Direction.UP) || !hasBomb2(tile, Direction.DOWN);
		} else {
			result = !hasBomb2(tile, Direction.LEFT) || !hasBomb2(tile, Direction.RIGHT);
		}
		
		return result;
	}
	/**
	 * method for checking safety of neighbors of a tile
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private boolean checkNeighbors(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		boolean cont = true;

		AiTile temp = tile.getNeighbor(d);		
		while(!result && temp.getBlocks().isEmpty() && temp.getHeroes().isEmpty() &&cont){	
			ai.checkInterruption();
			List <AiBomb> bombs = temp.getBombs();
			if(!bombs.isEmpty() ){
				cont = false;
			}
			result = checkSafety(temp, d);
			temp = temp.getNeighbor(d);
		}
		return result;
	}
    private static final Map<Direction, Direction> directionMap = new HashMap<Direction, Direction>();
    static {
    	directionMap.put(Direction.DOWNLEFT, Direction.DOWN);
    	directionMap.put(Direction.DOWNRIGHT, Direction.DOWN);
    	directionMap.put(Direction.UPLEFT, Direction.UP);
    	directionMap.put(Direction.UPRIGHT, Direction.UP);
    }

	/**
	 * method for testing if dropping a bomb is safe or not
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	private boolean isSafeToDropBomb(AiTile tile) throws StopRequestException{
		ai.checkInterruption();

		boolean result= false;
		
		result= checkNeighbors(tile, Direction.LEFT) || checkNeighbors(tile, Direction.RIGHT) 
				|| checkNeighbors(tile, Direction.UP) || checkNeighbors(tile, Direction.DOWN);

		return result;
	}
	/**
	 * check the d for possible bombs.
	 * @param tile
	 * @param d
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean hasBomb(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();

		boolean result = false;
		
		AiTile temp = tile;
		int i=0;
		temp=temp.getNeighbor(d);
		
		while(temp.getBlocks().isEmpty() && temp.getBombs().isEmpty()){
			ai.checkInterruption();

			temp=temp.getNeighbor(d);
			i++;
		}
		if(!temp.getBombs().isEmpty()){
//			if(temp.getBombs().get(0).getRange()>i-2){
				result = true;
//			}
		}
		if(i==0){
			result = true;
		}

		return result;
	}
	
	/**
	 * 
	 * @param tile
	 * @param d
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean hasBomb2(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();

		boolean result = false;
		
		AiTile n = tile.getNeighbor(d);
		if(!n.getBlocks().isEmpty()){
			result=true;
		}
		else{
			Float u = ai.utilityHandler.getUtilitiesByTile().get(n);
			if (u != null) {
				if (u == 0 ) {
					result = true;
				}
			}
			else{
				result = true;
			}
		}

		return result;
	}
	/**
	 * method for attacking strategies (naive)
	 * @return
	 * @throws StopRequestException
	 */
	private boolean attackBombing() throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		
		HashMap<AiTile,Float> hm = ai.utilityHandler.getUtilitiesByTile();
		
		Float heroUtility = hm.get(ownHero.getTile());
		
		if( /*heroUtility==5 ||*/ heroUtility==5 || heroUtility==7 ){
			if(ownHero.getBombNumberCurrent()==0){
				if(isSafeToDropBomb(ownHero.getTile())){
					result =true;
					print("a");
				}
			}
			else{
				result = false;
			}
		}
		else if( heroUtility==8 || heroUtility==9){
			if(ownHero.getBombNumberCurrent()==0){
				if(isSafeToDropBomb(ownHero.getTile())){
					result =true;
					print("b");
				}
			}
			else{
				result = false;
			}
		}
/*		else if( heroUtility==10 || heroUtility==11 || heroUtility==12){
			if(ownHero.getBombNumberMax()-ownHero.getBombNumberCurrent()>1){
				if(isSafeToDropBomb(ownHero.getTile())){
					result =true;
					print("c");
				}
			}
			else{
				result = false;
			}
		}
		*/
		else if( heroUtility==13 || heroUtility==14 || heroUtility==15){
			if(isSafeToDropBomb(ownHero.getTile()) && !ai.moveHandler.checkPathForEnemmies(ai.moveHandler.getCurrentPath())){
				result =true;
				print("d"+heroUtility+"tile :" +ownHero.getTile());
			}
			else{
				result = false;
			}
		}
		return result;
	}
	
	/**
	 * is there any enemy danger on the 4 principal directions. that method must be 
	 * improved because it is so naive
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isThereEnemyDanger() throws StopRequestException{
		ai.checkInterruption();
		
		boolean result = false;
		AiTile heroTile = ownHero.getTile();
		if((isThereAnyEnemyOnDirection(heroTile, Direction.LEFT)) != null){
			if(checkNeighbors(heroTile, Direction.LEFT)){
				result = false;
			}
		}
		else if( (isThereAnyEnemyOnDirection(heroTile, Direction.RIGHT)) != null){
			if(checkNeighbors(heroTile, Direction.LEFT)){
				result = false;
			}		
			else{
				result = true;
			}
		}
		else if( (isThereAnyEnemyOnDirection(heroTile, Direction.UP)) != null){
			if(checkNeighbors(heroTile, Direction.LEFT)){
				result = false;
			}
			else{
				result = true;
			}
		}
		else if( (isThereAnyEnemyOnDirection(heroTile, Direction.DOWN)) != null){
			if(checkNeighbors(heroTile, Direction.LEFT)){
				result = false;
			}
			else{
				result = true;
			}
		}
		else{
			result = false;
		}
		
		return result;
	}
	/**
	 * method for looking ennemies on 'd' direction
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private AiHero isThereAnyEnemyOnDirection(AiTile tile, Direction d) throws StopRequestException{
		ai.checkInterruption();
		AiHero result = null;
		AiTile temp = tile;
		Direction dtemp;
		if(directionMap.containsKey(d)){
			dtemp=directionMap.get(d);
		}
		else{
			dtemp =d;
		}
		
		while(temp.getBlocks().isEmpty() && temp.getBombs().isEmpty() && result == null){
			ai.checkInterruption();
			if(!temp.getHeroes().isEmpty()){
				result = temp.getHeroes().get(0);
			}
			temp = temp.getNeighbor(dtemp);
		}
		return result;
	}
		/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	
		ai.checkInterruption();
	}
}
