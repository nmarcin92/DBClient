package pl.edu.agh.dbclient.utils;

import org.apache.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author mnowak
 */
public class Configuration {

    private static final Logger LOGGER = Logger.getLogger(Configuration.class);
    private static final String PROP_FILENAME = "config";
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle(PROP_FILENAME);
    }

    private Configuration() {}

    public static String getString(String key) {
        return getString(key, null);
    }

    public static String getString(String key, String defaultValue) {
        try {
            return BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.error("Missing resource: " + key + ", returning default: " + defaultValue, e);
            return defaultValue;
        }
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException e) {
            LOGGER.error("Couldn't parse resource " + key + " to integer value", e);
            return defaultValue;
        }
    }

    public static Integer getInteger(String key) {
        return getInteger(key, null);
    }

}
