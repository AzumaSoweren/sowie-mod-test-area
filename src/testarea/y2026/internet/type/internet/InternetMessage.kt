package testarea.y2026.internet.type.internet

import arc.util.io.Reads
import arc.util.io.Writes
import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable

class InternetMessage<Content>(val content: Content, var source: Mac = Mac.EmptyMac, var target: Mac = Mac.EmptyMac) : Writable, Readable
        where Content : Writable, Content : Readable {
    override fun write(write: Writes) {
        content.write(write)
        source.write(write)
        target.write(write)
    }

    override fun read(read: Reads) {
        content.read(read)
        source = Mac.fromRead(read)
        target = Mac.fromRead(read)
    }
}