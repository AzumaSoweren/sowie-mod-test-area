package testarea.y2025.m12.internet.world.blocks.internet

open class InternetWire(name: String) : NetworkBlock(name) {
    init {
        solid = false
        destructible = true
    }

    override val networkType = NetworkBlockType.WIRE

    open inner class InternetWireBuild : NetworkBuild()
}