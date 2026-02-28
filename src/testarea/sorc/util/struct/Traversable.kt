package testarea.sorc.util.struct

interface Traversable<T> {
    fun each(cons: (T) -> Unit)
}