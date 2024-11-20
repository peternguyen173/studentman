package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val buttonAddNew = findViewById<Button>(R.id.button_add_new)
    val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_students)

    // Set up RecyclerView
    studentAdapter = StudentAdapter(students, ::onEditStudent, ::onDeleteStudent)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = studentAdapter

    // Add new student
    buttonAddNew.setOnClickListener {
      showStudentDialog(null) { newStudent ->
        students.add(newStudent)
        studentAdapter.notifyItemInserted(students.size - 1)
      }
    }
  }

  // Edit student
  private fun onEditStudent(position: Int) {
    val student = students[position]
    showStudentDialog(student) { updatedStudent ->
      students[position] = updatedStudent
      studentAdapter.notifyItemChanged(position)
    }
  }
  // Delete student with undo option
  private fun onDeleteStudent(position: Int) {
    val studentToRemove = students[position]

    // Hiển thị hộp thoại xác nhận
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${studentToRemove.studentName}?")
      .setPositiveButton("Yes") { _, _ ->
        // Xóa sinh viên nếu chọn Yes
        val removedStudent = students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        // Hiển thị Snackbar với tùy chọn Undo
        Snackbar.make(findViewById(R.id.main), "${removedStudent.studentName} has been deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(position, removedStudent)
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("No", null) // Không thực hiện gì nếu chọn No
      .show()
  }


  // Show dialog for adding or editing a student
  private fun showStudentDialog(student: StudentModel?, onSave: (StudentModel) -> Unit) {
    val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_student, null)
    val editStudentName = dialogView.findViewById<EditText>(R.id.edit_student_name)
    val editStudentId = dialogView.findViewById<EditText>(R.id.edit_student_id)

    if (student != null) {
      editStudentName.setText(student.studentName)
      editStudentId.setText(student.studentId)
    }

    val dialog = AlertDialog.Builder(this)
      .setView(dialogView)
      .setCancelable(false)
      .create()

    dialogView.findViewById<Button>(R.id.button_save).setOnClickListener {
      val name = editStudentName.text.toString().trim()
      val id = editStudentId.text.toString().trim()
      if (name.isNotBlank() && id.isNotBlank()) {
        onSave(StudentModel(name, id))
        dialog.dismiss()
      } else {
        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
      }
    }

    dialogView.findViewById<Button>(R.id.button_cancel).setOnClickListener {
      dialog.dismiss()
    }

    dialog.show()
  }
}
