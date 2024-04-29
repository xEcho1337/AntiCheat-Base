package net.echo.anticheatbase.check.model.info;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInfo {

    String name();

    String description() default "";

    Category category() default Category.NONE;
}
