package testarea.y2025.m12.internet.world.blocks.internet

import arc.Events
import mindustry.game.EventType
import testarea.sorc.struct.graph.Graph

object NetworkGraph {
    private val graph = Graph(::NetworkData)

    fun link(u: NetworkBlock.NetworkBuild, v: NetworkBlock.NetworkBuild) {
        graph.link(u.graphVertex, v.graphVertex)
    }

    fun cut(u: NetworkBlock.NetworkBuild, v: NetworkBlock.NetworkBuild) {
        graph.cut(u.graphVertex, v.graphVertex)
    }

    fun connected(u: NetworkBlock.NetworkBuild, v: NetworkBlock.NetworkBuild) =
        graph.connected(u.graphVertex, v.graphVertex)

    // contain src, you can exclude it in cons
    fun eachConnected(src: NetworkBlock.NetworkBuild, cons: (NetworkBlock.NetworkBuild) -> Unit) {
        val data = graph.ensureInfo(src.graphVertex)[0].node.root.data!!
        val userData = data.userData!!
        userData.devices.forEach(cons)
        userData.wires.forEach(cons)
        userData.docks.forEach(cons)
    }

    fun init() {
        Events.run(EventType.Trigger.newGame, graph::reset)
    }
}