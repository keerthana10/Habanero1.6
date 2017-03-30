package com.sysfore.pos.licensemanagement;

import java.util.ResourceBundle;

/**
 *
 * @author      S.A. Mateen
 * @on          May 2, 2013 7:25:10 PM
 * @email	mateen.sa@sysfore.com
 */
public abstract class ResourceAbs {

    private ResourceBundle getResource(){
        return ResourceBundle.getBundle("com.sysfore.licensemanagement.service.Service");
    }
    
    public String getURI(){
        return getResource().getString("service.basepath");
    }
    
}