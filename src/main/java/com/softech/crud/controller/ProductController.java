package com.softech.crud.controller;

import com.softech.crud.model.Product;
import com.softech.crud.service.ProductService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.net.URI;
import java.util.HashMap;


// TODO: Implement custom authentication and authorization next time.


@Path("/api/v1/products")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Product API", version = "1.0.0"), tags = @Tag(name = "Product", description = "All the product methods"))
public class ProductController {

    private final ProductService productService;

    @GET
    @Path("/")
    public Response getAllProducts(@Context HttpHeaders headers,@Context SecurityContext securityContext) {
        var isBearer = headers.getHeaderString("Authorization").startsWith("Bearer ");
        securityContext.getUserPrincipal().getName();
        if (!isBearer) {
            var body = new HashMap<String, String>();
            body.put("message", "Unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity(body).build();
        }
        var token  = headers.getHeaderString("Authorization").substring("Bearer ".length()).trim();
        var tokenIdEmpty = token.isEmpty() || token.isBlank();
        if (tokenIdEmpty) {
            var body = new HashMap<String, String>();
            body.put("message", "Unauthorized");
            return Response.status(Response.Status.UNAUTHORIZED).entity(body).build();
        }
        System.out.println(token);
        try {
            var products = productService.getAllProducts();
            if (products.isEmpty()) {
                var body = new HashMap<String, String>();
                body.put("message", "No products found");
                return Response.status(Response.Status.NOT_FOUND).entity(body).build();
            }
            return Response.ok(products).build();
        } catch (Exception e) {
            var body = new HashMap<String, String>();
            body.put("message", "Internal server error");
            return Response.serverError().entity(body).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(Long id) {
        try {
            var product = productService.findById(id);
            if (product.isEmpty()) {
                var body = new HashMap<String, String>();
                body.put("message", "Product not found");
                return Response.status(Response.Status.NOT_FOUND).entity(body).build();
            }
            return Response.ok().entity(product).build();
        } catch (Exception e) {
            var body = new HashMap<String, String>();
            body.put("message", "Internal server error");
            return Response.serverError().entity(body).build();
        }
    }

    @POST
    @Path("/")
    public Response createProduct(Product product) {
        try {
            return Response.created(URI.create("/api/v1/products")).entity(productService.createProduct(product)).build();
        } catch (Exception e) {
            var body = new HashMap<String, String>();
            body.put("message", "Internal server error");
            return Response.serverError().entity(body).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        try {
            var dbProduct = productService.findById(id);
            if (dbProduct.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            var productUpdate = productService.updateProduct(product);
            return Response.accepted().entity(productUpdate).build();
        } catch (Exception e) {
            var body = new HashMap<String, String>();
            body.put("message", "Internal server error");
            return Response.serverError().entity(body).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            var product = productService.findById(id);
            if (product.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.accepted().entity(productService.deleteProduct(id).orElseThrow()).build();
        } catch (Exception e) {
            var body = new HashMap<String, String>();
            body.put("message", "Internal server error");
            return Response.serverError().entity(body).build();
        }
    }


}
