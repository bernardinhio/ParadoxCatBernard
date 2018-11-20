I developed this App when I was asked by some employers to show an example of my work on the technical, conceptual and architectural level. First of all, the ability to score a Bowling game is not an easy task from the logical level, so this needed a smart algorithm to put in place (see explanation of the game down)

I decided to use Databinding technology that will always notify the observers in the Layout about the changes in logic. I used Kotlin because it is the trendiest language for Android that can be combined with Java. In addition I fell in Love with Kotlin since I was doing the Udemy.com online courses in January 2018 then using it in my new classes. I used the MVVM architecture even for such small number of Activities and Fragments. In the ViewModel I have put all the logic, in the XML I used the ViewModel to set values.

In this App I tried to implement many features to show how I deal with them. I created a cool animation to show the result of the game at the end, it appears in fade in, shakes and go back then fade out showing the result of the competition between the 2 teams.

I also designed some buttons, icons and titles in png using Corel Draw for example, and especially I applied the “reactive programing” approach to constantly change something in the UI (such as hints / messages / colors / icons…) when the user tries to enter something or interact with the page.

I like to implement advanced user experience concepts and UI that really engage with users. It is called “UCD” or user-centered-designs. I believe it is the secret of success of many Apps. It is also part of my Master studies in Usability Engineering and my hobby to explore new concepts and interactions.

I will try to refactor or add some features for better “clean code” and better usability. I do that during my free time only because in regular time I am full time employee 

-------------------- The Game --------------------

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


Screenshots
Home screen
![Jome screen](https://user-images.githubusercontent.com/20923486/48657013-7a881780-ea2c-11e8-88d8-f0cbf201783a.png)

Frame Strike
![Strike Frame](https://user-images.githubusercontent.com/20923486/48657034-c1760d00-ea2c-11e8-8f35-cae844f33362.png)

Spare Frame
![Spare Frame](https://user-images.githubusercontent.com/20923486/48657064-31849300-ea2d-11e8-8629-c996df97cc86.png)

Non-special Frame
![Non-special Frame](https://user-images.githubusercontent.com/20923486/48657305-888b6780-ea2f-11e8-8fbf-7a08ac0a5342.png)

Animation result shaking
![Animation result shking](https://user-images.githubusercontent.com/20923486/48657147-cf785d80-ea2d-11e8-818a-c29dd5fef11f.png)

Animation result moving fading & moving back
![Animation result fading & moving back](https://user-images.githubusercontent.com/20923486/48657182-0a7a9100-ea2e-11e8-9a8b-e83804f8c576.png)

Share App
![Share App](https://user-images.githubusercontent.com/20923486/48657194-46155b00-ea2e-11e8-8112-8a60368d2d39.png)

stop competition
![Stop competion](https://user-images.githubusercontent.com/20923486/48657220-73620900-ea2e-11e8-94fc-4e2b9309af68.png)
