-keep public class * extends mindustry.mod.Mod
-keep class * extends mindustry.mod.Mod { *; }
#-keep class * extends mindustry.gen.Building

#-keep class kotlin.Metadata { *; }
#-keepattributes Signature, InnerClasses, EnclosingMethod
#-keepattributes InnerClasses, EnclosingMethod
#-keep class **$* extends mindustry.gen.Building { *; }

-whyareyoukeeping class testarea.y2026.internet.world.blocks.logistics.DataStreamHub$DataStreamHubBuild {
    public void drawSelect();
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}