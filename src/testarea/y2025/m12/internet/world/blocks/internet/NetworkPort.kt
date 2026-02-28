package testarea.y2025.m12.internet.world.blocks.internet

import arc.util.io.Reads
import arc.util.io.Writes
import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable

class NetworkPort(val owner: InternetDevice.InternetDeviceBuild, val id: Int) : Writable, Readable {
    var link: Int = -1
    var linkBuild: PortDock.PortDockBuild? = null

    fun checkLink() {
        val build = linkBuild ?: return
        if (build.link == owner.pos() && build.port == id && build.isValid) return

        link = -1
        linkBuild = null
    }

    override fun write(write: Writes) {
        write.i(link)
    }

    override fun read(read: Reads) {
        link = read.i()
    }
}