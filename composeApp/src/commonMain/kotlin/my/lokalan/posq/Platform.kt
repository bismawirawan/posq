package my.lokalan.posq

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform