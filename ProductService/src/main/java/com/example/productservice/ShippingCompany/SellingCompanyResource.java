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

    @POST
    public SellingCompany addSellingCompany(SellingCompany sellingCompany) {
        sellingCompanyService.addSellingCompany(sellingCompany);
        return sellingCompany;
    }

    @PUT
    @Path("/{sellingCompanyId}")
    public SellingCompany updateSellingCompany(@PathParam("sellingCompanyId") int id, SellingCompany sellingCompany) {
        sellingCompany.setId(id);
        sellingCompanyService.updateSellingCompany(sellingCompany);
        return sellingCompany;
    }

    @DELETE
    @Path("/{sellingCompanyId}")
    public void deleteSellingCompany(@PathParam("sellingCompanyId") int id) {
        sellingCompanyService.deleteSellingCompany(sellingCompanyService.getSellingCompanyById(id));
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

    @PUT
    @Path("/login")
    public boolean login (SellingCompany sellingCompany) {
        return sellingCompanyService.login(sellingCompany.getName(), sellingCompany.getPassword());
    }

    @PUT
    @Path("/logout")
    public void logout (SellingCompany sellingCompany) {
        sellingCompanyService.logout(sellingCompany.getName());
    }

    @DELETE
    @Path("/{sellingCompanyId}/products/{productId}")
    public void deleteProductFromSellingCompany(@PathParam("sellingCompanyId") int sellingCompanyId, @PathParam("productId") int productId) {
        sellingCompanyService.deleteProductFromSellingCompany(sellingCompanyId, productId);
    }
}
