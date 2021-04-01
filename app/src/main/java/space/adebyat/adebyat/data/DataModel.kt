package space.adebyat.adebyat.data

class DataModel {
    fun getAuthors(): List<Author>{
        var mList: MutableList<Author> = mutableListOf()
        repeat(20){
            mList.add(Author(it.toString(),"Name $it", "","", "date $it"))
        }
        return mList
    }

    fun getCreations(direction: String): List<Creation>{
        var mList: MutableList<Creation> = mutableListOf()

        repeat(20){
            mList.add(Creation("$it", "Creation name $it", ""))
        }
        return mList
    }


}