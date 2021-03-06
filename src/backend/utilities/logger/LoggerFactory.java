package backend.utilities.logger;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
//import utilities.IdFactory;

import backend.ast.ASTException;
import backend.atoms.components.AtomicRegionException;
import backend.utilities.exception.ArgumentException;
import backend.utilities.exception.DebugException;
import backend.utilities.exception.GeometryException;
import backend.utilities.exception.NotImplementedException;

//
// Factory design patter for all logging channels in this project
//
public class LoggerFactory
{
    private static IdFactory _ids;

    public static final int DEBUG_OUTPUT_ID = 0;
    public static final int MATLAB_RECORDER_OUTPUT_ID = 1;
    public static final int DEFAULT_OUTPUT_ID = 2; // added by Drew Whitmire
    public static final int EXCEPTION_OUTPUT_ID = 3;

    // The set of loggers
    protected static ArrayList<Logger> _loggers;

    // A default logger where output goes to die
    protected static Logger _deathLogger;
    
    // A counter that is used to check if the logger factory should be
    // initialized or closed
    private static int init_close_count = 0;

    // This will be a static class
    protected LoggerFactory() {}

    /*A constructor of sorts
     * @author Chris Alvin
     * <modified by> Ryan Billingsly 9/6/2016
     * Made this a static declaration in place of initialize(Logger debug)
     * <p>
     * @modified Drew Whitmire
     * added default logger and made default exception logger a child of the default logger
     * moved back to initialize to handle multiple calls to initialize and close (uses a semaphore
     * to only close if the close matches up with the first initialize)
     * EX:
     *      initialize() // actually initializes
     *      initialize() // does nothing
     *      close()      // does nothing
     *      initialize() // does nothing  
     *      close()      // does nothing
     *      close()      // actually closes
     */
    static 
    {
        // This is all old code.  If you wish to move back to this type of initialization, take the code out 
        // of the Initialize() function.
//        _ids = new IdFactory(EXCEPTION_OUTPUT_ID + 1);
//        _loggers = new ArrayList<Logger>();
//        buildLogger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DebugLog.txt");                     // debug logger id 0
//        buildLogger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\MatlabLog.txt");                     // matlab logger id 1
//        
//        // default logger id 2
//        //_loggers.add(new Logger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultLog.txt"));     
//        buildLogger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultLog.txt"); 
//        
//        // exception default logger id 3
//        //_loggers.add(new Logger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultExceptionLog.txt", getLogger(DEFAULT_OUTPUT_ID)));    
//        buildLogger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultExceptionLog.txt", getLogger(DEFAULT_OUTPUT_ID));    
//        
//        // Initializing the null output stream
//        //_deathLogger = new Logger(new NullOutputStreamWriter());
//        _deathLogger = new Logger();
    }

    /*
     * Acquire a given logger based on a key value
     * If an id is not recognized return the null logger
     * @author Chris Alvin
     * <modified> 9/5/2016
     * <by> Ryan Billingsly
     * Now handles negative input appropriately 
     */
    public static Logger getLogger(int id)
    {
        if (id >= _loggers.size() || id <= 0) return _deathLogger;

        return _loggers.get(id);
    }

    /**
     * @modified Drew Whitmire
     * @param logger
     * @return the position of the logger in the array
     */
    protected static int addLogger(Logger logger)
    {
        _loggers.add(logger);

        _ids.getNextId();

        return _loggers.indexOf(logger);
    }

    // A constructor of sorts
    protected static int addLogger(Writer writer)
    {
        return addLogger(new Logger(writer));
    }



    /*
     * This will validate that the ID provide is associated with an existing logger
     * NOTE: This only checks and makes sure a logger is not its own parent.  
     * This DOES NOT check any circularity with other nodes.
     * THINKING OUT LOUD - Does it need to?  When we add a logger, it must pass a logger
     * as a parentId, but that logger must either a) currently exist on the tree; or
     * b) the logger must have been created in the addLogger function call
     * (i.e. addLogger(someLogger, new Logger());).  In either case, the parent logger
     * would exist in the tree and therefore could not generate circularity at all.
     * @author Ryan Billingsly
     * @param The logger being added to the list, and the ID of the parent
     * 
     * @modified Drew Whitmire
     * @date 9/24/2016
     * @return the logger id for a new logger
     */
    protected static int addLogger(Logger logger, Logger parentId)
    {
        if (logger.equals(parentId))
        {
            logger._parentId = null;
        }
        else 
        {
            logger._parentId = parentId;
        }
        _loggers.add(logger);
        _ids.getNextId();
        return _loggers.indexOf(logger);
    }

