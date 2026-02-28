package testarea.sorc.struct

import testarea.sorc.util.struct.Traversable
import java.util.LinkedList
import java.util.Queue
import java.util.NoSuchElementException

class WindowedQueue<T>(private val maxSize: Int) : Queue<T>, Traversable<T> {
    init {
        require(maxSize > 0) { "maxSize must be positive" }
    }

    private val deque = LinkedList<T>()
    
    private fun addInternal(element: T): Boolean {
        if (deque.size == maxSize) {
            deque.poll()
        }
        return deque.offer(element)
    }

    override val size: Int get() = deque.size

    override fun add(element: T): Boolean {
        addInternal(element)
        return true
    }

    override fun offer(element: T): Boolean {
        addInternal(element)
        return true
    }

    override fun remove(): T {
        if (deque.isEmpty()) throw NoSuchElementException()
        return deque.poll()
    }

    override fun poll(): T? = deque.poll()

    override fun element(): T {
        if (deque.isEmpty()) throw NoSuchElementException()
        return deque.element()
    }

    override fun peek(): T? = deque.peek()

    override fun iterator() = deque.iterator()

    override fun remove(element: T) = deque.remove(element)

    override fun addAll(elements: Collection<T>): Boolean {
        var changed = false
        for (e in elements)
            if (add(e)) changed = true
        return changed
    }

    override fun removeAll(elements: Collection<T>) = deque.removeAll(elements.toSet())

    override fun retainAll(elements: Collection<T>) = deque.retainAll(elements.toSet())

    override fun each(cons: (T) -> Unit) {
        for (elem in this) cons(elem)
    }

    override fun clear() = deque.clear()

    override fun isEmpty() = deque.isEmpty()
    
    fun isSpare() = deque.size < maxSize

    override fun contains(element: T) = deque.contains(element)

    override fun containsAll(elements: Collection<T>) = deque.containsAll(elements)
}