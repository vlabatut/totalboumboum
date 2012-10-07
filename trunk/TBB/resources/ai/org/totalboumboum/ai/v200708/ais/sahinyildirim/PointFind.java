package org.totalboumboum.ai.v200708.ais.sahinyildirim;

import java.awt.Point;

/**
 * Cette classe est pour construire une relation entre un point et son cout
 * donc on sait le cout d'un point et on sait si ce point dans la 
 * gameZone est un wallSoft
 * 
 * @author Serkan Şahin
 * @author Mehmet Yıldırım
 *
 */
public class PointFind {

	/** */
	private static final long serialVersionUID = 1L;
	/** le point du joueur */
	private Point rootPoint;
	/** */
	private int cost;
	/** */
	private boolean visited;
	/** */
	private boolean blockMure;
		
		
		/**
		 * @param pointDev le point
		 * @param cost le cout du point
		 * @param isMure vrai si ce point est wallSoft
		 */
		public PointFind(Point pointDev,int cost,boolean isMure)
		{
			this.rootPoint = pointDev;
			this.cost = cost;
			this.visited = false;
			this.blockMure = isMure;
		}
		
		
		/**
		 * @return le point
		 */
		public Point getRootPoint()
		{
			return rootPoint;
			
		}
		
		/**
		 * @return vrai si ce point est un wallSoft
		 */
		public boolean isPointMure()
		{
			return this.blockMure;
			
		}
		
		/**
 		 * @return le cout d'un point
		 */
		public int getCost()
		{
			return cost;
			
		}
		
		/**
		 * *
		 * @return
		 * 		? 
		 */
		public boolean isVisited()
		{
			return this.visited;
			
		}
		
		/**
		 * 
		 * @param value
		 */
		public void setNodeVisited(boolean value)
		{
			this.visited = value;
			
		}
		/**
		 * @return une texte representant les coordonnees d'un point et son cout
		 */
		public String nodeYazdir()
		{
			return "_X = "+rootPoint.x+ " _Y = "+rootPoint.y + " Cost = "+cost ;
			
			
		}
}
