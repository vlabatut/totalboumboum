package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v1;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class Matrix {

	private static AkbulutKupelioglu monIa = null;
	
	private int width;
	private int height;
	
	private int[][] matrix;
	
	
	public Matrix(int width, int height, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		if (monIa == null) {
			monIa = ia;
		}
		
		matrix = new int[height][width];
		this.width = width;
		this.height = height;
		resetMatrix();
	}
	
	public int getWidth() throws StopRequestException
	{
		monIa.checkInterruption();
		return this.width;
	}

	public int getHeight() throws StopRequestException
	{
		monIa.checkInterruption();
		return height;
	}
	
	public int getElement(int x, int y) throws StopRequestException
	{
		monIa.checkInterruption();
		return matrix[x][y];
	}
	
	public void setElement(int x,int y, int value) throws StopRequestException
	{
		monIa.checkInterruption();
		matrix[x][y] = value;
	}
	
	public void resetMatrix() throws StopRequestException
	{
		monIa.checkInterruption();
		int i,j;
		for(i=0;i<height;i++)
		{
			monIa.checkInterruption();
			for(j=0;j<width;j++)
			{
				monIa.checkInterruption();
				matrix[i][j] = 0;
			}
		}
	}
	
}
