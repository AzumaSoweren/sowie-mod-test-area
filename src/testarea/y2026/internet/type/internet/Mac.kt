package testarea.y2026.internet.type.internet

import arc.util.io.Reads
import arc.util.io.Writes

open class Mac(val key: Int) {
    object EmptyMac : Mac(-1)

    companion object {
        fun fromRead(read: Reads): Mac {
            val key = read.i()
            return Mac(key)
        }
    }

    val value = key and 0xffffff

    fun write(write: Writes) {
        write.i(key)
    }
}