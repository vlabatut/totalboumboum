package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v1;

import java.awt.Color;
import java.text.DecimalFormat;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class MatriceCalc {
	protected GocmenogluHekimoglu monIa;
	protected int zoneh,zonew;
	protected double matrix[][];
	
	/** une méthode bidon pour l'exemple */
	public MatriceCalc(GocmenogluHekimoglu monIa) throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		 
		this.monIa = monIa;	
		this.zoneh = monIa.getPercepts().getHeight();
		this.zonew = monIa.getPercepts().getWidth();
		this.setMatrix(this.getNulMatrix());
	}
	
	public double[][] getNulMatrix() throws StopRequestException{
		monIa.checkInterruption();
		
		double[][] mtx = new double[zoneh][zonew];
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				mtx[i][j] = 0;
			}
		}

		return mtx;
	}
	
	public void calculate() throws StopRequestException {
	}

	public void setMatrix(double matrix[][]) throws StopRequestException {
		monIa.checkInterruption();
		
		this.matrix = matrix;
	}

	public double[][] getMatrix() throws StopRequestException {
		monIa.checkInterruption();
		
		return matrix;
	}
	
	public void addWithWeight(MatriceCalc mtx,double weight) throws StopRequestException {
		monIa.checkInterruption();
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				matrix[i][j] += mtx.getMatrix()[i][j]*weight;
			}
		}
	}
	
	public void afficheText() throws StopRequestException {
		monIa.checkInterruption();
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				
				monIa.getOutput().setTileText(i,j, df.format(matrix[i][j]));
			}
		}
	}
	
	public void afficheColor() throws StopRequestException {
		monIa.checkInterruption();
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				double val = matrix[i][j];
				if(val <=0){
					monIa.getOutput().setTileColor(i, j, new Color( Math.min((int) (val*-255), 255) ,0,0));
				}else{
					monIa.getOutput().setTileColor(i, j, new Color(0, Math.min((int) (val*255), 255) ,0));
				}
			}
		}
	}
}
