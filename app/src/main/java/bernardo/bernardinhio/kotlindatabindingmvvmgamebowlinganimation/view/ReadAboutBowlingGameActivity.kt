package bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import bernardo.bernardinhio.kotlindatabindingmvvmgamebowlinganimation.R

class ReadAboutBowlingGameActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_about_bowling_game)

        title = "How to score hitting Pins?"
    }
}