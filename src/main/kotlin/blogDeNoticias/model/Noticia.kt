package blogDeNoticias.model

import java.time.Instant
import java.util.*

data class Noticia(
    var titulo: String,
    var cuerpo: String,
    var user: Usuario,
    val tags: List<String>,
    val fechaPub: Date = Date.from(Instant.now())
)

//TODO override el toString()