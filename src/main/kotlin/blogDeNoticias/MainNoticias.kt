package blogDeNoticias

import ConexionMongo
import blogDeNoticias.menu.menu
import blogDeNoticias.model.Comentario
import blogDeNoticias.model.Noticia
import blogDeNoticias.model.Usuario
import com.mongodb.client.MongoCollection

/**
 * Archivo de ejecución, inicia la conexión a la base de datos, recupera las colecciones y llama al menu, una vez termine este
 * se cierra la conexión.
 */
fun main() {

    val database = ConexionMongo.getDatabase("BlogDeNoticias")

    val usersColl: MongoCollection<Usuario> = database.getCollection("Usuarios", Usuario::class.java)

    val noticiasColl: MongoCollection<Noticia> = database.getCollection("Noticias", Noticia::class.java)

    val comentariosColl: MongoCollection<Comentario> = database.getCollection("Comentarios", Comentario::class.java)

    menu(usersColl,noticiasColl,comentariosColl)

    ConexionMongo.close()
}