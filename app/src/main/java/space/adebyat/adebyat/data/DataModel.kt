package space.adebyat.adebyat.data

class DataModel {
    fun getAuthors(): List<Autor>{
        var mList: MutableList<Autor> = mutableListOf()
        repeat(20){
            mList.add(Autor(it.toString(),"Name $it", "","", "date $it"))
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