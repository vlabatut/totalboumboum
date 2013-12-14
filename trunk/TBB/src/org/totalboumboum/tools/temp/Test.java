package org.totalboumboum.tools.temp;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.net.*;

/**
 * Class defined to perform some network-related tests.
 * 
 * @author Vincent
 */
@SuppressWarnings("javadoc")
public class Test
{
	public static void main(String[] args)
	{	
		ipAddress();
		//macAddresses();
	}
	
	public static void ipAddress()
	{
		try
		{	InetAddress thisIp = InetAddress.getLocalHost();
			System.out.println("getHostAddress:" + thisIp.getHostAddress());
			System.out.println("getHostName:" + thisIp.getHostName());
		}
		catch (Exception e)
		{	e.printStackTrace();
		}
	}
	
	public static void macAddresses()
	{	try
		{	InetAddress address = InetAddress.getLocalHost();
			//InetAddress address = InetAddress.getByName("192.168.46.53");
		 
			/*
			 * Get NetworkInterface for the current host and then read the
			 * hardware address.
			 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			if (ni != null)
			{	byte[] mac = ni.getHardwareAddress();
				if (mac != null)
				{	/*
				 	 * Extract each array of mac address and convert it to hexa with the
				 	 * following format 08-00-27-DC-4A-9E.
				 	 */
					for (int i = 0; i < mac.length; i++)
					{	System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
					}
				}
				else
				{	System.out.println("Address doesn't exist or is not accessible.");
				}
			}
			else
			{	System.out.println("Network Interface for the specified address is not found.");
			}
		}
		catch (UnknownHostException e)
		{	e.printStackTrace();
		}
		catch (SocketException e)
		{	e.printStackTrace();
		}
	}
}