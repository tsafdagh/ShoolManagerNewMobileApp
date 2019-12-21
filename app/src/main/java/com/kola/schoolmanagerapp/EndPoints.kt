package com.kola.schoolmanagerapp

object EndPoints {
    val SERVER_IP = "192.168.43.11"
    val URL_ROOt = "http://$SERVER_IP/APISchoolManager2/v1/?op="

    val URL_ADD_STUDENT_PICTURE = "${URL_ROOt}addStudentPicture"
    val URL_GET_ALL_STUDENTS = "${URL_ROOt}getAllStudents"
    val URL_GET_ALL_STUDENTS_FOR_CLASSROOM = "${URL_ROOt}getAllStudentsForClassroom"
    val URL_GET_STUDENTS_FOR_MATRICULE = "${URL_ROOt}getStudentForMatricule"
    val URL_ALL_CLASS_ROOM = "${URL_ROOt}getAllClassRoom"

}