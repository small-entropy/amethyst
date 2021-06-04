package Sources;

import DataTransferObjects.CategoryDTO;
import Exceptions.DataException;
import Filters.CategoriesFilter;
import Models.Embeddeds.BreadcrumbCategory;
import Models.Embeddeds.CatalogCategory;
import Models.Standalones.Category;
import Models.Embeddeds.Owner;
import Sources.Core.MorphiaSource;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;
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

    public List<Category> findAllByCatalogId(CategoriesFilter filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes())
                .skip(filter.getSkip())
                .limit(filter.getLimit());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("catalog.id", filter.getCatalog())
                ))
                .iterator(findOptions)
                .toList();
    }

    public Category update(CategoryDTO categoryDTO, CategoriesFilter filter) 
            throws DataException {
        Category category = findOneByOwnerAndId(filter);
        var title = categoryDTO.getTitle();
        var description = categoryDTO.getDescription();
        var breadcrumbs = categoryDTO.getBradcrumbs();
        if (category != null) {
            if (title != null && (category.getTitle() == null || !category.getTitle().equals(title))) {
                category.setTitle(title);
            }
            if (description != null && (category.getDescription() == null || !category.getDescription().equals(description))) {
                category.setDescription(description);
            }
            if (breadcrumbs != null) {
                category.setBreadcrumbs(breadcrumbs);
            }
            save(category);
            return category;
        } else {
            Error error = new Error("Can not find category document");
            throw new DataException("NotFound", error);
        }
    }
        
    /**
     * 
     * @param filer
     * @return
     * @throws DataException 
     */
    public Category deactivated(CategoriesFilter filer) throws DataException {
        Category category = findOneByOwnerAndId(filer);
        if (category != null) {
            category.deactivate();
            save(category);
            return category;
        } else {
            Error error = new Error("Can not find category document");
            throw new DataException("NotFound", error);
        }
    }
}
