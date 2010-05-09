package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_1;


import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;










/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
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


 
 /* méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
 public AiAction processAction() throws StopRequestException
 { 
	  checkInterruption(); //APPEL OBLIGATOIRE
  
	  IA_ZONE=getPercepts();
	 
	 
	  our_bomberman=IA_ZONE.getOwnHero();
	  our_tile=our_bomberman.getTile();
	  
	  our_x=our_tile.getCol();
	  our_y=our_tile.getLine();
	  
	  AiAction result = new AiAction(AiActionName.NONE);
  
		 
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
				}
			}
		else if(safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman.getCol()]!=safe_map.SAFE_CASE &&safe_map.returnMatrix()[our_bomberman.getLine()][our_bomberman.getCol()]!=safe_map.ENEMIE )
		{
			
			escapemanager = new Escape_Manager(this);
			  moveDir = escapemanager.direcition_updt();
			//  System.out.println("kac");
			
		}
		
	
		 
		else if(safe_map.returnMatrix()[our_y][our_x]==safe_map.SAFE_CASE && (our_bomberman.getBombNumber()<3)/*&& k>0 && our_bomberman.getBombCount()<2*/ ){
			 // System.out.println("bonus");
				
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
						 }
						 else{
							 
							 moveDir=bonusmanager.direcition_updt_b();
							 
						 }
				}
					  else{
						  q_test=1;
						  bonusmanager=null;
					  }
					  }
					  else{
						  bonusmanager = new Bonus_Manager(this);
						  moveDir = bonusmanager.direcition_updt_b();
					  }
				  
			  }
		
			  
			  if(r==true && q_test==1){
				  bonusmanager=null;
				  	
				  if(wallmanager!=null){
				
					   if(wallmanager.hasArrived_b()){
						  if(canescapemanager!=null){
							  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
								  wallmanager=null;
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
						  }
						  
					  }
					  
					  else{
						  moveDir=wallmanager.direcition_updt_b();
						
					  }
				  
				  }
				  else{
					q=0;
					  wallmanager=new Wall_Manager(this);
					  moveDir=wallmanager.direcition_updt_b();
					
				  }
			  }
			 
			  else if(r==false && our_bomberman.getBombCount()<3){
			
				  	
				  if(wallmanager!=null){
					
					   if(wallmanager.hasArrived_b()){
						  if(canescapemanager!=null){
							  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
								  wallmanager=null;
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
						  }
						  
					  }
					  
					  else{
						  moveDir=wallmanager.direcition_updt_b();
						
					  }
				  
				  }
				  else{
					q=0;
					  wallmanager=new Wall_Manager(this);
					  moveDir=wallmanager.direcition_updt_b();
				  }
				  
			  }
		
 }
		  
		
		
		else if(IA_ZONE.getOwnHero().getBombCount()<3)  {
			 // System.out.println("atak");
			  
			  if(enemiemanager!=null){
				  if(enemiemanager.accessiblePath()==false /*|| enemiemanager.sameposEnemie()==false*/){
					  xxx();
				  }
				  else if(enemiemanager.canPass()==true){
						 if(enemiemanager.hasArrived_b()==true){							
						  if(canescapemanager!=null){							
							  if(canescapemanager.getPathLength()<2 || canescapemanager.getPathLength()>5  )
							  {  
								  test=false;
								  canescapemanager=null;
							  }
							  else{
								  test=true;
						canescapemanager=null;
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
							  canescapemanager=new Can_escape_Manager(this);
							  q=0;
						  }	  
						  enemiemanager=null;
					  	
						 }
						 else{
							 moveDir=enemiemanager.direcition_updt_b();
									 q=0;	
						 }
	 
					 	
			}		
				  else
					  enemiemanager=null;
			
	}
			  
		else{
						  enemiemanager=new Enemie_Manager(this);
						  moveDir=enemiemanager.direcition_updt_b();
						  q=0;
		 }
		}
		
		if(q==0)   
		result = new AiAction(AiActionName.MOVE,moveDir);
		else if(q==1)
			result=new AiAction(AiActionName.DROP_BOMB);
	  }
	return result;
	  }



private void xxx() throws StopRequestException {
		
	 	enemiemanager=null;
		  	
		  if(wallmanager!=null){
			   if(wallmanager.hasArrived_b()){
				  if(canescapemanager!=null){
					  if(canescapemanager.getPathLength()<3 || canescapemanager.getPathLength()>5  )
					  {  
						  test=false;
						  canescapemanager=null;
					
					  }
					  else{
						  test=true;
				canescapemanager=null;
		
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
				
				  }
				  wallmanager=null;
			  }
			  
			  else{
				  moveDir=wallmanager.direcition_updt_b();
				
			  }
		  }
		  else{
				q=0;
				  wallmanager=new Wall_Manager(this);
				  moveDir=wallmanager.direcition_updt_b();
			  }
	
}

}



