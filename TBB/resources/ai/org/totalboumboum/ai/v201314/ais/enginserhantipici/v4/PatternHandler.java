package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;
import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v4.Agent;

/**
 * This class receives Agent Class an enemy tile or it self by it's constructor methods
 * 
 * 		
 * 
 * if an enemy location corresponds to a pattern described in this class,
 * then it returns the coordinates of 3 tiles with tilesXY[] by parameter
 * first and second tiles are the corners, and the last one is the middle one
 * 
 * if pattern doesn't exist then the public boolean isPatternExist returns false, 
 * if it is exist then it returns true
 * 
 * There is two public variable;
 * 		"isThereATileNearByEnemy" returns Boolean value of existence of a tile near target enemy 
 * 		(if "isPatternExist" is false, isThereATileNearByEnemy returns always false too)
 * 		and "tileNearByEnemy" returns AiTile object which shows that case
 * 
 * 					!!!!!!!!!!!ATTENTION!!!!!!!!!! 
 * 		"tileNearByEnemy" should control by "isThereATileNearByEnemy"
 * 								and 
 * 			"tilesXY" should control by "isPatternExist"
 * 					For NOT HAVING NULL values!
 * 				!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 * 
 */

public class PatternHandler extends AiAbstractHandler<Agent>{	
	
	/**
	 * pattern[][] holds the templates of patterns
	 * last 3 element of every list shows the designated tiles for an attack strategy
	 * and 26th element shows the number the pattern
	 */
    	private static int[][] pattern = new int[][]{
    		{0,0,0,0,0,0,1,1,0,0,2,2,2,1,0,2,1,2,1,0,2,2,2,0,0,1,11,23,21},
    		{0,0,0,0,0,0,0,1,1,0,0,1,2,2,2,0,1,2,1,2,0,0,2,2,2,2,15,23,25},
    		{0,0,2,2,2,0,1,2,1,2,0,1,2,2,2,0,0,1,1,0,0,0,0,0,0,3,3,15,5},
    		{2,2,2,0,0,2,1,2,1,0,2,2,2,1,0,0,1,1,0,0,0,0,0,0,0,4,3,11,1},
    		{0,0,0,0,0,0,0,1,1,0,0,2,2,2,1,0,2,1,2,1,0,2,2,2,0,5,12,24,22},
    		{0,1,1,0,0,2,2,2,1,0,2,1,2,1,0,2,2,2,0,0,0,0,0,0,0,6,6,18,16},
    		{0,0,0,0,0,0,1,1,0,0,1,2,2,2,0,1,2,1,2,0,0,2,2,2,0,7,14,22,24},
    		{0,0,1,1,0,0,1,2,2,2,0,1,2,1,2,0,0,2,2,2,0,0,0,0,0,8,10,18,20},
    		{0,2,2,2,0,1,2,1,2,0,1,2,2,2,0,1,1,1,0,0,0,0,0,0,0,9,2,14,4},
    		{0,0,0,0,0,0,0,2,2,2,0,1,2,1,2,0,1,2,2,2,0,0,1,1,0,10,10,18,20},
    		{0,0,0,0,0,2,2,2,0,0,2,1,2,1,0,2,2,2,1,0,0,1,1,0,0,11,8,16,6},
    		{0,2,2,2,0,0,2,1,2,1,0,2,2,2,1,0,0,1,1,0,0,0,0,0,0,12,4,12,2},
    	};
    
    	/**
    	 * tilesXY holds the coordinates of the tiles to return, (x1,y1),(x2,y2),(x3,y4) with this order
    	 */
    	public int[] tilesXY = new int[6];
    	/**
    	 * isPatternExist is true if "testPattern" exist in "pattern" 
    	 */
    	public boolean isPatternExist;
    	/**
    	 * isThereATileNearByEnemy is true if the pattern exist and  if there is an enemy near a selected tile
    	 */
    	public boolean isThereATileNearByEnemy;
    	
    	/**
    	 * when "isThereATileNearByEnemy" is true, "tileNearByEnemy[]" holds the coordinates of the tile concerned
    	 */
    	public int[] tileNearByEnemy = new int[2];
    	
