package blogDeNoticias.model

import java.time.Instant
import java.util.*

/**
 * Clase encargada de estandarizar los comentarios de las noticias
 * guarda el id de la noticia para poder acceder a esta a través de su comentario
 */
data class Comentario(
    //el campo "username" del usuario que ha escrito la noticia
    val usuario: String,
    //La noticia sobre la que se escribió el comentario
    val idNoticia: Date,
    val comentario: String,
    val fecha: Date = Date.from(Instant.now())
){
    override fun toString(): String {
        val s = "|Usuario: $usuario|- - -|Fecha de publicación de la noticia: $idNoticia" +
                "|- - -|Comentario: $comentario|- - -|" +
                "Fecha de publicación: $fecha|"
        return s
    }
}