fun main() {
    val input = readLine()
    val number = input?.toInt()

    number?.let {
        if (it in listOf<Number>(0, 4, 10))  {
            println("Yes")
        } else {
            println("No")
        }
    }
}