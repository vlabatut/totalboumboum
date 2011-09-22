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
	private static int[][] map = new int[17][15];
	
	public static int getValue( int x , int y ){
		return map[x][y];
	}
	public static void setValue(int x, int y, int value){
		map[x][y]=value;
	}
	
	public static void init ( int[][] matrix){
		for ( int i = 0 ; i < 15 ; i++ ){
			for ( int j = 0 ; j < 17 ; j++ ){
				setValue(j,i,matrix[j][i]);
			}
		}
	}
	
	public static void printMap (){
//		System.out.println();
//		for ( int i = 0 ; i < 15 ; i++ ){
//			for ( int j = 0 ; j < 17 ; j++ ){
//				System.out.print( getValue(j, i) + " ");
//			}
//			System.out.println();
//		}
	}
	
	public static int[][] getMap() {
		return map;
	}

	//
	// constructor
	//
	public Map(){}
}
