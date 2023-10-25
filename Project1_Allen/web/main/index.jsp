<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    
    <head>
        
        <title>Example Web Application #2</title>
        
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        
        <script type="text/javascript" src="main/scripts/jquery-3.7.0.min.js"></script>
        <script type="text/javascript" src="main/scripts/EX2.js"></script>
        
    </head>
    
    <body>
        
        <p id="version"></p>
        
        <form name="searchform" id="searchform" onsubmit="return EX2.submitSearchForm();">
            
            <fieldset>
                <label for="search">Search Parameter (Last Name):</label>
                <input type="text" name="search" id="search">
                <input type="submit" value="Submit">
            </fieldset>
            
        </form>
        
        <p>
            <input type="button" value="Logout" onclick="window.open('<%= request.getContextPath() %>/main/logout.jsp', '_self', false);" />
        </p>
        
        <div id="resultsarea">
            
            <p id="version"></p>
            
        </div>
        
        <script type="text/javascript">

            $(EX2.init);

        </script>
        
    </body>
    
</html>