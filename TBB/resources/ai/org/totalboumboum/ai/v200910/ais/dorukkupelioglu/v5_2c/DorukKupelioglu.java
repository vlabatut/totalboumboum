package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c;


import java.util.Date;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
@SuppressWarnings("deprecation")
public class DorukKupelioglu extends ArtificialIntelligence 
{
	private Matrix matrix;
	private Escape escape=null;
	private Target target=null;
	private Bomb bomb=null;
	private AiTile currentTile=null;
	private AiTile nextTile=null;
	private AiHero dk=null;
	private AiAction result;
	private Direction moveDir=Direction.NONE;
	private double time;
	private double tilePixel;
	private boolean wait=false;
	
	public AiAction processAction() throws StopRequestException
	{
		checkInterruption(); 
		if(dk==null)
			initFirst();//pour la premiere appelle
		init();	//toujours
		
		if(!dk.hasEnded())
		{
			if(currentTile!=nextTile && (new Date()).getTime()-time>(tilePixel/dk.getWalkingSpeed())*1000)
			{
				nextTile=currentTile;
				target=null;
				bomb=null;
				escape=new Escape(this);
				wait=false;
			}
			
			if(currentTile==nextTile)
			{
				result=new AiAction(AiActionName.NONE);
				////////////////////////////////////////////
				//////////////////ESCAPE////////////////////
				///////////////////////////////////////////
				if(escape!=null)//si le hero est en train de s'enfuir
				{
					if(escape.pathAvailable())
					{
						
						if(!wait)
						{
							moveDir=escape.moveTo();
							if(moveDir!=Direction.NONE)
							{
								result=new AiAction(AiActionName.MOVE,moveDir);
								nextTile=currentTile.getNeighbor(moveDir);
								if(matrix.getAreaMatrix()[nextTile.getLine()][nextTile.getCol()]==State.FIRE)
									wait=true;
							}
							else//en cas d'erreur
							{
								result=new AiAction(AiActionName.NONE);
								escape=new Escape(this);
							}
						}
						else if(matrix.getAreaMatrix()[nextTile.getLine()][nextTile.getCol()]!=State.FIRE)
							wait=false;
					}
					else
					{
						if(escape.succeed())
							escape=null;
						else
							escape=new Escape(this);
						result=new AiAction(AiActionName.NONE);
					}
				}
				//////////////////////////////////////////
				/////////////TARGET///////////////////////
				//////////////////////////////////////////
					
				else
				{
					if(target==null)//target yarattım
					{
						matrix.createAreaMatrix();
						target=new Target(this);
					}
					
					if(bomb==null && target.pathAvailable())//o esnada bomba bırakma aşamasında değilsem veya yol varsa
					{
						if(!wait)
						{
							moveDir=target.moveTo();
							if(moveDir!=Direction.NONE)
							{
								result=new AiAction(AiActionName.MOVE,moveDir);
								nextTile=currentTile.getNeighbor(moveDir);
							}
							else
							{
								target=null;
								result=new AiAction(AiActionName.NONE);
							}
						}
						else if(matrix.getAreaMatrix()[nextTile.getLine()][nextTile.getCol()]!=State.FIRE)
							wait=false;
							
					}
					else//j'ai posé une bombe ou je vais poser une bombe ou on ne trouve pas une chemin
					{	
						if(target.succeed())//si j'avais trouvé une chemin
						{
							if(target.dropBomb())//une case avaliable(si une bombe explose ici, l'autre hero peut mourir) ou a cote d'un mur
							{
								if(bomb==null)
									bomb=new Bomb(this,dk.getTile());
							
								if(bomb.pathAvailable())//je peut trouver une chemin si je pose une bombe
								{
									if(bomb.Dropped())//si j'avais posé une bombe et je suis en train de s'enfuir
									{
										moveDir=bomb.moveTo();
										if(moveDir!=Direction.NONE)
										{
											result=new AiAction(AiActionName.MOVE,moveDir);
											nextTile=currentTile.getNeighbor(moveDir);
										}
										else
										{
											bomb=null;
											target=null;
										}
									}
									else
									{
										if(dk.getBombNumber()-dk.getBombCount()>0)
										{
											result=new AiAction(AiActionName.DROP_BOMB);
											bomb.changeDropped(true);
										}
										else
										{
											if(matrix.getAreaMatrix()[dk.getTile().getLine()][dk.getTile().getCol()]<State.DANGER)
												result=new AiAction(AiActionName.NONE);
											else
											{
												escape=new Escape(this);
												target=null;
												bomb=null;
											}
										}
									}
								}
								else
								{
									if(!target.pathWorks())
									{
										escape=new Escape(this);
									}
									target=null;
									bomb=null;
									result=new AiAction(AiActionName.NONE);
								}
							}
							else
							{
								target=null;
								bomb=null;
								escape=new Escape(this);
							}
						}
						else 
						{
							if(!(target.pathWorks()))
							{
								escape=new Escape(this);
							}
							target=null;
							bomb=null;
						}
					}
				}
				time=(new Date()).getTime();
			}
		}
		return result;
	}
	
	private void initFirst()throws StopRequestException
	{
		checkInterruption();
		matrix=new Matrix(this);
		dk=getPercepts().getOwnHero();
		nextTile=dk.getTile();
		tilePixel=nextTile.getSize();
	}
	
	public void init()throws StopRequestException
	{
		checkInterruption();
		currentTile=dk.getTile();
		matrix.createAreaMatrix();
	}
	
	public AiHero getHero()throws StopRequestException
	{
		checkInterruption();
		return dk;
	}
	public Matrix getMatrix() throws StopRequestException
	{
		checkInterruption();
		return matrix;
	}
}