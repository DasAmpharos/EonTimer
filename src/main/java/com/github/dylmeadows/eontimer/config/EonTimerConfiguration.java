package com.github.dylmeadows.eontimer.config;

import com.github.dylmeadows.eontimer.model.*;
import com.github.dylmeadows.eontimer.model.config.ActionConfigurationModel;
import com.github.dylmeadows.eontimer.model.config.TimerConfigurationModel;
import com.github.dylmeadows.eontimer.model.timer.CustomTimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen3TimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen4TimerModel;
import com.github.dylmeadows.eontimer.model.timer.Gen5TimerModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hildan.fxgson.FxGson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EonTimerConfiguration {

    private final ApplicationContext context;
    private final ApplicationProperties properties;

    @PreDestroy
    public void destroy() throws IOException {
        Gson gson = context.getBean(Gson.class);
        ApplicationModel settings = context.getBean(ApplicationModel.class);
        // persist settings
        String json = gson.toJson(settings);
        File file = new File(properties.getName() + ".json");
        Files.write(file.toPath(), json.getBytes());
    }

    @Bean
    public ApplicationModel settings(@Qualifier("fxGson") Gson gson, ApplicationProperties properties) throws IOException {
        ApplicationModel settings;
        File file = new File(properties.getName() + ".json");
        if (file.exists()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            settings = gson.fromJson(new String(bytes), ApplicationModel.class);
        } else {
            settings = new ApplicationModel();
        }
        return settings;
    }

    @Bean
    public Gen3TimerModel gen3TimerModel(ApplicationModel settings) {
        return settings.getGen3();
    }

    @Bean
    public Gen4TimerModel gen4TimerModel(ApplicationModel settings) {
        return settings.getGen4();
    }

    @Bean
    public Gen5TimerModel gen5TimerModel(ApplicationModel settings) {
        return settings.getGen5();
    }

    @Bean
    public CustomTimerModel customTimerModel(ApplicationModel settings) {
        return settings.getCustom();
    }

    @Bean
    public ActionConfigurationModel actionSettingsModel(ApplicationModel settings) {
        return settings.getActionSettings();
    }

    @Bean
    public TimerConfigurationModel timerSettingsModel(ApplicationModel settings) {
        return settings.getTimerSettings();
    }

    @Bean
    public Gson fxGson(GsonBuilder builder) {
        return FxGson.addFxSupport(builder)
                .setPrettyPrinting()
                .create();
    }
}
