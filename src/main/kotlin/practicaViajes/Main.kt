package practicaViajes

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import io.github.cdimascio.dotenv.dotenv
import myPrintln
import org.bson.Document
import son

private fun main(){
    val dotenv = dotenv()

    val urlConnectionMongo = dotenv["URL_MONGODB"] ?: "error"

    val cluster = MongoClients.create(urlConnectionMongo)

    val db = cluster.getDatabase("ADATPrueba")

    val collection = db.getCollection("Viajes")

    viajes(collection)
}

private fun viajes(collection: MongoCollection<Document>){
    println("Bienvenido a la app de viajes AGS")
    var op:String
    do {
        println("¿Qué desea hacer?\n1.Añadir una nueva reserva\n2.Borrar una reserva\n3.Buscar una reserva\n4.Editar una reserva\n5.Mostrar todas las reservas\n0.Salir")
        op = readln()
        when (op) {
            "1" -> {
                println("Ha seleccionado añadir una reserva de viaje")
                añadirReserva(collection)
            }
            "2" -> {
                println("Ha seleccionado eliminar una reserva de viaje")
                eliminarReserva(collection)
            }
            "3" -> {
                println("Ha seleccionado buscar un viaje")
//                val l =buscarReserva(collection)
//                for (s in l){
//                    myPrintln(s)
//                }
            }
            "4" -> {
                println("Ha seleccionado editar una reserva de viaje")
                editarReserva(collection)
            }
            "5" ->{
                println("Ha seleccionado mostrar todas las reservas")
                reservas(collection)
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

private fun añadirReserva(c: MongoCollection<Document>){
    println("Escriba el origen del viaje:")
    val or = readln().lowercase()
    println("Escriba el destino del viaje:")
    val des = readln().lowercase()
    println("Escriba el precio del viaje:")
    //checkDouble TODO
    val prec = readln().toDouble()

    val doc = Document().append("Origen",or).append("Destino",des).append("Precio",prec)

    c.insertOne(doc)
}

private fun eliminarReserva(c: MongoCollection<Document>){
    println("Escriba el origen del viaje:")
    val or = readln().lowercase()
    println("Escriba el destino del viaje:")
    val des = readln().lowercase()

    val doc = Document().append("Origen",or).append("Destino",des)

    c.deleteOne(doc)
}

private fun buscarReserva(coll: MongoCollection<Document>, onlyOne:Boolean): List<String>{
    println("Escriba el origen del viaje:")
    val origen = readln().lowercase()
    println("Escriba el destino del viaje:")
    val destino = readln().lowercase()

    val filtro = Filters.and(Filters.eq("Origen",origen),Filters.eq("Destino",destino))

    val busqueda = coll.find(filtro)

    val lista = mutableListOf<String>()

    if (onlyOne){
        if (busqueda.first()!=null) {
            lista.add(busqueda.first().toString())
        }
    }else {
        for (resultado in busqueda) {
            lista.add(resultado.toString())
        }
    }
    return lista.toList()
}

private fun editarReserva(c: MongoCollection<Document>){
    val v = buscarReserva(c,true)
    var or: String? = null
    var des: String? = null
    var prec: String? = null
    if(son("¿Desea actualizar el origen?")){
        println("introduzca el nuevo valor")
        or = readln()
    }
    if (son("¿Desea actualizar el destino?")){
        println("introduzca el nuevo valor")
        des = readln()
    }
    if (son("¿Desea actualizar el precio?")){
        println("introduzca el nuevo valor")
        prec = readln()
    }


//TODO

//  updateDocument

}

private fun reservas(c: MongoCollection<Document>){
    val docConsulta = c.find()

    for (d in docConsulta) {
        myPrintln(d.toString())
    }
}
