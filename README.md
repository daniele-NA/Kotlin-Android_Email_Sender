Each class has access to the database through the Bridge object, the latter is responsible for giving "publish"
accessibility to the database and takes care of writing and reading operations in memory


VIEWS:

FRAGMENTS:
    the latter overwrite the elements of the upper bar with the actions they prefer
    each fragment uses the binding and extends the Utils interface, which allows a series of tools including
    notifications, toasts and animations

ACTIVITIES:
    the two activities are turned on and off by the Switcher which starts another activity
    and terminates the current one, that current one is established by overriding each Activity of the method
    onResume, which communicates with the Switcher at every start; here too the data exchange is completely public


CONTROLLER:
    AccessDone->tracks the first launch of the app, being the REadFromFile method in the onCreate() method
    the latter is also called when you go to Login and then go back to MAin, instead thanks to this Static Object
    we keep track and limit memory reading only on first boot


MODEL:
AbstractEmail shares parameters for both emails with and without files and is extended by SimpleEmail and StructuredEmail;
both override sendEmail() and the instance variables of the class, all integrated with Coroutines
which relaunch the value (through the database and the Bridge) up to the activities/fragments, the latter execute
these suspend function in the MainScope.launch{}, since it is not possible to catch exceptions from the coroutines outside
of the latter.



WHAT SHOULD YOU DO?

-modify your default email in the database.kt file for receiving a new user and subscriber, the latter performs 2 functions: 1st establishes the connection, 2nd notifies the creator of a new user

-modify the URL of the Insta, Discord and Facebook accounts in the login.kt

![20241211_175056](https://github.com/user-attachments/assets/1b8fb802-883e-4766-992c-5c7c75691bf0)

![20241211_175121](https://github.com/user-attachments/assets/dd4b074b-4b80-432e-9afe-bf627b313255)

