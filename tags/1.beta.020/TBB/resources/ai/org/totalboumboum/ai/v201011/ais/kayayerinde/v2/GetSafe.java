package org.totalboumboum.ai.v201011.ais.kayayerinde.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

@SuppressWarnings("unused")
public class GetSafe {
	
	private double nFeu=6;
	private double nBombe=7;
	private double nFree=1;
	private KayaYerinde ai;
	private AiHero ownHero;
	private AiZone zone;
	
	public GetSafe(KayaYerinde ai)throws StopRequestException
	{
		ai.checkInterruption();
		this.ai=ai;
		this.zone=ai.getPercepts();
		this.ownHero=this.zone.getOwnHero();
	}
	
	public Direction destinationDanger(Matrice mat) throws StopRequestException
	{
		Direction dir=Direction.NONE;
		ai.checkInterruption();
		AiTile bombTile=mat.getBombTile(ownHero);
		if(bombTile!=null)
		{
			if(bombTile.getCol()==this.ownHero.getCol() && bombTile.getLine()==this.ownHero.getLine())
				dir=Direction.DOWNLEFT;
			else if(bombTile.getCol()==this.ownHero.getCol())
			{
				if(bombTile.getLine()<this.ownHero.getLine())
					dir=Direction.UP;
				else
					dir=Direction.DOWN;
			}
			else if(bombTile.getLine()==this.ownHero.getLine())
			{
				if(bombTile.getCol()<this.ownHero.getCol())
					dir=Direction.LEFT;
				else
					dir=Direction.RIGHT;
			}
		}
		return dir;
	}
	
	public Direction getRescueDirection(Matrice m) throws StopRequestException
	{
		ai.checkInterruption();
		Direction dir=destinationDanger(m), result=null;
		int i=0, j=1;
		m.createMatrice();
		double[][] mat=m.getMatrice();
		if(dir!=null)
		{
			if(dir==Direction.UP)
			{
				if(mat[ownHero.getCol()+1][ownHero.getLine()+1]==nFree)
					result=Direction.LEFT;
				else if(mat[ownHero.getCol()-1][ownHero.getLine()+1]==nFree)
					result=Direction.RIGHT;
				else
					result=Direction.DOWN;	
			}
			
			else if(dir==Direction.DOWN)
			{
				if(mat[ownHero.getCol()+1][ownHero.getLine()-1]==nFree)
					result=Direction.LEFT;
				else if(mat[ownHero.getCol()-1][ownHero.getLine()-1]==nFree)
					result=Direction.RIGHT;
				else
					result=Direction.UP;	
			}
			
			else if(dir==Direction.LEFT)
			{
				if(mat[ownHero.getCol()+1][ownHero.getLine()-1]==nFree)
					result=Direction.UP;
				else if(mat[ownHero.getCol()+1][ownHero.getLine()+1]==nFree)
					result=Direction.DOWN;
				else
					result=Direction.RIGHT;	
			}
			
			else if(dir==Direction.RIGHT)
			{
				if(mat[ownHero.getCol()-1][ownHero.getLine()-1]==nFree)
					result=Direction.UP;
				else if(mat[ownHero.getCol()-1][ownHero.getLine()+1]==nFree)
					result=Direction.DOWN;
				else
					result=Direction.LEFT;
			}
			
			else
			{
				while(i==0)
				{
					ai.checkInterruption();
					if(mat[ownHero.getCol()+1][ownHero.getLine()+j]==nFree || mat[ownHero.getCol()-1][ownHero.getLine()+j]==nFree)
					{
						result=Direction.DOWN;
						i=1;
					}
					
					else if(mat[ownHero.getCol()+1][ownHero.getLine()-j]==nFree || mat[ownHero.getCol()-1][ownHero.getLine()-j]==nFree)
					{
						result=Direction.UP;
						i=1;
					}
					
					else if(mat[ownHero.getCol()+j][ownHero.getLine()-1]==nFree || mat[ownHero.getCol()+j][ownHero.getLine()+1]==nFree)
					{
						result=Direction.LEFT;
						i=1;
					}
					
					else if(mat[ownHero.getCol()-j][ownHero.getLine()-1]==nFree || mat[ownHero.getCol()-j][ownHero.getLine()+1]==nFree)
					{
						result=Direction.RIGHT;
						i=1;
					}
					j++;
				}
			}
		}
		
		return result;
	}

}
