package com.odin.odin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuración de recursos estáticos para el Centro de Ayuda y archivos multimedia.
 * Mapea tanto los recursos del classpath (/static/videos/) como la carpeta videos_Odin
 * reubicada dentro del proyecto Odinpini.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        List<String> locations = new ArrayList<>();
        locations.add("classpath:/static/videos/");

        // Posibles ubicaciones de la carpeta reubicada videos_Odin
        String[] candidatePaths = {
            "videos_Odin",
            "Odin/videos_Odin",
            "../videos_Odin",
            "C:/Users/Juan/Desktop/Gestor-Documental-Odin-Pini/Odin/videos_Odin",
            "C:/Users/Juan/Desktop/Gestor-Documental-Odin-Pini/videos_Odin"
        };

        for (String candidate : candidatePaths) {
            File dir = new File(candidate);
            if (dir.exists() && dir.isDirectory()) {
                String uri = dir.toURI().toString();
                if (!uri.endsWith("/")) {
                    uri += "/";
                }
                if (!locations.contains(uri)) {
                    locations.add(uri);
                }
            }
        }

        String[] locArray = locations.toArray(new String[0]);

        registry.addResourceHandler("/videos/**")
                .addResourceLocations(locArray);

        registry.addResourceHandler("/videos_Odin/**")
                .addResourceLocations(locArray);
    }
}
