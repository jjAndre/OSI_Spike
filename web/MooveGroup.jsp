<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import = "java.sql.*, java.util.*, javax.servlet.*, java.io.*, com.bazarket.localdbconnect.entities.*" %>

<html>

<!--
This file serves to show the results of DbAccessLocForSmth file execution. All elements of the Konstruktor's target group mooved
Here are 2 sets of the data are on a screen
 1) actual coordinates of the target group's elements
 2) the previous ones
-->

<head>
    <meta charset="utf-8">
</head>


<body>
	<h1 align="center">MooveGroup.jsp</h1>




    Actual coordinates of the RootGroup's items
    <p>
    <c:forEach var = "entry" items = "${newMapOfXY}" >
        <br>Item_ID: ${entry.key}

        <c:forEach var = "elxy" items = "${entry.value}" varStatus = "elTurn">


            <c:if test = "${elTurn.count == 1}" >
                X: ${elxy}
            </c:if>

            <c:if test = "${elTurn.count == 2}">
                Y: ${elxy}
            </c:if>

        </c:forEach>

     </c:forEach>


<%
      try{
             out.print("<br><br>Previous coordinates of the RootGroup's items");
             //
               Map<Integer, int[]> oldMapXY = (HashMap)request.getAttribute("oldMapOfXY");
               Set<Map.Entry<Integer, int[]>> setOld = oldMapXY.entrySet();

               	for(Map.Entry<Integer, int[]> sOld : setOld){
               		int curKey = sOld.getKey();
               		int[] curAr = sOld.getValue();

               		out.print("<br>Item_ID: " + curKey + " X: " + curAr[0] + ", Y: " + curAr[1]);
               	}
      } catch (Exception e){

      }

%>

<form method = "POST"
             action = "AskFocus.do">
             Ask for Client actions :<p>

             <br><br>Moove all Items in FindAllItemsInGroup?


                 <!--
                    <input type="checkbox" name="isMooved" value = "true">Yes
                 -->

                    <br><br>Restore the previous Group position?
                    <input type="checkbox" name="reStore" value = "true">Yes

                    <br><br>

                           <left>
                             <input type = "SUBMIT">
                           </left>
</form>

<br><br>
<form method = "POST"
       action = "http://localhost:8080/OSI-localv2/AskFocus.do">


       <input type = "submit" name = "newGroupMove" value = "true">
       <!--
       <input type = "submit" value = "To moove new Group items">
       -->
</form>

</body>
</html>