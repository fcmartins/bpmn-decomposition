package api;

import handler.VariableHandler;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import common.webservice.model.RestResult;
import common.webservice.model.VariableData;

@Path("/variables")
public class VariablesApi {

    public VariablesApi() {

    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RestResult add(VariableData data) throws IllegalStateException, IOException {
        System.out.println("Persist new variable...");

        if (!data.validate())
            return new RestResult(true, "Invalid input model");

        if (VariableHandler.addNewVariableValue(data.getVariable()))
            return new RestResult(false, null);

        return new RestResult(true, "Internal error");
    }
}
