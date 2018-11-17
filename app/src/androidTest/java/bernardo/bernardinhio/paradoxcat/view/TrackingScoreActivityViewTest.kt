package bernardo.bernardinhio.paradoxcat.view

import android.support.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule

class TrackingScoreActivityViewTest {

    private lateinit var trackingScoreActivityView : TrackingScoreActivityView

    @Rule
    @JvmField
    val activityTestRule : ActivityTestRule<TrackingScoreActivityView> =
            ActivityTestRule<TrackingScoreActivityView>(TrackingScoreActivityView::class.java)

    @Before
    fun setUp() {
        trackingScoreActivityView = activityTestRule.activity
    }

    @After
    fun tearDown() {
        activityTestRule.finishActivity()
    }
}