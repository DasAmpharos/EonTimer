package io.github.dylmeadows.eontimer.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application")
class AppProperties {
    lateinit var name: String
    lateinit var version: String
}