package blogDeNoticias.model

import java.time.Instant
import java.util.*

data class Noticia(
    var titulo: String,
    var cuerpo: String,
    var user: Usuario,
    val tag: List<String>?,
    val fechaPub: Date = Date.from(Instant.now())
)