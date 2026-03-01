package testarea.y2026.internet.world.blocks.logistics

// TODO
abstract class DataStreamReader(name: String) : DataStreamBlock(name) {
    init {
        solid = true
        destructible = true
        update = true
    }

    override fun setBars() {
        super.setBars()
    }

    override val blockType = Type.OUTPUT

    abstract inner class DataStreamReaderBuild : DataStreamBuild()
}