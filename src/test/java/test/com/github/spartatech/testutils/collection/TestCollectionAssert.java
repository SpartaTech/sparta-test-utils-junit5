package test.com.github.spartatech.testutils.collection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.jupiter.api.Test;

import com.github.spartatech.testutils.colletion.CollectionAssert;

/** 
 * 
 * Unit tests for CollectionAssert
 * 
 * @author ddiehl
 * 
 * History: 
 *    Jan 14, 2017 - ddiehl
 *    Aug 03, 2020 - ddiehl - Porting to Junit5 Jupiter
 *  
 */
public class TestCollectionAssert extends CollectionAssert {

    @Test
    public void testAssertReflectionSameList() {
        final Date date = new Date();
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));
        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 1, date, 1L));
        listTwo.add(new TestObj("two", 2, date, 2L));

        CollectionAssert.assertListByReflection(listOne, listTwo);
    }
    
    @Test
    public void testAssertReflectionSameListExcludeField() {
        final Date date = new Date();
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));
        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 3, date, 1L));
        listTwo.add(new TestObj("two", 4, date, 2L));

        CollectionAssert.assertListByReflection(listOne, listTwo, "field2");
    }

    @Test
    public void testAssertReflectionDifferentLists() {
        final Date date = new Date();
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));
        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 3, date, 1L));
        listTwo.add(new TestObj("two", 4, date, 2L));

        boolean hadException = false;
        try {
            CollectionAssert.assertListByReflection(listOne, listTwo);
        } catch (AssertionError e) {
            hadException = true;
        }
        
        if (!hadException) {
            fail("Expected assertion error");
        }
    }
    
    @Test
    public void testAssertListDoesNotMatch () {
        final Date date = new Date();
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));
        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 1, date, 1L));
        listTwo.add(new TestObj("two", 2, date, 2L));
        
        try {
            CollectionAssert.assertList(listOne, listTwo, (a,b) -> a.equals(b)? 0 : 1);
        } catch(AssertionError e) {
        	final String br = System.getProperty("line.separator");
        	assertEquals(
        			"Lists are not similar." +  br
        			+ "List one: Remaining: " + ReflectionToStringBuilder.toString(listOne.get(0)) + br
        			+ "List one: Remaining: " + ReflectionToStringBuilder.toString(listOne.get(1)) + br
        			+ "List two: Remaining: " + ReflectionToStringBuilder.toString(listTwo.get(0)) + br
        			+ "List two: Remaining: " + ReflectionToStringBuilder.toString(listTwo.get(1)) + br
        			,e.getMessage()
        			);
        }
    }
    
    @Test
    public void testAssertListMatches () {
        final Date date = new Date();
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));
        final List<TestObj> listTwo = new ArrayList<>(listOne);

        CollectionAssert.assertList(listOne, listTwo, (a,b) -> a.equals(b)? 0 : 1);
    }
    
    @Test
    public void testAssertNullFieldLeftSide () throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 02);
        cal.set(Calendar.DATE, 03);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 13);
        final Date date = cal.getTime();
        
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, null, 2L));

        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 1, date, 1L));
        listTwo.add(new TestObj("two", 2, date, 2L));

        try {
            CollectionAssert.assertListByReflection(listOne, listTwo);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            e.printStackTrace();
            
            final StringBuilder expectedMessage = new StringBuilder("Lists are not similar.");
            expectedMessage.append(System.getProperty("line.separator"));
            expectedMessage.append("List one: Remaining: " + ReflectionToStringBuilder.toString(listOne.get(1)));
            expectedMessage.append(System.getProperty("line.separator"));
            expectedMessage.append("List two: Remaining: " + ReflectionToStringBuilder.toString(listTwo.get(1)));
            expectedMessage.append(System.getProperty("line.separator"));
            assertEquals(expectedMessage.toString(), e.getMessage());
        }
    }
    
    @Test
    public void testAssertNullFieldRightSide () throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 02);
        cal.set(Calendar.DATE, 03);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 13);
        final Date date = cal.getTime();
        
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, date, 2L));

        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 1, date, 1L));
        listTwo.add(new TestObj("two", 2, null, 2L));

        try {
            CollectionAssert.assertListByReflection(listOne, listTwo);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            e.printStackTrace();
            
            final StringBuilder expectedMessage = new StringBuilder("Lists are not similar.");
            expectedMessage.append(System.getProperty("line.separator"));
            expectedMessage.append("List one: Remaining: " + ReflectionToStringBuilder.toString(listOne.get(1)));
            expectedMessage.append(System.getProperty("line.separator"));
            expectedMessage.append("List two: Remaining: " + ReflectionToStringBuilder.toString(listTwo.get(1)));
            expectedMessage.append(System.getProperty("line.separator"));
            assertEquals(expectedMessage.toString(), e.getMessage());
        }
    }
    
    @Test
    public void testAssertNullFieldBothSides () throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, 02);
        cal.set(Calendar.DATE, 03);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 11);
        cal.set(Calendar.SECOND, 13);
        final Date date = cal.getTime();
        
        final List<TestObj> listOne = new ArrayList<>();
        listOne.add(new TestObj("one", 1, date, 1L));
        listOne.add(new TestObj("two", 2, null, 2L));

        final List<TestObj> listTwo = new ArrayList<>();
        listTwo.add(new TestObj("one", 1, date, 1L));
        listTwo.add(new TestObj("two", 2, null, 2L));

        CollectionAssert.assertListByReflection(listOne, listTwo);
    }
    
    class TestObj {
        private String field1;
        private int field2;
        private Date field3;
        private Long field4;
        
        
        /**
         * @param field1
         * @param field2
         * @param field3
         * @param field4
         */
        public TestObj(String field1, int field2, Date field3, Long field4) {
            super();
            this.field1 = field1;
            this.field2 = field2;
            this.field3 = field3;
            this.field4 = field4;
        }
        /**
         * @return the field1
         */
        public String getField1() {
            return field1;
        }
        /**
         * @param field1 the field1 to set
         */
        public void setField1(String field1) {
            this.field1 = field1;
        }
        /**
         * @return the field2
         */
        public int getField2() {
            return field2;
        }
        /**
         * @param field2 the field2 to set
         */
        public void setField2(int field2) {
            this.field2 = field2;
        }
        /**
         * @return the field3
         */
        public Date getField3() {
            return field3;
        }
        /**
         * @param field3 the field3 to set
         */
        public void setField3(Date field3) {
            this.field3 = field3;
        }
        /**
         * @return the field4
         */
        public Long getField4() {
            return field4;
        }
        /**
         * @param field4 the field4 to set
         */
        public void setField4(Long field4) {
            this.field4 = field4;
        }
        
        
    }
}
