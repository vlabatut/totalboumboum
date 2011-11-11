/********************************************
 * DECCAL                                   *
 *                                          *
 * L'implément d'AI pour BombermanJA   *
 *                                          *
 * L'université Galatasaray @ 2007           *
 *                                          *
 * par Betul Okan et Erkan Yuksel           *
 ********************************************/

package org.totalboumboum.ai.v200708.ais.okanyuksel;

import java.util.LinkedList;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Betul Okan
 * @author Erkan Yüksel
 *
 */
@SuppressWarnings("deprecation")
public class OkanYuksel extends ArtificialIntelligence
{
	private static final long serialVersionUID = 1L;

	/* Pour calculer case suivant en utilisant l'index de direction. */
	private static int[][] yonelim = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, 0}};
	/*Pour réaligner les constants de direction.*/ 
	private static int[] yon = {AI_ACTION_GO_UP, AI_ACTION_GO_RIGHT, AI_ACTION_GO_DOWN, AI_ACTION_GO_LEFT};
	
	/* l'index pour la direction courante. */
	private int yonum;
	/* La route à s'échapper */
	private LinkedList<Integer> kacisR;
	/* La puissance de la bombe qui est détecté comme un danger. */
	private int tBombaG;
	/* Les informations de la position pour la bombe qui est détecté comme un danger. */
	private int tBombaKx, tBombaKy;
	/* un index qui détermine si le Contrôle des direction est croissante ou décroissante */
	private int artis;
	
	public OkanYuksel()
	{
		super("OkanYuksel");
		tBombaG = 1;
		tBombaKx = -1;
		tBombaKy = -1;
		yonum = 0;
		kacisR = new LinkedList<Integer>();
		artis = 1;
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{
		if(firstTime)
			firstTime = false;
		else
		{	
		try
		{
			//Les informations de la position de DECCAL.
			int xM = getOwnPosition()[0];
			int yM = getOwnPosition()[1];
			
			//S'il y a une route planifiée, d'abord celle va s'appliquer.
			if (!kacisR.isEmpty())
			{
				yonum = kacisR.pollLast();
				return yon[yonum];
			}
			
			/*
			 * S'il y a un autre joueur dans le champ d'effet de nos bombes, 
			 * et si DECCAL a une bombe à poser, 
			 * et si une route peut-être trouvée pour s'échapper, 
			 * DECCAL va poser une bombe pour détruire son rival.
			 */
			if (hedefVar(xM, yM) && getOwnBombCount()>0)
			{
				kacisBul(xM, yM, -1, xM, yM, getOwnFirePower());
				if (!kacisR.isEmpty())
				{
					return AI_ACTION_PUT_BOMB;
				}
			}
			//Enfin DECCAL va chercher une autre possibilité.
			return yonSec(xM, yM);
		}
		catch(Exception hata)
		{// Dans une situation pas exceptée, tous les variables sont resette, et DECCAL continue comme lui commence.
			tBombaG = 1;
			tBombaKx = -1;
			tBombaKy = -1;
			yonum = 0;
			kacisR = new LinkedList<Integer>();
			artis = 1;
			return AI_ACTION_DO_NOTHING;
		}
		}
		return AI_ACTION_DO_NOTHING;
	}

	/* L'information d'un point */
	private int nokta(int xM, int yM) throws Exception
	{
		return getZoneMatrix()[xM][yM];
	}

	/* 
	 * Les informations sur les cases qui sont situées en retour d'un point.
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @return Le list des informations des cases.  
	 * */
	private int[] cevre(int xM, int yM) throws Exception
	{
		int[] sonuc = new int[4];
		int[][] saha = getZoneMatrix();

		sonuc[0] = saha[xM][yM-1];
		sonuc[1] = saha[xM+1][yM];
		sonuc[2] = saha[xM][yM+1];
		sonuc[3] = saha[xM-1][yM];
		
		return sonuc;
	}

	/*
	 * Y'a-t-il une sortie en passant une case dans une direction
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @param yonu L'index de direction
	 * @return true ou false
	 */
	private boolean onuAcik(int xM, int yM, int yonu) throws Exception
	{
		int xA = xM + yonelim[yonu][0];
		int yA = yM + yonelim[yonu][1];
		
		int[] cevrem = cevre(xA, yA);
		
		boolean sonuc = false;
		
		sonuc |= cevrem[(yonu+3)%4]==AI_BLOCK_EMPTY | cevrem[(yonu+3)%4]==AI_BLOCK_ITEM_BOMB | cevrem[(yonu+3)%4]==AI_BLOCK_ITEM_FIRE;
		sonuc |= cevrem[yonu]==AI_BLOCK_EMPTY | cevrem[yonu]==AI_BLOCK_ITEM_BOMB | cevrem[yonu]==AI_BLOCK_ITEM_FIRE;
		sonuc |= cevrem[(yonu+1)%4]==AI_BLOCK_EMPTY | cevrem[(yonu+1)%4]==AI_BLOCK_ITEM_BOMB | cevrem[(yonu+1)%4]==AI_BLOCK_ITEM_FIRE;
		
		return sonuc;
	}
	
	/* 
	 * Est-ce que la route est verticalement ou horizontalement ouverte?
	 * (i.e Est-ce que la route peut-être verticalement ou horizontalement marchée?)
	 * @param n1 point 1
	 * @param n2 point 2
	 * @return true ou false
	 * */
	private boolean yolAcik(int[] n1, int[] n2) throws Exception
	{
		boolean sonuc = true;
		if (n1[0]==n2[0] && n1[1]==n2[1])
		{
			return sonuc;
		}
		else if (n1[0] == n2[0])
		{
			int kck = Math.min(n1[1], n2[1]);
			int byk = Math.max(n1[1], n2[1]);
			if (kck>=0 && byk<getZoneMatrixDimY())
			{
				for (int syc = 0; syc<=byk-kck; syc++)
				{
					int sinir = kck + syc;
					if (
							nokta(n1[0], sinir) != AI_BLOCK_EMPTY && 
							nokta(n1[0], sinir) != AI_BLOCK_ITEM_BOMB &&
							nokta(n1[0], sinir) != AI_BLOCK_ITEM_FIRE
						)
					{
						return false;
					}
				}
			}
			else
			{
				return false;
			}
		}
		else if (n1[1] == n2[1])
		{
			int kck = Math.min(n1[0], n2[0]);
			int byk = Math.max(n1[0], n2[0]);
			if (kck>=0 && byk<getZoneMatrixDimX())
			{
				for (int syc = 0; syc<=byk-kck; syc++)
				{
					int sinir = kck + syc;
					if (
							nokta(sinir, n1[1]) != AI_BLOCK_EMPTY &&
							nokta(sinir, n1[1]) != AI_BLOCK_ITEM_BOMB &&
							nokta(sinir, n1[1]) != AI_BLOCK_ITEM_FIRE
						)
					{
						return false;
					}
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			sonuc = false;
		}
		return sonuc;
	}
	
	/*
	 * Est-ce que la route est verticalement ou horizontalement ouverte et
	 *  la distance est égale ou moins de à menzil ?
	 * @param n1 point 1
	 * @param n2 point 2
	 * @param menzil distance
	 * @return true ou false
	 */
	private boolean yolAcikveMenzilde(int[] n1, int[] n2, int menzil) throws Exception
	{
		if (yolAcik(n1, n2))
		{
			if (Math.abs(n1[0]-n2[0])+Math.abs(n1[1]-n2[1]) <= menzil)
			{
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Pour trouver une route à s'échapper.
	 * @param xM coordonnée-x de la position de DECCAL
	 * @param yM coordonnée-y de la position de DECCAL
	 * @param gYonu Un index de direction
	 * @param kX coordonnée-x de la position de danger
	 * @param kY coordonnée-y de la position de danger
	 * @param menzil Puissance de danger(bombe)
	 */
	private void kacisBul(int xM, int yM, int gYonu, int kX, int kY, int menzil) throws Exception
	{
		int[] cevrem = cevre(xM, yM);
		
		for (int syc=0; syc<4; syc++)
		{
			if (
					gYonu != syc && //pour empêcher de regarder à la direction qui est déjà utilisé.
					(cevrem[syc] == AI_BLOCK_EMPTY ||
					cevrem[syc] == AI_BLOCK_ITEM_BOMB ||
					cevrem[syc] == AI_BLOCK_ITEM_FIRE)
				)
			{
				if (
						!tehlikeli(xM, yM, syc, true) && 
						!yolAcikveMenzilde(new int[]{kX, kY}, new int[]{xM+yonelim[syc][0],yM+yonelim[syc][1]}, menzil)
					)
				{//une position sûre a été trouvée
					kacisR.push(syc);
					return;
				}
				else if (onuAcik(xM, yM, syc))
				{//c'est encore dangereux mais peut-être on peut trouver en passant sur cette case.
					kacisR.push(syc);
					kacisBul(xM + yonelim[syc][0], yM + yonelim[syc][1], (syc+2)%4, kX, kY, menzil);
				}
			}
		}
		/*
		 * Cette fois, ça ne peut pas trouvé. 
		 * Le dernier est retiré et s'il y a un appel pas fini dans la récursivité, 
		 * ça continuera, sinon ça finira.
		 */
		if (!kacisR.isEmpty())
			kacisR.pop();
	}
	
	/* 
	 * Est-ce qu'il y a un rival dans la direction de DECCAL?
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @param yonu L'index de direction
	 * @return une index de direction
	 * */
	private int hedef(int xM, int yM, int yonu) throws Exception
	{
		for (int syc=0; syc<getPlayerCount(); syc++)
		{
			int[] kP = getPlayerPosition(syc);
			if (isPlayerAlive(syc) && yolAcik(kP, new int[]{xM,yM}))
			{
				/* ça ne regarde pas d'arrière, 
				 * pour empêche de tomber dans une piège 
				 * après poser une bombe pour un rival. 
				 * Donc il regardera à sa droite, à sa gauche et à son avant. 
				*/
				if (kP[0]==xM && kP[1]<yM && yonu!=2)
					return 0;
				if (kP[0]>xM && kP[1]==yM && yonu!=3)
					return 1;
				if (kP[0]==xM && kP[1]>yM && yonu!=0)
					return 2;
				if (kP[0]<xM && kP[1]==yM && yonu!=1)
					return 3;
			}
		}
		return -1;
	}

	/* 
	 * Est-ce qu'il y a un rival dans la champ d'effet de nos bombes?
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @return true ou false
	 * */
	private boolean hedefVar(int xM, int yM) throws Exception
	{
		for (int syc=0; syc<getPlayerCount(); syc++)
		{
			int[] kP = getPlayerPosition(syc);
			if (
					isPlayerAlive(syc) &&
					kP[0]>=xM-getOwnFirePower() && 
					kP[1]>=yM-getOwnFirePower() && 
					kP[0]<=xM+getOwnFirePower() && 
					kP[1]<=yM+getOwnFirePower() 
				)
			{
				return true;
			}
		}
		return false;
	}
	
	/* 
	 * La direction est dangereuse
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @param yonu L'index de direction
	 * @param arkanabak Regarder d'arrière aussi
	 * @return true ou false
	 */
	private boolean tehlikeli(int xM, int yM, int yonu, boolean arkanaBak) throws Exception
	{
		//les coordonnées de la cas suivant 
		int xA = xM + yonelim[yonu][0];
		int yA = yM + yonelim[yonu][1];
		
		//s'il y a un bloc de feu dans cette case, c'est dangereux
		if (nokta(xA, yA) == AI_BLOCK_FIRE)
		{
			return true;
		}
		//s'il y a un bloc de bombe dans cette case, c'est dangereux
		if (nokta(xA, yA) == AI_BLOCK_BOMB)
		{
			tBombaG = getBombPowerAt(xA, yA);
			tBombaKx = xA;
			tBombaKy = yA;
			return true;
		}
		
		int[][] saha = getZoneMatrix();
		
		if (yonelim[yonu][1]==0 || yonelim[yonu][1]==1)
		{//pour droite, gauche et bas
			for (int syc=yA; syc<getZoneMatrixDimY(); syc++)
			{
				if (saha[xA][syc] == AI_BLOCK_BOMB)
				{
					if (getBombPowerAt(xA, syc) >= Math.abs(yA-syc))
					{
						tBombaG = getBombPowerAt(xA, syc);
						tBombaKx = xA;
						tBombaKy = syc;
						return true;
					}
				}
				else if (nokta(xA, yA) != AI_BLOCK_FIRE && saha[xA][syc] != AI_BLOCK_EMPTY)
				{
					break;
				}
			}
		}
		if (yonelim[yonu][1]==0 || yonelim[yonu][1]==-1)
		{//pour droite, gauche et haut 
			for (int syc=yA; syc>0; syc--)
			{
				if (saha[xA][syc] == AI_BLOCK_BOMB)
				{
					if (getBombPowerAt(xA, syc) >= Math.abs(yA-syc))
					{
						tBombaG = getBombPowerAt(xA, syc);
						tBombaKx = xA;
						tBombaKy = syc;
						return true;
					}
				}
				else if (nokta(xA, yA) != AI_BLOCK_FIRE && saha[xA][syc] != AI_BLOCK_EMPTY)
				{
					break;
				}
			}
		}
		if (yonelim[yonu][0]==0 || yonelim[yonu][0]==1)
		{//pour haut, bas et droite
			for (int syc=xA; syc<getZoneMatrixDimX(); syc++)
			{
				if (saha[syc][yA] == AI_BLOCK_BOMB)
				{
					if (getBombPowerAt(syc, yA) >= Math.abs(xA-syc))
					{
						tBombaG = getBombPowerAt(syc, yA);
						tBombaKx = syc;
						tBombaKy = yA;
						return true;
					}
				}
				else if (nokta(xA, yA) != AI_BLOCK_FIRE && saha[syc][yA] != AI_BLOCK_EMPTY)
				{
					break;
				}
			}
		}
		if (yonelim[yonu][0]==0 || yonelim[yonu][0]==-1)
		{//pour haut, bas et gauche
			for (int syc=xA; syc>0; syc--)
			{
				if (saha[syc][yA] == AI_BLOCK_BOMB)
				{
					if (getBombPowerAt(syc, yA) >= Math.abs(xA-syc))
					{
						tBombaG = getBombPowerAt(syc, yA);
						tBombaKx = syc;
						tBombaKy = yA;
						return true;
					}
				}
				else if (nokta(xA, yA) != AI_BLOCK_FIRE && saha[syc][yA] != AI_BLOCK_EMPTY)
				{
					break;
				}
			}
		}
		
		if (arkanaBak)
		{//regarder son arrière
			return tehlikeli(xA, yA, (yonu+2)%4, false);
		}
		
		return false;
	}

	/* 
	 * Pour détecter s'il y a une shrink dans une direction.
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * @param yonu L'index de direction
	 * @return un index de direction
	 * */
	private int daralmaTehlikesi(int xM, int yM, int yonu) throws Exception
	{
		if (getTimeBeforeShrink()<1)//c'est l'heur?
		{
			int[] sonraki = getNextShrinkPosition();

			//DECCAL travaillera à aller au milieu. 
			switch (yonu)
			{
				case 0:
					if (sonraki[0]==xM && sonraki[1]<yM)
					{
						if (xM > getZoneMatrixDimX()/2)
						{
							return 3;
						}
						else
						{
							return 1;
						}
					}
					break;
				case 1:
					if (sonraki[0]>xM && sonraki[1]==yM)
					{
						if (yM > getZoneMatrixDimY()/2)
						{
							return 0;
						}
						else
						{
							return 2;
						}
					}
					break;
				case 2:
					if (sonraki[0]==xM && sonraki[1]>yM)
					{
						if (xM > getZoneMatrixDimX()/2)
						{
							return 3;
						}
						else
						{
							return 1;
						}
					}
					break;
				case 3:
					if (sonraki[0]<xM && sonraki[1]==yM)
					{
						if (yM > getZoneMatrixDimY()/2)
						{
							return 0;
						}
						else
						{
							return 2;
						}
					}
					break;
			}
		}

		return -1;
	}
	
	/* 
	 * Pour trouver une direction possible
	 * @param xM coordonnée-x
	 * @param yM coordonnée-y
	 * */
	private int yonSec(int xM, int yM) throws Exception
	{
		int[] cevrem = cevre(xM, yM);
		
		//Est-ce qu'il y a un rival à changer notre direction?
		int hdf = hedef(xM, yM, yonum);
		if(hdf>-1)
			yonum = hdf;
		
		int drlma = daralmaTehlikesi(xM, yM, yonum);
		if (drlma>-1)
			yonum = drlma;
		
		for (int syc=yonum; syc<4 && syc>-1; syc=syc+artis)
		{
			if (
					(cevrem[syc] == AI_BLOCK_EMPTY
					|| cevrem[syc] == AI_BLOCK_ITEM_BOMB
					|| cevrem[syc] == AI_BLOCK_ITEM_FIRE)
				)
			{//Est-ce que c'est possible à marcher?
				//Est-ce qu'il y a un danger?
				if (!tehlikeli(xM, yM, syc, false))
				{
					yonum = syc;
					return yon[syc];
				}
				else
				{
					//s'il y a une danger, trouve une route.
					kacisBul(xM, yM, yonum, tBombaKx, tBombaKy, tBombaG);
				}
			}
			else if (cevrem[syc] == AI_BLOCK_WALL_SOFT && getOwnBombCount()>0)
			{//ou est-ce que DECCAL peut detruire cette bloc?
				
				//D'abord trouve une route
				kacisBul(xM, yM, -1, xM, yM, getOwnFirePower());
				//s'il n'y a pas de possible route, DECCAL ne posera pas de bombe.
				if (!kacisR.isEmpty())
				{
					return AI_ACTION_PUT_BOMB;
				}
			}
		}
		//inverser la direction de Contrôle.
		artis *= -1;
		//attendre sans espoir
		return AI_ACTION_DO_NOTHING;
	}
}