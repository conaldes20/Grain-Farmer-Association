/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.buzlogicImpl;

import ng.grain.assoc.intface.BusinessLogic;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 *
 * @author CONALDES
 */
public class BusinessLogicImpl implements BusinessLogic{

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private static final Logger log = Logger.getLogger(BusinessLogicImpl.class.getName());	
    private String msg = " ";
    
    protected final String INSERT_FARMER = "insert into farmer (first_name, middle_name, last_name, state_of_origin, local_government, ward, home_address," +
 		       "phonenum, bvn, nim, groupid) values(?,?,?,?,?,?,?,?,?,?,?)";

    protected final String INSERT_FARM = "insert into farm (longi1, latit1, longi2, latit2, longi3, latit3, longi4, latit4, area, community, ward, " +
            "local_government, state_located, crop, area_planted, year_planted, harvest, net_worth, income, farmerid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    protected final String INSERT_FARMINPUT = "insert into farminput (farm_input, input_cost, year_given, farmerid) values(?,?,?,?)";

    protected final String INSERT_FMGROUP = "INSERT INTO fmgroup (group_name, address, ward, local_government, state_located, crops) VALUES(?,?,?,?,?,?)";

    protected final String INSERT_OFFICER = "INSERT INTO officer (first_name, middle_name, last_name, office, phonenum, bvn, nim, home_address, groupid) values(?,?,?,?,?,?,?,?,?)";

    protected final String INSERT_USERACCOUNT = "INSERT INTO useraccount (username, password, phonenum,  userrole) VALUES(?,?,?,?)";

    protected final String INSERT_AUDITTRAIL = "insert into audittrail (log_summary, log_detail, phonenum) values(?,?,?)"; 

    protected final String INSERT_AGGREGATION = "insert into aggregation (town, ward, local_government, state_located, groupid, aggregation_level) values(?,?,?,?,?,?)";

    protected final String INSERT_LOAN = "insert into loan (description, amount_given, amount_paid, farmerid) values(?,?,?,?)";

    protected final String INSERT_CONTACT = "insert into contact (full_name, email, subject, message) values(?,?,?,?)";

    protected final String INSERT_SUBSCRIBER = "insert into subscriber (email, status) values(?,?)";
    
    protected final String UPDATE_FARMER = "update farmer set first_name = ?, middle_name = ?, last_name = ?, state_of_origin = ?, local_government = ?, ward = ?, home_address = ?," +
 		       "phonenum = ?, bvn = ?, nim = ?, groupid = ? where id = ?";

    protected final String UPDATE_FARM = "update farm set cordinates = ?, area = ?, community = ?, ward = ?, local_government = ?, state_located = ?, crop = ?, area_planted = ?," +
                         "year_planted = ?, harvest = ?, net_worth = ?, income = ?, farmerid = ? where id = ?";

    protected final String UPDATE_FARMINPUT = "update farminput set farm_input = ?, input_cost = ?, year_given = ?, farmerid = ? where id = ?";

    protected final String UPDATE_FMGROUP = "update fmgroup set group_name = ?, address = ?, ward = ?, local_government = ?, state_located = ?, crops = ? where id = ?";

    protected final String UPDATE_OFFICER = "update officer set first_name = ?, middle_name = ?, last_name = ?, office = ?, phonenum = ?, bvn = ?, nim = ?, home_address = ?, groupid = ? where id = ?";

    protected final String UPDATE_USERACCOUNT = "update useraccount username = ?, password = ?, phonenum = ?,  userrole = ? where id = ?";

    protected final String UPDATE_AUDITTRAIL = "update audittrail set log_summary = ?, log_detail = ?, phonenum = ? where id = ?"; 

    protected final String UPDATE_AGGREGATION = "update into aggregation (town, ward, local_government, state_located, groupid, aggregation_level) where id = ?";

    protected final String UPDATE_LOAN = "update loan set description = ?, amount_given = ?, amount_paid = ?, farmerid = ? where id = ?";

    protected final String UPDATE_CONTACT = "update contact set full_name = ?, email = ?, subject = ?, message = ? where id = ?";

