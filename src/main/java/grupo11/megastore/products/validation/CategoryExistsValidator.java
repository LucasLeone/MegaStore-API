package grupo11.megastore.products.validation;

import grupo11.megastore.products.model.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryExistsValidator implements ConstraintValidator<CategoryExists, Long> {

    private final CategoryRepository categoryRepository;

    public CategoryExistsValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isValid(Long categoryId, ConstraintValidatorContext context) {
        if (categoryId == null) {
            return true;
        }
        return categoryRepository.existsById(categoryId);
    }
}
