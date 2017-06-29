package api;

import handler.SubscriptionHandler;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.Subscription;

import common.webservice.model.SubscriberData;

@Path("/subscriptions")
public class SubscriberApi {

    public SubscriberApi() {

    }

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscribe(SubscriberData data) throws IllegalStateException, IOException {
        System.out.println("Persist new subscription...");

        if (!data.validate())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input model").build();

        if (SubscriptionHandler.subscribe(data.getSubscriptions()))
            return Response.ok().build();

        return Response.serverError().build();
    }

    @POST
    @Path("/unsubscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response unsubscribe(Subscription data) throws IllegalStateException, IOException {
        System.out.println("Remove subscription...");

        if (!data.validate())
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid input model").build();

        if (SubscriptionHandler.unsubscribe(data))
            return Response.ok().build();

        return Response.serverError().build();
    }
}
