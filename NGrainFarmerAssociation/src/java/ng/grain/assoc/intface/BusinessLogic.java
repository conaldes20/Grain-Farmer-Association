/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.intface;

import java.math.BigDecimal;

/**
 *
 * @author CONALDES
 */
public interface BusinessLogic {
    public String addContact(String fullName, String email, String subject, String message);
    public String addSubscriber(String email, String status); 
    public String addFmgroup(String groupName, String address, String ward, String localGovernment, String stateLocated, String crops);
    public String addFarmer(String firstName, String middleName, String lastName, String stateOfOrigin, String localGovernment, String ward, 
            String homeAddress, String phonenum, String bvn, String nim, int groupid);    
    public String addOfficer(String firstName, String middleName, String lastName, String office, String phonenum, String bvn, String nim, 
            String homeAddress, int groupid);    
    public String addFarm(BigDecimal longi1, BigDecimal latit1, BigDecimal longi2, BigDecimal latit2, BigDecimal longi3, BigDecimal latit3, 
            BigDecimal longi4, BigDecimal latit4, BigDecimal area, String community, String ward, String localGovernment, String stateLocated, 
            String crop, BigDecimal areaPlanted, String yearPlanted, BigDecimal harvest, BigDecimal netWorth, BigDecimal income, int farmerid);
    public String addAggregation(String town, String ward, String localGovernment, String stateLocated, int groupid, String aggregationLevel);
    public String addFarmInput(String farmInput, BigDecimal inputCost, String yearGiven, int farmerid);
    public String addLoan(String description, BigDecimal amountGiven, BigDecimal amountPaid, int farmerid);   
    public String addUserAccounts(String username, String password, String phonenum, String userrole);
    public String addUserTrails(String logSummary, String logDetail, String phonenum);
    public String groupNumList();
    public String officerNumList(Integer groupId);
    public String famerNumList(Integer groupId);
    public String getCurrentUser(String phonenum, String password);
    public String getLoggedDetails(final String phonenum);
    public String getTotalProduction();
    public String getActualProduction();
    public String getExpectedIncome();
    public String getActualIncome();
    public String getTotalInputSupplied();
    public String getTatalLoanGranted();
    public String getTatalLoanRecovered();
    public String aggregationNumList(Integer groupId);
    public String farmNumList(Integer farmerId);
    public String farmInputNumList(Integer farmerId);
    public String loanNumList(Integer farmerId);
    public String loanDetails(Integer loanid);
    public boolean updateLoan(BigDecimal currentSum, Integer loanId);
    public String farmFinpLoanGRPNumList(Integer groupId, String tableref);
}
