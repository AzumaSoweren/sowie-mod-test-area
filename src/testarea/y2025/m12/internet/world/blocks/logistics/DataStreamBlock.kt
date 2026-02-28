package testarea.y2025.m12.internet.world.blocks.logistics

import arc.Core
import arc.math.Rand
import arc.util.Strings
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.graphics.Pal
import mindustry.ui.Bar
import mindustry.world.Block
import testarea.sorc.struct.graph.Vertex
import testarea.y2025.m12.internet.type.logistics.DataStream
import testarea.y2025.m12.internet.world.modules.DataStreamModule

abstract class DataStreamBlock(name: String) : Block(name) {
    companion object {
        private val random = Rand()
    }

    var hasDataStreams = false
    var dataStreamCapacity = 32f

    abstract val blockType: Type

    override fun setBars() {
        super.setBars()

        if (hasDataStreams) addBar<DataStreamBuild>("dataStreams") { entity ->
            Bar(
                { Core.bundle.format("bar.dataStreams", Strings.fixed(entity.dataStreams!!.total, 1)) },
                { Pal.techBlue },
                { entity.dataStreams!!.total / dataStreamCapacity }
            )
        }
    }

    open inner class DataStreamBuild : Building() {
        val graphVertex = Vertex(random) { DataStreamBlockInfo(this) }
        var dataStreams: DataStreamModule? = null

        open fun acceptDataStream(source: DataStreamBuild, data: DataStream) = false

        open fun handleDataStream(source: DataStreamBuild, data: DataStream) {}

        open fun block() = block as DataStreamBlock

        override fun create(block: Block, team: Team): Building {
            if (hasDataStreams) dataStreams = DataStreamModule()
            return super.create(block, team)
        }

        override fun write(write: Writes) {
            if (hasDataStreams) dataStreams!!.write(write)
        }

        override fun read(read: Reads, revision: Byte) {
            if (hasDataStreams) {
                if (dataStreams == null) dataStreams = DataStreamModule()
                dataStreams!!.read(read)
            }
        }
    }

    enum class Type {
        WIRE, DISK, MEMORY, INPUT, OUTPUT
    }
}