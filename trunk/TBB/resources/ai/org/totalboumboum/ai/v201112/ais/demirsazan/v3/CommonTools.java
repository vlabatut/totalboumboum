package org.totalboumboum.ai.v201112.ais.demirsazan.v3;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe contient quelques méthodes
 * utilisées par les différents gestionnaires.
 * et ajoutée quelques methodes 
 * 
 * @author Serdil Demir
 * @author Gokhan Sazan
 */
@SuppressWarnings("deprecation")
public class CommonTools extends AiAbstractHandler<DemirSazan>
{	
	/**
	 * Initialise la classe avec l'IA
	 * passée en paramètre.
	 * 
	 * @param ai
	 * 		IA de référence.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public CommonTools(DemirSazan ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption();
		
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le personnage contrôlé par l'agent */
	public AiHero ownHero = null;
	/** La zone courante */
	public AiZone zone = null;
	/** La case occupée en ce moment */
	public AiTile currentTile = null;
	/** La vitesse de déplacement courante de l'agent */
	public double currentSpeed = 0; 
	
	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * On met à jour quelques variables.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		
		currentTile = ownHero.getTile();
		currentSpeed = ownHero.getWalkingSpeed();
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indentifie l'ensemble des cases situées au plus à un rayon
	 * donné d'une case centrale, et dans lesquelles on pourra 
	 * poser une bombe.
	 * 
	 * @param center
	 * 		La case centrale.
	 * @param hero
	 * 		Le personnage à considérer.
	 * @return 
	 * 		?
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public Set<AiTile> getTilesForRadius(AiTile center, AiHero hero) throws StopRequestException
	{	ai.checkInterruption();
		// init
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		
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
		
		return result;
	}

	/**
	 * Calcule combien de murs une bombe posée dans
	 * la case passée en paramètre menaçerait.
	 * 
	 * @param center
	 * 		Le centre de l'explosion à envisager.
	 * @return
	 * 		L'ensemble des cases contenant des murs touchés par l'explosion.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public Set<AiTile> getThreatenedSoftwallTiles(AiTile center) throws StopRequestException
	{	ai.checkInterruption();
		// init
		Set<AiTile> result = new TreeSet<AiTile>();
		AiHero ownHero = ai.getZone().getOwnHero();
		int range = ownHero.getBombRange();
		AiFire fire = ownHero.getBombPrototype().getFirePrototype();
		Direction[] direc = {Direction.DOWN,Direction.LEFT,Direction.RIGHT,Direction.UP};
		// on ne teste pas la case de la cible, on la considère comme ok
		
		// par contre, on teste celles situées à porté de bombes
		for(Direction d: direc)
		{	ai.checkInterruption();
			AiTile neighbor = center;
			int i = 1;
			boolean blocked = false;
			while(i<=range && !blocked)
			{	ai.checkInterruption();
				neighbor = neighbor.getNeighbor(d);
				blocked = !neighbor.isCrossableBy(fire);
				List<AiBlock> blocks = neighbor.getBlocks();
				if(!blocks.isEmpty())
				{	AiBlock block = blocks.get(0);
					// si le mur est destructible, on le met dans la liste
					if(block.isDestructible())
						result.add(neighbor);
				}
				i++;
			}
		}
		
		return result;
	}
	
	/**
	 * Détermine si la case passée en paramètre
	 * est menacée par une bombe à l'instant présent.
	 * 
	 * @param tile
	 * 		La case à considérer
	 * @return
	 * 		{@code true} ssi la case est à portée d'une bombe.
	 * 
	 * @throws StopRequestException 
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public boolean isTileThreatened(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();	
		
		long crossTime = Math.round(1000*tile.getSize()/currentSpeed);
		boolean result = false; 
		List<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while(!result && it.hasNext())
		{	ai.checkInterruption();	
			
			AiBomb bomb = it.next();
			long timeRemaining = bomb.getNormalDuration() - bomb.getTime();
			// on ne traite que les bombes menaçante : soit pas temporelles, soit
			// temporelles avec moins de temps restant que pour traverser une case
			if(!bomb.hasCountdownTrigger() || timeRemaining>crossTime)
			{	List<AiTile> blast = bomb.getBlast();
				result = blast.contains(tile);
			}
		}
		return result;
	}
	
	/**
	 * Return possibilite de cross de bomb
	 * @param tile
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean isPossibleCrossAdv(AiTile tile)throws StopRequestException
	{	ai.checkInterruption();	
		AiZone gameZone = ai.getZone();
		AiHero ownHero = gameZone.getOwnHero();
		int range = ownHero.getBombRange()
		, posX = tile.getCol()
		, posY = tile.getRow();
		List<AiHero> heros = gameZone.getHeroes();
		heros.remove(ownHero);
		if(heros!=null){
			for(int i = 0 ; i< heros.size(); i++){
				ai.checkInterruption();
				if((heros.get(i).getCol()>= posX-range || heros.get(i).getCol()<= posX+range)&& heros.get(i).getRow()== posY &&  tile.isCrossableBy(heros.get(i)) ){
					return true;
				}
				else if((heros.get(i).getRow()>= posY-range || heros.get(i).getRow()<= posY+range) && heros.get(i).getCol()== posX  &&  tile.isCrossableBy(heros.get(i))){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Return possibilite d'Hero
	 * @param ownHero
	 * @param gameArea
	 * @return
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
	 * Calculate possibilite de passer tile precedent 
	 * @param next
	 * @param gameArea
	 * @return
	 * @throws StopRequestException
	 */
	 
