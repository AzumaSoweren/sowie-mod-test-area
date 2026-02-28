package testarea.y2026.m02.shader.world.draw

import arc.func.Cons
import mindustry.gen.Building
import mindustry.world.draw.DrawBlock

class DrawCons(val cons: Cons<Building>) : DrawBlock() {
    override fun draw(build: Building) = cons[build]
}