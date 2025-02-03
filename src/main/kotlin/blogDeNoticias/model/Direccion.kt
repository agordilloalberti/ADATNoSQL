package blogDeNoticias.model

/**
 * Clase encargada de estandarizar las direcciones.
 * Es una clase simple para guardar dentro de los usuarios como datos extra
 */
data class Direccion(
    val calle: String,
    val num: String,
    val puerta: String,
    val cp: String,
    val ciudad: String
)
