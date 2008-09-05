package fr.free.totalboumboum.game.point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.score.Score;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class PointProcessorLoader
{
	public static PointProcessor loadPointProcessorFromFilePath(String folderPath) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_POINT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		Element elt = XmlTools.getChildElement(root);
		PointProcessor result = loadPointProcessorElement(elt);
		return result;
	}

	public static PointProcessor loadPointProcessorFromName(String name) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getPointsPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+name+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_POINT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		Element elt = XmlTools.getChildElement(root);
		PointProcessor result = loadPointProcessorElement(elt);
		return result;
	}

	private static PointProcessor loadPointProcessorElement(Element root)
	{	PointProcessor result = null;
		String type = root.getTagName();

		if(type.equals(XmlTools.ELT_POINTS))
			result = loadPointsElement(root);

		else if(type.equals(XmlTools.ELT_SCORES))
			result = loadScoresElement(root);
		else if(type.equals(XmlTools.ELT_CONSTANT))
			result = loadConstantElement(root);

		else if(type.equals(XmlTools.ELT_MAXIMUM))
			result = loadMaximumElement(root);
		else if(type.equals(XmlTools.ELT_MINIMUM))
			result = loadMinimumElement(root);		
		else if(type.equals(XmlTools.ELT_TOTAL))
			result = loadTotalElement(root);
		else if(type.equals(XmlTools.ELT_RANKINGS))
			result = loadRankingsElement(root);
		else if(type.equals(XmlTools.ELT_DISCRETIZE))
			result = loadDiscretizeElement(root);
		
		else if(type.equals(XmlTools.ELT_ADDITION))
			result = loadAdditionElement(root);
		else if(type.equals(XmlTools.ELT_SUBTRACTION))
			result = loadSubtractionElement(root);
		else if(type.equals(XmlTools.ELT_MULTIPLICATION))
			result = loadMultiplicationElement(root);
		else if(type.equals(XmlTools.ELT_DIVISION))
			result = loadDivisionElement(root);
		
		return result;
	}

	private static PointPoints loadPointsElement(Element root)
	{	PointPoints result = new PointPoints();
		return result;
	}

	private static PointScores loadScoresElement(Element root)
	{	// type
		String str = root.getAttribute(XmlTools.ATT_TYPE);
		Score score  = Score.valueOf(str.toUpperCase().trim());
		// result
		PointScores result = new PointScores(score);
		return result;
	}
	private static PointConstant loadConstantElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE);
		float value = Float.valueOf(str);
		// result
		PointConstant result = new PointConstant(value);
		return result;
	}
	
	private static PointMaximum loadMaximumElement(Element root)
	{	// source
		Element src = XmlTools.getChildElement(root);
		PointProcessor source = loadPointProcessorElement(src);
		// result
		PointMaximum result = new PointMaximum(source);
		return result;
	}
	private static PointMinimum loadMinimumElement(Element root)
	{	// source
		Element src = XmlTools.getChildElement(root);
		PointProcessor source = loadPointProcessorElement(src);
		// result
		PointMinimum result = new PointMinimum(source);
		return result;
	}
	private static PointTotal loadTotalElement(Element root)
	{	// source
		Element src = XmlTools.getChildElement(root);
		PointProcessor source = loadPointProcessorElement(src);
		// result
		PointTotal result = new PointTotal(source);
		return result;
	}
	private static PointRankings loadRankingsElement(Element root)
	{	// invert
		String str = root.getAttribute(XmlTools.ATT_INVERT);
		boolean invert = Boolean.valueOf(str);
		// source
		Element src = XmlTools.getChildElement(root);
		PointProcessor source = loadPointProcessorElement(src);
		// result
		PointRankings result = new PointRankings(source,invert);
		return result;
	}
	private static PointDiscretize loadDiscretizeElement(Element root)
	{	// source
		Element src = XmlTools.getChildElement(root);
		PointProcessor source = loadPointProcessorElement(src);
		// thresholds
		Element thresholdsElt = XmlTools.getChildElement(root,XmlTools.ELT_THRESHOLDS);
		ArrayList<Element> thresholds = XmlTools.getChildElements(thresholdsElt,XmlTools.ELT_THRESHOLD);
		float thresh[] = new float[thresholds.size()];
		for(int i=0;i<thresh.length;i++)
		{	Element temp = thresholds.get(i);
			String str = temp.getAttribute(XmlTools.ATT_VALUE);
			float value = Float.valueOf(str);
			thresh[i] = value;
		}
		// values
		Element valuesElt = XmlTools.getChildElement(root,XmlTools.ELT_VALUES);
		ArrayList<Element> values = XmlTools.getChildElements(valuesElt,XmlTools.ELT_VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			String str = temp.getAttribute(XmlTools.ATT_VALUE);
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointDiscretize result = new PointDiscretize(source,thresh,vals);
		return result;
	}
	
	private static PointAddition loadAdditionElement(Element root)
	{	// left source
		ArrayList<Element> sources = XmlTools.getChildElements(root);
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadPointProcessorElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadPointProcessorElement(rightSrc);
		// result
		PointAddition result = new PointAddition(leftSource,rightSource);
		return result;
	}
	private static PointSubtraction loadSubtractionElement(Element root)
	{	// left source
		ArrayList<Element> sources = XmlTools.getChildElements(root);
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadPointProcessorElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadPointProcessorElement(rightSrc);
		// result
		PointSubtraction result = new PointSubtraction(leftSource,rightSource);
		return result;
	}
	private static PointMultiplication loadMultiplicationElement(Element root)
	{	// left source
		ArrayList<Element> sources = XmlTools.getChildElements(root);
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadPointProcessorElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadPointProcessorElement(rightSrc);
		// result
		PointMultiplication result = new PointMultiplication(leftSource,rightSource);
		return result;
	}
	private static PointDivision loadDivisionElement(Element root)
	{	// left source
		ArrayList<Element> sources = XmlTools.getChildElements(root);
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadPointProcessorElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadPointProcessorElement(rightSrc);
		// result
		PointDivision result = new PointDivision(leftSource,rightSource);
		return result;
	}
}
