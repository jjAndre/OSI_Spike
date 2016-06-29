<%@ page import = "java.sql.*, java.util.*" %>

<html>

<head>
    <meta charset="utf-8">
</head>


<body>
	<h1 align="center">From OSI Database JSP</h1>
	<p>		
		<!-- Scriptlet code. Java sitting inside percent tags-->

		<%
		try {
		int[] pos1 = (int[])request.getAttribute("focusXY1");
        int[] pos2 = (int[])request.getAttribute("focusXY2");

        out.print("<br>Focus Position N1: x = " + pos1[0] + " , y = " + pos1[1]);
        out.print("<br>Focus Position N2: x = " + pos2[0] + " , y = " + pos2[1]);

		} catch (Exception e){
		out.print("<br>We've asked for current File Focus Position, however we don't expect here we''ll be results from DbAccesLoc");
		}


		%>

		<form method = "POST"
             action = "AskFocus.do">

        <!--
             Ask for current File Focus:<p>


             Type your login (email)<p>
                     For AskFocus.do:
                     <input type="text" name="loginEmail" size = 25>
                     <br><br><br>

                     For AskFocus.do<p>
        -->

        Type elementID from your Browser:
               <input type="text" name="elementId" size = 25>

        <!--
                  <br>Type elementType 1 for groups
                  <input type="text" name="elementType" size = 25>
        -->
              <br><br>

              <left>
                <input type = "SUBMIT">
              </left>
            </form>


		<%

		//Запрашиваем объект, который присвоили в качестве аттрибута объекту request



		Map<int[], int[]> groupItemsMap = null;
		Set<Map.Entry<int[], int[]>> set = null;

		groupItemsMap = (HashMap)request.getAttribute("groupItemsMap");
        set = groupItemsMap.entrySet();

        for(Map.Entry<int[], int[]> s : set){
        		//s.getKey[0], s.getKey[1]

        	int curItemId = s.getKey()[0];
        	int curItemType = s.getKey()[1];
        		//int curItemType = s.getValue();

        	out.print("<br>Item_ID: " + curItemId + " Item_type: " + curItemType);
        }

		/*
		Map<Integer, int[]> mapXY = (HashMap)request.getAttribute("listitems");
		Set<Map.Entry<Integer, int[]>> set = mapXY.entrySet();

		for(Map.Entry<Integer, int[]> s : set){
		int curKey = s.getKey();
		int[] curAr = s.getValue();

		out.print("<br>Item_ID: " + curKey + " X: " + curAr[0] + ", Y: " + curAr[1]);
		}
		*/


		/*
		List listToShow = (List)request.getAttribute("listitems");
		for (int i = 0; i < listToShow.size(); i++) {
                    out.print("<br>Item_ID: " + listToShow.get(i));
                }
        */


		/*while(rs.next()){
		int item_id = rs.getInt("item_id");
		int item_type = rs.getInt("item_type");
				
		out.print("<br>Item_ID: " + item_id);
		out.print("<br>Item_Type: " + item_type);
		}*/


		%>
	</p>
	
	<!-- From JSP repeat the query-->
	<form method = "POST"
     action = "LookThroughDatabase.do">
     Select Konstruktor's Group to inspect<p>
     Group:

        <input type="text" name="elementId" size = 25>

         <br><br>
      <left>
        <input type = "SUBMIT">
      </left>
    </form>

</body>
</html>