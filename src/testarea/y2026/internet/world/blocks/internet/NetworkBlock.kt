package testarea.y2026.internet.world.blocks.internet

import arc.math.Rand
import mindustry.gen.Building
import mindustry.world.Block
import testarea.sorc.struct.graph.Vertex

abstract class NetworkBlock(name: String) : Block(name) {
    companion object {
        private val random = Rand()
    }

    abstract val networkType: NetworkBlockType

    open inner class NetworkBuild : Building() {
        var graphVertex = Vertex(random) { NetworkData(this) }

        open fun block() = block as NetworkBlock
    }

    enum class NetworkBlockType {
        DEVICE, WIRE, DOCK
    }
}