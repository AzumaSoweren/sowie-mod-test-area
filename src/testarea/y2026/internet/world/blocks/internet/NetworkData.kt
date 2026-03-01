package testarea.y2026.internet.world.blocks.internet

import testarea.sorc.struct.UnsafeLinkedList
import testarea.sorc.struct.graph.UserData

class NetworkData(var build: NetworkBlock.NetworkBuild? = null) : UserData<NetworkData>() {
    val devices = UnsafeLinkedList<InternetDevice.InternetDeviceBuild>()
    val wires = UnsafeLinkedList<InternetWire.InternetWireBuild>()
    val docks = UnsafeLinkedList<PortDock.PortDockBuild>()

    override fun maintain(left: NetworkData?, right: NetworkData?) {
        devices.clear()
        wires.clear()
        docks.clear()

        if (build != null) {
            when (build!!.block().networkType) {
                NetworkBlock.NetworkBlockType.DEVICE -> devices += build as InternetDevice.InternetDeviceBuild

                NetworkBlock.NetworkBlockType.WIRE -> wires += build as InternetWire.InternetWireBuild

                NetworkBlock.NetworkBlockType.DOCK -> docks += build as PortDock.PortDockBuild
            }
        }

        if (left != null) {
            devices += left.devices
            wires += left.wires
            docks += left.docks
        }

        if (right != null) {
            devices += right.devices
            wires += right.wires
            docks += right.docks
        }
    }
}