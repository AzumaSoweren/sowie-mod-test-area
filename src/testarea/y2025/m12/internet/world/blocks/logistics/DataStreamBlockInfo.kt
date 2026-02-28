package testarea.y2025.m12.internet.world.blocks.logistics

import testarea.sorc.struct.UnsafeLinkedList
import testarea.sorc.struct.graph.UserData

class DataStreamBlockInfo(var build: DataStreamBlock.DataStreamBuild? = null) : UserData<DataStreamBlockInfo>() {
//    val disks = UnsafeLinkedList<DataStreamDisk.DataStreamDiskBuild>()
    val memories = UnsafeLinkedList<DataStreamMemory.DataStreamMemoryBuild>()
    val wires = UnsafeLinkedList<DataStreamHub.DataStreamHubBuild>()
    val inputs = UnsafeLinkedList<DataStreamWriter.DataStreamWriterBuild>()
    val outputs = UnsafeLinkedList<DataStreamReader.DataStreamReaderBuild>()

    override fun maintain(left: DataStreamBlockInfo?, right: DataStreamBlockInfo?) {
        memories.clear()
        wires.clear()
        inputs.clear()
        outputs.clear()

        if (build != null)
            when (build!!.block().blockType) {
                DataStreamBlock.Type.MEMORY -> memories += build as DataStreamMemory.DataStreamMemoryBuild

                DataStreamBlock.Type.WIRE -> wires += build as DataStreamHub.DataStreamHubBuild

                DataStreamBlock.Type.INPUT -> inputs += build as DataStreamWriter.DataStreamWriterBuild

                DataStreamBlock.Type.OUTPUT -> outputs += build as DataStreamReader.DataStreamReaderBuild

                // TODO
                else -> {}
            }

        if (left != null) {
            memories += left.memories
            wires += left.wires
            inputs += left.inputs
            outputs += left.outputs
        }

        if (right != null) {
            memories += right.memories
            wires += right.wires
            inputs += right.inputs
            outputs += right.outputs
        }
    }
}