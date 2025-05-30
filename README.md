# More or less, less is more

We developed a game for the course Programming I.

![screenshot](screenshot.jpg)

## Instructions

- Select a difficulty preset (Easy, Medium, Hard)
- Press the `Start new game!` button

### Objective of the game

The sum of all the numbers on the buttons should equal target number displayed at the top of the screen.

### How to play

- Click on any button to select it.
- Look at the operators displayed on the right. The one at the top is going to be used next.
- Click on another button to perform the operation with the selected button.
- The result will be stored in the selected (first) button.
- The second button will become selected.
- Try to reach the target number by performing operations on the buttons.

> [!NOTE]
> The result is always a number between 0 and 9. If the result is -1, it will be set to 1. Similarly, if the result is 10, it will be set to 0.
>
> In other words, the number on the button is set to **the rightmost digit of the result**.

## Build and run from source

```sh
git clone https://github.com/urluur/weird-math-game.git
cd weird-math-game/src
javac VDN2.java
java VDN2
```
