package synthwave.repositories.mongodb.v1;

import synthwave.dto.CategoryDTO;
import synthwave.filters.CategoriesFilter;
import synthwave.models.mongodb.embeddeds.EmbeddedBreadcrumb;
import synthwave.models.mongodb.embeddeds.EmbeddedCatalog;
import synthwave.models.mongodb.standalones.Category;
import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import platform.repositories.mongodb.orm.MorphiaRepository;
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
public class CategoriesRepository 
        extends MorphiaRepository<Category, CategoriesFilter, CategoryDTO>{
    
    /**
     * Constructor for datastore source of categories collection
     * @param datastore Morphia datastore
     */
    public CategoriesRepository(Datastore datastore) {
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
        EmbeddedOwner owner = new EmbeddedOwner(
                categoryDTO.getOwner().getId(),
                categoryDTO.getOwner().getUsername()
        );
        // Create CatalogCategory document
        EmbeddedCatalog catalog = new EmbeddedCatalog(
                categoryDTO.getCatalog().getId(),
                categoryDTO.getCatalog().getTitle(),
                categoryDTO.getCatalog().getOwner()
        ); 
        // Create empty breadcrumbs list
        List<EmbeddedBreadcrumb> breadcrumbs = Arrays.asList();
        // Check on exist in data transfer object on exist parent field.
        // If field exist - create BreadcrumbCategory document and add it to
        // breadcrumbs list. 
        // If field not exist - no do anything.
        if (categoryDTO.getParent() != null) {
            EmbeddedBreadcrumb breadcrumb = new EmbeddedBreadcrumb(
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

    /**
     * Method for get all categories by catalog id
     * @param filter filter object
     * @return list of all categories for catalog
     */
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
    
    /**
     * Handler for update document method 
     * @param categoryDTO category data transfer object
     * @param category category document
     * @return result of update
     */
    @Override
    protected boolean updateHandler(
            CategoryDTO categoryDTO, 
            Category category
    ) {
        boolean changed = false;
        var title = categoryDTO.getTitle();
        var description = categoryDTO.getDescription();
        var breadcrumbs = categoryDTO.getBradcrumbs();
        if (title != null && (category.getTitle() == null
                || !category.getTitle().equals(title))) {
            category.setTitle(title);
            changed = true;
        }
        if (description != null && (category.getDescription() == null
                || !category.getDescription().equals(description))) {
            category.setDescription(description);
            if (!changed) {
                changed = true;
            }
        }
        if (breadcrumbs != null) {
            category.setBreadcrumbs(breadcrumbs);
            changed = true;
        }
        return changed;
    }
}
