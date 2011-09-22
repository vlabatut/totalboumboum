package org.totalboumboum.ai.v200708.ais.sahinyildirim;

import java.awt.Point;
import java.util.Vector;

/** 
 * cette classe  est  pour  construire une relation  pere-> child
 *  
 * cette classe prendre un pointFind dans le constructor qui nous sert a trouver
 * les fils d'un point 
 * 
 * @author Serkan Şahin
 * @author Mehmet Yıldırım
 *
 */
public class ParentChild {
	
	
	private PointFind parent;
	

	//vector pour deposer les fils de ce point
	private Vector<PointFind> child;
	
	
	/**Constructor
	 * @param PointFind parent le point pere
	 */
	public ParentChild(PointFind parent)
	{
		this.parent = parent;
		
		child = new Vector<PointFind>();
		
	}
	
	/**
	 * Methode  pour  ajouter les fils dans un vector d'un point pere
	 * @param PointFind point
	 */
	public void addChild(PointFind point)
	{
		
		child.add(point);
	}
	

	
	/**
	 * 
	 * @return le pointFind pere d'un point
	 */
	public PointFind getPere()
	{
		return this.parent;
		
	}
	
	/**
	 * @return le point d'un  point pereChild
	 */
	public Point getParentPoint()
	{
		return this.parent.getRootPoint();
		
	}
	
	/**
	 * @return le vector contenant les fils d'un point pere
	 */
	public Vector<PointFind> getChildVector()
	{
		return this.child;
		
	}
	
	/**
	 * @return vrai si ce point a des fils
	 */
	public boolean isPere()
	{
		boolean result = false;
		
		if(this.child.size() == 0)
			result = false;
		else if(this.child.size()>0)
			result = true;
		
		return result;
		
		
	}

}
