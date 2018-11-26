I started developing this App when I was asked by one employer in Munich and others to show an example of my work such as technical, conceptual and architectural level. As I am employee full time I did the App progressively during early mornings or evenings, and I am still doping it. Sure the code needs cleaning and more optimization for better readability, but the goal is to show my abilities after all. 

The idea of scoring a Bowling game is not an easy task on the logical level, so this needed a smart algorithm to put in place (see explanation of the game down). In addition when I made the reactive concept, this expanded a lot the code because every action on the UI required feedback or guidance in the flow of the bowling game.

I didn’t except it will be such huge task of programming, but honestly I enjoyed because finally I can put something online about my abilities.


--------------------  Tech things -------------------- 

I decided to use Databinding technology that will always notify the observers and the Layout about the changes in logic. I used Kotlin because it is the trendiest language for Android right now, but I could have done in Java too, however Kotlin is more powerful and concise. In reality I love it since I was doing the Udemy.com online courses in January 2018. I use it since then then for the new classes. 

I used the MVVM architecture even for such small number of Activities and Fragments. In the ViewModel I have put all the logic (that should also be tested with some Unit tests that I will add later). In the XML I used a viewModel object to get its fields and set them as values in the layout such as color / text / click listeners

In this App I tried to implement many features to show to any employer how I deal with them. But as I said, the whole code needs some “clean code” even it is working perfectly. 

I made a lot of work on the usability of things, such as drawers on enabled and disabled and different styling for background changes and colors
I also designed some Labels in png using Corel Draw for example. As I applied the “reactive programing” approach to constantly change something in the UI (such as hints / messages / colors / icons…) when the user tries to enter something or interact with the page.

I like to implement advanced user experience concepts and UI that really engage with users. It is my hobby to think of the user-centered-designs. I believe it is the secret of success of many Apps. It is also part of my Master studies in Usability Engineering. I like the Android material design classes.

I will try to refactor or add some features in the future during my free time, meanwhile I suggest to you to install the App and start using it to explore its features.


--------------------  Animation -------------------- 

In Summer 2018 is started doing animations, such as for notifications and layouts floating, so I created a cool animation to show the result of the game at the end, it appears in fade in, shakes (rapid short +3degrees rotations left-right), then goes back, then fade out showing the result of the competition between the 2 teams. (see screenshots down)


--------------------  The Game -------------------- 

![bowling_game_cover](https://user-images.githubusercontent.com/20923486/48757534-5da64b00-ec9d-11e8-89d8-8521fc3ee388.jpg)

Scoring the Bowling game needs the players to revisit the previous shots in order to re-calculate the scores. This might become cumbersome when done on papers because the logic is somehow complex. Usually at the Bowling location, the software shows on screen the effect of every new shot on scoring.

Bowling is a game played in 10 Frames. A “Frame” is the turn of a player to try to hit 10 “Pins”. The player has 2 trials, so called “Rolls” to try to knock down the 10 “Pins”.

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


--------------------  Major challenges -------------------- 

The first challenge was to be able to put all the logic in one Activity that uses Databinding and allows switching between 2 teams (or 2 players). To think about when switching is allowed or needed, and when the current team / player should continue playing.

Another point is when and how to switch to the phase of getting Extra rolls, and how to show feedback to users about where they are right now in the game, or how their input of score affects the previous “Frames” that they have played before and that are not shown anymore in the current screen.

I decided to make entering score is 2 phases of clicking on a submit button, one for saving the entering score and checking its validity, then showing how it affects the previous Frames, and one last click on the same submit button (with different label) to give the turn to the next player / team and calculate overall scores


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

Animation result shaking

![Animation result shking](https://user-images.githubusercontent.com/20923486/48657147-cf785d80-ea2d-11e8-818a-c29dd5fef11f.png)

Animation result shaking / moving fading & moving back

![Animation result fading & moving back](https://user-images.githubusercontent.com/20923486/48657182-0a7a9100-ea2e-11e8-9a8b-e83804f8c576.png)

Share App

![Share App](https://user-images.githubusercontent.com/20923486/48657194-46155b00-ea2e-11e8-8112-8a60368d2d39.png)

Stop competition

![Stop competion](https://user-images.githubusercontent.com/20923486/48657220-73620900-ea2e-11e8-94fc-4e2b9309af68.png) 


--------------------  A first / easiest scenario --------------------

When I was developing the logic, it wasn’t easy in the beginning till I made scenario that can be applied on my App, it is one of the most famous, when the player always hits the 10 Pins during the regular game and during all the future Extra rolls that he will have. I made this table on Word and exported in image. 

My scenario 1

![scenario1](https://user-images.githubusercontent.com/20923486/49044007-df7e0300-f1cc-11e8-994e-4f04d6d3081c.jpg)


--------------------  Espresso test --------------------

In such a project that has quiet complex Logic for calculating scores, I need Espresso Test. I started doing it to test the Activity opening, then I refactored the concept / UI and logic so now I need to add some more test methods. . I was using the 3 tables of the 3 scenarios that I draw on paper and I was manually testing the scoring till now. I have setup the 3 scenarios of Espresso test for 3 matches of Bowling, and I need now to continue developing them

