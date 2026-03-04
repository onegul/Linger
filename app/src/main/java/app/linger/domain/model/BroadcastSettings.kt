package app.linger.domain.model

data class BroadcastSettings(
    val discoverability: Discoverability,
    val showLifeToPeople: Boolean
)

enum class Discoverability {
    ONLY_BROADCASTERS,
    ALL
}