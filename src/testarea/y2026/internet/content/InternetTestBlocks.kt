package testarea.y2026.internet.content

import mindustry.content.Items
import mindustry.type.Category
import mindustry.type.ItemStack
import mindustry.world.Block
import testarea.sorc.load.ManageLoader
import testarea.y2026.internet.world.blocks.logistics.DataStreamHub
import testarea.y2026.internet.world.blocks.logistics.items.ItemStreamMemory
import testarea.y2026.internet.world.blocks.logistics.items.ItemStreamReader
import testarea.y2026.internet.world.blocks.logistics.items.ItemStreamWriter

object InternetTestBlocks : ManageLoader<Block>() {
//    val testMail1 by manager.register {
//        Mailbox("test1").apply {
//            requirements(Category.logic, ItemStack.with(Items.lead, 10))
//            size = 1
//            //update = false //TODO
//        }
//    }
//
//    val testMail2 by manager.register {
//        Mailbox("test2").apply {
//            requirements(Category.logic, ItemStack.with(Items.lead, 40))
//            size = 2
//            ports = 6
//            //update = false //TODO
//        }
//    }
//
//    val testPortDock1 by manager.register {
//        PortDock("dock1").apply {
//            requirements(Category.logic, ItemStack.with(Items.copper, 15))
//            size = 1
//        }
//    }
//
//    val testWire by manager.register {
//        Wire("wire1").apply {
//            requirements(Category.logic, ItemStack.with(Items.copper, 2))
//            size = 1
//        }
//    }

    val testHub by manager.register {
        DataStreamHub("hub1").apply {
            requirements(Category.logic, ItemStack.with(Items.copper, 6))
        }
    }

    val testMemory by manager.register {
        ItemStreamMemory("memory1").apply {
            requirements(Category.logic, ItemStack.with(Items.silicon, 48))
            size = 2
            hasPower = true
            consumePower(2f)
        }
    }

    val testWriter by manager.register {
        ItemStreamWriter("writer1").apply {
            requirements(Category.logic, ItemStack.with(Items.copper, 8, Items.silicon, 8))
        }
    }

    val testReader by manager.register {
        ItemStreamReader("reader1").apply {
            requirements(Category.logic, ItemStack.with(Items.copper, 8, Items.silicon, 12))
        }
    }
}