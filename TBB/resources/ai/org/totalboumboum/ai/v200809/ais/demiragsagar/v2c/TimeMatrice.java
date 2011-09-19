package org.totalboumboum.ai.v200809.ais.demiragsagar.v2c;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Dogus Burcu Demirag
 * @author Zeynep Sagar
 *
 */
public class TimeMatrice {
	private List<AiTile> caseBombes;
	private long timeMatrice[][];
	private AiZone zone;
	private int defaultPortee;
	private long durationNormale;
	private int extendTime;
	private boolean debug;
	
	ArtificialIntelligence ai;
	
	public TimeMatrice(AiZone zone,int defaultPortee, ArtificialIntelligence ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		this.zone=zone;
		this.timeMatrice=new long[20][20];
		this.defaultPortee=defaultPortee;
		this.extendTime=100;
		this.durationNormale=2400;
		this.debug=false;
		createTimeMatrice();
	}
	public void setDefaultPortee(int defaultPortee) throws StopRequestException {
		ai.checkInterruption();
		this.defaultPortee=defaultPortee;
	}
	/*
	 * Donner une valeur a une case de la matrice du temps
	 */
	public void putTime(int col,int line,long time) throws StopRequestException {
		ai.checkInterruption();
		this.timeMatrice[col][line]=time;
	};
	/*
	 * ça nous donne la matrice du temps
	 * Une case contient 0 s'il n y'a aucun danger 
	 * Sinon il contient le temps restant d'une bombe
	 */
	public long getTime(int col,int line) throws StopRequestException {
		ai.checkInterruption();
		return this.timeMatrice[col][line];
	}
	public void putTime(AiTile tile,long time) throws StopRequestException {
		ai.checkInterruption();
		this.putTime(tile.getCol(),tile.getLine(),time);
	}
	public long getTime(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		return this.getTime(tile.getCol(),tile.getLine());
	}
	/*
	 * Cr�ation de la matrice du temps
	 */
	public void createTimeMatrice() throws StopRequestException {
		ai.checkInterruption();
		// ajout des murs dans la matrice pour une seule fois
		int i, j;
		for (j = 0; j < 15; j++)
		{	ai.checkInterruption();
			for (i = 0; i < 17; i++)
			{	ai.checkInterruption();
				if (Functions.hasWall(this.zone.getTile(j, i),ai))
					this.putTime(i,j,-1);
				else
					this.putTime(i,j,0);
			}
		}
	}
	/*
	 * S'il ya un mur dans une case on voit -1 
	 * On augmente la valeur d'une case selon le nombre des bombes qui affectent cette case
	 */
	public int[][] getBombMatrice(AiZone zone) throws StopRequestException {
		ai.checkInterruption();
		int[][] maMatrice = new int[17][16];
		int etki;
		int i, j;
		for (j = 0; j < 15; j++)
		{	ai.checkInterruption();
			for (i = 0; i < 17; i++)
			{	ai.checkInterruption();
				if (Functions.hasWall(zone.getTile(j, i),ai))
					maMatrice[i][j] = -1;
				else
					maMatrice[i][j] = 0;
			}
		}
		for (AiTile t : this.caseBombes) {
			ai.checkInterruption();
			int x = t.getCol();
			int y = t.getLine();
				boolean up = false, down = false, left = false, right = false;
				maMatrice[x][y]++;
				for (etki = 1; etki <= 5; etki++) {
					ai.checkInterruption();
					if (x + etki < 16)
						if (maMatrice[x + etki][y] != -1 && right == false)
							maMatrice[x + etki][y]++;
						else
							right = true;
					if (x - etki > 0)
						if (maMatrice[x - etki][y] != -1 && left == false)
							maMatrice[x - etki][y]++;
						else
							left = true;
					if (y + etki < 14)
						if (maMatrice[x][y + etki] != -1 && down == false)
							maMatrice[x][y + etki]++;
						else
							down = true;
					if (y - etki > 0)
						if (maMatrice[x][y - etki] != -1 && up == false)
							maMatrice[x][y - etki]++;
						else
							up = true;
				}
		}

		return maMatrice;

	}
	public void printTimeMatrice() throws StopRequestException {
		ai.checkInterruption();
		Functions.printMatrice(this.timeMatrice,ai);
	}
	public void updateTimeMatrice(List<AiTile> nouvelleBombes) throws StopRequestException {
		ai.checkInterruption();
		if (this.debug)
			this.printTimeMatrice();
		// update matrice
		// il faut soustraire des cas avant
		int i, j;
		long elapsedTime = this.zone.getElapsedTime();
		if(debug)
			System.out.println("Elapsed time : "+elapsedTime);
		if (elapsedTime > 0) {
			for (j = 0; j < 15; j++)
			{	ai.checkInterruption();
				for (i = 0; i < 17; i++)
				{	ai.checkInterruption();
					if(zone.getTile(j, i).getFires().isEmpty())
					{						
						if(this.getTime(i,j)==0 && this.caseBombes!=null)
						{
							int port;
							for (AiTile temp : this.caseBombes) {
								ai.checkInterruption();
								if(temp.getBombs().isEmpty())
									port=this.defaultPortee;
								else
									port=temp.getBombs().iterator().next().getRange();
								long nombre = this.getTime(temp);
								this.corrigeEffetMatrice(i,j,temp,port,nombre);
								
							}
						}
						if (this.getTime(i,j) > 0) {
							// if(!this.zone.getTile(j, i).getBombs().isEmpty()
							// || isAffectedByBomb(this.zone.getTile(j, i)))
							// {
							
							this.putTime(i, j, this.getTime(i,j) - elapsedTime);
							if (this.getTime(i,j) < 0) {
								if (this.zone.getTile(j,i).getItem()==null) {
									this.putTime(i,j,0);
								}
								if(zone.getTile(j, i).getBombs().isEmpty())
									this.putTime(i,j,0);
								else{
									if(!zone.getTile(j, i).getBombs().iterator().next().isWorking()){
										this.putTime(i,j,this.extendTime);
										this.diffuseEffetMatrice(zone.getTile(j, i), zone.getTile(j, i).getBombs().iterator().next().getRange(),this.extendTime);
									}
									
								}
								
							}
						}
						else if (this.getTime(i,j) == -1 && Functions.hasWall(this.zone.getTile(j, i),ai)){
							this.putTime(i,j,-1);;
						}
						else if(this.getTime(i,j) == -1 && !Functions.hasWall(this.zone.getTile(j, i),ai))
							this.putTime(i,j,0);;
					}
					else
						this.putTime(i,j,0);
				}
			}
		
			if(this.debug){
				Iterator<AiTile> ita = this.caseBombes.iterator();
				while (ita.hasNext())
				{	ai.checkInterruption();
					System.out.println("Bombe ancient" + ita.next().getCol());				
				}
			}
			if (!nouvelleBombes.isEmpty()) {
				// si il existe au moins une bombe
				for(AiTile temp:nouvelleBombes) {
					ai.checkInterruption();
					if (this.getTime(temp)>= 0)
						// avant la bas il n'y avait pas de bombe 
						// ou quelque chose qui affecte cette case
						// Il ya une nouvelle bombe! 
						if (!this.caseBombes.contains(temp) || this.caseBombes.isEmpty())
							// if(this.firstBomb)
							// this.firstBomb=false;
							this.placerNouvelleBombe(temp);
				}
			}
			this.caseBombes = nouvelleBombes;
		}
	}
	/*
	 * Controle si les effets des bombes sont vrais
	 */
	private void corrigeEffetMatrice(int col,int lig,AiTile temp2,int port,long nombre) throws StopRequestException {
		ai.checkInterruption();
		boolean up=true,down=true,left=true,right=true;
		int step=1;
		while(up && step<=port)
		{	ai.checkInterruption();
			if(this.timeMatrice[temp2.getCol()][temp2.getLine()-step]==-1)
				up=false;
				else
				{
					if(temp2.getCol()==col && temp2.getLine()-step==lig)
						if (nombre < this.timeMatrice[temp2.getCol()][temp2.getLine()-step]|| this.timeMatrice[temp2.getCol()][temp2.getLine()-step] == 0)
							this.timeMatrice[temp2.getCol()][temp2.getLine()-step] = nombre;
	
				}
			step++;
		}
		step=1;
		while(down && step<=port)
		{	ai.checkInterruption();
			if(this.timeMatrice[temp2.getCol()][temp2.getLine()+step]==-1)
				down=false;
				else
				{	
					if(temp2.getCol()==col && temp2.getLine()+step==lig)
						if (nombre < this.timeMatrice[temp2.getCol()][temp2.getLine()+step]|| this.timeMatrice[temp2.getCol()][temp2.getLine()+step] == 0)
							this.timeMatrice[temp2.getCol()][temp2.getLine()+step] = nombre;
				}
			step++;
		}
		step=1;
		while(left && step<=port)
		{	ai.checkInterruption();
			if(this.timeMatrice[temp2.getCol()-step][temp2.getLine()]==-1)
				left=false;
				else
				{
					if(temp2.getCol()-step==col && temp2.getLine()==lig)
						if (nombre < this.timeMatrice[temp2.getCol()-step][temp2.getLine()]|| this.timeMatrice[temp2.getCol()-step][temp2.getLine()] == 0)
							this.timeMatrice[temp2.getCol()-step][temp2.getLine()] = nombre;
				}
			step++;
		}
		step=1;
		while(right && step<=port)
		{	ai.checkInterruption();
			if(this.timeMatrice[temp2.getCol()+step][temp2.getLine()]==-1)
				right=false;
				else
				{	
					if(temp2.getCol()+step==col && temp2.getLine()==lig)
						if (nombre < this.timeMatrice[temp2.getCol()+step][temp2.getLine()]|| this.timeMatrice[temp2.getCol()+step][temp2.getLine()] == 0)
							this.timeMatrice[temp2.getCol()+step][temp2.getLine()] = nombre;
				}
			step++;
		}
		right=true;
		left=true;
		up=true;
		down=true;	
	}

