package blogDeNoticias.model

/**
 * Clase encargada de estandarizar los usuarios
 * guarda el email como campo id para poder localizar los usuarios de forma
 * inequívoca, a pesar de esta el campo "username" también se comprueba para ser único
 */
data class Usuario(
    //el email es el campo "_id"
    var _id: String,
    var nombre: String,
    //el username debe ser único
    var username: String,
    var direccion: Direccion,
    val telefonos: List<String>,
    var estado: Boolean = true
)