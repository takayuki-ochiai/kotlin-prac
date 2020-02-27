package ktlin.prac

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root
import kotlin.test.assertEquals

// セットアップ関数を登録できたりもする
fun Root.setup() {
    println("setup!!!!")
}

class SpekSample3 : Spek({
    setup()

    group("Cloth") {
        // テストで使うfixtureデータの準備
        // テストブロックに入る度にオブジェクトが作られる
        val cloth by memoized{ Cloth("Spek T shirt", 100) }

        test("characters") {
            println(System.identityHashCode(cloth))
            assertEquals("Spek T shirt", cloth.characters)
        }

        test("price") {
            println(System.identityHashCode(cloth))
            assertEquals(100, cloth.price)
        }
    }
})