	public void placerNouvelleBombe(AiTile temp) throws StopRequestException {
		ai.checkInterruption();
		if(this.debug) System.out.println("nouvelle bombe");
		// bu 2400
		int portee = this.defaultPortee;
		if(!temp.getBombs().isEmpty())
			portee=temp.getBombs().iterator().next().getRange();
		if (this.timeMatrice[temp.getCol()][temp.getLine()] == 0) {
			//Dans la case ou on va poser une bombe ,il n'ya pas de bombe ni un effet
			// posez la bombe(2400)
			this.putTime(temp,this.durationNormale);
			// L'intersection va etre le minimum
			boolean up = true, left = true, right = true, down = true;
			int step = 1;
			//Regardez les effets :Si il ya un -1 dans un des 4 directions
			// stop
			while (up && step <= portee) {
				ai.checkInterruption();
				if (this.timeMatrice[temp.getCol()][temp.getLine() - step] == -1 || zone.getTile(temp.getLine()-step, temp.getCol()).getItem()!=null)
					// stop
					up = false;
				else // Si 0 alors durationNormale
				// dolu ise kucuk olan�
				// Si plein ,la petite 
				if (this.timeMatrice[temp.getCol()][temp.getLine() - step] == 0)
					this.timeMatrice[temp.getCol()][temp.getLine() - step] = this.durationNormale;
				step++;
			}
			step = 1;
			while (down && step <= portee) {
				ai.checkInterruption();
				if (this.timeMatrice[temp.getCol()][temp.getLine() + step] == -1 || zone.getTile(temp.getLine()+step, temp.getCol()).getItem()!=null)
					// stop
					down = false;
				else // Si 0 durationNormale
				// si plein,la petite
				if (this.timeMatrice[temp.getCol()][temp.getLine() + step] == 0)
					this.timeMatrice[temp.getCol()][temp.getLine() + step] = this.durationNormale;
				step++;
			}
			step = 1;
			while (left && step <= portee) {
				ai.checkInterruption();
				if (this.timeMatrice[temp.getCol() - step][temp.getLine()] == -1 || zone.getTile(temp.getLine(), temp.getCol()-step).getItem()!=null)
					// stop
					left = false;
				else //  Si 0 durationNormale
				// si plein,la petite
				if (this.timeMatrice[temp.getCol() - step][temp.getLine()] == 0)
					this.timeMatrice[temp.getCol() - step][temp.getLine()] = this.durationNormale;
				step++;
			}
			step = 1;
			while (right && step <= portee) {
				ai.checkInterruption();
				if (this.timeMatrice[temp.getCol() + step][temp.getLine()] == -1 || zone.getTile(temp.getLine(), temp.getCol()+step).getItem()!=null)
					// stop
					right = false;
				else // Si 0 durationNormale
				// si plein,la petite
				if (this.timeMatrice[temp.getCol() + step][temp.getLine()] == 0)
					this.timeMatrice[temp.getCol() + step][temp.getLine()] = this.durationNormale;
				step++;
			}

		} 
		else if (this.timeMatrice[temp.getCol()][temp.getLine()] > 0)
			this.diffuseEffetMatrice(temp, portee, this.timeMatrice[temp.getCol()][temp.getLine()]);
		//this.printTimeMatrice(this.timeMatrice);

	}
	

