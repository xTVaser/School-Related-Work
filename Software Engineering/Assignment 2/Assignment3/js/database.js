var database;
var conflicts = [];

//Onload function
function init() {

        openDatabase();
}

//Establishes the database
function openDatabase() {
        //Creates a new database object, version 3 (may be why it doesnt work in some browsers and firefoxOS sim.)
        var request = indexedDB.open('Course Program', 3);

        request.onerror = function(event) {
                console.log("Database Not Created");
        };
        request.onsuccess = function(event) {
                database = this.result;
                console.log("Database Created");
                //If successful, we can populate the courses that may be already stored in the database
                populateCourses();
        }
        request.onupgradeneeded = function(event) {
                database = event.target.result;
                //If the database has never been created, initialize its columns, called indexes, primary key is the department and coursecode combined.
                var store = database.createObjectStore("Courses", { keyPath: ['department', 'course_code'] });

                store.createIndex('course_name', 'course_name', { unique:false });
                store.createIndex('start_time', 'start_time', { unique:false });
                store.createIndex('end_time', 'end_time', { unique:false });
                store.createIndex('weekdays', 'weekdays', { unique:false });
                store.createIndex('room', 'room', { unique:false });
                store.createIndex('room_number', 'room_number', { unique:false });
        };
}

//Simple function to return the object that contains references to the database objects
function getObjectStore(type) {
        if (database != undefined) { //These hacks are everywhere because of the asynchronous nature of the function calls
                var transaction = database.transaction('Courses', type);
                return transaction.objectStore('Courses');
        }
}

//Wipes the database of its contents
function clearObjectStore() {
        var store = getObjectStore('readwrite');
        var request = store.clear();
        request.onsuccess = function(event) {
                console.log("Database Cleared");
                //Reload the page to get rid of the tags, hack #2
                window.location.reload();
        };
        request.onerror = function(event) {
                console.log("Database Cant be Cleared");
        };
}

//Function to automate adding a row to the database, im surprised this actually worked.
function addRow(courseCode, department, courseName, startTime, endTime, weekDays, room, roomNumber) {

        console.log("Adding a row for course"+courseCode);
        //Create an object to hold all of the information to match the databases fields
        var obj = {
                department: department,
                course_code: courseCode,
                course_name: courseName,
                start_time: startTime,
                end_time: endTime,
                weekdays: weekDays,
                room: room,
                room_number: roomNumber
        }

        var store = getObjectStore('readwrite');
        var request;

        request = store.add(obj);

        request.onsuccess = function(event) {
                console.log("Insertion Successful");
                //New course added, so we need to update the tags
                populateCourses();
        };
        request.onerror = function(event) {
                console.log("Failed"+this.error);
        };
}

//When the user hits the submit button, this function is called to grab the fields.
function getData() {

        //Get all of the fields
        var courseName = $('#courseName').val();
        var department = $('#department').val();
        var courseCode = $('#courseCode').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var sunday = $('#sunday').is(':checked');
        var monday = $('#monday').is(':checked');
        var tuesday = $('#tuesday').is(':checked');
        var wednesday = $('#wednesday').is(':checked');
        var thursday = $('#thursday').is(':checked');
        var friday = $('#friday').is(':checked');
        var saturday = $('#saturday').is(':checked');
        var room = $('#room').val();
        var roomNumber = $('#roomNumber').val();

        //Hack #3, manually convert the checkboxes into strings to add to a single entry in the database.
        var weekDays = "";

        if(sunday) {
                weekDays += "Sunday ";
        }
        if(monday) {
                weekDays += "Monday ";
        }
        if(tuesday) {
                weekDays += "Tuesday ";
        }
        if(wednesday) {
                weekDays += "Wednesday ";
        }
        if(thursday) {
                weekDays += "Thursday ";
        }
        if(friday) {
                weekDays += "Friday ";
        }
        if(saturday) {
                weekDays += "Saturday";
        }

        //Begin validating the input within the javascript
        var errorMessage = "<br>";
        var valid = true;

        var regex = /[^a-zA-Z\d\s]/; //Must be letters and numbers only
        if(courseName == "" || regex.exec(courseName)) {
                valid = false;
                errorMessage += "Invalid Course Name<br>";
        }

        if(department == "Select Department") { //If they didnt select a department
                valid = false;
                errorMessage += "Pick a Department<br>";
        }

        regex = /^(\d{4})$/; //Must only be 4 digits
        if(courseCode == "" || !regex.exec(courseCode)) {
                valid = false;
                errorMessage += "Invalid Course Code<br>";
        }

        regex = /^(\d*\d\:\d\d)((pm)|(am))$/; //Must only be a time format, 1:00pm for example
        if(startTime == "" || !regex.exec(startTime)) {
                valid = false;
                errorMessage += "Invalid Start Time<br>";
        }
        if(endTime == "" || !regex.exec(endTime)) { //Same regex used for end time
                valid = false;
                errorMessage += "Invalid End Time<br>";
        }

        if(weekDays == "") { //No days were selected
                valid = false;
                errorMessage += "Please Select one or More days of the Week<br>";
        }

        if(room == "Select Room") { //No room was selected
                valid = false;
                errorMessage += "Please Select a Room<br>";
        }
        regex = /^(\d{3})$/; //Must be 3 numbers long
        if(roomNumber == "" || !regex.exec(roomNumber)) {
                valid = false;
                errorMessage += "Invalid Room Number<br>";
        }

        if(database != undefined && valid == true) {
                //Hack #4, biggest one by far, when the data is collected, reset the fields of the form
                        //But also hard refresh the course listing page, and add the item to the database
                document.getElementById('formInput').reset();
                window.location.replace("#courseList");
                addRow(courseCode, department, courseName, startTime, endTime, weekDays, room, roomNumber);
        }
        else { //Invalid input, print out the error message as an alert under the submit button.
                var insertLocation = $('#formInput');

                var alert = $('<div class="alert alert-danger fade in">'+
                                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                                '<strong>Error!</strong> '+errorMessage+
                                '</div>');

                insertLocation.append(alert);
        }
}

