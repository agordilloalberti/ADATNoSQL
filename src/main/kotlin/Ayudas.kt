import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.github.cdimascio.dotenv.dotenv

/**
 * Función encargada de dar formato al string dado, está pensada para recibir un String proveniente de un Document
 */
fun documentReformat(s: String) : String{
    var r = "|"
    var string = s.split(",")
    string = string.drop(1)
    for (i  in string){
        val iS = i.split("=").toMutableList()
        if (iS[1].endsWith("}}")){
            iS[1] = iS[1].dropLast(2)
        }
        r+=iS[0].trim()+" : "+iS[1].trim()+" |- - -| "
    }
    r = r.dropLast(7)
    return r
}

/**
 * Función encargada de solicitar un dato de tipo string y asegurarse de que el usuario está conforme con su respuesta
 */
fun pedirDatoSimple(text: String): String{
    var r: String
    do {
        println(text)
        r = readln()
    }while (!confirmacionSobreSeleccion("\"$r\""))
    return r
}

/**
 * Función encargada de imprimir un mensaje y devolver un booleano según el input del usuario
 */
fun confirmacionSobreSeleccion(text:String): Boolean{
    println("$text ¿es correcto? (s/n)")
    return readln().lowercase()=="s"
}

/**
 *Función encargada de recibir un String y comprobar si es posible convertir a Double o no
 */
fun checkDouble(s: String): Double?{
    return try {
        s.toDouble()
    }catch (_: Exception){
        null
    }
}

/**
 *Función encargada de recibir un String y comprobar si es posible convertir a Int o no
 */
fun checkInt(s: String): Int?{
    return try {
        s.toInt()
    }catch (_: Exception){
        null
    }
}

/**
 * Objeto para acceder a mongo más facilmente
 */
object ConexionMongo {
    private val mongoClient: MongoClient by lazy {
        val dotenv = dotenv()
        val connectString = dotenv["URL_MONGODB"]

        MongoClients.create(connectString)
    }

    fun getDatabase(bd: String) : MongoDatabase {
        return mongoClient.getDatabase(bd)
    }

    fun close() {
        mongoClient.close()
    }
}