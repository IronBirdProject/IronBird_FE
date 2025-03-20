//package com.example.greetingcard.presentation.ui.home.posting
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.compose.ui.platform.ComposeView
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//
//class CommentBottomSheetDialog : BottomSheetDialogFragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val composeView = ComposeView(requireContext())
//        composeView.setContent {
//            CommentListContent(commentList = dummyCommentList)
//        }
//        return composeView
//    }
//
//    override fun onStart() {
//        super.onStart()
//        dialog?.let {
//            val bottomSheet =
//                it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            val behavior = BottomSheetBehavior.from(bottomSheet)
//            behavior.peekHeight = 600 // 중간 크기
//            behavior.isFitToContents = false
//            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        }
//    }
//}
