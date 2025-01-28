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
){
    override fun toString(): String {
        val s = "|Usuario: $usuario|- - -|Titulo de la noticia: ${noticia.titulo}" +
                "|- - -|Comentario: $comentario|- - -|" +
                "Fecha de publicación: $fecha|"
        return s
    }
}