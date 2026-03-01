package testarea.y2026.internet.type.logistics

import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.type.Item

class ItemStream(var item: Item? = null, var amount: Int = 0) : DataStream() {
    companion object {
        fun getSize(item: Item?, amount: Int) = amount * Type.ITEM.dataSizeMultiplier
    }

    constructor(id: Int, amount: Int = 0) : this(Vars.content.item(id)!!, amount)

    override val dataType = Type.ITEM

    override val size get() = getSize(item, amount)

    override fun write(write: Writes) {
        write.s(item!!.id.toInt())
        write.i(amount)
    }

    override fun read(read: Reads) {
        item = Vars.content.item(read.s().toInt())!!
        amount = read.i()
    }
}