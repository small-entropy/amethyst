package Sources;

import DataTransferObjects.CategoryDTO;
import Filters.CategoriesFilter;
import Models.BreadcrumbCategory;
import Models.CatalogCategory;
import Models.Category;
import Models.Owner;
import Sources.Core.MorphiaSource;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;

/**
 * Class datasource for categories collection
 * @author small-entropy
 */
public class CategoriesSource extends MorphiaSource<Category, CategoriesFilter, CategoryDTO>{
    
    /**
     * Constructor for datastore source of categories collection
     * @param datastore Morphia datastore
     */
    public CategoriesSource(Datastore datastore) {
        super(datastore, Category.class);
    }
    
    /**
     * Method for create category document
     * @param categoryDTO category data transfer object
     * @return created category document
     */
    @Override
    public Category create(CategoryDTO categoryDTO) {
        // Create owner document
        // Attention! Owner of category may be not equeal owner of catalog!
        Owner owner = new Owner(
                categoryDTO.getOwner().getId(),
                categoryDTO.getOwner().getUsername()
        );
        // Create CatalogCategory document
        CatalogCategory catalog = new CatalogCategory(
                categoryDTO.getCatalog().getId(),
                categoryDTO.getCatalog().getTitle(),
                categoryDTO.getCatalog().getOwner()
        ); 
        // Create empty breadcrumbs list
        List<BreadcrumbCategory> breadcrumbs = Arrays.asList();
        // Check on exist in data transfer object on exist parent field.
        // If field exist - create BreadcrumbCategory document and add it to
        // breadcrumbs list. 
        // If field not exist - no do anything.
        if (categoryDTO.getParent() != null) {
            BreadcrumbCategory breadcrumb = new BreadcrumbCategory(
                    categoryDTO.getParent().getId(),
                    categoryDTO.getParent().getTitle()
            );
            breadcrumbs.add(breadcrumb);
        }
        // Create category document
        Category category = new Category(
                categoryDTO.getName(),
                categoryDTO.getTitle(),
                categoryDTO.getDescription(),
                catalog,
                breadcrumbs,
                owner
        );
        // Save document in database
        save(category);
        // Return saved document
        return category;
    }
}
