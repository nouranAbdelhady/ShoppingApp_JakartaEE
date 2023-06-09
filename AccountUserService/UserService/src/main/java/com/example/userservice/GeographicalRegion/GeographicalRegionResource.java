package com.example.userservice.GeographicalRegion;

import com.example.userservice.User.Userr;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/regions")     //http://localhost:16957/UserService-1.0-SNAPSHOT/api/regions
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GeographicalRegionResource {

    @EJB
    private GeographicalRegionService geographicalRegionService;

    @GET
    public List<GeographicalRegion> getAllRegions() {
        return geographicalRegionService.getAllRegions();
    }

    @GET
    @Path("/{id}")
    public GeographicalRegion getRegionById(@PathParam("id") int id) {
        return geographicalRegionService.getRegionById(id);
    }

    @GET
    @Path("/name/{name}")
    public GeographicalRegion getRegionByName(@PathParam("name") String name) {
        return geographicalRegionService.getRegionByName(name);
    }

    @POST
    public void addRegion(GeographicalRegion region) {
        geographicalRegionService.addRegion(region);
    }

    @PUT
    @Path("/{id}")
    public void updateRegion(@PathParam("id") int id, GeographicalRegion region) {
        region.setId(id);
        geographicalRegionService.updateRegion(region);
    }

    @DELETE
    @Path("/{id}")
    public void deleteRegion(@PathParam("id") int id) {
        geographicalRegionService.deleteRegion(id);
    }


    @GET
    @Path("/{id}/customers")
    public List<Userr> getAllCustomersForRegion(@PathParam("id") int id) {
        GeographicalRegion region = geographicalRegionService.getRegionById(id);
        return geographicalRegionService.getAllCustomersForRegion(region);
    }
}
