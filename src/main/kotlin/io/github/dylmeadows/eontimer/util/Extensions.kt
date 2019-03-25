package io.github.dylmeadows.eontimer.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(javaClass)
