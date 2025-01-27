package blogDeNoticias.control

import blogDeNoticias.model.Comentario
import blogDeNoticias.model.Direccion
import blogDeNoticias.model.Noticia
import blogDeNoticias.model.Usuario
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import pedirDatoSimple

fun publicar(usersColl: MongoCollection<Usuario>, noticiasColl: MongoCollection<Noticia>) {

    println("Introduzca el usuario que va a publicar")
    val user = buscarUserPorUsername(usersColl)

    if (!user.estado){
        println("Su usuario no tiene permitido publicar")
        return
    }

    val titulo = pedirDatoSimple("Introduzca el titulo de la noticia")

    val cuerpo = pedirDatoSimple("Introduzca el cuerpo de la noticia")

    val tags = mutableListOf<String>()

    println("¿Desea añadir tags? (s/n)")
    var op = readln()
    while (op!="n"){

        val tag = pedirDatoSimple("Introduzca el tag")

        tags.add(tag)

        println("¿Desea añadir mas tags? (s/n)")

        op=readln()
    }

    val noticia = Noticia(titulo,cuerpo,user,tags.toList())

    noticiasColl.insertOne(noticia)
}

//TODO
fun comentar(usersColl: MongoCollection<Usuario>, noticiasColl: MongoCollection<Noticia>) {

}

fun registrar(usersColl: MongoCollection<Usuario>) {

    var email: String
    do {
        email = pedirDatoSimple("Escriba su email")
        val filtro = Filters.eq("username",email)
        val busqueda = usersColl.find(filtro).toList()
        if (busqueda.isNotEmpty()) {
            println("Este email ya esta en uso, escriba uno diferente")
        }
    }while(busqueda.isNotEmpty())

    var nombre = pedirDatoSimple("Escriba su nombre")

    var username: String
    do {
        username = pedirDatoSimple("Escriba su nombre de usuario")
        val filtro = Filters.eq("username",username)
        val busqueda = usersColl.find(filtro).toList()
        if (busqueda.isNotEmpty()) {
            println("Este nombre de usuario ya esta en uso, escriba uno diferente")
        }
    }while(busqueda.isNotEmpty())

    var telefonos = mutableListOf<String>()
    println("¿Desea añadir telefonos?")
    var op = readln()
    while (op!="n") {

        var tel = pedirDatoSimple("Introduzca el numero")

        telefonos.add(tel)

        println("¿Desea parar de añadir telefonos? (s/n)")
        op = readln()
    }

    var calle = pedirDatoSimple("Introduzca su calle")

    var num = pedirDatoSimple("Introduzca su numero")

    var puerta = pedirDatoSimple("Introduzca su puerta")

    var cp = pedirDatoSimple("Introduzca su código postal")

    var ciudad = pedirDatoSimple("Introduzca su ciudad")

    val direccion = Direccion(calle,num,puerta,cp,ciudad)

    val user = Usuario(email,nombre,username,direccion,telefonos)

    usersColl.insertOne(user)
}

fun listarPorUser(usersColl: MongoCollection<Usuario>, noticiasColl: MongoCollection<Noticia>) {

    var user = buscarUserPorUsername(usersColl)

    val filtro = Filters.eq("user",user)

    val busqueda = noticiasColl.find(filtro)

    for (noticia in busqueda){
        println(noticia)
    }
}

fun listarComentarios(noticiasColl: MongoCollection<Noticia>, comentariosColl: MongoCollection<Comentario>) {

    val noticia = buscarNoticiaPorTitulo(noticiasColl)

    println("A continuación se muestran los comentarios:")

    val filtro = Filters.eq("Noticia", noticia)

    val busqueda = comentariosColl.find(filtro).toList()

    for (resultado in busqueda){
        println(resultado)
    }
}

//TODO
fun buscarPorEtiquetas(noticiasColl: MongoCollection<Noticia>) {

}

fun listarUltimas(noticiasColl: MongoCollection<Noticia>) {
    noticiasColl.find()
        .sort(Sorts.descending("fechaPub"))
        .limit(10)
        .forEach {
            println(it)
        }
}


//Extras

fun buscarNoticiaPorTitulo(noticiasColl: MongoCollection<Noticia>) : Noticia{
    var titulo = pedirDatoSimple("Introduzca el titulo de la noticia")

    var filtro = Filters.eq("titulo",titulo)

    val busqueda = noticiasColl.find(filtro).toList()

    var noticia: Noticia? = null

    if (busqueda.isEmpty()){

        println("No se han encontrado noticias con ese titulo")

    }else if(busqueda.size==1){

        noticia = busqueda[0]

    }else{

        println("Se han encontrado varios resultados, se van a mostrar todos los resultados, seleccione cual desea")

        for (resultado in busqueda){
            println("1.$resultado")
        }

        var op: String

        do {
            println("Elija entre los resultado 1 y ${busqueda.size}")
            op = readln()
            try {
                val n = op.toInt()
                noticia=busqueda[n]
            }catch (e: Exception){
                println("Se ha introducido un valor no valido")
            }
        }while (noticia==null)
    }
    println("Se ha seleccionado la noticia correctamente")

    return noticia!!
}

fun buscarUserPorUsername(usersColl: MongoCollection<Usuario>): Usuario{
    var user: Usuario? = null
    do {
        var username = pedirDatoSimple("Introduzca el nombre de usuario")
        val filtro = Filters.eq("username",username)
        var busqueda = usersColl.find(filtro).toList()
        if (busqueda.isNotEmpty()) {
            user = busqueda[0]
        }else{
            println("El usuario \"$username\" no existe")
        }
    }while(busqueda.isEmpty())

    return user!!
}
