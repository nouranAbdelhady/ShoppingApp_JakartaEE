package com.example.productservice.ShippingCompany;

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
        sellingCompanyService.updateSellingCompany(name,sellingCompany);
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
    @Path("/{sellingCompanyId}/products")
    public List<Product> getProductsBySellingCompany(@PathParam("sellingCompanyId") int sellingCompanyId) {
        return sellingCompanyService.getProductsBySellingCompany(sellingCompanyId);
    }

    @DELETE
    @Path("/{sellingCompanyId}/products/{productId}")
    public void deleteProductFromSellingCompany(@PathParam("sellingCompanyId") int sellingCompanyId, @PathParam("productId") int productId) {
        sellingCompanyService.deleteProductFromSellingCompany(sellingCompanyId, productId);
    }
}
