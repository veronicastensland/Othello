# Othello

Spelet Othello implementerat med minimax-algoritmen för att säkerställa bästa drag hos datorspelaren. För att rita grafik har JavaFX använts.

Bra tutorials:

- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-1-introduction/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-2-evaluation-function/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/
- https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/

## Setup VSCode - Java/JavaFX

Installera JAVA samt verktyg i VSCode och sätt systemvariabeln JAVA_HOME så att den pekar på javainstallationen. Kika i konfigurationsfilerna:

- launch.json
- settings.json

för att se var och hur du bör inkludera de olika biblioteken.

Installera JavaFX från https://gluonhq.com/products/javafx/. Sätt PATH_TO_FX till katalogen där du installerade fx och lägg till 'lib' i sökvägen

```
# Powershell:
$PATH_TO_FX="C:\Programs\java\javafx-sdk-21.0.1\lib"

# Bash
export $PATH_TO_FX=/usr/local/lib/javafx/javafx-sdk-21.0.1
```

Observera att variablerna kan sättas på systemnivå och att man bör kontrollera vad $PATH_TO_FX verkligen är satt till.

Modifiera launch.json

```
"vmArgs": "--module-path \"${env:PATH_TO_FX}\" --add-modules javafx.controls,javafx.fxml"
```

Möjligt behöver även settings.json modifieras så att den innehåller sökvägen till alla fx-moduler

Starta spelet via RUN i vscode.
