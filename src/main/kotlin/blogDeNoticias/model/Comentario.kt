package blogDeNoticias.model

import java.time.Instant
import java.util.*

data class Comentario(
    //el campo "username" del usuario que ha escrito la noticia
    val usuario: String,
    //La noticia sobre la que se escribió el comentario
    val noticia: Noticia,
    val comentario: String,
    val fecha: Date = Date.from(Instant.now())
)