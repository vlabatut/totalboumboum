package org.totalboumboum.ai.v201011.ais.goncuonat.v6;

import java.util.ArrayList;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Elif Göncü
 * @author Yağız Onat
 */
@SuppressWarnings("deprecation")
public class GoncuOnat extends ArtificialIntelligence
{
	// notre hero sur la zone
	public AiHero ourHero;
	// la case vide qui ne contient aucuns sprites
	// est representée dans la matrice da la zone.
	public final int CASE_EMPTY=0;
	// chemin a suivre pour s'enfuir du danger
	public AiPath nextMove=null;
	public AiPath nextMoveCheck=null;
	public AiPath nextMoveBonus=null;
	public AiPath nextMoveAttack=null;
	public AiZone zone=null;

	//private boolean searchAttack = true;
	public boolean verbose=false;
	
	
	/**
	 * Methode initialisant notre matrice de zone avant la remplissage. Chaque
	 * case est initialement considere comme CASE_EMPTY
	 *
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void initialiseMatrice(double [][]matrice, AiZone zone)throws StopRequestException
	{
		checkInterruption();//APPEL OBLIGATOIRE
		int height=zone.getHeight();
		int width=zone.getWidth();
		for(int i=0; i<height; i++)
		{
			checkInterruption();//APPEL OBLIGATOIRE
			for(int j=0; j<width; j++)
			{
				checkInterruption();//APPEL OBLIGATOIRE
				matrice[i][j] = CASE_EMPTY;
				
			}
		}
	}
	
	/**
	 * Methode calculant la nouvelle action a effectuer
	 * 
	 * @param nextMove
	 * 			Le chemin precis a suivre.
	 *  
	 * @return la nouvelle action de notre hero dans ce chemin
	 * 
	 * @throws StopRequestException
	 */
	
