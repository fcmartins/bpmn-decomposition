package api;

import handler.ExecuteHandler;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import common.webservice.model.ExecutionData;

@Path("/executions")
public class ExecuteApi {

    public ExecuteApi() {

    }

    @POST
    @Path("/execute")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute(ExecutionData data) throws IllegalStateException, IOException {
        System.out.println("Preparing to execute WSN's function...");

        if (!data.validate())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input model").build();

        if (ExecuteHandler.execute(data.getExecution()))
            return Response.ok().build();

        return Response.serverError().build();
    }

}
