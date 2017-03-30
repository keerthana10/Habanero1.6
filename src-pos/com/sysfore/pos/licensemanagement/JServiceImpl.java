package com.sysfore.pos.licensemanagement;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;

/**
 *
 * @author      S.A. Mateen
 * @on          May 2, 2013 7:25:10 PM
 * @email	mateen.sa@sysfore.com
 */
public interface JServiceImpl {
 
    public ClientResponse returnActivateResponse(Form form);
    public ClientResponse returnValidateResponse(Form form);
    
}



/*


  ClientResponse response = test.returnActivateResponse(form);
            JAXBContext context = JAXBContext.newInstance(ClientEntity.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClientEntity obj = (ClientEntity) unmarshaller.unmarshal(new StringReader(response.getEntity(String.class)));
               
            if(response.getStatus() != 200 || obj.getErrors() != null ){


*/
