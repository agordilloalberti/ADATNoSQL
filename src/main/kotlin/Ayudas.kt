import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.github.cdimascio.dotenv.dotenv

/**
 * Función encargada de dar formato al string dado, esta pensada para recibir un String proveniente de un Document
 */
fun myPrintln(s: String){
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
    println(r)
}

/**
 * Función encargada de imprimir un mensaje y devolver un booleano según el input del usuario
 */
fun son(text:String): Boolean{
    println("$text (s/n)")
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