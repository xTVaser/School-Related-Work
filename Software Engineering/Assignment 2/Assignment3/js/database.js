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

        var errorMessage = "<br>";
        var valid = true;

        var regex = /[^a-zA-Z\d\s]/;
        if(courseName == "" || regex.exec(courseName)) {
                valid = false;
                errorMessage += "Invalid Course Name<br>";
        }

        if(department == "Select Department") {
                valid = false;
                errorMessage += "Pick a Department<br>";
        }

        regex = /^(\d{4})$/;
        if(courseCode == "" || !regex.exec(courseCode)) {
                valid = false;
                errorMessage += "Invalid Course Code<br>";
        }

        regex = /^(\d*\d\:\d\d)((pm)|(am))$/;
        if(startTime == "" || !regex.exec(startTime)) {
                valid = false;
                errorMessage += "Invalid Start Time<br>";
        }
        if(endTime == "" || !regex.exec(endTime)) {
                valid = false;
                errorMessage += "Invalid End Time<br>";
        }

        if(weekDays == "") {
                valid = false;
                errorMessage += "Please Select one or More days of the Week<br>";
        }

        if(room == "Select Room") {
                valid = false;
                errorMessage += "Please Select a Room<br>";
        }
        regex = /^(\d{3})$/;
        if(roomNumber == "" || !regex.exec(roomNumber)) {
                valid = false;
                errorMessage += "Invalid Room Number<br>";
        }

        if(database != undefined && valid == true) {
                document.getElementById('formInput').reset();
                window.location.replace("#courseList");
                addRow(courseCode, department, courseName, startTime, endTime, weekDays, room, roomNumber);
        }
        else {
                var insertLocation = $('#formInput');

                var alert = $('<div class="alert alert-danger fade in">'+
                                '<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+
                                '<strong>Error!</strong> '+errorMessage+
                                '</div>');

                insertLocation.append(alert);
        }
}

function populateCourses() {

        var store = getObjectStore('readonly');

        var list = $('#courseListings');
        list.empty();

        var request;
        request = store.openCursor();
        request.onsuccess = function(event) {

                var cursor = event.target.result;

                if(cursor) {
                        request = store.get(cursor.key);
                        request.onsuccess = function(event) {

                                var allTheItems;
                                store.getAll().onsuccess = function(evt) {

                                        var value = event.target.result;
                                        console.log(value);

                                        allTheItems = evt.target.result;

                                        var conflictColor = '';
                                        for (var i = 0; i < allTheItems.length; i++) {

                                                if(allTheItems[i].course_code == value.course_code && allTheItems[i].department == value.department) {
                                                        //Do nothing
                                                }
                                                else if(allTheItems[i].start_time == value.start_time) {

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


                                        //<h2>Data Structures II - COSC 2007</h2>
                                        //<b>2:30pm - 4:00pm </b>Monday Wednesday<br>
                                        //<i>NW 200</i>

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

function loadListPage() {
        window.location.replace("#courseList");
        window.location.reload();
}

function appendToList(content, array) {

        $("#courseListings").append("<li class=\"ui-li-static ui-body-inherit ui-first-child\"><h2>"+content+"</h2></li>")
}
