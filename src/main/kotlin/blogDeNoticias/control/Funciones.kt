package blogDeNoticias.control

import blogDeNoticias.model.Comentario
import blogDeNoticias.model.Direccion
import blogDeNoticias.model.Noticia
import blogDeNoticias.model.Usuario
import checkInt
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import pedirDatoSimple

/**
 * Función encargada de añadir un documento "Noticia" a la base de datos, se encarga de pedir los datos al usuario
 * asi como de confirmar que el usuario tiene permitido esta acción.
 */
fun publicar(usersColl: MongoCollection<Usuario>, noticiasColl: MongoCollection<Noticia>) {

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
    while (op.lowercase()!="n"){

        val tag = pedirDatoSimple("Introduzca el tag").lowercase()

        tags.add(tag)

        println("¿Desea añadir mas tags? (s/n)")

        op=readln()
    }

    val noticia = Noticia(titulo,cuerpo,user,tags.toList())

    try {
        noticiasColl.insertOne(noticia)
        println("Se ha publicado la noticia")
    }catch (_: Exception){
        println("No es posible publicar la noticia, intentelo de nuevo")
    }
}

/**
 * Función encargada de añadir un documento "Comentario" a la base de datos, de asegurarse que el usuario tiene permitido comentar
 * asi como encargase de pedir los datos al usuario
 */
fun comentar(usersColl: MongoCollection<Usuario>,noticiasColl: MongoCollection<Noticia>,comentariosColl: MongoCollection<Comentario>) {

    val user = buscarUserPorUsername(usersColl)

    if (!user.estado){
        println("El usuario ${user.username} no tiene permitido comentar")
        return
    }

    val noticia = buscarNoticiaPorTitulo(noticiasColl)._id

    val comentarioText = pedirDatoSimple("Escriba su comentario")

    val comentario = Comentario(user.username,noticia,comentarioText)

    comentariosColl.insertOne(comentario)

    println("Se ha añadido su comentario")
}

/**
 * Función encargada de añadir un documento "Usuario" a la base de datos, pidiendo datos al usuario y a su vez
 * asegurándose de que tanto el email como el username no están en uso
 */
fun registrar(usersColl: MongoCollection<Usuario>) {

    var email: String
    do {
        email = pedirDatoSimple("Escriba su email")
        val filtro = Filters.eq("_id",email)
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
    println("¿Desea añadir telefonos? (s/n)")
    var op = readln()
    while (op.lowercase()!="n") {

        var tel = pedirDatoSimple("Introduzca el numero")

        telefonos.add(tel)

        println("¿Desea parar de añadir telefonos? (s/n)")
        op = readln()
    }

    println("Procedemos a añadir su direccion")

    var calle = pedirDatoSimple("Introduzca su calle")

    var num = pedirDatoSimple("Introduzca su numero")

    var puerta = pedirDatoSimple("Introduzca su puerta")

    var cp = pedirDatoSimple("Introduzca su código postal")

    var ciudad = pedirDatoSimple("Introduzca su ciudad")

    val direccion = Direccion(calle,num,puerta,cp,ciudad)

    val user = Usuario(email,nombre,username,direccion,telefonos)

    usersColl.insertOne(user)

    println("Se ha registrado su usuario")
}

/**
 * Función simple que imprime todas las noticias que pertenezcan a un usuario.
 */
fun listarPorUser(usersColl: MongoCollection<Usuario>, noticiasColl: MongoCollection<Noticia>) {

    var user = buscarUserPorUsername(usersColl)

    val filtro = Filters.eq("user",user)

    val busqueda = noticiasColl.find(filtro)

    for (noticia in busqueda){
        println(noticia)
    }
}

/**
 * Función simple que imprime todos los comentarios que pertenezcan a una noticia
 */
fun listarComentarios(noticiasColl: MongoCollection<Noticia>, comentariosColl: MongoCollection<Comentario>) {

    val noticia = buscarNoticiaPorTitulo(noticiasColl)._id

    println("A continuación se muestran los comentarios:")

    val filtro = Filters.eq("idNoticia", noticia)

    val busqueda = comentariosColl.find(filtro).toList()

    for (resultado in busqueda){
        println(resultado)
    }
}

/**
 * Funcion encargada de buscar las noticias que contengan las etiquetas introducidas por el usuario.
 */
fun buscarPorEtiquetas(noticiasColl: MongoCollection<Noticia>) {
    val noticias = noticiasColl.find()

    val tags = mutableListOf<String>()

    do {
        var tag = pedirDatoSimple("Introduzca la etiqueta").lowercase()
        tags.add(tag)
        println("¿Desea añadir mas etiquetas? (s/n)")
        val op = readln().lowercase()
    }while (op.lowercase()!="n")

    val buscadas = mutableListOf<Noticia>()

    for (noticia in noticias){

        var buscada = true

        for (tag in tags){
            if (!noticia.tags.contains(tag)){
                buscada=false
                break
            }
        }

        if (buscada){
            buscadas.add(noticia)
        }
    }

    if (buscadas.isEmpty()){
        println("No hay noticias con las etiquetas seleccionadas")
    }else{
        println("La/s noticia/s que contiene/n las etiquetas son:")
        for (noticia in buscadas){
            println(noticia)
        }
    }
}

/**
 * Función encaragde de solicitar al usuario un número y mostrar las noticias mas recientes hasta llegar a ese número
 */
fun listarUltimas(noticiasColl: MongoCollection<Noticia>) {
    var n: Int? = null
    do {
        n = checkInt(pedirDatoSimple("Introduzca el numero de noticias de ultima hora que desea"))
        if (n==null || n<=0){
            println("Se ha introducido un valor invalido")
        }
    }while (n==null)

    noticiasColl.find()
        .sort(Sorts.descending("fechaPub"))
        .limit(n)
        .forEach {
            println(it)
        }
}


//Extras

/**
 * Funcion extra para facilitar el backend de la aplicación, busca noticias por título y obliga al usuario a seleccionar
 * una en caso de obtener varios resultados.
 */
fun buscarNoticiaPorTitulo(noticiasColl: MongoCollection<Noticia>) : Noticia{

    var noticia: Noticia? = null

    do {
        var titulo = pedirDatoSimple("Introduzca el titulo de la noticia")

        var filtro = Filters.eq("titulo",titulo)

        val busqueda = noticiasColl.find(filtro).toList()


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
                println("Elija entre los resultados 1 y ${busqueda.size}")
                op = readln()
                try {
                    val n = op.toInt()
                    noticia=busqueda[n]
                }catch (_: Exception){
                    println("Se ha introducido un valor no valido")
                }
            }while (noticia==null)
        }
    }while (noticia==null)

    println("Se ha seleccionado la noticia correctamente")

    return noticia
}

/**
 * Funcion extra para facilitar el backend de la aplicación, busca usuarios por username.
 */
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
    }while(user==null)

    return user
}
