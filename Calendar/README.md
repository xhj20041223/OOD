PT1
To run the calendar program, navigate to the CalendarApp.java file in the src folder.
This is the entry point for the program.
Run the file and pass in either '--interactive', or '--headless <filepath>' configuration commands.

The design for the model was a combined effort (builder pattern, fields, methods).
Model method implementations were done by Thomas. 
Controller methods and design was done by Jeffrey.
Program entry point (CalendarApp) was a combined effort.
Event and Calendar Tests were done by Thomas.
Controller and App tests were done by Jeffrey.
View was done by Jeffrey.

The some_invalid_commands.txt file contains invalid commands at the top and their corresponding
valid versions on the bottom.

PT2
Changes made:
- new class for user which implements an interface that extends the calendar interface. 
    This is because the user class can do everything the calendar class can do and apply it 
    to the current calendar.
- new implementation for a calendar called UserCalendar which contains information on the timezone.
    This extends the SimpleCalendar class because it can do everything a SimpleCalendar can do 
    and more including methods that help copying function.
- Minimal to no code was changed from the previous assignment to adhere to open close principle.

To use the jar located in the res folder, add it to a convenient location on your computer, 
open a terminal window, navigate to the directory that jar is in, and use this command:
java -jar <jar_file_path> <mode>
The mode can be either --interactive or --headless <commands_file_path> 

Model design was a group effort
Most of the model implementation were done by thomas (Jeffrey worked on UserCalendar.java impls)
The user controller was done by Jeffrey.
The model test were done by Thomas
The controller tests were done by Jeffrey.
