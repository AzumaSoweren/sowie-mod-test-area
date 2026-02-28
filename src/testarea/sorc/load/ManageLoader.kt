package testarea.sorc.load

abstract class ManageLoader<T> {
    protected val manager = InitManager()

    fun load() {
        manager.initAll()
    }
}