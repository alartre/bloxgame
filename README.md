# bloxgame-backendtest


“Primo!”

As a user, I want an engaging and interesting experience to interact with when determining what number I land on when playing the “Primo!” game.

The game is simple, a user presses a spin button and an animation similar to a slot machine/carousel/spinner plays and eventually lands on a number. If the number is Prime, the user wins, if it's not prime they lose.

BACK END ONLY

The application must:
Implement logic to generate a random number between 1 and 20 for each game spin which is cryptographically secure (server seed, client seed, and nonce) for which the results can be decoded using this information after they are generated. Results should be reproducible using (server seed, client seed and nonce). 
Include functionality to determine if the generated number is prime and handle game outcomes based on this.
Manage the overall game flow, ensuring correct win/loss determination and responding to the frontend.
Track each user's spin results, including wins and losses, and provide this data when requested.
Ensure the system can handle multiple simultaneous users without issues.
Create endpoints that allow the frontend to start spins, retrieve game results, and access spin history.
Include light documentation regarding the API endpoint for your fellow FE Engineers.
Technical limitations:
Must be done in java 11
Any portable database or in memory storage is allowed. 
Do not use Spring framework. You can use the JEE or Spark framework.
Must be compilable/demoable in a web-based environment like codepen or github codespaces.
