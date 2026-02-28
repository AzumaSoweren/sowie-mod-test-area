package testarea.y2025.m12.internet.type.logistics

import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.type.Liquid

class LiquidStream(var liquid: Liquid? = null, var amount: Float = 0f) : DataStream() {
    companion object {
        fun getSize(liquid: Liquid?, amount: Float) = amount * Type.LIQUID.dataSizeMultiplier
    }

    constructor(id: Int, amount: Float = 0f) : this(Vars.content.liquid(id)!!, amount)

    override val dataType = Type.LIQUID

    override val size get() = getSize(liquid, amount)

    override fun write(write: Writes) {
        write.s(liquid!!.id.toInt())
        write.f(amount)
    }

    override fun read(read: Reads) {
        liquid = Vars.content.liquid(read.s().toInt())!!
        amount = read.f()
    }
}