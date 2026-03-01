package testarea.y2026.internet.world.blocks.internet

import arc.math.Mathf

class Wire(name: String) : InternetWire(name) {
    init {
        rotate = true
    }

    inner class WireBuild : InternetWireBuild() {
        var front: NetworkBuild? = null
        var back: NetworkBuild? = null

        override fun onProximityUpdate() {
            super.onProximityUpdate()
            val newFront = front()
            val newBack = back()

            if (front != newFront) {
                if (front != null) NetworkGraph.cut(this, front!!)
                when (newFront) {
                    null -> front = null
                    is InternetWireBuild, is PortDock.PortDockBuild -> {
                        if (!newFront.block.rotate || Mathf.mod(newFront.rotation - rotation, 2) == 0) {
                            front = newFront
                            NetworkGraph.link(this, newFront)
                        }
                    }
                }
            }

            if (back != newBack) {
                if (back != null) NetworkGraph.cut(this, back!!)
                when (newBack) {
                    is InternetWireBuild, is PortDock.PortDockBuild -> {
                        if (!newBack.block.rotate || Mathf.mod(newBack.rotation - rotation, 2) == 0) {
                            back = newBack
                            NetworkGraph.link(this, newBack)
                        }
                    }
                    else -> back = null
                }
            }
        }

        override fun onProximityRemoved() {
            super.onProximityRemoved()

            if (front != null) {
                NetworkGraph.cut(this, front!!)
                front = null
            }
            if (back != null) {
                NetworkGraph.cut(this, back!!)
                back = null
            }
        }
    }
}