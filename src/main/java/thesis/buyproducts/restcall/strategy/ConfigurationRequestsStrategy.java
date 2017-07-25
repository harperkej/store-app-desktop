package thesis.buyproducts.restcall.strategy;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import thesis.buyproducts.restcall.model.ExceptionDetails;
import thesis.buyproducts.restcall.model.PointMappingDto;
import thesis.buyproducts.restcall.model.ValidationErrorDto;

public class ConfigurationRequestsStrategy {

	private static final String BASIC_REQUEST = "http://localhost:8080";

	public static Map<Integer, Object> updatePointMapping(PointMappingDto pointMappingDto) {

		Map<Integer, Object> res = new HashMap<>();
		ClientConfig clientConfiguration = new DefaultClientConfig();
		clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

		String updatePointMappingUrl = "/api/pointmapper";
		Client client = Client.create(clientConfiguration);

		WebResource webResource = client.resource(BASIC_REQUEST + updatePointMappingUrl);

		try {

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).put(ClientResponse.class,
					pointMappingDto);
			if (response.getStatus() == 200) {
				res.put(response.getStatus(), response.getEntity(Boolean.class));
				return res;
			}
			// CustomerDto has typed bad data()
			else if (response.getStatus() == 409) {
				res.put(response.getStatus(), response.getEntity(ValidationErrorDto.class));
				return res;
			}
		} catch (Exception e) {
			ExceptionDetails errorConnectionToServer = new ExceptionDetails();
			errorConnectionToServer.setMessage("Error conecting to server. Server is not available.");
			errorConnectionToServer.setTimestamp(new Timestamp(System.currentTimeMillis()));
			res.put(500, errorConnectionToServer);
			return res;
		}
		return null;
	}

	public static Map<Integer, Object> getPointMapping() {
		Map<Integer, Object> res = new HashMap<>();
		ClientConfig clientConfiguration = new DefaultClientConfig();
		clientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		String updatePointMappingUrl = "/api/pointmapper";
		Client client = Client.create(clientConfiguration);
		WebResource webResource = client.resource(BASIC_REQUEST + updatePointMappingUrl);
		try {

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			res.put(response.getStatus(), response.getEntity(Double.class));
			return res;
		} catch (Exception e) {
			ExceptionDetails errorConnectionToServer = new ExceptionDetails();
			errorConnectionToServer.setMessage("Error conecting to server. Server is not available.");
			errorConnectionToServer.setTimestamp(new Timestamp(System.currentTimeMillis()));
			res.put(500, errorConnectionToServer);
			return res;
		}
	}
}
