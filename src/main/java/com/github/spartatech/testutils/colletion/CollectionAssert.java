package com.github.spartatech.testutils.colletion;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 
 * Asserts for Collections.
 * 
 * @author Daniel Diehl - Sparta Team
 * 
 * History: 
 *    Jan 14, 2017 - ddiehl
 *    Apr 06, 2018 - ddiehl - Adjusting problem with asseertListByReflection with null field.
 *    Aug 03, 2020 - ddiehl - Porting to Junit5 Jupiter
 *  
 */
public abstract class CollectionAssert {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(CollectionAssert.class);
    
    /**
     * Compares two lists using the comparator
     * 
     * @param <A> Type of list element
     * @param listOne left side list
     * @param listTwo right side list
     * @param elementComparator comparator to compare records
     */
    public static <A> void assertList(final Collection<A> listOne, final Collection<A> listTwo, Comparator<A> elementComparator) {
        final List<A> listOneCopy = new ArrayList<>(listOne);
        final List<A> listTwoCopy = new ArrayList<>(listTwo);
        for (Iterator<A> iteratorList1 = listOneCopy.iterator(); iteratorList1.hasNext();) {
            A itemListOne = iteratorList1.next();
            
            if (listTwoCopy.isEmpty()) {
                fail("List two is missing items");
            }
            
            for (Iterator<A> iteratorList2 = listTwoCopy.iterator(); iteratorList2.hasNext();) {
                A itemListTwo = iteratorList2.next();
                if (elementComparator.compare(itemListOne, itemListTwo) == 0) {
                    iteratorList1.remove();
                    iteratorList2.remove();
                    break;
                }
            }
        }
        
        if (!listTwoCopy.isEmpty() || !listOneCopy.isEmpty()) {
            final StringBuilder msg =  new StringBuilder("Lists are not similar.").append(System.getProperty("line.separator"));
            listOneCopy.forEach(item -> msg.append("List one: ").append("Remaining: " + ReflectionToStringBuilder.toString(item)).append(System.getProperty("line.separator")));
            listTwoCopy.forEach(item -> msg.append("List two: ").append("Remaining: " + ReflectionToStringBuilder.toString(item)).append(System.getProperty("line.separator")));
            fail(msg.toString());
        }
    }
    
    /**
     * Assert that two lists are same using reflection to compare elements. 
     * 
     * @param <A> Type of list element
     * @param listOne List one to be compared
     * @param listTwo list two compared
     * @param excludedFields fields to exclude from comparison
     */
    public static <A> void assertListByReflection(final Collection<A> listOne, final Collection<A> listTwo, String...excludedFields) {
        List<String> excludedFieldsList = Arrays.asList(excludedFields);
        assertList(listOne, listTwo, (item1, item2) -> {
            Field[] fields = item1.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!excludedFieldsList.contains(field.getName())) {
                    try {
                        field.setAccessible(true);
                        Object val1 = field.get(item1);
                        Object val2 = field.get(item2);
                        field.setAccessible(false);
                        
                        if (val1 == null && val2 != null) {
                           LOGGER.debug("For Field {}: val1 was null, but val2 was not null");
                           return 1;
                        }

                        if (val1 != null && val2 == null) {
                            LOGGER.debug("For Field {}: val1 was not null, but val2 was null");
                            return 1;
                         }
                        
                        if (val1== null && val2 == null) {
                            LOGGER.debug("Both are null considering same");
                        } else {
                            String val1Str = ReflectionToStringBuilder.toString(val1, ToStringStyle.SHORT_PREFIX_STYLE);
                            String val2Str = ReflectionToStringBuilder.toString(val2, ToStringStyle.SHORT_PREFIX_STYLE);
                            if (!val1Str.equals(val2Str)) {
                                LOGGER.debug("Field=[{}]. val1={}, val2={}", field.getName(), val1, val2);
                                return 1;
                            }
                        }
                    } catch (Exception e) {
                        fail("Exception not expected comparing field "+ field.getName() + ": " + e.getMessage());
                    }
                }
            }
            return 0;
        });
    }
    
}
