package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */
public class DemirciDuzokErgok extends ArtificialIntelligence
{ 
 private OurZone zone;

 private AiTile nextcase=null;
 
 private AiTile ourcase;
 private AiZone zone_IA;
 private Astar star;
 private HeuristicCalculator heuristicCalc;
 private CostCalculator costCalc;
 
 private AiTile pasdedanger;
 
 Collection<AiHero> AllEnnemies;
 AiHero OurBomberMan;
 
 //bizim koordinatlar
 private int coord_x;
 private int coord_y;
 
 //private Cases control_matrix [] [];
 
 Cases Case;
 
 AiAction Action = new AiAction(AiActionName.NONE);
 
 Collection<AiTile> safecases;
 
 double[][] bomb_time = new double[50][50];//aslinda benim zone'daki case sayisi kadar olmali, [getHeight()][getweight()]
 
 
 /* méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
 public AiAction processAction() throws StopRequestException
 { 
  checkInterruption(); //APPEL OBLIGATOIRE
  
  zone_IA=getPercepts();
  OurBomberMan=zone_IA.getOwnHero();
  zone = new OurZone(zone_IA);  
  
  coord_x=OurBomberMan.getLine();
  coord_y=OurBomberMan.getCol();
  ourcase=OurBomberMan.getTile();
  
  costCalc = new BasicCostCalculator();
  heuristicCalc = new BasicHeuristicCalculator();
  star = new Astar(this,OurBomberMan, costCalc, heuristicCalc);
  
  System.out.println("Width : "+zone_IA.getWidth());
  System.out.println("Heigth : "+zone_IA.getHeight()); 
  for(int i=0; i< zone_IA.getHeight(); i++)
  {
	  for(int j=0; j< zone_IA.getWidth(); j++)
		  System.out.println(i+" "+j+" "+zone.returnMatrix()[i][j]);
  }
  
  if(OurBomberMan!=null)
  {
   Direction direction = Direction.NONE;
  
   if(nextcase==null)
   {
    nextcase=ourcase;
   }
   else
   {
    Go_On();
   }
   
   //direction
   if (nextcase != null) 
   {    
    if (zone.CanContinue(nextcase.getLine(), nextcase.getCol()))     
       direction = zone_IA.getDirection(ourcase,nextcase);
    else 
    {      
    	nextcase=sidanger();
    	if (nextcase != null && nextcase.getLine() < zone.height
          && nextcase.getCol() < zone.width
          && nextcase.getCol() > 0
          && nextcase.getLine() > 0)
    		direction = zone_IA.getDirection(ourcase, nextcase);
    }
   }
   
   //move or stop
   if (direction!=Direction.NONE
		&& direction != Direction.DOWNLEFT
		&& direction != Direction.DOWNRIGHT
		&& direction != Direction.UPLEFT
		&& direction != Direction.UPRIGHT) {
    Action = new AiAction(AiActionName.MOVE, direction);
   }
   else
	   Action=new AiAction(AiActionName.NONE);
  }
  
  return Action;
 }
 
 public void Go_On() throws StopRequestException
 {
  checkInterruption(); // APPEL OBLIGATOIRE
  Set_BombTime();
  
  if(zone.returnMatrix()[coord_x+1][coord_y]!=Cases.Safe_Case
		  || zone.returnMatrix()[coord_x-1][coord_y]!=Cases.Safe_Case
		  ||zone.returnMatrix()[coord_x][coord_y+1]!=Cases.Safe_Case
		  ||zone.returnMatrix()[coord_x][coord_y-1]!=Cases.Safe_Case)
   Escape();
  //else if(Controle(nextcase))
   //Escape();
  
 }
 
public AiTile sidanger() throws StopRequestException {
	checkInterruption(); // APPEL OBLIGATOIRE

	if (coord_x + 1 < zone.height && zone.CanContinue(coord_x + 1, coord_y) && zone_IA.getTile(coord_x + 1, coord_y ).getBlocks()==null) {
		pasdedanger = zone_IA.getTile(coord_x + 1, coord_y );
	}
	else if (coord_x - 1 > 0 && zone.CanContinue(coord_x - 1, coord_y) && zone_IA.getTile(coord_x - 1,coord_y ).getBlocks()==null) {
		pasdedanger = zone_IA.getTile( coord_x - 1,coord_y);	
	} 
	else if (coord_y + 1 < zone.width && zone.CanContinue(coord_x, coord_y + 1) && zone_IA.getTile(coord_x, coord_y + 1 ).getBlocks()==null) {
		pasdedanger = zone_IA.getTile(coord_x,coord_y + 1);
	} 
	else if (coord_y - 1 > 0 && zone.CanContinue(coord_x, coord_y - 1) && zone_IA.getTile(coord_x, coord_y - 1).getBlocks()==null) {
		pasdedanger = zone_IA.getTile(coord_x, coord_y - 1 );
	} 
	return pasdedanger;
}
 
 public Collection<AiTile> Safe_Cases(int c, int l) throws StopRequestException
 {
  checkInterruption(); // APPEL OBLIGATOIRE
  
  Collection<AiTile> secure = new ArrayList<AiTile>();
  AiTile jeu[][] = new AiTile[zone_IA.getHeight()][zone_IA.getWidth()];
  
  //une matrice vide
  for (int line = 0; line < zone_IA.getHeight(); line++) {
   checkInterruption(); // APPEL OBLIGATOIRE
   for (int col = 0; col < zone_IA.getWidth(); col++) {
    checkInterruption(); // APPEL OBLIGATOIRE
    jeu[line][col] = (zone_IA.getTile(line, col));
   }
  }
 
  AiTile jeu_case;
  
  for (int line = 0; line < zone_IA.getHeight(); line++) {
   checkInterruption(); // APPEL OBLIGATOIRE
   for (int col = 0; col < zone_IA.getWidth(); col++) {
    checkInterruption(); // APPEL OBLIGATOIRE
    if (c != col && l != line && zone.returnMatrix()[line][col]==Cases.Safe_Case) {
     jeu_case=jeu[line][col];
     if(star.processShortestPath(ourcase,jeu_case)!=null)
     {
      secure.add(jeu_case);
     }
    }
   }
  }
 
  return secure;
 } 
 
 public boolean Controle(AiTile tile) throws StopRequestException
 {
  checkInterruption();//APPEL OBLIGATOIRE  
  boolean resultat = false;  
  if(zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Fire||zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Range_Of_Fire||zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Wall_ND||zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Bomb ||zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Wall_D||zone.returnMatrix()[tile.getLine()][tile.getCol()]==Cases.Enemie)
  {
   resultat=true;
  }   
  return resultat;
 }
 
 public void Set_BombTime() throws StopRequestException
 {
  checkInterruption();//APPEL OBLIGATOIRE 
  
  double time=10000;
  AiBomb bomb;
  AiTile tile;
  Iterator<AiBomb> bombs;
  
  for (int line = 0; line < zone_IA.getHeight(); line++) {
   checkInterruption(); // APPEL OBLIGATOIRE
   for (int col = 0; col < zone_IA.getWidth(); col++) {
    checkInterruption(); // APPEL OBLIGATOIRE
    if(zone.returnMatrix()[line][col]==Cases.Bomb)
    {
     tile=zone_IA.getTile(line, col);
     if(tile.getBombs()!=null)
     {
      //ayni noktada birden fazla bomba olabilir, benim icin onemli olan ilk patlayacak olan bomba 
      bombs=tile.getBombs().iterator();
      while(bombs.hasNext())
      {
       bomb=bombs.next();
       if(time>bomb.getNormalDuration()-bomb.getTime())
       {
        time=bomb.getNormalDuration()-bomb.getTime();
       }
      }
      bomb_time[line][col]=time;
     }        
    }
    else if(zone.returnMatrix()[line][col]==Cases.Fire||zone.returnMatrix()[line][col]==Cases.Range_Of_Fire)
     bomb_time[line][col]=-1;
    else
     bomb_time[line][col]=0;
   }
  }
 }
 
 public void Escape() throws StopRequestException
 {
  checkInterruption(); // APPEL OBLIGATOIRE
  
  Collection<AiTile> sure= Safe_Cases(coord_x, coord_y);  
  Iterator<AiTile> itSure=sure.iterator();  
  AiPath sure_path;
  AiTile sure_case=ourcase;//ilk deger vermis olmak icin
  Iterator<AiTile> itPath_tile;
  
  AiTile itSure_Tile;
  
  int lengthlast=0;
  int length=0;  
  
  //burda bombaya ya da atese kadar olan en kisa mesafeyi buldum
  while(itSure.hasNext())
  {
   itSure_Tile=itSure.next();
	  
   sure_path=star.processShortestPath(ourcase, itSure_Tile);   
   itPath_tile=sure_path.getTiles().iterator();
   
   while(itPath_tile.hasNext())
   {
    //ates varsa ya da bombaya geldiysen dur
    if(bomb_time[itPath_tile.next().getLine()][itPath_tile.next().getCol()]==-1||bomb_time[itPath_tile.next().getLine()][itPath_tile.next().getCol()]>0)
    {     
     break;
    }
    else 
     length++;
   }
   if(lengthlast<length)
   {
    lengthlast=length;
    sure_case=itSure_Tile;
   }
   length=0;
  }
  nextcase=sure_case;
  
 
 }  

}
