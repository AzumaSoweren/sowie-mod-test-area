package testarea.y2026.internet.world.blocks.logistics

abstract class DataStreamMemory(name: String) : DataStreamBlock(name) {
    init {
        update = true
        solid = true
        destructible = true
        hasDataStreams = true
        configurable = true
        saveConfig = true
        clearOnDoubleTap = true
    }

    /** Whether the building makes non-selected items automatically flow to other memory selecting it. */
    var autoFlow = true

    /** The speed of automatically flow. (Mb per tick) */
    var autoFlowSpeed = 0.05f

    /** The interval for scanning which items need to be flowed. (Ticks) */
    var autoFlowInterval = 60f

    override val blockType = Type.MEMORY

    abstract inner class DataStreamMemoryBuild : DataStreamBuild()
}