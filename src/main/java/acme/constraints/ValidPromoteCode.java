
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PromoteCodeValidator.class)
@Target({
	ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPromoteCode {

	String message() default "{acme.validation.text.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
