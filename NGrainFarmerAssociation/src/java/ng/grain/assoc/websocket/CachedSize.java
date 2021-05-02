/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.grain.assoc.websocket;

/**
 *
 * @author hp
 */
public class CachedSize {
    private int sessionMapSize;
    
    public CachedSize(){
        sessionMapSize = 0;
    }
        
    public void setObjSize(int sessionMapSize){
        this.sessionMapSize = sessionMapSize;
    }
    
    public int getObjSize(){ 
        return sessionMapSize;
    }
}
