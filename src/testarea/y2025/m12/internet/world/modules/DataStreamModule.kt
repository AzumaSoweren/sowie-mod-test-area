package testarea.y2025.m12.internet.world.modules

import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.type.Item
import mindustry.world.modules.BlockModule
import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable
import testarea.y2025.m12.internet.type.logistics.DataStream
import testarea.y2025.m12.internet.type.logistics.ItemStream
import testarea.y2025.m12.internet.type.logistics.LiquidStream

class DataStreamModule : BlockModule(), Writable, Readable {
    var itemStreams = Array(Vars.content.items().size, ::ItemStream)
    var liquidStreams = Array(Vars.content.liquids().size, ::LiquidStream)

    // Mb
    var itemTotal = 0f
    var liquidTotal = 0f

    val total: Float get() = itemTotal + liquidTotal

    fun has(type: DataStream.Type, id: Int) = when (type) {
        DataStream.Type.ITEM -> itemStreams[id].amount > 0

        DataStream.Type.LIQUID -> liquidStreams[id].amount > 0f
    }

    fun has(item: ItemStream) = itemStreams[item.item!!.id.toInt()].amount > 0

    fun has(item: Item) = itemStreams[item.id.toInt()].amount > 0

    fun add(data: DataStream) {
        when (data.dataType) {
            DataStream.Type.ITEM -> {
                val data = data as ItemStream
                itemStreams[data.item!!.id.toInt()].amount += data.amount
                itemTotal += data.size
            }

            DataStream.Type.LIQUID -> {
                val data = data as LiquidStream
                liquidStreams[data.liquid!!.id.toInt()].amount += data.amount
                liquidTotal += data.size
            }
        }
    }

    fun remove(data: DataStream) {
        when (data.dataType) {
            DataStream.Type.ITEM -> {
                val data = data as ItemStream
                val target = itemStreams[data.item!!.id.toInt()]
                val amount = data.amount.coerceAtMost(target.amount)
                target.amount -= amount
                itemTotal -= data.size
            }

            DataStream.Type.LIQUID -> {
                val data = data as LiquidStream
                val target = liquidStreams[data.liquid!!.id.toInt()]
                val amount = data.amount.coerceAtMost(target.amount)
                target.amount -= amount
                liquidTotal -= data.size
            }
        }
    }

    fun remove(item: Item, amount: Int = 1) {
        val target = itemStreams[item.id.toInt()]
        val amount = amount.coerceAtMost(target.amount)
        target.amount -= amount
        itemTotal -= ItemStream.getSize(item, amount)
    }

    fun clear() {
        itemStreams.forEach { it.amount = 0 }
        liquidStreams.forEach { it.amount = 0f}

        itemTotal = 0f
        liquidTotal = 0f
    }

    override fun write(write: Writes) {
        val amountItem = itemStreams.count { it.amount > 0 }
        val amountLiquid = liquidStreams.count { it.amount > 0f }

        write.s(amountItem)
        write.s(amountLiquid)

        for (data in itemStreams) if (data.amount > 0) {
            write.s(data.item!!.id.toInt())
            write.i(data.amount)
        }

        for (data in liquidStreams) if (data.amount > 0f) {
            write.s(data.liquid!!.id.toInt())
            write.f(data.amount)
        }
    }

    override fun read(read: Reads) {
        val amountItem = read.s().toInt()
        val amountLiquid = read.s().toInt()

        itemStreams.forEach { it.amount = 0 }
        liquidStreams.forEach { it.amount = 0f }
        itemTotal = 0f
        liquidTotal = 0f

        repeat(amountItem) {
            val id = read.s().toInt()
            val amount = read.i()
            val item = Vars.content.item(id) ?: return@repeat
            itemStreams[id].amount = amount
            itemTotal += ItemStream.getSize(item, amount)
        }

        repeat(amountLiquid) {
            val id = read.s().toInt()
            val amount = read.f()
            val liquid = Vars.content.liquid(id) ?: return@repeat
            liquidStreams[id].amount = amount
            liquidTotal += LiquidStream.getSize(liquid, amount)
        }
    }
}