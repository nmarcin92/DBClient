package pl.edu.agh.dbclient.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.Collection;

/**
 * @author mnowak
 */
public class Utils {

    private Utils() {
    }

    public static String joinWithQuotationMarks(Collection<String> collection) {
        return Joiner.on(", ").join(Iterables.transform(collection, new Function<String, String>() {
            @Override public String apply(String s) {
                return '\'' + s + '\'';
            }
        }));
    }

}
