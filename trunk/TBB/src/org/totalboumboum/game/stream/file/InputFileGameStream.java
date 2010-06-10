package org.totalboumboum.game.stream.file;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.totalboumboum.game.stream.InputGameStream;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

public class InputFileGameStream extends InputGameStream
{	
	public InputFileGameStream()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// FOLDER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String folder = null;

	public String getFolder()
	{	return folder;		
	}
	
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// INPUT				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	public void initReplaying() throws IOException, ClassNotFoundException
	{	String folderPath = FilePaths.getReplaysPath() + File.separator + folder;
		String filePath = folderPath + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_DATA;
		File file = new File(filePath);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
//		ZipInputStream inZip = new ZipInputStream(inBuff);
//		in = new ObjectInputStream(inZip);
		in = new ObjectInputStream(inBuff);
		
		super.initReplaying();
	}

	/////////////////////////////////////////////////////////////////
	// PREVIEW				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage preview = null;
	
	public BufferedImage getPreview()
	{	return preview;		
	}
	
	public void setPreview(BufferedImage preview)
	{	this.preview = preview;		
	}
}
