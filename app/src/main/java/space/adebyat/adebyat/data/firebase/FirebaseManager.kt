package space.adebyat.adebyat.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import space.adebyat.adebyat.data.Autor
import space.adebyat.adebyat.data.Creation

class FirebaseManager(private val db: FirebaseFirestore) {

    fun getAuthors(onSuccess: (List<Autor>) -> Unit, onFailure: (msg: String?) -> Unit) {
        db.collection("autors").get()
                .addOnSuccessListener {
                    val mList = mutableListOf<Autor>()
                    it.documents.forEach { document ->
                        document.toObject(Autor::class.java)?.let { author->
                            mList.add(author)
                        }
                    }
                    onSuccess.invoke(mList)
                }
                .addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
    }

    fun getCreation(onSuccess: (List<Creation>) -> Unit
                    , onFailure: (msg: String?) -> Unit
                    , str: String) {
        var columnName: String = ""
        if(str == "Poeziya" || str == "Proza"){
            columnName = "direction"

        }else{
            columnName = "author"
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