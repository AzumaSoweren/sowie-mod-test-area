package testarea.sorc.load

import kotlin.properties.ReadOnlyProperty

class InitManager {
    private val delegates = mutableListOf<() -> Unit>()

    fun <T> register(initializer: () -> T): ReadOnlyProperty<Any?, T> {
        var value: T? = null
        delegates.add { value = initializer() }

        return ReadOnlyProperty<Any?, T> { _, _ -> value ?: throw IllegalStateException("Property not initialized.") }
    }

    fun initAll() {
        for (delegate in delegates) delegate()
    }
}