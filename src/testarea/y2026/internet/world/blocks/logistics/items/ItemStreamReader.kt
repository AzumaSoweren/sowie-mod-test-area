package testarea.y2026.internet.world.blocks.logistics.items

import arc.func.Prov
import arc.scene.ui.layout.Table
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.gen.Building
import mindustry.type.Item
import mindustry.world.blocks.ItemSelection
import testarea.y2026.internet.world.blocks.logistics.DataStreamGraph
import testarea.y2026.internet.world.blocks.logistics.DataStreamMemory
import testarea.y2026.internet.world.blocks.logistics.DataStreamReader

// TODO
open class ItemStreamReader(name: String) : DataStreamReader(name) {
    init {
        hasItems = true
        configurable = true
        saveConfig = true
        itemCapacity = 0
        clearOnDoubleTap = true

        config<Item, ItemStreamReaderBuild>(Item::class.java) { tile, item ->
            tile.sortItem = item
        }
        configClear<ItemStreamReaderBuild> { it.sortItem = null }

        buildType = Prov(::ItemStreamReaderBuild)
    }

    //    var readSpeed = 0.12f
    var pollingTime = 10f

    // just...like Unloader?
    open inner class ItemStreamReaderBuild : DataStreamReaderBuild() {
        val possibleBlocks = mutableListOf<Building>()
        var sortItem: Item? = null
        var memory: DataStreamMemory.DataStreamMemoryBuild? = null
        var pollingTimer = 0f
        var pollingRotation = 0

        override fun updateTile() {
            val item = sortItem ?: return

            pollingTimer += edelta()
            loop@ while (pollingTimer >= pollingTime) {
                pollingTimer -= pollingTime
                repeat(possibleBlocks.size) {
                    pollingRotation = (pollingRotation + 1) % possibleBlocks.size

                    val target = possibleBlocks[pollingRotation]
                    if (target.acceptItem(this, item)) {
                        var to = memory
                        if (to == null
                            || !to.isValid
                            || !to.dataStreams!!.has(item)
                            || !DataStreamGraph.connected(this, to)
                        ) {
                            to = null
                            DataStreamGraph.eachMemoryWhile(this) { other ->
                                val found = other.dataStreams!!.has(item)
                                if (found) to = other
                                !found
                            }
                        }

                        memory = to

                        if (to == null) {
                            pollingTimer %= pollingTime
                            break@loop
                        }

                        target.handleItem(this, item)
                        to.dataStreams!!.remove(item)
                        continue@loop
                    }
                }
            }
        }

        override fun onProximityUpdate() {
            super.onProximityUpdate()
            possibleBlocks.clear()
            proximity.filterTo(possibleBlocks) { it.interactable(team) }
        }

        override fun drawSelect() {
            super.drawSelect()
            drawItemSelection(sortItem)
        }

        override fun buildConfiguration(table: Table) {
            ItemSelection.buildTable(
                this@ItemStreamReader,
                table,
                Vars.content.items(),
                ::sortItem,
                ::configure,
                selectionRows,
                selectionColumns
            )
        }

        override fun config() = sortItem

        override fun write(write: Writes) {
            write.s(sortItem?.id?.toInt() ?: -1)
        }

        override fun read(read: Reads, revision: Byte) {
            sortItem = Vars.content.item(read.s().toInt())
        }
    }
}