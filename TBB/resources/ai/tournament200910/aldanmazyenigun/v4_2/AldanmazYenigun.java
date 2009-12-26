package tournament200910.aldanmazyenigun.v4_2;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class AldanmazYenigun extends ArtificialIntelligence 
{
	private AiHero ownHero = null;
	
	private AiZone zone = null;
	
	private AiTile caseActuelle = null;
	
	private AvoidController escapeManager = null;
	
	private SafeStateController safeManager = null;
	
	private SafeStateController abstractSafeManager = null;
	
	private PutBombController putBombController =null;
	
	private AbstractBombController abstractBombController = null;
	
	//private HeroController heroController = null;
	
	private HeroController abstractHeroController = null;
	
	
	private AiBlock targetWall = null;
	
	private PathController wallTargetManager = null;
	
    private AiTile targetPreviousTile;
    
	private AiTile currentWallTile = null;
	
	private AiHero targetHero = null;
	


	
	/** classe charg�e de d�terminer quelles cases sont s�res */
	private SafetyZone safetyZone = null;
	
	private boolean thereIsSafeTile = true;
	
	private boolean bonusAccessible = true;
	
	private boolean heroAccessible = true;
	
	private boolean assezArmee = false;
	
	//private boolean isThereAnyHeroInBombeRange = false;
	
	private boolean hasArrivedButDanger = false;
	
	/** les coordonn�es de notre hero*/
	@SuppressWarnings("unused")
	private double x;
	
	@SuppressWarnings({ "unused"})
	private double y;
	
	private PathController targetManager = null;
	

	@Override
	public AiAction processAction() throws StopRequestException {
		
		checkInterruption(); //APPEL OBLIGATOIRE
		//int i,j;		
		
		if(ownHero == null)
			init();
	
		AiAction result = new AiAction(AiActionName.NONE);
		
		updateMatrix();
		
		
		/*
		for(i=0;i<zone.getHeigh();i++){
			for(j=0;j<zone.getWidth();j++){
				//if(zoneFormee.returnMatrix()[i][j] == 100000 || zoneFormee.returnMatrix()[i][j] == 0)
				//	System.out.print("");
				//else{
					System.out.printf("%1.2f ", safetyZone.getMatrix()[i][j]);				

					
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		*/
				
		boolean start =false;
		if(!ownHero.hasEnded())
		{	// on met � jour la position de l'ia dans la zone
			//init();
			
			
			updateLocation();
			Direction moveDir = Direction.NONE;
			findIsBonusAccessible();
			findIsHeroAccessible();

			if(ownHero.getBombNumber() >= 4 && ownHero.getBombRange() >= 5)
				assezArmee = true;
			
			// si on est en train de fuir : on continue
			if(escapeManager!=null)
			{	if(escapeManager.hasArrived()){
					escapeManager = null;
				}
				else
					moveDir = escapeManager.update();
			result = new AiAction(AiActionName.MOVE,moveDir);
			}			
			// sinon si on est en danger : on commence � fuir
			else if(!isSafe(caseActuelle))
			{	
			escapeManager = new AvoidController(this);
			moveDir = escapeManager.update();
			result = new AiAction(AiActionName.MOVE,moveDir);
			}
			else if(heroAccessible)
			{
				updateTarget();
				
				ArrayList<AiHero> heroess = new ArrayList<AiHero>(zone.getRemainingHeroes());
				heroess.remove(ownHero);
				for(AiHero h: heroess){
					checkInterruption();
					if(h.getCol() != ownHero.getCol()){
						result = new AiAction(AiActionName.MOVE,moveDir);
					}
					else{
						if(Math.abs(h.getLine() - ownHero.getLine()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE,moveDir);
					}
					
					if(h.getLine() != ownHero.getLine()){
						result = new AiAction(AiActionName.MOVE,moveDir);
					}
					else{
						if(Math.abs(h.getCol() - ownHero.getCol()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE,moveDir);
					}
				}
				
				if(targetHero!=null)
					moveDir = targetManager.update();
				
				if(targetManager.hasArrived())
					start = true;
				
				if(targetHero.getCol() != ownHero.getCol()){
					result = new AiAction(AiActionName.MOVE,moveDir);
				}
				else{
					if(Math.abs(targetHero.getLine() - ownHero.getLine()) <= 2)
						start = true;
					else
						result = new AiAction(AiActionName.MOVE,moveDir);
				}
				
				if(targetHero.getLine() != ownHero.getLine()){
					result = new AiAction(AiActionName.MOVE,moveDir);
				}
				else{
					if(Math.abs(targetHero.getCol() - ownHero.getCol()) <= 2)
						start = true;
					else
						result = new AiAction(AiActionName.MOVE,moveDir);
				}
				if((ownHero.getLine() == targetHero.getLine()) && (ownHero.getCol() == targetHero.getCol())){
					start = true;
				}				
				if(targetHero.hasEnded())
					chooseTarget();
			}
			else if(safeManager!=null)
			{
				if(safeManager.hasArrived()){
					safeManager=null;
				}
				else 
				{	
					moveDir =safeManager.update();
					
					if(ownHero.getTile().getNeighbor(moveDir)!=null)
					    if(!isSafe(ownHero.getTile().getNeighbor(moveDir)))
					        moveDir=Direction.NONE;
				}
				result = new AiAction(AiActionName.MOVE,moveDir);
				
			}
			else if(!isBonus(caseActuelle) && !findItemsTiles(caseActuelle).isEmpty() && bonusAccessible && !assezArmee)
			{
				safeManager = new SafeStateController(this);
				moveDir = safeManager.update();
				result = new AiAction(AiActionName.MOVE,moveDir);
			}
				
			else if(putBombController!=null)
			{
				chooseTarget();			
				
				if(putBombController.hasArrived()){
					putBombController=null;
					hasArrivedButDanger = false;
					start = true;
				}
				else 
				{	
					moveDir =putBombController.update();
					if(ownHero.getTile().getNeighbor(moveDir)!=null)
					if(!isSafe(ownHero.getTile().getNeighbor(moveDir)))
					moveDir=Direction.NONE;
				}
				result = new AiAction(AiActionName.MOVE,moveDir);
				
				
				if(targetHero.getCol() != ownHero.getCol()){
					result = new AiAction(AiActionName.MOVE,moveDir);
				}
				else{
					if(Math.abs(targetHero.getLine() - ownHero.getLine()) <= 2)
						start = true;
					else
						result = new AiAction(AiActionName.MOVE,moveDir);
				}
				
				if(targetHero.getLine() != ownHero.getLine()){
					result = new AiAction(AiActionName.MOVE,moveDir);
				}
				else{
					if(Math.abs(targetHero.getCol() - ownHero.getCol()) <= 2)
						start = true;
					else
						result = new AiAction(AiActionName.MOVE,moveDir);
				}
				if((ownHero.getLine() == targetHero.getLine()) && (ownHero.getCol() == targetHero.getCol())){
					System.out.println("aa");
					start = true;
				}
				
				
				
			}
			else if(!findWallTiles(caseActuelle).isEmpty())
			{
				chooseTarget();	
				putBombController = new PutBombController(this);
				moveDir = putBombController.update();
				result = new AiAction(AiActionName.MOVE,moveDir);
				//if(putBombController.hasArrived()){
					//start=true;
					//hasArrivedButDanger = false;
				//}
			}
			//else
				//start=true;
			
			if(start){
				if(abstractBombController!=null){
					  if(abstractBombController.hasArrived()){
					    	abstractBombController=null;
					  }
					  else
					  {
						System.out.println(abstractBombController.isThereSafeTiles());
						if(abstractBombController.isThereSafeTiles())
						{
							if(abstractBombController.getPath().getDuration(ownHero) <= 2300 && abstractBombController.getPath().getDuration(ownHero) != 0){
								if(heroAccessible)
								{
									if(ownHero.getBombCount()<3)
									    result = new AiAction(AiActionName.DROP_BOMB);
								}
								else{
									if(ownHero.getBombCount()<2)
										result = new AiAction(AiActionName.DROP_BOMB);
								}
							}
							else{
								hasArrivedButDanger = true;
								//findWallTiles(ownHero.getTile()).remove(ownHero.getTile());
								//putBombController = new PutBombController(this);
								//moveDir = putBombController.update();
								//result = new AiAction(AiActionName.MOVE,moveDir);
						}
						}
					  }
					  start = false;
				}
				else if(!findAbstractSafeTiles(caseActuelle).isEmpty())
				{	abstractBombController = new AbstractBombController(this);
					//moveDir = abstractBombController.update();
					//result = new AiAction(AiActionName.MOVE,moveDir);
					start = false;
				}
			}
			
			
		}
		// on met � jour la direction renvoy�e au moteur du jeu
		
		return result;
	}
	
	
	
	private void init() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE

		zone = getPercepts();
		ownHero = zone.getOwnHero();
	
		updateLocation();

		targetManager = new PathController(this,caseActuelle);
	}
	
	private void updateMatrix() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE

	    zone = getPercepts();		
		safetyZone = new SafetyZone(this);
	}
		
	private void updateLocation() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
				
		caseActuelle = ownHero.getTile();
		x = ownHero.getPosX();
		y = ownHero.getPosY();				
	}
	
	public double getDangerLevel(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return safetyZone.getDangerLevel(tile);		
	}
	
	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.isSafe(x, y))
					result.add(tile);
				
			}
		}		
		return result;
				
	}
	
	public List<AiTile> findAbstractSafeTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.abstractIsSafe(x, y))
					result.add(tile);
			}
		}		
		return result;
				
	}
	
	public boolean getThereIsSafeTile() throws StopRequestException{
		checkInterruption();
		return this.thereIsSafeTile;
	}
	
	
	public List<AiTile> findItemsTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.isBonus(x,y))
					result.add(tile);
			}
		}		
		return result;
	}
	
	public List<AiTile> findHerosTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=1;line<zone.getHeigh()-1;line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=1;col<zone.getWidth()-1;col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.isHero(x-1, y))
					result.add(tile);

				if(safetyZone.isHero(x+1, y))
					result.add(tile);

			    if(safetyZone.isHero(x, y-1))
					result.add(tile);

				if(safetyZone.isHero(x, y+1))
					result.add(tile);
				
				/*
				if(safetyZone.isHero(x-2, y))
					result.add(tile);

				if(safetyZone.isHero(x+2, y))
					result.add(tile);

			    if(safetyZone.isHero(x, y-2))
					result.add(tile);

				if(safetyZone.isHero(x, y+2))
					result.add(tile);
				*/
					
			}
		}

		return result;			
	}
	
	public List<AiTile> findWallTiles(AiTile origin) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		for(int line=1;line<zone.getHeigh()-1;line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=1;col<zone.getWidth()-1;col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.iswall(x-1, y))
					result.add(tile);

				else if(safetyZone.iswall(x+1, y))
					result.add(tile);

				else if(safetyZone.iswall(x, y-1))
					result.add(tile);

				else if(safetyZone.iswall(x, y+1))
					result.add(tile);
				
				if(hasArrivedButDanger && (tile == caseActuelle))
					result.remove(tile);
			}
		}

		return result;
				
	}
	
	
	/*
	public void isInBombeRange() throws StopRequestException{
		
		ArrayList<AiTile> heroTiles = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeigh();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line,col);
				int x = tile.getCol();
				int y = tile.getLine();
				
				if(safetyZone.isHero(x,y))
					heroTiles.add(tile);
			}
		}
		for(AiTile t: heroTiles){
			checkInterruption(); //APPEL OBLIGATOIRE
			if(t.getCol() == ownHero.getCol()){
				if(Math.abs(t.getLine() - ownHero.getLine()) < ownHero.getBombRange())
					isThereAnyHeroInBombeRange = true;
					
			}
			if(t.getLine() == ownHero.getLine()){
				if(Math.abs(t.getCol() - ownHero.getCol()) < ownHero.getBombRange())
					isThereAnyHeroInBombeRange = true;
					
			}
		}
	}
	*/
	
	
	
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return ownHero;
	}
	
	public AiZone getZone() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return zone;
	}
	
	public AiTile getActualTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return caseActuelle;
	}
	
	public SafetyZone getZoneFormee() throws StopRequestException
	{
		checkInterruption(); //APPEL OBLIGATOIRE
		return safetyZone;
	}
	
	private boolean isSafe(AiTile tile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		
		try {
			result = safetyZone.isSafe(tile.getCol(),tile.getLine());
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean isBonus(AiTile tile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		
		try {
			result = safetyZone.isBonus(tile.getCol(),tile.getLine());
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	@SuppressWarnings("unused")
	private boolean isWall(AiTile tile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		
		try {
			result = safetyZone.iswall(tile.getCol(),tile.getLine());
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private boolean isHero(AiTile tile) throws StopRequestException{
		checkInterruption();
		boolean result = false;
		
		try {
			result = safetyZone.isHero(tile.getCol(),tile.getLine());
		} catch (StopRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private void findIsBonusAccessible() throws StopRequestException{
		checkInterruption();
		
		Direction dir = Direction.NONE;
		
		if(abstractSafeManager!=null)
		{	
			if(abstractSafeManager.hasArrived()){
				abstractSafeManager=null;
			}
			else 
			{	
				dir = abstractSafeManager.update();
				if(ownHero.getTile().getNeighbor(dir)!=null)
				    if(!isSafe(ownHero.getTile().getNeighbor(dir)))
				        dir=Direction.NONE;
			}
		}
		else if(!isBonus(caseActuelle) && !findItemsTiles(caseActuelle).isEmpty() )
		{
			abstractSafeManager = new SafeStateController(this);
			dir = abstractSafeManager.update();
		}
	}
	
	private void findIsHeroAccessible() throws StopRequestException{
		checkInterruption();
		
		Direction dir = Direction.NONE;
		
		if(abstractHeroController!=null)
		{	
			if(abstractHeroController.hasArrived()){
				abstractHeroController=null;
			}
			else 
			{	
				dir = abstractHeroController.update();
				if(ownHero.getTile().getNeighbor(dir)!=null)
				    if(!isHero(ownHero.getTile().getNeighbor(dir)))
				        dir=Direction.NONE;
			}
		}
		else if(!findHerosTiles(caseActuelle).isEmpty() )
		{
			abstractHeroController = new HeroController(this);
			dir = abstractHeroController.update();
		}
	}
	
	

	private void chooseWallTarget() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
	
		targetWall = null;
		ArrayList<AiBlock> blocks = new ArrayList<AiBlock>(zone.getBlocks());
		if(!blocks.isEmpty())
		{	int index = (int)Math.random()*blocks.size();
			targetWall = blocks.get(index);
			//System.out.println(targetWall.getCol() + " " + targetWall.getLine());
		}
	}
	
	@SuppressWarnings("unused")
	private void updateWallTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		if(targetWall==null || targetWall.hasEnded())
		{	chooseWallTarget();
			if(targetWall!=null)
			{	AiTile targetCurrentTile = targetWall.getTile();
				wallTargetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		else
		{	AiTile targetCurrentTile = targetWall.getTile();
			if(targetCurrentTile==currentWallTile)
			{	double targetX = targetWall.getPosX();
				double targetY = targetWall.getPosY();
				wallTargetManager.setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	wallTargetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
			
		}
	}

	public void setAccessible(boolean accessible) {
		this.bonusAccessible = accessible;
	}

	public boolean isBonusAccessible() {
		return bonusAccessible;
	}
	
	public void setHeroAccessible(boolean accessible) {
		this.heroAccessible = accessible;
	}

	public boolean isHeroAccessible() {
		return heroAccessible;
	}
	
	/**
	 * choisit al�atoirement un joueur comme cible � suivre
	 */
	private void chooseTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		targetHero = null;
		ArrayList<AiHero> heroes = new ArrayList<AiHero>(zone.getRemainingHeroes());
		heroes.remove(ownHero);
		if(!heroes.isEmpty())
		{
			int index = (int)Math.random()*heroes.size();
			targetHero = heroes.get(index);
			
			int difY = Math.abs(ownHero.getLine() - targetHero.getLine());
			int difX = Math.abs(ownHero.getCol() - targetHero.getCol());
			int diY = 0;
			int diX = 0;
			int min = difX + difY;
			int sumDi = 0;
			for(AiHero h: heroes){
				checkInterruption();
				diY = Math.abs(ownHero.getLine() - h.getLine());
				diX = Math.abs(ownHero.getCol() - h.getCol());
				sumDi = diX + diY;
				AiHero temp = h;
				if(sumDi < min)
				{
					min = sumDi;
					targetHero = temp;
				}
			}
			
	
			/*
			int index = (int)Math.random()*heroes.size();
			targetHero = heroes.get(0);
			*/
			if(targetHero.hasEnded() || !heroAccessible){
				ArrayList<AiHero> heros = new ArrayList<AiHero>(zone.getRemainingHeroes());
				heros.remove(ownHero);
				targetHero = heros.get(index);
			}
		}
	}

	/**
	 * met � jour la cible, et �ventuellement le chemin jusqu'� elle
	 */
	private void updateTarget() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		if(targetHero==null || targetHero.hasEnded())
		{	chooseTarget();
			if(targetHero!=null)
			{	AiTile targetCurrentTile = targetHero.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		else
		{	AiTile targetCurrentTile = targetHero.getTile();
			if(targetCurrentTile==caseActuelle)
			{	double targetX = targetHero.getPosX();
				double targetY = targetHero.getPosY();
				targetManager.setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
			
		}
	}
	
	public AiHero getTargetHero() throws StopRequestException{
		checkInterruption();
		return targetHero;
	}
	
	

		
}
