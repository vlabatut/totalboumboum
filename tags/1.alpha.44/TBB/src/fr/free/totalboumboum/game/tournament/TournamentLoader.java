package fr.free.totalboumboum.game.tournament;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.tournament.cup.CupTournamentLoader;
import fr.free.totalboumboum.game.tournament.league.LeagueTournamentLoader;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournamentLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournamentLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class TournamentLoader
{	
	private static final String CUP = "cup";
	private static final String LEAGUE = "league";
	private static final String SEQUENCE = "sequence";
	private static final String SINGLE = "single";

	public static AbstractTournament loadTournamentFromFolderPath(String folderPath, Configuration configuration) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_TOURNAMENT+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_TOURNAMENT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		AbstractTournament result = loadTournamentElement(folderPath,root,configuration);
		return result;
	}
	public static AbstractTournament loadTournamentFromName(String name, Configuration configuration) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FileTools.getTournamentsPath()+File.separator+name;
		AbstractTournament result = loadTournamentFromFolderPath(individualFolder,configuration);
		return result;
    }
	
	private static AbstractTournament loadTournamentElement(String path, Element root, Configuration configuration) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		AbstractTournament result = null;
		Element element;
		// name
		element = root.getChild(XmlTools.ELT_GENERAL);
		String name = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		// content
		List<Element> elements = root.getChildren();
		element = elements.get(2);
		String type = element.getName();
		if(type.equalsIgnoreCase(CUP))
			result = CupTournamentLoader.loadTournamentElement(path,element,configuration);
		else if(type.equalsIgnoreCase(LEAGUE))
			result = LeagueTournamentLoader.loadTournamentElement(path,element,configuration);
		else if(type.equalsIgnoreCase(SEQUENCE))
			result = SequenceTournamentLoader.loadTournamentElement(path,element,configuration);
		else if(type.equalsIgnoreCase(SINGLE))
			result = SingleTournamentLoader.loadTournamentElement(path,element,configuration);
		// notes
		element = root.getChild(XmlTools.ELT_NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		//
		result.setName(name);
		return result;
	}
}
