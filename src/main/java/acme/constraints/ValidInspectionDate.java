
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InspectionDateValidator.class)

public @interface ValidInspectionDate {

	String message() default "The next inspection date must be after the maintenance moment.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
