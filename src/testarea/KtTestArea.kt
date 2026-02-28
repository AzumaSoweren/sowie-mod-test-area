package testarea

import mindustry.mod.Mod
import testarea.y2025.m12.internet.content.InternetTestBlocks
import testarea.y2025.m12.internet.world.blocks.internet.NetworkGraph
import testarea.y2025.m12.internet.world.blocks.logistics.DataStreamGraph
import testarea.y2026.m02.ui.content.UITestBlocks

//import testarea.y2025.m07.d02.content.TestBlocks


class KtTestArea : Mod() {
//    var bloom: ShaderTestBloom? = null

    override fun loadContent() {
        InternetTestBlocks.load()
        UITestBlocks.load()
//        ShaderTestBlocks.load()
    }

//    override fun init() {
//        bloom = ShaderTestBloom()
//
//        Events.run(EventType.Trigger.draw) {
//            val bloom = bloom ?: return@run
//            bloom.resize(Core.graphics.width, Core.graphics.height)
//            bloom.setIterations(Core.settings.getInt("bloomblur", 1))
//            bloom.setOffset(0.2f)
//            bloom.setScale(Vars.renderer.displayScale / 1.5f + 0.3333333f)
////            bloom.iterations = 2
//            Draw.drawRange(111f, bloom::begin, bloom::render)
//
//            ShaderTestBloom.bbb()
//        }
//    }

    override fun init() {
        NetworkGraph.init()
        DataStreamGraph.init()
    }
}