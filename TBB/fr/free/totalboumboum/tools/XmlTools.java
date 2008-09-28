/*
 * Créé le 12 juin 2006
 *
 */
package fr.free.totalboumboum.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlTools
{	// Elements names
	public static final String ELT_ACTION = "action";
	public static final String ELT_ACTOR_PERMISSIONS = "actor-permissions";
	public static final String ELT_ACTOR_RESTRICTIONS = "actor-restrictions";
	public static final String ELT_ADDITION = "addition";
	public static final String ELT_ANIMES = "animes";
	public static final String ELT_ABILITY = "ability";
	public static final String ELT_ABILITIES = "abilities";
	public static final String ELT_ALL_LEVELS = "all-levels";
	public static final String ELT_BIGWALL = "bigwall";
	public static final String ELT_BIGWALLS = "bigwalls";
	public static final String ELT_BLOCK = "block";
	public static final String ELT_BLOCKS = "blocks";
	public static final String ELT_BOMBSET = "bombset";
	public static final String ELT_BOMB = "bomb";
	public static final String ELT_BOMBS = "bombs";
	public static final String ELT_CASE = "case";
	public static final String ELT_CHARACTER = "character";
	public static final String ELT_COLOR = "color";
	public static final String ELT_COLORS = "colors";
	public static final String ELT_COLORMAP = "colormap";
	public static final String ELT_COLORSPRITE = "colorsprite";
	public static final String ELT_CONFRONTATION = "confrontation";
	public static final String ELT_CONSTANT = "constant";
	public static final String ELT_CONTROLS = "controls";
	public static final String ELT_DIRECTION = "direction";
	public static final String ELT_DISCRETIZE = "discretize";
	public static final String ELT_DISPLAY = "display";
	public static final String ELT_DEFAULT = "default";
	public static final String ELT_DIVISION = "division";
	public static final String ELT_EVENT = "event";
	public static final String ELT_EXPLOSION = "explosion";
	public static final String ELT_FIRE = "fire";
	public static final String ELT_FIRESET = "fireset";
	public static final String ELT_FLOOR = "floor";
	public static final String ELT_FLOORS = "floors";
	public static final String ELT_FONT = "font";
	public static final String ELT_FORCE_POSITION = "forcePosition";
	public static final String ELT_FPS = "fps";
	public static final String ELT_GAME = "game";
	public static final String ELT_GAMEPLAY = "gameplay";
	public static final String ELT_GENERAL = "general";
	public static final String ELT_GESTURES = "gestures";
	public static final String ELT_GESTURE = "gesture";
	public static final String ELT_GLOBAL_DIMENSION = "globalDimension";
	public static final String ELT_GROUP = "group";
	public static final String ELT_HARDWALL = "hardwall";
	public static final String ELT_HARDWALLS = "hardwalls";
	public static final String ELT_HERO = "hero";
	public static final String ELT_HEROES = "heroes";
	public static final String ELT_INGAME = "ingame";
	public static final String ELT_INSTANCE = "instance";
	public static final String ELT_ITEM = "item";
	public static final String ELT_ITEMS = "items";
	public static final String ELT_ITEMSET = "itemset";
	public static final String ELT_KEY = "key";
	public static final String ELT_LANGUAGE = "language";
	public static final String ELT_LEVEL = "level";
	public static final String ELT_LEVELS = "levels";
	public static final String ELT_LIMIT = "limit";
	public static final String ELT_LIMITS = "limits";
	public static final String ELT_LINE = "line";
	public static final String ELT_LOCAL = "local";
	public static final String ELT_LOCATION = "location";
	public static final String ELT_LOCATIONS = "locations";
	public static final String ELT_MATCH = "match";
	public static final String ELT_MATCHES = "matches";
	public static final String ELT_MATRIX = "matrix";
	public static final String ELT_MAX = "max";
	public static final String ELT_MAXIMUM = "maximum";
	public static final String ELT_MIN = "min";
	public static final String ELT_MINIMUM = "minimum";
	public static final String ELT_MISC = "misc";
	public static final String ELT_MODULATION = "modulation";
	public static final String ELT_MULTIPLICATION = "multiplication";
	public static final String ELT_NAME = "name";
	public static final String ELT_NOTES = "notes";
	public static final String ELT_ON = "on";
	public static final String ELT_OFF = "off";
	public static final String ELT_OPTIONS = "options";
	public static final String ELT_ORIGIN = "origin";
	public static final String ELT_OUTGAME = "outgame";
	public static final String ELT_PANEL_DIMENSION = "panelDimension";
	public static final String ELT_PERMISSION = "permission";
	public static final String ELT_PERMISSIONS = "permissions";
	public static final String ELT_POINT = "point";
	public static final String ELT_POINTS = "points";
	public static final String ELT_PORTRAIT = "portrait";
	public static final String ELT_PREVIEW = "preview";
	public static final String ELT_PROFILE = "profile";
	public static final String ELT_PROFILES = "profiles";
	public static final String ELT_PROPERTIES = "properties";
	public static final String ELT_RANKINGS = "rankings";
	public static final String ELT_RANKPOINTS = "rankpoints";
	public static final String ELT_REFERENCE = "reference";
	public static final String ELT_ROUND = "round";
	public static final String ELT_ROUNDS = "rounds";
	public static final String ELT_SCORE = "score";
	public static final String ELT_SCORES = "scores";
	public static final String ELT_SETTING = "setting";
	public static final String ELT_SHADOW = "shadow";
	public static final String ELT_SHADOWS = "shadows";
	public static final String ELT_SITUATION = "situation";
	public static final String ELT_SMOOTH_GRAPHICS = "smoothGraphics";
	public static final String ELT_SOFTWALL = "softwall";
	public static final String ELT_SOFTWALLS = "softwalls";
	public static final String ELT_SPEED = "speed";
	public static final String ELT_SPRITE = "sprite";
	public static final String ELT_START = "start";
	public static final String ELT_STATE_MODULATIONS = "state-modulations";
	public static final String ELT_STEP = "step";
	public static final String ELT_STEPS = "steps";
	public static final String ELT_SUBTRACTION = "subtraction";
	public static final String ELT_SUMMATION = "summation";
	public static final String ELT_TARGET_PERMISSIONS = "target-permissions";
	public static final String ELT_TARGET_RESTRICTIONS = "target-restrictions";
	public static final String ELT_TEXT = "text";
	public static final String ELT_THEME = "theme";
	public static final String ELT_THIRD_PERMISSIONS = "third-permissions";
	public static final String ELT_THIRD_RESTRICTIONS = "third-restrictions";
	public static final String ELT_THRESHOLD = "threshold";
	public static final String ELT_THRESHOLDS = "thresholds";
	public static final String ELT_TILE = "tile";
	public static final String ELT_TIME = "time";
	public static final String ELT_TOTAL = "total";
	public static final String ELT_TOURNAMENT = "tournament";
	public static final String ELT_TRAJECTORIES = "trajectories";
	public static final String ELT_TRAJECTORY = "trajectory";
	public static final String ELT_VALUE = "value";
	public static final String ELT_VALUES = "values";
	public static final String ELT_VARIABLE_ITEM = "variable-item";
	public static final String ELT_VARIABLE_ITEMS = "variable-items";
	public static final String ELT_VARIABLE_TILE = "variable-tile";
	public static final String ELT_VARIABLE_TILES = "variable-tiles";
	public static final String ELT_VISIBLE_DIMENSION = "visibleDimension";
	public static final String ELT_VISIBLE_POSITION = "visiblePosition";
	// Attributes names
	public static final String ATT_ACTOR = "actor";
	public static final String ATT_AI = "ai";
	public static final String ATT_AUTOFIRE = "autofire";
	public static final String ATT_BLUE = "blue";
	public static final String ATT_BOUND_HEIGHT = "boundHeight";
	public static final String ATT_BOUND_YSHIFT = "boundYShift";
	public static final String ATT_BOUND_ZSHIFT = "boundZShift";
	public static final String ATT_COLOR = "color";
	public static final String ATT_COL = "col";
	public static final String ATT_CONTACT = "contact";
	public static final String ATT_DEFAULT = "default";
	public static final String ATT_DIRECTION = "direction";
	public static final String ATT_DURATION = "duration";
	public static final String ATT_EXAEQUO_SHARE = "exaequoShare";
	public static final String ATT_FILE = "file";
	public static final String ATT_FOLDER = "folder";
	public static final String ATT_FORCE_ALL = "forceAll";
	public static final String ATT_FRAME = "frame";
	public static final String ATT_GREEN = "green";
	public static final String ATT_GROUP = "group";
	public static final String ATT_HEIGHT = "height";
	public static final String ATT_HUMAN = "human";
	public static final String ATT_INDEX = "index";
	public static final String ATT_INVERT = "invert";
	public static final String ATT_KEY = "key";
	public static final String ATT_KIND = "kind";
	public static final String ATT_LEFTCOL = "leftCol";
	public static final String ATT_LEVEL_ORDER = "levelOrder";
	public static final String ATT_LINE = "line";
	public static final String ATT_LOCAL = "local";
	public static final String ATT_MARGIN = "margin";
	public static final String ATT_MATCH_LIMIT = "matchLimit";
	public static final String ATT_MATCH_LIMIT_VALUE = "matchLimitValue";
	public static final String ATT_MAXIMIZE = "maximize";
	public static final String ATT_MAXPLAYERS = "maxPlayers";
	public static final String ATT_MAX_STRENGTH = "maxStrength";
	public static final String ATT_NAME = "name";
	public static final String ATT_NUMBER = "number";
	public static final String ATT_ORIENTATION = "orientation";
	public static final String ATT_PACKNAME = "packname";
	public static final String ATT_PLAY_MODE = "playMode";
	public static final String ATT_PLAYER = "player";
	public static final String ATT_PLAYERS = "players";
	public static final String ATT_POSITION = "pos";
	public static final String ATT_PROBA = "proba";
	public static final String ATT_PROPORTIONAL = "proportional";
	public static final String ATT_RANDOM_ORDER = "randomOrder";
	public static final String ATT_RED = "red";
	public static final String ATT_REPEAT = "repeat";
	public static final String ATT_SCALE = "scale";
	public static final String ATT_SCORE = "score";
	public static final String ATT_SHADOW = "shadow";
	public static final String ATT_SHADOW_XSHIFT = "shadowXShift";
	public static final String ATT_SHADOW_YSHIFT = "shadowYShift";
	public static final String ATT_STRENGTH = "strength";
	public static final String ATT_SUP = "sup";
	public static final String ATT_TARGET = "target";
	public static final String ATT_TILE_POSITION = "tilePosition";
	public static final String ATT_TIME = "time";
	public static final String ATT_TIME_LIMIT = "timeLimit";
	public static final String ATT_TYPE = "type";
	public static final String ATT_UPLINE = "upLine";
	public static final String ATT_USES = "uses";
	public static final String ATT_VALUE = "value";
	public static final String ATT_WIDTH = "width";
	public static final String ATT_WIN = "win";
	public static final String ATT_XINTERACTION = "xInteraction";
	public static final String ATT_XPOSITION = "xPosition";
	public static final String ATT_XSHIFT = "xShift";
	public static final String ATT_YINTERACTION = "yInteraction";
	public static final String ATT_YPOSITION = "yPosition";
	public static final String ATT_YSHIFT = "yShift";
	public static final String ATT_ZSHIFT = "zShift";
	public static final String ATT_ZPOSITION = "zPosition";
	
	public static final HashMap<String,DocumentBuilder> documentBuilders = new HashMap<String,DocumentBuilder>();
	
	public static void init() throws SAXException, ParserConfigurationException
	{	// init
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    schemaFactory.setErrorHandler(new ErrorHandler()
	    {	public void error(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
			public void fatalError(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
			public void warning(SAXParseException e) throws SAXException
	    	{   throw e;
	    	}
	    });
	    // loading all schemas
//	    System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.parsers.SAXParser");
//	    System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");
		File folder = new File(FileTools.getSchemasPath());
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
		        {   public void fatalError(SAXParseException e) throws SAXException
		        	{   throw e;
		        	}
			        public void error(SAXParseException e) throws SAXParseException
			    	{   throw e;
			    	}
			        public void warning(SAXParseException e) throws SAXParseException
			        {   throw e;
			        }
				});
				documentBuilders.put(name,builder);
	        }
		}
	}
	
	public static Element getRootFromFile(File dataFile, File schemaFile) throws ParserConfigurationException, SAXException, IOException
	{	// JAXP
		DocumentBuilder bldr = documentBuilders.get(schemaFile.getName());
		org.w3c.dom.Document doc = bldr.parse(dataFile);
		// JDOM
		DOMBuilder builder = new DOMBuilder();
        Document document = builder.build(doc);
		// root
		Element result = document.getRootElement();
		return result;
	}
}
