package blogDeNoticias.model

import java.util.*

data class Noticia(
    var titulo: String,
    var cuerpo: String,
    var fechaPublic: Date,
    val tag: List<String>,
    var user: String
)