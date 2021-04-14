package space.adebyat.adebyat.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import space.adebyat.adebyat.data.*

class FirebaseManager(private val db: FirebaseFirestore) {

    fun getAuthors(onSuccess: (List<Author>) -> Unit
                   , onFailure: (msg: String?) -> Unit) {
        db.collection("autors").get()
                .addOnSuccessListener {
                    val mList = mutableListOf<Author>()
                    it.documents.forEach { document ->
                        document.toObject(Author::class.java)?.let { author->
                            mList.add(author)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getAllCreations(onSuccess: (List<Creation>) -> Unit
                        , onFailure: (msg: String?) -> Unit) {

        db.collection("creation")
                .get()
                .addOnSuccessListener {
                    val mList = mutableListOf<Creation>()
                    it.documents.forEach { document ->
                        document.toObject(Creation::class.java)?.let { creation->
                            mList.add(creation)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getThemes(onSuccess: (List<Theme>) -> Unit
                  , onFailure: (msg: String?) -> Unit){
        db.collection("theme").get()
                .addOnSuccessListener {
                    val mList = mutableListOf<Theme>()
                    it.documents.forEach { document ->
                        document.toObject(Theme::class.java)?.let { theme->
                            mList.add(theme)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getGenre(onSuccess: (List<Genre>) -> Unit,
                 onFailure: (msg: String?) -> Unit){
        db.collection("Genre").get()
                .addOnSuccessListener {
                    val mList: MutableList<Genre> = mutableListOf()
                    it.documents.forEach { document ->
                        document.toObject(Genre::class.java)?.let { genre ->
                            mList.add(genre)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getDirections(onSuccess: (List<Direction>) -> Unit,
                      onFailure: (msg: String?) -> Unit){
        db.collection("directions").get()
                .addOnSuccessListener {
                    val mList: MutableList<Direction> = mutableListOf()
                    it.documents.forEach { document ->
                        document.toObject(Direction::class.java)?.let { direction ->
                            mList.add(direction)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getPeriod(onSuccess: (List<Period>) -> Unit,
                  onFailure: (msg: String?) -> Unit){
        db.collection("period").get()
                .addOnSuccessListener {
                    val mList: MutableList<Period> = mutableListOf()
                    it.documents.forEach { document ->
                        document.toObject(Period::class.java)?.let {  period ->
                            mList.add(period)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getCreations(onSuccess: (List<Creation>) -> Unit
                     , onFailure: (msg: String?) -> Unit
                     , str: String) {
        var columnName = if(str == "Poeziya" || str == "Proza"){
            "direction"
        }else{
            "author"
        }

        db.collection("creation")
                .whereEqualTo(columnName, str)
                .get()
            .addOnSuccessListener {
                val mList = mutableListOf<Creation>()
                it.documents.forEach { document ->
                    document.toObject(Creation::class.java)?.let { creation->
                        mList.add(creation)
                    }
                }
                onSuccess.invoke(mList)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}