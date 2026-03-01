package testarea.y2026.internet.world.blocks.logistics

// TODO
class DataStreamDisk(name: String) : DataStreamBlock(name) {
    init {
        solid = true
        destructible = true
        hasDataStreams = true
    }

    // Mb/tick
    var maxWriteSpeed = 0.12f
    var maxReadSpeed = 0.3f

    override val blockType = Type.DISK

    inner class DataStreamDiskBuild : DataStreamBuild()
}