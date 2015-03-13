package org.totalboumboum.tools.network;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

/**
 * Network-related common methods.
 * 
 * @author Vincent Labatut
 */
public class NetworkTools
{	
	/**
	 * Checks if the specified string corresponds
	 * to an actual ip address.
	 * 
	 * @param ip
	 * 		String supposed to represent an ip address.
	 * @return
	 * 		{@code true} iff the ip is correct.
	 */
	public static boolean validateIPAddress(String ip)
	{	boolean result = true;
	
		String[] chunks = ip.split("\\.");
	    if(chunks.length!=4)
	    	result = false;
	    else
	    {	int i = 0;
	    	try
	    	{	do
		    	{	int value = Integer.parseInt(chunks[i]);
		    		result = value>=0 && value<=255;
		    		i++;
		    	}	
	    		while(i<4 && result);
	    	}
	    	catch(NumberFormatException e)
	    	{	result = false;
	    	}
	    }
	    
	    return result;
	}

	/**
	 * Checks if the specified string corresponds
	 * to an actual ip address containing a valid port.
	 * 
	 * @param ip
	 * 		String supposed to represent an ip address.
	 * @return
	 * 		{@code true} iff the ip has a valud port.
	 */
	public static boolean validateIPAddressPort(String ip)
	{	boolean result = true;
	
		String[] chunks = ip.split(":");
	    if(chunks.length!=2)
	    	result = false;
	    else
	    {	result = validateIPAddress(chunks[0]);
	    	try
	    	{	@SuppressWarnings("unused")
	    		int port = Integer.parseInt(chunks[1]);
	    	}
	    	catch(NumberFormatException e)
	    	{	result = false;
	    	}
	    }
	    
	    return result;
	}
}