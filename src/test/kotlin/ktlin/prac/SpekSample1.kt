package ktlin.prac

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SpekSample1 : Spek({
    println("this is root")
    group("some group") {
        println("some group")
        test("some test") {
            println("some test")
        }
    }

    group("another group") {
        println("another group")
        test("another test") {
            println("another test")
        }
    }
})