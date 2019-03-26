package io.github.dylmeadows.eontimer.service.factory

interface TimerFactory {
    fun createTimer(): List<Long>
}