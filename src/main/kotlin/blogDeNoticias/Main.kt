package blogDeNoticias

import ConexionMongo
import blogDeNoticias.model.Direccion
import blogDeNoticias.model.Noticia
import blogDeNoticias.model.Usuario
import com.mongodb.client.MongoCollection
import java.sql.Date
import java.time.Instant
import kotlin.random.Random

fun main() {

    val database = ConexionMongo.getDatabase("Noticias")

    val usersColl: MongoCollection<Usuario> = database.getCollection("Usuarios", Usuario::class.java)

    val noticiasColl: MongoCollection<Noticia> = database.getCollection("Noticias", Noticia::class.java)

    try {
        val direccion = Direccion("alamo", "24", "04638", "Mojacar")
        val cliente = Usuario("maria@gmail.com", "Maria", "mar14", true,direccion,listOf("950475656", "666888999"))

        usersColl.insertOne(cliente)

        val direccion2 = Direccion("desconocida", "24", "04003", "Almeria")
        val direccion3 = Direccion("principal", "2", "04003", "Almeria")
        val direccion4 = Direccion("principal", "1", "04003", "Almeria")

        val cliente2 = Usuario("pedro@gmail.com", "Pedro", "periko", true, direccion2,listOf("950475656", "666888999"))
        val cliente3 = Usuario("ana@gmail.com", "Ana", "anuski", true, direccion3,listOf("950475656", "666888999"))
        val cliente4 = Usuario("antonio@gmail.com", "Antonio", "toni", true, direccion4,listOf("950475656", "666888999"))
        val cliente5 = Usuario("agustin@gmail.com", "Agustin", "agus", true, direccion4, listOf("950475656", "666888999"))

        val listaClientes = listOf(
            cliente2,
            cliente3,
            cliente4,
            cliente5
        )

        usersColl.insertMany(listaClientes)
    } catch (e: Exception) {

    }
try {
    val usuarios = usersColl.find().toList()

    val user = usuarios[Random.nextInt(usuarios.size)]

    val noticia = Noticia(
        "${user.nombre} juega a mas de 20 fps",
        "${user.nombre} se ha instalado una grafica, por fin, y puede jugar a juegos a mas de 20 fps",
        Date.from(Instant.now()),
        listOf("News", "Gaming"),
        user._id
    )

    noticiasColl.insertOne(noticia)
}
catch (e: Exception){
    println("Noticia Añadida")
}

    ConexionMongo.close()
}