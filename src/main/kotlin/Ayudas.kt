import kotlin.math.absoluteValue

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

fun son(text:String): Boolean{
    println("$text (s/n)")
    return readln().lowercase()=="s"
}

fun checkDouble(s: String): Double?{
    return try {
        s.toDouble().absoluteValue
    }catch (e: Exception){
        null
    }
}