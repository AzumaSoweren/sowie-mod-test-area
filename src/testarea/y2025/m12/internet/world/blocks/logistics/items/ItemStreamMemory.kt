package testarea.y2025.m12.internet.world.blocks.logistics.items

import arc.scene.ui.layout.Table
import arc.struct.Seq
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.type.Item
import testarea.y2025.m12.internet.type.logistics.DataStream
import testarea.y2025.m12.internet.type.logistics.ItemStream
import testarea.y2025.m12.internet.world.blocks.ItemSelections
import testarea.y2025.m12.internet.world.blocks.logistics.DataStreamGraph
import testarea.y2025.m12.internet.world.blocks.logistics.DataStreamMemory

class ItemStreamMemory(name: String) : DataStreamMemory(name) {
    init {
        config<Item, ItemStreamMemoryBuild>(Item::class.java) { tile, item ->
            val id = item.id.toInt()
            val selections = tile.selectedItems
            selections[id] = !selections[id]
        }
        config<BooleanArray, ItemStreamMemoryBuild>(BooleanArray::class.java) { tile, selections ->
            selections.copyInto(tile.selectedItems, startIndex = 0, endIndex = tile.selectedItems.size)
        }
        configClear<ItemStreamMemoryBuild> { it.selectedItems.fill(false) }
    }

    override fun setBars() {
        super.setBars()


    }

    inner class ItemStreamMemoryBuild : DataStreamMemoryBuild() {
        private val data = ItemStream(null, 1)
        private var cleared = false

        val selectedItems = BooleanArray(Vars.content.items().size)
        val flowingItems = Seq<Item>(false)
        var flowingRotation = 0
        var flowingSize = 0f
        var flowingTo: DataStreamMemoryBuild? = null
        var scanningTimer = 0f

        override fun updateTile() {
            val streams = dataStreams!!

            if (efficiency > 0.9999f) {
                if (autoFlow) {
                    // regardless of timescale
                    if (flowingItems.isEmpty) flowingTo = null
                    scanningTimer += Time.delta
                    if (flowingTo != null) flowingSize += autoFlowSpeed * edelta()
                }

                if (scanningTimer >= autoFlowInterval) {
                    scanningTimer %= autoFlowInterval
                    flowingItems.clear()
                    selectedItems.forEachIndexed { i, selected ->
                        val has = streams.has(DataStream.Type.ITEM, i)
                        if (!selected && has) flowingItems.add(Vars.content.item(i))
                    }
                }

                repeat(flowingItems.size) {
                    if (flowingItems.isEmpty) return@repeat
                    flowingRotation %= flowingItems.size
                    val item = flowingItems[flowingRotation]

                    if (data.item != flowingItems[flowingRotation]) data.item = item
                    var to = flowingTo
                    if (to == null
                        || !to.isValid
                        || !to.acceptDataStream(this, data)
                        || !DataStreamGraph.connected(this, to)
                    ) {
                        to = null
                        DataStreamGraph.eachMemoryWhile(this) { other ->
                            val found = other.acceptDataStream(this, data)
                            if (found) to = other
                            !found
                        }
                    }

                    flowingTo = to

                    if (to == null) {
                        flowingRotation++
                        return@repeat
                    }

                    while (flowingSize >= data.size && to.acceptDataStream(this, data)) {
                        to.handleDataStream(this, data)
                        streams.remove(item)

                        flowingSize -= data.size

                        if (!streams.has(item)) {
                            flowingItems.remove(flowingRotation)
                            flowingRotation++
                            break
                        }
                    }
                }
            } else if (!cleared) streams.clear()
        }

        override fun acceptDataStream(source: DataStreamBuild, data: DataStream): Boolean {
            return efficiency > 0.9999f
                    && data is ItemStream
                    && selectedItems[data.item!!.id.toInt()]
                    && dataStreams!!.total + data.size <= dataStreamCapacity
        }

        override fun handleDataStream(source: DataStreamBuild, data: DataStream) {
            dataStreams!!.add(data)
        }

        override fun buildConfiguration(table: Table) {
            ItemSelections.buildTable(
                this@ItemStreamMemory,
                table,
                Vars.content.items(),
                { selectedItems[it.id.toInt()] },
                consumer = ::configure,
                consumers = ::configure,
                rows = selectionRows,
                columns = selectionColumns
            )
        }

        override fun configure(value: Any?) {
            Call.tileConfig(Vars.player, this, value)
            block.lastConfig = selectedItems
        }

        override fun config() = selectedItems

        override fun write(write: Writes) {
            super.write(write)

            write.s(selectedItems.count { it })

            selectedItems.forEachIndexed { i, selected ->
                if (selected) write.s(i)
            }
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)

            val amount = read.s().toInt()

            repeat(amount) {
                val id = read.s().toInt()
                Vars.content.item(id) ?: return@repeat
                selectedItems[id] = true
            }
        }
    }
}