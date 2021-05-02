var wsUri = getRootUri() + "/FarmerAssociation/gfassociation";
var websocket;
var __connected = false;
var photo_base64str;
var accessToken = null;

var groupnums = [];
var groupdata = [];
var farmernums = [];
var farmerdata = [];
//var tableRowsCols = [];
var selectedvalue = "";

function getRootUri() {
    if (window.location.protocol === 'http:') {
        return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port === "" ? "8080" : document.location.port);
    } else {
        return "wss://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
        (document.location.port === "" ? "8080" : document.location.port);
    }
    //return "ws://" + (document.location.hostname === "" ? "localhost" : document.location.hostname) + ":" +
    //    (document.location.port === "" ? "8080" : document.location.port);
}

function init() {  
	websocket = new WebSocket(wsUri);
        
	websocket.onopen = function(evt) {
            __connected === true;
            //document.getElementById("incomingMsgOutput").value = "Connected to websocket server";
            websocket.send("conn=conn&test=TDATA");            
            //var msg = 'Connected to: ' + evt.currentTarget.URL;
            //displayMessage(msg);
	};

	websocket.onmessage = function(evt) { 
            var msg = evt.data;
            //displayMessage(msg);
            if(msg === ""){
                //document.getElementById("incomingMsgOutput").value = "No message from server";
                //writeResponse("No message from server");
                msg = 'No message from server';                
                displayMessage(msg);
            }else if(msg.includes("close")){
                websocket.close();                
            }else if (msg.indexOf("TPE", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("APN", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("EOS", 0 !== -1)) {
                //getTable3Columns(msg);
                //$.buildTable();
            } else if (msg.indexOf("EPI", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("ACI", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("TIS", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("TLG", 0 !== -1)) {
                generateTable(msg);
            } else if (msg.indexOf("TLR", 0 !== -1)) {
                generateTable(msg);   
            } else {
               displayMessage(msg);
            } 
	};

	websocket.onerror = function(evt) {
            //document.getElementById("incomingMsgOutput").value = 'An error occured, closing application';
            //writeResponse('An error occured, closing application');            
            var msg = 'An error occured. <br/> Reason -> ' + evt.reason;
            displayMessage(msg);
	};

	websocket.onclose = function(evt) {
            __connected = false;
            var evtcodereason;
            if (evt.wasClean) {
                evtcodereason = "[close] Connection closed cleanly: code -> " + evt.code + " reason -> " + evt.reason;
                evtcodereason += ". Refresh current page to re-establish connection.";
                //document.getElementById("incomingMsgOutput").value = evtcodereason;
                //writeResponse(evtcodereason);
                var msg = evtcodereason;
                displayMessage(msg);
                not_cleanly_closed = false;
            } else {
                not_cleanly_closed = true;
                // e.g. server process killed or network down
                // event.code is usually 1006 in this case
                //writeResponse("socket closed: Connection died");
                var msg = "socket closed: Connection died";
                displayMessage(msg);
            }
	};
        
        //closeBtn.onclick = function(e) {
        //    e.preventDefault();
        //    websocket.send("close|WSCL");
        //    return false;
        //};
        /*
        $('#rpt-query').select(function() {
            var rptquerytext = $("#rpt-query option:selected").text();
            var query = rptquerytext.substring(3);
            var tableheader = document.getElementById("table-header");
            tableheader.innerHTML = query;
            return false;
        });
        */

        /*
        $('#reportform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=GROP';
            //indexOf("TPE", 0 !== -1)) {
            var datastr = ""; 
            var rptquery = $("#rpt-query option:selected").text(); 
            if (rptquery.indexOf("Total production", 0 !== -1)) {
                rptquery = "TPE";
            } else if (rptquery.indexOf("Actual Production", 0 !== -1)) {
                rptquery = "APN";        
            } else if (rptquery.indexOf("Economy", 0 !== -1)) {
                rptquery = "EOS";
            } else if (rptquery.indexOf("Expected income", 0 !== -1)) {
                rptquery = "EPI";
            } else if (rptquery.indexOf("Actual income", 0 !== -1)) {
                rptquery = "ACI";
            } else if (rptquery.indexOf("Total Input", 0 !== -1)) {
                rptquery = "TIL";
            } else if (rptquery.indexOf("Total loans", 0 !== -1)) {
                rptquery = "TLG";
            } else if (rptquery.indexOf("Total Loan", 0 !== -1)) {
                rptquery = "TLR";
            } else if (rptquery.indexOf("Choose a query", 0 !== -1)) {
                rptquery = "NAP-QRY";
            }
            datastr += "rptquery=" + rptquery + '&flag=' + rptquery;
            websocket.send(datastr);
           return false;    
        });
        */
       $('#reportform').submit(function() {
            //var datastr = $(this).serialize()+ '&flag=GROP';
            //indexOf("TPE", 0 !== -1)) {
            var datastr = ""; 
            
            /*
            var rptquery = $("#rpt-query option:selected").text(); 
            if (rptquery.indexOf("1. ", 0 !== -1)) {
                rptquery = "TPE";
            } else if (rptquery.indexOf("2. ", 0 !== -1)) {
                rptquery = "APN";        
            } else if (rptquery.indexOf("3. ", 0 !== -1)) {
                rptquery = "EOS";
            } else if (rptquery.indexOf("4. ", 0 !== -1)) {
                rptquery = "EPI";
            } else if (rptquery.indexOf("5.", 0 !== -1)) {
                rptquery = "ACI";
            } else if (rptquery.indexOf("6. ", 0 !== -1)) {
                rptquery = "TIL";
            } else if (rptquery.indexOf("7. ", 0 !== -1)) {
                rptquery = "TLG";
            } else if (rptquery.indexOf("8. ", 0 !== -1)) {
                rptquery = "TLR";
            } else if (rptquery.indexOf("Choose a query", 0 !== -1)) {
                rptquery = "NAP-QRY";
            }
            datastr += "rptquery=" + rptquery + '&flag=' + rptquery;
            */
    
            datastr += "rptquery=" + selectedvalue + '&flag=' + selectedvalue;
            websocket.send(datastr);
            //rptquery = "";              
           return false;    
        });
               
        $('#subscribe-form').submit(function() {
            var datastr = "";
            var emailaddr = $("#emailaddr").val();
            datastr += "emailaddr=" + emailaddr + "&flag=SUBS";
            //var datastr = $(this).serialize() + '&flag=SUBS';
            websocket.send(datastr);
            return false;
        });
        
        $('#facebook-button').on('click', function() {
            FB.login(function (response) {
                if (response.authResponse) {
                    // Get and display the user profile data
                    //getFbUserData();
                    FB.api('/me', {locale: 'en_US', fields: 'id,first_name,last_name,email,link,gender,locale,picture'},
                    function (response) {
                        //document.getElementById('fbLink').setAttribute("onclick","fbLogout()");
                        //document.getElementById('fbLink').innerHTML = 'Logout from Facebook';
                        //document.getElementById('status').innerHTML = '<p>Thanks for logging in, ' + response.first_name + '!</p>';
                        document.getElementById('userData').innerHTML = '<h2>Facebook Profile Details</h2><p><img src="'+response.picture.data.url+'"/></p><p><b>FB ID:</b> '+response.id+'</p><p><b>Name:</b> '+response.first_name+' '+response.last_name+'</p><p><b>Email:</b> '+response.email+'</p><p><b>Gender:</b> '+response.gender+'</p><p><b>FB Profile:</b> <a target="_blank" href="'+response.link+'">click to view profile</a></p>';
                        var msg = '<h2>Facebook Profile Details</h2><p><img src="'+response.picture.data.url+'"/></p><p><b>FB ID:</b> '+response.id+'</p><p><b>Name:</b> '+response.first_name+' '+response.last_name+'</p><p><b>Email:</b> '+response.email+'</p><p><b>Gender:</b> '+response.gender+'</p><p><b>FB Profile:</b> <a target="_blank" href="'+response.link+'">click to view profile</a></p>';
                        displayMessage(msg);
                    });
                } else {
                    //document.getElementById('status').innerHTML = 'User cancelled login or did not fully authorize.';
                    var msg = "User cancelled login or did not fully author";
                    displayMessage(msg);
                }
            }, {scope: 'email'});
            
        });
        
        $('#twitter-button').on('click', function() {
            // Initialize with your OAuth.io app public key
            OAuth.initialize('HwAr2OtSxRgEEnO2-JnYjsuA3tc');
            // Use popup for OAuth
            OAuth.popup('twitter').then(twitter => {
                console.log('twitter:', twitter);
                // Prompts 'welcome' message with User's email on successful login
                // #me() is a convenient method to retrieve user data without requiring you
                // to know which OAuth provider url to call
                twitter.me().then(data => {
                    console.log('data:', data);
                    alert('Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details");
                    //var msg = 'Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details";
                    //displayMessage(msg);
                });
                // Retrieves user data from OAuth provider by using #get() and
                // OAuth provider url    
                twitter.get('/1.1/account/verify_credentials.json?include_email=true').then(data => {
                    console.log('self data:', data);
                    //var msg = 'Twitter says your email is:' + data.email + ".\nView browser 'Console Log' for more details";
                    //displayMessage(msg);
                });    
            });
        });
        
        $('#instagram-button').on('click', function() {
            var instagramClientId = '16edb5c3bc05437594d69178f2aa646a';        
            var instagramRedirectUri = 'localhost/faceboo';
            var popupWidth = 700,
                popupHeight = 500,
                popupLeft = (window.screen.width - popupWidth) / 2,
                popupTop = (window.screen.height - popupHeight) / 2;
            // Url needs to point to instagram_auth.php
            var popup = window.open('instagram.html', '', 'width='+popupWidth+',height='+popupHeight+',left='+popupLeft+',top='+popupTop+'');
            //var popup = window.open('instagram_auth.php', '', 'width='+popupWidth+',height='+popupHeight+',left='+popupLeft+',top='+popupTop+'');
            popup.onload = function() {
                // Open authorize url in pop-up
                if(window.location.hash.length === 0) {
                    popup.open('https://instagram.com/oauth/authorize/?client_id='+instagramClientId+'&redirect_uri='+instagramRedirectUri+'&response_type=token', '_self');
                }
                // An interval runs to get the access token from the pop-up
                var interval = setInterval(function() {
                    try {
                        // Check if hash exists
                        if(popup.location.hash.length) {
                            // Hash found, that includes the access token
                            clearInterval(interval);
                            accessToken = popup.location.hash.slice(14); //slice #access_token= from string
                            popup.close();
                            //if(callback !== undefined && typeof callback === 'function'){
                            //    callback();
                            //}
                            //alert("You are successfully logged in! Access Token: "+accessToken);
                            $.ajax({
                                type: "GET",
                                dataType: "jsonp",
                                url: "https://api.instagram.com/v1/users/self/?access_token="+accessToken,
                                success: function(response){
                                    // Change button and show status
                                    $('.instagramLoginBtn').attr('onclick','instagramLogout()');
                                    $('.btn-text').text('Logout from Instagram');
                                    $('#status').text('Thanks for logging in, ' + response.data.username + '!');

                                    // Display user data
                                    var msg = '<p><b>Instagram ID:</b> '+response.data.id+'</p><p><b>Name:</b> '+response.data.full_name+'</p><p><b>Picture:</b> <img src="'+response.data.profile_picture+'"/></p><p><b>Instagram Profile:</b> <a target="_blank" href="https://www.instagram.com/'+response.data.username+'">click to view profile</a></p>';
                                    //displayUserProfileData(response.data);
                                    displayMessage(msg);
                                    // Save user data
                                    //saveUserData(response.data);
                                }
                            });
                        }
                    }
                    catch(evt) {
                        // Permission denied
                    }
                }, 100);
            };
        });
}

//var tableRowsCols = [];
function changeHeader(selectObj) {
    // get the index of the selected option 
    var idx = selectObj.selectedIndex; 
    var selectedtext = selectObj.options[idx].text; 
    selectedvalue = selectObj.options[idx].value; 
    var query = selectedtext.substring(3);
    var tableheader = document.getElementById("table-header");
    tableheader.innerHTML = query;
}

/*
function getTable3Columns(msgstr){ 
    var tableRowsCols = [];
    
    var data = msgstr.substring(4);
    if (msgstr.indexOf("TPE", 0 !== -1)) {
        data = "Local Govt|State Located|Total Production (ton)#" + data;
    } else if (msgstr.indexOf("APN", 0 !== -1)) {
    } else if (msgstr.indexOf("EOS", 0 !== -1)) {
    } else if (msgstr.indexOf("EPI", 0 !== -1)) {
    } else if (msgstr.indexOf("ACI", 0 !== -1)) {
    } 
    
    var outerArray = null;
    if (data.indexOf("#", 0 !== -1)) {
        outerArray = data.split("#");
        for(var i = 0; i < outerArray.length; i++){
            var innerArray = outerArray[i].split("|");
            tableRowsCols[i+1] = [innerArray[0], innerArray[1], innerArray[2]];  
        }
        displayMessage(tableRowsCols.toString());
    } else {    
        var innerArray = data.split("|");
        tableRowsCols[1] = [innerArray[0], innerArray[1], innerArray[2]];  
        displayMessage(tableRowsCols[0]);
    }
    //displayMessage(tableRowsCols.toString());
    return tableRowsCols;
}

function getTable4Columns(msgstr){
    var tableRowsCols = new Array();
    if (msgstr.indexOf("TIL", 0 !== -1)) {
    } else if (msgstr.indexOf("TLG", 0 !== -1)) {
    } else if (msgstr.indexOf("TLR", 0 !== -1)) {
    } 
    var data = msgstr.substring(4);
    var outerArray = null;
    if (data.indexOf("#", 0 !== -1)) {
        outerArray = data.split("#");
        for(var i = 0; i < outerArray.length; i++){
            var innerArray = outerArray[i].split("|");
            for (var j = 0; j < innerArray.length; j++){
                tableRowsCols[i+1] = [innerArray[j], innerArray[j+1], innerArray[j+2], innerArray[j+3]];                            
            }
        }
    } else {
        var innerArray = outerArray[i].split("|");
        for (var j = 0; j < innerArray.length; j++){
            tableRowsCols[1] = [innerArray[j], innerArray[j+1], innerArray[j+2], innerArray[j+3]];                            
        }
    }
    return tableRowsCols;
}
*/
function deleteRows(){ 
    var table = document.getElementById('rpt-table'); 
    if (table) {
        var rowCount = table.rows.length; 
        if(rowCount > '1'){ 
            var row = table.deleteRow(rowCount-1);
            rowCount--; 
        }
    }
} 

function generateTable(msgstr) {
    //Build an array containing Customer records.
    /*
    var customers = new Array();
    customers.push(["Customer Id", "Name", "Country"]);
    customers.push([1, "John Hammond", "United States"]);
    customers.push([2, "Mudassar Khan", "India"]);
    customers.push([3, "Suzanne Mathews", "France"]);
    customers.push([4, "Robert Schidner", "Russia"]);
    */
    var data = msgstr.substring(4);
   
    var modified_data = "";
    if (msgstr.indexOf("TPE", 0 !== -1)) {
        modified_data = "Local Govt|State Located|Total Production (ton)#" + data;
    } else if (msgstr.indexOf("APN", 0 !== -1)) {
        modified_data = "Local Govt|State Located|Actual Production (ton)#" + data;
    } else if (msgstr.indexOf("EOS", 0 !== -1)) {
        
    } else if (msgstr.indexOf("EPI", 0 !== -1)) {
        modified_data = "Local Govt|State Located|Expected income (Nm#" + data;        
    } else if (msgstr.indexOf("ACI", 0 !== -1)) {   
        modified_data = "Local Govt|State Located|Actual income (Nm#" + data;       
    } else if (msgstr.indexOf("TIL", 0 !== -1)) {
        modified_data = "Input Detail|Local Govt|State Located|Total Input Supplied (Nm#" + data; 
    } else if (msgstr.indexOf("TLG", 0 !== -1)) {
        modified_data = "Loan Detail|Local Govt|State Located|Total Loans Granted (Nm#" + data;
    } else if (msgstr.indexOf("TLR", 0 !== -1)) {
        modified_data = "Loan Detail|Local Govt|State Located|Total LoansRecovered (Nm#" + data;
    }    
   
   /*
    if (selectedvalue.equals("TPE")) {
        data = "Local Govt|State Located|Total Production (ton)#" + data;
    } else if (selectedvalue.equals("APN")) {
        data = "Local Govt|State Located|Actual Production (ton)#" + data;
    } else if (selectedvalue.equals("EOS")) {
        
    } else if (selectedvalue.equals("EPI")) {
        data = "Local Govt|State Located|Expected income (Nm#" + data;        
    } else if (selectedvalue.equals("ACI")) {   
        data = "Local Govt|State Located|Actual income (Nm#" + data;       
    } else if (selectedvalue.equals("TIL")) {
        data = "Input Detail|Local Govt|State Located|Total Input Supplied (Nm#" + data; 
    } else if (selectedvalue.equals("TLG")) {
        data = "Loan Detail|Local Govt|State Located|Total Loans Granted (Nm#" + data;
    } else if (selectedvalue.equals("TLR")) {
        data = "Loan Detail|Local Govt|State Located|Total LoansRecovered (Nm#" + data;
    } 
    */
    
    //var outerArray = modified_data.split("#");  
    
   
    //Create a HTML Table element.
    
    var table = document.createElement("TABLE");    
    table.border = "1";
    table.style="color: green; background: #b5dc9d";
    table.setAttribute('id', 'rpt-table');
    
    var outerArray = modified_data.split("#");  
    //Get the count of columns.  
    var headerelems = outerArray[0].split("|");
    var columnCount = headerelems.length;
    //Add the header row.
    var row = table.insertRow(-1);
    for (var i = 0; i < columnCount; i++) {
        var headerCell = document.createElement("TH");
        headerCell.innerHTML = headerelems[i];
        row.appendChild(headerCell);
    }
 
    var rowCount = outerArray.length;
    //Add the data rows.
    for (var i = 1; i < rowCount; i++) {
        row = table.insertRow(-1);
        var valueelems = outerArray[i].split("|");
        for (var j = 0; j < columnCount; j++) {
            var cell = row.insertCell(-1);
            cell.innerHTML = valueelems[j];
        }
    }
 
    var dvTable = document.getElementById("dvTable");
    dvTable.innerHTML = "";
    dvTable.appendChild(table);
    //selectedvalue = "";
}

/*    
function buildTable(){
    var number_of_rows = tableRowsCols.length;
    var number_of_cols = tableRowsCols[0].length;
    var table_body = '<table border="1">';
    
    for(var i=0;i<number_of_rows;i++){
       table_body+='<tr>';
       for(var j=0;j<number_of_cols;j++){
          table_body +='<td>';
          table_body += tableRowsCols[i][j];
          table_body +='</td>';
       }
       table_body+='</tr>';
    }
    table_body+='</table>' ;
    tableRowsCols = [];
    $('#parentDiv').html(table_body);    
};

function buildTable() {
    //e.preventDefault();
    var parentDiv = $("#parentHolder");
    parentDiv.html("");
    var aTable = $("<table>", {
        "id": "newTable"
    }).appendTo(parentDiv);
    var rowCount = tableRowsCols.length;
    var colmCount = tableRowsCols[0].length;

    // For loop for adding header .i.e th to our table
    for (var k = 0; k < 1; k++) {
        var fragTrow = $("<tr>", {
            "class": "trClass"
        }).appendTo(aTable);
        for (var j = 0; j < colmCount; j++) {
            $("<th>", {
                "class": "thClass"
            }).prependTo(fragTrow).html(tableRowsCols[k][j]);
        }
    }
    
    //For loop for adding data .i.e td with data to our dynamic generated table
    for (var i = 1; i < rowCount; i < i++) {
        var fragTrow = $("<tr>", {
            "class": "trClass"
        }).appendTo(aTable);
        for (var j = 0; j < colmCount; j++) {
            $("<td>", {
                "class": "tdClass"
            }).appendTo(fragTrow).html(tableRowsCols[i][j]);
        }
    }
    tableRowsCols = [];
};
*/

window.fbAsyncInit = function() {
    // FB JavaScript SDK configuration and setup
    FB.init({
      appId      : '1119999988888898981989819891', // FB App ID
      cookie     : true,  // enable cookies to allow the server to access the session
      xfbml      : true,  // parse social plugins on this page
      version    : 'v9.0' // use graph api version 2.8
    });
    
    // Check whether the user already logged in
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            //display user data
            var msg = 'facebook connected';
            displayMessage(msg);
            //getFbUserData();
        }
    });
};

setInterval(reconnect_ws, 5000);

var reconnect_ws = function(){ 
    if (not_cleanly_closed) {
        init();
        not_cleanly_closed = false;
    }
};

function encodeImageFileAsURL() {
    var fi = document.getElementById('inputFileToLoad'); // GET THE FILE INPUT.
    var fsize = 0;
    // VALIDATE OR CHECK IF ANY FILE IS SELECTED.
    if (fi.files.length > 0) {
        fsize = fi.files.item(0).size;
        
        /*
        // RUN A LOOP TO CHECK EACH SELECTED FILE.
        for (var i = 0; i <= fi.files.length - 1; i++) {

            var fsize = fi.files.item(i).size;      // THE SIZE OF THE FILE.
            document.getElementById('fp').innerHTML =
                document.getElementById('fp').innerHTML + '<br /> ' +
                    '<b>' + Math.round((fsize / 1024)) + '</b> KB';
        }
        */
        var fileSize = Math.round((fsize / 1024));
        if (fileSize <= 15){     
            modal.style.display = "none";
            var fileToLoad = fi.files[0];
            var fileReader = new FileReader();

            fileReader.onload = function(fileLoadedEvent) {
                var srcData = fileLoadedEvent.target.result; // <--- data: base64

                var newImage = document.createElement('img');
                newImage.src = srcData;

                document.getElementById("imgTest").innerHTML = newImage.outerHTML;
                photo_base64str = document.getElementById("imgTest").innerHTML;
                //alert("Converted Base64 version is " + document.getElementById("imgTest").innerHTML);
                //console.log("Converted Base64 version is " + document.getElementById("imgTest").innerHTML);
            };
            fileReader.readAsDataURL(fileToLoad);            
        }else{
            var msg = "File size is greater than 15kb.";
            displayMessage(msg);
        }
    }else{
        var msg = "File is invalid or empty.";
        displayMessage(msg);
    }
}

function displayMessage(cltmsg) { 
    modal.style.display = "block";
    //document.getElementById("sho_message").innerHTML = "<br/>" + text;
    document.getElementById("msg_para").innerHTML = cltmsg;
}

window.addEventListener("load", init, false);
