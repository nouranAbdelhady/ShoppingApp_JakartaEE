package com.example.shippingservice.Shipping;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/shipping_company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShippingCompanyResource {
    @EJB
    private ShippingCompanyService shippingCompanyService;

    @GET
    @Path("/{name}")
    public ShippingCompany getShippingCompanyByName(@PathParam("name") String name) {
        return shippingCompanyService.getShippingCompanyByName(name);
    }

    @GET
    public List<ShippingCompany> getAllShippingCompanies() {
        return shippingCompanyService.getAllShippingCompanies();
    }

    @POST
    public ShippingCompany addShippingCompany(ShippingCompany shippingCompany) {
        shippingCompanyService.addShippingCompany(shippingCompany);
        return shippingCompany;
    }

    @GET
    @Path("{name}/regions")
    public List<String> getAllRegions(@PathParam("name") String name) {
        return shippingCompanyService.getGeographicalAreas(name);
    }

    @POST
    @Path("{name}/regions")
    @Consumes(MediaType.TEXT_PLAIN)
    public ShippingCompanyxRegion addRegion(@PathParam("name") String name, String region) {
        return shippingCompanyService.addRegionToShippingCompany(name, region);
    }

    @DELETE
    @Path("{name}/regions/{region}")
    public boolean removeRegion(@PathParam("name") String name, @PathParam("region") String region) {
        return shippingCompanyService.removeRegionFromShippingCompany(name, region);
    }

    @PUT
    @Path("{name}")
    public ShippingCompany updateShippingCompany(@PathParam("name") String name, ShippingCompany shippingCompany) {
        return shippingCompanyService.updateShippingCompany(name, shippingCompany);
    }

    @DELETE
    @Path("{name}")
    public boolean deleteShippingCompany(@PathParam("name") String name) {
        return shippingCompanyService.deleteShippingCompany(name);
    }

    @GET
    @Path("/regions/{region}")
    public List<ShippingCompany> getShippingCompanyByRegion(@PathParam("region") String region) {
        return shippingCompanyService.getCompaniesByRegion(region);
    }


}
