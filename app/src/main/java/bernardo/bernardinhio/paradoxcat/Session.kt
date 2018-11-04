package bernardo.bernardinhio.paradoxcat

class Session(
        val sessionNumber : Int,
        val teamOneFramesList : ArrayList<Frame>,
        val teamTwoFramesList : ArrayList<Frame>,
        val teamOneSessionScore : Int,
        val teamTwoSessionScore : Int,
        val sessionWinner : Winner
){

}

enum class Winner(val team : String){
    TEAM_ONE("Team 1"),
    TEAM_TWO("Team 2")
}