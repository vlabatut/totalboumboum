package tournament200910.demirciduzokergok.v4_2;


import java.util.List;




import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;

import fr.free.totalboumboum.ai.adapter200910.data.AiHero;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;



import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * >> ce texte est � remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui d�finit son comportement.
 * n'h�sitez pas � d�composer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile �
 * d�bugger, modifier, relire, comprendre, etc.
 */
public class DemirciDuzokErgok extends ArtificialIntelligence
{ 

private AiZone IA_ZONE;

private AiHero our_bomberman;

private AiTile our_tile;
private int q=0;
private int q_test=0;
private int our_x;
private int our_y;
private Safety_Map safe_map;
//private int for_bonus=2;





int numb_bonus=0;


List <AiTile> possibleDest_w;
List <AiTile>  possible_Dest_bonus;



Direction moveDir;

private Bonus_Manager bonusmanager=null;
private Escape_Manager escapemanager=null;
private Wall_Manager wallmanager=null;
private Enemie_Manager enemiemanager=null;
private Can_escape_Manager canescapemanager=null;

private boolean test=false;

AiPath path_b;


 
 /* m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */
 public AiAction processAction() throws StopRequestException
 { 
  checkInterruption(); //APPEL OBLIGATOIRE
  
	  IA_ZONE=getPercepts();
	 
	 
	  our_bomberman=IA_ZONE.getOwnHero();
	  our_tile=our_bomberman.getTile();
	  
	  our_x=our_tile.getCol();
	  our_y=our_tile.getLine();
	  
	  AiAction result = new AiAction(AiActionName.NONE);
  
	//  safe_map=new Safety_Map(IA_ZONE);
	//System.out.println(compter);
	/*
	  safe_map=new Safety_Map(IA_ZONE);
	for(int q1=0;q1<IA_ZONE.getHeigh();q1++){
		  for(int q2=0;q2<IA_ZONE.getWidth();q2++){
			  System.out.println(q1+ ":"+q2+"  " + safe_map.returnMatrix()[q1][q2]);
		  }
		  System.out.println();
	  }
	  
	*/

	 
	  if(our_bomberman.hasEnded()==false){
		  
		  q=0;
		  q_test=0;
		  moveDir = Direction.NONE;	
		  safe_map=new Safety_Map(IA_ZONE);
		  test=false;

			int k=0;
			 for(int i=0;i<IA_ZONE.getHeigh();i++){
				  for(int j=0;j<IA_ZONE.getWidth();j++)
				  {
					  if(safe_map.returnMatrix()[i][j]==safe_map.DEST_WALL)
						 k++;
				  }
			 }
		  
		  
		  
		  
		if(escapemanager!=null){  
			
			  
			if(escapemanager.arrive_or_not()==true){
				
				escapemanager = null;
			
			}
				else{
					
					moveDir = escapemanager.direcition_updt();
			//	System.out.println("kacisa devam");
			}
			}
		else if(safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman.getCol()]!=safe_map.SAFE_CASE &&safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman.getCol()]!=safe_map.ENEMIE )
		{
			
			escapemanager = new Escape_Manager(this);
			  moveDir = escapemanager.direcition_updt();
			 // System.out.println("kac");
			
		}
		
	
		 
		else if(safe_map.returnMatrix()[our_y][our_x]==safe_map.SAFE_CASE && (our_bomberman.getBombNumber()<3)&& k>0 && our_bomberman.getBombCount()<3 ){
			  
				
				  boolean r=false;
			  for(int i=0;i<IA_ZONE.getHeigh();i++){
				  for(int j=0;j<IA_ZONE.getWidth();j++)
				  {
					  if(safe_map.returnMatrix()[i][j]==safe_map.BONUS)
						  r=true;
				  }
			  }
			  if(r==true && q_test==0){
			
				  if(bonusmanager!=null){
					  if(bonusmanager.accessiblePath()==true){
						 if(bonusmanager.hasArrived_b())
						 {
							
							 bonusmanager=null;
							// System.out.println("b");
						 }
						 else{
							 
							 moveDir=bonusmanager.direcition_updt_b();
							// System.out.println("a");
							 
						 }
				}
					  else{
						  q_test=1;
						  bonusmanager=null;
						//  System.out.println("d");
					  }
					  }
					  else{
						 q_test=1;
						  bonusmanager = new Bonus_Manager(this);
						  moveDir = bonusmanager.direcition_updt_b();
						//  System.out.println("c");
					  }
				  
			  }
			  if(r==true && q_test==1){
			
				  	
				  if(wallmanager!=null){
					 /*
					  if(wallmanager.isdang()==true){
						  wallmanager=null;
						  q=0;
					  }
					  */
					   if(wallmanager.hasArrived_b()){
						  if(canescapemanager!=null){
							  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
								  wallmanager=null;
								//  System.out.println("x");
							  }
							  else{
								  test=true;
						canescapemanager=null;
						wallmanager=null;
							  }
						  if(test==true){
							  q=1;
						  canescapemanager=null;
						  }
						  else
						  {
							  q=0;
							  canescapemanager=null;
						  }
						  }
						  else{
							 q=0;
							  canescapemanager=new Can_escape_Manager(this);
							 // System.out.println("y");
						  }
						  
					  }
					  
					  else{
						//  System.out.println("b");
						  moveDir=wallmanager.direcition_updt_b();
						
					  }
				  
				  }
				  else{
					q=0;
					  wallmanager=new Wall_Manager(this);
					  moveDir=wallmanager.direcition_updt_b();
					 // System.out.println("b");
				  }
			  }
			 
