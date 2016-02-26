var database;
var conflicts = [];

function init() {

        openDatabase();
}

function openDatabase() {
        var request = indexedDB.open('Course Program', 3);

        request.onerror = function(event) {
                console.log("Database Not Created");
        };
        request.onsuccess = function(event) {
                database = this.result;
                console.log("Database Created");
                populateCourses();
        }
        request.onupgradeneeded = function(event) {
                database = event.target.result;

                var store = database.createObjectStore("Courses", { keyPath: ['department', 'course_code'] });

                store.createIndex('course_name', 'course_name', { unique:false });
                store.createIndex('start_time', 'start_time', { unique:false });
                store.createIndex('end_time', 'end_time', { unique:false });
                store.createIndex('weekdays', 'weekdays', { unique:false });
                store.createIndex('room', 'room', { unique:false });
                store.createIndex('room_number', 'room_number', { unique:false });
        };


}

function getObjectStore(type) {
        if (database != undefined) {
                var transaction = database.transaction('Courses', type);
                return transaction.objectStore('Courses');
        }
}

function clearObjectStore() {
        var store = getObjectStore('readwrite');
        var request = store.clear();
        request.onsuccess = function(event) {
                console.log("Database Cleared");
                window.location.reload();
        };
        request.onerror = function(event) {
                console.log("Database Cant be Cleared");
        };
}

function addRow(courseCode, department, courseName, startTime, endTime, weekDays, room, roomNumber) {

        console.log("Adding a row for course"+courseCode);
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
                populateCourses();
        };
        request.onerror = function(event) {
                console.log("Failed"+this.error);
        };
}

function getData() {

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

        console.log(courseName);
        console.log(department);
        console.log(courseCode);
        console.log(startTime);
        console.log(endTime);
        console.log(sunday);
        console.log(monday);
        console.log(tuesday);
        console.log(wednesday);
        console.log(thursday);
        console.log(friday);
        console.log(saturday);
        console.log(room);
        console.log(roomNumber);
        console.log(weekDays);

        document.getElementById('formInput').reset();
        window.location.replace("#courseList");

        if(database != undefined) {
                addRow(courseCode, department, courseName, startTime, endTime, weekDays, room, roomNumber);
        }
}

function populateCourses() {

        console.log("Printing Courses");

        var store = getObjectStore('readonly');

        var list = $('#courseListings');
        list.empty();

        var request;
        request = store.openCursor();
        request.onsuccess = function(event) {

                var cursor = event.target.result;

                if(cursor) {
                        console.log("Printing:",cursor);
                        request = store.get(cursor.key);
                        request.onsuccess = function(event) {

                                //<h2>Data Structures II - COSC 2007</h2>
                                //<b>2:30pm - 4:00pm </b>Monday Wednesday<br>
                                //<i>NW 200</i>
                                var value = event.target.result;
                                var listItem = $('<li class=\"ui-li-static ui-body-inherit ui-first-child\">'+
                                                        '<h2>'+value.course_name+' - '+value.department+' '+value.course_code+'</h2>'+
                                                        '<b>'+value.start_time+' - '+value.end_time+' </b>'+value.weekdays+'<br>'+
                                                        '<i>'+value.room+' '+value.room_number+'</i>'+
                                                  '</li>');

                                list.append(listItem);
                        };

                        cursor.continue();
                }
                else {
                        console.log("No more entries");
                }
        };
}

function loadListPage() {
        window.location.replace("#courseList");
        window.location.reload();
}

function populateConflicts() {

        console.log("Conflicts:");
        console.log(conflicts);
}
function appendToList(content, array) {

        $("#courseListings").append("<li class=\"ui-li-static ui-body-inherit ui-first-child\"><h2>"+content+"</h2></li>")
}
