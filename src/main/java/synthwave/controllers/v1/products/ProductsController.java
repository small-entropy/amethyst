package synthwave.controllers.v1.products;

import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 * Class controller for work with products collection
 * @author small-entropy
 * @version 1
 */
public class ProductsController 
	extends BaseController<Object, JsonTransformer> {
	
	/**
	 * Default constructor for product controller. Create
	 * controller instance by datastore & transformer
	 * @param datatore Morphia datastore object
	 * @param transformer response transformer
	 */
	public ProductsController (
			Datastore datatore,
			JsonTransformer transformer
	) {
		super(
				null,
				transformer,
				DefaultRights.PRODUCTS.getName()
		);
	}
 
	/**
	 * Method for get list of products
	 */
	protected void getProductsListRoute() {
		get("", (req, res) -> "Get products list");
	}
	
	/**
	 * Method for get product entity by id
	 */
	protected void getProductByIdRoute() {
		get("/:product_id", (req, res) -> "Get product document");
	}
	
	/**
	 * Method for get products list by catalog id
	 */
	protected void getProductsLitByCatalogIdRoute() {
		get("/catalog/:catalog_id", (req, res) -> "Get catelog products");
	}
	
	/**
	 * Method for get products list by category id
	 */
	protected void getProductListByCategoryIdRoute() {
		get("/owner/:user_id/categories/:category_id", (req, res) -> "");		
	}
	
	/**
	 * Method for get products list by owner id & category id
	 */
	protected void getProductsListByOwnerIdAndCategoryIdRoute() {
		get("/categories/:category_id", (req, res) -> "Get product for category");
	}
	
	/**
	 * Method for get products list by owner id
	 */
	protected void getProductListByOwnerIdRoute() {
		get("/owner/:user_id", (req, res) -> "Get user products");
	}
	
	/**
	 * Method for get product entity by owner id & product id
	 */
	protected void getProductByOwnerIdAndIdRoute() {
		get("/owner/:user_id/product/:product_id", (req, res) -> "Get product document");
	}
	
	/**
	 * Method for update product entity
	 */
	protected void updateProductRoute() {
		put("/owner/:user_id/product/:product_id", (req, res) -> "Update product document");
	}
	
	/**
	 * Method for deactivate product entity
	 */
	protected void deleteProductRoute() {
		delete("/owner/:user_id/product/:product_id", (req, res) -> "Delete product document");
	}
	
	/*
	 * Method for get products list by owner id & catalog id
	 */
	protected void getProductsListByOwnerIdAndCatalogIdRoute() {
		get("/owner/:user_id/catalog/:catalog_id", (req, res) -> "Get user products for catelog");
	}
	
	/**
	 * Method for create product entity
	 */
	protected void createProductRoute() {
		post("/owner/:user_id/catalog/:catalog_id", (req, res) -> "Create product for catalog");
	}
	
	/**
	 * Method for register routes for products data
	 */
	@Override
    public void register() {
		createProductRoute();
		getProductsListRoute();
		getProductByIdRoute();
		getProductsLitByCatalogIdRoute();
		getProductListByCategoryIdRoute();
		getProductsListByOwnerIdAndCatalogIdRoute();
		getProductsListByOwnerIdAndCategoryIdRoute();
		getProductListByOwnerIdRoute();
		getProductByOwnerIdAndIdRoute();
		updateProductRoute();
		deleteProductRoute();
    }
}
