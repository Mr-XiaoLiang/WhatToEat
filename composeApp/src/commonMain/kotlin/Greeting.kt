class Greeting {
    fun greet(): String {
        return "Hello, ${Config.platform.name}!"
    }
}