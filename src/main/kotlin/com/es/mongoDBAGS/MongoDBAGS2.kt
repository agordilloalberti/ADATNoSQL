package com.es.mongoDBAGS

import com.mongodb.client.MongoClients
import com.mongodb.client.model.Filters
import io.github.cdimascio.dotenv.dotenv
import myPrintln
import org.bson.Document

fun main() {

    //Añadimos el archivo
    val dotenv = dotenv()

    //Accedemos a la variable guardada
    val urlConnectionMongo = dotenv["URL_MONGODB"] ?: "error"

    //Conectamos con mongo
    val cluster = MongoClients.create(urlConnectionMongo)

    //Accedemos a la base de datos
    val db = cluster.getDatabase("ADATPrueba")

    //Obtenemos la colección
    val collection = db.getCollection("Prueba")

    //Creamos el documento a insertar
    val doc = Document().append("saludo","iiiiiih").append("saludo","Andaluz")

    //Insertamos en la base de datos
    collection.insertOne(doc)

    //Creamos el filtro
    val filtroPorNombre = Filters.eq("saludo","hola mundoh")

    //Obtenemos los  documentos que cumplan el filtro
    val busqueda = collection.find(filtroPorNombre)

    //Escribimos por pantalla los resultados
    busqueda.forEach{myPrintln(it.toString())}

    //Realizamos una consulta
//    val docConsulta = collection.find()
//
//    for (d in docConsulta) {
//        myPrintln(d.toString())
//    }
}