//Function to take in the courses and output them in the course listing page
//Headaches ensue
function populateCourses() {

        var store = getObjectStore('readonly');

        var list = $('#courseListings');
        list.empty();

        var request;
        request = store.openCursor(); //We use a cursor to pass through each element one by one
        request.onsuccess = function(event) {

                var cursor = event.target.result;

                if(cursor) { //If we have an item to read
                        request = store.get(cursor.key); //Then start another request
                        request.onsuccess = function(event) {

                                var allTheItems;
                                store.getAll().onsuccess = function(evt) { //To get all the items, then have a single function

                                        //!! everything must be done inside this function or the asynchronous behaviour will make things go wrong :D......
                                        var value = event.target.result;
                                        console.log(value);

                                        allTheItems = evt.target.result;

                                        var conflictColor = '';
                                        for (var i = 0; i < allTheItems.length; i++) { //Loop through every item in the database

                                                //If we are looking at the course compraed to itself, ignore it
                                                if(allTheItems[i].course_code == value.course_code && allTheItems[i].department == value.department) {
                                                        //Do nothing
                                                }
                                                //Else, if they share the same start time they are a conflict.
                                                //This is an assuption that courses are the same as in the school scheduling where all classes have set starttimes.
                                                //Obviously this should be a range validation, but have fun trying to implement that.
                                                else if(allTheItems[i].start_time == value.start_time) {

                                                        //If any of the courses also share the same day, they are a conflict, they background color will be red.
                                                        if(allTheItems[i].weekdays.indexOf('Sunday') >= 0 && value.weekdays.indexOf('Sunday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Monday') >= 0 && value.weekdays.indexOf('Monday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Tuesday') >= 0 && value.weekdays.indexOf('Tuesday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Wednesday') >= 0 && value.weekdays.indexOf('Wednesday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Thursday') >= 0 && value.weekdays.indexOf('Thursday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Friday') >= 0 && value.weekdays.indexOf('Friday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }
                                                        if(allTheItems[i].weekdays.indexOf('Saturday') >= 0 && value.weekdays.indexOf('Saturday') >= 0) {
                                                                conflictColor = '#ff9090';
                                                                console.log("Found a conflict");
                                                        }

                                                }
                                        }

                                        //Output the course object with its potential red background
                                        var listItem = $('<li class=\"ui-li-static ui-body-inherit ui-first-child\" style=\"background-color:'+conflictColor+'\">'+
                                                                '<h2>'+value.course_name+' - '+value.department+' '+value.course_code+'</h2>'+
                                                                '<b>'+value.start_time+' - '+value.end_time+' </b>'+value.weekdays+'<br>'+
                                                                '<i>'+value.room+' '+value.room_number+'</i>'+
                                                          '</li>');

                                        list.append(listItem);
                                };
                        };

                        cursor.continue();
                }
                else {
                        console.log("No more entries");
                }
        };
}

//Function to load the course page, i dont think its even used
function loadListPage() {
        window.location.replace("#courseList");
        window.location.reload();
}

//Neither is this lol
function appendToList(content, array) {

        $("#courseListings").append("<li class=\"ui-li-static ui-body-inherit ui-first-child\"><h2>"+content+"</h2></li>")
}
