package com.elepy;

import com.elepy.concepts.Resource;

import java.math.BigDecimal;
import java.util.UUID;

public class BaseTest {
    public Resource validObject() {
        Resource resource = new Resource();

        resource.setId("id");
        resource.setMaxLen40("230428");
        resource.setMinLen20("My name is ryan and this is a string  with more than 20 chars");
        resource.setMinLen10MaxLen50("12345678910111213");
        resource.setNumberMax40(BigDecimal.valueOf(40));
        resource.setNumberMin20(BigDecimal.valueOf(20));
        resource.setNumberMin10Max50(BigDecimal.valueOf(15));
        resource.setUnique(UUID.randomUUID().toString());
        resource.setWysiwyg("wysiwyg");
        resource.setTextArea("textarea");
        resource.setTextField("textfield");

        resource.setRequired("required");

        resource.setNonEditable("nonEditable");

        return resource;
    }

}