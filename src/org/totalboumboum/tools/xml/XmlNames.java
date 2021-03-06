package org.totalboumboum.tools.xml;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;

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
 * List of strings used to access XML files.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("javadoc")
public class XmlNames
{
	public static final String ABILITY = "ability";
	public static final String ABILITIES = "abilities";
	public static final String ABSTRACT_BLOCKS = "abstractBlocks";
	public static final String ABSTRACT_BOMBS = "abstractBombs";
	public static final String ABSTRACT_FIRES = "abstractFires";
	public static final String ABSTRACT_FLOORS = "abstractFloors";
	public static final String ABSTRACT_ITEMS = "abstractItems";
	public static final String ACTION = "action";
	public static final String ACTOR = "actor";
	public static final String ACTOR_CIRCUMSTANCES = "actorCircumstances";
	public static final String ACTOR_CONTACT = "actorContact";
	public static final String ACTOR_MODULATIONS = "actorModulations";
	public static final String ACTOR_RESTRICTIONS = "actorRestrictions";
	public static final String ACTOR_TILE_POSITION = "actorTilePosition";
	public static final String ADDITION = "addition";
	public static final String ADJUST = "adjust";
	public static final String ANIMES = "animes";
	public static final String AI = "ai";
	public static final String AIS = "ais";
	public static final String ALL_LEVELS = "allLevels";
	public static final String ALLOWED_PLAYERS = "allowedPlayers";
	public static final String ARCHIVE = "archive";
	public static final String AUTHOR = "author";
	public static final String AUTHORS = "authors";
	public static final String AUTOFIRE = "autofire";
	public static final String AUTOLOAD = "autoload";
	public static final String AUTOSAVE = "autosave";
	public static final String AUTO_ADVANCE = "autoAdvance";
	public static final String BASE = "base";
	public static final String BIGWALL = "bigwall";
	public static final String BIGWALLS = "bigwalls";
	public static final String BLOCK = "block";
	public static final String BLOCKS = "blocks";
	public static final String BLUE = "blue";
	public static final String BOMBSET = "bombset";
	public static final String BOMB = "bomb";
	public static final String BOMB_CYCLING_AIS = "bombCyclingAis";
	public static final String BOMB_USELESS_AIS = "bombUselessAis";
	public static final String BOMBS = "bombs";
	public static final String BORDER = "border";
	public static final String BOUND_HEIGHT = "boundHeight";
	public static final String BOUND_YSHIFT = "boundYShift";
	public static final String BOUND_ZSHIFT = "boundZShift";
	public static final String CASE = "case";
	public static final String CACHE = "cache";
	public static final String CACHE_LIMIT = "cacheLimit";
	public static final String CATEGORY = "category";
	public static final String CATEGORIES = "categories";
	public static final String CENTRAL = "central";
	public static final String CHARACTER = "character";
	public static final String COL = "col";
	public static final String COLOR = "color";
	public static final String COLORS = "colors";
	public static final String COLORMAP = "colormap";
	public static final String COLORSPRITE = "colorsprite";
	public static final String COMBINATION = "combination";
	public static final String COMPARATOR = "comparator";
	public static final String CONCRETE_BLOCKS = "concreteBlocks";
	public static final String CONCRETE_BOMBS = "concreteBombs";
	public static final String CONCRETE_FIRES = "concreteFires";
	public static final String CONCRETE_FLOORS = "concreteFloors";
	public static final String CONCRETE_ITEMS = "concreteItems";
	public static final String CONFRONTATION = "confrontation";
	public static final String CONFRONTATIONS_ORDER = "confrontationsOrder";
	public static final String CONNECTIONS = "connections";
	public static final String CONSTANT = "constant";
	public static final String CONTACT = "contact";
	public static final String CONTROL = "control";
	public static final String CONTROLS = "controls";
	public static final String COUNT = "count";
	public static final String CRITERIA = "criteria";
	public static final String CRITERION = "criterion";
	public static final String DATE = "date";
	public static final String DATES = "dates";
	public static final String DEFAULT = "default";
	public static final String DEFAULT_RATING = "defaultRating";
	public static final String DEFAULT_RATING_DEVIATION = "defaultRatingDeviation";
	public static final String DEFAULT_RATING_VOLATILITY = "defaultRatingVolatility";
	public static final String DELAY = "delay";
	public static final String DIRECT = "direct";
	public static final String DIRECTION = "direction";
	public static final String DISABLED = "disabled";
	public static final String DISCRETIZE = "discretize";
	public static final String DISPLAY = "display";
	public static final String DISPLAY_EXCEPTIONS = "displayExceptions";
	public static final String DIVISION = "division";
	public static final String DRAW = "draw";
	public static final String DURATION = "duration";
	public static final String ENGINE = "engine";
	public static final String EVENT = "event";
	public static final String EVENTS = "events";
	public static final String EXAEQUO_SHARE = "exaequoShare";
	public static final String EXPLOSION = "explosion";
	public static final String EXPLOSIONS = "explosions";
	public static final String FILE = "file";
	public static final String FIRE = "fire";
	public static final String FIRESET = "fireset";
	public static final String FIRESETS = "firesets";
	public static final String FIXED = "fixed";
	public static final String FLOOR = "floor";
	public static final String FLOORS = "floors";
	public static final String FOLDER = "folder";
	public static final String FONT = "font";
	public static final String FORBID_PLAYER_NUMBERS = "forbidPlayerNumbers";
	public static final String FORCE_ALL = "forceAll";
	public static final String FORCE_POSITION = "forcePosition";
	public static final String FPS = "fps";
	public static final String FRAME = "frame";
	public static final String FULL_SCREEN = "fullScreen";
	public static final String GAME = "game";
	public static final String GAME_QUICKMATCH = "gameQuickmatch";
	public static final String GAME_QUICKSTART = "gameQuickstart";
	public static final String GAME_TOURNAMENT = "gameTournament";
	public static final String GAMES_PER_PERIOD = "gamesPerPeriod";
	public static final String GAMEPLAY = "gameplay";
	public static final String GENERAL = "general";
	public static final String GESTURES = "gestures";
	public static final String GESTURE = "gesture";
	public static final String GLICKO2 = "glicko2";
	public static final String GLOBAL_DIMENSION = "globalDimension";
	public static final String GREEN = "green";
	public static final String GROUP = "group";
	public static final String HARDWALL = "hardwall";
	public static final String HARDWALLS = "hardwalls";
	public static final String HEIGHT = "height";
	public static final String HERO = "hero";
	public static final String HEROES = "heroes";
	public static final String HIDE_ALLAIS = "hideAllAis";
	public static final String HOST = "host";
	public static final String HOSTING = "hosting";
	public static final String HOSTS = "hosts";
	public static final String HUMAN = "human";
	public static final String ID = "id";
	public static final String IMAGE = "image";
	public static final String IMAGES = "images";
	public static final String INCLUDE_QUICKSTARTS = "includeQuickStarts";
	public static final String INCLUDE_SIMULATIONS = "includeSimulations";
	public static final String INDEX = "index";
	public static final String INGAME = "ingame";
	public static final String INSTANCE = "instance";
	public static final String INVERT = "invert";
	public static final String IP = "ip";
	public static final String ITEM = "item";
	public static final String ITEMS = "items";
	public static final String ITEMSET = "itemset";
	public static final String KEY = "key";
	public static final String KIND = "kind";
	public static final String LANGUAGE = "language";
	public static final String LAST = "last";
	public static final String LAST_HOST = "lastHost";
	public static final String LAST_IP = "lastIp";
	public static final String LAST_PORT = "lastPort";
	public static final String LAST_STANDING = "lastStanding";
	public static final String LEFTCOL = "leftCol";
	public static final String LEG = "leg";
	public static final String LEVEL = "level";
	public static final String LEVEL_ORDER = "levelOrder";
	public static final String LEVELS = "levels";
	public static final String LIMIT = "limit";
	public static final String LIMITS = "limits";
	public static final String LINE = "line";
	public static final String LIST = "list";
	public static final String LOCAL = "local";
	public static final String LOCATION = "location";
	public static final String LOCATIONS = "locations";
	public static final String LOG = "log";
	public static final String LOG_EXCEPTIONS = "logExceptions";
	public static final String LOGSTATS = "logstats";
	public static final String MAC = "mac";
	public static final String MARGIN = "margin";
	public static final String MATCH = "match";
	public static final String MATCH_LIMIT = "matchLimit";
	public static final String MATCH_LIMIT_VALUE = "matchLimitValue";
	public static final String MATCHES = "matches";
	public static final String MATRIX = "matrix";
	public static final String MAX = "max";
	public static final String MAX_STRENGTH = "maxStrength";
	public static final String MAXIMIZE = "maximize";
	public static final String MAXIMUM = "maximum";
	public static final String MAXPLAYERS = "maxPlayers";
	public static final String MIN = "min";
	public static final String MINIMIZE_CONFRONTATIONS = "minimizeConfrontations";
	public static final String MINIMUM = "minimum";
	public static final String MISC = "misc";
	public static final String MODE = "mode";
	public static final String MODULATION = "modulation";
	public static final String MULTIPLICATION = "multiplication";
	public static final String NAME = "name";
	public static final String NBR = "nbr";
	public static final String NETWORK = "network";
	public static final String NONE = "none";
	public static final String NOTES = "notes";
	public static final String NUMBER = "number";
	public static final String NUMBER_ACTIVE = "numberActive";
	public static final String NUMBER_KEPT = "numberKept";
	public static final String ON = "on";
	public static final String OFF = "off";
	public static final String OPTIONS = "options";
	public static final String ORIENTATION = "orientation";
	public static final String ORIGIN = "origin";
	public static final String OTHER_MODULATIONS = "otherModulations";
	public static final String OUTGAME = "outgame";
	public static final String PACK = "pack";
	public static final String PACKNAME = "packname";
	public static final String PANEL_DIMENSION = "panelDimension";
	public static final String PART = "part";
	public static final String PERMISSION = "permission";
	public static final String PERMISSIONS = "permissions";
	public static final String PLAY_MODE = "playMode";
	public static final String PLAYED = "played";
	public static final String PLAYER = "player";
	public static final String PLAYERS = "players";
	public static final String POINT = "point";
	public static final String POINTS = "points";
	public static final String PORT = "port";
	public static final String PORTRAIT = "portrait";
	public static final String POSITION = "pos";
	public static final String PREFERRED = "preferred";
	public static final String PREVIEW = "preview";
	public static final String PROBA = "proba";
	public static final String PROFILE = "profile";
	public static final String PROFILES = "profiles";
	public static final String PROPERTIES = "properties";
	public static final String PROPORTIONAL = "proportional";
	public static final String QUICKMATCH = "quickmatch";
	public static final String QUICKSTART = "quickstart";
	public static final String QUICK_LAUNCH = "quickLaunch";
	public static final String RANDOM = "random";
	public static final String RANDOM_LOCATION = "randomLocation";
	public static final String RANDOM_ORDER = "randomOrder";
	public static final String RANDOMIZE_LEGS = "randomizeLegs";
	public static final String RANDOMIZE_MATCHES = "randomizeMatches";
	public static final String RANDOMIZE_PARTS = "randomizeParts";
	public static final String RANDOMIZE_PLAYERS = "randomizePlayers";
	public static final String RANGE = "range";
	public static final String RANK = "rank";
	public static final String RANKINGS = "rankings";
	public static final String RANKING = "ranking";
	public static final String RANKPOINTS = "rankpoints";
	public static final String RECORD_STATS = "recordStats";
	public static final String RED = "red";
	public static final String REFERENCE = "reference";
	public static final String REGULAR_LAUNCH = "regularLaunch";
	public static final String RELATIVE = "relative";
	public static final String REPEAT = "repeat";
	public static final String REPLAY = "replay";
	public static final String ROUND = "round";
	public static final String ROUNDS = "rounds";
	public static final String SAVE = "save";
	public static final String SCALE = "scale";
	public static final String SCORE = "score";
	public static final String SCORES = "scores";
	public static final String SELECTEDCTED = "selected";
	public static final String SELF_MODULATIONS = "selfModulations";
	public static final String SETTING = "setting";
	public static final String SETTINGS = "settings";
	public static final String SHADOW = "shadow";
	public static final String SHADOW_XSHIFT = "shadowXShift";
	public static final String SHADOW_YSHIFT = "shadowYShift";
	public static final String SHADOWS = "shadows";
	public static final String SHARE = "share";
	public static final String SITUATION = "situation";
	public static final String SMOOTH_GRAPHICS = "smoothGraphics";
	public static final String SOFTWALL = "softwall";
	public static final String SOFTWALLS = "softwalls";
	public static final String SORT_PLAYERS = "sortPlayers";
	public static final String SOURCE = "source";
	public static final String SPEED = "speed";
	public static final String SPRITE = "sprite";
	public static final String SPRITE_FOLDER = "spriteFolder";
	public static final String SPRITE_PACK = "spritePack";
	public static final String START = "start";
	public static final String STATISTICS = "statistics";
	public static final String STEP = "step";
	public static final String STEPS = "steps";
	public static final String STRENGTH = "strength";
	public static final String SUBTRACTION = "subtraction";
	public static final String SUDDEN_DEATH = "suddenDeath";
	public static final String SUMMATION = "summation";
	public static final String SUP = "sup";
	public static final String TABLE = "table";
	public static final String TABLES = "tables";
	public static final String TARGET = "target";
	public static final String TARGET_CIRCUMSTANCES = "targetCircumstances";
	public static final String TARGET_CONTACT = "targetContact";
	public static final String TARGET_MODULATIONS = "targetModulations";
	public static final String TARGET_RESTRICTIONS = "targetRestrictions";
	public static final String TARGET_TILE_POSITION = "targetTilePosition";
	public static final String TEXT = "text";
	public static final String THEME = "theme";
	public static final String THIRD_MODULATIONS = "thirdModulations";
	public static final String THIRD_RESTRICTIONS = "thirdRestrictions";
	public static final String THRESHOLD = "threshold";
	public static final String THRESHOLDS = "thresholds";
	public static final String TIE_BREAK = "tieBreak";
	public static final String TILE = "tile";
	public static final String TILE_POSITION = "tilePosition";
	public static final String TIME = "time";
	public static final String TIME_LIMIT = "timeLimit";
	public static final String TIMING = "timing";
	public static final String TITLE = "title";
	public static final String TOTAL = "total";
	public static final String TOURNAMENT = "tournament";
	public static final String TRAJECTORIES = "trajectories";
	public static final String TRAJECTORY = "trajectory";
	public static final String TYPE = "type";
	public static final String UNIFORM = "uniform";
	public static final String UPLINE = "upLine";
	public static final String UPS = "ups";
	public static final String USE = "use";
	public static final String USE_LAST_PLAYERS = "useLastPlayers";
	public static final String USE_LAST_LEVELS = "useLastLevels";
	public static final String USE_LAST_SETTINGS = "useLastSettings";
	public static final String USE_LAST_TOURNAMENT = "useLastTournament";
	public static final String USES = "uses";
	public static final String VALUE = "value";
	public static final String VALUES = "values";
	public static final String VARIABLE_ITEM = "variableItem";
	public static final String VARIABLE_ITEMS = "variableItems";
	public static final String VARIABLE_TILE = "variableTile";
	public static final String VARIABLE_TILES = "variableTiles";
	public static final String VERSION = "version";
	public static final String VIDEO = "video";
	public static final String VISIBLE_DIMENSION = "visibleDimension";
	public static final String VISIBLE_POSITION = "visiblePosition";
	public static final String WIDTH = "width";
	public static final String WIN = "win";
	public static final String XINTERACTION = "xInteraction";
	public static final String XPOSITION = "xPosition";
	public static final String XSHIFT = "xShift";
	public static final String YINTERACTION = "yInteraction";
	public static final String YPOSITION = "yPosition";
	public static final String YSHIFT = "yShift";
	public static final String ZONE = "zone";
	public static final String ZSHIFT = "zShift";
	public static final String ZPOSITION = "zPosition";
	public static final String VAL_ANY = "ANY";
	public static final String VAL_MAX = "MAXIMUM";
	public static final String VAL_SOME = "SOME";
	
	public static final HashMap<String,DocumentBuilder> documentBuilders = new HashMap<String,DocumentBuilder>();	
}
