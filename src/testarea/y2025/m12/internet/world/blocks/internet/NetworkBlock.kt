package testarea.y2025.m12.internet.world.blocks.internet

import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.math.Rand
import mindustry.gen.Building
import mindustry.graphics.Layer
import mindustry.graphics.Pal
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