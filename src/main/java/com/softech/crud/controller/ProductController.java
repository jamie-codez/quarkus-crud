package com.softech.crud.controller;

import com.softech.crud.model.Product;
import com.softech.crud.service.ProductService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jboss.logging.annotations.Pos;

import java.net.URI;
import java.util.List;


// TODO: Implement custom authentication and authorization next time.


@Path("/api/v1/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GET
    @Path("/")
    public Response getAllProducts() {
        try {
            var products = productService.getAllProducts();
            return Response.ok(products).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(Long id) {
        try{
            var product = productService.findById(id);
            if (product.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.accepted().build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/")
    public Response createProduct(Product product) {
        try{
            return Response.created(URI.create("")).entity(productService.createProduct(product)).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        try{
            var dbProduct = productService.findById(id);
            if (dbProduct.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            var productUpdate = productService.updateProduct(product);
            return Response.accepted().entity(productUpdate).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try{
            var product = productService.findById(id);
            if (product.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.accepted().entity(productService.deleteProduct(id).get()).build();
        }catch (Exception e){
            return Response.serverError().build();
        }
    }


}
