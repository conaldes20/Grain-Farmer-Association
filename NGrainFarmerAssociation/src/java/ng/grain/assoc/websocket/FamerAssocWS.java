/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.websocket;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import ng.grain.assoc.buzlogic.factory.BusinessLogicFactory;
import ng.grain.assoc.db.DBConnection;
import ng.grain.assoc.intface.BusinessLogic;

/**
 *
 * @author CONALDES
 */
@ServerEndpoint("/gfassociation")
public class FamerAssocWS {
    private String clientMessage = "";
    private String oldClientMessage = "";
    //private boolean closeConnection = false;
    protected Session newsession = null;
    private boolean processorThreadRunning = false;
    //private ProcessClientMsg processClientMsg = null;
    private RequestProcessor requestProcessor = null;
    
    private ConcurrentLinkedQueue<Object[]> clientMSGqueue = new ConcurrentLinkedQueue<>();
    private static BusinessLogic BUZLOGIC_INTERFACE = null;       //BusinessLogicFactory.create(); 
    
    private String current_username;
    private String current_userrole;
    private String current_phonenum;
    private Map<String, Object> userproperties = null;
    private static boolean server_running = false;
    private static String mysql_connection_status = "";
    
    static{
        Connection connection = DBConnection.getMSQLConnection();
        System.out.println("(FamerAssocWS connection != null): " + (connection != null));
        if ((connection != null)) {
            BUZLOGIC_INTERFACE  = BusinessLogicFactory.create(connection);
            server_running = true;            
        }       
    }
    
    @OnOpen
    public void onOpen(final Session session) { 
        
        newsession = session;
        session.setMaxTextMessageBufferSize(20000);
        System.out.println("onOpen session.getMaxTextMessageBufferSize(): " + session.getMaxTextMessageBufferSize());
        if(!processorThreadRunning){
            processorThreadRunning = true;
            
            try{
                regRequestProcessor();
            } catch (IOException ex) {
                System.out.println("onOpen IOException: " + ex.getMessage());
            } 
        }  
        /*
        try {
            session.getBasicRemote().sendText("Server endpoint gets connected!");        //SessionId:" + session.getId());
            System.out.println("onOpen newsession.getId(): " + newsession.getId());
        } catch (IOException ex) {
            System.out.println("onOpen IOException: " + ex.getMessage());
        }
        */
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        try {
            session.close();
        } catch (IOException ex){
            System.out.println("onError IOException: " + ex.getMessage());
        }
    }

    @OnMessage
    public void onMessage(final String message, final Session session) { 
        System.out.println("onMessage message: " + message);
        
        clientMessage = message;
        if(message.contains("close")){
            if (session.isOpen()){
                try {
                    session.getBasicRemote().sendText("close connection");
                } catch (IOException ex) {
                    System.out.println("onMessage IOException: " + ex.getMessage());
                }
            }
        }else{
            if (!server_running) {
               try {
                    session.getBasicRemote().sendText("<p><b> " + mysql_connection_status + " </p>");
                } catch (IOException ex) {
                    System.out.println("onMessage IOException: " + ex.getMessage());
                } 
            } else if(!clientMessage.equals(oldClientMessage)){
                oldClientMessage = clientMessage;
                receiveText(newsession, clientMessage);                
            } else {
                try {
                    session.getBasicRemote().sendText("<p><b> Duplicate message: " + clientMessage + "</p>");
                } catch (IOException ex) {
                    System.out.println("onMessage IOException: " + ex.getMessage());
                }
            }
        } 
    }

    @OnClose
    public void onClose(final Session session) {
        System.err.println("Close connection");        
        int numberOfSessionsInQueue = CachedSizeObject.getObjectSize();
        System.out.println("onClose numberOfSessionsInQueue: " + numberOfSessionsInQueue);
        if(numberOfSessionsInQueue == 0){
            shutdownRequestProcessor();
        }         
    } 
    
    private void receiveText(Session session, String message) {
        if(!message.isEmpty()){       
            System.out.println("receiveText/message: " + message);            
            String messageFromClient = message;   
            Object[] elemobjs = new Object[2];
            elemobjs[0] = (Object) messageFromClient;
            elemobjs[1] = (Object) session;
            clientMSGqueue.add(elemobjs);                       
            int queueSize = clientMSGqueue.size();        
            CachedSizeObject.cacheObjSize(queueSize);               
        }
    }     
      
    private class RequestProcessor implements Runnable {

	private boolean doStop = false;
        
        public synchronized void doStop() {
            this.doStop = true;
        }
        
        private synchronized boolean keepRunning() {
            return this.doStop == false;
        } 
        
        @Override
        public void run() { 
            while(keepRunning()){
                if (!clientMSGqueue.isEmpty()) {    
                    System.out.println("RequestProcessor/run() (!clientMSGqueue.isEmpty()): " + (!clientMSGqueue.isEmpty()));
                    Object[] elemobjs  = clientMSGqueue.poll();
                    String clientMessage = (String) elemobjs[0];
                    Session clientSession = (Session) elemobjs[1];
                    //String[] arraystr = stringToArray(clientMessage); 
                    userproperties = clientSession.getUserProperties(); 
                    System.out.println("RequestProcessor/To send client message to database");
                    String result = processRequest(clientMessage);
                    System.out.println("RequestProcessor/Information from database: " + result);
                    try {
                        clientSession.getBasicRemote().sendText(result);
                        System.out.println("RequestProcessor/run(): Message sent to remote client");   
                    } catch (IOException | NullPointerException ex) {
                        System.out.println("RequestProcessor/run() (IOException | NullPointerException): " + ex.getMessage());
                    }               
                    
                    int queueSize = clientMSGqueue.size();
                    CachedSizeObject.cacheObjSize(queueSize); 
                } 
            } 
            System.out.println("RequestProcessor: Shutting down thread");
	}        
    } 
    
