package ktlin.adapter.moshi

class ArrayListAdapter {
    companion object Factory {
        fun create(): GenericCollectionAdapterFactory<java.util.ArrayList<*>> = GenericCollectionAdapterFactory(ArrayList::class.java) { ArrayList() }
    }
}