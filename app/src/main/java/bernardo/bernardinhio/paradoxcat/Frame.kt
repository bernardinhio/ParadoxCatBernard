package bernardo.bernardinhio.paradoxcat

class Frame(
        val number : Int = 1,
        var score : Int = 0,
        var category : String = "",
        var textIsFrameCompleted : String = "",
        var titleInfo : String = "",
        val firstRollScore : Int = 0,
        val firstRollInfo : String = "",
        var secondRollScore : Int = 0,
        var secondRollInfo : String = "",
        var teamName : String = "",
        var teamTotalScore : Int = 0,
        var needsFirstBonus : Boolean = false,
        var needsSecondBonus : Boolean = false,
        var firstBonusReceived : Bonus = Bonus(),
        var secondBonusReceived : Bonus = Bonus(),
        var firstBonusGiven : Bonus = Bonus(),
        var secondBonusGiven : Bonus = Bonus()
){

}

class Bonus(
        var providerFrame : Frame? = null,
        var receiverFrame : Frame? = null,
        var providerRollNumber : Int = 0,
        var points : Int = 0,
        var message : String = "",
        var extraNumber : Int = 0
){

}

enum class FrameCategory(val categoryName : String){
    DEFAULT(""),
    STRIKE("Strike"),
    SPARE("Spare"),
    CLOSED("Closed")
}