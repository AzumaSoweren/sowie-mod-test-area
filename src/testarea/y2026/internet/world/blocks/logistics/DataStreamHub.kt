package testarea.y2026.internet.world.blocks.logistics

import arc.func.Prov
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import mindustry.Vars
import mindustry.graphics.Layer
import mindustry.graphics.Pal
import mindustry.world.Edges

class DataStreamHub(name: String) : DataStreamBlock(name) {
    init {
        solid = false
        destructible = true

        buildType = Prov(::DataStreamHubBuild)
    }

    override val blockType = Type.WIRE

    inner class DataStreamHubBuild : DataStreamBuild() {
        val links = arrayOfNulls<DataStreamBuild>(size * 4)
        val linked = HashSet<DataStreamBuild>(4)
        val nearby = Edges.getEdges(size)!!

        override fun drawSelect() {
            super.drawSelect()

            Draw.z(Layer.blockOver)
            Draw.color(Pal.accent, 0.3f)
            DataStreamGraph.eachWire(this) { wire ->
                Fill.square(wire.x, wire.y, 4.5f, 10f)
            }
            Draw.color(Pal.place, 0.3f)
            DataStreamGraph.eachMemory(this) { memory ->
                Fill.square(memory.x, memory.y, 8.5f, 10f)
            }
            Draw.reset()
        }

        override fun onProximityUpdate() {
            super.onProximityUpdate()

            linked.clear()
            nearby.forEachIndexed { i, point ->
                val old = links[i]
                val new = Vars.world.build(tile.x + point.x, tile.y + point.y)
                if (old == new || new in linked) return@forEachIndexed
                if (old != null) DataStreamGraph.cut(this, old)
                when (new) {
                    is DataStreamBuild -> {
                        links[i] = new
                        linked += new
                        DataStreamGraph.link(this, new)
                    }

                    else -> links[i] = null
                }
            }
        }

        override fun onProximityRemoved() {
            super.onProximityRemoved()

            links.forEach {
                if (it != null) DataStreamGraph.cut(this, it)
            }
        }
    }
}