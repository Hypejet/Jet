/**
 * Defines the Jet API module.
 *
 * @since 1.0
 * @author Codestech
 */
// We use modules to allow classes extend and implement sealed interfaces from other packages
module net.hypejet.jet {
    requires net.kyori.adventure;
    requires net.kyori.adventure.key;
    requires net.kyori.examination.api;
    requires org.checkerframework.checker.qual;
    requires org.slf4j;
    requires java.desktop;
    requires com.google.gson;
    requires net.kyori.adventure.nbt;
}