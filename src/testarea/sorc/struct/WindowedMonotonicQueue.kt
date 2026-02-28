package testarea.sorc.struct

import testarea.sorc.util.struct.Traversable
import java.util.TreeSet

class WindowedMonotonicQueue<T>(
    private val maxSize: Int,
    comparator: Comparator<T>,
    private val loadFactor: Float = 0.75f
) : Iterable<T>, Traversable<T> {
    init {
        require(maxSize > 0) { "Max size must be greater than 0." }
        require(loadFactor in 0.1f..0.9f) { "Load factor must be between 0.1 to 0.9." }
    }

    constructor(maxSize: Int, comparator: (T, T) -> Int) : this(maxSize, Comparator(comparator))

    companion object {
        fun <T> priorityWindowSet(
            maxSize: Int,
            comparator: (T, T) -> Int
        ) = WindowedMonotonicQueue(maxSize, Comparator(comparator))

        fun <T : Comparable<T>> priorityWindowSet(
            maxSize: Int
        ) = WindowedMonotonicQueue(maxSize, naturalOrder<T>())
    }

    private val prioritySet = TreeSet(comparator)
    private val elemMap = HashMap<T, Unit>(initCapacity(), loadFactor)

    val size: Int
        get() = prioritySet.size

    val empty: Boolean
        get() = prioritySet.isEmpty()

    val full: Boolean
        get() = size >= maxSize

    fun add(element: T): WindowedMonotonicQueue<T> {
        if (element in elemMap) return this

        if (full) {
            prioritySet.firstOrNull()?.let {
                prioritySet.remove(it)
                elemMap.remove(it)
            }
        }

        prioritySet.add(element)
        elemMap[element] = Unit

        return this
    }

    fun remove(element: T): WindowedMonotonicQueue<T> {
        if (prioritySet.remove(element)) elemMap.remove(element)

        return this
    }

    operator fun contains(element: T) = element in elemMap

    fun clear() {
        prioritySet.clear()
        elemMap.clear()
    }

    fun toList() = prioritySet.toList().reversed()

    fun toArray() = toList().toTypedArray()

    override fun iterator(): Iterator<T> = DescendingIterator()

    override fun each(cons: (T) -> Unit) {
        for (elem in this) cons(elem)
    }

    inner class DescendingIterator : Iterator<T> {
        private val iterator = prioritySet.descendingIterator()

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): T = iterator.next()
    }

    private fun initCapacity(): Int {
        return (maxSize / loadFactor).toInt() + 1
    }

    operator fun plus(element: T): WindowedMonotonicQueue<T> {
        add(element)
        return this
    }

    operator fun minus(element: T): WindowedMonotonicQueue<T> {
        remove(element)
        return this
    }

    override fun toString() = "PriorityWindowSet(size=$size, maxSize=$maxSize, elements=${toList()})"
}