package blogDeNoticias.model

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