var database;
openDatabase();
function openDatabase() {
        var request = indexedDB.open('Course Program', 3);

        request.onerror = function(event) {
                console.log("Database Not Created");
        };
        request.onsuccess = function(event) {
                database = this.result;
                console.log("Database Created");
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

function getObjectStore() {
        var transaction = database.transaction('Courses', 'readwrite');
        return transaction.objectStore('Courses');
}

function clearObjectStore() {
        var store = getObjectStore();
        var request = store.clear();
        request.onsuccess = function(event) {
                console.log("Database Cleared");
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

        var store = getObjectStore('Courses', 'readwrite');
        var request;

        request = store.add(obj);

        request.onsuccess = function(event) {
                console.log("Insertion Successful");
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
        var roomNumber = $('roomNumber').val();

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

        document.getElementById('formInput').reset();
        window.location.replace("#courseList");

        if(database != undefined) {
                addRow('2007', 'COSC', 'Data Structures 1', '1:00pm', '2:30pm', 'Monday, Wednesday', null, '200');
        }
}
