package bernardo.bernardinhio.paradoxcat

class Frame(
        val number : Int = 1,
        val score : Int = 0,
        val category : String = "",
        val textIsFrameCompleted : String = "",
        val titleInfo : String = "",
        val firstRoll : Int = 0,
        val firstRollInfo : String = "",
        val secondRoll : Int = 0,
        val secondRollInfo : String = "",
        val teamName : String = "",
        val teamTotalScore : Int = 0,
        val firstBonus : Bonus = Bonus(),
        val secondBonus : Bonus = Bonus()
){

}

enum class Category(val categoryName : String){
    STRIKE("Strike"),
    SPARE("Spare"),
    CLOSED("Closed")
}

class Bonus(
        val points : Int = 0,
        val frameNumber : Int = 0,
        val fromFrameRoll : Int = 1
){

}

