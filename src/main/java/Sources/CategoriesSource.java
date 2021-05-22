package Sources;

import DataTransferObjects.CategoryDTO;
import Filters.CatalogsFilter;
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
public class CategoriesSource extends MorphiaSource<Category, CatalogsFilter, CategoryDTO>{
    public CategoriesSource(Datastore datastore) {
        super(datastore, Category.class);
    }
    
    @Override
    public Category create(CategoryDTO categoryDTO) {
        // Create owner document
        // Attention! Owner of category may be not equeal owner of catalog!
        Owner owner = new Owner(
                categoryDTO.getOwner().getId(),
                categoryDTO.getOwner().getUsername()
        );
        // Create CatalogCategory document
        CatalogCategory catalogCategory = new CatalogCategory(
                categoryDTO.getCatalog().getId(),
                categoryDTO.getCatalog().getTitle(),
                categoryDTO.getCatalog().getOwner()
        ); 
        // Create catalogs list
        List<CatalogCategory> catalogs = Arrays.asList(catalogCategory);
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
                catalogs,
                breadcrumbs,
                owner
        );
        // Save document in database
        save(category);
        // Return saved document
        return category;
    }
}
