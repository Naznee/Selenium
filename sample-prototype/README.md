Test Given Search value in Google and assert Right hand side Title Values. 

1. input = Junit Test, expected = Unit testing, status = pass
2. input = Womens day, expected = Woman's Day Fail Test For report, status = fail
3. input = Fathers Day, expected = Father, status = pass

And Generate Report using Extent 
1. Start of test log
2. capture of Screenshot after search in google
3. Status of Test log using test watcher.

Sample test report are attached under src/test/resources for reference.

http://htmlpreview.github.io/?https://github.com/Naznee/Selenium/blob/master/sample-prototype/src/test/resources/GoogleExtentReport.html

Prerequisites
1. Eclipse 
2. JDK 1.8 
3. Maven and Git Plugin in Eclipse
 