    /*
     * Logging to System.out
     * @author Chris Alvin
     * @modified Ryan Billingsly
     * @date 9-6-2016
     * Modified so that the method will now add the logger to the list automatically
     * 
     * @modified Drew Whitmire
     * @date 9/24/2016
     * Sets the loggerId
     */
    public static Logger buildLogger()
    {
        Logger logger = new Logger(_loggers.get(0));
        logger.setLoggerId(addLogger(logger));
        return logger;
    }
    
    public static Logger buildLogger(Logger parentId)
    {
        Logger logger = new Logger(parentId);
        logger.setLoggerId(addLogger(logger));
        return logger;
    }
    

    /*
     * Logging to a particular file
     * @author Chris Alvin
     * @modified Ryan Billingsly
     * @date 9-6-2016
     * Modified so that the method will now add the logger to the list automatically
     */
    public static Logger buildLogger(String filePath)
    {
        Logger logger = new Logger(filePath);
        logger.setLoggerId(addLogger(logger));
        return logger;
    }

    /*
     * @author Ryan Billingsly
     * @param the intended filepath as a string, the id for the parent logger
     * @returns a newly constructed logger that is added to the tree of loggers.
     *  This logger will have a parent logger assigned, assuming the parent logger
     *  is a valid logger.
     */
    public static Logger buildLogger(String filePath, Logger parentId)
    {
        Logger logger = new Logger(filePath, parentId);
        logger.setLoggerId(addLogger(logger));
        return logger;
    }

    // A constructor of sorts
    // "Wouldn't this be a deconstructor of sorts?"  -Ryan Billingsly
    public static void close()
    {
        // decrement the counter]
        init_close_count--;
        
        if (init_close_count == 0)
        {
            try
            {
                for (Logger logger : _loggers)
                {
                    try
                    {
                        if (logger != null) logger.close();
                    }
                    catch (IOException ioe)
                    {
                        System.err.println(ioe.getMessage());
                        ioe.printStackTrace();
                    }
                }
                _deathLogger.close();
            }
            catch (IOException ioe)
            {
                System.err.println(ioe.getMessage());
                ioe.printStackTrace();
            }
        }
    }
    
    /**
     * method to create loggers and set loggerIDs of exception classes
     * @throws IOException 
     */
    public static void initialize() throws IOException
    {
        // increment the counter\
        init_close_count++;
        
        // if the logger factory is uninitialized
        if (init_close_count == 1)
        {
            // *****default loggers
            File dir = new File(".");
            String path = dir.getCanonicalPath();
            _ids = new IdFactory(EXCEPTION_OUTPUT_ID + 1);
            _loggers = new ArrayList<Logger>();
            buildLogger(path + "\\src\\backend\\logs\\DebugLog.txt");                     // debug logger id 0
            buildLogger(path + "\\src\\backend\\logs\\MatlabLog.txt");                    // matlab logger id 1
            
            // default logger id 2
            //_loggers.add(new Logger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultLog.txt"));     
            buildLogger(path + "\\src\\backend\\logs\\DefaultLog.txt"); 
            
            // exception default logger id 3
            //_loggers.add(new Logger("C:\\Users\\Drew W\\Documents\\bradley\\iTutor\\DefaultExceptionLog.txt", getLogger(DEFAULT_OUTPUT_ID)));    
            buildLogger(path + "\\src\\backend\\logs\\exception\\DefaultExceptionLog.txt", getLogger(DEFAULT_OUTPUT_ID));    
            
            // Initializing the null output stream
            //_deathLogger = new Logger(new NullOutputStreamWriter());
            _deathLogger = new Logger();
            
            // ****build the Loggers****
            // AST Loggers
            Logger astLogger = buildLogger(path + "\\src\\backend\\logs\\exception\\ast\\ASTLog.txt", 
                    getLogger(EXCEPTION_OUTPUT_ID));
            
            Logger geometryLogger = buildLogger(path + "\\src\\backend\\logs\\exception\\ast\\GeometryLog.txt", 
                    getLogger(astLogger.getLoggerId()));
            
            // atomic region loggers
            Logger atomsLogger = LoggerFactory.buildLogger(path + "\\src\\backend\\logs\\exception\\AtomicRegionsLog.txt", 
                    LoggerFactory.getLogger(LoggerFactory.EXCEPTION_OUTPUT_ID));
            
            // generic exception logger
            Logger generalExceptionLogger = buildLogger(path + "\\src\\backend\\logs\\exception\\GeneralExceptionLog.txt", 
                    getLogger(EXCEPTION_OUTPUT_ID));
            
            
            // ***set the Exception classes' static logger IDs***
            ASTException.setLoggerID(astLogger);
            GeometryException.setLoggerID(geometryLogger);
            NotImplementedException.setLoggerID(generalExceptionLogger);
            ArgumentException.setLoggerID(generalExceptionLogger);
            DebugException.setLoggerID(DEBUG_OUTPUT_ID);
            AtomicRegionException.setLoggerID(atomsLogger);
        }
        
    }

}
