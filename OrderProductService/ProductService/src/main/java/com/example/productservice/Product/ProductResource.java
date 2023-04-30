package com.example.productservice.Product;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    @EJB
    private ProductService productService;

    @GET
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{productId}")
    public Product getProductById(@PathParam("productId") int id) {
        return productService.getProductById(id);
    }

    @POST
    public Product addProduct(Product product) {
        productService.addProduct(product);
        return product;
    }
    @PUT
    @Path("/{productId}")
    public Product updateProduct(@PathParam("productId") int id, Product product) {
        product.setId(id);
        productService.updateProduct(product);
        return product;
    }
    @GET
    @Path("/state/{state}")
    public List<Product> getProductsByState(@PathParam("state") String state) {
        return productService.getProductsByState(state);
    }
}
