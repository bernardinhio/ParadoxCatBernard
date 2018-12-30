package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view

object DataProviderForScoreEntries {

    // mock didBothTeamsReachedTenFrames to reach 3 Frame
    val sequenceEntriesTest1 : Array<String> = arrayOf(
            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c"
    )

    // scenario where all the entries of a player are always 10 knocked Pins
    val sequenceEntriesTest2 : Array<String> = arrayOf(
            // Frame 1
            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            // frame 2
            "10", "c", "c",
            "9", "c",
            "0", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "10", "c", "c",
            "5", "c",
            "3", "c", "c",

            "10", "c", "c",
            "6", "c",
            "4", "c", "c",

            "10", "c", "c",
            "0", "c",
            "5", "c", "c",

            "10", "c", "c",
            "4", "c",
            "3", "c", "c",

            "10", "c", "c",
            "7", "c",
            "2", "c", "c",

            "10", "c", "c",
            "3", "c",
            "3", "c", "c",

            "10", "c", "c",
            "3", "c",
            "4", "c", "c",

            // all Extras
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c",
            "10", "c", "c"
    )

    // scenario provided by kotlindatabindingmvvmgamebowlinganimation
    val sequenceEntriesTest3 : Array<String> = arrayOf(
            // Frame 1
            "1", "c",
            "4", "c",
            "10", "c", "c",

            // Frame 2
            "4", "c",
            "5", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "5", "c",
            "5", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "0", "c",
            "1", "c",
            "10", "c", "c",

            "7", "c",
            "3", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            // Frame 10
            "2", "c",
            "8", "c",
            "10", "c", "c",

            // extra
            "6", "c", "c" // extra
    )

    // scenario provided here https://www.youtube.com/watch?v=YgIrYUGiVtc
    val sequenceEntriesTest4 : Array<String> = arrayOf(
            // Frame 1
            "8", "c",
            "2", "c",
            "10", "c", "c",

            // Frame 2
            "10", "c", "c",
            "10", "c", "c",

            "7", "c",
            "0", "c",
            "10", "c", "c",

            "6", "c",
            "4", "c",
            "10", "c", "c",

            "9", "c",
            "1", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            "8", "c",
            "2", "c",
            "10", "c", "c",

            "2", "c",
            "8", "c",
            "10", "c", "c",

            "10", "c", "c",
            "10", "c", "c",

            // Frame 10
            "9", "c",
            "1", "c",
            "10", "c", "c",

            "10", "c", "c" // extra
    )

}
