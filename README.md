# sparta-test-utils - Junit5 
[![Build Status](https://travis-ci.org/SpartaTech/sparta-test-utils-junit5.svg?branch=master)](https://travis-ci.org/SpartaTech/sparta-test-utils-junit5)
[![Coverage Status](https://coveralls.io/repos/github/SpartaTech/sparta-test-utils-junit5/badge.svg?branch=master)](https://coveralls.io/github/SpartaTech/sparta-test-utils-junit5?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.spartatech/sparta-test-utils-junit5/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.spartatech/sparta-test-utils-junit5/)

_*This is a port for Junit 5 Jupiter*_

*Library with Unit Test utilities.*

Sparta Test Utils provides several assert utilities to help you unit testing your Java application. These helpers include: 

* **Collection Assert** -> Helpers to assert lists.
* **Logback Asserts** -> Provides a way to assert that your log calls were executed.
* **Temporal Asserts** -> Helpers to assert Dates.

Below there are an explanation on how to use each one.

## Collection Assert
Collection assert provides a way to compare two lists and asserts that both lists has the same elements. The comparison algorithm can be either provided by you or using a field-by-field copmarison.

There are these options available currently:

### Assert using field-by-field comparison
In this option the asserter will compare all the fields using Reflection. For each field it will generate a reflectionToString and compare the values. 
The order of the elements in the lists is not considered, it will try to match elements in any order.

***Usage:***

~~~Java
CollectionAssert.assertListByReflection(listOne, listTwo);
~~~

### Assert using field-by-field comparison, with excluded fields
There is also the option to make the same asserts as described in the previous method, but excluding some fields from the comparison. In this case all the other fields will be compared but the ones in the list. 
The order of the elements in the lists is not considered, it will try to match elements in any order.

***Usage:***

~~~Java
CollectionAssert.assertListByReflection(listOne, listTwo, "address", "password");
~~~

### Assert using custom Comparator
Some times you need to have a custom way to compare the elements in the lists, to do so a method was provided where you can pass a comparator. Using this approach, the elements will be matched using your defined algorithm.
The order of the elements in the lists is not considered, it will try to match elements in any order.

***Usage:***

~~~Java
CollectionAssert.assertList(listOne, listTwo, 
                       (a,b) -> a.equals(b)? 0 : 1);
~~~

## Logback Asserts
There are some unit tests that require you to assert that the log was written. Testing log is something difficult because logging libraries, like Logback, do a good job abstracting all the logic.

In order to help developers to test their code, we created a way to assert that log calls were made to Logback system. 

The way it works is: 

* Write your log expectations
* Call the method to be tested
* Call the asserter method to ensure all the logs happened as expected. 

***The method UnitTestAsserterLogback.assertLogExpectations() is deprecated, use UnitTestAsserterLogback.assertLogExpectations(false) instead***



***Usage:***

~~~Java
//Init the Asserter
final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(<<You Class or Logger>>);

//Indicate which logLevel, message, and optional parameters for the log should happen
spyAppender.addExpectation(Level.INFO, message);
        
//Execute your code

//Calls assert method
/*False means the the logs has to happen in the same order 
as the expectations an the exact same logs hapened, */
/*True means that lib will only check if the expectations happened 
regardless of any extra ones, 
also it will not care for order that it was called.
*/
spyAppender.assertLogExpectations(false);
~~~

***Example:***

~~~Java
public static final Logger LOGGER = LoggerFactory.getLogger(MyTest.class);
    
@Test
public void test() {
	final String message = "new message {}, {}";
	final int param1 = 1;
	final String param2 = "New Param";
	final UnitTestAsserterLogback spyAppender = new UnitTestAsserterLogback(this.getClass());
	spyAppender.addExpectation(Level.INFO, message, param1, param2);
	
	LOGGER.info(message, param1, param2);
	
	spyAppender.assertLogExpectations(false);
}
~~~

## Temporal Asserts
Helpers to test Java Dates. 

Uses:

* Assert only some parts of a Date object
* Assert Date by a specific DateFormat

### Assert by date elements

You can assert a date only by some elements, let's say you want to assert the Date, but only the date piece, not time. 

***Usage:***

~~~Java
DateAssertUtils.assertDate(expected, actual, Calendar.DATE, Calendar.MONTH, Calendar.YEAR);
~~~

### Assert by DateFormat

Another usage, instead of informing which fields to test, is to give a DateFormat String to be compared. In this case, code will convert the Date to a String in that format and then compare.

***Usage:***

~~~Java
DateAssertUtils.assertDateByFormat("Date Did no match", expected, actual, "yyyy-MM-dd HH:mm:ss");
~~~

## Exception Asserts

Helpers to assert Exceptions. Provides more control to verify the Exception scenarios than @Test(expected=Exception)

Uses:

* Assert Exception message

### Assert exception message

Let's you assert that teh exception being thrown is the one you're expecting also matching the message.

* The first parameter is the exception you are expecting with the message that should happen.
* The second parameter is a boolean indicating of the exception happened in the tested method should be thrown or swallowed. You can use true if you want to stop execution and in you annotation use @Test(expected=Exception), or false if you just want to assert this exception, and finish the test.
* the third parameter is the block of code that you want to be tested (the one that suppose to throw the exception).

***Usage:***

~~~Java
ExceptionAssert.assertExceptionMessage(new Exception("Message Expected"), false, 
	() -> {
			//Call the method to be tested
	});
~~~
