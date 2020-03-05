package ktlin.config

class Config {
    fun env(key: Env): String {
        // 本当はSystem.getenv(key.value)とかやるけど、Kotlinまだコンテナで動かしていないのでベタ書きする
        return when (key) {
            Env.DB_HOST -> "kotlin_test"
            Env.DB_NAME -> "kotlin_test"
            Env.DB_PASSWORD -> "kotlin_test"
            Env.DB_USER -> "kotlin_test"
            Env.DB_PORT -> "3306"
        }
    }
}