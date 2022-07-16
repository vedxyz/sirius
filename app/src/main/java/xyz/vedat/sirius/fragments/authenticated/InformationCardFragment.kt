package xyz.vedat.sirius.fragments.authenticated

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch
import xyz.vedat.sirius.R
import xyz.vedat.sirius.viewmodels.InformationCardViewModel

class InformationCardFragment : Fragment(R.layout.fragment_information_card) {
    private val viewModel: InformationCardViewModel by activityViewModels()

    private lateinit var loadingScreen: View
    private lateinit var mainContent: SwipeRefreshLayout

    private lateinit var studentIdItem: View
    private lateinit var studentFullNameItem: View
    private lateinit var studentNationalIdItem: View
    private lateinit var studentFacultyItem: View
    private lateinit var studentDepartmentItem: View
    private lateinit var studentStatusItem: View
    private lateinit var advisorFullNameItem: View
    private lateinit var advisorEmailItem: View
    private lateinit var academicRegistrationSemesterItem: View
    private lateinit var academicCurriculumSemesterItem: View
    private lateinit var academicInClassItem: View
    private lateinit var academicCGPAItem: View
    private lateinit var academicGPAItem: View
    private lateinit var academicStandingItem: View
    private lateinit var academicNominalCreditLoadItem: View
    private lateinit var academicCourseLimitsItem: View
    private lateinit var academicCohortItem: View
    private lateinit var academicAGPAItem: View
    private lateinit var academicDetailsItem: View
    private lateinit var scholarshipByPlacementItem: View
    private lateinit var scholarshipMeritItem: View
    private lateinit var contactContactEmailItem: View
    private lateinit var contactBilkentEmailItem: View
    private lateinit var contactMobilePhoneItem: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetch()

        loadingScreen = view.findViewById(R.id.loading_screen)
        mainContent = view.findViewById(R.id.information_card_swipelayout)
        mainContent.setOnRefreshListener {
            viewModel.fetch()
        }

        // This approach is very questionable.
        studentIdItem = getAndSetKey(view, R.id.information_card_student_id, "Student ID")
        studentFullNameItem = getAndSetKey(view, R.id.information_card_student_fullname, "Full Name")
        studentNationalIdItem = getAndSetKey(view, R.id.information_card_student_nationalid, "National ID")
        studentFacultyItem = getAndSetKey(view, R.id.information_card_student_faculty, "Faculty")
        studentDepartmentItem = getAndSetKey(view, R.id.information_card_student_department, "Department")
        studentStatusItem = getAndSetKey(view, R.id.information_card_student_status, "Status")
        advisorFullNameItem = getAndSetKey(view, R.id.information_card_advisor_fullname, "Full Name")
        advisorEmailItem = getAndSetKey(view, R.id.information_card_advisor_email, "Email")
        academicRegistrationSemesterItem =
            getAndSetKey(view, R.id.information_card_academic_registrationsemester, "Registration Semester")
        academicCurriculumSemesterItem =
            getAndSetKey(view, R.id.information_card_academic_curriculumsemester, "Curriculum Semester")
        academicInClassItem = getAndSetKey(view, R.id.information_card_academic_inclass, "In Class")
        academicCGPAItem = getAndSetKey(view, R.id.information_card_academic_cgpa, "CGPA")
        academicGPAItem = getAndSetKey(view, R.id.information_card_academic_gpa, "GPA")
        academicStandingItem = getAndSetKey(view, R.id.information_card_academic_standing, "Standing")
        academicNominalCreditLoadItem =
            getAndSetKey(view, R.id.information_card_academic_nominalcreditload, "Nominal Credit Load")
        academicCourseLimitsItem = getAndSetKey(view, R.id.information_card_academic_courselimits, "Course Limits")
        academicCohortItem = getAndSetKey(view, R.id.information_card_academic_cohort, "Cohort")
        academicAGPAItem = getAndSetKey(view, R.id.information_card_academic_agpa, "AGPA")
        academicDetailsItem = getAndSetKey(view, R.id.information_card_academic_details, "Details")
        scholarshipByPlacementItem = getAndSetKey(view, R.id.information_card_scholarship_byplacement, "By Placement")
        scholarshipMeritItem = getAndSetKey(view, R.id.information_card_scholarship_merit, "Merit")
        contactContactEmailItem = getAndSetKey(view, R.id.information_card_contact_contactemail, "Contact Email")
        contactBilkentEmailItem = getAndSetKey(view, R.id.information_card_contact_bilkentemail, "Bilkent Email")
        contactMobilePhoneItem = getAndSetKey(view, R.id.information_card_contact_mobilephone, "Mobile Phone")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        if (it.informationCard == null) {
                            mainContent.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                        }
                    } else {
                        loadingScreen.visibility = View.GONE
                        mainContent.visibility = View.VISIBLE
                        mainContent.isRefreshing = false

                        val infoCard = it.informationCard!!
                        setValue(studentIdItem, infoCard.student.id)
                        setValue(studentFullNameItem, infoCard.student.fullName)
                        setValue(studentNationalIdItem, infoCard.student.nationalId)
                        setValue(studentFacultyItem, infoCard.student.faculty)
                        setValue(studentDepartmentItem, infoCard.student.department)
                        setValue(studentStatusItem, infoCard.student.status)
                        setValue(advisorFullNameItem, infoCard.advisor.fullName)
                        setValue(advisorEmailItem, infoCard.advisor.email)
                        setValue(academicRegistrationSemesterItem, infoCard.academic.registrationSemester)
                        setValue(academicCurriculumSemesterItem, infoCard.academic.curriculumSemester)
                        setValue(academicInClassItem, infoCard.academic.inClass)
                        setValue(academicCGPAItem, infoCard.academic.cgpa)
                        setValue(academicGPAItem, infoCard.academic.gpa)
                        setValue(academicStandingItem, infoCard.academic.standing)
                        setValue(academicNominalCreditLoadItem, infoCard.academic.nominalCreditLoad)
                        setValue(
                            academicCourseLimitsItem,
                            infoCard.academic.courseLimits.let { limit -> "${limit.lower} - ${limit.upper}" })
                        setValue(academicCohortItem, infoCard.academic.ranking.cohort)
                        setValue(academicAGPAItem, infoCard.academic.ranking.agpa)
                        setValue(academicDetailsItem, infoCard.academic.ranking.details)
                        setValue(scholarshipByPlacementItem, infoCard.scholarship.byPlacement)
                        setValue(scholarshipMeritItem, infoCard.scholarship.merit)
                        setValue(contactContactEmailItem, infoCard.contact.contactEmail)
                        setValue(contactBilkentEmailItem, infoCard.contact.bilkentEmail)
                        setValue(contactMobilePhoneItem, infoCard.contact.mobilePhone)
                    }
                }
            }
        }
    }

    private fun getAndSetKey(view: View, @IdRes id: Int, title: String): View {
        return view.findViewById<View>(id).also { it.findViewById<TextView>(R.id.key_value_item_key).text = title }
    }

    private fun setValue(view: View, value: String) {
        view.findViewById<TextView>(R.id.key_value_item_value).text = value
    }
}
