package com.github.spartatech.testutils.temporal;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.opentest4j.AssertionFailedError;

import com.github.spartatech.testutils.exception.FieldNotFoundException;



/** 
 * 
 * Assertion utils for Date.
 * 
 * @author Daniel Conde Diehl 
 * 
 * History: 
 *    Dec 29, 2016 - Daniel Conde Diehl
 *    Aug 03, 2020 - ddiehl - Porting to Junit5 Jupiter
 *  
 */
public abstract class DateAssertUtils {

    /**
     * Assert date fields in the date elements.
     *
     * @param message  message in case user wants to show a custom message
     * @param expected Expected Date
     * @param actual   Actual Date
     * @param fields   Calendar fields to be compared
     * @throws AssertionFailedError in case comparison fails
     */
    public static void assertDate(String message, Date expected, Date actual, int... fields) throws AssertionFailedError {
        if (fields.length == 0) {
            assertEquals(expected, actual, message == null ? "Date Mismatch": message);
        }
        
        final Calendar expectedCalendar = Calendar.getInstance();
        expectedCalendar.setTime(expected);
        
        final Calendar actualCalendar = Calendar.getInstance();
        actualCalendar.setTime(actual);
        for (int field : fields) {
            final String fieldName = findCalendarFieldName(field);
            if (expectedCalendar.get(field) != actualCalendar.get(field)) {
                throw new AssertionFailedError(message == null ? "Field "+ fieldName+ " mismatch": message, 
                        String.valueOf(expectedCalendar.get(field)), String.valueOf(actualCalendar.get(field)));
            }
        }
    }

    /**
     * Assert date fields in the date elements. Shows a standard failures message.
     *
     * @param expected Expected Date
     * @param actual   Actual Date
     * @param fields   Calendar fields to be compared
     * @throws AssertionFailedError in case comparison fails
     */
    public static void assertDate(Date expected, Date actual, int... fields) throws AssertionFailedError {
        assertDate(null, expected, actual, fields);
    }


    /**
     * Compares Dates by format
     *
     * @param message  to be presented in case of error
     * @param expected expected date
     * @param actual   actual date
     * @param format   format to be applied to both dates before comparing
     * @throws AssertionFailedError in case comparison fails
     */
    public static void assertDateByFormat(String message, Date expected, Date actual, String format) throws AssertionFailedError {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        final String actualFormatted = sdf.format(actual);
        final String exepctedFormatted =sdf.format(expected);
        
        if (!actualFormatted.equals(exepctedFormatted)) {
            throw new AssertionFailedError(message == null ? "Date mismatch": message, 
                    exepctedFormatted, actualFormatted);
        }
    }

    /**
     * Compares Dates by format.
     *
     * @param expected expected date
     * @param actual   actual date
     * @param format   format to be applied to both dates before comparing
     * @throws AssertionFailedError in case comparison fails
     */
    public static void assertDateByFormat(Date expected, Date actual, String format) throws AssertionFailedError {
        assertDateByFormat(null, expected, actual, format);
    }

    /**
     * Finds a field name of the calendar by the field Id
     *
     * @param fieldId Field Id, value
     * @return Field Name
     * @throws FieldNotFoundException when the fields does not exist for Calendar
     */
    private static String findCalendarFieldName(int fieldId) throws FieldNotFoundException {
        for (Field field : Calendar.class.getFields()) {
            if (field.getType() == Integer.TYPE) {
                try {
                    if (field.getInt(null) == fieldId) {
                        return field.getName();
                    }
                } catch (IllegalArgumentException|IllegalAccessException e) {
                    throw new FieldNotFoundException("Field ["+ fieldId + "] not found as a constant in Calendar");
                }
            }
        }
        throw new FieldNotFoundException("Field ["+ fieldId + "] not found as a constant in Calendar");
    }
    
}