	public void diffuseEffetMatrice(AiTile temp, int portee, long min) throws StopRequestException {
		ai.checkInterruption();
		//Allez dans les 4 directions dans la longeur de la portee
		boolean up = true, left = true, right = true, down = true;
		int step = 1;
		while (up && step <= portee) {			
			ai.checkInterruption();
			if (this.timeMatrice[temp.getCol()][temp.getLine() - step] == -1)
				// stop
				up = false;
			else if (this.caseBombes.contains(this.zone.getTile(temp.getLine() // Est-ce qu'il ya une bombe?
					- step, temp.getCol()))) {
				if (min < this.timeMatrice[temp.getCol()][temp.getLine() - step]) {
					this.diffuseEffetMatrice(this.zone.getTile(temp.getLine() - step, temp.getCol()), 5, min);
					up = false;
				}
			}
			else if (this.timeMatrice[temp.getCol()][temp.getLine() - step] > 0) { 
				// pas de bombe
				// est-ce qu'il ya un effet?
					if (this.timeMatrice[temp.getCol()][temp.getLine() - step] > min)
						this.timeMatrice[temp.getCol()][temp.getLine() - step] = min;
			}
			else if (this.timeMatrice[temp.getCol()][temp.getLine()	- step] == 0)
					this.timeMatrice[temp.getCol()][temp.getLine() - step] = min;
			step++;
		}
		step = 1;
		while (down && step <= portee) {
			ai.checkInterruption();
			if (this.timeMatrice[temp.getCol()][temp.getLine() + step] == -1)
				down = false;
			else if (this.caseBombes.contains(this.zone.getTile(temp.getLine()
					+ step, temp.getCol()))) {
				if (min < this.timeMatrice[temp.getCol()][temp.getLine() + step]) {
					this.diffuseEffetMatrice(this.zone.getTile(temp.getLine()
							+ step, temp.getCol()), 5, min);
					down = false;
				}
			} 
			else if (this.timeMatrice[temp.getCol()][temp.getLine() + step] > 0) {
					if (this.timeMatrice[temp.getCol()][temp.getLine() + step] > min)
						this.timeMatrice[temp.getCol()][temp.getLine() + step] = min;
			}
			else if (this.timeMatrice[temp.getCol()][temp.getLine() + step] == 0)
					this.timeMatrice[temp.getCol()][temp.getLine() + step] = min;
			step++;
		}
		step = 1;
		while (left && step <= portee) {
			ai.checkInterruption();
			if (this.timeMatrice[temp.getCol() - step][temp.getLine()] == -1)
				// stop
				left = false;
			else // Est-ce qu'il ya une bombe?
			if (this.caseBombes.contains(this.zone.getTile(temp.getLine(), temp.getCol() - step))) {
				if (min < this.timeMatrice[temp.getCol() - step][temp.getLine()]) {
					this.diffuseEffetMatrice(this.zone.getTile(temp.getLine(),temp.getCol() - step), 5, min);
					left = false;
				}
			}
				// Pas de bombe
				// Est-ce qu'il ya un effet
			else if (this.timeMatrice[temp.getCol() - step][temp.getLine()] > 0) {
					if (this.timeMatrice[temp.getCol() - step][temp.getLine()] > min)
						this.timeMatrice[temp.getCol() - step][temp.getLine()] = min;
			}
			else if (this.timeMatrice[temp.getCol() - step][temp.getLine()] == 0)
					this.timeMatrice[temp.getCol() - step][temp.getLine()] = min;			
			step++;
		}
		step = 1;
		while (right && step <= portee) {
			ai.checkInterruption();
			if (this.timeMatrice[temp.getCol() + step][temp.getLine()] == -1)
				right = false;
			else // Est-ce qu'il ya une bombe?
			if (this.caseBombes.contains(this.zone.getTile(temp.getLine(), temp.getCol() + step))) {
				if (min < this.timeMatrice[temp.getCol() + step][temp.getLine()]) {					
					this.diffuseEffetMatrice(this.zone.getTile(temp.getLine(),
							temp.getCol() + step), 5, min);
					right = false;
				}
			} 
			else if (this.timeMatrice[temp.getCol() + step][temp.getLine()] > 0) {
					if (this.timeMatrice[temp.getCol() + step][temp.getLine()] > min)
						this.timeMatrice[temp.getCol() + step][temp.getLine()] = min;
			}
			else if (this.timeMatrice[temp.getCol() + step][temp.getLine()] == 0)
					this.timeMatrice[temp.getCol() + step][temp.getLine()] = min;
			
			step++;
		}
	}


}
