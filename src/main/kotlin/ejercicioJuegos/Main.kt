package ejercicioJuegos

import checkDouble
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import documentReformat
import io.github.cdimascio.dotenv.dotenv
import org.bson.Document
import confirmacionSobreSeleccion
import kotlin.math.absoluteValue

/**
 * Función main al uso la cual inicia la conexión con MongoDB y obtiene la colección correspondiente
 */
private fun main() {
    val dotenv = dotenv()

    val urlConnectionMongo = dotenv["URL_MONGODB"] ?: "error"

    val cluster = MongoClients.create(urlConnectionMongo)

    val db = cluster.getDatabase("AlvaroGordilloSuano")

    val collection = db.getCollection("Juegos")

    ejercicio(collection)
}

/**
 * Función encargada de mostrar el menu y llamar a la función correspondiente según la selección del usuario
 */
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
                    documentReformat(resultado)
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

/**
 *Función encargada de listar todos los juegos insertados en la base de datos
 */
private fun listar(collection: MongoCollection<Document>){
    val everything = collection.find()

    for (juego in everything){
        documentReformat(juego.toString())
    }
}

/**
 * Función encargada de buscar todos los juegos que tengan el género seleccionado por el usuario
 */
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

/**
 * Función encargada de añadir los juegos a la base de datos
 */
private fun añadirJuego(collection: MongoCollection<Document>){

    var titulo: String

    println("Escriba el titulo del juego")

    //Este loop obliga al usuario a introducir un título de juego el cual no este ya presente
    do {
        titulo = readln()
        val filtro = Filters.eq("titulo",titulo)
        val busqueda = collection.find(filtro).toList()
        if (busqueda.isNotEmpty()) {
            println("No es posible añadir dos juegos con el mismo titulo, escriba un titulo diferente")
        }
    }while(busqueda.isNotEmpty())

    //Para mayor simplicidad de código y navegación en la base de datos los campos nunca quedan vacíos o nulos
    var genero = "SinGenero"
    if(confirmacionSobreSeleccion("¿Desea añadir genero al juego?")) {
        println("Escriba el genero del juego")
        genero = readln()
    }

    var precio = 0.0
    if(confirmacionSobreSeleccion("¿Desea añadir precio al juego?")) {
        var precioS : String?
        do {
            println("Escriba el precio del juego (Puede usar decimales)")
            precioS = readln()
            if (checkDouble(precioS)==null){
                println("Debe introducir un valor válido")
            }
        }while (checkDouble(precioS)==null)

        //Se obliga al precio a ser positivo
        precio=precioS.toDouble().absoluteValue
    }

    //Este proceso asegura que la fecha mantiene siempre el mismo formato e impide errores
    var fechaLanz = "0/0/0"
    if(confirmacionSobreSeleccion("¿Desea añadir fecha de lanzamiento al juego?")) {
        println("Escriba el año de lanzamiento")
        val año = readln()
        println("Escriba el mes de lanzamiento")
        val mes = readln()
        println("Escriba el dia de lanzamiento")
        val dia = readln()

        fechaLanz="$dia/$mes/$año"
    }

    val doc = Document()
        .append("titulo",titulo)
        .append("genero",genero)
        .append("precio",precio)
        .append("fechaLanzamiento",fechaLanz)

    collection.insertOne(doc)
    println("Se ha añadido el juego $titulo")
}

/**
 * Función encargada de eliminar los juegos del género seleccionado por el usuario
 */
private fun eliminarPorGenero(collection: MongoCollection<Document>){
    println("Introduzca el genero a borrar")

    val genero = readln()

    val filtro = Filters.eq("genero",genero)

    val busqueda = collection.find(filtro).toList()

    if (busqueda.isEmpty()) {
        println("No se ha encontrado el genero que busca.")
        return
    }

    val delResult = collection.deleteMany(filtro)

    if (delResult.deletedCount>0){
        println("Se ha borrado correctamente todo el genero $genero")
    }else{
        println("Ha ocurrido un error al borrar")
    }

}

/**
 * Función encargada de buscar el juego a modificar y de gestionar los errores que puedan surgir
 */
private fun modificarJuego(collection: MongoCollection<Document>){
    println("Escriba el titulo del juego")

    var titulo = readln()

    val filtro = Filters.eq("titulo",titulo)
    val busqueda = collection.find(filtro).toList()
    if (busqueda.isEmpty()) {
        println("No se ha encontrado el juego $titulo")
        return
    }

    val orDoc: Document = busqueda[0]

    if (confirmacionSobreSeleccion("¿Desea actualizar el titulo?")){
        println("Escriba el nuevo titulo")
        do {
            titulo = readln()
            val filtro = Filters.eq("titulo",titulo)
            val busqueda = collection.find(filtro).toList()
            if (busqueda.isNotEmpty()) {
                println("No es posible añadir dos juegos con el mismo titulo, escriba un titulo diferente")
            }
        }while(busqueda.isNotEmpty())
    }

    var genero = orDoc.getString("genero")
    if (confirmacionSobreSeleccion("¿Desea actualizar el genero?")){
        println("Escriba el nuevo genero")
        genero = readln()
    }

    var precio = orDoc.getDouble("precio")
    if (confirmacionSobreSeleccion("¿Desea actualizar el precio?")){
        var precioS : String?
        do {
            println("Escriba el nuevo precio")
            precioS = readln()
            if (checkDouble(precioS)==null){
                println("Debe introducir un valor válido")
            }
        }while (checkDouble(precioS)!=null)

        //Se obliga al precio a ser positivo
        precio=precioS.toDouble().absoluteValue
    }

    //Este proceso asegura que la fecha mantiene siempre el mismo formato e impide errores
    var fechaLanz = orDoc.getString("fechaLanzamiento")
    if (confirmacionSobreSeleccion("¿Desea actualizar la fecha de lanzamiento?")){
        println("Escriba el nuevo año de lanzamiento")
        val año = readln()
        println("Escriba el nuevo mes de lanzamiento")
        val mes = readln()
        println("Escriba el nuevo dia de lanzamiento")
        val dia = readln()

        fechaLanz="$dia/$mes/$año"
    }

    val doc = Document()
        .append("titulo",titulo)
        .append("genero",genero)
        .append("precio",precio)
        .append("fechaLanzamiento",fechaLanz)

    val docum = Document("\$set",doc)

    collection.updateOne(filtro,docum)

    println("Se ha actualizado la infomación del juego $titulo")
}