package testarea.y2026.internet.world.blocks.logistics

import arc.Events
import mindustry.game.EventType
import testarea.sorc.struct.graph.Graph

object DataStreamGraph {
    private val graph = Graph(::DataStreamBlockInfo)

    fun link(u: DataStreamBlock.DataStreamBuild, v: DataStreamBlock.DataStreamBuild) {
        graph.link(u.graphVertex, v.graphVertex)
    }

    fun cut(u: DataStreamBlock.DataStreamBuild, v: DataStreamBlock.DataStreamBuild) {
        graph.cut(u.graphVertex, v.graphVertex)
    }

    fun connected(u: DataStreamBlock.DataStreamBuild, v: DataStreamBlock.DataStreamBuild) = graph.connected(u.graphVertex, v.graphVertex)

    fun eachMemory(src: DataStreamBlock.DataStreamBuild, cons: (DataStreamMemory.DataStreamMemoryBuild) -> Unit) {
        val data = graph.ensureInfo(src.graphVertex)[0].node.root.data!!
        val userData = data.userData!!
        userData.memories.each(cons)
    }

    fun eachMemoryWhile(src: DataStreamBlock.DataStreamBuild, cons: (DataStreamMemory.DataStreamMemoryBuild) -> Boolean) {
        val data = graph.ensureInfo(src.graphVertex)[0].node.root.data!!
        val userData = data.userData!!
        for (memory in userData.memories) if (!cons(memory)) break
    }

    fun eachWire(src: DataStreamBlock.DataStreamBuild, cons: (DataStreamHub.DataStreamHubBuild) -> Unit) {
        val data = graph.ensureInfo(src.graphVertex)[0].node.root.data!!
        val userData = data.userData!!
        userData.wires.each(cons)
    }

    fun init() {
        Events.run(EventType.Trigger.newGame, graph::reset)
    }
}