	protected AiBomb inDangeraous(AiLocation next, AiZone gameArea) throws StopRequestException{
		ai.checkInterruption();
		AiTile ownTile = next.getTile();
		List<AiBomb> bombs = gameArea.getBombs();
		for(int i = 0 ; i< bombs.size(); i++){
			ai.checkInterruption();
			List<AiTile> scope = bombs.get(i).getBlast();
			if(scope.contains(ownTile)){
				return bombs.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Calculer possibilite de fuir si on pose bombe
	 * @param center
	 * @param gameArea
	 * @param hero
	 * @return
	 * @throws StopRequestException
	 */
	protected boolean possibleFuir(AiTile center, AiZone gameArea, AiHero hero, List<AiTile> tilesSelected) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		List<AiTile> selectedTiles = supprimerDuplicate(tilesSelected);
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
		}
		return false;
	}
	
	/**
	 * Calculate Direction
	 * @param loc
	 * @return
	 * @throws StopRequestException
	 */
	Direction calculateDirection(AiLocation loc) throws StopRequestException{
		ai.checkInterruption();
		AiTile ownHero = ai.getZone().getOwnHero().getTile();
		double ownPosX = ownHero.getPosX()
				, ownPosY = ownHero.getPosY()
				, nextPosX = loc.getPosX()
				, nextPosY = loc.getPosY();
		if(nextPosX == ownPosX && nextPosY > ownPosY){
			return Direction.DOWN;
		}
		else if(nextPosX == ownPosX && nextPosY < ownPosY){
			return Direction.UP;
		}
		else if(nextPosX > ownPosX && nextPosY > ownPosY){
			return Direction.DOWNRIGHT;
		}
		else if(nextPosX > ownPosX && nextPosY < ownPosY){
			return Direction.UPRIGHT;
		}
		else if(nextPosX > ownPosX && nextPosY == ownPosY){
			return Direction.RIGHT;
		}
		else if(nextPosX < ownPosX && nextPosY > ownPosY){
			return Direction.DOWNLEFT;
		}
		else if(nextPosX < ownPosX && nextPosY < ownPosY){
			return Direction.UPLEFT;
		}
		else if(nextPosX < ownPosX && nextPosY == ownPosY){
			return Direction.LEFT;
		}
		return Direction.NONE;
	}
	
	/**
	 * Calculate possibilite de passer tile precedent 
	 * @param hero
	 * @param path
	 * @return
	 * @throws StopRequestException
	 */
	protected boolean possibleDePasser(AiHero hero,AiPath path) throws StopRequestException{
		ai.checkInterruption();
		
		List<AiLocation> locations = path.getLocations();
		if(locations != null){
			AiLocation heroLoc = new AiLocation(hero);
			int possitionHero = locations.indexOf(heroLoc);
			if(locations.size()> possitionHero){
				AiLocation nextLocation = locations.get(possitionHero+1);
				AiBomb bomb = inDangeraous(nextLocation, ai.getZone());
				if(bomb!=null){
					int timePass =(int)path.getDuration(hero),timeBomb = (int)(bomb.getNormalDuration() - bomb.getTime());
					if((timePass)*3 > timeBomb){ 
						return false;
					}
				}
				else{
					return true;
				}
			}
		}
		return true;
	} 
	
	/**
	 * Method find tiles possibles d'arriver
	 * @param result
	 * @param tile
	 * @throws StopRequestException 
	 */	
	protected void possibleTiles(List<AiTile> result,AiTile tile) throws StopRequestException
	{
		ai.checkInterruption();
		List<AiTile> neighbor = tile.getNeighbors();
		for(int i=0;i<neighbor.size();i++){
			ai.checkInterruption();
			AiTile n = neighbor.get(i);
			if(n.getBlocks().size() == 0 && n.getFires().size() ==0 && n.getBombs().size() ==0){
				if(!checkExisting(result,n)){
					result.add(n);
					this.possibleTiles(result, n);
				}
			}
		}
	}
	
	/**
	 * Conttroler uniquement
	 * @param result
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	protected boolean checkExisting(List<AiTile> result,AiTile tile)throws StopRequestException
	{
		ai.checkInterruption();
		for(int i=0;i<result.size();i++){
			ai.checkInterruption();
			if(tile.equals(result.get(i))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculer Safe Tiles
	 * @param tiles
	 * @throws StopRequestException
	 */
	protected void safeTiles(List<AiTile> tiles)throws StopRequestException
	{
		ai.checkInterruption();
		AiZone gameArea = ai.getZone();
		List<AiBomb> bombs = gameArea.getBombs();
		for(int k = 0 ; k < tiles.size(); k++){
			ai.checkInterruption();
			AiTile tile = tiles.get(k);
			for(int i = 0 ; i< bombs.size(); i++){
				ai.checkInterruption();
				List<AiTile> scope = bombs.get(i).getBlast();
				if(scope.contains(tile)){
					tiles.remove(tile);
					k--;
					break;
				}
			}
		}
	}
	
	/**
	 * CalculerPossibilite Fuir
	 * @param center
	 * @param gameArea
	 * @param hero
	 * @return
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private boolean isPossibleFuir(AiTile center, AiZone gameArea, AiHero hero) throws StopRequestException {
		ai.checkInterruption();
		Set<AiTile> result = new TreeSet<AiTile>();
		int range = hero.getBombRange();
		AiFire fire = hero.getBombPrototype().getFirePrototype();
		List<AiTile> selectedTiles = ai.utilityHandler.selectedTiles;
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
		}
		return false;
	}
	
	/**
	 * @param tiles
	 * @return
	 * @throws StopRequestException
	 */
	private  List<AiTile> supprimerDuplicate(List<AiTile> tiles)throws StopRequestException {
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
	
	/**
	 * @return
	 * @throws StopRequestException
	 */
	protected boolean isPossibleArriver() throws StopRequestException
	{	
		ai.checkInterruption();
		List<AiHero> heros = ai.getZone().getRemainingHeroes();
		heros.remove(ai.getZone().getOwnHero());
		for(int i = 0; i< heros.size();i++){
			ai.checkInterruption();
			if(ai.utilityHandler.selectedTiles.contains(heros.get(i).getTile())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return
	 * @throws StopRequestException
	 */
	protected boolean isPossibleRammaserBonus() throws StopRequestException
	{	
		ai.checkInterruption();
		List<AiItem> items = ai.getZone().getItems();
		for(int i = 0; i< items.size();i++){
			ai.checkInterruption();
			if(ai.utilityHandler.selectedTiles.contains(items.get(i).getTile())){
				return true;
			}
		}
		return false;
	}
}
