package wdsr.exercise3.client;

import java.util.List;
import java.util.Set;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.util.JSONPObject;

import wdsr.exercise3.model.Product;
import wdsr.exercise3.model.ProductType;

public class ProductService extends RestClientBase {
	protected ProductService(final String serverHost, final int serverPort, final Client client) {
		super(serverHost, serverPort, client);
	}
	
	@Context
	Application application;
	
	/**
	 * Looks up all products of given types known to the server.
	 * @param types Set of types to be looked up
	 * @return A list of found products - possibly empty, never null.
	 */
	public List<Product> retrieveProducts(Set<ProductType> types) {
		WebTarget target = baseTarget.path("products");
		
		List<Product> listOfProducts = target
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Product>>() {});
		
		return listOfProducts.stream().filter(x->types.contains(x.getType())).collect(Collectors.toList());	
	}
	
	/**
	 * Looks up all products known to the server.
	 * @return A list of all products - possibly empty, never null.
	 */
	public List<Product> retrieveAllProducts() {
		WebTarget target = baseTarget.path("products");
		
		List<Product> listOfProducts = target
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Product>>() {});
		
		return listOfProducts;
	}
	
	/**
	 * Looks up the product for given ID on the server.
	 * @param id Product ID assigned by the server
	 * @return Product if found
	 * @throws NotFoundException if no product found for the given ID.
	 */
	public Product retrieveProduct(int id) {
		WebTarget target = baseTarget.path("products/"+id);
		
		Product product = target
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<Product>() {});
		
		if(product==null){
			throw new NotFoundException();
		} 
		
		return product;
	}	
	
	/**
	 * Creates a new product on the server.
	 * @param product Product to be created. Must have null ID field.
	 * @return ID of the new product.
	 * @throws WebApplicationException if request to the server failed
	 */
	public int storeNewProduct(Product product) {
		if(product.getId()!=null){
			throw new WebApplicationException();
		}
		WebTarget target = baseTarget.path("products");
		/*Response response = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(product, MediaType.APPLICATION_FORM_URLENCODED));*/
		Product product1 = target
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(product, MediaType.APPLICATION_FORM_URLENCODED), Product.class);
		return product1.getId();
	}
	
	/**
	 * Updates the given product.
	 * @param product Product with updated values. Its ID must identify an existing resource.
	 * @throws NotFoundException if no product found for the given ID.
	 */
	public void updateProduct(Product product) {
		// TODO
	}

	
	/**
	 * Deletes the given product.
	 * @param product Product to be deleted. Its ID must identify an existing resource.
	 * @throws NotFoundException if no product found for the given ID.
	 */
	public void deleteProduct(Product product) {
		// TODO
	}
}
