/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.buzlogic.factory;

import ng.grain.assoc.buzlogicImpl.BusinessLogicImpl;
import ng.grain.assoc.intface.BusinessLogic;
import java.sql.Connection;

/**
 *
 * @author CONALDES
 */
public class BusinessLogicFactory {
    public static BusinessLogic create(){
	return new BusinessLogicImpl();
    } 
    
    public static BusinessLogic create(Connection conn){
	return new BusinessLogicImpl( conn );
    }
}
