package ejercicioJuegos

import checkDouble
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.github.cdimascio.dotenv.dotenv
import myPrintln
import org.bson.Document
import son

private fun main() {
    val dotenv = dotenv()

    val urlConnectionMongo = dotenv["URL_MONGODB"] ?: "error"

    val cluster = MongoClients.create(urlConnectionMongo)

    val db = cluster.getDatabase("AlvaroGordilloSuano")

    val collection = db.getCollection("Juegos")

    ejercicio(collection)
}

private fun ejercicio(collection: MongoCollection<Document>) {
    println("Bienvenido a la app de videojuegos de Álvaro Gordillo Suano")
    var op:String
    do {
        println("¿Qué desea hacer?" +
                "\n1.Listar todos los juegos" +
                "\n2.Búsqueda por genero" +
                "\n3.Añadir nuevo juego" +
                "\n4.Eliminar juegos por genero" +
                "\n5.Editar un juego" +
                "\n0.Salir")
        op = readln()
        when (op) {
            "1" -> {
                println("Ha seleccionado listar todos los juegos")
                listar(collection)
            }
            "2" ->{
                println("Ha seleccionado buscar por genero")
                val resultados = busquedaPorGenero(collection)
                for (resultado in resultados) {
                    myPrintln(resultado)
                }
            }
            "3" -> {
                println("Ha seleccionado registrar un nuevo juego")
                añadirJuego(collection)
            }
            "4" -> {
                println("Ha seleccionado eliminar juegos por genero")
                eliminarPorGenero(collection)
            }
            "5" -> {
                println("Ha elegido modificar un juego")
                modificarJuego(collection)
            }
            "0" -> {
                println("Adios")
            }
            else -> {
                println("Opción invalida")
            }
        }
    }while (op!="0")
}

private fun listar(collection: MongoCollection<Document>){
    val everything = collection.find()

    for (juego in everything){
        myPrintln(juego.toString())
    }
}

private fun busquedaPorGenero(collection: MongoCollection<Document>): List<String> {
    println("Introduzca el genero a buscar")
    val genero = readln()

    val filtro = Filters.eq("genero",genero)

    val busqueda = collection.find(filtro)

    val resultados = mutableListOf<String>()

    for (resultado in busqueda){
        resultados.add(resultado.toString())
    }

    return resultados.toList()
}

private fun añadirJuego(collection: MongoCollection<Document>){

    var titulo: String

    do {
        println("Escriba el titulo del juego")
        titulo = readln()
        val filtro = Filters.eq("titulo",titulo)
        val busqueda = collection.find(filtro).toList()
        if (busqueda.isNotEmpty()) {
            println("No es posible añadir dos juegos con el mismo titulo")
        }
    }while(busqueda.isNotEmpty())

    val genero: String
    if(son("¿Desea añadir genero al juego?")) {
        println("Escriba el genero del juego")
        genero = readln()
    }

    var precio: Double
    if(son("¿Desea añadir precio al juego?")) {
        var precioS : String?
        do {
            println("Escriba el precio del juego (Puede usar decimales)")
            precioS = readln()
            if (checkDouble(precioS)==null){
                println("Debe introducir un valor válido")
            }
        }while (checkDouble(precioS)!=null)
        precio=precioS.toDouble()
    }

    val fechaLanz: String
    if(son("¿Desea añadir fecha de lanzamiento al juego?")) {
        println("Escriba el año de lanzamiento")
        val año = readln()
        println("Escriba el mes de lanzamiento")
        val mes = readln()
        println("Escriba el dia de lanzamiento")
        val dia = readln()

        fechaLanz="$dia/$mes/$año"
    }
}

private fun eliminarPorGenero(collection: MongoCollection<Document>){

}

private fun modificarJuego(collection: MongoCollection<Document>){

}