package es.ucm.fdi.tp.practica6;

import es.ucm.fdi.tp.basecode.bgame.control.*;
import es.ucm.fdi.tp.basecode.bgame.model.AIAlgorithm;
import es.ucm.fdi.tp.basecode.bgame.model.Game;
import es.ucm.fdi.tp.basecode.bgame.model.GameError;
import es.ucm.fdi.tp.basecode.bgame.model.Piece;
import es.ucm.fdi.tp.basecode.minmax.MinMax;
import es.ucm.fdi.tp.practica6.ataxx.AtaxxFactoryExt;
import es.ucm.fdi.tp.practica6.attt.AdvancedTTTFactoryExt;
import es.ucm.fdi.tp.practica6.connectn.ConnectNFactoryExt;
import es.ucm.fdi.tp.practica6.control.GameClient;
import es.ucm.fdi.tp.practica6.control.GameServer;
import es.ucm.fdi.tp.practica6.ttt.TicTacToeFactoryExt;
import org.apache.commons.cli.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



/**
 * This is the class with the main method for the board games application.
 * <p>
 * It uses the Commons-CLI library for parsing command-line arguments: the game
 * to play, the players list, etc.. More information is available at the
 * <a href="https://commons.apache.org/proper/commons-cli"/>commons-cli</a> page
 * <p>
 * <p>
 * Esta es la clase con el metodo main de inicio del programa. Se utiliza la
 * libreria Commons-CLI para leer argumentos de la linea de ordenes: el juego al
 * que se quiere jugar y la lista de jugadores. Puedes encontrar mas información
 * sobre esta libreria en la pagina de
 * <a href="https://commons.apache.org/proper/commons-cli"/>commons-cli</a>
 */
public class Main {

    /**
     * The possible views.
     * <p>
     * Vistas disponibles.
     */
    enum ViewInfo {
        WINDOW("window", "Swing"), CONSOLE("console", "Console");

        private String id;
        private String desc;

