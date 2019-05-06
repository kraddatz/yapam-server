package me.raddatz.jwarden.common.annotation.profileexecution;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface VerifiedEmail {
}
