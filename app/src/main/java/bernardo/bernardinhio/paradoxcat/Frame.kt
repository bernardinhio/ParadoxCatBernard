package bernardo.bernardinhio.paradoxcat

class Frame(
        val number : Int = 1,
        var score : Int = 0,
        val category : String = "",
        val textIsFrameCompleted : String = "",
        val titleInfo : String = "",
        val firstRoll : Int = 0,
        val firstRollInfo : String = "",
        val secondRoll : Int = 0,
        val secondRollInfo : String = "",
        val teamName : String = "",
        val teamTotalScore : Int = 0,
        var needsFirstBonus : Boolean = false,
        var needsSecondBonus : Boolean = false,
        var firstBonus : Bonus = Bonus(),
        var secondBonus : Bonus = Bonus()
){

}

enum class FrameCategory(val categoryName : String){
    DEFAULT(""),
    STRIKE("Strike"),
    SPARE("Spare"),
    CLOSED("Closed")
}

class Bonus(
        var points : Int = 0,
        var frameNumber : Int = 0,
        var fromFrameRoll : Int = 1,
        var bonusMessage : String = ""
){

}