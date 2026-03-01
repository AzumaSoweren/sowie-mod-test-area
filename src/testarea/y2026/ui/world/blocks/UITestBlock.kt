package testarea.y2026.ui.world.blocks

import arc.scene.ui.Dialog
import arc.scene.ui.layout.Table
import mindustry.gen.Building
import mindustry.gen.Icon
import mindustry.ui.Styles
import mindustry.world.Block
import mindustry.world.meta.BlockGroup
import mindustry.world.meta.Env

class UITestBlock(name: String) : Block(name) {
    var dialogProv: (() -> Dialog)? = null

    init {
        configurable = true
        solid = true
        destructible = true
        group = BlockGroup.logic
        drawDisabled = false
        envEnabled = Env.any
    }

    inner class UITestBuild : Building() {
        private var dialog = dialogProv?.invoke()

        override fun buildConfiguration(table: Table) {
            table.button(Icon.info, Styles.cleari) {
                dialog?.update {
                    if (tile.build != this) dialog!!.hide()
                }
                dialog?.show()
                deselect()
            }.size(40f)
        }
    }
}