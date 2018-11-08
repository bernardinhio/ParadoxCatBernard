package bernardo.bernardinhio.paradoxcat.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bernardo.bernardinhio.paradoxcat.R

class FragmentAnimationCongratulations : Fragment(){
    var tvCongratulationsMessage : TextView? = null
    var tvResultsTeamOne : TextView? = null
    var tvResultsTeamTwo : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewInflated : View = inflater.inflate(R.layout.layout_animation_result_game, container, false)
        tvCongratulationsMessage = viewInflated.findViewById<TextView>(R.id.congratulation_winner)
        tvResultsTeamOne = viewInflated.findViewById<TextView>(R.id.result_team_one)
        tvResultsTeamTwo = viewInflated.findViewById<TextView>(R.id.result_team_two)
        return viewInflated
    }
}
