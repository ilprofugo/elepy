package com.elepy.annotations;


import com.elepy.routes.DefaultFindMany;
import com.elepy.routes.DefaultFindOne;
import com.elepy.routes.FindManyHandler;
import com.elepy.routes.FindOneHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to change the way Elepy handles finds.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Find {


    /**
     * The class that handles the functionality of finds on this Resource.
     *
     * @return the route findManyHandler
     * @see DefaultFindMany
     * @see FindManyHandler
     */
    Class<? extends FindManyHandler> findManyHandler() default DefaultFindMany.class;

    /**
     * The class that handles the functionality of finds on this Resource.
     *
     * @return the route findOneHandler
     * @see DefaultFindMany
     * @see FindManyHandler
     */
    Class<? extends FindOneHandler> findOneHandler() default DefaultFindOne.class;

    /**
     * A list of required permissions to execute this A
     */
    String[] requiredPermissions() default {};
}