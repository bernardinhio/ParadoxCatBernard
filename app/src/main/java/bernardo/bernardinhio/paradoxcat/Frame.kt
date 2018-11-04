package bernardo.bernardinhio.paradoxcat

class Frame(
        val frameNumber : Int,

        val firstRoll : Int,
        val secondRoll : Int,
        val score : Int,
        val category : Category,
        val isCompleted : Boolean,
        val firstBonus : Int,
        val secondBonus : Int){

}

enum class Category(val categoryName : String){
    STRIKE("Strike"),
    SPARE("Spare"),
    OPEN("Open")
}
