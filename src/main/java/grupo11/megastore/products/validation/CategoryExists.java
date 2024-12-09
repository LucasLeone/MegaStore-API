package grupo11.megastore.products.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CategoryExistsValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface CategoryExists {
    String message() default "La categoría no existe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
