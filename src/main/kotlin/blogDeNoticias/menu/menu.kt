package blogDeNoticias.menu

import blogDeNoticias.control.*
import blogDeNoticias.model.Comentario
import blogDeNoticias.model.Noticia
import blogDeNoticias.model.Usuario
import com.mongodb.client.MongoCollection
import son


fun menu(
    usersColl: MongoCollection<Usuario>,
    noticiasColl: MongoCollection<Noticia>,
    comentariosColl: MongoCollection<Comentario>
) {
    var op : String

    println("Bienvenido al blog de noticias de Álvaro Gordillo Suano")
    do {
        println("¿Qué desea hacer?" +
                "\n1.Publicar una nueva noticia" +
                "\n2.Escribir comentario" +
                "\n3.Registrar nuevo usuario" +
                "\n4.Listar las noticias de publicadas por un usuario" +
                "\n5.Listar los comentarios de una noticia" +
                "\n6.Buscar noticias por etiquetas" +
                //TODO añade que el numero sea elegible por el usuario
                "\n7.Listar las 10 ultimas noticias publicadas" +
                "\n0.Salir")
        op = readln()
        when (op) {
            "1" -> {
                if (son("Ha seleccionado: Publicar una noticia")) {
                    publicar(usersColl,noticiasColl)
                }
            }
            "2" -> {
                if(son("Ha seleccionado: Escribir comentario")){
                    comentar(usersColl,noticiasColl)
                }
            }
            "3" -> {
                if (son("Ha seleccionado: Registrar un nuevo usuario")) {
                    registrar(usersColl)
                }
            }
            "4" -> {
                if (son("Ha seleccionado: Listar las noticias de un usuario")) {
                    listarPorUser(usersColl,noticiasColl)
                }
            }
            "5" -> {
                if (son("Ha seleccionado: Listar los comentario de una noticia")){
                    listarComentarios(noticiasColl,comentariosColl)
                }
            }
            "6" -> {
                if (son("Ha seleccionado: Buscar noticias por etiquetas")){
                    buscarPorEtiquetas(noticiasColl)
                }
            }
            "7" -> {
                if (son("Ha seleccionado: Listar las ultimas 10 noticias")){
                    listarUltimas(noticiasColl)
                }
            }
            "0" -> {
                if (son("Ha seleccionado: salir")){
                    println("Gracias por usar esta aplicación.")
                }
            }
            else -> {
                println("Opción invalida")
            }
        }
    }while(op!="0")
}