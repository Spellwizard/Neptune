*	Author: Liam Johnston
*	Creation Date: 04/26/2021
*
*	User Notes: the program is started by running the java file
*	On running you should find multiple buttons along with an image in the background, this is the menu
*
*	Program Options is an attempt to allow for some simple alterations with the settings eg player input in cleaner and more user friendly experience
*		such a window would allow for clicking various buttons to quickly re - key each button as the user inputs and allow for updating to same base settings with a range
*		for example to allow the option to select 1 or 2 players as the program is currently only setup to handle those number of players
*	Program Quit ends the program, in the windows environment using the close button (the X on the top right) serves the same purpose
*	Direct Connect is a button that literally does nothing at time of writing but was an idea of impleneting direct connection amongst computers to allow players to play on different computers
*	
*	Play / Start Program
*	This will actually 'hide' the menu and create a new window to start the actual 'program'
*
*
*	Within the GameSettings.txt you can edit various settings with the following notes:
*	The program has inbuilt defaults so don't worry if you delete something
*
*	The player keyboard values can be changed but they are using the number values of the keyboards and would probably require a google search
*		at time of creation i found https://www.oreilly.com/library/view/javascript-dhtml/9780596514082/apb.html as a reasonable reference but not inherently trustworthy / the only reference
*
*	Finally the class / 'program'that reads the various file will ignore any line containing the asterick '*'
*	it will look for a certain pre built values to override and if it can't find them the program will default to prebuilt values
*	This means that even if something isn't commented out that the program will still functionally ignore it
*	The only other real syntax to follow is the 'variable name' '=' 'value' format which can have spaces anywhere it must have an '=' to distinguish the variable from the value
* 	eg 'pa us eKey = 83' works but ' pause.Key = eighty - three' really doesn't
