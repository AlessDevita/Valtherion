package fr.alessdevita.valtherion.client.gui.inventory;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface SideOnly {
    Side value();
}
