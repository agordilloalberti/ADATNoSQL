package blogDeNoticias.model

import java.util.*

data class Comentario(
    var fecha: Date,
    var comentario: String,
    var usuarioId: String,
    var noticia: Noticia
)