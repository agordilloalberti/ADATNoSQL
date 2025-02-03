package blogDeNoticias.model

import java.time.Instant
import java.util.*

/**
 * Clase encargada de estandarizar las noticias
 * guarda la fecha actual hasta el segundo para poder identificarlas de forma única
 */
data class Noticia(
    var titulo: String,
    var cuerpo: String,
    var user: Usuario,
    val tags: List<String>,
    val _id: Date = Date.from(Instant.now())
){
    override fun toString(): String {
        val s = "|Titulo: $titulo|- - -|Cuerpo: $cuerpo|- - -|" +
                "Usuario: ${user.username}|- - -|Etiquetas: $tags|- - -|" +
                "Fecha de publicación: $_id|"
        return s
    }
}