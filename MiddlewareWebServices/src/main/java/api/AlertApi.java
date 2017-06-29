package api;

import handler.AlertHandler;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Subscription;

import common.webservice.model.VariableData;

@Path("/alert")
public class AlertApi {

    public AlertApi() {

    }

    @POST
    @Path("/new-subscriber")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newSubscriber(Subscription data) throws IllegalStateException, IOException {
        System.out.println("Notified about new subscriber...");
        System.out.println(data);

        if (!data.validate())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input model").build();

        if (AlertHandler.processNewSubscriber(data))
            return Response.ok().build();

        return Response.serverError().build();
    }

    @POST
    @Path("/new-variable")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newVariable(VariableData data) throws IllegalStateException, IOException {
        System.out.println("Notified about new variable...");

        if (!data.validate())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input model").build();

        if (AlertHandler.processNewVariable(data))
            return Response.ok().build();

        return Response.serverError().build();
    }

    @GET
    @Path("/test")
    public Response newVariable() throws IllegalStateException, IOException {
        System.out.println("Received test call");
        return Response.ok().build();
    }
}