			  else if(r==false && our_bomberman.getBombCount()<3){
			
				  	
				  if(wallmanager!=null){
					 /*
					  if(wallmanager.isdang()==true){
						  wallmanager=null;
						  q=0;
					  }
					  */
					   if(wallmanager.hasArrived_b()){
						  if(canescapemanager!=null){
							  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
								  wallmanager=null;
								//  System.out.println("x");
							  }
							  else{
								  test=true;
						canescapemanager=null;
						wallmanager=null;
							  }
						  if(test==true){
							  q=1;
						  canescapemanager=null;
						  }
						  else
						  {
							  q=0;
							  canescapemanager=null;
						  }
						  }
						  else{
							 q=0;
							  canescapemanager=new Can_escape_Manager(this);
							 // System.out.println("y");
						  }
						  
					  }
					  
					  else{
						//  System.out.println("b");
						  moveDir=wallmanager.direcition_updt_b();
						
					  }
				  
				  }
				  else{
					q=0;
					  wallmanager=new Wall_Manager(this);
					  moveDir=wallmanager.direcition_updt_b();
					 // System.out.println("b");
				  }
				  
				  
				  
				  
			  }
			  
			  
			  
			  
			  }
		
		  
		
		
		else  {
					 
			if(enemiemanager!=null && our_bomberman.getBombCount()<2){
					 if(enemiemanager.accessiblePath()==true){
						 if(enemiemanager.hasArrived_b()==true){
						  if(canescapemanager!=null){
							  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
								  enemiemanager=null;
								//  System.out.println("x");
							  }
							  else{
								  test=true;
						canescapemanager=null;
						enemiemanager=null;
							  }
						  if(test==true){
							  System.out.println("x");
							  q=1;
						  canescapemanager=null;
						  enemiemanager=null;
						  }
						  else
						  {
							  q=0;
							  canescapemanager=null;
							  enemiemanager=null;
						  }
					  }
						  else{
							  canescapemanager=new Can_escape_Manager(this);
							  q=0;
						  		}	  
						  
					  	
						 }
						 else{
							 moveDir=enemiemanager.direcition_updt_b();
							 // System.out.println(2);
									 q=0;	
						 }
					 
					 }		
		 			
					 else{ 
						  
					 
						  	
						  if(wallmanager!=null){
							 /*
							  if(wallmanager.isdang()==true){
								  wallmanager=null;
								  q=0;
							  }
							  */
							   if(wallmanager.hasArrived_b()){
								  if(canescapemanager!=null){
									  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
									  {  
										  test=false;
										  canescapemanager=null;
										  wallmanager=null;
										//  System.out.println("x");
									  }
									  else{
										  test=true;
								canescapemanager=null;
								wallmanager=null;
									  }
								  if(test==true){
									  q=1;
								  canescapemanager=null;
								  }
								  else
								  {
									  q=0;
									  canescapemanager=null;
								  }
								  }
								  else{
									 q=0;
									  canescapemanager=new Can_escape_Manager(this);
									 // System.out.println("y");
								  }
								  
							  }
							  
							  else{
								//  System.out.println("b");
								  moveDir=wallmanager.direcition_updt_b();
								
							  }
						  
						  }
						  else{
								q=0;
								  wallmanager=new Wall_Manager(this);
								  moveDir=wallmanager.direcition_updt_b();
								 // System.out.println("b");
							  }
						 
						 
					 
					}	 
			 
			
	}
		else{
						  enemiemanager=new Enemie_Manager(this);
						  moveDir=enemiemanager.direcition_updt_b();
						  q=0;
					//System.out.println(3);
		 }
		}
		
		if(q==0)   
		result = new AiAction(AiActionName.MOVE,moveDir);
		else if(q==1)
			result=new AiAction(AiActionName.DROP_BOMB);
	  }
	return result;
	  }


}



  