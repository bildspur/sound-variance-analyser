package ch.bildspur.event

/**
 * Created by cansik on 02.10.16.
 */
class Event<T> {
    private val handlers = arrayListOf<(Event<T>.(T) -> Unit)>()
    fun plusAssign(handler: Event<T>.(T) -> Unit) { handlers.add(handler) }
    fun invoke(value: T) { for (handler in handlers) handler(value) }
}