# Othello

Spelet Othello implementerat med minimax-algoritmen för att säkerställa bästa drag hos datorspelaren. För att rita grafik har JavaFX använts.

Bra tutorials:

- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-1-introduction/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-2-evaluation-function/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/

## Setup VSCode - Java/JavaFX

Installera JAVA och sätt JAVA_HOME

Installera JavaFX från openjfx.io. Sätt PATH_TO_FX till katalogen där du installerade fx och lägg till 'lib' i sökvägen

Ex: PATH_TO_FX C:\tools\javafx-sdk-18.0.1\lib
Modifiera launch.json "vmArgs": "--module-path \"${env:PATH_TO_FX}\" --add-modules javafx.controls,javafx.fxml"

Möjligt behöver även settings.json modifieras så att den innehåller sökvägen till alla fx-moduler

Starta spelet via RUN i vscode.
