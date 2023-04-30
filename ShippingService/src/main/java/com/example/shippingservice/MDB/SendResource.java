package com.example.shippingservice.MDB;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;

@Path("/send")      //http://localhost:8080/ShippingService-1.0-SNAPSHOT/api/send
@Consumes("text/plain")
public class SendResource {
    @EJB
    SendService test;

    @POST
    @Path("/customer")
    public String sendToCustomer(String body){
        // split body into senderUsername, response, and receiverUsername "-"
        String[] splitBody = body.split("-");
        String senderUsername = splitBody[0];
        String response = splitBody[1];
        String receiverUsername = splitBody[2];
        test.sendMessage(senderUsername, response, receiverUsername);
        return response;
    }

    @POST
    @Path("/shipping")
    public String sendToShipping(String body){
        String message = "Sending request to shipping company...";

        // split body into senderUsername, response, and receiverUsername "-"
        String[] splitBody = body.split("-");
        String senderUsername = splitBody[0];
        String response = splitBody[1];
        String receiverUsername = splitBody[2];

        test.sendMessage(senderUsername, response, receiverUsername);
        return message;
    }

    @POST
    @Path("/request_shipping")
    public void requestShipping(String body) throws InterruptedException {
        // split body into request and customerUsername ";"
        String[] splitBody = body.split(";");
        String request = splitBody[0];
        String customerUsername = splitBody[1];
        //System.out.println("request: " + request);
        //System.out.println("customerUsername: " + customerUsername);
        test.requestShipping(request, customerUsername);
        //return request;
    }

    @GET
    @Path("/review_request/{notification_id}/{response}")
    public void reviewRequest(@PathParam("notification_id") int notification_id, @PathParam("response") String response){
        test.updateShippingRequest(notification_id, response);
    }
}