    	/**
    	 * if there is a hero inside of the trap (triangle attack)
    	 * holds the tile of this hero
    	 */
    	public AiTile tileOfHeroInsideTheTrap = null;
    	
    	/**
    	 * it holds the tiles of the pattern for a path
    	 * it has 5 values (0 - 5) corresponds the five tile outside of the trap
    	 * where all the action occurs
    	 */
    	public ArrayList<AiTile> pathArrayList = null; 
    	
    	/**
    	 * it holds the number of the pattern in the template list
    	 */
    	private int patternNo;
    	
    	/**
    	 * game zone
    	 */
    	private AiZone zone;

    	/**
    	 * Constructor tests the testTile as if its an enemy tile
    	 * (it can be use for self security)
    	 * @param ai Agent
    	 * @param testTile AiTile
    	 */
    	public PatternHandler(Agent ai, AiTile testTile){
    		super(ai);
    		ai.checkInterruption();
    		zone = ai.getZone();  
    		this.pathArrayList = new ArrayList<AiTile>();

    		tileOfHeroInsideTheTrap = testTile;
    		testPat(testTile);
    		
    		if(isPatternExist)
    			tileOfHeroInsideTheTrap = testTile;
    		
    	}
   	
    	/**
    	 *constructor class 
    	 * 
    	 * @param ai Agent
    	 * @param testHero AiHero, an enemy or own hero to check the pattern
    	 */
    	public PatternHandler(Agent ai,AiHero testHero){
    		super(ai);
    		ai.checkInterruption();
    		zone = ai.getZone();
  
    		this.pathArrayList = new ArrayList<AiTile>();

    		tileOfHeroInsideTheTrap = testHero.getTile();
    		testPat(testHero.getTile());
    		if(isPatternExist)
    			tileOfHeroInsideTheTrap = testHero.getTile();
    	}
    	
    	/**
    	 * test method for constructor methods
    	 * 
    	 * @param testTile AiTile
    	 */
    	public void testPat(final AiTile testTile){
    		ai.checkInterruption();
    		
    		int yAdv = testTile.getRow();
			int xAdv = testTile.getCol();
			int y = yAdv - 3;
			int[] myCurrentCliche = new int[25];
			int k = 0;
			
			for(int i = 0; i < 5; i++){
				ai.checkInterruption();
				y++;
				// after first 5 values of x determined, we restart x from the beginning of the next row
				int x = xAdv - 3;
				for(int j = 0; j < 5; j++){
					ai.checkInterruption();
					x++;
					myCurrentCliche[k] = 2;
					//if the current coordinate is outside of the zone then consider that element as a wall
					//it should be change when there is a passage at the zone!
					if(y < 0 || x < 0 || x + 1 > ai.getZone().getWidth() || y + 1 > ai.getZone().getHeight() ) {
						myCurrentCliche[k] = 1; 
						//when there is a zone with noborders we have to write a control here, for not having "array out bound" exception
					}else if (!ai.getZone().getTile(y,x).getBlocks().isEmpty()){
						myCurrentCliche[k] = 1; 
					}	
					k++;
				}
			}
			
			pattern(myCurrentCliche,xAdv,yAdv);
    	}
    	
    	
    	
