package ktlin.adapter.moshi

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class GenericCollectionAdapter<TItem : Any, TCollection : MutableCollection<TItem>>(
    clazz: Class<TItem>,
    moshi: Moshi,
    private val createEmptyCollection: () -> TCollection
) : JsonAdapter<TCollection>() {
    private val typeAdapter = moshi.adapter<TItem>(clazz)

    @FromJson
    override fun fromJson(reader: JsonReader): TCollection? {
        val result = createEmptyCollection()

        reader.beginArray()

        while (reader.hasNext()) {
            val item = typeAdapter.fromJson(reader)
            if (item != null)
                result.add(item)
        }

        reader.endArray()

        return result
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: TCollection?) {
        writer.beginArray()

        if (value != null) {
            for (item in value) {
                typeAdapter.toJson(writer, item)
            }
        }

        writer.endArray()
    }
}