    private void regRequestProcessor() throws IOException {  
        requestProcessor = new RequestProcessor();
        Thread thread = new Thread(requestProcessor);
        thread.start(); 
    }
    
    private void shutdownRequestProcessor(){ 
        System.out.println("shutdownRequestProcessor");
        requestProcessor.doStop();
        processorThreadRunning = false; 
    }  
       
    private String processRequest(String datastr){ 
        String htmlMessage = "";
        String[] params = null;
        System.out.println("datastr: " + datastr);
        String validationstr = formInputsValidation(datastr);
        System.out.println("validationstr: " + validationstr);
        if(!validationstr.isEmpty()){
            htmlMessage = "<p><b>" + validationstr + "</p>";
        }else if (datastr.contains("SUBS")){ 
            //var datastr = emailaddr+"|SUBS";
            System.out.println("datastr pt 1: " + datastr);
            
            params = jqueryStringToArray(datastr);
            
            System.out.println("params[0] pt 1: " + params[0]);
            System.out.println("params[1] pt 1: " + params[1]);
            boolean validEmail = false;
            if (UtilFuncs.isEmailValid(params[0])) {
                validEmail = true;
            }
            if (validEmail){
                String genidstr = BUZLOGIC_INTERFACE.addSubscriber(params[0], "no");
                Integer generatedid = null;     
                try{
                    generatedid = Integer.valueOf(genidstr);
                    System.out.println("generatedid: " + generatedid);
                    if (generatedid >= 1){
                        htmlMessage = "<p><b>" + "Subscription noted!" + "</p>";
                        System.out.println("htmlMessage: " + htmlMessage);
                    }else{
                        htmlMessage = "<p><b>" + "Subscription failed!" + "</p>";
                    } 
                } catch (NumberFormatException nfe) {
                    System.out.println("htmlMessage: " + genidstr);
                    htmlMessage = "<p><b>" + genidstr + "</p>";
                }
            } else{
                htmlMessage = "<p><b> Email address (" +  params[0] + " is inavlid </p>";
            }
        } else if (datastr.contains("CONT")){
            //var datastr = dona_name+"|"+dona_email+"|"+dona_state+"|"+dona_country+"|"+dona_cardName+
            //    "|"+dona_cardNumber+"|"+dona_amount+"|"+dona_cardCvv+"|DONA";                       
            params = jqueryStringToArray(datastr);
            for(int i = 0; i < params.length; i++){
                System.out.println("params["+i+"]: " + params[i]);
            }
            String param0 = UtilFuncs.capitalize(params[0]);
            String param1 = UtilFuncs.capitalize(params[1]);
            boolean validEmail = false;
            if (UtilFuncs.isEmailValid(params[2])) {
                validEmail = true;
            }
            if (validEmail){
                String genidstr = BUZLOGIC_INTERFACE.addContact(param0, param1, params[2], params[3]);
                Integer generatedid = null;     
                try{
                    generatedid = Integer.valueOf(genidstr);
                    if (generatedid >= 1){
                        htmlMessage = "<p><b>" + "We shall get back to you.!" + "</p>";
                    }else{
                        htmlMessage = "<p><b>" + "Error occured. Resend contact!" + "</p>";
                    } 
                } catch (NumberFormatException nfe) {
                    System.out.println("htmlMessage: " + genidstr);
                    htmlMessage = "<p><b>" + genidstr + "</p>";
                }
            } else{
                htmlMessage = "<p><b> Email address (" +  params[2] + " is inavlid </p>";
            }
        } else if (datastr.contains("LGIN")){
            params = jqueryStringToArray(datastr);
            for(int i = 0; i < params.length; i ++){
                System.out.println("params[" + i + "]:" + params[i]);
            }
            String credentials = BUZLOGIC_INTERFACE.getCurrentUser(params[0], params[1]);
            int colpt = credentials.indexOf(":", 0);
            String username = "";       //credentials.substring(0, colpt - 1);
            if (!credentials.isEmpty()){
                username = credentials.substring(0, colpt - 1);
                String userrole = credentials.substring(colpt + 1);
                current_username = username;
                current_userrole = userrole;
                current_phonenum = params[0];
                //httpSession.setAttribute("member-id", Integer.toString(uniqid));
                userproperties.put("current_username", current_username);
                userproperties.put("current_userrole", current_userrole);
                userproperties.put("current_phonenum", current_phonenum);  
                saveTrails("logged in");
                htmlMessage = username + " logged in";       //<p><b>" + "You are logged in!" + "</p>";
            }else{
                htmlMessage = "<p><b>" + username + " with " + params[0] + " not authorised!</b></p>";
            }               
        } else if (datastr.contains("GROP")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                //Integer uaccId = Integer.valueOf(params[0]);
                // datastr += "grpname=" + grpname + "&grpaddress=" + grpaddress + "&grpward=" + grpward + "&";
                // datastr += "grplgvtarea=" + grplgvtarea + "&grpstate=" + grpstate + "&";
                // datastr += "selectedcrops=" + selectedcrops + '&flag=GROP';
                String param0 = UtilFuncs.capitaliseFirstLetter(params[0]);
                String param1 = UtilFuncs.capitaliseFirstLetter(params[1]);
                String param2 = UtilFuncs.capitalize(params[2]);
                String param3 = UtilFuncs.capitalize(params[3]);
                String param4 = UtilFuncs.capitalize(params[4]);
                String param5 = UtilFuncs.capitaliseFirstLetter(params[5]);
                String genidstr = BUZLOGIC_INTERFACE.addFmgroup(param0, param1, param2, param3, param4, param5);
                Integer gen_groupid = null;     
                try{
                    gen_groupid = Integer.valueOf(genidstr);
                    if (gen_groupid >= 101){
                        htmlMessage = "<p><b>" + "Group record saved" + "</b></p>";
                        saveTrails("saved group record");
                    }else{
                        htmlMessage = "<p><b>" + "Group record not saved" + "</p>";
                        System.out.println("htmlMessage: " + genidstr);
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("htmlMessage: " + genidstr);
                    htmlMessage = "<p><b>" + genidstr + "</p>";
                }
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("OFFI")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                //Integer uaccId = Integer.valueOf(params[0]);
                String middlename = noNull(params[1]);
                if (middlename.isEmpty()) {
                    middlename = "  ";
                }
                
                // datastr += "firstname=" + firstname + "&middlename=" + middlename + "&lastname=" + lastname + "&";
                // datastr += "office=" + office + "&phonenum=" + phonenum + "&bvn=" + bvn + "&nim=" + nim + "&";
                // datastr += "homeaddress=" + homeaddress + "&groupid=" + groupid +  '&flag=OFFI';
                Integer groupId = Integer.valueOf(params[8]);
                String param0 = UtilFuncs.capitalize(params[0]);                
                if (!middlename.equals("  ")) {
                    middlename = UtilFuncs.capitalize(params[1]);
                }                
                String param2 = UtilFuncs.capitalize(params[2]);
                String param3 = UtilFuncs.capitaliseFirstLetter(params[3]);                
                boolean isValidGSM = UtilFuncs.isValidGSMNumber(params[4]);
                boolean isValidBVN = UtilFuncs.isBVNValid(params[5]);
                boolean isValidNIM = UtilFuncs.isNIMValid(params[6]);
                String param7 = UtilFuncs.capitaliseFirstLetter(params[7]);
                if (isValidGSM && isValidBVN && isValidNIM) {
                    String genidstr = BUZLOGIC_INTERFACE.addOfficer(param0, middlename, param2, param3, params[4], params[6], params[6], param7, groupId);
                    Integer gen_officerid = null;       
                    try{
                        gen_officerid = Integer.valueOf(genidstr);
                        if (gen_officerid >= 100001){
                            //if (gen_groupid >= 101){
                            //    BUZLOGIC_INTERFACE.addOfficerFmgroup(gen_groupid, gen_officerid);
                            //}
                            saveTrails("saved officer record");
                            htmlMessage = "<p><b>" + "Officer record saved" + "</p>";
                        }else{
                            htmlMessage = "<p><b>" + "Officer record not saved" + "</p>";
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("htmlMessage: " + genidstr);
                        htmlMessage = "<p><b>" + genidstr + "</p>";
                    }
                } else {
                    htmlMessage = "<p><b>" + "Invalid phone number, BVN or NIM!" + "</p>";
                }                
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("AGGR")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);  
                String[] detail_arr = params[6].split(", ");  
                int clmnpt = detail_arr[2].indexOf(":", 0);
                String state = detail_arr[2].substring(clmnpt + 1);
                state = state.trim();
                params[3] = params[3].trim();
                System.out.println("state, params[2], params[3]: " + state + ", " + params[2] + ", " + params[3]);
                if (!state.equals(params[3]) && (params[2].contains("Local Government") || params[3].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government is selected,\nif not then, farm must be in " + state + " state";
                } else if (state.equals(params[3]) && (params[2].contains("Local Government") || params[3].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government selected";
                } else if (!state.equals(params[3]) && (!(params[2].contains("Local Government") || params[3].contains("State")))) {
                    htmlMessage = "Farm must be in " + state + " state";
                } else {                
                    Integer groupId = Integer.valueOf(params[4]);
                    // datastr += "aggrtown=" + aggrtown + "&aggrward=" + aggrward + "&aggrlocalgvt=" + aggrlocalgvt + "&";
                    // datastr += "aggrstate=" + aggrstate + "&groupid=" + groupid + "&nextlevel=" + nextlevel + '&flag=AGGR';

                    String param0 = UtilFuncs.capitalize(params[0]);                
                    String param1 = UtilFuncs.capitalize(params[1]);              
                    String param2 = UtilFuncs.capitalize(params[2]);
                    String param3 = UtilFuncs.capitalize(params[3]);
                    String param5 = UtilFuncs.capitaliseFirstLetter(params[5]); 
                    String genidstr = BUZLOGIC_INTERFACE.addAggregation(param0, param1, param2, param3, groupId, param5);
                    Integer gen_aggregationid = null;       
                    try{
                        gen_aggregationid = Integer.valueOf(genidstr);
                        if (gen_aggregationid >= 10003){
                            htmlMessage = "<p><b>" + "Aggregation record saved" + "</p>";
                            saveTrails("saved aggregation record");
                        }else{
                            htmlMessage = "<p><b>" + "Aggregation record not saved" + "</p>";                        
                        }
                    } catch (NumberFormatException nfe) {
                       System.out.println("htmlMessage: " + genidstr);
                    }  
                }
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("FAMR")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                //Integer uaccId = Integer.valueOf(params[0]);
                String[] detail_arr = params[11].split(", ");    
                int clmnpt = detail_arr[2].indexOf(":", 0);
                String state = detail_arr[2].substring(clmnpt + 1);
                state = state.trim();
                params[3] = params[3].trim();
                System.out.println("state, params[4], params[3]: " + state + ", " + params[4] + ", " + params[3]);
                if (!state.equals(params[3]) && (params[4].contains("Local Government") || params[3].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government is selected,\nif not then, farm must be in " + state + " state";
                } else if (state.equals(params[3]) && (params[4].contains("Local Government") || params[3].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government selected";
                } else if (!state.equals(params[3]) && (!(params[4].contains("Local Government") || params[3].contains("State")))) {
                    htmlMessage = "Farm must be in " + state + " state";
                } else { 
                    String middlename = noNull(params[1]);
                    if (middlename.isEmpty()) {
                        middlename = "  ";
                    }
                    Integer groupId = Integer.valueOf(params[10]);
                    // datastr += "firstname=" + firstname + "&middlenamee=" + middlename + "&lastname=" + lastname + "&";
                    // datastr += "state=" + state + "&localgvt=" + localgvt + "&ward=" + ward + "&homeaddress=" + homeaddress + "&";
                    // datastr += "phonenum=" + phonenum + "&bvn=" + bvn + "&nim=" + nim + "&groupid=" + groupid + '&flag=FAMR';

                    String param0 = UtilFuncs.capitalize(params[0]);                
                    if (!middlename.equals("  ")) {
                        middlename = UtilFuncs.capitalize(params[1]);
                    }                
                    String param2 = UtilFuncs.capitalize(params[2]);
                    String param3 = UtilFuncs.capitalize(params[3]);
                    String param4 = UtilFuncs.capitalize(params[4]);
                    String param5 = UtilFuncs.capitalize(params[5]);
                    String param6 = UtilFuncs.capitaliseFirstLetter(params[6]);                
                    boolean isValidGSM = UtilFuncs.isValidGSMNumber(params[7]);
                    boolean isValidBVN = UtilFuncs.isBVNValid(params[8]);
                    boolean isValidNIM = UtilFuncs.isNIMValid(params[9]);
                    if (isValidGSM && isValidBVN && isValidNIM) {
                        String genidstr = BUZLOGIC_INTERFACE.addFarmer(param0, middlename, param2, param3, param4, 
                                param5, param6, params[7], params[8], params[9], groupId);
                        Integer gen_farmerid = null;        
                        try{
                            gen_farmerid = Integer.valueOf(genidstr);
                            if (gen_farmerid >= 10000001){
                                //if (gen_groupid >= 101){
                                //    BUZLOGIC_INTERFACE.addFarmerFmgroup(gen_groupid, gen_farmerid);
                                //}
                                saveTrails("saved farmer record");
                                htmlMessage = "<p><b>" + "Farmer record saved" + "</p>";
                            }else{
                                htmlMessage = "<p><b>" + "Farmer record not saved" + "</p>";
                                System.out.println("htmlMessage: " + genidstr);
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println("htmlMessage: " + genidstr);
                            htmlMessage = "<p><b>" + genidstr + "</p>";
                        }
                    } else {
                        htmlMessage = "<p><b>" + "Invalid phone number, BVN or NIM!" + "</p>";
                    }
                }
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("FARM")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                
                // datastr += "longi1=" + longi1 + "&latit1=" + latit1 + "&longi2=" + longi2 + "&latit2=" + latit2 + "&";
                // datastr += "longi3=" + longi3 + "&latit13" + latit3 + "&longi4=" + longi4 + "&latit4=" + latit4 + "&";
                // datastr += "farmarea=" + farmarea + "&community=" + community + "&ward=" + ward + "&localgvt=" + localgvt + "&state=" + state + "&";
                // datastr += "cropplanted=" + cropplanted + "&areaplanted=" + areaplanted + "&cropyear=" + cropyear + "&cropharvested=" + cropharvested + "&";
                // datastr += "cropnetworth=" + cropnetworth + "&cropincome=" + cropincome + "&farmerid=" + farmerid + '&flag=FARM';
                
                params = jqueryStringToArray(datastr);
                System.out.println("params.length: " + params.length);
                for(int i = 0; i < params.length; i++) {
                    System.out.println("params[" + i +"]: " + params[i]);
                }
                String[] detail_arr = params[20].split(", ");     
                int clmnpt = detail_arr[2].indexOf(":", 0);
                String state = detail_arr[2].substring(clmnpt + 1);
                state = state.trim();
                params[12] = params[12].trim();
                System.out.println("state, params[11], params[12]: " + state + ", " + params[11] + ", " + params[12]);
                if (!state.equals(params[12]) && (params[11].contains("Local Government") || params[12].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government is selected,\nif not then, farm must be in " + state + " state";
                } else if (state.equals(params[12]) && (params[11].contains("Local Government") || params[12].contains("State"))) {
                    htmlMessage = "Invalid State or Local Government selected";
                } else if (!state.equals(params[12]) && (!(params[11].contains("Local Government") || params[12].contains("State")))) {
                    htmlMessage = "Farm must be in " + state + " state";
                } else {
                    BigDecimal param8 = BigDecimal.valueOf(Double.valueOf(params[8]));               
                    BigDecimal param14 = BigDecimal.valueOf(Double.valueOf(params[14]));
                    
                    BigDecimal param0 = BigDecimal.valueOf(Double.valueOf(params[0]));
                    BigDecimal param1 = BigDecimal.valueOf(Double.valueOf(params[1]));
                    BigDecimal param2 = BigDecimal.valueOf(Double.valueOf(params[2]));
                    BigDecimal param3 = BigDecimal.valueOf(Double.valueOf(params[3]));
                    BigDecimal param4 = BigDecimal.valueOf(Double.valueOf(params[4]));
                    BigDecimal param5 = BigDecimal.valueOf(Double.valueOf(params[5]));
                    BigDecimal param6 = BigDecimal.valueOf(Double.valueOf(params[6]));
                    BigDecimal param7 = BigDecimal.valueOf(Double.valueOf(params[7]));
                    boolean log_lat_withinRange = true;
                    if (((param0.doubleValue() < 3.00 ) || (param0.doubleValue() > 15.00)) || 
                            ((param2.doubleValue() < 3.00 ) || (param2.doubleValue() > 15.00)) ||
                            ((param4.doubleValue() < 3.00 ) || (param4.doubleValue() > 15.00)) ||
                            ((param6.doubleValue() < 3.00 ) || (param6.doubleValue() > 15.00)) ||
                            
                            ((param1.doubleValue() < 4.00 ) || (param1.doubleValue() > 14.00)) ||
                            ((param3.doubleValue() < 4.00 ) || (param3.doubleValue() > 14.00)) ||
                            ((param5.doubleValue() < 4.00 ) || (param5.doubleValue() > 14.00)) ||
                            ((param7.doubleValue() < 4.00 ) || (param7.doubleValue() > 14.00))) {
                        log_lat_withinRange = false;                        
                    }
                    boolean farmSizeValid = true;
                    if (param14.doubleValue() >  param8.doubleValue()) {
                        farmSizeValid = false;
                    }
                    int cont_valid_lglts = 0;
                    for(int i = 0; i < 8; i++) {
                        if (UtilFuncs.isNumeric(params[i])) {
                            cont_valid_lglts++;                        
                        }
                    }
                    int cont_other_valid_values = 0;
                    if (UtilFuncs.isNumeric(params[8])) {
                        cont_other_valid_values++;                        
                    }                
                    if (UtilFuncs.isNumeric(params[14])) {
                        cont_other_valid_values++;                        
                    }
                    if (UtilFuncs.isNumeric(params[15])) {
                        cont_other_valid_values++;                        
                    }                
                    if (UtilFuncs.isNumeric(params[16])) {
                        cont_other_valid_values++;                        
                    }
                    if (UtilFuncs.isNumeric(params[17])) {
                        cont_other_valid_values++;                        
                    }
                    if (UtilFuncs.isNumeric(params[18])) {
                        cont_other_valid_values++;                        
                    }
                    if ((cont_valid_lglts == 8) && (cont_other_valid_values == 6) && !log_lat_withinRange && farmSizeValid) {
                                                
                        String param9 = UtilFuncs.capitaliseFirstLetter(params[9]); 
                        String param10 = UtilFuncs.capitalize(params[10]);
                        String param11 = UtilFuncs.capitalize(params[11]);
                        String param12 = UtilFuncs.capitalize(params[12]);
                        String param13 = UtilFuncs.capitalize(params[13]); 
                        
                        BigDecimal param16 = BigDecimal.valueOf(Double.valueOf(params[16]));
                        BigDecimal param17 = BigDecimal.valueOf(Double.valueOf(params[17]));
                        BigDecimal param18 = BigDecimal.valueOf(Double.valueOf(params[18]));

                        Integer farmerId = Integer.valueOf(params[19]);
                        // addFarm(String coordinates, BigDecimal area, String community, String ward, String localGovernment, String stateLocated, 
                        //String crop, BigDecimal areaPlanted, String yearPlanted, BigDecimal harvest, BigDecimal netWorth, BigDecimal income, Integer farmerid);

                        String genidstr = BUZLOGIC_INTERFACE.addFarm(param0, param1, param2, param3, param4, param5, 
                                param6, param7, param8, param9, param10, param11, param12, param13, 
                                param14, params[15], param16, param17, param18, farmerId);

                        Integer gen_farmid = null;      
                        try{
                            gen_farmid = Integer.valueOf(genidstr);
                            if (gen_farmid >= 1000001){
                                // if (gen_farmerid >= 10000001){
                                //     BUZLOGIC_INTERFACE.addFarmFarmer(gen_farmid, gen_farmerid);
                                //}
                                saveTrails("saved farm record");
                                htmlMessage = "<p><b>" + "Farm record saved" + "</p>";
                            }else{
                                htmlMessage = "<p><b>" + "Farm record not saved" + "</p>";
                                System.out.println("htmlMessage: " + genidstr);
                            }
                        } catch (NumberFormatException nfe) {
                            System.out.println("htmlMessage: " + genidstr);
                            htmlMessage = "<p><b>" + genidstr + "</p>";
                        }
                    } else if (cont_valid_lglts < 8 ||  cont_other_valid_values == 6) {
                        htmlMessage = "<p><b>" + "Some coordinate values are invalid!" + "</p>";
                    } else if (cont_valid_lglts == 8 ||  cont_other_valid_values < 6) {
                        htmlMessage = "<p><b>" + "Some values other than coordinates are invalid!" + "</p>";
                    } else if (cont_valid_lglts < 8 ||  cont_other_valid_values < 6) {
                        htmlMessage = "<p><b>" + "Some numeric values  are invalid!" + "</p>";
                    } else if (!log_lat_withinRange) {
                        htmlMessage = "<p><b>" + "Some longitudes or latitudes are not witin range.\n" +
                                "Longitudes: 5deg E and 15deg E; Latitudes: 4deg N and 14deg N" + "</p>";
                    }    
                }
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
            
        } else if (datastr.contains("FINP")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                
                // datastr += "finpname=" + finpname + "&finpcost=" + finpcost + "&finpyear=" + finpyear + "&";
                // datastr += "farmerid=" + farmerid + '&flag=FINP';
                String param0 = UtilFuncs.capitaliseFirstLetter(params[0]); 
                if (UtilFuncs.isNumeric(params[1]) && UtilFuncs.isNumeric(params[2])) {
                    Integer farmerId = Integer.valueOf(params[3]);
                    BigDecimal param1 = BigDecimal.valueOf(Double.valueOf(params[1]));
                    String genidstr = BUZLOGIC_INTERFACE.addFarmInput(param0, param1, params[2], farmerId);
                    Integer gen_farminputid = null;     
                    try{
                        gen_farminputid = Integer.valueOf(genidstr);
                        if (gen_farminputid >= 1001){
                            //if (gen_farmerid >= 10000001){
                            //    BUZLOGIC_INTERFACE.addFarmInputFarmer(gen_farminputid, gen_farmerid);
                            //}
                            saveTrails("saved farm input record");
                            htmlMessage = "<p><b>" + "Farm Input record saved" + "</p>";
                        }else{
                            htmlMessage = "<p><b>" + "Farm Input record not saved" + "</p>";
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("htmlMessage: " + genidstr);
                        htmlMessage = "<p><b>" + genidstr + "</p>";
                    }
                } else {
                    htmlMessage = "<p><b>" + "Invalid numeric values!" + "</p>";
                }                
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("FLON")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                //Integer uaccId = Integer.valueOf(params[0]);
                // datastr += "flndesc=" + flndesc + "&flnloan=" + flnloan + "&flnpayment=" + flnpayment + "&";
                // datastr += "farmerid=" + farmerid + '&flag=FLON';
                String param0 = UtilFuncs.capitaliseFirstLetter(params[0]); 
                if (UtilFuncs.isNumeric(params[1]) && UtilFuncs.isNumeric(params[2])) {
                    BigDecimal param1 = BigDecimal.valueOf(Double.valueOf(params[1]));
                    BigDecimal param2 = BigDecimal.valueOf(Double.valueOf(params[2]));
                    Integer farmerId = Integer.valueOf(params[3]);
                    String genidstr = BUZLOGIC_INTERFACE.addLoan(param0, param1, param2, farmerId);
                    Integer gen_loanid = null;      
                    try{
                        gen_loanid = Integer.valueOf(genidstr);
                        if (gen_loanid >= 1000002){
                            //if (gen_farmerid >= 10000001){
                            //    BUZLOGIC_INTERFACE.addLoanFarmer(gen_loanid, gen_farmerid);
                            //}
                            saveTrails("saved loan record");
                            htmlMessage = "<p><b>" + "Loan record saved" + "</p>";
                        }else{
                            htmlMessage = "<p><b>" + "Loan record not saved" + "</p>";
                            System.out.println("htmlMessage: " + genidstr);
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("htmlMessage: " + genidstr);
                        htmlMessage = "<p><b>" + genidstr + "</p>";
                    }
                } else {
                    htmlMessage = "<p><b>" + "Invalid numeric values!" + "</p>";
                }
                
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("FLNREP")){
            //var datastr = authenticity_token+"|"+comment_content+"|"+pending_flag+"|COMM";
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                //Integer uaccId = Integer.valueOf(params[0]);
                //datastr += "flreploan=" + flreploan + "&flrepamountpaid=" + flrepamountpaid + "&";
               // datastr += "flrepnewpayment=" + flrepnewpayment + "&loanid=" + loanid + '&flag=FLNREP';
                //String param0 = UtilFuncs.capitaliseFirstLetter(params[0]); 
                String param0 = removeComaFromNumber(params[0]);
                String param1 = removeComaFromNumber(params[1]);
                String param2 = removeComaFromNumber(params[2]);
                double value0 = Double.valueOf(param0);
                double value1 = Double.valueOf(param1);
                double value2 = Double.valueOf(param2);
                double current_total = value1 + value2;
                boolean paymentValidity = true;
                if (current_total > value0) {
                    paymentValidity = false;
                }
                if (UtilFuncs.isNumeric(param0) && UtilFuncs.isNumeric(param1) && UtilFuncs.isNumeric(param2) && paymentValidity) {
                    //BigDecimal param1 = BigDecimal.valueOf(Double.valueOf(params[1]));
                    //BigDecimal param2 = BigDecimal.valueOf(Double.valueOf(params[2]));
                    //BigDecimal param3 = BigDecimal.valueOf(Double.valueOf(params[3]));
                    BigDecimal currentSum = new BigDecimal(current_total);
                    Integer loanId = Integer.valueOf(params[3]);
                    boolean updated = BUZLOGIC_INTERFACE.updateLoan(currentSum, loanId);
                    if (updated){                            
                        htmlMessage = "<p><b>" + "Loan payment updated!" + "</p>";
                    }else{
                        htmlMessage = "<p><b>" + "Loan payment failed. Re-try." + "</p>";
                    }
                } else if (!paymentValidity) {
                    double overpayment = current_total - value0;
                    htmlMessage = "<p><b>" + "Attempt to make over-payment by N" + Double.toString(overpayment) + "</p>";
                } else if (!UtilFuncs.isNumeric(param0) || !UtilFuncs.isNumeric(param1) || !UtilFuncs.isNumeric(param2)) {
                    htmlMessage = "<p><b>" + "Invalid numeric values!" + "</p>";
                }                
            }else{
                htmlMessage = "<p><b>" + "You are not given access to the database!" + "</p>";
            }
        } else if (datastr.contains("GRPN")){            
            String groupdetails = BUZLOGIC_INTERFACE.groupNumList();
            int textln = groupdetails.length();
            htmlMessage = groupdetails.substring(0, textln - 1);
        } else if (datastr.contains("TPE")){
            //params = jqueryStringToArray(datastr);
            String details = BUZLOGIC_INTERFACE.getTotalProduction();  
            int textln = details.length();
            System.out.println("details from websocket: " + details);
            System.out.println("textln from websocket: " + textln);
            htmlMessage = details.substring(0, textln - 1);
            textln = htmlMessage.length();
            System.out.println("htmlMessage from websocket: " + htmlMessage);
            System.out.println("textln from websocket: " + textln);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "TPE]0000|none|none";
            }
        } else if (datastr.contains("APN")){
            String details = BUZLOGIC_INTERFACE.getActualProduction();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "APN]0000|none|none";
            }
        } else if (datastr.contains("EPI")){
            String details = BUZLOGIC_INTERFACE.getExpectedIncome();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "EPI]0000|none|none";
            }
        } else if (datastr.contains("ACI")){
            String details = BUZLOGIC_INTERFACE.getActualIncome();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "ACI]0000|none|none";
            }
        } else if (datastr.contains("TIS")){
            String details = BUZLOGIC_INTERFACE.getTotalInputSupplied();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "TIS]0000|none|none|none"; 
            }
        } else if (datastr.contains("TLG")){
            String details = BUZLOGIC_INTERFACE.getTatalLoanGranted();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "TLG]0000|none|none|none";     
            }        
        } else if (datastr.contains("TLR")){
            String details = BUZLOGIC_INTERFACE.getTatalLoanRecovered();
            //int rbkt = details.indexOf("]");
            int textln = details.length();
            htmlMessage = details.substring(0, textln - 1);
            htmlMessage = noNull(htmlMessage);
            if (htmlMessage.isEmpty()){
                htmlMessage = "TLR]0000|none|none|none"; 
            }
        } else if (datastr.contains("NAP-QRY")){
            htmlMessage = "<p><b>" + "No query selected!" + "</p>";
        } else if (datastr.contains("OFFGNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.officerNumList(param0); 
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FMNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.famerNumList(param0); 
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("AGGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.aggregationNumList(param0); 
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FAMGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                //String details = BUZLOGIC_INTERFACE.farmGPNumList(param0);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, "FAMGPNU");
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }    
        } else if (datastr.contains("FINPGNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                //String details = BUZLOGIC_INTERFACE.farmInpGPNumList(param0);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, "FINPGNU");
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";     
            }
        } else if (datastr.contains("FLNGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                //String details = BUZLOGIC_INTERFACE.loanGPNumList(param0);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, "FLNGPNU");
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            } 
        } else if (datastr.contains("LRPGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                //String details = BUZLOGIC_INTERFACE.loanGPNumList(param0);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, "LRPGPNU");
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            } 
        } else if (datastr.contains("LRPGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                //String details = BUZLOGIC_INTERFACE.loanGPNumList(param0);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, "LRPGPNU");
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            } 
        } else if (datastr.contains("FAMGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));     
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmNumList(param0);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }                
        } else if (datastr.contains("FFIPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmInputNumList(param0);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FAMGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, params[1]);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FIPGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, params[1]);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln- 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FLNGPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmFinpLoanGRPNumList(param0, params[1]);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("FFIPNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.farmInputNumList(param0);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }        
        } else if (datastr.contains("FLNFMRNU")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.loanNumList(param0);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";
            }
        } else if (datastr.contains("LONDETS")){
            current_username = noNull((String) userproperties.get("current_username"));
            System.out.println("current_username: " + current_username);
            if(!current_username.isEmpty()){
                params = jqueryStringToArray(datastr);
                Integer param0 = Integer.valueOf(params[0]);
                String details = BUZLOGIC_INTERFACE.loanDetails(param0);
                //int rbkt = details.indexOf("]");
                int textln = details.length();
                htmlMessage = details.substring(0, textln - 1);
                htmlMessage = noNull(htmlMessage);
            }else{
                htmlMessage = "<p><b>" + "No data for reporting!" + "</p>";     
            }
        } else if (datastr.contains("TDATA")){
            htmlMessage = "<p><b>" + "Server endpoint gets connected!" + "</p>"; 
        } else if (datastr.contains("CSEMA")){      
            params = jqueryStringToArray(datastr);      
            String result = "";
            // Recipient's email ID needs to be mentioned.
            String to = params[0];        //abcd@gmail.com";
            String subject = params[3]; 
            String body = params[4]; 

            // Sender's email ID needs to be mentioned
            final String fromemail = params[1];      //"mcmohd@gmail.com";
            final String password = params[2];

            // Assuming you are sending email from localhost
            //String host = "localhost";
            String smtpserver = "localhost, 192.168.1.1";//your local host
            int port = 465;
            String[] toemails = {to}; 
            MailDaemon mDaemon = MailDaemon.getInstance(fromemail, password, smtpserver, port);
            result = mDaemon.sendEmail(toemails, subject, body, fromemail);
            htmlMessage = "<p><b>" + result + "</p>";            
        }
        System.out.println("htmlMessage: " + htmlMessage);
        return htmlMessage;     
    }
    
    /*
    private int uniqkey(String full_name, String email_phone, String password) {
        final int prime = 31;
        final int id = 1111;
	int result = 1;
	//result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + full_name.hashCode();
	result = prime * result + (int) (id ^ (id >>> 29));
        result = prime * result + email_phone.hashCode();
	result = prime * result + (int) (id ^ (id >>> 29));
	//result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + password.hashCode();
	return result;
    }
    */
    
    private void saveTrails(String summary){
        String ldtStr;  
        Instant timestamp = Instant.now();
        ldtStr = (timestamp.toString()).trim();
        String summary_detail = current_username.toUpperCase() + ": " + summary + ". Time: " + ldtStr;
        String loggedtrail =  current_username.toUpperCase() + "/" + current_userrole.toUpperCase() + ": " + summary + ". Time: " + ldtStr;
        BUZLOGIC_INTERFACE.addUserTrails(summary_detail, loggedtrail, current_phonenum);
    }
       
    private String removeComaFromNumber(String formated_number){
        System.out.println("formated_number: " + formated_number);
        String new_str = "";
        int fnln = formated_number.length();
        for(int i = 0; i < fnln; i++) {
            String str = formated_number.substring(i, i + 1);
            if (!str.equals(",")){
                new_str += str;
                System.out.println(i + ", str: " + i + ", " + str);
            }
        }
        System.out.println("new_str: " + new_str);
        return new_str;
    }
    
    private String noNull(String s) {
        return s == null ? "" : s;
    }
    
    private String[] jqueryStringToArray(String dataStr){
        System.out.println("jqueryStringToArray dataStr : " + dataStr);
        String mydelim = "&";
        String[] strarray = null;
        String[] params = null;
        try{
            StringTokenizer stringName = new StringTokenizer(dataStr, mydelim);
            int count = stringName.countTokens();
            System.out.println("jqueryStringToArray Number of tokens : " + count);
            List<String> strlst = new ArrayList<>();
            while (stringName.hasMoreElements()){
                String elem = (String) stringName.nextElement();
                System.out.println("jqueryStringToArray elem: " + elem);
                strlst.add(elem);
            }
            int strlstln = strlst.size();
            strarray = new String[strlstln];
            int i = 0;
            for(String elem: strlst){                
                strarray[i] = elem;
                System.out.println("jqueryStringToArray strarray["+i+"] : " + strarray[i]);
                i++;
            }
            
             //String[] subarray = stringSubArray(datastr);
            mydelim = "=";
            System.out.println("jqueryStringToArray strarray.length : " + strarray.length);
            params = new String[strarray.length];
            System.out.println("jqueryStringToArray params.length : " + params.length);
            for(i = 0; i < strarray.length; i++){
                System.out.println("jqueryStringToArray strarray[" + i + "] : " + strarray[i]);
                stringName = new StringTokenizer(strarray[i], mydelim);
                count = stringName.countTokens();
                System.out.println("jqueryStringToArray Number of tokens : " + count);
                List<String> strlist = new ArrayList<>();
                while (stringName.hasMoreElements()){
                    String elem = (String) stringName.nextElement();
                    System.out.println("jqueryStringToArray elem 2 : " + elem);
                    strlist.add(elem);
                }

                strlstln = strlist.size();
                String[] tmparray = new String[strlstln];
                int j = 0;
                for(String elem: strlist){                
                    tmparray[j] = elem;
                    System.out.println("jqueryStringToArray tmparray["+j+"] : " + tmparray[j]);
                    j++;
                }
                params[i] = tmparray[1];
            }            
            return params;
        }catch (NullPointerException | ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException ex) {
            System.out.println("jqueryStringToArray String data to array: " +  ex.getMessage());
        }
        
        return null;
    }
    
    private String formInputsValidation(String dataStr){
        // emailaddr=&flag=SUBS
        String errorstr = "";
        int nbval = 0;
        int i = 0;
        if(dataStr.charAt(0) == '&'){
            errorstr = "No input data. Please, enter all fields.";
            return errorstr;
        }else{
            int eqlcn;      // = dataStr.indexOf("=", i);
            while(i < dataStr.length()){      
                eqlcn = dataStr.indexOf("=", i);
                if(dataStr.charAt(eqlcn + 1) == '&'){
                    nbval++;
                    i = eqlcn + 1;
                    //eqlcn = dataStr.indexOf("=", i);
                }else{
                    i++;
                } 
                //eqlcn = dataStr.indexOf("=", i);
            }
            i = 0;
            int ugfdin; 
            while(i < dataStr.length()){      
                ugfdin = dataStr.indexOf("undefined", i);
                if(ugfdin != -1){
                    nbval++;
                    i = ugfdin + 8;
                    //eqlcn = dataStr.indexOf("=", i);
                }else{
                    i++;
                } 
                //eqlcn = dataStr.indexOf("=", i);
            }
            if(nbval > 0){
                errorstr = Integer.toString(nbval) + " empty field(s). Please, enter all fields.";
            }
            return errorstr;        
        } 
    }
}
