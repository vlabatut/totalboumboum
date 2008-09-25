package fr.free.totalboumboum.game.point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
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
		PointProcessor result = loadPointProcessorElement(root);
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
		PointProcessor result = loadPointProcessorElement(root);
		return result;
	}

    private static PointProcessor loadPointProcessorElement(Element root)
	{	// init
    	PointProcessor result;
    	Element element;
		// point processor
    	List<Element> elts = root.getChildren(); 
		element = elts.get(1);
		result = loadGeneralPointElement(element);
		// notes
		element = root.getChild(XmlTools.ELT_NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		//
		return result;
	}		

	private static PointProcessor loadGeneralPointElement(Element root)
	{	PointProcessor result = null;
		String type = root.getName();

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
		String str = root.getAttribute(XmlTools.ATT_TYPE).getValue();
		Score score  = Score.valueOf(str.toUpperCase().trim());
		// result
		PointScores result = new PointScores(score);
		return result;
	}
	private static PointConstant loadConstantElement(Element root)
	{	// value
		String str = root.getAttribute(XmlTools.ATT_VALUE).getValue();
		float value = Float.valueOf(str);
		// result
		PointConstant result = new PointConstant(value);
		return result;
	}
	
	private static PointMaximum loadMaximumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointProcessor source = loadGeneralPointElement(src);
		// result
		PointMaximum result = new PointMaximum(source);
		return result;
	}
	private static PointMinimum loadMinimumElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointProcessor source = loadGeneralPointElement(src);
		// result
		PointMinimum result = new PointMinimum(source);
		return result;
	}
	private static PointTotal loadTotalElement(Element root)
	{	// source
		Element src = (Element) root.getChildren().get(0);
		PointProcessor source = loadGeneralPointElement(src);
		// result
		PointTotal result = new PointTotal(source);
		return result;
	}
	private static PointRankings loadRankingsElement(Element root)
	{	// invert
		String str = root.getAttribute(XmlTools.ATT_INVERT).getValue();
		boolean invert = Boolean.valueOf(str);
		// sources
		ArrayList<PointProcessor> sources = new ArrayList<PointProcessor>();
		List<Element> srcs = root.getChildren();
		Iterator<Element> it = srcs.iterator();
		while(it.hasNext())
		{	Element src = it.next();
			PointProcessor source = loadGeneralPointElement(src);
			sources.add(source);
		}
		// result
		PointRankings result = new PointRankings(sources,invert);
		return result;
	}
	private static PointDiscretize loadDiscretizeElement(Element root)
	{	// share
		String str = root.getAttribute(XmlTools.ATT_EXAEQUO_SHARE).getValue();
		boolean exaequoShare = Boolean.valueOf(str);
		// source
		Element src = (Element) root.getChildren().get(0);
		PointProcessor source = loadGeneralPointElement(src);
		// thresholds
		Element thresholdsElt = root.getChild(XmlTools.ELT_THRESHOLDS);
		List<Element> thresholds = thresholdsElt.getChildren(XmlTools.ELT_THRESHOLD);
		float thresh[] = new float[thresholds.size()];
		for(int i=0;i<thresh.length;i++)
		{	Element temp = thresholds.get(i);
			str = temp.getAttribute(XmlTools.ATT_VALUE).getValue();
			float value = Float.valueOf(str);
			thresh[i] = value;
		}
		// values
		Element valuesElt = root.getChild(XmlTools.ELT_VALUES);
		List<Element> values = valuesElt.getChildren(XmlTools.ELT_VALUE);
		float vals[] = new float[values.size()];
		for(int i=0;i<vals.length;i++)
		{	Element temp = values.get(i);
			str = temp.getAttribute(XmlTools.ATT_VALUE).getValue();
			float value = Float.valueOf(str);
			vals[i] = value;
		}
		// result
		PointDiscretize result = new PointDiscretize(source,thresh,vals,exaequoShare);
		return result;
	}
	
	private static PointAddition loadAdditionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointAddition result = new PointAddition(leftSource,rightSource);
		return result;
	}
	private static PointSubtraction loadSubtractionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointSubtraction result = new PointSubtraction(leftSource,rightSource);
		return result;
	}
	private static PointMultiplication loadMultiplicationElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointMultiplication result = new PointMultiplication(leftSource,rightSource);
		return result;
	}
	private static PointDivision loadDivisionElement(Element root)
	{	// left source
		List<Element> sources = root.getChildren();
		Element leftSrc = sources.get(0);
		PointProcessor leftSource = loadGeneralPointElement(leftSrc);
		// right source
		Element rightSrc = sources.get(1);
		PointProcessor rightSource = loadGeneralPointElement(rightSrc);
		// result
		PointDivision result = new PointDivision(leftSource,rightSource);
		return result;
	}
}