        ViewInfo(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    /**
     * The available games.
     * <p>
     * Juegos disponibles.
     */
    enum GameInfo {
        CONNECT_N("cn", "ConnectN"), TIC_TAC_TOE("ttt", "Tic-Tac-Toe"), ADVANCED_TIC_TAC_TOE("attt",
                "Advanced Tic-Tac-Toe"), ATAXX("ataxx", "Ataxx");

        private String id;
        private String desc;

        GameInfo(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return id;
        }

    }

    /**
     * Player modes (manual, random, etc.)
     * <p>
     * Modos de juego.
     */
    enum PlayerMode {
        MANUAL("m", "Manual"), RANDOM("r", "Random"), AI("a", "Automatics");

        private String id;
        private String desc;

        PlayerMode(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    /**
     * App modes (server,client and normal).
     */
    enum AppMode {
        SERVER("server", "Server mode"), CLIENT("client", "Client mode"), NORMAL("normal", "Normal");
        private String id;
        private String desc;

        AppMode(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    /**
     * Algorithms for automatic players. The 'none' option means that the
     * default behavior is used (i.e., a player that waits for some time and
     * then generates a random move)
     */
    private enum AlgorithmForAIPlayer {
        NONE("none", "No AI Algorithm"), MINMAX("minmax", "MinMax"), MINMAXAB("minmaxab",
                "MinMax with Alpha-Beta Prunning");

        private String id;
        private String desc;

        AlgorithmForAIPlayer(String id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        public String getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    /**
     * Default game to play.
     * <p>
     * Juego por defecto.
     */
    final private static GameInfo DEFAULT_GAME = GameInfo.CONNECT_N;

    /**
     * default view to use.
     * <p>
     * Vista por defecto.
     */
    final private static ViewInfo DEFAULT_VIEW = ViewInfo.CONSOLE;

    /**
     * Default player mode to use.
     * <p>
     * Modo de juego por defecto.
     */
    final private static PlayerMode DEFAULT_PLAYERMODE = PlayerMode.MANUAL;

    /**
     * Default algorithm for automatic player.
     */
    final private static AlgorithmForAIPlayer DEFAULT_AIALG = AlgorithmForAIPlayer.NONE;

    /**
     * Default app mode to use.
     */
    final private static AppMode DEFAULT_APPMODE = AppMode.NORMAL;

    /*
     * Default port to use in server or client mode;
     */
    final private static Integer DEFAULT_SERVERPORT = 2000;
    /*
     * Default server ID to use in server or client mode;
     */
    final private static String DEFAULT_SERVERHOST = "localhost";
    /**
     * Default server timeout to use in server or client mdoe;
     */
    final private static Integer DEFAULT_TIMEOUT = 2000;
    /**
     * This field includes a game factory that is constructed after parsing the
     * command-line arguments. Depending on the game selected with the -g option
     * (by default {@link #DEFAULT_GAME}).
     * <p>
     * <p>
     * Este atributo incluye una factoria de juego que se crea despues de
     * extraer los argumentos de la linea de ordenes. Depende del juego
     * seleccionado con la opcion -g (por defecto, {@link #DEFAULT_GAME}).
     */
    private static GameFactory gameFactory;

    /**
     * List of pieces provided with the -p option, or taken from
     * {@link GameFactory#createDefaultPieces()} if this option was not
     * provided.
     * <p>
     * <p>
     * Lista de fichas proporcionadas con la opcion -p, u obtenidas de
     * {@link GameFactory#createDefaultPieces()} si no hay opcion -p.
     */
    private static List<Piece> pieces;

    /**
     * A list of player modes. The i-th mode corresponds to the i-th piece in
     * the list {@link #pieces}. They correspond to what is provided in the -p
     * option (or using the default value {@link #DEFAULT_PLAYERMODE}).
     * <p>
     * <p>
     * Lista de modos de juego. El modo i-esimo corresponde con la ficha i-esima
     * de la lista {@link #pieces}. Esta lista contiene lo que se proporciona en
     * la opcion -p (o el valor por defecto {@link #DEFAULT_PLAYERMODE}).
     */
    private static List<PlayerMode> playerModes;
    /**
     * The appMode to use. Depending on the selected game mode using the -am or -app-mode
     */
    private static AppMode appMode;
    /**
     * The view to use. Depending on the selected view using the -v option or
     * the default value {@link #DEFAULT_VIEW} if this option was not provided.
     * <p>
     * <p>
     * Vista a utilizar. Dependiendo de la vista seleccionada con la opcion -v o
     * el valor por defecto {@link #DEFAULT_VIEW} si el argumento -v no se
     * proporciona.
     */
    private static ViewInfo view;

    /**
     * {@code true} if the option -m was provided, to use a separate view for
     * each piece, and {@code false} otherwise.
     * <p>
     * <p>
     * {@code true} si se incluye la opcion -m, para utilizar una vista separada
     * por cada ficha, o {@code false} en caso contrario.
     */
    private static boolean multiviews;

    /**
     * Number of rows provided with the option -d ({@code null} if not
     * provided).
     * <p>
     * <p>
     * Numero de filas proporcionadas con la opcion -d, o {@code null} si no se
     * incluye la opcion -d.
     */
    private static Integer dimRows;
    /**
     * Number of columns provided with the option -d ({@code null} if not
     * provided).
     * <p>
     * <p>
     * Numero de columnas proporcionadas con la opcion -d, o {@code null} si no
     * se incluye la opcion -d.
     */
    private static Integer dimCols;

    /**
     * Number of obstacles provided with the option -o, zero if not provided);
     */
    private static Integer obstacles;

    /**
     * The algorithm to be used by the automatic player. Not used so far, it is
     * always {@code null}.
     * <p>
     * <p>
     * Algoritmo a utilizar por el jugador automatico.
     */
    private static AIAlgorithm aiPlayerAlg;

    /**
     * The depth of the maximum depth in the MinMax Algorithm.
     * <p>
     * <p>
     * La profundidad máxima del árbol MinMax
     */
    private static Integer minmaxTreeDepth;
    /**
     * The port used in server or client mode.
     */
    private static Integer serverPort;
    /**
     * The IP used in
     */
    private static String serverHost;

    /**
     * Processes the command-line arguments and modify the fields of this class
     * with corresponding values. E.g., the factory, the pieces, etc.
     * <p>
     * <p>
     * Procesa la linea de ordenes del programa y crea los objetos necesarios
     * para los atributos de esta clase. Por ejemplo, la factoria, las fichas,
     * etc.
     *
     * @param args Command line arguments.
     *             <p>
     *             <p>
     *             Lista de argumentos de la linea de ordenes.
     */
    private static void parseArgs(String[] args) {

        // define the valid command line option
        Options cmdLineOptions = new Options();
        cmdLineOptions.addOption(constructHelpOption()); // -h or --help
        cmdLineOptions.addOption(constructGameOption()); // -g or --game
        cmdLineOptions.addOption(constructViewOption()); // -v or --view
        cmdLineOptions.addOption(constructMultiViewOption()); // -m or
        // --multiviews
        cmdLineOptions.addOption(constructPlayersOption()); // -p or --players
        cmdLineOptions.addOption(constructDimensionOption()); // -d or --dim
        cmdLineOptions.addOption(constructMinMaxDepathOption()); // -md or
        // --minmax-depth
        cmdLineOptions.addOption(constructAppModeOption()); //<3// -am or --app-mode
        cmdLineOptions.addOption(constructServerPortOption()); // -sp or --server-port
        cmdLineOptions.addOption(constructServerHostOption()); //-sh or --server-host
        cmdLineOptions.addOption(constructAIAlgOption()); // -aialg ...
        cmdLineOptions.addOption(constructObstaclesOption()); // -o n
        // --obstacles
        // parse the command line as provided in args
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(cmdLineOptions, args);
            parseHelpOption(line, cmdLineOptions);
            parseDimensionOption(line);
            parseObstaclesOption(line);
            parseGameOption(line);
            parseViewOption(line);
            parseMultiViewOption(line);
            parsePlayersOptions(line);
            parseMixMaxDepthOption(line);
            parseAIAlgOption(line);
            parseAppModeOption(line);
            parseServerPortOption(line);
            parseServerHostOption(line);

            // if there are any remaining arguments, then something wrong is
            // provided in the command line!

            String[] remaining = line.getArgs();
            if (remaining.length > 0) {
                String error = "Illegal arguments:";
                for (String o : remaining)
                    error += (" " + o);
                throw new ParseException(error);
            }

        } catch (ParseException | GameError e) {
            // new Piece(...) might throw GameError exception
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * Builds the obstacles (-o or --obstacles) CLI option.
     *
     * @return CLI {@link {@link Option} for the obstacles option.
     */
    private static Option constructObstaclesOption() {
        return new Option("o", "obstacles", true,
                "The number of obstacles given by an integer number");
    }

    private static void parseObstaclesOption(CommandLine line)
            throws ParseException {
        String obsVal = line.getOptionValue("o");
        if (obsVal != null) {
            try {
                obstacles = Integer.parseInt(obsVal);
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid obstacle : " + obsVal);
            }
        }
    }

    /**
     * Builds the MinMax tree depth (-md or --minmax-depth) CLI option.
     *
     * @return CLI {@link {@link Option} for the MinMax tree depth option.
     */
    private static Option constructMinMaxDepathOption() {
        Option opt = new Option("md", "minmax-depth", true, "The maximum depth of the MinMax tree");
        opt.setArgName("number");
        return opt;
    }

    /**
     * Parses the MinMax tree depth option (-md or --minmax-depth). It sets the
     * value of {@link #minmaxTreeDepth} accordingly.
     *
     * @param line CLI {@link CommandLine} object.
     */
    private static void parseMixMaxDepthOption(CommandLine line) throws ParseException {
        String depthVal = line.getOptionValue("md");
        minmaxTreeDepth = null;

        if (depthVal != null) {
            try {
                minmaxTreeDepth = Integer.parseInt(depthVal);
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid value for the MinMax depth '" + depthVal + "'");
            }
        }
    }

    /**
     * Builds the ai-algorithm (-aialg or --ai-algorithm) CLI option.
     *
     * @return CLI {@link {@link Option} for the ai-algorithm option.
     */
    private static Option constructAIAlgOption() {
        String optionInfo = "The AI algorithm to use ( ";
        for (AlgorithmForAIPlayer alg : AlgorithmForAIPlayer.values()) {
            optionInfo += alg.getId() + " [for " + alg.getDesc() + "] ";
        }
        optionInfo += "). By defualt, no algorithm is used.";
        Option opt = new Option("aialg", "ai-algorithm", true, optionInfo);
        opt.setArgName("algorithm for ai player");
        return opt;
    }

    /**
     * Parses the ai-algorithm option (-aialg or --ai-algorithm). It sets the
     * value of {@link #minmaxTreeDepth} accordingly.
     *
     * @param line CLI {@link CommandLine} object.
     */
    private static void parseAIAlgOption(CommandLine line) throws ParseException {
        String aialg = line.getOptionValue("aialg", DEFAULT_AIALG.getId());

        AlgorithmForAIPlayer selectedAlg = null;
        for (AlgorithmForAIPlayer a : AlgorithmForAIPlayer.values()) {
            if (a.getId().equals(aialg)) {
                selectedAlg = a;
                break;
            }
        }

        if (selectedAlg == null) {
            throw new ParseException("Uknown AI algorithms '" + aialg + "'");
        }

        switch (selectedAlg) {
            case MINMAX:
                aiPlayerAlg = minmaxTreeDepth == null ? new MinMax(false) : new MinMax(minmaxTreeDepth, false);
                break;
            case MINMAXAB:
                aiPlayerAlg = minmaxTreeDepth == null ? new MinMax() : new MinMax(minmaxTreeDepth);
                break;
            case NONE:
                aiPlayerAlg = null;
                break;
        }
    }

    /**
     * Builds the multiview (-m or --multiviews) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -m.
     *
     * @return CLI {@link {@link Option} for the multiview option.
     */

    private static Option constructMultiViewOption() {
        return new Option("m", "multiviews", false,
                "Create a separate view for each player (valid only when using the " + ViewInfo.WINDOW + " view)");
    }

    /**
     * Parses the multiview option (-m or --multiview). It sets the value of
     * {@link #multiviews} accordingly.
     * <p>
     * <p>
     * Extrae la opcion multiview (-m) y asigna el valor de {@link #multiviews}.
     *
     * @param line CLI {@link CommandLine} object.
     */
    private static void parseMultiViewOption(CommandLine line) {
        multiviews = line.hasOption("m");
    }

    /**
     * Builds the view (-v or --view) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -v.
     *
     * @return CLI {@link Option} for the view option.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */
    private static Option constructViewOption() {
        String optionInfo = "The view to use ( ";
        for (ViewInfo i : ViewInfo.values()) {
            optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
        }
        optionInfo += "). By defualt, " + DEFAULT_VIEW.getId() + ".";
        Option opt = new Option("v", "view", true, optionInfo);
        opt.setArgName("view identifier");
        return opt;
    }

    /**
     * Parses the view option (-v or --view). It sets the value of {@link #view}
     * accordingly.
     * <p>
     * <p>
     * Extrae la opcion view (-v) y asigna el valor de {@link #view}.
     *
     * @param line CLI {@link CommandLine} object.
     * @throws ParseException If an invalid value is provided (the valid values are those
     *                        of {@link ViewInfo}.
     */
    private static void parseViewOption(CommandLine line) throws ParseException {
        String viewVal = line.getOptionValue("v", DEFAULT_VIEW.getId());
        for (ViewInfo v : ViewInfo.values()) {
            if (viewVal.equals(v.getId())) {
                view = v;
            }
        }
        if (view == null) {
            throw new ParseException("Uknown view '" + viewVal + "'");
        }
    }

    /**
     * Builds the players (-p or --player) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -p.
     *
     * @return CLI {@link Option} for the list of pieces/players.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */
    private static Option constructPlayersOption() {
        String optionInfo = "A player has the form A:B (or A), where A is sequence of characters (without any whitespace) to be used for the piece identifier, and B is the player mode (";
        for (PlayerMode i : PlayerMode.values()) {
            optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
        }
        optionInfo += "). If B is not given, the default mode '" + DEFAULT_PLAYERMODE.getId()
                + "' is used. If this option is not given a default list of pieces from the corresponding game is used, each assigmed the mode '"
                + DEFAULT_PLAYERMODE.getId() + "'.";

        Option opt = new Option("p", "players", true, optionInfo);
        opt.setArgName("list of players");
        return opt;
    }

    /**
     * Parses the players/pieces option (-p or --players). It sets the value of
     * {@link #pieces} and {@link #playerModes} accordingly.
     * <p>
     * <p>
     * Extrae la opcion players (-p) y asigna el valor de {@link #pieces} y
     * {@link #playerModes}.
     *
     * @param line CLI {@link CommandLine} object.
     * @throws ParseException If an invalid value is provided (@see
     *                        {@link #constructPlayersOption()}).
     *                        <p>
     *                        Si se proporciona un valor invalido (@see
     *                        {@link #constructPlayersOption()}).
     */
    private static void parsePlayersOptions(CommandLine line) throws ParseException {

        String playersVal = line.getOptionValue("p");

        if (playersVal == null) {
            // if no -p option, we take the default pieces from the
            // corresponding
            // factory, and for each one we use the default player mode.
            pieces = gameFactory.createDefaultPieces();
            playerModes = new ArrayList<PlayerMode>();
            for (int i = 0; i < pieces.size(); i++) {
                playerModes.add(DEFAULT_PLAYERMODE);
            }
        } else {
            pieces = new ArrayList<Piece>();
            playerModes = new ArrayList<PlayerMode>();
            String[] players = playersVal.split(",");
            for (String player : players) {
                String[] playerInfo = player.split(":");
                if (playerInfo.length == 1) { // only the piece name is provided
                    pieces.add(new Piece(playerInfo[0]));
                    playerModes.add(DEFAULT_PLAYERMODE);
                } else if (playerInfo.length == 2) { // piece name and mode are
                    // provided
                    pieces.add(new Piece(playerInfo[0]));
                    PlayerMode selectedMode = null;
                    for (PlayerMode mode : PlayerMode.values()) {
                        if (mode.getId().equals(playerInfo[1])) {
                            selectedMode = mode;
                        }
                    }
                    if (selectedMode != null) {
                        playerModes.add(selectedMode);
                    } else {
                        throw new ParseException("Invalid player mode in '" + player + "'");
                    }
                } else {
                    throw new ParseException("Invalid player information '" + player + "'");
                }
            }
        }
    }

    /**
     * Builds the game (-g or --game) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -g.
     *
     * @return CLI {@link {@link Option} for the game option.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */

    private static Option constructGameOption() {
        String optionInfo = "The game to play ( ";
        for (GameInfo i : GameInfo.values()) {
            optionInfo += i.getId() + " [for " + i.getDesc() + "] ";
        }
        optionInfo += "). By defualt, " + DEFAULT_GAME.getId() + ".";
        Option opt = new Option("g", "game", true, optionInfo);
        opt.setArgName("game identifier");
        return opt;
    }

    /**
     * Parses the game option (-g or --game). It sets the value of
     * {@link #gameFactory} accordingly. Usually it requires that
     * {@link #parseDimensionOption(CommandLine)} has been called already to
     * parse the dimension option.
     * <p>
     * <p>
     * Extrae la opcion de juego (-g). Asigna el valor del atributo
     * {@link #gameFactory}. Normalmente necesita que se haya llamado antes a
     * {@link #parseDimensionOption(CommandLine)} para extraer la dimension del
     * tablero.
     *
     * @param line CLI {@link CommandLine} object.
     * @throws ParseException If an invalid value is provided (the valid values are those
     *                        of {@link GameInfo}).
     *                        <p>
     *                        Si se proporciona un valor invalido (Los valores validos son
     *                        los de {@link GameInfo}).
     */
    private static void parseGameOption(CommandLine line) throws ParseException {
        String gameVal = line.getOptionValue("g", DEFAULT_GAME.getId());
        GameInfo selectedGame = null;

        for (GameInfo g : GameInfo.values()) {
            if (g.getId().equals(gameVal)) {
                selectedGame = g;
                break;
            }
        }

        if (selectedGame == null) {
            throw new ParseException("Unknown game '" + gameVal + "'");
        }

        switch (selectedGame) {
            case ADVANCED_TIC_TAC_TOE:
                gameFactory = new AdvancedTTTFactoryExt();
                break;
            case CONNECT_N:
                if (dimRows != null && dimCols != null && dimRows.equals(dimCols)) {
                    gameFactory = new ConnectNFactoryExt(dimRows);
                } else {
                    gameFactory = new ConnectNFactoryExt();
                }
                break;
            case TIC_TAC_TOE:
                gameFactory = new TicTacToeFactoryExt();
                break;
            case ATAXX:
                if (dimRows != null && dimCols != null && dimRows.equals(dimCols)
                        && obstacles != null) {
                    gameFactory = new AtaxxFactoryExt(dimRows, obstacles);
                } else if (obstacles != null) {
                    gameFactory = new AtaxxFactoryExt(obstacles);
                } else if (dimRows != null && dimCols != null && dimRows.equals(dimCols)) {
                    gameFactory = new AtaxxFactoryExt(dimRows, 0);
                } else {
                    gameFactory = new AtaxxFactoryExt();
                }
                break;
            default:
                throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
        }

    }

    /**
     * Builds the dimension (-d or --dim) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -d.
     *
     * @return CLI {@link {@link Option} for the dimension.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */
    private static Option constructDimensionOption() {
        return new Option("d", "dim", true,
                "The board size (if allowed by the selected game). It must has the form ROWSxCOLS.");
    }

    /**
     * Parses the dimension option (-d or --dim). It sets the value of
     * {@link #dimRows} and {@link #dimCols} accordingly. The dimension is
     * ROWSxCOLS.
     * <p>
     * <p>
     * Extrae la opcion dimension (-d). Asigna el valor de los atributos
     * {@link #dimRows} and {@link #dimCols}. La dimension es de la forma
     * ROWSxCOLS.
     *
     * @param line CLI {@link CommandLine} object.
     * @throws ParseException If an invalid value is provided.
     *                        <p>
     *                        Si se proporciona un valor invalido.
     */
    private static void parseDimensionOption(CommandLine line) throws ParseException {
        String dimVal = line.getOptionValue("d");
        if (dimVal != null) {
            try {
                String[] dim = dimVal.split("x");
                if (dim.length == 2) {
                    dimRows = Integer.parseInt(dim[0]);
                    dimCols = Integer.parseInt(dim[1]);
                } else {
                    throw new ParseException("Invalid dimension: " + dimVal);
                }
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid dimension: " + dimVal);
            }
        }

    }

    /**
     * Builds the help (-h or --help) CLI option.
     * <p>
     * <p>
     * Construye la opcion CLI -h.
     *
     * @return CLI {@link {@link Option} for the help option.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */

    private static Option constructHelpOption() {
        return new Option("h", "help", false, "Print this message");
    }

    /**
     * Parses the help option (-h or --help). It print the usage information on
     * the standard output.
     * <p>
     * <p>
     * Extrae la opcion help (-h) que imprime informacion de uso del programa en
     * la salida estandar.
     *
     * @param line           * CLI {@link CommandLine} object.
     * @param cmdLineOptions CLI {@link Options} object to print the usage information.
     */
    private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
            System.exit(0);
        }
    }

    /**
     * Builds the app mode (-am or --app-mode) CLI option.
     *
     * @return CLI {@link {@link Option} for the app mode option
     */
    private static Option constructAppModeOption() {
        return new Option("am", "app-mode", true, "The way the app is started : 'normal', 'server' or 'client'");
    }

    /**
     * Parses the app mode(-am or --app-mode). It sets the appMode value for the game.
     *
     * @param line * CLI {@link CommandLine} object;
     */
    private static void parseAppModeOption(CommandLine line) throws ParseException {
        if (line.hasOption("am")) {
            appMode = null;
            String modesVal = line.getOptionValue("am");
            for (AppMode mode : AppMode.values()) {
                if (modesVal.equals(mode.getId())) {
                    appMode = mode;
                    break;
                }
            }
            if (appMode == null) {
                throw new ParseException("The selected App Mode doesn't exist, please enter 'normal','server'" +
                        " or 'client'");
            }
        } else {
            appMode = DEFAULT_APPMODE;
        }
    }

    /**
     * Builds the server port (-sp or --server-port) CLI option.
     *
     * @return CLI {@link {@link Option} for the server port option
     */
    private static Option constructServerPortOption() {
        return new Option("sp", "server-port", true, "the port the app listens to if server or talks to if client.");

    }

    /**
     * Parses the server port (-sp or --server-port). It sets the serverPort for the game
     *
     * @param line * CLI {@link CommandLine} object;
     */
    private static void parseServerPortOption(CommandLine line) throws ParseException {
        try {
            if (!line.hasOption("sp")) {
                serverPort = DEFAULT_SERVERPORT;
            } else {
                serverPort = Integer.parseInt(line.getOptionValue("sp"));
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Invalid port number : " + serverPort);
        }
    }

    /**
     * Builds the server host (-sh or --server-host) CLI option.
     *
     * @return CLI {@link {@link Option} for the help option.
     * <p>
     * Objeto {@link Option} de esta opcion.
     */
    private static Option constructServerHostOption() {
        return new Option("sh", "server-host", true, "the name for the host of the game in server or client mode");

    }

    /**
     * Parses the help option (-sh or --server-host).It sets the serverHost option for the game.
     *
     * @param line * CLI {@link CommandLine} object.
     */
    private static void parseServerHostOption(CommandLine line) {
        if (!line.hasOption("sh")) {
            serverHost = line.getOptionValue("sh");
        } else {
            serverHost = DEFAULT_SERVERHOST;
        }
    }

    /**
     * Starts a game using a {@link ConsoleCtrl} which is not based on MVC. Is
     * used only for teaching the difference from the MVC one.
     * <p>
     * <p>
     * Método para iniciar un juego con el controlador {@link ConsoleCtrl}, no
     * basado en MVC. Solo se utiliza para mostrar las diferencias con el
     * controlador MVC.
     */
    public static void startGameNoMVC() {
        Game g = new Game(gameFactory.gameRules());
        Controller c = null;

        switch (view) {
            case CONSOLE:
                ArrayList<Player> players = new ArrayList<Player>();
                for (int i = 0; i < pieces.size(); i++) {
                    switch (playerModes.get(i)) {
                        case AI:
                            players.add(gameFactory.createAIPlayer(aiPlayerAlg));
                            break;
                        case MANUAL:
                            players.add(gameFactory.createConsolePlayer());
                            break;
                        case RANDOM:
                            players.add(gameFactory.createRandomPlayer());
                            break;
                        default:
                            throw new UnsupportedOperationException(
                                    "Something went wrong! This program point should be unreachable!");
                    }
                }
                c = new ConsoleCtrl(g, pieces, players, new Scanner(System.in));
                break;
            case WINDOW:
                throw new UnsupportedOperationException(
                        "Swing Views are not supported in startGameNoMVC!! Please use startGameMVC instead.");
            default:
                throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
        }

        c.start();
    }

    /**
     * Starts the application in server mode
     */
    private static void startServer() {
        Game g = new Game(gameFactory.gameRules());
        Controller v = new Controller(g, pieces);
        GameServer server = new GameServer(v, pieces, gameFactory, aiPlayerAlg, serverPort, DEFAULT_TIMEOUT);

        g.addObserver(server);
        server.start();
    }

    /**
     * Starts the application in client mode
     */
    private static void startClient() {
        try {
            Socket sockety = new Socket(serverHost, serverPort);
            GameClient client = new GameClient();
            client.start(sockety, DEFAULT_TIMEOUT);
        } catch (Exception e) {
            System.out.println("Sorry, something very bad happened: " + e);
        }
    }

    /**
     * Starts the application in window mdoe
     */
    private static void startWindow() {
        Game g = new Game(gameFactory.gameRules());
        Controller c = new Controller(g, pieces);

        if (multiviews) {
            for (Piece p: pieces) {
                gameFactory.createSwingView(g, c, p,
                        gameFactory.createRandomPlayer(),
                        gameFactory.createAIPlayer(aiPlayerAlg));
            }
        } else {
            gameFactory.createSwingView(g, c, null,
                    gameFactory.createRandomPlayer(),
                    gameFactory.createAIPlayer(aiPlayerAlg));
        }
        c.start();
    }

    /**
     * Starts the application in console mode(legacy)
     */
    private static void startConsole() {
        Game g = new Game(gameFactory.gameRules());
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            switch (playerModes.get(i)) {
                case AI:
                    System.out.println(aiPlayerAlg);
                    players.add(gameFactory.createAIPlayer(aiPlayerAlg));
                    break;
                case MANUAL:
                    players.add(gameFactory.createConsolePlayer());
                    break;
                case RANDOM:
                    players.add(gameFactory.createRandomPlayer());
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Something went wrong! This program point should be unreachable!");
            }
        }

        Controller c = new ConsoleCtrlMVC(g, pieces, players, new Scanner(System.in));
        gameFactory.createConsoleView(g, c);
        c.start();
    }

    /**
     * Starts a game. Should be called after {@link #parseArgs(String[])} so
     * some fields are set to their appropriate values.
     * <p>
     * <p>
     * Inicia un juego. Debe llamarse despues de {@link #parseArgs(String[])}
     * para que los atributos tengan los valores correctos.
     */
    public static void startGame() {
        switch (appMode) {
            case NORMAL:
                switch (view) {
                    case CONSOLE:
                        startConsole();
                        break;
                    case WINDOW:
                        startWindow();
                        break;
                    default:
                        throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");
                }
                break;
            case SERVER:
                startServer();
                break;
            case CLIENT:
                startClient();
                break;
            default:
                throw new UnsupportedOperationException("Something went wrong! This program point should be unreachable!");

        }

    }

    /**
     * The main method. It calls {@link #parseArgs(String[])} and then
     * {@link #startGame()}.
     * <p>
     * <p>
     * Metodo main. Llama a {@link #parseArgs(String[])} y a continuacion inicia
     * un juego con {@link #startGame()}.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        parseArgs(args);
        startGame();
    }

}
