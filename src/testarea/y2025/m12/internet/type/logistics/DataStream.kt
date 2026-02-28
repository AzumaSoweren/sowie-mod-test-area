package testarea.y2025.m12.internet.type.logistics

import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable

abstract class DataStream : Writable, Readable {
    abstract val dataType: Type

    // Mb
    abstract val size: Float

    enum class Type(val dataProv: () -> DataStream, var dataSizeMultiplier: Float = 1f) {
        ITEM(::ItemStream),
        LIQUID(::LiquidStream, 0.1f)
        ;

        companion object {
            val all = entries.toTypedArray()
        }
    }
}