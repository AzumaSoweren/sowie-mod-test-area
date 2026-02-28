package testarea.y2025.m12.internet.world.blocks.internet

import arc.util.io.Reads
import arc.util.io.Writes
import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable
import testarea.y2025.m12.internet.type.internet.InternetMessage
import testarea.y2025.m12.internet.type.internet.Mac

open class InternetDevice(name: String) : NetworkBlock(name) {
    init {
        update = true
        solid = true
        destructible = true
    }

    override val networkType = NetworkBlockType.DEVICE

    var ports: Int = 1

    open inner class InternetDeviceBuild : NetworkBuild() {
        // TODO wtf?
        val mac: Mac by lazy { Mac(pos()) }
        val portLists = List(ports) { NetworkPort(this, it) }

        override fun updateTile() {
            //TODO
        }

        fun <Content> send(content: Content, to: Mac) where Content : Writable, Content : Readable {
//            val message = InternetMessage(content, mac, to)
//
//            for (port in portLists) {
//                val dock = port.linkBuild
//                dock ?: continue
//                for (otherDock in dock.docks) {
//                    val other = otherDock.linkBuild as? InternetBuild
//                    other ?: continue
//                    other.receive(message, mac)
//                }
//            }
        }

        fun <Content> receive(message: InternetMessage<Content>, from: Mac) where Content : Writable, Content : Readable {
            //TODO
        }

        override fun block(): InternetDevice = block as InternetDevice

        override fun write(write: Writes) {
            super.write(write)
            for (port in portLists) port.write(write)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            for (port in portLists) port.read(read)
        }
    }
}