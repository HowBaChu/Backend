package com.HowBaChu.howbachu.core.factory;

import java.util.Objects;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class YamlLoadFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {

        YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean();
        ypfb.setResources(resource.getResource());

        return new PropertiesPropertySource(
            Objects.requireNonNull(resource.getResource().getFilename()),
            Objects.requireNonNull(ypfb.getObject()));
    }
}
