package com.sysfore.pos.licensemanagement;

import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

/**
 *
 * @author      S.A. Mateen
 * @on          May 2, 2013 7:25:10 PM
 * @email	mateen.sa@sysfore.com
 */
public class ServiceTester extends ResourceAbs implements JServiceImpl{
    
    private static JServiceImpl tester = null;
    private ClientConfig config = null;
    private Client client = null;
    private WebResource service = null;
    private String uri = "";
    
    private ServiceTester() {
        this.uri = uri;
        config = new DefaultClientConfig();
        client = Client.create(config);
        service = client.resource(getURI());
    }
    
    public static JServiceImpl getInstance(){
        if(tester == null)
            tester = (JServiceImpl) new ServiceTester();
        return tester;
    }
    
    @Override
    public ClientResponse returnActivateResponse(Form form){
        ClientResponse response2 = service.path("rest").path("todos").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
        return response2;
    }
    
    @Override
    public ClientResponse returnValidateResponse(Form form){
        ClientResponse response2 = service.path("rest").path("validate").type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
        return response2;
    }

}
