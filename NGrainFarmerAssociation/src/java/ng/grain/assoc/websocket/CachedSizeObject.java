/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ng.grain.assoc.websocket;

/**
 *
 * @author user
 */
public class CachedSizeObject {
    
    private static CachedSize cachedSize;
    static {
        cachedSize = new CachedSize();
    }
    
    public static void cacheObjSize(int objSize){ 
        cachedSize.setObjSize(objSize);
    }
    
    public static int getObjectSize(){        
        return cachedSize.getObjSize();
    }
}
