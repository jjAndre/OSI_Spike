<%@ page import = "java.sql.*, java.util.*, javax.servlet.*, java.io.*" %>


<html>

<head>
    <meta charset="utf-8">
</head>


<body>
	<h1 align="center">From OSI Database JSP</h1>
	<p>		




		<br>Focus Position N1: x = ${focusXY1[0]}, y = ${focusXY1[1]})<br>

		<br>Focus Position N2: x = ${focusXY2[0]}, y = ${focusXY2[1]}<br>



		<form method = "POST"
             action = "AskFocus.do">
             Ask for current File Focus:<p>

             Type elementID from your Browser:
                     <input type="text" name="elementId" size = 25>

                     <br><br>Type elementType 1 for groups
                     <input type="text" name="elementType" size = 25>



                     <!--Сдвиг по X, Y вручную-->
                     <br><br>Moove Focus X by:
                     <input type="text" name="mooveX" size = 25>

                     <br><br>Moove Focus Y by:
                     <input type="text" name="mooveY" size = 25>



                     <br><br><br>User's actions

                     <br><br>Find focus mooving again?
                     <input type="checkbox" name="findFocusAgain" value = "true">Yes, find again

                     <br><br>Moove all Items in FindAllItemsInGroup?
                     <input type="checkbox" name="isMooved" value = "true">Yes, moove them all

             <!--
                     <br><br>Restore the previous Group position?
                     <input type="checkbox" name="reStore" value = "true">Yes

              -->
              <br><br>



              <left>
                <input type = "SUBMIT">
              </left>
            </form>


	


</body>
</html>