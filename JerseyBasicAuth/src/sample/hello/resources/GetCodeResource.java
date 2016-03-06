/**
 * 
 */
package sample.hello.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import sample.hello.bean.AppParamConfiguration;
import sample.hello.bean.GetCodeBase;

/**
 * @author huangpeng
 *
 */
@Path("/Code")
public class GetCodeResource {
	@POST
	@Path("/GetCodeInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response configTomcat(@QueryParam("codeID") String codeID)
			throws JSONException {
		Response res = null;
		JSONObject entity = new JSONObject();
		GetCodeBase a = new GetCodeBase();
		String result = a.getCodeInfo(codeID);
		entity.put("response", result);
		res = Response.ok(entity).build();
		return res;
	}
	

}
