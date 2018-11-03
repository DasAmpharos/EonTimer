package com.github.dylmeadows.eontimer.config;

import com.github.dylmeadows.eontimer.model.*;
import com.github.dylmeadows.eontimer.model.CustomTimerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hildan.fxgson.FxGson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
public class EonTimerConfiguration {

    private final ApplicationContext context;

    @Autowired
    public EonTimerConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @PreDestroy
    public void destroy() throws IOException {
        Gson gson = context.getBean(Gson.class);
        ApplicationSettings settings = context.getBean(ApplicationSettings.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        // persist settings
        String json = gson.toJson(settings);
        File file = new File(properties.getName() + ".json");
        Files.write(file.toPath(), json.getBytes());
    }

    @Bean
    public ApplicationSettings settings(@Qualifier("fxGson") Gson gson, ApplicationProperties properties) throws IOException {
        ApplicationSettings settings;
        File file = new File(properties.getName() + ".json");
        if (file.exists()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            settings = gson.fromJson(new String(bytes), ApplicationSettings.class);
        } else {
            settings = new ApplicationSettings();
        }
        return settings;
    }

    @Bean
    public Gen3TimerModel gen3TimerModel(ApplicationSettings settings) {
        return settings.getGen3();
    }

    @Bean
    public Gen4TimerModel gen4TimerModel(ApplicationSettings settings) {
        return settings.getGen4();
    }

    @Bean
    public Gen5TimerModel gen5TimerModel(ApplicationSettings settings) {
        return settings.getGen5();
    }

    @Bean
    public CustomTimerModel customTimerModel(ApplicationSettings settings) {
        return settings.getCustom();
    }

    @Bean
    public ActionSettingsModel actionSettingsModel(ApplicationSettings settings) {
        return settings.getActionSettings();
    }

    @Bean
    public TimerSettingsModel timerSettingsModel(ApplicationSettings settings) {
        return settings.getTimerSettings();
    }

    @Bean
    public ThemeSettingsModel themeSettingsModel(ApplicationSettings settings) {
        return settings.getThemeSettings();
    }

    @Bean
    public Gson fxGson(GsonBuilder builder) {
        return FxGson.addFxSupport(builder)
                .setPrettyPrinting()
                .create();
    }
}
