package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;


import java.util.Date;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * >> Burası senin açıklamanla yer değiştirecek
 * 
 * hareketini tanımlayan birincil class
 * işlemleri birçok class a ayırın,
 * böylelikle programınız değiştirmek için ve
 * d�bugger, modifier, relire, comprendre, etmek için kolay olacaktır.
 */
public class DorukKupelioglu extends ArtificialIntelligence 
{
	private AiTile currentTile=null;
	private AiTile nextTile=null;
	private AiHero dk=null;
	private AiAction result=new AiAction(AiActionName.NONE);
	private Matrix matrix;
	private Escape escape=null;
	private Target target=null;
	private Bomb bomb=null;
	private Direction moveDir=Direction.NONE;
	private double time;
	private double tilePixel;
	
	public AiAction processAction() throws StopRequestException
	{
		checkInterruption(); //APPEL OBLIGATOIRE
		if(dk==null)
			initFirst();
		
		init();	
		
		if(!dk.hasEnded())
		{
			if(currentTile!=nextTile && (new Date()).getTime()-time>(tilePixel/dk.getWalkingSpeed())*1000)
			{
				nextTile=currentTile;
				target=null;
				bomb=null;
				escape=new Escape(this);
			}
			
			if(currentTile==nextTile)
			{
				if(escape!=null)
				{
					System.err.println("escape teyiz");
					if(escape.pathAvailable())
					{
						moveDir=escape.moveTo();
						result=new AiAction(AiActionName.MOVE,moveDir);
						nextTile=currentTile.getNeighbor(moveDir);
					}
					else
					{
						if(escape.succeed())
						{
							escape=null;
							System.err.println("kaçmayı başardık");
						}
						else
						{
							escape=new Escape(this);
							System.err.println("kaçmayı başaramadık bi daha deneyeceği");
						}
						result=new AiAction(AiActionName.NONE);
					}
				}
					
					
					
				else
				{
					if(target==null)//target yarattım
					{
						matrix.createAreaMatrix();
						target=new Target(this);
					}
					
					if(bomb==null && target.pathAvailable())//o esnada bomba bırakma aşamasında değilsem veya yol varsa
					{
						//System.out.println("target a yol bulduk");
						moveDir=target.moveTo();
						if(moveDir!=Direction.NONE)
						{
							result=new AiAction(AiActionName.MOVE,moveDir);
							nextTile=currentTile.getNeighbor(moveDir);
						}
						else
							target=null;
					}
					else//buraya geliosam ya bomba bırakmışımdır ya bırakacağımdır ya da yol yoktur
					{	
						if(target.succeed())//yol varsa
						{
							if(target.dropBomb())//ya rakiple ilgilidir ya da duvarın yanına gelmişimdir
							{
								if(bomb==null)
									bomb=new Bomb(this,dk.getTile());
							
								if(bomb.pathAvailable())//bomba bırakırsam kaçış yolum varsa
								{
									if(bomb.Dropped())//bomba bırakıldıysa
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
									else//bomba bırakılmadıysa
									{
										if(dk.getBombNumber()>0)//bombam varsa
										{
											if(matrix.getAreaMatrix()[currentTile.getLine()][currentTile.getCol()]==State.BONUS)//bulunduğum yer henüz bonus ise
												result=new AiAction(AiActionName.NONE);
											else//bonus değil ise
											{
												result=new AiAction(AiActionName.DROP_BOMB);
												System.out.println("bomba bıraktık");
												bomb.changeDropped(true);
											}
										}
										else//bombam yoksa olana kadar bekle
										{
											//buraya o esnada tehlike var ise bombamın gelmesini beklemeden kaç demeyi ekle
											result=new AiAction(AiActionName.NONE);
										}
									}
								}
								else//bomba bırakırsam kaçış yolum yoksa
								{
									//System.err.println("bombayı null yaptık");
									bomb=null;
									result=new AiAction(AiActionName.NONE);
								}
							}
						}

						else//target yolu yoksa
						{
							//System.err.println("bombayı null yaptık");
							bomb=null;
							//System.out.println("dest yolu bulamadık");
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