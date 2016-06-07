This FrameWork is KeyWord Driven . 
*********************************
Can create Test using excel
Grid
Used - TestNg , POI 
Reports

***********************************************
To Create you demo Test (Login to gmail with invalid input)
=======================================
Step 1 -> Import Project from Git hub 

Step 2 >  Open TestCaseSelection_Singlesheet.xlsx file and in TestCases Sheet  

BusinessModule	TestScript		Description	Execute		NumberOfIterations	 	StartIndexForIterations
Gmail		Gmail_login	Userentervalues	Yes		1			1

Step 3 > In testData[pakage]  Copy Demo_TestData.xls file and rename it to Gmail.xlsx 
Step 4 > Open Gmail.xlsx and enter all req. field in TestSteps and TestData Sheet 
ObjectID must match with OR.proprties file 

Now to Run ********
Step 5 > Double Click on Start_StandaloneExecution.bat 

Note :- If you want to Run Same test different browser Edit "Config.properties" file  [URL and Browser ...etc ]




KeyWords 
=======

click
confirm_Message
enter_text
function=EndOfScript
function=Logout
function=selectPhoneType
selectByVisibleText
highlight
verifyElementPresent
waitForPageToLoad
waitForXPath

**************
Passing data 
**************
getData=Amount
