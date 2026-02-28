package testarea.y2026.m02.shader.world.blocks

import arc.graphics.g2d.TextureRegion
import arc.struct.Seq
import arc.util.Eachable
import mindustry.entities.units.BuildPlan
import mindustry.world.blocks.defense.Wall
import mindustry.world.draw.DrawBlock
import mindustry.world.draw.DrawDefault

open class DrawerWall(name: String) : Wall(name) {
    var drawer: DrawBlock = DrawDefault()

    override fun load() {
        super.load()

        drawer.load(this)
    }

    override fun drawPlanRegion(plan: BuildPlan, list: Eachable<BuildPlan>) = drawer.drawPlan(this, plan, list)

    override fun icons(): Array<TextureRegion> = drawer.finalIcons(this)

    override fun getRegionsToOutline(out: Seq<TextureRegion>) = drawer.getRegionsToOutline(this, out)

    inner class DrawerWallBuild : WallBuild() {
        override fun draw() = drawer.draw(this)

        override fun drawLight() {
            super.drawLight()
            drawer.drawLight(this)
        }
    }
}