package org.totalboumboum.ai.v201314.ais._simplet;

import japa.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.ai.v201314.adapter.agent.AiManager;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceLoader;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.tools.classes.ClassTools;
import org.xml.sax.SAXException;

/**
 * Classe utilisée par le moteur du jeu pour retrouver les agents.
 * L'objet créé dans la méthode {@link #instantiateAgent()} 
 * doit être de la classe principale de l'agent.
 * <br/>
 * Les directives {@code import} doivent être modifiées de manière
 * à utiliser la version la plus appropriée de l'agent : {@code v1},
 * {@code v2}, {@code v3}, etc. 
 * 
 * @author Vincent Labatut
 */
public class AiMain extends AiManager
{
	/////////////////////////////////////////////////////////////////
	// AGENT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ArtificialIntelligence instantiateAgent()
	{	return new Simplet();
	}
	
	/////////////////////////////////////////////////////////////////
	// VERIFICATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * L'exécution de cette méthode permet
	 * de faire une vérification du code source
	 * de cet agent.
	 * <br/>
	 * Ici, on teste automatiquement certains des 
	 * points mentionnés dans le manuel de l'API.
	 * Mais <b>tous ne peuvent pas être testés depuis
	 * un programme</b> Reportez-vous au manuel pour
	 * plus d'informations.
	 * 
	 * @param args
	 *		Aucun argument n'est nécessaire. 
	 * @throws IOException
	 * 		Problème lors de l'accès aux fichiers. 
	 * @throws ParseException 
	 * 		Problème lors du parsing du code source.
	 */
	public static void main(String args[]) throws ParseException, IOException
	{	// on applique le parser
		parseSourceCode();
		
		// on vérifie si les préférences se chargent normalement
		loadPreferences();
	}
	
	/**
	 * Cette méthode permet d'appliquer le 
	 * parser de l'API IA au code source
	 * de cet agent.
	 * 
	 * @throws IOException
	 * 		Problème lors de l'accès aux fichiers. 
	 * @throws ParseException 
	 * 		Problème lors de l'analyse du code source.
	 */
	private static void parseSourceCode() throws ParseException, IOException
	{	// on construit le chemin vers le code source
		String path = Simplet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		String pckg = Simplet.class.getPackage().getName();
		String temp[] = pckg.split("\\"+ClassTools.CLASS_SEPARATOR);
		for(String t: temp)
			decodedPath = decodedPath + "/" + t;
		
		// on applique le parser à ce chemin
		checkSourceCode(decodedPath);
	}
	
	private static void loadPreferences() throws IllegalArgumentException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException
	{	// on construit le chemin vers le code source
		String path = Simplet.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		String pckg = AiMain.class.getPackage().getName();
		String temp[] = pckg.split("\\"+ClassTools.CLASS_SEPARATOR);
		for(String t: temp)
			decodedPath = decodedPath + "/" + t;
		
		String packName ;
		String aiName;
		Simplet agent = new Simplet();
		AiPreferenceLoader.loadAiPreferences(packName,aiName,agent);
		// TODO afficher msg intro + la table de préfs
	}
}
