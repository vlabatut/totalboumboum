package org.totalboumboum.tools.xml;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DOMBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Set of methods related to XML managment.
 * 
 * @author Vincent Labatut
 */
public class XmlTools
{	
	/**
	 * Initializes all objects used when loading/recording XML files.
	 * 
	 * @throws SAXException
	 * 		Problem while loading XML schemas.
	 * @throws ParserConfigurationException
	 * 		Problem while loading XML schemas.
	 */
	public static void init() throws SAXException, ParserConfigurationException
	{	// init
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    schemaFactory.setErrorHandler(new ErrorHandler()
	    {	@Override
	    	public void error(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
	    	@Override
    		public void fatalError(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
	    	@Override
	    	public void warning(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
	    });
	    
	    // loading all schemas
//	    System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.parsers.SAXParser");
//	    System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		File folder = new File(FilePaths.getSchemasPath());
		File[] files = folder.listFiles();
		for(int i=0;i<files.length;i++)
		{	if(files[i].isFile())
			{	String name = files[i].getName();
				Schema schema = schemaFactory.newSchema(files[i]);
				// DOM parser
				DocumentBuilderFactory documentBuilderfactory = DocumentBuilderFactory.newInstance();
		        documentBuilderfactory.setNamespaceAware(true);
		        documentBuilderfactory.setIgnoringElementContentWhitespace(true);
		        documentBuilderfactory.setSchema(schema);
		        DocumentBuilder builder = documentBuilderfactory.newDocumentBuilder();
		        builder.setErrorHandler(new ErrorHandler()
		        {   @Override
			    	public void fatalError(SAXParseException e) throws SAXException
		        	{   throw e;
		        	}
		        	@Override
		        	public void error(SAXParseException e) throws SAXParseException
			    	{   throw e;
			    	}
		        	@Override
			    	public void warning(SAXParseException e) throws SAXParseException
			        {   throw e;
			        }
				});
				XmlNames.documentBuilders.put(name,builder);
	        }
		}
	}
	
	/**
	 * Opens the specified XML file, checks it using
	 * the specified XSD file, and returns the root element
	 * of the loaded data.
	 *  
	 * @param dataFile
	 * 		Data file (XML).
	 * @param schemaFile
	 * 		Schema file (XSD).
	 * @return
	 * 		Root of the XML document.
	 * 
	 * @throws SAXException
	 * 		Problem while parsing the XML file.
	 * @throws IOException
	 * 		Problem while loading the XML file or its schema.
	 */
	public static Element getRootFromFile(File dataFile, File schemaFile) throws SAXException, IOException
	{	// init
		FileInputStream in = new FileInputStream(dataFile);
		BufferedInputStream inBuff = new BufferedInputStream(in);
		
		// JAXP
		DocumentBuilder bldr = XmlNames.documentBuilders.get(schemaFile.getName());
		org.w3c.dom.Document doc;
		try
		{	doc = bldr.parse(inBuff);
		}
		catch (SAXException e)
		{	System.out.println(dataFile+" : "+schemaFile);
			throw e;
		}
		catch (IOException e)
		{	System.out.println(dataFile+" : "+schemaFile);
			throw e;
		}
		
		// JDOM
		DOMBuilder builder = new DOMBuilder();
        Document document = builder.build(doc);
		
        // root
		Element result = document.getRootElement();
		inBuff.close();
		return result;
	}

	/**
	 * Records an XML file containing the data described
	 * in the specified element. That element is considered
	 * as the root of the new document.
	 *  
	 * @param dataFile
	 * 		XML file to be created.
	 * @param schemaFile
	 * 		Associated schema file.
	 * @param root
	 * 		Root element of the XML file.
	 * 
	 * @throws IOException
	 * 		Problem while creating the XML file.
	 */
	public static void makeFileFromRoot(File dataFile, File schemaFile, Element root) throws IOException
	{	// open file stream
		FileOutputStream out = new FileOutputStream(dataFile);
		BufferedOutputStream outBuf = new BufferedOutputStream(out);
		// create document
		Document document = new Document(root);
		// schema
		String schemaPath = schemaFile.getPath();
		File racine = new File(FilePaths.getResourcesPath());
		File tempFile = new File(dataFile.getPath());
		while(!tempFile.equals(racine))
		{	tempFile = tempFile.getParentFile();
			schemaPath = ".."+File.separator+schemaPath;
		}
		// Namespace sch = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
	    Namespace sch = Namespace.getNamespace("xsi",XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
		root.addNamespaceDeclaration(sch);
		root.setAttribute("noNamespaceSchemaLocation",schemaPath,sch);
		// define output format
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		// create outputter
		XMLOutputter outputter = new XMLOutputter(format);
		// write in the stream
	    outputter.output(document,outBuf);       
	    // close the stream
	    outBuf.close();
	}
	
	/**
	 * Returns a comment node to put
	 * in an XML document.
	 * 
	 * @return
	 * 		The corresponding comment node.
	 */
	public static Comment getGplComment()
	{	StringBuffer text = new StringBuffer();
		text.append("\n");
		text.append("\tTotal Boum Boum\n");
		text.append("\tCopyright 2008-2013 Vincent Labatut\n");
		text.append("\t\n");
		text.append("\tThis file is part of Total Boum Boum.\n");
		text.append("\t\n");
		text.append("\tTotal Boum Boum is free software: you can redistribute it and/or modify\n");
		text.append("\tit under the terms of the GNU General Public License as published by\n");
		text.append("\tthe Free Software Foundation, either version 2 of the License, or\n");
		text.append("\t(at your option) any later version.\n");
		text.append("\t\n");
		text.append("\tTotal Boum Boum is distributed in the hope that it will be useful,\n");
		text.append("\tbut WITHOUT ANY WARRANTY; without even the implied warranty of\n");
		text.append("\tMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n");
		text.append("\tGNU General Public License for more details.\n");
		text.append("\t\n");
		text.append("\tYou should have received a copy of the GNU General Public License\n");
		text.append("\talong with Total Boum Boum.  If not, see http://www.gnu.org/licenses.\n");
		Comment result = new Comment(text.toString());
		return result;
	}
	
	/**
	 * Parses a string of the form "1,4,6,10-16,58"
	 * and returns the corresponding integers under the
	 * form of a set. In the previous example, the result
	 * would therefore be {1,4,6,10,11,12,13,14,15,16,58}.
	 * 		
	 * @param multint
	 * 		Original string.
	 * @return
	 * 		Corresponding integers.
	 */
	public static Set<Integer> parseMultipleInteger(String multint)
	{	Set<Integer> result = new TreeSet<Integer>();
		String temp[] = multint.split(",");
		for(String tmp: temp)
		{	if(tmp.contains("-"))
			{	String temp2[] = tmp.split("-");
				int from = Integer.parseInt(temp2[0]);
				int to = Integer.parseInt(temp2[1]);
				if(from>to)
				{	int tmp2 = to;
					to = from;
					from = tmp2;
				}
				for(int i=from;i<=to;i++)
					result.add(i);
			}
			else
			{	int value = Integer.parseInt(tmp);
				result.add(value);
			}
		}
		return result;
	}
}
