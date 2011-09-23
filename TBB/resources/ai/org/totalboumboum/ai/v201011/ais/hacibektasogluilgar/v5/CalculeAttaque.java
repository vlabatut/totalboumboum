package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v5;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
public class CalculeAttaque {
	
	HacibektasogluIlgar hi;	
	AiHero notreHeros;

	CalculeZone cz;
	Action act;
	
	public CalculeAttaque(HacibektasogluIlgar hi,CalculeZone cz,Action act) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;
		notreHeros=cz.zone.getOwnHero();
		this.cz=cz;
		this.act=act;

	}
	
/**
 * pour le mode attaque on calcule s'il est necessaire de poser une bomb ou pas
 * 
 * @param matrice
 * @return
 * @throws StopRequestException
 * @throws LimitReachedException 
 */
private AiAction decisionBombPourAttaque(Etats[][] matriceZone,double matriceTotal[][],AiPath chemin) throws StopRequestException, LimitReachedException
{
	hi.checkInterruption();
	AiAction result=null;

	int heroLine = notreHeros.getLine();
	int heroColonne = notreHeros.getCol();
	
	if( matriceTotal[heroLine][heroColonne]>0)
	{
		if( matriceZone[heroLine][heroColonne]==Etats.VIDE_PRIORITAIRE ||
				matriceZone[heroLine][heroColonne]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
				|| 	matriceZone[heroLine][heroColonne]==Etats.ADVERSAIRE)
		{
				
				Path path=new Path(hi,notreHeros, cz.adversairesActuelles);					
				
				AiPath tilesSecure=new AiPath();
				boolean control= path.voisineControl(matriceTotal,tilesSecure);
				
				if(control)	
				{
					double[][] matriceImaginaire=  act.matricePourImaginaireBombe(heroLine, heroColonne,  matriceTotal);
					int longueur=(int) ( cz.bombeExplosionVitesse/notreHeros.getWalkingSpeed());
					
					path.plusProcheAccesible(heroLine, heroColonne, matriceZone, matriceImaginaire, longueur+2,chemin);

					
					if(chemin.getLength()>0)
						result= new AiAction(AiActionName.DROP_BOMB);
					else matriceTotal[notreHeros.getLine()][notreHeros.getCol()]=0;
				}
		}
	}
	return result;	
}	


/**
 * 
 * @param matrice
 * @return
 * @throws StopRequestException
 * @throws LimitReachedException
 */
public AiAction calculeMatriceAttaque(Etats matrice[][],double matriceTotal[][]) throws StopRequestException, LimitReachedException
{
	hi.checkInterruption();

	AiPath chemin=new AiPath();
	AiAction result = null;
	
	for(int i=0;i< cz.hauteur;i++)
	{
		hi.checkInterruption();
		
		for(int j=0;j< cz.largeur;j++)
		{
			hi.checkInterruption();
			
			// on donne couleurs aux tiles flammable vert, flammable rouge et inaccesible
			Color flammable_vert= new Color(64,225,68);
			Color flammable_rouge= new Color(251,11,47); 
			Color case_inaccesible=new Color(255,0,0);
			
			AiOutput output = hi.getOutput();
			
			switch (matrice[i][j]) {
			case VIDE:
				 matriceTotal[i][j]+=0;
				break;
			case MUR_DESTRUCTIBLE:
				 matriceTotal[i][j]=-100;
				 int note=0;
				 for(int q=0;q<cz.nbAdversaire;q++)
				 {
					 int distance=cz.zone.getTileDistance(cz.adversairesActuelles[q].getLine(), cz.adversairesActuelles[q].getCol(), i, j);
					 if(distance==0) note=200;
					 else note+=200/distance;
				 }
				 act.calculeDapresPortee( matriceTotal,notreHeros,i, j, note,1);
				break;
			case MUR_NON_DESTRUCTIBLE:
				 matriceTotal[i][j]=-100;
				break;
			case BOMBE:
				 matriceTotal[i][j]=-100;
				break;
			case ADVERSAIRE_BOMB:
				 matriceTotal[i][j]=-100;
				break;
			case FLAMMABLE_VERT:
				 matriceTotal[i][j]+=-50;
				output.setTileColor(i, j,flammable_vert);
				break;
			case FLAMMABLE_ROUGE:
				 matriceTotal[i][j]=-100;
				output.setTileColor(i, j,flammable_rouge);
				break;
			case FEU:
				 matriceTotal[i][j]=-100;
				break;
			case ADVERSAIRE:
				int distance=cz.zone.getTileDistance(notreHeros.getLine(),notreHeros.getCol(), i, j);
				if(distance==0) distance=1;
				 matriceTotal[i][j]+=100/distance;
				 act.calculeDapresPortee( matriceTotal,notreHeros,i, j, 100,2);
			case BONUS:
				/*
				 * on calcule tous les distances entre les heros et les bonus
				 * a la fin on calcule la matrice totale comme 
				 *   matriceTotal[i][j]+=100*(minDistance/distance1);
				 */
//				int distance1 = cz.zone.getTileDistance(notreHeros.getLine(), notreHeros.getCol(), i, j);
//				int distance2[]= new int[ cz.nbAdversaire];
//				int minDistance=100;
//				for(int x=0;x< cz.nbAdversaire;x++)
//				{
//					hi.checkInterruption();
//					distance2[x]=cz.zone.getTileDistance( cz.adversairesActuelles[x].getLine(),  cz.adversairesActuelles[x].getCol(), i, j);
//					if(distance2[x]<minDistance) minDistance=distance2[x];
//				}
//				 matriceTotal[i][j]+=10*((double)minDistance/distance1);
				
				 matriceTotal[i][j]+=1;
				break;
			default:
				break;
			}
			
			// on donne le couleur rouge aux tiles inaccesible par notre héro
			if( matriceTotal[i][j]==-100) output.setTileColor(i, j,case_inaccesible);
		}	
	}



	Path path=new Path(hi, notreHeros, cz.adversairesActuelles);
		
	result=decisionBombPourAttaque( matrice, matriceTotal,chemin);

	
	if(result==null)
	{	
		
		AiPath pathCible=path.trouverCible(notreHeros.getLine(), notreHeros.getCol(),matriceTotal);	
		
//		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]>=0 && !act.pathCibleControl(pathCible))
//		{
//			pathCible=new AiPath();
//			pathCible.addTile(notreHeros.getTile());
//		}
	
		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]>=0)
			 act.pathCibleControl(pathCible);
		
		AiPath tilesSecure=new AiPath();
		
		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]==-100)
		{
			if(path.voisineControl(matriceTotal,tilesSecure) && tilesSecure!=null)
			{
				pathCible=new AiPath();
				pathCible.addTile(notreHeros.getTile());
				pathCible.addTile(tilesSecure.getFirstTile());
			}
		}
			
		if(pathCible!=null && pathCible.getLength()>0)
		{
			result=path.nouvelleAction(pathCible, notreHeros,matriceTotal);
		}
	}

	return result;

}


}
