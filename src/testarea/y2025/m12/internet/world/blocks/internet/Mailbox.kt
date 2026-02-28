package testarea.y2025.m12.internet.world.blocks.internet

import arc.util.io.Reads
import arc.util.io.Writes
import testarea.sorc.struct.WindowedMonotonicQueue
import testarea.sorc.util.io.Readable
import testarea.sorc.util.io.Writable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.max

open class Mailbox(name: String) : InternetDevice(name) {
    var emailCapacity: Int = 10

    inner class MailboxBuild : InternetDeviceBuild() {
        var mails = WindowedMonotonicQueue.priorityWindowSet<Mail>(emailCapacity)
    }

    class Envelope(var mail: Mail = Mail.EmptyMail): Writable, Readable {
        override fun write(write: Writes) = mail.write(write)

        override fun read(read: Reads) {
            mail = Mail.fromRead(read)
        }
    }

    open class Mail(
        val level: Byte,
        val title: String,
        val text: String,
        val sender: String,
        val id: Int = idCounter++,
        val date: Date = Date.now()
    ) : Comparable<Mail> {
        object EmptyMail : Mail(-1, "", "", "")

        companion object {
            private var idCounter: Int = 0

            fun fromRead(read: Reads): Mail {
                val id = read.i()
                idCounter = max(id, idCounter)
                val level = read.b()
                val title = read.str()
                val text = read.str()
                val sender = read.str()
                return Mail(level, title, text, sender, id)
            }
        }

        override fun compareTo(other: Mail): Int {
            if (level != other.level) return if (level > other.level) 1 else -1
            return if (id < other.id) 1 else -1
        }

        override fun equals(other: Any?) = if (other is Mail) id == other.id else false

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + level
            result = 31 * result + title.hashCode()
            result = 31 * result + text.hashCode()
            result = 31 * result + sender.hashCode()
            result = 31 * result + date.hashCode()
            return result
        }

        fun write(write: Writes) {
            write.i(id)
            write.b(level.toInt())
            write.str(title)
            write.str(text)
            write.str(sender)
            write.str(date.time)
        }

        class Date(val time: String) {
            companion object {
                private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                fun now() = Date(formatter.format(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())).toString())
            }
        }
    }

}