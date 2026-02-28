package testarea.y2025.m12.internet.world.blocks

import arc.Core
import arc.scene.style.TextureRegionDrawable
import arc.scene.ui.ButtonGroup
import arc.scene.ui.ImageButton
import arc.scene.ui.ScrollPane
import arc.scene.ui.TextField
import arc.scene.ui.layout.Table
import arc.struct.Seq
import mindustry.Vars
import mindustry.ctype.UnlockableContent
import mindustry.gen.Icon
import mindustry.gen.Tex
import mindustry.ui.Styles
import mindustry.world.Block
import java.util.*

object ItemSelections {
    private var search: TextField? = null
    private var rowCount = 0
    private var selectAll = BooleanArray(Short.MAX_VALUE.toInt()) { true }
    private var cancelAll = BooleanArray(Short.MAX_VALUE.toInt()) { false }

    fun <T : UnlockableContent> buildTable(
        block: Block? = null, table: Table,
        items: Seq<T>,
        holders: (T) -> Boolean, maxHolders: Int = -1,
        consumer: (T?) -> Unit, consumers: (BooleanArray) -> Unit,
        closeSelect: Boolean = false,
        rows: Int, columns: Int
    ) {
        val group = ButtonGroup<ImageButton>()
        group.setMinCheckCount(0)
        group.setMaxCheckCount(maxHolders)
        val cont = Table().top()
        cont.defaults().size(40f)

        if (search != null) search!!.clearText()

        val rebuild = {
            group.clear()
            cont.clearChildren()

            val text = if (search != null) search!!.getText() else ""
            var i = 0
            rowCount = 0

            val list = items.select {
                (text.isEmpty() || it.localizedName.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault())))
            }
            for (item in list) {
                if (!item.unlockedNow() || !item.isOnPlanet(Vars.state.planet) || item.isHidden) continue

                val button = cont.button(
                    Tex.whiteui,
                    Styles.clearNoneTogglei,
                    item.selectionSize.coerceIn(0f, 40f)
                ) {
                    if (closeSelect) Vars.control.input.config.hideConfig()
                }.tooltip(item.localizedName).group(group).get()
                button.changed { consumer(item) }
                button.style.imageUp = TextureRegionDrawable(item.uiIcon)
                button.update { button.isChecked = holders(item) }

                if (i++ % columns == (columns - 1)) {
                    cont.row()
                    rowCount++
                }
            }
        }

        rebuild()

        val main = Table().background(Styles.black6)
        var shouldRow = false
        if (maxHolders == -1) {
            val button = main.button(Tex.whiteui, Styles.clearNoneTogglei) {
                val allSelected = group.allChecked.size == group.buttons.size
                consumers(if (allSelected) cancelAll else selectAll)
//                rebuild()
            }.size(40f).tooltip("@ui.selectAll").get()
            button.style.imageUp = Icon.ok
            button.style.imageChecked = Icon.cancel
            button.update { button.isChecked = group.allChecked.size == group.buttons.size }
            shouldRow = true
        }

        if (rowCount > rows * 1.5f) {
            main.table { s ->
                s.image(Icon.zoom).padLeft(4f)
                search =
                    s.field(null) { _ -> rebuild() }.padBottom(4f).left().growX().get()
                search!!.setMessageText("@players.search")
            }.fillX()
            shouldRow = true
        }

        if (shouldRow) main.row()

        val pane = ScrollPane(cont, Styles.smallPane)
        pane.setScrollingDisabled(true, false)
        pane.exited {
            if (pane.hasScroll()) {
                Core.scene.setScrollFocus(null)
            }
        }

        if (block != null) {
            pane.setScrollYForce(block.selectScroll)
            pane.update {
                block.selectScroll = pane.getScrollY()
            }
        }

        pane.setOverscroll(false, false)
        main.add(pane).maxHeight((40 * rows).toFloat())
        table.top().add(main)
    }
}