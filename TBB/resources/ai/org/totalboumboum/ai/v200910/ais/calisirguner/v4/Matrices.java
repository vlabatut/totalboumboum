package org.totalboumboum.ai.v200910.ais.calisirguner.v4;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;

/**
 * 
 * @version 4
 * 
 * @author Emre Çalışır
 * @author Burak Ozgen Güner
 *
 */
public class Matrices {
	
	private Etat accessibilite[][];
	private int risque[][];
	private Etat etat;
	
	
	

public Matrices(Map map){
	try {
		remplir(map);
	} catch (StopRequestException e) {
		// 
		e.printStackTrace();
	}
	
	
}
	@SuppressWarnings("static-access")
	private void remplir(Map map) throws StopRequestException{
		for (int i=0;i<map.width;i++){
			for (int j=0;i<map.height;j++){
				if (map.returnMatrix()[i][j]==etat.DESTRUCTIBLES ||
						map.returnMatrix()[i][j]==etat.INDESTRUCTIBLES 
						|| map.returnMatrix()[i][j]==etat.FEU
						|| map.returnMatrix()[i][j]==etat.BOMBE)
				accessibilite[i][j]=etat.INACCESSIBLE;
				
				else 
				{accessibilite[i][j]=etat.ACCESSIBLE;
				if (map.returnMatrix()[i][j]==etat.POINT)
					risque[i][j]=-10;
				else if (map.returnMatrix()[i][j]==etat.ADVERSAIRE)
					risque[i][j]=-5;
				
				else if (map.returnMatrix()[i][j]==etat.LIBRE)
					risque[i][j]=0;
			
				
					
					
					
					
				}}}}
		int return_risque (int x,int y){
			return risque[x][y];}
		
					
					
					
					
					
				
					
				
					
					
					
					
				
				
				
				
				
			
			
		
		
		
		
		
	
	
	
}
