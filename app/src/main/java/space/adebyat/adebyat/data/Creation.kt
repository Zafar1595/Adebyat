package space.adebyat.adebyat.data

class Creation(
        var id: String = "",
        var name: String = "",
        var content: String = "",
        var audioUrl: String = "",
        var author: String = "",
        var direction: String = "",
        var genre: String = "",
        var theme: MutableList<String> = mutableListOf(),
        var period: String = "",
        var viewed: Int = 0
)