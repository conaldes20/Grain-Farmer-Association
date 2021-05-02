/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.websocket;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author CONALDES
 */
public class UtilFuncs {
/*    
    public static boolean CheckInteger(String inputtxt){
        int strln = inputtxt.length();
        boolean invalidChar_seen = false;
        for (int i = 0; i < strln; i++) {
            if ((inputtxt.charAt(i) < '0') || (inputtxt.charAt(i) > '9')){
                invalidChar_seen = true;
                break;
            }
        }
        if (invalidChar_seen) {
            return false;
        } else {
            return true;   
        }
    }
        
    public static boolean CheckDecimal(String inputtxt){ 
        int strln = inputtxt.length();
        boolean invalid_decimal = false;
        int dotpt = inputtxt.indexOf(".", 0); 
        int k = 0;
        for (int i = 0; i < strln; i++) {
            if (inputtxt.charAt(i) == '.'){
                k++;
            }
        }
        for (int i = 0; i < strln; i++) {
            char a = inputtxt.charAt(i);
            if (!Character.isDigit(a)){
                invalid_decimal = true;
                break;
            }
        }
        if (!invalid_decimal && (dotpt < (strln - 1))) {
            return true;
        } else {
            return true;   
        }
    }   
    */
    
    public static String capitalize(String str){
        str = str.trim();
        char a = str.charAt(0);
        a = Character.toUpperCase(a);
        return a + str.substring(1).toLowerCase();
    }

    public static String capitaliseFirstLetter(String str) {
        String[] strarray = str.split(" ");
        String new_str = "";
        int arrln  =  strarray.length;
        for (int i = 0; i < arrln; i++) {
            str = str.trim();
            char a = strarray[i].charAt(0);
            a = Character.toUpperCase(a);
            strarray[i] = a + strarray[i].substring(1).toLowerCase();
            new_str += strarray[i] + " ";
        }
        return new_str.trim();
    }
    
    public static boolean isNIMValid(String str) {
        boolean flag = false;
        int strln  =  str.length();
        str = str.trim();
        boolean invalid_char_ssen = false;
        for (int i = 0; i < strln; i++) {            
            char a = str.charAt(i);
            if (!Character.isDigit(a)){
                invalid_char_ssen = true;
                break;
            }
        }
        if ((strln < 11) || (strln > 11) || invalid_char_ssen) {
           flag = false;
        } else if (strln == 11 && !invalid_char_ssen) {
            flag = true;
        }
        return flag;
    }
    
    public static boolean isBVNValid(String str) {
        boolean flag = false;
        int strln  =  str.length();
        str = str.trim();
        boolean invalid_char_ssen = false;
        for (int i = 0; i < strln; i++) {            
            char a = str.charAt(i);
            if (!Character.isDigit(a)){
                invalid_char_ssen = true;
                break;
            }
        }
        if ((strln < 11) || (strln > 11) || invalid_char_ssen) {
           flag = false;
        } else if (strln == 11 && !invalid_char_ssen) {
            flag = true;
        }
        return flag;
    }
    
    public static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }    
        
    public static boolean isValidGSMNumber(String str) { 
        //  0xxx xxx xxxx, +234904614000, 09034333433   
        int strln = str.length();        
        boolean valid_gsm_num = false;
               
        if (str.charAt(0) == '0' && strln == 13) {        
            int blk = 0;
            for (int i = 0; i < strln; i++) {            
                char a = str.charAt(i);
                if (a == ' '){
                    blk++;
                }
            }
            if (blk == 2) {                
                int ch4 = str.charAt(4);
                int ch8 = str.charAt(8);
                int k = 0;
                for (int i = 0; i < strln; i++) {            
                    char a = str.charAt(i);
                    if (Character.isDigit(a)){
                        k++;
                    }
                }
                if ((ch4 == ' ') && (ch8 == ' ') && (k == 11)) {
                    valid_gsm_num = true;
                }
            } 
        } else if (str.charAt(0) == '0' && strln == 11) {   
            int k = 0;
            for (int i = 0; i < strln; i++) {            
                char a = str.charAt(i);
                if (Character.isDigit(a)){
                    k++;
                }
            }
            if (k == 11) {
                valid_gsm_num = true;
            }
        } else if (str.charAt(0) == '+') {        
            String part1 = str.substring(0, 4);
            String part2 = str.substring(4);
            int part2ln = part2.length();
            int k = 0;
            for (int i = 0; i < part2ln; i++) {            
                char a = part2.charAt(i);
                if (Character.isDigit(a)){
                    k++;
                }
            }
            if ((part1.equals("+234")) && (k == 10)) {
                valid_gsm_num = true;
            }
        } 
        return valid_gsm_num; 
    } 

    public static boolean isNumeric(String numstr){
        boolean numeric = true;
        numeric = numstr.matches("-?\\d+(\\.\\d+)?");
        if (numeric){
            return true;
        }else{
            return false;       
        }        
    }    
}
