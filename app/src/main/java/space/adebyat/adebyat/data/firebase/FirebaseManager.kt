package space.adebyat.adebyat.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import space.adebyat.adebyat.data.Author
import space.adebyat.adebyat.data.Creation
import space.adebyat.adebyat.data.Theme

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
//    fun getCreation(onSuccess: (List<Creation>) -> Unit
//                    , onFailure: (msg: String?) -> Unit
//                    , str: String){
//        db.collection("creation")
//            .whereEqualTo("name", str)
//            .get()
//            .addOnSuccessListener {
//                val mList = mutableListOf<Creation>()
//                it.documents.forEach { document ->
//                    document.toObject(Creation::class.java)?.let { creation->
//                        mList.add(creation)
//                    }
//                }
//                onSuccess.invoke(mList)
//            }
//            .addOnFailureListener {
//                onFailure.invoke(it.localizedMessage)
//            }
//    }

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
}