package io.github.dylmeadows.eontimer.util

class Stack<T>(list: List<T>) {
    private val list: MutableList<T> = list.toMutableList()

    val size: Int = list.size
    val isEmpty: Boolean = list.isEmpty()

    fun push(value: T) {
        list.add(0, value)
    }

    fun peek(): T? = if (!isEmpty) list[0] else null

    fun pop(): T? = if (!isEmpty) list.removeAt(0) else null
}