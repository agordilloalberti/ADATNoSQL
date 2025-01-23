package blogDeNoticias.model

data class Usuario(
    var _id: String,
    var nombre: String,
    var nick: String,
    var estado: Boolean,
    var direccion: Direccion,
    val telefonos: List<String>
)