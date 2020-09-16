# Gitlet Design Document

**Name**: Aditi Mahajan

## Classes and Data Structures

##Command: 
interface to model each Git command off of. Includes a method to run the command and a method to check the arguments and display appropriate errors.

##Classes
###Init:   
Creates a new Gitlet version-control system in the current directory.

###Add
Adds a copy of the file as it currently exists to the staging area.

###Commit:
Saves a snapshot of certain files in the current commit and staging area so they can be restored at a later time, 
creating a new commit.

###RM
Unstage the file if it is currently staged for addition.

###Log
Starting at the current head commit, display information about each commit backwards along the commit tree until the 
initial commit, following the first parent commit links, ignoring any second parents found in merge commits.

###Global Log
Like log, except displays information about all commits ever made

###Find
Prints out the ids of all commits that have the given commit message, one per line. 

###Status
Displays what branches currently exist, and marks the current branch with a *

###Checkout
Depending on number of args, moves head to spescified location and moves files at current head  ot working  directory.

**rest of the commands are each a class**

##Gitlet Object: Class
The object class for Gitlet objects
This is where the SHA-1 hashcode needs to be implemented. All object of in Gitlet are extensions of this class 
(need to be  explicit tho).

###Blobs
Class that extends the gitlet Object. Contains an array of the blob's contents

###Commit
Class extends the gitlet Object. Contains
1. Date and time
2. Parent
3. Message
4. HashMap of blobs

###Commit Tree
Creates the commit tree for a repository and handles saving commits and the head pointer.

###Command Processor
Class with all possible commands a repo can do. Checks that a user input in valid and 
runs the command if it is. If not, throws error.

**Fields:**
1. Map of string version of a command and the command: every command 
processor has the same one (hard coded)
2. Process method: check the argument and the status of the repo  to run that. 

###Stage
Class that represents the files currently staged. 

**Fields:**
1. HashMap of blobs
2. Map of staged files
3. Map of removed files

## Algorithms

##Commit
**toString()**
- format the commit metadata to generate a log

##Command Processor
**add(Argument, command)**
- puts the arguent and command pair into the Map of commands in the repo

**process(repository, arguments)**
- makes sure there is a command in arguments, else error
- using the String arguent, finds the corresponding command in the map of commands
- runs that command on that repository

###Stage
**remove(File Name)**:
- If this stage has that file, remove it from list fo blobs.

**unstage(file Name):**
- If the stage has that file in staged, remove it

**commitToBlobs()**:
- mimics commiting the added files:clears the stage and returns the blobs



## Persistence

###Repository: Class
Represents a Gitlet Repository: storage of the commit tree

**Fields**:

1. Path to the working directory
2. Path to the gitlet directory
3. A directory of all objects of the repository
3. A directory of all references of the repository

**Methods:**
1. init
2. checkout: one for each # of args
