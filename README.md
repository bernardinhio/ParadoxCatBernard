I started developing this App during free hours because I wanted to show the beauty of the concept of Databinding in Kotlin when it is implemented in an MVVM architecture pattern, but also to show how reactive programing is beautiful by simply doing Databinding(1-way and 2-ways). I was doing these tasks at my past job but I felt that I discovered the big thing because I am personally really very interested in UX, Usability and the approach of reactive programming that filters or show specific information or change specific small things in the UI when something in the system changes. Also another big goal of making this project was to show what I have learned in summer 2018 so I made an example of floating layout acting as animation-notification that shows the final result of a game!

So this App is a game, most of the features are done, and the App is 100% working as it’s supposed to work (after long analysis of the complexity of scoring and the use-cases and limitations and game exceptions.

I did not use an existing code or any Library about bowling or Pins game, but I developed this algorithm and code in Kotlin after a long long analysis and work. The code however is quiet long at one place, and should absolutely be refactored, but the complexity of the game made me make so many pushes in order to fix the scoring or the flow of the game in every case. That say I kept the code long in one class but I know that this should be divided into many classes…

I made the concept of this App to allow 2 teams to compete and track the score of their Bowling / Pins game. When I made the reactive concept, this expanded a lot the code because every action on the UI required feedback or guidance in the flow of the game. I also designed logos and labels used in this App using Corel draw or simply Paint

All this game happens in only one Activity that is very Reactive

I didn’t except it will be such huge task of programming, but honestly I enjoyed because finally I can put something online about my abilities to deal with Mvvm, Databinding, Reactive approach, Kotlin 


--------------------  Tech things -------------------- 

What the secret of the power of Mvvm and Databinding?
Mvvm is basically and Observer-Observable Architectural pattern. The ViewModel (vm) acts as a middle-man between the Model (M) and the View (V). This middle-man is acting as an Observable for the View and at the same time as an Observer of the Model !!

Now the Databinding allows the View (Activity & Xml) to be “binded” in 1-way (just get data from the ViewModel) or in 2-ways (get data from the ViewModel and send data as well to the ViewModel).

Well, we write the data variables directly inside the Xml file! And these variables will control many parameters of every layout or View inside that Xml, such as text value, or color, or background or visibility…

This what makes the UI very reactive, and this what makes us save a lot of code that we write for listeners! So we can use many listeners of all componenets such as of an EditText (after text change) or buttons (on click) by passing Lambda expressions to call methods from inside the Xml.

All this beauty is amplified when we use Kotlin not Java, because we can use the automatically generated setters & getters of the field variables that we write inside the ViewModel. So by the way Mvvm is the recommended architecture by Google, and we can use any other method of reactive programming such as RxJava for example. 


--------------------  Animation -------------------- 

In Summer 2018 is started doing animations, such as for notifications and layouts floating on the screen, so I created a cool animation in this project to show the result of the game when it ends. It appears in fade in, shakes (rapid short +3degrees rotations left-right), then goes back, then fade out showing the result of the competition between the 2 teams. (see screenshots down)

Animation result shaking / moving fading & moving back

![Animation result fading & moving back](https://user-images.githubusercontent.com/20923486/48657182-0a7a9100-ea2e-11e8-9a8b-e83804f8c576.png)


--------------------  The Game -------------------- 

Scoring the Bowling game needs the players to revisit the previous shots in order to re-calculate the scores. This might become cumbersome when done on papers because the logic is somehow complex. Usually at the Bowling location, the software shows on screen the effect of every new shot on scoring.

Bowling is a game played in 10 Frames. A “Frame” is the turn of a player to try to hit 10 “Pins”. The player has 2 trials, so called “Rolls” to try to knock down the 10 “Pins”.

![bowling_game_cover](https://user-images.githubusercontent.com/20923486/48757534-5da64b00-ec9d-11e8-89d8-8521fc3ee388.jpg)

If the player knocks down the 10 Pins from the first Roll, then the Frame ends, he doesn’t have to do another Roll, and the next player will have the turn. Such Frame is called “Strike”.

If a player knocks down the 10 Pins using his 2 allowed Rolls, then this Frame also ends and the next player has to play. Such Frame is called “Spare”.

If a player doesn’t succeed in knocking down the 10 Pins in 2 Rolls, then also the Frame ends and the next player has to play. Such Frame is a not a special Frame, it is called “Closed”.

When the Frames are special Frames, means “Strike” or “Spare”, the scoring of those Frames is not simply the sum of the 2 Rolls (in case of Spare) or 1 Roll (in case of Strike) that the player used to knock down the 10 Pins, the score will also have “Bonus”.

In the case of “Strike”, the score of that Frame is equals to 10 (all 10 Pins knocked down) + the number of Pins that this player hits in the next 2 Rolls that will happen in the next 1 or 2 Frames.

In the case of “Spare”, the score of the Frame is equals to 10 (all 10 Pins knocked down) + the number of Pins that this player hits in the next 1 Roll. This will certainly happen in the next Frame of that player.

In the case of “Closed” Frame, the Player will not have any “Bonus” Points from the next Rolls, so the score of that Frame will be the sum of the Pins knocked down in the first Roll and the 2nd Roll. This is always less than 10.

This means a Strike Frame score = 10 + 1st next Roll + 2nd next Roll. In the best scenario, such Frame might have a total score of 30 if the next 2 Rolls are also each 10 Pins knocked down.

In the case of “Spare”, the total score of a Frame = 10 + first next Roll. In a best scenario, such Frame might have a total score of 20 if the next Roll is 10 Pins knocked.

When a Frame is played, it is scored normally as the sum of the 2 Rolls unless the 1st Roll hits all the 10 Pins or the 1st and the 2nd Rolls together hit all the 10 Pins. This require adding bonuses from the future Rolls.

The score of a game for one player is the sum of the scores of all the 10 Frames once all the 10 Frames are finished and no Frame requires future Bonus anymore. 

When the last Frames of the game require future bonuses equals to the number of Pins hit in the next Frames but also the player has reached 10 Frames already, then the player will be given extra Rolls beyond the 10 Frames to finish his previous Frames’ scores and be able to finish the game scoring.

In a best scenario, the player hits always the 10 Pins from the 1st Roll, this means every Frame requires 2 extra Bonuses collected from the 2 next Rolls, this means each Frame will be scored 30, this means the game for that player will be scored 10 X 30 = 300. It is the highest score that can ever be reached.

The general rules of the game are explained here:
https://www.youtube.com/watch?v=E2d8PizMe-8

A scenario of a typical game scoring is described here:
https://www.youtube.com/watch?v=YgIrYUGiVtc 


--------------------  Future tasks in this project --------------------

There are some parts (features) that I wanted to develop such as reviewing the history of the game (all previous steps and the evolution of scorings) in previous rounds, are not yet developed. But hopefully one day I will continue working on this project, also refactoring some code to spread it among specialized classes for better readability. I also want to continue the different scenarios of Espresso test that I did partially.


--------------------  Major challenges -------------------- 

- The first challenge was to be able to put all the logic in one Activity that uses Databinding and allows switching between 2 teams (or 2 players). To think about when switching is allowed or needed, and when the current team / player should continue playing.

- Another point is when and how to switch to the phase of getting Extra rolls, and how to show feedback to users about where they are right now in the game, or how their input of score affects the previous “Frames” that they have played before and that are not shown anymore in the current screen.

- I decided to make entering score is 2 phases of clicking on a submit button, one for saving the entering score and checking its validity, then showing how it affects the previous Frames, and one last click on the same submit button (with different label) to give the turn to the next player / team and calculate overall scores


--------------------  Screenshots-------------------- 

Home screen

![screenshot_1542832534](https://user-images.githubusercontent.com/20923486/48867119-7580ea80-edd5-11e8-91aa-3b81fddeabc5.png) 

Frame Strike

![screenshot_1542832628](https://user-images.githubusercontent.com/20923486/48997713-2201fa00-f151-11e8-9f9b-86a8ac3286cb.png) 

Spare Frame

![screenshot_1542832780](https://user-images.githubusercontent.com/20923486/48997755-43fb7c80-f151-11e8-99e6-ae533628993c.png) 

Bonus given to previous Frames

![screenshot_1543215695](https://user-images.githubusercontent.com/20923486/48997835-86bd5480-f151-11e8-89dd-e1c4092e27a4.png) 

![screenshot_1543215832](https://user-images.githubusercontent.com/20923486/48997913-d69c1b80-f151-11e8-893c-73d301af528b.png) 

Entering Extra Rolls phase

![screenshot_1550652355](https://user-images.githubusercontent.com/20923486/53078433-7fa9a680-34f4-11e9-9b35-cb76f85c039a.png)

Scoring Extra Roll

![screenshot_1550652404](https://user-images.githubusercontent.com/20923486/53078527-aec01800-34f4-11e9-857f-fa29a0334cae.png) 

Animation result shaking

![Animation result shking](https://user-images.githubusercontent.com/20923486/48657147-cf785d80-ea2d-11e8-818a-c29dd5fef11f.png) 

Share App

![Share App](https://user-images.githubusercontent.com/20923486/48657194-46155b00-ea2e-11e8-8112-8a60368d2d39.png) 

Stop competition

![Stop competion](https://user-images.githubusercontent.com/20923486/48657220-73620900-ea2e-11e8-94fc-4e2b9309af68.png) 


--------------------  A first / easiest scenario --------------------

This scenario is the most known scenario that we can find on Internet when we try to learn Bowling. When I was developing the logic, it wasn’t easy in the beginning till I made scenario that can be applied on my App, it is one of the most famous, when the player always hits the 10 Pins during the regular game and during all the future Extra rolls that he will have. I made this table on Word and exported in image. 

My scenario 1

![scenario1](https://user-images.githubusercontent.com/20923486/49044007-df7e0300-f1cc-11e8-994e-4f04d6d3081c.jpg) 


--------------------  Espresso test --------------------

In such a project that has quiet complex Logic for calculating scores, I did Espresso Test. I started doing it to test the Activity opening, and then I refactored the concept / UI and logic. I was using the 3 tables of the 3 scenarios that I draw on paper and I was manually testing the scoring till now. I have setup the 3 scenarios in the Test Class for 3 matches of Bowling, so now I need to add some more test methods during my free time.

--------------------  I planned 3 Espresso scenarios for game-----------------

Using one Youtube video that explains the scoring of Bowling, I collected the possible inputs (number of Pins hit) and the output (scores). I compiled these 3 scenarios on a paper because I have to use in my espresso test

![espresso data](https://user-images.githubusercontent.com/20923486/50736687-c0f22b00-11c0-11e9-8272-3afcc79604a1.JPG) 
