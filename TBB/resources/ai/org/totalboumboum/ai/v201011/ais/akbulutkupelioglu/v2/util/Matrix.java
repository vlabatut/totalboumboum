package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util;


import java.util.ArrayList;
import java.util.List;

import java.util.Collections;


import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.comparator.CoordinateComparator;

/**
 * An integer matrix.
 * 
 * @author Yasa Akbulut
 * 
 */
@SuppressWarnings("deprecation")
public class Matrix implements Cloneable
{

	private AkbulutKupelioglu monIa = null;

	private int width;
	private int height;

	private int[][] matrix;

	/**
	 * Creates a new matrix with the specified dimensions.
	 * 
	 * @param width
	 *            width of the matrix
	 * @param height
	 *            height of the matrix
	 * @param ia
	 *            AkbulutKupelioglu using this matrix. (for checkInterruption())
	 * @throws StopRequestException
	 */
	public Matrix(int width, int height, AkbulutKupelioglu ia)
			throws StopRequestException
	{
		ia.checkInterruption();
		matrix = new int[height][width];
		this.width = width;
		this.height = height;
		resetMatrix(ia);
	}

	/**
	 * Returns the width of the matrix.
	 * 
	 * @return width of the matrix.
	 * @throws StopRequestException
	 */
	public int getWidth() throws StopRequestException
	{
		monIa.checkInterruption();
		return this.width;
	}

	/**
	 * Returns the height of the matrix.
	 * 
	 * @return height of the matrix.
	 * @throws StopRequestException
	 */
	public int getHeight() throws StopRequestException
	{
		monIa.checkInterruption();
		return height;
	}

	/**
	 * Gets the element at the specified index.
	 * 
	 * @param x
	 *            the row in which the element is stored
	 * @param y
	 *            the column in which the element is stored
	 * @return the element at the specified index
	 * @throws StopRequestException
	 */
	public int getElement(int x, int y) throws StopRequestException
	{
		monIa.checkInterruption();
		return matrix[x][y];
	}

	/**
	 * Gets the element that has the same coordinates as an AiTile.
	 * 
	 * @param tile
	 *            the AiTile corresponding to the coordinates of the element
	 * @return the element at the specified coordinate
	 * @throws StopRequestException
	 */
	public int getElement(AiTile tile) throws StopRequestException
	{
		monIa.checkInterruption();
		return matrix[tile.getLine()][tile.getCol()];
	}

	/**
	 * Sets the element at the specified index.
	 * 
	 * @param x
	 *            the row in which the element will be stored
	 * @param y
	 *            the column in which the element will be stored
	 * @param value
	 *            the value to be stored
	 * @throws StopRequestException
	 */
	public void setElement(int x, int y, int value) throws StopRequestException
	{
		monIa.checkInterruption();
		matrix[x][y] = value;
	}

	/**
	 * Sets the element that has the same coordinates as an AiTile.
	 * 
	 * @param tile
	 *            the AiTile corresponding to the coordinates of the element
	 * @param value
	 *            value to be stored
	 * @throws StopRequestException
	 */
	public void setElement(AiTile tile, int value) throws StopRequestException
	{
		monIa.checkInterruption();
		matrix[tile.getLine()][tile.getCol()] = value;
	}

	/**
	 * Increments an element by a specified value.
	 * 
	 * @param x
	 *            the row in which the element is stored
	 * @param y
	 *            the column in which the element is stored
	 * @param value
	 *            the value by which the element will be incremented
	 * @throws StopRequestException
	 */
	public void addToElement(int x, int y, int value)
			throws StopRequestException
	{
		monIa.checkInterruption();
		matrix[x][y] += value;
	}

	/**
	 * Increments an element by a specified value.
	 * 
	 * @param tile
	 *            the AiTile corresponding to the coordinates of the element
	 * @param value
	 *            the value by which the element will be incremented
	 * @throws StopRequestException
	 */
	public void addToElement(AiTile tile, int value)
			throws StopRequestException
	{
		monIa.checkInterruption();
		matrix[tile.getLine()][tile.getCol()] += value;
	}

	/**
	 * Resets the matrix, setting all values to 0.
	 * @param ia 
	 * 
	 * @throws StopRequestException
	 */
	public void resetMatrix(AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		monIa = ia;
		int i, j;
		for(i = 0; i < height; i++)
		{
			monIa.checkInterruption();
			for(j = 0; j < width; j++)
			{
				monIa.checkInterruption();
				matrix[i][j] = 0;
			}
		}
	}
	
	public Coordinate getMaxElementCoords() throws StopRequestException
	{
		monIa.checkInterruption();
		int max=0;
		int maxi=0,maxj=0;
		int i=0, j=0;
		for(i = 0; i < height; i++)
		{
			monIa.checkInterruption();
			for(j = 0; j < width; j++)
			{
				monIa.checkInterruption();
				if(matrix[i][j] > max)
				{
					max = matrix[i][j];
					maxi=i;
					maxj=j;
				}
			}
		}
		return new Coordinate(maxi, maxj, max, monIa);
		
	}

	public List<Coordinate> getMax(int count) throws StopRequestException
	{
		monIa.checkInterruption();
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for(int i=0; i<height; i++)
		{
			monIa.checkInterruption();
			for(int j=0; j<width; j++)
			{
				monIa.checkInterruption();
				if(matrix[i][j]>=0)
					coords.add(new Coordinate(i,j, matrix[i][j], monIa));
			}
		}

		Collections.sort(coords, new CoordinateComparator());
		Collections.reverse(coords);
		return coords.subList(0, Math.max(count, coords.size()));
	}
	public Coordinate[] getSafe() throws StopRequestException
	{
		monIa.checkInterruption();
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for(int i=0; i<height; i++)
		{
			monIa.checkInterruption();
			for(int j=0; j<width; j++)
			{
				monIa.checkInterruption();
				if(matrix[i][j]>=0)
					coords.add(new Coordinate(i,j, matrix[i][j], monIa));
			}
		}
		return (Coordinate[])coords.toArray(new Coordinate[coords.size()]);	
	}
	

	public Matrix getCopy() throws StopRequestException
	{
		monIa.checkInterruption();
		Matrix result = new Matrix(width, height, monIa);
		for(int i=0; i<height; i++)
		{
			monIa.checkInterruption();
			for(int j=0; j<width; j++)
			{
				monIa.checkInterruption();
				result.setElement(i,j, matrix[i][j]);
			}
		}
		
		return result;
	}
}