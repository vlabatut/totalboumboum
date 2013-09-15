package org.totalboumboum.ai.v200708.ais.keceryaman;

/**
 * Important notes: Our map size is:
 *  - x/j axis: 17 (colonnes)
 *  - y/i axis: 15 (lignes)
 *  ++ THIS CLASS WORKS FINE!
 * 
 * @author Serkan Keçer
 * @author Onur Yaman
 *
 */
public class Map {
	/** */
//	private static int[][] map = new int[17][15];
	private static int[][] map = null; //adjustement
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	public static int getValue( int x , int y ){
		if(x>=0 && x<map.length && y>=0 && y<map[0].length)
			return map[x][y];
		else
			return 0;
	}
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param value
	 * 		Description manquante !
	 */
	public static void setValue(int x, int y, int value){
		if(x>=0 && x<map.length && y>=0 && y<map[0].length)
			map[x][y]=value;
	}
	/**
	 * 
	 * @param matrix
	 * 		Description manquante !
	 */
	public static void init ( int[][] matrix){
		map = matrix.clone();
//		for ( int i = 0 ; i < 15 ; i++ ){
		for ( int i = 0 ; i < map[0].length ; i++ ){ //adjustment
//			for ( int j = 0 ; j < 17 ; j++ ){
			for ( int j = 0 ; j < map.length ; j++ ){ //adjustment
				setValue(j,i,matrix[j][i]);
			}
		}
	}
	/**
	 * 
	 */
	public static void printMap (){
//		System.out.println();
//		for ( int i = 0 ; i < 15 ; i++ ){
//			for ( int j = 0 ; j < 17 ; j++ ){
//				System.out.print( getValue(j, i) + " ");
//			}
//			System.out.println();
//		}
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public static int[][] getMap() {
		return map;
	}

	/**
	 * 		
	 */
	public Map(){}
}
