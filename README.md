# SWEN222 Group Assignment

## Team Members and Responsibilities
* Prashant Bhikhu - Networking / 'All Rounder'
* Alex (Zhen) Wang - Graphical User Interface and Extras
* Donald Tang (Team Leader) - Game Logic and Objects, Game Sprite Making, JUnit Testing
* Sushant Balajee - Game Logic and Objects
* Priyanka Bhula  - Game Saving and Loading
* Everyone - Documentation (Class Diagram and CRC Cards)

## Note For Assignment Marker (Justifications)
Due to our Eclipse Git glitches, we discovered a problem where commits from Sushant and Alex weren't associated with their GitHub accounts but in fact an unknown account and someone else's git account respectively. Therefore on commit log inspection, any commits made by an unknown account "sushantbalajee" (account is not linked to a profile) are indeed sushant's commits and commits made by "raltu" are in fact Alex.

This is because when committing, it uses the email address in the commit to determine who committed. Sushant and Alex's Eclipse settings automatically changed their email address respectively; Sushant: "sushant@LOCAL_IP_ADDRESS" and Alex: "Alex@Alex.pc". In the case of Sushant, all the commits he did before we fixed are seen as seperate users (there are about 5 Sushant's in the Git!) and Oddly alex's eclipse assigned email address was actually valid so it shows the username linked to that email address 'raltu' but infact that was all of Alex's commits.


## How to Execute
1. Import the JAR file into Eclipse as a New Project
2. Add JUnit 4 and External Jars (json-simple-1.1.1.jar, jsonbeans-0.5.jar and kyro-2.23.1-SNAPSHOT-all-debug.jar) to Build Path (if it isn't already apart of the build path).
2. Run the main method found in the 'main' package within the 'main' class.
2. In the Game menu, Click "Create Game (As Server)"
3. Then go back the main frame, In the Game menu, "Join Game (as Client)"
4. Play!

## Git Rules (For Team Members)
1. When you open Eclipse, make sure the first thing you do before coding is 'pull' so you are sync'd with the remote server.
2. Make sure you are not editing the same file as someone else. Announce that you have pushed or currently editing in the group, so we can avoid touching that file, and it gives us an alert we will need to 'push' and 'pull' soon.
3. Do not touch other team member's packages without their permission.
4. Remember to commit regularly so we can see your activity on the project.
