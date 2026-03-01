package testarea.y2026.internet.world.blocks.logistics

// TODO finish disk
abstract class DataStreamWriter(name: String) : DataStreamBlock(name) {
    init {
        solid = true
        destructible = true
        update = true
    }

    override val blockType = Type.INPUT

    abstract inner class DataStreamWriterBuild : DataStreamBuild()
//        var writeSize = 0f
//        var dataBuffer = WindowedQueue<DataStream>(dataBufferCapacity)
//
//        override fun updateTile() {
//            val buffer = dataBuffer
//            val streams = dataStreams!!
//
//            if (buffer.isNotEmpty()) {
//                val currency = buffer.peek()!!
//                if (dataStreams!!.total + currency.size <= dataStreamCapacity) {
//                    writeSize = 0f
//                    buffer.poll()
//                    buffer += currency
//                } else {
//                    writeSize += writeSpeed * delta()
//
//                    if (writeSize >= currency.size) {
//                        writeSize = 0f
//                        buffer.poll()
//                        streams.add(currency)
//                    }
//                }
//            }
//        }
//
//        override fun write(write: Writes) {
//            super.write(write)
//
//            write.i(dataBuffer.size)
//            dataBuffer.each { data ->
//                write.s(data.dataType.ordinal)
//                data.write(write)
//            }
//        }
//
//        override fun read(read: Reads) {
//            super.read(read)
//
//            val size = read.i()
//            repeat(size) {
//                val data = DataStream.Type.all[read.s().toInt()].dataProv()
//                data.read(read)
//                dataBuffer += data
//            }
//        }
//    }
}