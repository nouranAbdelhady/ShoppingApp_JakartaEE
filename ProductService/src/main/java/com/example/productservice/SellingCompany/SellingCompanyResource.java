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
    @Path("/{sellingCompanyId}/products")
    public void addProductToSellingCompany(@PathParam("sellingCompanyId") int sellingCompanyId, Product product) {
        sellingCompanyService.addProductToSellingCompany(sellingCompanyService.getSellingCompanyById(sellingCompanyId), product);
        // Add the product to database
        product.setSellingCompany(sellingCompanyService.getSellingCompanyById(sellingCompanyId));
        productService.addProduct(product);
    }

    @GET
    @Path("/{sellingCompanyName}/products")
    public List<Product> getProductsBySellingCompany(@PathParam("sellingCompanyName") String sellingCompanyName) {
        return sellingCompanyService.getProductsBySellingCompany(sellingCompanyName);
    }

    @DELETE
    @Path("/{sellingCompanyId}/products/{productId}")
    public void deleteProductFromSellingCompany(@PathParam("sellingCompanyId") int sellingCompanyId, @PathParam("productId") int productId) {
        sellingCompanyService.deleteProductFromSellingCompany(sellingCompanyId, productId);
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
}