    	/**
    	 * it was the old constructor method of this pattern class (version 1, version 2)
    	 * now it receives its parameters from new constructor 
    	 * 
    	 * @param testPattern the pattern which is going to test the existence within the template list of patterns
    	 * @param xAdv current colon of the tile of the selected enemy
    	 * @param yAdv current row of the tile of the selected enemy
    	 */
    	public void pattern(int[] testPattern, int xAdv, int yAdv){
    		ai.checkInterruption();
    		
    		isPatternExist = false;
    		isThereATileNearByEnemy = false;

    		if(testMat(testPattern)){
    			
    			isPatternExist = true;
    			
    			switch (patternNo){
    			
    			case 1: 
    			tilesXY[0] = xAdv - 2;
    			tilesXY[1] = yAdv;
    			tilesXY[2] = xAdv;
    			tilesXY[3] = yAdv + 2;
    			tilesXY[4] = xAdv - 2;
    			tilesXY[5] = yAdv + 2;
    			
    			pathArrayList.add(0,zone.getTile(yAdv, xAdv - 2));
    			pathArrayList.add(1,zone.getTile(yAdv + 1, xAdv - 2));
    			pathArrayList.add(2,zone.getTile(yAdv + 2, xAdv - 2));
    			pathArrayList.add(3,zone.getTile(yAdv + 2, xAdv - 1));
    			pathArrayList.add(4,zone.getTile(yAdv + 2, xAdv));
    	//		mapOfPathTiles.put(0, pathArrayList);
    				
    				break;
    			case 2: 
    				tilesXY[0] = xAdv + 2;
        			tilesXY[1] = yAdv;
        			tilesXY[2] = xAdv;
        			tilesXY[3] = yAdv + 2;
        			tilesXY[4] = xAdv + 2;
        			tilesXY[5] = yAdv + 2;
        		
        			pathArrayList.add(0,zone.getTile(tilesXY[3], tilesXY[2]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 2, tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 1, tileOfHeroInsideTheTrap.getCol()  + 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[1],tilesXY[0] ));
        
        			//		mapOfPathTiles.put(1, pathArrayList);
        				
    				break;
    			case 3:
    				tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv - 2;
        			tilesXY[2] = xAdv + 2;
        			tilesXY[3] = yAdv;
        			tilesXY[4] = xAdv + 2;
        			tilesXY[5] = yAdv - 2;
        			
        			pathArrayList.add(0,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2 , tileOfHeroInsideTheTrap.getCol()));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2, tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2, tileOfHeroInsideTheTrap.getCol() + 2));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1, tileOfHeroInsideTheTrap.getCol() + 2));
        			pathArrayList.add(4,zone.getTile(tileOfHeroInsideTheTrap.getRow(), tileOfHeroInsideTheTrap.getCol() + 2));
        			
        	//		mapOfPathTiles.put(2, pathArrayList);
					break;	
    			case 4:
    				tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv-2;
        			tilesXY[2] = xAdv-2;
        			tilesXY[3] = yAdv;
        			tilesXY[4] = xAdv - 2;
        			tilesXY[5] = yAdv - 2;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2, tileOfHeroInsideTheTrap.getCol() - 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1, tileOfHeroInsideTheTrap.getCol() - 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        	//		mapOfPathTiles.put(3, pathArrayList);
        			
        			break;
    			case 5:
    				tilesXY[0] = xAdv - 1;
        			tilesXY[1] = yAdv;
        			tilesXY[2] = xAdv + 1;
        			tilesXY[3] = yAdv + 2;
        			tilesXY[4] = xAdv - 1;
        			tilesXY[5] = yAdv + 2;
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv - 1, yAdv);
					
					pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
					pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 1, tileOfHeroInsideTheTrap.getCol() - 1  ));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 2 , tileOfHeroInsideTheTrap.getCol()));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
					
					
					break;
    			case 6:

        			tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv + 1;
        			tilesXY[2] = xAdv - 2;
        			tilesXY[3] = yAdv - 1;
        			tilesXY[4] = xAdv - 2;
        			tilesXY[5] = yAdv + 1;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 1, tileOfHeroInsideTheTrap.getCol() - 1 ));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() , tileOfHeroInsideTheTrap.getCol() - 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));

					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv, yAdv + 1);
					break;
    			case 7:
    				tilesXY[0] = xAdv + 1;
        			tilesXY[1] = yAdv;
        			tilesXY[2] = xAdv - 1;
        			tilesXY[3] = yAdv + 2;
        			tilesXY[4] = xAdv + 1;
        			tilesXY[5] = yAdv + 2;
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv + 1, yAdv);
					
					pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 1 , tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 2 , tileOfHeroInsideTheTrap.getCol()));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
					
        			break;
    			case 8:      			
    				tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv + 1;
    				tilesXY[2] = xAdv + 2;
        			tilesXY[3] = yAdv - 1;
        			tilesXY[4] = xAdv + 2;
        			tilesXY[5] = yAdv + 1;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() + 1 , tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow()  , tileOfHeroInsideTheTrap.getCol() + 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        			
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv, yAdv + 1);
					break;
    			case 9:

        			tilesXY[0] = xAdv + 1;
        			tilesXY[1] = yAdv;
        			tilesXY[2] = xAdv - 1;
        			tilesXY[3] = yAdv - 2;
        			tilesXY[4] = xAdv + 1;
        			tilesXY[5] = yAdv - 2;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1 , tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2  , tileOfHeroInsideTheTrap.getCol()));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        			
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv + 1, yAdv);
					break;
    			case 10:

        			tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv - 1;
        			tilesXY[2] = xAdv + 2;
        			tilesXY[3] = yAdv + 1;
        			tilesXY[4] = xAdv + 2;
        			tilesXY[5] = yAdv - 1;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1 , tileOfHeroInsideTheTrap.getCol() + 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow()  , tileOfHeroInsideTheTrap.getCol() + 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        			
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv, yAdv + 1);
					break;
    			case 11:
    				tilesXY[0] = xAdv;
        			tilesXY[1] = yAdv - 1;
        			tilesXY[2] = xAdv - 2;
        			tilesXY[3] = yAdv + 1;
        			tilesXY[4] = xAdv - 2;
        			tilesXY[5] = yAdv - 1;
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1 , tileOfHeroInsideTheTrap.getCol() - 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow()  , tileOfHeroInsideTheTrap.getCol() - 2));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        			
					isThereATileNearByEnemy = true;
	//				tileNearByEnemy = currentZone1.getTile(xAdv, yAdv - 1);
					break;
    			case 12:
    				tilesXY[0] = xAdv - 1;
        			tilesXY[1] = yAdv;
        			tilesXY[2] = xAdv + 1;
        			tilesXY[3] = yAdv - 2;
        			tilesXY[4] = xAdv - 1;
        			tilesXY[5] = yAdv - 2;	
        			
        			pathArrayList.add(0,zone.getTile(tilesXY[1], tilesXY[0]));
        			pathArrayList.add(1,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 1 , tileOfHeroInsideTheTrap.getCol() - 1));
        			pathArrayList.add(2,zone.getTile(tilesXY[5], tilesXY[4]));
        			pathArrayList.add(3,zone.getTile(tileOfHeroInsideTheTrap.getRow() - 2 , tileOfHeroInsideTheTrap.getCol()));
        			pathArrayList.add(4,zone.getTile(tilesXY[3], tilesXY[2]));
        			
					isThereATileNearByEnemy = true;
