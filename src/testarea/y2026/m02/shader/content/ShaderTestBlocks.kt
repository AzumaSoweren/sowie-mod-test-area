package testarea.y2026.m02.shader.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.graphics.gl.FrameBuffer
import arc.graphics.gl.Shader
import arc.math.Angles
import arc.math.Mathf
import arc.struct.FloatSeq
import arc.util.Time
import arc.util.Tmp
import mindustry.Vars
import mindustry.content.Items
import mindustry.graphics.Pal
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import mindustry.world.draw.DrawDefault
import mindustry.world.draw.DrawMulti
import testarea.KtTestArea
import testarea.sorc.load.ManageLoader
import testarea.y2026.m02.shader.world.blocks.DrawerWall
import testarea.y2026.m02.shader.world.draw.DrawCons
import kotlin.math.absoluteValue

object ShaderTestBlocks : ManageLoader<Block>() {
    private val root = Vars.mods.getMod(KtTestArea::class.java).root.child("shaders")
    private const val DOWN_NAME = "dual_kawase_down"
    private const val UP_NAME = "dual_kawase_up"
    private val buffer = FrameBuffer(2, 2, false)
    private val pingPong1 = FrameBuffer(2, 2, false)
    private val pingPong2 = FrameBuffer(2, 2, false)
    private val down = Shader(root.child("$DOWN_NAME.vert"), root.child("$DOWN_NAME.frag"))
    private val up = Shader(root.child("$UP_NAME.vert"), root.child("$UP_NAME.frag"))

    private val polyFloats = FloatSeq()

    val test1 = manager.register {
        DrawerWall("test1").apply {
            requirements(Category.effect, ItemStack.with(Items.silicon, 48))
            scaledHealth = 800f
            size = 2

            drawer = DrawMulti(
                DrawDefault(),
                DrawCons { build ->
                    val z = Draw.z()
                    val x = build.x
                    val y = build.y

                    val rotation = -Time.time
                    val radius = 200f

                    Draw.z(111f)
                    Draw.color(Pal.accent)
                    Draw.alpha(0.2f)
                    Fill.circle(x, y, radius)
                    Draw.alpha(1f)
                    test(x, y, radius, 0.1f, rotation, Pal.accent, sides = 50)
                    Lines.stroke(2f)
                    Lines.lineAngle(x, y, rotation, radius)
                    Lines.stroke(3f)
                    Lines.circle(x, y, radius)

//                    Draw.z(110f)
//                    Draw.color(Pal.accent)
//                    Fill.circle(build.x - 8f, build.y, 3f)
//
//                    Draw.z(111f)
//                    Draw.color(Pal.accent)
//                    Fill.circle(build.x + 8f, build.y, 3f)
                    Draw.z(z)
                    Draw.reset()
                }
            )
        }
    }

    private fun test(x: Float, y: Float, radius: Float, fraction: Float, rotation: Float, color1: Color, alphaTo: Float = 0f, sides: Int = 50) {
        val max = Mathf.ceil(sides * fraction.absoluteValue)
        polyFloats.clear()
        val centerColor = Tmp.c2.set(color1).a(alphaTo).toFloatBits()
        polyFloats.add(x, y, centerColor)

        Tmp.c1.set(color1)
        for (i in 0..max) {
            val f = i.toFloat() / max
            val a = fraction * f * 360f + rotation
            val x1 = Angles.trnsx(a, radius)
            val y1 = Angles.trnsy(a, radius)
            Tmp.c1.set(color1).a(Mathf.lerp(1f, alphaTo, f))

            polyFloats.add(x + x1, y + y1, Tmp.c1.toFloatBits())
        }
        polyFloats.add(x, y, centerColor)

        val items = polyFloats.items
        val size = polyFloats.size

        for (i in 3..size - 6 step 6) Fill.quad(
            items[0], items[1], items[2],
            items[i], items[i + 1], items[i + 2],
            items[i + 3], items[i + 4], items[i + 5],
            items[i + 6], items[i + 7], items[i + 8]
        )
    }
}