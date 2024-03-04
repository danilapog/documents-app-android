package app.documents.core.model.cloud

import kotlinx.serialization.Serializable

@Serializable
sealed class Scheme(val value: String) {

    data object Http : Scheme(HTTP_SCHEME)
    data object Https : Scheme(HTTPS_SCHEME)
    data class Custom(val scheme: String) : Scheme(scheme)

    companion object {

        private const val HTTP_SCHEME = "http://"
        private const val HTTPS_SCHEME = "https://"

        fun valueOf(scheme: String): Scheme {
            return when (scheme) {
                HTTP_SCHEME -> Http
                HTTPS_SCHEME -> Https
                else -> if (scheme.contains("://")) {
                    Custom(scheme)
                } else {
                    throw IllegalArgumentException("$scheme not a default scheme")
                }
            }
        }
    }
}