//					tileNearByEnemy = currentZone1.getTile(xAdv - 1, yAdv);
					break;
    			default: break;// if it comes here, it means there is a problem
    			}	//case
    		}//if testMat true
    	}
		
		/** testMat() tests "testPattern[]" with the patterns in "pattern[][]" 
		 * if the pattern exist in the matrix returns 1 else 0
		 * it also updates the variable "patternNo" with the number of the pattern if it exists
		 * 
		 * @param testPattern - array of a pattern 
		 * @return boolean - if the testPattern exist in the template list of patterns returns true
		 */ 	
		private boolean testMat(int[] testPattern){
			ai.checkInterruption();
			boolean result = false;
			
			//quick search
			for(int i = 0; i < 12 ; i++){
				ai.checkInterruption();
				for(int j = 0; j < 25; j++){
					ai.checkInterruption();
						patternNo = pattern[i][25];
						if ((testPattern[j] * pattern[i][j]) == 2) {
						result = false;
						break;
					}
					result = true;
					/*if we could come till here it means we already found a pattern in the template list
					* last 3 elements of pattern[][] (26 27 28) shows the tiles which are going to be selected
					*/		
				}//for j
				if(result == true) break; //once we found a patern, we need to break all the loops
			}//for i
		return result;		
		}
}

