package com.kola.schoolmanagerapp

object EndPoints {
    var SERVER_IP = "192.168.1.102"
    var URL_ROOt = "http://$SERVER_IP/APISchoolManager2/v1/?op="

    var URL_ADD_STUDENT_PICTURE = "${URL_ROOt}addStudentPicture"
    var URL_GET_ALL_STUDENTS = "${URL_ROOt}getAllStudents"
    var URL_GET_ALL_STUDENTS_FOR_CLASSROOM = "${URL_ROOt}getAllStudentsForClassroom"
    var URL_GET_STUDENTS_FOR_MATRICULE = "${URL_ROOt}getStudentForMatricule"
    var URL_ALL_CLASS_ROOM = "${URL_ROOt}getAllClassRoom"
    var URL_ALL_TEACHERS ="${URL_ROOt}getAllTeachers"
    var URL_SAVE_student_NOTES ="${URL_ROOt}saveStudentNote"


    fun configureOtherRouteUsingIp(IpAddresse: String) {
        SERVER_IP = IpAddresse
        URL_ROOt = "http://$SERVER_IP/APISchoolManager2/v1/?op="
        URL_ADD_STUDENT_PICTURE = "${URL_ROOt}addStudentPicture"
        URL_GET_ALL_STUDENTS = "${URL_ROOt}getAllStudents"
        URL_GET_ALL_STUDENTS_FOR_CLASSROOM = "${URL_ROOt}getAllStudentsForClassroom"
        URL_GET_STUDENTS_FOR_MATRICULE = "${URL_ROOt}getStudentForMatricule"
        URL_ALL_CLASS_ROOM = "${URL_ROOt}getAllClassRoom"
        URL_ALL_TEACHERS ="${URL_ROOt}getAllTeachers"
        URL_SAVE_student_NOTES ="${URL_ROOt}saveStudentNote"
    }
}