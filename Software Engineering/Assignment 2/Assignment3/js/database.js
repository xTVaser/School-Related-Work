function addCourse() {
        var courseName;
        var courseTime;
        var courseCredits;

        //var input = $('#formInput').serializeArray();
        //var input = $('#formInput').serialize();
        //console.log(JSON.stringify(input));
        //console.log(input);

}

function getData() { //Backup plan, use a normal submit button instead of using a post.

        var courseName = $('#courseName').val();
        console.log(courseName);
        document.getElementById('formInput').reset();
        window.location.replace("#courseList");
}
