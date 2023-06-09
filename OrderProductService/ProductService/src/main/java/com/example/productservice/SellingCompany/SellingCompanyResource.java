package com.example.productservice.SellingCompany;

import com.example.productservice.Product.Product;
import com.example.productservice.Product.ProductService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/selling_company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SellingCompanyResource {
    @EJB
    private SellingCompanyService sellingCompanyService;

    @EJB
    private ProductService productService;

    @GET
    public List<SellingCompany> getAllSellingCompanies() {
        return sellingCompanyService.getAllSellingCompanies();
    }

    @GET
    @Path("/{sellingCompanyId}")
    public SellingCompany getSellingCompanyById(@PathParam("sellingCompanyId") int id) {
        return sellingCompanyService.getSellingCompanyById(id);
    }

    @GET
    @Path("/name/{name}")
    public SellingCompany getSellingCompanyByName(@PathParam("name") String name) {
        return sellingCompanyService.getSellingCompanyByName(name);
    }

    @POST
    public SellingCompany addSellingCompany(SellingCompany sellingCompany) {
        sellingCompanyService.addSellingCompany(sellingCompany);
        return sellingCompany;
    }

    @PUT
    @Path("/{name}")
    public SellingCompany updateSellingCompany(@PathParam("name") String name, SellingCompany sellingCompany) {
        sellingCompanyService.updateSellingCompany(name, sellingCompany);
        return sellingCompany;
    }

    @DELETE
    @Path("/{name}")
    public void deleteSellingCompany(@PathParam("name") String name) {
        sellingCompanyService.deleteSellingCompany(sellingCompanyService.getSellingCompanyByName(name));
    }

    @POST
    @Path("/{sellingCompanyName}/products")
    public void addProductToSellingCompany(@PathParam("sellingCompanyName") String sellingCompanyName, Product product) {
        sellingCompanyService.addProductToSellingCompany(sellingCompanyService.getSellingCompanyByName(sellingCompanyName), product);

    }

    @GET
    @Path("/{sellingCompanyName}/products")
    public List<Product> getProductsBySellingCompany(@PathParam("sellingCompanyName") String sellingCompanyName) {
        return sellingCompanyService.getProductsBySellingCompany(sellingCompanyName);
    }

    @DELETE
    @Path("/{sellingCompanyName}/products/{productId}")
    public void deleteProductFromSellingCompany(@PathParam("sellingCompanyName") String sellingCompanyName, @PathParam("productId") int productId) {
        sellingCompanyService.deleteProductFromSellingCompany(sellingCompanyName, productId);
    }

    @GET
    @Path("/{sellingCompanyName}/products/state/{state}")
    public List<Product> getProductsBySellingCompanyAndState(@PathParam("sellingCompanyName") String sellingCompanyName, @PathParam("state") String state) {
        return sellingCompanyService.getProductsBySellingCompanyAndState(sellingCompanyName, state);
    }

    // RepresentiveName
    @GET
    @Path("representative_name")
    public List<RepresentativeName> getAllRepresentativeNames() {
        return sellingCompanyService.getAllRepresentativeNames();
    }

    @GET
    @Path("/products/productId/{productId}")
    public SellingCompany getSellingCompanyByProductId(@PathParam("productId") int productId) {
        return sellingCompanyService.getSellingCompanyByProductId(productId);
    }

    @POST
    @Path("representative_name")
    public void addRepresentativeName(RepresentativeName representativeName) {
        sellingCompanyService.addRepresentativeName(representativeName);
    }

    @GET
    @Path("representative_name/assigned")
    public List<RepresentativeName> getAllAssignedRepresentativeNames() {
        return sellingCompanyService.getAssignedRepresentativeNames();
    }

    @GET
    @Path("representative_name/unassigned")
    public List<RepresentativeName> getAllUnassignedRepresentativeNames() {
        return sellingCompanyService.getUnassignedRepresentativeNames();
    }

    @GET
    @Path("/notificationId/{sellerUsername}")
    public List<List<String>> getNotificationId(@PathParam("sellerUsername") String sellerUsername){
        return sellingCompanyService.getNotification(sellerUsername);
    }

    @GET
    @Path("/updateOrderState/{sellerusername}/{sellingRequestId}/{response}")
    public void updateSellingRequest(@PathParam("sellerusername") String sellerUsername, @PathParam("sellingRequestId") int sellingRequestId,@PathParam("response") String response) {
         sellingCompanyService.updateSellingRequest(sellerUsername,sellingRequestId,response);
    }
}
