package com.sweethome.valid.annotation;

import com.sweethome.valid.ListValueConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        //可以指定多个不同的校验器，适配不同类型的校验
        validatedBy = {ListValueConstraintValidator.class}
)
public @interface ListValue {
    String message() default "{com.sweethome.valid.annotation.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};
}