    protected final String UPDATE_SUBSCRIBER = "update subscriber set email = ?, status = ? where id = ?";

    
    protected final String SQL_SELECT = "SELECT id, membname, passward, emailphone, domestic_state, current_state, input_city, input_zip, picture FROM members WHERE id = ?";

    protected final String SQL_GROUP = "INSERT INTO FMGROUP VALUES (?, ?, ?, ?, ?, ?)";

    protected final String SQL_UPDATE = "UPDATE members SET membname = ?, passward = ?, emailphone = ?, domestic_state = ?, current_state = ?, input_city = ?, input_zip = ?, picture = ? WHERE id = ?";
    
    protected final String SQL_DELETE = "DELETE FROM FROM members WHERE id = ?";
    
    protected Connection conn;
    
    public BusinessLogicImpl()
    {
    }

    public BusinessLogicImpl(final java.sql.Connection conn)
    {
        this.conn = conn;
    }
    
    @Override
    public String addFmgroup(String groupName, String address, String ward, String localGovernment, String state, String crops){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_FMGROUP, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, groupName);
            stmt.setString(2, address);
            stmt.setString(3, ward);
            stmt.setString(4, localGovernment);
            stmt.setString(5, state);
            stmt.setString(6, crops);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

            return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addFmgroup(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;         
    }
    
    @Override
    public String addFarmer(String firstName, String middleName, String lastName, String state, 
            String localGovernment, String ward, String homeAddress, String phonenum, String bvn, String nim, int groupid){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_FARMER, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, firstName);
            stmt.setString(2, middleName);
            stmt.setString(3, lastName);
            stmt.setString(4, state);
            stmt.setString(5, localGovernment);
            stmt.setString(6, ward);
            stmt.setString(7, homeAddress);
            stmt.setString(8, phonenum); 
            stmt.setString(9, bvn);
            stmt.setString(10, nim);
            stmt.setInt(11, groupid);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addFarmer(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception; 
    }
    
