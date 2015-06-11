package pl.edu.agh.dbclient.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Map;

/**
 * @author mnowak
 */
public class Utils {

    private Utils() {
    }

    public static String joinWithQuotationMarks(Collection<String> collection) {
        return Joiner.on(", ").join(Iterables.transform(collection, new Function<String, String>() {
            @Override
            public String apply(String s) {
                return '\'' + s + '\'';
            }
        }));
    }

    public static boolean isEmptyCollection(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isEmptyMap(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }
}
