# Hang-In

An Android app which helps the users to reach for the available workspaces and check their details.

## Functionalities & Features:
* Login & Sign up.
*	Sign up by Facebook or Google.
*	View the user profile.
*	View a list of the workspaces.
*	Ability to search for a specific workspace.
*	View the details of a specific workspace.
*	Navigate to the location of a workspace.

## Implementation:
*	The Projects is based on fetching the required data through the [Workspaces API documentation](https://documenter.getpostman.com/view/4822615/SVmu119c?fbclid=IwAR3hZzSovShwbk9NIr9ILKKKH_MJVxeMLfGpxgmtj3_78XT1DSqy7frRMZ0&version=latest#9e6c0400-4a53-4c9f-a6c4-b336addb8408).
*	The data is parsed using Retrofit third-party library and Coroutines for improving the runtime processing.
*	The fetched data is viewed on a RecyclerView.
*	The images are parsed using Picasso and Circular ImageView third-party library.
*	The location is detected by the Maps services.
*	The user is able to sign up by Facebook and Google using Firebase.
