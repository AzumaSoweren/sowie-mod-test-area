package testarea.y2026.internet.world.blocks.logistics.items

import mindustry.gen.Building
import mindustry.type.Item
import testarea.y2026.internet.type.logistics.ItemStream
import testarea.y2026.internet.world.blocks.logistics.DataStreamGraph
import testarea.y2026.internet.world.blocks.logistics.DataStreamWriter

// TODO
class ItemStreamWriter(name: String) : DataStreamWriter(name) {
    init {
        hasItems = true
        itemCapacity = 0
    }

//    var baseWriteSpeed = 0.3f
//    var dataBufferCapacity = 10

    inner class ItemStreamWriterBuild : DataStreamWriterBuild() {
        private val data = ItemStream(null, 1)
        private var target: DataStreamBuild? = null

        // TODO
        override fun updateTile() {
        }

        override fun acceptItem(source: Building?, item: Item): Boolean {
            data.item = item

            var to = target
            if (to != null
                && to.acceptDataStream(this, data)
                && DataStreamGraph.connected(this, to)
            ) return true

            to = null
            DataStreamGraph.eachMemoryWhile(this) { memory ->
                val found = memory.acceptDataStream(this, data)
                if (found) to = memory
                !found
            }
            
            target = to

            return to != null
        }

        override fun handleItem(source: Building?, item: Item) {
            if (data.item != item) data.item = item
            target!!.handleDataStream(this, data)
        }
    }
}