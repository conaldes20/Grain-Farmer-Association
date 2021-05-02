var wsUri = getRootUri() + "/FarmerAssociation/gfassociation";
var websocket;
var __connected = false;
var accessToken = null;

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

/*
function init() {
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (evt) {
        writeToScreen("connection to server opened");
    };
    websocket.onmessage = function (evt) {
        handleResponse(evt);
    };
    websocket.onerror = function (evt) {
        onError(evt);
    };
}
*/

function init() {    
    //if ("WebSocket" in window) {              
        //var wsUri = "ws://localhost:8080/takeitback20";
        //var wsUri = "ws://" + document.location.host + document.location.pathname + "takeitback20";
        //if(window.location.protocol === "http:"){
            //localhost
        //    wsUri = new WebSocket("ws://" + window.location.host + document.location.pathname + "takeitback20");
        //    }
        //else if(window.location.protocol === "https:"){
            //Dataplicity
        //    wsUri = new WebSocket("wss://" + window.location.host + document.location.pathname + "takeitback20");
        //}
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
            if(msg === ""){
                //document.getElementById("incomingMsgOutput").value = "No message from server";
                //writeResponse("No message from server");
                msg = 'No message from server';
                displayMessage(msg);
            }else if(msg.includes("close")){
                websocket.close();  
            }else{
                //document.getElementById("incomingMsgOutput").value = msg;
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
        
        $('#contactform').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            //var datastr = $(this).serialize()+ '&flag=CONT';
            var datastr = "";
            var contfname = $("#cont-firstname").val();
            datastr += "contfname=" + contfname + "&";
            var contlname = $("#cont-lastname").val();
            datastr += "contlname=" + contlname + "&";
            var contemailaddr = $("#cont-emailaddress").val();
            datastr += "contemailaddr=" + contemailaddr + "&";
            var contmessage = $("#cont-message").val();
            datastr += "contmessage=" + contmessage + "&flag=CONT";            
            websocket.send(datastr);
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
        
        /*
        $('#loginform').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#signupform').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#donateForm').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#contactForm').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#commentform').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#makeOrderForm').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        
        $('#membprofileform').submit(function() {
            //$('#formdata_container').show();
            //$('#formdata').html($(this).serialize());
            websocket.send($(this).serialize());
            return false;
        });
        */
        
    //} else {
	//document.getElementById("incomingMsgOutput").value = "Websockets not supported";
    //    modal.style.display = "block";
        //document.getElementById("sho_message").innerHTML = "<br/>" + text;
    //    document.getElementById("msg_para").innerHTML = "Websockets not supported";
    //};
}

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

/*
var __connected = false;
var not_cleanly_closed = false;
// Get the modal
var modal = document.getElementById("myModal");

var span = document.getElementsByClassName("close")[0];

span.onclick = function() {
    modal.style.display = "none";
};

window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
};
*/

/*
var trigger_int = setInterval(function() { 
    if (__connected === false) {        
        init();
        clearInterval(trigger_int);
    }
}, 1000);
*/

setInterval(reconnect_ws, 5000);

var reconnect_ws = function(){ 
    if (not_cleanly_closed) {
        init();
        not_cleanly_closed = false;
    }
};

function displayMessage(cltmsg) { 
    modal.style.display = "block";
    //document.getElementById("sho_message").innerHTML = "<br/>" + text;
    document.getElementById("msg_para").innerHTML = cltmsg;
    /*
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = cltmsg;
    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
    */
} 

window.addEventListener("load", init, false);