	public AiAction action(AiPath nextMove) throws StopRequestException
	{
		checkInterruption();//APPEL OBLIGATOIRE
		
		List<AiTile> tiles= new ArrayList<AiTile>();
		tiles = nextMove.getTiles();
	
		double dx;
		
		double dy;
		{
		dx = (tiles.get(0).getLine()) - (this.ourHero.getLine());
		dy = (tiles.get(0).getCol()) - (this.ourHero.getCol());

		if (dx < 0 && dy == 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.UP);
		}
		else if (dx < 0 && dy < 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} 
		else if (dx == 0 && dy < 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} 
		else if (dx > 0 && dy == 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} 
		else if (dx > 0 && dy > 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} 
		else if (dx == 0 && dy > 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} 
		else if (dx > 0 && dy < 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		}
		else if (dx < 0 && dy > 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		}
		else {
			return new AiAction(AiActionName.NONE);
		}
		}

	}
	
	/** méthode appelée par le moteur du jeu pour 
	 * obtenir une action de notre IA 
	 * */
	@Override
	public AiAction processAction() throws StopRequestException 
	{
		
		checkInterruption();//APPEL OBLIGATOIRE
		zone=getPercepts();
		int width = zone.getWidth();
		int height = zone.getHeight();
		double[][] matrice= new double[height][width];
		this.ourHero=zone.getOwnHero();
		boolean dropBomb = false;
		this.initialiseMatrice(matrice, zone);
		AiAction result=new AiAction(AiActionName.NONE);
		ModeAttack mattack=new ModeAttack(this);
		ModeCollecte mCollecte =new ModeCollecte(this);
		Runaway raway= new Runaway(this);
		DropBomb dbomb= new DropBomb(this);
		
		
		// Condition pour etre en mode attaque
		List<AiHero> heross=zone.getHeroes();
		List<AiTile> herostiless= new ArrayList<AiTile>();		
		for(int i=0; i<heross.size();i++)
		{
			this.checkInterruption();
			herostiless.add(heross.get(i).getTile());
		}
			if((ourHero.getBombNumberMax()>=3) )
			{
			
				 // Algorithme pour s'enfuir
				
				// On donne les valeurs aux cases suivant les objects
				mattack.valueBonusAttack(matrice, zone);
				mattack.valueBlocksAttack(matrice,zone);
				mattack.valueBombsAttack(matrice, zone);
				mattack.valueFiresAttack(matrice, zone);
				mattack.valueRivalAttack(matrice, zone);
				
				// On affiche des textes sur les cases
				updateOutput(matrice, zone);
				
				if(dbomb.dropBombCheckAttack(zone,matrice))
					{
						if(verbose)
							System.out.println("dropbombcheck");
						dropBomb = true;
					}
				else
					{
						dropBomb=false;
						
					
					}
					
				if(dropBomb)
						result = new AiAction(AiActionName.DROP_BOMB);
			
				
					
				if (!zone.getTile(ourHero.getLine(),ourHero.getCol()).getBombs().isEmpty()
							||!zone.getTile(ourHero.getLine(),ourHero.getCol()).getFires().isEmpty()||
							matrice[ourHero.getLine()][ourHero.getCol()]<0) 
					{
							nextMoveAttack=null;
							nextMoveBonus=null;
							if(nextMoveCheck!=null)
								if(nextMoveCheck.getLength()!=0)	
								{
									result = action(nextMoveCheck);
								}
							/*if(nextMoveCheck==null && nextMoveCheck.getLength()==0)
							{
								raway.runAwayAlgoAttackCheck(matrice,zone,mattack.endPoint(matrice, zone));
								if (nextMoveCheck != null)
									if(nextMoveCheck.getLength()!=0)
										result = action(nextMoveCheck);
						}*/
					}
							
				mattack.matriceAttack( matrice,zone);
					if(nextMoveAttack!= null)
					{
						if (nextMoveAttack.getLength()!=0)
							result = this.action(nextMoveAttack);
					}	
				
			}
			
			else
			{
				
				
				mCollecte.valueBonusCollecte(matrice, zone);
				mCollecte.valueRivalCollecte(matrice, zone);
				mCollecte.valueBlocksCollecte(matrice,zone);
				mCollecte.valueBombsCollecte(matrice, zone);
				mCollecte.valueFiresCollecte(matrice, zone);
				if(verbose)
					System.out.println("matrice collecte");
				
				updateOutput(matrice,zone);
				
				if ((matrice[ourHero.getLine()][ourHero.getCol()] == mCollecte.COLLECT_FIRE)
						||(matrice[ourHero.getLine()][ourHero.getCol()] == mCollecte.COLLECT_BOMB)) 
					{
						nextMoveBonus=null;
						nextMoveAttack=null;
						raway.runAwayAlgoCollecte(matrice,zone);
						if (nextMove != null)
						{
							if(nextMove.getLength()!=0)
								result = action(nextMove);
						}
					}
			
				else
				{
					//if(searchBonus)
						mCollecte.matriceCollecte( matrice,zone);
						if (matrice[ourHero.getTile().getLine()][ourHero.getTile().getCol()] == mCollecte.COLLECT_SOFTWALL)
						{
							if(dbomb.dropBombCheckCollecte(zone/*, matrice*/))
								dropBomb = true;
							
						}
							
						else 
						{
							if (matrice[ourHero.getTile().getLine()][ourHero.getTile().getCol()]  == mCollecte.COLLECT_FIRE)		
									dropBomb = false;
							
						}
						if (dropBomb) 
						{
							result = new AiAction(AiActionName.DROP_BOMB);
						}
						else
						{	
							if (nextMoveBonus != null)
							{
								if(verbose)
									System.out.println("null de ilse");
								if (nextMoveBonus.getLength() != 0)
								{	
									if(verbose)
										System.out.println("length 0 de ilse");
									result = this.action(nextMoveBonus);}
							}
						}
						
					}
					
				
			
			}
		return result;
		 
	}

	
	/**
	 * 
	 * Methode permettant d'afficher des textes sur 
	 * les cases de la matrice de la zone
	 * 
	 * @param matrice
	 *            la matrice de la zone du jeu
	 * @param zone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void updateOutput(double [][]matrice, AiZone zone) throws StopRequestException
	{	
		checkInterruption();//APPEL OBLIGATOIRE
	
		AiOutput output = getOutput();

		
		for(int line=0;line<zone.getHeight();line++)
		{	
			checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	
				checkInterruption(); //APPEL OBLIGATOIRE
				
			double a=(matrice[line][col]);
			String text=Double.toString(a);
			
			output.setTileText(line,col,text);
				 
			}
		}
	}
	
}
