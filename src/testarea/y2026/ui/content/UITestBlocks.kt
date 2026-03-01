package testarea.y2026.ui.content

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.math.Mathf
import arc.math.geom.Vec2
import arc.scene.ui.layout.Table
import arc.util.Tmp
import mindustry.content.Blocks
import mindustry.content.Items
import mindustry.gen.Tex
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.ui.dialogs.BaseDialog
import mindustry.world.Block
import testarea.sorc.load.ManageLoader
import testarea.y2026.ui.world.blocks.UITestBlock

object UITestBlocks : ManageLoader<Block>() {
    val test0 by manager.register {
        UITestBlock("ui-test0").apply {
            requirements(Category.logic, ItemStack.with(Items.silicon, 24))
            size = 1
            dialogProv = {
                BaseDialog("test0").apply {
                    setFillParent(true)
                    cont.add(
                        object : Table(Tex.pane) {
                            val realOffset = Vec2()
                            var block = Blocks.container
                            val texture = TextureRegion(block.uiIcon)
                            val size = 40f * block.size

                            override fun draw() {
                                super.draw()
                                Draw.color()
                                Draw.alpha(parentAlpha)
                                val drawOriginX = x + width / 2
                                val drawOriginY = y + height / 2
                                Tmp.v1.set(drawOriginX, drawOriginY).sub(Core.input.mouse())
                                val rotation = Mathf.mod(Tmp.v1.angle() + 45f, 90f)
//                                Geometry.
                                val len = Tmp.v1.len()
                                Tmp.v1.setLength(1000f / len.coerceIn(20f, 100f))
                                realOffset.lerp(Tmp.v1, 0.05f)
                                Draw.rect(texture, realOffset.x + drawOriginX, realOffset.y + drawOriginY, size, size)
                            }


                        }.apply {
                            setFillParent(false)
                        }
                    ).size(400f, 400f)
                    closeOnBack()
                }
            }
        }
    }
}