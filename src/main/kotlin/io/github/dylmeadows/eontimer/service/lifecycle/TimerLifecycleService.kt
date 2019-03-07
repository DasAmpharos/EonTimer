package io.github.dylmeadows.eontimer.service.lifecycle

import io.github.dylmeadows.eontimer.model.TimerState
import org.springframework.stereotype.Component
import java.util.*

@Component
class TimerLifecycleService {
    private val map: MutableMap<EventType, MutableList<TimerLifecycleListener>> = HashMap()

    fun doOnInit(onInit: TimerLifecycleListener) {
        map.getOrPut(EventType.ON_INIT) { ArrayList() }
            .add(onInit)
    }

    fun doOnStart(onStart: TimerLifecycleListener) {
        map.getOrPut(EventType.ON_START) { ArrayList() }
            .add(onStart)
    }

    fun doOnUpdate(onUpdate: TimerLifecycleListener) {
        map.getOrPut(EventType.ON_UPDATE) { ArrayList() }
            .add(onUpdate)
    }

    fun doOnStop(onStop: TimerLifecycleListener) {
        map.getOrPut(EventType.ON_STOP) { ArrayList() }
            .add(onStop)
    }

    fun emitEvent(eventType: EventType, state: TimerState) {
        if (map.containsKey(eventType)) {
            map[eventType]!!
                .forEach {
                    it.invoke(state)
                }
        }
    }

    private fun <K, V> Map<K, V>.ifPresent(key: K, presentAction: (V) -> Unit) {
        if (containsKey(key)) {
            presentAction.invoke(get(key)!!)
        }
    }
}