package thesis.buyproducts.restcall.strategy;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import thesis.buyproducts.restcall.model.BuyWithPointsDto;
import thesis.buyproducts.restcall.model.CustomerDto;
import thesis.buyproducts.restcall.model.ExceptionDetails;
import thesis.buyproducts.restcall.model.ValidationErrorDto;

import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UserRequestsStrategy {

    private static final String BASIC_REQUEST = "http://localhost:8080";

    public static Map<Integer, Object> registerUser(CustomerDto customerDto) {
        Map<Integer, Object> res = new HashMap<>();
        ClientConfig clientConfiguration = new DefaultClientConfig();
        clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        String registerCustomerUrl = "/api/customers";
        Client client = Client.create(clientConfiguration);
        WebResource webResource = client.resource(BASIC_REQUEST + registerCustomerUrl);
        try {
            ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, customerDto);
            if (response.getStatus() == 201) {
                res.put(response.getStatus(), response.getEntity(CustomerDto.class));
                return res;
            }
            // CustomerDto has typed bad data()
            else if (response.getStatus() == 409) {
                res.put(response.getStatus(), response.getEntity(ValidationErrorDto.class));
                return res;
            }
            // CustomerDto has type a username not unique
            else if (response.getStatus() == 400) {
                res.put(response.getStatus(), response.getEntity(ExceptionDetails.class));
                return res;
            } else {
                res.put(response.getStatus(), response.getEntity(ExceptionDetails.class));
                return res;
            }

        } catch (Exception e) {
            ExceptionDetails errorConnectionToServer = new ExceptionDetails();
            errorConnectionToServer.setMessage("Error conecting to server. Server is not available.");
            errorConnectionToServer.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.put(500, errorConnectionToServer);
            return res;
        }
    }

    public static Map<Integer, Object> findUserByUserNameRequest(String userName) {
        ClientConfig clientConfiguration = new DefaultClientConfig();
        clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        String registerCustomerUrl = "/api/customers/" + userName;
        Client client = Client.create(clientConfiguration);
        WebResource webResource = client.resource(BASIC_REQUEST + registerCustomerUrl + "/");
        try {
            Map<Integer, Object> res = new HashMap<>();
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                    .get(ClientResponse.class);
            if (response.getStatus() == 302) {
                CustomerDto foundCustomerDto = response.getEntity(CustomerDto.class);
                res.put(response.getStatus(), foundCustomerDto);
                return res;
            } else {
                res.put(404, response.getEntity(ExceptionDetails.class));
                return res;
            }
        } catch (Exception e) {
            Map<Integer, Object> res = new HashMap<>();
            ExceptionDetails execptionDetails = new ExceptionDetails();
            execptionDetails.setMessage("Error. Server might be temporar down");
            execptionDetails.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.put(new Integer(500), execptionDetails);
            System.out.println(e.getMessage());
            return res;
        }
    }

    public static Map<Integer, Object> updateUserRequest(CustomerDto customerDto) {
        ClientConfig clientConfiguration = new DefaultClientConfig();
        clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        String registerCustomerUrl = "/api/customers";
        Client client = Client.create(clientConfiguration);
        WebResource webResource = client.resource(BASIC_REQUEST + registerCustomerUrl);
        try {
            Map<Integer, Object> res = new HashMap<>();
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).entity(customerDto)
                    .type(MediaType.APPLICATION_JSON).put(ClientResponse.class);
            if (response.getStatus() == 200) {
                res.put(new Integer(200), response.getEntity(CustomerDto.class));
                return res;
            } else if (response.getStatus() == 400) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(400), exception);
                return res;
            } else if (response.getStatus() == 404) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(404), exception);
            } else if (response.getStatus() == 409) {
                res.put(response.getStatus(), response.getEntity(ValidationErrorDto.class));
                return res;
            }
        } catch (Exception e) {
            Map<Integer, Object> res = new HashMap<>();
            ExceptionDetails execptionDetails = new ExceptionDetails();
            execptionDetails.setMessage("Error. Server might be temporar down");
            execptionDetails.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.put(new Integer(500), execptionDetails);
            return res;
        }
        return null;
    }

    public static Map<Integer, Object> processPurchaseRequest(String userName, Double amount) {
        ClientConfig clientConfiguration = new DefaultClientConfig();
        clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        String registerCustomerUrl = "/api/customers/username/" + userName + "/points?amount="+amount;
        Client client = Client.create(clientConfiguration);
        WebResource webResource = client.resource(BASIC_REQUEST + registerCustomerUrl);
        try {
            Map<Integer, Object> res = new HashMap<Integer, Object>();
            webResource.queryParam("amount", String.valueOf(amount));
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);
            if (response.getStatus() == 200) {
                CustomerDto customerDtoState = response.getEntity(CustomerDto.class);
                res.put(new Integer(200), customerDtoState);
                return res;
            } else if (response.getStatus() == 409) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(409), exception);
                return res;
            } else {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(404), exception);
                return res;
            }
        } catch (Exception e) {
            Map<Integer, Object> res = new HashMap<>();
            ExceptionDetails execptionDetails = new ExceptionDetails();
            execptionDetails.setMessage("Error. Server might be temporar down");
            execptionDetails.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.put(new Integer(500), execptionDetails);
            return res;
        }
    }

    public static Map<Integer, Object> buyWithPointsRequest(String username, Double amount) {
        ClientConfig clientConfiguration = new DefaultClientConfig();
        clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        String registerCustomerUrl = "/api/customers/username/" + username + "/discount?amount=" + amount;
        Client client = Client.create(clientConfiguration);
        WebResource webResource = client.resource(BASIC_REQUEST + registerCustomerUrl);
        try {
            Map<Integer, Object> res = new HashMap<Integer, Object>();
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);
            if (response.getStatus() == 200) {
                BuyWithPointsDto userState = response.getEntity(BuyWithPointsDto.class);
                res.put(new Integer(200), userState);
                return res;
            } else if (response.getStatus() == 404) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(404), exception);
                return res;
            } else if (response.getStatus() == 409) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(409), exception);
                return res;
            } else if (response.getStatus() == 400) {
                ExceptionDetails exception = response.getEntity(ExceptionDetails.class);
                res.put(new Integer(409), exception);
                return res;
            }
        } catch (Exception e) {
            Map<Integer, Object> res = new HashMap<>();
            ExceptionDetails execptionDetails = new ExceptionDetails();
            execptionDetails.setMessage("Error. Server might be temporar down");
            execptionDetails.setTimestamp(new Timestamp(System.currentTimeMillis()));
            res.put(new Integer(500), execptionDetails);
            return res;
        }
        return null;
    }

}
