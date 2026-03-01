package testarea.y2026.internet.world.blocks.internet

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Lines
import arc.math.Mathf
import arc.math.geom.Point2
import arc.scene.ui.ButtonGroup
import arc.scene.ui.ScrollPane
import arc.scene.ui.TextButton
import arc.scene.ui.layout.Table
import arc.util.Scaling
import arc.util.Time
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.Vars
import mindustry.gen.Building
import mindustry.gen.Icon
import mindustry.gen.Tex
import mindustry.graphics.Drawf
import mindustry.graphics.Pal
import mindustry.ui.Styles
import mindustry.ui.dialogs.BaseDialog

//import testarea.y2026.internet.tools.struct.graph.GraphWithEET

open class PortDock(name: String) : NetworkBlock(name) {
    init {
        update = true
        solid = true
        destructible = true
        configurable = true
        clearOnDoubleTap = true

        config<NetworkPort, PortDockBuild>(NetworkPort::class.java) { tile, port ->
            val oldPort = tile.port
            tile.unport()
            if (oldPort != port.id) tile.port(port)
            tile.lastChange = -2
        }

        config<Integer, PortDockBuild>(Integer::class.java) { tile, point ->
            if (point.toInt() == -1) tile.unport()
            tile.link = point.toInt()
            tile.lastChange = -2
        }
        config<LinkData, PortDockBuild>(LinkData::class.java) { tile, data ->
            tile.link = Point2.pack(data.point.x + tile.tileX(), data.point.y + tile.tileY())
            tile.port = data.port
            tile.lastChange = -2
//            val other = Vars.world.build(tile.link) as? InternetBlock.InternetBuild
//            if (other == null) tile.port = data.port else {
//                val port = if (data.port != -1) other.portLists[data.port] else return@config
//                configurations[InternetPort::class.java][tile, port]
//            }
        }
    }

    override val networkType = NetworkBlockType.DOCK

    var range = 24f

    protected val portsRows = 2
    protected val portsColumns = 4

    data class LinkData(val point: Point2, val port: Int)

    open inner class PortDockBuild : NetworkBuild() {

        var linkBuild: InternetDevice.InternetDeviceBuild? = null
        var link = -1
        var port = -1
        var lastChange = -2

        override fun updateTile() {
            if (lastChange != Vars.world.tileChanges) {
                lastChange = Vars.world.tileChanges

                linkBuild = Vars.world.build(link) as? InternetDevice.InternetDeviceBuild
                val hasLink = linkValid()

                if (hasLink) {
                    val build = linkBuild!!
                    link = build.pos()
                    if (port >= build.block().ports) port = -1
                    else if (port != -1) {
                        val port = build.portLists[port]
                        if (port.link != pos()) port(port)
                    }
                }
            }
        }

        override fun buildConfiguration(table: Table) {
            table.button(Icon.book) {
                BaseDialog("@editport").apply {
                    val build = linkBuild

                    setFillParent(false)
                    cont.add(Table().apply {
                        image { build?.block?.uiIcon ?: this@PortDock.uiIcon }
                            .size(Vars.iconXLarge)
                            .scaling(Scaling.fit)
                            .left()
                        row()
                        add(if (build != null) "${build.block.localizedName} \n[gray]mac#${build.mac}" else "[gray]no connection")
                    })

                    cont.image(Tex.whiteui).color(Color.gray).fillY()

                    cont.add(ScrollPane(Table().apply {
                        val group = ButtonGroup<TextButton>()
                        group.setMinCheckCount(0)
                        val list = build?.portLists ?: emptyList()
                        var i = 0

                        for (item in list) {
                            item.checkLink()
                            button(i.toString(), Styles.clearTogglet) { configure(item) }
                                .group(group)
                                .size(40f)
                                .get().apply {
                                    update {
                                        isChecked = port == item.id
                                        isDisabled = item.link != -1 && pos() != item.link
                                    }
                                }

                            if (++i % portsColumns == 0) row()
                        }

                        if (list.size < portsColumns)
                            repeat(portsColumns - list.size) { add().size(40f) }
                    }).apply { setScrollingDisabled(true, false) })

                    buttons.button("@ok", ::hide).size(130f, 60f)
                    update { if (tile.build != this@PortDockBuild) hide() }
                    closeOnBack()
                    show()
                }
                deselect()
            }
        }

        override fun drawSelect() {
            super.drawSelect()

            Lines.stroke(1f)

            Draw.color(Pal.accent)
            Drawf.circles(x, y, range)
            Draw.reset()
        }

        override fun drawConfigure() {
            val sin = Mathf.absin(Time.time, 4f, 1f)

            Draw.color(Pal.accent)
            Lines.stroke(1f)
            Drawf.circles(x, y, tile.block().size / 2f * Vars.tilesize + sin + 1f)

            if (linkValid()) {
                val target = linkBuild!!
                Drawf.square(target.x, target.y, target.block.size / 2f * Vars.tilesize + 1f, Pal.place)
//                Drawf.arrow(x, y, target.x, target.y, size / 2f * Vars.tilesize + sin, 4f + sin)
            }

            Drawf.circles(x, y, range)
        }

        override fun onConfigureBuildTapped(other: Building): Boolean {
            if (this == other) {
                if (link == -1) deselect() else configure(-1)
                return false
            }

            if (link == other.pos()) {
                configure(-1)
                return false
            } else if (other is InternetDevice.InternetDeviceBuild && other.dst(tile) <= range && other.team == team) {
                configure(other.pos())
                return false
            }

            return true
        }

        fun port(other: NetworkPort) {
            other.linkBuild?.unport()
            other.link = pos()
            other.linkBuild = this
            port = other.id
        }

        fun unport() {
            if (link == -1 || port == -1) return
            val other = linkBuild ?: return
            other.portLists[port].link = -1
            other.portLists[port].linkBuild = null
            port = -1
        }

        fun linkValid(): Boolean {
            if (link == -1) return false
            val other = linkBuild ?: return false
            return other.team == team && within(other, range)
        }

//        fun portValid(): Boolean {
//            if (link == -1 || port == -1) return false
//            val other = linkBuild as? InternetBlock.InternetBuild ?: return false
//            return other.block().ports > port && other.portLists[port].link == pos()
//        }

        override fun config(): LinkData? {
            tile ?: return null
            val point = Point2.unpack(link).sub(tile.x.toInt(), tile.y.toInt())
            return LinkData(point, port)
        }

        override fun write(write: Writes) {
            super.write(write)
            write.i(link)
            write.i(port)
        }

        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            link = read.i()
            port = read.i()
        }
    }
}