    @Override
    public String addOfficer(String firstName, String middleName, String lastName, String office, 
            String phonenum, String bvn, String nim, String homeAddress, int groupid){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_OFFICER, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, firstName);
            stmt.setString(2, middleName);
            stmt.setString(3, lastName);
            stmt.setString(4, office);            
            stmt.setString(5, phonenum); 
            stmt.setString(6, bvn);
            stmt.setString(7, nim);
            stmt.setString(8, homeAddress);
            stmt.setInt(9, groupid);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addOfficer(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addFarm(BigDecimal longi1, BigDecimal latit1, BigDecimal longi2, BigDecimal latit2, BigDecimal longi3, BigDecimal latit3, 
            BigDecimal longi4, BigDecimal latit4, BigDecimal area, String community, String ward, String localGovernment, String stateLocated, 
            String crop, BigDecimal areaPlanted, String yearPlanted, BigDecimal harvest, BigDecimal netWorth, BigDecimal income, int farmerid){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_FARM, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setBigDecimal(1, longi1);
            stmt.setBigDecimal(2, latit1);
            stmt.setBigDecimal(3, longi2);
            stmt.setBigDecimal(4, latit2);
            stmt.setBigDecimal(5, longi3);
            stmt.setBigDecimal(6, latit3);
            stmt.setBigDecimal(7, longi4);
            stmt.setBigDecimal(8, latit4);            
            stmt.setBigDecimal(9, area);
            stmt.setString(10, community);
            stmt.setString(11, ward);            
            stmt.setString(12, localGovernment); 
            stmt.setString(13, stateLocated);
            stmt.setString(14, crop);
            stmt.setBigDecimal(15, areaPlanted);
            stmt.setString(16, yearPlanted);
            stmt.setBigDecimal(17, harvest);
            stmt.setBigDecimal(18, netWorth);
            stmt.setBigDecimal(19, income);
            stmt.setInt(20, farmerid);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addFarm(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addAggregation(String town, String ward, String localGovernment, String state, int groupid, String aggregationLevel){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_AGGREGATION, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, town);
            stmt.setString(2, ward);                     
            stmt.setString(3, localGovernment); 
            stmt.setString(4, state);
            stmt.setInt(5, groupid);
            stmt.setString(6, aggregationLevel);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addAggregation(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addFarmInput(String farmInput, BigDecimal inputCost, String yearGiven, int farmerid){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_FARMINPUT, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, farmInput);
            stmt.setBigDecimal(2, inputCost);
            stmt.setString(3, yearGiven);
            stmt.setInt(4, farmerid);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addFarmInput(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addLoan(String description, BigDecimal amountGiven, BigDecimal amountPaid, int farmerid){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_LOAN, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, description);
            stmt.setBigDecimal(2, amountGiven);
            stmt.setBigDecimal(3, amountPaid);
            stmt.setInt(4, farmerid);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addLoan(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addContact(String fullName, String email, String subject, String message){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_CONTACT, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, subject);
            stmt.setString(4, message);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addContact(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    } 
    
    @Override
    public String addSubscriber(String email, String resp_status){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_SUBSCRIBER, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, email);
            stmt.setString(2, resp_status);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addSubscriber(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addUserAccounts(String username, String password, String phonenum, String userrole){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_USERACCOUNT, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, phonenum);
            stmt.setString(4, userrole);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addUserAccounts(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }
    
    @Override
    public String addUserTrails(final String loggedsummary, final String loggeddetails, String phonenum){
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;
        Integer key = null;
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(INSERT_AUDITTRAIL, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, loggedsummary);
            stmt.setString(2, loggeddetails);
            stmt.setString(3, phonenum);
            int rows = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                key = (Integer)rs.getInt(1);
            }

             return key.toString();
        }
        catch (SQLException ex)
        {
            System.out.println("addUserTrails(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
        }
        return exception;
    }  
    
    /*       
    @Override
    public boolean update(String membName, String passward, String emailPhone, String domesticState, 
            String currentState, String inputCity, String inputZip, String picture, Integer id)
    {
        long t1 = System.currentTimeMillis();
	// declare variables
	//final boolean isConnSupplied = (membersConn != null);
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;

	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = membersConn.prepareStatement(SQL_UPDATE);

            stmt.setString(1, membName);
            stmt.setString(2, passward);
            stmt.setString(3, emailPhone);
            stmt.setString(4, domesticState);
            stmt.setString(5, currentState);
            stmt.setString(6, inputCity);
            stmt.setString(7, inputZip);
            stmt.setString(8, picture);

            stmt.setInt(9, id);
            int rows = stmt.executeUpdate();
            status = true;
            return status;
        }
        catch (SQLException ex)
        {
            System.out.println("MembersDaoImpl update() " + ex.getMessage());
        }
        return false;
    }
    */
    
    public boolean updateLoan(BigDecimal currentSum, Integer loanId) {
        long t1 = System.currentTimeMillis();
	// declare variables
	String UPDATE_LOAN = "update loan set amount_paid = ? where id = ?";
	boolean status = false;
	//Connection conn = null;
	PreparedStatement stmt = null;

	try {
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membersConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(UPDATE_LOAN);

            stmt.setBigDecimal(1, currentSum);
            stmt.setInt(2, loanId);
            int rows = stmt.executeUpdate();
            status = true;
            return status;
        }
        catch (SQLException ex)
        {
            System.out.println("updateLoal update() " + ex.getMessage());
        }
        return false;
    }
    
    @Override
    public String groupNumList(){
        /*
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteria = builder.createQuery( Integer.class );
        Root<Officer> officerRoot = criteria.from( Officer.class );
        criteria.select( officerRoot.get( Officer_.groupid) );
        criteria.where( builder.equal( officerRoot.get( Officer_.groupid ), groupId ) );
        List<Integer> officerids = entityManager.createQuery( criteria ).getResultList();
        */
                
        //String SQL = "SELECT g.id FROM fmgroup AS g " +
        //    "WHERE g.groupid = '%" + groupId + "";
        String SQL_GROUP_NUMS = "SELECT g.id, g.group_name, local_government, state_located FROM fmgroup AS g ";
       // List<String> grpids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String group_details = "groupnums]";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        
        //BigDecimal total_amount = new BigDecimal(0.00);
	try {
            stmt = conn.prepareStatement(SQL_GROUP_NUMS);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String groupname = rs.getString(2);
                group_details += id.toString() + "/" + groupname.trim() + ", ";
                String groulgovt = rs.getString(3);
                group_details += "LGovt: " + groulgovt.trim() + ", ";
                String groupstate = rs.getString(4);
                group_details += "State: " + groupstate.trim() + "#";
            }
            if (group_details.equals("groupnums]")){
                group_details += "groupnums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex) {
            System.out.println("groupNumList: " + ex.getMessage());
            group_details = "SQLException: " + ex.getMessage();
        }
        
        System.out.println("groupNumList(): " + group_details);
        return group_details;
    }
    
    @Override
    public String officerNumList(Integer groupId){                
        String SQL_OFFICER_NUMS = "SELECT f.id, f.first_name, f.middle_name, f.last_name " +
               "FROM officer AS f " +
                "WHERE g.groupid = ?";    
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String officer_details = "officernums]";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try {
            stmt = conn.prepareStatement(SQL_OFFICER_NUMS);
            stmt.setInt(1, groupId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String first_name = rs.getString(2);
                String middle_name = rs.getString(3);
                String last_name = rs.getString(4);
                String officer_name;
                if (middle_name.equals("xxx")){
                    officer_name = first_name.trim() + " " + last_name.trim();
                } else {
                    officer_name = first_name.trim() + " " + middle_name.trim() + " " + last_name.trim();
                }
                officer_details += id.toString() + "/" + officer_name + "#";
            }
            if (officer_details.equals("officernums]")){
                officer_details += "officernums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex) {
            System.out.println("officerNumList: " + ex.getMessage());
            officer_details = "SQLException: " + ex.getMessage();
        }

        System.out.println("officerNumList(): " + officer_details);
        return officer_details;
    }
    
    @Override
    public String famerNumList(Integer groupId){                
        String SQL_FARMER_NUM = "SELECT f.id, f.first_name, f.middle_name, f.last_name " +
                "FROM farmer AS f "  +
                "WHERE f.groupid = ?";
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String farmer_details = "farmernums]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_FARMER_NUM);
            //int index = 0;
            stmt.setInt(1, groupId);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String first_name = rs.getString(2);
                String middle_name = rs.getString(3);
                String last_name = rs.getString(4);
                String farmer_name;
                if (middle_name.equals("xxx")){
                    farmer_name = first_name.trim() + " " + last_name.trim();
                } else {
                    farmer_name = first_name.trim() + " " + middle_name.trim() + " " + last_name.trim();
                }
                farmer_details += id.toString() + "/" + farmer_name + "#";
            }
            if (farmer_details.equals("farmernums]")){
                farmer_details += "farmernums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex){
            System.out.println("famerNumListr() " + ex.getMessage());
            farmer_details = "SQLException: " + ex.getMessage();
	} 
        
        System.out.println("farmerNumList(): " + farmer_details);
        return farmer_details;
    }
    
    @Override
    public String aggregationNumList(Integer groupId){                
        String SQL_AGGREG_NUM = "SELECT g.id, g.town, g.ward, g.local_government, g.state_located " +
                "FROM aggregation AS g "  +
                "WHERE g.groupid = ?";
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String aggreg_details = "aggregnums]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_AGGREG_NUM);
            //int index = 0;
            stmt.setInt(1, groupId);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String town = rs.getString(2);
                String ward = rs.getString(3);
                String localgvt = rs.getString(4);
                String state = rs.getString(5);
                String details = town + " " + ward + " " + localgvt + " " + state;
                aggreg_details += id.toString() + "/" + details + "#";
            }
            if (aggreg_details.equals("aggregnums]")){
                aggreg_details += "aggregnums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex){
            System.out.println("aggregationNumList() " + ex.getMessage());
            aggreg_details = "SQLException: " + ex.getMessage();
	}   
        
        System.out.println("aggregNumList(): " + aggreg_details);
        return aggreg_details;
    }
    
    @Override
    public String farmNumList(Integer farmerId){                
        String SQL_FARM_NUM = "SELECT m.id, m.ward, m.local_government, m.state_located " +
                "FROM farm AS f "  + 
                "where f.farmerid = ?";  
        
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String farm_details = "farmnums]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_FARM_NUM);
            //int index = 0;
            stmt.setInt(1, farmerId);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String ward = rs.getString(21);
                String localgvt = rs.getString(3);
                String state = rs.getString(4);
                String details = ward + " " + localgvt + " " + state;
                farm_details += id.toString() + "/" + details + "#";
            }
            if (farm_details.equals("farmnums]")){
                farm_details += "farmnums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex){
            System.out.println("farmNumList() " + ex.getMessage());
            farm_details = "SQLException: " + ex.getMessage();
	} 
        
        System.out.println("farmNumList(): " + farm_details);
        return farm_details;
    }
    
    @Override
    public String farmFinpLoanGRPNumList(Integer groupId, String tableref){                
        String SQL_FARMER_NUM = "SELECT f.id AS farmerId, f.first_name AS firstName, f.middle_name AS middleName, f.last_name AS lastName " +
            "from farmer AS f WHERE  f.groupid = ?";
        
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String farmer_details = "farmernums(" + tableref + ")]";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try {
            stmt = conn.prepareStatement(SQL_FARMER_NUM);
            stmt.setInt(1, groupId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String first_name = rs.getString(2);
                String middle_name = rs.getString(3);
                String last_name = rs.getString(4);
                String farmer_name;
                //if (middle_name.isEmpty()){
                if (middle_name.equals("Xxx")){
                    farmer_name = first_name.trim() + " " + last_name.trim();
                } else {
                    farmer_name = first_name.trim() + " " + middle_name.trim() + " " + last_name.trim();
                }
                farmer_details += id.toString() + "/" + farmer_name + "#";
            }
            if (farmer_details.equals("farmernums(" + tableref + ")]")){
                farmer_details += "farmernums(" + tableref + ")]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex) {
            System.out.println("farmFinpLoanGRPNumList(): " + ex.getMessage());
            farmer_details = "SQLException: " + ex.getMessage();
        }   
        
        System.out.println("farmFinpLoanGRPNumList(): " + farmer_details);
        return farmer_details;
    }
    
    @Override
    public String farmInputNumList(Integer farmerId){                
        String SQL_FARMINP_NUM = "SELECT p.id, p.farm_input " +
                "FROM farminput AS p "  +  
                "where p.farmerid = ?";  
        
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String farminp_details = "farminpnums]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_FARMINP_NUM);
            //int index = 0;
            stmt.setInt(1, farmerId);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String farminput = rs.getString(2);
                farminp_details += id.toString() + "/" + farminput + "#";
            }
            if (farminp_details.equals("farminpnums]")){
                farminp_details += "farminpnums]00001/unknown 1#00002/unknown 2#";
            }
        } catch (SQLException ex){
            System.out.println("farmInputNumList() " + ex.getMessage());
            farminp_details = "SQLException: " + ex.getMessage();
	}  
        
        System.out.println("farmInputNumList(): " + farminp_details);
        return farminp_details;
    }
    
    @Override
    public String loanNumList(Integer farmerId){                
        String SQL_LOAN_NUM = "SELECT l.id, l.description " +
                "FROM loan AS l "  +  
                "where l.farmerid = ?";  
        
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String loan_details = "loannums]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_LOAN_NUM);
            //int index = 0;
            stmt.setInt(1, farmerId);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                Integer id = (Integer)rs.getInt(1);
                String description = rs.getString(2);
                loan_details += id.toString() + "/" + description + "#";
            }
            if (loan_details.equals("loannums]")){
                loan_details += "loannums]00001/unknown 1#00002/unknown 2#";
            } 
        } catch (SQLException ex){
            System.out.println("loanNumList() " + ex.getMessage());
            loan_details = "SQLException: " + ex.getMessage();
	}   
        
        System.out.println("loanNumList(): " + loan_details);
        return loan_details;
    }
    
    public String loanDetails(Integer loanid){                
        String SQL_LOAN_DET = "SELECT l.amount_given, l.amount_paid " +
                "FROM loan AS l "  +  
                "where l.id = ?";  
        
        //List<Integer> farmerids = new ArrayList<>();
        // "groupnums]2112/assoc 1#1211/assoc 2"
        String loan_details = "loandetails]";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_LOAN_DET);
            //int index = 0;
            stmt.setInt(1, loanid);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String loanamount = format("#,###.00", rs.getBigDecimal(1).doubleValue());(rs.getBigDecimal(1)).toString();
                String amountpaid = format("#,###.00", rs.getBigDecimal(2).doubleValue());(rs.getBigDecimal(2)).toString();
                loan_details += loanamount + "|" + amountpaid + "#";
            }
            if (loan_details.equals("loandetails]")){
                loan_details += "loandetails]0.00|0.00#";
            } 
        } catch (SQLException ex){
            System.out.println("loanDetails() " + ex.getMessage());
            loan_details = "SQLException: " + ex.getMessage();
	}   
        
        System.out.println("loanDetail(): " + loan_details);
        return loan_details;
    }
    
    @Override
    public String getCurrentUser(String phonenum, String password){  
        System.out.println("phonenum, password: " + phonenum + ", " + password);
        String SQL_SELECT_USER = "SELECT username, phonenum FROM useraccount\n" +
                                "WHERE  phonenum = ? AND password = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String username = null;  
        String userrole = null;
        //BigDecimal total_amount = new BigDecimal(0.00);
        String exception = "";
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_SELECT_USER);
            //int index = 0;
            stmt.setString(1, phonenum);
            stmt.setString(2, password);
            //stmt.setString(index++, password);
		
            rs = stmt.executeQuery();
            while (rs.next()) {
                username = noNull(rs.getString(1));
                userrole = noNull(rs.getString(2));
            }
			// fetch the results
            return username + ":" + userrole;
	} catch (SQLException ex){
            System.out.println("getCurrentUser(): " + ex.getMessage());
            exception = "SQLException: " + ex.getMessage();
	}
        return exception;
    }
    
    @Override
    public String getLoggedDetails(final String phonenum){   
        final String SQL_LOGGED = "SELECT u.username, a.log_summary, a.log_detail" +
                "FROM audittrail AS a INNER JOIN useraccount AS u WHERE a.phonenum = ?";
        String logged_details = "loggedinfos]";
        PreparedStatement stmt = null;
        ResultSet rs = null;   
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(SQL_LOGGED);
            //int index = 0;
            stmt.setString(1, phonenum);		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString(1);
                logged_details += username + "#";
                String log_summary = rs.getString(2);
                logged_details += log_summary + "#";
                String log_detail = rs.getString(3);
                logged_details += log_detail + "#";
            }
            return logged_details;
        } catch (SQLException ex){
            System.out.println("getLoggedDetails() " + ex.getMessage());
            logged_details = "SQLException: " + ex.getMessage();
	}
        return logged_details;
    }
      
    private String noNull(String s) {
        return s == null ? "" : s;
    }
    
    /*    
    String sql1 = "select sum(area_planted*3) as total_production from farm order by local_government desc";
    String sql2 = "select sum(area_planted*3) as total_production from farm order by state_located desc";
    String sql3 = "select local_government, state_located, sum(area_planted*3) as total_production from farm order by (local_government, state_located)";
    String sql4 = "select sum(area_planted*3) as total_production from farm GROUP BY ROLLUP(local_government, state_located)";
    String sql5 = "select sum(harvest*3) as actual_production from farm order by local_government desc";
    String sql6 = "select sum(harvest*3) as actual_production from farm order by state_located desc";
    String sql7 = "select local_government, state_located, sum(harvest*3) as actual_production from farm order by (local_government, state_located)";
    String sql8 = "select sum(harvest*3) as actual_production from farm GROUP BY ROLLUP(local_government, state_located)";

    String sql9 = "select sum(area_planted*3)*price_per_ton as expected_income from farm order by local_government desc";
    String sql10 = "select sum(area_planted*3)*price_per_ton as expected_income from farm order by state_located desc";
    String sql11 = "select local_government, state_located, sum(area_planted*3)*price_per_ton as expected_income from farm order by (local_government, state_located)";
    String sql12 = "select sum(area_planted*3)*price_per_ton as expected_income from farm GROUP BY ROLLUP(local_government, state_located)";

    String sql13 = "select sum(harvest*3)*price_per_ton as actual_income from farm order by local_government desc";
    String sql14 = "select sum(harvest*3)*price_per_ton as actual_income from farm order by state_located desc";
    String sql15 = "select local_government, state_located, sum(harvest*3)*price_per_ton as actual_income from farm order by (local_government, state_located)";
    String sql16 = "select sum(harvest*3)*price_per_ton as actual_income from farm GROUP BY ROLLUP(local_government, state_located)";

    String sql17 = "select t.description, g.local_government, g.state_located, sum(t.amountpaid) as loan_recovered" +
    "from fmgroup as g," +
    "(" +
    "select l.description as description, l.amount_paid as amountpaid, f.groupid as groupid" +
    "from loan as l inner join farmer as f on l.farmerid = f.id" +
    ") as t" +
    "where g.id = t.groupid";

    String sql18 = "select t.farminput, g.local_government, g.state_located, sum(t.inputcost) as input_supplied" +
    "from fmgroup as g," +
    "(" +
    "select p.farminput as farminput, p.input_cost as inputcost, f.groupid as groupid" + 
    "from farminput as p inner join farmer as f on p.farmerid = f.id" +
    ") as t" +
    "where g.id = t.groupid";
    */

    @Override
    public String getTotalProduction(){   
        String reportstr = "TPE]";
        String sql_total_prod = "select local_government, state_located, sum(area_planted*3) as total_production " +
                "from farm " +
                "GROUP BY local_government, state_located";
        //String sql_total_prod = "select local_government, state_located, sum(area_planted*3) as total_production " +
        //        "from farm " +
        //        "GROUP BY ROLLUP (local_government, state_located)";
        String msg;
        
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_total_prod);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String localgvt = rs.getString(1);
                reportstr += localgvt + "|";
                String state = rs.getString(2);
                reportstr += state + "|";
                String totalprod = format("#,###.00", rs.getBigDecimal(3).doubleValue());  //rs.getBigDecimal(3).toString();
                reportstr += totalprod + "#";
            }
            if (reportstr.equals("TPE]")){
                reportstr += "none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getTotalProduction(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        }        
        return reportstr;   
    }

    @Override
    public String getActualProduction(){   
        String reportstr = "APN]";
        final String sql_actual_prod = "select local_government, state_located, sum(harvest*3) as actual_production " +
                "from farm " +
                "GROUP BY local_government, state_located";
        String msg;
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_actual_prod);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String localgvt = rs.getString(1);
                reportstr += localgvt + "|";
                String state = rs.getString(2);
                reportstr += state + "|";
                String actualprod = format("#,###.00", rs.getBigDecimal(3).doubleValue());  //rs.getBigDecimal(3).toString();
                reportstr += actualprod + "#";
            }
            if (reportstr.equals("APN]")){
                reportstr += "none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getActualProduction(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }
    
    @Override
    public String getExpectedIncome(){   
        String reportstr = "EPI]";
        //int price_per_ton = 2300000;
        String sql_exp_income = "select local_government, state_located, sum(area_planted*3)*2300000 as expected_income " +
                "from farm " +
                "GROUP BY local_government, state_located";
        String msg;
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_exp_income);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String localgvt = rs.getString(1);
                reportstr += localgvt + "|";
                String state = rs.getString(2);
                reportstr += state + "|";
                String expincome = format("#,###.00", rs.getBigDecimal(3).doubleValue()); //rs.getBigDecimal(3).toString();
                reportstr += expincome + "#";
            }
            if (reportstr.equals("EPI]")){
                reportstr += "none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getExpectedIncome(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }
    
    @Override
    public String getActualIncome(){   
        String reportstr = "ACI]";
        final String sql_act_income = "select local_government, state_located, sum(harvest*3)*2300000 as actual_income " +
                "from farm " +
                "GROUP BY local_government, state_located";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_act_income);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String localgvt = rs.getString(1);
                reportstr += localgvt + "|";
                String state = rs.getString(2);
                reportstr += state + "|";
                String actualincome = format("#,###.00", rs.getBigDecimal(3).doubleValue()); //rs.getBigDecimal(3).toString();
                reportstr += actualincome + "#";
            }
            if (reportstr.equals("ACI]")){
                reportstr += "none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getActualIncome(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }

    @Override
    public String getTotalInputSupplied(){   
        String reportstr = "TIS]";
        String sql_input_supplied = "select t.farminput, g.local_government, g.state_located, sum(t.inputcost) as input_supplied " +
            "from fmgroup as g, " +            
            "(" +
            "select p.farm_input as farminput, p.input_cost as inputcost, f.groupid as groupid " + 
            "from farminput as p inner join farmer as f on p.farmerid = f.id" +
            ") as t " +
            "where g.id = t.groupid " +
            "GROUP BY t.farminput, g.local_government, g.state_located";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_input_supplied);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String farminput = rs.getString(1);
                reportstr += farminput + "|";
                String localgvt = rs.getString(2);
                reportstr += localgvt + "|";
                String state = rs.getString(3);
                reportstr += state + "|";
                String inpsupplied = format("#,###.00", rs.getBigDecimal(4).doubleValue());  //rs.getBigDecimal(4).toString();
                reportstr += inpsupplied + "#";
            }
            if (reportstr.equals("TIS]")){
                reportstr += "none|none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getTotalInputSupplied(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }
    
    @Override
    public String getTatalLoanGranted(){   
        String reportstr = "TLG]";
        final String sql_loan_granted = "select t.description, g.local_government, g.state_located, sum(t.amountgranted) as loan_granted " +
            "from fmgroup as g, " +
            "(" +
            "select l.description as description, l.amount_given as amountgranted, f.groupid as groupid " +
            "from loan as l inner join farmer as f on l.farmerid = f.id" +
            ") as t " +
            "where g.id = t.groupid " +
            "GROUP BY t.description, g.local_government, g.state_located";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_loan_granted);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String deacription = rs.getString(1);
                reportstr += deacription + "|";
                String localgvt = rs.getString(2);
                reportstr += localgvt + "|";
                String state = rs.getString(3);
                reportstr += state + "|";
                String lngranted = format("#,###.00", rs.getBigDecimal(4).doubleValue()); //rs.getBigDecimal(4).toString();
                reportstr += lngranted + "#";
            }
            if (reportstr.equals("TLG]")){
                reportstr += "none|none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getTatalLoanGranted(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }

    @Override
    public String getTatalLoanRecovered(){   
        String reportstr = "TLR]";
        final String sql_loan_recovered = "select t.description, g.local_government, g.state_located, sum(t.amountpaid) as loan_recovered " +
            "from fmgroup as g, " +
            "(" +
            "select l.description as description, l.amount_paid as amountpaid, f.groupid as groupid " +
            "from loan as l inner join farmer as f on l.farmerid = f.id" +
            ") as t " +
            "where g.id = t.groupid " +
            "GROUP BY t.description, g.local_government, g.state_located";
        PreparedStatement stmt = null;
        ResultSet rs = null;     
        //BigDecimal total_amount = new BigDecimal(0.00);
	try
	{
            // get the user-specified connection or get a connection from the DBConnection
            //conn = isConnSupplied ? membduesConn : DBConnection.getConnection();

            stmt = conn.prepareStatement(sql_loan_recovered);
            		
            rs = stmt.executeQuery();
            while (rs.next()) {
                String deacription = rs.getString(1);
                reportstr += deacription + "|";
                String localgvt = rs.getString(2);
                reportstr += localgvt + "|";
                String state = rs.getString(3);
                reportstr += state + "|";
                String lnrecovered = format("#,###.00", rs.getBigDecimal(4).doubleValue());                 
                reportstr += lnrecovered + "#";
            }
            if (reportstr.equals("TLR]")){
                reportstr += "none|none|none|none#";
            }
            return reportstr;
        } catch (SQLException ex){
            System.out.println("getTatalLoanRecovered(): " + ex.getMessage());
            reportstr = "SQLException: " + ex.getMessage();
        } 
        return reportstr;
    }
    
    private String format(String pattern, double value ) {
            
            //String strinvestment = custformat.customFormat("#,###.00", investment*10000);
            //String ftcashinflow = custformat.customFormat("#,###.00", cashinflow*10000);
            DecimalFormat myFormatter = new DecimalFormat(pattern);
            return myFormatter.format(value);
    }
}